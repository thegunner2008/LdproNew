package tamhoang.ldpro4.Fragment;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.HttpConnection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_Chaytrang extends Fragment {
    static long Curent_date_time = 0;
    String DangXuat = null;
    int Dem = 0;
    String Dieukien = "(the_loai = 'deb' or the_loai = 'det')";
    int GameType = 0;
    public List<Integer> HuyCuoc = new ArrayList();
    boolean LoLive = false;
    int MaxChay;
    public List<String> NoiDung = new ArrayList();
    int Price = 0;
    int PriceLive = 0;
    public List<String> SoTin = new ArrayList();
    public List<Integer> TheLoai = new ArrayList();
    public List<String> ThoiGian = new ArrayList();
    public List<String> TienCuoc = new ArrayList();
    String ToDay = "";
    Button btn_MaXuat;
    Button btn_Xuatso;
    Database db;
    String donvi = "n ";
    EditText edt_tien;
    Handler handler;
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonChayTrang = new JSONObject();
    JSONObject jsonGia = new JSONObject();
    JSONObject jsonTienxien = new JSONObject();
    String lay_xien = " length(so_chon) = 5 ";
    LinearLayout li_loaide;
    LinearLayout li_loaixi;
    ListView lview;
    public List<String> mGia = new ArrayList();
    public List<String> mMax = new ArrayList();
    public List<Integer> mNhay = new ArrayList();
    public List<String> mSo = new ArrayList();
    public List<String> mTienNhan = new ArrayList();
    public List<String> mTienOm = new ArrayList();
    public List<String> mTienTon = new ArrayList();
    public List<String> mTienchuyen = new ArrayList();
    public List<String> mpassword = new ArrayList();
    public List<String> mwebsite = new ArrayList();
    double myBalance = 0.0d;
    String myMax = "";
    RadioButton radio_de;
    RadioButton radio_dea;
    RadioButton radio_deb;
    RadioButton radio_dec;
    RadioButton radio_ded;
    RadioButton radio_lo;
    RadioButton radio_xi;
    RadioButton radio_xi2;
    RadioButton radio_xi3;
    RadioButton radio_xi4;
    private Runnable runnable = new Runnable() {
        /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass20 */

        public void run() {
            boolean Running = true;
            if (Frag_Chaytrang.Curent_date_time > 0) {
                DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date gioBatdau = Frag_Chaytrang.parseDate("01:00");
                Date gioLoxien = Frag_Chaytrang.parseDate("18:14");
                Date gioKetthuc = Frag_Chaytrang.parseDate("18:28");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Frag_Chaytrang.Curent_date_time * 1000);
                Date date = Frag_Chaytrang.parseDate(calendar.get(11) + ":" + calendar.get(12));
                if (date.after(gioLoxien) && date.before(gioKetthuc) && !Frag_Chaytrang.this.LoLive) {
                    Frag_Chaytrang.this.radio_xi.setEnabled(false);
                    Frag_Chaytrang.this.radio_lo.setText("Lô Live");
                    Frag_Chaytrang.this.LoLive = true;
                } else if (date.after(gioKetthuc)) {
                    Frag_Chaytrang.this.handler.removeCallbacks(Frag_Chaytrang.this.runnable);
                    Running = false;
                    Frag_Chaytrang.this.btn_Xuatso.setEnabled(false);
                    Frag_Chaytrang.this.btn_Xuatso.setText("Hết giờ");
                    Frag_Chaytrang.this.btn_Xuatso.setTextColor(-7829368);
                } else if (date.before(gioBatdau)) {
                    Frag_Chaytrang.this.btn_Xuatso.setEnabled(false);
                    Frag_Chaytrang.this.btn_Xuatso.setText("Chưa mở");
                    Frag_Chaytrang.this.btn_Xuatso.setTextColor(-7829368);
                    Running = false;
                }
                if (Frag_Chaytrang.this.LoLive && Frag_Chaytrang.this.radio_lo.isChecked()) {
                    Frag_Chaytrang.this.Dem++;
                    if (Frag_Chaytrang.this.Dem >= 4) {
                        Frag_Chaytrang.this.Dem = 0;
                        Frag_Chaytrang.this.Laygia();
                    }
                }
                if (Running) {
                    Frag_Chaytrang.this.btn_Xuatso.setText("Chạy trang (" + formatter.format(calendar.getTime()) + ")");
                }
            } else {
                Frag_Chaytrang.Curent_date_time = new Timestamp(System.currentTimeMillis()).getTime() / 1000;
            }
            if (Running) {
                Frag_Chaytrang.Curent_date_time++;
                Frag_Chaytrang.this.handler.postDelayed(this, 1000);
            }
        }
    };
    int spin_pointion = -1;
    Spinner spr_trang;
    String the_loai = "deb";
    View v;
    String xuatDan = "De:";

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.frag_chaytrang, container, false);
        init();
        this.db = new Database(getActivity());
        new MainActivity();
        this.ToDay = MainActivity.Get_date();
        this.radio_de.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass1 */

            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_Chaytrang.this.radio_de.isChecked()) {
                    Frag_Chaytrang.this.li_loaide.setVisibility(0);
                    try {
                        Database database = Frag_Chaytrang.this.db;
                        Cursor cursor = database.GetData("Select sum((the_loai = 'dea')* diem) as de_a\n,sum((the_loai = 'deb')* diem) as de_b\n,sum((the_loai = 'det')* diem) as de_t\n,sum((the_loai = 'dec')* diem) as de_c\n,sum((the_loai = 'ded')* diem) as de_d\nFrom tbl_soctS \nWhere ngay_nhan = '" + Frag_Chaytrang.this.ToDay + "'");
                        if (!cursor.moveToFirst() || cursor == null) {
                            Frag_Chaytrang.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                            Frag_Chaytrang.this.GameType = 0;
                            if (MainActivity.MyToken.length() > 0) {
                                Frag_Chaytrang.this.Laygia();
                                return;
                            }
                            return;
                        }
                        int[] dem = new int[5];
                        if (cursor.getDouble(0) > 0.0d) {
                            dem[0] = 1;
                            Frag_Chaytrang.this.radio_dea.setEnabled(true);
                        } else {
                            dem[0] = 0;
                            Frag_Chaytrang.this.radio_dea.setEnabled(false);
                        }
                        if (cursor.getDouble(1) > 0.0d) {
                            dem[1] = 1;
                            Frag_Chaytrang.this.radio_deb.setEnabled(true);
                        } else {
                            dem[1] = 0;
                            Frag_Chaytrang.this.radio_deb.setEnabled(false);
                        }
                        if (cursor.getDouble(2) > 0.0d) {
                            dem[2] = 1;
                        } else {
                            dem[2] = 0;
                        }
                        if (cursor.getDouble(3) > 0.0d) {
                            dem[3] = 1;
                            Frag_Chaytrang.this.radio_dec.setEnabled(true);
                        } else {
                            dem[3] = 0;
                            Frag_Chaytrang.this.radio_dec.setEnabled(false);
                        }
                        if (cursor.getDouble(4) > 0.0d) {
                            dem[4] = 1;
                            Frag_Chaytrang.this.radio_ded.setEnabled(true);
                        } else {
                            dem[4] = 0;
                            Frag_Chaytrang.this.radio_ded.setEnabled(false);
                        }
                        if (dem[0] == 0 && ((dem[1] == 1 || dem[2] == 1) && dem[3] == 0 && dem[4] == 0)) {
                            Frag_Chaytrang.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                            Frag_Chaytrang.this.li_loaixi.setVisibility(View.GONE);
                            Frag_Chaytrang.this.li_loaide.setVisibility(View.GONE);
                            Frag_Chaytrang.this.radio_deb.setChecked(true);
                            Frag_Chaytrang.this.xem_RecycView();
                        } else if (dem[0] == 0 && dem[1] == 0 && dem[2] == 0 && dem[3] == 0 && dem[4] == 0) {
                            Frag_Chaytrang.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                            Frag_Chaytrang.this.li_loaixi.setVisibility(View.GONE);
                            Frag_Chaytrang.this.li_loaide.setVisibility(View.GONE);
                            Frag_Chaytrang.this.radio_deb.setChecked(true);
                            Frag_Chaytrang.this.xem_RecycView();
                        } else {
                            Frag_Chaytrang.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                            Frag_Chaytrang.this.li_loaixi.setVisibility(View.GONE);
                            Frag_Chaytrang.this.li_loaide.setVisibility(View.GONE);
                            Frag_Chaytrang.this.radio_deb.setChecked(true);
                            Frag_Chaytrang.this.xem_RecycView();
                        }
                        if (!cursor.isClosed() && cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                        Frag_Chaytrang.this.GameType = 0;
                        if (MainActivity.MyToken.length() > 0) {
                            Frag_Chaytrang.this.Laygia();
                        }
                    } catch (SQLException e) {
                        Frag_Chaytrang.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                        Frag_Chaytrang.this.GameType = 0;
                        if (MainActivity.MyToken.length() > 0) {
                            Frag_Chaytrang.this.Laygia();
                        }
                    }
                }
            }
        });
        this.radio_dea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass2 */

            @SuppressLint("WrongConstant")
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_Chaytrang.this.radio_dea.isChecked()) {
                    Frag_Chaytrang.this.DangXuat = "the_loai = 'dea'";
                    Frag_Chaytrang.this.li_loaixi.setVisibility(View.GONE);
                    Frag_Chaytrang.this.GameType = 21;
                    Frag_Chaytrang.this.Laygia();
                }
            }
        });
        try {
            this.mwebsite.clear();
            this.mpassword.clear();
            Cursor cursor = this.db.GetData("Select * From tbl_chaytrang_acc");
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    this.mwebsite.add(cursor.getString(0));
                    this.mpassword.add(cursor.getString(1));
                }
                if (cursor != null) {
                    cursor.close();
                }
                this.spr_trang.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.mwebsite));
                if (this.mwebsite.size() > 0) {
                    this.spr_trang.setSelection(0);
                    this.spin_pointion = 0;
                }
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Đang copy dữ liệu bản mới!", Toast.LENGTH_SHORT).show();
        }
        this.radio_deb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass3 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_Chaytrang.this.radio_deb.isChecked()) {
                    Frag_Chaytrang.this.DangXuat = "(the_loai = 'deb' or the_loai = 'det')";
                    Frag_Chaytrang.this.li_loaixi.setVisibility(View.GONE);
                    Frag_Chaytrang.this.GameType = 0;
                    if (MainActivity.MyToken.length() > 0) {
                        Frag_Chaytrang.this.Laygia();
                    }
                }
            }
        });
        this.radio_dec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass4 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_Chaytrang.this.radio_dec.isChecked()) {
                    Frag_Chaytrang.this.DangXuat = "the_loai = 'dec'";
                    Frag_Chaytrang.this.li_loaixi.setVisibility(View.GONE);
                    Frag_Chaytrang.this.GameType = 23;
                    if (MainActivity.MyToken.length() > 0) {
                        Frag_Chaytrang.this.Laygia();
                    }
                }
            }
        });
        this.radio_ded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass5 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_Chaytrang.this.radio_ded.isChecked()) {
                    Frag_Chaytrang.this.DangXuat = "the_loai = 'ded'";
                    Frag_Chaytrang.this.li_loaixi.setVisibility(View.GONE);
                    Frag_Chaytrang.this.GameType = 22;
                    if (MainActivity.MyToken.length() > 0) {
                        Frag_Chaytrang.this.Laygia();
                    }
                }
            }
        });
        this.radio_lo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass6 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Frag_Chaytrang.this.radio_lo.isChecked()) {
                    Frag_Chaytrang.this.DangXuat = "the_loai = 'lo'";
                    Frag_Chaytrang.this.li_loaixi.setVisibility(View.GONE);
                    Frag_Chaytrang.this.li_loaide.setVisibility(View.GONE);
                    if (!Frag_Chaytrang.this.LoLive) {
                        Frag_Chaytrang.this.GameType = 1;
                    } else {
                        Frag_Chaytrang.this.GameType = 20;
                    }
                    if (MainActivity.MyToken.length() > 0) {
                        Frag_Chaytrang.this.Laygia();
                    }
                }
            }
        });
        this.radio_xi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass7 */

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Frag_Chaytrang.this.radio_xi.isChecked()) {
                    Frag_Chaytrang.this.DangXuat = "the_loai = 'xi'";
                    Frag_Chaytrang.this.li_loaixi.setVisibility(VISIBLE);
                    Frag_Chaytrang.this.li_loaide.setVisibility(View.GONE);
                    Frag_Chaytrang.this.radio_xi2.setChecked(true);
                    Frag_Chaytrang.this.GameType = 2;
                    if (MainActivity.MyToken.length() > 0) {
                        Frag_Chaytrang.this.Laygia();
                    }
                }
            }
        });
        this.radio_xi2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass8 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_Chaytrang.this.radio_xi2.isChecked()) {
                    Frag_Chaytrang.this.DangXuat = "the_loai = 'xi'";
                    Frag_Chaytrang.this.li_loaixi.setVisibility(VISIBLE);
                    Frag_Chaytrang.this.li_loaide.setVisibility(View.GONE);
                    Frag_Chaytrang.this.lay_xien = " length(so_chon) = 5 ";
                    Frag_Chaytrang.this.GameType = 2;
                    if (MainActivity.MyToken.length() > 0) {
                        Frag_Chaytrang.this.Laygia();
                    }
                }
            }
        });
        this.radio_xi3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass9 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_Chaytrang.this.radio_xi3.isChecked()) {
                    Frag_Chaytrang.this.DangXuat = "the_loai = 'xi'";
                    Frag_Chaytrang.this.li_loaixi.setVisibility(VISIBLE);
                    Frag_Chaytrang.this.li_loaide.setVisibility(View.GONE);
                    Frag_Chaytrang.this.lay_xien = " length(so_chon) = 8 ";
                    Frag_Chaytrang.this.GameType = 3;
                    if (MainActivity.MyToken.length() > 0) {
                        Frag_Chaytrang.this.Laygia();
                    }
                }
            }
        });
        this.radio_xi4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass10 */

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (Frag_Chaytrang.this.radio_xi4.isChecked()) {
                    Frag_Chaytrang.this.DangXuat = "the_loai = 'xi'";
                    Frag_Chaytrang.this.li_loaixi.setVisibility(VISIBLE);
                    Frag_Chaytrang.this.li_loaide.setVisibility(View.GONE);
                    Frag_Chaytrang.this.lay_xien = " length(so_chon) = 11 ";
                    Frag_Chaytrang.this.GameType = 4;
                    if (MainActivity.MyToken.length() > 0) {
                        Frag_Chaytrang.this.Laygia();
                    }
                }
            }
        });
        this.spr_trang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass11 */

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Frag_Chaytrang.this.spin_pointion = position;
                Frag_Chaytrang frag_Chaytrang = Frag_Chaytrang.this;
                frag_Chaytrang.login(frag_Chaytrang.mwebsite.get(position), Frag_Chaytrang.this.mpassword.get(position));
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (this.mwebsite.size() > 0) {
            login(this.mwebsite.get(this.spin_pointion), this.mpassword.get(this.spin_pointion));
        }
        this.btn_Xuatso.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass12 */

            public void onClick(View view) {
                if (Frag_Chaytrang.this.spin_pointion == -1) {
                    Toast.makeText(Frag_Chaytrang.this.getActivity(), "Không có trang để xuất", Toast.LENGTH_SHORT).show();
                }
                if (MainActivity.MyToken.length() > 0) {
                    int i = Frag_Chaytrang.this.GameType;
                    if (i != 0) {
                        if (i != 1) {
                            if (i == 2) {
                                Frag_Chaytrang.this.the_loai = "xi2";
                                Frag_Chaytrang.this.xuatDan = "Xi:";
                                Frag_Chaytrang.this.donvi = "n ";
                                Frag_Chaytrang.this.Dieukien = "the_loai = 'xi' AND length(so_chon) = 5";
                                Frag_Chaytrang.this.TaoTinXi();
                            } else if (i == 3) {
                                Frag_Chaytrang.this.the_loai = "xi3";
                                Frag_Chaytrang.this.xuatDan = "Xi:";
                                Frag_Chaytrang.this.donvi = "n ";
                                Frag_Chaytrang.this.Dieukien = "the_loai = 'xi' AND length(so_chon) = 8";
                                Frag_Chaytrang.this.TaoTinXi();
                            } else if (i != 4) {
                                switch (i) {
                                    case 21:
                                        Frag_Chaytrang.this.the_loai = "dea";
                                        Frag_Chaytrang.this.xuatDan = "De dau:";
                                        Frag_Chaytrang.this.donvi = "n ";
                                        Frag_Chaytrang.this.Dieukien = "the_loai = 'dea'";
                                        Frag_Chaytrang.this.TaoTinDe();
                                        break;
                                    case 22:
                                        Frag_Chaytrang.this.the_loai = "ded";
                                        Frag_Chaytrang.this.xuatDan = "De giai 1:";
                                        Frag_Chaytrang.this.donvi = "n ";
                                        Frag_Chaytrang.this.Dieukien = "the_loai = 'ded'";
                                        Frag_Chaytrang.this.TaoTinDe();
                                        break;
                                    case 23:
                                        Frag_Chaytrang.this.the_loai = "dec";
                                        Frag_Chaytrang.this.xuatDan = "De dau giai 1:";
                                        Frag_Chaytrang.this.donvi = "n ";
                                        Frag_Chaytrang.this.Dieukien = "the_loai = 'dec'";
                                        Frag_Chaytrang.this.TaoTinDe();
                                        break;
                                }
                            } else {
                                Frag_Chaytrang.this.the_loai = "xi4";
                                Frag_Chaytrang.this.xuatDan = "Xi:";
                                Frag_Chaytrang.this.donvi = "n ";
                                Frag_Chaytrang.this.Dieukien = "the_loai = 'xi' AND length(so_chon) = 11";
                                Frag_Chaytrang.this.TaoTinXi();
                            }
                        }
                        Frag_Chaytrang.this.the_loai = "lo";
                        Frag_Chaytrang.this.xuatDan = "Lo:";
                        Frag_Chaytrang.this.donvi = "d ";
                        Frag_Chaytrang.this.Dieukien = "the_loai = 'lo'";
                        Frag_Chaytrang.this.TaoTinDe();
                    } else {
                        Frag_Chaytrang.this.the_loai = "deb";
                        Frag_Chaytrang.this.xuatDan = "De:";
                        Frag_Chaytrang.this.donvi = "n ";
                        Frag_Chaytrang.this.Dieukien = "(the_loai = 'deb' or the_loai = 'det')";
                        Frag_Chaytrang.this.TaoTinDe();
                    }
                    Frag_Chaytrang.this.Dialog();
                }
            }
        });
        this.btn_MaXuat.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass13 */

            public void onClick(View view) {
                Frag_Chaytrang.this.Dialog2();
            }
        });
        Handler handler2 = new Handler();
        this.handler = handler2;
        handler2.postDelayed(this.runnable, 1000);
        xemlv();
        return this.v;
    }

    public String TaoTinDe() {
        int maxDang;
        int maxDang2;
        String str;
        String str2 = "Se_chuyen";
        int dem = 0;
        JSONObject jSon = new JSONObject();
        List<JSONObject> jsonValues = new ArrayList<>();
        try {
            maxDang = Integer.parseInt(this.myMax.replace(".", ""));
        } catch (Exception e) {
            maxDang = Integer.parseInt(this.myMax.replace(",", ""));
        }
        if (this.edt_tien.getText().toString().length() == 0) {
            maxDang2 = maxDang;
        } else if (Integer.parseInt(this.edt_tien.getText().toString()) > maxDang) {
            return "Số tiền vượt quá max ";
        } else {
            maxDang2 = Integer.parseInt(this.edt_tien.getText().toString());
        }
        int i = 0;
        while (true) {
            try {
                if (i >= this.mSo.size()) {
                    break;
                }
                String Ktra = this.mSo.get(i);
                if (dem >= 50) {
                    break;
                }
                if (this.jsonGia.has(Ktra)) {
                    if (this.jsonGia.getInt(Ktra) + this.Price > this.MaxChay) {
                        i++;
                    }
                } else if (this.Price > this.MaxChay) {
                    i++;
                }
                int TienTon = Integer.parseInt(this.mTienTon.get(i).replace(".", ""));
                JSONObject soCT = new JSONObject();
                soCT.put("So_chon", Ktra);
                soCT.put("Da_chuyen", jSon.has(Ktra) ? jSon.getJSONObject(Ktra).getInt("Da_chuyen") + TienTon : 0);
                soCT.put(str2, soCT.getInt("Da_chuyen") + TienTon <= maxDang2 ? TienTon : maxDang2 - soCT.getInt("Da_chuyen"));
                if (soCT.getInt(str2) > 0) {
                    jsonValues.add(soCT);
                    dem++;
                }
                i++;
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass14 */

            public int compare(JSONObject a, JSONObject b) {
                int valA = 0;
                Integer valB = 0;
                try {
                    valA = Integer.valueOf(a.getInt("Se_chuyen"));
                    valB = Integer.valueOf(b.getInt("Se_chuyen"));
                } catch (JSONException e) {
                }
                return valB.compareTo(valA);
            }
        });
        int tien = 0;
        int i2 = 0;
        int DemPhu = 0;
        while (i2 < jsonValues.size()) {
            try {
                JSONObject soCT2 = jsonValues.get(i2);
                int MaxTien = soCT2.getInt(str2);
                if (MaxTien > 0) {
                    if (tien > MaxTien) {
                        StringBuilder sb = new StringBuilder();
                        str = str2;
                        sb.append(this.xuatDan);
                        sb.append("x");
                        sb.append(tien);
                        sb.append(this.donvi);
                        this.xuatDan = sb.toString();
                        this.xuatDan += soCT2.getString("So_chon") + ",";
                        tien = MaxTien;
                        DemPhu = 0;
                    } else {
                        str = str2;
                        this.xuatDan += soCT2.getString("So_chon") + ",";
                        tien = MaxTien;
                    }
                    DemPhu++;
                } else {
                    str = str2;
                }
                i2++;
                str2 = str;
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
        }
        if (this.xuatDan.length() > 4 && DemPhu > 0) {
            this.xuatDan += "x" + tien + this.donvi;
        }
        if (DemPhu > 0) {
            return this.xuatDan;
        }
        return "";
    }

    public String TaoTinXi() {
        int dem = 0;
        JSONObject jSon = new JSONObject();
        List<JSONObject> jsonValues = new ArrayList<>();
        int maxDang = Integer.parseInt(this.myMax.replace(".", ""));
        try {
            if (this.edt_tien.getText().toString().trim().length() > 0 && Congthuc.isNumeric(this.edt_tien.getText().toString().trim())) {
                if (Integer.parseInt(this.edt_tien.getText().toString()) > maxDang) {
                    return "Số tiền vượt quá max ";
                }
                maxDang = Integer.parseInt(this.edt_tien.getText().toString());
            }
            for (int i = 0; i < this.mSo.size() && dem < 50; i++) {
                String Ktra = this.mSo.get(i);
                int TienTon = Integer.parseInt(this.mTienTon.get(i).replace(".", ""));
                if (TienTon > 0 && Integer.parseInt(this.mGia.get(i)) <= this.MaxChay) {
                    JSONObject soCT = new JSONObject();
                    soCT.put("So_chon", Ktra);
                    soCT.put("Da_chuyen", jSon.has(Ktra) ? jSon.getJSONObject(Ktra).getInt("Da_chuyen") + TienTon : 0);
                    soCT.put("Se_chuyen", soCT.getInt("Da_chuyen") + TienTon <= maxDang ? TienTon : maxDang - soCT.getInt("Da_chuyen"));
                    if (soCT.getInt("Se_chuyen") > 0) {
                        jsonValues.add(soCT);
                        dem++;
                    }
                }
            }
            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass15 */

                public int compare(JSONObject a, JSONObject b) {
                    int valA = 0;
                    Integer valB = 0;
                    try {
                        valA = Integer.valueOf(a.getInt("Se_chuyen"));
                        valB = Integer.valueOf(b.getInt("Se_chuyen"));
                    } catch (JSONException e) {
                    }
                    return valB.compareTo(valA);
                }
            });
            for (int i2 = 0; i2 < jsonValues.size(); i2++) {
                this.xuatDan += jsonValues.get(i2).getString("So_chon") + "x" + jsonValues.get(i2).getString("Se_chuyen") + this.donvi;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
        }
        if (this.xuatDan.length() > 5) {
            return this.xuatDan;
        }
        return "";
    }

    public void Dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.frag_chaytrang_diaglog);
        dialog.getWindow().setLayout(-1, -2);
        final EditText edt_XuatDan = (EditText) dialog.findViewById(R.id.edt_XuatDan);
        TextView taikhoan = (TextView) dialog.findViewById(R.id.taikhoan);
        TextView CreditLimit = (TextView) dialog.findViewById(R.id.CreditLimit);
        TextView Balance = (TextView) dialog.findViewById(R.id.Balance);
        final TextView edt_XuatErr = (TextView) dialog.findViewById(R.id.edt_XuatErr);
        edt_XuatErr.setVisibility(View.GONE);
        final Button btn_chuyendi = (Button) dialog.findViewById(R.id.btn_chuyendi);
        OkHttpClient okHttpClient = new OkHttpClient();
        if (MainActivity.MyToken.length() > 0 && Build.VERSION.SDK_INT >= 24) {
            CompletableFuture.runAsync(new Runnable() {
                public final OkHttpClient f$1;
                public final TextView f$2;
                public final TextView f$3;
                public final TextView f$4;

                {
                    this.f$1 = okHttpClient;
                    this.f$2 = taikhoan;
                    this.f$3 = CreditLimit;
                    this.f$4 = Balance;
                }

                public final void run() {
                    Frag_Chaytrang.this.lambda$Dialog$0$Frag_Chaytrang(this.f$1, this.f$2, this.f$3, this.f$4);
                }
            });
        }
        edt_XuatDan.setText("");
        edt_XuatDan.setText(this.xuatDan.replaceAll(",x", "x"));
        btn_chuyendi.setOnClickListener(new View.OnClickListener() {
            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass18 */

            public void onClick(View view) {
                String Kiermtra;
                btn_chuyendi.setEnabled(false);
                SQLiteDatabase database = Frag_Chaytrang.this.db.getWritableDatabase();
                DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(database, "tbl_soctS");
                String Kiermtra2 = null;
                try {
                    Kiermtra2 = Frag_Chaytrang.this.KiemTraTruocKhiChayTrang(edt_XuatDan.getText().toString().replaceAll("'", " ").trim());
                } catch (JSONException e) {
                    try {
                        e.printStackTrace();
                    } catch (Exception e2) {
                        edt_XuatErr.setText("Có lỗi khi xuất tin!");
                        edt_XuatErr.setVisibility(VISIBLE);
                        btn_chuyendi.setEnabled(true);
                        return;
                    }
                }
                if (Kiermtra2 == "") {
                    Kiermtra = Frag_Chaytrang.this.Laygia();
                } else {
                    Kiermtra = Kiermtra2;
                }
                if (Kiermtra == "") {
                    Frag_Chaytrang.this.jsonArray = new JSONArray();
                    String Postjson = null;
                    try {
                        Postjson = Frag_Chaytrang.this.CreateJson();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    OkHttpClient okHttpClient = new OkHttpClient();
                    AtomicReference<String> str3 = new AtomicReference<>("");
                    if (Build.VERSION.SDK_INT >= 24) {
                        String finalPostjson = Postjson;
                        CompletableFuture.runAsync(new Runnable() {
                            public final AtomicReference f$1;
                            public final OkHttpClient f$2;
                            public final String f$3;
                            public final SQLiteDatabase f$4;
                            public final DatabaseUtils.InsertHelper f$5;
                            public final EditText f$6;
                            public final Dialog f$7;
                            public final TextView f$8;
                            public final Button f$9;

                            {
                                this.f$1 = str3;
                                this.f$2 = okHttpClient;
                                this.f$3 = finalPostjson;
                                this.f$4 = database;
                                this.f$5 = ih;
                                this.f$6 = edt_XuatDan;
                                this.f$7 = dialog;
                                this.f$8 = edt_XuatErr;
                                this.f$9 = btn_chuyendi;
                            }

                            public final void run() {
                                lambda$onClick$0$Frag_Chaytrang$18(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
                            }
                        });
                        return;
                    }
                    return;
                }
                edt_XuatErr.setText(Kiermtra);
                edt_XuatErr.setVisibility(VISIBLE);
                btn_chuyendi.setEnabled(true);
            }

            public void lambda$onClick$0$Frag_Chaytrang$18(AtomicReference str3, OkHttpClient okHttpClient, String Postjson, final SQLiteDatabase database, final DatabaseUtils.InsertHelper ih, final EditText edt_XuatDan, final Dialog dialog, final TextView edt_XuatErr, final Button btn_chuyendi) {
                Exception e;
                try {
                    Request.Builder header = new Request.Builder().url("https://lotto.lotusapi.com/game-play/player/play").header(HttpConnection.CONTENT_TYPE, "application/json");
                    try {
                    } catch (Exception e2) {
                        e = e2;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass18.AnonymousClass4 */

                            public void run() {
                                edt_XuatErr.setText("Kết nối kém, hãy xuất lại.");
                                edt_XuatErr.setVisibility(VISIBLE);
                                btn_chuyendi.setEnabled(true);
                            }
                        });
                        e.printStackTrace();
                    }
                    try {
                        try {
                            str3.set(okHttpClient.newCall(header.header("Authorization", "Bearer " + MainActivity.MyToken).post(RequestBody.Companion.create(Postjson, MediaType.Companion.parse("application/json"))).build()).execute().body().string());
                            String Str = str3.toString();
                            if (Str.startsWith("[")) {
                                if (new JSONArray(Str).getJSONObject(0).has("Tx")) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass18.AnonymousClass1 */

                                        public void run() {
                                            String str;
                                            String str2 = "ngay_nhan";
                                            String str3 = " ";
                                            String str4 = "'";
                                            try {
                                                database.beginTransaction();
                                                JSONObject Json = null;
                                                int i = 0;
                                                while (i < Frag_Chaytrang.this.jsonArray.length()) {
                                                    Json = Frag_Chaytrang.this.jsonArray.getJSONObject(i);
                                                    ih.prepareForInsert();
                                                    ih.bind(ih.getColumnIndex("ID"), (byte[]) null);
                                                    ih.bind(ih.getColumnIndex(str2), Json.getString(str2));
                                                    ih.bind(ih.getColumnIndex("type_kh"), 2);
                                                    ih.bind(ih.getColumnIndex("ten_kh"), Frag_Chaytrang.this.mwebsite.get(Frag_Chaytrang.this.spin_pointion));
                                                    ih.bind(ih.getColumnIndex("so_dienthoai"), Frag_Chaytrang.this.mwebsite.get(Frag_Chaytrang.this.spin_pointion));
                                                    ih.bind(ih.getColumnIndex("so_tin_nhan"), Json.getInt("so_tin_nhan"));
                                                    DatabaseUtils.InsertHelper insertHelper = ih;
                                                    int columnIndex = ih.getColumnIndex("the_loai");
                                                    if (Frag_Chaytrang.this.the_loai.indexOf("xi") > -1) {
                                                        str = "xi";
                                                    } else {
                                                        str = Frag_Chaytrang.this.the_loai;
                                                    }
                                                    insertHelper.bind(columnIndex, str);
                                                    ih.bind(ih.getColumnIndex("so_chon"), Json.getString("so_chon"));
                                                    ih.bind(ih.getColumnIndex("diem"), Json.getInt("diem"));
                                                    ih.bind(ih.getColumnIndex("diem_quydoi"), Json.getInt("diem"));
                                                    ih.bind(ih.getColumnIndex("diem_khachgiu"), 0);
                                                    ih.bind(ih.getColumnIndex("diem_dly_giu"), 0);
                                                    ih.bind(ih.getColumnIndex("diem_ton"), Json.getInt("diem"));
                                                    ih.bind(ih.getColumnIndex("gia"), Json.getInt("gia"));
                                                    ih.bind(ih.getColumnIndex("lan_an"), Json.getInt("lan_an"));
                                                    ih.bind(ih.getColumnIndex("so_nhay"), 0);
                                                    ih.bind(ih.getColumnIndex("tong_tien"), Json.getInt("tong_tien"));
                                                    ih.bind(ih.getColumnIndex("ket_qua"), 0);
                                                    ih.execute();
                                                    i++;
                                                    str2 = str2;
                                                    str3 = str3;
                                                    str4 = str4;
                                                }
                                                database.setTransactionSuccessful();
                                                database.endTransaction();
                                                ih.close();
                                                database.close();
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(new Date());
                                                SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                                                dmyFormat.setTimeZone(TimeZone.getDefault());
                                                hourFormat.setTimeZone(TimeZone.getDefault());
                                                String mNgayNhan = dmyFormat.format(calendar.getTime());
                                                String mGionhan = hourFormat.format(calendar.getTime());
                                                Frag_Chaytrang.this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mNgayNhan + "', '" + mGionhan + "', " + 2 + ", '" + Frag_Chaytrang.this.mwebsite.get(Frag_Chaytrang.this.spin_pointion) + "', '" + Frag_Chaytrang.this.mwebsite.get(Frag_Chaytrang.this.spin_pointion) + "', 'ChayTrang', " + Json.getInt("so_tin_nhan") + ", '" + edt_XuatDan.getText().toString().replace(str4, str3).trim() + "', '" + edt_XuatDan.getText().toString().replace(str4, str3).trim() + "', '" + edt_XuatDan.getText().toString().replace(str4, str3).trim().toLowerCase() + "', 'ok',0, 0, 0, null)");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Frag_Chaytrang.this.xem_RecycView();
                                            dialog.dismiss();
                                            Toast.makeText(Frag_Chaytrang.this.getActivity(), "Đã chạy thành công!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass18.AnonymousClass2 */

                                        public void run() {
                                            edt_XuatErr.setText("Kết nối kém, hãy xuất lại.");
                                            edt_XuatErr.setVisibility(VISIBLE);
                                            btn_chuyendi.setEnabled(true);
                                        }
                                    });
                                }
                                return;
                            }
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass18.AnonymousClass3 */

                                public void run() {
                                    edt_XuatErr.setText("Kết nối kém, hãy xuất lại.");
                                    edt_XuatErr.setVisibility(VISIBLE);
                                    btn_chuyendi.setEnabled(true);
                                }
                            });
                        } catch (Exception e3) {
                            e = e3;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass18.AnonymousClass4 */

                                public void run() {
                                    edt_XuatErr.setText("Kết nối kém, hãy xuất lại.");
                                    edt_XuatErr.setVisibility(VISIBLE);
                                    btn_chuyendi.setEnabled(true);
                                }
                            });
                            e.printStackTrace();
                        }
                    } catch (Exception e4) {
                        e = e4;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass18.AnonymousClass4 */

                            public void run() {
                                edt_XuatErr.setText("Kết nối kém, hãy xuất lại.");
                                edt_XuatErr.setVisibility(VISIBLE);
                                btn_chuyendi.setEnabled(true);
                            }
                        });
                        e.printStackTrace();
                    }
                } catch (Exception e5) {
                    e = e5;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass18.AnonymousClass4 */

                        public void run() {
                            edt_XuatErr.setText("Kết nối kém, hãy xuất lại.");
                            edt_XuatErr.setVisibility(VISIBLE);
                            btn_chuyendi.setEnabled(true);
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        dialog.setCancelable(true);
        dialog.setTitle("Xem dạng:");
        dialog.show();
    }

    public void lambda$Dialog$0$Frag_Chaytrang(OkHttpClient okHttpClient, final TextView taikhoan, final TextView CreditLimit, final TextView Balance) {
        try {
            if (MainActivity.MyToken.length() > 0) {
                Request.Builder builder = new Request.Builder();
                ResponseBody body = okHttpClient.newCall(builder.header("Authorization", "Bearer " + MainActivity.MyToken).url("https://id.lotusapi.com/wallets/player/my-wallet").get().build()).execute().body();
                if (body != null) {
                    final JSONObject json = new JSONObject(body.string());
                    if (!json.has("message")) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass17 */

                            public void run() {
                                try {
                                    DecimalFormat decimalFormat = new DecimalFormat("###,###");
                                    taikhoan.setText(Frag_Chaytrang.this.mwebsite.get(Frag_Chaytrang.this.spin_pointion));
                                    CreditLimit.setText(decimalFormat.format(json.getDouble("CreditLimit")));
                                    Balance.setText(decimalFormat.format(json.getDouble("Balance")));
                                    Frag_Chaytrang.this.myBalance = json.getDouble("Balance");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else if (json.getString("message").indexOf("Unauthorized") > -1) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass16 */

                            public void run() {
                                Toast.makeText(Frag_Chaytrang.this.getActivity(), "Tài khoản đăng nhập lỗi!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public void Dialog2() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.frag_chaytrang_tinchay);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        dialog.getWindow().setLayout(-1, -2);
        ListView lv_cacmachay = (ListView) dialog.findViewById(R.id.lv_cacmachay);
        this.SoTin.clear();
        this.TheLoai.clear();
        this.NoiDung.clear();
        this.ThoiGian.clear();
        this.TienCuoc.clear();
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            if (Build.VERSION.SDK_INT >= 24) {
                CompletableFuture.runAsync(new Runnable() {
                    /* class tamhoang.ldpro4.Fragment.$$Lambda$Frag_Chaytrang$XywKntZqBQRIghYKYLeRhEnIabQ */
                    public final OkHttpClient f$1;
                    public final DecimalFormat f$2;
                    public final ListView f$3;

                    {
                        this.f$1 = okHttpClient;
                        this.f$2 = decimalFormat;
                        this.f$3 = lv_cacmachay;
                    }

                    public final void run() {
                        Frag_Chaytrang.this.lambda$Dialog2$1$Frag_Chaytrang(this.f$1, this.f$2, this.f$3);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.getWindow().setLayout(-1, -2);
        dialog.setCancelable(true);
        dialog.setTitle("Xem dạng:");
        dialog.show();
    }

    public void lambda$Dialog2$1$Frag_Chaytrang(OkHttpClient okHttpClient, DecimalFormat decimalFormat, final ListView lv_cacmachay) {
        try {
            Request.Builder builder = new Request.Builder();
            ResponseBody body = okHttpClient.newCall(builder.header("Authorization", "Bearer " + MainActivity.MyToken).url("https://lotto.lotusapi.com/game-play/player/tickets/current?limit=100").get().build()).execute().body();
            if (body != null) {
                JSONArray jsonArray2 = new JSONArray(body.string());
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonObject = jsonArray2.getJSONObject(i);
                    this.SoTin.add(jsonObject.getString("TicketNumber"));
                    this.TheLoai.add(Integer.valueOf(jsonObject.getInt("BetType")));
                    this.NoiDung.add(jsonObject.getString("Numbers"));
                    String[] SSS = jsonObject.getString("CreatedAt").substring(11).substring(0, 8).split(":");
                    SSS[0] = (Integer.parseInt(SSS[0]) + 7) + "";
                    this.ThoiGian.add(SSS[0] + ":" + SSS[1] + ":" + SSS[2]);
                    this.TienCuoc.add(decimalFormat.format(jsonObject.getLong("Amount")));
                    if (jsonObject.has("CancelledAt")) {
                        this.HuyCuoc.add(0);
                    } else {
                        this.HuyCuoc.add(1);
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass19 */

                    public void run() {
                        ListView listView = lv_cacmachay;
                        Frag_Chaytrang frag_Chaytrang = Frag_Chaytrang.this;
                        listView.setAdapter((ListAdapter) new Ma_da_chay(frag_Chaytrang.getActivity(), R.layout.frag_chaytrang_tinchay_lv, Frag_Chaytrang.this.NoiDung));
                    }
                });
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    class Ma_da_chay extends ArrayAdapter {
        public Ma_da_chay(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        class ViewHolder {
            TextView tv_HuyCuoc;
            TextView tv_NoiDung;
            TextView tv_SoTin;
            TextView tv_ThoiGian;
            TextView tv_TienCuoc;

            ViewHolder() {
            }
        }

        @SuppressLint("WrongConstant")
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            View view2 = null;
            if (0 == 0) {
                view2 = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.frag_chaytrang_tinchay_lv, (ViewGroup) null);
                holder = new ViewHolder();
                holder.tv_SoTin = (TextView) view2.findViewById(R.id.tv_SoTin);
                holder.tv_NoiDung = (TextView) view2.findViewById(R.id.tv_NoiDung);
                holder.tv_ThoiGian = (TextView) view2.findViewById(R.id.tv_ThoiGan);
                holder.tv_TienCuoc = (TextView) view2.findViewById(R.id.tv_TienCuoc);
                holder.tv_HuyCuoc = (TextView) view2.findViewById(R.id.tv_HuyCuoc);
            } else {
                holder = (ViewHolder) view2.getTag();
            }
            holder.tv_HuyCuoc.setFocusable(false);
            holder.tv_HuyCuoc.setFocusableInTouchMode(false);
            holder.tv_HuyCuoc.setOnClickListener(new View.OnClickListener() {
                /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.Ma_da_chay.AnonymousClass1 */

                public void onClick(View view) {
                }
            });
            if (Frag_Chaytrang.this.TheLoai.get(position).intValue() == 0) {
                TextView textView = holder.tv_NoiDung;
                textView.setText("Đề: " + Frag_Chaytrang.this.NoiDung.get(position));
            } else if (Frag_Chaytrang.this.TheLoai.get(position).intValue() == 1) {
                TextView textView2 = holder.tv_NoiDung;
                textView2.setText("Lô: " + Frag_Chaytrang.this.NoiDung.get(position));
            } else if (Frag_Chaytrang.this.TheLoai.get(position).intValue() == 2) {
                TextView textView3 = holder.tv_NoiDung;
                textView3.setText("Xiên 2: " + Frag_Chaytrang.this.NoiDung.get(position));
            } else if (Frag_Chaytrang.this.TheLoai.get(position).intValue() == 3) {
                TextView textView4 = holder.tv_NoiDung;
                textView4.setText("Xiên 3: " + Frag_Chaytrang.this.NoiDung.get(position));
            } else if (Frag_Chaytrang.this.TheLoai.get(position).intValue() == 4) {
                TextView textView5 = holder.tv_NoiDung;
                textView5.setText("Xiên 4: " + Frag_Chaytrang.this.NoiDung.get(position));
            } else if (Frag_Chaytrang.this.TheLoai.get(position).intValue() == 20) {
                TextView textView6 = holder.tv_NoiDung;
                textView6.setText("Lô Live: " + Frag_Chaytrang.this.NoiDung.get(position));
            } else if (Frag_Chaytrang.this.TheLoai.get(position).intValue() == 21) {
                TextView textView7 = holder.tv_NoiDung;
                textView7.setText("Đề đầu: " + Frag_Chaytrang.this.NoiDung.get(position));
            } else if (Frag_Chaytrang.this.TheLoai.get(position).intValue() == 22) {
                TextView textView8 = holder.tv_NoiDung;
                textView8.setText("Giải nhất: " + Frag_Chaytrang.this.NoiDung.get(position));
            } else if (Frag_Chaytrang.this.TheLoai.get(position).intValue() == 23) {
                TextView textView9 = holder.tv_NoiDung;
                textView9.setText("Đầu giải nhất: " + Frag_Chaytrang.this.NoiDung.get(position));
            }
            TextView textView10 = holder.tv_ThoiGian;
            textView10.setText("Time: " + Frag_Chaytrang.this.ThoiGian.get(position));
            TextView textView11 = holder.tv_TienCuoc;
            textView11.setText("Tổng: " + Frag_Chaytrang.this.TienCuoc.get(position));
            holder.tv_SoTin.setText(Frag_Chaytrang.this.SoTin.get(position));
            if (Frag_Chaytrang.this.HuyCuoc.get(position).intValue() == 0) {
                holder.tv_HuyCuoc.setTextColor(-7829368);
                holder.tv_HuyCuoc.setEnabled(false);
                holder.tv_HuyCuoc.setText("Đã huỷ");
            } else {
                holder.tv_HuyCuoc.setVisibility(8);
            }
            return view2;
        }
    }

    /* access modifiers changed from: private */
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("HH:mm", Locale.US).parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.runnable);
    }

    /* JADX WARNING: Removed duplicated region for block: B:189:0x0539 A[Catch:{ all -> 0x0548 }] */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x0544  */
    /* JADX WARNING: Removed duplicated region for block: B:198:0x054f  */
    public String KiemTraTruocKhiChayTrang(String DanSo) throws JSONException {
        String Str1;
        Cursor cursor;
        JSONException e;
        JSONException e2;
        String str;
        JSONObject jSon_Chuyen;
        Frag_Chaytrang frag_Chaytrang;
        int TongTienTinNay;
        JSONException e3;
        String str2;
        int mMax2;
        Iterator<String> iter;
        int TongTienTinNay2;
        String Dayso;
        String Tien;
        int i;
        String str3;
        String str4;
        int i2;
        Frag_Chaytrang frag_Chaytrang2 = this;
        frag_Chaytrang2.jsonChayTrang = new JSONObject();
        JSONObject jSon_Chuyen2 = new JSONObject();
        new MainActivity();
        String mDate = MainActivity.Get_date();
        if (frag_Chaytrang2.the_loai.indexOf("deb") > -1) {
            Str1 = "Select so_chon, Sum(diem) FROM tbl_soctS Where ten_kh = '" + frag_Chaytrang2.mwebsite.get(frag_Chaytrang2.spin_pointion) + "' AND type_kh = 2 AND (the_loai = 'deb' or the_loai = 'det') AND ngay_nhan = '" + mDate + "' Group by so_chon";
        } else if (frag_Chaytrang2.the_loai.indexOf("xi2") > -1) {
            Str1 = "Select so_chon, Sum(diem) FROM tbl_soctS Where ten_kh = '" + frag_Chaytrang2.mwebsite.get(frag_Chaytrang2.spin_pointion) + "' AND type_kh = 2 AND the_loai = 'xi' AND length(so_chon) = 5  AND ngay_nhan = '" + mDate + "' Group by so_chon";
        } else if (frag_Chaytrang2.the_loai.indexOf("xi3") > -1) {
            Str1 = "Select so_chon, Sum(diem) FROM tbl_soctS Where ten_kh = '" + frag_Chaytrang2.mwebsite.get(frag_Chaytrang2.spin_pointion) + "' AND type_kh = 2 AND the_loai = 'xi' AND length(so_chon) = 8  AND ngay_nhan = '" + mDate + "' Group by so_chon";
        } else if (frag_Chaytrang2.the_loai.indexOf("xi4") > -1) {
            Str1 = "Select so_chon, Sum(diem) FROM tbl_soctS Where ten_kh = '" + frag_Chaytrang2.mwebsite.get(frag_Chaytrang2.spin_pointion) + "' AND type_kh = 2 AND the_loai = 'xi' AND length(so_chon) = 11  AND ngay_nhan = '" + mDate + "' Group by so_chon";
        } else {
            Str1 = "Select so_chon, Sum(diem) FROM tbl_soctS Where ten_kh = '" + frag_Chaytrang2.mwebsite.get(frag_Chaytrang2.spin_pointion) + "' AND type_kh = 2 AND the_loai = '" + frag_Chaytrang2.the_loai + "' AND ngay_nhan = '" + mDate + "' Group by so_chon";
        }
        Cursor cursor2 = frag_Chaytrang2.db.GetData(Str1);
        while (cursor2.moveToNext()) {
            try {
                try {
                    jSon_Chuyen2.put(cursor2.getString(0), cursor2.getInt(1));
                } catch (JSONException e4) {
                    e2 = e4;
                    cursor = cursor2;
                } catch (Throwable th) {
                    cursor = cursor2;
                    if (!cursor.isClosed()) {
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                cursor = cursor2;
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                throw th3;
            }
        }
        String str5 = "k";
        String str6 = "d";
        String str7 = ",";
        int TongTienTinNay3 = 0;
        if (!frag_Chaytrang2.the_loai.contains("xi")) {
            try {
                String[] ListDan = DanSo.substring(DanSo.indexOf(":")).split(" ");
                int i3 = 0;
                while (i3 < ListDan.length) {
                    try {
                    } catch (Throwable th4) {
                        cursor = cursor2;
                        if (!cursor.isClosed()) {
                        }
                        throw th4;
                    }
                    try {
                        String Dayso2 = ListDan[i3].substring(ListDan[i3].indexOf(":") + 1);
                        try {
                            String Dayso3 = Dayso2.substring(0, Dayso2.indexOf("x"));
                            String Tien2 = ListDan[i3].substring(ListDan[i3].indexOf("x")).replaceAll("x", "").replaceAll("n", "").replaceAll(str6, "").replaceAll(str5, "");
                            String[] Numbers = Dayso3.split(str7);
                            int length = Numbers.length;
                            int TongTienTinNay4 = TongTienTinNay3;
                            int i4 = 0;
                            while (i4 < length) {
                                try {
                                    String Number = Numbers[i4];
                                    if (frag_Chaytrang2.jsonChayTrang.has(Number)) {
                                        try {
                                            cursor = cursor2;
                                            try {
                                                frag_Chaytrang2.jsonChayTrang.put(Number, frag_Chaytrang2.jsonChayTrang.getInt(Number) + Integer.parseInt(Tien2));
                                            } catch (JSONException e7) {
                                                e2 = e7;
                                            } catch (Throwable th5) {
                                                if (!cursor.isClosed()) {
                                                }
                                                throw th5;
                                            }
                                        } catch (Throwable th6) {
                                            cursor = cursor2;
                                            if (!cursor.isClosed()) {
                                            }
                                            throw th6;
                                        }
                                    } else {
                                        cursor = cursor2;
                                        try {
                                            frag_Chaytrang2.jsonChayTrang.put(Number, Tien2);
                                        } catch (JSONException e9) {
                                            e2 = e9;
                                            e2.printStackTrace();
                                            if (!cursor.isClosed()) {
                                            }
                                            if (!cursor.isClosed()) {
                                            }
                                            return "Không phân tích được nội dung!";
                                        } catch (Throwable th7) {
                                            if (!cursor.isClosed()) {
                                            }
                                            throw th7;
                                        }
                                    }
                                    if (frag_Chaytrang2.jsonGia.has(Number)) {
                                        str4 = str6;
                                        double d = (double) TongTienTinNay4;
                                        double parseDouble = Double.parseDouble(Tien2);
                                        str3 = str5;
                                        double d2 = (double) (frag_Chaytrang2.jsonGia.getInt(Number) + frag_Chaytrang2.Price);
                                        Double.isNaN(d2);
                                        Double.isNaN(d);
                                        i2 = (int) (d + (parseDouble * d2));
                                    } else {
                                        str3 = str5;
                                        str4 = str6;
                                        double d3 = (double) TongTienTinNay4;
                                        double parseDouble2 = Double.parseDouble(Tien2);
                                        double d4 = (double) frag_Chaytrang2.Price;
                                        Double.isNaN(d4);
                                        Double.isNaN(d3);
                                        i2 = (int) (d3 + (parseDouble2 * d4));
                                    }
                                    TongTienTinNay4 = i2;
                                    i4++;
                                    frag_Chaytrang2 = this;
                                    cursor2 = cursor;
                                    length = length;
                                    Numbers = Numbers;
                                    str6 = str4;
                                    str5 = str3;
                                } catch (JSONException e10) {
                                    e2 = e10;
                                    cursor = cursor2;
                                    e2.printStackTrace();
                                    if (!cursor.isClosed()) {
                                    }
                                    if (!cursor.isClosed()) {
                                    }
                                    return "Không phân tích được nội dung!";
                                } catch (Throwable th8) {
                                    cursor = cursor2;
                                    if (!cursor.isClosed()) {
                                    }
                                    throw th8;
                                }
                            }
                            i3++;
                            frag_Chaytrang2 = this;
                            TongTienTinNay3 = TongTienTinNay4;
                            mDate = mDate;
                            Str1 = Str1;
                            jSon_Chuyen2 = jSon_Chuyen2;
                            ListDan = ListDan;
                            str7 = str7;
                        } catch (Throwable th9) {
                            cursor = cursor2;
                            if (!cursor.isClosed()) {
                            }
                            throw th9;
                        }
                    } catch (Throwable th10) {
                        cursor = cursor2;
                        if (!cursor.isClosed()) {
                        }
                        throw th10;
                    }
                }
                jSon_Chuyen = jSon_Chuyen2;
                cursor = cursor2;
                str = str7;
                frag_Chaytrang = this;
                TongTienTinNay = TongTienTinNay3;
            } catch (Throwable th11) {
                cursor = cursor2;
                if (!cursor.isClosed()) {
                }
                throw th11;
            }
        } else {
            jSon_Chuyen = jSon_Chuyen2;
            cursor = cursor2;
            String str8 = str5;
            String str9 = str6;
            str = str7;
            String[] ListDan2 = DanSo.substring(DanSo.indexOf(":")).split(" ");
            int i5 = 0;
            int TongTienTinNay5 = 0;
            while (i5 < ListDan2.length) {
                String Dayso4 = ListDan2[i5].substring(ListDan2[i5].indexOf(":") + 1);
                Dayso = Dayso4.substring(0, Dayso4.indexOf("x"));
                Tien = ListDan2[i5].substring(ListDan2[i5].indexOf("x")).replaceAll("x", "").replaceAll("n", "").replaceAll(str9, "").replaceAll(str8, "");
                try {
                    if (this.jsonChayTrang.has(Dayso)) {
                        try {
                            this.jsonChayTrang.put(Dayso, this.jsonChayTrang.getInt(Dayso) + Integer.parseInt(Tien));
                        } catch (JSONException e16) {
                            e2 = e16;
                        } catch (Throwable th14) {
                            if (!cursor.isClosed()) {
                            }
                            throw th14;
                        }
                    } else {
                        this.jsonChayTrang.put(Dayso, Tien);
                    }
                    double d5 = (double) TongTienTinNay5;
                    try {
                        double parseDouble3 = Double.parseDouble(Tien) * this.jsonTienxien.getDouble(Dayso);
                        Double.isNaN(d5);
                        i = (int) (d5 + parseDouble3);
                    } catch (Exception e17) {
                        double d6 = (double) TongTienTinNay5;
                        double parseDouble4 = Double.parseDouble(Tien);
                        double d7 = (double) (this.Price + 20);
                        Double.isNaN(d7);
                        Double.isNaN(d6);
                        i = (int) (d6 + (parseDouble4 * d7));
                    }
                    TongTienTinNay5 = i;
                    i5++;
                    ListDan2 = ListDan2;
                    str9 = str9;
                    str8 = str8;
                } catch (JSONException e18) {
                    e2 = e18;
                    e2.printStackTrace();
                    if (!cursor.isClosed()) {
                    }
                    if (!cursor.isClosed()) {
                    }
                    return "Không phân tích được nội dung!";
                }
            }
            frag_Chaytrang = this;
            TongTienTinNay = TongTienTinNay5;
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        if (((double) TongTienTinNay) > frag_Chaytrang.myBalance) {
            return "Vượt quá số tiền còn lại";
        }
        Iterator<String> iter2 = frag_Chaytrang.jsonChayTrang.keys();
        String SoVuotMax = "";
        while (iter2.hasNext()) {
            String key = iter2.next();
            if (frag_Chaytrang.myMax.contains(".")) {
                mMax2 = Integer.parseInt(frag_Chaytrang.myMax.replaceAll("\\.", ""));
                str2 = str;
            } else {
                str2 = str;
                mMax2 = Integer.parseInt(frag_Chaytrang.myMax.replaceAll(str2, ""));
            }
            try {
                if (jSon_Chuyen.has(key)) {
                    TongTienTinNay2 = TongTienTinNay;
                    iter = iter2;
                    if (frag_Chaytrang.jsonChayTrang.getDouble(key) + jSon_Chuyen.getDouble(key) > ((double) mMax2)) {
                        SoVuotMax = SoVuotMax + key + " ";
                    }
                } else {
                    TongTienTinNay2 = TongTienTinNay;
                    iter = iter2;
                    if (frag_Chaytrang.jsonChayTrang.getDouble(key) > ((double) mMax2)) {
                        SoVuotMax = SoVuotMax + key + " ";
                    }
                }
                jSon_Chuyen = jSon_Chuyen;
                str = str2;
                TongTienTinNay = TongTienTinNay2;
                iter2 = iter;
            } catch (JSONException e22) {
                e3 = e22;
                e3.printStackTrace();
                return "Kiểm tra lại số liệu";
            }
        }
        if (SoVuotMax.length() <= 0) {
            return "";
        }
        return "Các cặp: " + SoVuotMax + " vượt quá max của trang";
    }

    /* JADX INFO: Multiple debug info for r4v9 java.lang.String[]: [D('SovaoTrang' java.lang.String), D('MyNumber' java.lang.String[])] */
    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x00b0, code lost:
        if (r26.GameType != 23) goto L_0x00cc;
     */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x013a A[SYNTHETIC, Splitter:B:52:0x013a] */
    private String CreateJson() throws JSONException {
        JSONException e;
        int LanAn;
        Iterator<String> iter;
        JSONObject jsonObject;
        JSONException e2;
        String mNgayNhan;
        String str;
        Iterator<String> iter2;
        Cursor cursor;
        String str2;
        int i;
        String[] MyNumber;
        String str3 = ",";
        JSONObject jsonObject2 = new JSONObject();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        String mNgayNhan2 = dmyFormat.format(calendar.getTime());
        Cursor cursor2 = this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhans where ngay_nhan = '" + mNgayNhan2 + "' And ten_kh = '" + this.mwebsite.get(this.spin_pointion) + "'");
        cursor2.moveToFirst();
        int Sotinnhan = cursor2.getInt(0) + 1;
        try {
            jsonObject2 = new JSONObject();
            try {
                jsonObject2.put("Term", mNgayNhan2);
                jsonObject2.put("IgnorePrice", true);
                JSONArray Tickets = new JSONArray();
                JSONObject ticket = new JSONObject();
                ticket.put("GameType", 0);
                ticket.put("BetType", this.GameType);
                JSONArray Items = new JSONArray();
                new JSONObject();
                new JSONArray();
                if (this.GameType != 0) {
                    if (this.GameType != 21) {
                        if (this.GameType != 22) {
                        }
                    }
                }
                if (this.Price < 80000) {
                    LanAn = 70000;
                    iter = this.jsonChayTrang.keys();
                    while (iter.hasNext()) {
                        try {
                            String key = iter.next();
                            try {
                                if (this.jsonChayTrang.getDouble(key) > 0.0d) {
                                    JSONObject number = new JSONObject();
                                    JSONArray Numbers = new JSONArray();
                                    String[] MyNumber2 = key.split(str3);
                                    cursor = cursor2;
                                    int length = MyNumber2.length;
                                    iter2 = iter;
                                    String SovaoTrang = "";
                                    jsonObject = jsonObject2;
                                    int i2 = 0;
                                    while (i2 < length) {
                                        String i3 = MyNumber2[i2];
                                        if (i3.length() > 0) {
                                            MyNumber = MyNumber2;
                                            Numbers.put(i3);
                                            i = length;
                                            SovaoTrang = SovaoTrang + i3 + str3;
                                        } else {
                                            MyNumber = MyNumber2;
                                            i = length;
                                        }
                                        i2++;
                                        MyNumber2 = MyNumber;
                                        length = i;
                                    }
                                    try {
                                        if (SovaoTrang.endsWith(str3)) {
                                            SovaoTrang = SovaoTrang.substring(0, SovaoTrang.length() - 1);
                                        }
                                        JSONObject Json = new JSONObject();
                                        Json.put("ngay_nhan", mNgayNhan2);
                                        Json.put("type_kh", 2);
                                        str = str3;
                                        Json.put("ten_kh", this.mwebsite.get(this.spin_pointion));
                                        Json.put("so_dienthoai", this.mwebsite.get(this.spin_pointion));
                                        Json.put("so_tin_nhan", Sotinnhan);
                                        if (this.the_loai.indexOf("xi") > -1) {
                                            str2 = "xi";
                                        } else {
                                            str2 = this.the_loai;
                                        }
                                        Json.put("the_loai", str2);
                                        Json.put("so_chon", SovaoTrang);
                                        mNgayNhan = mNgayNhan2;
                                        try {
                                            Json.put("diem", this.jsonChayTrang.getDouble(key));
                                            Json.put("diem_quydoi", this.jsonChayTrang.getDouble(key));
                                            Json.put("diem_khachgiu", 0);
                                            Json.put("diem_dly_giu", 0);
                                            Json.put("diem_ton", this.jsonChayTrang.getDouble(key));
                                            if (!this.radio_xi.isChecked()) {
                                                Json.put("gia", this.jsonGia.has(key) ? this.Price + this.jsonGia.getInt(key) : this.Price);
                                            } else if (this.jsonTienxien.has(SovaoTrang)) {
                                                Json.put("gia", this.jsonTienxien.getInt(SovaoTrang));
                                            } else {
                                                Json.put("gia", this.Price);
                                            }
                                            Json.put("lan_an", LanAn);
                                            Json.put("so_nhay", 0);
                                            double d = (double) (this.jsonGia.has(key) ? this.Price + this.jsonGia.getInt(key) : this.Price);
                                            double d2 = this.jsonChayTrang.getDouble(key);
                                            Double.isNaN(d);
                                            Json.put("tong_tien", d * d2);
                                            Json.put("ket_qua", 0);
                                            this.jsonArray.put(Json);
                                            number.put("Numbers", Numbers);
                                            number.put("Point", this.jsonChayTrang.getDouble(key));
                                            number.put("Price", 705);
                                            Items.put(number);
                                        } catch (JSONException e6) {
                                            e2 = e6;
                                            e2.printStackTrace();
                                            return "Kiểm tra lại số liệu";
                                        }
                                    } catch (JSONException e7) {
                                        e2 = e7;
                                        e2.printStackTrace();
                                        return "Kiểm tra lại số liệu";
                                    }
                                } else {
                                    str = str3;
                                    jsonObject = jsonObject2;
                                    mNgayNhan = mNgayNhan2;
                                    cursor = cursor2;
                                    iter2 = iter;
                                }
                                calendar = calendar;
                                jsonObject2 = jsonObject;
                                dmyFormat = dmyFormat;
                                cursor2 = cursor;
                                iter = iter2;
                                str3 = str;
                                mNgayNhan2 = mNgayNhan;
                            } catch (JSONException e9) {
                                e2 = e9;
                                jsonObject = jsonObject2;
                                e2.printStackTrace();
                                return "Kiểm tra lại số liệu";
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return jsonObject2.toString();
                        }
                    }
                    try {
                        ticket.put("Items", Items);
                        Tickets.put(ticket);
                        try {
                            jsonObject2.put("Tickets", Tickets);
                            jsonObject2 = jsonObject2;
                        } catch (JSONException e12) {
                            e = e12;
                            jsonObject2 = jsonObject2;
                            e.printStackTrace();
                            return jsonObject2.toString();
                        }
                    } catch (JSONException e13) {
                        e = e13;
                        jsonObject2 = jsonObject2;
                        e.printStackTrace();
                        return jsonObject2.toString();
                    }
                    return jsonObject2.toString();
                }
                if ((this.GameType == 0 || this.GameType == 21 || this.GameType == 22 || this.GameType == 23) && this.Price > 80000) {
                    LanAn = 80000;
                    iter = this.jsonChayTrang.keys();
                    while (iter.hasNext()) {
                    }
                    ticket.put("Items", Items);
                    Tickets.put(ticket);
                    jsonObject2.put("Tickets", Tickets);
                    jsonObject2 = jsonObject2;
                    return jsonObject2.toString();
                } else if (this.GameType == 1 || this.GameType == 20) {
                    LanAn = 80000;
                    iter = this.jsonChayTrang.keys();
                    while (iter.hasNext()) {
                    }
                    ticket.put("Items", Items);
                    Tickets.put(ticket);
                    jsonObject2.put("Tickets", Tickets);
                    jsonObject2 = jsonObject2;
                    return jsonObject2.toString();
                } else {
                    if (this.GameType == 2) {
                        LanAn = 10000;
                    } else if (this.GameType == 3) {
                        LanAn = 40000;
                    } else if (this.GameType == 4) {
                        LanAn = 100000;
                    } else if (this.GameType == 6) {
                        LanAn = 80000;
                    } else {
                        LanAn = 0;
                    }
                    iter = this.jsonChayTrang.keys();
                    while (iter.hasNext()) {
                    }
                    ticket.put("Items", Items);
                    Tickets.put(ticket);
                    jsonObject2.put("Tickets", Tickets);
                    jsonObject2 = jsonObject2;
                    return jsonObject2.toString();
                }
            } catch (JSONException e14) {
                e = e14;
                e.printStackTrace();
                return jsonObject2.toString();
            }
        } catch (Exception e15) {
            e15.printStackTrace();
            return jsonObject2.toString();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void login(String Username, String PassWord) {
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject Json = new JSONObject();
        AtomicReference<String> str3 = new AtomicReference<>("");
        if (Build.VERSION.SDK_INT >= 24) {
            CompletableFuture.runAsync(new Runnable() {
                /* class tamhoang.ldpro4.Fragment.$$Lambda$Frag_Chaytrang$lQEsOD1ZvBMJIMuUzyYsFMIunc */
                public final JSONObject f$1;
                public final String f$2;
                public final String f$3;
                public final AtomicReference f$4;
                public final OkHttpClient f$5;

                {
                    this.f$1 = Json;
                    this.f$2 = Username;
                    this.f$3 = PassWord;
                    this.f$4 = str3;
                    this.f$5 = okHttpClient;
                }

                public final void run() {
                    Frag_Chaytrang.this.lambda$login$2$Frag_Chaytrang(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
                }
            });
        }
    }

    public void lambda$login$2$Frag_Chaytrang(JSONObject Json, String Username, String PassWord, AtomicReference str3, OkHttpClient okHttpClient) {
        try {
            Json.put("Username", Username);
            Json.put("Password", PassWord);
            str3.set(okHttpClient.newCall(new Request.Builder().url("https://id.lotusapi.com/auth/sign-in").header(HttpConnection.CONTENT_TYPE, "application/json").post(RequestBody.Companion.create(Json.toString(), MediaType.Companion.parse("application/json"))).build()).execute().body().string());
            JSONObject jsonObject = new JSONObject(str3.toString());
            if (jsonObject.has("IdToken")) {
                MainActivity.MyToken = jsonObject.getString("IdToken");
                Laygia();
                return;
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass21 */

                public void run() {
                    Toast.makeText(Frag_Chaytrang.this.getActivity(), "Đăng nhập thất bại.", Toast.LENGTH_SHORT).show();
                    MainActivity.MyToken = "";
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String Laygia() {
        this.jsonGia = new JSONObject();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
        dmyFormat.setTimeZone(TimeZone.getDefault());
        String mNgayNhan = dmyFormat.format(calendar.getTime());
        String[] loi = {""};
        OkHttpClient okHttpClient = new OkHttpClient();
        if (MainActivity.MyToken.length() > 0 && Build.VERSION.SDK_INT >= 24) {
            CompletableFuture.runAsync(new Runnable() {
                /* class tamhoang.ldpro4.Fragment.$$Lambda$Frag_Chaytrang$hzE2FSVfsxb0QfhTAHA5cgHOUQM */
                public final String f$1;
                public final OkHttpClient f$2;
                public final String[] f$3;

                {
                    this.f$1 = mNgayNhan;
                    this.f$2 = okHttpClient;
                    this.f$3 = loi;
                }

                public final void run() {
                    Frag_Chaytrang.this.lambda$Laygia$3$Frag_Chaytrang(this.f$1, this.f$2, this.f$3);
                }
            });
        }
        return loi[0];
    }

    public void lambda$Laygia$3$Frag_Chaytrang(String mNgayNhan, OkHttpClient okHttpClient, String[] loi) {
        String Url;
        try {
            if (MainActivity.MyToken.length() > 0) {
                if (!this.radio_lo.isChecked() || !this.LoLive) {
                    Url = "https://lotto.lotusapi.com/odds/player?term=" + mNgayNhan + "&gameTypes=0&betTypes=" + this.GameType;
                } else {
                    this.GameType = 20;
                    Url = "https://lotto.lotusapi.com/odds/player/live?term=" + mNgayNhan + "&gameType=0&betType=20";
                }
                ResponseBody body = okHttpClient.newCall(new Request.Builder().header("Authorization", "Bearer " + MainActivity.MyToken).url(Url).get().build()).execute().body();
                if (body != null) {
                    if (!this.LoLive || !this.radio_lo.isChecked()) {
                        JSONArray JArray = new JSONArray(body.string());
                        JSONObject jsonObject = JArray.getJSONObject(0);
                        this.Price = jsonObject.getInt("Price");
                        this.PriceLive = 0;
                        JSONArray numbers = jsonObject.getJSONArray("Numbers");
                        this.jsonGia = new JSONObject();
                        int i = 0;
                        while (i < numbers.length()) {
                            JSONObject number = numbers.getJSONObject(i);
                            this.jsonGia.put(number.getString("Number"), number.getString("ExtraPrice"));
                            i++;
                            JArray = JArray;
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass24 */

                            public void run() {
                                Frag_Chaytrang.this.xem_RecycView();
                            }
                        });
                    } else {
                        JSONObject jsonObject2 = new JSONObject(body.string());
                        if (!jsonObject2.getBoolean("Active")) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass22 */

                                public void run() {
                                    Frag_Chaytrang.this.btn_Xuatso.setText("Trang đã đóng");
                                    Frag_Chaytrang.this.btn_Xuatso.setEnabled(false);
                                    Frag_Chaytrang.this.handler.removeCallbacks(Frag_Chaytrang.this.runnable);
                                }
                            });
                        }
                        this.Price = jsonObject2.getInt("Price");
                        JSONArray numbers2 = jsonObject2.getJSONArray("Numbers");
                        this.jsonGia = new JSONObject();
                        for (int i2 = 0; i2 < numbers2.length(); i2++) {
                            JSONObject number2 = numbers2.getJSONObject(i2);
                            this.jsonGia.put(number2.getString("Number"), number2.getString("ExtraPrice"));
                        }
                        if (this.Price != this.PriceLive) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                /* class tamhoang.ldpro4.Fragment.Frag_Chaytrang.AnonymousClass23 */

                                public void run() {
                                    Frag_Chaytrang.this.xem_RecycView();
                                }
                            });
                            this.PriceLive = this.Price;
                        }
                    }
                }
                ResponseBody body2 = okHttpClient.newCall(new Request.Builder().header("Authorization", "Bearer " + MainActivity.MyToken).url("https://comm.lotusapi.com/servers/current-date-time").get().build()).execute().body();
                if (body2 != null) {
                    JSONObject json = new JSONObject(body2.string());
                    if (json.has("Timestamp")) {
                        Curent_date_time = json.getLong("Timestamp");
                    }
                }
            }
        } catch (IOException | JSONException e) {
            loi[0] = "Kết nối kém, kiểm tra lại Internet";
            e.printStackTrace();
        }
    }

    public void xemlv() {
        if (this.DangXuat != null) {
            xem_RecycView();
        } else {
            this.radio_de.setChecked(true);
        }
    }

    public void xem_RecycView() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        new MainActivity();
        String mDate = MainActivity.Get_date();
        String str7 = null;
        this.jsonTienxien = new JSONObject();
        this.mSo.clear();
        this.mTienNhan.clear();
        this.mTienOm.clear();
        this.mTienchuyen.clear();
        this.mTienTon.clear();
        this.mMax.clear();
        this.mGia.clear();
        this.mNhay.clear();
        String str8 = this.DangXuat;
        if (str8 == "(the_loai = 'deb' or the_loai = 'det')") {
            StringBuilder sb = new StringBuilder();
            sb.append("Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_deB + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2)");
            if (this.spin_pointion > -1) {
                str6 = "*(tbl_soctS.ten_kh='" + this.mwebsite.get(this.spin_pointion) + "')";
            } else {
                str6 = "";
            }
            sb.append(str6);
            sb.append("* tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_deB as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So\n Where tbl_soctS.ngay_nhan='");
            sb.append(mDate);
            sb.append("' AND (tbl_soctS.the_loai='deb' OR tbl_soctS.the_loai='det') GROUP by so_om.So Order by ton DESC");
            str7 = sb.toString();
        } else if (str8 == "the_loai = 'lo'") {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_Lo + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2)");
            if (this.spin_pointion > -1) {
                str5 = "*(tbl_soctS.ten_kh='" + this.mwebsite.get(this.spin_pointion) + "')";
            } else {
                str5 = "";
            }
            sb2.append(str5);
            sb2.append(" * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_Lo as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='");
            sb2.append(mDate);
            sb2.append("' AND tbl_soctS.the_loai='lo' \n GROUP by so_om.So Order by ton DESC");
            str7 = sb2.toString();
        } else if (str8 == "the_loai = 'dea'") {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_DeA + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2)");
            if (this.spin_pointion > -1) {
                str4 = "*(tbl_soctS.ten_kh='" + this.mwebsite.get(this.spin_pointion) + "')";
            } else {
                str4 = "";
            }
            sb3.append(str4);
            sb3.append(" * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_DeA as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='");
            sb3.append(mDate);
            sb3.append("' AND tbl_soctS.the_loai='dea' GROUP by so_chon Order by ton DESC");
            str7 = sb3.toString();
        } else if (str8 == "the_loai = 'dec'") {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_DeC + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2)");
            if (this.spin_pointion > -1) {
                str3 = "*(tbl_soctS.ten_kh='" + this.mwebsite.get(this.spin_pointion) + "')";
            } else {
                str3 = "";
            }
            sb4.append(str3);
            sb4.append(" * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_DeC as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='");
            sb4.append(mDate);
            sb4.append("' AND tbl_soctS.the_loai='dec' GROUP by so_chon Order by ton DESC");
            str7 = sb4.toString();
        } else if (str8 == "the_loai = 'ded'") {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Select tbl_soctS.So_chon\n, Sum((tbl_soctS.type_kh = 1) * (100-tbl_soctS.diem_khachgiu)*diem_quydoi/100) as diem\n, so_om.Om_DeD + sum(tbl_soctS.diem_dly_giu*tbl_soctS.diem_quydoi/100) as So_Om\n, Sum((tbl_soctS.type_kh =2)");
            if (this.spin_pointion > -1) {
                str2 = "*(tbl_soctS.ten_kh='" + this.mwebsite.get(this.spin_pointion) + "')";
            } else {
                str2 = "";
            }
            sb5.append(str2);
            sb5.append(" * tbl_soctS.diem_quydoi) as chuyen\n, Sum((tbl_soctS.type_kh =1) * (100-tbl_soctS.diem_khachgiu-tbl_soctS.diem_dly_giu)*diem_quydoi/100) - Sum((tbl_soctS.type_kh =2) * tbl_soctS.diem_quydoi) - so_om.Om_DeD as ton\n, so_nhay  From so_om Left Join tbl_soctS On tbl_soctS.so_chon = so_om.So \n Where tbl_soctS.ngay_nhan='");
            sb5.append(mDate);
            sb5.append("' AND tbl_soctS.the_loai='ded' GROUP by so_chon Order by ton DESC");
            str7 = sb5.toString();
        } else if (str8 == "the_loai = 'xi'") {
            Cursor c1 = this.db.GetData("Select * From So_om WHERE ID = 1");
            c1.moveToFirst();
            StringBuilder sb6 = new StringBuilder();
            sb6.append("SELECT so_chon, sum((type_kh =1)*(100-diem_khachgiu)*diem_quydoi)/100 AS diem, ((length(so_chon) = 5) * ");
            sb6.append(c1.getString(7));
            sb6.append(" +(length(so_chon) = 8) * ");
            sb6.append(c1.getString(8));
            sb6.append(" +(length(so_chon) = 11) * ");
            sb6.append(c1.getString(9));
            sb6.append(" + sum(diem_dly_giu*diem_quydoi/100)) AS Om, SUm((type_kh =2)");
            if (this.spin_pointion > -1) {
                str = "*(tbl_soctS.ten_kh='" + this.mwebsite.get(this.spin_pointion) + "')";
            } else {
                str = "";
            }
            sb6.append(str);
            sb6.append(" *diem) as chuyen , SUm((type_kh =1)*(100-diem_khachgiu-diem_dly_giu)*diem_quydoi/100)-SUm((type_kh =2)*diem) -  ((length(so_chon) = 5) * ");
            sb6.append(c1.getString(7));
            sb6.append(" +(length(so_chon) = 8) * ");
            sb6.append(c1.getString(8));
            sb6.append(" +(length(so_chon) = 11) * ");
            sb6.append(c1.getString(9));
            sb6.append(") AS ton, so_nhay   From tbl_soctS Where ngay_nhan='");
            sb6.append(mDate);
            sb6.append("' AND the_loai='xi' AND");
            sb6.append(this.lay_xien);
            sb6.append("  GROUP by so_chon Order by ton DESC, diem DESC");
            str7 = sb6.toString();
            if (c1 != null && !c1.isClosed()) {
                c1.close();
            }
        }
        Cursor cursor = this.db.GetData(str7);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        int i = 1;
        if (this.spin_pointion > -1) {
            Cursor Laymax = this.db.GetData("select * from tbl_chaytrang_acc Where Username = '" + this.mwebsite.get(this.spin_pointion) + "'");
            Laymax.moveToFirst();
            try {
                JSONObject jsonMax = new JSONObject(Laymax.getString(2));
                int i2 = this.GameType;
                if (i2 != 0) {
                    if (i2 != 1) {
                        if (i2 == 2) {
                            this.myMax = decimalFormat.format((long) jsonMax.getInt("max_xi2"));
                            this.MaxChay = jsonMax.has("gia_xi2") ? jsonMax.getInt("gia_xi2") : 560;
                        } else if (i2 == 3) {
                            this.myMax = decimalFormat.format((long) jsonMax.getInt("max_xi3"));
                            this.MaxChay = jsonMax.has("gia_xi3") ? jsonMax.getInt("gia_xi3") : 520;
                        } else if (i2 != 4) {
                            switch (i2) {
                                case 21:
                                    this.myMax = decimalFormat.format((long) jsonMax.getInt("max_dea"));
                                    this.MaxChay = jsonMax.getInt("gia_dea");
                                    break;
                                case 22:
                                    this.myMax = decimalFormat.format((long) jsonMax.getInt("max_ded"));
                                    this.MaxChay = jsonMax.getInt("gia_ded");
                                    break;
                                case 23:
                                    this.myMax = decimalFormat.format((long) jsonMax.getInt("max_dec"));
                                    this.MaxChay = jsonMax.getInt("gia_dec");
                                    break;
                            }
                        } else {
                            this.myMax = decimalFormat.format((long) jsonMax.getInt("max_xi4"));
                            this.MaxChay = jsonMax.has("gia_xi4") ? jsonMax.getInt("gia_xi4") : 450;
                        }
                    }
                    this.myMax = decimalFormat.format((long) jsonMax.getInt("max_lo"));
                    this.MaxChay = jsonMax.getInt("gia_lo");
                } else {
                    this.myMax = decimalFormat.format((long) jsonMax.getInt("max_deb"));
                    this.MaxChay = jsonMax.getInt("gia_deb");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Throwable th) {
                Laymax.close();
                throw th;
            }
            Laymax.close();
        } else {
            this.myMax = "0";
        }
        if (cursor != null) {
            int GiaSo1 = 5;
            if (this.radio_xi.isChecked()) {
                while (cursor.moveToNext()) {
                    try {
                        this.mSo.add(cursor.getString(0));
                        this.mTienNhan.add(decimalFormat.format((long) cursor.getInt(i)));
                        this.mTienOm.add(decimalFormat.format((long) cursor.getInt(2)));
                        this.mTienchuyen.add(decimalFormat.format((long) cursor.getInt(3)));
                        this.mTienTon.add(decimalFormat.format((long) cursor.getInt(4)));
                        this.mNhay.add(Integer.valueOf(cursor.getInt(GiaSo1)));
                        this.mMax.add(this.myMax);
                        if (this.radio_xi2.isChecked()) {
                            String[] SoXien = cursor.getString(0).split(",");
                            if (SoXien.length >= 2) {
                                int GiaSo12 = !this.jsonGia.has(SoXien[0]) ? this.Price : this.Price + this.jsonGia.getInt(SoXien[0]);
                                int GiaSo2 = !this.jsonGia.has(SoXien[i]) ? this.Price : this.Price + this.jsonGia.getInt(SoXien[i]);
                                this.mGia.add("" + ((GiaSo12 + GiaSo2) / 2));
                                this.jsonTienxien.put(cursor.getString(0), (GiaSo12 + GiaSo2) / 2);
                            }
                        }
                        if (this.radio_xi3.isChecked()) {
                            String[] SoXien2 = cursor.getString(0).split(",");
                            if (SoXien2.length < 3) {
                                GiaSo1 = 5;
                            } else {
                                int GiaSo13 = !this.jsonGia.has(SoXien2[0]) ? this.Price : this.Price + this.jsonGia.getInt(SoXien2[0]);
                                int GiaSo22 = !this.jsonGia.has(SoXien2[i]) ? this.Price : this.Price + this.jsonGia.getInt(SoXien2[i]);
                                int GiaSo3 = !this.jsonGia.has(SoXien2[2]) ? this.Price : this.Price + this.jsonGia.getInt(SoXien2[2]);
                                this.mGia.add("" + (((GiaSo13 + GiaSo22) + GiaSo3) / 3));
                                this.jsonTienxien.put(cursor.getString(0), ((GiaSo13 + GiaSo22) + GiaSo3) / 3);
                            }
                        }
                        if (this.radio_xi4.isChecked()) {
                            String[] SoXien3 = cursor.getString(0).split(",");
                            if (SoXien3.length < 4) {
                                GiaSo1 = 5;
                                i = 1;
                            } else {
                                int GiaSo14 = !this.jsonGia.has(SoXien3[0]) ? this.Price : this.Price + this.jsonGia.getInt(SoXien3[0]);
                                int GiaSo23 = !this.jsonGia.has(SoXien3[1]) ? this.Price : this.Price + this.jsonGia.getInt(SoXien3[1]);
                                int GiaSo32 = !this.jsonGia.has(SoXien3[2]) ? this.Price : this.Price + this.jsonGia.getInt(SoXien3[2]);
                                int GiaSo4 = !this.jsonGia.has(SoXien3[3]) ? this.Price : this.Price + this.jsonGia.getInt(SoXien3[3]);
                                this.mGia.add("" + ((((GiaSo14 + GiaSo23) + GiaSo32) + GiaSo4) / 4));
                                this.jsonTienxien.put(cursor.getString(0), (((GiaSo14 + GiaSo23) + GiaSo32) + GiaSo4) / 4);
                                GiaSo1 = 5;
                                i = 1;
                            }
                        } else {
                            GiaSo1 = 5;
                            i = 1;
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            } else {
                while (cursor.moveToNext()) {
                    this.mSo.add(cursor.getString(0));
                    this.mTienNhan.add(decimalFormat.format((long) cursor.getInt(1)));
                    this.mTienOm.add(decimalFormat.format((long) cursor.getInt(2)));
                    this.mTienchuyen.add(decimalFormat.format((long) cursor.getInt(3)));
                    this.mTienTon.add(decimalFormat.format((long) cursor.getInt(4)));
                    this.mNhay.add(Integer.valueOf(cursor.getInt(5)));
                    if (this.jsonGia.has(cursor.getString(0))) {
                        try {
                            this.mGia.add("" + (this.Price + this.jsonGia.getInt(cursor.getString(0))));
                        } catch (JSONException e3) {
                            e3.printStackTrace();
                        }
                    } else {
                        this.mGia.add(this.Price + "");
                    }
                    this.mMax.add(this.myMax);
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        if (getActivity() != null) {
            this.lview.setAdapter((ListAdapter) new So_OmAdapter(getActivity(), R.layout.frag_canchuyen_lv, this.mSo));
        }
    }

    /* access modifiers changed from: package-private */
    public class So_OmAdapter extends ArrayAdapter {
        public So_OmAdapter(Context context, int resource, List<String> objects) {
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

        @SuppressLint("SetTextI18n")
        public View getView(int position, View view, ViewGroup parent) {
            @SuppressLint("WrongConstant") LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
            ViewHolder holder = new ViewHolder();
            if (view == null) {
                view = inflater.inflate(R.layout.frag_canchuyen_lv, (ViewGroup) null);
                holder.tview2 = (TextView) view.findViewById(R.id.stt);
                holder.tview5 = (TextView) view.findViewById(R.id.Tv_so);
                holder.tview7 = (TextView) view.findViewById(R.id.tv_diemNhan);
                holder.tview8 = (TextView) view.findViewById(R.id.tv_diemOm);
                holder.tview1 = (TextView) view.findViewById(R.id.tv_diemChuyen);
                holder.tview4 = (TextView) view.findViewById(R.id.tv_diemTon);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (Frag_Chaytrang.this.mNhay.get(position) > 0) {
                holder.tview5.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tview7.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tview8.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tview1.setTextColor(SupportMenu.CATEGORY_MASK);
                holder.tview4.setTextColor(SupportMenu.CATEGORY_MASK);
                if (Frag_Chaytrang.this.mNhay.get(position) == 1) {
                    TextView textView = holder.tview5;
                    textView.setText(Frag_Chaytrang.this.mSo.get(position) + "*");
                } else if (Frag_Chaytrang.this.mNhay.get(position) == 2) {
                    TextView textView2 = holder.tview5;
                    textView2.setText(Frag_Chaytrang.this.mSo.get(position) + "**");
                } else if (Frag_Chaytrang.this.mNhay.get(position) == 3) {
                    TextView textView3 = holder.tview5;
                    textView3.setText(Frag_Chaytrang.this.mSo.get(position) + "***");
                } else if (Frag_Chaytrang.this.mNhay.get(position).intValue() == 4) {
                    TextView textView4 = holder.tview5;
                    textView4.setText(Frag_Chaytrang.this.mSo.get(position) + "****");
                } else if (Frag_Chaytrang.this.mNhay.get(position).intValue() == 5) {
                    TextView textView5 = holder.tview5;
                    textView5.setText(Frag_Chaytrang.this.mSo.get(position) + "*****");
                } else if (Frag_Chaytrang.this.mNhay.get(position).intValue() == 6) {
                    TextView textView6 = holder.tview5;
                    textView6.setText(Frag_Chaytrang.this.mSo.get(position) + "******");
                }
                TextView textView7 = holder.tview2;
                textView7.setText((position + 1) + "");
                holder.tview7.setText(Frag_Chaytrang.this.mTienTon.get(position));
                holder.tview8.setText("0");
                holder.tview1.setText(Frag_Chaytrang.this.mMax.get(position));
                holder.tview4.setText(Frag_Chaytrang.this.mGia.get(position));
            } else {
                holder.tview5.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tview7.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tview8.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tview1.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                holder.tview4.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                TextView textView8 = holder.tview2;
                textView8.setText((position + 1) + "");
                holder.tview5.setText(Frag_Chaytrang.this.mSo.get(position));
                holder.tview7.setText(Frag_Chaytrang.this.mTienTon.get(position));
                holder.tview8.setText(Frag_Chaytrang.this.mTienchuyen.get(position));
                holder.tview1.setText(Frag_Chaytrang.this.mMax.get(position));
                if (Frag_Chaytrang.this.mGia.size() > 0) {
                    holder.tview4.setText(Frag_Chaytrang.this.mGia.get(position));
                    if (Integer.parseInt(Frag_Chaytrang.this.mGia.get(position)) > Frag_Chaytrang.this.Price) {
                        holder.tview4.setTextColor(SupportMenu.CATEGORY_MASK);
                    }
                }
            }
            return view;
        }
    }

    private void init() {
        this.radio_de = (RadioButton) this.v.findViewById(R.id.radio_de);
        this.radio_lo = (RadioButton) this.v.findViewById(R.id.radio_lo);
        this.radio_xi = (RadioButton) this.v.findViewById(R.id.radio_xi);
        this.radio_dea = (RadioButton) this.v.findViewById(R.id.radio_dea);
        this.radio_deb = (RadioButton) this.v.findViewById(R.id.radio_deb);
        this.radio_dec = (RadioButton) this.v.findViewById(R.id.radio_dec);
        this.radio_ded = (RadioButton) this.v.findViewById(R.id.radio_ded);
        this.radio_xi2 = (RadioButton) this.v.findViewById(R.id.radio_xi2);
        this.radio_xi3 = (RadioButton) this.v.findViewById(R.id.radio_xi3);
        this.radio_xi4 = (RadioButton) this.v.findViewById(R.id.radio_xi4);
        this.spr_trang = (Spinner) this.v.findViewById(R.id.spr_trang);
        this.btn_Xuatso = (Button) this.v.findViewById(R.id.btn_Xuatso);
        this.lview = (ListView) this.v.findViewById(R.id.lview);
        this.li_loaide = (LinearLayout) this.v.findViewById(R.id.li_loaide);
        this.li_loaixi = (LinearLayout) this.v.findViewById(R.id.li_loaixi);
        this.spr_trang = (Spinner) this.v.findViewById(R.id.spr_trang);
        this.btn_MaXuat = (Button) this.v.findViewById(R.id.btn_MaXuat);
        this.edt_tien = (EditText) this.v.findViewById(R.id.edt_tien);
    }
}