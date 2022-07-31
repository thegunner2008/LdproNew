package tamhoang.ldpro4;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import io.reactivex.Observable;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.Models.Action;
import tamhoang.ldpro4.data.BriteDb;
import tamhoang.ldpro4.data.Contact;
import tamhoang.ldpro4.data.Database;
import tamhoang.ldpro4.data.model.KhachHang;
import tamhoang.ldpro4.data.model.TinNhanS;
import tamhoang.ldpro4.util.NotificationUtils;

public class NotificationReader extends NotificationListenerService {
    public static final String VIBER = "com.viber.voip";
    public static final String WHATSAPP = "com.whatsapp";
    public static final String ZALO = "com.zing.zalo";

    String ID = "";
    String Ten_KH;
    String body = "";
    JSONObject caidat_tg;
    Context context;
    Database db;
    JSONObject json;
    String mWhat = "";
    int soTN;

    static Bundle currentExtras;

    public void onCreate() {
        Log.e(TAG, "onNotificationPosted: onCreate ");

        super.onCreate();
        this.db = new Database(getBaseContext());
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.e(TAG, "onNotificationPosted: " + sbn);

        if (!sbn.getPackageName().equals(ZALO) && !sbn.getPackageName().equals(WHATSAPP) && !sbn.getPackageName().equals(VIBER)) return;
        if (this.context == null) this.context = this;

        Bundle extras = sbn.getNotification().extras;
        currentExtras = extras;
        String text = extras.getCharSequence(NotificationCompat.EXTRA_TEXT).toString();//nội dung tin nhắn hoặc số(tin nhắn)
        CharSequence[] textLines = extras.getCharSequenceArray(NotificationCompat.EXTRA_TEXT_LINES);
//        Log.e(TAG, "onNotificationPosted: extras " + extras);

        Log.e(TAG, "aaaa onNotificationPosted: text " + text);

        Log.e(TAG, "aaaa onNotificationPosted: textLines " + Arrays.toString(textLines));

        if(textLines != null && textLines.length > 1) {
            for (int i = 0; i < textLines.length; i++) {
                processText(sbn, textLines[i].toString(), i + 1, textLines.length);
            }
        } else {
            try {
                NotificationCompat.MessagingStyle msgStyle = NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(sbn.getNotification());
                List<NotificationCompat.MessagingStyle.Message> messages = msgStyle.getMessages();

                if(messages.size() > 1) {

                    for (int i = 0; i < messages.size(); i++) {
                        Log.e(TAG, "onNotificationPosted: messages " + messages.get(i).getText());

                        processText(sbn, messages.get(i).getText().toString(), i + 1 , messages.size());
                    }
                } else if(!text.isEmpty()) processText(sbn, text, 1, 1);
            } catch (Exception e) {
                if(!text.isEmpty()) processText(sbn, text, 1, 1);
            }
        }
    }

