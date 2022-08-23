package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationReader;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.RangeSeekBar;
import tamhoang.ldpro4.data.BriteDb;
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
    RadioButton radio_loa;
    RadioButton radio_xi;
    RangeSeekBar<Integer> rangeSeekBar;
    private Runnable runnable = new Runnable() {
        /* class tamhoang.ldpro4.Fragment.Frag_CanChuyen.AnonymousClass16 */

        public void run() {
            new MainActivity();
            if (MainActivity.sms) {
                xemlv();
                MainActivity.sms = false;
            }
            handler.postDelayed(this, 1000);
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
        this.radio_loa = (RadioButton) this.v.findViewById(R.id.radio_loa);
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
            min = minValue.intValue();
            max = maxValue.intValue();
        });
        this.radio_de.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (radio_de.isChecked()) {
                layout.setVisibility(View.VISIBLE);
                new MainActivity();
                String mDate = MainActivity.Get_date();
                try {
                    Database database = db;
                    Cursor cursor = database.GetData("Select sum((the_loai = 'dea')* diem) as de_a\n,sum((the_loai = 'deb')* diem) as de_b\n,sum((the_loai = 'det')* diem) as de_t\n,sum((the_loai = 'dec')* diem) as de_c\n,sum((the_loai = 'ded')* diem) as de_d\nFrom tbl_soctS \nWhere ngay_nhan = '" + mDate + "'");
                    if (!cursor.moveToFirst() || cursor == null) {
                        DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                        return;
                    }
                    int[] dem = new int[5];
                    if (cursor.getDouble(0) > 0.0d) {
                        dem[0] = 1;
                        radio_dea.setEnabled(true);
                    } else {
                        dem[0] = 0;
                        radio_dea.setEnabled(false);
                    }
                    if (cursor.getDouble(1) > 0.0d) {
                        dem[1] = 1;
                        radio_deb.setEnabled(true);
                    } else {
                        dem[1] = 0;
                        radio_deb.setEnabled(false);
                    }
                    if (cursor.getDouble(2) > 0.0d) {
                        dem[2] = 1;
                    } else {
                        dem[2] = 0;
                    }
                    if (cursor.getDouble(3) > 0.0d) {
                        dem[3] = 1;
                        radio_dec.setEnabled(true);
                    } else {
                        dem[3] = 0;
                        radio_dec.setEnabled(false);
                    }
                    if (cursor.getDouble(4) > 0.0d) {
                        dem[4] = 1;
                        radio_ded.setEnabled(true);
                    } else {
                        dem[4] = 0;
                        radio_ded.setEnabled(false);
                    }
                    if (dem[0] == 0 && ((dem[1] == 1 || dem[2] == 1) && dem[3] == 0 && dem[4] == 0)) {
                        DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                        ln_xi.setVisibility(View.GONE);
                        li_loaide.setVisibility(View.GONE);
                        radio_deb.setChecked(true);
                        xem_RecycView();
                    } else if (dem[0] == 0 && dem[1] == 0 && dem[2] == 0 && dem[3] == 0 && dem[4] == 0) {
                        DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                        ln_xi.setVisibility(View.GONE);
                        li_loaide.setVisibility(View.GONE);
                        radio_deb.setChecked(true);
                        xem_RecycView();
                    } else {
                        DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                        ln_xi.setVisibility(View.GONE);
                        li_loaide.setVisibility(View.VISIBLE);
                        radio_deb.setChecked(true);
                        xem_RecycView();
                    }
                    if (!cursor.isClosed() && cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                } catch (SQLException e) {
                    DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                }
            }
        });
        this.radio_dea.setOnCheckedChangeListener((compoundButton, b) -> {
            if (radio_dea.isChecked()) {
                DangXuat = "the_loai = 'dea'";
                ln_xi.setVisibility(View.GONE);
                xem_RecycView();
            }
        });
        this.radio_deb.setOnCheckedChangeListener((compoundButton, b) -> {
            if (radio_deb.isChecked()) {
                DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                ln_xi.setVisibility(View.GONE);
                xem_RecycView();
            }
        });
        this.radio_dec.setOnCheckedChangeListener((compoundButton, b) -> {
            if (radio_dec.isChecked()) {
                DangXuat = "the_loai = 'dec'";
                ln_xi.setVisibility(View.GONE);
                xem_RecycView();
            }
        });
        this.radio_ded.setOnCheckedChangeListener((compoundButton, b) -> {
            if (radio_ded.isChecked()) {
                DangXuat = "the_loai = 'ded'";
                ln_xi.setVisibility(View.GONE);
                xem_RecycView();
            }
        });
        this.radio_lo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (radio_lo.isChecked()) {
                DangXuat = "the_loai = 'lo'";
                ln_xi.setVisibility(View.GONE);
                li_loaide.setVisibility(View.GONE);
                xem_RecycView();
            }
        });

        this.radio_loa.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (radio_loa.isChecked()) {
                DangXuat = "the_loai = 'loa'";
                ln_xi.setVisibility(View.GONE);
                li_loaide.setVisibility(View.GONE);
                xem_RecycView();
            }
        });
        this.radio_xi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (radio_xi.isChecked()) {
                new MainActivity();
                DangXuat = "the_loai = 'xi'";
                layout.setVisibility(View.GONE);
                ln_xi.setVisibility(View.VISIBLE);
                li_loaide.setVisibility(View.GONE);
                try {
                    Database database = db;
                    Cursor cursor = database.GetData("Select count(id) From tbl_soctS WHERE the_loai = 'xn' AND ngay_nhan = '" + MainActivity.Get_date() + "'");
                    if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
                        check_xn.setVisibility(View.VISIBLE);
                    }
                    xem_RecycView();
                } catch (SQLException e) {
                }
            }
        });
        this.radio_bc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (radio_bc.isChecked()) {
                DangXuat = "the_loai = 'bc'";
                layout.setVisibility(View.GONE);
                ln_xi.setVisibility(View.GONE);
                li_loaide.setVisibility(View.GONE);
                xem_RecycView();
            }
        });
        this.check_x2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (check_x2.isChecked()) {
                DangXuat = "the_loai = 'xi'";
                lay_x2 = "length(so_chon) = 5 ";
                check_xn.setChecked(false);
            } else {
                lay_x2 = "";
            }
            xem_RecycView();
        });
        this.check_x3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (check_x3.isChecked()) {
                DangXuat = "the_loai = 'xi'";
                lay_x3 = "OR length(so_chon) = 8 ";
                check_xn.setChecked(false);
            } else {
                lay_x3 = "";
            }
            xem_RecycView();
        });
        this.check_x4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (check_x4.isChecked()) {
                DangXuat = "the_loai = 'xi'";
                lay_x4 = "OR length(so_chon) = 11 ";
                check_xn.setChecked(false);
            } else {
                lay_x4 = "";
            }
            xem_RecycView();
        });
        this.check_xn.setOnClickListener(v -> {
            if (check_xn.isChecked()) {
                DangXuat = "the_loai = 'xn'";
                check_x2.setChecked(false);
                check_x3.setChecked(false);
                check_x4.setChecked(false);
                xem_RecycView();
            }
        });
        this.btn_Xuatso.setOnClickListener(v -> {
            if (Congthuc.isNumeric(edt_tien.getText().toString().replaceAll("%", "").replaceAll("n", "").replaceAll("k", "")
                    .replaceAll("d", "").replaceAll(">", "").replaceAll("\\.", "")) || edt_tien.getText().toString().length() == 0) {
                btn_click();
            } else {
                Toast.makeText(getActivity(), "Kiểm tra lại tiền!", Toast.LENGTH_LONG).show();
            }
        });
        this.lay_x2 = "length(so_chon) = 5 ";
        this.lay_x3 = "OR length(so_chon) = 8 ";
        this.lay_x4 = "OR length(so_chon) = 11 ";
        this.no_rp_number.setOnItemClickListener((adapterView, view, position, id) -> {
            try {
                Cursor c = db.GetData("Select ten_kh, sum(diem_quydoi) From tbl_soctS WHERE so_chon = '" + mSo.get(position)
                        + "' AND ngay_nhan = '" + MainActivity.Get_date() + "' AND type_kh = 1 AND " + DangXuat + " GROUP BY so_dienthoai");
                String s1 = "";
                while (c.moveToNext()) {
                    s1 = s1 + c.getString(0) + ": " + c.getString(1) + "\n";
                }
                Toast.makeText(getActivity(), s1, Toast.LENGTH_LONG).show();
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
        int MaxTien;
        int MaxTien2;
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
        if (mDate2.contains(curDate)) {
//            if (this.edt_tien.getText().toString().length() != 0) {
//                if (this.edt_tien.getText().toString() != "0") {
                    String str3 = this.edt_tien.getText().toString().replaceAll("%", "").replaceAll("n", "").replaceAll("k", "").replaceAll("d", "").replaceAll(">", "").replaceAll("\\.", "").replaceAll(",", "");
                    if (Congthuc.isNumeric(str3)) {
                        TienChuyen = Integer.parseInt(str3);
                    } else {
                        TienChuyen = 0;
                    }
                    switch (DangXuat) {
                        case "the_loai = 'xi'":
                            this.xuatDan = "Xien:\n";
                            int i = this.min;
                            while (i < this.mSo.size()) {
                                if (this.edt_tien.getText().toString().contains("%")) {
                                    MaxTien3 = (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) / mLamtron) * mLamtron;
                                } else if (this.edt_tien.getText().toString().contains(">")) {
                                    MaxTien3 = (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) / mLamtron) * mLamtron;
                                } else if (TienChuyen == 0) {
                                    MaxTien3 = (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) / mLamtron) * mLamtron;
                                } else if (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) > TienChuyen) {
                                    MaxTien3 = (TienChuyen / mLamtron) * mLamtron;
                                } else {
                                    MaxTien3 = (Integer.parseInt(this.mTienTon.get(i).replace(".", "")) / mLamtron) * mLamtron;
                                }
                                if (this.edt_tien.getText().toString().contains("%")) {
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
                                } else if (this.edt_tien.getText().toString().contains(">")) {
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
                                } else if (!this.edt_tien.getText().toString().contains(">") && !this.edt_tien.getText().toString().contains("%") && MaxTien3 > 0) {
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
                            }
                            break;
                        case "the_loai = 'bc'":
                            this.xuatDan = "Cang:\n";
                            int i2 = this.min;
                            int tien = 0;
                            while (i2 < this.mSo.size()) {
                                if (TienChuyen == 0) {
                                    MaxTien2 = (Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) / mLamtron) * mLamtron;
                                } else if (this.edt_tien.getText().toString().contains("%")) {
                                    MaxTien2 = (((Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) * TienChuyen) / mLamtron) / 100) * mLamtron;
                                } else if (this.edt_tien.getText().toString().contains(">")) {
                                    MaxTien2 = ((Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) - TienChuyen) / mLamtron) * mLamtron;
                                } else if (Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) > TienChuyen) {
                                    MaxTien2 = (TienChuyen / mLamtron) * mLamtron;
                                } else {
                                    MaxTien2 = (Integer.parseInt(this.mTienTon.get(i2).replace(".", "")) / mLamtron) * mLamtron;
                                }
                                if (MaxTien2 <= 0) {

                                } else if (tien > MaxTien2) {
                                    this.xuatDan += "x" + tien + "n " + this.mSo.get(i2) + ",";
                                    tien = MaxTien2;
                                } else {
                                    this.xuatDan += this.mSo.get(i2) + ",";
                                    tien = MaxTien2;
                                }
                                i2++;
                            }
                            if (this.xuatDan.length() > 4) {
                                this.xuatDan += "x" + tien + "n";
                            }
                            if (this.xuatDan.contains(":")) {
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
                        case "the_loai = 'xn'":
                            this.xuatDan = "Xnhay:\n";
                            for (int i3 = this.min; i3 < this.mSo.size(); i3++) {
                                if (this.edt_tien.getText().toString().contains("%")) {
                                    MaxTien = (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) / mLamtron) * mLamtron;
                                } else if (this.edt_tien.getText().toString().contains(">")) {
                                    MaxTien = (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) / mLamtron) * mLamtron;
                                } else if (TienChuyen == 0) {
                                    MaxTien = (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) / mLamtron) * mLamtron;
                                } else if (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) > TienChuyen) {
                                    MaxTien = (TienChuyen / mLamtron) * mLamtron;
                                } else {
                                    MaxTien = (Integer.parseInt(this.mTienTon.get(i3).replace(".", "")) / mLamtron) * mLamtron;
                                }
                                if (this.edt_tien.getText().toString().contains("%")) {
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
                                } else if (this.edt_tien.getText().toString().contains(">")) {
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
                            break;

                        case "(the_loai = 'deb' or the_loai = 'det')":
                            this.xuatDan = this.db.XuatDanTon2("deb", this.edt_tien.getText().toString(), this.min, this.max);
                            break;
                        case "the_loai = 'dea'":
                            this.xuatDan = this.db.XuatDanTon2("dea", this.edt_tien.getText().toString(), this.min, this.max);
                            break;
                        case "the_loai = 'dec'":
                            this.xuatDan = this.db.XuatDanTon2("dec", this.edt_tien.getText().toString(), this.min, this.max);
                            break;
                        case "the_loai = 'ded'":
                            this.xuatDan = this.db.XuatDanTon2("ded", this.edt_tien.getText().toString(), this.min, this.max);
                            break;
                        case "the_loai = 'loa'":
                            this.xuatDan = this.db.XuatDanTon2("loa", this.edt_tien.getText().toString(), this.min, this.max);
                            break;
                        case "the_loai = 'lo'":
                            this.xuatDan = this.db.XuatDanTon2("lo", this.edt_tien.getText().toString(), this.min, this.max);
                            break;
                        default:
                            Toast.makeText(getActivity(), "Không có số liệu!", Toast.LENGTH_LONG).show();
                            break;
                    }
            switch (DangXuat) {
                    }
                    Dialog(1);
