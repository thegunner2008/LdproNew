package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.volley.Response;
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
    String f197TK = "";
    Button bt_trangchu;
    Button button_default;

    /* renamed from: db */
    Database f198db;
    TextView edtImei;
    TextView tvHansd;
    TextView tvTaiKhoan;
    TextView tv_sodienthoai;
    String viewDate;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.frag_home, viewGroup, false);
        this.f198db = new Database(getActivity());
        String Get_link = new MainActivity().Get_link();
        this.viewDate = Get_link + "json_date1.php";
        this.tvTaiKhoan = (TextView) inflate.findViewById(R.id.tv_taikhoan);
        this.tvHansd = (TextView) inflate.findViewById(R.id.tv_hansudung);
        this.edtImei = (TextView) inflate.findViewById(R.id.edt_imei);
        this.tv_sodienthoai = (TextView) inflate.findViewById(R.id.tv_sodienthoai);
        this.button_default = (Button) inflate.findViewById(R.id.button_default);
        this.bt_trangchu = (Button) inflate.findViewById(R.id.bt_trangchu);
        this.f198db = new Database(getActivity());
        Kiemtra();
        this.bt_trangchu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Frag_Home.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.ldpro.org")));
            }
        });
        this.button_default.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.myDate = "";
                Frag_Home.this.Kiemtra();
                if (MainActivity.myDate.length() > 5) {
                    FragmentActivity activity = Frag_Home.this.getActivity();
                    Toast.makeText(activity, "Sử dụng đến: " + MainActivity.myDate, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return inflate;
    }

    public void Kiemtra() {
        this.Imei = Login.Imei;
        this.edtImei.setText(this.Imei);
        if (isNetworkConnected() && this.Imei != null) {
            check();
        } else if (this.Imei == null) {
            startActivity(new Intent(getActivity(), Login.class));
        } else {
            Toast.makeText(getActivity(), "Kiểm tra kết nối Internet!", Toast.LENGTH_SHORT).show();
        }
        this.tvHansd.setText(MainActivity.myDate);
        this.tvTaiKhoan.setText(MainActivity.Acc_manager);
    }

    public void check() {
        if (this.Imei != null) {
            try {
                Volley.newRequestQueue(getActivity()).add(new StringRequest(1, this.viewDate, new Response.Listener<String>() {
                    public void onResponse(String str) {
                        try {
                            MainActivity.listKH = new JSONObject(str).getJSONArray("listKHs").getJSONObject(0);
                            String replaceAll = MainActivity.listKH.getString("date").replaceAll("-", "");
                            String str2 = replaceAll.substring(6) + "/" + replaceAll.substring(4, 6) + "/" + replaceAll.substring(0, 4);
                            Frag_Home.this.tvHansd.setText(str2);
                            Frag_Home.this.f197TK = "";
                            Frag_Home.this.f197TK = MainActivity.listKH.getString("acc");
                            MainActivity.myDate = str2;
                            MainActivity.Acc_manager = MainActivity.listKH.getString("acc");
                            Frag_Home.this.tvTaiKhoan.setText(Frag_Home.this.f197TK);
                            Frag_Home.this.tv_sodienthoai.setText(MainActivity.listKH.getString("k_tra"));
                            try {
                                MainActivity.listKH.getString("date");
                                float time = (float) (((((new SimpleDateFormat("yyyy-MM-dd").parse(MainActivity.listKH.getString("date")).getTime() - new Date().getTime()) / 1000) / 60) / 60) / 24);
                                if (time >= 6.0f || time <= 0.0f) {
                                    if (time < 0.0f && Frag_Home.this.getActivity() != null) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Frag_Home.this.getActivity());
                                        builder.setTitle((CharSequence) "Thông báo hạn sử dụng:");
                                        builder.setMessage((CharSequence) "Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ Đại lý hoặc SĐT: " + MainActivity.listKH.getString("k_tra") + " để gia hạn");
                                        builder.setNegativeButton((CharSequence) "Đóng", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                        builder.create().show();
                                    }
                                } else if (Frag_Home.this.getActivity() != null) {
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(Frag_Home.this.getActivity());
                                    builder2.setTitle("Thông báo hạn sử dụng:");
                                    builder2.setMessage("Hạn sử dụng còn lại " + ((int) time) + " ngày! \nHãy liên hệ Đại lý hoặc SĐT: " + MainActivity.listKH.getString("k_tra") + " để gia hạn");
                                    builder2.setNegativeButton("Đóng", (dialogInterface, i) -> dialogInterface.cancel());
                                    builder2.create().show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                }, volleyError -> {

                }) {
                    /* access modifiers changed from: protected */
                    public Map<String, String> getParams() throws AuthFailureError {
                        HashMap hashMap = new HashMap();
                        hashMap.put("imei", Frag_Home.this.Imei);
                        hashMap.put("serial", Login.serial);
                        return hashMap;
                    }
                });
            } catch (Exception unused) {
                Toast.makeText(getActivity(), "Kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkConnected() {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
