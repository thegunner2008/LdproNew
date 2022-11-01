package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tamhoang.ldpro4.Activity.Activity_Tinnhan;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_NoRP3 extends Fragment {
    Database db;
    List<JSONObject> jsonValues;
    ListView lv_no_tinnhan;
    String mDate;
    private List<Integer> mID = new ArrayList();
    private ArrayList<String> mTen = new ArrayList<>();
    int sp_Position;
    Spinner sp_khachhang;
    String str = "";
    String tenKhach;

    @SuppressLint("ClickableViewAccessibility")
    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_norp3, container, false);
        this.db = new Database(getActivity());
        this.lv_no_tinnhan = (ListView) v.findViewById(R.id.no_rp_tinnhan);
        this.lv_no_tinnhan.setOnItemClickListener((adapterView, view, position, id) -> {
            String[] menus = {"Sửa", "Xóa"};
            PopupMenu popupL = new PopupMenu(getActivity(), view);
            for (int i = 0; i < menus.length; i++) {
                popupL.getMenu().add(1, i, i, menus[i]);
            }
            popupL.setOnMenuItemClickListener(item -> {
                int order = item.getOrder();
                if (order == 0) {
                    Intent intent = new Intent(getActivity(), Activity_Tinnhan.class);
                    intent.putExtra("m_ID", mID.get(position) + "");
                    startActivity(intent);
                } else if (order == 1) {
                    AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
                    bui.setTitle("Xoá tin nhắn này?");
                    bui.setPositiveButton("YES", (dialog, which) -> {
                        Database database = db;
                        Cursor cursor = database.GetData("Select * From tbl_tinnhanS where ID = " + mID.get(position));
                        cursor.moveToFirst();
                        Database database2 = db;
                        database2.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + cursor.getString(1) + "' AND ten_kh = '" + cursor.getString(4) + "' AND so_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3));
                        Database database3 = db;
                        database3.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + cursor.getString(1) + "' AND ten_kh = '" + cursor.getString(4) + "' AND so_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3));
                        cursor.close();
                        Toast.makeText(getActivity(), "Đã xóa tin", Toast.LENGTH_LONG).show();
                        lv_report_sms();
                    });
                    bui.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                    bui.create().show();
                }
                return true;
            });
            popupL.show();
        });
        this.sp_khachhang = (Spinner) v.findViewById(R.id.sp_khachhang);
        this.mTen.add("Lọc theo khách");
        this.sp_khachhang.setAdapter(new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.mTen));
        this.sp_khachhang.setSelection(0);
        this.sp_khachhang.setOnTouchListener((v1, event) -> {
            mDate = MainActivity.Get_date();
            mTen.clear();
            Database database = db;
            Cursor cursor = database.GetData("Select ten_kh From tbl_soctS WHERE ngay_nhan = '" + mDate + "' GROUP by ten_kh Order by ten_kh");
            while (cursor.moveToNext()) {
                mTen.add(cursor.getString(0));
            }
            cursor.close();
            if (mTen.size() == 0) {
                mTen.add("Hôm nay chưa có tin nhắn");
            }
            sp_khachhang.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_item, mTen));
            try {
                sp_khachhang.setSelection(mTen.indexOf(tenKhach));
            } catch (Exception ignored) {
            }
            return false;
        });
        this.sp_khachhang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                sp_Position = position;
                Frag_NoRP3 frag_NoRP3 = Frag_NoRP3.this;
                frag_NoRP3.str = " AND tbl_soctS.ten_kh = '" + ((String) mTen.get(sp_Position)) + "'";
                Frag_NoRP3 frag_NoRP32 = Frag_NoRP3.this;
                frag_NoRP32.tenKhach = (String) frag_NoRP32.mTen.get(sp_Position);
                lv_report_sms();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        registerForContextMenu(this.lv_no_tinnhan);
        return v;
    }

    public void lv_report_sms() {
        String getDate = MainActivity.Get_date();
        Cursor cursor = this.db.GetData("Select * From tbl_tinnhanS Where ngay_nhan = '" + getDate + "' and ten_kh = '" + this.mTen.get(this.sp_Position) + "' AND phat_hien_loi = 'ok' Order by type_kh, so_tin_nhan");
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        this.jsonValues = new ArrayList();
        if (cursor != null) {
            try {
                this.mID.clear();
                while (cursor.moveToNext()) {
                    Cursor cursor1 = this.db.GetData("SElect CASE \nWHEN the_loai = 'xi' And length(so_chon) = 5 THEN 'xi2' \nWHEN the_loai = 'xi' And length(so_chon) = 8 THEN 'xi3' \nWHEN the_loai = 'xi' And length(so_chon) = 11 THEN 'xi4' \nWHEN the_loai = 'xia' And length(so_chon) = 5 THEN 'xia2' \nWHEN the_loai = 'xia' And length(so_chon) = 8 THEN 'xia3' \nWHEN the_loai = 'xia' And length(so_chon) = 11 THEN 'xia4' \nELSE the_loai END theloai, sum(diem), sum(diem*so_nhay) as An\n, sum (tong_tien)/1000 as kq \n, sum(Ket_qua)/1000 as tienCuoi\n From tbl_soctS \n Where ten_kh = '" + this.mTen.get(this.sp_Position) + "' and ngay_nhan = '" + getDate + "' and So_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3) + " Group by theloai");
                    if (cursor1 != null) {
                        JSONObject json_tinnhan = new JSONObject();
                        json_tinnhan.put("ID", cursor.getInt(0));
                        json_tinnhan.put("gio_nhan", cursor.getString(2));
                        json_tinnhan.put("type_kh", cursor.getString(3));
                        json_tinnhan.put("ten_KH", cursor.getString(4));
                        json_tinnhan.put("so_tinnhan", cursor.getString(7));
                        json_tinnhan.put("tin_goc", cursor.getString(8));
                        json_tinnhan.put("nd_phantich", cursor.getString(10));
                        JSONObject json_chitiet = new JSONObject();
                        double tong_tien = 0.0d;
                        double ket_qua = 0.0d;
                        while (cursor1.moveToNext()) {
                            try {
                                json_chitiet.put("the_loai", cursor1.getString(0));
                                json_chitiet.put("diem", decimalFormat.format((long) cursor1.getInt(1)));
                                json_chitiet.put("diem_an", decimalFormat.format((long) cursor1.getInt(2)));
                                json_chitiet.put("tong_tien", decimalFormat.format((long) cursor1.getInt(3)));
                                json_chitiet.put("ket_qua", decimalFormat.format((long) cursor1.getInt(4)));
                                tong_tien += cursor1.getDouble(3);
                                ket_qua += cursor1.getDouble(4);
                                json_tinnhan.put(cursor1.getString(0), json_chitiet.toString());
                            } catch (JSONException ignored) {}
                        }
                        try {
                            json_tinnhan.put("tong_tien", decimalFormat.format(tong_tien));
                            json_tinnhan.put("ket_qua", decimalFormat.format(ket_qua));
                            this.jsonValues.add(json_tinnhan);
                            this.mID.add(Integer.valueOf(cursor.getInt(0)));
                            cursor1.close();
                        } catch (JSONException ignored) {}
                    }
                }
                cursor.close();
            } catch (JSONException ignored) {}
        }
        if (getActivity() != null) {
            this.lv_no_tinnhan.setAdapter(new TinNhan_Adapter(getActivity(), R.layout.activity_tinnhan_lv, this.jsonValues));
        }
    }

    public class TinNhan_Adapter extends ArrayAdapter {
        public TinNhan_Adapter(Context context, int resource, List<JSONObject> objects) {
            super(context, resource, objects);
        }

        @SuppressLint("RestrictedApi")
        public View getView(int position, View mView, ViewGroup parent) {
            JSONObject jsonDang;
            JSONObject jsonDang2;
            JSONObject jsonDang3;
            @SuppressLint("WrongConstant") View mView2 = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.activity_tinnhan_lv, (ViewGroup) null);
            TextView tv_tongtien = (TextView) mView2.findViewById(R.id.tv_no_Tong);
            TextView tv_ket_qua = (TextView) mView2.findViewById(R.id.tv_ThangThua);
            TextView tv_tenKH = (TextView) mView2.findViewById(R.id.tv_ten_KH);
            TextView tv_gio_nhan = (TextView) mView2.findViewById(R.id.tv_TG_nhan);
            TextView tv_so_tn = (TextView) mView2.findViewById(R.id.tv_TinNhan);
            TextView tv_Tingoc = (TextView) mView2.findViewById(R.id.tv_NdGoc);
            TextView tv_tinPhantich = (TextView) mView2.findViewById(R.id.tv_ndpt);
            LinearLayout liner_deA = (LinearLayout) mView2.findViewById(R.id.liner_deA);
            LinearLayout liner_deC = (LinearLayout) mView2.findViewById(R.id.liner_deC);
            LinearLayout liner_deD = (LinearLayout) mView2.findViewById(R.id.liner_deD);
            LinearLayout liner_deT = (LinearLayout) mView2.findViewById(R.id.liner_deT);
            LinearLayout liner_Loa = (LinearLayout) mView2.findViewById(R.id.liner_loa);
            LinearLayout liner_Xi2 = (LinearLayout) mView2.findViewById(R.id.lnxi2);
            LinearLayout liner_Xi3 = (LinearLayout) mView2.findViewById(R.id.lnxi3);
            LinearLayout liner_Xi4 = (LinearLayout) mView2.findViewById(R.id.lnxi4);
            LinearLayout liner_Xia2 = (LinearLayout) mView2.findViewById(R.id.lnxia2);
            LinearLayout liner_Xia3 = (LinearLayout) mView2.findViewById(R.id.lnxia3);
            LinearLayout liner_Xia4 = (LinearLayout) mView2.findViewById(R.id.lnxia4);
            LinearLayout liner_Xn = (LinearLayout) mView2.findViewById(R.id.liner_XN);
            LinearLayout liner_Bca = (LinearLayout) mView2.findViewById(R.id.liner_bca);
            TextView tv_diem_Dea = (TextView) mView2.findViewById(R.id.tv_diemDeA);
            TextView tv_an_Dea = (TextView) mView2.findViewById(R.id.tv_AnDeA);
            TextView tv_goc_Dea = (TextView) mView2.findViewById(R.id.tv_no_DeA);
            TextView tv_ketqua_Dea = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDeA);
            TextView tv_diem_De = (TextView) mView2.findViewById(R.id.tv_diemDe);
            TextView tv_an_De = (TextView) mView2.findViewById(R.id.tv_AnDe);
            TextView tv_goc_De2 = (TextView) mView2.findViewById(R.id.tv_no_De);
            TextView tv_no_ThangThuaDe = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDe);
            TextView tv_diem_Det = (TextView) mView2.findViewById(R.id.tv_diemDeT);
            TextView tv_an_Det = (TextView) mView2.findViewById(R.id.tv_AnDeT);
            TextView tv_no_DeT = (TextView) mView2.findViewById(R.id.tv_no_DeT);
            TextView tv_ketqua_Det2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDeT);
            TextView tv_diem_Dec = (TextView) mView2.findViewById(R.id.tv_diemDeC);
            TextView tv_an_Dec = (TextView) mView2.findViewById(R.id.tv_AnDeC);
            TextView tv_goc_Dec = (TextView) mView2.findViewById(R.id.tv_no_DeC);
            TextView tv_ketqua_Dec2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDeC);
            TextView tv_diem_Ded = (TextView) mView2.findViewById(R.id.tv_diemDeD);
            TextView tv_an_Ded = (TextView) mView2.findViewById(R.id.tv_AnDeD);
            TextView tv_no_DeD = (TextView) mView2.findViewById(R.id.tv_no_DeD);
            TextView tv_no_ThangThuaDeD = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDeD);
            TextView tv_diem_Lo = (TextView) mView2.findViewById(R.id.tv_diemlo);
            TextView tv_an_Lo = (TextView) mView2.findViewById(R.id.tv_anlo);
            TextView tv_no_Lo = (TextView) mView2.findViewById(R.id.tv_no_Lo);
            TextView tv_no_ThangThuaLo = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaLo);
            TextView tv_diem_Loa = (TextView) mView2.findViewById(R.id.tv_diemloa);
            TextView tv_an_Loa = (TextView) mView2.findViewById(R.id.tv_anloa);
            TextView tv_goc_Loa = (TextView) mView2.findViewById(R.id.tv_no_Loa);
            TextView tv_no_ThangThuaLoa = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaLoa);
            TextView tv_diem_Xi2 = (TextView) mView2.findViewById(R.id.tv_diemxi2);
            TextView tv_an_Xi22 = (TextView) mView2.findViewById(R.id.tv_anxi2);
            TextView tv_no_Xi2 = (TextView) mView2.findViewById(R.id.tv_no_Xi2);
            TextView tv_no_ThangThuaXi2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXi2);
            TextView tv_diem_Xi3 = (TextView) mView2.findViewById(R.id.tv_diemxi3);
            TextView tv_an_Xi3 = (TextView) mView2.findViewById(R.id.tv_anxi3);
            TextView tv_no_Xi3 = (TextView) mView2.findViewById(R.id.tv_no_Xi3);
            TextView tv_no_ThangThuaXi3 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXi3);
            TextView tv_diem_Xi4 = (TextView) mView2.findViewById(R.id.tv_diemxi4);
            TextView tv_an_Xi4 = (TextView) mView2.findViewById(R.id.tv_anxi4);
            TextView tv_no_Xi4 = (TextView) mView2.findViewById(R.id.tv_no_Xi4);
            TextView tv_no_ThangThuaXi4 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXi4);
            TextView tv_diem_Xia2 = (TextView) mView2.findViewById(R.id.tv_diemxia2);
            TextView tv_an_Xia2 = (TextView) mView2.findViewById(R.id.tv_anxia2);
            TextView tv_no_Xia2 = (TextView) mView2.findViewById(R.id.tv_no_Xia2);
            TextView tv_no_ThangThuaXia2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXia2);
            TextView tv_diem_Xia3 = (TextView) mView2.findViewById(R.id.tv_diemxia3);
            TextView tv_an_Xia3 = (TextView) mView2.findViewById(R.id.tv_anxia3);
            TextView tv_no_Xia3 = (TextView) mView2.findViewById(R.id.tv_no_Xia3);
            TextView tv_no_ThangThuaXia3 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXia3);
            TextView tv_diem_Xia4 = (TextView) mView2.findViewById(R.id.tv_diemxia4);
            TextView tv_an_Xia4 = (TextView) mView2.findViewById(R.id.tv_anxia4);
            TextView tv_no_Xia4 = (TextView) mView2.findViewById(R.id.tv_no_Xia4);
            TextView tv_no_ThangThuaXia4 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXia4);
            TextView tv_diem_Xn = (TextView) mView2.findViewById(R.id.tv_diemxn);
            TextView tv_an_Xn2 = (TextView) mView2.findViewById(R.id.tv_anxn);
            TextView tv_no_Xn = (TextView) mView2.findViewById(R.id.tv_no_Xn);
            TextView tv_ketqua_Xn2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXn);
            TextView tv_diem_Bc = (TextView) mView2.findViewById(R.id.tv_diembc);
            TextView tv_an_Bc = (TextView) mView2.findViewById(R.id.tv_anbc);
            TextView tv_no_Bc = (TextView) mView2.findViewById(R.id.tv_no_Bc);
            TextView tv_no_ThangThuaBc = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaBc);
            TextView tv_diem_Bca = (TextView) mView2.findViewById(R.id.tv_diembca);
            TextView tv_an_Bca = (TextView) mView2.findViewById(R.id.tv_anbca);
            TextView tv_no_Bca = (TextView) mView2.findViewById(R.id.tv_no_Bca);
            TextView tv_no_ThangThuaBca = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaBca);
            JSONObject Json = jsonValues.get(position);
            try {
                tv_Tingoc.setText(Json.getString("tin_goc"));
                tv_tenKH.setText(Json.getString("ten_KH"));
                if (Json.getString("type_kh").contains("2")) {
                    tv_tenKH.setTextColor(Color.parseColor("#1a40ea"));
                }
                tv_so_tn.setText(Json.getString("so_tinnhan"));
                tv_gio_nhan.setText(Json.getString("gio_nhan"));
                tv_tongtien.setText(Json.getString("tong_tien"));
                tv_ket_qua.setText(Json.getString("ket_qua"));
                String nd_phantich = Json.getString("nd_phantich");
                Spannable wordtoSpan2 = new SpannableString(nd_phantich);
                int i1 = 0;
                while (i1 < nd_phantich.length() - 1) {
                    try {
                        if (nd_phantich.substring(i1, i1 + 2).contains("*")) {
                            int i2 = i1;
                            while (i2 > 0) {
                                String ch = nd_phantich.substring(i2, i2 + 1);
                                if (ch.contains(",") || ch.contains(":")) {
                                    break;
                                } else {
                                    wordtoSpan2.setSpan(new ForegroundColorSpan(SupportMenu.CATEGORY_MASK), i2, i1 + 1, 33);
                                    i2--;
                                }
                            }
                        }
                        i1++;
                    } catch (Exception e5) {
                        e5.printStackTrace();
                        return mView2;
                    }
                }
                tv_tinPhantich.setText(wordtoSpan2);
                if (Json.has("dea")) {
                    try {
                        liner_deA.setVisibility(View.VISIBLE);
                        JSONObject jsonDang4 = new JSONObject(Json.getString("dea"));
                        tv_diem_Dea.setText(jsonDang4.getString("diem"));
                        tv_an_Dea.setText(jsonDang4.getString("diem_an"));
                        tv_goc_Dea.setText(jsonDang4.getString("tong_tien"));
                        tv_ketqua_Dea.setText(jsonDang4.getString("ket_qua"));
                    } catch (JSONException e8) {
                        e8.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("deb")) {
                    try {
                        JSONObject jsonDang5 = new JSONObject(Json.getString("deb"));
                        tv_diem_De.setText(jsonDang5.getString("diem"));
                        tv_an_De.setText(jsonDang5.getString("diem_an"));
                        tv_goc_De2.setText(jsonDang5.getString("tong_tien"));
                        tv_no_ThangThuaDe.setText(jsonDang5.getString("ket_qua"));
                    } catch (JSONException e9) {
                        e9.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("det")) {
                    try {
                        liner_deT.setVisibility(View.VISIBLE);
                        JSONObject jsonDang6 = new JSONObject(Json.getString("det"));
                        tv_diem_Det.setText(jsonDang6.getString("diem"));
                        tv_an_Det.setText(jsonDang6.getString("diem_an"));
                        tv_no_DeT.setText(jsonDang6.getString("tong_tien"));
                    } catch (JSONException e19) {
                        e19.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("dec")) {
                    try {
                        liner_deC.setVisibility(View.VISIBLE);
                        jsonDang3 = new JSONObject(Json.getString("dec"));
                        tv_diem_Dec.setText(jsonDang3.getString("diem"));
                        tv_an_Dec.setText(jsonDang3.getString("diem_an"));
                    } catch (JSONException e22) {
                        e22.printStackTrace();
                        return mView2;
                    }
                    try {
                        tv_goc_Dec.setText(jsonDang3.getString("tong_tien"));
                        tv_ketqua_Dec2.setText(jsonDang3.getString("ket_qua"));
                    } catch (JSONException e24) {
                        e24.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("ded")) {
                    try {
                        liner_deD.setVisibility(View.VISIBLE);
                        JSONObject jsonDang7 = new JSONObject(Json.getString("ded"));
                        tv_diem_Ded.setText(jsonDang7.getString("diem"));
                        tv_an_Ded.setText(jsonDang7.getString("diem_an"));
                        tv_no_DeD.setText(jsonDang7.getString("tong_tien"));
                        tv_no_ThangThuaDeD.setText(jsonDang7.getString("ket_qua"));
                    } catch (JSONException e30) {
                        e30.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("lo")) {
                    try {
                        JSONObject jsonDang8 = new JSONObject(Json.getString("lo"));
                        tv_diem_Lo.setText(jsonDang8.getString("diem"));
                        tv_an_Lo.setText(jsonDang8.getString("diem_an"));
                        tv_no_Lo.setText(jsonDang8.getString("tong_tien"));
                        tv_no_ThangThuaLo.setText(jsonDang8.getString("ket_qua"));
                    } catch (JSONException e38) {
                        e38.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("loa")) {
                    try {
                        liner_Loa.setVisibility(View.VISIBLE);
                        JSONObject jsonDang9 = new JSONObject(Json.getString("loa"));
                        tv_diem_Loa.setText(jsonDang9.getString("diem"));
                        tv_an_Loa.setText(jsonDang9.getString("diem_an"));
                        tv_goc_Loa.setText(jsonDang9.getString("tong_tien"));
                        tv_no_ThangThuaLoa.setText(jsonDang9.getString("ket_qua"));
                    } catch (JSONException e43) {
                        e43.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("xi2")) {
                    try {
                        liner_Xi2.setVisibility(View.VISIBLE);
                        JSONObject jsonDang10 = new JSONObject(Json.getString("xi2"));
                        tv_diem_Xi2.setText(jsonDang10.getString("diem"));
                        tv_an_Xi22.setText(jsonDang10.getString("diem_an"));
                        tv_no_Xi2.setText(jsonDang10.getString("tong_tien"));
                        tv_no_ThangThuaXi2.setText(jsonDang10.getString("ket_qua"));
                    } catch (JSONException e49) {
                        e49.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("xi3")) {
                    try {
                        liner_Xi3.setVisibility(View.VISIBLE);
                        JSONObject jsonDang11 = new JSONObject(Json.getString("xi3"));
                        tv_diem_Xi3.setText(jsonDang11.getString("diem"));
                        tv_an_Xi3.setText(jsonDang11.getString("diem_an"));
                        tv_no_Xi3.setText(jsonDang11.getString("tong_tien"));
                        tv_no_ThangThuaXi3.setText(jsonDang11.getString("ket_qua"));
                    } catch (JSONException e54) {
                        e54.printStackTrace();
                        return mView2;
                    }
                }
                try {
                    if (Json.has("xi4")) {
                        liner_Xi4.setVisibility(View.VISIBLE);
                        JSONObject jsonDang12 = new JSONObject(Json.getString("xi4"));
                        tv_diem_Xi4.setText(jsonDang12.getString("diem"));
                        tv_an_Xi4.setText(jsonDang12.getString("diem_an"));
                        tv_no_Xi4.setText(jsonDang12.getString("tong_tien"));
                        tv_no_ThangThuaXi4.setText(jsonDang12.getString("ket_qua"));
                    }
                } catch (JSONException e61) {
                    e61.printStackTrace();
                    return mView2;
                }
                if (Json.has("xia2")) {
                    try {
                        liner_Xia2.setVisibility(View.VISIBLE);
                        JSONObject jsonDang13 = new JSONObject(Json.getString("xia2"));
                        tv_diem_Xia2.setText(jsonDang13.getString("diem"));
                        tv_an_Xia2.setText(jsonDang13.getString("diem_an"));
                        tv_no_Xia2.setText(jsonDang13.getString("tong_tien"));
                        tv_no_ThangThuaXia2.setText(jsonDang13.getString("ket_qua"));
                    } catch (JSONException e67) {
                        e67.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("xia3")) {
                    liner_Xia3.setVisibility(View.VISIBLE);
                    jsonDang2 = new JSONObject(Json.getString("xia3"));
                    tv_diem_Xia3.setText(jsonDang2.getString("diem"));
                    tv_an_Xia3.setText(jsonDang2.getString("diem_an"));
                    tv_no_Xia3.setText(jsonDang2.getString("tong_tien"));
                    tv_no_ThangThuaXia3.setText(jsonDang2.getString("ket_qua"));
                }
                if (Json.has("xia4")) {
                    try {
                        liner_Xia4.setVisibility(View.VISIBLE);
                        JSONObject jsonDang14 = new JSONObject(Json.getString("xia4"));
                        tv_diem_Xia4.setText(jsonDang14.getString("diem"));
                        tv_an_Xia4.setText(jsonDang14.getString("diem_an"));
                        tv_no_Xia4.setText(jsonDang14.getString("tong_tien"));
                        tv_no_ThangThuaXia4.setText(jsonDang14.getString("ket_qua"));
                    } catch (JSONException e78) {
                        e78.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("xn")) {
                    try {
                        liner_Xn.setVisibility(View.VISIBLE);
                        JSONObject jsonDang15 = new JSONObject(Json.getString("xn"));
                        tv_diem_Xn.setText(jsonDang15.getString("diem"));
                        tv_an_Xn2.setText(jsonDang15.getString("diem_an"));
                        tv_no_Xn.setText(jsonDang15.getString("tong_tien"));
                        tv_ketqua_Xn2.setText(jsonDang15.getString("ket_qua"));
                    } catch (JSONException e85) {
                        e85.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("bc")) {
                    try {
                        jsonDang = new JSONObject(Json.getString("bc"));
                        tv_diem_Bc.setText(jsonDang.getString("diem"));
                        tv_an_Bc.setText(jsonDang.getString("diem_an"));
                        tv_no_Bc.setText(jsonDang.getString("tong_tien"));
                        tv_no_ThangThuaBc.setText(jsonDang.getString("ket_qua"));
                    } catch (JSONException e90) {
                        e90.printStackTrace();
                        return mView2;
                    }
                }
                if (Json.has("bca")) {
                    try {
                        liner_Bca.setVisibility(View.VISIBLE);
                        JSONObject jsonDang16 = new JSONObject(Json.getString("bca"));
                        tv_diem_Bca.setText(jsonDang16.getString("diem"));
                        tv_an_Bca.setText(jsonDang16.getString("diem_an"));
                        tv_no_Bca.setText(jsonDang16.getString("tong_tien"));
                        tv_no_ThangThuaBca.setText(jsonDang16.getString("ket_qua"));
                    } catch (JSONException e97) {
                        e97.printStackTrace();
                        return mView2;
                    }
                }
            } catch (JSONException e102) {
                e102.printStackTrace();
                return mView2;
            }
            return mView2;
        }
    }
}