//                    Toast.makeText(getActivity(), "Không có số liệu!", Toast.LENGTH_LONG).show();
                    return;
//                }
//            }
//            Toast.makeText(getActivity(), "Không có số liệu!", Toast.LENGTH_LONG).show();
//            return;
        }
        Toast.makeText(getActivity(), "Không làm việc với dữ liệu ngày cũ!", Toast.LENGTH_LONG).show();
    }

    public String TaoTinDe(String ten_kh) {
        Throwable th;
        String str = "Se_chuyen";
        JSONObject jSon = new JSONObject();
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
//                if (!cursor.isClosed()) {
//                }
                try {
                    throw th;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return str;
    }

    public String TaoTinLo(String ten_kh) throws Throwable {
        Throwable th;
        String str = "Se_chuyen";
        double maxDang = 0.0d;
        JSONObject jSon = new JSONObject();
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
//                if (!cursor.isClosed()) {
//                }
                throw th;
            }
        }
        return str;
    }

    public String TaoTinCang(String ten_kh) throws Throwable {
        Throwable th;
        String str = "Se_chuyen";
        JSONObject jSon = new JSONObject();
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
//                if (!cursor.isClosed()) {
//                }
                throw th;
            }
        }
        return str;
    }

    public String TaoTinXi(String ten_kh) throws Throwable {
        Throwable th;
        String str = "xien4";
        String str2 = "xien3";
        String str3 = "xien2";
        JSONObject jSon = new JSONObject();
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
//                if (!cursor.isClosed()) {
//                }
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
        Spinner sprin_tenkhach = dialog.findViewById(R.id.sprin_tenkhach);
        final EditText edt_XuatDan = dialog.findViewById(R.id.edt_XuatDan);
        Button btn_chuyendi = dialog.findViewById(R.id.btn_chuyendi);
        edt_XuatDan.setText("");
        edt_XuatDan.setText(this.xuatDan.replaceAll(",x", "x"));
        try {
            Cursor cur = this.db.GetData("Select * From tbl_kh_new WHERE type_kh <> 1 ORDER BY ten_kh");
            this.mContact.clear();
            this.mMobile.clear();
            this.mKhongMax.clear();
            this.mAppuse.clear();
            while (cur.moveToNext()) {
                if (!cur.getString(2).contains("sms")) {
                    if (!cur.getString(2).contains("TL")) {
                        if (MainActivity.contactsMap.containsKey(cur.getString(1))) {
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
            if (!cur.isClosed()) {
                cur.close();
            }
            sprin_tenkhach.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_item, this.mContact));
        } catch (SQLException e) {
            System.out.println("Quangbx: " + e);
        }
        sprin_tenkhach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mSpiner = position;
                try {
                    jsonKhongmax = new JSONObject(mKhongMax.get(mSpiner));
                    if (radio_deb.isChecked() && radio_de.isChecked()
                            && jsonKhongmax.getString("danDe").length() > 0) {
                        edt_XuatDan.setText(TaoTinDe(mContact.get(mSpiner)));
                    } else if (radio_lo.isChecked() && jsonKhongmax.getString("danLo").length() > 0) {
                        edt_XuatDan.setText(TaoTinLo(mContact.get(mSpiner)));
                    } else if (radio_xi.isChecked() && (jsonKhongmax.getInt("xien2") > 0
                            || jsonKhongmax.getInt("xien3") > 0
                            || jsonKhongmax.getInt("xien4") > 0)) {
                        edt_XuatDan.setText(TaoTinXi(mContact.get(mSpiner)));
                    } else if (!radio_bc.isChecked()
                            || jsonKhongmax.getInt("cang") <= 0) {
                        edt_XuatDan.setText(Chuyendi);
                    } else {
                        edt_XuatDan.setText(TaoTinCang(mContact.get(mSpiner)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    System.out.println("Quangbx " + throwable);
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        btn_chuyendi.setOnClickListener(v -> {
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
            if (mMobile.size() <= 0 || edt_XuatDan.getText().toString().length() <= 0 || Dachuyen) {
                str = str3;
                if (edt_XuatDan.getText().toString().length() != 0) {
                    if (Dachuyen) {
                        dialog.cancel();
                    } else {
                        Toast.makeText(getActivity(), "Chưa có chủ để chuyển tin!", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Dachuyen = true;
                String TinNhan = edt_XuatDan.getText().toString().replaceAll("'", " ").trim();
                edt_XuatDan.setText(str3);
                dialog.dismiss();
                if (TinNhan.trim().length() < dodai) {
                    int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mDate, 2, "so_dienthoai = '"+ mMobile.get(mSpiner) +"'");

                    Xulytin(maxSoTn + 1, TinNhan.replaceAll("'", " ").trim(), 1);
                    str = str3;
                } else {
                    int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mDate, 2, "so_dienthoai = '"+ mMobile.get(mSpiner) +"'");

                    int SotinNhan = maxSoTn + 1;
                    String DangGi = "";
                    String[] Chitiet = null;
                    if (TinNhan.substring(0, 3).contains("De")) {
                        DangGi = "De:";
                        TinNhan = TinNhan.replaceAll("De:", str3);
                        Chitiet = TinNhan.split(" ");
                    } else if (TinNhan.substring(0, 3).contains("Lo")) {
                        DangGi = "Lo:";
                        TinNhan = TinNhan.replaceAll("Lo:", str3);
                        Chitiet = TinNhan.split(" ");
                    } else if (TinNhan.substring(0, 5).contains("Cang")) {
                        DangGi = "Cang:";
                        TinNhan = TinNhan.replaceAll("Cang:\n", str3);
                        Chitiet = TinNhan.split(" ");
                    } else {
                        if (!TinNhan.substring(0, 3).contains("Xi")) {
                        } else {
                            DangGi = "Xien:";
                            TinNhan = TinNhan.replaceAll("Xien:\n", str3).replaceAll("d:", "0").replaceAll("\n", " ");
                            Chitiet = TinNhan.split(" ");
                        }
                    }
                    StringBuilder ndung = new StringBuilder();
                    if (!DangGi.equals("Xien:")) {
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
                                String ndung2 = ndung.toString().replaceAll(",x", str4);
                                if (ndung2.length() != 0) {
                                    str2 = str4;
                                    if (ndung2.length() + TienChiTiet2.length() + TienChiTiet2.length() < dodai) {
                                        if (j >= str_so.length - 1) {
                                            ndung = new StringBuilder(ndung2 + str_so[j] + "," + TienChiTiet2 + " ");
                                            break;
                                        }
                                        ndung = new StringBuilder(ndung2 + str_so[j] + ",");
                                    } else {
                                        if (j > 0) {
                                            ndung2 = ndung2 + TienChiTiet2;
                                        }
                                        Xulytin(SotinNhan, ndung2, 1);
                                        SotinNhan++;
                                        if (j >= str_so.length - 1) {
                                            ndung = new StringBuilder(DangGi + "\n" + str_so[j] + "," + TienChiTiet2 + " ");
                                            break;
                                        }
                                        ndung = new StringBuilder(DangGi + "\n" + str_so[j] + ",");
                                    }
                                } else {
                                    str2 = str4;
                                    ndung = new StringBuilder(str_so.length == 1 ? DangGi + "\n" + str_so[j] + "," + TienChiTiet2 + " " : DangGi + "\n" + str_so[j] + ",");
                                }
                                j++;
                                str4 = str2;
                            }
                            k++;
                        }
                        str = str3;
                    } else {
                        str = str3;
                        for (String s : Chitiet) {
                            if (ndung.length() == 0) {
                                ndung = new StringBuilder(DangGi + "\n" + s + " ");
                            } else if (ndung.length() + s.length() < dodai) {
                                ndung.append(s).append(" ");
                            } else {
                                Xulytin(SotinNhan, ndung.toString(), 1);
                                SotinNhan++;
                                ndung = new StringBuilder(DangGi + "\n" + s + " ");
                            }
                        }
                    }
                    if (ndung.length() > 0) {
                        Xulytin(SotinNhan, ndung.toString(), 1);
                    }
                }
                Toast.makeText(getActivity(), "Đã chuyển tin!", Toast.LENGTH_LONG).show();
            }
            xemlv();
            min = 0;
            max = 100;
            rangeSeekBar.setSelectedMinValue(0);
            rangeSeekBar.setSelectedMaxValue(100);
            edt_tien.setText(str);
        });
        dialog.getWindow().setLayout(-1, -2);
        dialog.setCancelable(true);
        dialog.setTitle("Xem dạng:");
        dialog.show();
    }

    public void Xulytin(int SotinNhan, String noidung, int Chuyen) {
        int type_kh;
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
                String sb = "Select id From tbl_tinnhanS WHERE ngay_nhan = '" + mDate +
                        "' AND so_dienthoai = '" + this.mMobile.get(this.mSpiner) +
                        "' AND type_kh = 2 AND so_tin_nhan = " + SotinNhan;
                Cursor c = database.GetData(sb);
                c.moveToFirst();
                if (Congthuc.CheckDate(MainActivity.hanSuDung)) {
                    try {
                        this.db.Update_TinNhanGoc(c.getInt(0), cur1.getInt(3));
                        final String NoiDungTin = "Tin " + SotinNhan + ":\n" + noidung;
                        if (Chuyen == 1) {
                            try {
                                if (cur1.getString(2).contains("TL")) {
                                    new Handler(Looper.getMainLooper()).post(() -> MainActivity.sendMessage(Long.parseLong(mMobile.get(mSpiner)), NoiDungTin));
                                    this.db.QueryData("Insert into Chat_database Values( null,'" + mDate + "', '" + mGionhan + "', 2, '" + this.mContact.get(this.mSpiner) + "', '" + this.mMobile.get(this.mSpiner) + "', '" + cur1.getString(2) + "','" + NoiDungTin + "',1)");
                                }
                            } catch (Exception e) {
                                this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                                this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.mSpiner) + "' AND so_tin_nhan = " + SotinNhan);
                                Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();

                            }
                        }
                        if (Chuyen == 1) {
                            try {
                                if (cur1.getString(2).contains("sms")) {
                                    this.db.SendSMS(this.mMobile.get(this.mSpiner), NoiDungTin);
                                }
                            } catch (Exception e2) {
                                this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                                this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.mSpiner) + "' AND so_tin_nhan = " + SotinNhan);
                                Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();

                            }
                        }
                        if (Chuyen == 1 && !cur1.getString(2).contains("sms")) {
                            new NotificationReader().NotificationWearReader(this.mMobile.get(this.mSpiner), NoiDungTin);
                            this.db.QueryData("Insert into Chat_database Values( null,'" + mDate + "', '" + mGionhan + "', 2, '" + this.mContact.get(this.mSpiner) + "', '" + this.mMobile.get(this.mSpiner) + "', '" + cur1.getString(2) + "','" + NoiDungTin + "',1)");
                        }
                    } catch (Exception e3) {
                        this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                        this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.mSpiner) + "' AND so_tin_nhan = " + SotinNhan);
                        Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                } else {
                    try {
                        Toast.makeText(getActivity(), "Đã hết hạn sử dụng\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn", Toast.LENGTH_LONG).show();
                    } catch (JSONException e4) {
                        e4.printStackTrace();
                    }
                }
                c.close();
                cur1.close();
            }
        }
    }

    @Override
    public void onResume() {
        xem_RecycView();
        super.onResume();
    }

    public void xem_RecycView() {
        String Noi;
        String mDate = MainActivity.Get_date();
        String query = null;
        this.mSo.clear();
        this.mTienNhan.clear();
        this.mTienOm.clear();
        this.mTienchuyen.clear();
        this.mTienTon.clear();
        this.mNhay.clear();
        switch (DangXuat) {
            case "(the_loai = 'deb' or the_loai = 'det')":
                query = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_deB + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_deB as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So\n Where tbl_soctS.ngay_nhan='" + mDate + "' AND (tbl_soctS.the_loai='deb' OR tbl_soctS.the_loai='det') GROUP by so_om.So Order by " + this.sapxep;
                break;
            case "the_loai = 'lo'":
                query = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_Lo + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_Lo as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='lo' \n GROUP by so_om.So Order by " + this.sapxep;
                break;
            case "the_loai = 'loa'":
                query = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_Lo + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_Lo as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='loa' \n GROUP by so_om.So Order by " + this.sapxep;
                break;
            case "the_loai = 'dea'":
                query = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_DeA + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_DeA as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='dea' GROUP by so_chon Order by " + this.sapxep;
                break;
            case "the_loai = 'dec'":
                query = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_DeC + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_DeC as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='dec' GROUP by so_chon Order by " + this.sapxep;
                break;
            case "the_loai = 'ded'":
                query = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_DeD + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_DeD as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='ded' GROUP by so_chon Order by " + this.sapxep;
                break;
            case "the_loai = 'xi'":
                if (this.lay_x2.equals("") && this.lay_x3.equals("") && this.lay_x4.equals(""))
                    Noi = "";
                else
                    Noi = (" And (" + this.lay_x2 + this.lay_x3 + this.lay_x4 + ")").replaceAll("\\(OR", "(");

                Cursor c1 = this.db.GetData("Select * From So_om WHERE ID = 1");
                c1.moveToFirst();
                query = "SELECT so_chon, sum((type_kh =1)*(100-diem_khachgiu)*diem_quydoi)/100 AS diem, ((length(so_chon) = 5) * " + c1.getString(7) + " +(length(so_chon) = 8) * " + c1.getString(8) + " +(length(so_chon) = 11) * " + c1.getString(9) + " + sum(diem_dly_giu*diem_quydoi/100)) AS Om, SUm((type_kh =2)*diem) as chuyen , SUm((type_kh =1)*(100-diem_khachgiu-diem_dly_giu)*diem_quydoi/100)-SUm((type_kh =2)*diem) -  ((length(so_chon) = 5) * " + c1.getString(7) + " +(length(so_chon) = 8) * " + c1.getString(8) + " +(length(so_chon) = 11) * " + c1.getString(9) + ") AS ton, so_nhay   From tbl_soctS Where ngay_nhan='" + mDate + "' AND the_loai='xi'" + Noi + "  GROUP by so_chon Order by ton DESC, diem DESC";
                if (!c1.isClosed()) c1.close();

                break;
            case "the_loai = 'bc'":
                Cursor c12 = this.db.GetData("Select * From So_om WHERE ID = 1");
                c12.moveToFirst();
                if (c12.getInt(10) == 1) {
                    this.db.QueryData("Update so_om set om_bc=0 WHERE id = 1");
                    c12 = this.db.GetData("Select * From So_om WHERE ID = 1");
                    c12.moveToFirst();
                }
                query = "SELECT so_chon, sum((type_kh = 1)*(100-diem_khachgiu)*diem_quydoi/100) AS diem, " + c12.getString(10) + " + sum(diem_dly_giu*diem_quydoi)/100 AS Om, SUm((type_kh = 2)*diem) as Chuyen, sum((type_kh =1)*(100-diem_khachgiu-diem_dly_giu)*diem_quydoi/100) - sum((type_kh =2)*diem) -" + c12.getString(10) + " AS ton, so_nhay   From tbl_soctS Where ngay_nhan='" + mDate + "' AND the_loai='bc' GROUP by so_chon Order by ton DESC, diem DESC";
                if (!c12.isClosed()) {
                    c12.close();
                }
                break;
            case "the_loai = 'xn'":
                query = "SELECT so_chon, sum((type_kh =1)*(diem_quydoi)) AS diem, sum(tbl_soctS.diem_dly_giu) AS Om, SUm((type_kh =2)*diem) as chuyen " +
                        ", SUm((type_kh =1)*diem_ton-(type_kh =2)*diem_ton) AS ton, so_nhay From tbl_soctS " +
                        "Where ngay_nhan='" + mDate + "' AND the_loai='xn' GROUP by so_chon Order by ton DESC, diem DESC";
                break;
        }
        Cursor cursor = this.db.GetData(query);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                this.mSo.add(cursor.getString(0));//tbl_soctS.So_chon

                this.mTienNhan.add(decimalFormat.format(cursor.getInt(1)));//Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem
                this.mTienOm.add(decimalFormat.format(cursor.getInt(2)));//so_om.Om_deB + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om
                this.mTienchuyen.add(decimalFormat.format(cursor.getInt(3)));//Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen
                this.mTienTon.add(decimalFormat.format(cursor.getInt(4)));//Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_deB as ton
                this.mNhay.add(cursor.getInt(5));//so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So

                //Where tbl_soctS.ngay_nhan='" + mDate + "' AND (tbl_soctS.the_loai='deb' OR tbl_soctS.the_loai='det') GROUP by so_om.So Order by " + this.sapxep;
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        if (getActivity() != null) {
            this.no_rp_number.setAdapter((ListAdapter) new So_OmAdapter(getActivity(), R.layout.frag_canchuyen_lv, this.mSo));
        }
    }

    public class So_OmAdapter extends ArrayAdapter {
        public So_OmAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        class ViewHolder {
            TextView tv_diemChuyen;
            TextView stt;
            TextView tv_diemTon;
            TextView Tv_so;
            TextView tv_diemNhan;
            TextView tv_diemOm;

            ViewHolder() {}
        }

        @SuppressLint("SetTextI18n")
        public View getView(int position, View view, ViewGroup parent) {
            @SuppressLint("WrongConstant") LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            ViewHolder holder = new ViewHolder();
            if (view == null) {
                view = inflater.inflate(R.layout.frag_canchuyen_lv, (ViewGroup) null);
                holder.Tv_so = (TextView) view.findViewById(R.id.Tv_so);
                holder.tv_diemNhan = (TextView) view.findViewById(R.id.tv_diemNhan);
                holder.tv_diemOm = (TextView) view.findViewById(R.id.tv_diemOm);
                holder.tv_diemChuyen = (TextView) view.findViewById(R.id.tv_diemChuyen);
                holder.tv_diemTon = (TextView) view.findViewById(R.id.tv_diemTon);
                holder.stt = (TextView) view.findViewById(R.id.stt);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (mNhay.get(position) > 0) {
                holder.Tv_so.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tv_diemNhan.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tv_diemOm.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tv_diemChuyen.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tv_diemTon.setTextColor(SupportMenu.CATEGORY_MASK);
                if (mNhay.get(position) == 1)
                    holder.Tv_so.setText(mSo.get(position) + "*");
                else if (mNhay.get(position) == 2)
                    holder.Tv_so.setText(mSo.get(position) + "**");
                else if (mNhay.get(position) == 3)
                    holder.Tv_so.setText(mSo.get(position) + "***");
                else if (mNhay.get(position) == 4)
                    holder.Tv_so.setText(mSo.get(position) + "****");
                else if (mNhay.get(position) == 5)
                    holder.Tv_so.setText(mSo.get(position) + "*****");
                else if (mNhay.get(position) == 6)
                    holder.Tv_so.setText(mSo.get(position) + "******");

                holder.tv_diemNhan.setText(mTienNhan.get(position));
                holder.tv_diemOm.setText(mTienOm.get(position));
                holder.tv_diemChuyen.setText(mTienchuyen.get(position));
                holder.tv_diemTon.setText(mTienTon.get(position));
                holder.stt.setText((position + 1) + "");
            } else {
                holder.Tv_so.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tv_diemNhan.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tv_diemOm.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tv_diemChuyen.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tv_diemTon.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.Tv_so.setText(mSo.get(position));
                holder.tv_diemNhan.setText(mTienNhan.get(position));
                holder.tv_diemOm.setText(mTienOm.get(position));
                holder.tv_diemChuyen.setText(mTienchuyen.get(position));
                holder.tv_diemTon.setText(mTienTon.get(position));
                holder.stt.setText((position + 1) + "");
            }
            return view;
        }
    }
}