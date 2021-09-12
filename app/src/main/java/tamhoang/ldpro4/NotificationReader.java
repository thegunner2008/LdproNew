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
        String str;
        Object obj;
        String mNgayNhan;
        String app;
        String app2;
        String GhepTen;
        String str2;
        String mNgayNhan2;
        String mGionhan;
        Iterator<Notification.Action> it;
        String charSequence;
        int i = 0;
        int i2 = 0;
        String charSequence2;
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
            String mNgayNhan3 = dmyFormat.format(calendar.getTime());
            String mGionhan2 = hourFormat.format(calendar.getTime());
            try {
                this.ID = "";
                Bundle extras = sbn.getNotification().extras;
                String text = extras.getCharSequence(NotificationCompat.EXTRA_TEXT).toString();
                Notification notification = sbn.getNotification();
                String Group = extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString();
                try {
                    if (sbn.getPackageName().equals(ZALO)) {
                        try {
                            if (text.indexOf("tin nhắn chưa đọc.") != -1) {
                                obj = "";
                                str = "'";
                                mNgayNhan = mNgayNhan3;
                            } else if (text.indexOf("đã gửi tập tin cho bạn") != -1) {
                                obj = "";
                                str = "'";
                                mNgayNhan = mNgayNhan3;
                            } else if (text.indexOf("cảm xúc với tin nhắn bạn gửi") != -1) {
                                obj = "";
                                str = "'";
                                mNgayNhan = mNgayNhan3;
                            } else if (text.indexOf("hình động cho bạn") != -1) {
                                obj = "";
                                str = "'";
                                mNgayNhan = mNgayNhan3;
                            } else if (text.indexOf("thành viên của nhóm") != -1) {
                                obj = "";
                                str = "'";
                                mNgayNhan = mNgayNhan3;
                            } else if (text.indexOf("thêm vào nhóm") != -1) {
                                obj = "";
                                str = "'";
                                mNgayNhan = mNgayNhan3;
                            } else if (text.indexOf("gửi ảnh cho bạn") != -1) {
                                obj = "";
                                str = "'";
                                mNgayNhan = mNgayNhan3;
                            } else if (text.indexOf("cuộc trò chuyện") == -1) {
                                String charSequence3 = extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString();
                                this.ID = charSequence3;
                                if (charSequence3.indexOf(" (") > -1) {
                                    mNgayNhan = mNgayNhan3;
                                    try {
                                        this.ID = this.ID.substring(0, this.ID.indexOf("(")).trim();
                                    } catch (Exception e) {
                                        return;
                                    }
                                } else {
                                    mNgayNhan = mNgayNhan3;
                                }
                                if (Group.indexOf("Nhóm") == 0) {
                                    this.ID = this.ID.substring(this.ID.indexOf(":") + 2).trim();
                                    text = text.substring(text.indexOf(":") + 1).trim();
                                }
                                text = text.replaceAll("'", "");
                                app = "ZL";
                                try {
                                    JSONObject jSONObject = MainActivity.Json_Tinnhan;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(app);
                                    sb.append(" - ");
                                    obj = "";
                                    sb.append(this.ID.trim());
                                    if (jSONObject.has(sb.toString())) {
                                        JSONObject jSONObject2 = MainActivity.Json_Tinnhan;
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append(app);
                                        sb2.append(" - ");
                                        str = "'";
                                        sb2.append(this.ID.trim());
                                        JSONObject jsonTin = new JSONObject(jSONObject2.getString(sb2.toString()));
                                        if (jsonTin.has(text.trim())) {
                                            app = null;
                                        } else {
                                            jsonTin.put(text.trim(), "OK");
                                            MainActivity.Json_Tinnhan.put(app + " - " + this.ID.trim(), jsonTin.toString());
                                        }
                                    } else {
                                        str = "'";
                                        JSONObject jsonTin2 = new JSONObject();
                                        jsonTin2.put(text.trim(), "OK");
                                        MainActivity.Json_Tinnhan.put(app + " - " + this.ID.trim(), jsonTin2.toString());
                                    }
                                    if (sbn.getPackageName().equals(VIBER) && text.indexOf("Bạn có các tin nhắn") == -1 && text.indexOf("thêm bạn vào") == -1 && text.indexOf("uộc gọi") == -1 && text.indexOf("tin nhắn chưa đọc") == -1) {
                                        charSequence2 = extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString();
                                        this.ID = charSequence2;
                                        if (charSequence2.indexOf("trong") > -1) {
                                            this.ID = this.ID.substring(this.ID.indexOf("trong") + 6);
                                        }
                                        if (this.ID.indexOf("tin nhắn chưa đọc") == -1) {
                                            app2 = "VB";
                                            try {
                                                if (MainActivity.Json_Tinnhan.has(app2 + " - " + this.ID.trim())) {
                                                    JSONObject jsonTin3 = new JSONObject(MainActivity.Json_Tinnhan.getString(app2 + " - " + this.ID.trim()));
                                                    if (jsonTin3.has(text.trim())) {
                                                        app2 = null;
                                                    } else {
                                                        jsonTin3.put(text.trim(), "OK");
                                                        MainActivity.Json_Tinnhan.put(app2 + " - " + this.ID.trim(), jsonTin3.toString());
                                                    }
                                                } else {
                                                    JSONObject jsonTin4 = new JSONObject();
                                                    jsonTin4.put(text.trim(), "OK");
                                                    MainActivity.Json_Tinnhan.put(app2 + " - " + this.ID.trim(), jsonTin4.toString());
                                                }
                                                if (sbn.getPackageName().equals(WHATSAPP) && text.indexOf("tin nhắn") == -1) {
                                                    charSequence = extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString();
                                                    this.ID = charSequence;
                                                    if (charSequence.indexOf(":") > -1) {
                                                        this.ID = this.ID.substring(0, this.ID.indexOf(":")).trim();
                                                        this.mWhat = Group.substring(Group.indexOf(":") + 1).trim();
                                                    }
                                                    if (this.ID.indexOf("@") > -1) {
                                                        this.mWhat = this.ID.substring(0, this.ID.indexOf("@"));
                                                        this.ID = this.ID.substring(this.ID.indexOf("@") + 1).trim();
                                                    }
                                                    if (this.ID.indexOf(" (") > -1) {
                                                        this.ID = this.ID.substring(0, this.ID.indexOf("(")).trim();
                                                    }
                                                    this.ID = this.ID.trim();
                                                    for (i = 0; i < "aaaaaaaaaaaaaaaaaeeeeeeeeeeeooooooooooooooooouuuuuuuuuuuiiiiiyyyyydx".length(); i++) {
                                                        this.ID = this.ID.replace("ăâàằầáắấảẳẩãẵẫạặậễẽểẻéêèềếẹệôòồơờóốớỏổởõỗỡọộợưúùứừủửũữụựìíỉĩịỳýỷỹỵđ×".charAt(i), "aaaaaaaaaaaaaaaaaeeeeeeeeeeeooooooooooooooooouuuuuuuuuuuiiiiiyyyyydx".charAt(i));
                                                    }
                                                    for (i2 = 0; i2 < this.ID.length(); i2++) {
                                                        if (this.ID.charAt(i2) > 127 || this.ID.charAt(i2) < 31) {
                                                            this.ID = this.ID.substring(0, i2) + this.ID.substring(i2 + 1);
                                                        }
                                                    }
                                                    app2 = "WA";
                                                    if (text.indexOf(this.mWhat.trim()) > -1 || this.mWhat.length() <= 0) {
                                                        this.mWhat = text;
                                                        if (MainActivity.Json_Tinnhan.has(app2 + " - " + this.ID.trim())) {
                                                            JSONObject jsonTin5 = new JSONObject(MainActivity.Json_Tinnhan.getString(app2 + " - " + this.ID.trim()));
                                                            if (jsonTin5.has(text.trim())) {
                                                                app2 = null;
                                                            } else {
                                                                jsonTin5.put(text.trim(), "OK");
                                                                MainActivity.Json_Tinnhan.put(app2 + " - " + this.ID.trim(), jsonTin5.toString());
                                                            }
                                                        } else {
                                                            JSONObject jsonTin6 = new JSONObject();
                                                            jsonTin6.put(text.trim(), "OK");
                                                            MainActivity.Json_Tinnhan.put(app2 + " - " + this.ID.trim(), jsonTin6.toString());
                                                        }
                                                    } else {
                                                        app2 = null;
                                                    }
                                                }
                                                GhepTen = app2 + " - " + this.ID.trim();
                                                if (MainActivity.arr_TenKH.indexOf(GhepTen) == -1 && GhepTen.indexOf("null") == -1) {
                                                    Notification.WearableExtender wearableExtender = new Notification.WearableExtender(notification);
                                                    ArrayList<Notification.Action> acti = new ArrayList<>();
                                                    acti.addAll(wearableExtender.getActions());
                                                    it = acti.iterator();
                                                    while (it.hasNext()) {
                                                        Notification.Action act = it.next();
                                                        if (act.title.toString().indexOf("Trả lời") > -1 || act.title.toString().indexOf("Reply") > -1) {
                                                            MainActivity.arr_TenKH.add(GhepTen);
                                                            Contact cont = new Contact();
                                                            cont.name = GhepTen;
                                                            cont.app = app2;
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
                                                if (text.toLowerCase().indexOf("ldpro") > -1 && text.trim().length() == 5) {
                                                    MainActivity.Notifi = this;
                                                    NotificationWearReader(GhepTen, "Tin mồi!");
                                                }
                                                if (app2 != null) {
                                                    try {
                                                        if (GhepTen.indexOf("null") == -1 && app2 != "null") {
                                                            Database database = this.db;
                                                            StringBuilder sb3 = new StringBuilder();
                                                            sb3.append("Select * From Chat_database WHERE ngay_nhan = '");
                                                            mNgayNhan2 = mNgayNhan;
                                                            try {
                                                                sb3.append(mNgayNhan2);
                                                                sb3.append("' And Ten_kh = '");
                                                                sb3.append(GhepTen);
                                                                sb3.append("' AND nd_goc = '");
                                                                sb3.append(text);
                                                                str2 = str;
                                                                sb3.append(str2);
                                                                Cursor cursor = database.GetData(sb3.toString());
                                                                if (cursor.getCount() == 0) {
                                                                    StringBuilder sb4 = new StringBuilder();
                                                                    sb4.append("Insert into Chat_database Values( null,'");
                                                                    sb4.append(mNgayNhan2);
                                                                    sb4.append("', '");
                                                                    mGionhan = mGionhan2;
                                                                    try {
                                                                        sb4.append(mGionhan);
                                                                        sb4.append("', 1, '");
                                                                        sb4.append(GhepTen);
                                                                        sb4.append("', '");
                                                                        sb4.append(GhepTen);
                                                                        sb4.append("', '");
                                                                        sb4.append(app2);
                                                                        sb4.append("','");
                                                                        sb4.append(text);
                                                                        sb4.append("',1)");
                                                                        this.db.QueryData(sb4.toString());
                                                                        MainActivity.sms = true;
                                                                    } catch (Exception e2) {
                                                                        return;
                                                                    }
                                                                } else {
                                                                    mGionhan = mGionhan2;
                                                                    app2 = null;
                                                                }
                                                                cursor.close();
                                                                if (app2 != null) {
                                                                    try {
                                                                        if (GhepTen.indexOf("null") == -1 && app2 != "null" && text.length() > 5) {
                                                                            this.body = text.replaceAll(str2, " ");
                                                                            this.Ten_KH = GhepTen;
                                                                            if (!(MainActivity.DSkhachhang.indexOf(this.Ten_KH) <= -1 || this.body.startsWith("Ok") || this.body.startsWith("Bỏ") || this.body.toLowerCase().startsWith("ldpro") || this.body.startsWith("Thiếu") || this.body.startsWith("Success")) || this.body.indexOf("Tra lai") > -1) {
                                                                                Cursor getTenKH = this.db.GetData("Select * FROM tbl_kh_new WHERE ten_kh ='" + this.Ten_KH + str2);
                                                                                getTenKH.moveToFirst();
                                                                                JSONObject jSONObject3 = new JSONObject(getTenKH.getString(5));
                                                                                this.json = jSONObject3;
                                                                                JSONObject jSONObject4 = jSONObject3.getJSONObject("caidat_tg");
                                                                                this.caidat_tg = jSONObject4;
                                                                                if (!Congthuc.CheckTime(jSONObject4.getString("tg_debc"))) {
                                                                                    try {
                                                                                        Cursor getSoTN = this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mNgayNhan2 + "' AND ten_kh = '" + this.Ten_KH + "' AND type_kh = 1");
                                                                                        getSoTN.moveToFirst();
                                                                                        this.soTN = getSoTN.getInt(0) + 1;
                                                                                        this.db.QueryData(this.body.indexOf("Tra lai") == -1 ? "Insert Into tbl_tinnhanS values (null, '" + mNgayNhan2 + "', '" + mGionhan + "',1, '" + this.Ten_KH + "', '" + getTenKH.getString(1) + "','" + app2 + "', " + this.soTN + ", '" + this.body + "',null,'" + this.body + "', 'ko',0,1,1, null)" : "Insert Into tbl_tinnhanS values (null, '" + mNgayNhan2 + "', '" + mGionhan + "',1, '" + this.Ten_KH + "', '" + getTenKH.getString(1) + "','" + app2 + "', " + this.soTN + ", '" + this.body + "',null,'" + this.body + "', 'ko',0,0,0, null)");
                                                                                        if (Congthuc.CheckDate(MainActivity.myDate)) {
                                                                                            Cursor c = this.db.GetData("Select * from tbl_tinnhanS WHERE ngay_nhan = '" + mNgayNhan2 + "' AND ten_kh = '" + this.Ten_KH + "' AND so_tin_nhan = " + this.soTN + " AND type_kh = 1");
                                                                                            c.moveToFirst();
                                                                                            try {
                                                                                                this.db.Update_TinNhanGoc(c.getInt(0), 1);
                                                                                            } catch (Exception e3) {
                                                                                                this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                                                                                                this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mNgayNhan2 + "' AND ten_kh = '" + this.Ten_KH + "' AND so_tin_nhan = " + this.soTN + " AND type_kh = 1");
                                                                                            } catch (Throwable throwable) {
                                                                                                throwable.printStackTrace();
                                                                                            }
                                                                                            if (!Congthuc.CheckTime("18:30") && this.body.indexOf("Tra lai") == -1) {
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
                                                                                    Cursor getSoTN2 = this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mNgayNhan2 + "' AND ten_kh = '" + this.Ten_KH + str2);
                                                                                    getSoTN2.moveToFirst();
                                                                                    this.soTN = getSoTN2.getInt(0) + 1;
                                                                                    this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mNgayNhan2 + "', '" + mGionhan + "',1, '" + this.Ten_KH + "', '" + getTenKH.getString(1) + "','" + app2 + "', " + this.soTN + ", '" + this.body + "',null,'" + this.body + "', 'Hết giờ nhận số!',0,1,1, null)");
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
                                                mGionhan = mGionhan2;
                                                mNgayNhan2 = mNgayNhan;
                                                str2 = str;
                                                if (app2 != null) {
                                                }
                                            } catch (Exception e8) {
                                                return;
                                            }
                                        }
                                    }
                                    app2 = app;
                                    try {
                                        charSequence = extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString();
                                        this.ID = charSequence;
                                        if (charSequence.indexOf(":") > -1) {
                                        }
                                        if (this.ID.indexOf("@") > -1) {
                                        }
                                        if (this.ID.indexOf(" (") > -1) {
                                        }
                                        this.ID = this.ID.trim();
                                        while (i < "aaaaaaaaaaaaaaaaaeeeeeeeeeeeooooooooooooooooouuuuuuuuuuuiiiiiyyyyydx".length()) {
                                        }
                                        while (i2 < this.ID.length()) {
                                        }
                                        app2 = "WA";
                                        if (text.indexOf(this.mWhat.trim()) > -1) {
                                        }
                                        this.mWhat = text;
                                        if (MainActivity.Json_Tinnhan.has(app2 + " - " + this.ID.trim())) {
                                        }
                                        GhepTen = app2 + " - " + this.ID.trim();
                                        Notification.WearableExtender wearableExtender2 = new Notification.WearableExtender(notification);
                                        ArrayList<Notification.Action> acti2 = new ArrayList<>();
                                        acti2.addAll(wearableExtender2.getActions());
                                        it = acti2.iterator();
                                        while (it.hasNext()) {
                                        }
                                        MainActivity.Notifi = this;
                                        NotificationWearReader(GhepTen, "Tin mồi!");
                                        if (app2 != null) {
                                        }
                                        mGionhan = mGionhan2;
                                        mNgayNhan2 = mNgayNhan;
                                        str2 = str;
                                        if (app2 != null) {
                                        }
                                    } catch (Exception e9) {
                                        return;
                                    }
                                } catch (Exception e10) {
                                    return;
                                }
                            } else {
                                obj = "";
                                str = "'";
                                mNgayNhan = mNgayNhan3;
                            }
                        } catch (Exception e11) {
                            return;
                        }
                    } else {
                        obj = "";
                        str = "'";
                        mNgayNhan = mNgayNhan3;
                    }
                    app = null;
                    try {
                        charSequence2 = extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString();
                        this.ID = charSequence2;
                        if (charSequence2.indexOf("trong") > -1) {
                        }
                        if (this.ID.indexOf("tin nhắn chưa đọc") == -1) {
                        }
                        app2 = app;
                        charSequence = extras.getCharSequence(NotificationCompat.EXTRA_TITLE).toString();
                        this.ID = charSequence;
                        if (charSequence.indexOf(":") > -1) {
                        }
                        if (this.ID.indexOf("@") > -1) {
                        }
                        if (this.ID.indexOf(" (") > -1) {
                        }
                        this.ID = this.ID.trim();
                        while (i < "aaaaaaaaaaaaaaaaaeeeeeeeeeeeooooooooooooooooouuuuuuuuuuuiiiiiyyyyydx".length()) {
                        }
                        while (i2 < this.ID.length()) {
                        }
                        app2 = "WA";
                        if (text.indexOf(this.mWhat.trim()) > -1) {
                        }
                        this.mWhat = text;
                        if (MainActivity.Json_Tinnhan.has(app2 + " - " + this.ID.trim())) {
                        }
                        GhepTen = app2 + " - " + this.ID.trim();
                        Notification.WearableExtender wearableExtender22 = new Notification.WearableExtender(notification);
                        ArrayList<Notification.Action> acti22 = new ArrayList<>();
                        acti22.addAll(wearableExtender22.getActions());
                        it = acti22.iterator();
                        while (it.hasNext()) {
                        }
                        MainActivity.Notifi = this;
                        NotificationWearReader(GhepTen, "Tin mồi!");
                        if (app2 != null) {
                        }
                        mGionhan = mGionhan2;
                        mNgayNhan2 = mNgayNhan;
                        str2 = str;
                        if (app2 != null) {
                        }
                    } catch (Exception e12) {
                    }
                } catch (Exception e13) {
                }
            } catch (Exception e14) {
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