package tamhoang.ldpro4.Fragment;

import static android.content.ContentValues.TAG;

import tamhoang.ldpro4.constants.Constants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
    String Url = Constants.URL_XOSOME_KQMB;
    Database db;
    Handler handler;
    List<JSONObject> jsonValues;
    ArrayList<String> listSo = new ArrayList<>();
    ListView listView;
    String mDate = "";
    WebView mWebView;
    RadioButton rdb_XemLo;
    RadioButton rdb_XemXien;
    RadioButton rdb_XsoMe;
    RadioButton rdb_ThienPhu;

    private final Runnable runnable = new Runnable() {

        public void run() {
            if (listSo.size() > 26) {
                handler.removeCallbacks(runnable);
                return;
            }
            if (rdb_ThienPhu.isChecked()) {
                loadJavascript("(function() { return document.getElementsByClassName('table table-lotto-xsmb')[0].innerText;; })();");
            } else {
                loadJavascript("(function() { return document.getElementsByClassName('firstlast-mb fl')[0].innerText;; })();");
            }
            handler.postDelayed(this, 2000);
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
        this.Switch1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (Switch1.isChecked()) {
                mWebView.setVisibility(View.VISIBLE);
            } else {
                mWebView.setVisibility(View.GONE);
            }
        });
        this.rdb_XemLo.setOnCheckedChangeListener((compoundButton, b) -> {
            if (rdb_XemLo.isChecked()) {
                DangXuat = "lo";
                Xem_lv();
            }
        });

        this.rdb_XemXien.setOnCheckedChangeListener((compoundButton, b) -> {
            if (rdb_XemXien.isChecked()) {
                DangXuat = "xi";
                Xem_lv();
            }
        });
        this.rdb_XsoMe.setOnCheckedChangeListener((compoundButton, z) -> {
            if (rdb_XsoMe.isChecked()) {
                Url = Constants.URL_XOSOME_KQMB;
                mWebView.loadUrl(Url);
            }
        });
        this.rdb_ThienPhu.setOnCheckedChangeListener((compoundButton, z) -> {
            if (rdb_ThienPhu.isChecked()) {
                String mDate = MainActivity.Get_ngay().replaceAll("/", "-");
                Url = "https://xosothienphu.com/ma-nhung/xsmb-" + mDate + ".html";
                mWebView.loadUrl(Url);
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
            this.mWebView.loadUrl(Constants.URL_XOSOME_KQMB);
        }
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                loadJavascript("document.getElementsByClassName('embeded-breadcrumb')[0].style.display = 'none';\ndocument.getElementsByClassName('tit-mien')[0].style.display = 'none';");
                mWebView.setVisibility(View.VISIBLE);
                Switch1.setEnabled(true);
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
        settings.setDisplayZoomControls(false);
        WebView.setWebContentsDebuggingEnabled(true);
    }

    public void loadJavascript(String javascript) {
        this.mWebView.evaluateJavascript(javascript, s -> {
            String msg;
            JsonReader reader = new JsonReader(new StringReader(s));
            Log.e(TAG, "loadJavascript: " + s);
            reader.setLenient(true);
            try {
                if (reader.peek() != JsonToken.NULL && reader.peek() == JsonToken.STRING && (msg = reader.nextString()) != null && msg.contains("\n")) {
                    String[] SSS = msg.substring(msg.indexOf("0")).split("\n");

                    SSS = Arrays.stream(SSS)
                            .map(s1 -> s1.length() > 2 ? s1.substring(2) : "")
                            .toArray(String[]::new);

                    if (SSS.length == 10) {
                        listSo = new ArrayList<>();
                        for (int i2 = 0; i2 < SSS.length; i2++) {
                            String[] Sodit = SSS[i2].replaceAll(" ", "").split(",");
                            for (String value : Sodit) {
                                if (value.length() == 1) {
                                    listSo.add(i2 + value);
                                } else if (value.length() == 2) {
                                    listSo.add(i2 + value.substring(1));
                                }
                            }
                        }
                        if (listSo.size() != So_giai) {
                            TinhTienTuDong(listSo);
                            Xem_lv();
                            So_giai = listSo.size();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Trang xoso.me đang bị lỗi!", Toast.LENGTH_LONG).show();
                        handler.removeCallbacks(runnable);
                    }
                }
                reader.close();
            } catch (IOException e2) {
                Log.e(TAG, "MainActivity: IOException", e2);
            } catch (Throwable ignored) {
            }
        });
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
        db.QueryData("Update tbl_soctS Set so_nhay = 0, ket_qua = 0 WHERE ngay_nhan = '" + mDate + "' AND the_loai <> 'tt' AND the_loai <> 'cn'");
        String Ketqua = "";
        for (String so: ArraySo) {
            db.QueryData("Update tbl_soctS Set so_nhay = so_nhay + 1 Where the_loai = 'lo' and ngay_nhan = '" + mDate + "' And so_chon ='" + so + "'");
            Ketqua = Ketqua + so + ",";
        }
        Cursor cursor = db.GetData("Select * From tbl_soctS Where ngay_nhan = '" + mDate + "' AND the_loai = 'xi'");
        while (cursor.moveToNext()) {
            String[] so_chon_arr = cursor.getString(7).split(",");
            boolean check = Arrays.stream(so_chon_arr).allMatch(Ketqua::contains);

            if (check) {
                db.QueryData("Update tbl_soctS Set so_nhay = 1 WHERE ID = " + cursor.getString(0));
            }
        }
        db.QueryData("Update tbl_soctS set ket_qua = diem * lan_an * so_nhay - tong_tien WHERE ngay_nhan = '" + mDate + "' AND type_kh = 1 AND the_loai <> 'tt' AND the_loai <> 'cn'");
        db.QueryData("Update tbl_soctS set ket_qua = -diem * lan_an * so_nhay + tong_tien WHERE ngay_nhan = '" + mDate + "' AND type_kh = 2 AND the_loai <> 'tt' AND the_loai <> 'cn'");
    }

    public void Xem_lv() {
        String str;
        jsonValues = new ArrayList<>();
        mDate = MainActivity.Get_date();
        if (DangXuat == "lo") {
            str = "Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, 0, Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='" + mDate + "' AND tbl_soctS.the_loai='lo' \n GROUP by so_om.So Order by ton DESC, diem DESC";
        } else {
            db.GetData("Select * From So_om WHERE ID = 1").moveToFirst();
            str = "SELECT so_chon, sum((type_kh =1)*(100-diem_khachgiu)*diem)/100 AS diem, 0, SUm((type_kh =2)*diem) as chuyen , SUm((type_kh =1)*(100-diem_khachgiu-diem_dly_giu)*diem/100)-SUm((type_kh =2)*diem) AS ton, so_nhay   From tbl_soctS Where ngay_nhan='" + mDate + "' AND the_loai='xi' GROUP by so_chon Order by ton DESC, diem DESC";
        }
        Cursor cursor = db.GetData(str);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    JSONObject jSonSo = new JSONObject();
                    if (DangXuat == "lo") {
                        try {
                            jSonSo.put("so_chon", cursor.getString(0));
                            jSonSo.put("xep_diem", cursor.getInt(5));
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                            cursor.close();
                        }
                    } else if (listSo.size() > 0) {
                        int dem_xien = 0;
                        String[] m_SoXien = cursor.getString(0).split(",");
                        String mGhepXien = "";

                        for (String so_xien : m_SoXien) {
                            if (listSo.contains(so_xien)) {
                                dem_xien++;
                                mGhepXien = mGhepXien + "<font color='#FF0000'>" + so_xien + "</font>,";
                            } else {
                                mGhepXien = mGhepXien + so_xien + ",";
                            }
                        }

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
                        jSonSo.put("so_chon", cursor.getString(0));
                        jSonSo.put("xep_diem", 0);
                    }
                    jSonSo.put("tien_nhan", decimalFormat.format((long) cursor.getInt(1)));
                    jSonSo.put("tien_om", decimalFormat.format((long) cursor.getInt(2)));
                    jSonSo.put("tien_chuyen", decimalFormat.format((long) cursor.getInt(3)));
                    jSonSo.put("tien_ton", decimalFormat.format((long) cursor.getInt(4)));
                    jSonSo.put("so_nhay", cursor.getInt(5));
                    if (!DangXuat.equals("lo")) {
                        if (!DangXuat.equals("xi") || cursor.getInt(4) <= 0) {
                            break;
                        }
                    }
                    jsonValues.add(jSonSo);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                    cursor.close();
                }
            }
            Collections.sort(jsonValues, (a, b) -> {
                int valA = 0;
                Integer valB = 0;
                try {
                    valA = a.getInt("xep_diem");
                    valB = b.getInt("xep_diem");
                } catch (JSONException e1) {
                }
                return valB.compareTo(valA);
            });
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        if (getActivity() != null) {
            listView.setAdapter((ListAdapter) new So_OmAdapter(getActivity(), R.layout.frag_canchuyen_lv, jsonValues));
        }
    }

    public class So_OmAdapter extends ArrayAdapter {
        public So_OmAdapter(Context context, int resource, List<JSONObject> objects) {
            super(context, resource, objects);
        }

        class ViewHolder {
            TextView tv_diemChuyen;
            TextView stt;
            TextView tv_diemTon;
            TextView Tv_so;
            TextView tv_diemNhan;
            TextView tv_diemOm;

            ViewHolder() {
            }
        }

        @SuppressLint({"WrongConstant", "RestrictedApi", "SetTextI18n"})
        public View getView(int position, View mView, ViewGroup parent) {
            ViewHolder holder;
            if (mView == null) {
                mView = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.frag_canchuyen_lv, (ViewGroup) null);
                holder = new ViewHolder();
                holder.Tv_so = (TextView) mView.findViewById(R.id.Tv_so);
                holder.tv_diemNhan = (TextView) mView.findViewById(R.id.tv_diemNhan);
                holder.tv_diemOm = (TextView) mView.findViewById(R.id.tv_diemOm);
                holder.tv_diemChuyen = (TextView) mView.findViewById(R.id.tv_diemChuyen);
                holder.tv_diemTon = (TextView) mView.findViewById(R.id.tv_diemTon);
                holder.stt = (TextView) mView.findViewById(R.id.stt);
                mView.setTag(holder);
            } else {
                holder = (ViewHolder) mView.getTag();
            }
            JSONObject Json = jsonValues.get(position);
            try {
                if (Json.getInt("so_nhay") > 0) {
                    holder.Tv_so.setTextColor(SupportMenu.CATEGORY_MASK);
                    holder.tv_diemNhan.setTextColor(SupportMenu.CATEGORY_MASK);
                    holder.tv_diemOm.setTextColor(SupportMenu.CATEGORY_MASK);
                    holder.tv_diemChuyen.setTextColor(SupportMenu.CATEGORY_MASK);
                    holder.tv_diemTon.setTextColor(SupportMenu.CATEGORY_MASK);

                    TextView textView = holder.Tv_so;
                    switch (Json.getInt("so_nhay")) {
                        case 1 : textView.setText(Html.fromHtml(Json.getString("so_chon")) + "*");
                        break;
                        case 2 : textView.setText(Html.fromHtml(Json.getString("so_chon")) + "**");
                        break;
                        case 3 : textView.setText(Html.fromHtml(Json.getString("so_chon")) + "***");
                        break;
                        case 4 : textView.setText(Html.fromHtml(Json.getString("so_chon")) + "****");
                        break;
                        case 5 : textView.setText(Html.fromHtml(Json.getString("so_chon")) + "*****");
                        break;
                        case 6 : textView.setText(Html.fromHtml(Json.getString("so_chon")) + "******");
                        break;
                    }
                    holder.tv_diemNhan.setText(Json.getString("tien_nhan"));
                    holder.tv_diemOm.setText(Json.getString("tien_om"));
                    holder.tv_diemChuyen.setText(Json.getString("tien_chuyen"));
                    holder.tv_diemTon.setText(Json.getString("tien_ton"));
                    TextView textView7 = holder.stt;
                    textView7.setText((position + 1) + "");
                } else {
                    holder.Tv_so.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.tv_diemNhan.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.tv_diemOm.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.tv_diemChuyen.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.tv_diemTon.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    holder.Tv_so.setText(Html.fromHtml(Json.getString("so_chon")));
                    holder.tv_diemNhan.setText(Json.getString("tien_nhan"));
                    holder.tv_diemOm.setText(Json.getString("tien_om"));
                    holder.tv_diemChuyen.setText(Json.getString("tien_chuyen"));
                    holder.tv_diemTon.setText(Json.getString("tien_ton"));
                    TextView textView8 = holder.stt;
                    textView8.setText((position + 1) + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mView;
        }
    }

    public void init() {
        Switch1 = (Switch) v.findViewById(R.id.Switch1);
        rdb_XemLo = (RadioButton) v.findViewById(R.id.rdb_XemLo);
        rdb_XemXien = (RadioButton) v.findViewById(R.id.rdb_XemXien);
        listView = (ListView) v.findViewById(R.id.ListviewTructiep);
        mWebView = (WebView) v.findViewById(R.id.fragment_main_webview);
        rdb_XsoMe = (RadioButton) v.findViewById(R.id.rdb_XsoMe);
        rdb_ThienPhu = (RadioButton) v.findViewById(R.id.rdb_ThienPhu);
    }
}