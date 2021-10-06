package tamhoang.ldpro4.data;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.internal.view.SupportMenu;

import java.io.File;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationReader;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.constants.Constants;

public class Database extends SQLiteOpenHelper {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    protected static SQLiteDatabase db;
    JSONObject caidat_gia;
    JSONObject caidat_tg;
    JSONObject json;
    JSONObject jsonDanSo;
    JSONObject json_Tralai;
    public String[][] mang;
    private final Context mcontext;

    public Database(Context context) {
        super(context, "DEMO_LDPRO", null, 1);
        this.mcontext = context;
    }

    public void SendSMS(String Sdt, String Mess) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendMultipartTextMessage(Sdt, null, sms.divideMessage(Mess), null, null);
    }

    public void Update_TinNhanGoc(int id, int type_kh) throws Throwable {
        Cursor c = GetData("Select * From tbl_tinnhanS WHERE id = " + id);
        c.moveToFirst();
        if (c.getString(11).contains("ok")) {
            Cursor c1 = GetData("Select nd_phantich FROM tbl_tinnhanS WHERE id = " + id);
            c1.moveToFirst();
            String str = c1.getString(0);
            for (int i = 1; i < 6; i++) {
                str = str.replaceAll("\\*", "");
            }
            QueryData("Update tbl_tinnhanS set nd_phantich = '" + str + "', nd_sua = '" + str + "' WHERE id = " + id);
            NhapSoChiTiet(id);
            StringBuilder sb = new StringBuilder();
            sb.append("Update tbl_tinnhanS set phat_hien_loi = 'ok' WHERE id = ");
            sb.append(id);
            QueryData(sb.toString());
        } else {
            String str2 = Congthuc.fixTinNhan1(Congthuc.convertKhongDau(c.getString(10)));
            String Loi = null;
            Cursor cursor = GetData("Select * From thay_the_phu");
            while (cursor.moveToNext()) {
                str2 = str2.replaceAll(cursor.getString(1), cursor.getString(2)).replace("  ", " ");
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            String str3 = Congthuc.fixTinNhan1(str2);
            QueryData("Update tbl_tinnhanS set nd_phantich = '" + str3 + "', nd_sua = '" + str3 + "' WHERE id = " + id);
            if (str3.contains("bo") && !str3.contains("bor")) {
                for (int j = str3.indexOf("bo") + 3; j < str3.length(); j++) {
                    if (str3.substring(j, j + 1).indexOf(" ") == -1 && !Congthuc.isNumeric(str3.substring(j, j + 1))) {
                        Loi = "Không hiểu " + str3.substring(str3.indexOf("bo"));
                    }
                }
            }
            if (str3.contains("Không hiểu")) {
                QueryData("Update tbl_tinnhanS set nd_phantich = '" + str3 + "', nd_sua = '" + str3 + "',  phat_hien_loi ='" + Loi + "' Where id = " + id);
                createNotification(str3, this.mcontext);
            } else {
                NhanTinNhan(id, type_kh);
                c = GetData("Select * From tbl_tinnhanS WHERE id = " + id);
                c.moveToFirst();
                String _xulytin = c.getString(11);
                if (_xulytin.contains("Không hiểu")) {
                    createNotification(_xulytin, this.mcontext);
                } else {
                    NhapSoChiTiet(id);
                }
                if (!c.isClosed()) {
                    c.close();
                }
            }
        }
    }

    private void createNotification(String aMessage, Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);
        new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(Constants.ALL_SMS_LOADER, 134217728);
        NotificationCompat.Builder nBuider = new NotificationCompat.Builder(context);
        nBuider.setContentTitle("Ld.pro");
        nBuider.setContentText(aMessage);
        nBuider.setSmallIcon(R.drawable.icon);
        nBuider.setContentIntent(pendingIntent);
        nBuider.setDefaults(1);
        nBuider.setVibrate(new long[]{100, 2000, 500, 2000});
        nBuider.setLights(-16711936, 400, 400);
        @SuppressLint("WrongConstant") NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("10001", "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 2000, 500, 2000});
            nBuider.setChannelId("10001");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        mNotificationManager.notify(0, nBuider.build());
    }

    public void NhanTinNhan(Integer id, int type_kh) throws Throwable {
        String str_Err;
        Cursor getThongtin;
        String str;
        String str2;
        String str3;
        String so_tin;
        String str4;
        String str5;
        String so_tin2;
        String str6;
        Cursor cursor = null;
        int i = 0;
        String str7;
        String str_Err2 = null;
        String so_tin3;
        String str8;
        String str1;
        String soxien;
        String TinGoc;
        String TinGoc2;
        String str9 = null;
        String str10 = null;
        String str11 = null;
        String str12 = null;
        Database database = null;
        String str_Err3 = null;
        String str_Err4 = null;
        String str13;
        String str14;
        String theodoi;
        int Dem_error = 0;
        StringBuilder TinGoc3;
        String TinXuly;
        int i2;
        String TinXuly2;
        int i3;
        String str15;
        String TinGoc4;
        String rWError;
        JSONObject jsonDan;
        int i4;
        String Bor;
        String Bor2;
        String str16;
        String TinGoc5;
        String str17;
        String str18;
        String str19;
        boolean xien;
        boolean lo;
        boolean nhat;
        String TinXuly3;
        String str20;
        JSONObject jsonDan2;
        String str21;
        String str22;
        String str23;
        String str24;
        String str25 = null;
        String str26 = null;
        char c = 0;
        String rWError2 = null;
        String KhongHieu = null;
        String str27 = null;
        String theodoi2 = null;
        String dayso = null;
        String str28 = null;
        String str29 = null;
        String theodoi3;
        int k;
        String str30 = null;
        String str31 = null;
        String str32 = null;
        int i32;
        String str33 = null;
        int k2 = 0;
        String theodoi4 = null;
        String str34 = null;
        String str35 = null;
        String str36 = null;
        String str37 = null;
        String str38 = null;
        Database database2;
        String theodoi5 = null;
        String soxien2 = null;
        String strf = null;
        String str39 = null;
        String str40 = null;
        Exception e;
        String str41 = null;
        String dayso2;
        String dayso3 = null;
        String dayso4 = null;
        String str42 = null;
        String str43;
        String so_tin4;
        Database database3 = this;
        database3.mang = (String[][]) Array.newInstance(String.class, 1000, 6);
        Cursor cursor2 = database3.GetData("Select nd_phantich, ten_kh, ok_tn, ngay_nhan From tbl_tinnhanS WHERE id = " + id);
        cursor2.moveToFirst();
        int rw = 0;
        String theodoi6 = ",";
        String soxien4 = null;
        String str44 = " ";
        boolean quagio = false;
        String str45 = "bc";
        String dayso5 = null;
        String str46 = "lo";
        String str47 = "xi";
        String so_tin5 = "xq";
        String str48 = "\n";
        String str49 = "xn";
        String str50 = cursor2.getString(0).replace("ldpro", "").replace("</font>", "").replaceAll("-", theodoi6).replaceAll("/", ";").replaceAll("\n", str44).replaceAll("\\.", theodoi6).replaceAll("x ", " x ").replaceAll("ba bc", str45).replaceAll(";,", ";").replaceAll("; ,", ";").replaceAll("; lo", str46).replaceAll(";lo", str46).replaceAll("; de", "de").replaceAll(";de", "de").replaceAll("; xi", str47).replaceAll(";xi", str47).replaceAll("; bc", str45).replaceAll(";bc", str45).replaceAll("; xq", so_tin5).replaceAll(";xq", so_tin5).replaceAll("; xn", str49).replaceAll(";xn", str49).replaceAll("bo", " bo").replaceAll("duoi", "dit").replaceAll("dit 10", "duoi 10").replaceAll("tong dit", "tong <");
        StringBuilder sb = new StringBuilder();
        sb.append("Select * From tbl_kh_new Where ten_kh = '");
        String str51 = "</font>";
        sb.append(cursor2.getString(1));
        sb.append("'");
        Cursor getThongtin2 = database3.GetData(sb.toString());
        getThongtin2.moveToFirst();
        String str52 = "ldpro";
        JSONObject jSONObject = new JSONObject(getThongtin2.getString(5));
        database3.json = jSONObject;
        database3.caidat_tg = jSONObject.getJSONObject("caidat_tg");
        String str53 = Congthuc.Xuly_DauTN(str50);
        String str54 = "Không hiểu ";
        if (str53.length() < 3) {
            str_Err = str54 + str53;
        } else {
            str_Err = "";
        }
        String str_Err5 = "t";
        if (str53.startsWith("tin")) {
            str53.replaceFirst("tin", str_Err5);
        }
        String str55 = Congthuc.fixTinNhan(str53);
        getThongtin = getThongtin2;
        String so_tin6 = "";
        str55 = str55 + str44;
        int i1 = -1;
        while (true) {
            so_tin4 = so_tin6;
            str3 = str45;

            int indexOf = str55.indexOf("tin", i1 + 1);
            i1 = indexOf;
            if (indexOf == -1) {
                break;
            }
            int i6 = i1 + 5;
            while (i6 < i1 + 10 && Congthuc.isNumeric(str55.substring(i1 + 4, i6))) {
                i6++;
            }
            str2 = str49;
            if (i6 - i1 > 5) {
                String sss = str55.substring(0, i6).replace("tin", "").trim();
                if (Congthuc.isNumeric(sss)) {
                    so_tin4 = sss;
                }
                StringBuilder sb2 = new StringBuilder();
                str = so_tin5;
                sb2.append(str55.substring(0, i1));
                sb2.append(str55.substring(i6));
                str55 = sb2.toString();
                so_tin6 = so_tin4;
                str45 = str3;
                str49 = str2;
                so_tin5 = str;
            } else {
                str = so_tin5;
                so_tin6 = so_tin4;
                str45 = str3;
                str49 = str2;
            }
        }
        str = so_tin5;
        str2 = str49;
        str55 = str55.trim();
        int i7 = 6;
        so_tin = so_tin4;
        while (i7 > 0) {
            String Sss = str55.substring(0, i7);
            if (Sss.trim().contains(str_Err5)) {
                String Sss2 = Sss.replaceAll(str_Err5, "").replaceAll(str44, "").replaceAll(theodoi6, "");
                if (Congthuc.isNumeric(Sss2)) {
                    so_tin = Sss2;
                    str55 = str55.substring(i7);
                }
            }
            i7--;
            i1 = i1;
        }
        Cursor getSoTN = GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ten_kh = '" + cursor2.getString(1) + "' and type_kh = 1");
        getSoTN.moveToFirst();
        int soTN = getSoTN.getInt(0) + 1;
        str4 = "Không hiểu";
        if (str55.contains(str4)) {
            so_tin2 = so_tin;
            cursor = cursor2;
            so_tin3 = str2;
            str7 = str;
            i = -1;
            str5 = str55;
            str6 = "";
            str8 = str3;
        } else {
            if (str55.length() < 8) {
                so_tin2 = so_tin;
                cursor = cursor2;
                so_tin3 = str2;
                i = -1;
                str6 = "";
                str_Err2 = str54 + str55;
                str7 = str;
                str5 = str55;
                str8 = str3;
            } else {
                cursor = cursor2;
                if (!str55.substring(0, 5).contains("de")) {
                    so_tin2 = so_tin;
                    if (str55.substring(0, 5).contains(str46)) {
                        so_tin3 = str2;
                        str7 = str;
                        str5 = str55;
                        str6 = "";
                        str8 = str3;
                    } else if (!str55.substring(0, 5).contains(str47)) {
                        str7 = str;
                        if (str55.substring(0, 5).contains(str7)) {
                            str5 = str55;
                            str8 = str3;
                            so_tin3 = str2;
                            str6 = "";
                        } else if (!str55.substring(0, 5).contains("hc")) {
                            str5 = str55;
                            so_tin3 = str2;
                            if (!str55.substring(0, 5).contains(so_tin3)) {
                                str6 = "";
                                String substring = str55.substring(0, 5);
                                str8 = str3;
                                if (!substring.contains(str8) && !str55.substring(0, 5).contains("xg")) {
                                    str_Err2 = "Không hiểu dạng";
                                    i = -1;
                                }
                            } else {
                                str6 = "";
                                str8 = str3;
                            }
                        } else {
                            str5 = str55;
                            str8 = str3;
                            so_tin3 = str2;
                            str6 = "";
                        }
                    } else {
                        so_tin3 = str2;
                        str7 = str;
                        str5 = str55;
                        str6 = "";
                        str8 = str3;
                    }
                } else {
                    so_tin2 = so_tin;
                    so_tin3 = str2;
                    str7 = str;
                    str5 = str55;
                    str6 = "";
                    str8 = str3;
                }
                i = -1;
                if (str55.indexOf(" bo ") > -1) {
                    str_Err2 = "Không hiểu bo ";
                }
            }
            str1 = Congthuc.PhanTichTinNhan(str55);
            if (str1.indexOf(str4) <= i) {
                str_Err2 = str1;
            } else if (str1.indexOf("x ") == i) {
                str_Err2 = str54 + str1;
            }
            if (Congthuc.CheckTime(database3.caidat_tg.getString("tg_loxien")) && !Congthuc.CheckTime("18:30")) {
                quagio = true;
            }
            if (!str_Err2.contains(str4)) {
                String str56 = str1.replaceAll(" , ", str44).replaceAll(" ,", str44);
                int i8 = 1;
                while (i8 < 10) {
                    str56 = str56.replaceAll("  ", str44).replaceAll(",,", theodoi6);
                    i8++;
                    str1 = str1;
                }
                String str57 = str56.trim() + str44;
                int k4 = 0;
                String theodoi7 = "";
                int i12 = -1;
                while (true) {
                    int indexOf2 = str57.indexOf(" x ", i12 + 1);
                    i12 = indexOf2;
                    if (indexOf2 == -1) {
                        break;
                    }
                    String tien = "";
                    int i22 = i12;
                    while (true) {
                        dayso = str54;
                        if (i22 >= str57.length()) {
                            str28 = str4;
                            break;
                        }
                        str28 = str4;
                        if (str57.charAt(i22) == ' ' && tien.length() > 0) {
                            break;
                        }
                        if ("0123456789,tr".indexOf(str57.substring(i22, i22 + 1)) > -1) {
                            tien = tien + str57.charAt(i22);
                        }
                        i22++;
                        str54 = dayso;
                        str4 = str28;
                    }
                    String dtien = "";
                    int i33 = i22;
                    while (i33 < str57.length() && (Character.isLetter(str57.charAt(i33)) || dtien.length() <= 0)) {
                        dtien = dtien + str57.charAt(i33);
                        i33++;
                        tien = tien;
                    }
                    if (i22 == i33) {
                        i33--;
                    }
                    if (dtien.indexOf("dau") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("dit") > -1) {
                        str29 = str7;
                    } else if (dtien.contains("tong")) {
                        str29 = str7;
                    } else if (dtien.indexOf("cham") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("dan") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("boj") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf(str46) > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("de") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("xi") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf(so_tin3) > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("hc") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("xg") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf(" x") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("kep") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("sat") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("to") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("nho") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("chan") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("le") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("ko") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("chia") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("duoi") > -1) {
                        str29 = str7;
                    } else if (dtien.indexOf("be") > -1) {
                        str29 = str7;
                    } else {
                        if (dtien.indexOf("x ") > -1) {
                            int i34 = i22 - 1;
                            while (true) {
                                if (i34 <= 0) {
                                    i33 = i34;
                                    str29 = str7;
                                    theodoi3 = theodoi7;
                                    break;
                                } else if (!Congthuc.isNumeric(str57.substring(i34, i34 + 1))) {
                                    dayso5 = str57.substring(k4, i34 + 1);
                                    int k5 = i34 + 1;
                                    i33 = i34;
                                    str29 = str7;
                                    k4 = k5;
                                    theodoi3 = str57.substring(k5);
                                    break;
                                } else {
                                    i34--;
                                }
                            }
                        } else {
                            String dayso6 = str57.substring(k4, i33);
                            if (dayso6.trim().length() > 3) {
                                str29 = str7;
                                if (dayso6.substring(0, 4).indexOf("bor") > -1) {
                                    dayso5 = "de " + dayso6;
                                    theodoi3 = str57.substring(i33);
                                    k4 = i33;
                                }
                            } else {
                                str29 = str7;
                            }
                            dayso5 = dayso6;
                            theodoi3 = str57.substring(i33);
                            k4 = i33;
                        }
                        k = rw + 1;
                        strf = dayso5.trim();
                        if (!strf.startsWith(str_Err5)) {

                            str33 = str57;
                            if (strf.length() > 6) {
                                int f = 6;
                                while (f > 0) {
                                    String Sss3 = strf.substring(0, f);
                                    theodoi4 = theodoi3;
                                    if (Sss3.trim().contains(str_Err5)) {
                                        theodoi5 = str6;

                                        if (Congthuc.isNumeric(Sss3.replaceAll(str_Err5, theodoi5).replaceAll(str44, theodoi5).replaceAll(theodoi6, theodoi5))) {
                                            StringBuilder sb3 = new StringBuilder();
                                            sb3.append(str44);
                                            str34 = str_Err5;
                                            sb3.append(strf.substring(f + 1));
                                            sb3.append(str44);
                                            dayso5 = sb3.toString();
                                        } else {
                                            str34 = str_Err5;
                                        }
                                    } else {
                                        theodoi5 = str6;
                                        str34 = str_Err5;
                                    }
                                    f--;
                                    str_Err5 = str34;
                                    i22 = i22;
                                    str6 = theodoi5;
                                    theodoi3 = theodoi4;
                                }
                                str39 = dayso5;
                                database2 = this;
                                database2.mang[k][0] = str39;
                                if (str39.indexOf("loa") > -1) {
                                    database2.mang[k][1] = "lo dau";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.indexOf(str46) > -1) {
                                    database2.mang[k][1] = str46;
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.contains("dea")) {
                                    database2.mang[k][1] = "de dau db";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.indexOf("deb") > -1) {
                                    database2.mang[k][1] = "de dit db";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.contains("det")) {
                                    database2.mang[k][1] = "de 8";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.contains("hc")) {
                                    database2.mang[k][1] = "hai cua";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.contains(so_tin3)) {
                                    database2.mang[k][1] = so_tin3;
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.contains("dec")) {
                                    database2.mang[k][1] = "de dau nhat";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.contains("ded")) {
                                    database2.mang[k][1] = "de dit nhat";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.indexOf("de ") > -1) {
                                    database2.mang[k][1] = "de dit db";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.indexOf("bca") > -1) {
                                    database2.mang[k][1] = "bc dau";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.indexOf("xia") > -1) {
                                    database2.mang[k][1] = "xien dau";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.indexOf("xi") > -1) {
                                    database2.mang[k][1] = "xi";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else if (str39.indexOf("xqa") > -1) {
                                    database2.mang[k][1] = "xqa";
                                    i32 = i33;
                                    str41 = str29;
                                    k2 = k4;
                                } else {
                                    str41 = str29;
                                    if (str39.indexOf(str41) > -1) {
                                        database2.mang[k][1] = str41;
                                        k2 = k4;
                                        i32 = i33;
                                    } else {
                                        k2 = k4;
                                        i32 = i33;
                                        database2.mang[k][1] = database2.mang[k - 1][1];
                                    }
                                }
                                if (str39.contains(" x ")) {
                                    if (str39.trim().indexOf("x ") < 2) {
                                        str32 = so_tin3;
                                        soxien2 = str47;
                                        str31 = str44;
                                        str37 = str51;
                                        str38 = str52;
                                        str44 = dayso;
                                        str30 = str28;
                                        str36 = str8;
                                        dayso3 = str39;
                                        str35 = str41;
                                    } else {
                                        database2.mang[k][2] = str39.substring(0, str39.indexOf(" x ")).trim();
                                        database2.mang[k][3] = str39.substring(str39.indexOf(" x "));
                                        if (k <= 1) {
                                            str32 = so_tin3;
                                        } else if (!database2.mang[k - 1][2].contains("xi 2 ") && !database2.mang[k - 1][2].contains("xi 3 ") && !database2.mang[k - 1][2].contains("xi 4 ")) {
                                            str32 = so_tin3;
                                        } else if (!database2.mang[k][1].contains(str47) || database2.mang[k][2].contains(str47)) {
                                            str32 = so_tin3;
                                        } else {
                                            String[] strArr4 = database2.mang[k];
                                            StringBuilder sb4 = new StringBuilder();
                                            str32 = so_tin3;
                                            sb4.append(database2.mang[k - 1][2].substring(0, 5));
                                            sb4.append(database2.mang[k][2]);
                                            strArr4[2] = sb4.toString();
                                        }
                                        database2.XulyMang(k);
                                        database2.BaoLoiTien(k);
                                        if (database2.mang[k][4] != null) {
                                            String ketquaDaySo = database2.mang[k][4].trim();
                                            if (ketquaDaySo.contains("Không hiểu")) {
                                                Dem_error++;
                                            }else {
                                                if (ketquaDaySo.charAt(ketquaDaySo.length() - 1) == ',') {
                                                    database2.mang[k][4] = ketquaDaySo.substring(0, ketquaDaySo.length() - 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                theodoi4 = theodoi3;
                                theodoi5 = str6;
                                str34 = str_Err5;
                            }
                        } else {
                            str33 = str57;
                            theodoi4 = theodoi3;
                            theodoi5 = str6;
                            str34 = str_Err5;
                        }
                        str39 = dayso5;
                        database2 = this;
                        database2.mang[k][0] = str39;
                        rw = k;
                        k4 = k2;
                    }
                    k = rw + 1;
                }
                database = database3;
                TinGoc = so_tin3;
                soxien = str4;
                str13 = str47;
                str10 = str51;
                str11 = str52;
                str_Err4 = str6;
                str9 = str8;
                TinGoc2 = str7;
                if (theodoi7.length() > 0) {
                    str14 = str44;
                    theodoi2 = theodoi7.replaceAll(str14, str_Err4).replaceAll("\\.", str_Err4).replaceAll(theodoi6, str_Err4).replaceAll(";", str_Err4);
                    if (theodoi2.length() > 0) {
                        String[][] strArr10 = database.mang;
                        strArr10[rw + 1][0] = theodoi7;
                        strArr10[rw + 1][2] = theodoi7;
                        strArr10[rw + 1][3] = theodoi7;
                        strArr10[rw + 1][4] = str54 + theodoi7;
                        database.BaoLoiDan(rw + 1);
                    }
                } else {
                    theodoi2 = theodoi7;
                    str14 = str44;
                }
                str12 = soxien4;
                str_Err3 = str_Err2;
                theodoi = str57;
            } else {
                database = database3;
                TinGoc = so_tin3;
                soxien = str4;
                str13 = str47;
                str14 = str44;
                str10 = str51;
                str11 = str52;
                str_Err4 = str6;
                str9 = str8;
                TinGoc2 = str7;
                String[][] strArr11 = database.mang;
                strArr11[1][0] = str5;
                strArr11[1][4] = str_Err2;
                str_Err3 = str_Err2;
                if (str_Err3.indexOf("Không hiểu dạng") > -1) {
                    String[] strArr12 = database.mang[1];
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(str54);
                    str27 = str5;
                    sb10.append(str27.substring(0, 5));
                    strArr12[4] = sb10.toString();
                } else {
                    str27 = str5;
                }
                String[][] strArr13 = database.mang;
                strArr13[1][2] = str27;
                strArr13[1][3] = str_Err4;
                database.BaoLoiDan(1);
                theodoi = str27;
                str12 = null;
            }
            TinGoc3 = new StringBuilder();
            TinXuly = "";
            String rWError322 = "";
            JSONObject jsonDan322 = new JSONObject();
            int k322 = 0;
            i2 = 1;
            while (true) {
                if (i2 < 1000) {
                    TinXuly2 = TinXuly;
                    break;
                }
                String[][] strArr14 = database.mang;
                if (strArr14[i2][0] == null) {
                    TinXuly2 = TinXuly;
                    break;
                }
                if (strArr14[i2][4].indexOf(soxien) > -1 || database.mang[i2][5].indexOf(soxien) > -1) {
                    int Dem_error2 = Dem_error + 1;
                    if (database.mang[i2][4].indexOf(soxien) > -1) {
                        String[][] strArr15 = database.mang;
                        rWError2 = strArr15[i2][4];
                        KhongHieu = strArr15[i2][4].replaceAll(soxien, str_Err4).trim();
                    } else {
                        String[][] strArr16 = database.mang;
                        rWError2 = strArr16[i2][5];
                        KhongHieu = strArr16[i2][5].replaceAll(soxien, str_Err4).trim();
                    }
                    if (database.mang[i2][0].indexOf(str11) == -1) {
                        String[][] strArr17 = database.mang;
                        c = 0;
                        str26 = soxien;
                        strArr17[i2][0] = strArr17[i2][0].replaceAll(KhongHieu, str11 + KhongHieu + str10);
                    } else {
                        str26 = soxien;
                        c = 0;
                    }
                    rWError322 = rWError2;
                    Dem_error = Dem_error2;
                } else {
                    str26 = soxien;
                    c = 0;
                }
                TinGoc3 = (TinGoc3 == null ? new StringBuilder("null") : TinGoc3).append(database.mang[i2][c]);
                i2++;
                str12 = str12;
                TinXuly = TinXuly;
                str_Err3 = str_Err3;
                soxien = str26;
            }
            if (Dem_error == 0) {
                boolean lo2 = false;
                boolean xien2 = false;
                boolean nhat2 = false;
                int rw2 = 1;
                String TinXuly4 = TinXuly2;
                while (true) {
                    if (rw2 >= 1000) {
                        i3 = i2;
                        str15 = str_Err4;
                        TinGoc4 = (TinGoc3 == null ? null : TinGoc3.toString());
                        rWError = rWError322;
                        jsonDan = jsonDan322;
                        i4 = 1;
                        break;
                    } else if (database.mang[rw2][0] == null) {
                        i3 = i2;
                        str15 = str_Err4;
                        TinGoc4 = (TinGoc3 == null ? null : TinGoc3.toString());
                        rWError = rWError322;
                        jsonDan = jsonDan322;
                        i4 = 1;
                        break;
                    } else {
                        JSONObject json_ct = new JSONObject();
                        json_ct.put("du_lieu", database.mang[rw2][0]);
                        json_ct.put("the_loai", database.mang[rw2][1]);
                        json_ct.put("dan_so", database.mang[rw2][4]);
                        json_ct.put("so_tien", database.mang[rw2][5]);
                        if (database.mang[rw2][1].indexOf(str46) > -1) {
                            lo2 = true;
                            str16 = str46;
                            str17 = TinGoc2;
                            TinGoc5 = (TinGoc3 == null ? null : TinGoc3.toString());
                        } else {
                            if (!database.mang[rw2][1].contains(str13)) {
                                str16 = str46;
                                str17 = TinGoc2;
                                if (!database.mang[rw2][1].contains(str17)) {
                                    TinGoc5 = (TinGoc3 == null ? null : TinGoc3.toString());
                                    if (database.mang[rw2][1].indexOf(TinGoc) <= -1) {
                                        TinGoc = TinGoc;
                                        if (!database.mang[rw2][1].contains("xg")) {
                                            if (database.mang[rw2][1].contains("de dau nhat") || database.mang[rw2][1].contains("de dit nhat") || database.mang[rw2][1].indexOf("hai cua") > -1) {
                                                nhat2 = true;
                                            }
                                        }
                                    } else {
                                        TinGoc = TinGoc;
                                    }
                                    xien2 = true;
                                }
                            } else {
                                str16 = str46;
                                str17 = TinGoc2;
                            }
                            TinGoc5 = (TinGoc3 == null ? null : TinGoc3.toString());
                            xien2 = true;
                        }
                        if (quagio) {
                            if (type_kh == 1) {
                                lo = lo2;
                                if (database.mang[rw2][1].contains("de dit db") || database.mang[rw2][1].contains("de dau db")) {
                                    xien = xien2;
                                    str25 = str48;
                                    str24 = str9;
                                    nhat = nhat2;
                                    jsonDan2 = jsonDan322;
                                } else {
                                    str24 = str9;
                                    if (!database.mang[rw2][1].contains(str24)) {
                                        xien = xien2;
                                        if (database.mang[rw2][1].contains("de 8")) {
                                            nhat = nhat2;
                                            jsonDan2 = jsonDan322;
                                            str25 = str48;
                                        } else {
                                            if (database.mang[rw2][1].indexOf("hai cua") > -1) {
                                                String TinXuly5 = TinXuly4 + "de dit db:" + database.mang[rw2][4].trim() + "x" + database.mang[rw2][5] + str48;
                                                json_ct.put("the_loai", "de dit db");
                                                String[] So = database.mang[rw2][4].split(theodoi6);
                                                StringBuilder sb11 = new StringBuilder();
                                                nhat = nhat2;
                                                sb11.append(So.length);
                                                sb11.append(" số.");
                                                json_ct.put("so_luong", sb11.toString());
                                                jsonDan2 = jsonDan322;
                                                jsonDan2.put(String.valueOf(k322), json_ct);
                                                str19 = theodoi6;
                                                str18 = str48;
                                                str20 = str24;
                                                i2 = i2;
                                                TinXuly4 = TinXuly5;
                                                TinXuly3 = str_Err4;
                                                str21 = str14;
                                            } else {
                                                nhat = nhat2;
                                                jsonDan2 = jsonDan322;
                                                TinXuly3 = str_Err4;
                                                str19 = theodoi6;
                                                str18 = str48;
                                                str20 = str24;
                                                i2 = i2;
                                                str21 = str14;
                                            }
                                            k322++;
                                            rw2++;
                                            str14 = str21;
                                            str_Err4 = TinXuly3;
                                            TinGoc3 = TinGoc5 == null ? null : new StringBuilder(TinGoc5);
                                            lo2 = lo;
                                            xien2 = xien;
                                            theodoi6 = str19;
                                            str48 = str18;
                                            TinGoc2 = str17;
                                            str46 = str16;
                                            jsonDan322 = jsonDan2;
                                            nhat2 = nhat;
                                            str9 = str20;
                                        }
                                    } else {
                                        xien = xien2;
                                        nhat = nhat2;
                                        jsonDan2 = jsonDan322;
                                        str25 = str48;
                                    }
                                }
                                StringBuilder sb12 = new StringBuilder();
                                sb12.append(TinXuly4);
                                str20 = str24;
                                sb12.append(database.mang[rw2][1]);
                                sb12.append(":");
                                sb12.append(database.mang[rw2][4].trim());
                                sb12.append("x");
                                sb12.append(database.mang[rw2][5]);
                                sb12.append(str25);
                                String TinXuly6 = sb12.toString();
                                String[] So2 = database.mang[rw2][4].split(theodoi6);
                                json_ct.put("so_luong", So2.length + " số.");
                                jsonDan2.put(String.valueOf(k322), json_ct);
                                str19 = theodoi6;
                                str18 = str25;
                                i2 = i2;
                                TinXuly4 = TinXuly6;
                                TinXuly3 = str_Err4;
                                str21 = str14;
                                k322++;
                                rw2++;
                                str14 = str21;
                                str_Err4 = TinXuly3;
                                TinGoc3 = TinGoc5 == null ? null : new StringBuilder(TinGoc5);
                                lo2 = lo;
                                xien2 = xien;
                                theodoi6 = str19;
                                str48 = str18;
                                TinGoc2 = str17;
                                str46 = str16;
                                jsonDan322 = jsonDan2;
                                nhat2 = nhat;
                                str9 = str20;
                            }
                        }
                        lo = lo2;
                        xien = xien2;
                        String str65 = str48;
                        nhat = nhat2;
                        jsonDan2 = jsonDan322;
                        str20 = str9;
                        String[][] strArr18 = database.mang;
                        TinXuly3 = str_Err4;
                        if (strArr18[rw2][1].equals("hai cua")) {
                            String TinXuly7 = TinXuly4 + "de dit db:" + database.mang[rw2][4].trim() + "x" + database.mang[rw2][5] + str65;
                            String[] So3 = database.mang[rw2][4].split(theodoi6);
                            JSONObject json_hc = new JSONObject();
                            json_hc.put("du_lieu", database.mang[rw2][0].replaceFirst("hc", "de"));
                            json_hc.put("the_loai", "de dit db");
                            json_hc.put("dan_so", database.mang[rw2][4]);
                            json_hc.put("so_tien", database.mang[rw2][5]);
                            json_hc.put("so_luong", So3.length + " số.");
                            jsonDan2.put(String.valueOf(k322), json_hc);
                            k322++;
                            json_ct.put("du_lieu", database.mang[rw2][0].replaceFirst("hc", "nhat"));
                            json_ct.put("the_loai", "de dit nhat");
                            json_ct.put("so_luong", So3.length + " số.");
                            jsonDan2.put(String.valueOf(k322), json_ct);
                            TinXuly4 = TinXuly7 + "de dit nhat:" + database.mang[rw2][4].trim() + "x" + database.mang[rw2][5] + str65;
                            str19 = theodoi6;
                            str18 = str65;
                            i2 = i2;
                            str21 = str14;
                        } else if (strArr18[rw2][1].contains(str13) || database.mang[rw2][1].contains(str17) || database.mang[rw2][1].contains("xg")) {
                            str21 = str14;
                            String[] mArr = database.mang[rw2][4].split(str21);
                            int i10 = 0;
                            while (i10 < mArr.length) {
                                TinXuly4 = TinXuly4 + database.mang[rw2][1] + ":" + mArr[i10] + "x" + database.mang[rw2][5] + str65;
                                if (database.mang[rw2][1].contains(str17)) {
                                    String[] XienQuay = database.xuly_Xq(mArr[i10]).split(str21);
                                    JSONObject json_xq = new JSONObject();
                                    str23 = theodoi6;
                                    StringBuilder sb13 = new StringBuilder();
                                    str22 = str65;
                                    sb13.append(database.mang[rw2][1]);
                                    sb13.append(":");
                                    sb13.append(mArr[i10]);
                                    sb13.append("x");
                                    sb13.append(database.mang[rw2][5]);
                                    json_xq.put("du_lieu", sb13.toString());
                                    if (database.mang[rw2][1].indexOf("xq dau") > -1) {
                                        json_xq.put("the_loai", "xien dau");
                                    } else {
                                        json_xq.put("the_loai", str13);
                                    }
                                    json_xq.put("dan_so", database.xuly_Xq(mArr[i10]));
                                    json_xq.put("so_tien", database.mang[rw2][5]);
                                    json_xq.put("so_luong", XienQuay.length + " cặp.");
                                    jsonDan2.put(String.valueOf(k322), json_xq);
                                    k322++;
                                } else {
                                    str23 = theodoi6;
                                    str22 = str65;
                                    if (database.mang[rw2][1].indexOf("xg") > -1) {
                                        String[] So4 = database.mang[rw2][4].split(str21);
                                        json_ct.put("so_luong", So4.length + " cặp.");
                                        jsonDan2.put(String.valueOf(k322), json_ct);
                                    } else {
                                        String[] So5 = database.mang[rw2][4].split(str21);
                                        json_ct.put("so_luong", So5.length + " cặp.");
                                        jsonDan2.put(String.valueOf(k322), json_ct);
                                    }
                                }
                                i10++;
                                theodoi6 = str23;
                                str65 = str22;
                            }
                            str19 = theodoi6;
                            str18 = str65;
                            i2 = i10;
                        } else {
                            String[] So6 = database.mang[rw2][4].split(theodoi6);
                            json_ct.put("so_luong", So6.length + " số.");
                            jsonDan2.put(String.valueOf(k322), json_ct);
                            TinXuly4 = TinXuly4 + database.mang[rw2][1] + ":" + database.mang[rw2][4].trim() + "x" + database.mang[rw2][5] + str65;
                            str19 = theodoi6;
                            str18 = str65;
                            i2 = i2;
                            str21 = str14;
                        }
                        k322++;
                        rw2++;
                        str14 = str21;
                        str_Err4 = TinXuly3;
                        TinGoc3 = TinGoc5 == null ? null : new StringBuilder(TinGoc5);
                        lo2 = lo;
                        xien2 = xien;
                        theodoi6 = str19;
                        str48 = str18;
                        TinGoc2 = str17;
                        str46 = str16;
                        jsonDan322 = jsonDan2;
                        nhat2 = nhat;
                        str9 = str20;
                    }
                }
                if (quagio) {
                    if (type_kh == i4) {
                        if (lo2 || xien2 || nhat2) {
                            if (lo2) {
                                Bor = "Bỏ " + "lô,";
                            } else {
                                Bor = "Bỏ ";
                            }
                            if (xien2) {
                                Bor = Bor + "xiên,";
                            }
                            if (nhat2) {
                                Bor = Bor + "giải nhất";
                            }
                            TinXuly4 = Bor + " vì quá giờ!\n" + TinXuly4;
                            if (TinXuly4 != null) {
                                TinXuly4 = TinXuly4.replaceAll("xg 2:", "xi:").replaceAll("xg 3:", "xi:").replaceAll("xg 4:", "xi:");
                            }
                            if (so_tin2 != str15) {
                                database.QueryData("Update tbl_tinnhanS set so_tin_nhan = " + soTN + ",  nd_phantich='" + TinXuly4 + "', phan_tich = '" + jsonDan.toString() + "', phat_hien_loi ='ok' Where id =" + id);
                            } else {
                                database.QueryData("Update tbl_tinnhanS set nd_phantich='" + TinXuly4 + "', phan_tich = '" + jsonDan.toString() + "', phat_hien_loi ='ok' Where id =" + id);
                            }
                        } else {
                            Bor2 = "Bỏ ";
                            Bor = Bor2;
                        }
                    }
                }
                Bor2 = "Bỏ ";
                Bor = Bor2;
                if (TinXuly4 != null) {
                    database.QueryData("Update tbl_tinnhanS set so_tin_nhan = " + soTN + ",  nd_phantich='" + TinXuly4 + "', phan_tich = '" + jsonDan.toString() + "', phat_hien_loi ='ok' Where id =" + id);
                }
            } else if (so_tin2.equals(str_Err4)) {
                database.QueryData("Update tbl_tinnhanS set nd_phantich='" + TinGoc3 + "', phat_hien_loi = '" + rWError322 + "'  Where id =" + id);
            } else {
                database.QueryData("Update tbl_tinnhanS set so_tin_nhan = " + so_tin2 + ", nd_phantich='" + TinGoc3 + "', phat_hien_loi = '" + rWError322 + "'  Where id =" + id);
            }
            if (getThongtin != null && !getThongtin.isClosed()) {
                getThongtin.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                return;
            }
        }
    }

    /* JADX INFO: Multiple debug info for r2v7 int: [D('sql1' java.lang.String), D('soTN' int)] */
    public void TralaiSO(int ID) {
        String Tralai = "";
        this.json_Tralai = new JSONObject();
        Cursor cursor = GetData("Select * from tbl_tinnhanS where id = " + ID);
        cursor.moveToFirst();
        try {
            Cursor Thongtin = GetData("Select * From tbl_kh_new Where ten_kh = '" + cursor.getString(4) + "'");
            if (Thongtin.getCount() > 0 && Thongtin.moveToFirst()) {
                JSONObject jSONObject = new JSONObject(Thongtin.getString(6));
                this.json = jSONObject;
                if (jSONObject.getString("danDe").length() > 0) {
                    String TralaiDe = TraDe(Thongtin.getString(0), this.json.getString("soDe"));
                    if (TralaiDe.length() > 0) {
                        Tralai = Tralai + "\n" + TralaiDe;
                    }
                }
                if (this.json.getString("danLo").length() > 0) {
                    String TralaiLo = TraLo(Thongtin.getString(0), this.json.getString("soLo"));
                    if (TralaiLo.length() > 0) {
                        Tralai = Tralai + "\n" + TralaiLo;
                    }
                }
                if (this.json.getInt("xien2") > 0 || this.json.getInt("xien3") > 0 || this.json.getInt("xien4") > 0) {
                    JSONObject JsonXien = new JSONObject();
                    JsonXien.put("xien2", this.json.getInt("xien2"));
                    JsonXien.put("xien3", this.json.getInt("xien3"));
                    JsonXien.put("xien4", this.json.getInt("xien4"));
                    String TralaiXi = TraXi(Thongtin.getString(0), JsonXien.toString());
                    if (TralaiXi.length() > 0) {
                        Tralai = Tralai + "\n" + TralaiXi;
                    }
                }
                if (this.json.getInt("cang") > 0) {
                    String TralaiCang = TraCang(Thongtin.getString(0), this.json.getInt("cang"));
                    if (TralaiCang.length() > 0) {
                        Tralai = Tralai + "\n" + TralaiCang;
                    }
                }
                if (this.json_Tralai.length() > 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                    dmyFormat.setTimeZone(TimeZone.getDefault());
                    hourFormat.setTimeZone(TimeZone.getDefault());
                    String mNgayNhan = dmyFormat.format(calendar.getTime());
                    String mGionhan = hourFormat.format(calendar.getTime());
                    Cursor getSoTN = GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mNgayNhan + "' AND ten_kh = '" + Thongtin.getString(0) + "' and type_kh = 2");
                    getSoTN.moveToFirst();
                    int soTN = getSoTN.getInt(0) + 1;
                    final String Tralai2 = "Tra lai " + soTN + ":" + Tralai;
                    QueryData("Insert Into tbl_tinnhanS values (null, '" + mNgayNhan + "', '" + mGionhan + "',2, '" + Thongtin.getString(0) + "', '" + Thongtin.getString(1) + "','" + Thongtin.getString(2) + "', " + soTN + ", '" + Tralai2.trim() + "','" + Tralai2.substring(Tralai2.indexOf(":") + 1) + "','" + Tralai2.substring(Tralai2.indexOf(":") + 1) + "', 'ko',0,0,0, '" + this.json_Tralai.toString() + "')");
                    StringBuilder sb = new StringBuilder();
                    sb.append("Select id From tbl_tinnhanS where ngay_nhan = '");
                    sb.append(mNgayNhan);
                    sb.append("' AND type_kh = 2 AND ten_kh ='");
                    sb.append(Thongtin.getString(0));
                    sb.append("' AND nd_goc = '");
                    sb.append(Tralai2.trim());
                    sb.append("'");
                    Cursor ccc = GetData(sb.toString());
                    ccc.moveToFirst();
                    Update_TinNhanGoc(ccc.getInt(0), 2);
                    if (Thongtin.getString(2).indexOf("TL") > -1) {
                        final Long TralaiID = Long.valueOf(Thongtin.getLong(1));
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            /* class tamhoang.ldpro4.data.Database.AnonymousClass1 */

                            public void run() {
                                new MainActivity();
                                MainActivity.sendMessage(TralaiID.longValue(), Tralai2);
                            }
                        });
                    } else if (Thongtin.getString(2).indexOf("sms") > -1) {
                        SendSMS(Thongtin.getString(1), Tralai2);
                    } else {
                        JSONObject jsonObject = new JSONObject(MainActivity.json_Tinnhan.getString(Thongtin.getString(1)));
                        if (jsonObject.getInt("Time") > 3) {
                            new NotificationReader().NotificationWearReader(Thongtin.getString(1), Tralai2);
                        } else {
                            jsonObject.put(Tralai2, "OK");
                            MainActivity.json_Tinnhan.put(Thongtin.getString(1), jsonObject);
                        }
                    }
                    NhapSoChiTiet(ccc.getInt(0));
                    ccc.close();
                }
            }
            Thongtin.close();
        } catch (Exception e) {
            e.getMessage();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        cursor.close();
    }

    /* JADX INFO: Multiple debug info for r9v9 'Str'  java.lang.String: [D('Str' java.lang.String), D('i' int)] */
    public String TraDe(String TenKH, String DanDe) {
        JSONException e;
        String Str1;
        Cursor cursor;
        JSONObject json_DeKhong;
        String Str;
        int i;
        String str;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        hourFormat.setTimeZone(TimeZone.getDefault());
        String mDate = dmyFormat.format(calendar.getTime());
        String Str2 = "";
        JSONObject jSon_Deb = new JSONObject();
        JSONObject jsonSoCt = new JSONObject();
        String TheLoai = "De";
        String DangLoc = "deb";
        String LoaiDe = "de dit db";
        try {
            if (this.caidat_tg.getInt("khach_de") == 1) {
                TheLoai = "Det";
                DangLoc = "det";
                LoaiDe = "de 8";
            }
        } catch (JSONException e2) {
        }
        try {
            JSONObject json_DeKhong2 = new JSONObject(DanDe);
            StringBuilder sb = new StringBuilder();
            sb.append("Select the_loai, so_chon, Sum(diem_ton*(type_kh = 1)) as Nhan, Sum(diem_ton *(type_kh = 2)) As Tra \nFROM tbl_soctS Where ten_kh = '");
            sb.append(TenKH);
            sb.append("' AND ngay_nhan = '");
            sb.append(mDate);
            sb.append("' AND the_loai = '");
            sb.append(DangLoc);
            sb.append("' Group by so_chon Order by so_chon");
            String Str12 = sb.toString();
            Cursor cursor2 = GetData(Str12);
            while (true) {
                Str1 = "So_chon";
                if (!cursor2.moveToNext()) {
                    break;
                }
                if (json_DeKhong2.has(cursor2.getString(1))) {
                    try {
                        jsonSoCt.put(Str1, cursor2.getString(1));
                        jsonSoCt.put("Da_nhan", cursor2.getInt(2));
                        jsonSoCt.put("Da_tra", cursor2.getInt(3));
                        jsonSoCt.put("Khong_Tien", json_DeKhong2.getInt(cursor2.getString(1)));
                        jsonSoCt.put("Se_tra", (jsonSoCt.getInt("Da_nhan") - jsonSoCt.getInt("Da_tra")) - jsonSoCt.getInt("Khong_Tien"));
                        if (jsonSoCt.getInt("Se_tra") > 0) {
                            jSon_Deb.put(cursor2.getString(1), jsonSoCt.toString());
                            Str2 = Str2;
                            Str12 = Str12;
                            dmyFormat = dmyFormat;
                            json_DeKhong2 = json_DeKhong2;
                            mDate = mDate;
                            cursor2 = cursor2;
                            hourFormat = hourFormat;
                        } else {
                            Str2 = Str2;
                            Str12 = Str12;
                            dmyFormat = dmyFormat;
                            json_DeKhong2 = json_DeKhong2;
                            mDate = mDate;
                            cursor2 = cursor2;
                            hourFormat = hourFormat;
                        }
                    } catch (JSONException e3) {
                        e = e3;
                        Str2 = Str2;
                        e.printStackTrace();
                        return Str2;
                    }
                } else {
                    Str12 = Str12;
                    dmyFormat = dmyFormat;
                    json_DeKhong2 = json_DeKhong2;
                    mDate = mDate;
                    cursor2 = cursor2;
                    hourFormat = hourFormat;
                }
            }
            cursor = cursor2;
            json_DeKhong = json_DeKhong2;
            if (jSon_Deb.length() <= 0) {
                return Str2;
            }
            Iterator<String> iter = jSon_Deb.keys();
            List<JSONObject> jsonValues = new ArrayList<>();
            while (iter.hasNext()) {
                try {
                    jsonSoCt = new JSONObject(jSon_Deb.getString(iter.next()));
                    jsonValues.add(jsonSoCt);
                } catch (JSONException e6) {
                    e6.printStackTrace();
                }
            }
            /* class tamhoang.ldpro4.data.Database.AnonymousClass2 */
            Collections.sort(jsonValues, (a, b) -> {
                int valA = 0;
                Integer valB = 0;
                try {
                    valA = Integer.valueOf(a.getInt("Se_tra"));
                    valB = Integer.valueOf(b.getInt("Se_tra"));
                } catch (JSONException e1) {
                }
                return valB.compareTo(valA);
            });
            int tien = 0;
            String Str3 = "";
            int i2 = 0;
            String Str111 = Str2;
            while (i2 < jsonValues.size()) {
                try {
                    try {
                        JSONObject soCT = jsonValues.get(i2);
                        if (tien > soCT.getInt("Se_tra")) {
                            JSONObject json_Tra = new JSONObject();
                            String[] sss = Str3.split(",");
                            i = i2;
                            json_Tra.put("du_lieu", Str3 + "x" + tien + "n");
                            json_Tra.put("the_loai", LoaiDe);
                            json_Tra.put("dan_so", Str3);
                            json_Tra.put("so_tien", tien);
                            json_Tra.put("so_luong", sss.length);
                            this.json_Tralai.put(String.valueOf(this.json_Tralai.length() + 1), json_Tra.toString());
                            String Str1112 = Str3 + "x" + tien + "n ";
                            StringBuilder sb2 = new StringBuilder();
                            Str2 = Str111;
                            sb2.append(Str2);
                            sb2.append(Str1112);
                            String Str4 = sb2.toString();
                            try {
                                StringBuilder sb3 = new StringBuilder();
                                str = Str1;
                                sb3.append(soCT.getString(str));
                                sb3.append(",");
                                String Str1113 = sb3.toString();
                                tien = soCT.getInt("Se_tra");
                                Str3 = Str1113;
                                Str111 = Str4;
                            } catch (JSONException e8) {
                                e = e8;
                                Str2 = Str4;
                                e.printStackTrace();
                                return Str2;
                            }
                        } else {
                            i = i2;
                            str = Str1;
                            tien = soCT.getInt("Se_tra");
                            Str3 = Str3 + soCT.getString(str) + ",";
                            Str111 = Str111;
                        }
                        i2 = i + 1;
                        Str1 = str;
                        cursor = cursor;
                        json_DeKhong = json_DeKhong;
                        jSon_Deb = jSon_Deb;
                        jsonSoCt = jsonSoCt;
                        DangLoc = DangLoc;
                        TheLoai = TheLoai;
                        jsonValues = jsonValues;
                    } catch (JSONException e10) {
                        e = e10;
                        Str2 = Str111;
                        e.printStackTrace();
                        return Str2;
                    }
                } catch (Exception e11) {
                    Str2 = Str111;
                    e11.printStackTrace();
                    return Str2;
                }
            }
            Str2 = Str111;
            try {
                if (Str3.length() > 0) {
                    JSONObject json_Tra2 = new JSONObject();
                    String[] sss2 = Str3.split(",");
                    json_Tra2.put("du_lieu", Str3 + "x" + tien + "n");
                    json_Tra2.put("the_loai", LoaiDe);
                    json_Tra2.put("dan_so", Str3);
                    json_Tra2.put("so_tien", tien);
                    json_Tra2.put("so_luong", sss2.length);
                    if (tien > 0) {
                        this.json_Tralai.put(String.valueOf(this.json_Tralai.length() + 1), json_Tra2.toString());
                        Str = Str2 + Str3 + "x" + tien + "n ";
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(TheLoai);
                        sb4.append(": ");
                        sb4.append(Str);
                        return sb4.toString();
                    }
                }
                Str = Str2;
                StringBuilder sb42 = new StringBuilder();
                sb42.append(TheLoai);
                sb42.append(": ");
                sb42.append(Str);
                return sb42.toString();
            } catch (JSONException e13) {
                e = e13;
                e.printStackTrace();
                return Str2;
            }
        } catch (JSONException e16) {
            e = e16;
            e.printStackTrace();
            return Str2;
        }
    }

    public String TraLo(String TenKH, String DanLo) {
        JSONException e;
        String str;
        String Str;
        int i;
        String str2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        hourFormat.setTimeZone(TimeZone.getDefault());
        String mDate = dmyFormat.format(calendar.getTime());
        String Str2 = "";
        JSONObject jSon_Deb = new JSONObject();
        JSONObject jsonSoCt = new JSONObject();
        try {
            JSONObject json_LoKhong = new JSONObject(DanLo);
            String Str1 = "Select the_loai, so_chon, Sum(diem_ton *(type_kh = 1)) as Nhan, Sum(diem_ton *(type_kh = 2)) As Tra \nFROM tbl_soctS Where ten_kh = '" + TenKH + "' AND ngay_nhan = '" + mDate + "' AND the_loai = 'lo' Group by so_chon Order by so_chon";
            Cursor cursor = GetData(Str1);
            while (true) {
                str = "So_chon";
                if (!cursor.moveToNext()) {
                    break;
                }
                try {
                    if (json_LoKhong.has(cursor.getString(1))) {
                        jsonSoCt.put(str, cursor.getString(1));
                        jsonSoCt.put("Da_nhan", cursor.getInt(2));
                        jsonSoCt.put("Da_tra", cursor.getInt(3));
                        jsonSoCt.put("Khong_Tien", json_LoKhong.getInt(cursor.getString(1)));
                        jsonSoCt.put("Se_tra", (jsonSoCt.getInt("Da_nhan") - jsonSoCt.getInt("Da_tra")) - jsonSoCt.getInt("Khong_Tien"));
                        if (jsonSoCt.getInt("Se_tra") > 0) {
                            jSon_Deb.put(cursor.getString(1), jsonSoCt.toString());
                            mDate = mDate;
                            calendar = calendar;
                            dmyFormat = dmyFormat;
                            hourFormat = hourFormat;
                        } else {
                            mDate = mDate;
                            calendar = calendar;
                            dmyFormat = dmyFormat;
                            hourFormat = hourFormat;
                        }
                    } else {
                        mDate = mDate;
                        calendar = calendar;
                        dmyFormat = dmyFormat;
                        hourFormat = hourFormat;
                    }
                } catch (JSONException e2) {
                    e = e2;
                    e.printStackTrace();
                    return Str2;
                }
            }
            if (jSon_Deb.length() <= 0) {
                return Str2;
            }
            Iterator<String> iter = jSon_Deb.keys();
            List<JSONObject> jsonValues = new ArrayList<>();
            while (iter.hasNext()) {
                try {
                    jsonSoCt = new JSONObject(jSon_Deb.getString(iter.next()));
                    jsonValues.add(jsonSoCt);
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            }
            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                /* class tamhoang.ldpro4.data.Database.AnonymousClass3 */

                public int compare(JSONObject a, JSONObject b) {
                    int valA = 0;
                    Integer valB = 0;
                    try {
                        valA = Integer.valueOf(a.getInt("Se_tra"));
                        valB = Integer.valueOf(b.getInt("Se_tra"));
                    } catch (JSONException e) {
                    }
                    return valB.compareTo(valA);
                }
            });
            int tien = 0;
            String Str111 = "";
            int i2 = 0;
            while (i2 < jsonValues.size()) {
                try {
                    JSONObject soCT = jsonValues.get(i2);
                    if (tien > soCT.getInt("Se_tra")) {
                        JSONObject json_Tra = new JSONObject();
                        String[] sss = Str111.split(",");
                        i = i2;
                        json_Tra.put("du_lieu", Str111 + "x" + tien + "d");
                        json_Tra.put("the_loai", "lo");
                        json_Tra.put("dan_so", Str111);
                        json_Tra.put("so_tien", tien);
                        json_Tra.put("so_luong", sss.length);
                        this.json_Tralai.put(String.valueOf(this.json_Tralai.length() + 1), json_Tra.toString());
                        String Str1112 = Str111 + "x" + tien + "d ";
                        StringBuilder sb = new StringBuilder();
                        Str2 = Str2;
                        sb.append(Str2);
                        sb.append(Str1112);
                        String Str3 = sb.toString();
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            str2 = str;
                            sb2.append(soCT.getString(str2));
                            sb2.append(",");
                            Str111 = sb2.toString();
                            tien = soCT.getInt("Se_tra");
                            Str2 = Str3;
                        } catch (JSONException e5) {
                            e = e5;
                            Str2 = Str3;
                            e.printStackTrace();
                            return Str2;
                        }
                    } else {
                        i = i2;
                        str2 = str;
                        Str2 = Str2;
                        tien = soCT.getInt("Se_tra");
                        Str111 = Str111 + soCT.getString(str2) + ",";
                    }
                    i2 = i + 1;
                    str = str2;
                    iter = iter;
                    jSon_Deb = jSon_Deb;
                    jsonSoCt = jsonSoCt;
                    json_LoKhong = json_LoKhong;
                    Str1 = Str1;
                    cursor = cursor;
                    jsonValues = jsonValues;
                } catch (JSONException e7) {
                    return Str2;
                }
            }
            try {
                if (Str111.length() > 0) {
                    JSONObject json_Tra2 = new JSONObject();
                    String[] sss2 = Str111.split(",");
                    json_Tra2.put("du_lieu", Str111 + "x" + tien + "d");
                    json_Tra2.put("the_loai", "lo");
                    json_Tra2.put("dan_so", Str111);
                    json_Tra2.put("so_tien", tien);
                    json_Tra2.put("so_luong", sss2.length);
                    if (tien > 0) {
                        this.json_Tralai.put(String.valueOf(this.json_Tralai.length() + 1), json_Tra2.toString());
                        Str = Str2 + Str111 + "x" + tien + "d ";
                        return "Lo: " + Str;
                    }
                }
                Str = Str2;
                return "Lo: " + Str;
            } catch (JSONException e9) {
                e = e9;
                Str2 = Str2;
                e.printStackTrace();
                return Str2;
            }
        } catch (JSONException e11) {
            e = e11;
            e.printStackTrace();
            return Str2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x012e A[Catch:{ JSONException -> 0x0190 }] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x013a A[SYNTHETIC] */
    public String TraXi(String TenKH, String KhongXien) {
        JSONException e;
        Cursor cursor;
        String Str1;
        String str;
        String Str;
        String Str222 = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        hourFormat.setTimeZone(TimeZone.getDefault());
        String mDate = dmyFormat.format(calendar.getTime());
        String Str2 = "";
        JSONObject jSon_Deb = new JSONObject();
        try {
            JSONObject jsonXien = new JSONObject(KhongXien);
            StringBuilder sb = new StringBuilder();
            sb.append("Select the_loai, so_chon, Sum(diem_ton *(type_kh = 1)) as Nhan, Sum(diem_ton *(type_kh = 2)) As Tra \nFROM tbl_soctS Where ten_kh = '");
            sb.append(TenKH);
            sb.append("' AND ngay_nhan = '");
            sb.append(mDate);
            sb.append("' AND the_loai = 'xi' Group by so_chon");
            String Str12 = sb.toString();
            cursor = GetData(Str12);
            while (true) {
                Str1 = "So_chon";
                if (!cursor.moveToNext()) {
                    break;
                }
                JSONObject jsonSoCt = new JSONObject();
                try {
                    jsonSoCt.put(Str1, cursor.getString(1));
                    jsonSoCt.put("Da_nhan", cursor.getInt(2));
                    jsonSoCt.put("Da_tra", cursor.getInt(3));
                    if (jsonXien.has("xien2")) {
                        try {
                            if (jsonSoCt.getString(Str1).length() == 5) {
                                jsonSoCt.put("Khong_Tien", jsonXien.getInt("xien2"));
                                jsonSoCt.put("Se_tra", (jsonSoCt.getInt("Da_nhan") - jsonSoCt.getInt("Da_tra")) - jsonSoCt.getInt("Khong_Tien"));
                                if (jsonSoCt.getInt("Se_tra") > 0) {
                                    jSon_Deb.put(cursor.getString(1), jsonSoCt.toString());
                                }
                                hourFormat = hourFormat;
                                Str12 = Str12;
                                dmyFormat = dmyFormat;
                                Str2 = Str2;
                                mDate = mDate;
                            }
                        } catch (JSONException e2) {
                            e = e2;
                            Str2 = Str2;
                            e.printStackTrace();
                            return Str2;
                        }
                    }
                    if (!jsonXien.has("xien3") || jsonSoCt.getString(Str1).length() != 8) {
                        if (jsonXien.has("xien4") && jsonSoCt.getString(Str1).length() == 11) {
                            jsonSoCt.put("Khong_Tien", jsonXien.getInt("xien4"));
                            jsonSoCt.put("Se_tra", (jsonSoCt.getInt("Da_nhan") - jsonSoCt.getInt("Da_tra")) - jsonSoCt.getInt("Khong_Tien"));
                        }
                        if (jsonSoCt.getInt("Se_tra") > 0) {
                        }
                        hourFormat = hourFormat;
                        Str12 = Str12;
                        dmyFormat = dmyFormat;
                        Str2 = Str2;
                        mDate = mDate;
                    } else {
                        jsonSoCt.put("Khong_Tien", jsonXien.getInt("xien3"));
                        jsonSoCt.put("Se_tra", (jsonSoCt.getInt("Da_nhan") - jsonSoCt.getInt("Da_tra")) - jsonSoCt.getInt("Khong_Tien"));
                        if (jsonSoCt.getInt("Se_tra") > 0) {
                        }
                        hourFormat = hourFormat;
                        Str12 = Str12;
                        dmyFormat = dmyFormat;
                        Str2 = Str2;
                        mDate = mDate;
                    }
                } catch (JSONException e3) {
                    e = e3;
                    e.printStackTrace();
                    return Str2;
                }
            }
            try {
                if (jSon_Deb.length() <= 0) {
                    return Str2;
                }
                Iterator<String> iter = jSon_Deb.keys();
                List<JSONObject> jsonValues = new ArrayList<>();
                while (iter.hasNext()) {
                    try {
                        jsonValues.add(new JSONObject(jSon_Deb.getString(iter.next())));
                    } catch (JSONException e6) {
                        e6.printStackTrace();
                    }
                }
                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                    /* class tamhoang.ldpro4.data.Database.AnonymousClass4 */

                    public int compare(JSONObject a, JSONObject b) {
                        int valA = 0;
                        Integer valB = 0;
                        try {
                            valA = Integer.valueOf(a.getInt("Se_tra"));
                            valB = Integer.valueOf(b.getInt("Se_tra"));
                        } catch (JSONException e) {
                        }
                        return valB.compareTo(valA);
                    }
                });
                int tien = 0;
                String Str111 = Str222;
                int i = 0;
                Str2 = Str2;
                while (i < jsonValues.size()) {
                    try {
                        JSONObject soCT = jsonValues.get(i);
                        if (tien > soCT.getInt("Se_tra")) {
                            JSONObject json_Tra = new JSONObject();
                            String[] sss = Str111.split(" ");
                            json_Tra.put("du_lieu", Str111 + "x" + tien + "n");
                            json_Tra.put("the_loai", "xi");
                            json_Tra.put("dan_so", Str111);
                            json_Tra.put("so_tien", tien);
                            json_Tra.put("so_luong", sss.length);
                            this.json_Tralai.put(String.valueOf(this.json_Tralai.length() + 1), json_Tra.toString());
                            String Str1112 = Str111 + "x" + tien + "n\n";
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(Str222);
                            str = Str1;
                            sb2.append(soCT.getString(str));
                            sb2.append("x");
                            sb2.append(soCT.getInt("Se_tra"));
                            sb2.append("n\n");
                            Str222 = sb2.toString();
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(Str2);
                            sb3.append(Str1112);
                            Str = sb3.toString();
                            try {
                                Str111 = soCT.getString(str) + " ";
                                tien = soCT.getInt("Se_tra");
                            } catch (JSONException e8) {
                                e = e8;
                                Str2 = Str;
                                e.printStackTrace();
                                return Str2;
                            }
                        } else {
                            str = Str1;
                            tien = soCT.getInt("Se_tra");
                            Str111 = Str111 + soCT.getString(str) + " ";
                            Str222 = Str222 + soCT.getString(str) + "x" + soCT.getInt("Se_tra") + "n\n";
                            Str = Str2;
                        }
                        Str1 = str;
                        jSon_Deb = jSon_Deb;
                        jsonXien = jsonXien;
                        cursor = cursor;
                        jsonValues = jsonValues;
                        Str2 = Str;
                        i++;
                        iter = iter;
                    } catch (JSONException e9) {
                        e = e9;
                        Str2 = Str2;
                        e.printStackTrace();
                        return Str2;
                    }
                }
                if (Str111.length() > 0) {
                    JSONObject json_Tra2 = new JSONObject();
                    String[] sss2 = Str111.split(" ");
                    json_Tra2.put("du_lieu", Str111 + "x" + tien + "n");
                    json_Tra2.put("the_loai", "xi");
                    json_Tra2.put("dan_so", Str111);
                    json_Tra2.put("so_tien", tien);
                    json_Tra2.put("so_luong", sss2.length);
                    if (tien > 0) {
                        this.json_Tralai.put(String.valueOf(this.json_Tralai.length() + 1), json_Tra2.toString());
                        Str2 = Str2 + json_Tra2.getString("du_lieu") + " \n";
                        return "Xien:\n" + Str222 + "\n";
                    }
                }
                Str2 = Str2;
                return "Xien:\n" + Str222 + "\n";
            } catch (JSONException e12) {
                e = e12;
                Str2 = Str2;
                e.printStackTrace();
                return Str2;
            }
        } catch (JSONException e13) {
            e = e13;
            e.printStackTrace();
            return Str2;
        }
    }

    public String TraCang(String TenKH, int maxDang) {
        JSONException e;
        String str;
        String Str;
        int i;
        String str2;
        Iterator<String> iter;
        JSONException e2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        hourFormat.setTimeZone(TimeZone.getDefault());
        String mDate = dmyFormat.format(calendar.getTime());
        String Str2 = "";
        JSONObject jSon_Deb = new JSONObject();
        JSONObject jsonSoCt = new JSONObject();
        String Str1 = "Select the_loai, so_chon, Sum(diem_ton *(type_kh = 1)) as Nhan, Sum(diem_ton *(type_kh = 2)) As Tra \nFROM tbl_soctS Where ten_kh = '" + TenKH + "' AND ngay_nhan = '" + mDate + "' AND the_loai = 'bc' Group by so_chon Order by so_chon";
        Cursor cursor = GetData(Str1);
        while (true) {
            str = "So_chon";
            if (!cursor.moveToNext()) {
                break;
            }
            try {
                jsonSoCt.put(str, cursor.getString(1));
                jsonSoCt.put("Da_nhan", cursor.getInt(2));
                jsonSoCt.put("Da_tra", cursor.getInt(3));
            } catch (JSONException e3) {
                e = e3;
                e.printStackTrace();
                return Str2;
            }
            try {
                jsonSoCt.put("Khong_Tien", maxDang);
                jsonSoCt.put("Se_tra", (jsonSoCt.getInt("Da_nhan") - jsonSoCt.getInt("Da_tra")) - jsonSoCt.getInt("Khong_Tien"));
                if (jsonSoCt.getInt("Se_tra") > 0) {
                    jSon_Deb.put(cursor.getString(1), jsonSoCt.toString());
                    calendar = calendar;
                    dmyFormat = dmyFormat;
                } else {
                    calendar = calendar;
                    dmyFormat = dmyFormat;
                }
            } catch (JSONException e4) {
                e = e4;
                e.printStackTrace();
                return Str2;
            }
        }
        if (jSon_Deb.length() <= 0) {
            return Str2;
        }
        Iterator<String> iter2 = jSon_Deb.keys();
        List<JSONObject> jsonValues = new ArrayList<>();
        while (iter2.hasNext()) {
            iter = iter2;
            try {
                jsonSoCt = new JSONObject(jSon_Deb.getString(iter2.next()));
                jsonValues.add(jsonSoCt);
            } catch (JSONException e5) {
                e2 = e5;
            }
            iter2 = iter;
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            /* class tamhoang.ldpro4.data.Database.AnonymousClass5 */

            public int compare(JSONObject a, JSONObject b) {
                int valA = 0;
                Integer valB = 0;
                try {
                    valA = Integer.valueOf(a.getInt("Se_tra"));
                    valB = Integer.valueOf(b.getInt("Se_tra"));
                } catch (JSONException e) {
                }
                return valB.compareTo(valA);
            }
        });
        int tien = 0;
        String Str111 = "";
        int i2 = 0;
        while (i2 < jsonValues.size()) {
            try {
                JSONObject soCT = jsonValues.get(i2);
                if (tien > soCT.getInt("Se_tra")) {
                    JSONObject json_Tra = new JSONObject();
                    String[] sss = Str111.split(",");
                    i = i2;
                    json_Tra.put("du_lieu", Str111 + "x" + tien + "n");
                    json_Tra.put("the_loai", "bc");
                    json_Tra.put("dan_so", Str111);
                    json_Tra.put("so_tien", tien);
                    json_Tra.put("so_luong", sss.length);
                    this.json_Tralai.put(String.valueOf(this.json_Tralai.length() + 1), json_Tra.toString());
                    String Str1112 = Str111 + "x" + tien + "n ";
                    StringBuilder sb = new StringBuilder();
                    sb.append(Str2);
                    sb.append(Str1112);
                    Str2 = sb.toString();
                    try {
                        StringBuilder sb2 = new StringBuilder();
                        str2 = str;
                        sb2.append(soCT.getString(str2));
                        sb2.append(",");
                        Str111 = sb2.toString();
                        tien = soCT.getInt("Se_tra");
                    } catch (JSONException e9) {
                        e = e9;
                        e.printStackTrace();
                        return Str2;
                    }
                } else {
                    i = i2;
                    str2 = str;
                    Str111 = Str111 + soCT.getString(str2) + ",";
                    tien = soCT.getInt("Se_tra");
                    Str2 = Str2;
                }
                i2 = i + 1;
                str = str2;
                hourFormat = hourFormat;
                mDate = mDate;
                jSon_Deb = jSon_Deb;
                jsonSoCt = jsonSoCt;
                Str1 = Str1;
                cursor = cursor;
                jsonValues = jsonValues;
            } catch (JSONException e10) {
                e = e10;
                Str2 = Str2;
                e.printStackTrace();
                return Str2;
            }
        }
        try {
            if (Str111.length() > 0) {
                JSONObject json_Tra2 = new JSONObject();
                String[] sss2 = Str111.split(",");
                json_Tra2.put("du_lieu", Str111 + "x" + tien + "n");
                json_Tra2.put("the_loai", "bc");
                json_Tra2.put("dan_so", Str111);
                json_Tra2.put("so_tien", tien);
                json_Tra2.put("so_luong", sss2.length);
                if (tien > 0) {
                    this.json_Tralai.put(String.valueOf(this.json_Tralai.length() + 1), json_Tra2.toString());
                    Str = Str2 + Str111 + "x" + tien + "n ";
                    return "Cang: " + Str;
                }
            }
            Str = Str2;
            return "Cang: " + Str;
        } catch (JSONException e12) {
            e = e12;
            Str2 = Str2;
            e.printStackTrace();
            return Str2;
        }
    }


    private void XulyMang(int rw) {
        int i;
        String[] Danxi;
        int i2;
        char c;
        String Danxienghep;
        char c2;
        char c3;
        String[] socon;
        String[] Danxi2;
        String[] Danxi3;
        char c4;
        String Danxienghep2;
        char c5;
        char c6;
        String[] socon2;
        String Danxienghep3;
        String[] danlayS;
        char c7;
        String[] socon3;
        int i3 = 5;
        int i4 = -1;
        int i5 = 4;
        if (this.mang[rw][1].indexOf("lo dau") > -1) {
            if (this.mang[rw][2].indexOf("loa") <= -1 || this.mang[rw][2].trim().indexOf("loa") <= 0) {
                String[][] strArr = this.mang;
                strArr[rw][4] = strArr[rw][2].replaceFirst("loa :", "");
                String[][] strArr2 = this.mang;
                strArr2[rw][4] = strArr2[rw][4].replaceFirst("loa:", "");
                String[][] strArr3 = this.mang;
                strArr3[rw][4] = strArr3[rw][4].replaceFirst("loa", "");
                String[][] strArr4 = this.mang;
                strArr4[rw][4] = Congthuc.XulyLoDe(strArr4[rw][4]);
                i = 4;
            } else {
                String[] strArr5 = this.mang[rw];
                StringBuilder sb = new StringBuilder();
                sb.append("Không hiểu ");
                String[][] strArr6 = this.mang;
                sb.append(strArr6[rw][2].substring(0, strArr6[rw][2].indexOf("loa")));
                strArr5[4] = sb.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].contains("lo")) {
            if (!this.mang[rw][2].contains("lo") || this.mang[rw][2].trim().indexOf("lo") <= 0) {
                String[][] strArr7 = this.mang;
                strArr7[rw][4] = strArr7[rw][2].replaceFirst("lo :", "");
                String[][] strArr8 = this.mang;
                strArr8[rw][4] = strArr8[rw][4].replaceFirst("lo:", "");
                String[][] strArr9 = this.mang;
                strArr9[rw][4] = strArr9[rw][4].replaceFirst("lo", "");
                String[][] strArr10 = this.mang;
                strArr10[rw][4] = Congthuc.XulyLoDe(strArr10[rw][4]);
                i = 4;
            } else {
                String[] strArr11 = this.mang[rw];
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Không hiểu ");
                String[][] strArr12 = this.mang;
                sb2.append(strArr12[rw][2].substring(0, strArr12[rw][2].indexOf("lo")));
                strArr11[4] = sb2.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].contains("de dau db")) {
            if (!this.mang[rw][2].contains("dea") || this.mang[rw][2].trim().indexOf("dea") <= 0) {
                String[][] strArr13 = this.mang;
                strArr13[rw][4] = strArr13[rw][2].replaceFirst("dea :", "");
                String[][] strArr14 = this.mang;
                strArr14[rw][4] = strArr14[rw][4].replaceFirst("dea:", "");
                String[][] strArr15 = this.mang;
                strArr15[rw][4] = strArr15[rw][4].replaceFirst("dea", "");
                String[][] strArr16 = this.mang;
                strArr16[rw][4] = Congthuc.XulyLoDe(strArr16[rw][4]);
                i = 4;
            } else {
                String[] strArr17 = this.mang[rw];
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Không hiểu ");
                String[][] strArr18 = this.mang;
                sb3.append(strArr18[rw][2].substring(0, strArr18[rw][2].indexOf("de")));
                strArr17[4] = sb3.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].indexOf("de dit db") > -1) {
            if (this.mang[rw][2].indexOf("deb") <= -1 || this.mang[rw][2].trim().indexOf("deb") <= 0) {
                String[][] strArr19 = this.mang;
                strArr19[rw][4] = strArr19[rw][2].replaceFirst("deb :", "");
                String[][] strArr20 = this.mang;
                strArr20[rw][4] = strArr20[rw][4].replaceFirst("deb:", "");
                String[][] strArr21 = this.mang;
                strArr21[rw][4] = strArr21[rw][4].replaceFirst("deb", "");
                String[][] strArr22 = this.mang;
                strArr22[rw][4] = strArr22[rw][4].replaceFirst("de :", "");
                String[][] strArr23 = this.mang;
                strArr23[rw][4] = strArr23[rw][4].replaceFirst("de:", "");
                String[][] strArr24 = this.mang;
                strArr24[rw][4] = strArr24[rw][4].replaceFirst("de ", "");
                String[][] strArr25 = this.mang;
                strArr25[rw][4] = Congthuc.XulyLoDe(strArr25[rw][4]);
                i = 4;
            } else {
                String[] strArr26 = this.mang[rw];
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Không hiểu ");
                String[][] strArr27 = this.mang;
                sb4.append(strArr27[rw][2].substring(0, strArr27[rw][2].indexOf("de")));
                strArr26[4] = sb4.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].indexOf("de dau nhat") > -1) {
            if (this.mang[rw][2].indexOf("dec") <= -1 || this.mang[rw][2].trim().indexOf("dec") <= 0) {
                String[][] strArr28 = this.mang;
                strArr28[rw][4] = strArr28[rw][2].replaceFirst("dec :", "");
                String[][] strArr29 = this.mang;
                strArr29[rw][4] = strArr29[rw][4].replaceFirst("dec:", "");
                String[][] strArr30 = this.mang;
                strArr30[rw][4] = strArr30[rw][4].replaceFirst("dec", "");
                String[][] strArr31 = this.mang;
                strArr31[rw][4] = Congthuc.XulyLoDe(strArr31[rw][4]);
                i = 4;
            } else {
                String[] strArr32 = this.mang[rw];
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Không hiểu ");
                String[][] strArr33 = this.mang;
                sb5.append(strArr33[rw][2].substring(0, strArr33[rw][2].indexOf("de")));
                strArr32[4] = sb5.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].indexOf("de dit nhat") > -1) {
            if (this.mang[rw][2].indexOf("ded") <= -1 || this.mang[rw][2].trim().indexOf("ded") <= 0) {
                String[][] strArr34 = this.mang;
                strArr34[rw][4] = strArr34[rw][2].replaceFirst("ded :", "");
                String[][] strArr35 = this.mang;
                strArr35[rw][4] = strArr35[rw][4].replaceFirst("ded:", "");
                String[][] strArr36 = this.mang;
                strArr36[rw][4] = strArr36[rw][4].replaceFirst("ded", "");
                String[][] strArr37 = this.mang;
                strArr37[rw][4] = Congthuc.XulyLoDe(strArr37[rw][4]);
                i = 4;
            } else {
                String[] strArr38 = this.mang[rw];
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Không hiểu ");
                String[][] strArr39 = this.mang;
                sb6.append(strArr39[rw][2].substring(0, strArr39[rw][2].indexOf("de")));
                strArr38[4] = sb6.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].indexOf("de 8") > -1) {
            if (this.mang[rw][2].indexOf("det") <= -1 || this.mang[rw][2].trim().indexOf("det") <= 0) {
                String[][] strArr40 = this.mang;
                strArr40[rw][4] = strArr40[rw][2].replaceFirst("det :", "");
                String[][] strArr41 = this.mang;
                strArr41[rw][4] = strArr41[rw][4].replaceFirst("det:", "");
                String[][] strArr42 = this.mang;
                strArr42[rw][4] = strArr42[rw][4].replaceFirst("det", "");
                String[][] strArr43 = this.mang;
                strArr43[rw][4] = Congthuc.XulyLoDe(strArr43[rw][4]);
                i = 4;
            } else {
                String[] strArr44 = this.mang[rw];
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Không hiểu ");
                String[][] strArr45 = this.mang;
                sb7.append(strArr45[rw][2].substring(0, strArr45[rw][2].indexOf("de")));
                strArr44[4] = sb7.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].indexOf("hai cua") > -1) {
            if (this.mang[rw][2].indexOf("hc") <= -1 || this.mang[rw][2].trim().indexOf("hc") <= 0) {
                String[][] strArr46 = this.mang;
                strArr46[rw][4] = strArr46[rw][2].replaceFirst("hc :", "");
                String[][] strArr47 = this.mang;
                strArr47[rw][4] = strArr47[rw][4].replaceFirst("hc:", "");
                String[][] strArr48 = this.mang;
                strArr48[rw][4] = strArr48[rw][4].replaceFirst("hc", "");
                String[][] strArr49 = this.mang;
                strArr49[rw][4] = Congthuc.XulyLoDe(strArr49[rw][4]);
                i = 4;
            } else {
                String[] strArr50 = this.mang[rw];
                StringBuilder sb8 = new StringBuilder();
                sb8.append("Không hiểu ");
                String[][] strArr51 = this.mang;
                sb8.append(strArr51[rw][2].substring(0, strArr51[rw][2].indexOf("hc")));
                strArr50[4] = sb8.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].indexOf("xn") > -1) {
            if (this.mang[rw][2].indexOf("xn") <= -1 || this.mang[rw][2].trim().indexOf("xn") <= 0) {
                String[][] strArr52 = this.mang;
                strArr52[rw][4] = strArr52[rw][2].replaceFirst("xn :", "");
                String[][] strArr53 = this.mang;
                strArr53[rw][4] = strArr53[rw][4].replaceFirst("xn:", "");
                String[][] strArr54 = this.mang;
                strArr54[rw][4] = strArr54[rw][4].replaceFirst("xn", "");
                if (this.mang[rw][2].indexOf("xn 2 ") > -1) {
                    String[][] strArr55 = this.mang;
                    strArr55[rw][4] = strArr55[rw][4].trim();
                    String[][] strArr56 = this.mang;
                    strArr56[rw][4] = strArr56[rw][4].substring(2);
                    String[][] strArr57 = this.mang;
                    strArr57[rw][4] = Congthuc.XulySo(strArr57[rw][4]);
                    String XienGhep = "";
                    Iterator<String> it = Congthuc.XulyXienGhep(this.mang[rw][4], 2).iterator();
                    while (it.hasNext()) {
                        XienGhep = XienGhep + it.next() + " ";
                    }
                    this.mang[rw][4] = XienGhep;
                } else {
                    String[][] strArr58 = this.mang;
                    strArr58[rw][4] = Congthuc.XulySo(strArr58[rw][4].replaceAll("xn", " "));
                    this.mang[rw][4] = Congthuc.XulyXien("2 " + this.mang[rw][4].trim());
                }
                String[] ArrXien = this.mang[rw][4].split(" ");
                boolean ktra = false;
                int s1 = 0;
                while (true) {
                    if (s1 >= ArrXien.length) {
                        break;
                    } else if (ArrXien[s1].replaceAll(",", "").length() != 2 || !Congthuc.isNumeric(ArrXien[s1].replaceAll(",", ""))) {
                        ktra = true;
                    } else {
                        s1++;
                    }
                }
                ktra = true;
                if (!ktra && ArrXien.length < 5) {
                    String[][] strArr59 = this.mang;
                    strArr59[rw][4] = Congthuc.XulySo(strArr59[rw][4]);
                }
                String[] ArrXien2 = this.mang[rw][4].split(" ");
                int i6 = 0;
                while (true) {
                    if (i6 >= ArrXien2.length) {
                        c7 = 4;
                        break;
                    }
                    String ss = Congthuc.XulySo(ArrXien2[i6]);
                    if (ss.length() >= i3 && ss.length() <= 6 && ss.indexOf("Không hiểu") <= i4) {
                        String[] danlayS2 = ss.split(",");
                        int i42 = 0;
                        while (i42 < danlayS2.length) {
                            if (danlayS2[i42].length() != 2 || !Congthuc.isNumeric(danlayS2[i42])) {
                                if (this.mang[rw][i5].length() > i5) {
                                    this.mang[rw][4] = "Không hiểu " + this.mang[rw][2];
                                } else {
                                    this.mang[rw][4] = "Không hiểu " + this.mang[rw][0];
                                }
                            }
                            i42++;
                            i5 = 4;
                        }
                        i6++;
                        i3 = 5;
                        i4 = -1;
                        i5 = 4;
                    }
                }
                if (ArrXien2[i6].length() > 4) {
                    c7 = 4;
                    this.mang[rw][4] = "Không hiểu " + this.mang[rw][2];
                } else {
                    c7 = 4;
                    this.mang[rw][4] = "Không hiểu " + this.mang[rw][0];
                }
                if (this.mang[rw][c7].indexOf("Không hiểu") == -1) {
                    this.mang[rw][c7] = "";
                    int i7 = 0;
                    String soxien = "";
                    while (true) {
                        if (i7 >= ArrXien2.length) {
                            break;
                        }
                        try {
                        } catch (Exception e) {
                            this.mang[rw][4] = "Không hiểu " + ArrXien2[i7];
                        }
                        if (soxien.indexOf("Không hiểu") != -1) {
                            break;
                        }
                        boolean check = false;
                        for (String str : soxien.split(",")) {
                            if (soxien.length() - soxien.replaceAll(str, "").length() > 2) {
                                check = true;
                            }
                        }
                        if (soxien.length() < 5 || soxien.length() > 6 || check) {
                            this.mang[rw][4] = "Không hiểu " + this.mang[rw][2];
                        } else {
                            this.mang[rw][4] = this.mang[rw][4] + Congthuc.sortXien(soxien) + " ";
                            i7++;
                        }
                    }
                    this.mang[rw][4] = "Không hiểu " + this.mang[rw][2];
                }
                i = 4;
            } else {
                String[] strArr60 = this.mang[rw];
                StringBuilder sb9 = new StringBuilder();
                sb9.append("Không hiểu ");
                String[][] strArr61 = this.mang;
                sb9.append(strArr61[rw][2].substring(0, strArr61[rw][2].indexOf("xn")));
                strArr60[4] = sb9.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].indexOf("bc dau") > -1) {
            if (this.mang[rw][2].indexOf("bca") <= -1 || this.mang[rw][2].trim().indexOf("bca") <= 0) {
                String[][] strArr62 = this.mang;
                i = 4;
                strArr62[rw][4] = strArr62[rw][2].replaceFirst("bca :", "");
                String[][] strArr63 = this.mang;
                strArr63[rw][4] = strArr63[rw][4].replaceFirst("bca:", "");
                String[][] strArr64 = this.mang;
                strArr64[rw][4] = strArr64[rw][4].replaceFirst("bca", "");
                String[][] strArr65 = this.mang;
                strArr65[rw][4] = Congthuc.Xu3cang(strArr65[rw][4]);
            } else {
                String[] strArr66 = this.mang[rw];
                StringBuilder sb10 = new StringBuilder();
                sb10.append("Không hiểu ");
                String[][] strArr67 = this.mang;
                sb10.append(strArr67[rw][2].substring(0, strArr67[rw][2].indexOf("bca")));
                strArr66[4] = sb10.toString();
                i = 4;
            }
        } else if (this.mang[rw][1].indexOf("bc") <= -1) {
            int i8 = 8;
            if (this.mang[rw][1].indexOf("xi") > -1) {
                if (this.mang[rw][2].indexOf("xi") <= -1 || this.mang[rw][2].trim().indexOf("xi") <= 0) {
                    if (this.mang[rw][2].indexOf("xia") > -1) {
                        Danxi3 = this.mang[rw][2].split("xia");
                    } else {
                        Danxi3 = this.mang[rw][2].split("xi");
                    }
                    String Danxienghep4 = "";
                    if (Danxi3.length > 2) {
                        for (int i43 = 1; i43 < Danxi3.length; i43++) {
                            if (Danxi3[i43].length() > 4) {
                                Danxienghep4 = Danxienghep4 + Congthuc.XulySo(Danxi3[i43]) + " ";
                                if (Congthuc.XulySo(Danxi3[i43]).contains("Không hiểu")) {
                                    this.mang[rw][4] = "Không hiểu " + Danxi3[i43];
                                }
                            }
                        }
                        this.mang[rw][4] = Danxienghep4;
                        Danxienghep2 = Danxienghep4;
                        c4 = 4;
                    } else {
                        if (this.mang[rw][2].indexOf("xia") > -1) {
                            String[][] strArr68 = this.mang;
                            c4 = 4;
                            strArr68[rw][4] = strArr68[rw][2].replaceFirst("xia", "");
                        } else {
                            c4 = 4;
                            String[][] strArr69 = this.mang;
                            strArr69[rw][4] = strArr69[rw][2].replaceFirst("xi", "");
                        }
                        Danxienghep2 = Danxienghep4;
                    }
                    if (Danxi3.length < 3) {
                        String[][] strArr70 = this.mang;
                        strArr70[rw][c4] = Congthuc.XulyXien(strArr70[rw][c4].trim());
                    }
                    String[] ArrXien3 = this.mang[rw][c4].split(" ");
                    boolean ktra2 = false;
                    int s12 = 0;
                    while (true) {
                        if (s12 >= ArrXien3.length) {
                            break;
                        } else if (ArrXien3[s12].replaceAll(",", "").length() != 2 || !Congthuc.isNumeric(ArrXien3[s12].replaceAll(",", ""))) {
                            ktra2 = true;
                        } else {
                        }
                        s12++;
                    }
                    if (ktra2 || ArrXien3.length >= 5) {
                        c5 = 4;
                    } else {
                        String[][] strArr71 = this.mang;
                        c5 = 4;
                        strArr71[rw][4] = Congthuc.XulySo(strArr71[rw][4]);
                    }
                    String[] ArrXien4 = this.mang[rw][c5].split(" ");
                    int i9 = 0;
                    while (i9 < ArrXien4.length) {
                        String ss2 = Congthuc.XulySo(ArrXien4[i9]);
                        if (ss2.length() >= 5 && ss2.length() <= 12) {
                            if (ss2.indexOf("Không hiểu") <= -1) {
                                if (this.mang[rw][1] != "xq" || ss2.length() >= i8) {
                                    String[] danlayS3 = ss2.split(",");
                                    int i44 = 0;
                                    while (i44 < danlayS3.length) {
                                        if (danlayS3[i44].length() == 2 && Congthuc.isNumeric(danlayS3[i44])) {
                                            danlayS = danlayS3;
                                        } else if (this.mang[rw][4].length() > 4) {
                                            String[] strArr72 = this.mang[rw];
                                            StringBuilder sb11 = new StringBuilder();
                                            sb11.append("Không hiểu ");
                                            danlayS = danlayS3;
                                            sb11.append(this.mang[rw][2]);
                                            strArr72[4] = sb11.toString();
                                        } else {
                                            danlayS = danlayS3;
                                            this.mang[rw][4] = "Không hiểu " + this.mang[rw][0];
                                        }
                                        i44++;
                                        Danxienghep2 = Danxienghep2;
                                        danlayS3 = danlayS;
                                    }
                                    Danxienghep3 = Danxienghep2;
                                } else {
                                    this.mang[rw][4] = "Không hiểu " + this.mang[rw][0];
                                    Danxienghep3 = Danxienghep2;
                                }
                                Danxienghep2 = Danxienghep3;
                                i8 = 8;
                            }
                        }
                        c6 = 4;
                        if (ArrXien4[i9].length() < 4) {
                            this.mang[rw][4] = "Không hiểu " + this.mang[rw][2];
                        }
                        if (!this.mang[rw][c6].contains("Không hiểu")) {
                            this.mang[rw][c6] = "";
                            int i10 = 0;
                            String soxien2 = "";
                            while (true) {
                                if (i10 >= ArrXien4.length) {
                                    break;
                                }
                                try {
                                    soxien2 = Congthuc.XulySo(ArrXien4[i10]);
                                } catch (Exception e2) {
                                    this.mang[rw][4] = "Không hiểu " + ArrXien4[i10];
                                }
                                if (soxien2.indexOf("Không hiểu") != -1) {
                                    break;
                                }
                                boolean check2 = false;
                                for (String str2 : soxien2.split(",")) {
                                    if (soxien2.length() - soxien2.replaceAll(str2, "").length() > 2) {
                                        check2 = true;
                                    }
                                }
                                if (soxien2.length() < 5 || soxien2.length() > 12 || check2) {
                                    this.mang[rw][4] = "Không hiểu " + this.mang[rw][2];
                                } else {
                                    this.mang[rw][4] = this.mang[rw][4] + Congthuc.sortXien(soxien2) + " ";
                                    i10++;
                                }
                            }
                        }
                        i9++;
                    }
                    c6 = 4;

                    i = 4;
                } else {
                    String[] strArr73 = this.mang[rw];
                    StringBuilder sb12 = new StringBuilder();
                    sb12.append("Không hiểu ");
                    String[][] strArr74 = this.mang;
                    sb12.append(strArr74[rw][2].substring(0, strArr74[rw][2].indexOf("xi")));
                    strArr73[4] = sb12.toString();
                    i = 4;
                }
            } else if (this.mang[rw][1].contains("xq")) {
                if (!this.mang[rw][2].contains("xq") || this.mang[rw][2].trim().indexOf("xq") <= 0) {
                    if (this.mang[rw][2].contains("xqa")) {
                        String[] Danxi4 = this.mang[rw][2].split("xqa");
                        this.mang[rw][1] = "xq dau";
                        Danxi = Danxi4;
                        i2 = 2;
                    } else {
                        i2 = 2;
                        Danxi = this.mang[rw][2].split("xq");
                    }
                    String Danxienghep5 = "";
                    if (Danxi.length > i2) {
                        for (int i45 = 1; i45 < Danxi.length; i45++) {
                            if (Danxi[i45].length() > 4) {
                                Danxienghep5 = Danxienghep5 + Congthuc.XulySo(Danxi[i45]) + " ";
                                if (Congthuc.XulySo(Danxi[i45]).indexOf("Không hiểu") > -1) {
                                    this.mang[rw][4] = "Không hiểu " + Danxi[i45];
                                }
                            }
                        }
                        this.mang[rw][4] = Danxienghep5;
                        Danxienghep = Danxienghep5;
                        c = 4;
                    } else {
                        if (this.mang[rw][2].indexOf("xqa") > -1) {
                            String[][] strArr75 = this.mang;
                            c = 4;
                            strArr75[rw][4] = strArr75[rw][2].replaceFirst("xqa", "");
                        } else {
                            c = 4;
                            String[][] strArr76 = this.mang;
                            strArr76[rw][4] = strArr76[rw][2].replaceFirst("xq", "");
                        }
                        Danxienghep = Danxienghep5;
                    }
                    String[][] strArr77 = this.mang;
                    strArr77[rw][c] = Congthuc.XulyXien(strArr77[rw][c].trim());
                    String[] ArrXien5 = this.mang[rw][c].split(" ");
                    boolean ktra3 = false;
                    int s13 = 0;
//                    while (true) {
//                        if (s13 >= ArrXien5.length) {
//                            break;
//                        } else if (ArrXien5[s13].replaceAll(",", "").length() != 2 || !Congthuc.isNumeric(ArrXien5[s13].replaceAll(",", ""))) {
//                            ktra3 = true;
//                        } else {
//                            s13++;
//                        }
//                    }
                    if (ktra3 || ArrXien5.length >= 8) {
                        c2 = 4;
                    } else {
                        String[][] strArr78 = this.mang;
                        c2 = 4;
                        strArr78[rw][4] = Congthuc.XulySo(strArr78[rw][4]);
                    }
                    String[] ArrXien6 = this.mang[rw][c2].split(" ");
                    int i11 = 0;
                    while (i11 < ArrXien6.length) {
                        String ss3 = Congthuc.XulySo(ArrXien6[i11]);
                        if (ss3.length() >= 8 && ss3.length() <= 12) {
                            if (ss3.indexOf("Không hiểu") <= -1) {
                                if (this.mang[rw][1] != "xq" || ss3.length() >= 8) {
                                    String[] danlayS4 = ss3.split(",");
                                    int i46 = 0;
                                    while (i46 < danlayS4.length) {
                                        if (danlayS4[i46].length() == 2 && Congthuc.isNumeric(danlayS4[i46])) {
                                            Danxi2 = Danxi;
                                        } else if (this.mang[rw][4].length() > 4) {
                                            String[] strArr79 = this.mang[rw];
                                            StringBuilder sb13 = new StringBuilder();
                                            sb13.append("Không hiểu ");
                                            Danxi2 = Danxi;
                                            sb13.append(this.mang[rw][2]);
                                            strArr79[4] = sb13.toString();
                                        } else {
                                            Danxi2 = Danxi;
                                            this.mang[rw][4] = "Không hiểu " + this.mang[rw][0];
                                        }
                                        i46++;
                                        Danxienghep = Danxienghep;
                                        Danxi = Danxi2;
                                    }
                                } else {
                                    this.mang[rw][4] = "Không hiểu " + this.mang[rw][0];
                                }
                                i11++;
                                Danxienghep = Danxienghep;
                                Danxi = Danxi;
                            }
                        }
                        if (ArrXien6[i11].length() > 8) {
                            c3 = 4;
                            this.mang[rw][4] = "Không hiểu " + this.mang[rw][2];
                        } else {
                            c3 = 4;
                            this.mang[rw][4] = "Không hiểu " + this.mang[rw][0];
                        }
                        if (this.mang[rw][c3].indexOf("Không hiểu") == -1) {
                            this.mang[rw][c3] = "";
                            int i12 = 0;
                            String soxien3 = "";
                            while (true) {
                                if (i12 >= ArrXien6.length) {
                                    break;
                                }
                                try {
                                    soxien3 = Congthuc.XulySo(ArrXien6[i12]);
                                } catch (Exception e3) {
                                    this.mang[rw][4] = "Không hiểu " + ArrXien6[i12];
                                }
                                if (soxien3.indexOf("Không hiểu") != -1) {
                                    break;
                                }
                                boolean check3 = false;
                                for (String str3 : soxien3.split(",")) {
                                    if (soxien3.length() - soxien3.replaceAll(str3, "").length() > 2) {
                                        check3 = true;
                                    }
                                }
                                if (soxien3.length() < 5 || soxien3.length() > 12 || check3) {
                                    this.mang[rw][4] = "Không hiểu " + this.mang[rw][2];
                                } else {
                                    this.mang[rw][4] = this.mang[rw][4] + Congthuc.sortXien(soxien3) + " ";
                                    i12++;
                                }
                            }
                        }
                        i = 4;
                    }
                    c3 = 4;
                    if (this.mang[rw][c3].indexOf("Không hiểu") == -1) {
                    }
                } else {
                    String[] strArr80 = this.mang[rw];
                    StringBuilder sb14 = new StringBuilder();
                    sb14.append("Không hiểu ");
                    String[][] strArr81 = this.mang;
                    sb14.append(strArr81[rw][2].substring(0, strArr81[rw][2].indexOf("xq")));
                    strArr80[4] = sb14.toString();
                }
                i = 4;
            } else if (this.mang[rw][1].indexOf("xg") <= -1) {
                i = 4;
            } else if (this.mang[rw][2].indexOf("xg") <= -1 || this.mang[rw][2].trim().indexOf("xg") <= 0) {
                if (this.mang[rw][1].indexOf("xg 2") > -1) {
                    String[][] strArr82 = this.mang;
                    strArr82[rw][4] = strArr82[rw][2].replaceFirst("xg 2 ", "");
                } else if (this.mang[rw][1].indexOf("xg 3") > -1) {
                    String[][] strArr83 = this.mang;
                    strArr83[rw][4] = strArr83[rw][2].replaceFirst("xg 3 ", "");
                } else if (this.mang[rw][1].indexOf("xg 4") > -1) {
                    String[][] strArr84 = this.mang;
                    strArr84[rw][4] = strArr84[rw][2].replaceFirst("xg 4 ", "");
                }
                ArrayList<String> listXienGhep = null;
                String XienGhep2 = "";
                String[][] strArr85 = this.mang;
                strArr85[rw][4] = Congthuc.XulySo(strArr85[rw][4]);
                if (this.mang[rw][4].indexOf("Không hiểu") == -1) {
                    if (this.mang[rw][1].indexOf("xg 2") > -1) {
                        listXienGhep = Congthuc.XulyXienGhep(this.mang[rw][4], 2);
                    } else if (this.mang[rw][1].indexOf("xg 3") > -1) {
                        listXienGhep = Congthuc.XulyXienGhep(this.mang[rw][4], 3);
                    } else if (this.mang[rw][1].indexOf("xg 4") > -1) {
                        listXienGhep = Congthuc.XulyXienGhep(this.mang[rw][4], 4);
                    }
                    Iterator<String> it2 = listXienGhep.iterator();
                    while (it2.hasNext()) {
                        XienGhep2 = XienGhep2 + it2.next() + " ";
                    }
                    i = 4;
                    this.mang[rw][4] = XienGhep2;
                } else {
                    i = 4;
                }
            } else {
                String[] strArr86 = this.mang[rw];
                StringBuilder sb15 = new StringBuilder();
                sb15.append("Không hiểu ");
                String[][] strArr87 = this.mang;
                sb15.append(strArr87[rw][2].substring(0, strArr87[rw][2].indexOf("xg")));
                strArr86[4] = sb15.toString();
                i = 4;
            }
        } else if (this.mang[rw][2].indexOf("bc") <= -1 || this.mang[rw][2].trim().indexOf("bc") <= 0) {
            String[][] strArr88 = this.mang;
            i = 4;
            strArr88[rw][4] = strArr88[rw][2].replaceFirst("bc :", "");
            String[][] strArr89 = this.mang;
            strArr89[rw][4] = strArr89[rw][4].replaceFirst("bc:", "");
            String[][] strArr90 = this.mang;
            strArr90[rw][4] = strArr90[rw][4].replaceFirst("bc", "");
            String[][] strArr91 = this.mang;
            strArr91[rw][4] = Congthuc.Xu3cang(strArr91[rw][4]);
        } else {
            String[] strArr92 = this.mang[rw];
            StringBuilder sb16 = new StringBuilder();
            sb16.append("Không hiểu ");
            String[][] strArr93 = this.mang;
            sb16.append(strArr93[rw][2].substring(0, strArr93[rw][2].indexOf("bc")));
            strArr92[4] = sb16.toString();
            i = 4;
        }
        String[][] strArr94 = this.mang;
        if (strArr94[rw][i] == null) {
            strArr94[rw][4] = "Không hiểu " + this.mang[rw][0].substring(0, 5);
        } else if (strArr94[rw][4].trim().length() == 10 && this.mang[rw][4].indexOf("Không hiểu") > -1) {
            this.mang[rw][4] = "Không hiểu " + this.mang[rw][0];
        }
    }

    private void BaoLoiDan(int rw) {
        if (this.mang[rw][4].indexOf("Không hiểu") > -1) {
            String[][] strArr = this.mang;
            strArr[rw][0] = Congthuc.ToMauError(strArr[rw][4].substring(11), this.mang[rw][0]);
        }
    }

    private void BaoLoiTien(int rw) {
        try {
            this.mang[rw][5] = Congthuc.XulyTien(this.mang[rw][3]);
            if (this.mang[rw][5].indexOf("Không hiểu") > -1 && this.mang[rw][5].trim().length() < 13) {
                this.mang[rw][0] = Congthuc.ToMauError(this.mang[rw][5].substring(11), this.mang[rw][0]);
            } else if (this.mang[rw][5].indexOf("Không hiểu") > -1 && this.mang[rw][5].trim().length() > 12) {
                this.mang[rw][0] = Congthuc.ToMauError(this.mang[rw][3], this.mang[rw][0]);
            }
        } catch (Exception e) {
            String[][] strArr = this.mang;
            strArr[rw][0] = Congthuc.ToMauError(strArr[rw][3], strArr[rw][0]);
        }
    }


    public void NhapSoChiTiet(int id) throws Throwable {
        Cursor cursor_ktra;
        double mGia;
        String str3 = null;
        SQLiteDatabase db2;
        DatabaseUtils.InsertHelper ih;
        SQLiteDatabase db3;
        Throwable th;
        Cursor cursor_ktra2;
        Cursor cursor;
        Exception e;
        Iterator<String> keys;
        Cursor cursor2 = null;
        String mNgay_Nhan = null;
        DatabaseUtils.InsertHelper ih2 = null;
        String mThe_loai = null;
        double mKhachGiu = 0;
        String str4 = null;
        double mKhachGiu2 = 0;
        String str = null;
        double mLanAn = 0;
        double mGia2 = 0;
        double mDiem = 0;
        double mDiemquydoi = 0;
        double mDlyGiu = 0;
        double mLanAn2 = 0;
        double mLanAn3 = 0;
        int mDiem_DlyGiu = 0;
        int mDiem_KhachGiu = 0;
        String[] str2 = new String[0];
        int length = 0;
        int i = 0;
        int mDiem_KhachGiu2 = 0;
        int mDiem_DlyGiu2 = 0;
        JSONException e2;
        Database database = this;
        String str5 = "the_loai";
        double mDlyGiu2 = 0.0d;
        Cursor c = database.GetData("Select * From tbl_tinnhanS WHERE id = " + id);
        c.moveToFirst();
        String mThe_loai2 = "Select * From tbl_soctS where ten_kh = '" + c.getString(4) + "' And ngay_nhan = '" + c.getString(1) + "' And type_kh = " + c.getString(3) + " And so_tin_nhan = " + c.getString(7);
        Cursor c2 = database.GetData(mThe_loai2);
        if (c2.getCount() == 0) {
            database.jsonDanSo = new JSONObject(c.getString(15));
            String mTenKH = c.getString(4);
            String mThe_loai3 = "";
            String mNgay_Nhan2 = c.getString(1);
            StringBuilder sb = new StringBuilder();
            String str42 = "";
            sb.append("Select * From tbl_KH_new where ten_kh = '");
            sb.append(mTenKH);
            sb.append("'");
            Cursor cursor3 = database.GetData(sb.toString());
            cursor3.moveToFirst();
            str3 = "";
            mGia = 0.0d;
            JSONObject jSONObject = new JSONObject(cursor3.getString(5));
            database.json = jSONObject;
            database.caidat_gia = jSONObject.getJSONObject("caidat_gia");
            database.caidat_tg = database.json.getJSONObject("caidat_tg");
            db2 = getWritableDatabase();
            ih = new DatabaseUtils.InsertHelper(db2, "tbl_soctS");
            db2.beginTransaction();
            keys = database.jsonDanSo.keys();
            while (keys.hasNext()) {
                JSONObject dan = new JSONObject(database.jsonDanSo.getString(keys.next()));
                String str43 = dan.getString("dan_so");
                str3 = dan.getString("so_tien");
                cursor_ktra = c2;
                db3 = db2;
                cursor2 = cursor3;
                mNgay_Nhan = mNgay_Nhan2;
                ih2 = ih;
                String str6 = "xi";
                if (dan.getString(str5).contains("de dau db")) {
                    mThe_loai = "dea";
                } else {
                    mThe_loai = dan.getString(str5).contains("de dit db") ? "deb" : dan.getString(str5).contains("de 8") ? "det" : dan.getString(str5).contains("de dau nhat") ? "dec" : dan.getString(str5).contains("de dit nhat") ? "ded" : dan.getString(str5).contains("bc dau") ? "bca" : dan.getString(str5).contains("bc") ? "bc" : dan.getString(str5).contains("lo dau") ? "loa" : dan.getString(str5).contains("lo") ? "lo" : dan.getString(str5).contains("xien dau") ? "xia" : (dan.getString(str5).contains(str6) || dan.getString(str5).contains("xg")) ? str6 : dan.getString(str5).contains("xn") ? "xn" : mThe_loai3;
                }

                if (mThe_loai.contains("dea")) {
                    str4 = str43;
                } else if (mThe_loai.contains("deb")) {
                    str4 = str43;
                } else if (mThe_loai.contains("dec")) {
                    str4 = str43;
                } else if (mThe_loai.contains("ded")) {
                    str4 = str43;
                } else if (mThe_loai.contains("det")) {
                    str4 = str43;
                } else {
                }

                if (mThe_loai.contains("lo")) {
                    str4 = str43;
                    mKhachGiu2 = database.caidat_tg.getInt("khgiu_lo");
                    mKhachGiu = database.caidat_tg.getInt("dlgiu_lo");
                } else {
                    str4 = str43;
                    if (!mThe_loai.contains(str6)) {
                        if (!mThe_loai.contains("xq")) {
                            if (mThe_loai.contains("xn")) {
                                mKhachGiu2 = database.caidat_tg.getInt("khgiu_xn");
                                mKhachGiu = database.caidat_tg.getInt("dlgiu_xn");
                            } else if (mThe_loai.contains("bc")) {
                                mKhachGiu2 = database.caidat_tg.getInt("khgiu_bc");
                                mKhachGiu = database.caidat_tg.getInt("dlgiu_bc");
                            } else {
                                mKhachGiu2 = 0.0d;
                                mKhachGiu = 0.0d;
                            }
                        }
                    }
                    mKhachGiu2 = database.caidat_tg.getInt("khgiu_xi");
                    mKhachGiu = database.caidat_tg.getInt("dlgiu_xi");
                }
                str = str5;
                String str7 = "gia_x2";
                if (mThe_loai.contains("dea")) {
                    mGia2 = database.caidat_gia.getDouble("dea");
                    mLanAn = database.caidat_gia.getDouble("an_dea");
                } else if (mThe_loai.contains("deb")) {
                    mGia2 = database.caidat_gia.getDouble("deb");
                    mLanAn = database.caidat_gia.getDouble("an_deb");
                } else if (mThe_loai.contains("dec")) {
                    mGia2 = database.caidat_gia.getDouble("dec");
                    mLanAn = database.caidat_gia.getDouble("an_dec");
                } else if (mThe_loai.contains("ded")) {
                    mGia2 = database.caidat_gia.getDouble("ded");
                    mLanAn = database.caidat_gia.getDouble("an_ded");
                } else if (mThe_loai.contains("det")) {
                    mGia2 = database.caidat_gia.getDouble("det");
                    mLanAn = database.caidat_gia.getDouble("an_det");
                } else if (mThe_loai.contains("lo")) {
                    mGia2 = database.caidat_gia.getDouble("lo");
                    mLanAn = database.caidat_gia.getDouble("an_lo");
                } else if (mThe_loai.contains(str6) && str4.length() == 5) {
                    mGia2 = database.caidat_gia.getDouble(str7);
                    mLanAn = database.caidat_gia.getDouble("an_x2");
                } else if (mThe_loai.contains(str6) && str4.length() == 8) {
                    mGia2 = database.caidat_gia.getDouble("gia_x3");
                    mLanAn = database.caidat_gia.getDouble("an_x3");
                } else if (mThe_loai.contains(str6) && str4.length() == 11) {
                    mGia2 = database.caidat_gia.getDouble("gia_x4");
                    mLanAn = database.caidat_gia.getDouble("an_x4");
                } else if (mThe_loai.contains("xn")) {
                    mGia2 = database.caidat_gia.getDouble("gia_xn");
                    mLanAn = database.caidat_gia.getDouble("an_xn");
                } else if (mThe_loai.contains("bc")) {
                    mGia2 = database.caidat_gia.getDouble("gia_bc");
                    mLanAn = database.caidat_gia.getDouble("an_bc");
                } else {
                    mGia2 = mGia;
                    mLanAn = mDlyGiu2;
                }
                mGia = mGia2;
                mDiem = Integer.parseInt(str3);
                String str8 = ",";

                if (mThe_loai.equals("deb")) {
                    if (database.caidat_tg.getInt("heso_de") == 2) {
                        mDiemquydoi = (int) (0.875d * mDiem);
                    } else if (database.caidat_tg.getInt("heso_de") == 1) {
                        mDiemquydoi = (int) (1.143d * mDiem);
                    } else {
                        mDiemquydoi = mDiem;
                    }
                }
                cursor_ktra2 = c;
                if (cursor_ktra2.getInt(3) == 1) {
                    mDiem_DlyGiu = (int) ((mDiemquydoi * mKhachGiu) / 100.0d);
                    mLanAn2 = mLanAn;
                    mDiem_KhachGiu = (int) ((mDiemquydoi * mKhachGiu2) / 100.0d);
                    mDlyGiu = mKhachGiu;
                    mLanAn3 = mKhachGiu2;
                } else {
                    mLanAn2 = mLanAn;
                    mDlyGiu = 0.0d;
                    mLanAn3 = 0.0d;
                    mDiem_DlyGiu = 0;
                    mDiem_KhachGiu = 0;
                }
                double mKhachGiu3 = mLanAn3;
                double mKhachGiu4 = mDiem_KhachGiu;
                Double.isNaN(mKhachGiu4);
                double d = mDiemquydoi - mKhachGiu4;
                double mDiemquydoi2 = mDiemquydoi;
                double mDiemquydoi3 = mDiem_DlyGiu;
                Double.isNaN(mDiemquydoi3);
                int mDiemton = (int) (d - mDiemquydoi3);
                if ("dea,deb,dec,ded,det,lo,loa,bc,bca".contains(mThe_loai)) {
                    str2 = str4.split(str8);
                } else {
                    str2 = str4.split(" ");
                }
                length = str2.length;
                i = 0;
                while (i < length) {
                    String So_chon = str2[i].trim();
                    if (So_chon.endsWith(str8)) {
                        mDiem_KhachGiu2 = mDiem_KhachGiu;
                        mDiem_DlyGiu2 = mDiem_DlyGiu;
                        So_chon = So_chon.substring(0, So_chon.length() - 1);
                    } else {
                        mDiem_KhachGiu2 = mDiem_KhachGiu;
                        mDiem_DlyGiu2 = mDiem_DlyGiu;
                    }
                    if (mThe_loai.contains(str6) && So_chon.length() == 5) {
                        mGia = database.caidat_gia.getDouble(str7);
                        mLanAn2 = database.caidat_gia.getDouble("an_x2");
                    } else if (mThe_loai.contains(str6) && So_chon.length() == 8) {
                        mGia = database.caidat_gia.getDouble("gia_x3");
                        mLanAn2 = database.caidat_gia.getDouble("an_x3");
                    } else if (mThe_loai.contains(str6) && So_chon.length() == 11) {
                        mGia = database.caidat_gia.getDouble("gia_x4");
                        mLanAn2 = database.caidat_gia.getDouble("an_x4");
                    }
                    Double.isNaN(mDiem);
                    double mThanhTien = mDiem * mGia;
                    ih2.prepareForInsert();

                    ih2.bind(ih2.getColumnIndex("ID"), (byte[]) null);
                    ih2.bind(ih2.getColumnIndex("ngay_nhan"), mNgay_Nhan);
                    ih2.bind(ih2.getColumnIndex("type_kh"), cursor_ktra2.getInt(3));
                    ih2.bind(ih2.getColumnIndex("ten_kh"), cursor2.getString(0));
                    ih2.bind(ih2.getColumnIndex("so_dienthoai"), cursor_ktra2.getString(5));
                    ih2.bind(ih2.getColumnIndex("so_tin_nhan"), cursor_ktra2.getInt(7));
                    ih2.bind(ih2.getColumnIndex("the_loai"), mThe_loai);
                    ih2.bind(ih2.getColumnIndex("so_chon"), So_chon);
                    ih2.bind(ih2.getColumnIndex("diem"), mDiem);
                    ih2.bind(ih2.getColumnIndex("diem_quydoi"), mDiemquydoi2);
                    ih2.bind(ih2.getColumnIndex("diem_khachgiu"), mKhachGiu3);
                    ih2.bind(ih2.getColumnIndex("diem_dly_giu"), mDlyGiu);
                    ih2.bind(ih2.getColumnIndex("diem_ton"), mDiemton);
                    ih2.bind(ih2.getColumnIndex("gia"), mGia * 1000.0d);
                    ih2.bind(ih2.getColumnIndex("lan_an"), mLanAn2 * 1000.0d);
                    ih2.bind(ih2.getColumnIndex("so_nhay"), 0);
                    ih2.bind(ih2.getColumnIndex("tong_tien"), mThanhTien * 1000.0d);
                    ih2.bind(ih2.getColumnIndex("ket_qua"), 0);
                    ih2.execute();
                    i++;
                }
                ih = ih2;
                database = this;
                mThe_loai3 = mThe_loai;
                c = cursor_ktra2;
                keys = keys;
                mDlyGiu2 = mLanAn2;
                mThe_loai2 = mThe_loai2;
                mTenKH = mTenKH;
                c2 = cursor_ktra;
                db2 = db3;
                mNgay_Nhan2 = mNgay_Nhan;
                str42 = str4;
                cursor3 = cursor2;
                str5 = str;
                cursor_ktra2 = c;
                if (cursor_ktra2.getInt(3) == 1) {
                }
                double mKhachGiu32 = mLanAn3;
                double mKhachGiu42 = mDiem_KhachGiu;
                Double.isNaN(mKhachGiu42);
                double d2 = mDiemquydoi - mKhachGiu42;
                double mDiemquydoi22 = mDiemquydoi;
                double mDiemquydoi32 = mDiem_DlyGiu;
                Double.isNaN(mDiemquydoi32);
                int mDiemton2 = (int) (d2 - mDiemquydoi32);

                String str82 = ",";
//                if ("dea,deb,dec,ded,det,lo,loa,bc,bca,xi".contains(mThe_loai)) {
//                    i = 0;
//                    str2 = str4.split(",");
//                    length = str2.length;
//                    while (i < length) {
//                        String So_chon = str2[i].trim();
//                        if (So_chon.endsWith(str8)) {
//                            mDiem_KhachGiu2 = mDiem_KhachGiu;
//                            mDiem_DlyGiu2 = mDiem_DlyGiu;
//                            So_chon = So_chon.substring(0, So_chon.length() - 1);
//                        } else {
//                            mDiem_KhachGiu2 = mDiem_KhachGiu;
//                            mDiem_DlyGiu2 = mDiem_DlyGiu;
//                        }
//                        Double.isNaN(mDiem);
//                        double mThanhTien = mDiem * mGia;
//                        ih2 = ih;
//                        ih2.prepareForInsert();
//
//                        ih2.bind(ih2.getColumnIndex("ID"), (byte[]) null);
//                        ih2.bind(ih2.getColumnIndex("ngay_nhan"), mNgay_Nhan);
//                        ih2.bind(ih2.getColumnIndex("type_kh"), cursor_ktra2.getInt(3));
//                        ih2.bind(ih2.getColumnIndex("ten_kh"), cursor2.getString(4));
//                        ih2.bind(ih2.getColumnIndex("so_dienthoai"), cursor_ktra2.getString(5));
//                        ih2.bind(ih2.getColumnIndex("so_tin_nhan"), cursor_ktra2.getInt(7));
//                        ih2.bind(ih2.getColumnIndex("the_loai"), mThe_loai);
//                        ih2.bind(ih2.getColumnIndex("so_chon"), So_chon);
//                        ih2.bind(ih2.getColumnIndex("diem"), mDiem);
//                        ih2.bind(ih2.getColumnIndex("diem_quydoi"), dan.getString("so_tien"));
//
//                        ih2.bind(ih2.getColumnIndex("diem_khachgiu"), mDiem_KhachGiu);
//                        ih2.bind(ih2.getColumnIndex("diem_dly_giu"), mDlyGiu);
//                        ih2.bind(ih2.getColumnIndex("diem_ton"), mDiemton2);
//                        ih2.bind(ih2.getColumnIndex("gia"), mGia * 1000.0d);
//                        ih2.bind(ih2.getColumnIndex("lan_an"), mLanAn2 * 1000.0d);
//                        ih2.bind(ih2.getColumnIndex("so_nhay"), 0);
//                        ih2.bind(ih2.getColumnIndex("tong_tien"), mThanhTien * 1000.0d);
//                        ih2.bind(ih2.getColumnIndex("ket_qua"), 0);
//                        ih2.execute();
//                        i++;
//                    }
//                }
                if ("xi ".contains(mThe_loai)) {

                }
            }
            db3 = db2;
            cursor_ktra = c2;
            cursor_ktra2 = c;
            db3.setTransactionSuccessful();
            db3.endTransaction();
            ih.close();
            db3.close();
            cursor = cursor3;

            if (!cursor_ktra2.isClosed()) {
                cursor_ktra2.close();
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        } else {
            cursor_ktra = c2;
        }
        cursor_ktra.close();
    }

    private String xuly_Xq(String str) {
        String dan_xien = "";
        String[] so_xien = str.split(",");
        if (so_xien.length == 3) {
            for (int i1 = 0; i1 < so_xien.length - 1; i1++) {
                for (int i2 = i1 + 1; i2 < so_xien.length; i2++) {
                    dan_xien = dan_xien + so_xien[i1] + "," + so_xien[i2] + " ";
                }
            }
            return dan_xien + so_xien[0] + "," + so_xien[1] + "," + so_xien[2];
        } else if (so_xien.length != 4) {
            return dan_xien;
        } else {
            for (int i12 = 0; i12 < so_xien.length - 1; i12++) {
                for (int i22 = i12 + 1; i22 < so_xien.length; i22++) {
                    dan_xien = dan_xien + so_xien[i12] + "," + so_xien[i22] + " ";
                }
            }
            for (int i13 = 0; i13 < so_xien.length - 2; i13++) {
                for (int i23 = i13 + 1; i23 < so_xien.length - 1; i23++) {
                    for (int i3 = i23 + 1; i3 < so_xien.length; i3++) {
                        dan_xien = dan_xien + so_xien[i13] + "," + so_xien[i23] + "," + so_xien[i3] + " ";
                    }
                }
            }
            return dan_xien + so_xien[0] + "," + so_xien[1] + "," + so_xien[2] + "," + so_xien[3];
        }
    }

    public String XuatDanTon2(String TheLoai, String Tienxuat, int mFrom, int mTo) {
        String xuatDan;
        int tien;
        String xuatDan2;
        int Dem;
        int mFrom2;
        int mFrom3;
        int MaxTien;
        int tien2;
        String xuatDan3;
        String str = Tienxuat;
        int DemPhu = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        String mDate = dmyFormat.format(calendar.getTime());
        String my_str = Tienxuat;
        if (my_str.length() == 0) {
            my_str = "0";
        }
        int TienChuyen = Integer.parseInt(my_str.replaceAll("%", "").replaceAll("n", "").replaceAll("k", "").replaceAll("d", "").replaceAll(">", ""));
        String SoOm = "";
        String tloai = null;
        String donvi = "n ";
        if (TheLoai == "deb") {
            xuatDan = "De:";
            SoOm = "Om_deB";
            tloai = "(the_loai = 'deb' or the_loai = 'det')";
            donvi = "n ";
        } else if (TheLoai == "dea") {
            xuatDan = "Dau DB:";
            SoOm = "Om_deA";
            tloai = "the_loai = 'dea'";
            donvi = "n ";
        } else if (TheLoai == "dec") {
            xuatDan = "Dau nhat:";
            SoOm = "Om_deC";
            tloai = "the_loai = 'dec'";
            donvi = "n ";
        } else if (TheLoai == "ded") {
            xuatDan = "Dit nhat:";
            SoOm = "Om_deD";
            tloai = "the_loai = 'ded'";
            donvi = "n ";
        } else if (TheLoai == "lo") {
            xuatDan = "Lo:";
            SoOm = "Om_lo";
            tloai = "the_loai = 'lo'";
            donvi = "d ";
        } else {
            xuatDan = "";
        }
        Cursor cursor = GetData("Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om." + SoOm + " + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om." + SoOm + " as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So\n Where tbl_soctS.ngay_nhan='" + mDate + "' AND " + tloai + " GROUP by so_om.So Order by ton DESC, diem DESC");
        int mLamtron = 1;
        try {
            if (MainActivity.jSon_Setting.getInt("lam_tron") == 0) {
                mLamtron = 1;
            } else if (MainActivity.jSon_Setting.getInt("lam_tron") == 1) {
                mLamtron = 10;
            } else if (MainActivity.jSon_Setting.getInt("lam_tron") == 2) {
                mLamtron = 50;
            } else if (MainActivity.jSon_Setting.getInt("lam_tron") == 3) {
                mLamtron = 100;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mFrom > 0) {
            mFrom2 = mFrom - 1;
            xuatDan2 = xuatDan;
            Dem = 0;
            tien = 0;
        } else {
            mFrom2 = mFrom;
            xuatDan2 = xuatDan;
            Dem = 0;
            tien = 0;
        }
        while (cursor.moveToNext()) {
            if (Dem < mFrom2 || Dem > mTo - 1) {
                mFrom3 = mFrom2;
            } else {
                if (TienChuyen == 0) {
                    mFrom3 = mFrom2;
                    MaxTien = (cursor.getInt(4) / mLamtron) * mLamtron;
                } else {
                    mFrom3 = mFrom2;
                    if (str.indexOf("%") > -1) {
                        MaxTien = (((cursor.getInt(4) * TienChuyen) / mLamtron) / 100) * mLamtron;
                    } else if (str.indexOf(">") > -1) {
                        MaxTien = ((cursor.getInt(4) - TienChuyen) / mLamtron) * mLamtron;
                    } else if (cursor.getInt(4) > TienChuyen) {
                        MaxTien = (TienChuyen / mLamtron) * mLamtron;
                    } else {
                        MaxTien = (cursor.getInt(4) / mLamtron) * mLamtron;
                    }
                }
                if (MaxTien > 0) {
                    if (tien > MaxTien) {
                        DemPhu = 0;
                        xuatDan3 = (xuatDan2 + "x" + tien + donvi) + cursor.getString(0) + ",";
                        tien2 = MaxTien;
                    } else {
                        xuatDan3 = xuatDan2 + cursor.getString(0) + ",";
                        tien2 = MaxTien;
                    }
                    DemPhu++;
                    tien = tien2;
                    xuatDan2 = xuatDan3;
                }
            }
            Dem++;
            str = Tienxuat;
            mFrom2 = mFrom3;
            dmyFormat = dmyFormat;
            mDate = mDate;
        }
        if (xuatDan2.length() > 4 && DemPhu > 0) {
            xuatDan2 = xuatDan2 + "x" + tien + donvi;
        }
        if (cursor != null) {
            cursor.close();
        }
        return xuatDan2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:110:0x0385  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x051e  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0552  */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x0610  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0115 A[Catch:{ JSONException -> 0x0309 }] */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x06e6  */
    /* JADX WARNING: Removed duplicated region for block: B:161:0x06ef  */
    public String Tin_Chottien(String TenKH) throws JSONException {
        String str;
        Cursor CongNo_Nhan;
        String mDate;
        String nocu;
        String nocu2;
        String str2;
        String str3;
        String socuoi;
        Cursor Tin_nhan;
        JSONObject jsonKhach;
        double TienNhan;
        double TienChuyen;
        double TienChuyen2;
        double TienNhan2;
        String socuoi2;
        Cursor ThongTin_khach;
        String nocu3;
        String mDate2;
        Cursor CongNo_Nhan2;
        String str4;
        String str5;
        String mDate3;
        String str6;
        String nocu4;
        JSONObject jsonKhach2;
        Iterator<String> keys;
        Cursor ThanhToan;
        String str7;
        Cursor Tin_nhan2;
        JSONObject jsonKhach3;
        Iterator<String> keys2;
        String str8;
        String str9;
        String str10;
        String Str_c;
        JSONException e;
        Database database = this;
        String str11 = "AnNhan";
        String str12 = "KQNhan";
        new MainActivity();
        String mDate4 = MainActivity.Get_date();
        String mNgay = MainActivity.Get_ngay();
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String TinChot = null;
        Cursor ThongTin_khach2 = database.GetData("Select * From tbl_kh_new Where ten_kh = '" + TenKH + "'");
        ThongTin_khach2.moveToFirst();
        str = "xn";
        JSONObject jSONObject = new JSONObject(ThongTin_khach2.getString(5));
        database.json = jSONObject;
        database.caidat_tg = jSONObject.getJSONObject("caidat_tg");
        CongNo_Nhan = database.GetData("Select ten_kh, so_dienthoai \n, SUM((ngay_nhan < '" + mDate4 + "') * ket_qua * (100-diem_khachgiu)/100)/1000  as NoCu \n, SUM((ngay_nhan <= '" + mDate4 + "')*ket_qua*(100-diem_khachgiu)/100)/1000 as SoCuoi  \n FROM tbl_soctS WHERE ten_kh = '" + TenKH + "'  GROUP BY ten_kh");
        CongNo_Nhan.moveToFirst();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("So cu: ");
        mDate = "xi";
        nocu = "lo";
        sb3.append(decimalFormat.format(CongNo_Nhan.getDouble(2)));
        nocu2 = sb3.toString();
        StringBuilder sb22 = new StringBuilder();
        sb22.append("So cuoi: ");
        str2 = "KQChuyen";
        str3 = "DiemChuyen";
        sb22.append(decimalFormat.format(CongNo_Nhan.getDouble(3)));
        socuoi = sb22.toString();
        Tin_nhan = database.GetData("Select ten_kh, so_dienthoai, the_loai\n, sum((type_kh = 1)*diem) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 1)*diem*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 1)*diem*so_nhay)  END nAn\n, sum((type_kh = 1)*ket_qua/1000) as mKetqua\n, sum((type_kh = 2)*diem) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 2)*diem*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 2)*diem*so_nhay)  END nAn\n, sum((type_kh = 2)*ket_qua/1000) as mKetqua\n, 100-(diem_khachgiu*(type_kh=1)) as PT\n  From tbl_soctS Where ngay_nhan = '" + mDate4 + "' AND ten_kh = '" + TenKH + "'\n  AND the_loai <> 'tt' GROUP by ten_kh, the_loai");
        jsonKhach = new JSONObject();
        TienNhan = 0.0d;
        TienChuyen = 0.0d;
        while (Tin_nhan.moveToNext()) {
            JSONObject jsonDang = new JSONObject();
            socuoi2 = socuoi;
            jsonDang.put("DiemNhan", Tin_nhan.getDouble(3));
            jsonDang.put(str11, Tin_nhan.getDouble(4));
            CongNo_Nhan2 = CongNo_Nhan;
            jsonDang.put(str12, Tin_nhan.getDouble(5));
            str6 = str3;
            try {
                jsonDang.put(str6, Tin_nhan.getDouble(6));
                ThongTin_khach = ThongTin_khach2;

                jsonDang.put("AnChuyen", Tin_nhan.getDouble(7));
                str4 = str2;

                jsonDang.put(str4, Tin_nhan.getDouble(8));
                if (Tin_nhan.getString(2).indexOf("de") > -1) {
                    jsonDang.put("PhanTram", 100 - database.caidat_tg.getInt("khgiu_de"));
                    str5 = nocu;
                    nocu3 = nocu2;
                    nocu4 = mDate;
                    mDate2 = mDate4;
                    mDate3 = str;
                } else {
                    str5 = nocu;
                    nocu3 = nocu2;
                    if (Tin_nhan.getString(2).contains(str5)) {
                        jsonDang.put("PhanTram", 100 - database.caidat_tg.getInt("khgiu_lo"));
                        nocu4 = mDate;
                        mDate2 = mDate4;
                        mDate3 = str;
                    } else {
                        nocu4 = mDate;
                        mDate2 = mDate4;
                        if (Tin_nhan.getString(2).indexOf(nocu4) > -1) {
                            jsonDang.put("PhanTram", 100 - database.caidat_tg.getInt("khgiu_xi"));
                            mDate3 = str;
                        } else {

                            if (Tin_nhan.getString(2).indexOf("bc") > -1) {
                                jsonDang.put("PhanTram", 100 - database.caidat_tg.getInt("khgiu_bc"));
                                mDate3 = str;
                            } else {
                                mDate3 = str;
                                if (Tin_nhan.getString(2).indexOf(mDate3) > -1) {
                                    jsonDang.put("PhanTram", 100 - database.caidat_tg.getInt("khgiu_xn"));
                                } else {
                                    jsonDang.put("PhanTram", Tin_nhan.getDouble(9));
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e19) {
            }
        }
        socuoi2 = socuoi;
        CongNo_Nhan2 = CongNo_Nhan;
        mDate3 = str;
        str5 = nocu;
        str6 = str3;
        jsonKhach2 = jsonKhach;
        nocu3 = nocu2;
        ThongTin_khach = ThongTin_khach2;
        str4 = str2;
        nocu4 = mDate;
        mDate2 = mDate4;
        TienNhan2 = TienNhan;
        TienChuyen2 = TienChuyen;
        JSONObject jsonObject2222222222222222 = new JSONObject();
        jsonObject2222222222222222.put("dea", "Dau DB: ");
        jsonObject2222222222222222.put("deb", "De: ");
        jsonObject2222222222222222.put("det", "De 8: ");
        jsonObject2222222222222222.put("dec", "Dau Nhat: ");
        jsonObject2222222222222222.put("ded", "Dit Nhat: ");
        jsonObject2222222222222222.put(str5, "Lo: ");
        jsonObject2222222222222222.put(nocu4, "Xien: ");
        jsonObject2222222222222222.put(mDate3, "X.nhay: ");
        jsonObject2222222222222222.put("bc", "3Cang: ");
        jsonObject2222222222222222.put("loa", "Lo dau: ");
        jsonObject2222222222222222.put("xia", "Xien dau: ");
        jsonObject2222222222222222.put("bca", "Cang dau: ");
        keys = jsonObject2222222222222222.keys();
        String Str_n2222222222222222 = "";
        String Str_c22222222222222222 = "";
        while (keys.hasNext()) {
            String key = keys.next();
            if (jsonKhach2.has(key)) {
                keys2 = keys;
                JSONObject jsonDang2 = new JSONObject(jsonKhach2.getString(key));
                jsonKhach3 = jsonKhach2;
                Tin_nhan2 = Tin_nhan;
                if (jsonDang2.getInt("PhanTram") != 100) {
                    Str_c = Str_c22222222222222222;
                    str9 = str6;
                    if (jsonDang2.getDouble("DiemNhan") > 0.0d) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(Str_n2222222222222222);
                        sb4.append(jsonObject2222222222222222.getString(key));
                        str10 = str4;
                        sb4.append(decimalFormat.format(jsonDang2.getDouble("DiemNhan")));
                        sb4.append("(");
                        sb4.append(decimalFormat.format(jsonDang2.getDouble(str11)));
                        sb4.append(") =");
                        sb4.append(decimalFormat.format(jsonDang2.getDouble(str12)));
                        sb4.append("x");
                        sb4.append(jsonDang2.getString("PhanTram"));
                        sb4.append("%=");
                        sb4.append(decimalFormat.format((jsonDang2.getDouble(str12) * jsonDang2.getDouble("PhanTram")) / 100.0d));
                        sb4.append("\n");
                        Str_n2222222222222222 = sb4.toString();
                    } else {
                        str10 = str4;
                    }
                } else if (jsonDang2.getDouble("DiemNhan") > 0.0d) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(Str_n2222222222222222);
                    sb5.append(jsonObject2222222222222222.getString(key));
                    Str_c = Str_c22222222222222222;
                    str9 = str6;
                    sb5.append(decimalFormat.format(jsonDang2.getDouble("DiemNhan")));
                    sb5.append("(");
                    sb5.append(decimalFormat.format(jsonDang2.getDouble(str11)));
                    sb5.append(")=");
                    sb5.append(decimalFormat.format(jsonDang2.getDouble(str12)));
                    sb5.append("\n");
                    Str_n2222222222222222 = sb5.toString();
                    str10 = str4;
                } else {
                    Str_c = Str_c22222222222222222;
                    str9 = str6;
                    str10 = str4;
                }
                if (jsonDang2.getDouble(str9) > 0.0d) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(Str_c);
                    sb6.append(jsonObject2222222222222222.getString(key));
                    str8 = str11;
                    str7 = str12;
                    sb6.append(decimalFormat.format(jsonDang2.getDouble(str9)));
                    sb6.append("(");
                    sb6.append(decimalFormat.format(jsonDang2.getDouble("AnChuyen")));
                    sb6.append(")=");
                    sb6.append(decimalFormat.format(jsonDang2.getDouble(str10)));
                    sb6.append("\n");
                    Str_c22222222222222222 = sb6.toString();
                } else {
                    str8 = str11;
                    str7 = str12;
                    Str_c22222222222222222 = Str_c;
                }
            } else {
                jsonKhach3 = jsonKhach2;
                keys2 = keys;
                Tin_nhan2 = Tin_nhan;
                str7 = str12;
                str9 = str6;
                str10 = str4;
                str8 = str11;
            }
            str11 = str8;
            keys = keys2;
            jsonKhach2 = jsonKhach3;
            str12 = str7;
            str4 = str10;
            str6 = str9;
            Tin_nhan = Tin_nhan2;
        }
        ThanhToan = GetData("SELECT SUM((the_loai = 'tt') * ket_qua) AS Ttoan \n FROM tbl_soctS WHERE ten_kh = '" + TenKH + "' AND ngay_nhan ='" + mDate2 + "'");
        ThanhToan.moveToFirst();
        String Ttoan2222222222222222 = "";
        if (ThanhToan.getInt(0) != 0) {
            Ttoan2222222222222222 = "T.toan: " + decimalFormat.format(ThanhToan.getDouble(0) / 1000.0d) + "\n";
        }
        if (this.caidat_tg.getInt("chot_sodu") == 0) {
            if (Str_n2222222222222222.length() > 0 && Str_c22222222222222222.length() > 0) {
                TinChot = mNgay + ":\n" + Str_n2222222222222222 + "Tong nhan:" + decimalFormat.format(TienNhan2) + "\n\n" + Str_c22222222222222222 + "Tong chuyen:" + decimalFormat.format(TienChuyen2) + "\nTong tien: " + decimalFormat.format(TienNhan2 + TienChuyen2);
            } else if (Str_n2222222222222222.length() > 0) {
                TinChot = mNgay + ":\n" + Str_n2222222222222222 + "Tong nhan:" + decimalFormat.format(TienNhan2);
            } else if (Str_c22222222222222222.length() > 0) {
                TinChot = mNgay + ":\n" + Str_c22222222222222222 + "Tong chuyen:" + decimalFormat.format(TienChuyen2);
            }
        } else if (Str_n2222222222222222.length() > 0 && Str_c22222222222222222.length() > 0) {
            TinChot = mNgay + ":\n" + nocu3 + "\n" + Str_n2222222222222222 + "Tong nhan:" + decimalFormat.format(TienNhan2) + "\n\n" + Str_c22222222222222222 + "Tong chuyen:" + decimalFormat.format(TienChuyen2) + "\nTong tien: " + decimalFormat.format(TienNhan2 + TienChuyen2) + "\n" + Ttoan2222222222222222 + socuoi2;
        } else if (Str_n2222222222222222.length() > 0) {
            TinChot = mNgay + ":\n" + nocu3 + "\n" + Str_n2222222222222222 + "Tong chuyen:" + decimalFormat.format(TienNhan2) + "\n" + Ttoan2222222222222222 + socuoi2;
        } else if (Str_c22222222222222222.length() > 0) {
            TinChot = mNgay + ":\n" + nocu3 + "\n" + Str_c22222222222222222 + "Tong chuyen:" + decimalFormat.format(TienChuyen2) + "\n" + Ttoan2222222222222222 + socuoi2;
        }
        if (!CongNo_Nhan2.isClosed()) {
            CongNo_Nhan2.close();
        }
        if (!ThongTin_khach.isClosed()) {
            ThongTin_khach.close();
        }
        return TinChot;
    }

    public String Tin_Chottien_xien(String TenKH) throws JSONException {
        String str;
        Cursor CongNo_Nhan;
        String str2;
        String socuoi;
        String nocu;
        String str3;
        String str4;
        String socuoi2;
        Cursor Tin_nhan;
        JSONObject jsonKhach;
        double TienNhan;
        double TienChuyen;
        double TienChuyen2;
        String nocu2;
        String socuoi3;
        Cursor ThongTin_khach;
        Cursor CongNo_Nhan2;
        String str5;
        String str6;
        String str7;
        double TienNhan2;
        JSONObject jsonKhach2;
        String nocu3;
        String str8;
        Iterator<String> keys;
        Cursor ThanhToan;
        String mDate;
        JSONObject jsonKhach3;
        Iterator<String> keys2;
        String str9;
        String str10;
        String mDate2;
        String str11;
        String str12;
        JSONException e;
        String str13 = "DiemChuyen";
        String str14 = "AnNhan";
        String str15 = "KQNhan";
        new MainActivity();
        String mDate3 = MainActivity.Get_date();
        String mNgay = MainActivity.Get_ngay();
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String TinChot = null;
        Cursor ThongTin_khach2 = GetData("Select * From tbl_kh_new Where ten_kh = '" + TenKH + "'");
        ThongTin_khach2.moveToFirst();
        str = "'";
        JSONObject jSONObject = new JSONObject(ThongTin_khach2.getString(5));
        this.json = jSONObject;
        this.caidat_tg = jSONObject.getJSONObject("caidat_tg");
        CongNo_Nhan = GetData("Select ten_kh, so_dienthoai \n, SUM((ngay_nhan < '" + mDate3 + "') * ket_qua * (100-diem_khachgiu)/100)/1000  as NoCu \n, SUM((ngay_nhan <= '" + mDate3 + "')*ket_qua*(100-diem_khachgiu)/100)/1000 as SoCuoi  \n FROM tbl_soctS WHERE ten_kh = '" + TenKH + "'  GROUP BY ten_kh");
        CongNo_Nhan.moveToFirst();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("So cu: ");
        str2 = "bc";
        socuoi = "xn";
        sb3.append(decimalFormat.format(CongNo_Nhan.getDouble(2)));
        nocu = sb3.toString();
        StringBuilder sb22 = new StringBuilder();
        sb22.append("So cuoi: ");
        str3 = "lo";
        str4 = "KQChuyen";
        sb22.append(decimalFormat.format(CongNo_Nhan.getDouble(3)));
        socuoi2 = sb22.toString();
        String Sql2 = "Select ten_kh, so_dienthoai, CASE \nWHEN the_loai = 'xi' And length(so_chon) = 5 THEN 'xi2' \nWHEN the_loai = 'xi' And length(so_chon) = 8 THEN 'xi3' \nWHEN the_loai = 'xi' And length(so_chon) = 11 THEN 'xi4' \nWHEN the_loai = 'xia' And length(so_chon) = 5 THEN 'xia2' \nWHEN the_loai = 'xia' And length(so_chon) = 8 THEN 'xia3' \nWHEN the_loai = 'xia' And length(so_chon) = 11 THEN 'xia4' \nELSE the_loai END m_theloai\n, sum((type_kh = 1)*diem) as mDiem\n, sum((type_kh = 1)*diem*so_nhay) as mAn \n, sum((type_kh = 1)*ket_qua)/1000 as mKetqua\n, sum((type_kh = 2)*diem) as mDiem\n, sum((type_kh = 2)*diem*so_nhay) as mAn \n, sum((type_kh = 2)*ket_qua)/1000 as mKetqua\n, 100-(diem_khachgiu*(type_kh=1)) as PT\n  From tbl_soctS Where the_loai <> 'tt' AND ten_kh = '" + TenKH + "' and ngay_nhan = '" + mDate3 + "'\n  GROUP by m_theloai";
        Tin_nhan = GetData(Sql2);
        jsonKhach = new JSONObject();
        TienNhan = 0.0d;
        TienChuyen = 0.0d;
        while (Tin_nhan.moveToNext()) {
            try {
                JSONObject jsonDang = new JSONObject();
                jsonDang.put("DiemNhan", Tin_nhan.getDouble(3));
                jsonDang.put(str14, Tin_nhan.getDouble(4));
                CongNo_Nhan2 = CongNo_Nhan;
                jsonDang.put(str15, Tin_nhan.getDouble(5));
                jsonDang.put(str13, Tin_nhan.getDouble(6));
                nocu2 = nocu;
                try {
                    jsonDang.put("AnChuyen", Tin_nhan.getDouble(7));
                    str6 = str4;
                    try {
                        jsonDang.put(str6, Tin_nhan.getDouble(8));
                        if (Tin_nhan.getString(2).indexOf("de") > -1) {
                            jsonDang.put("PhanTram", 100 - this.caidat_tg.getInt("khgiu_de"));
                            str5 = str13;
                            str8 = str3;
                            nocu3 = str2;
                            ThongTin_khach = ThongTin_khach2;
                            str7 = socuoi;
                            socuoi3 = socuoi2;
                        } else {
                            str8 = str3;
                            try {
                                if (Tin_nhan.getString(2).indexOf(str8) > -1) {
                                    jsonDang.put("PhanTram", 100 - this.caidat_tg.getInt("khgiu_lo"));
                                    str5 = str13;
                                    nocu3 = str2;
                                    ThongTin_khach = ThongTin_khach2;
                                    str7 = socuoi;
                                    socuoi3 = socuoi2;
                                } else if (Tin_nhan.getString(2).indexOf("xi") > -1) {
                                    jsonDang.put("PhanTram", 100 - this.caidat_tg.getInt("khgiu_xi"));
                                    str5 = str13;
                                    nocu3 = str2;
                                    ThongTin_khach = ThongTin_khach2;
                                    str7 = socuoi;
                                    socuoi3 = socuoi2;
                                } else {
                                    nocu3 = str2;
                                    try {
                                        ThongTin_khach = ThongTin_khach2;
                                        if (Tin_nhan.getString(2).indexOf(nocu3) > -1) {
                                            jsonDang.put("PhanTram", 100 - this.caidat_tg.getInt("khgiu_bc"));
                                            str5 = str13;
                                            str7 = socuoi;
                                            socuoi3 = socuoi2;
                                        } else {
                                            str7 = socuoi;
                                            socuoi3 = socuoi2;
                                            if (Tin_nhan.getString(2).indexOf(str7) > -1) {
                                                jsonDang.put("PhanTram", 100 - this.caidat_tg.getInt("khgiu_xn"));
                                                str5 = str13;
                                            } else {
                                                str5 = str13;
                                                jsonDang.put("PhanTram", Tin_nhan.getDouble(9));
                                            }
                                        }
                                    } catch (JSONException e14) {
                                    }
                                }
                            } catch (JSONException e15) {
                            }
                        }
                        TienNhan += (jsonDang.getDouble(str15) * jsonDang.getDouble("PhanTram")) / 100.0d;
                        TienChuyen += jsonDang.getDouble(str6);
                        String string = Tin_nhan.getString(2);
                        jsonKhach2 = jsonKhach;
                    } catch (JSONException e17) {
                    }
                } catch (JSONException e18) {
                }
            } catch (JSONException e19) {
            }
        }
        nocu2 = nocu;
        CongNo_Nhan2 = CongNo_Nhan;
        str8 = str3;
        nocu3 = str2;
        str6 = str4;
        ThongTin_khach = ThongTin_khach2;
        jsonKhach2 = jsonKhach;
        str5 = str13;
        str7 = socuoi;
        socuoi3 = socuoi2;
        TienNhan2 = TienNhan;
        TienChuyen2 = TienChuyen;
        JSONObject jsonObject22222222222222222 = new JSONObject();
        String str1622222222222222222 = str5;
        jsonObject22222222222222222.put("dea", "Dau DB: ");
        jsonObject22222222222222222.put("deb", "De: ");
        jsonObject22222222222222222.put("det", "De 8: ");
        jsonObject22222222222222222.put("dec", "Dau Nhat: ");
        jsonObject22222222222222222.put("ded", "Dit Nhat: ");
        jsonObject22222222222222222.put(str8, "Lo: ");
        jsonObject22222222222222222.put("xi2", "Xien 2: ");
        jsonObject22222222222222222.put("xi3", "Xien 3: ");
        jsonObject22222222222222222.put("xi4", "Xien 4: ");
        jsonObject22222222222222222.put(str7, "X.nhay: ");
        jsonObject22222222222222222.put(nocu3, "3Cang: ");
        jsonObject22222222222222222.put("loa", "Lo dau: ");
        jsonObject22222222222222222.put("xia2", "Xia 2: ");
        jsonObject22222222222222222.put("xia3", "Xia 3: ");
        jsonObject22222222222222222.put("xia4", "Xia 4: ");
        jsonObject22222222222222222.put("bca", "3Cang dau: ");
        keys = jsonObject22222222222222222.keys();
        String Str_n22222222222222222 = "";
        String Str_c22222222222222222 = "";
        while (keys.hasNext()) {
            String key = keys.next();
            if (jsonKhach2.has(key)) {
                keys2 = keys;
                JSONObject jsonDang2 = new JSONObject(jsonKhach2.getString(key));
                jsonKhach3 = jsonKhach2;
                mDate = mDate3;
                if (jsonDang2.getInt("PhanTram") != 100) {
                    str12 = str6;
                    if (jsonDang2.getDouble("DiemNhan") > 0.0d) {
                        Str_n22222222222222222 = Str_n22222222222222222 + jsonObject22222222222222222.getString(key) + decimalFormat.format(jsonDang2.getDouble("DiemNhan")) + "(" + decimalFormat.format(jsonDang2.getDouble(str14)) + ")=" + decimalFormat.format(jsonDang2.getDouble(str15)) + "x" + jsonDang2.getString("PhanTram") + "%=" + decimalFormat.format((jsonDang2.getDouble(str15) * jsonDang2.getDouble("PhanTram")) / 100.0d) + "\n";
                    }
                } else if (jsonDang2.getDouble("DiemNhan") > 0.0d) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(Str_n22222222222222222);
                    sb4.append(jsonObject22222222222222222.getString(key));
                    str12 = str6;
                    sb4.append(decimalFormat.format(jsonDang2.getDouble("DiemNhan")));
                    sb4.append("(");
                    sb4.append(decimalFormat.format(jsonDang2.getDouble(str14)));
                    sb4.append(")=");
                    sb4.append(decimalFormat.format(jsonDang2.getDouble(str15)));
                    sb4.append("\n");
                    Str_n22222222222222222 = sb4.toString();
                } else {
                    str12 = str6;
                }
                mDate2 = str1622222222222222222;
                if (jsonDang2.getDouble(mDate2) > 0.0d) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(Str_c22222222222222222);
                    sb5.append(jsonObject22222222222222222.getString(key));
                    str10 = str14;
                    str9 = str15;
                    sb5.append(decimalFormat.format(jsonDang2.getDouble(mDate2)));
                    sb5.append("(");
                    sb5.append(decimalFormat.format(jsonDang2.getDouble("AnChuyen")));
                    sb5.append(")=");
                    str11 = str12;
                    sb5.append(decimalFormat.format(jsonDang2.getDouble(str11)));
                    sb5.append("\n");
                    Str_c22222222222222222 = sb5.toString();
                } else {
                    str10 = str14;
                    str9 = str15;
                    str11 = str12;
                }
            } else {
                keys2 = keys;
                jsonKhach3 = jsonKhach2;
                str10 = str14;
                mDate = mDate3;
                str11 = str6;
                mDate2 = str1622222222222222222;
                str9 = str15;
            }
            str6 = str11;
            str14 = str10;
            str15 = str9;
            keys = keys2;
            jsonKhach2 = jsonKhach3;
            str1622222222222222222 = mDate2;
            mDate3 = mDate;
        }
        ThanhToan = GetData("SELECT SUM((the_loai = 'tt') * ket_qua) AS Ttoan \n FROM tbl_soctS WHERE ten_kh = '" + TenKH + "' AND ngay_nhan ='" + mDate3 + str);
        ThanhToan.moveToFirst();
        String Ttoan22222222222222222 = "";
        if (ThanhToan.getInt(0) != 0) {
            Ttoan22222222222222222 = "T.toan: " + decimalFormat.format(ThanhToan.getDouble(0) / 1000.0d) + "\n";
        }
        if (this.caidat_tg.getInt("chot_sodu") != 0) {
            if (Str_n22222222222222222.length() > 0 && Str_c22222222222222222.length() > 0) {
                TinChot = mNgay + ":\n" + Str_n22222222222222222 + "Tong nhan:" + decimalFormat.format(TienNhan2) + "\n\n" + Str_c22222222222222222 + "Tong chuyen:" + decimalFormat.format(TienChuyen2) + "\nTong tien: " + decimalFormat.format(TienNhan2 + TienChuyen2);
            } else if (Str_n22222222222222222.length() > 0) {
                TinChot = mNgay + ":\n" + Str_n22222222222222222 + "Tong nhan:" + decimalFormat.format(TienNhan2);
            } else if (Str_c22222222222222222.length() > 0) {
                TinChot = mNgay + ":\n" + Str_c22222222222222222 + "Tong chuyen:" + decimalFormat.format(TienChuyen2);
            }
        } else if (Str_n22222222222222222.length() > 0 && Str_c22222222222222222.length() > 0) {
            TinChot = mNgay + ":\n" + nocu2 + "\n" + Str_n22222222222222222 + "Tong nhan:" + decimalFormat.format(TienNhan2) + "\n\n" + Str_c22222222222222222 + "Tong chuyen:" + decimalFormat.format(TienChuyen2) + "\nTong tien: " + decimalFormat.format(TienNhan2 + TienChuyen2) + "\n" + Ttoan22222222222222222 + socuoi3;
        } else if (Str_n22222222222222222.length() > 0) {
            TinChot = mNgay + ":\n" + nocu2 + "\n" + Str_n22222222222222222 + "Tong chuyen:" + decimalFormat.format(TienNhan2) + "\n" + Ttoan22222222222222222 + socuoi3;
        } else if (Str_c22222222222222222.length() > 0) {
            TinChot = mNgay + ":\n" + nocu2 + "\n" + Str_c22222222222222222 + "Tong chuyen:" + decimalFormat.format(TienChuyen2) + "\n" + Ttoan22222222222222222 + socuoi3;
        }
        if (!CongNo_Nhan2.isClosed()) {
            CongNo_Nhan2.close();
        }
        if (!ThongTin_khach.isClosed()) {
            ThongTin_khach.close();
        }
        return TinChot;
    }

    public String Tin_Chottien_CT(String TenKH) {
        String str;
        MainActivity activity = new MainActivity();
        String mDate = MainActivity.Get_date();
        String mNgay = MainActivity.Get_ngay();
        String pattern = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String NoiDung = "";
        Cursor cursor = GetData("Select so_tin_nhan, CASE \nWHEN the_loai = 'xi' And length(so_chon) = 5 THEN 'xi2' \nWHEN the_loai = 'xi' And length(so_chon) = 8 THEN 'xi3' \nWHEN the_loai = 'xi' And length(so_chon) = 11 THEN 'xi4' \nWHEN the_loai = 'xia' And length(so_chon) = 5 THEN 'xia2' \nWHEN the_loai = 'xia' And length(so_chon) = 8 THEN 'xia3' \nWHEN the_loai = 'xia' And length(so_chon) = 11 THEN 'xia4' \nELSE the_loai END m_theloai\n, sum(diem) as mDiem\n, sum(diem*so_nhay) as mAn \n, sum(ket_qua) as ThanhTien \nFrom tbl_soctS Where ngay_nhan = '" + mDate + "' And ten_kh = '" + TenKH + "' and the_loai <> 'tt' AND type_kh = 1\nGROUP by so_tin_nhan, m_theloai ORDER by type_kh DESC, ten_kh");
        String str1 = "Select so_tin_nhan, CASE \nWHEN the_loai = 'xi' And length(so_chon) = 5 THEN 'xi2' \nWHEN the_loai = 'xi' And length(so_chon) = 8 THEN 'xi3' \nWHEN the_loai = 'xi' And length(so_chon) = 11 THEN 'xi4' \nWHEN the_loai = 'xia' And length(so_chon) = 5 THEN 'xia2' \nWHEN the_loai = 'xia' And length(so_chon) = 8 THEN 'xia3' \nWHEN the_loai = 'xia' And length(so_chon) = 11 THEN 'xia4' \nELSE the_loai END m_theloai\n, sum(diem) as mDiem\n, sum(diem*so_nhay) as mAn \n, sum(ket_qua) as ThanhTien \nFrom tbl_soctS Where ngay_nhan = '" + mDate + "' And ten_kh = '" + TenKH + "' and the_loai <> 'tt' AND type_kh = 2\nGROUP by so_tin_nhan, m_theloai ORDER by type_kh DESC, ten_kh";
        Cursor cursor1 = GetData(str1);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dea", "Dau DB: ");
            jsonObject.put("deb", "De: ");
            jsonObject.put("det", "De 8: ");
            jsonObject.put("dec", "Dau Nhat: ");
            jsonObject.put("ded", "Dit Nhat: ");
            jsonObject.put("lo", "Lo: ");
            jsonObject.put("xi2", "Xien 2: ");
            jsonObject.put("xi3", "Xien 3: ");
            jsonObject.put("xi4", "Xien 4: ");
            jsonObject.put("xn", "X.nhay: ");
            jsonObject.put("bc", "3Cang: ");
            jsonObject.put("loa", "Lo dau: ");
            jsonObject.put("xia2", "Xia 2: ");
            jsonObject.put("xia3", "Xia 3: ");
            jsonObject.put("xia4", "Xia 4: ");
            jsonObject.put("bca", "3Cang dau: ");
            if (cursor.getCount() > 0) {
                NoiDung = "\nTin nhan:";
            }
            int Sotin = 0;
            while (true) {
                str = "\nTin ";
                if (!cursor.moveToNext()) {
                    break;
                }
                try {
                    if (Sotin != cursor.getInt(0)) {
                        Sotin = cursor.getInt(0);
                        NoiDung = (NoiDung + str + cursor.getString(0) + ":\n") + jsonObject.getString(cursor.getString(1)) + decimalFormat.format(cursor.getDouble(2)) + "(" + decimalFormat.format(cursor.getDouble(3)) + ")\n";
                        activity = activity;
                        mDate = mDate;
                    } else {
                        NoiDung = NoiDung + jsonObject.getString(cursor.getString(1)) + decimalFormat.format(cursor.getDouble(2)) + "(" + decimalFormat.format(cursor.getDouble(3)) + ")\n";
                        activity = activity;
                        mDate = mDate;
                    }
                } catch (JSONException e) {
                }
            }
            int Sotin2 = 0;
            if (cursor1.getCount() > 0) {
                NoiDung = NoiDung + "\n\nTin Chuyen:";
            }
            while (cursor1.moveToNext()) {
                if (Sotin2 != cursor1.getInt(0)) {
                    Sotin2 = cursor1.getInt(0);
                    NoiDung = NoiDung + str + cursor1.getString(0) + ":\n";
                    StringBuilder sb = new StringBuilder();
                    sb.append(NoiDung);
                    sb.append(jsonObject.getString(cursor1.getString(1)));
                    sb.append(decimalFormat.format(cursor1.getDouble(2)));
                    sb.append("(");
                    sb.append(decimalFormat.format(cursor1.getDouble(3)));
                    sb.append(")\n");
                    NoiDung = sb.toString();
                    mNgay = mNgay;
                    str = str;
                    pattern = pattern;
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(NoiDung);
                    sb2.append(jsonObject.getString(cursor1.getString(1)));
                    sb2.append(decimalFormat.format(cursor1.getDouble(2)));
                    sb2.append("(");
                    sb2.append(decimalFormat.format(cursor1.getDouble(3)));
                    sb2.append(")\n");
                    NoiDung = sb2.toString();
                    str1 = str1;
                    mNgay = mNgay;
                    str = str;
                    pattern = pattern;
                }
            }
        } catch (JSONException e8) {
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (cursor1 != null && !cursor1.isClosed()) {
            cursor1.close();
        }
        return NoiDung;
    }

    public String Tin_Chottien_CT11(String TenKH) throws JSONException {
        Cursor cursor1;
        String mDate;
        String mNgay;
        String pattern;
        String str1;
        Cursor cursor12;
        String str;
        String str2;
        String str3 = null;
        String str4 = null;
        double TongTien = 0;
        new MainActivity();
        String mDate2 = MainActivity.Get_date();
        String mNgay2 = MainActivity.Get_ngay();
        String pattern2 = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern2);
        String NoiDung = "";
        String str5 = "Select so_tin_nhan, the_loai\n, sum(diem) as mDiem\n, CASE WHEN the_loai = 'xi' Then sum(diem*so_nhay*lan_an/1000) ELSE sum(diem*so_nhay) END as mAn \n, sum(ket_qua) as ThanhTien \nFrom tbl_soctS Where ngay_nhan = '" + mDate2 + "' And ten_kh = '" + TenKH + "' and the_loai <> 'tt' AND type_kh = 1\nGROUP by so_tin_nhan, the_loai ORDER by type_kh DESC, ten_kh";
        Cursor cursor = GetData(str5);
        String str12 = "Select so_tin_nhan, the_loai \n, sum(diem) as mDiem\n, CASE WHEN the_loai = 'xi' Then sum(diem*so_nhay*lan_an/1000) ELSE sum(diem*so_nhay) END as mAn \n, sum(ket_qua) as ThanhTien \nFrom tbl_soctS Where ngay_nhan = '" + mDate2 + "' And ten_kh = '" + TenKH + "' and the_loai <> 'tt' AND type_kh = 2\nGROUP by so_tin_nhan, the_loai ORDER by type_kh DESC, ten_kh";
        Cursor cursor13 = GetData(str12);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dea", "Dau DB: ");
        jsonObject.put("deb", "De: ");
        jsonObject.put("det", "De 8: ");
        jsonObject.put("dec", "Dau Nhat: ");
        jsonObject.put("ded", "Dit Nhat: ");
        jsonObject.put("lo", "Lo: ");
        jsonObject.put("xi", "Xien: ");
        jsonObject.put("xn", "X.nhay: ");
        jsonObject.put("bc", "3Cang: ");
        jsonObject.put("loa", "Lo dau: ");
        jsonObject.put("xia", "Xia: ");
        jsonObject.put("bca", "3Cang dau: ");
        if (cursor.getCount() > 0) {
            NoiDung = "\nTin nhan:";
        }
        int Sotin = 0;
        double TongTien2 = 0.0d;
        double TongCacTin = 0.0d;
        String TongTin = "";
        while (true) {
            try {
                mDate = ":";
                mNgay = "Tong tin ";
                pattern = ":\n";
                str1 = "\nTin ";
                cursor12 = cursor13;
                str = "(";
                if (!cursor.moveToNext()) {
                    break;
                }

                if (Sotin != cursor.getInt(0)) {
                    if (Sotin > 0) {
                        NoiDung = NoiDung + TongTin + decimalFormat.format(TongTien2) + "\n";
                        TongTien2 = 0.0d;
                    }
                    Sotin = cursor.getInt(0);
                    TongTin = mNgay + cursor.getString(0) + mDate;
                    NoiDung = ((NoiDung + str1 + cursor.getString(0) + pattern) + jsonObject.getString(cursor.getString(1)) + decimalFormat.format(cursor.getDouble(2)) + str + decimalFormat.format(cursor.getDouble(3)) + ")") + "=" + decimalFormat.format(cursor.getDouble(4) / 1000.0d) + "\n";
                    TongTien2 += cursor.getDouble(4) / 1000.0d;
                    TongCacTin += cursor.getDouble(4) / 1000.0d;
                    mDate2 = mDate2;
                    mNgay2 = mNgay2;
                    pattern2 = pattern2;
                    str12 = str12;
                    str5 = str5;
                    cursor13 = cursor12;
                } else {
                    NoiDung = (NoiDung + jsonObject.getString(cursor.getString(1)) + decimalFormat.format(cursor.getDouble(2)) + str + decimalFormat.format(cursor.getDouble(3)) + ")") + "=" + decimalFormat.format(cursor.getDouble(4) / 1000.0d) + "\n";
                    TongTien2 += cursor.getDouble(4) / 1000.0d;
                    TongCacTin += cursor.getDouble(4) / 1000.0d;
                    mDate2 = mDate2;
                    mNgay2 = mNgay2;
                    pattern2 = pattern2;
                    str12 = str12;
                    str5 = str5;
                    cursor13 = cursor12;
                }
            } catch (JSONException e2) {
                cursor1 = cursor13;
            }
        }
        String str6 = "=";
        if (TongTien2 > 0.0d) {
            str2 = ")";
            NoiDung = (NoiDung + TongTin + decimalFormat.format(TongTien2) + "\n\n") + "Tong cong:" + decimalFormat.format(TongCacTin);
        } else {
            str2 = ")";
        }
        int Sotin2 = 0;
        double TongCacTin2 = 0.0d;
        String TongTin2 = "";

        if (cursor12.getCount() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(NoiDung);
            sb.append("\n\nTin Chuyen:");
            NoiDung = sb.toString();
            Sotin2 = 0;
        }
        str4 = "Tong cong:";
        str3 = "\n\n";
        TongTien = 0.0d;
        while (cursor12.moveToNext()) {
            try {
                cursor1 = cursor12;
                if (Sotin2 != cursor1.getInt(0)) {
                    if (Sotin2 > 0) {
                        TongTien = 0.0d;
                        NoiDung = NoiDung + TongTin2 + decimalFormat.format(TongTien) + "\n";
                    }
                    int Sotin3 = cursor1.getInt(0);
                    TongTin2 = mNgay + cursor.getString(0) + mDate;
                    NoiDung = (NoiDung + str1 + cursor1.getString(0) + pattern) + jsonObject.getString(cursor1.getString(1)) + decimalFormat.format(cursor1.getDouble(2)) + str + decimalFormat.format(cursor1.getDouble(3)) + str2;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(NoiDung);
                    sb2.append(str6);
                    sb2.append(decimalFormat.format(cursor.getDouble(4) / 1000.0d));
                    sb2.append("\n");
                    NoiDung = sb2.toString();
                    TongTien += cursor.getDouble(4) / 1000.0d;
                    str2 = str2;
                    mNgay = mNgay;
                    str1 = str1;
                    str6 = str6;
                    pattern = pattern;
                    cursor12 = cursor1;
                    TongCacTin2 += cursor.getDouble(4) / 1000.0d;
                    str = str;
                    Sotin2 = Sotin3;
                    mDate = mDate;
                } else {
                    NoiDung = (NoiDung + jsonObject.getString(cursor1.getString(1)) + decimalFormat.format(cursor1.getDouble(2)) + str + decimalFormat.format(cursor1.getDouble(3)) + str2) + str6 + decimalFormat.format(cursor.getDouble(4) / 1000.0d) + "\n";
                    TongTien += cursor.getDouble(4) / 1000.0d;
                    str2 = str2;
                    str = str;
                    mNgay = mNgay;
                    TongTin2 = TongTin2;
                    str1 = str1;
                    str6 = str6;
                    pattern = pattern;
                    cursor12 = cursor1;
                    TongCacTin2 += cursor.getDouble(4) / 1000.0d;
                    Sotin2 = Sotin2;
                    mDate = mDate;
                }
            } catch (JSONException e7) {
            }
        }
        cursor1 = cursor12;
        if (TongTien > 0.0d) {
            NoiDung = (NoiDung + TongTin2 + decimalFormat.format(TongTien) + str3) + str4 + decimalFormat.format(TongCacTin2);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (cursor1 != null && !cursor1.isClosed()) {
            cursor1.close();
        }
        return NoiDung;
    }

    public void Gui_Tin_Nhan(int mID) throws JSONException {
        int sotin;
        int ok_TIN = 0;
        String str = null;
        String str2 = null;
        int mSoTN = 0;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String mNgay = null;
        String str7 = null;
        String mGionhan = null;
        String tinNhan = null;
        String tinNhan2 = null;
        String str8 = null;
        int mSoTN2 = 0;
        String mGionhan2 = null;
        String mNgay2 = null;
        String mSoDT = null;
        String mSoDT2 = null;
        String mGionhan3 = null;
        String str9 = null;
        String mNgay3 = null;
        String mNgay4 = null;
        String mNgayNhan = null;
        String str10 = null;
        Cursor cur = null;
        Cursor getTin = null;
        Cursor chuyen = null;
        String my_app = null;
        String str11 = null;
        String str12 = null;
        String str13 = null;
        int mSoTN3;
        String tinNhan3 = null;
        Cursor cur2 = null;
        String mSoDT3 = null;
        Object obj;
        JSONException e;
        Cursor cursor;
        JSONException e2;
        StringBuilder sb;
        String myApp = null;
        Cursor chuyen2;
        int sotin2;
        String tinNhan4;
        String str14 = null;
        int mSoTN4;
        String str15 = null;
        String str16 = null;
        JSONException e3;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        hourFormat.setTimeZone(TimeZone.getDefault());
        String mNgayNhan2 = dmyFormat.format(calendar.getTime());
        String mGionhan4 = hourFormat.format(calendar.getTime());
        Cursor getTinNhan = GetData("Select * From tbl_tinnhanS WHERE ID = " + mID);
        getTinNhan.moveToFirst();
        int mSoTN5 = getTinNhan.getInt(7);
        final String mSoDT4 = getTinNhan.getString(5);
        String mNgay5 = getTinNhan.getString(1);
        Cursor c = GetData("Select * From tbl_kh_new Where sdt = '" + getTinNhan.getString(5) + "'");
        c.moveToFirst();
        try {
            sotin = 0;
            JSONObject jSONObject = new JSONObject(c.getString(5));
            this.json = jSONObject;
            JSONObject jSONObject2 = jSONObject.getJSONObject("caidat_tg");
            this.caidat_tg = jSONObject2;
            ok_TIN = jSONObject2.getInt("ok_tin");
        } catch (JSONException e5) {
        }
        String mGionhan522 = mGionhan4;
        if (getTinNhan.getInt(13) != 1) {
//            mSoTN = "\n";
            str2 = "Time";
            str = "TL";
            tinNhan = "' AND so_tin_nhan = ";
            str6 = "',1)";
            str7 = "','";
            mGionhan = mGionhan522;
//            mGionhan522 = ok_TIN;
            str4 = "Ok Tin ";
            str3 = "sms";
            str5 = "' AND so_dienthoai = '";
            mNgay = mNgay5;
            tinNhan2 = "', 2, '";
        } else if (ok_TIN == 5) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Ok Tin ");
            sb3.append(mSoTN5);
            sb3.append("\n");
//            mSoTN = "\n";
            sb3.append(getTinNhan.getString(8));
            final String tinNhan5 = sb3.toString();
            if (getTinNhan.getString(6).contains("sms")) {
                SendSMS(mSoDT4, tinNhan5);
                str2 = "Time";
                str = "TL";
                str6 = "',1)";
                str7 = "','";
                mGionhan = mGionhan522;
            } else if (getTinNhan.getString(6).indexOf("TL") > -1) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    /* class tamhoang.ldpro4.data.Database.AnonymousClass6 */

                    public void run() {
                        MainActivity.sendMessage(Long.parseLong(mSoDT4), tinNhan5);
                    }
                });
                str2 = "Time";
                str = "TL";
                str6 = "',1)";
                str7 = "','";
                mGionhan = mGionhan522;
            } else {
                JSONObject jsonObject = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT4));
                str2 = "Time";
                if (jsonObject.getInt("Time") > 3) {
                    str = "TL";
                    new NotificationReader().NotificationWearReader(getTinNhan.getString(5), tinNhan5);
                } else {
                    str = "TL";
                    jsonObject.put(tinNhan5, "OK");
                    MainActivity.json_Tinnhan.put(mSoDT4, jsonObject);
                }
                StringBuilder sb4222 = new StringBuilder();
                sb4222.append("Insert into Chat_database Values( null,'");
                sb4222.append(mNgayNhan2);
                sb4222.append("', '");
                mGionhan = mGionhan522;
                sb4222.append(mGionhan);
                sb4222.append("', 2, '");
                sb4222.append(getTinNhan.getString(4));
                sb4222.append("', '");
                sb4222.append(getTinNhan.getString(5));
                sb4222.append("', '");
                sb4222.append(getTinNhan.getString(6));
                str7 = "','";
                sb4222.append(str7);
                sb4222.append(tinNhan5);
                str6 = "',1)";
                sb4222.append(str6);
                QueryData(sb4222.toString());
            }
            StringBuilder sb5222 = new StringBuilder();
            sb5222.append("Update tbl_tinnhanS set ok_tn = 0 WHERE ngay_nhan = '");
            sb5222.append(mNgay5);
            sb5222.append("' AND so_dienthoai = '");
            sb5222.append(mSoDT4);
            tinNhan = "' AND so_tin_nhan = ";
            sb5222.append(tinNhan);
            sb5222.append(mSoTN5);
            QueryData(sb5222.toString());
            //mGionhan522 = ok_TIN;
            str4 = "Ok Tin ";
            str3 = "sms";
            str5 = "' AND so_dienthoai = '";
            mNgay = mNgay5;
            tinNhan2 = "', 2, '";
        } else {
//            mSoTN = "\n";
            str2 = "Time";
            str = "TL";
            str6 = "',1)";
            str7 = "','";
            mGionhan = mGionhan522;
            if (ok_TIN == 4) {
                final String tinNhan6 = "Ok Tin " + mSoTN5;
                str4 = "Ok Tin ";
                if (getTinNhan.getString(6).indexOf("sms") > -1) {
                    SendSMS(mSoDT4, tinNhan6);
                    //mGionhan522 = ok_TIN;
                    str3 = "sms";
                    tinNhan2 = "', 2, '";
                } else if (getTinNhan.getString(6).indexOf(str) > -1) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        /* class tamhoang.ldpro4.data.Database.AnonymousClass7 */

                        public void run() {
                            MainActivity.sendMessage(Long.parseLong(mSoDT4), tinNhan6);
                        }
                    });
                    //mGionhan522 = ok_TIN;
                    str3 = "sms";
                    tinNhan2 = "', 2, '";
                } else {

                    JSONObject jsonObject2 = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT4));
                    str3 = "sms";
                    int i = jsonObject2.getInt(str2);
                    str2 = str2;
                    if (i > 3) {
                        new NotificationReader().NotificationWearReader(getTinNhan.getString(5), tinNhan6);
                    } else {
                        //mGionhan522 = ok_TIN;
                        jsonObject2.put(tinNhan6, "OK");
                        MainActivity.json_Tinnhan.put(mSoDT4, jsonObject2);
                    }
                    StringBuilder sb62222 = new StringBuilder();
                    sb62222.append("Insert into Chat_database Values( null,'");
                    sb62222.append(mNgayNhan2);
                    sb62222.append("', '");
                    sb62222.append(mGionhan);
                    tinNhan2 = "', 2, '";
                    sb62222.append(tinNhan2);
                    sb62222.append(getTinNhan.getString(5));
                    sb62222.append("', '");
                    sb62222.append(mSoDT4);
                    sb62222.append("', '");
                    sb62222.append(getTinNhan.getString(6));
                    sb62222.append(str7);
                    sb62222.append(tinNhan6);
                    sb62222.append(str6);
                    QueryData(sb62222.toString());
                }
                StringBuilder sb72222 = new StringBuilder();
                sb72222.append("Update tbl_tinnhanS set ok_tn = 0 WHERE ngay_nhan = '");
                mNgay = mNgay5;
                sb72222.append(mNgay);
                str5 = "' AND so_dienthoai = '";
                sb72222.append(str5);
                sb72222.append(mSoDT4);
                tinNhan = "' AND so_tin_nhan = ";
                sb72222.append(tinNhan);
                sb72222.append(mSoTN5);
                QueryData(sb72222.toString());
            } else {
                //mGionhan522 = ok_TIN;
                str4 = "Ok Tin ";
                str3 = "sms";
                tinNhan = "' AND so_tin_nhan = ";
                str5 = "' AND so_dienthoai = '";
                mNgay = mNgay5;
                tinNhan2 = "', 2, '";
            }
        }
        if (getTinNhan.getString(11).indexOf("ok") != 0 && getTinNhan.getString(11).length() == 2 && getTinNhan.getInt(13) == 1) {
            if (mGionhan522 == null) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str4);
                sb8.append(mSoTN5);
                sb8.append(mSoTN);
                str11 = str5;
                mNgay2 = mNgay;
                sb8.append(getTinNhan.getString(10).replaceAll("de dit db", "de"));
                final String tinNhan7 = sb8.toString();
                if (getTinNhan.getString(6).indexOf(str3) > -1) {
                    SendSMS(mSoDT4, tinNhan7);
                    mSoTN = mSoTN5;
                    str16 = str3;
                } else if (getTinNhan.getString(6).indexOf(str) > -1) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        /* class tamhoang.ldpro4.data.Database.AnonymousClass8 */

                        public void run() {
                            MainActivity.sendMessage(Long.parseLong(mSoDT4), tinNhan7);
                        }
                    });
                    mSoTN = mSoTN5;
                    str16 = str3;
                } else {
                    JSONObject jsonObject3 = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT4));
                    str16 = str3;
                    int i2 = jsonObject3.getInt(str2);
                    str2 = str2;
                    if (i2 > 3) {
                        mSoTN = mSoTN5;
                        new NotificationReader().NotificationWearReader(getTinNhan.getString(5), tinNhan7);
                    } else {
                        mSoTN = mSoTN5;
                        jsonObject3.put(tinNhan7, "OK");
                        MainActivity.json_Tinnhan.put(mSoDT4, jsonObject3);
                    }
                    QueryData("Insert into Chat_database Values( null,'" + mNgayNhan2 + "', '" + mGionhan + tinNhan2 + getTinNhan.getString(4) + "', '" + getTinNhan.getString(5) + "', '" + getTinNhan.getString(6) + str7 + tinNhan7 + str6);
                }
                tinNhan3 = tinNhan2;
                str12 = str6;
                str10 = str16;
                mSoTN3 = mSoTN;
                str13 = str7;
                str9 = str;
                str = str2;
            } else {
                mNgay2 = mNgay;
                str11 = str5;
                int mSoTN6 = mSoTN5;
                if (mGionhan522 != "1") {
                    mSoTN3 = mSoTN6;
                    if (mGionhan522 != "3") {
                        tinNhan3 = tinNhan2;
                        str10 = str3;
                        str13 = str7;
                        str9 = str;
                        str = str2;
                        str12 = str6;
                    } else if (getTinNhan.getString(10).indexOf("Bỏ ") == -1) {
                        final String tinNhan8 = str4 + mSoTN3 + mSoTN + getTinNhan.getString(8);
                        if (getTinNhan.getString(6).indexOf(str3) > -1) {
                            SendSMS(mSoDT4, tinNhan8);
                            str14 = str3;
                            str13 = str7;
                            str9 = str;
                            str = str2;
                            str12 = str6;
                        } else {
                            str9 = str;
                            str14 = str3;
                            if (getTinNhan.getString(6).indexOf(str9) > -1) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    /* class tamhoang.ldpro4.data.Database.AnonymousClass11 */

                                    public void run() {
                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                            /* class tamhoang.ldpro4.data.Database.AnonymousClass11.AnonymousClass1 */

                                            public void run() {
                                                new MainActivity();
                                                MainActivity.sendMessage(Long.parseLong(mSoDT4), tinNhan8);
                                            }
                                        });
                                    }
                                });
                                str = str2;
                                str13 = str7;
                                str12 = str6;
                            } else {
                                JSONObject jsonObject4 = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT4));
                                if (jsonObject4.getInt(str2) > 3) {
                                    str = str2;
                                    new NotificationReader().NotificationWearReader(getTinNhan.getString(5), tinNhan8);
                                } else {
                                    str = str2;
                                    jsonObject4.put(tinNhan8, "OK");
                                    MainActivity.json_Tinnhan.put(mSoDT4, jsonObject4);
                                }
                                StringBuilder sb9222 = new StringBuilder();
                                sb9222.append("Insert into Chat_database Values( null,'");
                                sb9222.append(mNgayNhan2);
                                sb9222.append("', '");
                                sb9222.append(mGionhan);
                                sb9222.append(tinNhan2);
                                sb9222.append(getTinNhan.getString(4));
                                sb9222.append("', '");
                                sb9222.append(getTinNhan.getString(5));
                                sb9222.append("', '");
                                sb9222.append(getTinNhan.getString(6));
                                str13 = str7;
                                sb9222.append(str13);
                                sb9222.append(tinNhan8);
                                str12 = str6;
                                sb9222.append(str12);
                                QueryData(sb9222.toString());
                            }
                        }
                        tinNhan3 = tinNhan2;
                        str10 = str14;
                    } else {
                        str10 = str3;
                        str9 = str;
                        str = str2;
                        final String tinNhan9 = getTinNhan.getString(10).substring(0, getTinNhan.getString(10).indexOf(mSoTN) - 1) + "\nOK Tin" + mSoTN3 + mSoTN + getTinNhan.getString(8);
                        if (getTinNhan.getString(6).indexOf(str10) > -1) {
                            SendSMS(mSoDT4, tinNhan9);
                            str13 = str7;
                            tinNhan3 = tinNhan2;
                            str12 = str6;
                        } else if (getTinNhan.getString(6).indexOf(str9) > -1) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                /* class tamhoang.ldpro4.data.Database.AnonymousClass12 */

                                public void run() {
                                    new MainActivity();
                                    MainActivity.sendMessage(Long.parseLong(mSoDT4), tinNhan9);
                                }
                            });
                            str13 = str7;
                            tinNhan3 = tinNhan2;
                            str12 = str6;
                        } else {
                            try {
                                JSONObject jsonObject5 = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT4));
                                try {
                                    if (jsonObject5.getInt(str) > 3) {
                                        str = str;
                                        new NotificationReader().NotificationWearReader(getTinNhan.getString(5), tinNhan9);
                                    } else {
                                        str = str;
                                        jsonObject5.put(tinNhan9, "OK");
                                        MainActivity.json_Tinnhan.put(mSoDT4, jsonObject5);
                                    }
                                } catch (Exception e20) {
                                }
                            } catch (Exception e21) {
                            }
                            StringBuilder sb1022 = new StringBuilder();
                            sb1022.append("Insert into Chat_database Values( null,'");
                            sb1022.append(mNgayNhan2);
                            sb1022.append("', '");
                            sb1022.append(mGionhan);
                            tinNhan3 = tinNhan2;
                            sb1022.append(tinNhan3);
                            sb1022.append(getTinNhan.getString(4));
                            sb1022.append("', '");
                            sb1022.append(getTinNhan.getString(5));
                            sb1022.append("', '");
                            sb1022.append(getTinNhan.getString(6));
                            str13 = str7;
                            sb1022.append(str13);
                            sb1022.append(tinNhan9);
                            str12 = str6;
                            sb1022.append(str12);
                            QueryData(sb1022.toString());
                        }
                    }
                } else if (getTinNhan.getString(10).indexOf("Bỏ ") == -1) {
                    final String tinNhan10 = str4 + mSoTN6;
                    if (getTinNhan.getString(6).indexOf(str3) > -1) {
                        SendSMS(mSoDT4, tinNhan10);
                        mSoTN6 = mSoTN6;
                        str15 = str3;
                    } else if (getTinNhan.getString(6).indexOf(str) > -1) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            /* class tamhoang.ldpro4.data.Database.AnonymousClass9 */

                            public void run() {
                                MainActivity.sendMessage(Long.parseLong(mSoDT4), tinNhan10);
                            }
                        });
                        mSoTN6 = mSoTN6;
                        str15 = str3;
                    } else {
                        JSONObject jsonObject6 = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT4));
                        str15 = str3;

                        int i3 = jsonObject6.getInt(str2);
                        str2 = str2;
                        if (i3 > 3) {
                            mSoTN6 = mSoTN6;
                            new NotificationReader().NotificationWearReader(getTinNhan.getString(5), tinNhan10);
                        } else {
                            mSoTN6 = mSoTN6;
                            jsonObject6.put(tinNhan10, "OK");
                            MainActivity.json_Tinnhan.put(mSoDT4, jsonObject6);
                        }
                        QueryData("Insert into Chat_database Values( null,'" + mNgayNhan2 + "', '" + mGionhan + tinNhan2 + getTinNhan.getString(4) + "', '" + getTinNhan.getString(5) + "', '" + getTinNhan.getString(6) + str7 + tinNhan10 + str6);
                    }
                    tinNhan3 = tinNhan2;
                    str12 = str6;
                    str10 = str15;
                    mSoTN3 = mSoTN6;
                    str13 = str7;
                    str9 = str;
                    str = str2;
                } else {
                    final String tinNhan11 = getTinNhan.getString(10).substring(0, getTinNhan.getString(10).indexOf(mSoTN) - 1) + "\nOk tin " + mSoTN6 + ": ";
                    if (getTinNhan.getString(6).indexOf(str3) > -1) {
                        SendSMS(mSoDT4, tinNhan11);
                        mSoTN4 = mSoTN6;
                        str3 = str3;
                    } else {
                        mSoTN4 = mSoTN6;
                        if (getTinNhan.getString(6).indexOf(str) > -1) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                /* class tamhoang.ldpro4.data.Database.AnonymousClass10 */

                                public void run() {
                                    new MainActivity();
                                    MainActivity.sendMessage(Long.parseLong(mSoDT4), tinNhan11);
                                }
                            });
                            str = str;
                            str3 = str3;
                        } else {
                            JSONObject jsonObject7 = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT4));
                            str = str;
                            int i4 = jsonObject7.getInt(str2);
                            str2 = str2;
                            if (i4 > 3) {
                                str3 = str3;
                                new NotificationReader().NotificationWearReader(getTinNhan.getString(5), tinNhan11);
                            } else {
                                str3 = str3;
                                jsonObject7.put(tinNhan11, "OK");
                                MainActivity.json_Tinnhan.put(mSoDT4, jsonObject7);
                            }
                            QueryData("Insert into Chat_database Values( null,'" + mNgayNhan2 + "', '" + mGionhan + tinNhan2 + getTinNhan.getString(4) + "', '" + getTinNhan.getString(5) + "', '" + getTinNhan.getString(6) + str7 + tinNhan11 + str6);
                        }
                    }
                    tinNhan3 = tinNhan2;
                    str12 = str6;
                    str10 = str3;
                    mSoTN3 = mSoTN4;
                    str13 = str7;
                    str9 = str;
                    str = str2;
                }
            }
            cur2 = GetData("Select * From tbl_chuyenthang WHERE sdt_nhan = '" + mSoDT4 + "'");
            cur2.moveToFirst();
            if (cur2.getCount() <= 0) {
                obj = "OK";
                if (getTinNhan.getInt(14) == 1) {
                    Cursor c1 = GetData("Select max(so_tin_nhan) From tbl_tinnhanS WHERE so_dienthoai = '" + cur2.getString(4) + "' AND ngay_nhan = '" + mNgay2 + "' And type_kh = 2");
                    c1.moveToFirst();
                    int sotin3 = c1.getInt(0) + 1;
                    if (cur2.getString(3).indexOf(str9) > -1) {
                        myApp = "TL";
                    } else if (cur2.getString(3).indexOf("ZL") > -1) {
                        myApp = "ZL";
                    } else if (cur2.getString(3).indexOf("VB") > -1) {
                        myApp = "VB";
                    } else if (cur2.getString(3).indexOf("WA") > -1) {
                        myApp = "WA";
                    } else {
                        myApp = "sms";
                    }
                    QueryData("Insert Into tbl_tinnhanS values (null, '" + getTinNhan.getString(1) + "', '" + getTinNhan.getString(2) + "',2, '" + cur2.getString(3) + "', '" + cur2.getString(4) + "', '" + myApp + "', " + sotin3 + ", '" + getTinNhan.getString(8) + "', '" + getTinNhan.getString(9) + str13 + getTinNhan.getString(10) + "', 'ok',0,0,1, '" + getTinNhan.getString(15) + "')");
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append("Select * From tbl_tinnhanS WHERE ngay_nhan = '");
                    sb11.append(getTinNhan.getString(1));
                    sb11.append(str11);
                    sb11.append(cur2.getString(4));
                    sb11.append(tinNhan);
                    sb11.append(sotin3);
                    sb11.append(" AND type_kh = 2");
                    Cursor getid = GetData(sb11.toString());
                    getid.moveToFirst();
                    StringBuilder sb12 = new StringBuilder();
                    sb12.append("Update tbl_tinnhanS set del_sms = 0 WHERE ngay_nhan = '");
                    sb12.append(mNgay2);
                    sb12.append(str11);
                    sb12.append(mSoDT4);
                    sb12.append(tinNhan);
                    sb12.append(mSoTN3);
                    QueryData(sb12.toString());
                    try {
                        NhapSoChiTiet(getid.getInt(0));
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Cursor chuyen4 = GetData("Select Om_Xi3 FROM so_Om WHERE id = 13");
                    chuyen4.moveToFirst();
                    if (chuyen4.getInt(0) == 0) {
                        StringBuilder sb13 = new StringBuilder();
                        mSoTN2 = mSoTN3;
                        sb13.append("Tin ");
                        sb13.append(sotin3);
                        sb13.append(":\n");
                        str8 = tinNhan;
                        sb13.append(getTinNhan.getString(8));
                        final String tinNhan12 = sb13.toString();
                        if (getid.getString(6).indexOf(str10) > -1) {
                            SendSMS(cur2.getString(4), tinNhan12);
                            mSoDT3 = mSoDT4;
                            chuyen2 = chuyen4;
                            tinNhan4 = str12;
                            mGionhan3 = mGionhan;
                            mSoDT2 = mNgayNhan2;
                            mGionhan2 = str11;
                            mNgayNhan = tinNhan3;
                            mNgay2 = mNgay2;
                            mNgay3 = str13;
                        } else if (getid.getString(6).contains(str9)) {
                            new Handler(Looper.getMainLooper()).post(() -> {
//                                MainActivity.sendMessage(cur2.getLong(4), tinNhan12);
                            });
                            mSoDT3 = mSoDT4;
                            chuyen2 = chuyen4;
                            tinNhan4 = str12;
                            mGionhan3 = mGionhan;
                            mSoDT2 = mNgayNhan2;
                            mGionhan2 = str11;
                            mNgayNhan = tinNhan3;
                            mNgay2 = mNgay2;
                            mNgay3 = str13;
                        } else {
                            mSoDT3 = mSoDT4;
                            new NotificationReader().NotificationWearReader(cur2.getString(4), tinNhan12);
                            StringBuilder sb14 = new StringBuilder();
                            sb14.append("Insert into Chat_database Values( null,'");
                            mSoDT2 = mNgayNhan2;
                            sb14.append(mSoDT2);
                            sb14.append("', '");
                            mGionhan3 = mGionhan;
                            sb14.append(mGionhan3);
                            mGionhan2 = str11;
                            mNgayNhan = tinNhan3;
                            sb14.append(mNgayNhan);
                            mNgay2 = mNgay2;
                            chuyen2 = chuyen4;
                            sb14.append(cur2.getString(3));
                            sb14.append("', '");
                            sb14.append(cur2.getString(4));
                            mNgay3 = str13;
                            sb14.append(mNgay3);
                            sb14.append(myApp);
                            sb14.append(mNgay3);
                            sb14.append(tinNhan12);
                            tinNhan4 = str12;
                            sb14.append(tinNhan4);
                            QueryData(sb14.toString());
                        }
                        sotin2 = sotin3;
                        str12 = tinNhan4;
                    } else {
                        str8 = tinNhan;
                        mSoTN2 = mSoTN3;
                        mSoDT3 = mSoDT4;
                        chuyen2 = chuyen4;
                        mGionhan3 = mGionhan;
                        mSoDT2 = mNgayNhan2;
                        mGionhan2 = str11;
                        mNgayNhan = tinNhan3;
                        mNgay2 = mNgay2;
                        mNgay3 = str13;
                        final String tinNhan13 = "Tin " + sotin3 + ":\n" + getTinNhan.getString(9);
                        sotin2 = sotin3;
                        if (getid.getString(6).indexOf(str10) > -1) {
                            SendSMS(cur2.getString(4), tinNhan13);
                            str12 = str12;
                        } else if (getid.getString(6).indexOf(str9) > -1) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                /* class tamhoang.ldpro4.data.Database.AnonymousClass14 */

                                public void run() {
//                                    new MainActivity();
//                                    MainActivity.sendMessage(cur2.getLong(4), tinNhan13);
                                }
                            });
                            str12 = str12;
                        } else {
                            new NotificationReader().NotificationWearReader(cur2.getString(4), tinNhan13);
                            StringBuilder sb15 = new StringBuilder();
                            sb15.append("Insert into Chat_database Values( null,'");
                            sb15.append(mSoDT2);
                            sb15.append("', '");
                            sb15.append(mGionhan3);
                            sb15.append(mNgayNhan);
                            sb15.append(cur2.getString(3));
                            sb15.append("', '");
                            sb15.append(cur2.getString(4));
                            sb15.append(mNgay3);
                            sb15.append(myApp);
                            sb15.append(mNgay3);
                            sb15.append(tinNhan13);
                            str12 = str12;
                            sb15.append(str12);
                            QueryData(sb15.toString());
                        }
                    }
                    if (chuyen2 != null && !chuyen2.isClosed()) {
                        chuyen2.close();
                    }
                    if (c1 != null && !c1.isClosed()) {
                        c1.close();
                    }
                    sotin = sotin2;
                    if (getTinNhan.getInt(3) == 1 || MainActivity.jSon_Setting.getInt("baotinthieu") <= 0) {
                        mNgay4 = str12;
                        mSoDT = mSoDT3;
                    } else {
                        StringBuilder sb16 = new StringBuilder();
                        sb16.append("Select * From tbl_tinnhanS WHERE ngay_nhan = '");
                        String mNgay6 = mNgay2;

                        sb16.append(mNgay6);
                        sb16.append(mGionhan2);
                        mSoDT = mSoDT3;
                        sb16.append(mSoDT);

                        sb16.append("' AND type_kh = 1 ORDER BY so_tin_nhan");
                        Cursor cursor2 = GetData(sb16.toString());
                        JSONObject jsonTinnhan = new JSONObject();
                        mGionhan2 = mGionhan2;
                        int maxTin = 0;
                        while (cursor2.moveToNext()) {
                            sb = new StringBuilder();
                            mNgay2 = mNgay6;
                            sb.append(cursor2.getString(7));
                            sb.append("-");
                            jsonTinnhan.put(sb.toString(), cursor2.getString(7));
                            maxTin = cursor2.getInt(7);
                            jsonTinnhan = jsonTinnhan;
                            mNgay6 = mNgay2;
                            str12 = str12;
                        }
                        mNgay2 = mNgay6;
                        String tinthieu = "";
                        int i5 = 1;
                        while (i5 < maxTin) {
                            StringBuilder sb17 = new StringBuilder();
                            sb17.append(i5);
                            cursor = cursor2;
                            sb17.append("-");
                            if (!jsonTinnhan.has(sb17.toString())) {
                                tinthieu = tinthieu + i5 + ",";
                            }
                            i5++;
                            maxTin = maxTin;
                            cursor2 = cursor;
                        }
                        cursor = cursor2;

                        if (tinthieu.length() > 0) {
                            final String NoIDungThieu = "Thiếu tin " + tinthieu;
                            if (getTinNhan.getString(6).indexOf(str10) > -1) {
                                SendSMS(mSoDT, NoIDungThieu);
                                mNgay4 = str12;
                            } else if (getTinNhan.getString(6).indexOf(str9) > -1) {
                                String finalMSoDT = mSoDT;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    public void run() {
                                        new MainActivity();
                                        MainActivity.sendMessage(Long.parseLong(finalMSoDT), NoIDungThieu);
                                    }
                                });
                                mNgay4 = str12;
                            } else {
                                try {
                                    JSONObject jsonObject8 = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT));
                                    if (jsonObject8.getInt(str) > 3) {
                                        new NotificationReader().NotificationWearReader(getTinNhan.getString(5), NoIDungThieu);
                                        StringBuilder sb18 = new StringBuilder();
                                        sb18.append("Insert into Chat_database Values( null,'");
                                        sb18.append(mSoDT2);
                                        sb18.append("', '");
                                        sb18.append(mGionhan3);
                                        sb18.append(mNgayNhan);
                                        sb18.append(getTinNhan.getString(4));
                                        sb18.append("', '");
                                        sb18.append(getTinNhan.getString(5));
                                        sb18.append("', '");
                                        sb18.append(getTinNhan.getString(6));
                                        sb18.append(mNgay3);
                                        sb18.append(NoIDungThieu);
                                        mNgay4 = str12;
                                        sb18.append(mNgay4);
                                        QueryData(sb18.toString());
                                        new NotificationReader().NotificationWearReader(getTinNhan.getString(5), NoIDungThieu);
                                    } else {
                                        jsonObject8.put(NoIDungThieu, obj);
                                        MainActivity.json_Tinnhan.put(mSoDT, jsonObject8);
                                    }
                                } catch (Exception e42) {
                                }
                                StringBuilder sb18222 = new StringBuilder();
                                sb18222.append("Insert into Chat_database Values( null,'");
                                sb18222.append(mSoDT2);
                                sb18222.append("', '");
                                sb18222.append(mGionhan3);
                                sb18222.append(mNgayNhan);
                                sb18222.append(getTinNhan.getString(4));
                                sb18222.append("', '");
                                sb18222.append(getTinNhan.getString(5));
                                sb18222.append("', '");
                                sb18222.append(getTinNhan.getString(6));
                                sb18222.append(mNgay3);
                                sb18222.append(NoIDungThieu);
                                mNgay4 = str12;
                                sb18222.append(mNgay4);
                                QueryData(sb18222.toString());
                            }
                        } else {
                            mNgay4 = str12;
                        }
                        if (cursor != null) {
                            cursor.close();
                        }

                    }
                }
            } else {
                obj = "OK";
            }
            mSoTN2 = mSoTN3;
            mSoDT3 = mSoDT4;
            mNgay3 = str13;
            str8 = tinNhan;
            mGionhan2 = str11;
            mSoDT2 = mNgayNhan2;
            mNgayNhan = tinNhan3;
            mGionhan3 = mGionhan;
            if (getTinNhan.getInt(3) == 1) {
            }
            mNgay4 = str12;
            mSoDT = mSoDT3;
        } else {
            mNgay3 = str7;
            mSoTN2 = mSoTN5;
            mNgay2 = mNgay;
            mGionhan2 = str5;
            str8 = tinNhan;
            str10 = str3;
            str9 = str;
            mGionhan3 = mGionhan;
            mNgay4 = str6;
            mSoDT = mSoDT4;
            mSoDT2 = mNgayNhan2;
            mNgayNhan = tinNhan2;
        }
        Cursor getTin22222222222222222222222222222222222222222222 = GetData("Select * From tbl_tinnhanS WHERE ngay_nhan = '" + mNgay2 + mGionhan2 + mSoDT + str8 + mSoTN2);
        getTin22222222222222222222222222222222222222222222.moveToFirst();
        StringBuilder sb22222222222222222222222222222222222222222222 = new StringBuilder();
        sb22222222222222222222222222222222222222222222.append("Select * From tbl_chuyenthang WHERE sdt_nhan = '");
        sb22222222222222222222222222222222222222222222.append(mSoDT);
        sb22222222222222222222222222222222222222222222.append("'");
        cur = GetData(sb22222222222222222222222222222222222222222222.toString());
        cur.moveToFirst();
        Cursor chuyen32222222222222222222222222222222222222222222 = GetData("Select Om_Xi3 FROM so_Om WHERE id = 13");
        chuyen32222222222222222222222222222222222222222222.moveToFirst();
        if (cur.getCount() > 0) {
            getTin = getTin22222222222222222222222222222222222222222222;
            chuyen = chuyen32222222222222222222222222222222222222222222;
        } else if (chuyen32222222222222222222222222222222222222222222.getInt(0) == 0) {
            chuyen = chuyen32222222222222222222222222222222222222222222;
            if (getTin22222222222222222222222222222222222222222222.getInt(14) == 1) {
                StringBuilder sb19 = new StringBuilder();
                sb19.append("Select max(so_tin_nhan) From tbl_tinnhanS WHERE so_dienthoai = '");
                getTin = getTin22222222222222222222222222222222222222222222;
                sb19.append(cur.getString(4));
                sb19.append("' AND ngay_nhan = '");
                sb19.append(mNgay2);
                sb19.append("'");
                Cursor c12 = GetData(sb19.toString());
                c12.moveToFirst();
                int sotin4 = c12.getInt(0) + 1;
                if (cur.getString(3).indexOf("ZL") > -1) {
                    my_app = "ZL";
                } else if (cur.getString(3).indexOf("VB") > -1) {
                    my_app = "VB";
                } else if (cur.getString(3).indexOf("WA") > -1) {
                    my_app = "WA";
                } else if (cur.getString(3).indexOf(str9) > -1) {
                    my_app = "TL";
                } else {
                    my_app = "sms";
                }
                QueryData("Insert Into tbl_tinnhanS values (null, '" + getTinNhan.getString(1) + "', '" + getTinNhan.getString(2) + "',2, '" + cur.getString(3) + "', '" + cur.getString(4) + "', '" + my_app + "', " + sotin4 + ", '" + getTinNhan.getString(8) + "', null, '" + getTinNhan.getString(10) + "', 'ko',0,0,1, null)");
                if (my_app.indexOf(str10) > -1) {
                    SendSMS(cur.getString(4), "Tin " + sotin4 + ":\n" + getTinNhan.getString(8));
                } else if (getTinNhan.getString(6).indexOf(str9) > -1) {
                    final String tinNhan14 = "Tin " + sotin4 + ":\n" + getTinNhan.getString(8);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        /* class tamhoang.ldpro4.data.Database.AnonymousClass16 */

                        public void run() {
//                            new MainActivity();
//                            MainActivity.sendMessage(cur.getLong(4), tinNhan14);
                        }
                    });
                } else {
                    String tinNhan15 = "Tin " + sotin4 + ":\n" + getTinNhan.getString(8);
                    new NotificationReader().NotificationWearReader(cur.getString(4), tinNhan15);
                    QueryData("Insert into Chat_database Values( null,'" + mSoDT2 + "', '" + mGionhan3 + mNgayNhan + cur.getString(3) + "', '" + cur.getString(4) + "', '" + my_app + mNgay3 + tinNhan15 + mNgay4);
                }
                QueryData("Update tbl_tinnhanS set del_sms = 0 WHERE ngay_nhan = '" + mNgay2 + mGionhan2 + mSoDT + str8 + mSoTN2);
                if (c12 != null) {
                    c12.close();
                }
                TralaiSO(mID);
                if (getTin != null) {
                    getTin.close();
                }
                if (chuyen != null) {
                    chuyen.close();
                }
                if (getTinNhan != null) {
                    getTinNhan.close();
                }
                if (c != null) {
                    c.close();
                }
                if (cur != null) {
                    cur.close();
                    return;
                }
                return;
            }
            getTin = getTin22222222222222222222222222222222222222222222;
        } else {
            getTin = getTin22222222222222222222222222222222222222222222;
            chuyen = chuyen32222222222222222222222222222222222222222222;
        }
        TralaiSO(mID);
    }

    /* JADX INFO: Multiple debug info for r1v164 java.lang.String: [D('str3' java.lang.String), D('Laydan' java.lang.String)] */
    public void Tinhtien(String mDate) throws JSONException {
        int i;
        int i2;
        int i1;
        int l;
        boolean errKQ;
        Cursor c1;
        String BaCang;
        String BaCangDau = "";
        Cursor c12 = null;
        boolean errKQ2 = false;
        Cursor cursor = GetData("Select * From KetQua WHERE ngay = '" + mDate + "'");
        cursor.moveToFirst();
        int i3 = 2;
        while (true) {
            if (i3 >= 29) {
                break;
            } else if (cursor.getString(i3) == null) {
                errKQ2 = true;
                break;
            } else {
                i3++;
            }
        }
        if (!errKQ2) {
            String[][] mang2 = (String[][]) Array.newInstance(String.class, 1000, 8);
            String str = "Select * From KetQua WHERE ngay = '" + mDate + "'";
            Cursor cursor1 = GetData(str);
            cursor1.moveToFirst();
            int i4 = 0;
            while (true) {
                i = 1;
                if (i4 >= mang2.length) {
                    break;
                }
                mang2[i4][0] = "";
                mang2[i4][1] = "";
                mang2[i4][2] = "";
                mang2[i4][3] = "";
                mang2[i4][4] = "";
                mang2[i4][5] = "";
                mang2[i4][6] = "";
                mang2[i4][7] = "";
                i4++;
            }
            QueryData("Delete FROM tbl_tinnhanS WHERE Length(nd_phantich) <5 ");
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = 0 WHERE ngay_nhan = '" + mDate + "' AND the_loai <> 'tt' AND the_loai <> 'cn'");
            int i5 = 2;
            while (i5 < 29) {
                if (i5 <= i || i5 >= 12) {
                    BaCang = BaCangDau;
                    c1 = c12;
                    errKQ = errKQ2;
                    if (i5 > 11 && i5 < 22) {
                        mang2[Integer.parseInt(cursor1.getString(i5).substring(2, 4))][0] = mang2[Integer.parseInt(cursor1.getString(i5).substring(2, 4))][0] + "*";
                        mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][6] = mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][6] + "*";
                        QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'lo' AND so_chon = '" + cursor1.getString(i5).substring(2, 4) + "'");
                        QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'loa' AND so_chon = '" + cursor1.getString(i5).substring(0, 2) + "'");
                    } else if (i5 <= 21 || i5 >= 25) {
                        mang2[Integer.parseInt(cursor1.getString(i5))][0] = mang2[Integer.parseInt(cursor1.getString(i5))][0] + "*";
                        mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][6] = mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][6] + "*";
                        QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'lo' AND so_chon = '" + cursor1.getString(i5) + "'");
                        QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'loa' AND so_chon = '" + cursor1.getString(i5) + "'");
                    } else {
                        mang2[Integer.parseInt(cursor1.getString(i5).substring(1, 3))][0] = mang2[Integer.parseInt(cursor1.getString(i5).substring(1, 3))][0] + "*";
                        mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][6] = mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][6] + "*";
                        QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'lo' AND so_chon = '" + cursor1.getString(i5).substring(1, 3) + "'");
                        QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'loa' AND so_chon = '" + cursor1.getString(i5).substring(0, 2) + "'");
                    }
                } else {
                    BaCang = BaCangDau;
                    String[] strArr = mang2[Integer.parseInt(cursor1.getString(i5).substring(3, 5))];
                    StringBuilder sb = new StringBuilder();
                    c1 = c12;
                    errKQ = errKQ2;
                    sb.append(mang2[Integer.parseInt(cursor1.getString(i5).substring(3, 5))][0]);
                    sb.append("*");
                    strArr[0] = sb.toString();
                    mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][6] = mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][6] + "*";
                    QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'lo' AND so_chon = '" + cursor1.getString(i5).substring(3, 5) + "'");
                    QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'loa' AND so_chon = '" + cursor1.getString(i5).substring(0, 2) + "'");
                }
                if (i5 == 2) {
                    mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][1] = "*";
                    mang2[Integer.parseInt(cursor1.getString(i5).substring(3, 5))][2] = "*";
                    String BaCang2 = cursor1.getString(i5).substring(2, 5);
                    QueryData("Update tbl_soctS Set so_nhay = 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'bc' AND so_chon = '" + BaCang2 + "'");
                    mang2[Integer.parseInt(cursor1.getString(i5).substring(2, 5))][5] = "*";
                    String BaCangDau2 = cursor1.getString(i5).substring(0, 3);
                    QueryData("Update tbl_soctS Set so_nhay = 1 WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'bca' AND so_chon = '" + BaCangDau2 + "'");
                    mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 3))][7] = "*";
                    if (MainActivity.jSon_Setting.getInt("ap_man") > 0) {
                        int j = 0;
                        while (j < 10) {
                            if (Integer.parseInt(j + cursor1.getString(i5).substring(3, 5)) != Integer.parseInt(BaCang2)) {
                                QueryData("Update tbl_soctS Set so_nhay = 1, lan_an = " + (MainActivity.jSon_Setting.getInt("ap_man") * 1000) + " WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'bc' AND so_chon = '" + j + cursor1.getString(i5).substring(3, 5) + "'");
                            }
                            mang2[Integer.parseInt(j + cursor1.getString(i5).substring(3, 5))][5] = "*";
                            j++;
                            BaCangDau2 = BaCangDau2;
                        }
                    }
                    BaCangDau = BaCang2;
                } else {
                    BaCangDau = BaCang;
                }
                if (i5 == 3) {
                    mang2[Integer.parseInt(cursor1.getString(i5).substring(0, 2))][3] = "*";
                    mang2[Integer.parseInt(cursor1.getString(i5).substring(3, 5))][4] = "*";
                }
                i5++;
                c12 = c1;
                errKQ2 = errKQ;
                i = 1;
            }
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = -tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'dea' AND so_chon <> '" + cursor1.getString(2).substring(0, 2) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 1, ket_qua = diem * lan_an -tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'dea' AND so_chon ='" + cursor1.getString(2).substring(0, 2) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'dea' AND so_chon <> '" + cursor1.getString(2).substring(0, 2) + "' AND type_kh = 2");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Update tbl_soctS Set so_nhay = 1, ket_qua = -diem * lan_an +tong_tien WHERE ngay_nhan = '");
            sb2.append(mDate);
            sb2.append("' AND the_loai = 'dea' AND so_chon = '");
            String str2 = str;
            sb2.append(cursor1.getString(2).substring(0, 2));
            sb2.append("' AND type_kh = 2");
            QueryData(sb2.toString());
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = -tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'deb' AND so_chon <> '" + cursor1.getString(2).substring(3, 5) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 1, ket_qua = diem * lan_an -tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'deb' AND so_chon ='" + cursor1.getString(2).substring(3, 5) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'deb' AND so_chon <> '" + cursor1.getString(2).substring(3, 5) + "' AND type_kh = 2");
            QueryData("Update tbl_soctS Set so_nhay = 1, ket_qua = -diem * lan_an +tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'deb' AND so_chon = '" + cursor1.getString(2).substring(3, 5) + "' AND type_kh = 2");
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = -tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'dec' AND so_chon <> '" + cursor1.getString(3).substring(0, 2) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 1, ket_qua = diem * lan_an -tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'dec' AND so_chon ='" + cursor1.getString(3).substring(0, 2) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'dec' AND so_chon <> '" + cursor1.getString(3).substring(0, 2) + "' AND type_kh = 2");
            QueryData("Update tbl_soctS Set so_nhay = 1, ket_qua = -diem * lan_an +tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'dec' AND so_chon = '" + cursor1.getString(3).substring(0, 2) + "' AND type_kh = 2");
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = -tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'ded' AND so_chon <> '" + cursor1.getString(3).substring(3, 5) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 1, ket_qua = diem * lan_an -tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'ded' AND so_chon ='" + cursor1.getString(3).substring(3, 5) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'ded' AND so_chon <> '" + cursor1.getString(3).substring(3, 5) + "' AND type_kh = 2");
            QueryData("Update tbl_soctS Set so_nhay = 1, ket_qua = -diem * lan_an +tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'ded' AND so_chon = '" + cursor1.getString(3).substring(3, 5) + "' AND type_kh = 2");
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = -tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'det' AND so_chon <> '" + cursor1.getString(2).substring(3, 5) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 1, ket_qua = diem * lan_an-tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'det' AND so_chon ='" + cursor1.getString(2).substring(3, 5) + "' AND type_kh = 1");
            QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'det' AND so_chon <> '" + cursor1.getString(2).substring(3, 5) + "' AND type_kh = 2");
            QueryData("Update tbl_soctS Set so_nhay = 1, ket_qua = -diem * lan_an+tong_tien WHERE ngay_nhan = '" + mDate + "' AND the_loai = 'det' AND so_chon = '" + cursor1.getString(2).substring(3, 5) + "' AND type_kh = 2");
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Select * From tbl_soctS Where ngay_nhan = '");
            sb3.append(mDate);
            sb3.append("' AND (the_loai = 'xn' OR the_loai = 'xi')");
            Cursor cursor2 = GetData(sb3.toString());
            while (true) {
                i2 = -1;
                if (!cursor2.moveToNext()) {
                    break;
                } else if ("xi".indexOf(cursor2.getString(6)) > -1) {
                    String[] str22 = cursor2.getString(7).split(",");
                    boolean check = true;
                    int j2 = 0;
                    while (true) {
                        if (j2 >= str22.length) {
                            break;
                        } else if (mang2[Integer.parseInt(str22[j2])][0].length() == 0) {
                            check = false;
                            break;
                        } else {
                            j2++;
                        }
                    }
                    if (check) {
                        QueryData("Update tbl_soctS Set so_nhay = 1 WHERE ID = " + cursor2.getString(0));
                    }
                } else if ("xn".indexOf(cursor2.getString(6)) > -1) {
                    String[] str23 = cursor2.getString(7).split(",");
                    boolean check2 = false;
                    if (mang2[Integer.parseInt(str23[0])][0].length() > 1 || mang2[Integer.parseInt(str23[1])][0].length() > 1 || (mang2[Integer.parseInt(str23[0])][0].length() > 0 && mang2[Integer.parseInt(str23[1])][0].length() > 0)) {
                        check2 = true;
                    }
                    if (check2) {
                        QueryData("Update tbl_soctS Set so_nhay = 1 WHERE ID = " + cursor2.getString(0));
                    }
                }
            }
            Cursor cursor22 = GetData("Select * From tbl_soctS Where ngay_nhan = '" + mDate + "' AND the_loai = 'xia'");
            while (cursor22.moveToNext()) {
                String[] str24 = cursor22.getString(7).split(",");
                boolean check3 = true;
                int j3 = 0;
                while (true) {
                    if (j3 >= str24.length) {
                        break;
                    } else if (mang2[Integer.parseInt(str24[j3])][6].length() == 0) {
                        check3 = false;
                        break;
                    } else {
                        j3++;
                    }
                }
                if (check3) {
                    QueryData("Update tbl_soctS Set so_nhay = 1 WHERE ID = " + cursor22.getString(0));
                }
            }
            if (MainActivity.jSon_Setting.getInt("tra_thuong_lo") > 0) {
                int Sonhaymax = MainActivity.jSon_Setting.getInt("tra_thuong_lo") + 1;
                QueryData("Update tbl_soctS Set so_nhay = " + Sonhaymax + " Where so_nhay > " + Sonhaymax);
            }
            QueryData("Update tbl_soctS set ket_qua = diem * lan_an * so_nhay - tong_tien WHERE ngay_nhan = '" + mDate + "' AND type_kh = 1 AND the_loai <> 'tt' AND the_loai <> 'cn'");
            QueryData("Update tbl_soctS set ket_qua = -diem * lan_an * so_nhay + tong_tien WHERE ngay_nhan = '" + mDate + "' AND type_kh = 2 AND the_loai <> 'tt' AND the_loai <> 'cn'");
            QueryData("UPDATE tbl_tinnhanS set tinh_tien = 0 Where ngay_nhan = '" + mDate + "'");
            Cursor cursor23 = GetData("Select * From tbl_tinnhanS Where ngay_nhan = '" + mDate + "' AND tinh_tien = 0 AND phat_hien_loi = 'ok'");
            while (cursor23.moveToNext()) {
                if (cursor23.getInt(12) == 0) {
                    String str3 = cursor23.getString(10);
                    if (str3.indexOf("Bỏ ") == 0) {
                        str3 = str3.substring(str3.indexOf("\n") + 1);
                    }
                    for (int i6 = 0; i6 < 9; i6++) {
                        str3 = str3.replaceAll("\\*", "");
                    }
                    String Laydan = "";
                    int k = 0;
                    int i12 = -1;
                    while (true) {
                        int indexOf = str3.indexOf("\n", i12 + 1);
                        int i13 = indexOf;
                        if (indexOf == i2) {
                            break;
                        }
                        String strT = str3.substring(k);
                        String str1 = strT.substring(0, strT.indexOf("\n") + 1);
                        int l2 = str1.indexOf("\n") + 1;
                        int k2 = k + l2;
                        if (str1.indexOf("de dau db") > -1) {
                            String[] str25 = str1.substring(10, str1.indexOf("x")).split(",");
                            String str32 = str1.substring(str1.indexOf("x") + 2, str1.indexOf("\n"));
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(Laydan);
                            l = l2;
                            sb4.append("de dau db:");
                            String Laydan2 = sb4.toString();
                            int j4 = 0;
                            while (j4 < str25.length) {
                                Laydan2 = Laydan2 + str25[j4] + mang2[Integer.parseInt(str25[j4])][1] + ",";
                                j4++;
                                i13 = i13;
                            }
                            i1 = i13;
                            Laydan = Laydan2 + "x" + str32 + "\n";
                        } else {
                            l = l2;
                            i1 = i13;
                            if (str1.indexOf("de dit db") > -1) {
                                String str4 = str1.substring(10, str1.indexOf("x"));
                                String[] str26 = str4.split(",");
                                String str33 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan3 = Laydan + "de dit db:";
                                int j5 = 0;
                                while (j5 < str26.length) {
                                    Laydan3 = Laydan3 + str26[j5] + mang2[Integer.parseInt(str26[j5])][2] + ",";
                                    j5++;
                                    str4 = str4;
                                }
                                Laydan = Laydan3 + "x" + str33 + "\n";
                            } else if (str1.indexOf("de 8") > -1) {
                                String str42 = str1.substring(5, str1.indexOf("x"));
                                String[] str27 = str42.split(",");
                                String str34 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan4 = Laydan + "de 8:";
                                int j6 = 0;
                                while (j6 < str27.length) {
                                    Laydan4 = Laydan4 + str27[j6] + mang2[Integer.parseInt(str27[j6])][2] + ",";
                                    j6++;
                                    str42 = str42;
                                }
                                Laydan = Laydan4 + "x" + str34 + "\n";
                            } else if (str1.indexOf("de dau nhat") > -1) {
                                String str43 = str1.substring(12, str1.indexOf("x"));
                                String[] str28 = str43.split(",");
                                String str35 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan5 = Laydan + "de dau nhat:";
                                int j7 = 0;
                                while (j7 < str28.length) {
                                    Laydan5 = Laydan5 + str28[j7] + mang2[Integer.parseInt(str28[j7])][3] + ",";
                                    j7++;
                                    str43 = str43;
                                }
                                Laydan = Laydan5 + "x" + str35 + "\n";
                            } else if (str1.indexOf("de dit nhat") > -1) {
                                String str44 = str1.substring(12, str1.indexOf("x"));
                                String[] str29 = str44.split(",");
                                String str36 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan6 = Laydan + "de dit nhat:";
                                int j8 = 0;
                                while (j8 < str29.length) {
                                    Laydan6 = Laydan6 + str29[j8] + mang2[Integer.parseInt(str29[j8])][4] + ",";
                                    j8++;
                                    str44 = str44;
                                }
                                Laydan = Laydan6 + "x" + str36 + "\n";
                            } else if (str1.indexOf("bc dau") > -1) {
                                String str45 = str1.substring(7, str1.indexOf(",x"));
                                String[] str210 = str45.split(",");
                                String str37 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan7 = Laydan + "bc dau:";
                                int j9 = 0;
                                while (j9 < str210.length) {
                                    Laydan7 = Laydan7 + str210[j9] + mang2[Integer.parseInt(str210[j9])][7] + ",";
                                    j9++;
                                    str45 = str45;
                                }
                                Laydan = Laydan7 + "x" + str37 + "\n";
                            } else if (str1.indexOf("bc") > -1) {
                                String str46 = str1.substring(3, str1.indexOf("x"));
                                String[] str211 = str46.split(",");
                                String str38 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan8 = Laydan + "bc:";
                                int j10 = 0;
                                while (j10 < str211.length) {
                                    Laydan8 = Laydan8 + str211[j10] + mang2[Integer.parseInt(str211[j10])][5] + ",";
                                    j10++;
                                    str46 = str46;
                                }
                                Laydan = Laydan8 + "x" + str38 + "\n";
                            } else if (str1.indexOf("lo dau") > -1) {
                                String str47 = str1.substring(7, str1.indexOf("x"));
                                String[] str212 = str47.split(",");
                                String str39 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan9 = Laydan + "lo dau:";
                                int j11 = 0;
                                while (j11 < str212.length) {
                                    Laydan9 = Laydan9 + str212[j11] + mang2[Integer.parseInt(str212[j11])][6] + ",";
                                    j11++;
                                    str47 = str47;
                                }
                                Laydan = Laydan9 + "x" + str39 + "\n";
                            } else if (str1.indexOf("lo") > -1) {
                                String str48 = str1.substring(3, str1.indexOf("x"));
                                String[] str213 = str48.split(",");
                                String str310 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan10 = Laydan + "lo:";
                                int j12 = 0;
                                while (j12 < str213.length) {
                                    Laydan10 = Laydan10 + str213[j12] + mang2[Integer.parseInt(str213[j12])][0] + ",";
                                    j12++;
                                    str48 = str48;
                                }
                                Laydan = Laydan10 + "x" + str310 + "\n";
                            } else if (str1.indexOf("xien dau") > -1) {
                                String str49 = str1.substring(9, str1.indexOf(",x"));
                                str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String[] str214 = str49.split(",");
                                boolean check4 = true;
                                int j13 = 0;
                                while (true) {
                                    if (j13 >= str214.length) {
                                        break;
                                    } else if (mang2[Integer.parseInt(str214[j13])][6].length() == 0) {
                                        check4 = false;
                                        break;
                                    } else {
                                        j13++;
                                    }
                                }
                                Laydan = check4 ? Laydan + str1.substring(0, str1.indexOf("\n")) + "*\n" : Laydan + str1;
                            } else if (str1.indexOf("xi") > -1) {
                                String str410 = str1.substring(3, str1.lastIndexOf("x"));
                                str1.substring(str1.indexOf(",x") + 2, str1.lastIndexOf("\n"));
                                String[] str215 = str410.split(",");
                                boolean check5 = true;
                                int j14 = 0;
                                while (true) {
                                    if (j14 >= str215.length) {
                                        break;
                                    } else if (mang2[Integer.parseInt(str215[j14])][0].length() == 0) {
                                        check5 = false;
                                        break;
                                    } else {
                                        j14++;
                                    }
                                }
                                Laydan = check5 ? Laydan + str1.substring(0, str1.indexOf("\n")) + "*\n" : Laydan + str1;
                            } else if (str1.indexOf("xn") > -1) {
                                String str411 = str1.substring(3, str1.indexOf(",x"));
                                str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String[] str216 = str411.split(",");
                                boolean check6 = false;
                                if (mang2[Integer.parseInt(str216[0])][0].length() > 1 || mang2[Integer.parseInt(str216[1])][0].length() > 1 || (mang2[Integer.parseInt(str216[0])][0].length() > 0 && mang2[Integer.parseInt(str216[1])][0].length() > 0)) {
                                    check6 = true;
                                }
                                Laydan = check6 ? Laydan + str1.substring(0, str1.indexOf("\n")) + "*\n" : Laydan + str1;
                            } else if (str1.indexOf("xq dau") > -1) {
                                String str412 = str1.substring(7, str1.lastIndexOf("x"));
                                String[] str217 = str412.split(",");
                                String str311 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan11 = Laydan + "xq dau:";
                                int j15 = 0;
                                while (j15 < str217.length) {
                                    Laydan11 = Laydan11 + str217[j15] + mang2[Integer.parseInt(str217[j15])][6] + ",";
                                    j15++;
                                    str412 = str412;
                                }
                                Laydan = Laydan11 + "x" + str311 + "\n";
                            } else if (str1.indexOf("xq") > -1) {
                                String[] str218 = str1.substring(3, str1.indexOf(",x")).split(",");
                                String str312 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                String Laydan12 = Laydan + "xq:";
                                int j16 = 0;
                                while (j16 < str218.length) {
                                    Laydan12 = Laydan12 + str218[j16] + mang2[Integer.parseInt(str218[j16])][0] + ",";
                                    j16++;
                                    str1 = str1;
                                }
                                Laydan = Laydan12 + "x" + str312 + "\n";
                            }
                        }
                        str3 = str3;
                        cursor1 = cursor1;
                        k = k2;
                        i12 = i1;
                        i2 = -1;
                    }
                    str2 = str3;
                    QueryData("Update tbl_tinnhanS Set nd_phantich ='" + Laydan + "', tinh_tien = 1 WHERE ID = " + cursor23.getString(0));
                    i2 = -1;
                } else {
                    i2 = -1;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            if (cursor23 != null) {
                cursor23.close();
            }
        }
    }

    public void LayDanhsachKH() {
        MainActivity.DSkhachhang = new ArrayList<>();
        Cursor cursor = GetData("Select * From tbl_kh_new WHERE type_kh <> 2");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MainActivity.DSkhachhang.add(cursor.getString(1));
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public void Create_table_Chat() {
        try {
            Cursor cursor = GetData("Select * From Chat_database");
            if (cursor.getColumnCount() == 8) {
                QueryData("Drop table Chat_database");
            }
            cursor.close();
        } catch (Exception e) {
        }
        QueryData("CREATE TABLE IF NOT EXISTS Chat_database( ID INTEGER PRIMARY KEY AUTOINCREMENT,\n ngay_nhan DATE NOT NULL,\n gio_nhan VARCHAR(8) NOT NULL,\n type_kh INTEGER DEFAULT 0,\n ten_kh VARCHAR(20) NOT NULL,\n so_dienthoai VARCHAR(20) NOT NULL,\n use_app VARCHAR(20) NOT NULL,\n nd_goc VARCHAR(500) DEFAULT NULL,\n del_sms INTEGER DEFAULT 0);");
    }

    public void Creat_TinNhanGoc() {
        QueryData("CREATE TABLE IF NOT EXISTS tbl_tinnhanS(\n ID INTEGER PRIMARY KEY AUTOINCREMENT,\n ngay_nhan DATE NOT NULL,\n gio_nhan VARCHAR(8) NOT NULL,\n type_kh INTEGER DEFAULT 0,\n ten_kh VARCHAR(20) NOT NULL,\n so_dienthoai VARCHAR(20) NOT NULL,\n use_app VARCHAR(20) NOT NULL,\n so_tin_nhan INTEGER DEFAULT 0,\n nd_goc VARCHAR(500) DEFAULT NULL,\n nd_sua VARCHAR(500) DEFAULT NULL,\n nd_phantich VARCHAR(500) DEFAULT NULL,\n phat_hien_loi VARCHAR(100) DEFAULT NULL,\n tinh_tien INTEGER DEFAULT 0,\n ok_tn INTEGER DEFAULT 0,\n del_sms INTEGER DEFAULT 0,  phan_tich TEXT);");
    }

    public void Creat_SoCT() {
        QueryData("CREATE TABLE IF NOT EXISTS tbl_soctS(\n ID INTEGER PRIMARY KEY AUTOINCREMENT,\n ngay_nhan DATE NOT NULL,\n type_kh INTEGER DEFAULT 1,\n ten_kh VARCHAR(20) NOT NULL,\n so_dienthoai VARCHAR(20) NOT NULL,\n so_tin_nhan INTEGER DEFAULT 0,\n the_loai VARCHAR(4) DEFAULT NULL,\n so_chon VARCHAR(20) DEFAULT NULL,\n diem DOUBLE DEFAULT 0,\n diem_quydoi DOUBLE DEFAULT 0,\n diem_khachgiu DOUBLE DEFAULT 0,\n diem_dly_giu DOUBLE DEFAULT 0,\n diem_ton DOUBLE DEFAULT 0,\n gia DOUBLE DEFAULT 0,\n lan_an DOUBLE DEFAULT 0,\n so_nhay DOUBLE DEFAULT 0,\n tong_tien DOUBLE DEFAULT 0,\n ket_qua DOUBLE DEFAULT 0)");
        QueryData("CREATE TABLE IF NOT EXISTS tbl_chuyenthang ( ID INTEGER PRIMARY KEY AUTOINCREMENT, kh_nhan VARCHAR(20) NOT NULL, sdt_nhan VARCHAR(15) NOT NULL, kh_chuyen VARCHAR(20) NOT NULL, sdt_chuyen VARCHAR(15) NOT NULL)");
    }

    public void Creat_So_Om() {
        QueryData("CREATE TABLE IF NOT EXISTS So_om(  ID INTEGER PRIMARY KEY AUTOINCREMENT,  So VARCHAR(2) DEFAULT NULL,  Om_DeA INTEGER DEFAULT 0,  Om_DeB INTEGER DEFAULT 0,  Om_DeC INTEGER DEFAULT 0,  Om_DeD INTEGER DEFAULT 0,  Om_Lo INTEGER Default 0,  Om_Xi2 INTEGER Default 0,  Om_Xi3 INTEGER Default 0,  Om_Xi4 INTEGER Default 0,  Om_bc INTEGER Default 0,  Sphu1 VARCHAR(200) DEFAULT NULL,  Sphu2 VARCHAR(200) DEFAULT NULL)");
    }

    public void Creat_Chaytrang_acc() {
        QueryData("CREATE TABLE IF NOT EXISTS tbl_chaytrang_acc( \n Username VARCHAR(30) PRIMARY KEY,\n Password VARCHAR(20) NOT NULL,\n Setting TEXT NOT NULL,\n Status VARCHAR(20) DEFAULT NULL)");
    }

    public void Creat_Chaytrang_ticket() {
        QueryData("CREATE TABLE IF NOT EXISTS tbl_chaytrang_ticket( ID INTEGER PRIMARY KEY AUTOINCREMENT, \nngay_nhan DATE NOT NULL, \nCreatedAt VARCHAR(20) DEFAULT NULL, \nUsername VARCHAR(30), \nTicketNumber INTEGER DEFAULT 0, \nGameType INTEGER DEFAULT 0,\nNumbers Text DEFAULT NULL, \nPoint DOUBLE DEFAULT 0, \nAmount DOUBLE DEFAULT 0, \nCancelledAt INTEGER DEFAULT 1)");
    }

    public void ThayThePhu() {
        QueryData("CREATE TABLE IF NOT EXISTS thay_the_phu(  ID INTEGER PRIMARY KEY AUTOINCREMENT,  str VARCHAR(20) NOT NULL,  str_rpl VARCHAR(20) NOT NULL)");
    }

    public void List_Khach_Hang() {
        QueryData("CREATE TABLE IF NOT EXISTS tbl_kh_new (ten_kh VARCHAR(30) PRIMARY KEY,sdt VARCHAR(15),use_app Varchar(10), type_kh INTEGER default 0, type_pt Integer default 0, tbl_MB TEXT, tbl_XS TEXT)");
        QueryData("Delete From tbl_kh_new Where substr(sdt,0,3) = 'TL'");
    }

    public void Another_setting() {
        QueryData("CREATE TABLE IF NOT EXISTS tbl_Setting(\n ID INTEGER PRIMARY KEY AUTOINCREMENT,\n Setting TEXT)");
        Cursor cursor = GetData("SELECT * FROM 'tbl_Setting'");
        if (cursor.getCount() == 0) {
            JSONObject setting = new JSONObject();
            try {
                setting.put("ap_man", 0);
                setting.put("chuyen_xien", 0);
                setting.put("lam_tron", 0);
                setting.put("gioi_han_tin", 1);
                setting.put("tin_qua_gio", 0);
                setting.put("tin_trung", 0);
                setting.put("kieu_bao_cao", 0);
                setting.put("bao_cao_so", 0);
                setting.put("tra_thuong_lo", 0);
                setting.put("canhbaodonvi", 0);
                setting.put("tudongxuly", 0);
                setting.put("tachxien_tinchot", 0);
                setting.put("baotinthieu", 0);
                QueryData("insert into tbl_Setting Values( null,'" + setting.toString() + "')");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cursor.close();
            return;
        }
        try {
            cursor.moveToFirst();
            JSONObject setting2 = new JSONObject(cursor.getString(1));
            if (!setting2.has("canhbaodonvi")) {
                setting2.put("canhbaodonvi", 0);
            }
            if (!setting2.has("tachxien_tinchot")) {
                setting2.put("tachxien_tinchot", 0);
            }
            if (!setting2.has("baotinthieu")) {
                setting2.put("baotinthieu", 0);
            }
            QueryData("Update tbl_Setting set Setting = '" + setting2.toString() + "'");
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public void Save_Setting(String Keys, int values) {
        Cursor cursor = GetData("Select * From tbl_Setting WHERE ID = 1");
        if (cursor != null && cursor.moveToFirst()) {
            try {
                MainActivity.jSon_Setting.put(Keys, values);
                QueryData("Update tbl_Setting set Setting = '" + MainActivity.jSon_Setting.toString() + "'");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
    }

    public void Bang_KQ() {
        QueryData("CREATE TABLE IF NOT EXISTS KetQua(  ID INTEGER PRIMARY KEY AUTOINCREMENT,  Ngay DATE DEFAULT NULL,  GDB VARCHAR(5) DEFAULT NULL,  G11 VARCHAR(5) DEFAULT NULL,  G21 VARCHAR(5) DEFAULT NULL,  G22 VARCHAR(5) DEFAULT NULL,  G31 VARCHAR(5) DEFAULT NULL,  G32 VARCHAR(5) DEFAULT NULL,  G33 VARCHAR(5) DEFAULT NULL,  G34 VARCHAR(5) DEFAULT NULL,  G35 VARCHAR(5) DEFAULT NULL,  G36 VARCHAR(5) DEFAULT NULL,  G41 VARCHAR(4) DEFAULT NULL,  G42 VARCHAR(4) DEFAULT NULL,  G43 VARCHAR(4) DEFAULT NULL,  G44 VARCHAR(4) DEFAULT NULL,  G51 VARCHAR(4) DEFAULT NULL,  G52 VARCHAR(4) DEFAULT NULL,  G53 VARCHAR(4) DEFAULT NULL,  G54 VARCHAR(4) DEFAULT NULL,  G55 VARCHAR(4) DEFAULT NULL,  G56 VARCHAR(4) DEFAULT NULL,  G61 VARCHAR(3) DEFAULT NULL,  G62 VARCHAR(3) DEFAULT NULL,  G63 VARCHAR(3) DEFAULT NULL,  G71 VARCHAR(2) DEFAULT NULL,  G72 VARCHAR(2) DEFAULT NULL,  G73 VARCHAR(2) DEFAULT NULL,  G74 VARCHAR(2) DEFAULT NULL);");
    }

    public Cursor GetData(String sql) {
        return getReadableDatabase().rawQuery(sql, null);
    }

    public void QueryData(String sql) {
        getWritableDatabase().execSQL(sql);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
        if (oldVersion > 1) {
        }
    }
}