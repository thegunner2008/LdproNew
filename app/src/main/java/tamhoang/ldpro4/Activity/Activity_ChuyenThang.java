package tamhoang.ldpro4.Activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_ChuyenThang extends BaseToolBarActivity {
    Button add_chuyen;
    Database db;
    ListView lv_chuyenthang;
    public List<String> nameChu = new ArrayList();
    public List<String> nameKhach = new ArrayList();
    RadioButton rad_chuyenthang;
    RadioButton rad_sauxuly;
    public List<String> sdtChu = new ArrayList();
    public List<String> sdtKhach = new ArrayList();
    public List<String> sdt_Chu = new ArrayList();
    public List<String> sdt_KH = new ArrayList();
    int sp_Chu;
    int sp_KH;
    Spinner spin_Chu;
    Spinner spin_KH;
    public List<String> ten_Chu = new ArrayList();
    public List<String> ten_KH = new ArrayList();

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity
    public int getLayoutId() {
        return R.layout.activity_chuyenthang;
    }

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyenthang);
        this.db = new Database(this);
        this.add_chuyen = (Button) findViewById(R.id.add_Chuyenthang);
        this.spin_KH = (Spinner) findViewById(R.id.spinter_KH);
        this.spin_Chu = (Spinner) findViewById(R.id.spinter_Chu);
        this.lv_chuyenthang = (ListView) findViewById(R.id.lv_ChuyenThang);
        this.nameKhach.clear();
        this.sdtKhach.clear();
        Cursor cur = this.db.GetData("Select * From tbl_kh_new WHERE type_kh = 1 ORDER by ten_kh");
        if (cur != null && cur.getCount() > 0) {
            while (cur.moveToNext()) {
                this.nameKhach.add(cur.getString(0));
                this.sdtKhach.add(cur.getString(1));
            }
            cur.close();
        }
        this.spin_KH.setAdapter((SpinnerAdapter) new ArrayAdapter<>(this, (int) R.layout.spinner_item, this.nameKhach));
        this.nameChu.clear();
        this.sdtChu.clear();
        Cursor cur2 = this.db.GetData("Select * From tbl_kh_new WHERE type_kh <> 1 ORDER by ten_kh");
        if (cur2 != null && cur2.getCount() > 0) {
            while (cur2.moveToNext()) {
                this.nameChu.add(cur2.getString(0));
                this.sdtChu.add(cur2.getString(1));
            }
            cur2.close();
        }
        this.spin_Chu.setAdapter((SpinnerAdapter) new ArrayAdapter<>(this, (int) R.layout.spinner_item, this.nameChu));
        this.spin_KH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Activity.Activity_ChuyenThang.AnonymousClass1 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Activity_ChuyenThang.this.sp_KH = position;
                Database database = Activity_ChuyenThang.this.db;
                Cursor cursor = database.GetData("Select * from tbl_chuyenthang where sdt_nhan = '" + Activity_ChuyenThang.this.sdtKhach.get(Activity_ChuyenThang.this.sp_KH) + "'");
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    Activity_ChuyenThang.this.spin_Chu.setSelection(Activity_ChuyenThang.this.sdtChu.indexOf(cursor.getString(4)));
                    cursor.close();
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.spin_Chu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Activity.Activity_ChuyenThang.AnonymousClass2 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Activity_ChuyenThang.this.sp_Chu = position;
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.add_chuyen.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_ChuyenThang.AnonymousClass3 */

            public void onClick(View v) {
                try {
                    Database database = Activity_ChuyenThang.this.db;
                    Cursor cursor = database.GetData("Select * From tbl_chuyenthang WHERE kh_nhan = '" + Activity_ChuyenThang.this.nameKhach.get(Activity_ChuyenThang.this.sp_KH) + "'");
                    cursor.moveToFirst();
                    if (cursor.getCount() != 0 || Activity_ChuyenThang.this.sp_Chu <= -1) {
                        Activity_ChuyenThang.this.db.QueryData("UPDATE tbl_chuyenthang set kh_chuyen = '" + Activity_ChuyenThang.this.nameChu.get(Activity_ChuyenThang.this.sp_Chu) + "', sdt_chuyen = '" + Activity_ChuyenThang.this.sdtChu.get(Activity_ChuyenThang.this.sp_Chu) + "' Where sdt_nhan = '" + Activity_ChuyenThang.this.sdtKhach.get(Activity_ChuyenThang.this.sp_KH) + "'");
                        Toast.makeText(Activity_ChuyenThang.this, "Đã sửa!", Toast.LENGTH_LONG).show();
                    } else {
                        Activity_ChuyenThang.this.db.QueryData("Insert into tbl_chuyenthang Values (null, '" + Activity_ChuyenThang.this.nameKhach.get(Activity_ChuyenThang.this.sp_KH) + "', '" + Activity_ChuyenThang.this.sdtKhach.get(Activity_ChuyenThang.this.sp_KH) + "', '" + Activity_ChuyenThang.this.nameChu.get(Activity_ChuyenThang.this.sp_Chu) + "', '" + Activity_ChuyenThang.this.sdtChu.get(Activity_ChuyenThang.this.sp_Chu) + "')");
                        Toast.makeText(Activity_ChuyenThang.this, "Đã thêm!", Toast.LENGTH_LONG).show();
                    }
                    cursor.close();
                } catch (Exception e) {
                    Toast.makeText(Activity_ChuyenThang.this, "Thêm lỗi!", Toast.LENGTH_LONG).show();
                }
                Activity_ChuyenThang.this.xem_lv();
            }
        });
        this.lv_chuyenthang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_ChuyenThang.AnonymousClass4 */

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int k_hang = Activity_ChuyenThang.this.nameKhach.indexOf(Activity_ChuyenThang.this.ten_KH.get(position));
                int chu_nhan = Activity_ChuyenThang.this.nameChu.indexOf(Activity_ChuyenThang.this.ten_Chu.get(position));
                Activity_ChuyenThang.this.spin_KH.setSelection(k_hang);
                Activity_ChuyenThang.this.spin_Chu.setSelection(chu_nhan);
            }
        });
        this.rad_chuyenthang = (RadioButton) findViewById(R.id.rad_chuyenngay);
        this.rad_sauxuly = (RadioButton) findViewById(R.id.rad_chuyensauxl);
        this.rad_chuyenthang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Activity.Activity_ChuyenThang.AnonymousClass5 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Activity_ChuyenThang.this.rad_chuyenthang.isChecked()) {
                    Activity_ChuyenThang.this.db.QueryData("UPDATE So_Om set Om_Xi3 = 0 WHERE ID = 13");
                }
            }
        });
        this.rad_sauxuly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Activity.Activity_ChuyenThang.AnonymousClass6 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Activity_ChuyenThang.this.rad_sauxuly.isChecked()) {
                    Activity_ChuyenThang.this.db.QueryData("UPDATE So_Om set Om_Xi3 = 1 WHERE ID = 13");
                }
            }
        });
        Cursor chuyenthang = this.db.GetData("Select Om_Xi3 From so_om WHERE id = 13");
        if (chuyenthang != null && chuyenthang.moveToFirst()) {
            if (chuyenthang.getInt(0) == 0) {
                this.rad_chuyenthang.setChecked(true);
                this.rad_sauxuly.setChecked(false);
            } else {
                this.rad_sauxuly.setChecked(true);
                this.rad_chuyenthang.setChecked(false);
            }
            if (chuyenthang != null && !chuyenthang.isClosed()) {
                chuyenthang.close();
            }
        }
        xem_lv();
    }

    public void xem_lv() {
        this.ten_KH.clear();
        this.sdt_KH.clear();
        this.ten_Chu.clear();
        this.sdt_Chu.clear();
        Cursor cursor = this.db.GetData("Select * From tbl_chuyenthang");
        while (cursor.moveToNext()) {
            this.ten_KH.add(cursor.getString(1));
            this.sdt_KH.add(cursor.getString(2));
            this.ten_Chu.add(cursor.getString(3));
            this.sdt_Chu.add(cursor.getString(4));
        }
        cursor.close();
        this.lv_chuyenthang.setAdapter((ListAdapter) new CTAdapter(this, R.layout.activity_chuyenthang_lv, this.ten_KH));
    }

    /* access modifiers changed from: package-private */
    public class CTAdapter extends ArrayAdapter {
        public CTAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_chuyenthang_lv, (ViewGroup) null);
            ((TextView) v.findViewById(R.id.tv_stt)).setText((position + 1) + "");
            ((TextView) v.findViewById(R.id.tv_khach)).setText(Activity_ChuyenThang.this.ten_KH.get(position));
            ((TextView) v.findViewById(R.id.tv_chu)).setText(Activity_ChuyenThang.this.ten_Chu.get(position));
            ((TextView) v.findViewById(R.id.tv_delete)).setOnClickListener(new View.OnClickListener() {
                /* class tamhoang.ldpro4.Activity.Activity_ChuyenThang.CTAdapter.AnonymousClass1 */

                public void onClick(View v) {
                    Database database = Activity_ChuyenThang.this.db;
                    database.QueryData("Delete FROM tbl_chuyenthang WHERE sdt_nhan = '" + Activity_ChuyenThang.this.sdt_KH.get(position) + "'");
                    Activity_ChuyenThang.this.xem_lv();
                }
            });
            return v;
        }
    }
}