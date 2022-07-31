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

import tamhoang.ldpro4.Login;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_Home extends Fragment {
    String Imei = null;

    /* renamed from: TK */
    Button bt_trangchu;
    Button button_default;

    /* renamed from: db */
    TextView edtImei;
    TextView tvHansd;
    TextView tvTaiKhoan;
    TextView tv_sodienthoai;
    String viewDate;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.frag_home, viewGroup, false);
        String Get_link = new MainActivity().Get_link();
        this.viewDate = Get_link + "json_date1.php";
        this.tvTaiKhoan = (TextView) inflate.findViewById(R.id.tv_taikhoan);
        this.tvHansd = (TextView) inflate.findViewById(R.id.tv_hansudung);
        this.edtImei = (TextView) inflate.findViewById(R.id.edt_imei);
        this.tv_sodienthoai = (TextView) inflate.findViewById(R.id.tv_sodienthoai);
        this.button_default = (Button) inflate.findViewById(R.id.button_default);
        this.bt_trangchu = (Button) inflate.findViewById(R.id.bt_trangchu);
        Kiemtra();
        this.bt_trangchu.setOnClickListener(view ->
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.ldpro.pro")))
        );
        this.button_default.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.hanSuDung = "";
                Kiemtra();
                if (MainActivity.hanSuDung.length() > 5) {
                    FragmentActivity activity = getActivity();
                    Toast.makeText(activity, "Sử dụng đến: " + MainActivity.hanSuDung, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                Volley.newRequestQueue(getActivity()).add(new StringRequest(1, viewDate, str -> {
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
                                    builder.setTitle((CharSequence) "Thông báo hạn sử dụng:");
                                    builder.setMessage((CharSequence) "Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ Đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                                    builder.setNegativeButton((CharSequence) "Đóng", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
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
                    public Map<String, String> getParams() throws AuthFailureError {
                        HashMap hashMap = new HashMap();
                        hashMap.put("imei", Imei);
                        hashMap.put("serial", Login.serial);
                        return hashMap;
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
