package tamhoang.ldpro4.data;

import static android.Manifest.permission_group.SMS;
import static android.content.ContentValues.TAG;

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
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.internal.view.SupportMenu;

import org.json.JSONException;
import org.json.JSONObject;

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

import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationReader;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.constants.Constants;
import tamhoang.ldpro4.data.model.Chat;
import tamhoang.ldpro4.data.model.KhachHang;
import tamhoang.ldpro4.data.model.TinNhanS;

public class Database extends SQLiteOpenHelper {
//    static final /* synthetic */ boolean $assertionsDisabled = false;
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

    public void SendSMS(String Sdt, String Mess) { // gui sms
        SmsManager sms = SmsManager.getDefault();
        sms.sendMultipartTextMessage(Sdt, null, sms.divideMessage(Mess), null, null);
    }

    public void Update_TinNhanGoc(int id, int type_kh) throws Throwable {

        TinNhanS tinNhanS_s = BriteDb.INSTANCE.selectTinNhanS("id = " + id);
        if (tinNhanS_s.getPhat_hien_loi().contains("ok")) {

            String nd_phantich = tinNhanS_s.getNd_phantich();
            nd_phantich = nd_phantich.replaceAll("\\*", "");

            QueryData("Update tbl_tinnhanS set nd_phantich = '" + nd_phantich + "', nd_sua = '" + nd_phantich + "' WHERE id = " + id);
            NhapSoChiTiet(id);
            QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ok' WHERE id = " + id);
        } else {
            String nd_phantich = Congthuc.fixTinNhan1(Congthuc.convertKhongDau(tinNhanS_s.getNd_phantich()));
            String Loi = null;
            Cursor cursor = GetData("Select * From thay_the_phu");
            while (cursor.moveToNext()) {
                nd_phantich = nd_phantich.replaceAll(cursor.getString(1), cursor.getString(2)).replace("  ", " ");
            }

            if (!cursor.isClosed()) cursor.close();

            nd_phantich = Congthuc.fixTinNhan1(nd_phantich);
            QueryData("Update tbl_tinnhanS set nd_phantich = '" + nd_phantich + "', nd_sua = '" + nd_phantich + "' WHERE id = " + id);
            if (nd_phantich.contains("bo") && !nd_phantich.contains("bor")) {
                for (int j = nd_phantich.indexOf("bo") + 3; j < nd_phantich.length(); j++) {
                    String ch = nd_phantich.substring(j, j + 1);
                    if (!ch.contains(" ") && !Congthuc.isNumeric(ch)) {
                        Loi = "Không hiểu " + nd_phantich.substring(nd_phantich.indexOf("bo"));
                    }
                }
            }

            if (nd_phantich.contains("Không hiểu")) {
                QueryData("Update tbl_tinnhanS set nd_phantich = '" + nd_phantich + "', nd_sua = '" + nd_phantich + "',  phat_hien_loi ='" + Loi + "' Where id = " + id);
                createNotification(nd_phantich, mcontext);
            } else {
                NhanTinNhan(id, type_kh);
                tinNhanS_s = BriteDb.INSTANCE.selectTinNhanS("id = " + id);
                String phat_hien_loi = tinNhanS_s.getPhat_hien_loi();

                if (phat_hien_loi.contains("Không hiểu")) {
                    createNotification(phat_hien_loi, mcontext);
                } else {
                    NhapSoChiTiet(id);
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
        final String KHONG_HIEU = "Không hiểu";
        final String KHONG_HIEU_ = "Không hiểu ";
        final String XQ = "xq";
        final String XN = "xn";
        final String FONT = "</font>";
        final String LDPRO = "ldpro";
        final String XI = "xi";
        final String LO = "lo";
        final String _N = "\n";
        final String T = "t";

        String str_Err1 = null;
        String phatHienLoi = null;
        int Dem_error = 0;
        StringBuilder ndPhantichLoi;
        JSONObject jsonDan = null;
        String Bor;
        int k;
        int k2 = 0;
        mang = (String[][]) Array.newInstance(String.class, 1000, 6);
        Cursor cursor2 = GetData("Select nd_phantich, ten_kh, ok_tn, ngay_nhan From tbl_tinnhanS WHERE id = " + id);
        cursor2.moveToFirst();
        int rw = 0;
        String str44 = " ";
        boolean quagio = false;
        String dayso = null;
        String nd_phantich = cursor2.getString(0).replace("ldpro", "").replace(FONT, "").replaceAll("-", ",").replaceAll("/", ";")
                .replaceAll("\n", " ").replaceAll("\\.", ",").replaceAll("x ", " x ").replaceAll("ba bc", "bc").replaceAll(";,", ";")
                .replaceAll("; ,", ";").replaceAll("; lo", LO).replaceAll(";lo", LO).replaceAll("; de", "de").replaceAll(";de", "de")
                .replaceAll("; xi", XI).replaceAll(";xi", XI).replaceAll("; bc", "bc").replaceAll(";bc", "bc").replaceAll("; xq", "xq").replaceAll(";xq", "xq")
                .replaceAll("; xn", "xn").replaceAll(";xn", "xn").replaceAll("bo", " bo").replaceAll("duoi", "dit").replaceAll("dit 10", "duoi 10").replaceAll("tong dit", "tong <");

        String query = "Select * From tbl_kh_new Where ten_kh = '" + cursor2.getString(1) + "'";
        Cursor getKhachHang = GetData(query);
        getKhachHang.moveToFirst();
        JSONObject jSONObject = new JSONObject(getKhachHang.getString(5));
        json = jSONObject;
        caidat_tg = jSONObject.getJSONObject("caidat_tg");

        nd_phantich = Congthuc.Xuly_DauTN(nd_phantich);
        if (nd_phantich.startsWith("tin"))
            nd_phantich.replaceFirst("tin", T);
        nd_phantich = Congthuc.fixTinNhan(nd_phantich);
        nd_phantich = nd_phantich + " ";
        int i1 = -1;
        while (true) {// nd_phantich : "abc tin 123456 def" => "abc def"

            int indexOf = nd_phantich.indexOf("tin", i1 + 1);
            i1 = indexOf;// vi tri cua "tin" trong nd_phantich
            if (indexOf == -1) {
                break;
            }
            int i6 = i1 + 5; //i1 + 3 là vi tri ket thuc cua tin trong nd_phantich
            while (i6 < i1 + 10 && Congthuc.isNumeric(nd_phantich.substring(i1 + 4, i6))) {// sau chu "tin" kiem tra 6 chu tiep theo deu là so thi dung lai
                i6++;
            }
            if (i6 - i1 > 5) {
                nd_phantich = nd_phantich.substring(0, i1) + nd_phantich.substring(i6);
            }
        }

        nd_phantich = nd_phantich.trim();
        for (int i7 = 6; i7 > 0 ; i7--) { // nd_phantich: "T 123 abc" => " abc"
            String Sss = nd_phantich.substring(0, i7);
            if (Sss.trim().contains(T)) {
                String Sss2 = Sss.replaceAll(T, "").replaceAll(" ", "").replaceAll(",", "");
                if (Congthuc.isNumeric(Sss2)) {
                    nd_phantich = nd_phantich.substring(i7);
                }
            }
        }

        if (!nd_phantich.contains(KHONG_HIEU)) {
            if (nd_phantich.length() < 8) {
                str_Err1 = KHONG_HIEU_ + nd_phantich;
            } else {
                String substring = nd_phantich.substring(0, 5);
                if (!substring.contains("de") && !substring.contains(LO) && !substring.contains(XI) && !substring.contains(FONT)
                        && !substring.contains("hc") && !substring.contains("xn")
                        && !substring.contains("bc") && !substring.contains("xg"))
                    str_Err1 = "Không hiểu dạng";

                if (nd_phantich.contains(" bo "))
                    str_Err1 = "Không hiểu bo ";
            }

            String phanTichTN = Congthuc.PhanTichTinNhan(nd_phantich);
            if (!phanTichTN.contains(KHONG_HIEU))
                str_Err1 = phanTichTN;

            if (Congthuc.CheckTime(caidat_tg.getString("tg_loxien")) && !Congthuc.CheckTime("18:30")) {
                quagio = true;
            }

            if (!str_Err1.contains(KHONG_HIEU)) {
                phanTichTN = phanTichTN.replaceAll(" , ", " ").replaceAll(" ,", " ");

                for(int i = 1; i < 10; i++) {
                    phanTichTN = phanTichTN.replaceAll(" {2}", " ").replaceAll(",,", ",");
                }
                phanTichTN = phanTichTN.trim() + " ";
                int k4 = 0;
                String theodoi7 = "";
                int i11 = -1;
                while (true) {
                    int indexX = phanTichTN.indexOf(" x ", i11 + 1);
                    i11 = indexX;
                    if (indexX == -1) break;

                    String tien = "";
                    int i22 = indexX;
                    while (true) {
                        if (i22 >= phanTichTN.length()) break;

                        if (phanTichTN.charAt(i22) == ' ' && tien.length() > 0) break;

                        if ("0123456789,tr".contains(phanTichTN.substring(i22, i22 + 1))) {
                            tien = tien + phanTichTN.charAt(i22);
                        }
                        i22++;
                    }
                    String dtien = "";
                    int i33 = i22;
                    while (i33 < phanTichTN.length() && (Character.isLetter(phanTichTN.charAt(i33)) || dtien.length() <= 0)) {
                        dtien = dtien + phanTichTN.charAt(i33);
                        i33++;
                    }
                    if (i22 == i33) {
                        i33--;
                    }

                    if (dtien.contains("dau") && dtien.contains("dit") && dtien.contains("tong") && dtien.contains("cham") && dtien.contains("dan") && dtien.contains("boj")
                            && dtien.contains("lo") && dtien.contains("de") && dtien.contains("xi") && dtien.contains("xn") && dtien.contains("hc") && dtien.contains("xg")
                            && dtien.contains(" x") && dtien.contains("kep") && dtien.contains("sat") && dtien.contains("to") && dtien.contains("nho") && dtien.contains("chan")
                            && dtien.contains("le") && dtien.contains("ko") && dtien.contains("chia") && dtien.contains("duoi") && dtien.contains("be")) {

                    } else {

                        if (dtien.contains("x ")) {// impossible
                            int i44 = i22 - 1;
                            while (true) {
                                if (i44 <= 0) {
                                    break;
                                } else if (!Congthuc.isNumeric(phanTichTN.substring(i44, i44 + 1))) {
                                    dayso = phanTichTN.substring(k4, i44 + 1);
                                    k4 = i44 + 1;
                                    theodoi7 = phanTichTN.substring(k4);
                                    break;
                                } else {
                                    i44--;
                                }
                            }
                        } else {
                            dayso = phanTichTN.substring(k4, i33);
                            k4 = i33;
                        }
                        k = rw + 1;
                        String dayso_c = dayso.trim();
                        if (!dayso_c.startsWith(T)) {
                            if (dayso_c.length() > 6) {
                                for (int i = 6; i > 0; i--) {
                                    String ss = dayso_c.substring(0, i);
                                    if (ss.trim().contains(T)) {
                                        if (Congthuc.isNumeric(ss.replaceAll(T, "").replaceAll(" ", "").replaceAll(",", "")))
                                            dayso = " " + dayso_c.substring(i + 1) + " ";
                                    }
                                }
                                mang[k][0] = dayso;
                                k2 = k4;
                                if (dayso.contains("loa")) mang[k][1] = "lo dau";
                                else if (dayso.contains(LO)) mang[k][1] = LO;
                                else if (dayso.contains("dea")) mang[k][1] = "de dau db";
                                else if (dayso.contains("deb")) mang[k][1] = "de dit db";
                                else if (dayso.contains("det")) mang[k][1] = "de 8";
                                else if (dayso.contains("hc")) mang[k][1] = "hai cua";
                                else if (dayso.contains("xn")) mang[k][1] = "xn";
                                else if (dayso.contains("dec")) mang[k][1] = "de dau nhat";
                                else if (dayso.contains("ded")) mang[k][1] = "de dit nhat";
                                else if (dayso.contains("de ")) mang[k][1] = "de dit db";
                                else if (dayso.contains("bca")) mang[k][1] = "bc dau";
                                else if (dayso.contains("xia")) mang[k][1] = "xien dau";
                                else if (dayso.contains("xi")) mang[k][1] = "xi";
                                else if (dayso.contains("xqa")) mang[k][1] = "xqa";
                                else {
                                    mang[k][1] = dayso.contains(FONT) ? FONT : mang[k - 1][1];
                                }
                                if (dayso.contains(" x ")) {
                                    if (dayso.trim().indexOf("x ") < 2) {
                                        str44 = KHONG_HIEU_;
                                    } else {
                                        mang[k][2] = dayso.substring(0, dayso.indexOf(" x ")).trim();
                                        mang[k][3] = dayso.substring(dayso.indexOf(" x "));
                                        XulyMang(k);
                                        BaoLoiTien(k);
                                        if (mang[k][4] != null) {
                                            String ketquaDaySo = mang[k][4].trim();
                                            if (ketquaDaySo.contains(KHONG_HIEU)) {
                                                Dem_error++;
                                            } else {
                                                if (ketquaDaySo.charAt(ketquaDaySo.length() - 1) == ',') {
                                                    mang[k][4] = ketquaDaySo.substring(0, ketquaDaySo.length() - 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        mang[k][0] = dayso;
                        rw = k;
                        k4 = k2;
                    }

                }

                if (theodoi7.length() > 0) {
                    String theodoi2 = theodoi7.replaceAll(str44, "").replaceAll("\\.", "").replaceAll(",", "").replaceAll(";", "");
                    if (theodoi2.length() > 0) {
                        mang[rw + 1][0] = theodoi7;
                        mang[rw + 1][2] = theodoi7;
                        mang[rw + 1][3] = theodoi7;
                        mang[rw + 1][4] = KHONG_HIEU_ + theodoi7;
                        BaoLoiDan(rw + 1);
                    }
                }
            } else {
                mang[1][0] = nd_phantich;
                mang[1][4] = str_Err1;
                if (str_Err1.contains("Không hiểu dạng")) {
                    mang[1][4] = KHONG_HIEU_ + nd_phantich.substring(0, 5);
                }
                mang[1][2] = nd_phantich;
                mang[1][3] = "";

                BaoLoiDan(1);

            }

            ndPhantichLoi = new StringBuilder();
            int k322 = 0;

            for (int i2 = 1; i2 < 1000; i2++) {
                if (mang[i2][0] == null) break;
                if (mang[i2][4].contains(KHONG_HIEU) || mang[i2][5].contains(KHONG_HIEU)) {
                    Dem_error++;
                    if (mang[i2][4].contains(KHONG_HIEU))
                        phatHienLoi = mang[i2][4];
                    else
                        phatHienLoi = mang[i2][5];

                    if (!mang[i2][0].contains(LDPRO)) {
                        String str = phatHienLoi.replaceAll(KHONG_HIEU, "").trim();
                        mang[i2][0] = mang[i2][0].replaceAll(str, LDPRO + str + FONT);
                    }
                }
                ndPhantichLoi = ndPhantichLoi.append(mang[i2][0]);

            }

            if (Dem_error == 0) {
                boolean lo = false;
                boolean xien = false;
                boolean nhat = false;
                int rw2 = 1;
                String nd_phantich_b = "";
                jsonDan = new JSONObject();
                while (true) {
                    if (rw2 >= 1000 || mang[rw2][0] == null) {
                        break;
                    } else {
                        JSONObject json_ct = new JSONObject();
                        json_ct.put("du_lieu", mang[rw2][0]);
                        json_ct.put("the_loai", mang[rw2][1]);
                        json_ct.put("dan_so", mang[rw2][4]);
                        json_ct.put("so_tien", mang[rw2][5]);
                        if (mang[rw2][1].contains(LO)) {
                            lo = true;
                        } else {
                            if (!mang[rw2][1].contains(XI)) {
                                if (!mang[rw2][1].contains(XQ)) {
                                    if (!mang[rw2][1].contains(XN)) {
                                        if (!mang[rw2][1].contains("xg")) {
                                            if (mang[rw2][1].contains("de dau nhat") || mang[rw2][1].contains("de dit nhat") || mang[rw2][1].contains("hai cua")) {
                                                nhat = true;
                                            }
                                        }
                                    }
                                }
                            }
                            xien = true;
                        }

                        if (quagio) {
                            if (type_kh == 1) {
                                if (!mang[rw2][1].contains("de dit db") && !mang[rw2][1].contains("de dau db")
                                        && !mang[rw2][1].contains("bc") && !mang[rw2][1].contains("de 8")) {

                                    if (mang[rw2][1].contains("hai cua")) {
                                        nd_phantich_b = nd_phantich_b + "de dit db:" + mang[rw2][4].trim() + "x" + mang[rw2][5] + _N;
                                        json_ct.put("the_loai", "de dit db");
                                        String[] So = mang[rw2][4].split(",");
                                        json_ct.put("so_luong", So.length + " số.");
                                        jsonDan.put(String.valueOf(k322), json_ct);
                                    }
                                    k322++;
                                    rw2++;
                                }
                                nd_phantich_b = nd_phantich_b + mang[rw2][1] + ":" + mang[rw2][4].trim() + "x" + mang[rw2][5] + _N;
                                String[] So2 = mang[rw2][4].split(",");
                                json_ct.put("so_luong", So2.length + " số.");
                                jsonDan.put(String.valueOf(k322), json_ct);
                                k322++;
                                rw2++;
                            }
                        }

                        if (mang[rw2][1].equals("hai cua")) {
                            nd_phantich_b = nd_phantich_b + "de dit db:" + mang[rw2][4].trim() + "x" + mang[rw2][5] + _N;
                            String[] So = mang[rw2][4].split(",");
                            JSONObject json_hc = new JSONObject();
                            json_hc.put("du_lieu", mang[rw2][0].replaceFirst("hc", "de"));
                            json_hc.put("the_loai", "de dit db");
                            json_hc.put("dan_so", mang[rw2][4]);
                            json_hc.put("so_tien", mang[rw2][5]);
                            json_hc.put("so_luong", So.length + " số.");
                            jsonDan.put(String.valueOf(k322), json_hc);
                            k322++;
                            json_ct.put("du_lieu", mang[rw2][0].replaceFirst("hc", "nhat"));
                            json_ct.put("the_loai", "de dit nhat");
                            json_ct.put("so_luong", So.length + " số.");
                            jsonDan.put(String.valueOf(k322), json_ct);
                            nd_phantich_b = nd_phantich_b + "de dit nhat:" + mang[rw2][4].trim() + "x" + mang[rw2][5] + _N;
                        } else if (mang[rw2][1].contains(XI) || mang[rw2][1].contains(XQ) || mang[rw2][1].contains("xg")) {

                            String[] mArr = mang[rw2][4].split(str44);
                            for (String s : mArr) {
                                nd_phantich_b = nd_phantich_b + mang[rw2][1] + ":" + s + "x" + mang[rw2][5] + _N;
                                if (mang[rw2][1].contains(XQ)) {
                                    String[] XienQuay = xuly_Xq(s).split(str44);
                                    JSONObject json_xq = new JSONObject();
                                    String du_lieu = mang[rw2][1] + ":" + s + "x" + mang[rw2][5];
                                    json_xq.put("du_lieu", du_lieu);
                                    if (mang[rw2][1].contains("xq dau")) {
                                        json_xq.put("the_loai", "xien dau");
                                    } else {
                                        json_xq.put("the_loai", XI);
                                    }
                                    json_xq.put("dan_so", xuly_Xq(s));
                                    json_xq.put("so_tien", mang[rw2][5]);
                                    json_xq.put("so_luong", XienQuay.length + " cặp.");
                                    jsonDan.put(String.valueOf(k322), json_xq);
                                    k322++;
                                } else {
                                    if (mang[rw2][1].contains("xg")) {
                                        String[] So4 = mang[rw2][4].split(str44);
                                        json_ct.put("so_luong", So4.length + " cặp.");
                                        jsonDan.put(String.valueOf(k322), json_ct);
                                    } else {
                                        String[] So5 = mang[rw2][4].split(str44);
                                        json_ct.put("so_luong", So5.length + " cặp.");
                                        jsonDan.put(String.valueOf(k322), json_ct);
                                    }
                                }
                            }
                        } else {
                            String[] So6 = mang[rw2][4].split(",");
                            json_ct.put("so_luong", So6.length + " số.");
                            jsonDan.put(String.valueOf(k322), json_ct);
                            nd_phantich_b = nd_phantich_b + mang[rw2][1] + ":" + mang[rw2][4].trim() + "x" + mang[rw2][5] + _N;
                        }
                        k322++;
                        rw2++;

                    }
                }
                
                if (quagio) {
                    if (type_kh == 1) {
                        if (lo || xien || nhat) {
                            if (lo) {
                                Bor = "Bỏ " + "lô,";
                            } else {
                                Bor = "Bỏ ";
                            }
                            if (xien) {
                                Bor = Bor + "xiên,";
                            }
                            if (nhat) {
                                Bor = Bor + "giải nhất";
                            }
                            nd_phantich_b = Bor + " vì quá giờ!\n" + nd_phantich_b;
                            nd_phantich_b = nd_phantich_b.replaceAll("xg 2:", "xi:").replaceAll("xg 3:", "xi:").replaceAll("xg 4:", "xi:");
                            
                            QueryData("Update tbl_tinnhanS set nd_phantich='" + nd_phantich_b + "', phan_tich = '" + jsonDan + "', phat_hien_loi ='ok' Where id =" + id);
                        }
                    }
                }

                QueryData("Update tbl_tinnhanS set nd_phantich='" + nd_phantich_b + "', phan_tich = '" + jsonDan + "', phat_hien_loi ='ok' Where id =" + id);
            } else {
                QueryData("Update tbl_tinnhanS set nd_phantich ='" + ndPhantichLoi + "', phat_hien_loi = '"+ phatHienLoi + "'  Where id =" + id);
            }

            if (!getKhachHang.isClosed()) getKhachHang.close();

            if (!cursor2.isClosed()) cursor2.close();
        }
    }

    
    
    
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
                    int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mNgayNhan, 2, "ten_kh = '"+ Thongtin.getString(0) +"'");
                    int soTN = maxSoTn + 1;
                    final String Tralai2 = "Tra lai " + soTN + ":" + Tralai;
                    QueryData("Insert Into tbl_tinnhanS values (null, '" + mNgayNhan + "', '" + mGionhan + "',2, '" + Thongtin.getString(0) + "', '" + Thongtin.getString(1) + "','" + Thongtin.getString(2) + "', " + soTN + ", '" + Tralai2.trim() + "','" + Tralai2.substring(Tralai2.indexOf(":") + 1) + "','" + Tralai2.substring(Tralai2.indexOf(":") + 1) + "', 'ko',0,0,0, '" + this.json_Tralai.toString() + "')");
                    String sb = "Select id From tbl_tinnhanS where ngay_nhan = '" + mNgayNhan +
                            "' AND type_kh = 2 AND ten_kh ='" + Thongtin.getString(0) +
                            "' AND nd_goc = '" + Tralai2.trim() + "'";
                    Cursor ccc = GetData(sb);
                    ccc.moveToFirst();
                    Update_TinNhanGoc(ccc.getInt(0), 2);
                    if (Thongtin.getString(2).contains("TL")) {
                        final Long TralaiID = Thongtin.getLong(1);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            /* class tamhoang.ldpro4.data.Database.AnonymousClass1 */

                            public void run() {
                                new MainActivity();
                                MainActivity.sendMessage(TralaiID, Tralai2);
                            }
                        });
                    } else if (Thongtin.getString(2).contains("sms")) {
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




    public String TraDe(String TenKH, String DanDe) {
        JSONException e;
        String Str1;
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
            String query = "Select the_loai, so_chon, Sum(diem_ton*(type_kh = 1)) as Nhan, Sum(diem_ton *(type_kh = 2)) As Tra \nFROM tbl_soctS Where ten_kh = '" +
                    TenKH + "' AND ngay_nhan = '" + mDate + "' AND the_loai = '" + DangLoc + "' Group by so_chon Order by so_chon";
            Cursor cursor2 = GetData(query);
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
                        }
                    } catch (JSONException e3) {
                        e = e3;
                        e.printStackTrace();
                        return Str2;
                    }
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
                                str = Str1;
                                Str3 = soCT.getString(str) + ",";
                                Str111 = Str4;
                                tien = soCT.getInt("Se_tra");
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
                        }
                        i2 = i + 1;
                        Str1 = str;
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
                return TheLoai + ": " + Str;
            } catch (JSONException e13) {
                e13.printStackTrace();
                return Str2;
            }
        } catch (JSONException e16) {
            e16.printStackTrace();
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




    public String TraXi(String TenKH, String KhongXien) {
        JSONException e;
        Cursor cursor;
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
            String query = "Select the_loai, so_chon, Sum(diem_ton *(type_kh = 1)) as Nhan, Sum(diem_ton *(type_kh = 2)) As Tra \nFROM tbl_soctS " +
                    "Where ten_kh = '" + TenKH + "' AND ngay_nhan = '" + mDate + "' AND the_loai = 'xi' Group by so_chon";
            cursor = GetData(query);
            while (true) {
                if (!cursor.moveToNext()) {
                    break;
                }
                JSONObject jsonSoCt = new JSONObject();
                try {
                    jsonSoCt.put("So_chon", cursor.getString(1));
                    jsonSoCt.put("Da_nhan", cursor.getInt(2));
                    jsonSoCt.put("Da_tra", cursor.getInt(3));
                    if (jsonXien.has("xien2")) {
                        try {
                            if (jsonSoCt.getString("So_chon").length() == 5) {
                                jsonSoCt.put("Khong_Tien", jsonXien.getInt("xien2"));
                                jsonSoCt.put("Se_tra", (jsonSoCt.getInt("Da_nhan") - jsonSoCt.getInt("Da_tra")) - jsonSoCt.getInt("Khong_Tien"));
                                if (jsonSoCt.getInt("Se_tra") > 0) {
                                    jSon_Deb.put(cursor.getString(1), jsonSoCt.toString());
                                }
                            }
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                            return Str2;
                        }
                    }
                    if (!jsonXien.has("xien3") || jsonSoCt.getString("So_chon").length() != 8) {
                        if (jsonXien.has("xien4") && jsonSoCt.getString("So_chon").length() == 11) {
                            jsonSoCt.put("Khong_Tien", jsonXien.getInt("xien4"));
                            jsonSoCt.put("Se_tra", (jsonSoCt.getInt("Da_nhan") - jsonSoCt.getInt("Da_tra")) - jsonSoCt.getInt("Khong_Tien"));
                        }
                    } else {
                        jsonSoCt.put("Khong_Tien", jsonXien.getInt("xien3"));
                        jsonSoCt.put("Se_tra", (jsonSoCt.getInt("Da_nhan") - jsonSoCt.getInt("Da_tra")) - jsonSoCt.getInt("Khong_Tien"));
                    }
                } catch (JSONException e3) {
                    e3.printStackTrace();
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
                            Str222 = Str222 + soCT.getString("So_chon") + "x" + soCT.getInt("Se_tra") + "n\n";
                            Str = Str2 +  Str111 + "x" + tien + "n\n";
                            try {
                                Str111 = soCT.getString("So_chon") + " ";
                                tien = soCT.getInt("Se_tra");
                            } catch (JSONException e8) {
                                e = e8;
                                Str2 = Str;
                                e.printStackTrace();
                                return Str2;
                            }
                        } else {
                            tien = soCT.getInt("Se_tra");
                            Str111 = Str111 + soCT.getString("So_chon") + " ";
                            Str222 = Str222 + soCT.getString("So_chon") + "x" + soCT.getInt("Se_tra") + "n\n";
                            Str = Str2;
                        }
                        Str2 = Str;
                        i++;
                    } catch (JSONException e9) {
                        e = e9;
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
                return "Xien:\n" + Str222 + "\n";
            } catch (JSONException e12) {
                e12.printStackTrace();
                return Str2;
            }
        } catch (JSONException e13) {
            e13.printStackTrace();
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
                }
            } catch (JSONException e4) {
                e4.printStackTrace();
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
                }
                i2 = i + 1;
                str = str2;
            } catch (JSONException e10) {
                e = e10;
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
            e12.printStackTrace();
            return Str2;
        }
    }

    
    

    private void XulyMang(int k) {
        String[] Danxi;
        String[] Danxi2;
        String[] Danxi3;
        String[] danlayS;
        if (mang[k][1].contains("lo dau")) {
            if (!mang[k][2].contains("loa") || mang[k][2].trim().indexOf("loa") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("loa :", "");
                mang[k][4] = mang[k][4].replaceFirst("loa:", "");
                mang[k][4] = mang[k][4].replaceFirst("loa", "");
                mang[k][4] = Congthuc.XulyLoDe(mang[k][4]);
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("loa"));
            }
        } else if (mang[k][1].contains("lo")) {
            if (!mang[k][2].contains("lo") || mang[k][2].trim().indexOf("lo") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("lo :", "");
                mang[k][4] = mang[k][4].replaceFirst("lo:", "");
                mang[k][4] = mang[k][4].replaceFirst("lo", "");
                mang[k][4] = Congthuc.XulyLoDe(mang[k][4]);
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("lo"));
            }
        } else if (mang[k][1].contains("de dau db")) {
            if (!mang[k][2].contains("dea") || mang[k][2].trim().indexOf("dea") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("dea :", "");
                mang[k][4] = mang[k][4].replaceFirst("dea:", "");
                mang[k][4] = mang[k][4].replaceFirst("dea", "");
                mang[k][4] = Congthuc.XulyLoDe(mang[k][4]);
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("de"));
            }
        } else if (mang[k][1].contains("de dit db")) {
            if (!mang[k][2].contains("deb") || mang[k][2].trim().indexOf("deb") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("deb :", "");
                mang[k][4] = mang[k][4].replaceFirst("deb:", "");
                mang[k][4] = mang[k][4].replaceFirst("deb", "");
                mang[k][4] = mang[k][4].replaceFirst("de :", "");
                mang[k][4] = mang[k][4].replaceFirst("de:", "");
                mang[k][4] = mang[k][4].replaceFirst("de ", "");
                mang[k][4] = Congthuc.XulyLoDe(mang[k][4]);
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("de"));
            }
        } else if (mang[k][1].contains("de dau nhat")) {
            if (!mang[k][2].contains("dec") || mang[k][2].trim().indexOf("dec") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("dec :", "");
                mang[k][4] = mang[k][4].replaceFirst("dec:", "");
                mang[k][4] = mang[k][4].replaceFirst("dec", "");
                mang[k][4] = Congthuc.XulyLoDe(mang[k][4]);
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("de"));
            }
        } else if (mang[k][1].contains("de dit nhat")) {
            if (!mang[k][2].contains("ded") || mang[k][2].trim().indexOf("ded") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("ded :", "");
                mang[k][4] = mang[k][4].replaceFirst("ded:", "");
                mang[k][4] = mang[k][4].replaceFirst("ded", "");
                mang[k][4] = Congthuc.XulyLoDe(mang[k][4]);
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("de"));
            }
        } else if (mang[k][1].contains("de 8")) {
            if (!mang[k][2].contains("det") || mang[k][2].trim().indexOf("det") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("det :", "");
                mang[k][4] = mang[k][4].replaceFirst("det:", "");
                mang[k][4] = mang[k][4].replaceFirst("det", "");
                mang[k][4] = Congthuc.XulyLoDe(mang[k][4]);
            } else {
                mang[k][4] ="Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("de"));
            }
        } else if (mang[k][1].contains("hai cua")) {
            if (!mang[k][2].contains("hc") || mang[k][2].trim().indexOf("hc") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("hc :", "");
                mang[k][4] = mang[k][4].replaceFirst("hc:", "");
                mang[k][4] = mang[k][4].replaceFirst("hc", "");
                mang[k][4] = Congthuc.XulyLoDe(mang[k][4]);
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("hc"));
            }
        } else if (mang[k][1].contains("xn")) {
            if (!mang[k][2].contains("xn") || mang[k][2].trim().indexOf("xn") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("xn :", "");
                mang[k][4] = mang[k][4].replaceFirst("xn:", "");
                mang[k][4] = mang[k][4].replaceFirst("xn", "");
                if (mang[k][2].contains("xn 2 ")) {
                    mang[k][4] = mang[k][4].trim();
                    mang[k][4] = mang[k][4].substring(2);
                    mang[k][4] = Congthuc.XulySo(mang[k][4]);
                    String XienGhep = "";
                    for (String s : Congthuc.XulyXienGhep(mang[k][4], 2)) {
                        XienGhep = XienGhep + s + " ";
                    }
                    mang[k][4] = XienGhep;
                } else {
                    mang[k][4] = Congthuc.XulySo(mang[k][4].replaceAll("xn", " "));
                    mang[k][4] = Congthuc.XulyXien("2 " + mang[k][4].trim());
                }
                String[] ArrXien = mang[k][4].split(" ");
                int s1 = 0;
                while (true) {
                    if (s1 >= ArrXien.length) {
                        break;
                    } else if (ArrXien[s1].replaceAll(",", "").length() != 2 || !Congthuc.isNumeric(ArrXien[s1].replaceAll(",", ""))) {
                    } else {
                        s1++;
                    }
                }
                String[] ArrXien2 = mang[k][4].split(" ");
                int i6 = 0;
                while (i6 < ArrXien2.length) {
                    String ss = Congthuc.XulySo(ArrXien2[i6]);
                    if (ss.length() >= 5 && ss.length() <= 6 && ss.indexOf("Không hiểu") <= -1) {
                        String[] danlayS2 = ss.split(",");
                        int i42 = 0;
                        while (i42 < danlayS2.length) {
                            if (danlayS2[i42].length() != 2 || !Congthuc.isNumeric(danlayS2[i42])) {
                                if (mang[k][4].length() > 4) {
                                    mang[k][4] = "Không hiểu " + mang[k][2];
                                } else {
                                    mang[k][4] = "Không hiểu " + mang[k][0];
                                }
                            }
                            i42++;
                        }
                        i6++;
                    }
                }
                if (ArrXien2[i6].length() > 4) {
                    mang[k][4] = "Không hiểu " + mang[k][2];
                } else {
                    mang[k][4] = "Không hiểu " + mang[k][0];
                }
                if (!mang[k][4].contains("Không hiểu")) {
                    mang[k][4] = "";
                    int i7 = 0;
                    String soxien = "";
                    while (true) {
                        if (i7 >= ArrXien2.length) {
                            break;
                        }
                        if (soxien.contains("Không hiểu")) {
                            break;
                        }
                        boolean check = false;
                        for (String str : soxien.split(",")) {
                            if (soxien.length() - soxien.replaceAll(str, "").length() > 2) {
                                check = true;
                            }
                        }
                        if (soxien.length() < 5 || soxien.length() > 6 || check) {
                            mang[k][4] = "Không hiểu " + mang[k][2];
                        } else {
                            mang[k][4] = mang[k][4] + Congthuc.sortXien(soxien) + " ";
                            i7++;
                        }
                    }
                    mang[k][4] = "Không hiểu " + mang[k][2];
                }
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("xn"));
            }
        } else if (mang[k][1].contains("bc dau")) {
            if (!mang[k][2].contains("bca") || mang[k][2].trim().indexOf("bca") <= 0) {
                mang[k][4] = mang[k][2].replaceFirst("bca :", "");
                mang[k][4] = mang[k][4].replaceFirst("bca:", "");
                mang[k][4] = mang[k][4].replaceFirst("bca", "");
                mang[k][4] = Congthuc.Xu3cang(mang[k][4]);
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("bca"));
            }
        } else if (!mang[k][1].contains("bc")) {
            if (mang[k][1].contains("xi")) {
                if (!mang[k][2].contains("xi") || mang[k][2].trim().indexOf("xi") <= 0) {
                    if (mang[k][2].contains("xia")) {
                        Danxi3 = mang[k][2].split("xia");
                    } else {
                        Danxi3 = mang[k][2].split("xi");
                    }
                    String Danxienghep4 = "";
                    if (Danxi3.length > 2) {
                        for (int i43 = 1; i43 < Danxi3.length; i43++) {
                            if (Danxi3[i43].length() > 4) {
                                Danxienghep4 = Danxienghep4 + Congthuc.XulySo(Danxi3[i43]) + " ";
                                if (Congthuc.XulySo(Danxi3[i43]).contains("Không hiểu")) {
                                    mang[k][4] = "Không hiểu " + Danxi3[i43];
                                }
                            }
                        }
                        mang[k][4] = Danxienghep4;
                    } else {
                        if (mang[k][2].contains("xia")) {
                            mang[k][4] = mang[k][2].replaceFirst("xia", "");
                        } else {
                            mang[k][4] = mang[k][2].replaceFirst("xi", "");
                        }
                    }

                    if (Danxi3.length < 3) {
                        mang[k][4] = Congthuc.XulyXien(mang[k][4].trim());
                    }
                    String[] ArrXien3 = mang[k][4].split(" ");
                    boolean ktra2 = false;
                    int s12 = 0;
                    while (true) {
                        if (s12 >= ArrXien3.length) {
                            break;
                        } else if (ArrXien3[s12].replaceAll(",", "").length() != 2 || !Congthuc.isNumeric(ArrXien3[s12].replaceAll(",", ""))) {
                            ktra2 = true;
                        }
                        s12++;
                    }
                    if (ktra2 || ArrXien3.length >= 5) {
                    } else {
                        mang[k][4] = Congthuc.XulySo(mang[k][4]);
                    }
                    String[] ArrXien4 = mang[k][4].split(" ");
                    int i9 = 0;
                    while (i9 < ArrXien4.length) {
                        String ss2 = Congthuc.XulySo(ArrXien4[i9]);
                        if (ss2.length() >= 5 && ss2.length() <= 12) {
                            if (!ss2.contains("Không hiểu")) {
                                if (mang[k][1] != "xq" || ss2.length() >= 8) {
                                    String[] danlayS3 = ss2.split(",");
                                    int i44 = 0;
                                    while (i44 < danlayS3.length) {
                                        if (danlayS3[i44].length() == 2 && Congthuc.isNumeric(danlayS3[i44])) {
                                            danlayS = danlayS3;
                                        } else if (mang[k][4].length() > 4) {
                                            danlayS = danlayS3;
                                            mang[k][4] = "Không hiểu " + mang[k][2];
                                        } else {
                                            danlayS = danlayS3;
                                            mang[k][4] = "Không hiểu " + mang[k][0];
                                        }
                                        i44++;
                                        danlayS3 = danlayS;
                                    }
                                } else {
                                    mang[k][4] = "Không hiểu " + mang[k][0];
                                }
                            }
                        }
                        if (ArrXien4[i9].length() < 4) {
                            mang[k][4] = "Không hiểu " + mang[k][2];
                        }
                        if (!mang[k][4].contains("Không hiểu")) {
                            mang[k][4] = "";
                            int i10 = 0;
                            String soxien2 = "";
                            while (true) {
                                if (i10 >= ArrXien4.length) {
                                    break;
                                }
                                try {
                                    soxien2 = Congthuc.XulySo(ArrXien4[i10]);
                                } catch (Exception e2) {
                                    mang[k][4] = "Không hiểu " + ArrXien4[i10];
                                }
                                if (soxien2.contains("Không hiểu")) {
                                    break;
                                }
                                boolean check2 = false;
                                for (String str2 : soxien2.split(",")) {
                                    if (soxien2.length() - soxien2.replaceAll(str2, "").length() > 2) {
                                        check2 = true;
                                    }
                                }
                                if (soxien2.length() < 5 || soxien2.length() > 12 || check2) {
                                    mang[k][4] = "Không hiểu " + mang[k][2];
                                } else {
                                    mang[k][4] = mang[k][4] + Congthuc.sortXien(soxien2) + " ";
                                    i10++;
                                }
                            }
                        }
                        i9++;
                    }
                } else {
                    mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("xi"));
                }
            } else if (mang[k][1].contains("xq")) {
                if (mang[k][2].contains("xq") || mang[k][2].trim().indexOf("xq") <= 0) {
                    if (mang[k][2].contains("xqa")) {
                        String[] Danxi4 = mang[k][2].split("xqa");
                        mang[k][1] = "xq dau";
                        Danxi = Danxi4;
                    } else {
                        Danxi = mang[k][2].split("xq");
                    }
                    String Danxienghep5 = "";
                    if (Danxi.length > 2) {
                        for (int i45 = 1; i45 < Danxi.length; i45++) {
                            if (Danxi[i45].length() > 4) {
                                Danxienghep5 = Danxienghep5 + Congthuc.XulySo(Danxi[i45]) + " ";
                                if (Congthuc.XulySo(Danxi[i45]).contains("Không hiểu")) {
                                    mang[k][4] = "Không hiểu " + Danxi[i45];
                                }
                            }
                        }
                        mang[k][4] = Danxienghep5;
                    } else {
                        if (mang[k][2].contains("xqa")) {
                            mang[k][4] = mang[k][2].replaceFirst("xqa", "");
                        } else {
                            mang[k][4] = mang[k][2].replaceFirst("xq", "");
                        }
                    }
                    mang[k][4] = Congthuc.XulyXien(mang[4][4].trim());
                    String[] ArrXien6 = mang[k][4].split(" ");
                    int i11 = 0;
                    while (i11 < ArrXien6.length) {
                        String ss3 = Congthuc.XulySo(ArrXien6[i11]);
                        if (ss3.length() >= 8 && ss3.length() <= 12) {
                            if (!ss3.contains("Không hiểu")) {
                                if (mang[k][1] != "xq" || ss3.length() >= 8) {
                                    String[] danlayS4 = ss3.split(",");
                                    int i46 = 0;
                                    while (i46 < danlayS4.length) {
                                        if (danlayS4[i46].length() == 2 && Congthuc.isNumeric(danlayS4[i46])) {
                                            Danxi2 = Danxi;
                                        } else if (mang[k][4].length() > 4) {
                                            Danxi2 = Danxi;
                                            mang[k][4] = "Không hiểu " + mang[k][2];
                                        } else {
                                            Danxi2 = Danxi;
                                            mang[k][4] = "Không hiểu " + mang[k][0];
                                        }
                                        i46++;
                                        Danxi = Danxi2;
                                    }
                                } else {
                                    mang[k][4] = "Không hiểu " + mang[k][0];
                                }
                                i11++;
                            }
                        }
                        try {
                            if (!mang[k][4].contains("Không hiểu")) {
                                mang[k][4] = "";
                                int i12 = 0;
                                String soxien3 = "";
                                while (true) {
                                    if (i12 >= ArrXien6.length) {
                                        break;
                                    }
                                    try {
                                        soxien3 = Congthuc.XulySo(ArrXien6[i12]);
                                    } catch (Exception e3) {
                                        mang[k][4] = "Không hiểu " + ArrXien6[i12];
                                    }
                                    if (soxien3.contains("Không hiểu")) {
                                        break;
                                    }
                                    boolean check3 = false;
                                    for (String str3 : soxien3.split(",")) {
                                        if (soxien3.length() - soxien3.replaceAll(str3, "").length() > 2) {
                                            check3 = true;
                                        }
                                    }
                                    if (soxien3.length() < 5 || soxien3.length() > 12 || check3) {
                                        mang[k][4] = "Không hiểu " + mang[k][2];
                                    } else {
                                        mang[k][4] = mang[k][4] + Congthuc.sortXien(soxien3) + " ";
                                        i12++;
                                    }
                                }
                            }
                        }catch (Throwable throwable){
                            Log.e("Class: Database, Func: XulyMang, Line: 2585", throwable.getMessage());
                        }
                    }
                } else {
                    mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("xq"));
                }
            } else if (!mang[k][1].contains("xg")) {
            } else if (!mang[k][2].contains("xg") || mang[k][2].trim().indexOf("xg") <= 0) {
                if (mang[k][1].contains("xg 2")) {
                    mang[k][4] = mang[k][2].replaceFirst("xg 2 ", "");
                } else if (mang[k][1].contains("xg 3")) {
                    mang[k][4] = mang[k][2].replaceFirst("xg 3 ", "");
                } else if (mang[k][1].contains("xg 4")) {
                    String[][] strArr84 = mang;
                    strArr84[k][4] = strArr84[k][2].replaceFirst("xg 4 ", "");
                }
                ArrayList<String> listXienGhep = null;
                StringBuilder XienGhep2 = new StringBuilder();
                mang[k][4] = Congthuc.XulySo(mang[k][4]);
                if (!mang[k][4].contains("Không hiểu")) {
                    if (mang[k][1].contains("xg 2")) {
                        listXienGhep = Congthuc.XulyXienGhep(mang[k][4], 2);
                    } else if (mang[k][1].contains("xg 3")) {
                        listXienGhep = Congthuc.XulyXienGhep(mang[k][4], 3);
                    } else if (mang[k][1].contains("xg 4")) {
                        listXienGhep = Congthuc.XulyXienGhep(mang[k][4], 4);
                    }
                    for (String s : listXienGhep) {
                        XienGhep2.append(s).append(" ");
                    }
                    mang[k][4] = XienGhep2.toString();
                }
            } else {
                mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("xg"));
            }
        } else if (!mang[k][2].contains("bc") || mang[k][2].trim().indexOf("bc") <= 0) {
            mang[k][4] = mang[k][2].replaceFirst("bc :", "");
            mang[k][4] = mang[k][4].replaceFirst("bc:", "");
            mang[k][4] = mang[k][4].replaceFirst("bc", "");
            mang[k][4] = Congthuc.Xu3cang(mang[k][4]);
        } else {
            mang[k][4] = "Không hiểu " + mang[k][2].substring(0, mang[k][2].indexOf("bc"));
        }
        if (mang[k][4] == null) {
            mang[k][4] = "Không hiểu " + mang[k][0].substring(0, 5);
        } else if (mang[k][4].trim().length() == 10 && mang[k][4].contains("Không hiểu")) {
            mang[k][4] = "Không hiểu " + mang[k][0];
        }
    }

    private void BaoLoiDan(int k) {
        if (mang[k][4].contains("Không hiểu")) {
            String[][] strArr = mang;
            strArr[k][0] = Congthuc.ToMauError(strArr[k][4].substring(11), mang[k][0]);
        }
    }

    private void BaoLoiTien(int k) {
        try {
            mang[k][5] = Congthuc.XulyTien(mang[k][3]);
            if (mang[k][5].contains("Không hiểu") && mang[k][5].trim().length() < 13) {
                mang[k][0] = Congthuc.ToMauError(mang[k][5].substring(11), mang[k][0]);
            } else if (mang[k][5].contains("Không hiểu") && mang[k][5].trim().length() > 12) {
                mang[k][0] = Congthuc.ToMauError(mang[k][3], mang[k][0]);
            }
        } catch (Exception e) {
            mang[k][0] = Congthuc.ToMauError(mang[k][3], mang[k][0]);
        }
    }


    
    
    public void NhapSoChiTiet(int id) throws Throwable {
        final String XI = "xi";
        final String THE_LOAI = "the_loai";

        double mGia;
        String so_tien = null;
        SQLiteDatabase db2;
        Iterator<String> keys;
        DatabaseUtils.InsertHelper ih2 = null;
        String mThe_loai = null;
        double mKhachGiu = 0;
        String dan_so = null;
        double mKhachGiu2 = 0;
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
        Database database = this;
        double mDlyGiu2 = 0.0d;
        Cursor c = database.GetData("Select * From tbl_tinnhanS WHERE id = " + id);
        c.moveToFirst();
        String mThe_loai2 = "Select * From tbl_soctS where ten_kh = '" + c.getString(4) + "' And ngay_nhan = '" + c.getString(1) + "' And type_kh = " + c.getString(3) + " And so_tin_nhan = " + c.getString(7);
        Cursor c2 = database.GetData(mThe_loai2);
        if (c2.getCount() > 0) return;

        database.jsonDanSo = new JSONObject(c.getString(15));
        String mTenKH = c.getString(4);
        String mThe_loai3 = "";
        String mNgay_Nhan = c.getString(1);
        String sb = "Select * From tbl_KH_new where ten_kh = '" + mTenKH + "'";
        Cursor cursor3 = database.GetData(sb);
        cursor3.moveToFirst();
        mGia = 0.0d;
        JSONObject jSONObject = new JSONObject(cursor3.getString(5));
        database.json = jSONObject;
        database.caidat_gia = jSONObject.getJSONObject("caidat_gia");
        database.caidat_tg = database.json.getJSONObject("caidat_tg");
        db2 = getWritableDatabase();
        db2.beginTransaction();
        keys = database.jsonDanSo.keys();
        while (keys.hasNext()) {
            JSONObject dan = new JSONObject(database.jsonDanSo.getString(keys.next()));
            dan_so = dan.getString("dan_so");
            so_tien = dan.getString("so_tien");
            ih2 = new DatabaseUtils.InsertHelper(db2, "tbl_soctS");
            if (dan.getString(THE_LOAI).contains("de dau db")) {
                mThe_loai = "dea";
            } else {
                mThe_loai = dan.getString(THE_LOAI).contains("de dit db") ? "deb" : dan.getString(THE_LOAI).contains("de 8")
                        ? "det" : dan.getString(THE_LOAI).contains("de dau nhat") ? "dec" : dan.getString(THE_LOAI).contains("de dit nhat")
                        ? "ded" : dan.getString(THE_LOAI).contains("bc dau") ? "bca" : dan.getString(THE_LOAI).contains("bc")
                        ? "bc" : dan.getString(THE_LOAI).contains("lo dau") ? "loa" : dan.getString(THE_LOAI).contains("lo")
                        ? "lo" : dan.getString(THE_LOAI).contains("xien dau") ? "xia" : (dan.getString(THE_LOAI).contains(XI) || dan.getString(THE_LOAI).contains("xg"))
                        ? XI : dan.getString(THE_LOAI).contains("xn") ? "xn" : mThe_loai3;
            }

            if (mThe_loai.contains("lo")) {
                mKhachGiu2 = database.caidat_tg.getInt("khgiu_lo");
                mKhachGiu = database.caidat_tg.getInt("dlgiu_lo");
            } else {
                mKhachGiu2 = database.caidat_tg.getInt("khgiu_xi");
                mKhachGiu = database.caidat_tg.getInt("dlgiu_xi");
            }
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
            } else if (mThe_loai.contains(XI) && dan_so.length() == 5) {
                mGia2 = database.caidat_gia.getDouble("gia_x2");
                mLanAn = database.caidat_gia.getDouble("an_x2");
            } else if (mThe_loai.contains(XI) && dan_so.length() == 8) {
                mGia2 = database.caidat_gia.getDouble("gia_x3");
                mLanAn = database.caidat_gia.getDouble("an_x3");
            } else if (mThe_loai.contains(XI) && dan_so.length() == 11) {
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
            mDiem = Integer.parseInt(so_tien);
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
            if (c.getInt(3) == 1) {
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
                str2 = dan_so.split(str8);
            } else {
                str2 = dan_so.split(" ");
            }
            int i = 0;
            while (i < str2.length) {
                String So_chon = str2[i].trim();
                if (So_chon.endsWith(str8)) {
                    So_chon = So_chon.substring(0, So_chon.length() - 1);
                }
                if (mThe_loai.contains(XI) && So_chon.length() == 5) {
                    mGia = database.caidat_gia.getDouble("gia_x2");
                    mLanAn2 = database.caidat_gia.getDouble("an_x2");
                } else if (mThe_loai.contains(XI) && So_chon.length() == 8) {
                    mGia = database.caidat_gia.getDouble("gia_x3");
                    mLanAn2 = database.caidat_gia.getDouble("an_x3");
                } else if (mThe_loai.contains(XI) && So_chon.length() == 11) {
                    mGia = database.caidat_gia.getDouble("gia_x4");
                    mLanAn2 = database.caidat_gia.getDouble("an_x4");
                }
                Double.isNaN(mDiem);
                double mThanhTien = mDiem * mGia;
                ih2.prepareForInsert();

                ih2.bind(ih2.getColumnIndex("ID"), (byte[]) null);
                ih2.bind(ih2.getColumnIndex("ngay_nhan"), mNgay_Nhan);
                ih2.bind(ih2.getColumnIndex("type_kh"), c.getInt(3));
                ih2.bind(ih2.getColumnIndex("ten_kh"), cursor3.getString(0));
                ih2.bind(ih2.getColumnIndex("so_dienthoai"), c.getString(5));
                ih2.bind(ih2.getColumnIndex("so_tin_nhan"), c.getInt(7));
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
            database = this;
            mThe_loai3 = mThe_loai;
            mDlyGiu2 = mLanAn2;
            double mKhachGiu42 = mDiem_KhachGiu;
            Double.isNaN(mKhachGiu42);
            double d2 = mDiemquydoi - mKhachGiu42;
            double mDiemquydoi32 = mDiem_DlyGiu;
            Double.isNaN(mDiemquydoi32);

        }
        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();
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
//
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

    private void sendAndInsertDB(TinNhanS tinNhanS, String mSoDT4, String nd_tra_loi,
                   String mNgayNhan, String mGioNhan, String mNgay5, int mSoTN5) throws JSONException {

        if (tinNhanS.getUse_app().contains("sms")) {
            SendSMS(mSoDT4, nd_tra_loi);
        } else if (tinNhanS.getUse_app().contains("TL")) {
            new Handler(Looper.getMainLooper()).post(
                    () -> MainActivity.sendMessage(Long.parseLong(mSoDT4), nd_tra_loi));
        } else {
            JSONObject jsonObject = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT4));

            Log.e(TAG, "Gui_Tin_Nhan: sendAndInsertDB: " + jsonObject + " - "+ MainActivity.json_Tinnhan);
            if (jsonObject.getInt("Time") < 3) {
                new NotificationReader().NotificationWearReader(tinNhanS.getSo_dienthoai(), nd_tra_loi);
            } else {
                jsonObject.put(nd_tra_loi, "OK");
                MainActivity.json_Tinnhan.put(mSoDT4, jsonObject);
            }
            Chat chat = new Chat(null, mNgayNhan, mGioNhan, 2, tinNhanS.getTen_kh(),
                    tinNhanS.getSo_dienthoai(), tinNhanS.getUse_app(), nd_tra_loi, 1);
            BriteDb.INSTANCE.insertChat(chat);
        }
        String update = "Update tbl_tinnhanS set ok_tn = 0 WHERE ngay_nhan = '" + mNgay5
                + "' AND so_dienthoai = '" + mSoDT4 + "' AND so_tin_nhan = " + mSoTN5;
        QueryData(update);
    }

    public void Gui_Tin_Nhan(int mID) throws JSONException { //test
        final String TIME = "Time";
        final String SMS = "sms";
        final String OK_TIN_ = "Ok Tin ";
        final String TL = "TL ";

        Chat result_chat;

        int kieu_tra_loi = 2;
        String str = "TL";

        Object obj;
        String myApp = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        hourFormat.setTimeZone(TimeZone.getDefault());
        String mNgayNhan = dmyFormat.format(calendar.getTime());
        String mGioNhan = hourFormat.format(calendar.getTime());

        TinNhanS tinNhanS = BriteDb.INSTANCE.selectTinNhanS(mID);
        int mSoTN5 = tinNhanS.getSo_tin_nhan();
        final String mSoDT4 = tinNhanS.getSo_dienthoai();
        String mNgay5 = tinNhanS.getNgay_nhan();
        try {
            KhachHang khachHang = BriteDb.INSTANCE.selectKhachHangQuery(KhachHang.SDT + " = '" + tinNhanS.getSo_dienthoai() + "'");
            JSONObject jSONObject = new JSONObject(khachHang.getTbl_MB());
            this.json = jSONObject;
            caidat_tg = jSONObject.getJSONObject("caidat_tg");
            kieu_tra_loi = caidat_tg.getInt("ok_tin") + 1;//bat dau = 1
        } catch (JSONException e) {
            Log.e(TAG, "Gui_Tin_Nhan: error 1 " + e );
        }

        if (tinNhanS.getOk_tn() == 1) {
            String nd_tra_loi = "";
            switch (kieu_tra_loi) {// "1. Ok tin và nd phân tích", "2. Chỉ ok tin", "3. Không trả lời", "4. Ok tin nguyên mẫu", "5. Chỉ ok tin (ngay khi nhận)", "6. OK nguyên mẫu (ngay khi nhận)"};
//                case 1: nd_tra_loi = OK_TIN_ + mSoTN5 + tinNhanS.getNd_phantich();
//                    break;
//                case 2: nd_tra_loi = OK_TIN_ + mSoTN5;
//                    break;
//                case 3: nd_tra_loi = "";
//                    break;
//                case 4: nd_tra_loi = OK_TIN_ + mSoTN5 + "\n" + tinNhanS.getNd_goc();
//                    break;
                case 5: nd_tra_loi = OK_TIN_ + mSoTN5;
                    break;
                case 6: nd_tra_loi = OK_TIN_ + mSoTN5+ "\n" + tinNhanS.getNd_goc();
            }

            Log.e(TAG, "Gui_Tin_Nhan: ok kieu_tra_loi: " + kieu_tra_loi + " tin nhăn " + nd_tra_loi );
            if(nd_tra_loi != "")
                sendAndInsertDB(tinNhanS, mSoDT4, nd_tra_loi, mNgayNhan, mGioNhan, mNgay5, mSoTN5);
        }

        Log.e(TAG, "Gui_Tin_Nhan: tinNhanS: " + tinNhanS);

        Log.e(TAG, "Gui_Tin_Nhan: indexOf(ok): " + tinNhanS.getPhat_hien_loi().indexOf("ok") +" phat hien loi: "+ tinNhanS.getPhat_hien_loi() + " getOk_tn " + tinNhanS.getOk_tn());

        if (tinNhanS.getPhat_hien_loi().indexOf("ok") == 0 && tinNhanS.getPhat_hien_loi().length() == 2 && tinNhanS.getOk_tn() == 1) {
            if (kieu_tra_loi == 4) {
                if (!tinNhanS.getNd_phantich().contains("Bỏ ")) {
                    final String tinNhan8 = OK_TIN_ + mSoTN5 + "\n" + tinNhanS.getNd_goc();
                    sendAndInsertDB(tinNhanS, mSoDT4, tinNhan8, mNgayNhan, mGioNhan, mNgay5, mSoTN5);
                } else {
                    final String tinNhan9 = tinNhanS.getNd_phantich()
                            .substring(0, tinNhanS.getNd_phantich().indexOf(0) - 1) + "\nOK Tin" + mSoTN5 + 0 + tinNhanS.getNd_goc();
                    sendAndInsertDB(tinNhanS, mSoDT4, tinNhan9, mNgayNhan, mGioNhan, mNgay5, mSoTN5);
                }
            } else if (kieu_tra_loi == 2 && !tinNhanS.getNd_phantich().contains("Bỏ ")) {
                final String tinNhan10 = OK_TIN_ + mSoTN5;
                sendAndInsertDB(tinNhanS, mSoDT4, tinNhan10, mNgayNhan, mGioNhan, mNgay5, mSoTN5);
            } else if (kieu_tra_loi == 1) {

                final String tinNhan11 = "Ok tin " + mSoTN5 + "\n" +tinNhanS.getNd_phantich();
                sendAndInsertDB(tinNhanS, mSoDT4, tinNhan11, mNgayNhan, mGioNhan, mNgay5, mSoTN5);
            }

            Cursor cur2 = GetData("Select * From tbl_chuyenthang WHERE sdt_nhan = '" + mSoDT4 + "'");
            cur2.moveToFirst();
            if (cur2.getCount() > 0) {
                obj = "OK";
                if (tinNhanS.getDel_sms() == 1) {
                    int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mNgay5, 2, "so_dienthoai = '"+ cur2.getString(4) +"'");

                    int sotin3 = maxSoTn + 1;
                    if (cur2.getString(3).contains(TL)) {
                        myApp = "TL";
                    } else if (cur2.getString(3).contains("ZL")) {
                        myApp = "ZL";
                    } else if (cur2.getString(3).contains("VB")) {
                        myApp = "VB";
                    } else if (cur2.getString(3).contains("WA")) {
                        myApp = "WA";
                    } else {
                        myApp = "sms";
                    }

                    TinNhanS tinNhanS_i = new TinNhanS(null, tinNhanS.getNgay_nhan(), tinNhanS.getGio_nhan(), 2, cur2.getString(3), cur2.getString(4),
                            myApp, sotin3, tinNhanS.getNd_goc(), tinNhanS.getNd_sua(), tinNhanS.getNd_phantich(), "ok", 0, 0, 1, tinNhanS.getPhan_tich());
                    BriteDb.INSTANCE.insertTinNhanS(tinNhanS_i);
                    TinNhanS tinNhanS_s = BriteDb.INSTANCE.selectTinNhanS(tinNhanS.getNgay_nhan(), cur2.getString(4), sotin3, 2);

                    String updateDel_sms = "Update tbl_tinnhanS set del_sms = 0 WHERE ngay_nhan = '" + mNgay5 +
                            "' AND so_dienthoai = '" + mSoDT4 + "' AND so_tin_nhan = " + mSoTN5;
                    QueryData(updateDel_sms);

//                    QueryData("Insert Into tbl_tinnhanS values (null, '" + getTinNhan.getString(1) + "', '" + getTinNhan.getString(2) + "',2, '" +
//                            cur2.getString(3) + "', '" + cur2.getString(4) + "', '" + myApp + "', " + sotin3 + ", '" + getTinNhan.getString(8) + "', '"
//                            + getTinNhan.getString(9) + "','" + getTinNhan.getString(10) + "', 'ok',0,0,1, '" + getTinNhan.getString(15) + "')");
//                    String sb11 = "Select * From tbl_tinnhanS WHERE ngay_nhan = '"
//                            + getTinNhan.getString(1) +
//                            "' AND so_dienthoai = '" + cur2.getString(4) +
//                            "' AND so_tin_nhan = " + sotin3 + " AND type_kh = 2";
//                    Cursor getid = GetData(sb11);
//
//                    getid.moveToFirst();
//                    String sb12 = "Update tbl_tinnhanS set del_sms = 0 WHERE ngay_nhan = '" + mNgay5 +
//                            "' AND so_dienthoai = '" + mSoDT4 +
//                            "' AND so_tin_nhan = " + mSoTN5;
//                    QueryData(sb12);

                    try {
                        NhapSoChiTiet(tinNhanS_s.getID());
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Cursor chuyen4 = GetData("Select Om_Xi3 FROM so_Om WHERE id = 13");
                    chuyen4.moveToFirst();

                    String tinNhan12 = "";
                    if (chuyen4.getInt(0) == 0)
                        tinNhan12 = "Tin " + sotin3 + ":\n" + tinNhanS.getNd_goc();
                    else
                        tinNhan12 = "Tin " + sotin3 + ":\n" + tinNhanS.getNd_sua();

                    if (tinNhanS_s.getUse_app().contains(SMS)) {
                        SendSMS(cur2.getString(4), tinNhan12);
                    } else if (tinNhanS_s.getUse_app().contains(TL)) {
                        new Handler(Looper.getMainLooper()).post(() -> {
//                                MainActivity.sendMessage(cur2.getLong(4), tinNhan12);
                        });
                    } else {
                        new NotificationReader().NotificationWearReader(cur2.getString(4), tinNhan12);
                        Chat chat = new Chat(null, mNgayNhan, mGioNhan, 2, cur2.getString(3), cur2.getString(4), myApp, tinNhan12, 1);
                        BriteDb.INSTANCE.insertChat(chat);
                    }

                    if (!chuyen4.isClosed()) chuyen4.close();

                    if (tinNhanS.getType_kh() == 1 || MainActivity.jSon_Setting.getInt("baotinthieu") <= 0) {
                    } else {

                        List<TinNhanS> listTinNhans = BriteDb.INSTANCE.selectListTinNhanS("ngay_nhan = '" + mNgay5 +
                                "' AND so_dienthoai = '" + mSoDT4 + "' AND type_kh = 1 ORDER BY so_tin_nhan");
//                        String query = "Select * From tbl_tinnhanS WHERE ngay_nhan = '" + mNgay5 + "' AND so_dienthoai = '" + mSoDT4 + "' AND type_kh = 1 ORDER BY so_tin_nhan";
//                        Cursor cursor2 = GetData(query);
                        JSONObject jsonTinnhan = new JSONObject();
                        int maxTin = 0;
                        for( TinNhanS tin : listTinNhans) {
                            int so_tin_nhan = tin.getSo_tin_nhan();
                            jsonTinnhan.put(so_tin_nhan + "-", so_tin_nhan);
                            maxTin = so_tin_nhan;
                        }

                        String tinthieu = "";
                        int i5 = 1;
                        while (i5 < maxTin) {
                            if (!jsonTinnhan.has(i5 + "-"))
                                tinthieu += i5 + ",";
                            i5++;
                        }

                        if (tinthieu.length() > 0) {
                            final String NoIDungThieu = "Thiếu tin " + tinthieu;
                            if (tinNhanS.getUse_app().contains(SMS)) {
                                SendSMS(mSoDT4, NoIDungThieu);
                            } else if (tinNhanS.getUse_app().contains(TL)) {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    MainActivity.sendMessage(Long.parseLong(mSoDT4), NoIDungThieu);
                                });
                            } else {
                                try {
                                    JSONObject jsonObject8 = new JSONObject(MainActivity.json_Tinnhan.getString(mSoDT4));
                                    if (jsonObject8.getInt(str) > 3) {
                                        new NotificationReader().NotificationWearReader(tinNhanS.getSo_dienthoai(), NoIDungThieu);

                                        Chat chat = new Chat(null, mNgayNhan, mGioNhan, 2, tinNhanS.getTen_kh(),
                                                tinNhanS.getSo_dienthoai(), tinNhanS.getUse_app(), NoIDungThieu, 1);
                                        BriteDb.INSTANCE.insertChat(chat);
                                    } else {
                                        jsonObject8.put(NoIDungThieu, obj);
                                        MainActivity.json_Tinnhan.put(mSoDT4, jsonObject8);
                                    }
                                } catch (Exception ignored) { }
                                Chat chat = new Chat(null, mNgayNhan, mGioNhan, 2, tinNhanS.getTen_kh(),
                                        tinNhanS.getSo_dienthoai(), tinNhanS.getUse_app(), NoIDungThieu, 1);
                                BriteDb.INSTANCE.insertChat(chat);
                            }
                        }
                    }
                }
            }
        }

        TinNhanS tinNhanS2 = BriteDb.INSTANCE.selectTinNhanS("ngay_nhan = '" + mNgay5
                + "' AND so_dienthoai = '" + mSoDT4 + "' AND so_tin_nhan = " + mSoTN5);

        String query = "Select * From tbl_chuyenthang WHERE sdt_nhan = '" + mSoDT4 + "'";
        Cursor cur = GetData(query);
        cur.moveToFirst();

        Cursor chuyen32 = GetData("Select Om_Xi3 FROM so_Om WHERE id = 13");
        chuyen32.moveToFirst();

        if (cur.getCount() > 0 && chuyen32.getInt(0) == 0) {
            if (tinNhanS2.getDel_sms() == 1) {

                int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mNgay5, null, "so_dienthoai = '"+ cur.getString(4) +"'");
                int sotin4 = maxSoTn + 1;

                try {
                    String my_app = "";
                    if (cur.getString(3).contains("ZL"))
                        my_app = "ZL";
                    else if (cur.getString(3).contains("VB"))
                        my_app = "VB";
                    else if (cur.getString(3).contains("WA"))
                        my_app = "WA";
                    else if (cur.getString(3).contains(TL))
                        my_app = "TL";
                    else
                        my_app = "sms";

                    TinNhanS tinNhanS_i = new TinNhanS(null, tinNhanS.getNgay_nhan(), tinNhanS.getGio_nhan(), 2, cur.getString(3),
                            cur.getString(4), my_app, sotin4, tinNhanS.getNd_goc(), "null", tinNhanS.getNd_phantich(),
                            "ko", 0, 0, 1, "null");
                    BriteDb.INSTANCE.insertTinNhanS(tinNhanS_i);

                    if (my_app.contains(SMS)) {
                        SendSMS(cur.getString(4), "Tin " + sotin4 + ":\n" + tinNhanS.getNd_goc());
                    } else if (tinNhanS.getUse_app().contains(TL)) {
                        final String tinNhan14 = "Tin " + sotin4 + ":\n" + tinNhanS.getNd_goc();
                        new Handler(Looper.getMainLooper()).post(() -> {
                            MainActivity.sendMessage(cur.getLong(4), tinNhan14);
                        });
                    } else {
                        String tinNhan15 = "Tin " + sotin4 + ":\n" + tinNhanS.getDel_sms();
                        new NotificationReader().NotificationWearReader(cur.getString(4), tinNhan15);
                        QueryData("Insert into Chat_database Values( null,'" + mNgayNhan + "', '" + mGioNhan
                                + "', 2, '" + cur.getString(3) + "', '" + cur.getString(4) + "', '" + my_app
                                + "','" + tinNhan15 + "',1)");
                    }
                } catch (Exception exception){
                    Log.e(TAG, "Gui_Tin_Nhan: error" + exception.getMessage());
                }

                QueryData("Update tbl_tinnhanS set del_sms = 0 WHERE ngay_nhan = '" + mNgay5 + "' AND so_dienthoai = '" + mSoDT4 + "' AND so_tin_nhan = " + mSoTN5);
                TralaiSO(mID);
                cur.close();
                return;
            }
        }

        TralaiSO(mID);

        Log.e(TAG, "Gui_Tin_Nhan: end function " + mID );

    }

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
                    boolean check2 = mang2[Integer.parseInt(str23[0])][0].length() > 1 || mang2[Integer.parseInt(str23[1])][0].length() > 1 || (mang2[Integer.parseInt(str23[0])][0].length() > 0 && mang2[Integer.parseInt(str23[1])][0].length() > 0);
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
                                boolean check6 = mang2[Integer.parseInt(str216[0])][0].length() > 1 || mang2[Integer.parseInt(str216[1])][0].length() > 1 || (mang2[Integer.parseInt(str216[0])][0].length() > 0 && mang2[Integer.parseInt(str216[1])][0].length() > 0);
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
                QueryData("insert into tbl_Setting Values( null,'" + setting + "')");
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
            QueryData("Update tbl_Setting set Setting = '" + setting2 + "'");
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