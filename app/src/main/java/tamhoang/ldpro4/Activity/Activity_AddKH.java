package tamhoang.ldpro4.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_AddKH extends BaseToolBarActivity {
    String app_use;
    Button btn_danhba;
    Button btn_them_KH;
    JSONObject caidat_gia;
    JSONObject caidat_tg;
    Cursor cursor;
    Database db;
    EditText edt_an3c;
    EditText edt_anLo;
    EditText edt_anXN;
    EditText edt_andea;
    EditText edt_andeb;
    EditText edt_andec;
    EditText edt_anded;
    EditText edt_andet;
    EditText edt_anx2;
    EditText edt_anx3;
    EditText edt_anx4;
    EditText edt_gia3c;
    EditText edt_giaXN;
    EditText edt_giadea;
    EditText edt_giadeb;
    EditText edt_giadec;
    EditText edt_giaded;
    EditText edt_giadet;
    EditText edt_gialo;
    EditText edt_giax2;
    EditText edt_giax3;
    EditText edt_giax4;
    EditText edt_sdt;
    EditText edt_ten;
    JSONObject json;
    JSONObject json_KhongMax;
    LinearLayout linner_sodienthoai;
    RadioButton rad_chu;
    RadioButton rad_chu_khach;
    RadioButton rad_khach;
    String so_dienthoai;
    String ten_khach;
    int type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_kh;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kh);
        this.db = new Database(this);
        init();
        Intent intent = getIntent();
        this.ten_khach = intent.getStringExtra("tenKH");
        this.so_dienthoai = intent.getStringExtra("so_dienthoai");
        this.app_use = intent.getStringExtra("use_app");
        this.db = new Database(this);
        int Khachhang_moi = 0;
        if (this.ten_khach.length() > 0) {
            this.edt_ten.setText(this.ten_khach);
            this.edt_sdt.setText(this.so_dienthoai);
            Database database = this.db;
            Cursor GetData = database.GetData("Select * From tbl_kh_new where ten_kh = '" + this.ten_khach + "'");
            this.cursor = GetData;
            GetData.moveToFirst();
            Khachhang_moi = this.cursor.getCount();
        }
        if (Khachhang_moi > 0) {
            this.edt_sdt.setText(this.cursor.getString(1));
            if (this.cursor.getString(2).indexOf("sms") == -1) {
                this.linner_sodienthoai.setEnabled(false);
                this.edt_ten.setEnabled(false);
                this.edt_sdt.setEnabled(false);
                this.btn_danhba.setEnabled(false);
            }
            if (this.cursor.getCount() > 0) {
                try {
                    JSONObject jSONObject = new JSONObject(this.cursor.getString(5));
                    this.json = jSONObject;
                    JSONObject jSONObject2 = jSONObject.getJSONObject("caidat_gia");
                    this.caidat_gia = jSONObject2;
                    this.edt_giadea.setText(jSONObject2.getString("dea"));
                    this.edt_andea.setText(this.caidat_gia.getString("an_dea"));
                    this.edt_giadeb.setText(this.caidat_gia.getString("deb"));
                    this.edt_andeb.setText(this.caidat_gia.getString("an_deb"));
                    this.edt_giadec.setText(this.caidat_gia.getString("dec"));
                    this.edt_andec.setText(this.caidat_gia.getString("an_dec"));
                    this.edt_giaded.setText(this.caidat_gia.getString("ded"));
                    this.edt_anded.setText(this.caidat_gia.getString("an_ded"));
                    this.edt_giadet.setText(this.caidat_gia.getString("det"));
                    this.edt_andet.setText(this.caidat_gia.getString("an_det"));
                    this.edt_gialo.setText(this.caidat_gia.getString("lo"));
                    this.edt_anLo.setText(this.caidat_gia.getString("an_lo"));
                    this.edt_giax2.setText(this.caidat_gia.getString("gia_x2"));
                    this.edt_anx2.setText(this.caidat_gia.getString("an_x2"));
                    this.edt_giax3.setText(this.caidat_gia.getString("gia_x3"));
                    this.edt_anx3.setText(this.caidat_gia.getString("an_x3"));
                    this.edt_giax4.setText(this.caidat_gia.getString("gia_x4"));
                    this.edt_anx4.setText(this.caidat_gia.getString("an_x4"));
                    this.edt_giaXN.setText(this.caidat_gia.getString("gia_xn"));
                    this.edt_anXN.setText(this.caidat_gia.getString("an_xn"));
                    this.edt_gia3c.setText(this.caidat_gia.getString("gia_bc"));
                    this.edt_an3c.setText(this.caidat_gia.getString("an_bc"));
                    if (this.cursor.getInt(3) == 1) {
                        this.rad_khach.setChecked(true);
                        this.rad_chu.setChecked(false);
                        this.rad_chu_khach.setChecked(false);
                    } else if (this.cursor.getInt(3) == 2) {
                        this.rad_khach.setChecked(false);
                        this.rad_chu.setChecked(true);
                        this.rad_chu_khach.setChecked(false);
                    } else if (this.cursor.getInt(3) == 3) {
                        this.rad_khach.setChecked(false);
                        this.rad_chu.setChecked(false);
                        this.rad_chu_khach.setChecked(true);
                    }
                    this.json_KhongMax = new JSONObject(this.cursor.getString(6));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Cursor cursor2 = this.cursor;
                if (cursor2 != null && !cursor2.isClosed()) {
                    this.cursor.close();
                }
            }
        } else if (this.app_use.indexOf("sms") <= -1) {
            this.edt_ten.setText(this.ten_khach);
            this.edt_sdt.setText(this.so_dienthoai);
            this.edt_ten.setEnabled(false);
            this.edt_sdt.setEnabled(false);
            this.btn_danhba.setEnabled(false);
        }
        this.btn_danhba.setOnClickListener(v -> Activity_AddKH.this.startActivityForResult(new Intent("android.intent.action.PICK", ContactsContract.CommonDataKinds.Phone.CONTENT_URI), 2015));
        this.btn_them_KH.setOnClickListener(v -> {
            String str;
            String anLo;
            String str2;
            JSONException e;
            String ten_kh = Activity_AddKH.this.edt_ten.getText().toString();
            String str3 = Activity_AddKH.this.edt_sdt.getText().toString();
            if (str3.length() > 0 && ten_kh.length() > 0) {
                if (str3.startsWith("0") && Congthuc.isNumeric(str3)) {
                    str3 = "+84" + str3.substring(1);
                }
                Activity_AddKH activity_AddKH = Activity_AddKH.this;
                activity_AddKH.cursor = activity_AddKH.db.GetData("Select * From tbl_kh_new Where ten_kh <> '" + Activity_AddKH.this.edt_ten.getText().toString() + "' AND sdt = '" + str3 + "'");
                if (Activity_AddKH.this.cursor.getCount() > 0) {
                    Activity_AddKH.this.showAlertBox("Đã có số SĐT này!", "Mỗi khách hàng chỉ được dùng 1 số điện thoại và mỗi số điện thoại chỉ dùng cho 1 khách hàng.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show().setCanceledOnTouchOutside(false);
                    str = str3;
                } else {
                    String giaDea = Activity_AddKH.this.edt_giadea.getText().toString();
                    String anDea = Activity_AddKH.this.edt_andea.getText().toString();
                    String giaDeb = Activity_AddKH.this.edt_giadeb.getText().toString();
                    String anDeb = Activity_AddKH.this.edt_andeb.getText().toString();
                    String giaDec = Activity_AddKH.this.edt_giadec.getText().toString();
                    String anDec = Activity_AddKH.this.edt_andec.getText().toString();
                    String giaDed = Activity_AddKH.this.edt_giaded.getText().toString();
                    String anDed = Activity_AddKH.this.edt_anded.getText().toString();
                    String giaDet = Activity_AddKH.this.edt_giadet.getText().toString();
                    String anDet = Activity_AddKH.this.edt_andet.getText().toString();
                    String giaLo = Activity_AddKH.this.edt_gialo.getText().toString();
                    String anLo2 = Activity_AddKH.this.edt_anLo.getText().toString();
                    String giaX2 = Activity_AddKH.this.edt_giax2.getText().toString();
                    String anX2 = Activity_AddKH.this.edt_anx2.getText().toString();
                    String giaX3 = Activity_AddKH.this.edt_giax3.getText().toString();
                    String anX3 = Activity_AddKH.this.edt_anx3.getText().toString();
                    String giaX4 = Activity_AddKH.this.edt_giax4.getText().toString();
                    String anX4 = Activity_AddKH.this.edt_anx4.getText().toString();
                    String giaXN = Activity_AddKH.this.edt_giaXN.getText().toString();
                    String anXN = Activity_AddKH.this.edt_anXN.getText().toString();
                    String gia3c = Activity_AddKH.this.edt_gia3c.getText().toString();
                    String an3c = Activity_AddKH.this.edt_an3c.getText().toString();
                    if (Activity_AddKH.this.rad_khach.isChecked()) {
                        anLo = anLo2;
                        Activity_AddKH.this.type = 1;
                    } else {
                        anLo = anLo2;
                    }
                    if (Activity_AddKH.this.rad_chu.isChecked()) {
                        Activity_AddKH.this.type = 2;
                    }
                    if (Activity_AddKH.this.rad_chu_khach.isChecked()) {
                        Activity_AddKH.this.type = 3;
                    }
                    if (Activity_AddKH.this.app_use == null) {
                        Activity_AddKH.this.app_use = "sms";
                    }
                    Activity_AddKH activity_AddKH2 = Activity_AddKH.this;
                    activity_AddKH2.cursor = activity_AddKH2.db.GetData("Select * From tbl_kh_new Where ten_kh = '" + Activity_AddKH.this.edt_ten.getText().toString() + "'");
                    Activity_AddKH.this.cursor.moveToFirst();
                    if (Activity_AddKH.this.cursor.getCount() > 0) {
                        str2 = "'";
                        try {
                            Activity_AddKH.this.json = new JSONObject(Activity_AddKH.this.cursor.getString(5));
                            Activity_AddKH.this.caidat_gia = Activity_AddKH.this.json.getJSONObject("caidat_gia");
                            Activity_AddKH.this.caidat_tg = Activity_AddKH.this.json.getJSONObject("caidat_tg");
                            Activity_AddKH.this.app_use = Activity_AddKH.this.cursor.getString(2);
                            Activity_AddKH.this.json_KhongMax = new JSONObject(Activity_AddKH.this.cursor.getString(6));
                        } catch (JSONException e2) {
                            e = e2;
                        }
                    } else {
                        str2 = "'";
                        Activity_AddKH.this.json = new JSONObject();
                        Activity_AddKH.this.caidat_gia = new JSONObject();
                        Activity_AddKH.this.caidat_tg = new JSONObject();
                        Activity_AddKH.this.json_KhongMax = new JSONObject();
                        try {
                            Activity_AddKH.this.caidat_tg.put("dlgiu_de", 0);
                            Activity_AddKH.this.caidat_tg.put("dlgiu_lo", 0);
                            Activity_AddKH.this.caidat_tg.put("dlgiu_xi", 0);
                            Activity_AddKH.this.caidat_tg.put("dlgiu_xn", 0);
                            Activity_AddKH.this.caidat_tg.put("dlgiu_bc", 0);
                            Activity_AddKH.this.caidat_tg.put("khgiu_de", 0);
                            Activity_AddKH.this.caidat_tg.put("khgiu_lo", 0);
                            Activity_AddKH.this.caidat_tg.put("khgiu_xi", 0);
                            Activity_AddKH.this.caidat_tg.put("khgiu_xn", 0);
                            Activity_AddKH.this.caidat_tg.put("khgiu_bc", 0);
                            Activity_AddKH.this.caidat_tg.put("ok_tin", 3);
                            Activity_AddKH.this.caidat_tg.put("xien_nhan", 0);
                            Activity_AddKH.this.caidat_tg.put("chot_sodu", 0);
                            Activity_AddKH.this.caidat_tg.put("tg_loxien", "18:13");
                            Activity_AddKH.this.caidat_tg.put("tg_debc", "18:20");
                            Activity_AddKH.this.caidat_tg.put("loi_donvi", 0);
                            Activity_AddKH.this.caidat_tg.put("heso_de", 0);
                            Activity_AddKH.this.caidat_tg.put("maxDe", 0);
                            Activity_AddKH.this.caidat_tg.put("maxLo", 0);
                            Activity_AddKH.this.caidat_tg.put("maxXi", 0);
                            Activity_AddKH.this.caidat_tg.put("maxCang", 0);
                            Activity_AddKH.this.json_KhongMax.put("danDe", "");
                            Activity_AddKH.this.json_KhongMax.put("danLo", "");
                            Activity_AddKH.this.json_KhongMax.put("soDe", new JSONObject().toString());
                            Activity_AddKH.this.json_KhongMax.put("soLo", new JSONObject().toString());
                            Activity_AddKH.this.json_KhongMax.put("xien2", 0);
                            Activity_AddKH.this.json_KhongMax.put("xien3", 0);
                            Activity_AddKH.this.json_KhongMax.put("xien4", 0);
                            Activity_AddKH.this.json_KhongMax.put("cang", 0);
                        } catch (JSONException e16) {
                            e16.printStackTrace();
                        }
                    }
                    try {
                        Activity_AddKH.this.caidat_gia.put("dea", giaDea);
                        Activity_AddKH.this.caidat_gia.put("an_dea", anDea);
                        Activity_AddKH.this.caidat_gia.put("deb", giaDeb);
                        Activity_AddKH.this.caidat_gia.put("an_deb", anDeb);
                        Activity_AddKH.this.caidat_gia.put("det", giaDet);
                        Activity_AddKH.this.caidat_gia.put("an_det", anDet);
                        Activity_AddKH.this.caidat_gia.put("dec", giaDec);
                        Activity_AddKH.this.caidat_gia.put("an_dec", anDec);
                        Activity_AddKH.this.caidat_gia.put("ded", giaDed);
                        try {
                            Activity_AddKH.this.caidat_gia.put("an_ded", anDed);
                            Activity_AddKH.this.caidat_gia.put("lo", giaLo);
                            Activity_AddKH.this.caidat_gia.put("an_lo", anLo);
                            Activity_AddKH.this.caidat_gia.put("gia_x2", giaX2);
                            Activity_AddKH.this.caidat_gia.put("an_x2", anX2);
                        } catch (Exception e17) {
                            str = str3;
                            Toast.makeText(Activity_AddKH.this, "Sai số liệu, hãy kiểm tra lại", Toast.LENGTH_LONG).show();
                            Activity_AddKH.this.db.LayDanhsachKH();
                            Activity_AddKH.this.finish();
                        }
                        try {
                            Activity_AddKH.this.caidat_gia.put("gia_x3", giaX3);
                            Activity_AddKH.this.caidat_gia.put("an_x3", anX3);
                            Activity_AddKH.this.caidat_gia.put("gia_x4", giaX4);
                            Activity_AddKH.this.caidat_gia.put("an_x4", anX4);
                            Activity_AddKH.this.caidat_gia.put("gia_xn", giaXN);
                            Activity_AddKH.this.caidat_gia.put("an_xn", anXN);
                            Activity_AddKH.this.caidat_gia.put("gia_bc", gia3c);
                            Activity_AddKH.this.caidat_gia.put("an_bc", an3c);
                            Activity_AddKH.this.json.put("caidat_gia", Activity_AddKH.this.caidat_gia);
                            Activity_AddKH.this.json.put("caidat_tg", Activity_AddKH.this.caidat_tg);
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("REPLACE Into tbl_kh_new Values ('");
                            sb2.append(Activity_AddKH.this.edt_ten.getText().toString());
                            sb2.append("','");
                            sb2.append(str3);
                            sb2.append("','");
                            sb2.append(Activity_AddKH.this.app_use);
                            sb2.append("',");
                            sb2.append(Activity_AddKH.this.type);
                            sb2.append(",0,'");
                            sb2.append(Activity_AddKH.this.json.toString());
                            sb2.append("','");
                            sb2.append(Activity_AddKH.this.json_KhongMax.toString());
                            sb2.append("')");
                            Activity_AddKH.this.db.QueryData(sb2.toString());
                            if (Activity_AddKH.this.type != 2) {
                                Database database = Activity_AddKH.this.db;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("Delete FROM tbl_chuyenthang WHERE sdt_nhan = '");
                                sb3.append(Activity_AddKH.this.edt_sdt.getText().toString());
                                str = str3;
                                try {
                                    sb3.append(str2);
                                    database.QueryData(sb3.toString());
                                } catch (Exception e18) {
                                    Toast.makeText(Activity_AddKH.this, "Sai số liệu, hãy kiểm tra lại", Toast.LENGTH_LONG).show();
                                    Activity_AddKH.this.db.LayDanhsachKH();
                                    Activity_AddKH.this.finish();
                                }
                            } else {
                                str = str3;
                            }
                            if (Activity_AddKH.this.cursor.getCount() <= 0) {
                                Toast.makeText(Activity_AddKH.this, "Đã cập nhật thông tin!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Activity_AddKH.this, "Đã thêm khách hàng!", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e19) {
                            str = str3;
                            Toast.makeText(Activity_AddKH.this, "Sai số liệu, hãy kiểm tra lại", Toast.LENGTH_LONG).show();
                            Activity_AddKH.this.db.LayDanhsachKH();
                            Activity_AddKH.this.finish();
                        }
                    } catch (Exception e20) {
                        str = str3;
                        Toast.makeText(Activity_AddKH.this, "Sai số liệu, hãy kiểm tra lại", Toast.LENGTH_LONG).show();
                        Activity_AddKH.this.db.LayDanhsachKH();
                        Activity_AddKH.this.finish();
                    }
                    Activity_AddKH.this.db.LayDanhsachKH();
                    Activity_AddKH.this.finish();
                }
            }
        });
    }

    public AlertDialog.Builder showAlertBox(String title, String message) {
        return new AlertDialog.Builder(this).setTitle(title).setMessage(message);
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2015 && i2 == -1) {
            Cursor query = getContentResolver().query(intent.getData(), null, null, null, null);
            query.moveToFirst();
            int columnIndex = query.getColumnIndex("data1");
            query.getColumnIndex("display_name");
            String name = query.getString(query.getColumnIndex("display_name"));
            String sdt = query.getString(columnIndex).replaceAll(" ", "");
            if (sdt.length() < 12) {
                sdt = "+84" + sdt.substring(1);
            }
            this.edt_sdt.setText(sdt);
            this.edt_ten.setText(name);
        }
    }

    public void init() {
        this.linner_sodienthoai = findViewById(R.id.linner_sodienthoai);
        this.edt_ten = findViewById(R.id.edt_ten);
        this.edt_sdt = findViewById(R.id.edt_sdt);
        this.rad_chu = findViewById(R.id.rad_chu);
        this.rad_khach = findViewById(R.id.rad_khach);
        this.rad_chu_khach = findViewById(R.id.rad_chu_khach);
        this.edt_giadea = findViewById(R.id.edt_giadea);
        this.edt_andea = findViewById(R.id.edt_andea);
        this.edt_giadeb = findViewById(R.id.edt_giadeb);
        this.edt_andeb = findViewById(R.id.edt_andeb);
        this.edt_giadec = findViewById(R.id.edt_giadec);
        this.edt_andec = findViewById(R.id.edt_andec);
        this.edt_giaded = findViewById(R.id.edt_giaded);
        this.edt_anded = findViewById(R.id.edt_anded);
        this.edt_giadet = findViewById(R.id.edt_giadet);
        this.edt_andet = findViewById(R.id.edt_andet);
        this.edt_gialo = findViewById(R.id.edt_giaLo);
        this.edt_anLo = findViewById(R.id.edt_anLo);
        this.edt_giax2 = findViewById(R.id.edt_giaXien2);
        this.edt_anx2 = findViewById(R.id.edt_anXien2);
        this.edt_giax3 = findViewById(R.id.edt_giaXien3);
        this.edt_anx3 = findViewById(R.id.edt_anXien3);
        this.edt_giax4 = findViewById(R.id.edt_giaXien4);
        this.edt_anx4 = findViewById(R.id.edt_anXien4);
        this.edt_giaXN = findViewById(R.id.edt_giaXienNhay);
        this.edt_anXN = findViewById(R.id.edt_anXienNhay);
        this.edt_gia3c = findViewById(R.id.edt_gia3c);
        this.edt_an3c = findViewById(R.id.edt_an3c);
        this.btn_them_KH = findViewById(R.id.btn_them_KH);
        this.btn_danhba = findViewById(R.id.btn_danhba);
    }
}