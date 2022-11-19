package tamhoang.ldpro4.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_AddKH2 extends BaseToolBarActivity {
    String[] baoloi_donvi;
    SeekBar bc_dly;
    SeekBar bc_khach;
    Button btn_exit;
    JSONObject caidat_tg;
    String[] chot_sodu;
    Database db;
    SeekBar de_dly;
    SeekBar de_khach;
    String[] dv_nhanXien;
    TextView edt_ten;
    String[] heso_de;
    JSONObject json;
    JSONObject json_KhongMax;
    String[] khach_de;
    LinearLayout li_nhanxien;
    SeekBar lo_dly;
    SeekBar lo_khach;
    String message;
    TextView pt_giu_bc_dly;
    TextView pt_giu_bc_khach;
    TextView pt_giu_de_dly;
    TextView pt_giu_de_khach;
    TextView pt_giu_lo_dly;
    TextView pt_giu_lo_khach;
    TextView pt_giu_xi_dly;
    TextView pt_giu_xi_khach;
    Spinner sp_Chot_sodu;
    Spinner sp_baoloidonvi;
    Spinner sp_hesode;
    Spinner sp_khachde;
    Spinner sp_nhanXien;
    Spinner sp_traloitn;
    String[] tl_tinnhan;
    TextView tv_KhongMax;
    TextView tv_Lo_xien;
    TextView tv_de_cang;
    SeekBar xi_dly;
    SeekBar xi_khach;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_kh2;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        String DanGiu;
        String DanGiu2;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kh2);
        this.db = new Database(this);
        init();
        String stringExtra = getIntent().getStringExtra("tenKH");
        this.message = stringExtra;
        if (stringExtra.length() > 0) {
            this.edt_ten.setText(this.message);
            this.tl_tinnhan = new String[]{"1. Ok tin và nd phân tích", "2. Chỉ ok tin", "3. Không trả lời", "4. Ok tin nguyên mẫu", "5. Chỉ ok tin (ngay khi nhận)", "6. OK nguyên mẫu (ngay khi nhận)"};
            this.sp_traloitn.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, this.tl_tinnhan));
            this.dv_nhanXien = new String[]{"1. Giữ nguyên giá", "2. Nhân 10 khi là điểm", "3. Nhân 10 tất cả xiên"};
            this.sp_nhanXien.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, this.dv_nhanXien));
            this.chot_sodu = new String[]{"1. Chốt tiền trong ngày", "2. Chốt có công nợ"};
            this.sp_Chot_sodu.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, this.chot_sodu));
            this.baoloi_donvi = new String[]{"1. Ko báo lỗi sai đơn vị", "2. Báo lỗi khi sai đơn vị"};
            this.sp_baoloidonvi.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, this.baoloi_donvi));
            this.khach_de = new String[]{"1. Thường (de = deb, de8 = det)", "2. Đề 8 (de = det)"};
            this.sp_khachde.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, this.khach_de));
            this.heso_de = new String[]{"1. Giữ nguyên (HS=1)", "2. Đề 8->7 (HS=1,143)", "3. Đề 7->8 (HS=0,875)"};
            this.sp_hesode.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, this.heso_de));
            Cursor cursor = this.db.GetData("Select * From tbl_kh_new WHERE ten_kh ='" + this.message + "'");
            cursor.moveToFirst();
            try {
                JSONObject jSONObject = new JSONObject(cursor.getString(5));
                this.json = jSONObject;
                JSONObject jSONObject2 = jSONObject.getJSONObject("caidat_tg");
                this.caidat_tg = jSONObject2;
                this.sp_traloitn.setSelection(jSONObject2.getInt("ok_tin"));
                this.sp_nhanXien.setSelection(this.caidat_tg.getInt("xien_nhan"));
                this.sp_Chot_sodu.setSelection(this.caidat_tg.getInt("chot_sodu"));
                this.tv_Lo_xien.setText(this.caidat_tg.getString("tg_loxien"));
                this.tv_de_cang.setText(this.caidat_tg.getString("tg_debc"));
                this.sp_hesode.setSelection(this.caidat_tg.getInt("heso_de"));
                try {
                    this.sp_khachde.setSelection(this.caidat_tg.getInt("khach_de"));
                } catch (JSONException e) {
                    this.caidat_tg.put("khach_de", 0);
                }
                this.sp_baoloidonvi.setSelection(this.caidat_tg.getInt("loi_donvi"));
                this.pt_giu_de_dly.setText(this.caidat_tg.getInt("dlgiu_de") + "%");
                this.pt_giu_lo_dly.setText(this.caidat_tg.getInt("dlgiu_lo") + "%");
                this.pt_giu_xi_dly.setText(this.caidat_tg.getInt("dlgiu_xi") + "%");
                this.pt_giu_bc_dly.setText(this.caidat_tg.getInt("dlgiu_bc") + "%");
                this.de_dly.setProgress(this.caidat_tg.getInt("dlgiu_de") / 5);
                this.lo_dly.setProgress(this.caidat_tg.getInt("dlgiu_lo") / 5);
                this.xi_dly.setProgress(this.caidat_tg.getInt("dlgiu_xi") / 5);
                this.bc_dly.setProgress(this.caidat_tg.getInt("dlgiu_bc") / 5);
                this.pt_giu_de_khach.setText(this.caidat_tg.getInt("khgiu_de") + "%");
                this.pt_giu_lo_khach.setText(this.caidat_tg.getInt("khgiu_lo") + "%");
                this.pt_giu_xi_khach.setText(this.caidat_tg.getInt("khgiu_xi") + "%");
                this.pt_giu_bc_khach.setText(this.caidat_tg.getInt("khgiu_bc") + "%");
                this.de_khach.setProgress(this.caidat_tg.getInt("khgiu_de") / 5);
                this.lo_khach.setProgress(this.caidat_tg.getInt("khgiu_lo") / 5);
                this.xi_khach.setProgress(this.caidat_tg.getInt("khgiu_xi") / 5);
                this.bc_khach.setProgress(this.caidat_tg.getInt("khgiu_bc") / 5);
                JSONObject jSONObject3 = new JSONObject(cursor.getString(6));
                this.json_KhongMax = jSONObject3;
                if (jSONObject3.getString("danDe").length() == 0) {
                    DanGiu = "" + "  Đề: Không khống";
                } else {
                    DanGiu = "" + "  Đề: " + this.json_KhongMax.getString("danDe");
                }
                if (this.json_KhongMax.getString("danLo").length() == 0) {
                    DanGiu2 = DanGiu + "\n  Lô: Không khống";
                } else {
                    DanGiu2 = DanGiu + "\n  Lô: " + this.json_KhongMax.getString("danLo");
                }
                this.tv_KhongMax.setText((((DanGiu2 + "\n  Xiên 2: " + this.json_KhongMax.getString("xien2")) + "\n  Xiên 3: " + this.json_KhongMax.getString("xien3")) + "\n  Xiên 4: " + this.json_KhongMax.getString("xien4")) + "\n  Càng: " + this.json_KhongMax.getString("cang"));
                if (!cursor.isClosed()) cursor.close();

            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        this.sp_traloitn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    caidat_tg.put("ok_tin", i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        this.sp_nhanXien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    caidat_tg.put("xien_nhan", i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        this.sp_Chot_sodu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    caidat_tg.put("chot_sodu", i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        this.sp_khachde.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    caidat_tg.put("khach_de", i);
                } catch (JSONException e) {
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        this.sp_hesode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    caidat_tg.put("heso_de", i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        this.sp_baoloidonvi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    caidat_tg.put("loi_donvi", i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.btn_exit.setOnClickListener(view -> {
            try {
                json.put("caidat_tg", caidat_tg);
                Database database = db;
                database.QueryData("update tbl_kh_new set tbl_MB = '" + json.toString() + "', tbl_XS = '" + json_KhongMax.toString() + "' WHERE ten_kh = '" + message + "'");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finish();
        });
        this.de_khach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = pt_giu_de_khach;
                textView.setText((progress * 5) + "%");
                this.max = progress * 5;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("khgiu_de", this.max);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                Toast.makeText(activity_AddKH2, "Giữ cho khách " + this.max + "%", Toast.LENGTH_LONG).show();
            }
        });
        this.lo_khach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = pt_giu_lo_khach;
                textView.setText((progress * 5) + "%");
                this.max = progress * 5;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("khgiu_lo", this.max);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                Toast.makeText(activity_AddKH2, "Giữ cho khách " + this.max + "%", Toast.LENGTH_LONG).show();
            }
        });
        this.xi_khach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = pt_giu_xi_khach;
                textView.setText((progress * 5) + "%");
                this.max = progress * 5;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("khgiu_xi", this.max);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                Toast.makeText(activity_AddKH2, "Giữ cho khách " + this.max + "%", Toast.LENGTH_LONG).show();
            }
        });
        this.bc_khach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = pt_giu_bc_khach;
                textView.setText((progress * 5) + "%");
                this.max = progress * 5;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("khgiu_bc", this.max);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                Toast.makeText(activity_AddKH2, "Giữ cho khách " + this.max + "%", Toast.LENGTH_LONG).show();
            }
        });
        this.de_dly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /* class tamhoang.ldpro4.Activity.Activity_AddKH2.AnonymousClass12 */
            int max;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = pt_giu_de_dly;
                textView.setText((progress * 5) + "%");
                this.max = progress * 5;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("dlgiu_de", this.max);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                Toast.makeText(activity_AddKH2, "Mình giữ " + this.max + "%", Toast.LENGTH_LONG).show();
            }
        });
        this.lo_dly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = pt_giu_lo_dly;
                textView.setText((progress * 5) + "%");
                this.max = progress * 5;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("dlgiu_lo", this.max);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                Toast.makeText(activity_AddKH2, "Mình giữ " + this.max + "%", Toast.LENGTH_LONG).show();
            }
        });
        this.xi_dly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = pt_giu_xi_dly;
                textView.setText((progress * 5) + "%");
                this.max = progress * 5;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("dlgiu_xi", this.max);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                Toast.makeText(activity_AddKH2, "Mình giữ " + this.max + "%", Toast.LENGTH_LONG).show();
            }
        });
        this.bc_dly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int max;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = pt_giu_bc_dly;
                textView.setText((progress * 5) + "%");
                this.max = progress * 5;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    caidat_tg.put("dlgiu_bc", this.max);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                Toast.makeText(activity_AddKH2, "Mình giữ " + this.max + "%", Toast.LENGTH_LONG).show();
            }
        });
        this.tv_Lo_xien.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            TimePickerDialog mTimePicker = new TimePickerDialog(Activity_AddKH2.this, (timePicker, selectedHour, selectedMinute) -> {
                if (selectedMinute < 10) {
                    TextView textView = tv_Lo_xien;
                    textView.setText(selectedHour + ":0" + selectedMinute);
                    Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                    Toast.makeText(activity_AddKH2, "Đặt không nhận lô, xiên sau: " + selectedHour + ":0" + selectedMinute, Toast.LENGTH_LONG).show();
                } else {
                    TextView textView2 = tv_Lo_xien;
                    textView2.setText(selectedHour + ":" + selectedMinute);
                    Activity_AddKH2 activity_AddKH22 = Activity_AddKH2.this;
                    Toast.makeText(activity_AddKH22, "Đặt không nhận lô, xiên sau: " + selectedHour + ":" + selectedMinute, Toast.LENGTH_LONG).show();
                }
                try {
                    caidat_tg.put("tg_loxien", tv_Lo_xien.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, mcurrentTime.get(11), mcurrentTime.get(12), true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
        this.tv_de_cang.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            TimePickerDialog mTimePicker = new TimePickerDialog(Activity_AddKH2.this, (timePicker, selectedHour, selectedMinute) -> {
                if (selectedMinute < 10) {
                    TextView textView = tv_de_cang;
                    textView.setText(selectedHour + ":0" + selectedMinute);
                    Activity_AddKH2 activity_AddKH2 = Activity_AddKH2.this;
                    Toast.makeText(activity_AddKH2, "Đặt không nhận đề/càng sau: " + selectedHour + ":0" + selectedMinute, Toast.LENGTH_LONG).show();
                } else {
                    TextView textView2 = tv_de_cang;
                    textView2.setText(selectedHour + ":" + selectedMinute);
                    Activity_AddKH2 activity_AddKH22 = Activity_AddKH2.this;
                    Toast.makeText(activity_AddKH22, "Đặt không nhận đề/càng sau: " + selectedHour + ":" + selectedMinute, Toast.LENGTH_LONG).show();
                }
                try {
                    caidat_tg.put("tg_debc", tv_de_cang.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, mcurrentTime.get(11), mcurrentTime.get(12), true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
        this.tv_KhongMax.setOnClickListener(v -> showDialog2());
    }

    public void showDialog2() {
        JSONException e;
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.frag_khongmax);
//        dialog.getWindow().setLayout(-1, -2);
        Button btnThemdanDe = dialog.findViewById(R.id.btn_KhongDe);
        Button btnXoaDanDe = dialog.findViewById(R.id.btn_XoaDe);
        Button btnThemdanLo = dialog.findViewById(R.id.btn_KhongLo);
        Button btnXoaDanLo = dialog.findViewById(R.id.btn_XoaLo);
        Button btnThemXien = dialog.findViewById(R.id.btn_KhongXienCang);
        Button btnXoaXien = dialog.findViewById(R.id.btn_XoaXien);
        final EditText edt_NhapDanDe = dialog.findViewById(R.id.edt_NhapDanDe);
        final EditText edt_NhapDanLo = dialog.findViewById(R.id.edt_NhapDanLo);
        final EditText giuxien2 = dialog.findViewById(R.id.giuxien2);
        final EditText giuxien3 = dialog.findViewById(R.id.giuxien3);
        final EditText giuxien4 = dialog.findViewById(R.id.giuxien4);
        final EditText giu3cang = dialog.findViewById(R.id.giu3cang);
        try {
            edt_NhapDanDe.setText(this.json_KhongMax.getString("danDe"));
            edt_NhapDanLo.setText(this.json_KhongMax.getString("danLo"));
            giuxien2.setText(this.json_KhongMax.getString("xien2"));
            giuxien3.setText(this.json_KhongMax.getString("xien3"));
            giuxien4.setText(this.json_KhongMax.getString("xien4"));
            giu3cang.setText(this.json_KhongMax.getString("cang"));
        } catch (JSONException e2) {
            e = e2;
        }
        btnThemdanDe.setOnClickListener(v -> {
            boolean ktra = true;
            String str = "de " + edt_NhapDanDe.getText().toString();
            int i = 1;
            if (str.length() > 7) {
                try {
                    str = Congthuc.NhanTinNhan(Congthuc.convertKhongDau(str)).replace("de dit db:", "de:");
                    if (str.indexOf("Không hiểu") > -1) {
                        Toast.makeText(Activity_AddKH2.this, str, Toast.LENGTH_LONG).show();
                        ktra = false;
                    }
                } catch (Exception e1) {
                    Toast.makeText(Activity_AddKH2.this, "Thêm bị lỗi, hãy sửa lại", Toast.LENGTH_LONG).show();
                    ktra = false;
                }
            }
            if (ktra) {
                try {
                    if (str.length() > 7) {
                        json_KhongMax.put("danDe", edt_NhapDanDe.getText().toString().replaceAll("\n", " "));
                        JSONObject json_sole = new JSONObject();
                        while (true) {
                            String str1 = str.substring(0, str.indexOf("\n") + i);
                            String str6 = str.substring(str1.indexOf(":") + i, str1.indexOf("\n") + i);
                            String[] str2 = str6.substring(0, str6.indexOf(",x")).split(",");
                            String str3 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                            for (String So_chon : str2) {
                                if (!json_sole.has(So_chon)) {
                                    json_sole.put(So_chon, str3);
                                } else if (Integer.parseInt(str3) < json_sole.getInt(So_chon)) {
                                    json_sole.put(So_chon, str3);
                                }
                            }
                            str = str.replaceAll(str1, "");
                            if (str.length() <= 0) {
                                break;
                            }
                            i = 1;
                        }
                        json_KhongMax.put("soDe", json_sole.toString());
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                UPdate();
            }
        });
        btnXoaDanDe.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_AddKH2.AnonymousClass20 */

            public void onClick(View v) {
                try {
                    json_KhongMax.put("danDe", "");
                    json_KhongMax.put("soDe", new JSONObject().toString());
                    edt_NhapDanDe.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Throwable th) {
                    UPdate();
                    throw th;
                }
                UPdate();
            }
        });
        btnThemdanLo.setOnClickListener(v -> {
            boolean ktra = true;
            String str = "de " + edt_NhapDanLo.getText().toString();
            int i = 1;
            if (str.length() > 7) {
                try {
                    str = Congthuc.NhanTinNhan(Congthuc.convertKhongDau(str)).replace("de dit db:", "de:");
                    if (str.indexOf("Không hiểu") > -1) {
                        Toast.makeText(Activity_AddKH2.this, str, Toast.LENGTH_LONG).show();
                        ktra = false;
                    }
                } catch (Exception e12) {
                    Toast.makeText(Activity_AddKH2.this, "Thêm bị lỗi, hãy sửa lại", Toast.LENGTH_LONG).show();
                    ktra = false;
                }
            }
            if (ktra) {
                try {
                    if (str.length() > 7) {
                        json_KhongMax.put("danLo", edt_NhapDanLo.getText().toString().replaceAll("\n", " "));
                        JSONObject json_sole = new JSONObject();
                        while (true) {
                            String str1 = str.substring(0, str.indexOf("\n") + i);
                            String str6 = str.substring(str1.indexOf(":") + i, str1.indexOf("\n") + i);
                            String[] str2 = str6.substring(0, str6.indexOf(",x")).split(",");
                            String str3 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                            for (String So_chon : str2) {
                                if (!json_sole.has(So_chon)) {
                                    json_sole.put(So_chon, str3);
                                } else if (Integer.parseInt(str3) < json_sole.getInt(So_chon)) {
                                    json_sole.put(So_chon, str3);
                                }
                            }
                            str = str.replaceAll(str1, "");
                            if (str.length() <= 0) {
                                break;
                            }
                            i = 1;
                        }
                        json_KhongMax.put("soLo", json_sole.toString());
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                UPdate();
            }
        });
        btnXoaDanLo.setOnClickListener(v -> {
            try {
                json_KhongMax.put("danLo", "");
                json_KhongMax.put("soLo", new JSONObject().toString());
                edt_NhapDanLo.setText("");
            } catch (JSONException e13) {
                e13.printStackTrace();
            } catch (Throwable th) {
                UPdate();
                throw th;
            }
            UPdate();
        });
        btnThemXien.setOnClickListener(v -> {
            try {
                json_KhongMax.put("xien2", giuxien2.getText().toString());
                json_KhongMax.put("xien3", giuxien3.getText().toString());
                json_KhongMax.put("xien4", giuxien4.getText().toString());
                json_KhongMax.put("cang", giu3cang.getText().toString());
            } catch (JSONException e14) {
                e14.printStackTrace();
            } catch (Throwable th) {
                UPdate();
                throw th;
            }
            UPdate();
        });
        btnXoaXien.setOnClickListener(v -> {
            try {
                json_KhongMax.put("xien2", 0);
                json_KhongMax.put("xien3", 0);
                json_KhongMax.put("xien4", 0);
                json_KhongMax.put("cang", 0);
                giuxien2.setText("0");
                giuxien3.setText("0");
                giuxien4.setText("0");
                giu3cang.setText("0");
            } catch (JSONException e15) {
                e15.printStackTrace();
            } catch (Throwable th) {
                UPdate();
                throw th;
            }
            UPdate();
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public void UPdate() {
        String DanGiu;
        String DanGiu2;
        try {
            if (this.json_KhongMax.getString("danDe").length() == 0) {
                DanGiu = "" + "  Đề: Không khống";
            } else {
                DanGiu = "" + "  Đề: " + this.json_KhongMax.getString("danDe");
            }
            if (this.json_KhongMax.getString("danLo").length() == 0) {
                DanGiu2 = DanGiu + "\n  Lô: Không khống";
            } else {
                DanGiu2 = DanGiu + "\n  Lô: " + this.json_KhongMax.getString("danLo");
            }
            this.tv_KhongMax.setText((((DanGiu2 + "\n  Xiên 2: " + this.json_KhongMax.getString("xien2")) + "\n  Xiên 3: " + this.json_KhongMax.getString("xien3")) + "\n  Xiên 4: " + this.json_KhongMax.getString("xien4")) + "\n  Càng: " + this.json_KhongMax.getString("cang"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        this.edt_ten = findViewById(R.id.edt_ten);
        this.btn_exit = findViewById(R.id.btn_exit_KH2);
        this.pt_giu_de_khach = findViewById(R.id.pt_giu_de_khach);
        this.pt_giu_lo_khach = findViewById(R.id.pt_giu_lo_khach);
        this.pt_giu_xi_khach = findViewById(R.id.pt_giu_xi_khach);
        this.pt_giu_bc_khach = findViewById(R.id.pt_giu_bc_khach);
        this.pt_giu_de_dly = findViewById(R.id.pt_giu_de_dly);
        this.pt_giu_lo_dly = findViewById(R.id.pt_giu_lo_dly);
        this.pt_giu_xi_dly = findViewById(R.id.pt_giu_xi_dly);
        this.pt_giu_bc_dly = findViewById(R.id.pt_giu_bc_dly);
        this.de_khach = findViewById(R.id.seek_GiuDekhach);
        this.lo_khach = findViewById(R.id.seek_GiuLokhach);
        this.xi_khach = findViewById(R.id.seek_GiuXikhach);
        this.bc_khach = findViewById(R.id.seek_Giu3ckhach);
        this.de_dly = findViewById(R.id.seek_GiuDedly);
        this.lo_dly = findViewById(R.id.seek_GiuLodly);
        this.xi_dly = findViewById(R.id.seek_GiuXidly);
        this.bc_dly = findViewById(R.id.seek_Giu3cdly);
        this.sp_traloitn = findViewById(R.id.sp_traloitn);
        this.sp_nhanXien = findViewById(R.id.sp_nhanXien);
        this.sp_Chot_sodu = findViewById(R.id.sp_Chot_sodu);
        this.sp_hesode = findViewById(R.id.sp_hesode);
        this.sp_baoloidonvi = findViewById(R.id.sp_baoloidonvi);
        this.sp_khachde = findViewById(R.id.sp_khachde);
        this.li_nhanxien = findViewById(R.id.ln_nhanXien);
        this.tv_Lo_xien = findViewById(R.id.tv_Lo_xien);
        this.tv_de_cang = findViewById(R.id.tv_de_cang);
        this.tv_KhongMax = findViewById(R.id.tv_KhongMax);
    }
}