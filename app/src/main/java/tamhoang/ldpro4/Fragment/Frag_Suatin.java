package tamhoang.ldpro4.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.internal.cache.DiskLruCache;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.BriteDb;
import tamhoang.ldpro4.data.BusEvent;
import tamhoang.ldpro4.data.Database;

public class Frag_Suatin extends Fragment {
    String CurDate;
    String Cur_date;
    Button btn_LoadTin;
    Button btn_suatin;
    Button btn_tai_All;
    Database db;
    EditText editTsuatin;
    boolean error;
    Handler handler;
    int lv_position = -1;
    ListView lv_suatin;
    public List<String> mApp = new ArrayList();
    public List<String> mContact = new ArrayList();
    public List<Integer> mID = new ArrayList();
    public List<String> mMobile = new ArrayList();
    public List<String> mND_DaSua = new ArrayList();
    public List<String> mND_PhanTich = new ArrayList();
    public List<String> mNgay = new ArrayList();
    public List<String> mPhatHienLoi = new ArrayList();
    public List<String> mSDT = new ArrayList();
    public List<Integer> mSoTinNhan = new ArrayList();
    public List<String> mTenKH = new ArrayList();
    public List<String> mTinNhanGoc = new ArrayList();
    public List<Integer> mTypeKH = new ArrayList();
    public List<Integer> mType_kh = new ArrayList();
    RadioButton radio_SuaTin;
    RadioButton radio_TaiTin;
    private Runnable runnable = new Runnable() {

        public void run() {
            new MainActivity();
            if (MainActivity.sms) {
                xem_lv();
                MainActivity.sms = false;
            }
            handler.postDelayed(this, 1000);
        }
    };
    Spinner sp_TenKH;
    int spin_pointion = -1;
    String str;
    int type_kh;
    View v;
    private Runnable xulyTinnhan = new Runnable() {
        @SuppressLint("WrongConstant")
        public void run() {
            Cursor cur = null;
            Cursor cursor = null;
            error = true;
            Log.e(TAG, "run:ww " +  MainActivity.tenAcc);

            if (editTsuatin.getText().toString().length() < 6) {
                error = false;
            } else if (lv_position < 0 || !Congthuc.CheckDate(MainActivity.hanSuDung)) {
                error = false;
                if (!Congthuc.CheckDate("31/12/2022")) {
                    try {
                        AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
                        bui.setTitle("Thông báo:");
                        bui.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                        bui.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                        bui.create().show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (MainActivity.tenAcc.length() == 0) {
                    Log.e(TAG, "run: " +  MainActivity.tenAcc);
                    AlertDialog.Builder bui2 = new AlertDialog.Builder(getActivity());
                    bui2.setTitle("Thông báo:");
                    bui2.setMessage("Kiểm tra kết nối Internet! 1");
                    bui2.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                    bui2.create().show();
                } else {
                    Add_tin();
                }
            } else {
                db.QueryData("Update tbl_tinnhanS Set nd_phantich = '" + editTsuatin.getText().toString() + "', nd_sua = '" + editTsuatin.getText().toString() + "' WHERE id = " + mID.get(lv_position));
                cur = db.GetData("Select type_kh From tbl_tinnhanS WHERE id = " + mID.get(lv_position));
                cur.moveToFirst();
                try {
                    db.Update_TinNhanGoc(mID.get(lv_position), cur.getInt(0));
                    EventBus.getDefault().post(new BusEvent.SetupErrorBagde(0));
                } catch (Throwable e) {
                    db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + mID.get(lv_position));
                    db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mNgay.get(lv_position) + "' AND so_dienthoai = '" + mSDT.get(lv_position) + "' AND so_tin_nhan = " + mSoTinNhan.get(lv_position) + " AND type_kh = " + cur.getString(0));
                    error = false;
                    Log.e("ContentValues", "Đã xảy ra lỗi 1 " );
                    Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                }
                if (!Congthuc.CheckTime("18:30") && Cur_date.contains(CurDate)) {
                    try {
                        db.Gui_Tin_Nhan(mID.get(lv_position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                cursor = db.GetData("Select * FROM tbl_tinnhanS Where id = " + mID.get(lv_position));
                cursor.moveToFirst();
                if (cursor.getString(11).contains("Không high")) {
                    String str1 = cursor.getString(10).replace("ldpro", "<font color='#FF0000'>");
                    editTsuatin.setText(Html.fromHtml(str1));
                    if (cursor.getString(10).contains("ldpro")) {
                        try {
                            editTsuatin.setSelection(str1.indexOf("<font"));
                        } catch (Exception e3) {
                        }
                    }
                    error = false;
                } else {
                    editTsuatin.setText("");
                    xem_lv();
                    if (mND_DaSua.size() > 0) {
                        lv_position = 0;
                        if (mPhatHienLoi.get(0).contains("Không hiểu")) {
                            editTsuatin.setText(Html.fromHtml(mND_PhanTich.get(0).replace("ldpro", "<font color='#FF0000'>")));
                            int KKK = mND_PhanTich.get(0).indexOf("ldpro");
                            if (KKK > -1) {
                                try {
                                    editTsuatin.setSelection(KKK);
                                } catch (Exception e4) {
                                }
                            }
                            sp_TenKH.setSelection(mContact.indexOf(mTenKH.get(0)));
                            error = false;
                        } else {
                            editTsuatin.setText(mND_DaSua.get(0));
                        }
                    } else {
                        lv_position = -1;
                        error = false;
                    }
                }
            }
            if (cur != null) {
                cur.close();
            }
            if (cursor != null) {
                cursor.close();
            }
            if (!error) {
                handler.removeCallbacks(xulyTinnhan);
            } else {
                handler.postDelayed(this, 300);
            }
        }
    };

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.frag_suatin, container, false);
        this.db = new Database(getActivity());
        this.btn_suatin = (Button) this.v.findViewById(R.id.btn_suatin_suatin);
        this.btn_LoadTin = (Button) this.v.findViewById(R.id.btn_loadtin);
        this.editTsuatin = (EditText) this.v.findViewById(R.id.editText_suatin);
        this.btn_tai_All = (Button) this.v.findViewById(R.id.btn_tai_All);
        this.sp_TenKH = (Spinner) this.v.findViewById(R.id.spr_KH);
        this.radio_SuaTin = (RadioButton) this.v.findViewById(R.id.radio_suaTin);
        this.radio_TaiTin = (RadioButton) this.v.findViewById(R.id.radio_TaiTin);
        this.lv_suatin = (ListView) this.v.findViewById(R.id.lv_suatin);
        Handler handler2 = new Handler();
        this.handler = handler2;
        handler2.postDelayed(this.runnable, 1000);
        new MainActivity();
        final String mDate = MainActivity.Get_date();
        this.btn_suatin.setOnClickListener(view -> {
            CurDate = MainActivity.Get_date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Cur_date = sdf.format(new Date());
            handler = new Handler();
            handler.postDelayed(xulyTinnhan, 300);
        });
        this.lv_suatin.setOnItemClickListener((adapterView, view, i, l) -> {
            lv_position = i;
            editTsuatin.setText(Html.fromHtml(mND_PhanTich.get(i).replace("ldpro", "<font color='#FF0000'>")));
            int KKK = mND_PhanTich.get(i).indexOf("ldpro");
            if (KKK > -1) {
                try {
                    editTsuatin.setSelection(KKK);
                } catch (Exception e) {
                }
            }
            sp_TenKH.setSelection(mContact.indexOf(mTenKH.get(i)));
        });
        this.lv_suatin.setOnItemLongClickListener((adapterView, view, position, id) -> {
            lv_position = position;
            return false;
        });
        this.mContact.clear();
        this.mMobile.clear();
        this.mType_kh.clear();
        this.mApp.clear();
        try {
            Cursor cursor = this.db.GetData("Select * From tbl_kh_new Order by type_kh, ten_kh");
            while (cursor.moveToNext()) {
                this.mContact.add(cursor.getString(0));
                this.mMobile.add(cursor.getString(1));
                this.mApp.add(cursor.getString(2));
                this.mType_kh.add(cursor.getInt(3));
            }
            cursor.close();
            this.sp_TenKH.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.mContact));
            if (this.mContact.size() > 0) {
                this.sp_TenKH.setSelection(0);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Đang copy dữ liệu bản mới!", Toast.LENGTH_SHORT).show();
        }
        this.sp_TenKH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spin_pointion = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        this.radio_SuaTin.setOnCheckedChangeListener((buttonView, isChecked) -> control_RadioButton());
        this.radio_TaiTin.setOnCheckedChangeListener((buttonView, isChecked) -> control_RadioButton());
        this.btn_LoadTin.setOnClickListener(v -> {
            if (spin_pointion <= -1 || mContact.size() <= 0) {
                Toast.makeText(getActivity(), "Chưa có tên khách hàng!", Toast.LENGTH_LONG).show();
            } else if (mApp.get(spin_pointion).contains("sms")) {
                AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
                bui.setTitle("Tải lại tin nhắn khách này?");
                bui.setPositiveButton("YES", (dialog, which) -> {
                    try {
                        getFullSms(mMobile.get(spin_pointion));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    db.QueryData("Update chat_database set del_sms = 1 WHERE ten_kh = '" + mContact.get(spin_pointion) + "' AND ngay_nhan = '" + mDate + "'");
                });
                bui.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                bui.create().show();
            } else {
                AlertDialog.Builder bui2 = new AlertDialog.Builder(getActivity());
                bui2.setTitle("Tải lại tin nhắn khách này?");
                bui2.setPositiveButton("YES", (dialog, which) -> {
                    getAllChat(mType_kh.get(spin_pointion));
                    Database database = db;
                    database.QueryData("Update chat_database set del_sms = 1 WHERE ten_kh = '" + mContact.get(spin_pointion) + "' AND ngay_nhan = '" + mDate + "'");
                });
                bui2.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                bui2.create().show();
            }
        });
        this.btn_tai_All.setOnClickListener(v -> {
            AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
            bui.setTitle("Tải lại tin nhắn của tất cả khách?");
            bui.setPositiveButton("YES", (dialog, which) -> {
                try {
                    getFullSms("Full");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                db.QueryData("Update chat_database set del_sms = 1 WHERE ngay_nhan = '" + mDate + "'");
            });
            bui.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            bui.create().show();
        });
        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.READ_SMS") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), "android.permission.READ_SMS")) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, 1);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, 1);
            }
        }
        control_RadioButton();
        registerForContextMenu(this.lv_suatin);
        return this.v;
    }

    private void Add_tin() {
        final MainActivity activity = new MainActivity();
        if (this.mContact.size() > 0 && this.editTsuatin.getText().toString().length() > 6) {
            new MainActivity();
            final String mDate = MainActivity.Get_date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
            hourFormat.setTimeZone(TimeZone.getDefault());
            final String mGionhan = hourFormat.format(calendar.getTime());
            String str2 = "Select * From tbl_tinnhanS WHERE nd_goc = '" + this.editTsuatin.getText().toString().replaceAll("'", "").trim() + "' AND ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "'";
            this.str = str2;
            Cursor Ktratin = this.db.GetData(str2);
            Ktratin.moveToFirst();
            final Cursor cur1 = this.db.GetData("Select * From tbl_kh_new Where sdt = '" + this.mMobile.get(this.spin_pointion) + "'");
            cur1.moveToFirst();
            if (this.spin_pointion <= -1 || Ktratin.getCount() != 0) {
                if (Ktratin.getCount() > 0) {
                    Toast.makeText(getActivity(), "Đã có tin này trong CSDL!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Hãy chọn tên khách hàng", Toast.LENGTH_LONG).show();
                }
            } else if (cur1.getInt(3) == 3) {
                AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
                bui.setTitle("Chọn loại tin nhắn:");
                bui.setMessage("Đây là khách vừa nhận vừa chuyển, thêm tin nhận hay tin chuyển?");
                bui.setNeutralButton("Tin nhận", new DialogInterface.OnClickListener() {
                    /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass9 */

                    public void onClick(DialogInterface dialog, int which) {
                        type_kh = 1;
                        int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mDate, type_kh, "so_dienthoai = '"+ mMobile.get(spin_pointion) +"'");

                        db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mDate + "', '" + mGionhan + "', " + type_kh + ", '" + mContact.get(spin_pointion) + "', '" + mMobile.get(spin_pointion) + "', '" + cur1.getString(2) + "', " + (maxSoTn + 1) + ", '" + editTsuatin.getText().toString().replace("'", " ").trim() + "', '" + editTsuatin.getText().toString().replace("'", " ").trim() + "', '" + editTsuatin.getText().toString().replace("'", " ").trim() + "', 'ko',0, 0, 0, null)");
                        editTsuatin.setText("");
                        Database database = db;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Select id From tbl_tinnhanS WHERE ngay_nhan = '");
                        sb.append(mDate);
                        sb.append("' AND so_dienthoai = '");
                        sb.append(mMobile.get(spin_pointion));
                        sb.append("' AND so_tin_nhan = ");
                        sb.append(maxSoTn + 1);
                        sb.append(" AND type_kh = ");
                        sb.append(type_kh);
                        Cursor c = database.GetData(sb.toString());
                        c.moveToFirst();
                        if (Congthuc.CheckDate(MainActivity.hanSuDung)) {
                            try {
                                db.Update_TinNhanGoc(c.getInt(0), cur1.getInt(3));
                            } catch (Exception e) {
                                Database database2 = db;
                                database2.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                                Frag_Suatin frag_Suatin = Frag_Suatin.this;
                                frag_Suatin.str = "Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + mMobile.get(spin_pointion) + "' AND so_tin_nhan = " + (maxSoTn + 1) + " AND type_kh = " + type_kh;
                                db.QueryData(str);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } else if (MainActivity.tenAcc.length() == 0) {
                            AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
                            bui.setTitle("Thông báo:");
                            bui.setMessage("Kiểm tra kết nối Internet! 2");
                            bui.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                                /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass9.AnonymousClass1 */

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            bui.create().show();
                        } else {
                            try {
                                AlertDialog.Builder bui2 = new AlertDialog.Builder(getActivity());
                                bui2.setTitle("Thông báo:");
                                bui2.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                                bui2.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                                    /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass9.AnonymousClass2 */

                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                bui2.create().show();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                        cur1.close();
                        c.close();
                        dialog.cancel();
                        MainActivity.sms = true;
                    }
                });
                bui.setPositiveButton("Tin Chuyển", (dialog, which) -> {
                    type_kh = 2;
                    int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mDate, type_kh, "so_dienthoai = '"+ mMobile.get(spin_pointion) +"'");
                    db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mDate + "', '" + mGionhan + "', " + type_kh + ", '" + mContact.get(spin_pointion) + "', '" + mMobile.get(spin_pointion) + "', '" + cur1.getString(2) + "', " + (maxSoTn + 1) + ", '" + editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', '" + editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', '" + editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', 'ko',0, 0, 0, null)");
                    editTsuatin.setText("");
                    Database database = db;
                    String sb = "Select id From tbl_tinnhanS WHERE ngay_nhan = '" + mDate +
                            "' AND so_dienthoai = '" + mMobile.get(spin_pointion) +
                            "' AND so_tin_nhan = " + (maxSoTn + 1) +
                            " AND type_kh = " + type_kh;
                    Cursor c = database.GetData(sb);
                    c.moveToFirst();
                    if (Congthuc.CheckDate(MainActivity.hanSuDung)) {
                        try {
                            db.Update_TinNhanGoc(c.getInt(0), cur1.getInt(3));
                        } catch (Exception e) {
                            Log.e("aaa", "onClick: error " + e.getMessage() );
                            db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                            str = "Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + mMobile.get(spin_pointion) + "' AND so_tin_nhan = " + (maxSoTn + 1) + " AND type_kh = " + type_kh;
                            db.QueryData(str);
                            Log.e("ContentValues", "Đã xảy ra lỗi! 2 " );

                            Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    } else if (MainActivity.tenAcc.length() == 0) {
                        AlertDialog.Builder bui1 = new AlertDialog.Builder(getActivity());
                        bui1.setTitle("Thông báo:");
                        bui1.setMessage("Kiểm tra kết nối Internet! 3");
                        bui1.setNegativeButton("Đóng", (dialog1, which1) -> dialog1.cancel());
                        bui1.create().show();
                    } else {
                        try {
                            AlertDialog.Builder bui2 = new AlertDialog.Builder(getActivity());
                            bui2.setTitle("Thông báo:");
                            bui2.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                            bui2.setNegativeButton("Đóng", (dialog12, which12) -> dialog12.cancel());
                            bui2.create().show();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                    cur1.close();
                    c.close();
                    dialog.cancel();
                    MainActivity.sms = true;
                });
                bui.create().show();
            } else {
                this.type_kh = cur1.getInt(3);
                int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mDate, type_kh, "so_dienthoai = '"+ mMobile.get(this.spin_pointion) +"'");

                this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mDate + "', '" + mGionhan + "', " + this.type_kh + ", '" + this.mContact.get(this.spin_pointion) + "', '" + this.mMobile.get(this.spin_pointion) + "', '" + cur1.getString(2) + "', " + (maxSoTn + 1) + ", '" + this.editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', '" + this.editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', '" + this.editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', 'ko',0, 0, 0, null)");
                this.editTsuatin.setText("");
                Database database = this.db;
                String sb = "Select id From tbl_tinnhanS WHERE ngay_nhan = '" + mDate +
                        "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) +
                        "' AND so_tin_nhan = " + (maxSoTn + 1) +
                        " AND type_kh = " + this.type_kh;
                Cursor c = database.GetData(sb);
                c.moveToFirst();
                if (Congthuc.CheckDate(MainActivity.hanSuDung)) {
                    try {
                        this.db.Update_TinNhanGoc(c.getInt(0), cur1.getInt(3));
                    } catch (Exception e) {
                        Log.e("ContentValues", "onClick: error " + e.getMessage() );

                        this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                        this.str = "Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "' AND so_tin_nhan = " + (maxSoTn + 1) + " AND type_kh = " + this.type_kh;;
                        this.db.QueryData(str);
                        Log.e("ContentValues", "Đã xảy ra lỗi!  3" );
                        Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                } else if (MainActivity.tenAcc.length() == 0) {
                    AlertDialog.Builder bui2 = new AlertDialog.Builder(getActivity());
                    bui2.setTitle("Thông báo:");
                    bui2.setMessage("Kiểm tra kết nối Internet! 4");
                    bui2.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                    bui2.create().show();
                } else {
                    try {
                        AlertDialog.Builder bui3 = new AlertDialog.Builder(getActivity());
                        bui3.setTitle("Thông báo:");
                        bui3.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                        bui3.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                        bui3.create().show();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
                MainActivity.sms = true;
                cur1.close();
                c.close();
            }
            xem_lv();
            if (Ktratin != null) {
                Ktratin.close();
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.runnable);
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateContextMenu(ContextMenu menu, View v2, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v2, menuInfo);
        menu.add(0, 1, 0, "Xóa tin này?");
    }

    @Override // android.support.v4.app.Fragment
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        if (item.getItemId() == 1) {
            if (this.lv_position >= 0) {
                Database database = this.db;
                database.QueryData("Delete FROM tbl_tinnhanS WHERE id = " + this.mID.get(this.lv_position));
                this.lv_position = -1;
                xem_lv();
                Toast.makeText(getActivity(), "Xoá thành công", Toast.LENGTH_LONG).show();
                this.editTsuatin.setText("");
            }
            xem_lv();
            if (this.mND_DaSua.size() > 0) {
                this.lv_position = 0;
                if (this.mPhatHienLoi.get(0).contains("Không hiểu")) {
                    this.editTsuatin.setText(Html.fromHtml(this.mND_PhanTich.get(0).replace("ldpro", "<font color='#FF0000'>")));
                    int KKK = this.mND_PhanTich.get(0).indexOf("ldpro");
                    if (KKK > -1) {
                        try {
                            this.editTsuatin.setSelection(KKK);
                        } catch (Exception e) {
                        }
                    }
                    this.sp_TenKH.setSelection(this.mContact.indexOf(this.mTenKH.get(0)));
                    this.error = false;
                } else {
                    this.editTsuatin.setText(this.mND_DaSua.get(0));
                }
            } else {
                this.lv_position = -1;
                this.error = false;
            }
        }
        if (item.getItemId() == 2) {
            if (this.lv_position >= 0) {
                new MainActivity();
                String mDate = MainActivity.Get_date();
                Database database2 = this.db;
                database2.QueryData("Delete FROM tbl_tinnhanS WHERE phat_hien_loi <> 'ok' And ngay_nhan = '" + mDate + "'");
                this.lv_position = -1;
                xem_lv();
                Toast.makeText(getActivity(), "Xoá thành công", Toast.LENGTH_LONG).show();
                this.editTsuatin.setText("");
            }
            xem_lv();
        }
        return true;
    }

    public void control_RadioButton() {
        LinearLayout li_KhachHang = this.v.findViewById(R.id.li_KhachHang);
        LinearLayout li_Button = this.v.findViewById(R.id.li_button);
        LinearLayout li_edittinnhan = this.v.findViewById(R.id.edittinnhan);
        if (this.radio_SuaTin.isChecked()) {
            li_edittinnhan.setVisibility(View.VISIBLE);
            li_KhachHang.setVisibility(View.VISIBLE);
            li_Button.setVisibility(View.VISIBLE);
            this.btn_LoadTin.setVisibility(View.GONE);
            this.btn_suatin.setVisibility(View.VISIBLE);
            this.editTsuatin.setVisibility(View.VISIBLE);
            this.btn_tai_All.setVisibility(View.GONE);
            xem_lv();
        } else if (this.radio_TaiTin.isChecked()) {
            li_edittinnhan.setVisibility(View.VISIBLE);
            li_KhachHang.setVisibility(View.VISIBLE);
            li_Button.setVisibility(View.VISIBLE);
            this.btn_suatin.setVisibility(View.GONE);
            this.btn_LoadTin.setVisibility(View.VISIBLE);
            this.editTsuatin.setVisibility(View.GONE);
            this.btn_tai_All.setVisibility(View.VISIBLE);
            xem_lv();
        }
    }

    public void getAllChat(int Type_kh) {
        String sql;
        new MainActivity();
        String mDate = MainActivity.Get_date();
        int soTN = 0;
        this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "'");
        this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "'");
        Database database = this.db;
        String sb = "Select * From tbl_kh_new Where sdt = '" + this.mMobile.get(this.spin_pointion) + "'";
        Cursor cur1 = database.GetData(sb);
        cur1.moveToFirst();
        int i = 3;
        if (Type_kh == 1) {
            sql = "Select * From Chat_database Where ngay_nhan = '" + mDate + "' AND ten_kh = '" + this.mContact.get(this.spin_pointion) + "' and type_kh = 1";
        } else if (Type_kh == 2) {
            sql = "Select * From Chat_database Where ngay_nhan = '" + mDate + "' AND ten_kh = '" + this.mContact.get(this.spin_pointion) + "' and type_kh = 2";
        } else if (Type_kh == 3) {
            sql = "Select * From Chat_database Where ngay_nhan = '" + mDate + "' AND ten_kh = '" + this.mContact.get(this.spin_pointion) + "'";
        } else {
            sql = null;
        }
        Cursor curSQL = this.db.GetData(sql);
        SQLiteDatabase database2 = this.db.getWritableDatabase();
        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
        database2.beginTransaction();
        try {
            if (curSQL.getCount() > 0) {
                while (curSQL.moveToNext()) {
                    soTN++;
                    ih.prepareForInsert();
                    ih.bind(ih.getColumnIndex("ngay_nhan"), mDate);
                    ih.bind(ih.getColumnIndex("gio_nhan"), curSQL.getString(2));
                    ih.bind(ih.getColumnIndex("type_kh"), curSQL.getString(i));
                    ih.bind(ih.getColumnIndex("ten_kh"), this.mContact.get(this.spin_pointion));
                    ih.bind(ih.getColumnIndex("so_dienthoai"), this.mMobile.get(this.spin_pointion));
                    ih.bind(ih.getColumnIndex("use_app"), cur1.getInt(2));
                    ih.bind(ih.getColumnIndex("so_tin_nhan"), soTN);
                    ih.bind(ih.getColumnIndex("nd_goc"), curSQL.getString(7));
                    ih.bind(ih.getColumnIndex("nd_sua"), curSQL.getString(7));
                    ih.bind(ih.getColumnIndex("nd_phantich"), curSQL.getString(7));
                    ih.bind(ih.getColumnIndex("phat_hien_loi"), "ko");
                    ih.bind(ih.getColumnIndex("tinh_tien"), 0);
                    ih.bind(ih.getColumnIndex("ok_tn"), 0);
                    ih.bind(ih.getColumnIndex("del_sms"), 0);
                    ih.execute();
                    i = 3;
                }
            }
            curSQL.close();
            database2.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            database2.endTransaction();
            ih.close();
            database2.close();
            throw th;
        }
        database2.endTransaction();
        ih.close();
        database2.close();
        this.db.QueryData("Delete From tbl_tinnhanS where substr(nd_goc,0,7) = 'Ok Tin'");
        this.db.QueryData("Delete From tbl_tinnhanS where length(nd_goc) < 4");
        xem_lv();
        Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
        cur1.close();
    }

    public void getFullSms(String str) throws ParseException {
        Database database;
        String sb;
        DatabaseUtils.InsertHelper insertHelper;
        DatabaseUtils.InsertHelper insertHelper3;

        String ngay_nhan;
        boolean z;
        SQLiteDatabase sQLiteDatabase5;

        Cursor GetData;
        SQLiteDatabase writableDatabase;
        SQLiteDatabase sQLiteDatabase6;
        String str15;
        String str17;
        String str18;
        boolean z2;
        boolean z3;
        String str20 = " ";
        String str21 = "";
        new MainActivity();
        String Get_ngay = MainActivity.Get_ngay();
        String Get_date = MainActivity.Get_date();
        String tin_trung = "tin_trung";
        String str23 = "'";
        if (!MainActivity.jSon_Setting.has("tin_trung")) {
            try {
                MainActivity.jSon_Setting.put("tin_trung", 0);
                this.db.QueryData("Update tbl_Setting set Setting = '" + MainActivity.jSon_Setting.toString() + "'");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.READ_SMS") != 0) {
            return;
        }
        try {
            Date parse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(Get_date + "T00:00:00");
            String str24 = "date>=" + parse.getTime();
            if (str.contains("Full")) {
                this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + Get_date + "'");
                this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + Get_date + "'");
                database = this.db;
                sb = "Select * From tbl_kh_new";
            } else {
                this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + Get_date + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "'");
                this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + Get_date + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "'");
                database = this.db;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Select * From tbl_kh_new Where sdt = '");
                sb2.append(this.mMobile.get(this.spin_pointion));
                sb2.append("'");
                sb = sb2.toString();
            }
            Cursor GetData2 = database.GetData(sb);
            JSONObject jSONObject = new JSONObject();
            while (GetData2.moveToNext()) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("type_kh", GetData2.getString(3));
                jSONObject2.put("ten_kh", GetData2.getString(0));
                jSONObject2.put("so_tn", 0);
                jSONObject.put(GetData2.getString(1), jSONObject2);
            }
            String type_kh = "type_kh";
            Cursor query = getActivity().getContentResolver().query(Uri.parse("content://sms"), null, str24, null, "date ASC");
            getActivity().startManagingCursor(query);
            int count = query.getCount();
            SQLiteDatabase writableDatabase2 = this.db.getWritableDatabase();
            insertHelper = new DatabaseUtils.InsertHelper(writableDatabase2, "tbl_tinnhanS");
            boolean moveToFirst = query.moveToFirst();
            ngay_nhan = "ngay_nhan";
            String so_dienthoai = "so_dienthoai";
            String nd_sua = "nd_sua";
            String nd_goc = "nd_goc";
            String so_tin_nhan = "so_tin_nhan";
            String sms = "sms";
            String use_app = "use_app";
            String ten_kh = "ten_kh";
            String str34 = DiskLruCache.VERSION_1;
            String gio_nhan = "gio_nhan";
            String ok_tin = "Ok Tin";
            String str38 = "3";
            if (moveToFirst) {
                try {
                    writableDatabase2.beginTransaction();
                    int i = 0;
                    while (i < count) {
                        int i2 = count;
                        try {
                            try {
                                Long valueOf = query.getLong(query.getColumnIndexOrThrow("date"));
                                int i3 = i;
                                StringBuilder sb3 = new StringBuilder();
                                try {
                                    try {
                                        try {
                                            sb3.append((Object) DateFormat.format("dd/MM/yyyy HH:mm:ss", new Date(valueOf.longValue())));
                                            sb3.append(str21);
                                            String sb4 = sb3.toString();
                                            String str41 = ((Object) DateFormat.format("HH:mm:ss", new Date(valueOf.longValue()))) + str21;
                                            str34 = query.getString(query.getColumnIndexOrThrow("address")).replaceAll(str20, str21);
                                            String replaceAll = query.getString(query.getColumnIndexOrThrow("body")).toString().replaceAll(str23, str20).replaceAll("\"", str20);
                                            String string = query.getString(query.getColumnIndexOrThrow("type"));
                                            String str42 = str20;
                                            String str43 = str21;
                                            if (str34.length() < 12) {
                                                str34 = "+84" + str34.substring(1);
                                            }
                                            if (jSONObject.has(str34) && sb4.contains(Get_ngay) && !replaceAll.contains("Ok Tin")) {
                                                JSONObject jSONObject3 = jSONObject.getJSONObject(str34);
                                                writableDatabase2.endTransaction();
                                                insertHelper.close();
                                                writableDatabase = this.db.getWritableDatabase();
                                                insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                                                try {
                                                    writableDatabase.beginTransaction();
                                                    sQLiteDatabase6 = writableDatabase;
                                                    sQLiteDatabase6.setTransactionSuccessful();
                                                    sQLiteDatabase6.endTransaction();
                                                } catch (Exception e3) {
                                                    sQLiteDatabase6 = writableDatabase;
                                                } catch (Throwable th) {
                                                    sQLiteDatabase5 = writableDatabase;
                                                    sQLiteDatabase5.endTransaction();
                                                    insertHelper3.close();
                                                    sQLiteDatabase5.close();
                                                    throw th;
                                                }
                                                insertHelper3.close();
                                                sQLiteDatabase6.close();
                                                xem_lv();
                                                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                                try {
                                                    if (!jSONObject3.getString(type_kh).contains("3")) {
                                                        if (jSONObject3.getString(type_kh).contains("2")) {
                                                            writableDatabase2.endTransaction();
                                                            insertHelper.close();
                                                            writableDatabase = this.db.getWritableDatabase();
                                                            insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                                                            writableDatabase.beginTransaction();
                                                            sQLiteDatabase6 = writableDatabase;
                                                            sQLiteDatabase6.setTransactionSuccessful();
                                                            sQLiteDatabase6.endTransaction();
                                                            insertHelper3.close();
                                                            sQLiteDatabase6.close();
                                                            xem_lv();
                                                            Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                                        }
                                                        z = jSONObject3.getString(type_kh).contains(str34) && string.contains(str34);
                                                        if (!((MainActivity.jSon_Setting.getInt(tin_trung) == 1 || !jSONObject3.has(replaceAll)) && z)) {
                                                            jSONObject3.put("so_tn", jSONObject3.getInt("so_tn") + 1);
                                                            jSONObject3.put(replaceAll, replaceAll);
                                                            insertHelper.prepareForInsert();

                                                            try {
                                                                try {
                                                                    try {
                                                                        insertHelper.bind(insertHelper.getColumnIndex(ngay_nhan), Get_date);
                                                                    } catch (Exception e5) {
                                                                        writableDatabase2.endTransaction();
                                                                        insertHelper.close();
                                                                        writableDatabase = this.db.getWritableDatabase();
                                                                        insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                                                                        writableDatabase.beginTransaction();
                                                                        sQLiteDatabase6 = writableDatabase;
                                                                        sQLiteDatabase6.setTransactionSuccessful();
                                                                        sQLiteDatabase6.endTransaction();
                                                                        insertHelper3.close();
                                                                        sQLiteDatabase6.close();
                                                                        xem_lv();
                                                                        Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                                                    }
                                                                    try {
                                                                        insertHelper.bind(insertHelper.getColumnIndex(gio_nhan), str41);
                                                                        insertHelper.bind(insertHelper.getColumnIndex(type_kh), string);

                                                                        try {
                                                                            insertHelper.bind(insertHelper.getColumnIndex(ten_kh), jSONObject3.getString(ten_kh));
                                                                            String str45 = so_dienthoai;
                                                                            try {
                                                                                insertHelper.bind(insertHelper.getColumnIndex(str45), str34);
                                                                                so_dienthoai = str45;
                                                                                String str46 = use_app;
                                                                                try {
                                                                                    int columnIndex = insertHelper.getColumnIndex(str46);
                                                                                    use_app = str46;
                                                                                    String str47 = sms;
                                                                                    try {
                                                                                        insertHelper.bind(columnIndex, str47);
                                                                                        sms = str47;
                                                                                        String str48 = so_tin_nhan;
                                                                                        try {
                                                                                            so_tin_nhan = str48;
                                                                                            insertHelper.bind(insertHelper.getColumnIndex(str48), jSONObject3.getInt("so_tn"));
                                                                                            String str49 = nd_goc;
                                                                                            try {
                                                                                                insertHelper.bind(insertHelper.getColumnIndex(str49), replaceAll);
                                                                                                nd_goc = str49;

                                                                                                try {
                                                                                                    insertHelper.bind(insertHelper.getColumnIndex(nd_sua), replaceAll);
                                                                                                    insertHelper.bind(insertHelper.getColumnIndex("nd_phantich"), replaceAll);
                                                                                                    insertHelper.bind(insertHelper.getColumnIndex("phat_hien_loi"), "ko");
                                                                                                    insertHelper.bind(insertHelper.getColumnIndex("tinh_tien"), 0);
                                                                                                    insertHelper.bind(insertHelper.getColumnIndex("ok_tn"), 0);
                                                                                                    insertHelper.bind(insertHelper.getColumnIndex("del_sms"), 0);
                                                                                                    insertHelper.execute();
                                                                                                    jSONObject.put(str34, jSONObject3);
                                                                                                } catch (Exception e6) {
                                                                                                    writableDatabase2.endTransaction();

                                                                                                    insertHelper.close();

                                                                                                    writableDatabase = this.db.getWritableDatabase();
                                                                                                    insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                                                                                                    writableDatabase.beginTransaction();
                                                                                                    sQLiteDatabase6 = writableDatabase;
                                                                                                    sQLiteDatabase6.setTransactionSuccessful();
                                                                                                    sQLiteDatabase6.endTransaction();
                                                                                                    insertHelper3.close();
                                                                                                    sQLiteDatabase6.close();
                                                                                                    xem_lv();
                                                                                                    Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                                                                                }
                                                                                            } catch (Exception e7) {
                                                                                                nd_goc = str49;

                                                                                                writableDatabase2.endTransaction();

                                                                                                insertHelper.close();

                                                                                                writableDatabase = this.db.getWritableDatabase();
                                                                                                insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                                                                                                writableDatabase.beginTransaction();
                                                                                                sQLiteDatabase6 = writableDatabase;
                                                                                                sQLiteDatabase6.setTransactionSuccessful();
                                                                                                sQLiteDatabase6.endTransaction();
                                                                                                insertHelper3.close();
                                                                                                sQLiteDatabase6.close();
                                                                                                xem_lv();
                                                                                                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                                                                            }
                                                                                        } catch (Exception e8) {
                                                                                            so_tin_nhan = str48;
                                                                                        }
                                                                                    } catch (Exception e9) {
                                                                                        sms = str47;
                                                                                    }
                                                                                } catch (Exception e10) {
                                                                                    use_app = str46;
                                                                                }
                                                                            } catch (Exception e11) {
                                                                                so_dienthoai = str45;
                                                                            }
                                                                        } catch (Exception e12) {
                                                                        }
                                                                    } catch (Exception e13) {


                                                                        writableDatabase2.endTransaction();

                                                                        insertHelper.close();

                                                                        writableDatabase = this.db.getWritableDatabase();
                                                                        insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                                                                        writableDatabase.beginTransaction();
                                                                        sQLiteDatabase6 = writableDatabase;
                                                                        sQLiteDatabase6.setTransactionSuccessful();
                                                                        sQLiteDatabase6.endTransaction();
                                                                        insertHelper3.close();
                                                                        sQLiteDatabase6.close();
                                                                        xem_lv();
                                                                        Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                                                    }
                                                                } catch (Throwable th2) {
                                                                    insertHelper.close();
                                                                }
                                                            } catch (Exception e14) {




                                                                writableDatabase2.endTransaction();

                                                                insertHelper.close();

                                                                writableDatabase = this.db.getWritableDatabase();
                                                                insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                                                                writableDatabase.beginTransaction();
                                                                sQLiteDatabase6 = writableDatabase;
                                                                sQLiteDatabase6.setTransactionSuccessful();
                                                                sQLiteDatabase6.endTransaction();
                                                                insertHelper3.close();
                                                                sQLiteDatabase6.close();
                                                                xem_lv();
                                                                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e15) {




                                                    writableDatabase2.endTransaction();
                                                    insertHelper.close();

                                                    writableDatabase = this.db.getWritableDatabase();
                                                    insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                                                    writableDatabase.beginTransaction();
                                                    sQLiteDatabase6 = writableDatabase;
                                                    sQLiteDatabase6.setTransactionSuccessful();
                                                    sQLiteDatabase6.endTransaction();
                                                    insertHelper3.close();
                                                    sQLiteDatabase6.close();
                                                    xem_lv();
                                                    Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                                }
                                                z = true;
                                            }
                                            query.moveToNext();
                                            count = i2;
                                            str20 = str42;
                                            i = i3 + 1;
                                            str21 = str43;
                                        } catch (Throwable th3) {
                                            insertHelper.close();
                                        }
                                    } catch (Exception e16) {
                                    }
                                } catch (Exception e17) {
                                    writableDatabase2.endTransaction();
                                    insertHelper.close();
                                    writableDatabase = this.db.getWritableDatabase();
                                    insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                                    writableDatabase.beginTransaction();
                                    sQLiteDatabase6 = writableDatabase;
                                    sQLiteDatabase6.setTransactionSuccessful();
                                    sQLiteDatabase6.endTransaction();
                                    insertHelper3.close();
                                    sQLiteDatabase6.close();
                                    xem_lv();
                                    Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                }
                            } catch (Throwable th4) {
                            }
                        } catch (Exception e18) {
                            writableDatabase2.endTransaction();
                            insertHelper.close();
                            writableDatabase = this.db.getWritableDatabase();
                            insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                            writableDatabase.beginTransaction();
                            sQLiteDatabase6 = writableDatabase;
                            sQLiteDatabase6.setTransactionSuccessful();
                            sQLiteDatabase6.endTransaction();
                            insertHelper3.close();
                            sQLiteDatabase6.close();
                            xem_lv();
                            Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                        }
                    }
                    writableDatabase2.setTransactionSuccessful();
                    writableDatabase2.endTransaction();

                } catch (Exception e19) {
                } catch (Throwable th5) {
                }
                insertHelper.close();

            } else {
                str34 = DiskLruCache.VERSION_1;
            }
            try {
                GetData = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + Get_date + "' And use_app <> 'sms'");
                writableDatabase = this.db.getWritableDatabase();
                insertHelper3 = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
                writableDatabase.beginTransaction();
                while (GetData.moveToNext()) {
                    String string2 = GetData.getString(1);
                    String string3 = GetData.getString(2);
                    sQLiteDatabase6 = writableDatabase;
                    try {
                        String string4 = GetData.getString(4);
                        String string5 = GetData.getString(7);
                        String string6 = GetData.getString(3);
                        if (jSONObject.has(string4) && string2.contains(Get_date) && !string5.contains(ok_tin)) {
                            JSONObject jSONObject4 = jSONObject.getJSONObject(string4);
                            if (!jSONObject4.getString(type_kh).contains(str38) && (!jSONObject4.getString(type_kh).contains("2") || !string6.contains("2"))) {
                                z2 = jSONObject4.getString(type_kh).contains(str34) && string6.contains(str34);
                                z3 = z2;
                                if (!((MainActivity.jSon_Setting.getInt(tin_trung) == 1 || !jSONObject4.has(string5)) && z3)) {
                                    jSONObject4.put("so_tn", jSONObject4.getInt("so_tn") + 1);
                                    jSONObject4.put(string5, string5);
                                    insertHelper3.prepareForInsert();
                                    insertHelper3.bind(insertHelper3.getColumnIndex(ngay_nhan), Get_date);
                                    insertHelper3.bind(insertHelper3.getColumnIndex(gio_nhan), string3);
                                    insertHelper3.bind(insertHelper3.getColumnIndex(type_kh), string6);
                                    insertHelper3.bind(insertHelper3.getColumnIndex(ten_kh), jSONObject4.getString(ten_kh));
                                    str18 = so_dienthoai;
                                    insertHelper3.bind(insertHelper3.getColumnIndex(str18), string4);
                                    str15 = use_app;
                                    String str52 = sms;
                                    insertHelper3.bind(insertHelper3.getColumnIndex(str15), str52);
                                    sms = str52;
                                    String str53 = so_tin_nhan;
                                    so_tin_nhan = str53;
                                    insertHelper3.bind(insertHelper3.getColumnIndex(str53), jSONObject4.getInt("so_tn"));
                                    String str54 = nd_goc;
                                    insertHelper3.bind(insertHelper3.getColumnIndex(str54), string5);
                                    nd_goc = str54;
                                    insertHelper3.bind(insertHelper3.getColumnIndex(nd_sua), string5);
                                    insertHelper3.bind(insertHelper3.getColumnIndex("nd_phantich"), string5);
                                    insertHelper3.bind(insertHelper3.getColumnIndex("phat_hien_loi"), "ko");
                                    insertHelper3.bind(insertHelper3.getColumnIndex("tinh_tien"), 0);
                                    insertHelper3.bind(insertHelper3.getColumnIndex("ok_tn"), 0);
                                    insertHelper3.bind(insertHelper3.getColumnIndex("del_sms"), 0);
                                    insertHelper3.execute();
                                    jSONObject.put(string4, jSONObject4);
                                }
                            }
                            str17 = str38;
                        } else {
                            str17 = str38;
                        }
                        writableDatabase = sQLiteDatabase6;
                        str38 = str17;
                    } catch (Exception e20) {
                        sQLiteDatabase6.endTransaction();
                        insertHelper3.close();
                        sQLiteDatabase6.close();
                        xem_lv();
                        Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                    }
                }
                sQLiteDatabase6 = writableDatabase;
                sQLiteDatabase6.setTransactionSuccessful();
                sQLiteDatabase6.endTransaction();
                insertHelper3.close();
                sQLiteDatabase6.close();
                xem_lv();
                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
            } catch (Throwable th6) {
            }
        } catch (SQLiteException unused) {
        } catch (JSONException e21) {
            e21.printStackTrace();
        }
    }

    public void xem_lv() {
        new MainActivity();
        String mDate = MainActivity.Get_date();
        this.mID.clear();
        this.mNgay.clear();
        this.mSDT.clear();
        this.mTenKH.clear();
        this.mSoTinNhan.clear();
        this.mTinNhanGoc.clear();
        this.mND_DaSua.clear();
        this.mND_PhanTich.clear();
        this.mPhatHienLoi.clear();
        this.mTypeKH.clear();
        Database database = this.db;
        Cursor cursor = database.GetData("select * from tbl_tinnhanS WHERE phat_hien_loi <> 'ok' AND ngay_nhan = '" + mDate + "'");
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                this.mID.add(Integer.valueOf(cursor.getInt(0)));
                this.mNgay.add(cursor.getString(1));
                this.mTenKH.add(cursor.getString(4));
                this.mSDT.add(cursor.getString(5));
                this.mSoTinNhan.add(Integer.valueOf(Integer.parseInt(cursor.getString(7))));
                this.mTinNhanGoc.add(cursor.getString(8));
                this.mND_DaSua.add(cursor.getString(9));
                this.mND_PhanTich.add(cursor.getString(10));
                this.mPhatHienLoi.add(cursor.getString(11));
                this.mTypeKH.add(Integer.valueOf(cursor.getInt(3)));
            }
            cursor.close();
        }
        if (getActivity() != null) {
            this.lv_suatin.setAdapter(new TNGAdapter(getActivity(), R.layout.frag_suatin_lv, this.mTinNhanGoc));
        }
    }

    public class TNGAdapter extends ArrayAdapter {
        public TNGAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        class ViewHolder {
            TextView tv_suatin_nd;
            TextView tv_suatin_err;
            ViewHolder() {}
        }
        @SuppressLint("WrongConstant")
        public View getView(int position, View mView, ViewGroup parent) {
            ViewHolder holder;
            if (mView == null) {
                mView = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.frag_suatin_lv, null);
                holder = new ViewHolder();
                holder.tv_suatin_nd = mView.findViewById(R.id.tv_suatin_nd);
                holder.tv_suatin_err = mView.findViewById(R.id.tv_suatin_err);
                mView.setTag(holder);
            } else {
                holder = (ViewHolder) mView.getTag();
            }
            holder.tv_suatin_nd.setText(mTinNhanGoc.get(position));
            holder.tv_suatin_err.setText(mPhatHienLoi.get(position));
            return mView;
        }
    }
}