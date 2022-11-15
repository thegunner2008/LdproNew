package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import tamhoang.ldpro4.BuildConfig;
import tamhoang.ldpro4.Login;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;
import tamhoang.ldpro4.util.Convert;

public class Frag_Home extends Fragment {
    String Imei = null;

    Button bt_trangchu;
    Button button_default;

    TextView edtImei;
    TextView tvHansd;
    TextView tvTaiKhoan;
    TextView tv_sodienthoai;
    TextView tvVersion;

    @SuppressLint("SetTextI18n")
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.frag_home, viewGroup, false);
        this.tvTaiKhoan = (TextView) inflate.findViewById(R.id.tv_taikhoan);
        this.tvHansd = (TextView) inflate.findViewById(R.id.tv_hansudung);
        this.edtImei = (TextView) inflate.findViewById(R.id.edt_imei);
        this.tv_sodienthoai = (TextView) inflate.findViewById(R.id.tv_sodienthoai);
        this.button_default = (Button) inflate.findViewById(R.id.button_default);
        this.bt_trangchu = (Button) inflate.findViewById(R.id.bt_trangchu);
        this.tvVersion = (TextView) inflate.findViewById(R.id.tv_version);
        Kiemtra();
        this.bt_trangchu.setOnClickListener(view ->
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://drive.google.com/file/d/13M3CsFtk_uxlBkukOeT-vTFl3y-57Rst/view")))
        );
        this.button_default.setOnClickListener(view -> {
            MainActivity.hanSuDung = "";
            Kiemtra();
            if (MainActivity.hanSuDung.length() > 5) {
                FragmentActivity activity = getActivity();
                Toast.makeText(activity, "Sử dụng đến: " + MainActivity.hanSuDung, Toast.LENGTH_SHORT).show();
            }
        });
        this.tvVersion.setText("Phiên bản: " + BuildConfig.VERSION_NAME + " - " + Convert.versionCodeToDate(BuildConfig.VERSION_CODE));
        return inflate;
    }

    public void Kiemtra() {
        this.Imei = Login.Imei;
        this.edtImei.setText(this.Imei);
        if (isNetworkConnected() && this.Imei != null) {
            //check();
            fakeTaiKhoan();
        } else if (this.Imei == null) {
            startActivity(new Intent(getActivity(), Login.class));
        } else {
            Toast.makeText(getActivity(), "Kiểm tra kết nối Internet!", Toast.LENGTH_SHORT).show();
        }
        this.tvHansd.setText(MainActivity.hanSuDung);
        this.tvTaiKhoan.setText(MainActivity.tenAcc);
    }

    public void check() {
        if (this.Imei != null) {
            try {
                Volley.newRequestQueue(getActivity()).add(new StringRequest(1, MainActivity.Get_link_signin(), str -> {
                    try {
                        MainActivity.thongTinAcc = new JSONObject(str).getJSONArray("listKHs").getJSONObject(0);
                        String dateString = MainActivity.thongTinAcc.getString("date").replaceAll("-", "");
                        String hsd = dateString.substring(6) + "/" + dateString.substring(4, 6) + "/" + dateString.substring(0, 4);
                        tvHansd.setText(hsd);
                        MainActivity.hanSuDung = hsd;

                        MainActivity.tenAcc = MainActivity.thongTinAcc.getString("acc");
                        String acc = MainActivity.thongTinAcc.getString("acc");
                        tvTaiKhoan.setText(acc);
                        tv_sodienthoai.setText(MainActivity.thongTinAcc.getString("k_tra"));
                        try {
                            MainActivity.thongTinAcc.getString("date");
                            float time = (float) (((((new SimpleDateFormat("yyyy-MM-dd").parse(MainActivity.thongTinAcc.getString("date")).getTime() - new Date().getTime()) / 1000) / 60) / 60) / 24);
                            if (time >= 6.0f || time <= 0.0f) {
                                if (time < 0.0f && getActivity() != null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Thông báo hạn sử dụng:");
                                    builder.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ Đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                                    builder.setNegativeButton("Đóng", (dialogInterface, i) -> dialogInterface.cancel());
                                    builder.create().show();
                                }
                            } else if (getActivity() != null) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                                dialogBuilder.setTitle("Thông báo hạn sử dụng:");
                                dialogBuilder.setMessage("Hạn sử dụng còn lại " + ((int) time) + " ngày! \nHãy liên hệ Đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                                dialogBuilder.setNegativeButton("Đóng", (dialogInterface, i) -> dialogInterface.cancel());
                                dialogBuilder.create().show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }, volleyError -> {

                }) {
                    public Map<String, String> getParams() {
                        HashMap parameters = new HashMap();
                        parameters.put("password", "admin");
                        parameters.put("login", "admin");
                        parameters.put("img", Imei);
                        parameters.put("version", BuildConfig.VERSION_NAME);
                        return parameters;
                    }
                });
            } catch (Exception unused) {
                Toast.makeText(getActivity(), "Kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void fakeTaiKhoan() {
        try {
            MainActivity.thongTinAcc = new JSONObject("{'date':'20221230','acc':'vip', 'k_tra':'091231231', '':''}");
            String dateString = MainActivity.thongTinAcc.getString("date").replaceAll("-", "");
            String hsd = dateString.substring(6) + "/" + dateString.substring(4, 6) + "/" + dateString.substring(0, 4);
            tvHansd.setText(hsd);
            MainActivity.hanSuDung = hsd;

            MainActivity.tenAcc = MainActivity.thongTinAcc.getString("acc");
            String acc = MainActivity.thongTinAcc.getString("acc");
            tvTaiKhoan.setText(acc);
            tv_sodienthoai.setText(MainActivity.thongTinAcc.getString("k_tra"));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally { }
    }

    private boolean isNetworkConnected() {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
