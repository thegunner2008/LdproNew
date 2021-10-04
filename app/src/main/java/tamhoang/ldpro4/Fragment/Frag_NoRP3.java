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
import android.view.MenuItem;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_norp3, container, false);
        this.db = new Database(getActivity());
        ListView listView = (ListView) v.findViewById(R.id.no_rp_tinnhan);
        this.lv_no_tinnhan = listView;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_NoRP3.AnonymousClass1 */

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                String[] menus = {"Sửa", "Xóa"};
                PopupMenu popupL = new PopupMenu(Frag_NoRP3.this.getActivity(), view);
                for (int i = 0; i < menus.length; i++) {
                    popupL.getMenu().add(1, i, i, menus[i]);
                }
                new AlertDialog.Builder(Frag_NoRP3.this.getActivity());
                /* class tamhoang.ldpro4.Fragment.Frag_NoRP3.AnonymousClass1.AnonymousClass1 */
                popupL.setOnMenuItemClickListener(item -> {
                    int order = item.getOrder();
                    if (order == 0) {
                        Intent intent = new Intent(Frag_NoRP3.this.getActivity(), Activity_Tinnhan.class);
                        intent.putExtra("m_ID", Frag_NoRP3.this.mID.get(position) + "");
                        Frag_NoRP3.this.startActivity(intent);
                    } else if (order == 1) {
                        AlertDialog.Builder bui = new AlertDialog.Builder(Frag_NoRP3.this.getActivity());
                        bui.setTitle("Xoá tin nhắn này?");
                        bui.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Database database = Frag_NoRP3.this.db;
                                Cursor cursor = database.GetData("Select * From tbl_tinnhanS where ID = " + Frag_NoRP3.this.mID.get(position));
                                cursor.moveToFirst();
                                Database database2 = Frag_NoRP3.this.db;
                                database2.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + cursor.getString(1) + "' AND ten_kh = '" + cursor.getString(4) + "' AND so_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3));
                                Database database3 = Frag_NoRP3.this.db;
                                database3.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + cursor.getString(1) + "' AND ten_kh = '" + cursor.getString(4) + "' AND so_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3));
                                cursor.close();
                                Toast.makeText(Frag_NoRP3.this.getActivity(), "Đã xóa tin", Toast.LENGTH_LONG).show();
                                Frag_NoRP3.this.lv_report_sms();
                            }
                        });
                        bui.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            /* class tamhoang.ldpro4.Fragment.Frag_NoRP3.AnonymousClass1.AnonymousClass1.AnonymousClass2 */

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        bui.create().show();
                    }
                    return true;
                });
                popupL.show();
            }
        });
        this.sp_khachhang = (Spinner) v.findViewById(R.id.sp_khachhang);
        this.mTen.add("Lọc theo khách");
        this.sp_khachhang.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.mTen));
        this.sp_khachhang.setSelection(0);
        this.sp_khachhang.setOnTouchListener(new View.OnTouchListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_NoRP3.AnonymousClass2 */

            public boolean onTouch(View v, MotionEvent event) {
                new MainActivity();
                Frag_NoRP3.this.mDate = MainActivity.Get_date();
                Frag_NoRP3.this.mTen.clear();
                Database database = Frag_NoRP3.this.db;
                Cursor cursor = database.GetData("Select ten_kh From tbl_soctS WHERE ngay_nhan = '" + Frag_NoRP3.this.mDate + "' GROUP by ten_kh Order by ten_kh");
                while (cursor.moveToNext()) {
                    Frag_NoRP3.this.mTen.add(cursor.getString(0));
                }
                cursor.close();
                if (Frag_NoRP3.this.mTen.size() == 0) {
                    Frag_NoRP3.this.mTen.add("Hôm nay chưa có tin nhắn");
                }
                Frag_NoRP3.this.sp_khachhang.setAdapter((SpinnerAdapter) new ArrayAdapter<>(Frag_NoRP3.this.getActivity(), (int) R.layout.spinner_item, Frag_NoRP3.this.mTen));
                try {
                    Frag_NoRP3.this.sp_khachhang.setSelection(Frag_NoRP3.this.mTen.indexOf(Frag_NoRP3.this.tenKhach));
                } catch (Exception e) {
                }
                return false;
            }
        });
        this.sp_khachhang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_NoRP3.AnonymousClass3 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Frag_NoRP3.this.sp_Position = position;
                Frag_NoRP3 frag_NoRP3 = Frag_NoRP3.this;
                frag_NoRP3.str = " AND tbl_soctS.ten_kh = '" + ((String) Frag_NoRP3.this.mTen.get(Frag_NoRP3.this.sp_Position)) + "'";
                Frag_NoRP3 frag_NoRP32 = Frag_NoRP3.this;
                frag_NoRP32.tenKhach = (String) frag_NoRP32.mTen.get(Frag_NoRP3.this.sp_Position);
                Frag_NoRP3.this.lv_report_sms();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        registerForContextMenu(this.lv_no_tinnhan);
        return v;
    }

    public void lv_report_sms() {
        String mDate2;
        MainActivity mainActivity;
        MainActivity mainActivity2 = new MainActivity();
        String mDate3 = MainActivity.Get_date();
        Cursor cursor = this.db.GetData("Select * From tbl_tinnhanS Where ngay_nhan = '" + mDate3 + "' and ten_kh = '" + this.mTen.get(this.sp_Position) + "' AND phat_hien_loi = 'ok' Order by type_kh, so_tin_nhan");
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        this.jsonValues = new ArrayList();
        if (cursor != null) {
            try {
                this.mID.clear();
                while (cursor.moveToNext()) {
                    Cursor cursor1 = this.db.GetData("SElect CASE \nWHEN the_loai = 'xi' And length(so_chon) = 5 THEN 'xi2' \nWHEN the_loai = 'xi' And length(so_chon) = 8 THEN 'xi3' \nWHEN the_loai = 'xi' And length(so_chon) = 11 THEN 'xi4' \nWHEN the_loai = 'xia' And length(so_chon) = 5 THEN 'xia2' \nWHEN the_loai = 'xia' And length(so_chon) = 8 THEN 'xia3' \nWHEN the_loai = 'xia' And length(so_chon) = 11 THEN 'xia4' \nELSE the_loai END theloai, sum(diem), sum(diem*so_nhay) as An\n, sum (tong_tien)/1000 as kq \n, sum(Ket_qua)/1000 as tienCuoi\n From tbl_soctS \n Where ten_kh = '" + this.mTen.get(this.sp_Position) + "' and ngay_nhan = '" + mDate3 + "' and So_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3) + " Group by theloai");
                    if (cursor1 != null) {
                        JSONObject json_tinnhan = new JSONObject();
                        int i = 0;
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
                                json_chitiet.put("the_loai", cursor1.getString(i));
                                json_chitiet.put("diem", decimalFormat.format((long) cursor1.getInt(1)));
                                json_chitiet.put("diem_an", decimalFormat.format((long) cursor1.getInt(2)));
                                json_chitiet.put("tong_tien", decimalFormat.format((long) cursor1.getInt(3)));
                                json_chitiet.put("ket_qua", decimalFormat.format((long) cursor1.getInt(4)));
                                tong_tien += cursor1.getDouble(3);
                                ket_qua += cursor1.getDouble(4);
                                json_tinnhan.put(cursor1.getString(0), json_chitiet.toString());
                                cursor1 = cursor1;
                                i = 0;
                            } catch (JSONException e) {
                            }
                        }
                        mainActivity = mainActivity2;
                        mDate2 = mDate3;
                        try {
                            json_tinnhan.put("tong_tien", decimalFormat.format(tong_tien));
                            json_tinnhan.put("ket_qua", decimalFormat.format(ket_qua));
                            this.jsonValues.add(json_tinnhan);
                            this.mID.add(Integer.valueOf(cursor.getInt(0)));
                            cursor1.close();
                        } catch (JSONException e2) {
                        }
                    } else {
                        mainActivity = mainActivity2;
                        mDate2 = mDate3;
                    }
                    mainActivity2 = mainActivity;
                    mDate3 = mDate2;
                }
                cursor.close();
            } catch (JSONException e3) {
            }
        }
        if (getActivity() != null) {
            this.lv_no_tinnhan.setAdapter((ListAdapter) new TinNhan_Adapter(getActivity(), R.layout.activity_tinnhan_lv, this.jsonValues));
        }
    }

    /* access modifiers changed from: package-private */
    public class TinNhan_Adapter extends ArrayAdapter {
        public TinNhan_Adapter(Context context, int resource, List<JSONObject> objects) {
            super(context, resource, objects);
        }

        /* JADX INFO: Multiple debug info for r1v27 'tv_ketqua_Xn'  android.widget.TextView: [D('jsonDang' org.json.JSONObject), D('tv_ketqua_Bc' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r3v53 'tv_goc_Xn'  android.widget.TextView: [D('tv_goc_Xn' android.widget.TextView), D('tv_an_Xn' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v31 'tv_ketqua_Xia4'  android.widget.TextView: [D('jsonDang' org.json.JSONObject), D('tv_ketqua_Xn' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v37 'tv_ketqua_Xia3'  android.widget.TextView: [D('tv_goc_Xia4' android.widget.TextView), D('tv_ketqua_Xia4' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r3v74 'tv_goc_Xia3'  android.widget.TextView: [D('tv_an_Xia3' android.widget.TextView), D('tv_goc_Xia3' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v41 'tv_ketqua_Xia2'  android.widget.TextView: [D('jsonDang' org.json.JSONObject), D('tv_ketqua_Xia3' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v47 'tv_ketqua_Xi4'  android.widget.TextView: [D('tv_ketqua_Xia2' android.widget.TextView), D('tv_goc_Xia2' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r3v95 'tv_goc_Xi4'  android.widget.TextView: [D('tv_an_Xi4' android.widget.TextView), D('tv_goc_Xi4' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v51 'tv_ketqua_Xi3'  android.widget.TextView: [D('jsonDang' org.json.JSONObject), D('tv_ketqua_Xi4' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v57 'tv_ketqua_Xi2'  android.widget.TextView: [D('tv_goc_Xi3' android.widget.TextView), D('tv_ketqua_Xi3' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r3v116 'tv_goc_Xi2'  android.widget.TextView: [D('tv_goc_Xi2' android.widget.TextView), D('tv_an_Xi2' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v61 'tv_ketqua_Loa'  android.widget.TextView: [D('jsonDang' org.json.JSONObject), D('tv_ketqua_Xi2' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v67 'tv_ketqua_Lo'  android.widget.TextView: [D('tv_goc_Loa' android.widget.TextView), D('tv_ketqua_Loa' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r3v136 'tv_goc_Ded'  android.widget.TextView: [D('tv_an_Lo' android.widget.TextView), D('tv_goc_Lo' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v70 'tv_ketqua_Ded'  android.widget.TextView: [D('jsonDang' org.json.JSONObject), D('tv_ketqua_Lo' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r3v146 'tv_goc_Ded'  android.widget.TextView: [D('tv_an_Ded' android.widget.TextView), D('tv_goc_Ded' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v74 'tv_ketqua_Dec'  android.widget.TextView: [D('jsonDang' org.json.JSONObject), D('tv_ketqua_Ded' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v80 'tv_ketqua_Det'  android.widget.TextView: [D('tv_goc_Dec' android.widget.TextView), D('tv_ketqua_Dec' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r3v166 'tv_ketqua_Dea'  android.widget.TextView: [D('tv_an_Det' android.widget.TextView), D('tv_goc_Det' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v84 'tv_ketqua_De'  android.widget.TextView: [D('jsonDang' org.json.JSONObject), D('tv_ketqua_Det' android.widget.TextView)] */
        /* JADX INFO: Multiple debug info for r1v89 android.widget.TextView: [D('tv_ketqua_De' android.widget.TextView), D('tv_goc_De' android.widget.TextView)] */
        public View getView(int position, View mView, ViewGroup parent) {
            JSONException e;
            TextView tv_ketqua_Dea;
            TextView tv_goc_De;
            TextView tv_goc_Det;
            TextView tv_ketqua_Det;
            TextView tv_ketqua_Dec;
            TextView tv_goc_Ded;
            TextView tv_ketqua_Ded;
            TextView tv_goc_Lo;
            TextView tv_ketqua_Lo;
            TextView tv_ketqua_Loa;
            TextView tv_an_Xi2;
            TextView tv_ketqua_Xi2;
            TextView tv_ketqua_Xi3;
            TextView tv_goc_Xi4;
            TextView tv_ketqua_Xi4;
            TextView tv_goc_Xia2;
            TextView tv_goc_Xia3;
            TextView tv_ketqua_Xia3;
            TextView tv_ketqua_Xia4;
            TextView tv_an_Xn;
            TextView tv_ketqua_Xn;
            TextView tv_ketqua_Bc;
            JSONObject jsonDang;
            JSONObject jsonDang2;
            JSONObject jsonDang3;
            String str;
            TextView tv_gio_nhan;
            Spannable wordtoSpan = null;
            @SuppressLint("WrongConstant") View mView2 = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.activity_tinnhan_lv, (ViewGroup) null);
            TextView tv_tongtien = (TextView) mView2.findViewById(R.id.tv_no_Tong);
            TextView tv_ket_qua = (TextView) mView2.findViewById(R.id.tv_ThangThua);
            TextView tv_tenKH = (TextView) mView2.findViewById(R.id.tv_ten_KH);
            TextView tv_gio_nhan2 = (TextView) mView2.findViewById(R.id.tv_TG_nhan);
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
            TextView tv_ketqua_Dea2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDeA);
            TextView tv_diem_De = (TextView) mView2.findViewById(R.id.tv_diemDe);
            TextView tv_an_De = (TextView) mView2.findViewById(R.id.tv_AnDe);
            TextView tv_goc_De2 = (TextView) mView2.findViewById(R.id.tv_no_De);
            TextView tv_ketqua_De = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDe);
            TextView tv_diem_Det = (TextView) mView2.findViewById(R.id.tv_diemDeT);
            TextView tv_an_Det = (TextView) mView2.findViewById(R.id.tv_AnDeT);
            TextView tv_goc_Det2 = (TextView) mView2.findViewById(R.id.tv_no_DeT);
            TextView tv_ketqua_Det2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDeT);
            TextView tv_diem_Dec = (TextView) mView2.findViewById(R.id.tv_diemDeC);
            TextView tv_an_Dec = (TextView) mView2.findViewById(R.id.tv_AnDeC);
            TextView tv_goc_Dec = (TextView) mView2.findViewById(R.id.tv_no_DeC);
            TextView tv_ketqua_Dec2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDeC);
            TextView tv_diem_Ded = (TextView) mView2.findViewById(R.id.tv_diemDeD);
            TextView tv_an_Ded = (TextView) mView2.findViewById(R.id.tv_AnDeD);
            TextView tv_goc_Ded2 = (TextView) mView2.findViewById(R.id.tv_no_DeD);
            TextView tv_ketqua_Ded2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaDeD);
            TextView tv_diem_Lo = (TextView) mView2.findViewById(R.id.tv_diemlo);
            TextView tv_an_Lo = (TextView) mView2.findViewById(R.id.tv_anlo);
            TextView tv_goc_Lo2 = (TextView) mView2.findViewById(R.id.tv_no_Lo);
            TextView tv_ketqua_Lo2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaLo);
            TextView tv_diem_Loa = (TextView) mView2.findViewById(R.id.tv_diemloa);
            TextView tv_an_Loa = (TextView) mView2.findViewById(R.id.tv_anloa);
            TextView tv_goc_Loa = (TextView) mView2.findViewById(R.id.tv_no_Loa);
            TextView tv_ketqua_Loa2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaLoa);
            TextView tv_diem_Xi2 = (TextView) mView2.findViewById(R.id.tv_diemxi2);
            TextView tv_an_Xi22 = (TextView) mView2.findViewById(R.id.tv_anxi2);
            TextView tv_goc_Xi2 = (TextView) mView2.findViewById(R.id.tv_no_Xi2);
            TextView tv_ketqua_Xi22 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXi2);
            TextView tv_diem_Xi3 = (TextView) mView2.findViewById(R.id.tv_diemxi3);
            TextView tv_an_Xi3 = (TextView) mView2.findViewById(R.id.tv_anxi3);
            TextView tv_goc_Xi3 = (TextView) mView2.findViewById(R.id.tv_no_Xi3);
            TextView tv_ketqua_Xi32 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXi3);
            TextView tv_diem_Xi4 = (TextView) mView2.findViewById(R.id.tv_diemxi4);
            TextView tv_an_Xi4 = (TextView) mView2.findViewById(R.id.tv_anxi4);
            TextView tv_goc_Xi42 = (TextView) mView2.findViewById(R.id.tv_no_Xi4);
            TextView tv_ketqua_Xi42 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXi4);
            TextView tv_diem_Xia2 = (TextView) mView2.findViewById(R.id.tv_diemxia2);
            TextView tv_an_Xia2 = (TextView) mView2.findViewById(R.id.tv_anxia2);
            TextView tv_goc_Xia22 = (TextView) mView2.findViewById(R.id.tv_no_Xia2);
            TextView tv_ketqua_Xia2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXia2);
            TextView tv_diem_Xia3 = (TextView) mView2.findViewById(R.id.tv_diemxia3);
            TextView tv_an_Xia3 = (TextView) mView2.findViewById(R.id.tv_anxia3);
            TextView tv_goc_Xia32 = (TextView) mView2.findViewById(R.id.tv_no_Xia3);
            TextView tv_ketqua_Xia32 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXia3);
            TextView tv_diem_Xia4 = (TextView) mView2.findViewById(R.id.tv_diemxia4);
            TextView tv_an_Xia4 = (TextView) mView2.findViewById(R.id.tv_anxia4);
            TextView tv_goc_Xia4 = (TextView) mView2.findViewById(R.id.tv_no_Xia4);
            TextView tv_ketqua_Xia42 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXia4);
            TextView tv_diem_Xn = (TextView) mView2.findViewById(R.id.tv_diemxn);
            TextView tv_an_Xn2 = (TextView) mView2.findViewById(R.id.tv_anxn);
            TextView tv_goc_Xn = (TextView) mView2.findViewById(R.id.tv_no_Xn);
            TextView tv_ketqua_Xn2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaXn);
            TextView tv_diem_Bc = (TextView) mView2.findViewById(R.id.tv_diembc);
            TextView tv_an_Bc = (TextView) mView2.findViewById(R.id.tv_anbc);
            TextView tv_goc_Bc = (TextView) mView2.findViewById(R.id.tv_no_Bc);
            TextView tv_ketqua_Bc2 = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaBc);
            TextView tv_diem_Bca = (TextView) mView2.findViewById(R.id.tv_diembca);
            TextView tv_an_Bca = (TextView) mView2.findViewById(R.id.tv_anbca);
            TextView tv_goc_Bca = (TextView) mView2.findViewById(R.id.tv_no_Bca);
            TextView tv_ketqua_Bca = (TextView) mView2.findViewById(R.id.tv_no_ThangThuaBca);
            JSONObject Json = Frag_NoRP3.this.jsonValues.get(position);
            try {
                tv_Tingoc.setText(Json.getString("tin_goc"));
                tv_tenKH.setText(Json.getString("ten_KH"));
                try {
                    if (Json.getString("type_kh").indexOf("2") > -1) {
                        tv_tenKH.setTextColor(Color.parseColor("#1a40ea"));
                    }
                    tv_so_tn.setText(Json.getString("so_tinnhan"));
                    tv_gio_nhan2.setText(Json.getString("gio_nhan"));
                    tv_tongtien.setText(Json.getString("tong_tien"));
                    tv_ket_qua.setText(Json.getString("ket_qua"));
                    String str2 = Json.getString("nd_phantich");
                    Spannable wordtoSpan2 = new SpannableString(str2);
                    int i1 = 0;
                    while (i1 < str2.length() - 1) {
                        try {
                            try {
                                if (str2.substring(i1, i1 + 2).indexOf("*") > -1) {
                                    int i2 = i1;
                                    while (true) {
                                        if (i2 <= 0) {
                                            str = str2;
                                            tv_gio_nhan = tv_gio_nhan2;
                                            break;
                                        }
                                        tv_gio_nhan = tv_gio_nhan2;
                                        if (str2.substring(i2, i2 + 1).indexOf(",") > -1) {
                                            str = str2;
                                            break;
                                        } else if (str2.substring(i2, i2 + 1).indexOf(":") > -1) {
                                            str = str2;
                                            break;
                                        } else {
                                            wordtoSpan2.setSpan(new ForegroundColorSpan(SupportMenu.CATEGORY_MASK), i2, i1 + 1, 33);
                                            i2--;
                                            tv_gio_nhan2 = tv_gio_nhan;
                                            str2 = str2;
                                        }
                                    }
                                } else {
                                    str = str2;
                                    tv_gio_nhan = tv_gio_nhan2;
                                }
                                i1++;
                                tv_tongtien = tv_tongtien;
                                tv_tenKH = tv_tenKH;
                                tv_gio_nhan2 = tv_gio_nhan;
                                str2 = str;
                            } catch (Exception e5) {
                                wordtoSpan = wordtoSpan2;
                                e5.printStackTrace();
                                return mView2;
                            }
                        } catch (Exception e6) {
                            wordtoSpan = wordtoSpan2;
                            e6.printStackTrace();
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
                            tv_ketqua_Dea = tv_ketqua_Dea2;
                            try {
                                tv_ketqua_Dea.setText(jsonDang4.getString("ket_qua"));
                            } catch (JSONException e7) {
                                e = e7;
                                wordtoSpan = wordtoSpan2;
                            }
                        } catch (JSONException e8) {
                            e = e8;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_ketqua_Dea = tv_ketqua_Dea2;
                    }
                    if (Json.has("deb")) {
                        try {
                            JSONObject jsonDang5 = new JSONObject(Json.getString("deb"));
                            tv_diem_De.setText(jsonDang5.getString("diem"));
                            tv_an_De.setText(jsonDang5.getString("diem_an"));
                            tv_goc_De2.setText(jsonDang5.getString("tong_tien"));
                            tv_goc_De = tv_ketqua_De;
                            tv_goc_De.setText(jsonDang5.getString("ket_qua"));
                        } catch (JSONException e13) {
                            e = e13;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_goc_De = tv_ketqua_De;
                    }
                    if (Json.has("det")) {
                        try {
                            liner_deT.setVisibility(View.VISIBLE);
                            JSONObject jsonDang6 = new JSONObject(Json.getString("det"));
                            tv_diem_Det.setText(jsonDang6.getString("diem"));
                            tv_an_Det.setText(jsonDang6.getString("diem_an"));
                            tv_goc_Det = tv_goc_Det2;
                            tv_goc_Det.setText(jsonDang6.getString("tong_tien"));
                            String string = jsonDang6.getString("ket_qua");
                            tv_ketqua_Det = tv_ketqua_Det2;
                        } catch (JSONException e19) {
                            e = e19;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_goc_Det = tv_goc_Det2;
                        tv_ketqua_Det = tv_ketqua_Det2;
                    }
                    if (Json.has("dec")) {
                        try {
                            liner_deC.setVisibility(View.VISIBLE);
                            jsonDang3 = new JSONObject(Json.getString("dec"));
                            tv_diem_Dec.setText(jsonDang3.getString("diem"));
                            tv_an_Dec.setText(jsonDang3.getString("diem_an"));
                        } catch (JSONException e22) {
                            e = e22;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                        try {
                            tv_goc_Dec.setText(jsonDang3.getString("tong_tien"));
                            tv_ketqua_Dec = tv_ketqua_Dec2;
                            tv_ketqua_Dec.setText(jsonDang3.getString("ket_qua"));
                        } catch (JSONException e24) {
                            e = e24;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_ketqua_Dec = tv_ketqua_Dec2;
                    }
                    if (Json.has("ded")) {
                        try {
                            liner_deD.setVisibility(View.VISIBLE);
                            JSONObject jsonDang7 = new JSONObject(Json.getString("ded"));
                            try {
                                tv_diem_Ded.setText(jsonDang7.getString("diem"));
                                try {
                                    tv_an_Ded.setText(jsonDang7.getString("diem_an"));
                                    tv_goc_Ded = tv_goc_Ded2;
                                    try {
                                        tv_goc_Ded.setText(jsonDang7.getString("tong_tien"));
                                        String string2 = jsonDang7.getString("ket_qua");
                                        tv_ketqua_Ded = tv_ketqua_Ded2;
                                        tv_ketqua_Ded.setText(string2);
                                    } catch (JSONException e27) {
                                        e = e27;
                                        wordtoSpan = wordtoSpan2;
                                        e.printStackTrace();
                                        return mView2;
                                    }
                                } catch (JSONException e28) {
                                    e = e28;
                                    wordtoSpan = wordtoSpan2;
                                    e.printStackTrace();
                                    return mView2;
                                }
                            } catch (JSONException e29) {
                                e = e29;
                                wordtoSpan = wordtoSpan2;
                                e.printStackTrace();
                                return mView2;
                            }
                        } catch (JSONException e30) {
                            e = e30;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_goc_Ded = tv_goc_Ded2;
                        tv_ketqua_Ded = tv_ketqua_Ded2;
                    }
                    if (Json.has("lo")) {
                        try {
                            JSONObject jsonDang8 = new JSONObject(Json.getString("lo"));
                            try {
                                tv_diem_Lo.setText(jsonDang8.getString("diem"));
                                try {
                                    tv_an_Lo.setText(jsonDang8.getString("diem_an"));
                                    tv_goc_Lo = tv_goc_Lo2;
                                    try {
                                        tv_goc_Lo.setText(jsonDang8.getString("tong_tien"));
                                        String string3 = jsonDang8.getString("ket_qua");
                                        tv_ketqua_Lo = tv_ketqua_Lo2;
                                        tv_ketqua_Lo.setText(string3);
                                    } catch (JSONException e35) {
                                        e = e35;
                                        wordtoSpan = wordtoSpan2;
                                        e.printStackTrace();
                                        return mView2;
                                    }
                                } catch (JSONException e36) {
                                    e = e36;
                                    wordtoSpan = wordtoSpan2;
                                    e.printStackTrace();
                                    return mView2;
                                }
                            } catch (JSONException e37) {
                                e = e37;
                                wordtoSpan = wordtoSpan2;
                                e.printStackTrace();
                                return mView2;
                            }
                        } catch (JSONException e38) {
                            e = e38;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_goc_Lo = tv_goc_Lo2;
                        tv_ketqua_Lo = tv_ketqua_Lo2;
                    }
                    if (Json.has("loa")) {
                        try {
                            liner_Loa.setVisibility(View.VISIBLE);
                            JSONObject jsonDang9 = new JSONObject(Json.getString("loa"));
                            try {
                                tv_diem_Loa.setText(jsonDang9.getString("diem"));
                                try {
                                    tv_an_Loa.setText(jsonDang9.getString("diem_an"));
                                    try {
                                        tv_goc_Loa.setText(jsonDang9.getString("tong_tien"));
                                        tv_ketqua_Loa = tv_ketqua_Loa2;
                                        try {
                                            tv_ketqua_Loa.setText(jsonDang9.getString("ket_qua"));
                                        } catch (JSONException e39) {
                                            e = e39;
                                            wordtoSpan = wordtoSpan2;
                                        }
                                    } catch (JSONException e40) {
                                        e = e40;
                                        wordtoSpan = wordtoSpan2;
                                        e.printStackTrace();
                                        return mView2;
                                    }
                                } catch (JSONException e41) {
                                    e = e41;
                                    wordtoSpan = wordtoSpan2;
                                    e.printStackTrace();
                                    return mView2;
                                }
                            } catch (JSONException e42) {
                                e = e42;
                                wordtoSpan = wordtoSpan2;
                                e.printStackTrace();
                                return mView2;
                            }
                        } catch (JSONException e43) {
                            e = e43;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_ketqua_Loa = tv_ketqua_Loa2;
                    }
//                        try {
                    if (Json.has("xi2")) {
                        try {
                            liner_Xi2.setVisibility(View.VISIBLE);
                            JSONObject jsonDang10 = new JSONObject(Json.getString("xi2"));
                            try {
                                tv_diem_Xi2.setText(jsonDang10.getString("diem"));
                                try {
                                    tv_an_Xi22.setText(jsonDang10.getString("diem_an"));
                                    tv_an_Xi2 = tv_goc_Xi2;
                                    try {
                                        tv_an_Xi2.setText(jsonDang10.getString("tong_tien"));
                                        String string4 = jsonDang10.getString("ket_qua");
                                        tv_ketqua_Xi2 = tv_ketqua_Xi22;
                                        tv_ketqua_Xi2.setText(string4);
                                    } catch (JSONException e46) {
                                        e = e46;
                                        wordtoSpan = wordtoSpan2;
                                        e.printStackTrace();
                                        return mView2;
                                    }
                                } catch (JSONException e47) {
                                    e = e47;
                                    wordtoSpan = wordtoSpan2;
                                    e.printStackTrace();
                                    return mView2;
                                }
                            } catch (JSONException e48) {
                                e = e48;
                                wordtoSpan = wordtoSpan2;
                                e.printStackTrace();
                                return mView2;
                            }
                        } catch (JSONException e49) {
                            e = e49;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_an_Xi2 = tv_goc_Xi2;
                        tv_ketqua_Xi2 = tv_ketqua_Xi22;
                    }
                    if (Json.has("xi3")) {
                        try {
                            liner_Xi3.setVisibility(View.VISIBLE);
                            JSONObject jsonDang11 = new JSONObject(Json.getString("xi3"));
                            tv_diem_Xi3.setText(jsonDang11.getString("diem"));
                            tv_an_Xi3.setText(jsonDang11.getString("diem_an"));
                            tv_goc_Xi3.setText(jsonDang11.getString("tong_tien"));
                            tv_ketqua_Xi3 = tv_ketqua_Xi32;
                            tv_ketqua_Xi3.setText(jsonDang11.getString("ket_qua"));
                        } catch (JSONException e54) {
                            e = e54;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_ketqua_Xi3 = tv_ketqua_Xi32;
                    }
                    try {
                        if (Json.has("xi4")) {
                                liner_Xi4.setVisibility(View.VISIBLE);
                                JSONObject jsonDang12 = new JSONObject(Json.getString("xi4"));
                                tv_diem_Xi4.setText(jsonDang12.getString("diem"));
                                tv_an_Xi4.setText(jsonDang12.getString("diem_an"));
                                tv_goc_Xi4 = tv_goc_Xi42;
                                tv_goc_Xi4.setText(jsonDang12.getString("tong_tien"));
                                String string5 = jsonDang12.getString("ket_qua");
                                tv_ketqua_Xi4 = tv_ketqua_Xi42;
                                tv_ketqua_Xi4.setText(string5);
                        } else {
                            tv_goc_Xi4 = tv_goc_Xi42;
                            tv_ketqua_Xi4 = tv_ketqua_Xi42;
                        }
                    } catch (JSONException e61) {
                        e = e61;
                        wordtoSpan = wordtoSpan2;
                        e.printStackTrace();
                        return mView2;
                    }
//                        } catch (JSONException e62) {
//                            e = e62;
//                            wordtoSpan = wordtoSpan2;
//                            e.printStackTrace();
//                            return mView2;
//                        }
                    if (Json.has("xia2")) {
                        try {
                            liner_Xia2.setVisibility(View.VISIBLE);
                            JSONObject jsonDang13 = new JSONObject(Json.getString("xia2"));
                            try {
                                tv_diem_Xia2.setText(jsonDang13.getString("diem"));
                                try {
                                    tv_an_Xia2.setText(jsonDang13.getString("diem_an"));
                                    try {
                                        tv_goc_Xia22.setText(jsonDang13.getString("tong_tien"));
                                        tv_goc_Xia2 = tv_ketqua_Xia2;
                                        try {
                                            tv_goc_Xia2.setText(jsonDang13.getString("ket_qua"));
                                        } catch (JSONException e63) {
                                            e = e63;
                                            wordtoSpan = wordtoSpan2;
                                        }
                                    } catch (JSONException e64) {
                                        e = e64;
                                        wordtoSpan = wordtoSpan2;
                                        e.printStackTrace();
                                        return mView2;
                                    }
                                } catch (JSONException e65) {
                                    e = e65;
                                    wordtoSpan = wordtoSpan2;
                                    e.printStackTrace();
                                    return mView2;
                                }
                            } catch (JSONException e66) {
                                e = e66;
                                wordtoSpan = wordtoSpan2;
                                e.printStackTrace();
                                return mView2;
                            }
                        } catch (JSONException e67) {
                            e = e67;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_goc_Xia2 = tv_ketqua_Xia2;
                    }
                    if (Json.has("xia3")) {
                        try {
                            liner_Xia3.setVisibility(View.VISIBLE);
                            jsonDang2 = new JSONObject(Json.getString("xia3"));
                        } catch (JSONException e68) {
                            e = e68;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                        try {
                            tv_diem_Xia3.setText(jsonDang2.getString("diem"));
                            try {
                                tv_an_Xia3.setText(jsonDang2.getString("diem_an"));
                                tv_goc_Xia3 = tv_goc_Xia32;
                                try {
                                    tv_goc_Xia3.setText(jsonDang2.getString("tong_tien"));
                                    String string6 = jsonDang2.getString("ket_qua");
                                    tv_ketqua_Xia3 = tv_ketqua_Xia32;
                                    tv_ketqua_Xia3.setText(string6);
                                    tv_ketqua_Xia3.setText(string6);

                                } catch (JSONException e70) {
                                    e = e70;
                                    wordtoSpan = wordtoSpan2;
                                    e.printStackTrace();
                                    return mView2;
                                }
                            } catch (JSONException e71) {
                                e = e71;
                                wordtoSpan = wordtoSpan2;
                                e.printStackTrace();
                                return mView2;
                            }
                        } catch (JSONException e72) {
                            e = e72;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_goc_Xia3 = tv_goc_Xia32;
                        tv_ketqua_Xia3 = tv_ketqua_Xia32;
                    }
                    if (Json.has("xia4")) {
                        try {
                            liner_Xia4.setVisibility(View.VISIBLE);
                            JSONObject jsonDang14 = new JSONObject(Json.getString("xia4"));
                            try {
                                tv_diem_Xia4.setText(jsonDang14.getString("diem"));
                                try {
                                    tv_an_Xia4.setText(jsonDang14.getString("diem_an"));
                                    try {
                                        tv_goc_Xia4.setText(jsonDang14.getString("tong_tien"));
                                        tv_ketqua_Xia4 = tv_ketqua_Xia42;
                                        try {
                                            tv_ketqua_Xia4.setText(jsonDang14.getString("ket_qua"));
                                        } catch (JSONException e74) {
                                            e = e74;
                                            wordtoSpan = wordtoSpan2;
                                        }
                                    } catch (JSONException e75) {
                                        e = e75;
                                        wordtoSpan = wordtoSpan2;
                                        e.printStackTrace();
                                        return mView2;
                                    }
                                } catch (JSONException e76) {
                                    e = e76;
                                    wordtoSpan = wordtoSpan2;
                                    e.printStackTrace();
                                    return mView2;
                                }
                            } catch (JSONException e77) {
                                e = e77;
                                wordtoSpan = wordtoSpan2;
                                e.printStackTrace();
                                return mView2;
                            }
                        } catch (JSONException e78) {
                            e = e78;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_ketqua_Xia4 = tv_ketqua_Xia42;
                    }
                    if (Json.has("xn")) {
                        try {
                            liner_Xn.setVisibility(View.VISIBLE);
                            JSONObject jsonDang15 = new JSONObject(Json.getString("xn"));
                            try {
                                tv_diem_Xn.setText(jsonDang15.getString("diem"));
                                try {
                                    tv_an_Xn2.setText(jsonDang15.getString("diem_an"));
                                    tv_an_Xn = tv_goc_Xn;
                                    try {
                                        tv_an_Xn.setText(jsonDang15.getString("tong_tien"));
                                        String string7 = jsonDang15.getString("ket_qua");
                                        tv_ketqua_Xn = tv_ketqua_Xn2;
                                        tv_ketqua_Xn.setText(string7);
                                    } catch (JSONException e82) {
                                        e = e82;
                                        wordtoSpan = wordtoSpan2;
                                        e.printStackTrace();
                                        return mView2;
                                    }
                                } catch (JSONException e83) {
                                    e = e83;
                                    wordtoSpan = wordtoSpan2;
                                    e.printStackTrace();
                                    return mView2;
                                }
                            } catch (JSONException e84) {
                                e = e84;
                                wordtoSpan = wordtoSpan2;
                                e.printStackTrace();
                                return mView2;
                            }
                        } catch (JSONException e85) {
                            e = e85;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_an_Xn = tv_goc_Xn;
                        tv_ketqua_Xn = tv_ketqua_Xn2;
                    }
                    if (Json.has("bc")) {
                        try {
                            jsonDang = new JSONObject(Json.getString("bc"));
                            try {
                                tv_diem_Bc.setText(jsonDang.getString("diem"));
                                try {
                                    tv_an_Bc.setText(jsonDang.getString("diem_an"));
                                } catch (JSONException e86) {
                                    e = e86;
                                    wordtoSpan = wordtoSpan2;
                                    e.printStackTrace();
                                    return mView2;
                                }
                            } catch (JSONException e87) {
                                e = e87;
                                wordtoSpan = wordtoSpan2;
                                e.printStackTrace();
                                return mView2;
                            }
                        } catch (JSONException e88) {
                            e = e88;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                        try {
                            tv_goc_Bc.setText(jsonDang.getString("tong_tien"));
                            String string8 = jsonDang.getString("ket_qua");
                            tv_ketqua_Bc = tv_ketqua_Bc2;
                            tv_ketqua_Bc.setText(string8);
                        } catch (JSONException e90) {
                            e = e90;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    } else {
                        tv_ketqua_Bc = tv_ketqua_Bc2;
                    }
                    if (Json.has("bca")) {
                        try {
                            liner_Bca.setVisibility(View.VISIBLE);
                            JSONObject jsonDang16 = new JSONObject(Json.getString("bca"));
                            tv_diem_Bca.setText(jsonDang16.getString("diem"));
                            tv_an_Bca.setText(jsonDang16.getString("diem_an"));
                            tv_goc_Bca.setText(jsonDang16.getString("tong_tien"));
                            tv_ketqua_Bca.setText(jsonDang16.getString("ket_qua"));
                        } catch (JSONException e97) {
                            e = e97;
                            wordtoSpan = wordtoSpan2;
                            e.printStackTrace();
                            return mView2;
                        }
                    }
                } catch (JSONException e101) {
                    e = e101;
                    e.printStackTrace();
                    return mView2;
                }
            } catch (JSONException e102) {
                e = e102;
                e.printStackTrace();
                return mView2;
            }
            return mView2;
        }
    }
}