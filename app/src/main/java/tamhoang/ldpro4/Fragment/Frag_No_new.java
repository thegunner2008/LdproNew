package tamhoang.ldpro4.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tamhoang.ldpro4.Activity.Activity_khach;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationNewReader;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_No_new extends Fragment {
    boolean Running = true;
    TextView bc_Chuyen;
    TextView bc_ChuyenAn;
    TextView bc_Nhan;
    TextView bc_NhanAn;
    TextView bca_Chuyen;
    TextView bca_ChuyenAn;
    TextView bca_Nhan;
    TextView bca_NhanAn;
    JSONObject caidat_tg;

    Database db;
    TextView dea_Chuyen;
    TextView dea_ChuyenAn;
    TextView dea_Nhan;
    TextView dea_NhanAn;
    TextView deb_Chuyen;
    TextView deb_ChuyenAn;
    TextView deb_Nhan;
    TextView deb_NhanAn;
    TextView dec_Chuyen;
    TextView dec_ChuyenAn;
    TextView dec_Nhan;
    TextView dec_NhanAn;
    TextView ded_Chuyen;
    TextView ded_ChuyenAn;
    TextView ded_Nhan;
    TextView ded_NhanAn;
    TextView det_Chuyen;
    TextView det_ChuyenAn;
    TextView det_Nhan;
    TextView det_NhanAn;
    Handler handler;
    JSONObject json;
    List<JSONObject> jsonKhachHang;
    LinearLayout li_bca;
    LinearLayout li_dea;
    LinearLayout li_dec;
    LinearLayout li_ded;
    LinearLayout li_det;
    LinearLayout li_loa;
    LinearLayout li_xia2;
    LinearLayout li_xn;
    TextView lo_Chuyen;
    TextView lo_ChuyenAn;
    TextView lo_Nhan;
    TextView lo_NhanAn;
    TextView loa_Chuyen;
    TextView loa_ChuyenAn;
    TextView loa_Nhan;
    TextView loa_NhanAn;
    ListView lv_baocaoKhach;
    LayoutInflater mInflate;
    public List<String> mSDT = new ArrayList();
    public List<String> mTenKH = new ArrayList();
    int position;
    private Runnable runnable = new Runnable() {
        public void run() {
            if (MainActivity.sms) {
                try {
                    lv_baoCao();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MainActivity.sms = false;
            }
            handler.postDelayed(this, 1000);
        }
    };
    TextView tv_TongGiu;
    TextView tv_TongTienChuyen;
    TextView tv_TongTienNhan;

    View rootView;
    TextView xi2_Chuyen;
    TextView xi2_ChuyenAn;
    TextView xi2_Nhan;
    TextView xi2_NhanAn;
    TextView xia2_Chuyen;
    TextView xia2_ChuyenAn;
    TextView xia2_Nhan;
    TextView xia2_NhanAn;
    TextView xn_Chuyen;
    TextView xn_ChuyenAn;
    TextView xn_Nhan;
    TextView xn_NhanAn;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.rootView = layoutInflater.inflate(R.layout.frag_norp1, viewGroup, false);
        this.db = new Database(getActivity());
        this.mInflate = layoutInflater;
        init();
        this.lv_baocaoKhach.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                position = i;
                Dialog(mTenKH.get(i));
                return false;
            }
        });
        if (!Congthuc.CheckTime("18:30")) {
            this.handler = new Handler();
            this.handler.postDelayed(this.runnable, 1000);
        }
        try {
            lv_baoCao();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.rootView;
    }

    public void onStop() {
        this.Running = false;
        super.onStop();
        try {
            this.handler.removeCallbacks(this.runnable);
        } catch (Exception unused) {
        }
    }

    public void Dialog(String str) {
        int i = 0;
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.frag_no_menu);
        String Get_date = MainActivity.Get_date();
        @SuppressLint("WrongConstant") ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService("clipboard");
        String sb4 = "Select * From tbl_kh_new WHERE ten_kh ='" + str + "'";
        Cursor GetData = db.GetData(sb4);
        GetData.moveToFirst();
        Button btnTinhlaitien = (Button) dialog.findViewById(R.id.tinhlaitien);
        Button btnNhanchottien = (Button) dialog.findViewById(R.id.nhanchottien);
        Button btnCopytinchitiet = (Button) dialog.findViewById(R.id.copytinchitiet);
        Button btnCopytinchotien = (Button) dialog.findViewById(R.id.copytinchotien);
        Button btnXoadulieu = (Button) dialog.findViewById(R.id.xoadulieu);
        Button btnBaocaochitiet = (Button) dialog.findViewById(R.id.Baocaochitiet);
        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.ln2);
        Switch switchR = (Switch) dialog.findViewById(R.id.switch1);
        SeekBar seek_Giu3ckhach = (SeekBar) dialog.findViewById(R.id.seek_Giu3ckhach);
        SeekBar seek_GiuDedly = (SeekBar) dialog.findViewById(R.id.seek_GiuDedly);
        SeekBar seek_GiuXikhach = (SeekBar) dialog.findViewById(R.id.seek_GiuXikhach);
        SeekBar seek_GiuLodly = (SeekBar) dialog.findViewById(R.id.seek_GiuLodly);
        SeekBar seek_GiuLokhach = (SeekBar) dialog.findViewById(R.id.seek_GiuLokhach);
        SeekBar seek_GiuXidly = (SeekBar) dialog.findViewById(R.id.seek_GiuXidly);
        SeekBar seek_GiuDekhach = (SeekBar) dialog.findViewById(R.id.seek_GiuDekhach);
        SeekBar seek_Giu3cdly = (SeekBar) dialog.findViewById(R.id.seek_Giu3cdly);
        String khgiu_bc = "khgiu_bc";
        String khgiu_xi = "khgiu_xi";
        String khgiu_lo = "khgiu_lo";
        TextView txt_pt_giu_lo_khach = (TextView) dialog.findViewById(R.id.pt_giu_lo_khach);
        TextView txt_pt_giu_xi_khach = (TextView) dialog.findViewById(R.id.pt_giu_xi_khach);
        TextView txt_pt_giu_bc_khach = (TextView) dialog.findViewById(R.id.pt_giu_bc_khach);
        TextView txt_pt_giu_de_dly = (TextView) dialog.findViewById(R.id.pt_giu_de_dly);
        TextView txt_pt_giu_de_khach = (TextView) dialog.findViewById(R.id.pt_giu_de_khach);
        TextView txt_pt_giu_lo_dly = (TextView) dialog.findViewById(R.id.pt_giu_lo_dly);
        String khgiu_de = "khgiu_de";
        TextView txt_pt_giu_xi_dly = (TextView) dialog.findViewById(R.id.pt_giu_xi_dly);
        TextView txt_pt_giu_bc_dly = (TextView) dialog.findViewById(R.id.pt_giu_bc_dly);
        try {
            this.json = new JSONObject(GetData.getString(5));
            this.caidat_tg = this.json.getJSONObject("caidat_tg");
            txt_pt_giu_de_dly.setText(this.caidat_tg.getInt("dlgiu_de") + "%");
            txt_pt_giu_lo_dly.setText(this.caidat_tg.getInt("dlgiu_lo") + "%");
            txt_pt_giu_xi_dly.setText(this.caidat_tg.getInt("dlgiu_xi") + "%");
            txt_pt_giu_bc_dly.setText(this.caidat_tg.getInt("dlgiu_bc") + "%");
            seek_GiuDedly.setProgress(this.caidat_tg.getInt("dlgiu_de") / 5);
            seek_GiuLodly.setProgress(this.caidat_tg.getInt("dlgiu_lo") / 5);
            seek_GiuXidly.setProgress(this.caidat_tg.getInt("dlgiu_xi") / 5);
            seek_Giu3cdly.setProgress(this.caidat_tg.getInt("dlgiu_bc") / 5);
            
            String sb = this.caidat_tg.getInt(khgiu_de) + "%";
            txt_pt_giu_de_khach.setText(sb);

            String sb2 = this.caidat_tg.getInt(khgiu_lo) + "%";
            txt_pt_giu_lo_khach.setText(sb2);

            String sb3 = this.caidat_tg.getInt(khgiu_xi) + "%";
            txt_pt_giu_xi_khach.setText(sb3);

            String sb5 = this.caidat_tg.getInt(khgiu_bc) + "%";
            txt_pt_giu_bc_khach.setText(sb5);
            
            seek_GiuDekhach.setProgress(this.caidat_tg.getInt(khgiu_de) / 5);
            int i2 = this.caidat_tg.getInt(khgiu_lo) / 5;
            seek_GiuLokhach.setProgress(i2);
            
            i = this.caidat_tg.getInt(khgiu_xi) / 5;
            seek_GiuXikhach.setProgress(i);
            
            int i3 = this.caidat_tg.getInt(khgiu_bc) / 5;
            seek_Giu3ckhach.setProgress(i3);
            GetData.close();
        } catch (Exception unused2) {
        }

        switchR.setOnCheckedChangeListener((compoundButton, z) -> {
            linearLayout.setVisibility(switchR.isChecked()? View.VISIBLE : View.GONE);
        });
        btnBaocaochitiet.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Activity_khach.class);
            intent.putExtra("tenKH", str);
            startActivity(intent);
            dialog.cancel();
        });
        btnTinhlaitien.setOnClickListener(view -> {
            try {
                TinhlaitienKhachnay(Get_date, str);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            dialog.cancel();
        });
        btnNhanchottien.setOnClickListener(view -> {
            Cursor GetData1 = db.GetData("Select * From tbl_kh_new Where ten_kh = '" + mTenKH.get(position) + "'");
            GetData1.moveToFirst();
            if (GetData1.getString(2).contains("sms")) {
                try {
                    if (MainActivity.jSon_Setting.getInt("tachxien_tinchot") == 0) {
                        db.SendSMS(mSDT.get(position), db.Tin_Chottien(mTenKH.get(position)));
                    } else {
                        db.SendSMS(mSDT.get(position), db.Tin_Chottien_xien(mTenKH.get(position)));
                    }
                    Toast.makeText(getActivity(), "Đã nhắn chốt tiền!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (GetData1.getString(2).contains("TL")) {
                try {
                    if (MainActivity.jSon_Setting.getInt("tachxien_tinchot") == 0) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            try {
                                MainActivity.sendMessage(Long.parseLong(mSDT.get(position)), db.Tin_Chottien(mTenKH.get(position)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            try {
                                MainActivity.sendMessage(Long.parseLong(mSDT.get(position)), db.Tin_Chottien_xien(mTenKH.get(position)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    Toast.makeText(getActivity(), "Đã nhắn chốt tiền!", Toast.LENGTH_LONG).show();
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            } else if (MainActivity.contactsMap.containsKey(GetData1.getString(1))) {
                NotificationNewReader NotificationNewReader = new NotificationNewReader();
                try {
                    if (MainActivity.jSon_Setting.getInt("tachxien_tinchot") == 0) {
                        NotificationNewReader.NotificationWearReader(GetData1.getString(1), db.Tin_Chottien(mTenKH.get(position)));
                    } else {
                        NotificationNewReader.NotificationWearReader(GetData1.getString(1), db.Tin_Chottien_xien(mTenKH.get(position)));
                    }
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Không có người này trong Chatbox", Toast.LENGTH_LONG).show();
            }
            dialog.cancel();
        });
        final ClipboardManager clipboardManager1 = clipboardManager;
        btnCopytinchitiet.setOnClickListener(view -> {
            clipboardManager1.setPrimaryClip(ClipData.newPlainText("Tin chốt:", db.Tin_Chottien_CT(mTenKH.get(position))));
            Toast.makeText(getActivity(), "Đã copy vào bộ nhớ tạm!", Toast.LENGTH_LONG).show();
            dialog.cancel();
        });
        btnCopytinchotien.setOnClickListener(view -> {
            ClipData clipData;
            db.GetData("Select * From tbl_kh_new Where ten_kh = '" + mTenKH.get(position) + "'").moveToFirst();
            try {
                if (MainActivity.jSon_Setting.getInt("tachxien_tinchot") == 0) {
                    clipData = ClipData.newPlainText("Tin chốt:", db.Tin_Chottien(mTenKH.get(position)));
                } else {
                    clipData = ClipData.newPlainText("Tin chốt:", db.Tin_Chottien_xien(mTenKH.get(position)));
                }
                clipboardManager1.setPrimaryClip(clipData);
                Toast.makeText(getActivity(), "Đã copy vào bộ nhớ tạm!" + this.caidat_tg.getInt("chot_sodu"),  Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.cancel();
        });
        btnXoadulieu.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Xoá dữ liệu của KH này?");
            builder.setPositiveButton("YES", (dialogInterface, i1) -> {
                db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + Get_date + "' AND ten_kh = '" + mTenKH.get(position) + "'");
                db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + Get_date + "' AND ten_kh = '" + mTenKH.get(position) + "'");
                try {
                    lv_baoCao();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.cancel();
                Toast.makeText(getActivity(), "Đã xoá", Toast.LENGTH_LONG).show();
            });
            builder.setNegativeButton("No", (dialogInterface, i1) -> dialogInterface.cancel());
            builder.create().show();
        });
        seek_GiuDekhach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i * 5;
                String sb = i2 + "%";
                txt_pt_giu_de_khach.setText(sb);
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("khgiu_de", this.max);
                    db.QueryData("update tbl_kh_new set tbl_MB = '" + json.toString() + "' WHERE ten_kh = '" + str + "'");
                    TinhlaitienKhachnay(Get_date, str);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
        seek_GiuLokhach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i * 5;
                String sb = i2 + "%";
                txt_pt_giu_lo_khach.setText(sb);
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("khgiu_lo", this.max);
                    db.QueryData("update tbl_kh_new set tbl_MB = '" + json.toString() + "' WHERE ten_kh = '" + str + "'");
                    TinhlaitienKhachnay(Get_date, str);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        seek_GiuXikhach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i * 5;
                String sb = i2 + "%";
                txt_pt_giu_xi_khach.setText(sb);
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("khgiu_xi", this.max);
                    db.QueryData("update tbl_kh_new set tbl_MB = '" + json.toString() + "' WHERE ten_kh = '" + str + "'");
                    TinhlaitienKhachnay(Get_date, str);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        seek_Giu3ckhach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i * 5;
                String sb = i2 + "%";
                txt_pt_giu_bc_khach.setText(sb);
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("khgiu_bc", this.max);
                    db.QueryData("update tbl_kh_new set tbl_MB = '" + json.toString() + "' WHERE ten_kh = '" + str + "'");
                    TinhlaitienKhachnay(Get_date, str);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        seek_GiuDedly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i * 5;
                String sb = i2 + "%";
                txt_pt_giu_de_dly.setText(sb);
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("dlgiu_de", this.max);
                    db.QueryData("update tbl_kh_new set tbl_MB = '" + json.toString() + "' WHERE ten_kh = '" + str + "'");
                    TinhlaitienKhachnay(Get_date, str);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        seek_GiuLodly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i * 5;
                String sb = i2 + "%";
                txt_pt_giu_lo_dly.setText(sb);
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("dlgiu_lo", this.max);
                    db.QueryData("update tbl_kh_new set tbl_MB = '" + json.toString() + "' WHERE ten_kh = '" + str + "'");
                    TinhlaitienKhachnay(Get_date, str);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        seek_GiuXidly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i * 5;
                String sb = i2 + "%";
                txt_pt_giu_xi_dly.setText(sb);
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("dlgiu_xi", this.max);
                    db.QueryData("update tbl_kh_new set tbl_MB = '" + json.toString() + "' WHERE ten_kh = '" + str + "'");
                    TinhlaitienKhachnay(Get_date, str);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        seek_Giu3cdly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i * 5;
                String sb = i2 + "%";
                txt_pt_giu_bc_dly.setText(sb);
                this.max = i2;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("dlgiu_bc", this.max);
                    db.QueryData("update tbl_kh_new set tbl_MB = '" + json.toString() + "' WHERE ten_kh = '" + str + "'");
                    TinhlaitienKhachnay(Get_date, str);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.getWindow().

                setLayout(-1, -2);
        dialog.setCancelable(true);
        dialog.setTitle("Xem dạng:");
        dialog.show();
    }

    /* access modifiers changed from: private */
    public void TinhlaitienKhachnay(String str, String str2) throws Throwable {
        db.QueryData("Delete From tbl_soctS WHERE  ngay_nhan = '" + str + "' AND ten_kh = '" + this.mTenKH.get(this.position) + "'");
        Cursor GetData = db.GetData("Select * FROM tbl_tinnhanS WHERE  ngay_nhan = '" + str + "' AND phat_hien_loi = 'ok' AND ten_kh = '" + this.mTenKH.get(this.position) + "'");
        while (GetData.moveToNext()) {
            String replaceAll = GetData.getString(10).replaceAll("\\*", "");
            db.QueryData("Update tbl_tinnhanS set nd_phantich = '" + replaceAll + "' WHERE id = " + GetData.getInt(0));
            db.NhapSoChiTiet(GetData.getInt(0));
        }
        Tinhtien();
        lv_baoCao();
        if (!GetData.isClosed()) {
            GetData.close();
        }
    }

    private void Tinhtien() throws JSONException {
        String Get_date = MainActivity.Get_date();
        Cursor GetData = db.GetData("Select * From Ketqua WHERE ngay = '" + Get_date + "'");

        if (GetData == null || GetData.getCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Không tìm thấy dữ liệu kết quả ngày " + Get_date);
            builder.setMessage("Đi đến trang cơ sở dữ liệu để tính lại?");
            builder.setNegativeButton("Có", (dialog, which) -> {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new Frag_Database()).commit();
            });
            builder.setPositiveButton("Không", (dialog, which) -> dialog.dismiss());
            builder.show();
            return;
        }
        GetData.moveToFirst();
        int i = 2;
        while (true) {
            if (i >= 29) {
                break;
            }
            try {
                if (GetData.isNull(i)) {
                    break;
                } else if (!Congthuc.isNumeric(GetData.getString(i))) {
                    break;
                } else {
                    i++;
                }
            } catch (Exception unused) {
            }
        }
        if (i >= 29) {
            this.db.Tinhtien(Get_date);
        }
        if (GetData != null && !GetData.isClosed()) {
            GetData.close();
        }
    }

    public void onDestroy() {
        try {
            this.mTenKH.clear();
            this.mSDT.clear();
            this.lv_baocaoKhach.setAdapter((ListAdapter) null);
        } catch (Exception unused) {
        }
        super.onDestroy();
    }

    @SuppressLint("SetTextI18n")
    public void lv_baoCao() throws JSONException {
        final String DEC = "dec";
        final String DET = "det";
        final String DED = "deb";
        String Get_date = MainActivity.Get_date();
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        Cursor cursor = db.GetData("Select the_loai\n, sum((type_kh = 1)*(100-diem_khachgiu)*diem/100) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 1)*(100-diem_khachgiu)*diem/100*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 1)*(100-diem_khachgiu)*diem/100*so_nhay)  END nAn\n, sum((type_kh = 1)*ket_qua*(100-diem_khachgiu)/100/1000) as mKetqua\n, sum((type_kh = 2)*(100-diem_khachgiu)*diem/100) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 2)*(100-diem_khachgiu)*diem/100*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 2)*(100-diem_khachgiu)*diem/100*so_nhay)  END nAn\n, sum((type_kh = 2)*ket_qua*(100-diem_khachgiu)/100/1000) as mKetqua\n  From tbl_soctS Where ngay_nhan = '" + Get_date + "'\n  AND the_loai <> 'tt' GROUP by the_loai");
        if (cursor != null) {
            JSONObject jSONObject = new JSONObject();
            double d = 0.0d;
            double d2 = 0.0d;
            while (cursor.moveToNext()) {
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("DiemNhan", decimalFormat.format(cursor.getDouble(1)));
                    jSONObject2.put("AnNhan", decimalFormat.format(cursor.getDouble(2)));
                    jSONObject2.put("KQNhan", decimalFormat.format(cursor.getDouble(3)));
                    jSONObject2.put("DiemChuyen", decimalFormat.format(cursor.getDouble(4)));
                    jSONObject2.put("AnChuyen", decimalFormat.format(cursor.getDouble(5)));
                    jSONObject2.put("KQChuyen", decimalFormat.format(cursor.getDouble(6)));
                    d += cursor.getDouble(3);
                    d2 += cursor.getDouble(6);
                    jSONObject.put(cursor.getString(0), jSONObject2.toString());
                } catch (JSONException ignored) {}
            }
            if (jSONObject.length() > 0) {
                if (jSONObject.has("dea")) {
                    try {
                        li_dea.setVisibility(View.VISIBLE);
                        JSONObject jSONObject3 = new JSONObject(jSONObject.getString("dea"));
                        if (jSONObject3.getString("DiemNhan").length() > 0) {
                            TextView textView = dea_Nhan;
                            StringBuilder sb = new StringBuilder();
                            sb.append(jSONObject3.getString("DiemNhan"));
                            sb.append("(");
                            sb.append(jSONObject3.getString("AnNhan"));
                            sb.append(")");
                            textView.setText(sb.toString());
                            dea_NhanAn.setText(jSONObject3.getString("KQNhan"));
                        }
                        if (jSONObject3.getString("DiemChuyen").length() > 0) {
                            dea_Chuyen.setText(jSONObject3.getString("DiemChuyen") + "(" + jSONObject3.getString("AnChuyen") + ")");
                            dea_ChuyenAn.setText(jSONObject3.getString("KQChuyen"));
                        }
                    } catch (JSONException ignored) {}
                }
                if (jSONObject.has(DED)) {
                    JSONObject jSONObject4 = new JSONObject(jSONObject.getString(DED));
                    if (jSONObject4.getString("DiemNhan").length() > 0) {
                        deb_Nhan.setText(jSONObject4.getString("DiemNhan") + "(" + jSONObject4.getString("AnNhan") + ")");
                        deb_NhanAn.setText(jSONObject4.getString("KQNhan"));
                    }
                    if (jSONObject4.getString("DiemChuyen").length() > 0) {
                        deb_Chuyen.setText(jSONObject4.getString("DiemChuyen") + "(" + jSONObject4.getString("AnChuyen") + ")");
                        deb_ChuyenAn.setText(jSONObject4.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has(DET)) {
                    li_det.setVisibility(View.VISIBLE);
                    JSONObject jSONObject5 = new JSONObject(jSONObject.getString(DET));
                    if (jSONObject5.getString("DiemNhan").length() > 0) {
                        det_Nhan.setText(jSONObject5.getString("DiemNhan") + "(" + jSONObject5.getString("AnNhan") + ")");
                        det_NhanAn.setText(jSONObject5.getString("KQNhan"));
                    }
                    if (jSONObject5.getString("DiemChuyen").length() > 0) {
                        det_Chuyen.setText(jSONObject5.getString("DiemChuyen") + "(" + jSONObject5.getString("AnChuyen") + ")");
                        det_ChuyenAn.setText(jSONObject5.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has(DEC)) {
                    li_dec.setVisibility(View.VISIBLE);
                    JSONObject jSONObject6 = new JSONObject(jSONObject.getString(DEC));
                    if (jSONObject6.getString("DiemNhan").length() > 0) {
                        dec_Nhan.setText(jSONObject6.getString("DiemNhan") + "(" + jSONObject6.getString("AnNhan") + ")");
                        dec_NhanAn.setText(jSONObject6.getString("KQNhan"));
                    }
                    if (jSONObject6.getString("DiemChuyen").length() > 0) {
                        dec_Chuyen.setText(jSONObject6.getString("DiemChuyen") + "(" + jSONObject6.getString("AnChuyen") + ")");
                        dec_ChuyenAn.setText(jSONObject6.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("ded")) {
                    li_ded.setVisibility(View.VISIBLE);
                    JSONObject jSONObject7 = new JSONObject(jSONObject.getString("ded"));
                    if (jSONObject7.getString("DiemNhan").length() > 0) {
                        ded_Nhan.setText(jSONObject7.getString("DiemNhan") + "(" + jSONObject7.getString("AnNhan") + ")");
                        ded_NhanAn.setText(jSONObject7.getString("KQNhan"));
                    }
                    if (jSONObject7.getString("DiemChuyen").length() > 0) {
                        ded_Chuyen.setText(jSONObject7.getString("DiemChuyen") + "(" + jSONObject7.getString("AnChuyen") + ")");
                        ded_ChuyenAn.setText(jSONObject7.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("lo")) {
                    JSONObject jSONObject8 = new JSONObject(jSONObject.getString("lo"));
                    if (jSONObject8.getString("DiemNhan").length() > 0) {
                        lo_Nhan.setText(jSONObject8.getString("DiemNhan") + "(" + jSONObject8.getString("AnNhan") + ")");
                        lo_NhanAn.setText(jSONObject8.getString("KQNhan"));
                    }
                    if (jSONObject8.getString("DiemChuyen").length() > 0) {
                        lo_Chuyen.setText(jSONObject8.getString("DiemChuyen") + "(" + jSONObject8.getString("AnChuyen") + ")");
                        lo_ChuyenAn.setText(jSONObject8.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("loa")) {
                    li_loa.setVisibility(View.VISIBLE);
                    JSONObject jSONObject9 = new JSONObject(jSONObject.getString("loa"));
                    if (jSONObject9.getString("DiemNhan").length() > 0) {
                        loa_Nhan.setText(jSONObject9.getString("DiemNhan") + "(" + jSONObject9.getString("AnNhan") + ")");
                        loa_NhanAn.setText(jSONObject9.getString("KQNhan"));
                    }
                    if (jSONObject9.getString("DiemChuyen").length() > 0) {
                        loa_Chuyen.setText(jSONObject9.getString("DiemChuyen") + "(" + jSONObject9.getString("AnChuyen") + ")");
                        loa_ChuyenAn.setText(jSONObject9.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xi")) {
                    JSONObject jSONObject10 = new JSONObject(jSONObject.getString("xi"));
                    if (jSONObject10.getString("DiemNhan").length() > 0) {
                        xi2_Nhan.setText(jSONObject10.getString("DiemNhan") + "(" + jSONObject10.getString("AnNhan") + ")");
                        xi2_NhanAn.setText(jSONObject10.getString("KQNhan"));
                    }
                    if (jSONObject10.getString("DiemChuyen").length() > 0) {
                        xi2_Chuyen.setText(jSONObject10.getString("DiemChuyen") + "(" + jSONObject10.getString("AnChuyen") + ")");
                        xi2_ChuyenAn.setText(jSONObject10.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xn")) {
                    li_xn.setVisibility(View.VISIBLE);
                    JSONObject jSONObject11 = new JSONObject(jSONObject.getString("xn"));
                    if (jSONObject11.getString("DiemNhan").length() > 0) {
                        xn_Nhan.setText(jSONObject11.getString("DiemNhan") + "(" + jSONObject11.getString("AnNhan") + ")");
                        xn_NhanAn.setText(jSONObject11.getString("KQNhan"));
                    }
                    if (jSONObject11.getString("DiemChuyen").length() > 0) {
                        xn_Chuyen.setText(jSONObject11.getString("DiemChuyen") + "(" + jSONObject11.getString("AnChuyen") + ")");
                        xn_ChuyenAn.setText(jSONObject11.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xia")) {
                    li_xia2.setVisibility(View.VISIBLE);
                    JSONObject jSONObject12 = new JSONObject(jSONObject.getString("xia"));
                    if (jSONObject12.getString("DiemNhan").length() > 0) {
                        xia2_Nhan.setText(jSONObject12.getString("DiemNhan") + "(" + jSONObject12.getString("AnNhan") + ")");
                        xia2_NhanAn.setText(jSONObject12.getString("KQNhan"));
                    }
                    if (jSONObject12.getString("DiemChuyen").length() > 0) {
                        xia2_Chuyen.setText(jSONObject12.getString("DiemChuyen") + "(" + jSONObject12.getString("AnChuyen") + ")");
                        xia2_ChuyenAn.setText(jSONObject12.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("bc")) {
                    JSONObject jSONObject13 = new JSONObject(jSONObject.getString("bc"));
                    if (jSONObject13.getString("DiemNhan").length() > 0) {
                        bc_Nhan.setText(jSONObject13.getString("DiemNhan") + "(" + jSONObject13.getString("AnNhan") + ")");
                        bc_NhanAn.setText(jSONObject13.getString("KQNhan"));
                    }
                    if (jSONObject13.getString("DiemChuyen").length() > 0) {
                        bc_Chuyen.setText(jSONObject13.getString("DiemChuyen") + "(" + jSONObject13.getString("AnChuyen") + ")");
                        bc_ChuyenAn.setText(jSONObject13.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("bca")) {
                    li_bca.setVisibility(View.VISIBLE);
                    JSONObject jSONObject14 = new JSONObject(jSONObject.getString("bca"));
                    if (jSONObject14.getString("DiemNhan").length() > 0) {
                        bca_Nhan.setText(jSONObject14.getString("DiemNhan") + "(" + jSONObject14.getString("AnNhan") + ")");
                        bca_NhanAn.setText(jSONObject14.getString("KQNhan"));
                    }
                    if (jSONObject14.getString("DiemChuyen").length() > 0) {
                        bca_Chuyen.setText(jSONObject14.getString("DiemChuyen") + "(" + jSONObject14.getString("AnChuyen") + ")");
                        bca_ChuyenAn.setText(jSONObject14.getString("KQChuyen"));
                    }
                }
                tv_TongTienNhan.setText(decimalFormat.format(d));
                tv_TongTienChuyen.setText(decimalFormat.format(d2));
                tv_TongGiu.setText(decimalFormat.format((-d) - d2));
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                XemListview();
            }
            cursor.close();
            XemListview();
        }
    }

    private void XemListview() {
        String Get_date = MainActivity.Get_date();
        this.jsonKhachHang = new ArrayList();
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        this.mTenKH.clear();
        this.mSDT.clear();
        try {
            Cursor GetData = this.db.GetData("Select ten_kh, so_dienthoai, the_loai\n, sum((type_kh = 1)*(100-diem_khachgiu)*diem/100) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 1)*(100-diem_khachgiu)*diem/100*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 1)*(100-diem_khachgiu)*diem/100*so_nhay)  END nAn\n, sum((type_kh = 1)*ket_qua*(100-diem_khachgiu)/100/1000) as mKetqua\n, sum((type_kh = 2)*(100-diem_khachgiu)*diem/100) as mDiem\n, CASE WHEN the_loai = 'xi' OR the_loai = 'xia' \n THEN sum((type_kh = 2)*(100-diem_khachgiu)*diem/100*so_nhay*lan_an/1000) \n ELSE sum((type_kh = 2)*(100-diem_khachgiu)*diem/100*so_nhay)  END nAn\n, sum((type_kh = 2)* ket_qua/1000) as mKetqua\n  From tbl_soctS Where ngay_nhan = '" + Get_date + "'\n  AND the_loai <> 'tt' GROUP by ten_kh, the_loai");
            JSONObject jSONObject = new JSONObject();
            if (GetData != null) {
                String str = "";
                double d = 0.0d;
                double d2 = 0.0d;
                while (GetData.moveToNext()) {
                    if (str.length() == 0) {
                        this.mTenKH.add(GetData.getString(0));
                        this.mSDT.add(GetData.getString(1));
                        str = GetData.getString(0);
                    } else if (str.indexOf(GetData.getString(0)) != 0) {
                        jSONObject.put("Tien_Nhan", decimalFormat.format(d));
                        jSONObject.put("Tien_Chuyen", decimalFormat.format(d2));
                        jSONObject.put("Tong_Tien", decimalFormat.format(d + d2));
                        this.jsonKhachHang.add(jSONObject);
                        this.mTenKH.add(GetData.getString(0));
                        this.mSDT.add(GetData.getString(1));
                        String string = GetData.getString(0);
                        d = 0.0d;
                        d2 = 0.0d;
                        str = string;
                        jSONObject = new JSONObject();
                    }
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("DiemNhan", decimalFormat.format(GetData.getDouble(3)));
                    jSONObject2.put("AnNhan", decimalFormat.format(GetData.getDouble(4)));
                    jSONObject2.put("KQNhan", decimalFormat.format(GetData.getDouble(5)));
                    jSONObject2.put("DiemChuyen", decimalFormat.format(GetData.getDouble(6)));
                    jSONObject2.put("AnChuyen", decimalFormat.format(GetData.getDouble(7)));
                    jSONObject2.put("KQChuyen", decimalFormat.format(GetData.getDouble(8)));
                    d += GetData.getDouble(5);
                    d2 += GetData.getDouble(8);
                    jSONObject.put(GetData.getString(2), jSONObject2.toString());
                }
                jSONObject.put("Tien_Nhan", decimalFormat.format(d));
                jSONObject.put("Tien_Chuyen", decimalFormat.format(d2));
                jSONObject.put("Tong_Tien", decimalFormat.format(d + d2));
                if (GetData.getCount() > 0) {
                    this.jsonKhachHang.add(jSONObject);
                }
                if (GetData != null && !GetData.isClosed()) {
                    GetData.close();
                }
            }
        } catch (SQLException ignored) {
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (getActivity() != null) {
            this.lv_baocaoKhach.setAdapter(new NoRP_TN_Adapter(getActivity(), R.layout.frag_norp1_2, this.jsonKhachHang));
        }
    }

    class NoRP_TN_Adapter extends ArrayAdapter {
        TextView bc_Chuyen;
        TextView bc_ChuyenAn;
        TextView bc_Nhan;
        TextView bc_NhanAn;
        TextView bca_Chuyen;
        TextView bca_ChuyenAn;
        TextView bca_Nhan;
        TextView bca_NhanAn;
        TextView dea_Chuyen;
        TextView dea_ChuyenAn;
        TextView dea_Nhan;
        TextView dea_NhanAn;
        TextView deb_Chuyen;
        TextView deb_ChuyenAn;
        TextView deb_Nhan;
        TextView deb_NhanAn;
        TextView dec_Chuyen;
        TextView dec_ChuyenAn;
        TextView dec_Nhan;
        TextView dec_NhanAn;
        TextView ded_Chuyen;
        TextView ded_ChuyenAn;
        TextView ded_Nhan;
        TextView ded_NhanAn;
        TextView det_Chuyen;
        TextView det_ChuyenAn;
        TextView det_Nhan;
        TextView det_NhanAn;
        LinearLayout li_bca;
        LinearLayout li_dea;
        LinearLayout li_dec;
        LinearLayout li_ded;
        LinearLayout li_det;
        LinearLayout li_loa;
        LinearLayout li_xi2;
        LinearLayout li_xia2;
        TextView lo_Chuyen;
        TextView lo_ChuyenAn;
        TextView lo_Nhan;
        TextView lo_NhanAn;
        TextView loa_Chuyen;
        TextView loa_ChuyenAn;
        TextView loa_Nhan;
        TextView loa_NhanAn;
        TextView tv_TongKhach;
        TextView tv_TongTienChuyen;
        TextView tv_TongTienNhan;
        TextView tv_ket_qua;
        TextView tv_tenKH;
        TextView tv_tongtien;
        TextView xi2_Chuyen;
        TextView xi2_ChuyenAn;
        TextView xi2_Nhan;
        TextView xi2_NhanAn;
        TextView xia2_Chuyen;
        TextView xia2_ChuyenAn;
        TextView xia2_Nhan;
        TextView xia2_NhanAn;

        public NoRP_TN_Adapter(Context context, int i, List<JSONObject> list) {
            super(context, i, list);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            String str;
            String str2;
            int i2 = i;
            View inflate = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.frag_norp1_2, (ViewGroup) null);
            this.tv_tongtien = (TextView) inflate.findViewById(R.id.tv_no_Tong);
            this.tv_ket_qua = (TextView) inflate.findViewById(R.id.tv_ThangThua);
            this.tv_tenKH = (TextView) inflate.findViewById(R.id.tv_tenKH);
            this.dea_Nhan = (TextView) inflate.findViewById(R.id.dea_Nhan);
            this.deb_Nhan = (TextView) inflate.findViewById(R.id.deb_Nhan);
            this.det_Nhan = (TextView) inflate.findViewById(R.id.det_Nhan);
            this.dec_Nhan = (TextView) inflate.findViewById(R.id.dec_Nhan);
            this.ded_Nhan = (TextView) inflate.findViewById(R.id.ded_Nhan);
            this.lo_Nhan = (TextView) inflate.findViewById(R.id.lo_Nhan);
            this.loa_Nhan = (TextView) inflate.findViewById(R.id.loa_Nhan);
            this.bc_Nhan = (TextView) inflate.findViewById(R.id.bc_Nhan);
            this.bca_Nhan = (TextView) inflate.findViewById(R.id.bca_Nhan);
            this.dea_NhanAn = (TextView) inflate.findViewById(R.id.dea_NhanAn);
            this.deb_NhanAn = (TextView) inflate.findViewById(R.id.deb_NhanAn);
            this.det_NhanAn = (TextView) inflate.findViewById(R.id.det_NhanAn);
            this.dec_NhanAn = (TextView) inflate.findViewById(R.id.dec_NhanAn);
            this.ded_NhanAn = (TextView) inflate.findViewById(R.id.ded_NhanAn);
            this.lo_NhanAn = (TextView) inflate.findViewById(R.id.lo_NhanAn);
            this.loa_NhanAn = (TextView) inflate.findViewById(R.id.loa_NhanAn);
            this.bc_NhanAn = (TextView) inflate.findViewById(R.id.bc_NhanAn);
            this.dea_Chuyen = (TextView) inflate.findViewById(R.id.dea_Chuyen);
            this.deb_Chuyen = (TextView) inflate.findViewById(R.id.deb_Chuyen);
            this.det_Chuyen = (TextView) inflate.findViewById(R.id.det_Chuyen);
            this.dec_Chuyen = (TextView) inflate.findViewById(R.id.dec_Chuyen);
            this.ded_Chuyen = (TextView) inflate.findViewById(R.id.ded_Chuyen);
            this.lo_Chuyen = (TextView) inflate.findViewById(R.id.lo_Chuyen);
            this.loa_Chuyen = (TextView) inflate.findViewById(R.id.loa_Chuyen);
            this.bc_Chuyen = (TextView) inflate.findViewById(R.id.bc_Chuyen);
            this.dea_ChuyenAn = (TextView) inflate.findViewById(R.id.dea_ChuyenAn);
            this.deb_ChuyenAn = (TextView) inflate.findViewById(R.id.deb_ChuyenAn);
            this.det_ChuyenAn = (TextView) inflate.findViewById(R.id.det_ChuyenAn);
            this.dec_ChuyenAn = (TextView) inflate.findViewById(R.id.dec_ChuyenAn);
            this.ded_ChuyenAn = (TextView) inflate.findViewById(R.id.ded_ChuyenAn);
            this.lo_ChuyenAn = (TextView) inflate.findViewById(R.id.lo_ChuyenAn);
            this.loa_ChuyenAn = (TextView) inflate.findViewById(R.id.loa_ChuyenAn);
            this.bc_ChuyenAn = (TextView) inflate.findViewById(R.id.bc_ChuyenAn);
            this.tv_TongKhach = (TextView) inflate.findViewById(R.id.tv_TongTien);
            this.tv_TongTienNhan = (TextView) inflate.findViewById(R.id.tv_TongTienNhan);
            this.tv_TongTienChuyen = (TextView) inflate.findViewById(R.id.tv_TongTienChuyen);
            this.li_dea = (LinearLayout) inflate.findViewById(R.id.li_dea);
            this.li_det = (LinearLayout) inflate.findViewById(R.id.li_det);
            this.li_dec = (LinearLayout) inflate.findViewById(R.id.li_dec);
            this.li_ded = (LinearLayout) inflate.findViewById(R.id.li_ded);
            this.li_loa = (LinearLayout) inflate.findViewById(R.id.li_loa);
            this.li_bca = (LinearLayout) inflate.findViewById(R.id.li_bca);
            this.li_xi2 = (LinearLayout) inflate.findViewById(R.id.li_xi2);
            this.li_xia2 = (LinearLayout) inflate.findViewById(R.id.li_xia2);
            this.xi2_Nhan = (TextView) inflate.findViewById(R.id.xi2_Nhan);
            this.xi2_NhanAn = (TextView) inflate.findViewById(R.id.xi2_NhanAn);
            this.xi2_Chuyen = (TextView) inflate.findViewById(R.id.xi2_Chuyen);
            this.xi2_ChuyenAn = (TextView) inflate.findViewById(R.id.xi2_ChuyenAn);
            this.xia2_Nhan = (TextView) inflate.findViewById(R.id.xia2_Nhan);
            this.xia2_NhanAn = (TextView) inflate.findViewById(R.id.xia2_NhanAn);
            this.xia2_Chuyen = (TextView) inflate.findViewById(R.id.xia2_Chuyen);
            this.xia2_ChuyenAn = (TextView) inflate.findViewById(R.id.xia2_ChuyenAn);
            this.bca_Nhan = (TextView) inflate.findViewById(R.id.bca_Nhan);
            this.bca_NhanAn = (TextView) inflate.findViewById(R.id.bca_NhanAn);
            this.bca_Chuyen = (TextView) inflate.findViewById(R.id.bca_Chuyen);
            this.bca_ChuyenAn = (TextView) inflate.findViewById(R.id.bca_ChuyenAn);
            JSONObject jSONObject = jsonKhachHang.get(i2);
            try {
                this.tv_TongTienNhan.setText(jSONObject.getString("Tien_Nhan"));
                this.tv_TongTienChuyen.setText(jSONObject.getString("Tien_Chuyen"));
                this.tv_TongKhach.setText(jSONObject.getString("Tong_Tien"));
                this.tv_tenKH.setText((CharSequence) mTenKH.get(i2));
                view2 = inflate;
                if (jSONObject.has("dea")) {
                    try {
                        str = "ded";
                        this.li_dea.setVisibility(View.VISIBLE);
                        JSONObject jSONObject2 = new JSONObject(jSONObject.getString("dea"));
                        if (jSONObject2.getString("DiemNhan").length() > 0) {
                            TextView textView = this.dea_Nhan;
                            StringBuilder sb = new StringBuilder();
                            str2 = "dec";
                            sb.append(jSONObject2.getString("DiemNhan"));
                            sb.append("(");
                            sb.append(jSONObject2.getString("AnNhan"));
                            sb.append(")");
                            textView.setText(sb.toString());
                            this.dea_NhanAn.setText(jSONObject2.getString("KQNhan"));
                        } else {
                            str2 = "dec";
                        }
                        if (jSONObject2.getString("DiemChuyen").length() > 0) {
                            TextView textView2 = this.dea_Chuyen;
                            textView2.setText(jSONObject2.getString("DiemChuyen") + "(" + jSONObject2.getString("AnChuyen") + ")");
                            this.dea_ChuyenAn.setText(jSONObject2.getString("KQChuyen"));
                        }
                    } catch (Exception unused) {
                        Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                        return view2;
                    }
                } else {
                    str = "ded";
                    str2 = "dec";
                }
                if (jSONObject.has("deb")) {
                    JSONObject jSONObject3 = new JSONObject(jSONObject.getString("deb"));
                    if (jSONObject3.getString("DiemNhan").length() > 0) {
                        TextView textView3 = this.deb_Nhan;
                        textView3.setText(jSONObject3.getString("DiemNhan") + "(" + jSONObject3.getString("AnNhan") + ")");
                        this.deb_NhanAn.setText(jSONObject3.getString("KQNhan"));
                    }
                    if (jSONObject3.getString("DiemChuyen").length() > 0) {
                        TextView textView4 = this.deb_Chuyen;
                        textView4.setText(jSONObject3.getString("DiemChuyen") + "(" + jSONObject3.getString("AnChuyen") + ")");
                        this.deb_ChuyenAn.setText(jSONObject3.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("det")) {
                    this.li_det.setVisibility(View.VISIBLE);
                    JSONObject jSONObject4 = new JSONObject(jSONObject.getString("det"));
                    if (jSONObject4.getString("DiemNhan").length() > 0) {
                        TextView textView5 = this.det_Nhan;
                        textView5.setText(jSONObject4.getString("DiemNhan") + "(" + jSONObject4.getString("AnNhan") + ")");
                        this.det_NhanAn.setText(jSONObject4.getString("KQNhan"));
                    }
                    if (jSONObject4.getString("DiemChuyen").length() > 0) {
                        TextView textView6 = this.det_Chuyen;
                        textView6.setText(jSONObject4.getString("DiemChuyen") + "(" + jSONObject4.getString("AnChuyen") + ")");
                        this.det_ChuyenAn.setText(jSONObject4.getString("KQChuyen"));
                    }
                }
                String str3 = str2;
                if (jSONObject.has(str3)) {
                    this.li_dec.setVisibility(View.VISIBLE);
                    JSONObject jSONObject5 = new JSONObject(jSONObject.getString(str3));
                    if (jSONObject5.getString("DiemNhan").length() > 0) {
                        TextView textView7 = this.dec_Nhan;
                        textView7.setText(jSONObject5.getString("DiemNhan") + "(" + jSONObject5.getString("AnNhan") + ")");
                        this.dec_NhanAn.setText(jSONObject5.getString("KQNhan"));
                    }
                    if (jSONObject5.getString("DiemChuyen").length() > 0) {
                        TextView textView8 = this.dec_Chuyen;
                        textView8.setText(jSONObject5.getString("DiemChuyen") + "(" + jSONObject5.getString("AnChuyen") + ")");
                        this.dec_ChuyenAn.setText(jSONObject5.getString("KQChuyen"));
                    }
                }
                String str4 = str;
                if (jSONObject.has(str4)) {
                    this.li_ded.setVisibility(View.VISIBLE);
                    JSONObject jSONObject6 = new JSONObject(jSONObject.getString(str4));
                    if (jSONObject6.getString("DiemNhan").length() > 0) {
                        TextView textView9 = this.ded_Nhan;
                        textView9.setText(jSONObject6.getString("DiemNhan") + "(" + jSONObject6.getString("AnNhan") + ")");
                        this.ded_NhanAn.setText(jSONObject6.getString("KQNhan"));
                    }
                    if (jSONObject6.getString("DiemChuyen").length() > 0) {
                        TextView textView10 = this.ded_Chuyen;
                        textView10.setText(jSONObject6.getString("DiemChuyen") + "(" + jSONObject6.getString("AnChuyen") + ")");
                        this.ded_ChuyenAn.setText(jSONObject6.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("lo")) {
                    JSONObject jSONObject7 = new JSONObject(jSONObject.getString("lo"));
                    if (jSONObject7.getString("DiemNhan").length() > 0) {
                        TextView textView11 = this.lo_Nhan;
                        textView11.setText(jSONObject7.getString("DiemNhan") + "(" + jSONObject7.getString("AnNhan") + ")");
                        this.lo_NhanAn.setText(jSONObject7.getString("KQNhan"));
                    }
                    if (jSONObject7.getString("DiemChuyen").length() > 0) {
                        TextView textView12 = this.lo_Chuyen;
                        textView12.setText(jSONObject7.getString("DiemChuyen") + "(" + jSONObject7.getString("AnChuyen") + ")");
                        this.lo_ChuyenAn.setText(jSONObject7.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("loa")) {
                    this.li_loa.setVisibility(View.VISIBLE);
                    JSONObject jSONObject8 = new JSONObject(jSONObject.getString("loa"));
                    if (jSONObject8.getString("DiemNhan").length() > 0) {
                        TextView textView13 = this.loa_Nhan;
                        textView13.setText(jSONObject8.getString("DiemNhan") + "(" + jSONObject8.getString("AnNhan") + ")");
                        this.loa_NhanAn.setText(jSONObject8.getString("KQNhan"));
                    }
                    if (jSONObject8.getString("DiemChuyen").length() > 0) {
                        TextView textView14 = this.loa_Chuyen;
                        textView14.setText(jSONObject8.getString("DiemChuyen") + "(" + jSONObject8.getString("AnChuyen") + ")");
                        this.loa_ChuyenAn.setText(jSONObject8.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xi")) {
                    this.li_xi2.setVisibility(View.VISIBLE);
                    JSONObject jSONObject9 = new JSONObject(jSONObject.getString("xi"));
                    if (jSONObject9.getString("DiemNhan").length() > 0) {
                        TextView textView15 = this.xi2_Nhan;
                        textView15.setText(jSONObject9.getString("DiemNhan") + "(" + jSONObject9.getString("AnNhan") + ")");
                        this.xi2_NhanAn.setText(jSONObject9.getString("KQNhan"));
                    }
                    if (jSONObject9.getString("DiemChuyen").length() > 0) {
                        TextView textView16 = this.xi2_Chuyen;
                        textView16.setText(jSONObject9.getString("DiemChuyen") + "(" + jSONObject9.getString("AnChuyen") + ")");
                        this.xi2_ChuyenAn.setText(jSONObject9.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("xia")) {
                    this.li_xia2.setVisibility(View.VISIBLE);
                    JSONObject jSONObject10 = new JSONObject(jSONObject.getString("xia"));
                    if (jSONObject10.getString("DiemNhan").length() > 0) {
                        TextView textView17 = this.xia2_Nhan;
                        textView17.setText(jSONObject10.getString("DiemNhan") + "(" + jSONObject10.getString("AnNhan") + ")");
                        this.xia2_NhanAn.setText(jSONObject10.getString("KQNhan"));
                    }
                    if (jSONObject10.getString("DiemChuyen").length() > 0) {
                        TextView textView18 = this.xia2_Chuyen;
                        textView18.setText(jSONObject10.getString("DiemChuyen") + "(" + jSONObject10.getString("AnChuyen") + ")");
                        this.xia2_ChuyenAn.setText(jSONObject10.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("bc")) {
                    JSONObject jSONObject11 = new JSONObject(jSONObject.getString("bc"));
                    if (jSONObject11.getString("DiemNhan").length() > 0) {
                        TextView textView19 = this.bc_Nhan;
                        textView19.setText(jSONObject11.getString("DiemNhan") + "(" + jSONObject11.getString("AnNhan") + ")");
                        this.bc_NhanAn.setText(jSONObject11.getString("KQNhan"));
                    }
                    if (jSONObject11.getString("DiemChuyen").length() > 0) {
                        TextView textView20 = this.bc_Chuyen;
                        textView20.setText(jSONObject11.getString("DiemChuyen") + "(" + jSONObject11.getString("AnChuyen") + ")");
                        this.bc_ChuyenAn.setText(jSONObject11.getString("KQChuyen"));
                    }
                }
                if (jSONObject.has("bca")) {
                    this.li_bca.setVisibility(View.VISIBLE);
                    JSONObject jSONObject12 = new JSONObject(jSONObject.getString("bca"));
                    if (jSONObject12.getString("DiemNhan").length() > 0) {
                        TextView textView21 = this.bca_Nhan;
                        textView21.setText(jSONObject12.getString("DiemNhan") + "(" + jSONObject12.getString("AnNhan") + ")");
                        this.bca_NhanAn.setText(jSONObject12.getString("KQNhan"));
                    }
                    if (jSONObject12.getString("DiemChuyen").length() > 0) {
                        TextView textView22 = this.bca_Chuyen;
                        textView22.setText(jSONObject12.getString("DiemChuyen") + "(" + jSONObject12.getString("AnChuyen") + ")");
                        this.bca_ChuyenAn.setText(jSONObject12.getString("KQChuyen"));
                    }
                }
            } catch (Exception unused2) {
                view2 = inflate;
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                return view2;
            }
            return view2;
        }

    }

    private void init() {
        this.xn_Nhan = (TextView) this.rootView.findViewById(R.id.xn_Nhan);
        this.xn_NhanAn = (TextView) this.rootView.findViewById(R.id.xn_NhanAn);
        this.xn_Chuyen = (TextView) this.rootView.findViewById(R.id.xn_Chuyen);
        this.xn_ChuyenAn = (TextView) this.rootView.findViewById(R.id.xn_ChuyenAn);
        this.dea_Nhan = (TextView) this.rootView.findViewById(R.id.dea_Nhan);
        this.deb_Nhan = (TextView) this.rootView.findViewById(R.id.deb_Nhan);
        this.det_Nhan = (TextView) this.rootView.findViewById(R.id.det_Nhan);
        this.dec_Nhan = (TextView) this.rootView.findViewById(R.id.dec_Nhan);
        this.ded_Nhan = (TextView) this.rootView.findViewById(R.id.ded_Nhan);
        this.lo_Nhan = (TextView) this.rootView.findViewById(R.id.lo_Nhan);
        this.loa_Nhan = (TextView) this.rootView.findViewById(R.id.loa_Nhan);
        this.bc_Nhan = (TextView) this.rootView.findViewById(R.id.bc_Nhan);
        this.bca_Nhan = (TextView) this.rootView.findViewById(R.id.bca_Nhan);
        this.dea_NhanAn = (TextView) this.rootView.findViewById(R.id.dea_NhanAn);
        this.deb_NhanAn = (TextView) this.rootView.findViewById(R.id.deb_NhanAn);
        this.det_NhanAn = (TextView) this.rootView.findViewById(R.id.det_NhanAn);
        this.dec_NhanAn = (TextView) this.rootView.findViewById(R.id.dec_NhanAn);
        this.ded_NhanAn = (TextView) this.rootView.findViewById(R.id.ded_NhanAn);
        this.lo_NhanAn = (TextView) this.rootView.findViewById(R.id.lo_NhanAn);
        this.loa_NhanAn = (TextView) this.rootView.findViewById(R.id.loa_NhanAn);
        this.bc_NhanAn = (TextView) this.rootView.findViewById(R.id.bc_NhanAn);
        this.dea_Chuyen = (TextView) this.rootView.findViewById(R.id.dea_Chuyen);
        this.deb_Chuyen = (TextView) this.rootView.findViewById(R.id.deb_Chuyen);
        this.det_Chuyen = (TextView) this.rootView.findViewById(R.id.det_Chuyen);
        this.dec_Chuyen = (TextView) this.rootView.findViewById(R.id.dec_Chuyen);
        this.ded_Chuyen = (TextView) this.rootView.findViewById(R.id.ded_Chuyen);
        this.lo_Chuyen = (TextView) this.rootView.findViewById(R.id.lo_Chuyen);
        this.loa_Chuyen = (TextView) this.rootView.findViewById(R.id.loa_Chuyen);
        this.bc_Chuyen = (TextView) this.rootView.findViewById(R.id.bc_Chuyen);
        this.dea_ChuyenAn = (TextView) this.rootView.findViewById(R.id.dea_ChuyenAn);
        this.deb_ChuyenAn = (TextView) this.rootView.findViewById(R.id.deb_ChuyenAn);
        this.det_ChuyenAn = (TextView) this.rootView.findViewById(R.id.det_ChuyenAn);
        this.dec_ChuyenAn = (TextView) this.rootView.findViewById(R.id.dec_ChuyenAn);
        this.ded_ChuyenAn = (TextView) this.rootView.findViewById(R.id.ded_ChuyenAn);
        this.lo_ChuyenAn = (TextView) this.rootView.findViewById(R.id.lo_ChuyenAn);
        this.loa_ChuyenAn = (TextView) this.rootView.findViewById(R.id.loa_ChuyenAn);
        this.bc_ChuyenAn = (TextView) this.rootView.findViewById(R.id.bc_ChuyenAn);
        this.tv_TongGiu = (TextView) this.rootView.findViewById(R.id.tv_TongGiu);
        this.tv_TongTienNhan = (TextView) this.rootView.findViewById(R.id.tv_TongTienNhan);
        this.tv_TongTienChuyen = (TextView) this.rootView.findViewById(R.id.tv_TongTienChuyen);
        this.li_dea = (LinearLayout) this.rootView.findViewById(R.id.li_dea);
        this.li_det = (LinearLayout) this.rootView.findViewById(R.id.li_det);
        this.li_dec = (LinearLayout) this.rootView.findViewById(R.id.li_dec);
        this.li_ded = (LinearLayout) this.rootView.findViewById(R.id.li_ded);
        this.li_loa = (LinearLayout) this.rootView.findViewById(R.id.li_loa);
        this.li_bca = (LinearLayout) this.rootView.findViewById(R.id.li_bca);
        this.li_xia2 = (LinearLayout) this.rootView.findViewById(R.id.li_xia2);
        this.li_xn = (LinearLayout) this.rootView.findViewById(R.id.li_xn);
        this.xi2_Nhan = (TextView) this.rootView.findViewById(R.id.xi2_Nhan);
        this.xi2_NhanAn = (TextView) this.rootView.findViewById(R.id.xi2_NhanAn);
        this.xi2_Chuyen = (TextView) this.rootView.findViewById(R.id.xi2_Chuyen);
        this.xi2_ChuyenAn = (TextView) this.rootView.findViewById(R.id.xi2_ChuyenAn);
        this.xia2_Nhan = (TextView) this.rootView.findViewById(R.id.xia2_Nhan);
        this.xia2_NhanAn = (TextView) this.rootView.findViewById(R.id.xia2_NhanAn);
        this.xia2_Chuyen = (TextView) this.rootView.findViewById(R.id.xia2_Chuyen);
        this.xia2_ChuyenAn = (TextView) this.rootView.findViewById(R.id.xia2_ChuyenAn);
        this.bca_Nhan = (TextView) this.rootView.findViewById(R.id.bca_Nhan);
        this.bca_NhanAn = (TextView) this.rootView.findViewById(R.id.bca_NhanAn);
        this.bca_Chuyen = (TextView) this.rootView.findViewById(R.id.bca_Chuyen);
        this.bca_ChuyenAn = (TextView) this.rootView.findViewById(R.id.bca_ChuyenAn);
        this.lv_baocaoKhach = (ListView) this.rootView.findViewById(R.id.lv_baocaoKhach);
    }
}
