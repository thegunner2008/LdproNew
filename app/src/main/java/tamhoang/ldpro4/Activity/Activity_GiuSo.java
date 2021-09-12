package tamhoang.ldpro4.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity
    public int getLayoutId() {
        return R.layout.activity_giu_so;
    }

    /* access modifiers changed from: protected */
    @Override // tamhoang.ldpro4.Congthuc.BaseToolBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.SupportActivity, android.support.v4.app.FragmentActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giu_so);
        this.db = new Database(this);
        init();
        this.btnThemdan.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_GiuSo.AnonymousClass1 */

            public void onClick(View v) {
                boolean ktra = true;
                String str = "de " + Activity_GiuSo.this.edtNhapDan.getText().toString();
                int i = 1;
                if (str.length() > 7) {
                    try {
                        str = Congthuc.NhanTinNhan(Congthuc.convertKhongDau(str)).replace("de dit db:", "de:");
                        if (str.indexOf("Không hiểu") > -1) {
                            Toast.makeText(Activity_GiuSo.this, str, Toast.LENGTH_LONG).show();
                            ktra = false;
                        }
                    } catch (Exception e) {
                        Toast.makeText(Activity_GiuSo.this, "Thêm bị lỗi, hãy sửa lại", Toast.LENGTH_LONG).show();
                        ktra = false;
                    }
                }
                if (ktra) {
                    Toast.makeText(Activity_GiuSo.this, "Đã sửa dàn giữ!", Toast.LENGTH_LONG).show();
                    int i2 = 0;
                    if (Activity_GiuSo.this.radioDeB.isChecked()) {
                        if (str.length() > 7) {
                            Activity_GiuSo.this.db.QueryData("Update So_om Set Om_DeB =0");
                            Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = '" + Activity_GiuSo.this.edtNhapDan.getText().toString() + "' WHERE ID = 21");
                            while (true) {
                                String str1 = str.substring(i2, str.indexOf("\n") + i);
                                String str6 = str.substring(str1.indexOf(":") + i, str1.indexOf("\n") + i);
                                String[] str2 = str6.substring(i2, str6.indexOf(",x")).split(",");
                                String str3 = str1.substring(str1.indexOf(",x") + 2, str1.indexOf("\n"));
                                int i3 = 0;
                                while (i3 < str2.length) {
                                    Activity_GiuSo.this.db.QueryData("Update So_om Set Om_DeB = Om_DeB +" + str3 + " WHERE So = '" + str2[i3] + "'");
                                    i3++;
                                    ktra = ktra;
                                    str6 = str6;
                                }
                                str = str.replaceAll(str1, "");
                                if (str.length() > 0) {
                                    ktra = ktra;
                                    i = 1;
                                    i2 = 0;
                                } else {
                                    return;
                                }
                            }
                        }
                    } else if (Activity_GiuSo.this.radioDeA.isChecked()) {
                        if (str.length() > 7) {
                            Activity_GiuSo.this.db.QueryData("Update So_om Set Om_DeA =0");
                            Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = '" + Activity_GiuSo.this.edtNhapDan.getText().toString() + "' WHERE ID = 20");
                            do {
                                String str12 = str.substring(0, str.indexOf("\n") + 1);
                                String str62 = str.substring(str12.indexOf(":") + 1, str12.indexOf("\n") + 1);
                                String[] str22 = str62.substring(0, str62.indexOf(",x")).split(",");
                                String str32 = str12.substring(str12.indexOf(",x") + 2, str12.indexOf("\n"));
                                int i4 = 0;
                                while (i4 < str22.length) {
                                    Activity_GiuSo.this.db.QueryData("Update So_om Set Om_DeA = Om_DeA +" + str32 + " WHERE So = '" + str22[i4] + "'");
                                    i4++;
                                    str62 = str62;
                                }
                                str = str.replaceAll(str12, "");
                            } while (str.length() > 0);
                        }
                    } else if (Activity_GiuSo.this.radioDeC.isChecked()) {
                        if (str.length() > 7) {
                            Activity_GiuSo.this.db.QueryData("Update So_om Set Om_DeC =0");
                            Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = '" + Activity_GiuSo.this.edtNhapDan.getText().toString() + "' WHERE ID = 22");
                            do {
                                String str13 = str.substring(0, str.indexOf("\n") + 1);
                                String str63 = str.substring(str13.indexOf(":") + 1, str13.indexOf("\n") + 1);
                                String[] str23 = str63.substring(0, str63.indexOf(",x")).split(",");
                                String str33 = str13.substring(str13.indexOf(",x") + 2, str13.indexOf("\n"));
                                int i5 = 0;
                                while (i5 < str23.length) {
                                    Activity_GiuSo.this.db.QueryData("Update So_om Set Om_DeC = Om_DeC +" + str33 + " WHERE So = '" + str23[i5] + "'");
                                    i5++;
                                    str63 = str63;
                                }
                                str = str.replaceAll(str13, "");
                            } while (str.length() > 0);
                        }
                    } else if (Activity_GiuSo.this.radioDeD.isChecked()) {
                        if (str.length() > 7) {
                            Activity_GiuSo.this.db.QueryData("Update So_om Set Om_DeD =0");
                            Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = '" + Activity_GiuSo.this.edtNhapDan.getText().toString() + "' WHERE ID = 23");
                            do {
                                String str14 = str.substring(0, str.indexOf("\n") + 1);
                                String str64 = str.substring(str14.indexOf(":") + 1, str14.indexOf("\n") + 1);
                                String[] str24 = str64.substring(0, str64.indexOf(",x")).split(",");
                                String str34 = str14.substring(str14.indexOf(",x") + 2, str14.indexOf("\n"));
                                int i6 = 0;
                                while (i6 < str24.length) {
                                    Activity_GiuSo.this.db.QueryData("Update So_om Set Om_DeD = Om_DeD +" + str34 + " WHERE So = '" + str24[i6] + "'");
                                    i6++;
                                    str64 = str64;
                                }
                                str = str.replaceAll(str14, "");
                            } while (str.length() > 0);
                        }
                    } else if (Activity_GiuSo.this.radioLo.isChecked() && str.length() > 7) {
                        Activity_GiuSo.this.db.QueryData("Update So_om Set Om_Lo =0");
                        Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = '" + Activity_GiuSo.this.edtNhapDan.getText().toString() + "' WHERE ID = 24");
                        do {
                            String str15 = str.substring(0, str.indexOf("\n") + 1);
                            String str65 = str.substring(str15.indexOf(":") + 1, str15.indexOf("\n") + 1);
                            String[] str25 = str65.substring(0, str65.indexOf(",x")).split(",");
                            String str35 = str15.substring(str15.indexOf(",x") + 2, str15.indexOf("\n"));
                            int i7 = 0;
                            while (i7 < str25.length) {
                                Activity_GiuSo.this.db.QueryData("Update So_om Set Om_Lo = Om_Lo +" + str35 + " WHERE So = '" + str25[i7] + "'");
                                i7++;
                                str65 = str65;
                            }
                            str = str.replaceAll(str15, "");
                        } while (str.length() > 0);
                    }
                }
            }
        });
        this.btnXoaDan.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_GiuSo.AnonymousClass2 */

            public void onClick(View v) {
                if (Activity_GiuSo.this.radioDeA.isChecked()) {
                    Activity_GiuSo.this.db.QueryData("UPdate so_Om set Om_DeA = 0");
                    Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = null WHERE ID = 20");
                }
                if (Activity_GiuSo.this.radioDeB.isChecked()) {
                    Activity_GiuSo.this.db.QueryData("UPdate so_Om set Om_DeB = 0");
                    Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = null WHERE ID = 21");
                }
                if (Activity_GiuSo.this.radioDeC.isChecked()) {
                    Activity_GiuSo.this.db.QueryData("UPdate so_Om set Om_DeC = 0");
                    Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = null WHERE ID = 22");
                }
                if (Activity_GiuSo.this.radioDeD.isChecked()) {
                    Activity_GiuSo.this.db.QueryData("UPdate so_Om set Om_DeD = 0");
                    Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = null WHERE ID = 23");
                }
                if (Activity_GiuSo.this.radioLo.isChecked()) {
                    Activity_GiuSo.this.db.QueryData("UPdate so_Om set Om_Lo = 0");
                    Activity_GiuSo.this.db.QueryData("UPDATE So_om SET Sphu1 = null WHERE ID = 24");
                }
                Activity_GiuSo.this.edtNhapDan.setText("");
                Toast.makeText(Activity_GiuSo.this, "Đã xóa dàn giữ!", Toast.LENGTH_LONG).show();
            }
        });
        this.btnThemXien.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_GiuSo.AnonymousClass3 */

            public void onClick(View view) {
                int mXien2 = 0;
                int mXien3 = 0;
                int mXien4 = 0;
                int m3Cang = 0;
                if (Activity_GiuSo.this.giuxien2.getText().toString().length() > 0) {
                    mXien2 = Integer.parseInt(Activity_GiuSo.this.giuxien2.getText().toString());
                }
                if (Activity_GiuSo.this.giuxien3.getText().toString().length() > 0) {
                    mXien3 = Integer.parseInt(Activity_GiuSo.this.giuxien3.getText().toString());
                }
                if (Activity_GiuSo.this.giuxien4.getText().toString().length() > 0) {
                    mXien4 = Integer.parseInt(Activity_GiuSo.this.giuxien4.getText().toString());
                }
                if (Activity_GiuSo.this.giu3cang.getText().toString().length() > 0) {
                    m3Cang = Integer.parseInt(Activity_GiuSo.this.giu3cang.getText().toString());
                }
                Activity_GiuSo.this.db.QueryData("Update So_om Set Om_Xi2 = " + mXien2 + ", Om_Xi3 = " + mXien3 + ", Om_Xi4 = " + mXien4 + ", Om_bc = " + m3Cang + " WHERE ID = 1");
                Toast.makeText(Activity_GiuSo.this, "Đã lưu giữ xiên/càng!", Toast.LENGTH_LONG).show();
            }
        });
        this.btnXoaXien.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Activity.Activity_GiuSo.AnonymousClass4 */

            public void onClick(View view) {
                Activity_GiuSo.this.db.QueryData("Update So_om Set Om_Xi2 = 0, Om_Xi3 = 0, Om_Xi4 = 0, Om_bc = 0 WHERE ID = 1");
                Toast.makeText(Activity_GiuSo.this, "Đã xóa giữ xiên/càng!", Toast.LENGTH_LONG).show();
                Activity_GiuSo.this.giuxien2.setText("");
                Activity_GiuSo.this.giuxien3.setText("");
                Activity_GiuSo.this.giuxien4.setText("");
                Activity_GiuSo.this.giu3cang.setText("");
            }
        });
        this.radioDeA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Activity.Activity_GiuSo.AnonymousClass5 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Cursor cursor = Activity_GiuSo.this.db.GetData("Select Sphu1 FROM So_om WHERE ID = 20");
                    if (cursor.moveToFirst()) {
                        Activity_GiuSo.this.edtNhapDan.setText(cursor.getString(0));
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    }
                }
            }
        });
        this.radioDeB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Activity.Activity_GiuSo.AnonymousClass6 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Cursor cursor = Activity_GiuSo.this.db.GetData("Select Sphu1 FROM So_om WHERE ID = 21");
                    if (cursor.moveToFirst()) {
                        Activity_GiuSo.this.edtNhapDan.setText(cursor.getString(0));
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    }
                }
            }
        });
        this.radioDeC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Activity.Activity_GiuSo.AnonymousClass7 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Cursor cursor = Activity_GiuSo.this.db.GetData("Select Sphu1 FROM So_om WHERE ID = 22");
                    if (cursor.moveToFirst()) {
                        Activity_GiuSo.this.edtNhapDan.setText(cursor.getString(0));
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    }
                }
            }
        });
        this.radioDeD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Activity.Activity_GiuSo.AnonymousClass8 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Cursor cursor = Activity_GiuSo.this.db.GetData("Select Sphu1 FROM So_om WHERE ID = 23");
                    if (cursor.moveToFirst()) {
                        Activity_GiuSo.this.edtNhapDan.setText(cursor.getString(0));
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    }
                }
            }
        });
        this.radioLo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Activity.Activity_GiuSo.AnonymousClass9 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && b) {
                    Cursor cursor = Activity_GiuSo.this.db.GetData("Select Sphu1 FROM So_om WHERE ID = 24");
                    if (cursor.moveToFirst()) {
                        Activity_GiuSo.this.edtNhapDan.setText(cursor.getString(0));
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                    }
                }
            }
        });
        Cursor cursor = this.db.GetData("Select Sphu1 FROM So_om WHERE ID = 21");
        if (cursor.moveToFirst()) {
            this.edtNhapDan.setText(cursor.getString(0));
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
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
            if (cursor1 != null && !cursor1.isClosed()) {
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
}