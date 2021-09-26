package tamhoang.ldpro4;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import tamhoang.ldpro4.data.Database;

public class Login extends AppCompatActivity {
    public static String Imei;
    public static String serial;

    Database db;
    Intent intent;
    Button login;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        this.db = new Database(this);
        this.login = findViewById(R.id.btn_login);
        int[] iArr = {ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS")};
        int[] iArr2 = {ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE")};
        int[] iArr3 = {ContextCompat.checkSelfPermission(this, "android.permission.RECEIVE_SMS")};
        int[] iArr4 = {ContextCompat.checkSelfPermission(this, "android.permission.SEND_SMS")};
        int[] iArr5 = {ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")};
        if (iArr[0] == -1 || iArr2[0] == -1 || iArr3[0] == -1 || iArr5[0] == -1 || iArr4[0] == -1) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.INTERNET", "android.permission.READ_CONTACTS", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS", "android.permission.READ_PHONE_STATE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        Button button = this.login;
        final int[] iArr6 = iArr;
        final int[] iArr7 = iArr2;
        final int[] iArr8 = iArr3;
        final int[] iArr9 = iArr4;
        final int[] iArr10 = iArr5;
        button.setOnClickListener(view -> {
            iArr6[0] = ContextCompat.checkSelfPermission(Login.this, "android.permission.READ_CONTACTS");
            iArr7[0] = ContextCompat.checkSelfPermission(Login.this, "android.permission.READ_PHONE_STATE");
            iArr8[0] = ContextCompat.checkSelfPermission(Login.this, "android.permission.RECEIVE_SMS");
            iArr9[0] = ContextCompat.checkSelfPermission(Login.this, "android.permission.SEND_SMS");
            iArr10[0] = ContextCompat.checkSelfPermission(Login.this, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (iArr6[0] != 0 || iArr7[0] != 0 || iArr8[0] != 0 || iArr10[0] != 0 || iArr9[0] != 0) {
                ActivityCompat.requestPermissions(Login.this, new String[]{"android.permission.INTERNET", "android.permission.READ_CONTACTS", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS", "android.permission.READ_PHONE_STATE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            } else if (Login.this.getImei() != null) {
                Login.this.intent = new Intent(Login.this, MainActivity.class);
                Login login = Login.this;
                login.startActivities(new Intent[]{login.intent});
            }
        });
        try {
            Create_Table_database();
            if (iArr[0] == 0 && iArr2[0] == 0 && iArr3[0] == 0 && iArr5[0] == 0 && iArr4[0] == 0 && getImei() != null) {
                this.intent = new Intent(this, MainActivity.class);
                startActivities(new Intent[]{this.intent});
            }
        } catch (SQLException ignored) {
        }
    }

    @SuppressLint({"WrongConstant", "HardwareIds"})
    public String getImei() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != -1) {
            try {
                Imei = ((TelephonyManager) getSystemService("phone")).getDeviceId();
                serial = Settings.Secure.getString(getContentResolver(), "android_id");
            } catch (Exception e) {
                e.getMessage();
            }
        }
        if (Imei != null) {
            XmlSerializer newSerializer = Xml.newSerializer();
            try {
                newSerializer.setOutput(new StringWriter());
                newSerializer.startDocument("UTF-8", true);
                String str = Imei;
                FileOutputStream openFileOutput = openFileOutput("new.xml", 0);
                openFileOutput.write(str.getBytes(), 0, str.length());
                openFileOutput.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
                Toast.makeText(this, "Loi tao file", 0).show();
            }
        } else {
            try {
                FileInputStream openFileInput = openFileInput("new.xml");
                Imei = "";
                while (true) {
                    int read = openFileInput.read();
                    if (read == -1) {
                        break;
                    }
                    Imei += ((char) read);
                }
            } catch (FileNotFoundException e4) {
                checkDefaultSettings();
                e4.printStackTrace();
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        }
        return Imei;
    }

    public AlertDialog.Builder showAlertBox(String str, String str2) {
        return new AlertDialog.Builder(this).setTitle(str).setMessage(str2);
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_SMS") != 0 && !ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_SMS")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}, 1);
        }
    }

    public boolean checkDefaultSettings() {
        if (Telephony.Sms.getDefaultSmsPackage(getApplicationContext()).equals(getApplicationContext().getPackageName())) {
            return true;
        }
        showAlertBox("Cài đặt mặc định!", "Để ứng dụng thành quản lý tin nhắn mặc định để quản lý tin nhắn tốt hơn!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent setSmsAppIntent =
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        getPackageName());
                startActivityForResult(setSmsAppIntent, 202);
            }
        }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).show().setCanceledOnTouchOutside(false);
        return false;
    }

    public void Create_Table_database() {
        this.db.Creat_TinNhanGoc();
        this.db.Create_table_Chat();
        this.db.Creat_SoCT();
        this.db.Creat_So_Om();
        this.db.List_Khach_Hang();
        this.db.Bang_KQ();
        this.db.ThayThePhu();
        this.db.Another_setting();
        this.db.Creat_Chaytrang_acc();
        this.db.Creat_Chaytrang_ticket();
        try {
            Cursor c = this.db.GetData("Select * From so_om");
            if (c.getCount() < 1) {
                for (int i = 0; i < 10; i++) {
                    this.db.QueryData("Insert into so_om Values (null, '0" + i + "', 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null)");
                }
                for (int i2 = 10; i2 < 100; i2++) {
                    this.db.QueryData("Insert into so_om Values (null, '" + i2 + "', 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null)");
                }
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (SQLException e) {
        }
        try {
            Cursor cursor = this.db.GetData("Select Om_Xi3 FROM So_om WHERE So = '05'");
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                this.db.QueryData("UPDATE So_om SET Om_Xi3 = 18, Om_Xi4 = 15 WHERE So = '05'");
            }
        } catch (SQLException e2) {
        }
    }
}
