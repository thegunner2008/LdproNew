package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Livestream extends Fragment {
    MainActivity activity;

    /* renamed from: db */
    Database f213db;
    Handler handler;
    LinearLayout ln1;
    ListView lvLivestrem;
    String mDate;
    public List<Integer> mDemso = new ArrayList();
    public List<String> mDiem = new ArrayList();
    public List<Double> mThangthua = new ArrayList();
    public List<Double> mTongTien = new ArrayList();
    RadioButton radio_dea;
    RadioButton radio_deb;
    RadioButton radio_dec;
    RadioButton radio_ded;
    private Runnable runnable = new Runnable() {
        public void run() {
            new MainActivity();
            if (MainActivity.sms) {
                Livestream.this.xem_lv();
                MainActivity.sms = false;
            }
            Livestream.this.handler.postDelayed(this, 1000);
        }
    };

    /* renamed from: so */
    int f214so;
    Switch switch1;
    String th_loai = "the_loai = 'deb'";
    TextView tvChuy;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreate(bundle);
        View inflate = layoutInflater.inflate(R.layout.activity_livestream, viewGroup, false);
        this.f213db = new Database(getActivity());
        this.lvLivestrem = (ListView) inflate.findViewById(R.id.lv_livestrem);
        this.tvChuy = (TextView) inflate.findViewById(R.id.tv_chu_y);
        this.switch1 = (Switch) inflate.findViewById(R.id.switch1);
        this.ln1 = (LinearLayout) inflate.findViewById(R.id.ln1);
        this.radio_dea = (RadioButton) inflate.findViewById(R.id.radio_Dea);
        this.radio_deb = inflate.findViewById(R.id.radio_Deb);
        this.radio_dec = (RadioButton) inflate.findViewById(R.id.radio_Dec);
        this.radio_ded = (RadioButton) inflate.findViewById(R.id.radio_Ded);
        this.activity = new MainActivity();
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        this.mDate = simpleDateFormat.format(instance.getTime());
        this.lvLivestrem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            }
        });
        this.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Livestream.this.switch1.isChecked()) {
                    Livestream.this.ln1.setVisibility(0);
                } else {
                    Livestream.this.ln1.setVisibility(8);
                }
            }
        });
        this.handler = new Handler();
        this.handler.postDelayed(this.runnable, 1000);
        this.radio_dea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Livestream.this.radio_dea.isChecked()) {
                    Livestream livestream = Livestream.this;
                    livestream.th_loai = "the_loai = 'dea'";
                    livestream.xem_lv();
                }
            }
        });
        this.radio_deb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Livestream.this.radio_deb.isChecked()) {
                    Livestream livestream = Livestream.this;
                    livestream.th_loai = "the_loai = 'deb'";
                    livestream.xem_lv();
                }
            }
        });
        this.radio_dec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Livestream.this.radio_dec.isChecked()) {
                    Livestream livestream = Livestream.this;
                    livestream.th_loai = "the_loai = 'dec'";
                    livestream.xem_lv();
                }
            }
        });
        this.radio_ded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (Livestream.this.radio_ded.isChecked()) {
                    Livestream livestream = Livestream.this;
                    livestream.th_loai = "the_loai = 'ded'";
                    livestream.xem_lv();
                }
            }
        });
        xem_lv();
        return inflate;
    }

    public void onStop() {
        super.onStop();
        this.handler.removeCallbacks(this.runnable);
    }

    public void onDestroy() {
        this.mDiem.clear();
        this.mDemso.clear();
        this.mTongTien.clear();
        this.mThangthua.clear();
        this.lvLivestrem.setAdapter((ListAdapter) null);
        super.onDestroy();
    }

    public void xem_lv() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        new MainActivity();
        String Get_date = MainActivity.Get_date();
        Cursor GetData = this.f213db.GetData("SELECT count(mycount) AS dem FROM (SELECT sum((type_kh = 1) *diem_ton) - sum((type_kh = 2) * diem_ton) AS mycount FROM tbl_soctS WHERE " + this.th_loai + " AND ngay_nhan = '" + Get_date + "' GROUP BY so_chon ) a");
        GetData.moveToFirst();
        this.f214so = GetData.getInt(0);
        if (this.f214so > 0) {
            TextView textView = this.tvChuy;
            textView.setText("Có " + (100 - GetData.getInt(0)) + " số 0 đồng");
        } else {
            this.tvChuy.setText("Chưa có dữ liệu ngày hôm nay.");
        }
        this.mDiem.clear();
        this.mDemso.clear();
        this.mTongTien.clear();
        this.mThangthua.clear();
        Cursor GetData2 = this.f213db.GetData("SELECT moctien, count(moctien) AS dem\nFROM (Select (Sum((tbl_soctS.type_kh =1) * tbl_soctS.diem_ton) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_ton) - so_om.Om_DeB ) as moctien\nFrom so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \nWhere tbl_soctS.ngay_nhan='" + Get_date + "' AND (tbl_soctS." + this.th_loai + " OR tbl_soctS.the_loai='det') \nGROUP by so_om.So order by moctien DESC) as a \nGROUP BY moctien ORDER BY moctien DESC");
        while (GetData2.moveToNext()) {
            this.mDiem.add(decimalFormat.format(GetData2.getDouble(0)));
            this.mDemso.add(Integer.valueOf(GetData2.getInt(1)));
        }
        if (this.mDiem.size() > 0) {
            for (int i = 0; i < this.mDiem.size(); i++) {
                int parseInt = Integer.parseInt(this.mDiem.get(i).replaceAll("\\.", ""));
                double d = (double) (parseInt * 100);
                if (i < this.mDiem.size()) {
                    for (int i2 = i + 1; i2 < this.mDiem.size(); i2++) {
                        double parseInt2 = (double) ((parseInt - Integer.parseInt(this.mDiem.get(i2).replaceAll("\\.", ""))) * this.mDemso.get(i2).intValue());
                        Double.isNaN(parseInt2);
                        d -= parseInt2;
                    }
                }
                List<Double> list = this.mTongTien;
                double d2 = (double) ((100 - this.f214so) * parseInt);
                Double.isNaN(d2);
                list.add(Double.valueOf(((d - d2) * 715.0d) / 1000.0d));
                List<Double> list2 = this.mThangthua;
                double d3 = (double) ((100 - this.f214so) * parseInt);
                Double.isNaN(d3);
                double d4 = (double) (parseInt * 70);
                Double.isNaN(d4);
                list2.add(Double.valueOf((((d - d3) * 715.0d) / 1000.0d) - d4));
            }
        }
        if (getActivity() != null) {
            this.lvLivestrem.setAdapter(new TNGAdapter(getActivity(), R.layout.activity_livestream_lv, this.mDiem));
        }
        if (GetData2 != null) {
            GetData2.close();
        }
        if (GetData != null) {
            GetData.close();
        }
    }

    class TNGAdapter extends ArrayAdapter {
        String pattern = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        public TNGAdapter(Context context, int i, List<String> list) {
            super(context, i, list);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_livestream_lv, (ViewGroup) null);
            ((TextView) inflate.findViewById(R.id.tv_diem)).setText(Livestream.this.mDiem.get(i));
            ((TextView) inflate.findViewById(R.id.tv_so)).setText(Livestream.this.mDemso.get(i) + "");
            ((TextView) inflate.findViewById(R.id.tv_tiengiu)).setText(this.decimalFormat.format(Livestream.this.mTongTien.get(i)));
            ((TextView) inflate.findViewById(R.id.tv_ThangThua)).setText(this.decimalFormat.format(Livestream.this.mThangthua.get(i)));
            return inflate;
        }
    }
}
