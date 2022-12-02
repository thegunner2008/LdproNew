package tamhoang.ldpro4.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.akaman.AkaManSec;
import tamhoang.ldpro4.data.BriteDb;
import tamhoang.ldpro4.data.Database;

public class ActivityDangNhap extends AppCompatActivity {
    CheckBox cbSavePass;

    Button btnLogin;

    EditText edtPassword;

    TextInputLayout password_error;

    private void clickLogin() {
        if (cbSavePass.isChecked()) {
            BriteDb.INSTANCE.savePassWord(this.edtPassword.getText().toString());
        } else {
            BriteDb.INSTANCE.savePassWord("");
        }
        String pass = AkaManSec.userPwd;
        if (edtPassword != null) {
            String textEncode = AkaManSec.md5(edtPassword.getText().toString().trim());
            if (!textEncode.equals(pass)) {
                this.password_error.setErrorEnabled(true);
                this.password_error.setError("Mật khẩu không đúng!");
                Toast.makeText(this, "Mật khẩu không đúng!", Toast.LENGTH_LONG).show();
                return;
            }
        }
        goMainActivity();
    }

    private void goMainActivity() {
        startActivities(new Intent[]{new Intent(this, MainActivity.class)});
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_dang_nhap);
        AkaManSec.queryAkaManPwd(new Database(this));
        this.cbSavePass = findViewById(R.id.cbSavePass);
        this.password_error = findViewById(R.id.password_error);
        this.edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> clickLogin());

        String savePass = BriteDb.INSTANCE.getPassWord();
        if (!savePass.equals("")) {
            this.cbSavePass.setChecked(true);
            this.edtPassword.setText(savePass);
        }
    }
}
