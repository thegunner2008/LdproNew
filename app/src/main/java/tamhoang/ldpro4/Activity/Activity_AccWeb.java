package tamhoang.ldpro4.Activity;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.internal.view.SupportMenu;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Activity_AccWeb extends BaseToolBarActivity {
    Button btn_them_trang;

    Database db;
    EditText edt_Giabc;
    EditText edt_Giadea;
    EditText edt_Giadeb;
    EditText edt_Giadec;
    EditText edt_Giaded;
    EditText edt_Gialo;
    EditText edt_Giaxi2;
    EditText edt_Giaxi3;
    EditText edt_Giaxi4;
    EditText edt_account;
    EditText edt_password;
    LinearLayout liner_caidat;
    String new_web = "";
    TextView tview_Maxbc;
    TextView tview_Maxdea;
    TextView tview_Maxdeb;
    TextView tview_Maxdec;
    TextView tview_Maxded;
    TextView tview_Maxlo;
    TextView tview_Maxxi2;
    TextView tview_Maxxi3;
    TextView tview_Maxxi4;

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.activity_acc_web;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_acc_web);
        this.db = new Database(this);
        this.new_web = getIntent().getStringExtra("new_web");
        init();
        if (this.new_web.length() == 0) {
            this.liner_caidat.setVisibility(View.GONE);
        } else {
            Database database = this.db;
            Cursor GetData = database.GetData("Select * from tbl_chaytrang_acc Where Username = '" + this.new_web + "'");
            GetData.moveToFirst();
            this.edt_account.setText(GetData.getString(0));
            this.edt_password.setText(GetData.getString(1));
            this.edt_account.setEnabled(false);
            this.edt_password.setEnabled(false);
            this.btn_them_trang.setText("Thêm / Sửa trang");
            this.btn_them_trang.setTextColor(SupportMenu.CATEGORY_MASK);
            try {
                JSONObject jSONObject = new JSONObject(GetData.getString(2));
                this.edt_Giadea.setText(jSONObject.getString("gia_dea"));
                this.edt_Giadeb.setText(jSONObject.getString("gia_deb"));
                this.edt_Giadec.setText(jSONObject.getString("gia_dec"));
                this.edt_Giaded.setText(jSONObject.getString("gia_ded"));
                this.edt_Gialo.setText(jSONObject.getString("gia_lo"));
                this.edt_Giaxi2.setText(!jSONObject.has("gia_xi2") ? "560" : jSONObject.getString("gia_xi2"));
                this.edt_Giaxi3.setText(!jSONObject.has("gia_xi3") ? "520" : jSONObject.getString("gia_xi3"));
                this.edt_Giaxi4.setText(!jSONObject.has("gia_xi4") ? "450" : jSONObject.getString("gia_xi4"));
                this.tview_Maxdea.setText(jSONObject.getString("max_dea"));
                this.tview_Maxdeb.setText(jSONObject.getString("max_deb"));
                this.tview_Maxdec.setText(jSONObject.getString("max_dec"));
                this.tview_Maxded.setText(jSONObject.getString("max_ded"));
                this.tview_Maxlo.setText(jSONObject.getString("max_lo"));
                this.tview_Maxxi2.setText(jSONObject.getString("max_xi2"));
                this.tview_Maxxi3.setText(jSONObject.getString("max_xi3"));
                this.tview_Maxxi4.setText(jSONObject.getString("max_xi4"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.btn_them_trang.setOnClickListener(view -> {
            if (new_web.length() == 0) {
                Database database = db;
                if (database.GetData("Select * from tbl_chaytrang_acc Where Username = '" + edt_account.getText().toString() + "'").getCount() == 0) {
                    this.btn_them_trang.setEnabled(false);
                    RUN();
                } else {
                    Toast.makeText(Activity_AccWeb.this, "Đã có tài khoản này trong hệ thống", Toast.LENGTH_SHORT).show();
                }
            } else {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("gia_dea", edt_Giadea.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("gia_deb", edt_Giadeb.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("gia_dec", edt_Giadec.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("gia_ded", edt_Giaded.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("gia_lo", edt_Gialo.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("gia_xi2", edt_Giaxi2.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("gia_xi3", edt_Giaxi3.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("gia_xi4", edt_Giaxi4.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("max_dea", tview_Maxdea.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("max_deb", tview_Maxdeb.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("max_dec", tview_Maxdec.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("max_ded", tview_Maxded.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("max_lo", tview_Maxlo.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("max_xi2", tview_Maxxi2.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("max_xi3", tview_Maxxi3.getText().toString().replaceAll("\\.", ""));
                    jSONObject.put("max_xi4", tview_Maxxi4.getText().toString().replaceAll("\\.", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Đã lưu thành công", Toast.LENGTH_SHORT).show();
                db.QueryData("INSERT OR REPLACE Into tbl_chaytrang_acc (Username, Password, Setting) Values ('" + edt_account.getText().toString() + "', '" + edt_password.getText().toString() + "', '" + jSONObject + "')");
                finish();
            }
        });
    }

    public void RUN() {
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject jSONObject = new JSONObject();
        AtomicReference atomicReference = new AtomicReference("");
        if (Build.VERSION.SDK_INT >= 24) {
            CompletableFuture.runAsync(new Runnable() {
                private final JSONObject f$1;
                private final AtomicReference f$2;
                private final OkHttpClient f$3;
                {
                    this.f$1 = jSONObject;
                    this.f$2 = atomicReference;
                    this.f$3 = okHttpClient;
                }

                public final void run() {
                    lambda$RUN$0$Activity_AccWeb(this.f$1, this.f$2, this.f$3);
                }
            });
        }
    }

    public void lambda$RUN$0$Activity_AccWeb(JSONObject jSONObject, AtomicReference atomicReference, OkHttpClient okHttpClient) {
        try {
            jSONObject.put("Username", this.edt_account.getText().toString());
            jSONObject.put("Password", this.edt_password.getText().toString());
            atomicReference.set(okHttpClient.newCall(new Request.Builder().url("https://id.lotusapi.com/auth/sign-in").header("Content-Type", "application/json")
                    .post(RequestBody.Companion.create(jSONObject.toString(), MediaType.Companion.parse("application/json"))).build()).execute().body().string());
            JSONObject jSONObject2 = new JSONObject(atomicReference.toString());
            if (jSONObject2.has("IdToken")) {
                MainActivity.MyToken = jSONObject2.getString("IdToken");
                this.new_web = this.edt_account.getText().toString();
                Request.Builder builder = new Request.Builder();
                ResponseBody body = okHttpClient.newCall(builder.header("Authorization", "Bearer " + MainActivity.MyToken)
                        .url("https://lotto.lotusapi.com/user-game-settings/player").get().build()).execute().body();
                if (body != null) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        liner_caidat.setVisibility(View.VISIBLE);
                        edt_account.setEnabled(false);
                        edt_password.setEnabled(false);
                        btn_them_trang.setText("Thêm / Sửa trang");
                        btn_them_trang.setTextColor(SupportMenu.CATEGORY_MASK);
                        Toast.makeText(Activity_AccWeb.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    });
                    JSONArray jSONArray = new JSONArray(body.string());
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                        if (jSONObject3.getInt("GameType") == 0) {
                            if (jSONObject3.getInt("BetType") == 0) {
                                this.tview_Maxdeb.setText(jSONObject3.getString("MaxPointPerNumber"));
                            }
                        }
                        if (jSONObject3.getInt("GameType") == 0 && jSONObject3.getInt("BetType") == 1) {
                            this.tview_Maxlo.setText(jSONObject3.getString("MaxPointPerNumber"));
                        }
                        if (jSONObject3.getInt("GameType") == 0 && jSONObject3.getInt("BetType") == 2) {
                            this.tview_Maxxi2.setText(jSONObject3.getString("MaxPointPerNumber"));
                        }
                        if (jSONObject3.getInt("GameType") == 0 && jSONObject3.getInt("BetType") == 3) {
                            this.tview_Maxxi3.setText(jSONObject3.getString("MaxPointPerNumber"));
                        }
                        if (jSONObject3.getInt("GameType") == 0 && jSONObject3.getInt("BetType") == 4) {
                            this.tview_Maxxi4.setText(jSONObject3.getString("MaxPointPerNumber"));
                        }
                        if (jSONObject3.getInt("GameType") == 0 && jSONObject3.getInt("BetType") == 21) {
                            this.tview_Maxdea.setText(jSONObject3.getString("MaxPointPerNumber"));
                        }
                        if (jSONObject3.getInt("GameType") == 0 && jSONObject3.getInt("BetType") == 22) {
                            this.tview_Maxded.setText(jSONObject3.getString("MaxPointPerNumber"));
                        }
                        if (jSONObject3.getInt("GameType") == 0 && jSONObject3.getInt("BetType") == 23) {
                            this.tview_Maxdec.setText(jSONObject3.getString("MaxPointPerNumber"));
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(() -> {
                        this.btn_them_trang.setEnabled(true);
                    });
                    return;
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        this.btn_them_trang.setVisibility(View.GONE);
                        Toast.makeText(this, "Không thể lấy được Max dạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    });
                }
                return;
            }
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(Activity_AccWeb.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
            );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    private void init() {
        this.liner_caidat = (LinearLayout) findViewById(R.id.liner_caidat);
        this.btn_them_trang = (Button) findViewById(R.id.btn_them_trang);
        this.edt_account = (EditText) findViewById(R.id.edt_account);
        this.edt_password = (EditText) findViewById(R.id.edt_password);
        this.edt_Giadea = (EditText) findViewById(R.id.edt_Giadea);
        this.edt_Giadeb = (EditText) findViewById(R.id.edt_Giadeb);
        this.edt_Giadec = (EditText) findViewById(R.id.edt_Giadec);
        this.edt_Giaded = (EditText) findViewById(R.id.edt_Giaded);
        this.edt_Gialo = (EditText) findViewById(R.id.edt_Gialo);
        this.tview_Maxdea = (TextView) findViewById(R.id.tview_Maxdea);
        this.tview_Maxdeb = (TextView) findViewById(R.id.tview_Maxdeb);
        this.tview_Maxdec = (TextView) findViewById(R.id.tview_Maxdec);
        this.tview_Maxded = (TextView) findViewById(R.id.tview_Maxded);
        this.tview_Maxlo = (TextView) findViewById(R.id.tview_Maxlo);
        this.tview_Maxxi2 = (TextView) findViewById(R.id.tview_Maxxi2);
        this.tview_Maxxi3 = (TextView) findViewById(R.id.tview_Maxxi3);
        this.tview_Maxxi4 = (TextView) findViewById(R.id.tview_Maxxi4);
        this.edt_Giaxi2 = (EditText) findViewById(R.id.edt_Giaxi2);
        this.edt_Giaxi3 = (EditText) findViewById(R.id.edt_Giaxi3);
        this.edt_Giaxi4 = (EditText) findViewById(R.id.edt_Giaxi4);
    }
}
