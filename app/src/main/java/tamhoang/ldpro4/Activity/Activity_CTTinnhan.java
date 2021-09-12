package tamhoang.ldpro4.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.internal.view.SupportMenu;

import java.text.DecimalFormat;
import org.json.JSONException;
import org.json.JSONObject;
import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_CTTinnhan extends BaseToolBarActivity {
    Database db;
    String id = "";
    String type_kh = "";

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity
    public int getLayoutId() {
        return R.layout.activity_cttinnhan;
    }

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        Cursor cursor = null;
        JSONException e;
        String str;
        String str2;
        JSONObject json = null;
        JSONObject json2 = null;
        JSONObject json3;
        double mKetQua;
        TextView tview12;
        TextView tview13;
        TextView tview5;
        TextView tview1;
        String str3;
        String str4;
        TextView tview52;
        JSONObject jsonDang = null;
        String str5 = "loa";
        String str6 = "deb";
        String str7 = "xn";
        String str8 = "det";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cttinnhan);
        double mTongTien = 0.0d;
        double mKetQua2 = 0.0d;
        this.id = getIntent().getStringExtra("m_ID");
        this.db = new Database(this);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        Cursor cursor2 = this.db.GetData("Select * From tbl_tinnhanS Where ID = " + this.id);
        cursor2.moveToFirst();
        String Str = "SElect CASE \nWHEN the_loai = 'xi' And length(so_chon) = 5 THEN 'xi2' \nWHEN the_loai = 'xi' And length(so_chon) = 8 THEN 'xi3' \nWHEN the_loai = 'xi' And length(so_chon) = 11 THEN 'xi4' \nWHEN the_loai = 'xia' And length(so_chon) = 5 THEN 'xia2' \nWHEN the_loai = 'xia' And length(so_chon) = 8 THEN 'xia3' \nWHEN the_loai = 'xia' And length(so_chon) = 11 THEN 'xia4' \nELSE the_loai END theloai, sum(diem), sum(diem*so_nhay) as An\n, sum (tong_tien)/1000 as kq \n, sum(Ket_qua)/1000 as tienCuoi\n From tbl_soctS \n Where ten_kh = '" + cursor2.getString(4) + "' and ngay_nhan = '" + cursor2.getString(1) + "' and so_tin_nhan = " + cursor2.getString(7) + " Group by theloai";
        Cursor ct_tin = this.db.GetData(Str);
        JSONObject json4 = new JSONObject();
        while (ct_tin.moveToNext()) {
            try {
                JSONObject jsonDang2 = new JSONObject();
                jsonDang2.put("diem", ct_tin.getDouble(1));
                jsonDang2.put("diem_an", ct_tin.getDouble(2));
                jsonDang2.put("tong_tien", ct_tin.getDouble(3));
                jsonDang2.put("ket_qua", ct_tin.getDouble(4));
                try {
                    json4.put(ct_tin.getString(0), jsonDang2.toString());
                    json4 = json4;
                    str6 = str6;
                    Str = Str;
                    cursor2 = cursor2;
                    str5 = str5;
                    str7 = str7;
                    str8 = str8;
                } catch (JSONException e2) {
                    e = e2;
                    cursor = cursor2;
                    e.printStackTrace();
                    cursor.close();
                    ct_tin.close();
                }
            } catch (JSONException e3) {
                e = e3;
                cursor = cursor2;
                e.printStackTrace();
                cursor.close();
                ct_tin.close();
            }
        }
        try {
            if (json4.has("dea")) {
                ((LinearLayout) findViewById(R.id.liner_deA)).setVisibility(View.VISIBLE);
                JSONObject jsonDang3 = new JSONObject(json4.getString("dea"));
                str2 = "dec";
                ((TextView) findViewById(R.id.tv_diemDeA)).setText(decimalFormat.format(jsonDang3.getDouble("diem")));
                str = "ded";
                ((TextView) findViewById(R.id.tv_AnDeA)).setText(decimalFormat.format(jsonDang3.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_DeA)).setText(decimalFormat.format(jsonDang3.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaDeA)).setText(decimalFormat.format(jsonDang3.getDouble("ket_qua")));
                mTongTien = 0.0d + jsonDang3.getDouble("tong_tien");
                mKetQua2 = 0.0d + jsonDang3.getDouble("ket_qua");
            } else {
                str = "ded";
                str2 = "dec";
            }
            if (json4.has(str2)) {
                ((LinearLayout) findViewById(R.id.liner_deC)).setVisibility(View.VISIBLE);
                JSONObject jsonDang4 = new JSONObject(json4.getString(str2));
                ((TextView) findViewById(R.id.tv_diemDeC)).setText(decimalFormat.format(jsonDang4.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_AnDeC)).setText(decimalFormat.format(jsonDang4.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_DeC)).setText(decimalFormat.format(jsonDang4.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaDeC)).setText(decimalFormat.format(jsonDang4.getDouble("ket_qua")));
                mTongTien += jsonDang4.getDouble("tong_tien");
                mKetQua2 += jsonDang4.getDouble("ket_qua");
            }
            if (json4.has(str)) {
                ((LinearLayout) findViewById(R.id.liner_deD)).setVisibility(View.VISIBLE);
                JSONObject jsonDang5 = new JSONObject(json4.getString(str));
                ((TextView) findViewById(R.id.tv_diemDeD)).setText(decimalFormat.format(jsonDang5.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_AnDeD)).setText(decimalFormat.format(jsonDang5.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_DeD)).setText(decimalFormat.format(jsonDang5.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaDeD)).setText(decimalFormat.format(jsonDang5.getDouble("ket_qua")));
                mTongTien += jsonDang5.getDouble("tong_tien");
                mKetQua2 += jsonDang5.getDouble("ket_qua");
            }
            if (json4.has(str8)) {
                ((LinearLayout) findViewById(R.id.liner_deT)).setVisibility(View.VISIBLE);
                JSONObject jsonDang6 = new JSONObject(json4.getString(str8));
                ((TextView) findViewById(R.id.tv_diemDeT)).setText(decimalFormat.format(jsonDang6.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_AnDeT)).setText(decimalFormat.format(jsonDang6.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_DeT)).setText(decimalFormat.format(jsonDang6.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaDeT)).setText(decimalFormat.format(jsonDang6.getDouble("ket_qua")));
                mTongTien += jsonDang6.getDouble("tong_tien");
                mKetQua2 += jsonDang6.getDouble("ket_qua");
            }
            if (json4.has(str7)) {
                ((LinearLayout) findViewById(R.id.liner_XN)).setVisibility(View.VISIBLE);
                JSONObject jsonDang7 = new JSONObject(json4.getString(str7));
                ((TextView) findViewById(R.id.tv_diemxn)).setText(decimalFormat.format(jsonDang7.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_anxn)).setText(decimalFormat.format(jsonDang7.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_Xn)).setText(decimalFormat.format(jsonDang7.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaXn)).setText(decimalFormat.format(jsonDang7.getDouble("ket_qua")));
                mTongTien += jsonDang7.getDouble("tong_tien");
                mKetQua2 += jsonDang7.getDouble("ket_qua");
            }
            if (json4.has(str6)) {
                ((LinearLayout) findViewById(R.id.liner_deB)).setVisibility(View.VISIBLE);
                JSONObject jsonDang8 = new JSONObject(json4.getString(str6));
                ((TextView) findViewById(R.id.tv_diemDe)).setText(decimalFormat.format(jsonDang8.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_AnDe)).setText(decimalFormat.format(jsonDang8.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_De)).setText(decimalFormat.format(jsonDang8.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaDe)).setText(decimalFormat.format(jsonDang8.getDouble("ket_qua")));
                mTongTien += jsonDang8.getDouble("tong_tien");
                mKetQua2 += jsonDang8.getDouble("ket_qua");
            }
            if (json4.has(str5)) {
                ((LinearLayout) findViewById(R.id.liner_loa)).setVisibility(View.VISIBLE);
                JSONObject jsonDang9 = new JSONObject(json4.getString(str5));
                ((TextView) findViewById(R.id.tv_diemloa)).setText(decimalFormat.format(jsonDang9.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_anloa)).setText(decimalFormat.format(jsonDang9.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_Loa)).setText(decimalFormat.format(jsonDang9.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaLoa)).setText(decimalFormat.format(jsonDang9.getDouble("ket_qua")));
                mTongTien += jsonDang9.getDouble("tong_tien");
                mKetQua2 += jsonDang9.getDouble("ket_qua");
            }
            if (json4.has("bca")) {
                ((LinearLayout) findViewById(R.id.liner_bca)).setVisibility(View.VISIBLE);
                JSONObject jsonDang10 = new JSONObject(json4.getString("bca"));
                ((TextView) findViewById(R.id.tv_diembca)).setText(decimalFormat.format(jsonDang10.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_anbca)).setText(decimalFormat.format(jsonDang10.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_Bca)).setText(decimalFormat.format(jsonDang10.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaBca)).setText(decimalFormat.format(jsonDang10.getDouble("ket_qua")));
                mTongTien += jsonDang10.getDouble("tong_tien");
                mKetQua2 += jsonDang10.getDouble("ket_qua");
            }
            if (json4.has("lo")) {
                ((LinearLayout) findViewById(R.id.liner_lo)).setVisibility(View.VISIBLE);
                JSONObject jsonDang11 = new JSONObject(json4.getString("lo"));
                ((TextView) findViewById(R.id.tv_diemlo)).setText(decimalFormat.format(jsonDang11.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_anlo)).setText(decimalFormat.format(jsonDang11.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_Lo)).setText(decimalFormat.format(jsonDang11.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaLo)).setText(decimalFormat.format(jsonDang11.getDouble("ket_qua")));
                mTongTien += jsonDang11.getDouble("tong_tien");
                mKetQua2 += jsonDang11.getDouble("ket_qua");
            }
            if (json4.has("xi2")) {
                ((LinearLayout) findViewById(R.id.lnxi2)).setVisibility(View.VISIBLE);
                JSONObject jsonDang12 = new JSONObject(json4.getString("xi2"));
                ((TextView) findViewById(R.id.tv_diemxi2)).setText(decimalFormat.format(jsonDang12.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_anxi2)).setText(decimalFormat.format(jsonDang12.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_Xi2)).setText(decimalFormat.format(jsonDang12.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaXi2)).setText(decimalFormat.format(jsonDang12.getDouble("ket_qua")));
                mTongTien += jsonDang12.getDouble("tong_tien");
                mKetQua2 += jsonDang12.getDouble("ket_qua");
            }
            if (json4.has("xi3")) {
                ((LinearLayout) findViewById(R.id.lnxi3)).setVisibility(View.VISIBLE);
                JSONObject jsonDang13 = new JSONObject(json4.getString("xi3"));
                ((TextView) findViewById(R.id.tv_diemxi3)).setText(decimalFormat.format(jsonDang13.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_anxi3)).setText(decimalFormat.format(jsonDang13.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_Xi3)).setText(decimalFormat.format(jsonDang13.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaXi3)).setText(decimalFormat.format(jsonDang13.getDouble("ket_qua")));
                mTongTien += jsonDang13.getDouble("tong_tien");
                mKetQua2 += jsonDang13.getDouble("ket_qua");
            }
            if (json4.has("xi4")) {
                ((LinearLayout) findViewById(R.id.lnxi4)).setVisibility(View.VISIBLE);
                JSONObject jsonDang14 = new JSONObject(json4.getString("xi4"));
                ((TextView) findViewById(R.id.tv_diemxi4)).setText(decimalFormat.format(jsonDang14.getDouble("diem")));
                ((TextView) findViewById(R.id.tv_anxi4)).setText(decimalFormat.format(jsonDang14.getDouble("diem_an")));
                ((TextView) findViewById(R.id.tv_no_Xi4)).setText(decimalFormat.format(jsonDang14.getDouble("tong_tien")));
                ((TextView) findViewById(R.id.tv_no_ThangThuaXi4)).setText(decimalFormat.format(jsonDang14.getDouble("ket_qua")));
                mTongTien += jsonDang14.getDouble("tong_tien");
                mKetQua2 += jsonDang14.getDouble("ket_qua");
            }
            if (json4.has("xia2")) {
                try {
                    ((LinearLayout) findViewById(R.id.lnxia2)).setVisibility(View.VISIBLE);
                    JSONObject jsonDang15 = new JSONObject(json4.getString("xia2"));
                    ((TextView) findViewById(R.id.tv_diemxia2)).setText(decimalFormat.format(jsonDang15.getDouble("diem")));
                    json = json4;
                    try {
                        ((TextView) findViewById(R.id.tv_anxia2)).setText(decimalFormat.format(jsonDang15.getDouble("diem_an")));
                        ((TextView) findViewById(R.id.tv_no_Xia2)).setText(decimalFormat.format(jsonDang15.getDouble("tong_tien")));
                        ((TextView) findViewById(R.id.tv_no_ThangThuaXia2)).setText(decimalFormat.format(jsonDang15.getDouble("ket_qua")));
                        mTongTien += jsonDang15.getDouble("tong_tien");
                        mKetQua2 += jsonDang15.getDouble("ket_qua");
                    } catch (JSONException e5) {
                        e = e5;
                        cursor = cursor2;
                        e.printStackTrace();
                        cursor.close();
                        ct_tin.close();
                    }
                } catch (JSONException e6) {
                    e = e6;
                    cursor = cursor2;
                    e.printStackTrace();
                    cursor.close();
                    ct_tin.close();
                }
            } else {
                json = json4;
            }
            if (json.has("xia3")) {
                try {
                    ((LinearLayout) findViewById(R.id.lnxia3)).setVisibility(View.VISIBLE);
                    jsonDang = new JSONObject(json.getString("xia3"));
                    ((TextView) findViewById(R.id.tv_diemxia3)).setText(decimalFormat.format(jsonDang.getDouble("diem")));
                    json2 = json;
                } catch (JSONException e7) {
                    e = e7;
                    cursor = cursor2;
                    e.printStackTrace();
                    cursor.close();
                    ct_tin.close();
                }
                try {
                    ((TextView) findViewById(R.id.tv_anxia3)).setText(decimalFormat.format(jsonDang.getDouble("diem_an")));
                    ((TextView) findViewById(R.id.tv_no_Xia3)).setText(decimalFormat.format(jsonDang.getDouble("tong_tien")));
                    ((TextView) findViewById(R.id.tv_no_ThangThuaXia3)).setText(decimalFormat.format(jsonDang.getDouble("ket_qua")));
                    mTongTien += jsonDang.getDouble("tong_tien");
                    mKetQua2 += jsonDang.getDouble("ket_qua");
                } catch (JSONException e8) {
                    e = e8;
                    cursor = cursor2;
                    e.printStackTrace();
                    cursor.close();
                    ct_tin.close();
                }
            } else {
                json2 = json;
            }
            if (json2.has("xia4")) {
                try {
                    ((LinearLayout) findViewById(R.id.lnxia4)).setVisibility(View.VISIBLE);
                    JSONObject jsonDang16 = new JSONObject(json2.getString("xia4"));
                    ((TextView) findViewById(R.id.tv_diemxia4)).setText(decimalFormat.format(jsonDang16.getDouble("diem")));
                    json3 = json2;
                    ((TextView) findViewById(R.id.tv_anxia4)).setText(decimalFormat.format(jsonDang16.getDouble("diem_an")));
                    ((TextView) findViewById(R.id.tv_no_Xia4)).setText(decimalFormat.format(jsonDang16.getDouble("tong_tien")));
                    ((TextView) findViewById(R.id.tv_no_ThangThuaXia4)).setText(decimalFormat.format(jsonDang16.getDouble("ket_qua")));
                    mTongTien += jsonDang16.getDouble("tong_tien");
                    mKetQua2 += jsonDang16.getDouble("ket_qua");
                    if (json3.has("bc")) {
                        ((LinearLayout) findViewById(R.id.ln_bc)).setVisibility(View.VISIBLE);
                        JSONObject jsonDang17 = new JSONObject(json3.getString("bc"));
                        ((TextView) findViewById(R.id.tv_diembc)).setText(decimalFormat.format(jsonDang17.getDouble("diem")));
                        ((TextView) findViewById(R.id.tv_anbc)).setText(decimalFormat.format(jsonDang17.getDouble("diem_an")));
                        ((TextView) findViewById(R.id.tv_no_Bc)).setText(decimalFormat.format(jsonDang17.getDouble("tong_tien")));
                        ((TextView) findViewById(R.id.tv_no_ThangThuaBc)).setText(decimalFormat.format(jsonDang17.getDouble("ket_qua")));
                        mTongTien += jsonDang17.getDouble("tong_tien");
                        mKetQua = mKetQua2 + jsonDang17.getDouble("ket_qua");
                        tview12 = (TextView) findViewById(R.id.tv_no_Tong);
                        tview12.setText(decimalFormat.format(mTongTien));
                        tview13 = (TextView) findViewById(R.id.tv_no_ThangThua);
                        tview13.setText(decimalFormat.format(mKetQua));
                        tview5 = (TextView) findViewById(R.id.tv_no_rp_nd);
                        cursor = cursor2;

                        tview5.setText(cursor.getString(8));
                        ((TextView) findViewById(R.id.tv_no_KH)).setText(cursor.getString(4));
                        ((TextView) findViewById(R.id.tv_no_TinNhan)).setText(cursor.getString(7));
                        ((TextView) findViewById(R.id.tv_no_TG_nhan)).setText(cursor.getString(2));
                        tview1 = (TextView) findViewById(R.id.tv_ndpt);
                        str3 = cursor.getString(10);
                        Spannable wordtoSpan = new SpannableString(str3);
                        int i1 = 0;
                        while (i1 < str3.length() - 1) {
                            if (str3.substring(i1, i1 + 2).indexOf("*") > -1) {
                                int i2 = i1;
                                while (true) {
                                    if (i2 <= 0) {
                                        str4 = str3;
                                        tview52 = tview5;
                                        break;
                                    }
                                    tview52 = tview5;
                                    if (str3.substring(i2, i2 + 1).indexOf(",") > -1) {
                                        str4 = str3;
                                        break;
                                    } else if (str3.substring(i2, i2 + 1).indexOf(":") > -1) {
                                        str4 = str3;
                                        break;
                                    } else {
                                        wordtoSpan.setSpan(new ForegroundColorSpan((int) SupportMenu.CATEGORY_MASK), i2, i1 + 1, 33);
                                        i2--;
                                        tview5 = tview52;
                                        str3 = str3;
                                    }
                                }
                            } else {
                                str4 = str3;
                                tview52 = tview5;
                            }
                            i1++;
                            tview13 = tview13;
                            tview12 = tview12;
                            tview5 = tview52;
                            str3 = str4;
                        }
                        tview1.setText(wordtoSpan);
                    } else {
                        mKetQua = mKetQua2;
                    }
                } catch (JSONException e9) {
                    e = e9;
                    cursor = cursor2;
                    e.printStackTrace();
                    cursor.close();
                    ct_tin.close();
                }
            } else {
                json3 = json2;
            }


        } catch (JSONException e15) {
            cursor = cursor2;
            e = e15;
            e.printStackTrace();
            cursor.close();
            ct_tin.close();
        }
        cursor.close();
        ct_tin.close();
    }
}