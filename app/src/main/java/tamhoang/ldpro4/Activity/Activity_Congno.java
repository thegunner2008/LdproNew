package tamhoang.ldpro4.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_Congno extends BaseToolBarActivity {
    Button btn_congno;
    Database db;
    ListView lv_congno;
    public List<String> mKetQua = new ArrayList();
    public List<String> mLuy_ke = new ArrayList();
    public List<String> mNgay = new ArrayList();
    public List<String> mNgayNhan = new ArrayList();
    public List<String> mSdt = new ArrayList();
    public List<String> mThanhToan = new ArrayList();
    String message;
    TextView tv_tenKH;

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity
    public int getLayoutId() {
        return R.layout.activity_congno;
    }

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congno);
        this.db = new Database(this);
        this.lv_congno = (ListView) findViewById(R.id.lv_congno);
        this.tv_tenKH = (TextView) findViewById(R.id.tv_tenKH);
        this.btn_congno = (Button) findViewById(R.id.btn_congno);
        this.message = getIntent().getStringExtra("tenKH");
        TextView textView = this.tv_tenKH;
        textView.setText("Khách hàng: " + this.message);
        this.btn_congno.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_Congno.AnonymousClass1 */

            public void onClick(View view) {
                if (!Activity_Congno.this.isFinishing()) {
                    Activity_Congno.this.showDialog1(1);
                }
            }
        });
        this.lv_congno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_Congno.AnonymousClass2 */

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Activity_Congno.this.isFinishing()) {
                    Activity_Congno.this.showDialog2(i);
                }
            }
        });
        Congno_report_listview();
    }

    public void showDialog1(int poin) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.frag_morp1_1);
        dialog.getWindow().setLayout(-1, -2);
        final EditText edt_thanhtoan = (EditText) dialog.findViewById(R.id.edt_thanhtoan);
        Button btn_chinhsua = (Button) dialog.findViewById(R.id.btn_chinhsua);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        Database database = this.db;
        Cursor cursor = database.GetData("Select sum(ket_qua)/1000 From tbl_soctS WHere ten_kh = '" + this.message + "' AND the_loai = 'cn'");
        cursor.moveToFirst();
        edt_thanhtoan.setText(decimalFormat.format(cursor.getDouble(0)));
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        edt_thanhtoan.addTextChangedListener(new TextWatcher() {
            /* class tamhoang.ldpro4.Activity.Activity_Congno.AnonymousClass3 */
            int len = 0;
            String str;

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                this.len = edt_thanhtoan.getText().toString().length();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String obj = edt_thanhtoan.getText().toString();
                this.str = obj;
                if (obj.length() == 0) {
                    edt_thanhtoan.setText("0");
                } else if (this.len != this.str.length() && this.len > 2) {
                    try {
                        DecimalFormat decimalFormat = new DecimalFormat("###,###");
                        String replaceAll = this.str.replaceAll("[$,.]", "");
                        this.str = replaceAll;
                        String format = decimalFormat.format(Double.parseDouble(replaceAll));
                        this.str = format;
                        edt_thanhtoan.setText(format);
                        edt_thanhtoan.setSelection(this.str.length());
                    } catch (Exception e) {
                    }
                }
            }
        });
        btn_chinhsua.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_Congno.AnonymousClass4 */

            public void onClick(View view) {
                if (Congthuc.isNumeric(edt_thanhtoan.getText().toString().replaceAll("\\.", "").replace("-", ""))) {
                    Database database = Activity_Congno.this.db;
                    Cursor cursor = database.GetData("Select count(id) From tbl_soctS WHere ten_kh = '" + Activity_Congno.this.message + "' AND the_loai = 'cn'");
                    cursor.moveToFirst();
                    if (cursor.getInt(0) == 0) {
                        Database database2 = Activity_Congno.this.db;
                        Cursor c = database2.GetData("Select min(ngay_nhan), so_dienthoai From tbl_soctS Where ten_kh = '" + Activity_Congno.this.message + "'");
                        c.moveToFirst();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar calendar = Calendar.getInstance();
                        try {
                            calendar.setTime(sdf.parse(c.getString(0)));
                        } catch (Exception e) {
                        }
                        calendar.add(5, -1);
                        String Ngay = sdf.format(new Date(calendar.getTimeInMillis()));
                        Activity_Congno.this.db.QueryData("Insert Into tbl_soctS (ngay_nhan, ten_kh, so_dienthoai, the_loai, ket_qua, diem_quydoi) Values ('" + Ngay + "','" + Activity_Congno.this.message + "','" + c.getString(1) + "', 'cn'," + edt_thanhtoan.getText().toString().replaceAll("\\.", "") + "000,1)");
                        Activity_Congno.this.Congno_report_listview();
                        if (c != null && !c.isClosed()) {
                            c.close();
                        }
                    } else {
                        Activity_Congno.this.db.QueryData("Update tbl_soctS set ket_qua = " + edt_thanhtoan.getText().toString().replaceAll("\\.", "") + "000 WHere ten_kh = '" + Activity_Congno.this.message + "' AND the_loai = 'cn'");
                        Activity_Congno.this.Congno_report_listview();
                    }
                    dialog.cancel();
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public void showDialog2(final int poin) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.frag_morp1_2);
        dialog.getWindow().setLayout(-1, -2);
        final EditText edt_thanhtoan = (EditText) dialog.findViewById(R.id.edt_thanhtoan);
        Button btn_chinhsua = (Button) dialog.findViewById(R.id.btn_chinhsua);
        ((TextView) dialog.findViewById(R.id.tv_ngaytt)).setText(this.mNgay.get(poin));
        edt_thanhtoan.addTextChangedListener(new TextWatcher() {
            /* class tamhoang.ldpro4.Activity.Activity_Congno.AnonymousClass5 */
            int len = 0;
            String str;

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                this.len = edt_thanhtoan.getText().toString().length();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String obj = edt_thanhtoan.getText().toString();
                this.str = obj;
                if (obj.length() == 0) {
                    edt_thanhtoan.setText("0");
                } else if (this.len != this.str.length() && this.len > 2) {
                    try {
                        DecimalFormat decimalFormat = new DecimalFormat("###,###");
                        String replaceAll = this.str.replaceAll("[$,.]", "");
                        this.str = replaceAll;
                        String format = decimalFormat.format(Double.parseDouble(replaceAll));
                        this.str = format;
                        edt_thanhtoan.setText(format);
                        edt_thanhtoan.setSelection(this.str.length());
                    } catch (Exception e) {
                    }
                }
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        Database database = this.db;
        Cursor cursor = database.GetData("Select sum(ket_qua)/1000 From tbl_soctS WHere ten_kh = '" + this.message + "' AND the_loai = 'tt' And ngay_nhan = '" + this.mNgayNhan.get(poin) + "'");
        cursor.moveToFirst();
        edt_thanhtoan.setText(decimalFormat.format(cursor.getDouble(0)));
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        btn_chinhsua.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_Congno.AnonymousClass6 */

            public void onClick(View view) {
                if (Congthuc.isNumeric(edt_thanhtoan.getText().toString().replaceAll("\\.", "").replace("-", ""))) {
                    Database database = Activity_Congno.this.db;
                    Cursor cursor = database.GetData("Select count(id) From tbl_soctS WHere ten_kh = '" + Activity_Congno.this.message + "' AND the_loai = 'tt' AND ngay_nhan = '" + Activity_Congno.this.mNgayNhan.get(poin) + "'");
                    cursor.moveToFirst();
                    if (cursor.getInt(0) == 0) {
                        Activity_Congno.this.db.QueryData("Insert Into tbl_soctS (ngay_nhan, ten_kh, so_dienthoai, the_loai, ket_qua, diem_quydoi) Values ('" + Activity_Congno.this.mNgayNhan.get(poin) + "','" + Activity_Congno.this.message + "','" + Activity_Congno.this.mSdt.get(poin) + "', 'tt'," + edt_thanhtoan.getText().toString().replaceAll("\\.", "") + "000,1)");
                        Activity_Congno.this.Congno_report_listview();
                    } else {
                        Activity_Congno.this.db.QueryData("Update tbl_soctS set ket_qua = " + edt_thanhtoan.getText().toString().replaceAll("\\.", "") + "000 WHere ten_kh = '" + Activity_Congno.this.message + "' AND the_loai = 'tt' AND ngay_nhan = '" + Activity_Congno.this.mNgayNhan.get(poin) + "'");
                        Activity_Congno.this.Congno_report_listview();
                    }
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                    dialog.cancel();
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public void Congno_report_listview() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        this.mNgayNhan.clear();
        this.mSdt.clear();
        this.mNgay.clear();
        this.mKetQua.clear();
        this.mThanhToan.clear();
        this.mLuy_ke.clear();
        Cursor cursor = this.db.GetData("Select ngay_nhan, so_dienthoai,strftime('%d/%m/%Y',ngay_nhan) as Ngay\n, sum((the_loai <> 'tt') *ket_qua*(100 - diem_khachgiu)/100)/1000 as KQ \n, sum((the_loai = 'tt') *ket_qua)/1000 as TT \n, (Select sum(ket_qua*(100 - diem_khachgiu)/100) FROM tbl_soctS t2 \nWHERE tbl_soctS.ngay_nhan >= t2.ngay_nhan And tbl_soctS.ten_kh = t2.ten_kh)/1000 AS luy_ke \nFROM tbl_soctS \nWHERE ten_kh = '" + this.message + "' \nGROUP BY ngay_nhan ORDER BY ngay_nhan");
        while (cursor.moveToNext()) {
            this.mNgayNhan.add(cursor.getString(0));
            this.mSdt.add(cursor.getString(1));
            this.mNgay.add(cursor.getString(2));
            this.mKetQua.add(decimalFormat.format(cursor.getDouble(3)));
            this.mThanhToan.add(decimalFormat.format(cursor.getDouble(4)));
            this.mLuy_ke.add(decimalFormat.format(cursor.getDouble(5)));
        }
        this.lv_congno.setAdapter((ListAdapter) new Congno_Adapter(this, R.layout.activity_congno_lv, this.mNgay));
    }

    /* access modifiers changed from: package-private */
    public class Congno_Adapter extends ArrayAdapter {
        public Congno_Adapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_congno_lv, (ViewGroup) null);
            ((TextView) v.findViewById(R.id.tv_ngay_thanhtoan)).setText(Activity_Congno.this.mNgay.get(position));
            ((TextView) v.findViewById(R.id.tv_phatsinh)).setText(Activity_Congno.this.mKetQua.get(position));
            ((TextView) v.findViewById(R.id.tv_thanhtoan)).setText(Activity_Congno.this.mThanhToan.get(position));
            ((TextView) v.findViewById(R.id.tv_luyke)).setText(Activity_Congno.this.mLuy_ke.get(position));
            return v;
        }
    }
}