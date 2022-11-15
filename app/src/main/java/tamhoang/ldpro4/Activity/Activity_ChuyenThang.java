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
    public List<String> nameChu = new ArrayList<>();
    public List<String> nameKhach = new ArrayList<>();
    RadioButton rad_chuyenthang;
    RadioButton rad_sauxuly;
    public List<String> sdtChu = new ArrayList<>();
    public List<String> sdtKhach = new ArrayList<>();
    public List<String> sdt_Chu = new ArrayList<>();
    public List<String> sdt_KH = new ArrayList<>();
    int sp_Chu;
    int sp_KH;
    Spinner spin_Chu;
    Spinner spin_KH;
    public List<String> ten_Chu = new ArrayList<>();
    public List<String> ten_KH = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_chuyenthang;
    }

    @Override
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

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                sp_KH = position;
                Database database = db;
                Cursor cursor = database.GetData("Select * from tbl_chuyenthang where sdt_nhan = '" + sdtKhach.get(sp_KH) + "'");
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    spin_Chu.setSelection(sdtChu.indexOf(cursor.getString(4)));
                    cursor.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.spin_Chu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                sp_Chu = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.add_chuyen.setOnClickListener(v -> {
            try {
                Cursor cursor = db.GetData("Select * From tbl_chuyenthang WHERE kh_nhan = '" + nameKhach.get(sp_KH) + "'");
                cursor.moveToFirst();
                if (cursor.getCount() != 0 || sp_Chu <= -1) {
                    db.QueryData("UPDATE tbl_chuyenthang set kh_chuyen = '" + nameChu.get(sp_Chu) + "', sdt_chuyen = '" + sdtChu.get(sp_Chu) + "' Where sdt_nhan = '" + sdtKhach.get(sp_KH) + "'");
                    Toast.makeText(Activity_ChuyenThang.this, "Đã sửa!", Toast.LENGTH_LONG).show();
                } else {
                    db.QueryData("Insert into tbl_chuyenthang Values (null, '" + nameKhach.get(sp_KH) + "', '" + sdtKhach.get(sp_KH) + "', '" + nameChu.get(sp_Chu) + "', '" + sdtChu.get(sp_Chu) + "')");
                    Toast.makeText(Activity_ChuyenThang.this, "Đã thêm!", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            } catch (Exception e) {
                Toast.makeText(Activity_ChuyenThang.this, "Thêm lỗi!", Toast.LENGTH_LONG).show();
            }
            xem_lv();
        });
        this.lv_chuyenthang.setOnItemClickListener((adapterView, view, position, id) -> {
            int k_hang = nameKhach.indexOf(ten_KH.get(position));
            int chu_nhan = nameChu.indexOf(ten_Chu.get(position));
            spin_KH.setSelection(k_hang);
            spin_Chu.setSelection(chu_nhan);
        });
        this.rad_chuyenthang = (RadioButton) findViewById(R.id.rad_chuyenngay);
        this.rad_sauxuly = (RadioButton) findViewById(R.id.rad_chuyensauxl);
        this.rad_chuyenthang.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (rad_chuyenthang.isChecked()) {
                db.QueryData("UPDATE So_Om set Om_Xi3 = 0 WHERE ID = 13");
            }
        });
        this.rad_sauxuly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (rad_sauxuly.isChecked()) {
                db.QueryData("UPDATE So_Om set Om_Xi3 = 1 WHERE ID = 13");
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
            if (!chuyenthang.isClosed()) {
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

    public class CTAdapter extends ArrayAdapter {
        public CTAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_chuyenthang_lv, (ViewGroup) null);
            ((TextView) v.findViewById(R.id.tv_stt)).setText((position + 1) + "");
            ((TextView) v.findViewById(R.id.tv_khach)).setText(ten_KH.get(position));
            ((TextView) v.findViewById(R.id.tv_chu)).setText(ten_Chu.get(position));
            ((TextView) v.findViewById(R.id.tv_delete)).setOnClickListener(v1 -> {
                Database database = db;
                database.QueryData("Delete FROM tbl_chuyenthang WHERE sdt_nhan = '" + sdt_KH.get(position) + "'");
                xem_lv();
            });
            return v;
        }
    }
}