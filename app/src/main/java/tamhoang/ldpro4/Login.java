package tamhoang.ldpro4;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.role.RoleManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Xml;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import java.util.Arrays;

import tamhoang.ldpro4.Activity.ActivityDangNhap;
import tamhoang.ldpro4.akaman.AkaManSec;
import tamhoang.ldpro4.data.BriteDb;
import tamhoang.ldpro4.data.Database;

public class Login extends AppCompatActivity {
    public static String Imei;
    public static String serial;

    Database db;
    Intent intent;
    Button btn_login;

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        db = new Database(this);
        BriteDb.INSTANCE.init(getApplication());
        this.btn_login = findViewById(R.id.btn_login);
        int[] check = {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS),
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE),
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS),
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS),
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        };
        if (Arrays.stream(check).anyMatch(i -> i == -1)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_CONTACTS,
                    Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        int[] reCheck = new int[6];
        btn_login.setOnClickListener(view -> {
            reCheck[0] = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            reCheck[1] = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            reCheck[2] = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
            reCheck[3] = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            reCheck[4] = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            reCheck[5] = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (Arrays.stream(reCheck).anyMatch(i -> i != 0)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else if (this.getImei() != null) {
                intent = new Intent(this, MainActivity.class);
                startActivities(new Intent[]{intent});
            }

            checkFileAccessPermission();
        });
        try {
            Create_Table_database();
            AkaManSec.queryAkaManPwd(db);
            if (Arrays.stream(check).allMatch(i -> i == 0) && getImei() != null) {
                String pass = AkaManSec.userPwd;
                if (pass == null || pass.isEmpty()) {
                    intent = new Intent(this, MainActivity.class);
                } else {
                    intent = new Intent(this, ActivityDangNhap.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivities(new Intent[]{this.intent});
            }
        } catch (SQLException ignored) {
        }
    }

    @SuppressLint({"WrongConstant", "HardwareIds"})
    public String getImei() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != -1) {
            try {
                Imei = ((TelephonyManager) getSystemService("phone")).getDeviceId();
                serial = Settings.Secure.getString(getContentResolver(), "android_id");
            } catch (Exception ignored) {
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != 0 && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);
        }
    }

    public void checkDefaultSettings() {
        if (Telephony.Sms.getDefaultSmsPackage(getApplicationContext()).equals(getApplicationContext().getPackageName())) {
            return;
        }
        showAlertBox("Cài đặt mặc định!", "Để ứng dụng thành quản lý tin nhắn mặc định để quản lý tin nhắn tốt hơn!")
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                        RoleManager roleManager = getSystemService(RoleManager.class);
                        Intent setSmsAppIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                        startActivityForResult(setSmsAppIntent, 202);
                    } else {
                        Intent setSmsAppIntent =
                                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                getPackageName());
                        startActivityForResult(setSmsAppIntent, 202);
                    }

                }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).show().setCanceledOnTouchOutside(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void checkFileAccessPermission() {
        if (Environment.isExternalStorageManager()) {
            return;
        }
        Intent getPermission = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        startActivityForResult(getPermission, 123);
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
        AkaManSec.initSecTable(db);
        try {
            Cursor c = this.db.GetData("Select * From so_om");
            if (c.getCount() < 1) {
                for (int i = 0; i < 100; i++) {
                    this.db.QueryData("Insert into so_om Values (null, '"
                            + (i < 10 ? "0" : "") + i + "', 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null)");
                }
            }
            if (!c.isClosed()) {
                c.close();
            }
        } catch (SQLException ignored) {
        }
        try {
            Cursor cursor = this.db.GetData("Select Om_Xi3 FROM So_om WHERE So = '05'");
            cursor.moveToFirst();
            if (cursor.getInt(0) == 0) {
                this.db.QueryData("UPDATE So_om SET Om_Xi3 = 18, Om_Xi4 = 15 WHERE So = '05'");
            }
        } catch (SQLException ignored) {
        }
    }
}
