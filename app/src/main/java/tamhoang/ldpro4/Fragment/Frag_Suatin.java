package tamhoang.ldpro4.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import okhttp3.internal.cache.DiskLruCache;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.BriteDb;
import tamhoang.ldpro4.data.BusEvent;
import tamhoang.ldpro4.data.Database;
import tamhoang.ldpro4.data.model.Chat;
import tamhoang.ldpro4.data.model.KhachHang;
import tamhoang.ldpro4.data.model.TinNhanS;

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
    TNGAdapter tngAdapter;

    public List<KhachHang> mKhachHang = new ArrayList<>();
    public List<String> mNameKhach = new ArrayList<>();

    public List<TinNhanS> mTinNhanS = new ArrayList();

    TinNhanS tinNhanS() {
        return mTinNhanS.size() > lv_position ? mTinNhanS.get(lv_position) : null;
    }

    RadioButton radio_SuaTin;
    RadioButton radio_TaiTin;
    private final Runnable runnable = new Runnable() {

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
    KhachHang khachHang;
    int type_kh;
    View v;
    private final Runnable xulyTinnhan = new Runnable() {
        @SuppressLint("WrongConstant")
        public void run() {
            error = true;
            Log.e(TAG, "run:ww " + MainActivity.tenAcc);

            if (editTsuatin.getText().toString().length() < 6) {
                error = false;
            } else if (lv_position < 0 || !Congthuc.CheckDate(MainActivity.hanSuDung)) {
                error = false;
                if (!Congthuc.CheckDate("31/12/2022") || MainActivity.tenAcc.length() == 0) {
                    try {
                        AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
                        bui.setTitle("Thông báo:");
                        if (MainActivity.tenAcc.length() == 0)
                            bui.setMessage("Kiểm tra kết nối Internet! 1");
                        else
                            bui.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                        bui.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                        bui.create().show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Add_tin();
                }
            } else {
                db.QueryData("Update tbl_tinnhanS Set nd_phantich = '" + editTsuatin.getText().toString() + "', nd_sua = '" + editTsuatin.getText().toString() + "' WHERE id = " + tinNhanS().getID());
                int type_kh = BriteDb.INSTANCE.getIntField(TinNhanS.TABLE_NAME, TinNhanS.TYPE_KH, "id = " + tinNhanS().getID());

                try {
                    db.Update_TinNhanGoc(tinNhanS().getID(), type_kh);
                    EventBus.getDefault().post(new BusEvent.SetupErrorBagde(0));
                } catch (Throwable e) {
                    db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + tinNhanS().getID());
                    db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + tinNhanS().getNgay_nhan() + "' AND so_dienthoai = '" + tinNhanS().getSo_dienthoai() + "' AND so_tin_nhan = " + tinNhanS().getSo_tin_nhan() + " AND type_kh = " + type_kh);
                    error = false;
                    Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                }
                if (!Congthuc.CheckTime("18:30") && Cur_date.contains(CurDate)) {
                    try {
                        db.Gui_Tin_Nhan(tinNhanS().getID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                TinNhanS tinNhanS_g = BriteDb.INSTANCE.selectTinNhanS(tinNhanS().getID());
                if (tinNhanS_g.getPhat_hien_loi().contains("Không high")) {
                    String str1 = tinNhanS_g.getNd_phantich().replace("ldpro", "<font color='#FF0000'>");
                    editTsuatin.setText(Html.fromHtml(str1));
                    if (tinNhanS_g.getNd_phantich().contains("ldpro")) {
                        try {
                            editTsuatin.setSelection(str1.indexOf("<font"));
                        } catch (Exception ignored) {
                        }
                    }
                    error = false;
                } else {
                    editTsuatin.setText("");
                    xem_lv();
                    if (mTinNhanS.size() > 0) {
                        lv_position = 0;
                        if (tinNhanS().getPhat_hien_loi().contains("Không hiểu")) {
                            editTsuatin.setText(Html.fromHtml(tinNhanS().getNd_phantich().replace("ldpro", "<font color='#FF0000'>")));
                            int KKK = tinNhanS().getNd_phantich().indexOf("ldpro");
                            if (KKK > -1) {
                                try {
                                    editTsuatin.setSelection(KKK);
                                } catch (Exception ignored) {
                                }
                            }
                            sp_TenKH.setSelection(mNameKhach.indexOf(tinNhanS().getTen_kh()));
                            error = false;
                        } else {
                            editTsuatin.setText(tinNhanS().getNd_sua());
                        }
                    } else {
                        lv_position = -1;
                        error = false;
                    }
                }
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
        tngAdapter = new TNGAdapter(getActivity(), R.layout.frag_suatin_lv);

        this.lv_suatin.setAdapter(this.tngAdapter);
        this.lv_suatin.setOnItemClickListener((adapterView, view, i, l) -> {
            lv_position = i;
            editTsuatin.setText(Html.fromHtml(tinNhanS().getNd_phantich().replace("ldpro", "<font color='#FF0000'>")));
            int KKK = tinNhanS().getNd_phantich().indexOf("ldpro");
            if (KKK > -1) {
                try {
                    editTsuatin.setSelection(KKK);
                } catch (Exception ignored) {
                }
            }
            sp_TenKH.setSelection(mNameKhach.indexOf(tinNhanS().getTen_kh()));
            tngAdapter.notifyDataSetChanged();
        });
        this.lv_suatin.setOnItemLongClickListener((adapterView, view, position, id) -> {
            lv_position = position;
            return false;
        });
        try {
            mKhachHang = BriteDb.INSTANCE.selectListKhachHang("Order by type_kh, ten_kh");
            mNameKhach = mKhachHang.stream().map(KhachHang::getTen_kh).collect(Collectors.toList());
            this.sp_TenKH.setAdapter(new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, mKhachHang.stream().map(KhachHang::getTen_kh).collect(Collectors.toList())));
            if (mKhachHang.size() > 0) {
                this.sp_TenKH.setSelection(0);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Đang copy dữ liệu bản mới!", Toast.LENGTH_SHORT).show();
        }
        this.sp_TenKH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spin_pointion = position;
                if (mKhachHang.size() > position) khachHang = mKhachHang.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.radio_SuaTin.setOnCheckedChangeListener((buttonView, isChecked) -> control_RadioButton());
        this.radio_TaiTin.setOnCheckedChangeListener((buttonView, isChecked) -> control_RadioButton());
        this.btn_LoadTin.setOnClickListener(v -> {
            if (khachHang == null || mKhachHang.size() <= 0) {
                Toast.makeText(getActivity(), "Chưa có tên khách hàng!", Toast.LENGTH_LONG).show();
            } else if (khachHang.getUse_app().contains("sms")) {
                AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
                bui.setTitle("Tải lại tin nhắn khách này?");
                bui.setPositiveButton("YES", (dialog, which) -> {
                    try {
                        getFullSms(khachHang.getSdt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    db.QueryData("Update chat_database set del_sms = 1 WHERE ten_kh = '" + khachHang.getTen_kh() + "' AND ngay_nhan = '" + mDate + "'");
                });
                bui.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                bui.create().show();
            } else {
                AlertDialog.Builder bui2 = new AlertDialog.Builder(getActivity());
                bui2.setTitle("Tải lại tin nhắn khách này?");
                bui2.setPositiveButton("YES", (dialog, which) -> {
                    getAllChat(khachHang.getType_kh());
                    Database database = db;
                    database.QueryData("Update chat_database set del_sms = 1 WHERE ten_kh = '" + khachHang.getTen_kh() + "' AND ngay_nhan = '" + mDate + "'");
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
        if (mKhachHang.size() > 0 && this.editTsuatin.getText().toString().length() > 6) {
            final String mDate = MainActivity.Get_date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
            hourFormat.setTimeZone(TimeZone.getDefault());
            final String mGionhan = hourFormat.format(calendar.getTime());
            String str = "Select * From tbl_tinnhanS WHERE nd_goc = '" + editTsuatin.getText().toString().replaceAll("'", "").trim() + "' AND ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + khachHang.getSdt() + "'";
            Cursor Ktratin = db.GetData(str);
            Ktratin.moveToFirst();
            KhachHang khachHangDB = BriteDb.INSTANCE.selectKhachHangQuery("Where sdt = '" + khachHang.getSdt() + "'");

            if (this.spin_pointion <= -1 || Ktratin.getCount() != 0) {
                if (Ktratin.getCount() > 0) {
                    Toast.makeText(getActivity(), "Đã có tin này trong CSDL!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Hãy chọn tên khách hàng", Toast.LENGTH_LONG).show();
                }
            } else {
                assert khachHangDB != null;
                if (khachHangDB.getType_kh() == 3) {
                    AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
                    bui.setTitle("Chọn loại tin nhắn:");
                    bui.setMessage("Đây là khách vừa nhận vừa chuyển, thêm tin nhận hay tin chuyển?");
                    bui.setNeutralButton("Tin nhận", (dialog, which) -> {
                        type_kh = 1;
                        int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mDate, type_kh, "so_dienthoai = '" + khachHang.getSdt() + "'");
                        String textSua = editTsuatin.getText().toString().replace("'", " ").trim();

                        BriteDb.INSTANCE.insertTinNhanS(
                                new TinNhanS(null, mDate, mGionhan, type_kh, khachHang.getTen_kh(), khachHang.getSdt(), khachHangDB.getUse_app(),
                                        maxSoTn + 1, textSua, textSua, textSua, "ko", 0, 0, 0, null)
                        );
                        editTsuatin.setText("");

                        String query = "ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + khachHang.getSdt() + "' AND so_tin_nhan = " + (maxSoTn + 1) + " AND type_kh = " + type_kh;
                        int idTinNhan = BriteDb.INSTANCE.getIntField(TinNhanS.TABLE_NAME, "id", query);
                        if (Congthuc.CheckDate(MainActivity.hanSuDung) && idTinNhan > 0) {
                            try {
                                db.Update_TinNhanGoc(idTinNhan, khachHangDB.getType_kh());
                            } catch (Exception e) {
                                db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + idTinNhan);
                                String delete = "Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + khachHang.getSdt() + "' AND so_tin_nhan = " + (maxSoTn + 1) + " AND type_kh = " + type_kh;
                                db.QueryData(delete);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } else if (MainActivity.tenAcc.length() == 0) {
                            AlertDialog.Builder bui12 = new AlertDialog.Builder(getActivity());
                            bui12.setTitle("Thông báo:");
                            bui12.setMessage("Kiểm tra kết nối Internet! 2");
                            bui12.setNegativeButton("Đóng", (dialog13, which13) -> dialog13.cancel());
                            bui12.create().show();
                        } else {
                            try {
                                AlertDialog.Builder bui2 = new AlertDialog.Builder(getActivity());
                                bui2.setTitle("Thông báo:");
                                bui2.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                                bui2.setNegativeButton("Đóng", (dialog14, which14) -> dialog14.cancel());
                                bui2.create().show();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                        dialog.cancel();
                        MainActivity.sms = true;
                    });
                    bui.setPositiveButton("Tin Chuyển", (dialog, which) -> {
                        type_kh = 2;
                        int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mDate, type_kh, "so_dienthoai = '" + khachHang.getSdt() + "'");
                        String textSua = editTsuatin.getText().toString().replace("'", " ").trim();
                        BriteDb.INSTANCE.insertTinNhanS(
                                new TinNhanS(null, mDate, mGionhan, type_kh, khachHang.getTen_kh(), khachHang.getSdt(), khachHangDB.getUse_app(), maxSoTn + 1,
                                        textSua, textSua, textSua, "ko", 0, 0, 0, null)
                        );

                        editTsuatin.setText("");
                        String query = "ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + khachHang.getSdt() +
                                "' AND so_tin_nhan = " + (maxSoTn + 1) + " AND type_kh = " + type_kh;

                        int idTinNhan = BriteDb.INSTANCE.getIntField(TinNhanS.TABLE_NAME, TinNhanS.ID, query);
                        if (Congthuc.CheckDate(MainActivity.hanSuDung) && idTinNhan > 0) {
                            try {
                                db.Update_TinNhanGoc(idTinNhan, khachHangDB.getType_kh());
                            } catch (Exception e) {
                                db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + idTinNhan);
                                String delete = "Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + khachHang.getSdt() + "' AND so_tin_nhan = " + (maxSoTn + 1) + " AND type_kh = " + type_kh;
                                db.QueryData(delete);

                                Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } else {
                            try {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Thông báo:");
                                if (MainActivity.tenAcc.length() == 0)
                                    builder.setMessage("Kiểm tra kết nối Internet! 3");
                                else
                                    builder.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                                builder.setNegativeButton("Đóng", (dialog12, which12) -> dialog12.cancel());
                                builder.create().show();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                        dialog.cancel();
                        MainActivity.sms = true;
                    });
                    bui.create().show();
                } else {
                    this.type_kh = khachHangDB.getType_kh();
                    int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mDate, type_kh, "so_dienthoai = '" + khachHang.getSdt() + "'");

                    String textSua = editTsuatin.getText().toString().replace("'", " ").trim();
                    BriteDb.INSTANCE.insertTinNhanS(
                            new TinNhanS(null, mDate, mGionhan, type_kh, khachHang.getTen_kh(), khachHang.getSdt(), khachHangDB.getUse_app(), maxSoTn + 1,
                                    textSua, textSua, textSua, "ko", 0, 0, 0, null)
                    );
                    editTsuatin.setText("");
                    String query = "ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.khachHang.getSdt() +
                            "' AND so_tin_nhan = " + (maxSoTn + 1) + " AND type_kh = " + this.type_kh;
                    int idTinNhan = BriteDb.INSTANCE.getIntField(TinNhanS.TABLE_NAME, TinNhanS.ID, query);

                    if (Congthuc.CheckDate(MainActivity.hanSuDung)) {
                        try {
                            db.Update_TinNhanGoc(idTinNhan, khachHangDB.getType_kh());
                        } catch (Exception e) {
                            db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + idTinNhan);
                            String delete = "Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.khachHang.getSdt() + "' AND so_tin_nhan = " + (maxSoTn + 1) + " AND type_kh = " + this.type_kh;
                            db.QueryData(delete);
                            Toast.makeText(getActivity(), "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    } else {
                        try {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                            builder2.setTitle("Thông báo:");
                            if (MainActivity.tenAcc.length() == 0)
                                builder2.setMessage("Kiểm tra kết nối Internet! 4");
                            else
                                builder2.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                            builder2.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                            builder2.create().show();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                    MainActivity.sms = true;
                }
            }
            xem_lv();
            Ktratin.close();
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
                database.QueryData("Delete FROM tbl_tinnhanS WHERE id = " + mTinNhanS.get(lv_position).getID());
                this.lv_position = -1;
                xem_lv();
                Toast.makeText(getActivity(), "Xoá thành công", Toast.LENGTH_LONG).show();
                this.editTsuatin.setText("");
                EventBus.getDefault().post(new BusEvent.SetupErrorBagde(0));
            }
            xem_lv();
            if (mTinNhanS.size() > 0) {
                this.lv_position = 0;
                if (tinNhanS().getPhat_hien_loi().contains("Không hiểu")) {
                    this.editTsuatin.setText(Html.fromHtml(tinNhanS().getNd_phantich().replace("ldpro", "<font color='#FF0000'>")));
                    int KKK = tinNhanS().getNd_phantich().indexOf("ldpro");
                    if (KKK > -1) {
                        try {
                            this.editTsuatin.setSelection(KKK);
                        } catch (Exception ignored) {
                        }
                    }
                    this.sp_TenKH.setSelection(mNameKhach.indexOf(tinNhanS().getTen_kh()));
                    this.error = false;
                } else {
                    this.editTsuatin.setText(tinNhanS().getNd_sua());
                }
            } else {
                this.lv_position = -1;
                this.error = false;
            }
        }
        if (item.getItemId() == 2) {
            if (this.lv_position >= 0) {
                String mDate = MainActivity.Get_date();
                db.QueryData("Delete FROM tbl_tinnhanS WHERE phat_hien_loi <> 'ok' And ngay_nhan = '" + mDate + "'");
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
        String mDate = MainActivity.Get_date();
        int soTN = 0;
        this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + khachHang.getSdt() + "'");
        this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + khachHang.getSdt() + "'");
        String useApp = BriteDb.INSTANCE.getStringField(KhachHang.TABLE_NAME, KhachHang.USE_APP, "sdt = '" + khachHang.getSdt() + "'");

        String query = "Where ngay_nhan = '" + mDate + "' AND ten_kh = '" + this.khachHang.getTen_kh() + "'";
        if (Type_kh != 3) query += " AND type_kh = " + Type_kh;
        List<Chat> listChat = BriteDb.INSTANCE.selectChatsQuery(query);

        SQLiteDatabase database2 = this.db.getWritableDatabase();
        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
        database2.beginTransaction();
        try {
            if (listChat.size() > 0) {
                for (Chat chat : listChat) {
                    soTN++;
                    ih.prepareForInsert();
                    ih.bind(ih.getColumnIndex("ngay_nhan"), mDate);
                    ih.bind(ih.getColumnIndex("gio_nhan"), chat.getGio_nhan());
                    ih.bind(ih.getColumnIndex("type_kh"), chat.getType_kh());
                    ih.bind(ih.getColumnIndex("ten_kh"), khachHang.getTen_kh());
                    ih.bind(ih.getColumnIndex("so_dienthoai"), khachHang.getSdt());
                    ih.bind(ih.getColumnIndex("use_app"), useApp);
                    ih.bind(ih.getColumnIndex("so_tin_nhan"), soTN);
                    ih.bind(ih.getColumnIndex("nd_goc"), chat.getNd_goc());
                    ih.bind(ih.getColumnIndex("nd_sua"), chat.getNd_goc());
                    ih.bind(ih.getColumnIndex("nd_phantich"), chat.getNd_goc());
                    ih.bind(ih.getColumnIndex("phat_hien_loi"), "ko");
                    ih.bind(ih.getColumnIndex("tinh_tien"), 0);
                    ih.bind(ih.getColumnIndex("ok_tn"), 0);
                    ih.bind(ih.getColumnIndex("del_sms"), 0);
                    ih.execute();
                }
            }
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
    }

    public void getFullSms(String str) throws ParseException {
        String Get_ngay = MainActivity.Get_ngay();
        String Get_date = MainActivity.Get_date();
        String version1;

        if (!MainActivity.jSon_Setting.has("tin_trung")) {
            try {
                MainActivity.jSon_Setting.put("tin_trung", 0);
                this.db.QueryData("Update tbl_Setting set Setting = '" + MainActivity.jSon_Setting.toString() + "' WHERE ID = 1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.READ_SMS") != 0) {
            return;
        }
        Date parse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(Get_date + "T00:00:00");
        String str24 = "date>=" + parse.getTime();
        String sb;
        if (str.contains("Full")) {
            this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + Get_date + "'");
            this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + Get_date + "'");
            sb = "Select * From tbl_kh_new";
        } else {
            this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + Get_date + "' AND so_dienthoai = '" + this.khachHang.getSdt() + "'");
            this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + Get_date + "' AND so_dienthoai = '" + this.khachHang.getSdt() + "'");
            sb = "Select * From tbl_kh_new Where sdt = '" + this.khachHang.getSdt() + "'";
        }
        Cursor GetKhach = db.GetData(sb);
        JSONObject jsonALlKH = new JSONObject();
        try {
            while (GetKhach.moveToNext()) {
                JSONObject jsonKh = new JSONObject();
                jsonKh.put("type_kh", GetKhach.getString(3));
                jsonKh.put("ten_kh", GetKhach.getString(0));
                jsonKh.put("so_tn", 0);
                jsonALlKH.put(GetKhach.getString(1), jsonKh);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Cursor query = getActivity().getContentResolver().query(Uri.parse("content://sms"), null, str24, null, "date ASC");
        getActivity().startManagingCursor(query);
        int count = query.getCount();
        SQLiteDatabase writableDatabase = this.db.getWritableDatabase();
        DatabaseUtils.InsertHelper insertHelper = new DatabaseUtils.InsertHelper(writableDatabase, "tbl_tinnhanS");
        boolean moveToFirst = query.moveToFirst();

        if (moveToFirst) {
            try {
                writableDatabase.beginTransaction();
                for (int i = 0; i < count; i++) {
                    try {
                        long dateValue = query.getLong(query.getColumnIndexOrThrow("date"));
                        String sb4 = DateFormat.format("dd/MM/yyyy HH:mm:ss", new Date(dateValue)) + "";
                        String gioNhan = DateFormat.format("HH:mm:ss", new Date(dateValue)) + "";
                        version1 = query.getString(query.getColumnIndexOrThrow("address")).replaceAll(" ", "");
                        String ndSms = query.getString(query.getColumnIndexOrThrow("body")).replaceAll("'", " ").replaceAll("\"", " ");
                        String type = query.getString(query.getColumnIndexOrThrow("type"));
                        if (version1.length() < 12) {
                            version1 = "+84" + version1.substring(1);
                        }
                        if (jsonALlKH.has(version1) && sb4.contains(Get_ngay) && !ndSms.contains("Ok Tin")) {
                            JSONObject jsonKHsms = jsonALlKH.getJSONObject(version1);
                            if (!jsonKHsms.getString(TYPE_KH).contains("3")) {
                                if (jsonKHsms.getString(TYPE_KH).contains("2")) {
                                    xem_lv();
                                    Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                                }
                                boolean z = jsonKHsms.getString(TYPE_KH).contains(version1) && type.contains(version1);
                                if (!((MainActivity.jSon_Setting.getInt(TIN_TRUNG) == 1 || !jsonKHsms.has(ndSms)) && z)) {
                                    jsonKHsms.put("so_tn", jsonKHsms.getInt("so_tn") + 1);
                                    jsonKHsms.put(ndSms, ndSms);
                                    insertHelper.prepareForInsert();

                                    try {
                                        insertHelper.bind(insertHelper.getColumnIndex(NGAY_NHAN), Get_date);
                                        insertHelper.bind(insertHelper.getColumnIndex(GIO_NHAN), gioNhan);
                                        insertHelper.bind(insertHelper.getColumnIndex(TYPE_KH), type);
                                        insertHelper.bind(insertHelper.getColumnIndex(TEN_KH), jsonKHsms.getString(TEN_KH));
                                        insertHelper.bind(insertHelper.getColumnIndex(SO_DIENTHOAI), version1);
                                        insertHelper.bind(insertHelper.getColumnIndex(USE_APP), SMS);
                                        insertHelper.bind(insertHelper.getColumnIndex(SO_TIN_NHAN), jsonKHsms.getInt("so_tn"));
                                        insertHelper.bind(insertHelper.getColumnIndex(ND_GOC), ndSms);
                                        insertHelper.bind(insertHelper.getColumnIndex(ND_SUA), ndSms);
                                        insertHelper.bind(insertHelper.getColumnIndex("nd_phantich"), ndSms);
                                        insertHelper.bind(insertHelper.getColumnIndex("phat_hien_loi"), "ko");
                                        insertHelper.bind(insertHelper.getColumnIndex("tinh_tien"), 0);
                                        insertHelper.bind(insertHelper.getColumnIndex("ok_tn"), 0);
                                        insertHelper.bind(insertHelper.getColumnIndex("del_sms"), 0);
                                        insertHelper.execute();
                                        jsonALlKH.put(version1, jsonKHsms);

                                    } catch (Throwable th2) {
                                        insertHelper.close();
                                    }
                                }
                            }
                        }
                        query.moveToNext();
                    } catch (Exception e18) {
                        writableDatabase.setTransactionSuccessful();
                        writableDatabase.endTransaction();
                        writableDatabase.close();
                        xem_lv();
                        Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();
                    }
                }
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
                writableDatabase.close();
                insertHelper.close();

            } catch (Throwable ignored) {
            }
        }

        try {
            List<Chat> listChat = BriteDb.INSTANCE.selectChatsQuery("Where ngay_nhan = '" + Get_date + "' And use_app <> 'sms'");

            List<TinNhanS> listTinNhanS_i = new ArrayList<>();

            if (listChat.size() > 0) {
                for (Chat chat : listChat) {
                    String ngay_nhan = chat.getNgay_nhan();
                    String gio_nhan = chat.getGio_nhan();
                    int type_kh = chat.getType_kh();
                    String so_dt = chat.getSo_dienthoai();
                    String nd_goc = chat.getNd_goc();
                    if (jsonALlKH.has(so_dt) && ngay_nhan.contains(Get_date) && !nd_goc.contains(OK_TIN)) {
                        JSONObject jsonKH = jsonALlKH.getJSONObject(so_dt);
                        if (!jsonKH.getString(TYPE_KH).contains("3") && (!jsonKH.getString(TYPE_KH).contains("2") || type_kh != 2)) {
                            boolean isTypeKhach = jsonKH.getString(TYPE_KH).contains("1") && type_kh == 1;
                            boolean isTrung = MainActivity.jSon_Setting.getInt(TIN_TRUNG) == 1 && jsonKH.has(nd_goc);
                            if (!isTrung || !isTypeKhach) {
                                jsonKH.put("so_tn", jsonKH.getInt("so_tn") + 1);
                                jsonKH.put(nd_goc, nd_goc);
                                jsonALlKH.put(so_dt, jsonKH);

                                listTinNhanS_i.add(
                                        new TinNhanS(null,
                                                Get_date,
                                                gio_nhan,
                                                type_kh,
                                                jsonKH.getString(TEN_KH),
                                                so_dt,
                                                SMS,
                                                jsonKH.getInt("so_tn"),
                                                nd_goc,
                                                nd_goc,
                                                nd_goc,
                                                "ko",
                                                0,
                                                0,
                                                0,
                                                null)
                                );
                            }
                        }
                    }
                }
                BriteDb.INSTANCE.insertListTinNhanS(listTinNhanS_i);
            }
            xem_lv();
            Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", Toast.LENGTH_LONG).show();

        } catch (Throwable ignored) {
            Log.e(TAG, "getFullSms: Throwable " + ignored);
        }
    }

    public void xem_lv() {
        String mDate = MainActivity.Get_date();
        mTinNhanS = BriteDb.INSTANCE.selectListTinNhanS("Where phat_hien_loi <> 'ok' AND ngay_nhan = '" + mDate + "'");
        if (getActivity() != null) {
            tngAdapter.clear();
            tngAdapter.addAll(mTinNhanS);
            tngAdapter.notifyDataSetChanged();
        }
    }

    public class TNGAdapter extends ArrayAdapter {
        public TNGAdapter(Context context, int resource) {
            super(context, resource);
        }

        class ViewHolder {
            LinearLayout ll_suatin_lv;
            TextView tv_suatin_nd;
            TextView tv_suatin_err;

            ViewHolder() {
            }
        }

        @SuppressLint("WrongConstant")
        public View getView(int position, View mView, ViewGroup parent) {
            ViewHolder holder;
            if (mView == null) {
                mView = LayoutInflater.from(getContext()).inflate(R.layout.frag_suatin_lv, null);
                holder = new ViewHolder();
                holder.tv_suatin_nd = mView.findViewById(R.id.tv_suatin_nd);
                holder.tv_suatin_err = mView.findViewById(R.id.tv_suatin_err);
                holder.ll_suatin_lv = mView.findViewById(R.id.ll_suatin_lv);
                mView.setTag(holder);
            } else {
                holder = (ViewHolder) mView.getTag();
            }
            Log.e(TAG, "getView: " + lv_position);
            holder.ll_suatin_lv.setBackgroundColor(ContextCompat.getColor(getContext(), position != lv_position ? R.color.white : R.color.colorBackGround));
            holder.tv_suatin_nd.setText(mTinNhanS.get(position).getNd_goc());
            holder.tv_suatin_err.setText(mTinNhanS.get(position).getPhat_hien_loi());
            return mView;
        }
    }

    final String NGAY_NHAN = "ngay_nhan";
    final String SO_DIENTHOAI = "so_dienthoai";
    final String ND_SUA = "nd_sua";
    final String ND_GOC = "nd_goc";
    final String SO_TIN_NHAN = "so_tin_nhan";
    final String SMS = "sms";
    final String USE_APP = "use_app";
    final String TEN_KH = "ten_kh";
    final String TYPE_KH = "type_kh";
    final String GIO_NHAN = "gio_nhan";
    final String OK_TIN = "Ok Tin";
    final String TIN_TRUNG = "tin_trung";

}