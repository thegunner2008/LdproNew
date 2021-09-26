package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationReader;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.RangeSeekBar;
import tamhoang.ldpro4.data.Database;

public class Frag_CanChuyen extends Fragment {
    boolean Dachuyen = false;
    String DangXuat = null;
    Button btn_Xuatso;
    CheckBox check_x2;
    CheckBox check_x3;
    CheckBox check_x4;
    CheckBox check_xn;
    Database db;
    EditText edt_tien;
    Handler handler;
    JSONObject jsonKhongmax;
    String lay_x2;
    String lay_x3;
    String lay_x4;
    LinearLayout layout;
    LinearLayout li_loaide;
    LinearLayout ln_xi;
    public List<String> mAppuse = new ArrayList();
    public List<String> mContact = new ArrayList();
    public List<String> mKhongMax = new ArrayList();
    public List<String> mMobile = new ArrayList();
    public List<Integer> mNhay = new ArrayList();
    public List<String> mSo = new ArrayList();
    int mSpiner = 0;
    public List<String> mTienNhan = new ArrayList();
    public List<String> mTienOm = new ArrayList();
    public List<String> mTienTon = new ArrayList();
    public List<String> mTienchuyen = new ArrayList();
    int max = 100;
    int min = 0;
    ListView no_rp_number;
    RadioButton radio_bc;
    RadioButton radio_de;
    RadioButton radio_dea;
    RadioButton radio_deb;
    RadioButton radio_dec;
    RadioButton radio_ded;
    RadioButton radio_lo;
    RadioButton radio_xi;
    RangeSeekBar<Integer> rangeSeekBar;
    private Runnable runnable = new Runnable() {
        /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass16 */

        public void run() {
            new MainActivity();
            if (MainActivity.sms) {
                Frag_CanChuyen.this.xemlv();
                MainActivity.sms = false;
            }
            Frag_CanChuyen.this.handler.postDelayed(this, 1000);
        }
    };
    String sapxep;
    public View v;
    String xuatDan;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.frag_canchuyen, container, false);
        this.v = inflate;
        this.radio_de = (RadioButton) inflate.findViewById(R.id.radio_de);
        this.radio_lo = (RadioButton) this.v.findViewById(R.id.radio_lo);
        this.radio_xi = (RadioButton) this.v.findViewById(R.id.radio_xi);
        this.radio_bc = (RadioButton) this.v.findViewById(R.id.radio_bc);
        this.radio_dea = (RadioButton) this.v.findViewById(R.id.radio_dea);
        this.radio_deb = (RadioButton) this.v.findViewById(R.id.radio_deb);
        this.radio_dec = (RadioButton) this.v.findViewById(R.id.radio_dec);
        this.radio_ded = (RadioButton) this.v.findViewById(R.id.radio_ded);
        this.btn_Xuatso = (Button) this.v.findViewById(R.id.btn_Xuatso);
        LinearLayout linearLayout = (LinearLayout) this.v.findViewById(R.id.ln_xi);
        this.ln_xi = linearLayout;
        linearLayout.setVisibility(View.GONE);
        LinearLayout linearLayout2 = (LinearLayout) this.v.findViewById(R.id.li_loaide);
        this.li_loaide = linearLayout2;
        linearLayout2.setVisibility(View.GONE);
        this.edt_tien = (EditText) this.v.findViewById(R.id.edt_tien);
        this.check_x2 = (CheckBox) this.v.findViewById(R.id.check_x2);
        this.check_x3 = (CheckBox) this.v.findViewById(R.id.check_x3);
        this.check_x4 = (CheckBox) this.v.findViewById(R.id.check_x4);
        this.check_xn = (CheckBox) this.v.findViewById(R.id.check_xn);
        this.no_rp_number = (ListView) this.v.findViewById(R.id.lview);
        this.db = new Database(getActivity());
        Handler handler2 = new Handler();
        this.handler = handler2;
        handler2.postDelayed(this.runnable, 1000);
        RangeSeekBar<Integer> rangeSeekBar2 = new RangeSeekBar<>(getActivity());
        this.rangeSeekBar = rangeSeekBar2;
        rangeSeekBar2.setRangeValues(0, 100);
        this.rangeSeekBar.setSelectedMinValue(0);
        this.rangeSeekBar.setSelectedMaxValue(100);
        LinearLayout linearLayout3 = (LinearLayout) this.v.findViewById(R.id.seekbar);
        this.layout = linearLayout3;
        linearLayout3.addView(this.rangeSeekBar);
        this.rangeSeekBar.setOnRangeSeekBarChangeListener((rangeSeekBar, minValue, maxValue) -> {
            Frag_CanChuyen.this.min = minValue.intValue();
            Frag_CanChuyen.this.max = maxValue.intValue();
        });
        this.radio_de.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass2 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Frag_CanChuyen.this.radio_de.isChecked()) {
                    Frag_CanChuyen.this.layout.setVisibility(View.VISIBLE);
                    new MainActivity();
                    String mDate = MainActivity.Get_date();
                    try {
                        Database database = Frag_CanChuyen.this.db;
                        Cursor cursor = database.GetData("Select sum((the_loai = 'dea')* diem) as de_a\n,sum((the_loai = 'deb')* diem) as de_b\n,sum((the_loai = 'det')* diem) as de_t\n,sum((the_loai = 'dec')* diem) as de_c\n,sum((the_loai = 'ded')* diem) as de_d\nFrom tbl_soctS \nWhere ngay_nhan = '" + mDate + "'");
                        if (!cursor.moveToFirst() || cursor == null) {
                            Frag_CanChuyen.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                            return;
                        }
                        int[] dem = new int[5];
                        if (cursor.getDouble(0) > 0.0d) {
                            dem[0] = 1;
                            Frag_CanChuyen.this.radio_dea.setEnabled(true);
                        } else {
                            dem[0] = 0;
                            Frag_CanChuyen.this.radio_dea.setEnabled(false);
                        }
                        if (cursor.getDouble(1) > 0.0d) {
                            dem[1] = 1;
                            Frag_CanChuyen.this.radio_deb.setEnabled(true);
                        } else {
                            dem[1] = 0;
                            Frag_CanChuyen.this.radio_deb.setEnabled(false);
                        }
                        if (cursor.getDouble(2) > 0.0d) {
                            dem[2] = 1;
                        } else {
                            dem[2] = 0;
                        }
                        if (cursor.getDouble(3) > 0.0d) {
                            dem[3] = 1;
                            Frag_CanChuyen.this.radio_dec.setEnabled(true);
                        } else {
                            dem[3] = 0;
                            Frag_CanChuyen.this.radio_dec.setEnabled(false);
                        }
                        if (cursor.getDouble(4) > 0.0d) {
                            dem[4] = 1;
                            Frag_CanChuyen.this.radio_ded.setEnabled(true);
                        } else {
                            dem[4] = 0;
                            Frag_CanChuyen.this.radio_ded.setEnabled(false);
                        }
                        if (dem[0] == 0 && ((dem[1] == 1 || dem[2] == 1) && dem[3] == 0 && dem[4] == 0)) {
                            Frag_CanChuyen.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                            Frag_CanChuyen.this.ln_xi.setVisibility(View.GONE);
                            Frag_CanChuyen.this.li_loaide.setVisibility(View.GONE);
                            Frag_CanChuyen.this.radio_deb.setChecked(true);
                            Frag_CanChuyen.this.xem_RecycView();
                        } else if (dem[0] == 0 && dem[1] == 0 && dem[2] == 0 && dem[3] == 0 && dem[4] == 0) {
                            Frag_CanChuyen.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                            Frag_CanChuyen.this.ln_xi.setVisibility(View.GONE);
                            Frag_CanChuyen.this.li_loaide.setVisibility(View.GONE);
                            Frag_CanChuyen.this.radio_deb.setChecked(true);
                            Frag_CanChuyen.this.xem_RecycView();
                        } else {
                            Frag_CanChuyen.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                            Frag_CanChuyen.this.ln_xi.setVisibility(View.GONE);
                            Frag_CanChuyen.this.li_loaide.setVisibility(View.VISIBLE);
                            Frag_CanChuyen.this.radio_deb.setChecked(true);
                            Frag_CanChuyen.this.xem_RecycView();
                        }
                        if (!cursor.isClosed() && cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    } catch (SQLException e) {
                        Frag_CanChuyen.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                    }
                }
            }
        });
        this.radio_dea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass3 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_CanChuyen.this.radio_dea.isChecked()) {
                    Frag_CanChuyen.this.DangXuat = "the_loai = 'dea'";
                    Frag_CanChuyen.this.ln_xi.setVisibility(View.GONE);
                    Frag_CanChuyen.this.xem_RecycView();
                }
            }
        });
        this.radio_deb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass4 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_CanChuyen.this.radio_deb.isChecked()) {
                    Frag_CanChuyen.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                    Frag_CanChuyen.this.ln_xi.setVisibility(View.GONE);
                    Frag_CanChuyen.this.xem_RecycView();
                }
            }
        });
        this.radio_dec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass5 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_CanChuyen.this.radio_dec.isChecked()) {
                    Frag_CanChuyen.this.DangXuat = "the_loai = 'dec'";
                    Frag_CanChuyen.this.ln_xi.setVisibility(View.GONE);
                    Frag_CanChuyen.this.xem_RecycView();
                }
            }
        });
        this.radio_ded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass6 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_CanChuyen.this.radio_ded.isChecked()) {
                    Frag_CanChuyen.this.DangXuat = "the_loai = 'ded'";
                    Frag_CanChuyen.this.ln_xi.setVisibility(View.GONE);
                    Frag_CanChuyen.this.xem_RecycView();
                }
            }
        });
        this.radio_lo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass7 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Frag_CanChuyen.this.radio_lo.isChecked()) {
                    Frag_CanChuyen.this.DangXuat = "the_loai = 'lo'";
                    Frag_CanChuyen.this.ln_xi.setVisibility(View.GONE);
                    Frag_CanChuyen.this.li_loaide.setVisibility(View.GONE);
                    Frag_CanChuyen.this.layout.setVisibility(View.GONE);
                    Frag_CanChuyen.this.xem_RecycView();
                }
            }
        });
        this.radio_xi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass8 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Frag_CanChuyen.this.radio_xi.isChecked()) {
                    new MainActivity();
                    Frag_CanChuyen.this.DangXuat = "the_loai = 'xi'";
                    Frag_CanChuyen.this.layout.setVisibility(View.GONE);
                    Frag_CanChuyen.this.ln_xi.setVisibility(View.VISIBLE);
                    Frag_CanChuyen.this.li_loaide.setVisibility(View.GONE);
                    try {
                        Database database = Frag_CanChuyen.this.db;
                        Cursor cursor = database.GetData("Select count(id) From tbl_soctS WHERE the_loai = 'xn' AND ngay_nhan = '" + MainActivity.Get_date() + "'");
                        if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                            Frag_CanChuyen.this.check_xn.setVisibility(View.VISIBLE);
                        }
                        Frag_CanChuyen.this.xem_RecycView();
                    } catch (SQLException e) {
                    }
                }
            }
        });
        this.radio_bc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Frag_CanChuyen.this.radio_bc.isChecked()) {
                Frag_CanChuyen.this.DangXuat = "the_loai = 'bc'";
                Frag_CanChuyen.this.layout.setVisibility(View.GONE);
                Frag_CanChuyen.this.ln_xi.setVisibility(View.GONE);
                Frag_CanChuyen.this.li_loaide.setVisibility(View.GONE);
                Frag_CanChuyen.this.xem_RecycView();
            }
        });
        this.check_x2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Frag_CanChuyen.this.check_x2.isChecked()) {
                Frag_CanChuyen.this.DangXuat = "the_loai = 'xi'";
                Frag_CanChuyen.this.lay_x2 = "length(so_chon) = 5 ";
                Frag_CanChuyen.this.check_xn.setChecked(false);
            } else {
                Frag_CanChuyen.this.lay_x2 = "";
            }
            Frag_CanChuyen.this.xem_RecycView();
        });
        this.check_x3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Frag_CanChuyen.this.check_x3.isChecked()) {
                Frag_CanChuyen.this.DangXuat = "the_loai = 'xi'";
                Frag_CanChuyen.this.lay_x3 = "OR length(so_chon) = 8 ";
                Frag_CanChuyen.this.check_xn.setChecked(false);
            } else {
                Frag_CanChuyen.this.lay_x3 = "";
            }
            Frag_CanChuyen.this.xem_RecycView();
        });
        this.check_x4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Frag_CanChuyen.this.check_x4.isChecked()) {
                Frag_CanChuyen.this.DangXuat = "the_loai = 'xi'";
                Frag_CanChuyen.this.lay_x4 = "OR length(so_chon) = 11 ";
                Frag_CanChuyen.this.check_xn.setChecked(false);
            } else {
                Frag_CanChuyen.this.lay_x4 = "";
            }
            Frag_CanChuyen.this.xem_RecycView();
        });
        this.check_xn.setOnClickListener(v -> {
            if (Frag_CanChuyen.this.check_xn.isChecked()) {
                Frag_CanChuyen.this.DangXuat = "the_loai = 'xn'";
                Frag_CanChuyen.this.check_x2.setChecked(false);
                Frag_CanChuyen.this.check_x3.setChecked(false);
                Frag_CanChuyen.this.check_x4.setChecked(false);
                Frag_CanChuyen.this.xem_RecycView();
            }
        });
        this.btn_Xuatso.setOnClickListener(v -> {
            if (Congthuc.isNumeric(Frag_CanChuyen.this.edt_tien.getText().toString().replaceAll("%", "").replaceAll("n", "").replaceAll("k", "").replaceAll("d", "").replaceAll(">", "").replaceAll("\\.", "")) || Frag_CanChuyen.this.edt_tien.getText().toString().length() == 0) {
                Frag_CanChuyen.this.btn_click();
            } else {
                Toast.makeText(Frag_CanChuyen.this.getActivity(), "Kiểm tra lại tiền!", Toast.LENGTH_LONG).show();
            }
        });
        this.lay_x2 = "length(so_chon) = 5 ";
        this.lay_x3 = "OR length(so_chon) = 8 ";
        this.lay_x4 = "OR length(so_chon) = 11 ";
        this.no_rp_number.setOnItemClickListener((adapterView, view, position, id) -> {
            try {
                new MainActivity();
                Cursor c = Frag_CanChuyen.this.db.GetData("Select ten_kh, sum(diem_quydoi) From tbl_soctS WHERE so_chon = '" + Frag_CanChuyen.this.mSo.get(position) + "' AND ngay_nhan = '" + MainActivity.Get_date() + "' AND type_kh = 1 AND " + Frag_CanChuyen.this.DangXuat + " GROUP BY so_dienthoai");
                String s1 = "";
                while (c.moveToNext()) {
                    s1 = s1 + c.getString(0) + ": " + c.getString(1) + "\n";
                }
                Toast.makeText(Frag_CanChuyen.this.getActivity(), s1, Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                System.out.println(e);
            }
        });
        try {
            if (MainActivity.jSon_Setting.getInt("bao_cao_so") == 0) {
                this.sapxep = "diem DESC";
            } else {
                this.sapxep = "ton DESC, diem DESC";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        xemlv();
        return this.v;
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.runnable);
    }

    public void xemlv() {
        if (this.DangXuat != null) {
            xem_RecycView();
        } else {
            this.radio_de.setChecked(true);
        }
    }

    public void btn_click() {
        int TienChuyen;
        String str;
        String str2;
        int MaxTien;
        int MaxTien2;
        String mDate;
        int MaxTien3;
        this.xuatDan = "";
        new MainActivity();
        String curDate = MainActivity.Get_date();
        xemlv();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        hourFormat.setTimeZone(TimeZone.getDefault());
        String mDate2 = dmyFormat.format(calendar.getTime());
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
        if (mDate2.indexOf(curDate) > -1) {
            if (this.edt_tien.getText().toString().length() != 0) {
                if (this.edt_tien.getText().toString() != "0") {
                    String str3 = this.edt_tien.getText().toString().replaceAll("%", "").replaceAll("n", "").replaceAll("k", "").replaceAll("d", "").replaceAll(">", "").replaceAll("\\.", "").replaceAll(",", "");
                    if (Congthuc.isNumeric(str3)) {
                        TienChuyen = Integer.parseInt(str3);
                    } else {
                        TienChuyen = 0;
                    }
                    str = this.DangXuat;
                    if (str == "(the_loai = 'deb' or the_loai = 'det')") {
                        if (str != "the_loai = 'lo'" && str != "the_loai = 'dea'" && str != "the_loai = 'dec'") {
                            if (str != "the_loai = 'ded'") {
                                if (str == "the_loai = 'xi'") {
                                    this.xuatDan = "Xien:\n";
                                    int i = this.min;
                                    while (i < this.mSo.size()) {
                                        if (this.edt_tien.getText().toString().indexOf("%") > -1) {
                                            MaxTien3 = (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) / mLamtron) * mLamtron;
                                        } else if (this.edt_tien.getText().toString().indexOf(">") > -1) {
                                            MaxTien3 = (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) / mLamtron) * mLamtron;
                                        } else if (TienChuyen == 0) {
                                            MaxTien3 = (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) / mLamtron) * mLamtron;
                                        } else if (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) > TienChuyen) {
                                            MaxTien3 = (TienChuyen / mLamtron) * mLamtron;
                                        } else {
                                            MaxTien3 = (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) / mLamtron) * mLamtron;
                                        }
                                        if (this.edt_tien.getText().toString().indexOf("%") > -1) {
                                            if (MaxTien3 > 0) {
                                                try {
                                                    if (MainActivity.jSon_Setting.getInt("chuyen_xien") > 0) {
                                                        this.xuatDan += this.mSo.get(i) + "x" + ((MaxTien3 * TienChuyen) / 1000) + "d ";
                                                    } else if (MainActivity.jSon_Setting.getInt("chuyen_xien") == 0) {
                                                        this.xuatDan += this.mSo.get(i) + "x" + ((MaxTien3 * TienChuyen) / 100) + "n ";
                                                    }
                                                } catch (JSONException e2) {
                                                    e2.printStackTrace();
                                                }
                                            }
                                        } else if (this.edt_tien.getText().toString().indexOf(">") > -1) {
                                            if (MaxTien3 > TienChuyen) {
                                                try {
                                                    if (MainActivity.jSon_Setting.getInt("chuyen_xien") > 0) {
                                                        this.xuatDan += this.mSo.get(i) + "x" + ((MaxTien3 - TienChuyen) / 10) + "d ";
                                                    } else if (MainActivity.jSon_Setting.getInt("chuyen_xien") == 0) {
                                                        this.xuatDan += this.mSo.get(i) + "x" + (MaxTien3 - TienChuyen) + "n ";
                                                    }
                                                } catch (JSONException e3) {
                                                    e3.printStackTrace();
                                                }
                                            }
                                        } else if (this.edt_tien.getText().toString().indexOf(">") == -1 && this.edt_tien.getText().toString().indexOf("%") == -1 && MaxTien3 > 0) {
                                            try {
                                                if (MainActivity.jSon_Setting.getInt("chuyen_xien") > 0) {
                                                    this.xuatDan += this.mSo.get(i) + "x" + (MaxTien3 / 10) + "d ";
                                                } else if (MainActivity.jSon_Setting.getInt("chuyen_xien") == 0) {
                                                    this.xuatDan += this.mSo.get(i) + "x" + MaxTien3 + "n ";
                                                }
                                            } catch (JSONException e4) {
                                                e4.printStackTrace();
                                            }
                                        }
                                        i++;
                                        hourFormat = hourFormat;
                                    }
                                } else if (str == "the_loai = 'bc'") {
                                    this.xuatDan = "Cang:\n";
                                    int i2 = this.min;
                                    int tien = 0;
                                    while (i2 < this.mSo.size()) {
                                        if (TienChuyen == 0) {
                                            MaxTien2 = (Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) / mLamtron) * mLamtron;
                                        } else if (this.edt_tien.getText().toString().indexOf("%") > -1) {
                                            MaxTien2 = (((Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) * TienChuyen) / mLamtron) / 100) * mLamtron;
                                        } else if (this.edt_tien.getText().toString().indexOf(">") > -1) {
                                            MaxTien2 = ((Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) - TienChuyen) / mLamtron) * mLamtron;
                                        } else if (Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) > TienChuyen) {
                                            MaxTien2 = (TienChuyen / mLamtron) * mLamtron;
                                        } else {
                                            MaxTien2 = (Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) / mLamtron) * mLamtron;
                                        }
                                        if (MaxTien2 <= 0) {
                                            mDate = mDate2;
                                        } else if (tien > MaxTien2) {
                                            StringBuilder sb = new StringBuilder();
                                            mDate = mDate2;
                                            sb.append(this.xuatDan);
                                            sb.append("x");
                                            sb.append(tien);
                                            sb.append("n ");
                                            this.xuatDan = sb.toString();
                                            this.xuatDan += this.mSo.get(i2) + ",";
                                            tien = MaxTien2;
                                        } else {
                                            mDate = mDate2;
                                            this.xuatDan += this.mSo.get(i2) + ",";
                                            tien = MaxTien2;
                                        }
                                        i2++;
                                        mDate2 = mDate;
                                    }
                                    if (this.xuatDan.length() > 4) {
                                        this.xuatDan += "x" + tien + "n";
                                    }
                                    if (this.xuatDan.indexOf(":") > -1) {
                                        String str4 = this.xuatDan;
                                        if (str4.substring(str4.indexOf(":")).length() > 7) {
                                            if (!getActivity().isFinishing()) {
                                                Dialog(1);
                                                return;
                                            }
                                            return;
                                        }
                                    }
                                    Toast.makeText(getActivity(), "Không có số liệu!", Toast.LENGTH_LONG).show();
                                    return;
                                } else if (str == "the_loai = 'xn'") {
                                    this.xuatDan = "Xnhay:\n";
                                    for (int i3 = this.min; i3 < this.mSo.size(); i3++) {
                                        if (this.edt_tien.getText().toString().indexOf("%") > -1) {
                                            MaxTien = (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) / mLamtron) * mLamtron;
                                        } else if (this.edt_tien.getText().toString().indexOf(">") > -1) {
                                            MaxTien = (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) / mLamtron) * mLamtron;
                                        } else if (TienChuyen == 0) {
                                            MaxTien = (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) / mLamtron) * mLamtron;
                                        } else if (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) > TienChuyen) {
                                            MaxTien = (TienChuyen / mLamtron) * mLamtron;
                                        } else {
                                            MaxTien = (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) / mLamtron) * mLamtron;
                                        }
                                        if (this.edt_tien.getText().toString().indexOf("%") > -1) {
                                            if (MaxTien > 0) {
                                                try {
                                                    if (MainActivity.jSon_Setting.getInt("chuyen_xien") > 0) {
                                                        this.xuatDan += this.mSo.get(i3) + "x" + ((MaxTien * TienChuyen) / 1000) + "d\n";
                                                    } else if (MainActivity.jSon_Setting.getInt("chuyen_xien") == 0) {
                                                        this.xuatDan += this.mSo.get(i3) + "x" + ((MaxTien * TienChuyen) / 100) + "n\n";
                                                    }
                                                } catch (JSONException e5) {
                                                    e5.printStackTrace();
                                                }
                                            }
                                        } else if (this.edt_tien.getText().toString().indexOf(">") > -1) {
                                            if (MaxTien > TienChuyen) {
                                                try {
                                                    if (MainActivity.jSon_Setting.getInt("chuyen_xien") > 0) {
                                                        this.xuatDan += this.mSo.get(i3) + "x" + ((MaxTien - TienChuyen) / 10) + "d\n";
                                                    } else if (MainActivity.jSon_Setting.getInt("chuyen_xien") == 0) {
                                                        this.xuatDan += this.mSo.get(i3) + "x" + (MaxTien - TienChuyen) + "n\n";
                                                    }
                                                } catch (JSONException e6) {
                                                    e6.printStackTrace();
                                                }
                                            }
                                        } else if (!this.edt_tien.getText().toString().contains(">") && !this.edt_tien.getText().toString().contains("%") && MaxTien > 0) {
                                            try {
                                                if (MainActivity.jSon_Setting.getInt("chuyen_xien") > 0) {
                                                    this.xuatDan += this.mSo.get(i3) + "x" + (MaxTien / 10) + "d\n";
                                                } else if (MainActivity.jSon_Setting.getInt("chuyen_xien") == 0) {
                                                    this.xuatDan += this.mSo.get(i3) + "x" + MaxTien + "n\n";
                                                }
                                            } catch (JSONException e7) {
                                                e7.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                if (this.xuatDan.indexOf(":") > -1) {
                                }
                                Toast.makeText(getActivity(), "Không có số liệu!", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }
                    str2 = this.DangXuat;
                    if (str2 != "(the_loai = 'deb' or the_loai = 'det')") {
                        this.xuatDan = this.db.XuatDanTon2("deb", this.edt_tien.getText().toString(), this.min, this.max);
                    } else if (str2 == "the_loai = 'dea'") {
                        this.xuatDan = this.db.XuatDanTon2("dea", this.edt_tien.getText().toString(), this.min, this.max);
                    } else if (str2 == "the_loai = 'dec'") {
                        this.xuatDan = this.db.XuatDanTon2("dec", this.edt_tien.getText().toString(), this.min, this.max);
                    } else if (str2 == "the_loai = 'ded'") {
                        this.xuatDan = this.db.XuatDanTon2("ded", this.edt_tien.getText().toString(), this.min, this.max);
                    } else {
                        this.xuatDan = this.db.XuatDanTon2("lo", this.edt_tien.getText().toString(), this.min, this.max);
                    }
                    if (this.xuatDan.indexOf(":") > -1) {
                    }
                    Toast.makeText(getActivity(), "Không có số liệu!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            TienChuyen = 0;
            str = this.DangXuat;
            if (str == "(the_loai = 'deb' or the_loai = 'det')") {
            }
            str2 = this.DangXuat;
            if (str2 != "(the_loai = 'deb' or the_loai = 'det')") {
            }
            if (this.xuatDan.indexOf(":") > -1) {
            }
            Toast.makeText(getActivity(), "Không có số liệu!", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getActivity(), "Không làm việc với dữ liệu ngày cũ!", Toast.LENGTH_LONG).show();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0079, code lost:
        if (r12.isClosed() != false) goto L_0x007c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x007c, code lost:
        r0 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0083, code lost:
        if (r0 >= r23.mSo.size()) goto L_0x0178;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0085, code lost:
        r14 = r23.mSo.get(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r15 = new org.json.JSONObject(r23.jsonKhongmax.getString("soDe"));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00a9, code lost:
        if (r15.has(r23.mSo.get(r0)) == false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00b7, code lost:
        r5 = (double) r15.getInt(r23.mSo.get(r0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00ba, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00bb, code lost:
        r5 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00c4, code lost:
        r5 = 100000.0d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r8 = java.lang.Integer.parseInt(r23.mTienTon.get(r0).replace(".", ""));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e6, code lost:
        if (r0.has(r14) == false) goto L_0x0131;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e8, code lost:
        if (r8 <= 0) goto L_0x0131;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ea, code lost:
        r9 = new org.json.JSONObject();
        r16 = r0.getJSONObject(r14).getDouble("Da_chuyen");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00f7, code lost:
        r15 = r11;
        r10 = (double) r8;
        java.lang.Double.isNaN(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0100, code lost:
        if ((r16 + r10) > r5) goto L_0x0115;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r0.getJSONObject(r14).getInt("Da_chuyen") + r8);
        r9.put(r3, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0115, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0123, code lost:
        r10 = (double) r0.getJSONObject(r14).getInt("Da_chuyen");
        java.lang.Double.isNaN(r10);
        r9.put(r3, r5 - r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x012c, code lost:
        r0.put(r14, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0131, code lost:
        r15 = r11;
        r9 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x013a, code lost:
        if (((double) r8) > r5) goto L_0x0146;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x013c, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r8);
        r9.put(r3, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0146, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r5);
        r9.put(r3, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x014f, code lost:
        r0.put(r14, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0152, code lost:
        r0 = r0 + 1;
        r11 = r15;
        r8 = r8;
        r9 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x015e, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0160, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0168, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x016f, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0170, code lost:
        r5 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0180, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0188, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x018b, code lost:
        r4 = r0.keys();
        r0 = new java.util.ArrayList<>();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0199, code lost:
        if (r4.hasNext() != false) goto L_0x019b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x019b, code lost:
        r9 = r4.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x01aa, code lost:
        if (r0.getJSONObject(r9).getInt(r3) > 0) goto L_0x01ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x01ac, code lost:
        r0.add(r0.getJSONObject(r9));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01b4, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x01b5, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x01b9, code lost:
        java.util.Collections.sort(r0, new tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass17(r23));
        r23.xuatDan = "De:";
        r9 = 0;
        r0 = 0;
        r11 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x01d6, code lost:
        if (r0 < r0.size()) goto L_0x01d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01d8, code lost:
        r13 = r0.get(r0);
        r16 = r13.getInt(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01e8, code lost:
        if (r16 > 0) goto L_0x01ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0063, code lost:
        if (r12.isClosed() == false) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x01ea, code lost:
        r17 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01ee, code lost:
        if (r9 > r16) goto L_0x01f0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x01f0, code lost:
        r20 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:?, code lost:
        r23.xuatDan += "x" + r9 + "n ";
        r23.xuatDan += r13.getString("So_chon") + ",";
        r4 = r16;
        r11 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0228, code lost:
        r20 = r5;
        r23.xuatDan += r13.getString("So_chon") + ",";
        r4 = r16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0245, code lost:
        r11 = r11 + 1;
        r9 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0249, code lost:
        r17 = r4;
        r20 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x024e, code lost:
        r0 = r0 + 1;
        r3 = r3;
        r4 = r17;
        r5 = r20;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0065, code lost:
        r12.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0267, code lost:
        r23.xuatDan += "x" + r9 + "n ";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0281, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0283, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x0288, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x028d, code lost:
        return r23.xuatDan;
     */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x029a  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x019b  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01d8  */
    public String TaoTinDe(String ten_kh) {
        Throwable th;
        String str = "Se_chuyen";
        double maxDang = 0.0d;
        JSONObject jSon = new JSONObject();
        MainActivity mainActivity = new MainActivity();
        String mDate = MainActivity.Get_date();
        String Str1 = "Select so_chon, Sum(diem) FROM tbl_soctS Where ten_kh = '" + ten_kh + "' AND type_kh = 2 AND (the_loai = 'deb' or the_loai = 'det') AND ngay_nhan = '" + mDate + "' Group by so_chon";
        Cursor cursor = this.db.GetData(Str1);
        while (cursor.moveToNext()) {
            try {
                JSONObject jsonSoCt = new JSONObject();
                jsonSoCt.put("Da_chuyen", cursor.getInt(1));
                jsonSoCt.put(str, 0);
                jSon.put(cursor.getString(0), jsonSoCt);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Throwable th2) {
                th = th2;
                if (!cursor.isClosed()) {
                }
                try {
                    throw th;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0079, code lost:
        if (r12.isClosed() != false) goto L_0x007c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x007c, code lost:
        r0 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0083, code lost:
        if (r0 >= r23.mSo.size()) goto L_0x0178;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0085, code lost:
        r14 = r23.mSo.get(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r15 = new org.json.JSONObject(r23.jsonKhongmax.getString("soLo"));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00a9, code lost:
        if (r15.has(r23.mSo.get(r0)) == false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00b7, code lost:
        r5 = (double) r15.getInt(r23.mSo.get(r0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00ba, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00bb, code lost:
        r5 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00c4, code lost:
        r5 = 100000.0d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r8 = java.lang.Integer.parseInt(r23.mTienTon.get(r0).replace(".", ""));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e6, code lost:
        if (r0.has(r14) == false) goto L_0x0131;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e8, code lost:
        if (r8 <= 0) goto L_0x0131;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ea, code lost:
        r9 = new org.json.JSONObject();
        r16 = r0.getJSONObject(r14).getDouble("Da_chuyen");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00f7, code lost:
        r15 = r11;
        r10 = (double) r8;
        java.lang.Double.isNaN(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0100, code lost:
        if ((r16 + r10) > r5) goto L_0x0115;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r0.getJSONObject(r14).getInt("Da_chuyen") + r8);
        r9.put(r3, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0115, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0123, code lost:
        r10 = (double) r0.getJSONObject(r14).getInt("Da_chuyen");
        java.lang.Double.isNaN(r10);
        r9.put(r3, r5 - r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x012c, code lost:
        r0.put(r14, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0131, code lost:
        r15 = r11;
        r9 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x013a, code lost:
        if (((double) r8) > r5) goto L_0x0146;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x013c, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r8);
        r9.put(r3, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0146, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r5);
        r9.put(r3, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x014f, code lost:
        r0.put(r14, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0152, code lost:
        r0 = r0 + 1;
        r11 = r15;
        r8 = r8;
        r9 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x015e, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0160, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0168, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x016f, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0170, code lost:
        r5 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0180, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0188, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x018b, code lost:
        r4 = r0.keys();
        r0 = new java.util.ArrayList<>();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0199, code lost:
        if (r4.hasNext() != false) goto L_0x019b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x019b, code lost:
        r9 = r4.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x01aa, code lost:
        if (r0.getJSONObject(r9).getInt(r3) > 0) goto L_0x01ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x01ac, code lost:
        r0.add(r0.getJSONObject(r9));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01b4, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x01b5, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x01b9, code lost:
        java.util.Collections.sort(r0, new tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass18(r23));
        r23.xuatDan = "Lo:";
        r9 = 0;
        r0 = 0;
        r11 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x01d6, code lost:
        if (r0 < r0.size()) goto L_0x01d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01d8, code lost:
        r13 = r0.get(r0);
        r16 = r13.getInt(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01e8, code lost:
        if (r16 > 0) goto L_0x01ea;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0063, code lost:
        if (r12.isClosed() == false) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x01ea, code lost:
        r17 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01ee, code lost:
        if (r9 > r16) goto L_0x01f0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x01f0, code lost:
        r20 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:?, code lost:
        r23.xuatDan += "x" + r9 + "d ";
        r23.xuatDan += r13.getString("So_chon") + ",";
        r4 = r16;
        r11 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0228, code lost:
        r20 = r5;
        r23.xuatDan += r13.getString("So_chon") + ",";
        r4 = r16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0245, code lost:
        r11 = r11 + 1;
        r9 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0249, code lost:
        r17 = r4;
        r20 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x024e, code lost:
        r0 = r0 + 1;
        r3 = r3;
        r4 = r17;
        r5 = r20;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0065, code lost:
        r12.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0267, code lost:
        r23.xuatDan += "x" + r9 + "d ";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0281, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0283, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x0288, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x028d, code lost:
        return r23.xuatDan;
     */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x029a  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x019b  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x01d8  */
    public String TaoTinLo(String ten_kh) throws Throwable {
        Throwable th;
        String str = "Se_chuyen";
        double maxDang = 0.0d;
        JSONObject jSon = new JSONObject();
        MainActivity mainActivity = new MainActivity();
        String mDate = MainActivity.Get_date();
        String Str1 = "Select so_chon, Sum(diem) FROM tbl_soctS Where ten_kh = '" + ten_kh + "' AND type_kh = 2 AND the_loai = 'lo' AND ngay_nhan = '" + mDate + "' Group by so_chon";
        Cursor cursor = this.db.GetData(Str1);
        while (cursor.moveToNext()) {
            try {
                JSONObject jsonSoCt = new JSONObject();
                jsonSoCt.put("Da_chuyen", cursor.getInt(1));
                jsonSoCt.put(str, 0);
                jSon.put(cursor.getString(0), jsonSoCt);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Throwable th2) {
                th = th2;
                if (!cursor.isClosed()) {
                }
                throw th;
            }
        }
        return str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:101:0x025c, code lost:
        return r23.xuatDan;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x007a, code lost:
        if (r12.isClosed() != false) goto L_0x007d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x007d, code lost:
        r0 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0084, code lost:
        if (r0 >= r23.mSo.size()) goto L_0x013c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0086, code lost:
        r14 = r23.mSo.get(r0);
        r5 = (double) r23.jsonKhongmax.getInt("cang");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r8 = java.lang.Integer.parseInt(r23.mTienTon.get(r0).replace(".", ""));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00b1, code lost:
        if (r7.has(r14) == false) goto L_0x0106;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00b3, code lost:
        if (r8 <= 0) goto L_0x0106;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r13 = new org.json.JSONObject();
        r17 = r7.getJSONObject(r14).getDouble("Da_chuyen");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00c2, code lost:
        r15 = r9;
        r9 = (double) r8;
        java.lang.Double.isNaN(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00cb, code lost:
        if ((r17 + r9) > r5) goto L_0x00e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r13.put("So_chon", r14);
        r13.put("Da_chuyen", r7.getJSONObject(r14).getInt("Da_chuyen") + r8);
        r13.put(r3, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00e0, code lost:
        r13.put("So_chon", r14);
        r13.put("Da_chuyen", r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00ee, code lost:
        r9 = (double) r7.getJSONObject(r14).getInt("Da_chuyen");
        java.lang.Double.isNaN(r5);
        java.lang.Double.isNaN(r9);
        r13.put(r3, r5 - r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00fa, code lost:
        r7.put(r14, r13);
        r13 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00ff, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0102, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0106, code lost:
        r15 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        r9 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x010c, code lost:
        r13 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0110, code lost:
        if (((double) r8) > r5) goto L_0x011c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r8);
        r9.put(r3, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x011c, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r5);
        r9.put(r3, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0125, code lost:
        r7.put(r14, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0128, code lost:
        r0 = r0 + 1;
        r11 = r13;
        r9 = r15;
        r8 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0133, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0135, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0138, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0141, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0146, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0149, code lost:
        r4 = r7.keys();
        r0 = new java.util.ArrayList<>();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0157, code lost:
        if (r4.hasNext() != false) goto L_0x0159;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0159, code lost:
        r9 = r4.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0168, code lost:
        if (r7.getJSONObject(r9).getInt(r3) > 0) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x016a, code lost:
        r0.add(r7.getJSONObject(r9));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0172, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0173, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0177, code lost:
        java.util.Collections.sort(r0, new tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass19(r23));
        r23.xuatDan = "Cang:";
        r9 = 0;
        r0 = 0;
        r11 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0196, code lost:
        if (r0 < r0.size()) goto L_0x0198;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:?, code lost:
        r14 = r0.get(r0);
        r18 = r14.getInt(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x01a8, code lost:
        if (r18 > 0) goto L_0x01aa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x01aa, code lost:
        r19 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01ae, code lost:
        if (r9 > r18) goto L_0x01b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0063, code lost:
        if (r12.isClosed() == false) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:?, code lost:
        r6 = new java.lang.StringBuilder();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x01b5, code lost:
        r21 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:?, code lost:
        r6.append(r23.xuatDan);
        r6.append("x");
        r6.append(r9);
        r6.append("n ");
        r23.xuatDan = r6.toString();
        r23.xuatDan += r14.getString("So_chon") + ",";
        r4 = r18;
        r11 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x01e8, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01ed, code lost:
        r21 = r7;
        r23.xuatDan += r14.getString("So_chon") + ",";
        r4 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x020a, code lost:
        r11 = r11 + 1;
        r9 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x020e, code lost:
        r19 = r5;
        r21 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0213, code lost:
        r0 = r0 + 1;
        r4 = r4;
        r3 = r3;
        r5 = r19;
        r7 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0065, code lost:
        r12.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x021f, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x0234, code lost:
        r23.xuatDan += "x" + r9 + "n ";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x024e, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0250, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0257, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x026a  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0159  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0198 A[SYNTHETIC, Splitter:B:73:0x0198] */
    public String TaoTinCang(String ten_kh) throws Throwable {
        Throwable th;
        String str = "Se_chuyen";
        double maxDang = 0.0d;
        JSONObject jSon = new JSONObject();
        MainActivity mainActivity = new MainActivity();
        String mDate = MainActivity.Get_date();
        String Str1 = "Select so_chon, Sum(diem) FROM tbl_soctS Where ten_kh = '" + ten_kh + "' AND type_kh = 2 AND the_loai = 'bc' AND ngay_nhan = '" + mDate + "' Group by so_chon";
        Cursor cursor = this.db.GetData(Str1);
        while (cursor.moveToNext()) {
            try {
                JSONObject jsonSoCt = new JSONObject();
                jsonSoCt.put("Da_chuyen", cursor.getInt(1));
                jsonSoCt.put(str, 0);
                jSon.put(cursor.getString(0), jsonSoCt);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Throwable th2) {
                th = th2;
                if (!cursor.isClosed()) {
                }
                throw th;
            }
        }
        return str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x007c, code lost:
        if (r13.isClosed() != false) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x007f, code lost:
        r0 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0086, code lost:
        if (r0 >= r23.mSo.size()) goto L_0x0180;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0088, code lost:
        r14 = r23.mSo.get(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0090, code lost:
        r15 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x009a, code lost:
        if (r14.length() != 5) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00a2, code lost:
        if (r23.jsonKhongmax.getInt(r4) != 0) goto L_0x00a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00a4, code lost:
        r15 = 100000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00a8, code lost:
        r15 = r23.jsonKhongmax.getInt(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00b6, code lost:
        if (r14.length() != 8) goto L_0x00cc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00be, code lost:
        if (r23.jsonKhongmax.getInt(r3) != 0) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00c0, code lost:
        r15 = 100000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00c4, code lost:
        r15 = r23.jsonKhongmax.getInt(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00d2, code lost:
        if (r14.length() != 11) goto L_0x00e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00da, code lost:
        if (r23.jsonKhongmax.getInt(r2) != 0) goto L_0x00e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00dc, code lost:
        r15 = 100000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00e0, code lost:
        r15 = r23.jsonKhongmax.getInt(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00e7, code lost:
        r2 = java.lang.Integer.parseInt(r23.mTienTon.get(r0).replace(".", ""));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0101, code lost:
        if (r0.has(r14) == false) goto L_0x014b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0103, code lost:
        if (r2 <= 0) goto L_0x014b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0105, code lost:
        r9 = new org.json.JSONObject();
        r19 = r0.getJSONObject(r14).getDouble("Da_chuyen");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0112, code lost:
        r10 = r3;
        r21 = r4;
        r3 = (double) r2;
        java.lang.Double.isNaN(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x011e, code lost:
        if ((r19 + r3) > ((double) r15)) goto L_0x0133;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0120, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r0.getJSONObject(r14).getInt("Da_chuyen") + r2);
        r9.put("Se_chuyen", r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0133, code lost:
        r9.put("So_chon", r14);
        r9.put("Da_chuyen", r15);
        r9.put("Se_chuyen", r15 - r0.getJSONObject(r14).getInt("Da_chuyen"));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0146, code lost:
        r0.put(r14, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x014b, code lost:
        r10 = r3;
        r21 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x014e, code lost:
        if (r2 <= 0) goto L_0x018d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0150, code lost:
        r3 = new org.json.JSONObject();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0155, code lost:
        if (r2 > r15) goto L_0x0161;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0157, code lost:
        r3.put("So_chon", r14);
        r3.put("Da_chuyen", r2);
        r3.put("Se_chuyen", r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0161, code lost:
        r3.put("So_chon", r14);
        r3.put("Da_chuyen", r15);
        r3.put("Se_chuyen", r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x016a, code lost:
        r0.put(r14, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x016d, code lost:
        r0 = r0 + 1;
        r3 = r10;
        r9 = r9;
        r10 = r10;
        r2 = r2;
        r4 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x017a, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x017c, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0185, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x018a, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x018d, code lost:
        r2 = r0.keys();
        r0 = new java.util.ArrayList<>();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x019b, code lost:
        if (r2.hasNext() != false) goto L_0x019d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x019d, code lost:
        r4 = r2.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0067, code lost:
        if (r13.isClosed() == false) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x01ac, code lost:
        if (r0.getJSONObject(r4).getInt("Se_chuyen") > 0) goto L_0x01ae;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x01ae, code lost:
        r0.add(r0.getJSONObject(r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x01b6, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x01b7, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x01bb, code lost:
        java.util.Collections.sort(r0, new tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass20(r23));
        r23.xuatDan = "Xien:\n";
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x01cd, code lost:
        if (r4 < r0.size()) goto L_0x01cf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x01cf, code lost:
        r7 = r0.get(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0069, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x01e0, code lost:
        if (tamhoang.ldpro4.MainActivity.jSon_Setting.getInt("chuyen_xien") > 0) goto L_0x01e2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x01e2, code lost:
        r23.xuatDan += r7.getString("So_chon") + "x" + (r7.getInt("Se_chuyen") / 10) + "d ";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x020b, code lost:
        r23.xuatDan += r7.getString("So_chon") + "x" + r7.getInt("Se_chuyen") + "n ";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0232, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0233, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0236, code lost:
        r4 = r4 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x023b, code lost:
        return r23.xuatDan;
     */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x019d  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01cf  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0247  */
    public String TaoTinXi(String ten_kh) throws Throwable {
        Throwable th;
        String str = "xien4";
        String str2 = "xien3";
        String str3 = "xien2";
        JSONObject jSon = new JSONObject();
        MainActivity mainActivity = new MainActivity();
        String mDate = MainActivity.Get_date();
        Cursor cursor = this.db.GetData("Select so_chon, Sum(diem) FROM tbl_soctS Where ten_kh = '" + ten_kh + "' AND type_kh = 2 AND the_loai = 'xi' AND ngay_nhan = '" + mDate + "' Group by so_chon");
        while (cursor.moveToNext()) {
            try {
                JSONObject jsonSoCt = new JSONObject();
                jsonSoCt.put("Da_chuyen", cursor.getInt(1));
                jsonSoCt.put("Se_chuyen", 0);
                jSon.put(cursor.getString(0), jsonSoCt);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Throwable th2) {
                th = th2;
                if (!cursor.isClosed()) {
                }
                throw th;
            }
        }
        return str;
    }

    public void Dialog(int poin) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.frag_canchuyen1);
        dialog.getWindow().setLayout(-1, -2);
        final String Chuyendi = this.xuatDan.replaceAll(",x", "x");
        this.Dachuyen = false;
        Spinner sprin_tenkhach = (Spinner) dialog.findViewById(R.id.sprin_tenkhach);
        final EditText edt_XuatDan = (EditText) dialog.findViewById(R.id.edt_XuatDan);
        Button btn_chuyendi = (Button) dialog.findViewById(R.id.btn_chuyendi);
        edt_XuatDan.setText("");
        edt_XuatDan.setText(this.xuatDan.replaceAll(",x", "x"));
        try {
            Cursor cur = this.db.GetData("Select * From tbl_kh_new WHERE type_kh <> 1 ORDER BY ten_kh");
            this.mContact.clear();
            this.mMobile.clear();
            this.mKhongMax.clear();
            this.mAppuse.clear();
            while (cur.moveToNext()) {
                if (cur.getString(2).indexOf("sms") <= -1) {
                    if (cur.getString(2).indexOf("TL") <= -1) {
                        if (MainActivity.arr_TenKH.indexOf(cur.getString(1)) > -1) {
                            this.mContact.add(cur.getString(0));
                            this.mMobile.add(cur.getString(1));
                            this.mKhongMax.add(cur.getString(6));
                            this.mAppuse.add(cur.getString(2));
                        }
                    }
                }
                this.mContact.add(cur.getString(0));
                this.mMobile.add(cur.getString(1));
                this.mKhongMax.add(cur.getString(6));
                this.mAppuse.add(cur.getString(2));
            }
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
            sprin_tenkhach.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.mContact));
        } catch (SQLException e) {
        }
        sprin_tenkhach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass21 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Frag_CanChuyen.this.mSpiner = position;
                try {
                    Frag_CanChuyen.this.jsonKhongmax = new JSONObject(Frag_CanChuyen.this.mKhongMax.get(Frag_CanChuyen.this.mSpiner));
                    if (Frag_CanChuyen.this.radio_deb.isChecked() && Frag_CanChuyen.this.radio_de.isChecked() && Frag_CanChuyen.this.jsonKhongmax.getString("danDe").length() > 0) {
                        edt_XuatDan.setText(Frag_CanChuyen.this.TaoTinDe(Frag_CanChuyen.this.mContact.get(Frag_CanChuyen.this.mSpiner)));
                    } else if (Frag_CanChuyen.this.radio_lo.isChecked() && Frag_CanChuyen.this.jsonKhongmax.getString("danLo").length() > 0) {
                        edt_XuatDan.setText(Frag_CanChuyen.this.TaoTinLo(Frag_CanChuyen.this.mContact.get(Frag_CanChuyen.this.mSpiner)));
                    } else if (Frag_CanChuyen.this.radio_xi.isChecked() && (Frag_CanChuyen.this.jsonKhongmax.getInt("xien2") > 0 || Frag_CanChuyen.this.jsonKhongmax.getInt("xien3") > 0 || Frag_CanChuyen.this.jsonKhongmax.getInt("xien4") > 0)) {
                        edt_XuatDan.setText(Frag_CanChuyen.this.TaoTinXi(Frag_CanChuyen.this.mContact.get(Frag_CanChuyen.this.mSpiner)));
                    } else if (!Frag_CanChuyen.this.radio_bc.isChecked() || Frag_CanChuyen.this.jsonKhongmax.getInt("cang") <= 0) {
                        edt_XuatDan.setText(Chuyendi);
                    } else {
                        edt_XuatDan.setText(Frag_CanChuyen.this.TaoTinCang(Frag_CanChuyen.this.mContact.get(Frag_CanChuyen.this.mSpiner)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        btn_chuyendi.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass22 */

            public void onClick(View v) {
                String str;
                String str2;
                new MainActivity();
                String mDate = MainActivity.Get_date();
                int NganDai = 0;
                try {
                    NganDai = MainActivity.jSon_Setting.getInt("gioi_han_tin");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int dodai = 2000;
                if (NganDai == 1) {
                    dodai = PathInterpolatorCompat.MAX_NUM_POINTS;
                } else if (NganDai == 2) {
                    dodai = 155;
                } else if (NganDai == 3) {
                    dodai = 315;
                } else if (NganDai == 4) {
                    dodai = 475;
                } else if (NganDai == 5) {
                    dodai = 995;
                } else if (NganDai == 6) {
                    dodai = 2000;
                }
                String str3 = "";
                if (Frag_CanChuyen.this.mMobile.size() <= 0 || edt_XuatDan.getText().toString().length() <= 0 || Frag_CanChuyen.this.Dachuyen) {
                    str = str3;
                    if (edt_XuatDan.getText().toString().length() != 0) {
                        if (Frag_CanChuyen.this.Dachuyen) {
                            dialog.cancel();
                        } else {
                            Toast.makeText(Frag_CanChuyen.this.getActivity(), "Chưa có chủ để chuyển tin!", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Frag_CanChuyen.this.Dachuyen = true;
                    String TinNhan = edt_XuatDan.getText().toString().replaceAll("'", " ").trim();
                    edt_XuatDan.setText(str3);
                    dialog.dismiss();
                    if (TinNhan.trim().length() < dodai) {
                        Cursor getSoTN = Frag_CanChuyen.this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + Frag_CanChuyen.this.mMobile.get(Frag_CanChuyen.this.mSpiner) + "' AND type_kh = 2");
                        getSoTN.moveToFirst();
                        Frag_CanChuyen.this.Xulytin(getSoTN.getInt(0) + 1, TinNhan.replaceAll("'", " ").trim(), 1);
                        if (getSoTN != null) {
                            getSoTN.close();
                        }
                        str = str3;
                    } else {
                        String TienChiTiet = "Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + Frag_CanChuyen.this.mMobile.get(Frag_CanChuyen.this.mSpiner) + "' AND type_kh = 2";
                        Cursor getSoTN2 = Frag_CanChuyen.this.db.GetData(TienChiTiet);
                        getSoTN2.moveToFirst();
                        int SotinNhan = getSoTN2.getInt(0) + 1;
                        String DangGi = "";
                        String[] Chitiet = null;
                        if (TinNhan.substring(0, 3).indexOf("De") > -1) {
                            DangGi = "De:";
                            TinNhan = TinNhan.replaceAll("De:", str3);
                            Chitiet = TinNhan.split(" ");
                        } else if (TinNhan.substring(0, 3).indexOf("Lo") > -1) {
                            DangGi = "Lo:";
                            TinNhan = TinNhan.replaceAll("Lo:", str3);
                            Chitiet = TinNhan.split(" ");
                        } else if (TinNhan.substring(0, 5).indexOf("Cang") > -1) {
                            DangGi = "Cang:";
                            TinNhan = TinNhan.replaceAll("Cang:\n", str3);
                            Chitiet = TinNhan.split(" ");
                        } else if (TinNhan.substring(0, 3).indexOf("Xi") > -1) {
                            DangGi = "Xien:";
                            TinNhan = TinNhan.replaceAll("Xien:\n", str3).replaceAll("d:", "0").replaceAll("\n", " ");
                            Chitiet = TinNhan.split(" ");
                        }
                        String ndung = "";
                        if (DangGi != "Xien:") {
                            int k = 0;
                            while (k < Chitiet.length) {
                                String str4 = "x";
                                String DanChiTiet = Chitiet[k].substring(0, Chitiet[k].indexOf(str4));
                                String TienChiTiet2 = Chitiet[k].substring(Chitiet[k].indexOf(str4)).replaceAll(",", str3);
                                String[] str_so = DanChiTiet.split(",");
                                int j = 0;
                                while (true) {
                                    if (j >= str_so.length) {
                                        break;
                                    }
                                    String ndung2 = ndung.replaceAll(",x", str4);
                                    if (ndung2.length() != 0) {
                                        str2 = str4;
                                        if (ndung2.length() + TienChiTiet2.length() + TienChiTiet2.length() < dodai) {
                                            if (j >= str_so.length - 1) {
                                                ndung = ndung2 + str_so[j] + "," + TienChiTiet2 + " ";
                                                break;
                                            }
                                            ndung = ndung2 + str_so[j] + ",";
                                        } else {
                                            if (j > 0) {
                                                ndung2 = ndung2 + TienChiTiet2;
                                            }
                                            Frag_CanChuyen.this.Xulytin(SotinNhan, ndung2, 1);
                                            SotinNhan++;
                                            if (j >= str_so.length - 1) {
                                                ndung = DangGi + "\n" + str_so[j] + "," + TienChiTiet2 + " ";
                                                break;
                                            }
                                            ndung = DangGi + "\n" + str_so[j] + ",";
                                        }
                                    } else {
                                        str2 = str4;
                                        ndung = str_so.length == 1 ? DangGi + "\n" + str_so[j] + "," + TienChiTiet2 + " " : DangGi + "\n" + str_so[j] + ",";
                                    }
                                    j++;
                                    str3 = str3;
                                    str4 = str2;
                                }
                                k++;
                                mDate = mDate;
                                NganDai = NganDai;
                                TienChiTiet = TienChiTiet;
                                TinNhan = TinNhan;
                                str3 = str3;
                            }
                            str = str3;
                            if (ndung.length() > 0) {
                                Frag_CanChuyen.this.Xulytin(SotinNhan, ndung, 1);
                            }
                        } else {
                            str = str3;
                            for (int k2 = 0; k2 < Chitiet.length; k2++) {
                                if (ndung.length() == 0) {
                                    ndung = DangGi + "\n" + Chitiet[k2] + " ";
                                } else if (ndung.length() + Chitiet[k2].length() < dodai) {
                                    ndung = ndung + Chitiet[k2] + " ";
                                } else {
                                    Frag_CanChuyen.this.Xulytin(SotinNhan, ndung, 1);
                                    SotinNhan++;
                                    ndung = DangGi + "\n" + Chitiet[k2] + " ";
                                }
                            }
                            if (ndung.length() > 0) {
                                Frag_CanChuyen.this.Xulytin(SotinNhan, ndung, 1);
                            }
                        }
                        if (getSoTN2 != null) {
                            getSoTN2.close();
                        }
                    }
                    Toast.makeText(Frag_CanChuyen.this.getActivity(), "Đã chuyển tin!", Toast.LENGTH_LONG).show();
                }
                Frag_CanChuyen.this.xemlv();
                Frag_CanChuyen.this.min = 0;
                Frag_CanChuyen.this.max = 100;
                Frag_CanChuyen.this.rangeSeekBar.setSelectedMinValue(0);
                Frag_CanChuyen.this.rangeSeekBar.setSelectedMaxValue(100);
                Frag_CanChuyen.this.edt_tien.setText(str);
            }
        });
        dialog.getWindow().setLayout(-1, -2);
        dialog.setCancelable(true);
        dialog.setTitle("Xem dạng:");
        dialog.show();
    }

    /* JADX WARNING: Removed duplicated region for block: B:48:0x02fb  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0300  */
    /* JADX WARNING: Removed duplicated region for block: B:54:? A[RETURN, SYNTHETIC] */
    public void Xulytin(int SotinNhan, String noidung, int Chuyen) {
        int type_kh;
        final MainActivity mainActivity = new MainActivity();
        String mDate = MainActivity.Get_date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        hourFormat.setTimeZone(TimeZone.getDefault());
        String mGionhan = hourFormat.format(calendar.getTime());
        if (this.mSpiner > -1) {
            Cursor cur1 = this.db.GetData("Select * From tbl_kh_new Where sdt = '" + this.mMobile.get(this.mSpiner) + "'");
            if (cur1.moveToFirst()) {
                if (cur1.getInt(3) == 3) {
                    type_kh = 2;
                } else {
                    type_kh = cur1.getInt(3);
                }
                this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mDate + "', '" + mGionhan + "', " + type_kh + ", '" + this.mContact.get(this.mSpiner) + "', '" + this.mMobile.get(this.mSpiner) + "', 2, " + SotinNhan + ", 'Tin " + SotinNhan + ":\n" + noidung + "',null, '" + noidung + "', 'ko',0, 0, 1, null)");
                Database database = this.db;
                StringBuilder sb = new StringBuilder();
                sb.append("Select id From tbl_tinnhanS WHERE ngay_nhan = '");
                sb.append(mDate);
                sb.append("' AND so_dienthoai = '");
                sb.append(this.mMobile.get(this.mSpiner));
                sb.append("' AND type_kh = 2 AND so_tin_nhan = ");
                sb.append(SotinNhan);
                Cursor c = database.GetData(sb.toString());
                c.moveToFirst();
                if (Congthuc.CheckDate(MainActivity.myDate)) {
                    try {
                        this.db.Update_TinNhanGoc(c.getInt(0), cur1.getInt(3));
                        final String NoiDungTin = "Tin " + SotinNhan + ":\n" + noidung;
                        if (Chuyen == 1) {
                            try {
                                if (cur1.getString(2).indexOf("TL") > -1) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass23 */

                                        public void run() {
                                            MainActivity.sendMessage(Long.parseLong(Frag_CanChuyen.this.mMobile.get(Frag_CanChuyen.this.mSpiner)), NoiDungTin);
                                        }
                                    });
                                    this.db.QueryData("Insert into Chat_database Values( null,'" + mDate + "', '" + mGionhan + "', 2, '" + this.mContact.get(this.mSpiner) + "', '" + this.mMobile.get(this.mSpiner) + "', '" + cur1.getString(2) + "','" + NoiDungTin + "',1)");
                                }
                            } catch (Exception e) {
                                this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                                this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.mSpiner) + "' AND so_tin_nhan = " + SotinNhan);
                                Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                                if (c != null) {
                                }
                                if (cur1 == null) {
                                }
                            }
                        }
                        if (Chuyen == 1) {
                            try {
                                if (cur1.getString(2).indexOf("sms") > -1) {
                                    this.db.SendSMS(this.mMobile.get(this.mSpiner), NoiDungTin);
                                }
                            } catch (Exception e2) {
                                this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                                this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.mSpiner) + "' AND so_tin_nhan = " + SotinNhan);
                                Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                                if (c != null) {
                                }
                                if (cur1 == null) {
                                }
                            }
                        }
                        if (Chuyen == 1 && cur1.getString(2).indexOf("sms") == -1) {
                            new NotificationReader().NotificationWearReader(this.mMobile.get(this.mSpiner), NoiDungTin);
                            this.db.QueryData("Insert into Chat_database Values( null,'" + mDate + "', '" + mGionhan + "', 2, '" + this.mContact.get(this.mSpiner) + "', '" + this.mMobile.get(this.mSpiner) + "', '" + cur1.getString(2) + "','" + NoiDungTin + "',1)");
                        }
                    } catch (Exception e3) {
                        this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                        this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.mSpiner) + "' AND so_tin_nhan = " + SotinNhan);
                        Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                        if (c != null) {
                        }
                        if (cur1 == null) {
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                } else {
                    try {
                        Toast.makeText(getActivity(), "Đã hết hạn sử dụng\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.listKH.getString("k_tra") + " để gia hạn", Toast.LENGTH_LONG).show();
                    } catch (JSONException e4) {
                        e4.printStackTrace();
                    }
                }
                if (c != null) {
                    c.close();
                }
                if (cur1 == null) {
                    cur1.close();
                }
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        xem_RecycView();
        super.onResume();
    }

    public void xem_RecycView() {
        String Noi;
        new MainActivity();
        String mDate = MainActivity.Get_date();
        String str = null;
        this.mSo.clear();
        this.mTienNhan.clear();
        this.mTienOm.clear();
        this.mTienchuyen.clear();
        this.mTienTon.clear();
        this.mNhay.clear();
        String str2 = this.DangXuat;
        if (str2 == "(the_loai = 'deb' or the_loai = 'det')") {
            str = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_deB + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_deB as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So\n Where tbl_soctS.ngay_nhan='" + mDate + "' AND (tbl_soctS.the_loai='deb' OR tbl_soctS.the_loai='det') GROUP by so_om.So Order by " + this.sapxep;
        } else if (str2 == "the_loai = 'lo'") {
            str = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_Lo + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_Lo as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='lo' \n GROUP by so_om.So Order by " + this.sapxep;
        } else if (str2 == "the_loai = 'dea'") {
            str = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_DeA + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_DeA as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='dea' GROUP by so_chon Order by " + this.sapxep;
        } else if (str2 == "the_loai = 'dec'") {
            str = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_DeC + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_DeC as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='dec' GROUP by so_chon Order by " + this.sapxep;
        } else if (str2 == "the_loai = 'ded'") {
            str = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_DeD + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_DeD as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='ded' GROUP by so_chon Order by " + this.sapxep;
        } else if (str2 == "the_loai = 'xi'") {
            if (this.lay_x2 == "" && this.lay_x3 == "" && this.lay_x4 == "") {
                Noi = "";
            } else {
                Noi = (" And (" + this.lay_x2 + this.lay_x3 + this.lay_x4 + ")").replaceAll("\\(OR", "(");
            }
            Cursor c1 = this.db.GetData("Select * From So_om WHERE ID = 1");
            c1.moveToFirst();
            str = "SELECT so_chon, sum((type_kh =1)*(100-diem_khachgiu)*diem_quydoi)/100 AS diem, ((length(so_chon) = 5) * " + c1.getString(7) + " +(length(so_chon) = 8) * " + c1.getString(8) + " +(length(so_chon) = 11) * " + c1.getString(9) + " + sum(diem_dly_giu*diem_quydoi/100)) AS Om, SUm((type_kh =2)*diem) as chuyen , SUm((type_kh =1)*(100-diem_khachgiu-diem_dly_giu)*diem_quydoi/100)-SUm((type_kh =2)*diem) -  ((length(so_chon) = 5) * " + c1.getString(7) + " +(length(so_chon) = 8) * " + c1.getString(8) + " +(length(so_chon) = 11) * " + c1.getString(9) + ") AS ton, so_nhay   From tbl_soctS Where ngay_nhan='" + mDate + "' AND the_loai='xi'" + Noi + "  GROUP by so_chon Order by ton DESC, diem DESC";
            if (c1 != null && !c1.isClosed()) {
                c1.close();
            }
        } else if (str2 == "the_loai = 'bc'") {
            Cursor c12 = this.db.GetData("Select * From So_om WHERE ID = 1");
            c12.moveToFirst();
            if (c12.getInt(10) == 1) {
                this.db.QueryData("Update so_om set om_bc=0 WHERE id = 1");
                c12 = this.db.GetData("Select * From So_om WHERE ID = 1");
                c12.moveToFirst();
            }
            str = "SELECT so_chon, sum((type_kh = 1)*(100-diem_khachgiu)*diem_quydoi/100) AS diem, " + c12.getString(10) + " + sum(diem_dly_giu*diem_quydoi)/100 AS Om, SUm((type_kh = 2)*diem) as Chuyen, sum((type_kh =1)*(100-diem_khachgiu-diem_dly_giu)*diem_quydoi/100) - sum((type_kh =2)*diem) -" + c12.getString(10) + " AS ton, so_nhay   From tbl_soctS Where ngay_nhan='" + mDate + "' AND the_loai='bc' GROUP by so_chon Order by ton DESC, diem DESC";
            if (c12 != null && !c12.isClosed()) {
                c12.close();
            }
        } else if (str2 == "the_loai = 'xn'") {
            str = "SELECT so_chon, sum((type_kh =1)*(diem_quydoi)) AS diem, sum(tbl_soctS.diem_dly_giu) AS Om, SUm((type_kh =2)*diem) as chuyen , SUm((type_kh =1)*diem_ton-(type_kh =2)*diem_ton) AS ton, so_nhay   From tbl_soctS Where ngay_nhan='" + mDate + "' AND the_loai='xn' GROUP by so_chon Order by ton DESC, diem DESC";
        }
        Cursor cursor = this.db.GetData(str);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                this.mSo.add(cursor.getString(0));
                this.mTienNhan.add(decimalFormat.format((long) cursor.getInt(1)));
                this.mTienOm.add(decimalFormat.format((long) cursor.getInt(2)));
                this.mTienchuyen.add(decimalFormat.format((long) cursor.getInt(3)));
                this.mTienTon.add(decimalFormat.format((long) cursor.getInt(4)));
                this.mNhay.add(Integer.valueOf(cursor.getInt(5)));
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        if (getActivity() != null) {
            this.no_rp_number.setAdapter((ListAdapter) new So_OmAdapter(getActivity(), R.layout.frag_canchuyen_lv, this.mSo));
        }
    }

    /* access modifiers changed from: package-private */
    public class So_OmAdapter extends ArrayAdapter {
        public So_OmAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        class ViewHolder {
            TextView tview1;
            TextView tview2;
            TextView tview4;
            TextView tview5;
            TextView tview7;
            TextView tview8;

            ViewHolder() {
            }
        }

        public View getView(int position, View view, ViewGroup parent) {
            @SuppressLint("WrongConstant") LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            ViewHolder holder = new ViewHolder();
            if (view == null) {
                view = inflater.inflate(R.layout.frag_canchuyen_lv, (ViewGroup) null);
                holder.tview5 = (TextView) view.findViewById(R.id.Tv_so);
                holder.tview7 = (TextView) view.findViewById(R.id.tv_diemNhan);
                holder.tview8 = (TextView) view.findViewById(R.id.tv_diemOm);
                holder.tview1 = (TextView) view.findViewById(R.id.tv_diemChuyen);
                holder.tview4 = (TextView) view.findViewById(R.id.tv_diemTon);
                holder.tview2 = (TextView) view.findViewById(R.id.stt);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (Frag_CanChuyen.this.mNhay.get(position).intValue() > 0) {
                holder.tview5.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tview7.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tview8.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tview1.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tview4.setTextColor(SupportMenu.CATEGORY_MASK);
                if (Frag_CanChuyen.this.mNhay.get(position).intValue() == 1) {
                    TextView textView = holder.tview5;
                    textView.setText(Frag_CanChuyen.this.mSo.get(position) + "*");
                } else if (Frag_CanChuyen.this.mNhay.get(position).intValue() == 2) {
                    TextView textView2 = holder.tview5;
                    textView2.setText(Frag_CanChuyen.this.mSo.get(position) + "**");
                } else if (Frag_CanChuyen.this.mNhay.get(position).intValue() == 3) {
                    TextView textView3 = holder.tview5;
                    textView3.setText(Frag_CanChuyen.this.mSo.get(position) + "***");
                } else if (Frag_CanChuyen.this.mNhay.get(position).intValue() == 4) {
                    TextView textView4 = holder.tview5;
                    textView4.setText(Frag_CanChuyen.this.mSo.get(position) + "****");
                } else if (Frag_CanChuyen.this.mNhay.get(position).intValue() == 5) {
                    TextView textView5 = holder.tview5;
                    textView5.setText(Frag_CanChuyen.this.mSo.get(position) + "*****");
                } else if (Frag_CanChuyen.this.mNhay.get(position).intValue() == 6) {
                    TextView textView6 = holder.tview5;
                    textView6.setText(Frag_CanChuyen.this.mSo.get(position) + "******");
                }
                holder.tview7.setText(Frag_CanChuyen.this.mTienNhan.get(position));
                holder.tview8.setText(Frag_CanChuyen.this.mTienOm.get(position));
                holder.tview1.setText(Frag_CanChuyen.this.mTienchuyen.get(position));
                holder.tview4.setText(Frag_CanChuyen.this.mTienTon.get(position));
                TextView textView7 = holder.tview2;
                textView7.setText((position + 1) + "");
            } else {
                holder.tview5.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tview7.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tview8.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tview1.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tview4.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tview5.setText(Frag_CanChuyen.this.mSo.get(position));
                holder.tview7.setText(Frag_CanChuyen.this.mTienNhan.get(position));
                holder.tview8.setText(Frag_CanChuyen.this.mTienOm.get(position));
                holder.tview1.setText(Frag_CanChuyen.this.mTienchuyen.get(position));
                holder.tview4.setText(Frag_CanChuyen.this.mTienTon.get(position));
                TextView textView8 = holder.tview2;
                textView8.setText((position + 1) + "");
            }
            return view;
        }
    }
}