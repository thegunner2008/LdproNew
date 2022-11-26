package tamhoang.ldpro4.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_GiuSo extends BaseToolBarActivity {
    Button btnThemXien;
    Button btnThemdan;
    Button btnXoaDan;
    Button btnXoaXien;
    Database db;
    EditText edtNhapDan;
    EditText giu3cang;
    EditText giuxien2;
    EditText giuxien3;
    EditText giuxien4;
    RadioButton radioDeA;
    RadioButton radioDeB;
    RadioButton radioDeC;
    RadioButton radioDeD;
    RadioButton radioLo;
    Spinner spr_KH;

    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity
    public int getLayoutId() {
        return R.layout.activity_giu_so;
    }

    @Override
    // tamhoang.ldpro4.Congthuc.BaseToolBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giu_so);
        this.db = new Database(this);
        init();
        this.btnThemdan.setOnClickListener(v -> {
            boolean ktra = true;
            String phan_tich = "de " + edtNhapDan.getText().toString();
            if (phan_tich.length() > 7) {
                try {
                    phan_tich = Congthuc.NhanTinNhan(Congthuc.convertKhongDau(phan_tich))
                            .replace("de dit db:", "de:");
                    if (phan_tich.contains("Không hiểu")) {
                        Toast.makeText(Activity_GiuSo.this, phan_tich, Toast.LENGTH_LONG).show();
                        ktra = false;
                    }
                } catch (Exception e) {
                    Toast.makeText(Activity_GiuSo.this, "Thêm bị lỗi, hãy sửa lại", Toast.LENGTH_LONG).show();
                    ktra = false;
                }
            }
            if (ktra) {
                Toast.makeText(Activity_GiuSo.this, "Đã sửa dàn giữ!", Toast.LENGTH_LONG).show();

                Type type = radioDeB.isChecked() ? Type.DE_B
                        : radioDeA.isChecked() ? Type.DE_A
                        : radioDeC.isChecked() ? Type.DE_C
                        : radioDeD.isChecked() ? Type.DE_D
                        : radioLo.isChecked() ? Type.LO
                        : null;

                if (type != null && phan_tich.length() > 7) {
                    db.QueryData("Update So_om Set " + type.colunm + " =0");
                    db.QueryData("UPDATE So_om SET Sphu1 = '" + edtNhapDan.getText().toString() + "' WHERE ID = " + type.ID);
                    do {
                        String line = phan_tich.substring(0, phan_tich.indexOf("\n") + 1); // 'de: 11,22,33 x 100'
                        String dayso_tien = phan_tich.substring(line.indexOf(":") + 1, line.indexOf("\n") + 1); // '11,22,33 x 100'
                        String[] so_arr = dayso_tien.substring(0, dayso_tien.indexOf(",x")).split(","); // ['11','22','33']
                        String tien = line.substring(line.indexOf(",x") + 2, line.indexOf("\n")); // '100'
                        for (String so : so_arr) {
                            db.QueryData("Update So_om Set " + type.colunm + " = " + type.colunm + " +" + tien + " WHERE So = '" + so + "'");
                        }
                        phan_tich = phan_tich.replaceAll(line, "");
                    } while (phan_tich.length() > 0);
                }
            }
        });
        this.btnXoaDan.setOnClickListener(v -> {
            Type type = radioDeB.isChecked() ? Type.DE_B
                    : radioDeA.isChecked() ? Type.DE_A
                    : radioDeC.isChecked() ? Type.DE_C
                    : radioDeD.isChecked() ? Type.DE_D
                    : radioLo.isChecked() ? Type.LO
                    : null;
            if (type != null) {
                db.QueryData("UPdate so_Om set " + type.colunm + " = 0");
                db.QueryData("UPDATE So_om SET Sphu1 = null WHERE ID = " + type.ID);
            }
            edtNhapDan.setText("");
            Toast.makeText(Activity_GiuSo.this, "Đã xóa dàn giữ!", Toast.LENGTH_LONG).show();
        });
        this.btnThemXien.setOnClickListener(view -> {
            int mXien2 = 0;
            int mXien3 = 0;
            int mXien4 = 0;
            int m3Cang = 0;
            if (giuxien2.getText().toString().length() > 0) {
                mXien2 = Integer.parseInt(giuxien2.getText().toString());
            }
            if (giuxien3.getText().toString().length() > 0) {
                mXien3 = Integer.parseInt(giuxien3.getText().toString());
            }
            if (giuxien4.getText().toString().length() > 0) {
                mXien4 = Integer.parseInt(giuxien4.getText().toString());
            }
            if (giu3cang.getText().toString().length() > 0) {
                m3Cang = Integer.parseInt(giu3cang.getText().toString());
            }
            db.QueryData("Update So_om Set Om_Xi2 = " + mXien2 + ", Om_Xi3 = " + mXien3 + ", Om_Xi4 = " + mXien4 + ", Om_bc = " + m3Cang + " WHERE ID = 1");
            Toast.makeText(Activity_GiuSo.this, "Đã lưu giữ xiên/càng!", Toast.LENGTH_LONG).show();
        });
        this.btnXoaXien.setOnClickListener(view -> {
            db.QueryData("Update So_om Set Om_Xi2 = 0, Om_Xi3 = 0, Om_Xi4 = 0, Om_bc = 0 WHERE ID = 1");
            Toast.makeText(Activity_GiuSo.this, "Đã xóa giữ xiên/càng!", Toast.LENGTH_LONG).show();
            giuxien2.setText("");
            giuxien3.setText("");
            giuxien4.setText("");
            giu3cang.setText("");
        });
        this.radioDeA.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Cursor cursor = db.GetData("Select Sphu1 FROM So_om WHERE ID = 20");
                if (cursor.moveToFirst()) {
                    edtNhapDan.setText(cursor.getString(0));
                    if (!cursor.isClosed()) cursor.close();
                }
            }
        });
        this.radioDeB.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Cursor cursor = db.GetData("Select Sphu1 FROM So_om WHERE ID = 21");
                if (cursor.moveToFirst()) {
                    edtNhapDan.setText(cursor.getString(0));
                    if (!cursor.isClosed()) cursor.close();
                }
            }
        });
        this.radioDeC.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Cursor cursor = db.GetData("Select Sphu1 FROM So_om WHERE ID = 22");
                if (cursor.moveToFirst()) {
                    edtNhapDan.setText(cursor.getString(0));
                    if (!cursor.isClosed()) cursor.close();
                }
            }
        });
        this.radioDeD.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Cursor cursor = db.GetData("Select Sphu1 FROM So_om WHERE ID = 23");
                if (cursor.moveToFirst()) {
                    edtNhapDan.setText(cursor.getString(0));
                    if (!cursor.isClosed()) cursor.close();
                }
            }
        });
        this.radioLo.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Cursor cursor = db.GetData("Select Sphu1 FROM So_om WHERE ID = 24");
                if (cursor.moveToFirst()) {
                    edtNhapDan.setText(cursor.getString(0));
                    if (!cursor.isClosed()) cursor.close();
                }
            }
        });
        Cursor cursor = this.db.GetData("Select Sphu1 FROM So_om WHERE ID = 21");
        if (cursor.moveToFirst()) {
            this.edtNhapDan.setText(cursor.getString(0));
            if (!cursor.isClosed()) cursor.close();
        }
        Cursor cursor1 = this.db.GetData("Select * From so_om WHERE id = 1");
        if (cursor1.moveToFirst()) {
            EditText editText = this.giuxien2;
            editText.setText(cursor1.getString(7) + "");
            EditText editText2 = this.giuxien3;
            editText2.setText(cursor1.getString(8) + "");
            EditText editText3 = this.giuxien4;
            editText3.setText(cursor1.getString(9) + "");
            EditText editText4 = this.giu3cang;
            editText4.setText(cursor1.getString(10) + "");
            if (!cursor1.isClosed()) {
                cursor1.close();
            }
        }
    }

    public void init() {
        this.btnThemdan = (Button) findViewById(R.id.btn_Them_Om);
        this.btnXoaDan = (Button) findViewById(R.id.btn_Xoa);
        this.btnThemXien = (Button) findViewById(R.id.btn_GiuXien);
        this.btnXoaXien = (Button) findViewById(R.id.btn_XoaXien);
        this.radioDeA = (RadioButton) findViewById(R.id.radio_DeA);
        this.radioDeB = (RadioButton) findViewById(R.id.radio_DeB);
        this.radioDeC = (RadioButton) findViewById(R.id.radio_DeC);
        this.radioDeD = (RadioButton) findViewById(R.id.radio_DeD);
        this.radioLo = (RadioButton) findViewById(R.id.radio_lo);
        this.edtNhapDan = (EditText) findViewById(R.id.edt_NhapDan);
        this.spr_KH = (Spinner) findViewById(R.id.spr_KH);
        this.giuxien2 = (EditText) findViewById(R.id.giuxien2);
        this.giuxien3 = (EditText) findViewById(R.id.giuxien3);
        this.giuxien4 = (EditText) findViewById(R.id.giuxien4);
        this.giu3cang = (EditText) findViewById(R.id.giu3cang);
    }

    enum Type {
        DE_A("Om_DeA", 20),
        DE_B("Om_DeB", 21),
        DE_C("Om_DeC", 22),
        DE_D("Om_DeD", 23),
        LO("Om_Lo", 24);

        Type(String col, int id) {
            this.colunm = col;
            this.ID = id;
        }

        public String colunm;
        public int ID;
    }
}