package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.Login;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationBindObject;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_Database extends Fragment {
    String[] ArrayGiai;
    String Imei = null;
    Button btnDelete;
    Button btn_tt;
    Database db;
    RadioGroup gr1;
    RadioGroup gr2;
    RadioButton ketquanet;
    WebView mWebView;
    RadioButton minhngoc;
    RadioButton nazzy;
    View v;
    RadioButton xosome;
    RadioButton xsme;
    RadioButton xsmn;

    @SuppressLint({"WrongConstant", "HardwareIds"})
    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v2 = inflater.inflate(R.layout.frag_database, container, false);
        this.db = new Database(getActivity());
        this.btn_tt = (Button) v2.findViewById(R.id.btn_tt);
        this.btnDelete = (Button) v2.findViewById(R.id.btn_Delete);
        this.xosome = (RadioButton) v2.findViewById(R.id.xosome);
        this.minhngoc = (RadioButton) v2.findViewById(R.id.minhngoc);
        this.nazzy = (RadioButton) v2.findViewById(R.id.nazzy);
        this.ketquanet = (RadioButton) v2.findViewById(R.id.ketquanet);
        this.xsme = (RadioButton) v2.findViewById(R.id.xsme);
        this.xsmn = (RadioButton) v2.findViewById(R.id.xsmn);
        this.gr1 = (RadioGroup) v2.findViewById(R.id.gr1);
        this.gr2 = (RadioGroup) v2.findViewById(R.id.gr2);
        this.nazzy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass1 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Frag_Database.this.gr1.clearCheck();
                if (Frag_Database.this.nazzy.isChecked()) {
                    try {
                        new MainActivity();
                        String ngay = MainActivity.Get_ngay();
                        final String str_date = MainActivity.Get_date();
                        Volley.newRequestQueue(Frag_Database.this.getActivity()).add(new StringRequest(1, "http://thongke.nazzy.vn/handler/thongke.ashx?t=kqxsmb&date=" + (ngay.substring(3, 5) + "/" + ngay.substring(0, 2) + "/" + ngay.substring(6)), new Response.Listener<String>() {
                            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass1.AnonymousClass1 */

                            public void onResponse(String response) {
                                FragmentActivity activity;
                                StringBuilder sb;
                                String Str = "";
                                try {
                                    JSONObject outerObject = new JSONObject(response);
                                    if (outerObject.getString("Ngay").contains(MainActivity.Get_ngay())) {
                                        Frag_Database.this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                        Str = (((((((((((((((((((((((((("'" + outerObject.getString("GDB") + "',") + "'" + outerObject.getString("G1") + "',") + "'" + outerObject.getString("G21") + "',") + "'" + outerObject.getString("G22") + "',") + "'" + outerObject.getString("G31") + "',") + "'" + outerObject.getString("G32") + "',") + "'" + outerObject.getString("G33") + "',") + "'" + outerObject.getString("G34") + "',") + "'" + outerObject.getString("G35") + "',") + "'" + outerObject.getString("G36") + "',") + "'" + outerObject.getString("G41") + "',") + "'" + outerObject.getString("G42") + "',") + "'" + outerObject.getString("G43") + "',") + "'" + outerObject.getString("G44") + "',") + "'" + outerObject.getString("G51") + "',") + "'" + outerObject.getString("G52") + "',") + "'" + outerObject.getString("G53") + "',") + "'" + outerObject.getString("G54") + "',") + "'" + outerObject.getString("G55") + "',") + "'" + outerObject.getString("G56") + "',") + "'" + outerObject.getString("G61") + "',") + "'" + outerObject.getString("G62") + "',") + "'" + outerObject.getString("G63") + "',") + "'" + outerObject.getString("G71") + "',") + "'" + outerObject.getString("G72") + "',") + "'" + outerObject.getString("G73") + "',") + "'" + outerObject.getString("G74") + "')";
                                    }
                                    if (Str.length() > 0) {
                                        Frag_Database.this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str);
                                        activity = Frag_Database.this.getActivity();
                                        sb = new StringBuilder();
                                        sb.append("Đã tải xong kết quả ngày: ");
                                        sb.append(MainActivity.Get_ngay());
                                        Toast.makeText(activity, sb.toString(), 1).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    if (Str.length() > 0) {
                                        Frag_Database.this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str);
                                        activity = Frag_Database.this.getActivity();
                                        sb = new StringBuilder();
                                    }
                                } catch (Throwable th) {
                                    if (Str.length() > 0) {
                                        Frag_Database.this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str);
                                        FragmentActivity activity2 = Frag_Database.this.getActivity();
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("Đã tải xong kết quả ngày: ");
                                        sb2.append(MainActivity.Get_ngay());
                                        Toast.makeText(activity2, sb2.toString(), 1).show();
                                    } else {
                                        Toast.makeText(Frag_Database.this.getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                                    }
                                    throw th;
                                }
                                Toast.makeText(Frag_Database.this.getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass1.AnonymousClass2 */

                            @Override // com.android.volley.Response.ErrorListener
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {
                            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass1.AnonymousClass3 */

                            /* access modifiers changed from: protected */
                            @Override // com.android.volley.Request
                            public Map<String, String> getParams() throws AuthFailureError {
                                return new HashMap<>();
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(Frag_Database.this.getActivity(), "Kiểm tra kết nối mạng!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        this.mWebView = (WebView) v2.findViewById(R.id.fragment_main_webview);
        this.Imei = ((TelephonyManager) getActivity().getSystemService("phone")).getDeviceId();
        if (isNetworkConnected() && this.Imei != null) {
            check();
        }
        this.btn_tt.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass2 */

            public void onClick(View v) {
                new MainActivity();
                String mDate = MainActivity.Get_date();
                String mNgay = MainActivity.Get_ngay();
                Database database = Frag_Database.this.db;
                Cursor cursor = database.GetData("Select * From Ketqua WHERE ngay = '" + mDate + "'");
                cursor.moveToFirst();
                int i2 = 2;
                while (true) {
                    if (i2 >= 29) {
                        break;
                    }
                    try {
                        if (cursor.isNull(i2)) {
                            break;
                        } else if (!Congthuc.isNumeric(cursor.getString(i2))) {
                            break;
                        } else {
                            i2++;
                        }
                    } catch (Exception e) {
                        FragmentActivity activity = Frag_Database.this.getActivity();
                        Toast.makeText(activity, "Chưa có kết quả ngày: " + mNgay, Toast.LENGTH_LONG).show();
                    }
                }
                if (i2 >= 29) {
                    try {
                        Frag_Database.this.db.Tinhtien(mDate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    FragmentActivity activity2 = Frag_Database.this.getActivity();
                    Toast.makeText(activity2, "Đã tính tiền xong ngày " + mNgay, Toast.LENGTH_LONG).show();
                } else {
                    FragmentActivity activity3 = Frag_Database.this.getActivity();
                    Toast.makeText(activity3, "Chưa có kết quả ngày " + mNgay + " hãy cập nhật thủ công.", 1).show();
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        });
        this.btnDelete.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass3 */

            public void onClick(View view) {
                new MainActivity();
                final String mDate = MainActivity.Get_date();
                String[] menus = {"Xóa vẫn lưu lại công nợ", "Xóa hết cơ sở dữ liệu", "Xóa hết dữ liệu hôm nay"};
                PopupMenu popupL = new PopupMenu(Frag_Database.this.getActivity(), view);
                for (int i = 0; i < menus.length; i++) {
                    popupL.getMenu().add(1, i, i, menus[i]);
                }
                new AlertDialog.Builder(Frag_Database.this.getActivity());
                popupL.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass3.AnonymousClass1 */

                    public boolean onMenuItemClick(MenuItem item) {
                        int order = item.getOrder();
                        if (order == 0) {
                            Frag_Database.this.DelAllSQL_Congno();
                        } else if (order == 1) {
                            Frag_Database.this.DelAllSQL();
                        } else if (order == 2) {
                            AlertDialog.Builder bui = new AlertDialog.Builder(Frag_Database.this.getActivity());
                            bui.setTitle("Xoá hết dữ liệu hôm nay?");
                            bui.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass3.AnonymousClass1.AnonymousClass1 */

                                public void onClick(DialogInterface dialog, int which) {
                                    Frag_Database.this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + mDate + "'");
                                    Frag_Database.this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + mDate + "'");
                                    Database database = Frag_Database.this.db;
                                    database.QueryData("DELETE FROM Chat_database WHERE ngay_nhan = '" + mDate + "'");
                                    Toast.makeText(Frag_Database.this.getActivity(), "Đã xoá", Toast.LENGTH_LONG).show();
                                }
                            });
                            bui.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass3.AnonymousClass1.AnonymousClass2 */

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            bui.create().show();
                        }
                        return true;
                    }
                });
                popupL.show();
            }
        });
        this.minhngoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass4 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Frag_Database.this.gr1.clearCheck();
                if (Frag_Database.this.minhngoc.isChecked()) {
                    Frag_Database.this.DisplayKQnet();
                }
            }
        });
        this.ketquanet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass5 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Frag_Database.this.gr2.clearCheck();
                if (Frag_Database.this.ketquanet.isChecked()) {
                    Frag_Database.this.DisplayKQnetNew();
                }
            }
        });
        this.xosome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass6 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Frag_Database.this.gr1.clearCheck();
                if (Frag_Database.this.xosome.isChecked()) {
                    Frag_Database.this.DisplayXSme();
                }
            }
        });
        this.xsme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass7 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Frag_Database.this.gr2.clearCheck();
                if (Frag_Database.this.xsme.isChecked()) {
                    Frag_Database.this.DisplayXSmeNew();
                }
            }
        });
        this.xsmn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass8 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Frag_Database.this.gr2.clearCheck();
                if (Frag_Database.this.xsmn.isChecked()) {
                    Frag_Database.this.DisplayXSMNNew();
                }
            }
        });
        this.mWebView.addJavascriptInterface(new NotificationBindObject(getActivity().getApplicationContext()), "NotificationBind");
        setUpWebViewDefaults(this.mWebView);
        if (savedInstanceState != null) {
            this.mWebView.restoreState(savedInstanceState);
        }
        DisplayXSmeNew();
        return v2;
    }

    private boolean isNetworkConnected() {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override // android.support.v4.app.Fragment
    public void onStop() {
        super.onStop();
        this.mWebView.clearCache(true);
    }

    public void check() {
        try {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass9 */
            Volley.newRequestQueue(getActivity()).add(new StringRequest(1, "https://api.ldpro.us/subcription", response -> {
                try {
                    MainActivity.listKH = new JSONObject(response).getJSONArray("listKHs").getJSONObject(0);
                    String str_ngay = MainActivity.listKH.getString("date").replaceAll("-", "");
                    MainActivity.myDate = str_ngay.substring(6) + "/" + str_ngay.substring(4, 6) + "/" + str_ngay.substring(0, 4);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, new Response.ErrorListener() {
                /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass10 */

                @Override // com.android.volley.Response.ErrorListener
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass11 */

                /* access modifiers changed from: protected */
                @Override // com.android.volley.Request
                public Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("imei", Frag_Database.this.Imei);
                    parameters.put("serial", Login.serial);
                    return parameters;
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Kiểm tra kết nối mạng!", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT > 11) {
            settings.setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public void DisplayKQnetNew() {
        this.mWebView.setVisibility(View.GONE);
        new MainActivity();
        String mDate = MainActivity.Get_ngay().replaceAll("/", "-");
        this.mWebView.loadUrl("https://ketqua.net/xo-so-mien-bac.php?ngay=" + mDate);
        this.mWebView.setWebViewClient(new WebViewClient() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass12 */

            public void onPageFinished(WebView view, String url) {
                Frag_Database.this.mWebView.setVisibility(View.VISIBLE);
                Frag_Database frag_Database = Frag_Database.this;
                frag_Database.loadJavascript("(function() { return " + "document.getElementsByClassName('table table-condensed kqcenter kqvertimarginw table-kq-border table-kq-hover-div table-bordered kqbackground table-kq-bold-border tb-phoi-border watermark table-striped')[0].innerText;" + "; })();");
            }
        });
    }

    public void loadJavascript(String javascript) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.mWebView.evaluateJavascript(javascript, new ValueCallback<String>() {
                /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass13 */

                public void onReceiveValue(String s) {
                    String msg;
                    JsonReader reader = new JsonReader(new StringReader(s));
                    reader.setLenient(true);
                    try {
                        if (!(reader.peek() == JsonToken.NULL || reader.peek() != JsonToken.STRING || (msg = reader.nextString()) == null)) {
                            Frag_Database.this.ArrayGiai = msg.trim().replaceAll("\t", "!").replaceAll("\n", "!").replaceAll("!!", "!").replaceAll("!!", "!").replaceAll("!!", "!").replaceAll("!!", "!").split("!");
                            if (Frag_Database.this.ArrayGiai.length <= 0) {
                                Toast.makeText(Frag_Database.this.getActivity(), "Kiểm tra lại kết nối Internet!", Toast.LENGTH_LONG).show();
                            } else if (Frag_Database.this.ArrayGiai.length > 16) {
                                if (Frag_Database.this.xosome.isChecked()) {
                                    Frag_Database.this.PhantichXosome();
                                } else if (Frag_Database.this.xsme.isChecked()) {
                                    Frag_Database.this.PhantichXosomeNew();
                                } else if (Frag_Database.this.xsmn.isChecked()) {
                                    Frag_Database.this.PhantichXosomeNewNew();
                                } else {
                                    Frag_Database.this.PhantichMinhngoc();
                                }
                            }
                        }
                        try {
                            reader.close();
                        } catch (IOException e) {
                        }
                    } catch (IOException e2) {
                        Log.e("TAG", "MainActivity: IOException", e2);
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Throwable th) {
                        try {
                            reader.close();
                        } catch (IOException e3) {
                        }
                        throw th;
                    }
                }
            });
            return;
        }
        WebView webView = this.mWebView;
        webView.loadUrl("javascript:" + javascript);
    }

    public void DelAllSQL() {
        AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
        bui.setTitle("Xoá hết cơ sở dữ liệu?");
        bui.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass14 */

            public void onClick(DialogInterface dialog, int which) {
                Frag_Database.this.db.QueryData("DROP TABLE if exists Chat_database");
                Frag_Database.this.db.QueryData("DROP TABLE if exists tbl_tinnhanS");
                Frag_Database.this.db.QueryData("DROP TABLE if exists tbl_soctS");
                Frag_Database.this.db.Creat_TinNhanGoc();
                Frag_Database.this.db.Creat_SoCT();
                Frag_Database.this.db.Create_table_Chat();
                Toast.makeText(Frag_Database.this.getActivity(), "Đã xoá", Toast.LENGTH_LONG).show();
            }
        });
        bui.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass15 */

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        bui.create().show();
    }

    public void DelAllSQL_Congno() {
        AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
        bui.setTitle("Xoá dữ liệu vẫn giữ công nợ?");
        final List<String> mTenKH = new ArrayList<>();
        final List<String> mSodt = new ArrayList<>();
        final List<String> mSoTien = new ArrayList<>();
        bui.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass16 */

            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.add(5, -1);
                String Ngay = sdf.format(new Date(calendar.getTimeInMillis()));
                Cursor cursor = Frag_Database.this.db.GetData("Select tbl_soctS.ten_kh\n, SUM(tbl_soctS.ket_qua * (100-tbl_soctS.diem_khachgiu)/100)/1000  as NoCu,   \ntbl_soctS.so_dienthoai, tbl_kh_new.type_kh  \nFROM tbl_soctS INNER JOIN tbl_kh_new ON tbl_soctS.so_dienthoai = tbl_kh_new.sdt\nGROUP BY tbl_soctS.ten_kh ORDER BY tbl_soctS.type_kh DESC");
                mTenKH.clear();
                mSodt.clear();
                mSoTien.clear();
                while (cursor.moveToNext()) {
                    mTenKH.add(cursor.getString(0));
                    mSodt.add(cursor.getString(2));
                    List list = mSoTien;
                    list.add((cursor.getDouble(1) * 1000.0d) + "");
                }
                Frag_Database.this.db.QueryData("DROP TABLE if exists Chat_database");
                Frag_Database.this.db.QueryData("DROP TABLE if exists tbl_tinnhanS");
                Frag_Database.this.db.QueryData("DROP TABLE if exists tbl_soctS");
                Frag_Database.this.db.Creat_TinNhanGoc();
                Frag_Database.this.db.Creat_SoCT();
                Frag_Database.this.db.Create_table_Chat();
                for (int i = 0; i < mTenKH.size(); i++) {
                    Frag_Database.this.db.QueryData("Insert Into tbl_soctS (ngay_nhan, ten_kh, so_dienthoai, the_loai, ket_qua) Values ('" + Ngay + "','" + ((String) mTenKH.get(i)) + "','" + ((String) mSodt.get(i)) + "', 'cn'," + ((String) mSoTien.get(i)) + ")");
                }
                Toast.makeText(Frag_Database.this.getActivity(), "Đã xoá", Toast.LENGTH_LONG).show();
            }
        });
        bui.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass17 */

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        bui.create().show();
    }

    public void DisplayXSme() {
        this.mWebView.setVisibility(View.GONE);
        new MainActivity();
        String mDate = MainActivity.Get_ngay().replaceAll("/", "-");
        this.mWebView.loadUrl("https://xosodaiphat.com/xsmb-" + mDate + ".html");
        this.mWebView.setWebViewClient(new WebViewClient() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass18 */

            public void onPageFinished(WebView view, String url) {
                String str;
                Frag_Database.this.loadJavascript("document.getElementsByClassName('embeded-breadcrumb')[0].style.display = 'none';\ndocument.getElementsByClassName('tit-mien')[0].style.display = 'none';");
                Frag_Database.this.mWebView.setVisibility(View.VISIBLE);
                if (Frag_Database.this.xosome.isChecked()) {
                    str = "document.getElementsByClassName('table table-bordered table-striped table-xsmb')[0].innerText;";
                } else {
                    str = "document.getElementsByClassName('table-result')[0].innerText;";
                }
                Frag_Database frag_Database = Frag_Database.this;
                frag_Database.loadJavascript("(function() { return " + str + "; })();");
            }
        });
    }

    public void DisplayXSmeNew() {
        this.mWebView.setVisibility(View.GONE);
        new MainActivity();
        String mDate = MainActivity.Get_ngay().replaceAll("/", "-");
        this.mWebView.loadUrl("https://xoso.me/embedded/kq-mienbac?ngay_quay=" + mDate);
        this.mWebView.setWebViewClient(new WebViewClient() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass19 */

            public void onPageFinished(WebView view, String url) {
                Frag_Database.this.mWebView.setVisibility(View.VISIBLE);
                Frag_Database frag_Database = Frag_Database.this;
                frag_Database.loadJavascript("(function() { return " + "document.getElementsByClassName('kqmb extendable')[0].innerText;" + "; })();");
            }
        });
    }

    public void DisplayXSMNNew() {
        this.mWebView.setVisibility(View.GONE);
        new MainActivity();
        String mDate = MainActivity.Get_ngay().replaceAll("/", "-");
        this.mWebView.loadUrl("https://xsmn.me/embedded/kq-mienbac?ngay_quay=" + mDate);
        this.mWebView.setWebViewClient(new WebViewClient() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass20 */

            public void onPageFinished(WebView view, String url) {
                Frag_Database.this.mWebView.setVisibility(View.VISIBLE);
                Frag_Database frag_Database = Frag_Database.this;
                frag_Database.loadJavascript("(function() { return " + "document.getElementsByClassName('extendable kqmb colgiai')[0].innerText;" + "; })();");
            }
        });
    }

    public void DisplayKQnet() {
        this.mWebView.setVisibility(View.GONE);
        new MainActivity();
        String mDate = MainActivity.Get_ngay().replaceAll("/", "-");
        this.mWebView.loadUrl("https://xoso.com.vn/xsmb-" + mDate + ".html");
        this.mWebView.setWebViewClient(new WebViewClient() {
            /* class tamhoang.ldpro4.Fragment.Frag_Database.AnonymousClass21 */

            public void onPageFinished(WebView view, String url) {
                Frag_Database.this.mWebView.setVisibility(View.VISIBLE);
                Frag_Database frag_Database = Frag_Database.this;
                frag_Database.loadJavascript("(function() { return " + "document.getElementsByClassName('table-result')[0].innerText;" + "; })();");
            }
        });
    }

    public void PhantichXosome() {
        new MainActivity();
        String str_date = MainActivity.Get_date();
        boolean Ktra = true;
        try {
            String Str_sql = "InSert Into KETQUA VALUES(null,'" + str_date + "',";
            for (int i = 0; i < this.ArrayGiai.length; i++) {
                if (Congthuc.isNumeric(this.ArrayGiai[i])) {
                    Str_sql = Str_sql + "'" + this.ArrayGiai[i] + "',";
                } else if (this.ArrayGiai[i].length() < 2) {
                    Ktra = false;
                }
            }
            if (Ktra) {
                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                StringBuilder sb = new StringBuilder();
                sb.append(Str_sql.substring(0, Str_sql.length() - 1));
                sb.append(")");
                this.db.QueryData(sb.toString());
                Toast.makeText(getActivity(), "Đã tải xong kết quả ngày: " + MainActivity.Get_ngay(), Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(getActivity(), "Chưa có kết quả!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }

    public void PhantichXosomeNew() {
        FragmentActivity activity;
        StringBuilder sb;
        new MainActivity();
        String str_date = MainActivity.Get_date();
        try {
            if (Congthuc.isNumeric(this.ArrayGiai[2])) {
                String Str = "'" + this.ArrayGiai[2].trim() + "',";
                if (Congthuc.isNumeric(this.ArrayGiai[4])) {
                    String Str2 = Str + "'" + this.ArrayGiai[4].trim() + "',";
                    if (Congthuc.isNumeric(this.ArrayGiai[6])) {
                        String Str3 = (Str2 + "'" + this.ArrayGiai[6].trim().substring(0, 5) + "',") + "'" + this.ArrayGiai[6].trim().substring(5, this.ArrayGiai[6].trim().length()) + "',";
                        if (Congthuc.isNumeric(this.ArrayGiai[8])) {
                            String Str4 = (((((Str3 + "'" + this.ArrayGiai[8].trim().substring(0, 5) + "',") + "'" + this.ArrayGiai[8].trim().substring(5, 10) + "',") + "'" + this.ArrayGiai[8].trim().substring(10, 15) + "',") + "'" + this.ArrayGiai[8].trim().substring(15, 20) + "',") + "'" + this.ArrayGiai[8].trim().substring(20, 25) + "',") + "'" + this.ArrayGiai[8].trim().substring(25, this.ArrayGiai[8].trim().length()) + "',";
                            if (Congthuc.isNumeric(this.ArrayGiai[10])) {
                                String Str5 = (((Str4 + "'" + this.ArrayGiai[10].trim().substring(0, 4) + "',") + "'" + this.ArrayGiai[10].trim().substring(4, 8) + "',") + "'" + this.ArrayGiai[10].trim().substring(8, 12) + "',") + "'" + this.ArrayGiai[10].trim().substring(12, this.ArrayGiai[10].trim().length()) + "',";
                                if (Congthuc.isNumeric(this.ArrayGiai[12])) {
                                    String Str6 = (((((Str5 + "'" + this.ArrayGiai[12].trim().substring(0, 4) + "',") + "'" + this.ArrayGiai[12].trim().substring(4, 8) + "',") + "'" + this.ArrayGiai[12].trim().substring(8, 12) + "',") + "'" + this.ArrayGiai[12].trim().substring(12, 16) + "',") + "'" + this.ArrayGiai[12].trim().substring(16, 20) + "',") + "'" + this.ArrayGiai[12].trim().substring(20, this.ArrayGiai[12].trim().length()) + "',";
                                    if (Congthuc.isNumeric(this.ArrayGiai[14])) {
                                        String Str7 = ((Str6 + "'" + this.ArrayGiai[14].trim().substring(0, 3) + "',") + "'" + this.ArrayGiai[14].trim().substring(3, 6) + "',") + "'" + this.ArrayGiai[14].trim().substring(6, this.ArrayGiai[14].trim().length()) + "',";
                                        if (Congthuc.isNumeric(this.ArrayGiai[16])) {
                                            String Str8 = (((Str7 + "'" + this.ArrayGiai[16].trim().substring(0, 2) + "',") + "'" + this.ArrayGiai[16].trim().substring(2, 4) + "',") + "'" + this.ArrayGiai[16].trim().substring(4, 6) + "',") + "'" + this.ArrayGiai[16].trim().substring(6, this.ArrayGiai[16].trim().length()) + "')";
                                            if (Str8.length() > 185) {
                                                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str8);
                                                activity = getActivity();
                                                sb = new StringBuilder();
                                                sb.append("Đã tải xong kết quả ngày: ");
                                                sb.append(MainActivity.Get_ngay());
                                                Toast.makeText(activity, sb.toString(), Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                                        } else if (Str7.length() > 185) {
                                            this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                            this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str7);
                                            FragmentActivity activity2 = getActivity();
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("Đã tải xong kết quả ngày: ");
                                            sb2.append(MainActivity.Get_ngay());
                                            Toast.makeText(activity2, sb2.toString(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                                        }
                                    } else if (Str6.length() > 185) {
                                        this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                        this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str6);
                                        FragmentActivity activity3 = getActivity();
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("Đã tải xong kết quả ngày: ");
                                        sb3.append(MainActivity.Get_ngay());
                                        Toast.makeText(activity3, sb3.toString(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                                    }
                                } else if (Str5.length() > 185) {
                                    this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                    this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str5);
                                    FragmentActivity activity4 = getActivity();
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Đã tải xong kết quả ngày: ");
                                    sb4.append(MainActivity.Get_ngay());
                                    Toast.makeText(activity4, sb4.toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                                }
                            } else if (Str4.length() > 185) {
                                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str4);
                                FragmentActivity activity5 = getActivity();
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("Đã tải xong kết quả ngày: ");
                                sb5.append(MainActivity.Get_ngay());
                                Toast.makeText(activity5, sb5.toString(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                            }
                        } else if (Str3.length() > 185) {
                            this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                            this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str3);
                            FragmentActivity activity6 = getActivity();
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("Đã tải xong kết quả ngày: ");
                            sb6.append(MainActivity.Get_ngay());
                            Toast.makeText(activity6, sb6.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                        }
                    } else if (Str2.length() > 185) {
                        this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                        this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str2);
                        FragmentActivity activity7 = getActivity();
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("Đã tải xong kết quả ngày: ");
                        sb7.append(MainActivity.Get_ngay());
                        Toast.makeText(activity7, sb7.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                    }
                } else if (Str.length() > 185) {
                    this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                    this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str);
                    FragmentActivity activity8 = getActivity();
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("Đã tải xong kết quả ngày: ");
                    sb8.append(MainActivity.Get_ngay());
                    Toast.makeText(activity8, sb8.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                }
            } else if ("".length() > 185) {
                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + "");
                FragmentActivity activity9 = getActivity();
                StringBuilder sb9 = new StringBuilder();
                sb9.append("Đã tải xong kết quả ngày: ");
                sb9.append(MainActivity.Get_ngay());
                Toast.makeText(activity9, sb9.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
            if ("".length() > 185) {
                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + "");
                activity = getActivity();
                sb = new StringBuilder();
            }
        } catch (Throwable th) {
            if ("".length() > 185) {
                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + "");
                FragmentActivity activity10 = getActivity();
                StringBuilder sb10 = new StringBuilder();
                sb10.append("Đã tải xong kết quả ngày: ");
                sb10.append(MainActivity.Get_ngay());
                Toast.makeText(activity10, sb10.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
            }
            throw th;
        }
    }

    public void PhantichXosomeNewNew() {
        FragmentActivity activity;
        StringBuilder sb;
        new MainActivity();
        String str_date = MainActivity.Get_date();
        try {
            if (Congthuc.isNumeric(this.ArrayGiai[3])) {
                String Str = "'" + this.ArrayGiai[3].trim() + "',";
                if (Congthuc.isNumeric(this.ArrayGiai[5])) {
                    String Str2 = Str + "'" + this.ArrayGiai[5].trim() + "',";
                    if (Congthuc.isNumeric(this.ArrayGiai[7])) {
                        String Str3 = (Str2 + "'" + this.ArrayGiai[7].trim().substring(0, 5) + "',") + "'" + this.ArrayGiai[7].trim().substring(5, this.ArrayGiai[7].trim().length()) + "',";
                        if (Congthuc.isNumeric(this.ArrayGiai[9])) {
                            String Str4 = (((((Str3 + "'" + this.ArrayGiai[9].trim().substring(0, 5) + "',") + "'" + this.ArrayGiai[9].trim().substring(5, 10) + "',") + "'" + this.ArrayGiai[9].trim().substring(10, 15) + "',") + "'" + this.ArrayGiai[9].trim().substring(15, 20) + "',") + "'" + this.ArrayGiai[9].trim().substring(20, 25) + "',") + "'" + this.ArrayGiai[9].trim().substring(25, this.ArrayGiai[9].trim().length()) + "',";
                            if (Congthuc.isNumeric(this.ArrayGiai[11])) {
                                String Str5 = (((Str4 + "'" + this.ArrayGiai[11].trim().substring(0, 4) + "',") + "'" + this.ArrayGiai[11].trim().substring(4, 8) + "',") + "'" + this.ArrayGiai[11].trim().substring(8, 12) + "',") + "'" + this.ArrayGiai[11].trim().substring(12, this.ArrayGiai[11].trim().length()) + "',";
                                if (Congthuc.isNumeric(this.ArrayGiai[13])) {
                                    String Str6 = (((((Str5 + "'" + this.ArrayGiai[13].trim().substring(0, 4) + "',") + "'" + this.ArrayGiai[13].trim().substring(4, 8) + "',") + "'" + this.ArrayGiai[13].trim().substring(8, 12) + "',") + "'" + this.ArrayGiai[13].trim().substring(12, 16) + "',") + "'" + this.ArrayGiai[13].trim().substring(16, 20) + "',") + "'" + this.ArrayGiai[13].trim().substring(20, this.ArrayGiai[13].trim().length()) + "',";
                                    if (Congthuc.isNumeric(this.ArrayGiai[15])) {
                                        String Str7 = ((Str6 + "'" + this.ArrayGiai[15].trim().substring(0, 3) + "',") + "'" + this.ArrayGiai[15].trim().substring(3, 6) + "',") + "'" + this.ArrayGiai[15].trim().substring(6, this.ArrayGiai[15].trim().length()) + "',";
                                        if (Congthuc.isNumeric(this.ArrayGiai[17])) {
                                            String Str8 = (((Str7 + "'" + this.ArrayGiai[17].trim().substring(0, 2) + "',") + "'" + this.ArrayGiai[17].trim().substring(2, 4) + "',") + "'" + this.ArrayGiai[17].trim().substring(4, 6) + "',") + "'" + this.ArrayGiai[17].trim().substring(6, this.ArrayGiai[17].trim().length()) + "')";
                                            if (Str8.length() > 185) {
                                                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str8);
                                                activity = getActivity();
                                                sb = new StringBuilder();
                                                sb.append("Đã tải xong kết quả ngày: ");
                                                sb.append(MainActivity.Get_ngay());
                                                Toast.makeText(activity, sb.toString(), Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                                        } else if (Str7.length() > 185) {
                                            this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                            this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str7);
                                            FragmentActivity activity2 = getActivity();
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("Đã tải xong kết quả ngày: ");
                                            sb2.append(MainActivity.Get_ngay());
                                            Toast.makeText(activity2, sb2.toString(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                                        }
                                    } else if (Str6.length() > 185) {
                                        this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                        this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str6);
                                        FragmentActivity activity3 = getActivity();
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("Đã tải xong kết quả ngày: ");
                                        sb3.append(MainActivity.Get_ngay());
                                        Toast.makeText(activity3, sb3.toString(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                                    }
                                } else if (Str5.length() > 185) {
                                    this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                    this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str5);
                                    FragmentActivity activity4 = getActivity();
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Đã tải xong kết quả ngày: ");
                                    sb4.append(MainActivity.Get_ngay());
                                    Toast.makeText(activity4, sb4.toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                                }
                            } else if (Str4.length() > 185) {
                                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str4);
                                FragmentActivity activity5 = getActivity();
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("Đã tải xong kết quả ngày: ");
                                sb5.append(MainActivity.Get_ngay());
                                Toast.makeText(activity5, sb5.toString(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                            }
                        } else if (Str3.length() > 185) {
                            this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                            this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str3);
                            FragmentActivity activity6 = getActivity();
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("Đã tải xong kết quả ngày: ");
                            sb6.append(MainActivity.Get_ngay());
                            Toast.makeText(activity6, sb6.toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                        }
                    } else if (Str2.length() > 185) {
                        this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                        this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str2);
                        FragmentActivity activity7 = getActivity();
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("Đã tải xong kết quả ngày: ");
                        sb7.append(MainActivity.Get_ngay());
                        Toast.makeText(activity7, sb7.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                    }
                } else if (Str.length() > 185) {
                    this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                    this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + Str);
                    FragmentActivity activity8 = getActivity();
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("Đã tải xong kết quả ngày: ");
                    sb8.append(MainActivity.Get_ngay());
                    Toast.makeText(activity8, sb8.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
                }
            } else if ("".length() > 185) {
                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + "");
                FragmentActivity activity9 = getActivity();
                StringBuilder sb9 = new StringBuilder();
                sb9.append("Đã tải xong kết quả ngày: ");
                sb9.append(MainActivity.Get_ngay());
                Toast.makeText(activity9, sb9.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
            if ("".length() > 185) {
                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + "");
                activity = getActivity();
                sb = new StringBuilder();
            }
        } catch (Throwable th) {
            if ("".length() > 185) {
                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                this.db.QueryData("InSert Into KETQUA VALUES(null,'" + str_date + "'," + "");
                FragmentActivity activity10 = getActivity();
                StringBuilder sb10 = new StringBuilder();
                sb10.append("Đã tải xong kết quả ngày: ");
                sb10.append(MainActivity.Get_ngay());
                Toast.makeText(activity10, sb10.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Không có kết quả phù hợp!", Toast.LENGTH_LONG).show();
            }
            throw th;
        }
    }

    public void PhantichMinhngoc() {
        new MainActivity();
        String str_date = MainActivity.Get_date();
        boolean Ktra = true;
        try {
            String Str_sql = "InSert Into KETQUA VALUES(null,'" + str_date + "',";
            for (int i = 0; i < this.ArrayGiai.length; i++) {
                String[] CacGiai = this.ArrayGiai[i].split(" ");
                for (int ii = 0; ii < CacGiai.length; ii++) {
                    if (Congthuc.isNumeric(CacGiai[ii]) && CacGiai[ii].length() > 1) {
                        Str_sql = Str_sql + "'" + CacGiai[ii] + "',";
                    } else if (CacGiai[ii].length() < 1) {
                        Ktra = false;
                    }
                }
            }
            if (Ktra) {
                this.db.QueryData("Delete From ketqua WHERE ngay = '" + str_date + "'");
                StringBuilder sb = new StringBuilder();
                sb.append(Str_sql.substring(0, Str_sql.length() - 1));
                sb.append(")");
                this.db.QueryData(sb.toString());
                Toast.makeText(getActivity(), "Đã tải xong kết quả ngày: " + MainActivity.Get_ngay(), Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(getActivity(), "Chưa có kết quả!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }
}