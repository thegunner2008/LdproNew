package tamhoang.ldpro4;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import org.json.JSONObject;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.data.Contact;
import tamhoang.ldpro4.data.Database;

public class NotificationReader extends NotificationListenerService {
    public static final String VIBER = "com.viber.voip";
    public static final String WHATSAPP = "com.whatsapp";
    public static final String ZALO = "com.zing.zalo";
    static boolean replied;
    String ID = "";
    String Ten_KH;
    private ArrayList<NotificationCompat.Action> actions;
    String body = "";
    JSONObject caidat_tg;
    Context context;
    Database db;
    JSONObject json;
    String mWhat = "";
    int soTN;

    public void onCreate() {
        super.onCreate();
        this.actions = new ArrayList<>();
        this.db = new Database(getBaseContext());
    }

    public void onNotificationPosted(StatusBarNotification sbn) {

        String app = "";
        String GhepTen; // app - tên KH
        Iterator<Notification.Action> it;
        String charSequence;
        if (sbn.getPackageName().equals(ZALO) || sbn.getPackageName().equals(WHATSAPP) || sbn.getPackageName().equals(VIBER)) {
            if (this.context == null) {
                this.context = this;
            }
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
                String text = extras.getCharSequence(NotificationCompat.EXTRA_TEXT).toString();//nội dung tin nhắn hoặc số(tin nhắn)
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
                    charSequence = extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString();
                    this.ID = charSequence;
                    if (charSequence.contains(":")) {
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

                if (!MainActivity.arr_TenKH.contains(GhepTen) && !GhepTen.contains("null")) {
                    Notification.WearableExtender wearableExtender = new Notification.WearableExtender(notification);
                    ArrayList<Notification.Action> actions = new ArrayList<>(wearableExtender.getActions());
                    it = actions.iterator();
                    while (it.hasNext()) {
                        Notification.Action act = it.next();
                        if (act.title.toString().contains("Trả lời") || act.title.toString().contains("Reply")) {
                            MainActivity.arr_TenKH.add(GhepTen);
                            Contact cont = new Contact();
                            cont.name = GhepTen;
                            cont.app = app;
                            cont.pendingIntent = act.actionIntent;
                            cont.remoteInput = act.getRemoteInputs()[0];
                            cont.remoteExtras = sbn.getNotification().extras;
                            MainActivity.contactslist.add(cont);
                            if (MainActivity.Notifi == null) {
                                MainActivity.Notifi = this;
                            }
                        }
                    }
                }

                if (text.toLowerCase().contains("ldpro") && text.trim().length() == 5) {
                    MainActivity.Notifi = this;
                    NotificationWearReader(GhepTen, "Tin mồi!");
                }

                if (app != null) {
                    try {
                        if (!GhepTen.contains("null") && app != "") {
                            Database database = this.db;
                            String query = "Select * From Chat_database WHERE " +
                                    "ngay_nhan = '" + mNgayNhan +"' And Ten_kh = '" + GhepTen + "' AND nd_goc = '"+ text + "'";
                            try {
                                Cursor cursor = database.GetData(query);
                                if (cursor.getCount() == 0) {
                                    String queryInsert = "Insert into Chat_database Values" +
                                            "( null,'" + mNgayNhan + "', '" + mGionhan +"', 1, '"+GhepTen +"', '"+GhepTen+"', '"+app+"','"+text+"',1)";
                                    try {
                                        this.db.QueryData(queryInsert);
                                        MainActivity.sms = true;
                                    } catch (Exception e2) {
                                        return;
                                    }
                                } else {
                                    app = null;
                                }
                                cursor.close();
                                if (app != null) {
                                    try {
                                        if (!GhepTen.contains("null") && app != "" && text.length() > 5) {
                                            this.body = text.replaceAll("'", " ");
                                            this.Ten_KH = GhepTen;
                                            if (!(!MainActivity.DSkhachhang.contains(this.Ten_KH) || this.body.startsWith("Ok") || this.body.startsWith("Bỏ")
                                                    || this.body.toLowerCase().startsWith("ldpro") || this.body.startsWith("Thiếu")
                                                    || this.body.startsWith("Success")) || this.body.contains("Tra lai")) {
                                                Cursor getTenKH = this.db.GetData("Select * FROM tbl_kh_new WHERE ten_kh ='" + this.Ten_KH + "'");
                                                getTenKH.moveToFirst();
                                                JSONObject jSONObject3 = new JSONObject(getTenKH.getString(5));
                                                this.json = jSONObject3;//"caidat_tg":{"dlgiu_de":0,"dlgiu_lo":0,"dlgiu_xi":0,"dlgiu_xn":0,"dlgiu_bc":0,"khgiu_de":0,"khgiu_lo":0,"khgiu_xi":0,"khgiu_xn":0,"khgiu_bc":0,"ok_tin":3,"xien_nhan":0,"chot_sodu":0,"tg_loxien":"18:13","tg_debc":"18:20","loi_donvi":0,"heso_de":0,"maxDe":0,"maxLo":0,"maxXi":0,"maxCang":0}}
                                                JSONObject jSONObject4 = jSONObject3.getJSONObject("caidat_tg");
                                                this.caidat_tg = jSONObject4;
                                                if (!Congthuc.CheckTime(jSONObject4.getString("tg_debc"))) {
                                                    try {
                                                        Cursor getSoTN = this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mNgayNhan + "' AND ten_kh = '" + this.Ten_KH + "' AND type_kh = 1");
                                                        getSoTN.moveToFirst();
                                                        this.soTN = getSoTN.getInt(0) + 1;
                                                        this.db.QueryData(!this.body.contains("Tra lai") ? "Insert Into tbl_tinnhanS values (null, '" + mNgayNhan + "', '" + mGionhan + "',1, '" + this.Ten_KH + "', '" + getTenKH.getString(1) + "','" + app + "', " + this.soTN + ", '" + this.body + "',null,'" + this.body + "', 'ko',0,1,1, null)" : "Insert Into tbl_tinnhanS values (null, '" + mNgayNhan + "', '" + mGionhan + "',1, '" + this.Ten_KH + "', '" + getTenKH.getString(1) + "','" + app + "', " + this.soTN + ", '" + this.body + "',null,'" + this.body + "', 'ko',0,0,0, null)");
                                                        if (Congthuc.CheckDate(MainActivity.myDate)) {
                                                            Cursor c = this.db.GetData("Select * from tbl_tinnhanS WHERE ngay_nhan = '" + mNgayNhan + "' AND ten_kh = '" + this.Ten_KH + "' AND so_tin_nhan = " + this.soTN + " AND type_kh = 1");
                                                            c.moveToFirst();
                                                            try {
                                                                this.db.Update_TinNhanGoc(c.getInt(0), 1);
                                                            } catch (Exception e3) {
                                                                this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                                                                this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mNgayNhan + "' AND ten_kh = '" + this.Ten_KH + "' AND so_tin_nhan = " + this.soTN + " AND type_kh = 1");
                                                            } catch (Throwable throwable) {
                                                                throwable.printStackTrace();
                                                            }
                                                            if (!Congthuc.CheckTime("18:30") && !this.body.contains("Tra lai")) {
                                                                if (MainActivity.handler == null) {
                                                                    MainActivity.handler = new Handler();
                                                                    MainActivity.handler.postDelayed(MainActivity.runnable, 1000);
                                                                }
                                                                if (!MainActivity.json_Tinnhan.has(this.Ten_KH)) {
                                                                    JSONObject jsontinnan = new JSONObject();
                                                                    jsontinnan.put("Time", 0);
                                                                    MainActivity.json_Tinnhan.put(this.Ten_KH, jsontinnan.toString());
                                                                } else {
                                                                    JSONObject jsontinnan2 = new JSONObject(MainActivity.json_Tinnhan.getString(this.Ten_KH));
                                                                    jsontinnan2.put("Time", 0);
                                                                    MainActivity.json_Tinnhan.put(this.Ten_KH, jsontinnan2.toString());
                                                                }
                                                                this.db.Gui_Tin_Nhan(c.getInt(0));
                                                            }
                                                            c.close();
                                                        }
                                                        getSoTN.close();
                                                        getTenKH.close();
                                                    } catch (Exception e4) {
                                                        return;
                                                    }
                                                } else {
                                                    Cursor getSoTN2 = this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mNgayNhan + "' AND ten_kh = '" + this.Ten_KH + "'");
                                                    getSoTN2.moveToFirst();
                                                    this.soTN = getSoTN2.getInt(0) + 1;
                                                    this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mNgayNhan + "', '" + mGionhan + "',1, '" + this.Ten_KH + "', '" + getTenKH.getString(1) + "','" + app + "', " + this.soTN + ", '" + this.body + "',null,'" + this.body + "', 'Hết giờ nhận số!',0,1,1, null)");
                                                    getSoTN2.close();
                                                    getTenKH.close();
                                                    if (!Congthuc.CheckTime("18:30") && MainActivity.jSon_Setting.getInt("tin_qua_gio") == 1) {
                                                        NotificationWearReader(this.Ten_KH, "Hết giờ nhận!");
                                                    }
                                                }
                                                if (!getTenKH.isClosed()) {
                                                    getTenKH.close();
                                                    return;
                                                }
                                                return;
                                            }
                                            return;
                                        }
                                    } catch (Exception e5) {
                                        return;
                                    }
                                }
                            } catch (Exception e6) {
                                return;
                            }
                        }
                    } catch (Exception e7) {
                        return;
                    }
                }

                try {
                    this.ID = title.trim();
                    this.mWhat = text;
                    GhepTen = app + " - " + this.ID.trim();
                    Notification.WearableExtender wearableExtender2 = new Notification.WearableExtender(notification);
                    ArrayList<Notification.Action> acti2 = new ArrayList<>();
                    acti2.addAll(wearableExtender2.getActions());
                    it = acti2.iterator();
                    while (it.hasNext()) {
                    }
                    MainActivity.Notifi = this;
                    NotificationWearReader(GhepTen, "Tin mồi!");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                Log.e("TAG", "onNotificationPosted: error " + e.getMessage());
            }
        }
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    public void NotificationWearReader(String mName, String message) {
        int indexName = MainActivity.arr_TenKH.indexOf(mName);
        if (indexName > -1) {
            try {
                Intent localIntent = new Intent();
                Bundle localBundle = MainActivity.contactslist.get(indexName).remoteExtras;
                RemoteInput[] remoteInputs = {MainActivity.contactslist.get(indexName).remoteInput};
                if (Build.VERSION.SDK_INT >= 20) {
                    localBundle.putCharSequence(remoteInputs[0].getResultKey(), message);
                    RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle);
                }
                MainActivity.contactslist.get(indexName).pendingIntent.send(MainActivity.Notifi, 0, localIntent);
                if (MainActivity.Json_Tinnhan.has(mName)) {
                    MainActivity.Json_Tinnhan.remove(mName);
                }
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
    }
}