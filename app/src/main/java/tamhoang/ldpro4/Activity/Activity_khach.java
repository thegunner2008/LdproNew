package tamhoang.ldpro4.Activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_khach extends BaseToolBarActivity {
    String DangXuat;

    /* renamed from: db */
    Database f184db;
    ListView lv_khach;
    /* access modifiers changed from: private */
    public List<String> mDiem = new ArrayList();
    /* access modifiers changed from: private */
    public List<String> mDiemGiu = new ArrayList();
    /* access modifiers changed from: private */
    public List<Integer> mNhay = new ArrayList();
    /* access modifiers changed from: private */
    public List<String> mSo = new ArrayList();
    /* access modifiers changed from: private */
    public List<String> mThanhTien = new ArrayList();
    String message;
    RadioButton radio_bc;
    RadioButton radio_de;
    RadioButton radio_lo;
    RadioButton radio_xi;
    TextView textView;

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.activity_khach;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_khach);
        this.f184db = new Database(this);
        init();
        this.message = getIntent().getStringExtra("tenKH");
        ((TextView) findViewById(R.id.textView)).setText("Khách hàng: " + this.message);
        this.radio_de.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Activity_khach.this.radio_de.isChecked()) {
                    Activity_khach activity_khach = Activity_khach.this;
                    activity_khach.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                    activity_khach.lv_Khach();
                }
            }
        });
        this.radio_lo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Activity_khach.this.radio_lo.isChecked()) {
                    Activity_khach activity_khach = Activity_khach.this;
                    activity_khach.DangXuat = "the_loai = 'lo'";
                    activity_khach.lv_Khach();
                }
            }
        });
        this.radio_xi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Activity_khach.this.radio_xi.isChecked()) {
                    Activity_khach activity_khach = Activity_khach.this;
                    activity_khach.DangXuat = "the_loai = 'xi'";
                    activity_khach.lv_Khach();
                }
            }
        });
        this.radio_bc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Activity_khach.this.radio_bc.isChecked()) {
                    Activity_khach activity_khach = Activity_khach.this;
                    activity_khach.DangXuat = "the_loai = 'bc'";
                    activity_khach.lv_Khach();
                }
            }
        });
        this.radio_de.setChecked(true);
    }

    public void lv_Khach() {
        new MainActivity();
        String Get_date = MainActivity.Get_date();
        Cursor GetData = this.f184db.GetData("Select so_chon, sum(diem_quydoi) as diem, sum(diem_khachgiu*diem_quydoi)/100 as diemgiu, sum((100-diem_khachgiu)*diem_quydoi/100) as diemton, so_nhay From tbl_soctS where ngay_nhan = '" + Get_date + "' AND ten_kh = '" + this.message + "' AND " + this.DangXuat + " GRoup by so_chon order by diem DESC;");
        this.mSo.clear();
        this.mDiem.clear();
        this.mDiemGiu.clear();
        this.mThanhTien.clear();
        this.mNhay.clear();
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        while (GetData.moveToNext()) {
            this.mSo.add(GetData.getString(0));
            this.mDiem.add(decimalFormat.format(GetData.getDouble(1)));
            this.mDiemGiu.add(decimalFormat.format(GetData.getDouble(2)));
            this.mThanhTien.add(decimalFormat.format(GetData.getDouble(3)));
            this.mNhay.add(Integer.valueOf(GetData.getInt(4)));
        }
        this.lv_khach.setAdapter(new Khach_Adapter(this, R.layout.activity_khach_lv, this.mSo));
    }

    class Khach_Adapter extends ArrayAdapter {
        private ViewHolder holder;
        private LayoutInflater mInflater;

        public Khach_Adapter(Context context, int i, List<String> list) {
            super(context, i, list);
            this.mInflater = LayoutInflater.from(context);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(R.layout.activity_khach_lv, (ViewGroup) null);
                this.holder = new ViewHolder();
                TextView unused = this.holder.stt = (TextView) view.findViewById(R.id.stt);
                TextView unused2 = this.holder.sochon = (TextView) view.findViewById(R.id.Tv_so);
                TextView unused3 = this.holder.tv_diemnhan = (TextView) view.findViewById(R.id.tv_diemNhan);
                TextView unused4 = this.holder.tv_diemgiu = (TextView) view.findViewById(R.id.tv_diemGiu);
                TextView unused5 = this.holder.tv_thanhtien = (TextView) view.findViewById(R.id.tv_thanhtien);
                view.setTag(this.holder);
            } else {
                this.holder = (ViewHolder) view.getTag();
            }
            if (((Integer) Activity_khach.this.mNhay.get(i)).intValue() > 0) {
                this.holder.sochon.setTextColor(SupportMenu.CATEGORY_MASK);
                this.holder.tv_diemnhan.setTextColor(SupportMenu.CATEGORY_MASK);
                this.holder.tv_diemgiu.setTextColor(SupportMenu.CATEGORY_MASK);
                this.holder.tv_thanhtien.setTextColor(SupportMenu.CATEGORY_MASK);
                if (((Integer) Activity_khach.this.mNhay.get(i)).intValue() == 1) {
                    TextView access$100 = this.holder.sochon;
                    access$100.setText(((String) Activity_khach.this.mSo.get(i)) + "*");
                } else if (((Integer) Activity_khach.this.mNhay.get(i)).intValue() == 2) {
                    TextView access$1002 = this.holder.sochon;
                    access$1002.setText(((String) Activity_khach.this.mSo.get(i)) + "**");
                } else if (((Integer) Activity_khach.this.mNhay.get(i)).intValue() == 3) {
                    TextView access$1003 = this.holder.sochon;
                    access$1003.setText(((String) Activity_khach.this.mSo.get(i)) + "***");
                } else if (((Integer) Activity_khach.this.mNhay.get(i)).intValue() == 4) {
                    TextView access$1004 = this.holder.sochon;
                    access$1004.setText(((String) Activity_khach.this.mSo.get(i)) + "****");
                }
                TextView access$000 = this.holder.stt;
                access$000.setText((i + 1) + "");
                this.holder.tv_diemnhan.setText((CharSequence) Activity_khach.this.mDiem.get(i));
                this.holder.tv_diemgiu.setText((CharSequence) Activity_khach.this.mDiemGiu.get(i));
                this.holder.tv_thanhtien.setText((CharSequence) Activity_khach.this.mThanhTien.get(i));
            } else {
                this.holder.sochon.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.holder.tv_diemnhan.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.holder.tv_diemgiu.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                this.holder.tv_thanhtien.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                TextView access$0002 = this.holder.stt;
                access$0002.setText((i + 1) + "");
                this.holder.sochon.setText((CharSequence) Activity_khach.this.mSo.get(i));
                this.holder.tv_diemnhan.setText((CharSequence) Activity_khach.this.mDiem.get(i));
                this.holder.tv_diemgiu.setText((CharSequence) Activity_khach.this.mDiemGiu.get(i));
                this.holder.tv_thanhtien.setText((CharSequence) Activity_khach.this.mThanhTien.get(i));
            }
            return view;
        }

        public class ViewHolder {
            /* access modifiers changed from: private */
            public TextView sochon;
            /* access modifiers changed from: private */
            public TextView stt;
            /* access modifiers changed from: private */
            public TextView tv_diemgiu;
            /* access modifiers changed from: private */
            public TextView tv_diemnhan;
            /* access modifiers changed from: private */
            public TextView tv_thanhtien;

            public ViewHolder() {
            }
        }
    }

    public void init() {
        this.radio_de = (RadioButton) findViewById(R.id.radio_de);
        this.radio_lo = (RadioButton) findViewById(R.id.radio_lo);
        this.radio_xi = (RadioButton) findViewById(R.id.radio_xi);
        this.radio_bc = (RadioButton) findViewById(R.id.radio_bc);
        this.lv_khach = (ListView) findViewById(R.id.lv_khach);
        this.textView = (TextView) findViewById(R.id.textView);
    }
}
