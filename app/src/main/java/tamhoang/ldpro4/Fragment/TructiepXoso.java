package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationBindObject;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class TructiepXoso extends Fragment {
    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";
    String DangXuat = "lo";
    int So_giai = 0;
    Switch Switch1;
    Database db;
    Handler handler;
    List<JSONObject> jsonValues;
    ArrayList<String> listSo = new ArrayList<>();
    ListView listView;
    String mDate = "";
    WebView mWebView;
    RadioButton rdb_XemLo;
    RadioButton rdb_XemXien;
    private Runnable runnable = new Runnable() {
        /* class tamhoang.ldpro4.Fragment.TructiepXoso.AnonymousClass6 */

        public void run() {
            if (TructiepXoso.this.listSo.size() > 26) {
                TructiepXoso.this.handler.removeCallbacks(TructiepXoso.this.runnable);
                return;
            }
            TructiepXoso tructiepXoso = TructiepXoso.this;
            tructiepXoso.loadJavascript("(function() { return " + "document.getElementsByClassName('firstlast-mb fl')[0].innerText;" + "; })();");
            TructiepXoso.this.handler.postDelayed(this, 2000);
        }
    };
    View v;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.v = inflater.inflate(R.layout.tructiepxoso, container, false);
        this.db = new Database(getActivity());
        init();
        Calendar.getInstance().setTime(new Date());
        new SimpleDateFormat("yyyy-MM-dd").setTimeZone(TimeZone.getDefault());
        this.Switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.TructiepXoso.AnonymousClass1 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (TructiepXoso.this.Switch1.isChecked()) {
                    TructiepXoso.this.mWebView.setVisibility(View.VISIBLE);
                } else {
                    TructiepXoso.this.mWebView.setVisibility(View.GONE);
                }
            }
        });
        this.rdb_XemLo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.TructiepXoso.AnonymousClass2 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (TructiepXoso.this.rdb_XemLo.isChecked()) {
                    TructiepXoso.this.DangXuat = "lo";
                    TructiepXoso.this.Xem_lv();
                }
            }
        });
        this.rdb_XemXien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.TructiepXoso.AnonymousClass3 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (TructiepXoso.this.rdb_XemXien.isChecked()) {
                    TructiepXoso.this.DangXuat = "xi";
                    TructiepXoso.this.Xem_lv();
                }
            }
        });
        this.handler = new Handler();
        if (!Congthuc.CheckTime("18:14") || Congthuc.CheckTime("24:30")) {
            this.mWebView.setVisibility(View.GONE);
        } else {
            this.Switch1.setText("Ẩn/hiện bảng Kết quả");
            this.handler.postDelayed(this.runnable, 3000);
            this.mWebView.setVisibility(View.GONE);
        }
        this.mWebView.addJavascriptInterface(new NotificationBindObject(getActivity().getApplicationContext()), "NotificationBind");
        setUpWebViewDefaults(this.mWebView);
        if (savedInstanceState != null) {
            this.mWebView.restoreState(savedInstanceState);
        }
        if (this.mWebView.getUrl() == null) {
            this.mWebView.loadUrl("https://xoso.me/embedded/kq-mienbac");
        }
        this.mWebView.setWebViewClient(new WebViewClient() {
            /* class tamhoang.ldpro4.Fragment.TructiepXoso.AnonymousClass4 */

            public void onPageFinished(WebView view, String url) {
                TructiepXoso.this.loadJavascript("document.getElementsByClassName('embeded-breadcrumb')[0].style.display = 'none';\ndocument.getElementsByClassName('tit-mien')[0].style.display = 'none';");
                TructiepXoso.this.mWebView.setVisibility(View.VISIBLE);
                TructiepXoso.this.Switch1.setEnabled(true);
            }
        });
        this.mWebView.setEnabled(false);
        Xem_lv();
        return this.v;
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

    public void loadJavascript(String javascript) {
        this.mWebView.evaluateJavascript(javascript, new ValueCallback<String>() {
            /* class tamhoang.ldpro4.Fragment.TructiepXoso.AnonymousClass5 */

            public void onReceiveValue(String s) {
                String msg;
                JsonReader reader = new JsonReader(new StringReader(s));
                reader.setLenient(true);
                try {
                    if (reader.peek() != JsonToken.NULL && reader.peek() == JsonToken.STRING && (msg = reader.nextString()) != null && msg.indexOf("\n") > -1) {
                        String[] SSS = msg.substring(msg.indexOf("0")).split("\n");
                        for (int i = 0; i < SSS.length; i++) {
                            if (SSS[i].length() > 2) {
                                SSS[i] = SSS[i].substring(2);
                            } else {
                                SSS[i] = "";
                            }
                        }
                        if (SSS.length == 10) {
                            TructiepXoso.this.listSo = new ArrayList<>();
                            for (int i2 = 0; i2 < SSS.length; i2++) {
                                String[] Sodit = SSS[i2].replaceAll(" ", "").split(",");
                                for (int j = 0; j < Sodit.length; j++) {
                                    if (Sodit[j].length() == 1) {
                                        ArrayList<String> arrayList = TructiepXoso.this.listSo;
                                        arrayList.add(i2 + Sodit[j]);
                                    } else if (Sodit[j].length() == 2) {
                                        ArrayList<String> arrayList2 = TructiepXoso.this.listSo;
                                        arrayList2.add(i2 + Sodit[j].substring(1));
                                    }
                                }
                            }
                            if (TructiepXoso.this.listSo.size() != TructiepXoso.this.So_giai) {
                                TructiepXoso.this.TinhTienTuDong(TructiepXoso.this.listSo);
                                TructiepXoso.this.Xem_lv();
                                TructiepXoso.this.So_giai = TructiepXoso.this.listSo.size();
                            }
                        } else {
                            Toast.makeText(TructiepXoso.this.getActivity(), "Trang xoso.me đang bị lỗi!", Toast.LENGTH_LONG).show();
                            TructiepXoso.this.handler.removeCallbacks(TructiepXoso.this.runnable);
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

    @Override // android.support.v4.app.Fragment
    public void onStop() {
        super.onStop();
        this.mWebView.clearCache(true);
        this.handler.removeCallbacks(this.runnable);
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (Congthuc.CheckTime("18:14") && !Congthuc.CheckTime("18:30") && isNetworkConnected()) {
            this.Switch1.setText("Ẩn/hiện bảng Kết quả");
            this.handler.postDelayed(this.runnable, 3000);
        }
    }

    private boolean isNetworkConnected() {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void TinhTienTuDong(ArrayList<String> ArraySo) {
        Database database = this.db;
        database.QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = 0 WHERE ngay_nhan = '" + this.mDate + "' AND the_loai <> 'tt' AND the_loai <> 'cn'");
        String Ketqua = "";
        for (int i = 0; i < ArraySo.size(); i++) {
            Database database2 = this.db;
            database2.QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 Where the_loai = 'lo' and ngay_nhan = '" + this.mDate + "' And so_chon ='" + ArraySo.get(i) + "'");
            StringBuilder sb = new StringBuilder();
            sb.append(Ketqua);
            sb.append(ArraySo.get(i));
            sb.append(",");
            Ketqua = sb.toString();
        }
        Database database3 = this.db;
        Cursor cursor = database3.GetData("Select * From tbl_soctS Where ngay_nhan = '" + this.mDate + "' AND the_loai = 'xi'");
        while (cursor.moveToNext()) {
            String[] str2 = cursor.getString(7).split(",");
            boolean check = true;
            int j = 0;
            while (true) {
                if (j >= str2.length) {
                    break;
                } else if (Ketqua.indexOf(str2[j]) == -1) {
                    check = false;
                    break;
                } else {
                    j++;
                }
            }
            if (check) {
                Database database4 = this.db;
                database4.QueryData("Update tbl_soctS Set so_nhay = 1 WHERE ID = " + cursor.getString(0));
            }
        }
        Database database5 = this.db;
        database5.QueryData("Update tbl_soctS set ket_qua = diem * lan_an * so_nhay - tong_tien WHERE ngay_nhan = '" + this.mDate + "' AND type_kh = 1 AND the_loai <> 'tt' AND the_loai <> 'cn'");
        Database database6 = this.db;
        database6.QueryData("Update tbl_soctS set ket_qua = -diem * lan_an * so_nhay + tong_tien WHERE ngay_nhan = '" + this.mDate + "' AND type_kh = 2 AND the_loai <> 'tt' AND the_loai <> 'cn'");
    }

    /* JADX INFO: Multiple debug info for r18v7 'str'  java.lang.String: [D('m_SoXien' java.lang.String[]), D('str' java.lang.String)] */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x022e  */
    /* JADX WARNING: Removed duplicated region for block: B:112:? A[RETURN, SYNTHETIC] */
    public void Xem_lv() {
        String str;
        JSONException e;
        MainActivity activity = null;
        String str2 = null;
        this.jsonValues = new ArrayList();
        MainActivity activity2 = new MainActivity();
        this.mDate = MainActivity.Get_date();
        if (this.DangXuat == "lo") {
            str = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, 0, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + this.mDate + "' AND tbl_soctS.the_loai='lo' \n GROUP by so_om.So Order by ton DESC, diem DESC";
        } else {
            this.db.GetData("Select * From So_om WHERE ID = 1").moveToFirst();
            str = "SELECT so_chon, sum((type_kh =1)*(100-diem_khachgiu)*diem)/100 AS diem, 0, SUm((type_kh =2)*diem) as chuyen , SUm((type_kh =1)*(100-diem_khachgiu-diem_dly_giu)*diem/100)-SUm((type_kh =2)*diem) AS ton, so_nhay   From tbl_soctS Where ngay_nhan='" + this.mDate + "' AND the_loai='xi' GROUP by so_chon Order by ton DESC, diem DESC";
        }
        Cursor cursor = this.db.GetData(str);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        if (cursor != null) {
            while (true) {
                try {
                    if (!cursor.moveToNext()) {
                        break;
                    }
                    JSONObject jSonSo = new JSONObject();
                    if (this.DangXuat == "lo") {
                        try {
                            jSonSo.put("so_chon", cursor.getString(0));
                            jSonSo.put("xep_diem", cursor.getInt(5));
                            str2 = str;
                            activity = activity2;
                        } catch (JSONException e2) {
                            e = e2;
                            e.printStackTrace();
                            cursor.close();
                            if (getActivity() != null) {
                            }
                        }
                    } else if (this.listSo.size() > 0) {
                        int dem_xien = 0;
                        String[] m_SoXien = cursor.getString(0).split(",");
                        String mGhepXien = "";
                        int i = 0;
                        while (true) {
                            str2 = str;
                            if (i >= m_SoXien.length) {
                                break;
                            }
                            if (this.listSo.indexOf(m_SoXien[i]) > -1) {
                                dem_xien++;
                                mGhepXien = mGhepXien + "<font color='#FF0000'>" + m_SoXien[i] + "</font>,";
                            } else {
                                mGhepXien = mGhepXien + m_SoXien[i] + ",";
                            }
                            i++;
                            str = str2;
                            activity2 = activity2;
                            m_SoXien = m_SoXien;
                        }
                        activity = activity2;
                        if (dem_xien == 4) {
                            jSonSo.put("xep_diem", 1000);
                        } else if (dem_xien == 3 && m_SoXien.length == 3) {
                            jSonSo.put("xep_diem", 900);
                        } else if (dem_xien == 2 && m_SoXien.length == 2) {
                            jSonSo.put("xep_diem", 800);
                        } else if (dem_xien == 3 && m_SoXien.length == 4) {
                            jSonSo.put("xep_diem", 100);
                        } else if (dem_xien == 2 && m_SoXien.length == 4) {
                            jSonSo.put("xep_diem", 70);
                        } else if (dem_xien == 1 && m_SoXien.length == 4) {
                            jSonSo.put("xep_diem", 50);
                        } else if (dem_xien == 2 && m_SoXien.length == 3) {
                            jSonSo.put("xep_diem", 90);
                        } else if (dem_xien == 1 && m_SoXien.length == 3) {
                            jSonSo.put("xep_diem", 60);
                        } else if (dem_xien == 1 && m_SoXien.length == 2) {
                            jSonSo.put("xep_diem", 80);
                        } else {
                            jSonSo.put("xep_diem", 0);
                        }
                        jSonSo.put("so_chon", mGhepXien);
                    } else {
                        str2 = str;
                        activity = activity2;
                        jSonSo.put("so_chon", cursor.getString(0));
                        jSonSo.put("xep_diem", 0);
                    }
                    jSonSo.put("tien_nhan", decimalFormat.format((long) cursor.getInt(1)));
                    jSonSo.put("tien_om", decimalFormat.format((long) cursor.getInt(2)));
                    jSonSo.put("tien_chuyen", decimalFormat.format((long) cursor.getInt(3)));
                    jSonSo.put("tien_ton", decimalFormat.format((long) cursor.getInt(4)));
                    jSonSo.put("so_nhay", cursor.getInt(5));
                    if (!this.DangXuat.equals("lo")) {
                        if (!this.DangXuat.equals("xi") || cursor.getInt(4) <= 0) {
                            break;
                        }
                    }
                    this.jsonValues.add(jSonSo);
                    str = str2;
                    activity2 = activity;
                } catch (JSONException e5) {
                    e = e5;
                    e.printStackTrace();
                    cursor.close();
                    if (getActivity() != null) {
                    }
                }
            }
            Collections.sort(this.jsonValues, new Comparator<JSONObject>() {
                /* class tamhoang.ldpro4.Fragment.TructiepXoso.AnonymousClass7 */

                public int compare(JSONObject a, JSONObject b) {
                    int valA = 0;
                    Integer valB = 0;
                    try {
                        valA = Integer.valueOf(a.getInt("xep_diem"));
                        valB = Integer.valueOf(b.getInt("xep_diem"));
                    } catch (JSONException e) {
                    }
                    return valB.compareTo(valA);
                }
            });
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        if (getActivity() != null) {
            this.listView.setAdapter((ListAdapter) new So_OmAdapter(getActivity(), R.layout.frag_canchuyen_lv, this.jsonValues));
        }
    }

    /* access modifiers changed from: package-private */
    public class So_OmAdapter extends ArrayAdapter {
        public So_OmAdapter(Context context, int resource, List<JSONObject> objects) {
            super(context, resource, objects);
        }

        class ViewHolder {
            TextView tview1;
            TextView tview2;
            TextView tview4;
            TextView tview5;
            TextView tview7;
            TextView tview8;

            ViewHolder() {
            }
        }

        @SuppressLint("WrongConstant")
        public View getView(int position, View mView, ViewGroup parent) {
            ViewHolder holder;
            if (mView == null) {
                mView = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.frag_canchuyen_lv, (ViewGroup) null);
                holder = new ViewHolder();
                holder.tview5 = (TextView) mView.findViewById(R.id.Tv_so);
                holder.tview7 = (TextView) mView.findViewById(R.id.tv_diemNhan);
                holder.tview8 = (TextView) mView.findViewById(R.id.tv_diemOm);
                holder.tview1 = (TextView) mView.findViewById(R.id.tv_diemChuyen);
                holder.tview4 = (TextView) mView.findViewById(R.id.tv_diemTon);
                holder.tview2 = (TextView) mView.findViewById(R.id.stt);
                mView.setTag(holder);
            } else {
                holder = (ViewHolder) mView.getTag();
            }
            JSONObject Json = TructiepXoso.this.jsonValues.get(position);
            try {
                if (Json.getInt("so_nhay") > 0) {
                    holder.tview5.setTextColor(SupportMenu.CATEGORY_MASK);
                    holder.tview7.setTextColor(SupportMenu.CATEGORY_MASK);
                    holder.tview8.setTextColor(SupportMenu.CATEGORY_MASK);
                    holder.tview1.setTextColor(SupportMenu.CATEGORY_MASK);
                    holder.tview4.setTextColor(SupportMenu.CATEGORY_MASK);
                    if (Json.getInt("so_nhay") == 1) {
                        TextView textView = holder.tview5;
                        textView.setText(((Object) Html.fromHtml(Json.getString("so_chon"))) + "*");
                    } else if (Json.getInt("so_nhay") == 2) {
                        TextView textView2 = holder.tview5;
                        textView2.setText(((Object) Html.fromHtml(Json.getString("so_chon"))) + "**");
                    } else if (Json.getInt("so_nhay") == 3) {
                        TextView textView3 = holder.tview5;
                        textView3.setText(((Object) Html.fromHtml(Json.getString("so_chon"))) + "***");
                    } else if (Json.getInt("so_nhay") == 4) {
                        TextView textView4 = holder.tview5;
                        textView4.setText(((Object) Html.fromHtml(Json.getString("so_chon"))) + "****");
                    } else if (Json.getInt("so_nhay") == 5) {
                        TextView textView5 = holder.tview5;
                        textView5.setText(((Object) Html.fromHtml(Json.getString("so_chon"))) + "*****");
                    } else if (Json.getInt("so_nhay") == 6) {
                        TextView textView6 = holder.tview5;
                        textView6.setText(((Object) Html.fromHtml(Json.getString("so_chon"))) + "******");
                    }
                    holder.tview7.setText(Json.getString("tien_nhan"));
                    holder.tview8.setText(Json.getString("tien_om"));
                    holder.tview1.setText(Json.getString("tien_chuyen"));
                    holder.tview4.setText(Json.getString("tien_ton"));
                    TextView textView7 = holder.tview2;
                    textView7.setText((position + 1) + "");
                } else {
                    holder.tview5.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.tview7.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.tview8.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.tview1.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.tview4.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.tview5.setText(Html.fromHtml(Json.getString("so_chon")));
                    holder.tview7.setText(Json.getString("tien_nhan"));
                    holder.tview8.setText(Json.getString("tien_om"));
                    holder.tview1.setText(Json.getString("tien_chuyen"));
                    holder.tview4.setText(Json.getString("tien_ton"));
                    TextView textView8 = holder.tview2;
                    textView8.setText((position + 1) + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mView;
        }
    }

    public void init() {
        this.Switch1 = (Switch) this.v.findViewById(R.id.Switch1);
        this.rdb_XemLo = (RadioButton) this.v.findViewById(R.id.rdb_XemLo);
        this.rdb_XemXien = (RadioButton) this.v.findViewById(R.id.rdb_XemXien);
        this.listView = (ListView) this.v.findViewById(R.id.ListviewTructiep);
        this.mWebView = (WebView) this.v.findViewById(R.id.fragment_main_webview);
    }
}