    public void processText(StatusBarNotification sbn, String text, int process, int number) {
        Log.e(TAG, "processText: " + text);

        String app = "";
        String GhepTen; // app - tên KH

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        hourFormat.setTimeZone(TimeZone.getDefault());
        String mNgayNhan = dmyFormat.format(calendar.getTime());
        String mGionhan = hourFormat.format(calendar.getTime());
        try {
            this.ID = "";
            Bundle extras = sbn.getNotification().extras;
            Notification notification = sbn.getNotification();
            String title = extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString();// 1/ = Tên KH (số tin nhắn)

            if (sbn.getPackageName().equals(ZALO)) {
                if ((text.contains("tin nhắn chưa đọc.")) || (text.contains("đã gửi tập tin cho bạn")) || (text.contains("cảm xúc với tin nhắn bạn gửi"))
                        || (text.contains("hình động cho bạn")) || (text.contains("thành viên của nhóm")) || (text.contains("thêm vào nhóm")) || (text.contains("gửi ảnh cho bạn"))) {

                } else if (!text.contains("cuộc trò chuyện")) {
                    this.ID = title;
                    if (title.contains(" (")) {
                        this.ID = this.ID.substring(0, this.ID.indexOf("(")).trim(); // 2/ = Tên người nhắn
                    }

                    if (title.indexOf("Nhóm") == 0) {
                        this.ID = this.ID.substring(this.ID.indexOf(":") + 2).trim();
                        text = text.substring(text.indexOf(":") + 1).trim();
                    }
                    text = text.replaceAll("'", "");
                    app = "ZL";
                }
            }


            if (sbn.getPackageName().equals(VIBER) && !text.contains("Bạn có các tin nhắn") && !text.contains("thêm bạn vào")
                    && !text.contains("uộc gọi") && !text.contains("tin nhắn chưa đọc")) {
                this.ID = title;
                if (this.ID.contains("trong")) {
                    this.ID = this.ID.substring(this.ID.indexOf("trong") + 6);
                }
                if (!this.ID.contains("tin nhắn chưa đọc"))
                    app = "VB";
            }


            if (sbn.getPackageName().equals(WHATSAPP) && !text.contains("tin nhắn")) {
                this.ID = text;
                if (text.contains(":")) {
                    this.ID = this.ID.substring(0, this.ID.indexOf(":")).trim();
                    this.mWhat = title.substring(title.indexOf(":") + 1).trim();
                }
                if (this.ID.contains("@")) {
                    this.mWhat = this.ID.substring(0, this.ID.indexOf("@"));
                    this.ID = this.ID.substring(this.ID.indexOf("@") + 1).trim();
                }
                if (this.ID.contains(" (")) {
                    this.ID = this.ID.substring(0, this.ID.indexOf("(")).trim();
                }
                this.ID = this.ID.trim();
                for (int i = 0; i < "aaaaaaaaaaaaaaaaaeeeeeeeeeeeooooooooooooooooouuuuuuuuuuuiiiiiyyyyydx".length(); i++) {
                    this.ID = this.ID.replace("ăâàằầáắấảẳẩãẵẫạặậễẽểẻéêèềếẹệôòồơờóốớỏổởõỗỡọộợưúùứừủửũữụựìíỉĩịỳýỷỹỵđ×".charAt(i), "aaaaaaaaaaaaaaaaaeeeeeeeeeeeooooooooooooooooouuuuuuuuuuuiiiiiyyyyydx".charAt(i));
                }
                for (int i2 = 0; i2 < this.ID.length(); i2++) {
                    if (this.ID.charAt(i2) > 127 || this.ID.charAt(i2) < 31) {
                        this.ID = this.ID.substring(0, i2) + this.ID.substring(i2 + 1);
                    }
                }
                if (text.contains(this.mWhat.trim()) || this.mWhat.length() <= 0) {
                    this.mWhat = text;
                    app = "WA";
                }
            }

            GhepTen = app + " - " + this.ID.trim();
            if(app != "") {
                JSONObject jsonTin;
                if (MainActivity.Json_Tinnhan.has(GhepTen)) {
                    jsonTin = new JSONObject(MainActivity.Json_Tinnhan.getString(GhepTen));

                    if (jsonTin.has(text.trim())) { // nếu KH đã có trong danh sách && trùng tin
                        app = null;
                    } else {
                        jsonTin.put(text.trim(), "OK"); //{tin nhắn : OK}
                        MainActivity.Json_Tinnhan.put(GhepTen, jsonTin.toString());
                    }
                } else {
                    jsonTin = new JSONObject();
                    jsonTin.put(text.trim(), "OK"); //{tin nhắn : OK}
                    MainActivity.Json_Tinnhan.put(GhepTen, jsonTin.toString());
                }
            }

            Log.e(TAG, "onNotificationPosted: MainActivity.contactsMap.containsKey(GhepTen) =" + MainActivity.contactsMap.containsKey(GhepTen) + " GhepTen null = "+  GhepTen.contains("null"));

            if ( !GhepTen.contains("null")) {
                Notification.Action[] actions = notification.actions;

                for (Notification.Action act: actions) {
                    if (act.title.toString().contains("Trả lời") || act.title.toString().contains("Reply")) {
                        if(MainActivity.contactsMap.containsKey(GhepTen)) {
                            if(MainActivity.contactsMap.get(GhepTen) != null) {
                                MainActivity.contactsMap.get(GhepTen).process = process;
                                MainActivity.contactsMap.get(GhepTen).number = number;
                                MainActivity.contactsMap.get(GhepTen).name = GhepTen;
                                MainActivity.contactsMap.get(GhepTen).app = app;
                                MainActivity.contactsMap.get(GhepTen).pendingIntent = act.actionIntent;
                                MainActivity.contactsMap.get(GhepTen).remoteInput = act.getRemoteInputs()[0];
                                MainActivity.contactsMap.get(GhepTen).remoteExtras = sbn.getNotification().extras;
                            }
                        } else {
                            Contact cont = new Contact();
                            cont.process = process;
                            cont.number = number;
                            cont.name = GhepTen;
                            cont.app = app;
                            cont.pendingIntent = act.actionIntent;
                            cont.remoteInput = act.getRemoteInputs()[0];
                            cont.remoteExtras = sbn.getNotification().extras;

                            MainActivity.contactsMap.put(GhepTen, cont);
                        }
                        if (MainActivity.Notifi == null) MainActivity.Notifi = this;

                    }
                }
            }

            if (text.toLowerCase().contains("ldpro") && text.trim().length() == 5) {
                MainActivity.Notifi = this;
                NotificationWearReader(GhepTen, "Tin mồi!");
            }

            if (app != null) {
                try {
                    Log.e(TAG, "Gui_Tin_Nhan onNotificationPosted: action GhepTen " + GhepTen );

                    if (!GhepTen.contains("null") && app != "") {
                        String query = "Select * From Chat_database WHERE " +
                                "ngay_nhan = '" + mNgayNhan +"' And Ten_kh = '" + GhepTen + "' AND nd_goc = '"+ text + "'";
                        Cursor cursor = db.GetData(query);
                        Log.e(TAG, "Gui_Tin_Nhan onNotificationPosted: action cursor.getCount() " + cursor.getCount() );

                        if (cursor.getCount() == 0) {//neu khong bi trung tin nhan

                            String queryInsert = "Insert into Chat_database Values" +
                                    "( null,'" + mNgayNhan + "', '" + mGionhan +"', 1, '"+GhepTen +"', '"+GhepTen+"', '"+app+"','"+text+"',1)";
                            try {
                                this.db.QueryData(queryInsert);
                                MainActivity.sms = true;
                            } catch (Exception e) {
                                return;
                            }
                        } else {
                            app = null;
                        }

                        cursor.close();
                        if (app != null) {
                            Log.e(TAG, "onNotificationPosted: APP: " + app + "- GhepTen: " + GhepTen + "- text: "+text);
                            if (!GhepTen.contains("null") && app != "" && text.length() > 5) {
                                this.body = text.replaceAll("'", " ");
                                this.Ten_KH = GhepTen;
                                if (!(!MainActivity.DSkhachhang.contains(GhepTen) || this.body.startsWith("Ok") || this.body.startsWith("Bỏ")
                                        || this.body.toLowerCase().startsWith("ldpro") || this.body.startsWith("Thiếu")
                                        || this.body.startsWith("Success")) || this.body.contains("Tra lai")) {

                                    KhachHang khachHang_s = BriteDb.INSTANCE.selectKhachHang(Ten_KH);
                                    json = new JSONObject(khachHang_s.getTbl_MB());//"caidat_tg":{"dlgiu_de":0,"dlgiu_lo":0,"dlgiu_xi":0,"dlgiu_xn":0,"dlgiu_bc":0,"khgiu_de":0,"khgiu_lo":0,"khgiu_xi":0,"khgiu_xn":0,"khgiu_bc":0,"ok_tin":3,"xien_nhan":0,"chot_sodu":0,"tg_loxien":"18:13","tg_debc":"18:20","loi_donvi":0,"heso_de":0,"maxDe":0,"maxLo":0,"maxXi":0,"maxCang":0}}
                                    caidat_tg = json.getJSONObject("caidat_tg");

                                    Log.e(TAG, "onNotificationPosted: CheckTime: " + !Congthuc.CheckTime(caidat_tg.getString("tg_debc")));

                                    if (!Congthuc.CheckTime(caidat_tg.getString("tg_debc"))) {
                                        int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mNgayNhan, 1, "ten_kh = '"+ Ten_KH +"'");
                                        soTN = maxSoTn + 1;
                                        Log.e(TAG, "onNotificationPosted: tinNhanS_i " + maxSoTn );

                                        boolean isTraLai = body.contains("Tra lai");
                                        int okTn = isTraLai? 0: 1;
                                        int del_sms = okTn;
                                        TinNhanS tinNhanS_i = new TinNhanS(null, mNgayNhan, mGionhan, 1, Ten_KH, khachHang_s.getSdt(), app,
                                                soTN, body, null, body, "ko", 0, okTn, del_sms, null);
                                        Log.e(TAG, "onNotificationPosted: tinNhanS_i " + tinNhanS_i );
                                        BriteDb.INSTANCE.insertTinNhanS(tinNhanS_i);

                                        if (Congthuc.CheckDate(MainActivity.hanSuDung)) {
                                            TinNhanS tinNhanS_g = BriteDb.INSTANCE.selectTinNhanS(mNgayNhan, Ten_KH, soTN, 1);
                                            try {
                                                db.Update_TinNhanGoc(tinNhanS_g.getID(), 1);
                                            } catch (Exception e) {
                                                Log.e(TAG, "onNotificationPosted: Exception " +e );

                                                db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + tinNhanS_g.getID());
                                                db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mNgayNhan + "' AND ten_kh = '" + this.Ten_KH + "' AND so_tin_nhan = " + this.soTN + " AND type_kh = 1");
                                            } catch (Throwable throwable) {
                                                Log.e(TAG, "onNotificationPosted: throwable " +throwable );
                                            }
                                            if (!Congthuc.CheckTime("18:30") && !isTraLai) {
                                                Log.e(TAG, "onNotificationPosted: !CheckTime(18:30) " +  MainActivity.json_Tinnhan.has(Ten_KH) + " - "+ Ten_KH);
                                                if (MainActivity.handler == null) {
                                                    MainActivity.handler = new Handler();
                                                    MainActivity.handler.postDelayed(MainActivity.runnable, 1000);
                                                }
                                                if (!MainActivity.json_Tinnhan.has(Ten_KH)) {
                                                    JSONObject jsontinnan = new JSONObject();
                                                    jsontinnan.put("Time", 0);
                                                    MainActivity.json_Tinnhan.put(Ten_KH, jsontinnan.toString());
                                                } else {
                                                    JSONObject jsontinnan = new JSONObject(MainActivity.json_Tinnhan.getString(Ten_KH));
                                                    jsontinnan.put("Time", 0);
                                                    MainActivity.json_Tinnhan.put(Ten_KH, jsontinnan.toString());
                                                }

                                                db.Gui_Tin_Nhan(tinNhanS_g.getID());
                                            }

                                        }

                                    } else {
                                        int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mNgayNhan, null, "ten_kh = '"+ Ten_KH +"'");
                                        soTN = maxSoTn + 1;

                                        TinNhanS tinNhanS_i = new TinNhanS(null, mNgayNhan, mGionhan, 1, Ten_KH, khachHang_s.getSdt(), app,
                                                soTN, body, null, body, "Hết giờ nhận số!", 0, 1, 1, null);
                                        BriteDb.INSTANCE.insertTinNhanS(tinNhanS_i);

                                        if (!Congthuc.CheckTime("18:30") && MainActivity.jSon_Setting.getInt("tin_qua_gio") == 1) {
                                            NotificationWearReader(this.Ten_KH, "Hết giờ nhận!");
                                        }
                                    }
                                    return;
                                }
                                return;
                            }
                        }
                    }
                } catch (Exception e7) {
                    return;
                }

                Log.e(TAG, "NotificationWearReader processText: " + text);

                NotificationWearReader(Ten_KH, null);
            }

        } catch (Exception e) {
            Log.e(TAG, "onNotificationPosted: error " + e.getMessage());
        }
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.e(TAG, "onNotificationRemoved " + sbn);
        super.onNotificationRemoved(sbn);
    }

    public void NotificationWearReader(String mName, String message) {
        Contact contact = MainActivity.contactsMap.get(mName);
        Log.e(TAG, "NotificationWearReader: name: " + mName +" - Message: " + message );

        if (contact != null) {
            Log.e(TAG, "NotificationWearReader: contact: " + contact );

            try {
                if(message != null && !contact.waitingList.contains(message)) contact.waitingList.add(message);
                if(contact.process < contact.number) return;

                StringBuilder textSend = new StringBuilder();
                if(contact.waitingList != null && contact.waitingList.size() > 0) {
                    for (int i = 0; i < contact.waitingList.size(); i++) {
                        textSend.append(contact.waitingList.get(i));
                        if (i < contact.waitingList.size() - 1) textSend.append("\n \n");
                    }
                } else {
                    return;
                }

                Intent localIntent = new Intent();
                Bundle localBundle = contact.remoteExtras;

                RemoteInput[] remoteInputs = {contact.remoteInput};

                localBundle.putCharSequence(remoteInputs[0].getResultKey(), textSend.toString());
                RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle);

                Bundle extras = contact.remoteExtras;

                int delayTime;
                try {
                    delayTime = MainActivity.jSon_Setting.getInt("thoigiancho");
                } catch (Exception e) {
                    delayTime = 0;
                }

                Log.e(TAG, "aaaa onNotificationPosted: delayTime " + delayTime);

                Observable.just(localIntent)
                        .delay(delayTime, TimeUnit.MILLISECONDS)
                        .subscribe( intent -> {

                            Log.e(TAG, "aaaa onNotificationPosted: condition = " + currentExtras.equals(extras));

                            if(!currentExtras.equals(extras)) return;

                            contact.pendingIntent.send(MainActivity.Notifi, 0, intent);
                            Log.e(TAG, "aaaa onNotificationPosted: send done " + intent.toString());

                            if (MainActivity.contactsMap.containsKey(mName)) MainActivity.contactsMap.remove(mName);
                            if (MainActivity.Json_Tinnhan.has(mName)) MainActivity.Json_Tinnhan.remove(mName);
                        });
//                Log.e(TAG, "aaaa onNotificationPosted: send " + Thread.currentThread().getName());
//                contact.pendingIntent.send(MainActivity.Notifi, 0, localIntent);
//
//                if (MainActivity.Json_Tinnhan.has(mName)) MainActivity.Json_Tinnhan.remove(mName);

            } catch (Exception e) {
                Toast.makeText(context, "Notification Error " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "aaaa onNotificationPosted: error " + e);

                e.printStackTrace();
            }
        }
    }
}