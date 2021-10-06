package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tamhoang.ldpro4.Activity.Activity_Congno;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_MoRP1 extends Fragment {
    TextView TienNoCu;
    TextView TienPhatSinh;
    TextView TienSoCuoi;
    Database db;
    String pattern = "###,###";
    DecimalFormat decimalFormat = new DecimalFormat(this.pattern);
    Handler handler;
    ListView lv_Morp;
    public ArrayList mKhachHang = new ArrayList();
    public ArrayList mNocu = new ArrayList();
    public ArrayList mPhatSinh = new ArrayList();
    int mPoistion = 0;
    public List<String> mSdt = new ArrayList();
    public List<String> mSoCuoi = new ArrayList();
    public List<String> mtype = new ArrayList();
    String ngayChon;
    private Runnable runnable = new Runnable() {
        /* class tamhoang.ldpro4.Fragment.Frag_MoRP1.AnonymousClass4 */

        public void run() {
            new MainActivity();
            if (MainActivity.sms) {
                Frag_MoRP1.this.money_lv();
                MainActivity.sms = false;
            }
            Frag_MoRP1.this.handler.postDelayed(this, 1000);
        }
    };
    View v;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.frag_morp1, container, false);
        this.db = new Database(getActivity());
        this.lv_Morp = (ListView) this.v.findViewById(R.id.lv_mo_rp1);
        this.TienNoCu = (TextView) this.v.findViewById(R.id.TienNoCu);
        this.TienPhatSinh = (TextView) this.v.findViewById(R.id.TienPhatSinh);
        this.TienSoCuoi = (TextView) this.v.findViewById(R.id.TienSoCuoi);
        money_lv();
        new MainActivity();
        this.ngayChon = MainActivity.Get_date();
        this.lv_Morp.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_MoRP1.AnonymousClass1 */

            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Frag_MoRP1.this.mPoistion = position;
                return false;
            }
        });
        this.lv_Morp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_MoRP1.AnonymousClass2 */

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Frag_MoRP1.this.mPoistion = i;
                Frag_MoRP1.this.itemClick(view);
            }
        });
        Handler handler2 = new Handler();
        this.handler = handler2;
        handler2.postDelayed(this.runnable, 1000);
        return this.v;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void itemClick(View v2) {
        String[] menus = {"Xem phát sinh chi tiết", "Xóa khách này"};
        PopupMenu popupL = new PopupMenu(getActivity(), v2);
        for (int i = 0; i < menus.length; i++) {
            popupL.getMenu().add(1, i, i, menus[i]);
        }
        popupL.show();
        popupL.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_MoRP1.AnonymousClass3 */

            public boolean onMenuItemClick(MenuItem menuItem) {
                int order = menuItem.getOrder();
                if (order == 0) {
                    Intent intent = new Intent(Frag_MoRP1.this.getActivity(), Activity_Congno.class);
                    intent.putExtra("tenKH", (String) Frag_MoRP1.this.mKhachHang.get(Frag_MoRP1.this.mPoistion));
                    Frag_MoRP1.this.startActivity(intent);
                    return false;
                } else if (order != 1) {
                    return false;
                } else {
                    AlertDialog.Builder bui = new AlertDialog.Builder(Frag_MoRP1.this.getActivity());
                    bui.setTitle("Xóa hết số liệu khách này?");
                    bui.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        /* class tamhoang.ldpro4.Fragment.Frag_MoRP1.AnonymousClass3.AnonymousClass1 */

                        public void onClick(DialogInterface dialog, int which) {
                            Database database = Frag_MoRP1.this.db;
                            database.QueryData("Delete FROM tbl_tinnhanS WHERE ten_kh = '" + Frag_MoRP1.this.mKhachHang.get(Frag_MoRP1.this.mPoistion) + "'");
                            Database database2 = Frag_MoRP1.this.db;
                            database2.QueryData("Delete FROM tbl_soctS WHERE ten_kh = '" + Frag_MoRP1.this.mKhachHang.get(Frag_MoRP1.this.mPoistion) + "'");
                            Frag_MoRP1.this.money_lv();
                            Toast.makeText(Frag_MoRP1.this.getActivity(), "Xoá thành công", Toast.LENGTH_LONG).show();
                        }
                    });
                    bui.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        /* class tamhoang.ldpro4.Fragment.Frag_MoRP1.AnonymousClass3.AnonymousClass2 */

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    bui.create().show();
                    return false;
                }
            }
        });
    }

    @Override // android.support.v4.app.Fragment
    public void onStop() {
        super.onStop();
        this.handler.removeCallbacks(this.runnable);
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        try {
            this.mKhachHang.clear();
            this.mSdt.clear();
            this.mNocu.clear();
            this.mPhatSinh.clear();
            this.mSoCuoi.clear();
            this.mtype.clear();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        money_lv();
    }

    public void money_lv() {
        double mTienNo = 0.0d;
        double mTienPS = 0.0d;
        double mTienCuoi = 0.0d;
        this.mKhachHang.clear();
        this.mSdt.clear();
        this.mNocu.clear();
        this.mPhatSinh.clear();
        this.mSoCuoi.clear();
        this.mtype.clear();
        MainActivity mainActivity = new MainActivity();
        String mDate = MainActivity.Get_date();
        Cursor cursor = this.db.GetData("Select tbl_soctS.ten_kh\n, SUM((tbl_soctS.ngay_nhan < '" + mDate + "') * tbl_soctS.ket_qua * (100-tbl_soctS.diem_khachgiu)/100)/1000  as NoCu   \n, SUM((tbl_soctS.ngay_nhan = '" + mDate + "') * tbl_soctS.ket_qua * (100-tbl_soctS.diem_khachgiu)/100)/1000  as PhatSinh   \n, SUM((tbl_soctS.ngay_nhan <= '" + mDate + "')*tbl_soctS.ket_qua*(100-tbl_soctS.diem_khachgiu)/100)/1000 as SoCuoi, tbl_soctS.so_dienthoai, tbl_kh_new.type_kh  \nFROM tbl_soctS INNER JOIN tbl_kh_new ON tbl_soctS.so_dienthoai = tbl_kh_new.sdt\nGROUP BY tbl_soctS.ten_kh ORDER BY tbl_soctS.type_kh DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                this.mKhachHang.add(cursor.getString(0));
                this.mSdt.add(cursor.getString(4));
                this.mNocu.add(this.decimalFormat.format(cursor.getDouble(1)));
                this.mPhatSinh.add(this.decimalFormat.format(cursor.getDouble(2)));
                this.mSoCuoi.add(this.decimalFormat.format(cursor.getDouble(3)));
                this.mtype.add(cursor.getString(5));
                mTienNo += cursor.getDouble(1);
                mTienPS += cursor.getDouble(2);
                mTienCuoi += cursor.getDouble(3);
                mDate = mDate;
                mainActivity = mainActivity;
            }
            this.TienNoCu.setText(this.decimalFormat.format(-mTienNo));
            this.TienPhatSinh.setText(this.decimalFormat.format(-mTienPS));
            this.TienSoCuoi.setText(this.decimalFormat.format(-mTienCuoi));
        }
        if (getActivity() != null) {
            this.lv_Morp.setAdapter((ListAdapter) new MoneyReport(getActivity(), R.layout.frag_morp1_lv, this.mKhachHang));
        }
    }

    /* access modifiers changed from: package-private */
    public class MoneyReport extends ArrayAdapter {
        public MoneyReport(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @SuppressLint("ResourceAsColor")
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.frag_morp1_lv, (ViewGroup) null);
            TextView tview1 = (TextView) v.findViewById(R.id.tv_KhachHang);
            tview1.setText((String) Frag_MoRP1.this.mKhachHang.get(position));
            TextView tview3 = (TextView) v.findViewById(R.id.tv_nocu);
            tview3.setText((String) Frag_MoRP1.this.mNocu.get(position));
            TextView tview4 = (TextView) v.findViewById(R.id.tv_phatsinh);
            tview4.setText((String) Frag_MoRP1.this.mPhatSinh.get(position));
            TextView tview5 = (TextView) v.findViewById(R.id.tv_tienton);
            tview5.setText(Frag_MoRP1.this.mSoCuoi.get(position));
            if (!Frag_MoRP1.this.mtype.get(position).contains("1")) {
                tview1.setTextColor(R.color.mtrl_scrim_color);
                tview3.setTextColor(R.color.mtrl_scrim_color);
                tview4.setTextColor(R.color.mtrl_scrim_color);
                tview5.setTextColor(R.color.mtrl_scrim_color);
            }
            return v;
        }
    }
}