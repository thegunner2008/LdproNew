//package tamhoang.ldpro4.Fragment;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.database.Cursor;
//import android.database.DatabaseUtils;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteException;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Html;
//import android.text.format.DateFormat;
//import android.view.ContextMenu;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.Spinner;
//import android.widget.SpinnerAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.TimeZone;
//import org.json.JSONException;
//import org.json.JSONObject;
//import tamhoang.ldpro4.Congthuc.Congthuc;
//import tamhoang.ldpro4.MainActivity;
//import tamhoang.ldpro4.R;
//import tamhoang.ldpro4.data.Database;
//
//public class Frag_Suatin extends Fragment {
//    String CurDate;
//    String Cur_date;
//    Button btn_LoadTin;
//    Button btn_suatin;
//    Button btn_tai_All;
//    Database db;
//    EditText editTsuatin;
//    boolean error;
//    Handler handler;
//    int lv_position = -1;
//    ListView lv_suatin;
//    public List<String> mApp = new ArrayList();
//    public List<String> mContact = new ArrayList();
//    public List<Integer> mID = new ArrayList();
//    public List<String> mMobile = new ArrayList();
//    public List<String> mND_DaSua = new ArrayList();
//    public List<String> mND_PhanTich = new ArrayList();
//    public List<String> mNgay = new ArrayList();
//    public List<String> mPhatHienLoi = new ArrayList();
//    public List<String> mSDT = new ArrayList();
//    public List<Integer> mSoTinNhan = new ArrayList();
//    public List<String> mTenKH = new ArrayList();
//    public List<String> mTinNhanGoc = new ArrayList();
//    public List<Integer> mTypeKH = new ArrayList();
//    public List<Integer> mType_kh = new ArrayList();
//    RadioButton radio_SuaTin;
//    RadioButton radio_TaiTin;
//    private Runnable runnable = new Runnable() {
//
//        public void run() {
//            new MainActivity();
//            if (MainActivity.sms) {
//                Frag_Suatin.this.xem_lv();
//                MainActivity.sms = false;
//            }
//            Frag_Suatin.this.handler.postDelayed(this, 1000);
//        }
//    };
//    Spinner sp_TenKH;
//    int spin_pointion = -1;
//    String str;
//    int type_kh;
//    View v;
//    private Runnable xulyTinnhan = new Runnable() {
//        /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass14 */
//
//        public void run() {
//            Cursor cur = null;
//            Cursor cursor = null;
//            Frag_Suatin.this.error = true;
//            if (Frag_Suatin.this.editTsuatin.getText().toString().length() < 6) {
//                Frag_Suatin.this.error = false;
//            } else if (Frag_Suatin.this.lv_position < 0 || !Congthuc.CheckDate(MainActivity.myDate)) {
//                Frag_Suatin.this.error = false;
//                if (!Congthuc.CheckDate(MainActivity.myDate)) {
//                    try {
//                        AlertDialog.Builder bui = new AlertDialog.Builder(Frag_Suatin.this.getActivity());
//                        bui.setTitle("Thông báo:");
//                        bui.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.listKH.getString("k_tra") + " để gia hạn");
//                        bui.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass14.AnonymousClass2 */
//
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        bui.create().show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else if (MainActivity.Acc_manager.length() == 0) {
//                    AlertDialog.Builder bui2 = new AlertDialog.Builder(Frag_Suatin.this.getActivity());
//                    bui2.setTitle("Thông báo:");
//                    bui2.setMessage("Kiểm tra kết nối Internet!");
//                    bui2.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                        /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass14.AnonymousClass1 */
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    bui2.create().show();
//                } else {
//                    Frag_Suatin.this.Add_tin();
//                }
//            } else {
//                Frag_Suatin.this.db.QueryData("Update tbl_tinnhanS Set nd_phantich = '" + Frag_Suatin.this.editTsuatin.getText().toString() + "', nd_sua = '" + Frag_Suatin.this.editTsuatin.getText().toString() + "' WHERE id = " + Frag_Suatin.this.mID.get(Frag_Suatin.this.lv_position));
//                Database database = Frag_Suatin.this.db;
//                StringBuilder sb = new StringBuilder();
//                sb.append("Select type_kh From tbl_tinnhanS WHERE id = ");
//                sb.append(Frag_Suatin.this.mID.get(Frag_Suatin.this.lv_position));
//                cur = database.GetData(sb.toString());
//                cur.moveToFirst();
//                try {
//                    Frag_Suatin.this.db.Update_TinNhanGoc(Frag_Suatin.this.mID.get(Frag_Suatin.this.lv_position).intValue(), cur.getInt(0));
//                } catch (Throwable e2) {
//                    Database database2 = Frag_Suatin.this.db;
//                    database2.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + Frag_Suatin.this.mID.get(Frag_Suatin.this.lv_position));
//                    Frag_Suatin.this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + Frag_Suatin.this.mNgay.get(Frag_Suatin.this.lv_position) + "' AND so_dienthoai = '" + Frag_Suatin.this.mSDT.get(Frag_Suatin.this.lv_position) + "' AND so_tin_nhan = " + Frag_Suatin.this.mSoTinNhan.get(Frag_Suatin.this.lv_position) + " AND type_kh = " + cur.getString(0));
//                    Frag_Suatin.this.error = false;
//                    Toast.makeText(Frag_Suatin.this.getActivity(), "Đã xảy ra lỗi!", 1).show();
//                }
//                if (!Congthuc.CheckTime("18:30") && Frag_Suatin.this.Cur_date.contains(Frag_Suatin.this.CurDate)) {
//                    try {
//                        Frag_Suatin.this.db.Gui_Tin_Nhan(Frag_Suatin.this.mID.get(Frag_Suatin.this.lv_position).intValue());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Database database3 = Frag_Suatin.this.db;
//                cursor = database3.GetData("Select * FROM tbl_tinnhanS Where id = " + Frag_Suatin.this.mID.get(Frag_Suatin.this.lv_position));
//                cursor.moveToFirst();
//                if (cursor.getString(11).indexOf("Không hiểu") > -1) {
//                    String str1 = cursor.getString(10).replace("ldpro", "<font color='#FF0000'>");
//                    Frag_Suatin.this.editTsuatin.setText(Html.fromHtml(str1));
//                    if (cursor.getString(10).indexOf("ldpro") > -1) {
//                        try {
//                            Frag_Suatin.this.editTsuatin.setSelection(str1.indexOf("<font"));
//                        } catch (Exception e3) {
//                        }
//                    }
//                    Frag_Suatin.this.error = false;
//                } else {
//                    Frag_Suatin.this.editTsuatin.setText("");
//                    Frag_Suatin.this.xem_lv();
//                    if (Frag_Suatin.this.mND_DaSua.size() > 0) {
//                        Frag_Suatin.this.lv_position = 0;
//                        if (Frag_Suatin.this.mPhatHienLoi.get(0).indexOf("Không hiểu") > -1) {
//                            Frag_Suatin.this.editTsuatin.setText(Html.fromHtml(Frag_Suatin.this.mND_PhanTich.get(0).replace("ldpro", "<font color='#FF0000'>")));
//                            int KKK = Frag_Suatin.this.mND_PhanTich.get(0).indexOf("ldpro");
//                            if (KKK > -1) {
//                                try {
//                                    Frag_Suatin.this.editTsuatin.setSelection(KKK);
//                                } catch (Exception e4) {
//                                }
//                            }
//                            Frag_Suatin.this.sp_TenKH.setSelection(Frag_Suatin.this.mContact.indexOf(Frag_Suatin.this.mTenKH.get(0)));
//                            Frag_Suatin.this.error = false;
//                        } else {
//                            Frag_Suatin.this.editTsuatin.setText(Frag_Suatin.this.mND_DaSua.get(0));
//                        }
//                    } else {
//                        Frag_Suatin.this.lv_position = -1;
//                        Frag_Suatin.this.error = false;
//                    }
//                }
//            }
//            if (cur != null) {
//                cur.close();
//            }
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (!Frag_Suatin.this.error) {
//                Frag_Suatin.this.handler.removeCallbacks(Frag_Suatin.this.xulyTinnhan);
//            } else {
//                Frag_Suatin.this.handler.postDelayed(this, 300);
//            }
//        }
//    };
//
//    @Override // android.support.v4.app.Fragment
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        this.v = inflater.inflate(R.layout.frag_suatin, container, false);
//        this.db = new Database(getActivity());
//        this.btn_suatin = (Button) this.v.findViewById(R.id.btn_suatin_suatin);
//        this.btn_LoadTin = (Button) this.v.findViewById(R.id.btn_loadtin);
//        this.editTsuatin = (EditText) this.v.findViewById(R.id.editText_suatin);
//        this.btn_tai_All = (Button) this.v.findViewById(R.id.btn_tai_All);
//        this.sp_TenKH = (Spinner) this.v.findViewById(R.id.spr_KH);
//        this.radio_SuaTin = (RadioButton) this.v.findViewById(R.id.radio_suaTin);
//        this.radio_TaiTin = (RadioButton) this.v.findViewById(R.id.radio_TaiTin);
//        this.lv_suatin = (ListView) this.v.findViewById(R.id.lv_suatin);
//        Handler handler2 = new Handler();
//        this.handler = handler2;
//        handler2.postDelayed(this.runnable, 1000);
//        new MainActivity();
//        final String mDate = MainActivity.Get_date();
//        this.btn_suatin.setOnClickListener(new View.OnClickListener() {
//            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass1 */
//
//            public void onClick(View view) {
//                new MainActivity();
//                Frag_Suatin.this.CurDate = MainActivity.Get_date();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Frag_Suatin.this.Cur_date = sdf.format(new Date());
//                Frag_Suatin.this.handler = new Handler();
//                Frag_Suatin.this.handler.postDelayed(Frag_Suatin.this.xulyTinnhan, 300);
//            }
//        });
//        this.lv_suatin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass2 */
//
//            @Override // android.widget.AdapterView.OnItemClickListener
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Frag_Suatin.this.lv_position = i;
//                Frag_Suatin.this.editTsuatin.setText(Html.fromHtml(Frag_Suatin.this.mND_PhanTich.get(i).replace("ldpro", "<font color='#FF0000'>")));
//                int KKK = Frag_Suatin.this.mND_PhanTich.get(i).indexOf("ldpro");
//                if (KKK > -1) {
//                    try {
//                        Frag_Suatin.this.editTsuatin.setSelection(KKK);
//                    } catch (Exception e) {
//                    }
//                }
//                Frag_Suatin.this.sp_TenKH.setSelection(Frag_Suatin.this.mContact.indexOf(Frag_Suatin.this.mTenKH.get(i)));
//            }
//        });
//        this.lv_suatin.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass3 */
//
//            @Override // android.widget.AdapterView.OnItemLongClickListener
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Frag_Suatin.this.lv_position = position;
//                return false;
//            }
//        });
//        this.mContact.clear();
//        this.mMobile.clear();
//        this.mType_kh.clear();
//        this.mApp.clear();
//        try {
//            Cursor cursor = this.db.GetData("Select * From tbl_kh_new Order by type_kh, ten_kh");
//            while (cursor.moveToNext()) {
//                this.mContact.add(cursor.getString(0));
//                this.mMobile.add(cursor.getString(1));
//                this.mApp.add(cursor.getString(2));
//                this.mType_kh.add(Integer.valueOf(cursor.getInt(3)));
//            }
//            if (cursor != null) {
//                cursor.close();
//            }
//            this.sp_TenKH.setAdapter((SpinnerAdapter) new ArrayAdapter<>(getActivity(), (int) R.layout.spinner_item, this.mContact));
//            if (this.mContact.size() > 0) {
//                this.sp_TenKH.setSelection(0);
//            }
//        } catch (Exception e) {
//            Toast.makeText(getActivity(), "Đang copy dữ liệu bản mới!", 1).show();
//        }
//        this.sp_TenKH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass4 */
//
//            @Override // android.widget.AdapterView.OnItemSelectedListener
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                Frag_Suatin.this.spin_pointion = position;
//            }
//
//            @Override // android.widget.AdapterView.OnItemSelectedListener
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
//        this.radio_SuaTin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass5 */
//
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Frag_Suatin.this.control_RadioButton();
//            }
//        });
//        this.radio_TaiTin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass6 */
//
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Frag_Suatin.this.control_RadioButton();
//            }
//        });
//        this.btn_LoadTin.setOnClickListener(new View.OnClickListener() {
//            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass7 */
//
//            public void onClick(View v) {
//                if (Frag_Suatin.this.spin_pointion <= -1 || Frag_Suatin.this.mContact.size() <= 0) {
//                    Toast.makeText(Frag_Suatin.this.getActivity(), "Chưa có tên khách hàng!", 1).show();
//                } else if (Frag_Suatin.this.mApp.get(Frag_Suatin.this.spin_pointion).toString().indexOf("sms") > -1) {
//                    AlertDialog.Builder bui = new AlertDialog.Builder(Frag_Suatin.this.getActivity());
//                    bui.setTitle("Tải lại tin nhắn khách này?");
//                    bui.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass7.AnonymousClass1 */
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            try {
//                                Frag_Suatin.this.getFullSms(Frag_Suatin.this.mMobile.get(Frag_Suatin.this.spin_pointion));
//                                Database database = Frag_Suatin.this.db;
//                                database.QueryData("Update chat_database set del_sms = 1 WHERE ten_kh = '" + Frag_Suatin.this.mContact.get(Frag_Suatin.this.spin_pointion) + "' AND ngay_nhan = '" + mDate + "'");
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    bui.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass7.AnonymousClass2 */
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    bui.create().show();
//                } else {
//                    AlertDialog.Builder bui2 = new AlertDialog.Builder(Frag_Suatin.this.getActivity());
//                    bui2.setTitle("Tải lại tin nhắn khách này?");
//                    bui2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass7.AnonymousClass3 */
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            Frag_Suatin.this.getAllChat(Frag_Suatin.this.mType_kh.get(Frag_Suatin.this.spin_pointion).intValue());
//                            Database database = Frag_Suatin.this.db;
//                            database.QueryData("Update chat_database set del_sms = 1 WHERE ten_kh = '" + Frag_Suatin.this.mContact.get(Frag_Suatin.this.spin_pointion) + "' AND ngay_nhan = '" + mDate + "'");
//                        }
//                    });
//                    bui2.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass7.AnonymousClass4 */
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    bui2.create().show();
//                }
//            }
//        });
//        this.btn_tai_All.setOnClickListener(new View.OnClickListener() {
//            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass8 */
//
//            public void onClick(View v) {
//                AlertDialog.Builder bui = new AlertDialog.Builder(Frag_Suatin.this.getActivity());
//                bui.setTitle("Tải lại tin nhắn của tất cả khách?");
//                bui.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass8.AnonymousClass1 */
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        try {
//                            Frag_Suatin.this.getFullSms("Full");
//                            Database database = Frag_Suatin.this.db;
//                            database.QueryData("Update chat_database set del_sms = 1 WHERE ngay_nhan = '" + mDate + "'");
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                bui.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass8.AnonymousClass2 */
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                bui.create().show();
//            }
//        });
//        if (ContextCompat.checkSelfPermission(getContext(), "android.permission.READ_SMS") != 0) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), "android.permission.READ_SMS")) {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, 1);
//            } else {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_SMS"}, 1);
//            }
//        }
//        control_RadioButton();
//        registerForContextMenu(this.lv_suatin);
//        return this.v;
//    }
//
//    /* access modifiers changed from: private */
//    /* access modifiers changed from: public */
//    private void Add_tin() {
//        final MainActivity activity = new MainActivity();
//        if (this.mContact.size() > 0 && this.editTsuatin.getText().toString().length() > 6) {
//            new MainActivity();
//            final String mDate = MainActivity.Get_date();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
//            hourFormat.setTimeZone(TimeZone.getDefault());
//            final String mGionhan = hourFormat.format(calendar.getTime());
//            String str2 = "Select * From tbl_tinnhanS WHERE nd_goc = '" + this.editTsuatin.getText().toString().replaceAll("'", "").trim() + "' AND ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "'";
//            this.str = str2;
//            Cursor Ktratin = this.db.GetData(str2);
//            Ktratin.moveToFirst();
//            final Cursor cur1 = this.db.GetData("Select * From tbl_kh_new Where sdt = '" + this.mMobile.get(this.spin_pointion) + "'");
//            cur1.moveToFirst();
//            if (this.spin_pointion <= -1 || Ktratin.getCount() != 0) {
//                if (Ktratin.getCount() > 0) {
//                    Toast.makeText(getActivity(), "Đã có tin này trong CSDL!", 1).show();
//                } else {
//                    Toast.makeText(getActivity(), "Hãy chọn tên khách hàng", 1).show();
//                }
//            } else if (cur1.getInt(3) == 3) {
//                AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
//                bui.setTitle("Chọn loại tin nhắn:");
//                bui.setMessage("Đây là khách vừa nhận vừa chuyển, thêm tin nhận hay tin chuyển?");
//                bui.setNeutralButton("Tin nhận", new DialogInterface.OnClickListener() {
//                    /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass9 */
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        Frag_Suatin.this.type_kh = 1;
//                        Cursor getSoTN = Frag_Suatin.this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + Frag_Suatin.this.mMobile.get(Frag_Suatin.this.spin_pointion) + "' AND type_kh = " + Frag_Suatin.this.type_kh);
//                        getSoTN.moveToFirst();
//                        Frag_Suatin.this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mDate + "', '" + mGionhan + "', " + Frag_Suatin.this.type_kh + ", '" + Frag_Suatin.this.mContact.get(Frag_Suatin.this.spin_pointion) + "', '" + Frag_Suatin.this.mMobile.get(Frag_Suatin.this.spin_pointion) + "', '" + cur1.getString(2) + "', " + (getSoTN.getInt(0) + 1) + ", '" + Frag_Suatin.this.editTsuatin.getText().toString().replace("'", " ").trim() + "', '" + Frag_Suatin.this.editTsuatin.getText().toString().replace("'", " ").trim() + "', '" + Frag_Suatin.this.editTsuatin.getText().toString().replace("'", " ").trim() + "', 'ko',0, 0, 0, null)");
//                        Frag_Suatin.this.editTsuatin.setText("");
//                        Database database = Frag_Suatin.this.db;
//                        StringBuilder sb = new StringBuilder();
//                        sb.append("Select id From tbl_tinnhanS WHERE ngay_nhan = '");
//                        sb.append(mDate);
//                        sb.append("' AND so_dienthoai = '");
//                        sb.append(Frag_Suatin.this.mMobile.get(Frag_Suatin.this.spin_pointion));
//                        sb.append("' AND so_tin_nhan = ");
//                        sb.append(getSoTN.getInt(0) + 1);
//                        sb.append(" AND type_kh = ");
//                        sb.append(Frag_Suatin.this.type_kh);
//                        Cursor c = database.GetData(sb.toString());
//                        c.moveToFirst();
//                        if (Congthuc.CheckDate(MainActivity.myDate)) {
//                            try {
//                                Frag_Suatin.this.db.Update_TinNhanGoc(c.getInt(0), cur1.getInt(3));
//                            } catch (Exception e) {
//                                Database database2 = Frag_Suatin.this.db;
//                                database2.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
//                                Frag_Suatin frag_Suatin = Frag_Suatin.this;
//                                frag_Suatin.str = "Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + Frag_Suatin.this.mMobile.get(Frag_Suatin.this.spin_pointion) + "' AND so_tin_nhan = " + (getSoTN.getInt(0) + 1) + " AND type_kh = " + Frag_Suatin.this.type_kh;
//                                Frag_Suatin.this.db.QueryData(Frag_Suatin.this.str);
//                            } catch (Throwable throwable) {
//                                throwable.printStackTrace();
//                            }
//                        } else if (MainActivity.Acc_manager.length() == 0) {
//                            AlertDialog.Builder bui = new AlertDialog.Builder(Frag_Suatin.this.getActivity());
//                            bui.setTitle("Thông báo:");
//                            bui.setMessage("Kiểm tra kết nối Internet!");
//                            bui.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                                /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass9.AnonymousClass1 */
//
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                            bui.create().show();
//                        } else {
//                            try {
//                                AlertDialog.Builder bui2 = new AlertDialog.Builder(Frag_Suatin.this.getActivity());
//                                bui2.setTitle("Thông báo:");
//                                bui2.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.listKH.getString("k_tra") + " để gia hạn");
//                                bui2.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                                    /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass9.AnonymousClass2 */
//
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                });
//                                bui2.create().show();
//                            } catch (JSONException e2) {
//                                e2.printStackTrace();
//                            }
//                        }
//                        getSoTN.close();
//                        cur1.close();
//                        c.close();
//                        dialog.cancel();
//                        MainActivity.sms = true;
//                    }
//                });
//                bui.setPositiveButton("Tin Chuyển", new DialogInterface.OnClickListener() {
//                    /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass10 */
//
//                    public void onClick(DialogInterface dialog, int which) {
//                        Frag_Suatin.this.type_kh = 2;
//                        Cursor getSoTN = Frag_Suatin.this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + Frag_Suatin.this.mMobile.get(Frag_Suatin.this.spin_pointion) + "' AND type_kh = " + Frag_Suatin.this.type_kh);
//                        getSoTN.moveToFirst();
//                        Frag_Suatin.this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mDate + "', '" + mGionhan + "', " + Frag_Suatin.this.type_kh + ", '" + Frag_Suatin.this.mContact.get(Frag_Suatin.this.spin_pointion) + "', '" + Frag_Suatin.this.mMobile.get(Frag_Suatin.this.spin_pointion) + "', '" + cur1.getString(2) + "', " + (getSoTN.getInt(0) + 1) + ", '" + Frag_Suatin.this.editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', '" + Frag_Suatin.this.editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', '" + Frag_Suatin.this.editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', 'ko',0, 0, 0, null)");
//                        Frag_Suatin.this.editTsuatin.setText("");
//                        Database database = Frag_Suatin.this.db;
//                        StringBuilder sb = new StringBuilder();
//                        sb.append("Select id From tbl_tinnhanS WHERE ngay_nhan = '");
//                        sb.append(mDate);
//                        sb.append("' AND so_dienthoai = '");
//                        sb.append(Frag_Suatin.this.mMobile.get(Frag_Suatin.this.spin_pointion));
//                        sb.append("' AND so_tin_nhan = ");
//                        sb.append(getSoTN.getInt(0) + 1);
//                        sb.append(" AND type_kh = ");
//                        sb.append(Frag_Suatin.this.type_kh);
//                        Cursor c = database.GetData(sb.toString());
//                        c.moveToFirst();
//                        if (Congthuc.CheckDate(MainActivity.myDate)) {
//                            try {
//                                Frag_Suatin.this.db.Update_TinNhanGoc(c.getInt(0), cur1.getInt(3));
//                            } catch (Exception e) {
//                                Database database2 = Frag_Suatin.this.db;
//                                database2.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
//                                Frag_Suatin frag_Suatin = Frag_Suatin.this;
//                                frag_Suatin.str = "Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + Frag_Suatin.this.mMobile.get(Frag_Suatin.this.spin_pointion) + "' AND so_tin_nhan = " + (getSoTN.getInt(0) + 1) + " AND type_kh = " + Frag_Suatin.this.type_kh;
//                                Frag_Suatin.this.db.QueryData(Frag_Suatin.this.str);
//                                Toast.makeText(Frag_Suatin.this.getActivity(), "Đã xảy ra lỗi!", 1).show();
//                            } catch (Throwable throwable) {
//                                throwable.printStackTrace();
//                            }
//                        } else if (MainActivity.Acc_manager.length() == 0) {
//                            AlertDialog.Builder bui = new AlertDialog.Builder(Frag_Suatin.this.getActivity());
//                            bui.setTitle("Thông báo:");
//                            bui.setMessage("Kiểm tra kết nối Internet!");
//                            bui.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                                /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass10.AnonymousClass1 */
//
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//                            bui.create().show();
//                        } else {
//                            try {
//                                AlertDialog.Builder bui2 = new AlertDialog.Builder(Frag_Suatin.this.getActivity());
//                                bui2.setTitle("Thông báo:");
//                                bui2.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.listKH.getString("k_tra") + " để gia hạn");
//                                bui2.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                                    /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass10.AnonymousClass2 */
//
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                });
//                                bui2.create().show();
//                            } catch (JSONException e2) {
//                                e2.printStackTrace();
//                            }
//                        }
//                        getSoTN.close();
//                        cur1.close();
//                        c.close();
//                        dialog.cancel();
//                        MainActivity.sms = true;
//                    }
//                });
//                bui.create().show();
//            } else {
//                this.type_kh = cur1.getInt(3);
//                Cursor getSoTN = this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "' AND type_kh = " + this.type_kh);
//                getSoTN.moveToFirst();
//                this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mDate + "', '" + mGionhan + "', " + this.type_kh + ", '" + this.mContact.get(this.spin_pointion) + "', '" + this.mMobile.get(this.spin_pointion) + "', '" + cur1.getString(2) + "', " + (getSoTN.getInt(0) + 1) + ", '" + this.editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', '" + this.editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', '" + this.editTsuatin.getText().toString().replaceAll("'", " ").trim() + "', 'ko',0, 0, 0, null)");
//                this.editTsuatin.setText("");
//                Database database = this.db;
//                StringBuilder sb = new StringBuilder();
//                sb.append("Select id From tbl_tinnhanS WHERE ngay_nhan = '");
//                sb.append(mDate);
//                sb.append("' AND so_dienthoai = '");
//                sb.append(this.mMobile.get(this.spin_pointion));
//                sb.append("' AND so_tin_nhan = ");
//                sb.append(getSoTN.getInt(0) + 1);
//                sb.append(" AND type_kh = ");
//                sb.append(this.type_kh);
//                Cursor c = database.GetData(sb.toString());
//                c.moveToFirst();
//                if (Congthuc.CheckDate(MainActivity.myDate)) {
//                    try {
//                        this.db.Update_TinNhanGoc(c.getInt(0), cur1.getInt(3));
//                    } catch (Exception e) {
//                        this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
//                        String str3 = "Delete From tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "' AND so_tin_nhan = " + (getSoTN.getInt(0) + 1) + " AND type_kh = " + this.type_kh;
//                        this.str = str3;
//                        this.db.QueryData(str3);
//                        Toast.makeText(getActivity(), "Đã xảy ra lỗi!", 1).show();
//                    } catch (Throwable throwable) {
//                        throwable.printStackTrace();
//                    }
//                } else if (MainActivity.Acc_manager.length() == 0) {
//                    AlertDialog.Builder bui2 = new AlertDialog.Builder(getActivity());
//                    bui2.setTitle("Thông báo:");
//                    bui2.setMessage("Kiểm tra kết nối Internet!");
//                    bui2.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                        /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass11 */
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    bui2.create().show();
//                } else {
//                    try {
//                        AlertDialog.Builder bui3 = new AlertDialog.Builder(getActivity());
//                        bui3.setTitle("Thông báo:");
//                        bui3.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.listKH.getString("k_tra") + " để gia hạn");
//                        bui3.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                            /* class tamhoang.ldpro4.Fragment.Frag_Suatin.AnonymousClass12 */
//
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                        bui3.create().show();
//                    } catch (JSONException e2) {
//                        e2.printStackTrace();
//                    }
//                }
//                MainActivity.sms = true;
//                getSoTN.close();
//                cur1.close();
//                c.close();
//            }
//            xem_lv();
//            if (Ktratin != null) {
//                Ktratin.close();
//            }
//        }
//    }
//
//    @Override // android.support.v4.app.Fragment
//    public void onDestroy() {
//        super.onDestroy();
//        this.handler.removeCallbacks(this.runnable);
//    }
//
//    @Override // android.support.v4.app.Fragment
//    public void onCreateContextMenu(ContextMenu menu, View v2, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v2, menuInfo);
//        menu.add(0, 1, 0, "Xóa tin này?");
//    }
//
//    @Override // android.support.v4.app.Fragment
//    public boolean onContextItemSelected(MenuItem item) {
//        super.onContextItemSelected(item);
//        if (item.getItemId() == 1) {
//            if (this.lv_position >= 0) {
//                Database database = this.db;
//                database.QueryData("Delete FROM tbl_tinnhanS WHERE id = " + this.mID.get(this.lv_position));
//                this.lv_position = -1;
//                xem_lv();
//                Toast.makeText(getActivity(), "Xoá thành công", 1).show();
//                this.editTsuatin.setText("");
//            }
//            xem_lv();
//            if (this.mND_DaSua.size() > 0) {
//                this.lv_position = 0;
//                if (this.mPhatHienLoi.get(0).indexOf("Không hiểu") > -1) {
//                    this.editTsuatin.setText(Html.fromHtml(this.mND_PhanTich.get(0).replace("ldpro", "<font color='#FF0000'>")));
//                    int KKK = this.mND_PhanTich.get(0).indexOf("ldpro");
//                    if (KKK > -1) {
//                        try {
//                            this.editTsuatin.setSelection(KKK);
//                        } catch (Exception e) {
//                        }
//                    }
//                    this.sp_TenKH.setSelection(this.mContact.indexOf(this.mTenKH.get(0)));
//                    this.error = false;
//                } else {
//                    this.editTsuatin.setText(this.mND_DaSua.get(0));
//                }
//            } else {
//                this.lv_position = -1;
//                this.error = false;
//            }
//        }
//        if (item.getItemId() == 2) {
//            if (this.lv_position >= 0) {
//                new MainActivity();
//                String mDate = MainActivity.Get_date();
//                Database database2 = this.db;
//                database2.QueryData("Delete FROM tbl_tinnhanS WHERE phat_hien_loi <> 'ok' And ngay_nhan = '" + mDate + "'");
//                this.lv_position = -1;
//                xem_lv();
//                Toast.makeText(getActivity(), "Xoá thành công", 1).show();
//                this.editTsuatin.setText("");
//            }
//            xem_lv();
//        }
//        return true;
//    }
//
//    public void control_RadioButton() {
//        LinearLayout li_KhachHang = (LinearLayout) this.v.findViewById(R.id.li_KhachHang);
//        LinearLayout li_Button = (LinearLayout) this.v.findViewById(R.id.li_button);
//        LinearLayout li_edittinnhan = (LinearLayout) this.v.findViewById(R.id.edittinnhan);
//        if (this.radio_SuaTin.isChecked()) {
//            li_edittinnhan.setVisibility(0);
//            li_KhachHang.setVisibility(0);
//            li_Button.setVisibility(0);
//            this.btn_LoadTin.setVisibility(8);
//            this.btn_suatin.setVisibility(0);
//            this.editTsuatin.setVisibility(0);
//            this.btn_tai_All.setVisibility(8);
//            xem_lv();
//        } else if (this.radio_TaiTin.isChecked()) {
//            li_edittinnhan.setVisibility(0);
//            li_KhachHang.setVisibility(0);
//            li_Button.setVisibility(0);
//            this.btn_suatin.setVisibility(8);
//            this.btn_LoadTin.setVisibility(0);
//            this.editTsuatin.setVisibility(8);
//            this.btn_tai_All.setVisibility(0);
//            xem_lv();
//        }
//    }
//
//    public void getAllChat(int Type_kh) {
//        String sql;
//        new MainActivity();
//        String mDate = MainActivity.Get_date();
//        int soTN = 0;
//        this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "'");
//        this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + mDate + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + "'");
//        Database database = this.db;
//        StringBuilder sb = new StringBuilder();
//        sb.append("Select * From tbl_kh_new Where sdt = '");
//        sb.append(this.mMobile.get(this.spin_pointion));
//        sb.append("'");
//        Cursor cur1 = database.GetData(sb.toString());
//        cur1.moveToFirst();
//        int i = 3;
//        if (Type_kh == 1) {
//            sql = "Select * From Chat_database Where ngay_nhan = '" + mDate + "' AND ten_kh = '" + this.mContact.get(this.spin_pointion) + "' and type_kh = 1";
//        } else if (Type_kh == 2) {
//            sql = "Select * From Chat_database Where ngay_nhan = '" + mDate + "' AND ten_kh = '" + this.mContact.get(this.spin_pointion) + "' and type_kh = 2";
//        } else if (Type_kh == 3) {
//            sql = "Select * From Chat_database Where ngay_nhan = '" + mDate + "' AND ten_kh = '" + this.mContact.get(this.spin_pointion) + "'";
//        } else {
//            sql = null;
//        }
//        Cursor curSQL = this.db.GetData(sql);
//        SQLiteDatabase database2 = this.db.getWritableDatabase();
//        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//        database2.beginTransaction();
//        try {
//            if (curSQL.getCount() > 0) {
//                while (curSQL.moveToNext()) {
//                    soTN++;
//                    ih.prepareForInsert();
//                    ih.bind(ih.getColumnIndex("ngay_nhan"), mDate);
//                    ih.bind(ih.getColumnIndex("gio_nhan"), curSQL.getString(2));
//                    ih.bind(ih.getColumnIndex("type_kh"), curSQL.getString(i));
//                    ih.bind(ih.getColumnIndex("ten_kh"), this.mContact.get(this.spin_pointion));
//                    ih.bind(ih.getColumnIndex("so_dienthoai"), this.mMobile.get(this.spin_pointion));
//                    ih.bind(ih.getColumnIndex("use_app"), cur1.getInt(2));
//                    ih.bind(ih.getColumnIndex("so_tin_nhan"), soTN);
//                    ih.bind(ih.getColumnIndex("nd_goc"), curSQL.getString(7));
//                    ih.bind(ih.getColumnIndex("nd_sua"), curSQL.getString(7));
//                    ih.bind(ih.getColumnIndex("nd_phantich"), curSQL.getString(7));
//                    ih.bind(ih.getColumnIndex("phat_hien_loi"), "ko");
//                    ih.bind(ih.getColumnIndex("tinh_tien"), 0);
//                    ih.bind(ih.getColumnIndex("ok_tn"), 0);
//                    ih.bind(ih.getColumnIndex("del_sms"), 0);
//                    ih.execute();
//                    i = 3;
//                }
//            }
//            curSQL.close();
//            database2.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } catch (Throwable th) {
//            database2.endTransaction();
//            ih.close();
//            database2.close();
//            throw th;
//        }
//        database2.endTransaction();
//        ih.close();
//        database2.close();
//        this.db.QueryData("Delete From tbl_tinnhanS where substr(nd_goc,0,7) = 'Ok Tin'");
//        this.db.QueryData("Delete From tbl_tinnhanS where length(nd_goc) < 4");
//        xem_lv();
//        Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//        cur1.close();
//    }
//
//    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:296:0x0521 */
//    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:33:0x01f7 */
//    /* JADX WARN: Multi-variable type inference failed */
//    /* JADX WARN: Type inference failed for: r6v2, types: [org.json.JSONObject] */
//    /* JADX WARN: Type inference failed for: r9v20 */
//    /* JADX WARN: Type inference failed for: r15v8, types: [android.database.DatabaseUtils$InsertHelper] */
//    /* JADX WARN: Type inference failed for: r9v21 */
//    /* JADX WARN: Type inference failed for: r8v11, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r7v6, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r4v10, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r3v37, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r37v10 */
//    /* JADX WARN: Type inference failed for: r15v11 */
//    /* JADX WARN: Type inference failed for: r9v24 */
//    /* JADX WARN: Type inference failed for: r7v9 */
//    /* JADX WARN: Type inference failed for: r4v13 */
//    /* JADX WARN: Type inference failed for: r3v39 */
//    /* JADX WARN: Type inference failed for: r3v42, types: [java.lang.Long] */
//    /* JADX WARN: Type inference failed for: r9v27, types: [java.lang.StringBuilder] */
//    /* JADX WARN: Type inference failed for: r15v13, types: [java.lang.CharSequence, java.lang.String] */
//    /* JADX WARN: Type inference failed for: r37v12 */
//    /* JADX WARN: Type inference failed for: r9v33, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r15v18 */
//    /* JADX WARN: Type inference failed for: r2v15, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r12v33, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r37v16 */
//    /* JADX WARN: Type inference failed for: r8v12 */
//    /* JADX WARN: Type inference failed for: r7v18 */
//    /* JADX WARN: Type inference failed for: r15v23 */
//    /* JADX WARN: Type inference failed for: r9v38 */
//    /* JADX WARN: Type inference failed for: r4v25 */
//    /* JADX WARN: Type inference failed for: r37v17 */
//    /* JADX WARN: Type inference failed for: r4v27 */
//    /* JADX WARN: Type inference failed for: r3v58, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r4v28 */
//    /* JADX WARN: Type inference failed for: r13v18, types: [org.json.JSONObject, java.lang.Object] */
//    /* JADX WARN: Type inference failed for: r15v25, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r7v21 */
//    /* JADX WARN: Type inference failed for: r7v25 */
//    /* JADX WARN: Type inference failed for: r9v47, types: [org.json.JSONObject] */
//    /* JADX WARN: Type inference failed for: r4v39, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r9v51 */
//    /* JADX WARN: Type inference failed for: r37v23 */
//    /* JADX WARN: Type inference failed for: r37v25, types: [int] */
//    /* JADX WARN: Type inference failed for: r9v55, types: [android.database.DatabaseUtils$InsertHelper] */
//    /* JADX WARN: Type inference failed for: r4v42, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r37v27 */
//    /* JADX WARN: Type inference failed for: r4v45, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r7v31, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r9v56 */
//    /* JADX WARN: Type inference failed for: r9v59 */
//    /* JADX WARN: Type inference failed for: r4v48, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r7v33, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r4v51, types: [java.lang.String] */
//    /* JADX WARN: Type inference failed for: r7v34 */
//    /* JADX WARN: Type inference failed for: r4v63 */
//    /* JADX WARN: Type inference failed for: r7v36 */
//    /* JADX WARN: Type inference failed for: r15v27 */
//    /* JADX WARN: Type inference failed for: r7v37 */
//    /* JADX WARN: Type inference failed for: r7v38 */
//    /* JADX WARN: Type inference failed for: r7v39 */
//    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0357, code lost:
//        r0 = move-exception;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0358, code lost:
//        r1 = r0;
//        r9 = r36;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:102:0x035f, code lost:
//        r0 = move-exception;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0360, code lost:
//        r2 = r0;
//        r1 = r29;
//        r9 = r36;
//        r11 = r37;
//        r36 = r8;
//        r37 = r33;
//        r33 = r4;
//        r4 = r32;
//        r32 = r31;
//        r31 = r7;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:143:0x0466, code lost:
//        r0 = e;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:144:0x0467, code lost:
//        r1 = r29;
//        r37 = r33;
//        r9 = r36;
//        r33 = r4;
//        r36 = r8;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:146:0x048e, code lost:
//        r0 = e;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:147:0x048f, code lost:
//        r1 = r29;
//        r9 = r36;
//        r11 = r37;
//        r36 = r8;
//        r37 = r33;
//        r33 = r4;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:148:0x049b, code lost:
//        r4 = r32;
//        r32 = r31;
//        r31 = r7;
//        r36 = r36;
//        r37 = r37;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:149:0x04a2, code lost:
//        r0 = e;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:150:0x04a3, code lost:
//        r1 = r29;
//        r4 = r32;
//        r9 = r36;
//        r11 = r37;
//        r36 = r8;
//        r32 = r31;
//        r37 = r33;
//        r31 = r7;
//        r33 = r22;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:151:0x04b5, code lost:
//        r7 = r30;
//        r36 = r36;
//        r37 = r37;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:152:0x04b8, code lost:
//        r0 = move-exception;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:153:0x04b9, code lost:
//        r9 = r36;
//        r1 = r0;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:157:0x04d8, code lost:
//        r0 = e;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:158:0x04d9, code lost:
//        r1 = r29;
//        r7 = r30;
//        r4 = r32;
//        r9 = r36;
//        r11 = r37;
//        r36 = r8;
//        r3 = r3;
//        r15 = r15;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:159:0x04e9, code lost:
//        r0 = e;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:160:0x04ea, code lost:
//        r1 = r29;
//        r4 = r32;
//        r9 = r36;
//        r11 = r37;
//        r3 = r3;
//        r15 = r15;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:167:0x0549, code lost:
//        r0 = e;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:168:0x054a, code lost:
//        r3 = r15;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:172:0x0564, code lost:
//        r0 = e;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:173:0x0565, code lost:
//        r3 = r1;
//        r1 = r29;
//        r4 = r32;
//        r9 = r36;
//        r11 = r37;
//        r15 = r13;
//        r5 = r12;
//        r36 = r8;
//        r7 = r30;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:174:0x0579, code lost:
//        r0 = e;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:175:0x057a, code lost:
//        r3 = r1;
//        r5 = r12;
//        r15 = r13;
//        r1 = r29;
//        r7 = r30;
//        r4 = r32;
//        r9 = r36;
//        r11 = r37;
//        r36 = r8;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:176:0x058b, code lost:
//        r32 = r31;
//        r37 = r33;
//        r31 = r2;
//        r3 = r3;
//        r15 = r15;
//        r36 = r36;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:178:0x0594, code lost:
//        r0 = move-exception;
//     */
//    /* JADX WARNING: Code restructure failed: missing block: B:179:0x0595, code lost:
//        r9 = r36;
//        r1 = r0;
//     */
//    /* JADX WARNING: Failed to process nested try/catch */
//    /* JADX WARNING: Removed duplicated region for block: B:100:0x0357 A[ExcHandler: all (r0v36 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:79:0x030e] */
//    /* JADX WARNING: Removed duplicated region for block: B:108:0x037c  */
//    /* JADX WARNING: Removed duplicated region for block: B:145:0x0472 A[Catch:{ Exception -> 0x0603 }] */
//    /* JADX WARNING: Removed duplicated region for block: B:152:0x04b8 A[Catch:{ Exception -> 0x0603 }, ExcHandler: all (r0v34 'th' java.lang.Throwable A[CUSTOM_DECLARE, Catch:{ Exception -> 0x0603 }]), Splitter:B:72:0x02ff] */
//    /* JADX WARNING: Removed duplicated region for block: B:178:0x0594 A[Catch:{ Exception -> 0x0603 }, ExcHandler: all (r0v21 'th' java.lang.Throwable A[CUSTOM_DECLARE, Catch:{ Exception -> 0x0603 }]), Splitter:B:36:0x0210] */
//    /* JADX WARNING: Removed duplicated region for block: B:208:0x069c A[Catch:{ Exception -> 0x086e, all -> 0x0865 }] */
//    /* JADX WARNING: Unknown variable types count: 7 */
//    public void getFullSms(String mName) throws ParseException {
//        JSONException e;
//        Cursor cur1;
//        String str2;
//        String str3;
//        String str4;
//        DatabaseUtils.InsertHelper ih;
//        SQLiteDatabase database;
//        String str5;
//        String Ngay;
//        String mDate;
//        String str6;
//        Cursor cur2;
//        SQLiteDatabase database2;
//        DatabaseUtils.InsertHelper ih2;
//        Throwable th;
//        Exception e2;
//        String str7;
//        String str8;
//        Cursor cur22;
//        String str9;
//        String str10;
//        String str11;
//        boolean KT_type;
//        String typeKT;
//        boolean KT_type2;
//        SQLiteDatabase database3;
//        DatabaseUtils.InsertHelper ih3;
//        Throwable th2;
//        String str12;
//        SQLiteDatabase sQLiteDatabase;
//        DatabaseUtils.InsertHelper ih4;
//        Exception e3;
//        String str13;
//        DatabaseUtils.InsertHelper insertHelper;
//        String str14;
//        DatabaseUtils.InsertHelper ih5;
//        String str15;
//        String mDate2;
//        String str16;
//        Exception e4;
//        String r37;
//        DatabaseUtils.InsertHelper ih6;
//        Object obj;
//        String str17;
//        String Ngay2;
//        String str18;
//        boolean KT_type3;
//        int columnIndex;
//        String str19 = " ";
//        String str20 = "";
//        new MainActivity();
//        String Get_ngay = MainActivity.Get_ngay();
//        String mDate3 = MainActivity.Get_date();
//        String str21 = "'";
//        if (!MainActivity.jSon_Setting.has("tin_trung")) {
//            try {
//                MainActivity.jSon_Setting.put("tin_trung", 0);
//                this.db.QueryData("Update tbl_Setting set Setting = '" + MainActivity.jSon_Setting.toString() + str21);
//            } catch (JSONException e5) {
//                e5.printStackTrace();
//            }
//        }
//        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.READ_SMS") == 0) {
//            try {
//                Date dateStart = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(mDate3 + "T00:00:00");
//                String filter = "date>=" + dateStart.getTime();
//                if (mName.indexOf("Full") > -1) {
//                    try {
//                        this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + mDate3 + str21);
//                        this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + mDate3 + str21);
//                        cur1 = this.db.GetData("Select * From tbl_kh_new");
//                    } catch (SQLiteException e6) {
//                        return;
//                    } catch (JSONException e7) {
//                        e = e7;
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        this.db.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + mDate3 + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + str21);
//                        this.db.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + mDate3 + "' AND so_dienthoai = '" + this.mMobile.get(this.spin_pointion) + str21);
//                        Database database4 = this.db;
//                        StringBuilder sb = new StringBuilder();
//                        sb.append("Select * From tbl_kh_new Where sdt = '");
//                        sb.append(this.mMobile.get(this.spin_pointion));
//                        sb.append(str21);
//                        cur1 = database4.GetData(sb.toString());
//                    } catch (SQLiteException e8) {
//                        return;
//                    } catch (JSONException e9) {
//                        e = e9;
//                        e.printStackTrace();
//                    }
//                }
//                JSONObject jSONObject = new JSONObject();
//                while (true) {
//                    str2 = "so_tn";
//                    str3 = "type_kh";
//                    if (!cur1.moveToNext()) {
//                        break;
//                    }
//                    JSONObject json_kh = new JSONObject();
//                    json_kh.put(str3, cur1.getString(3));
//                    json_kh.put("ten_kh", cur1.getString(0));
//                    json_kh.put(str2, 0);
//                    jSONObject.put(cur1.getString(1), json_kh);
//                    dateStart = dateStart;
//                }
//                Cursor c = getActivity().getContentResolver().query(Uri.parse("content://sms"), null, filter, null, "date ASC");
//                getActivity().startManagingCursor(c);
//                int totalSMS = c.getCount();
//                SQLiteDatabase database5 = this.db.getWritableDatabase();
//                String str22 = "tin_trung";
//                DatabaseUtils.InsertHelper ih7 = new DatabaseUtils.InsertHelper(database5, "tbl_tinnhanS");
//                SQLiteDatabase sQLiteDatabase2 = "so_dienthoai";
//                String str23 = "ten_kh";
//                String str24 = "3";
//                String str25 = "gio_nhan";
//                String mGioNhan = "Ok Tin";
//                String mDate4 = mDate3;
//                String mDate5 = "1";
//                String str26 = "ngay_nhan";
//                String str27 = "2";
//                if (c.moveToFirst()) {
//                    try {
//                        database5.beginTransaction();
//                        database3 = database5;
//                        DatabaseUtils.InsertHelper ih8 = 0;
//                        String mDate6 = str19;
//                        String Ngay3 = Get_ngay;
//                        String str28 = ih7;
//                        SQLiteDatabase str29 = sQLiteDatabase2;
//                        while (ih8 < totalSMS) {
//                            String str30 = "date";
//                            try {
//                                str30 = Long.valueOf(c.getLong(c.getColumnIndexOrThrow(str30)));
//                                ih8 = new StringBuilder();
//                                ih6 = str28;
//                                str28 = "dd/MM/yyyy HH:mm:ss";
//                                r37 = str2;
//                            } catch (Exception e10) {
//                                str6 = mGioNhan;
//                                ih = mDate5;
//                                str21 = str2;
//                                str20 = str24;
//                                ih4 = str28;
//                                mGioNhan = str29;
//                                Ngay = str23;
//                                mDate = mDate4;
//                                str4 = str26;
//                                str5 = str3;
//                                str26 = str22;
//                                mDate4 = str25;
//                                str25 = str27;
//                                e3 = e10;
//                                sQLiteDatabase = str29;
//                                try {
//                                    e3.printStackTrace();
//                                    database3.endTransaction();
//                                    ih4.close();
//                                    str12 = sQLiteDatabase;
//                                    database3.close();
//                                    database = str12;
//                                    cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                    database2 = this.db.getWritableDatabase();
//                                    ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                    database2.beginTransaction();
//                                    while (cur2.moveToNext()) {
//                                    }
//                                    database = database2;
//                                    database.setTransactionSuccessful();
//                                    database.endTransaction();
//                                    ih2.close();
//                                    database.close();
//                                    xem_lv();
//                                    Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                } catch (Throwable th3) {
//                                    th2 = th3;
//                                    ih3 = ih4;
//                                    database3.endTransaction();
//                                    ih3.close();
//                                    database3.close();
//                                    throw th2;
//                                }
//                            } catch (Throwable th4) {
//                                ih3 = str28;
//                                th2 = th4;
//                                database3.endTransaction();
//                                ih3.close();
//                                database3.close();
//                                throw th2;
//                            }
//                            try {
//                                ih8.append(DateFormat.format((CharSequence) str28, new Date(str30.longValue())));
//                                ih8.append(str20);
//                                ih8 = ih8.toString();
//                                StringBuilder sb2 = new StringBuilder();
//                                str28 = mGioNhan;
//                                sb2.append((Object) DateFormat.format("HH:mm:ss", new Date(str30.longValue())));
//                                sb2.append(str20);
//                                mGioNhan = sb2.toString();
//                                String mSDT2 = c.getString(c.getColumnIndexOrThrow("address")).replaceAll(mDate6, str20);
//                                String body = c.getString(c.getColumnIndexOrThrow("body")).toString().replaceAll(str21, mDate6).replaceAll("\"", mDate6);
//                                ?? string = c.getString(c.getColumnIndexOrThrow("type"));
//                                if (mSDT2.length() < 12) {
//                                    try {
//                                        StringBuilder sb3 = new StringBuilder();
//                                        sb3.append("+84");
//                                        obj = mDate6;
//                                        sb3.append(mSDT2.substring(1));
//                                        mSDT2 = sb3.toString();
//                                    } catch (Exception e11) {
//                                        e3 = e11;
//                                        str6 = str28;
//                                        mGioNhan = str29;
//                                        mDate = mDate4;
//                                        ih4 = ih6;
//                                        str21 = r37;
//                                        str5 = str3;
//                                        str20 = str24;
//                                        ih = mDate5;
//                                        Ngay = str23;
//                                        mDate4 = str25;
//                                        str4 = str26;
//                                        str25 = str27;
//                                        str26 = str22;
//                                        sQLiteDatabase = str29;
//                                        e3.printStackTrace();
//                                        database3.endTransaction();
//                                        ih4.close();
//                                        str12 = sQLiteDatabase;
//                                        database3.close();
//                                        database = str12;
//                                        cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                        database2 = this.db.getWritableDatabase();
//                                        ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                        database2.beginTransaction();
//                                        while (cur2.moveToNext()) {
//                                        }
//                                        database = database2;
//                                        database.setTransactionSuccessful();
//                                        database.endTransaction();
//                                        ih2.close();
//                                        database.close();
//                                        xem_lv();
//                                        Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                    } catch (Throwable th5) {
//                                        th2 = th5;
//                                        ih3 = ih6;
//                                        database3.endTransaction();
//                                        ih3.close();
//                                        database3.close();
//                                        throw th2;
//                                    }
//                                } else {
//                                    obj = mDate6;
//                                }
//                                if (jSONObject.has(mSDT2)) {
//                                    mDate6 = -1;
//                                    if (ih8.indexOf(Ngay3) > -1) {
//                                        str30 = str28;
//                                        if (body.indexOf((String) str30) == -1) {
//                                            mDate6 = 0;
//                                            JSONObject jSONObject2 = jSONObject.getJSONObject(mSDT2);
//                                            str28 = str3;
//                                            str17 = str20;
//                                            str20 = str24;
//                                            Ngay2 = Ngay3;
//                                            Ngay3 = -1;
//                                            if (jSONObject2.getString(str28).indexOf(str20) > -1) {
//                                                KT_type3 = true;
//                                                Ngay3 = str27;
//                                            } else {
//                                                try {
//                                                    Ngay3 = str27;
//                                                    if (jSONObject2.getString(str28).indexOf(Ngay3) > -1) {
//                                                        try {
//                                                            if (string.indexOf(Ngay3) > -1) {
//                                                                KT_type3 = true;
//                                                                Ngay3 = Ngay3;
//                                                            }
//                                                        } catch (Exception e12) {
//                                                            e3 = e12;
//                                                            mGioNhan = str29;
//                                                            String str31 = mDate4;
//                                                            DatabaseUtils.InsertHelper insertHelper2 = ih6;
//                                                            str21 = r37;
//                                                            DatabaseUtils.InsertHelper ih9 = mDate5;
//                                                            mDate4 = str25;
//                                                            String str32 = str26;
//                                                            str25 = Ngay3;
//                                                            str26 = str22;
//                                                            Ngay = str23;
//                                                            str6 = str30;
//                                                            mDate = str31;
//                                                            ih4 = insertHelper2;
//                                                            str5 = str28;
//                                                            sQLiteDatabase = str29;
//                                                            ih = ih9;
//                                                            str4 = str32;
//                                                            e3.printStackTrace();
//                                                            database3.endTransaction();
//                                                            ih4.close();
//                                                            database3.close();
//                                                            database = sQLiteDatabase;
//                                                            cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                                            database2 = this.db.getWritableDatabase();
//                                                            ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                                            database2.beginTransaction();
//                                                            while (cur2.moveToNext()) {
//                                                            }
//                                                            database = database2;
//                                                            database.setTransactionSuccessful();
//                                                            database.endTransaction();
//                                                            ih2.close();
//                                                            database.close();
//                                                            xem_lv();
//                                                            Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                                        } catch (Throwable th6) {
//                                                        }
//                                                    }
//                                                    if (jSONObject2.getString(str28).indexOf(mDate5) <= -1 || string.indexOf(mDate5) <= -1) {
//                                                        KT_type3 = false;
//                                                        Ngay3 = Ngay3;
//                                                    } else {
//                                                        KT_type3 = true;
//                                                        Ngay3 = Ngay3;
//                                                    }
//                                                } catch (Exception e13) {
//                                                    Exception e14 = e13;
//                                                    mGioNhan = str29;
//                                                    String str33 = str23;
//                                                    String str34 = mDate4;
//                                                    DatabaseUtils.InsertHelper insertHelper3 = ih6;
//                                                    str21 = r37;
//                                                    DatabaseUtils.InsertHelper ih10 = mDate5;
//                                                    mDate4 = str25;
//                                                    String str35 = str26;
//                                                    str25 = str27;
//                                                    str26 = str22;
//                                                    e3 = e14;
//                                                    str6 = str30;
//                                                    mDate = str34;
//                                                    Ngay = str33;
//                                                    ih4 = insertHelper3;
//                                                    str5 = str28;
//                                                    sQLiteDatabase = str29;
//                                                    ih = ih10;
//                                                    str4 = str35;
//                                                    e3.printStackTrace();
//                                                    database3.endTransaction();
//                                                    ih4.close();
//                                                    str12 = sQLiteDatabase;
//                                                    database3.close();
//                                                    database = str12;
//                                                    cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                                    database2 = this.db.getWritableDatabase();
//                                                    ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                                    database2.beginTransaction();
//                                                    while (cur2.moveToNext()) {
//                                                    }
//                                                    database = database2;
//                                                    database.setTransactionSuccessful();
//                                                    database.endTransaction();
//                                                    ih2.close();
//                                                    database.close();
//                                                    xem_lv();
//                                                    Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                                } catch (Throwable th7) {
//                                                }
//                                            }
//                                            ih8 = MainActivity.jSon_Setting;
//                                            mDate6 = str22;
//                                            str18 = str21;
//                                            if (ih8.getInt(mDate6) == 1) {
//                                                if (jSONObject2.has(body)) {
//                                                    ih8 = 0;
//                                                    if (ih8 != 1) {
//                                                        str21 = r37;
//                                                        r37 = jSONObject2.getInt(str21);
//                                                        jSONObject2.put(str21, r37 + 1);
//                                                        jSONObject2.put(body, body);
//                                                        ih6.prepareForInsert();
//                                                        ih8 = ih6;
//                                                        ih6 = mDate5;
//                                                        str26 = mDate6;
//                                                        mDate6 = str26;
//                                                        try {
//                                                            columnIndex = ih8.getColumnIndex(mDate6);
//                                                            r37 = mDate6;
//                                                            mDate6 = mDate4;
//                                                        } catch (Exception e15) {
//                                                            str4 = mDate6;
//                                                            mGioNhan = str29;
//                                                            mDate = mDate4;
//                                                            mDate4 = str25;
//                                                            str25 = Ngay3;
//                                                            Ngay = str23;
//                                                            e3 = e15;
//                                                            str6 = str30;
//                                                            ih4 = ih8;
//                                                            str5 = str28;
//                                                            sQLiteDatabase = str29;
//                                                            ih = ih6;
//                                                            e3.printStackTrace();
//                                                            database3.endTransaction();
//                                                            ih4.close();
//                                                            str12 = sQLiteDatabase;
//                                                            database3.close();
//                                                            database = str12;
//                                                            cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                                            database2 = this.db.getWritableDatabase();
//                                                            ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                                            database2.beginTransaction();
//                                                            while (cur2.moveToNext()) {
//                                                            }
//                                                            database = database2;
//                                                            database.setTransactionSuccessful();
//                                                            database.endTransaction();
//                                                            ih2.close();
//                                                            database.close();
//                                                            xem_lv();
//                                                            Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                                        } catch (Throwable th8) {
//                                                            th2 = th8;
//                                                            ih3 = ih8;
//                                                            database3.endTransaction();
//                                                            ih3.close();
//                                                            database3.close();
//                                                            throw th2;
//                                                        }
//                                                        try {
//                                                            ih8.bind(columnIndex, mDate6);
//                                                            str25 = Ngay3;
//                                                            try {
//                                                                ih8.bind(ih8.getColumnIndex(str25), mGioNhan);
//                                                                ih8.bind(ih8.getColumnIndex(str28), string);
//                                                                Ngay3 = str23;
//                                                                try {
//                                                                    mDate4 = str25;
//                                                                } catch (Exception e16) {
//                                                                    e4 = e16;
//                                                                    mDate4 = str25;
//                                                                    mGioNhan = str29;
//                                                                    str16 = str30;
//                                                                    mDate2 = mDate6;
//                                                                    str15 = Ngay3;
//                                                                    ih5 = ih8;
//                                                                    str14 = str28;
//                                                                    insertHelper = ih6;
//                                                                    str13 = r37;
//                                                                    e3 = e4;
//                                                                    str6 = str16;
//                                                                    mDate = mDate2;
//                                                                    Ngay = str15;
//                                                                    ih4 = ih5;
//                                                                    str5 = str14;
//                                                                    sQLiteDatabase = str29;
//                                                                    ih = insertHelper;
//                                                                    str4 = str13;
//                                                                    e3.printStackTrace();
//                                                                    database3.endTransaction();
//                                                                    ih4.close();
//                                                                    str12 = sQLiteDatabase;
//                                                                    database3.close();
//                                                                    database = str12;
//                                                                    cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                                                    database2 = this.db.getWritableDatabase();
//                                                                    ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                                                    database2.beginTransaction();
//                                                                    while (cur2.moveToNext()) {
//                                                                    }
//                                                                    database = database2;
//                                                                    database.setTransactionSuccessful();
//                                                                    database.endTransaction();
//                                                                    ih2.close();
//                                                                    database.close();
//                                                                    xem_lv();
//                                                                    Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                                                }
//                                                            } catch (Exception e17) {
//                                                                e4 = e17;
//                                                                mDate4 = str25;
//                                                                mGioNhan = str29;
//                                                                str15 = str23;
//                                                                str16 = str30;
//                                                                mDate2 = mDate6;
//                                                                ih5 = ih8;
//                                                                str14 = str28;
//                                                                insertHelper = ih6;
//                                                                str13 = r37;
//                                                                e3 = e4;
//                                                                str6 = str16;
//                                                                mDate = mDate2;
//                                                                Ngay = str15;
//                                                                ih4 = ih5;
//                                                                str5 = str14;
//                                                                sQLiteDatabase = str29;
//                                                                ih = insertHelper;
//                                                                str4 = str13;
//                                                                e3.printStackTrace();
//                                                                database3.endTransaction();
//                                                                ih4.close();
//                                                                str12 = sQLiteDatabase;
//                                                                database3.close();
//                                                                database = str12;
//                                                                cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                                                database2 = this.db.getWritableDatabase();
//                                                                ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                                                database2.beginTransaction();
//                                                                while (cur2.moveToNext()) {
//                                                                }
//                                                                database = database2;
//                                                                database.setTransactionSuccessful();
//                                                                database.endTransaction();
//                                                                ih2.close();
//                                                                database.close();
//                                                                xem_lv();
//                                                                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                                            }
//                                                        } catch (Exception e18) {
//                                                            e4 = e18;
//                                                            mGioNhan = str29;
//                                                            mDate4 = str25;
//                                                            str25 = Ngay3;
//                                                            str15 = str23;
//                                                            str16 = str30;
//                                                            mDate2 = mDate6;
//                                                            ih5 = ih8;
//                                                            str14 = str28;
//                                                            insertHelper = ih6;
//                                                            str13 = r37;
//                                                            e3 = e4;
//                                                            str6 = str16;
//                                                            mDate = mDate2;
//                                                            Ngay = str15;
//                                                            ih4 = ih5;
//                                                            str5 = str14;
//                                                            sQLiteDatabase = str29;
//                                                            ih = insertHelper;
//                                                            str4 = str13;
//                                                            e3.printStackTrace();
//                                                            database3.endTransaction();
//                                                            ih4.close();
//                                                            str12 = sQLiteDatabase;
//                                                            database3.close();
//                                                            database = str12;
//                                                            cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                                            database2 = this.db.getWritableDatabase();
//                                                            ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                                            database2.beginTransaction();
//                                                            while (cur2.moveToNext()) {
//                                                            }
//                                                            database = database2;
//                                                            database.setTransactionSuccessful();
//                                                            database.endTransaction();
//                                                            ih2.close();
//                                                            database.close();
//                                                            xem_lv();
//                                                            Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                                        }
//                                                        try {
//                                                            ih8.bind(ih8.getColumnIndex(Ngay3), jSONObject2.getString(Ngay3));
//                                                            mGioNhan = str29;
//                                                            try {
//                                                                ih8.bind(ih8.getColumnIndex(mGioNhan), mSDT2);
//                                                                ih8.bind(ih8.getColumnIndex("use_app"), "sms");
//                                                                ih8.bind(ih8.getColumnIndex("so_tin_nhan"), jSONObject2.getInt(str21));
//                                                                ih8.bind(ih8.getColumnIndex("nd_goc"), body);
//                                                                ih8.bind(ih8.getColumnIndex("nd_sua"), body);
//                                                                ih8.bind(ih8.getColumnIndex("nd_phantich"), body);
//                                                                ih8.bind(ih8.getColumnIndex("phat_hien_loi"), "ko");
//                                                                ih8.bind(ih8.getColumnIndex("tinh_tien"), 0);
//                                                                ih8.bind(ih8.getColumnIndex("ok_tn"), 0);
//                                                                ih8.bind(ih8.getColumnIndex("del_sms"), 0);
//                                                                ih8.execute();
//                                                                jSONObject.put(mSDT2, jSONObject2);
//                                                            } catch (Exception e19) {
//                                                                e4 = e19;
//                                                                str16 = str30;
//                                                                mDate2 = mDate6;
//                                                                str15 = Ngay3;
//                                                                ih5 = ih8;
//                                                                str14 = str28;
//                                                                insertHelper = ih6;
//                                                                str13 = r37;
//                                                                e3 = e4;
//                                                                str6 = str16;
//                                                                mDate = mDate2;
//                                                                Ngay = str15;
//                                                                ih4 = ih5;
//                                                                str5 = str14;
//                                                                sQLiteDatabase = str29;
//                                                                ih = insertHelper;
//                                                                str4 = str13;
//                                                                e3.printStackTrace();
//                                                                database3.endTransaction();
//                                                                ih4.close();
//                                                                str12 = sQLiteDatabase;
//                                                                database3.close();
//                                                                database = str12;
//                                                                cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                                                database2 = this.db.getWritableDatabase();
//                                                                ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                                                database2.beginTransaction();
//                                                                while (cur2.moveToNext()) {
//                                                                }
//                                                                database = database2;
//                                                                database.setTransactionSuccessful();
//                                                                database.endTransaction();
//                                                                ih2.close();
//                                                                database.close();
//                                                                xem_lv();
//                                                                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                                            }
//                                                        } catch (Exception e20) {
//                                                            e4 = e20;
//                                                            mGioNhan = str29;
//                                                            str16 = str30;
//                                                            mDate2 = mDate6;
//                                                            str15 = Ngay3;
//                                                            ih5 = ih8;
//                                                            str14 = str28;
//                                                            insertHelper = ih6;
//                                                            str13 = r37;
//                                                            e3 = e4;
//                                                            str6 = str16;
//                                                            mDate = mDate2;
//                                                            Ngay = str15;
//                                                            ih4 = ih5;
//                                                            str5 = str14;
//                                                            sQLiteDatabase = str29;
//                                                            ih = insertHelper;
//                                                            str4 = str13;
//                                                            e3.printStackTrace();
//                                                            database3.endTransaction();
//                                                            ih4.close();
//                                                            str12 = sQLiteDatabase;
//                                                            database3.close();
//                                                            database = str12;
//                                                            cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                                            database2 = this.db.getWritableDatabase();
//                                                            ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                                            database2.beginTransaction();
//                                                            while (cur2.moveToNext()) {
//                                                            }
//                                                            database = database2;
//                                                            database.setTransactionSuccessful();
//                                                            database.endTransaction();
//                                                            ih2.close();
//                                                            database.close();
//                                                            xem_lv();
//                                                            Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                                        }
//                                                    } else {
//                                                        ih8 = ih6;
//                                                        str21 = r37;
//                                                        ih6 = mDate5;
//                                                        r37 = str26;
//                                                        str26 = mDate6;
//                                                        mDate6 = mDate4;
//                                                        mDate4 = str25;
//                                                        str25 = Ngay3;
//                                                        Ngay3 = str23;
//                                                        mGioNhan = str29;
//                                                    }
//                                                    c.moveToNext();
//                                                    str29 = mGioNhan;
//                                                    mGioNhan = str30;
//                                                    str24 = str20;
//                                                    str23 = Ngay3;
//                                                    str2 = str21;
//                                                    str3 = str28;
//                                                    str21 = str18;
//                                                    totalSMS = totalSMS;
//                                                    str22 = str26;
//                                                    mDate5 = ih6;
//                                                    str26 = r37;
//                                                    Ngay3 = Ngay2;
//                                                    str20 = str17;
//                                                    str28 = ih8;
//                                                    ih8 = (ih8 == true ? 1 : 0) + 1;
//                                                    str27 = str25;
//                                                    str25 = mDate4;
//                                                    mDate4 = mDate6;
//                                                    mDate6 = obj;
//                                                }
//                                            }
//                                            ih8 = KT_type3;
//                                            if (ih8 != 1) {
//                                            }
//                                            c.moveToNext();
//                                            str29 = mGioNhan;
//                                            mGioNhan = str30;
//                                            str24 = str20;
//                                            str23 = Ngay3;
//                                            str2 = str21;
//                                            str3 = str28;
//                                            str21 = str18;
//                                            totalSMS = totalSMS;
//                                            str22 = str26;
//                                            mDate5 = ih6;
//                                            str26 = r37;
//                                            Ngay3 = Ngay2;
//                                            str20 = str17;
//                                            str28 = ih8;
//                                            ih8 = (ih8 == true ? 1 : 0) + 1;
//                                            str27 = str25;
//                                            str25 = mDate4;
//                                            mDate4 = mDate6;
//                                            mDate6 = obj;
//                                        } else {
//                                            str17 = str20;
//                                            str30 = str30;
//                                            mDate6 = mDate4;
//                                            str28 = str3;
//                                            str20 = str24;
//                                            Ngay2 = Ngay3;
//                                            Ngay3 = str23;
//                                            mDate4 = str25;
//                                            str25 = str27;
//                                            mGioNhan = str29;
//                                            ih8 = ih6;
//                                            ih6 = mDate5;
//                                            str18 = str21;
//                                            str21 = r37;
//                                            r37 = str26;
//                                            str26 = str22;
//                                            c.moveToNext();
//                                            str29 = mGioNhan;
//                                            mGioNhan = str30;
//                                            str24 = str20;
//                                            str23 = Ngay3;
//                                            str2 = str21;
//                                            str3 = str28;
//                                            str21 = str18;
//                                            totalSMS = totalSMS;
//                                            str22 = str26;
//                                            mDate5 = ih6;
//                                            str26 = r37;
//                                            Ngay3 = Ngay2;
//                                            str20 = str17;
//                                            str28 = ih8;
//                                            ih8 = (ih8 == true ? 1 : 0) + 1;
//                                            str27 = str25;
//                                            str25 = mDate4;
//                                            mDate4 = mDate6;
//                                            mDate6 = obj;
//                                        }
//                                    }
//                                }
//                                str17 = str20;
//                                str30 = str28;
//                                mDate6 = mDate4;
//                                str28 = str3;
//                                str20 = str24;
//                                Ngay2 = Ngay3;
//                                Ngay3 = str23;
//                                mDate4 = str25;
//                                str25 = str27;
//                                mGioNhan = str29;
//                                ih8 = ih6;
//                                ih6 = mDate5;
//                                str18 = str21;
//                                str21 = r37;
//                                r37 = str26;
//                                str26 = str22;
//                                c.moveToNext();
//                                str29 = mGioNhan;
//                                mGioNhan = str30;
//                                str24 = str20;
//                                str23 = Ngay3;
//                                str2 = str21;
//                                str3 = str28;
//                                str21 = str18;
//                                totalSMS = totalSMS;
//                                str22 = str26;
//                                mDate5 = ih6;
//                                str26 = r37;
//                                Ngay3 = Ngay2;
//                                str20 = str17;
//                                str28 = ih8;
//                                ih8 = (ih8 == true ? 1 : 0) + 1;
//                                str27 = str25;
//                                str25 = mDate4;
//                                mDate4 = mDate6;
//                                mDate6 = obj;
//                            } catch (Exception e21) {
//                                Exception e22 = e21;
//                                String str36 = str30;
//                                mGioNhan = str29;
//                                String str37 = mDate4;
//                                DatabaseUtils.InsertHelper insertHelper4 = ih6;
//                                str21 = r37;
//                                String str38 = str3;
//                                String str39 = str36;
//                                str20 = str24;
//                                DatabaseUtils.InsertHelper ih11 = mDate5;
//                                String Ngay4 = str23;
//                                String str40 = str39;
//                                String str41 = str38;
//                                mDate4 = str25;
//                                String str42 = str26;
//                                str25 = str27;
//                                String str43 = str40;
//                                String str44 = str41;
//                                DatabaseUtils.InsertHelper ih12 = ih11;
//                                str26 = str22;
//                                e3 = e22;
//                                str6 = str43;
//                                mDate = str37;
//                                Ngay = Ngay4;
//                                ih4 = insertHelper4;
//                                str5 = str44;
//                                sQLiteDatabase = str29;
//                                ih = ih12;
//                                str4 = str42;
//                                e3.printStackTrace();
//                                database3.endTransaction();
//                                ih4.close();
//                                str12 = sQLiteDatabase;
//                                database3.close();
//                                database = str12;
//                                cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                                database2 = this.db.getWritableDatabase();
//                                ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                                database2.beginTransaction();
//                                while (cur2.moveToNext()) {
//                                }
//                                database = database2;
//                                database.setTransactionSuccessful();
//                                database.endTransaction();
//                                ih2.close();
//                                database.close();
//                                xem_lv();
//                                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                            } catch (Throwable th9) {
//                            }
//                        }
//                        ih = mDate5;
//                        str21 = str2;
//                        str20 = str24;
//                        Ngay = str23;
//                        mDate = mDate4;
//                        str4 = str26;
//                        str6 = mGioNhan;
//                        str5 = str3;
//                        str26 = str22;
//                        mGioNhan = str29;
//                        mDate4 = str25;
//                        str25 = str27;
//                        database3.setTransactionSuccessful();
//                        try {
//                            database3.endTransaction();
//                            str28.close();
//                            str12 = str29;
//                        } catch (SQLiteException e23) {
//                            return;
//                        } catch (JSONException e24) {
//                            e = e24;
//                            e.printStackTrace();
//                        }
//                    } catch (Exception e25) {
//                        ih = mDate5;
//                        database3 = database5;
//                        str21 = str2;
//                        str20 = str24;
//                        ih4 = ih7;
//                        Ngay = str23;
//                        mDate = mDate4;
//                        str4 = str26;
//                        str6 = mGioNhan;
//                        str5 = str3;
//                        str26 = str22;
//                        mGioNhan = sQLiteDatabase2;
//                        mDate4 = str25;
//                        str25 = str27;
//                        e3 = e25;
//                        sQLiteDatabase = sQLiteDatabase2;
//                        e3.printStackTrace();
//                        database3.endTransaction();
//                        ih4.close();
//                        str12 = sQLiteDatabase;
//                        database3.close();
//                        database = str12;
//                        cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                        database2 = this.db.getWritableDatabase();
//                        ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                        database2.beginTransaction();
//                        while (cur2.moveToNext()) {
//                        }
//                        database = database2;
//                        database.setTransactionSuccessful();
//                        database.endTransaction();
//                        ih2.close();
//                        database.close();
//                        xem_lv();
//                        Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                    } catch (Throwable th10) {
//                        database3 = database5;
//                        ih3 = ih7;
//                        th2 = th10;
//                        database3.endTransaction();
//                        ih3.close();
//                        database3.close();
//                        throw th2;
//                    }
//                    database3.close();
//                    database = str12;
//                } else {
//                    ih = mDate5;
//                    str21 = str2;
//                    str20 = str24;
//                    Ngay = str23;
//                    mDate = mDate4;
//                    str4 = str26;
//                    str6 = mGioNhan;
//                    str5 = str3;
//                    str26 = str22;
//                    mGioNhan = sQLiteDatabase2;
//                    mDate4 = str25;
//                    str25 = str27;
//                    database = sQLiteDatabase2;
//                }
//                cur2 = this.db.GetData("Select * From Chat_database Where ngay_nhan = '" + mDate + "' And use_app <> 'sms'");
//                database2 = this.db.getWritableDatabase();
//                ih2 = new DatabaseUtils.InsertHelper(database2, "tbl_tinnhanS");
//                try {
//                    database2.beginTransaction();
//                    while (cur2.moveToNext()) {
//                        String NgayTinNhan = cur2.getString(1);
//                        String mGioNhan2 = cur2.getString(2);
//                        try {
//                            String mSDT3 = cur2.getString(4);
//                            database = database2;
//                            try {
//                                String body2 = cur2.getString(7);
//                                String str45 = mGioNhan;
//                                String typeKT2 = cur2.getString(3);
//                                if (!jSONObject.has(mSDT3)) {
//                                    str8 = str6;
//                                    str7 = str25;
//                                    str11 = str4;
//                                    str9 = str20;
//                                    str10 = ih;
//                                    cur22 = cur2;
//                                } else if (NgayTinNhan.indexOf(mDate) <= -1 || body2.indexOf(str6) != -1) {
//                                    str8 = str6;
//                                    str7 = str25;
//                                    str11 = str4;
//                                    str9 = str20;
//                                    str10 = ih;
//                                    cur22 = cur2;
//                                } else {
//                                    JSONObject Gia_khach = jSONObject.getJSONObject(mSDT3);
//                                    str8 = str6;
//                                    if (Gia_khach.getString(str5).indexOf(str20) > -1) {
//                                        KT_type = true;
//                                        str7 = str25;
//                                        typeKT = typeKT2;
//                                        str9 = str20;
//                                        cur22 = cur2;
//                                        str10 = ih;
//                                    } else {
//                                        int indexOf = Gia_khach.getString(str5).indexOf(str25);
//                                        str9 = str20;
//                                        if (indexOf > -1) {
//                                            typeKT = typeKT2;
//                                            try {
//                                                str7 = str25;
//                                                if (typeKT.indexOf(str25) > -1) {
//                                                    KT_type = true;
//                                                    cur22 = cur2;
//                                                    str10 = ih;
//                                                }
//                                            } catch (Exception e26) {
//                                                e2 = e26;
//                                                try {
//                                                    e2.printStackTrace();
//                                                    database.endTransaction();
//                                                    ih2.close();
//                                                    database.close();
//                                                    xem_lv();
//                                                    Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                                } catch (Throwable th11) {
//                                                    th = th11;
//                                                    database.endTransaction();
//                                                    ih2.close();
//                                                    database.close();
//                                                    throw th;
//                                                }
//                                            } catch (Throwable th12) {
//                                                th = th12;
//                                                database.endTransaction();
//                                                ih2.close();
//                                                database.close();
//                                                throw th;
//                                            }
//                                        } else {
//                                            str7 = str25;
//                                            typeKT = typeKT2;
//                                        }
//                                        str10 = ih;
//                                        cur22 = cur2;
//                                        if (Gia_khach.getString(str5).indexOf(str10) > -1) {
//                                            try {
//                                                if (typeKT.indexOf(str10) > -1) {
//                                                    KT_type = true;
//                                                }
//                                            } catch (Exception e27) {
//                                                e2 = e27;
//                                                e2.printStackTrace();
//                                                database.endTransaction();
//                                                ih2.close();
//                                                database.close();
//                                                xem_lv();
//                                                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                                            }
//                                        }
//                                        KT_type = false;
//                                    }
//                                    int i = MainActivity.jSon_Setting.getInt(str26);
//                                    str26 = str26;
//                                    if (i != 1 || !Gia_khach.has(body2)) {
//                                        KT_type2 = KT_type;
//                                    } else {
//                                        KT_type2 = false;
//                                    }
//                                    if (KT_type2) {
//                                        Gia_khach.put(str21, Gia_khach.getInt(str21) + 1);
//                                        Gia_khach.put(body2, body2);
//                                        ih2.prepareForInsert();
//                                        str11 = str4;
//                                        ih2.bind(ih2.getColumnIndex(str11), mDate);
//                                        ih2.bind(ih2.getColumnIndex(mDate4), mGioNhan2);
//                                        ih2.bind(ih2.getColumnIndex(str5), typeKT);
//                                        ih2.bind(ih2.getColumnIndex(Ngay), Gia_khach.getString(Ngay));
//                                        ih2.bind(ih2.getColumnIndex(str45), mSDT3);
//                                        str45 = str45;
//                                        ih2.bind(ih2.getColumnIndex("use_app"), "sms");
//                                        ih2.bind(ih2.getColumnIndex("so_tin_nhan"), Gia_khach.getInt(str21));
//                                        ih2.bind(ih2.getColumnIndex("nd_goc"), body2);
//                                        ih2.bind(ih2.getColumnIndex("nd_sua"), body2);
//                                        ih2.bind(ih2.getColumnIndex("nd_phantich"), body2);
//                                        ih2.bind(ih2.getColumnIndex("phat_hien_loi"), "ko");
//                                        ih2.bind(ih2.getColumnIndex("tinh_tien"), 0);
//                                        ih2.bind(ih2.getColumnIndex("ok_tn"), 0);
//                                        ih2.bind(ih2.getColumnIndex("del_sms"), 0);
//                                        ih2.execute();
//                                        jSONObject.put(mSDT3, Gia_khach);
//                                    } else {
//                                        str11 = str4;
//                                    }
//                                }
//                                str4 = str11;
//                                ih = str10;
//                                c = c;
//                                database2 = database;
//                                mGioNhan = str45;
//                                str20 = str9;
//                                cur2 = cur22;
//                                str6 = str8;
//                                str25 = str7;
//                            } catch (Exception e28) {
//                                e2 = e28;
//                                e2.printStackTrace();
//                                database.endTransaction();
//                                ih2.close();
//                                database.close();
//                                xem_lv();
//                                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                            } catch (Throwable th13) {
//                                th = th13;
//                                database.endTransaction();
//                                ih2.close();
//                                database.close();
//                                throw th;
//                            }
//                        } catch (Exception e29) {
//                            database = database2;
//                            e2 = e29;
//                            e2.printStackTrace();
//                            database.endTransaction();
//                            ih2.close();
//                            database.close();
//                            xem_lv();
//                            Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                        } catch (Throwable th14) {
//                            database = database2;
//                            th = th14;
//                            database.endTransaction();
//                            ih2.close();
//                            database.close();
//                            throw th;
//                        }
//                    }
//                    database = database2;
//                    database.setTransactionSuccessful();
//                    database.endTransaction();
//                    ih2.close();
//                } catch (Exception e30) {
//                    database = database2;
//                    e2 = e30;
//                    e2.printStackTrace();
//                    database.endTransaction();
//                    ih2.close();
//                    database.close();
//                    xem_lv();
//                    Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//                } catch (Throwable th15) {
//                    database = database2;
//                    th = th15;
//                    database.endTransaction();
//                    ih2.close();
//                    database.close();
//                    throw th;
//                }
//                database.close();
//                xem_lv();
//                Toast.makeText(getActivity(), "Đã tải xong tin nhắn!", 1).show();
//            } catch (SQLiteException e31) {
//            } catch (JSONException e32) {
//                e = e32;
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void xem_lv() {
//        new MainActivity();
//        String mDate = MainActivity.Get_date();
//        this.mID.clear();
//        this.mNgay.clear();
//        this.mSDT.clear();
//        this.mTenKH.clear();
//        this.mSoTinNhan.clear();
//        this.mTinNhanGoc.clear();
//        this.mND_DaSua.clear();
//        this.mND_PhanTich.clear();
//        this.mPhatHienLoi.clear();
//        this.mTypeKH.clear();
//        Database database = this.db;
//        Cursor cursor = database.GetData("select * from tbl_tinnhanS WHERE phat_hien_loi <> 'ok' AND ngay_nhan = '" + mDate + "'");
//        if (cursor != null && cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                this.mID.add(Integer.valueOf(cursor.getInt(0)));
//                this.mNgay.add(cursor.getString(1));
//                this.mTenKH.add(cursor.getString(4));
//                this.mSDT.add(cursor.getString(5));
//                this.mSoTinNhan.add(Integer.valueOf(Integer.parseInt(cursor.getString(7))));
//                this.mTinNhanGoc.add(cursor.getString(8));
//                this.mND_DaSua.add(cursor.getString(9));
//                this.mND_PhanTich.add(cursor.getString(10));
//                this.mPhatHienLoi.add(cursor.getString(11));
//                this.mTypeKH.add(Integer.valueOf(cursor.getInt(3)));
//            }
//            cursor.close();
//        }
//        if (getActivity() != null) {
//            this.lv_suatin.setAdapter((ListAdapter) new TNGAdapter(getActivity(), R.layout.frag_suatin_lv, this.mTinNhanGoc));
//        }
//    }
//
//    /* access modifiers changed from: package-private */
//    public class TNGAdapter extends ArrayAdapter {
//        public TNGAdapter(Context context, int resource, List<String> objects) {
//            super(context, resource, objects);
//        }
//
//        class ViewHolder {
//            TextView tview5;
//            TextView tview7;
//
//            ViewHolder() {
//            }
//        }
//
//        public View getView(int position, View mView, ViewGroup parent) {
//            ViewHolder holder;
//            if (mView == null) {
//                mView = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.frag_suatin_lv, (ViewGroup) null);
//                holder = new ViewHolder();
//                holder.tview5 = (TextView) mView.findViewById(R.id.tv_suatin_nd);
//                holder.tview7 = (TextView) mView.findViewById(R.id.tv_suatin_err);
//                mView.setTag(holder);
//            } else {
//                holder = (ViewHolder) mView.getTag();
//            }
//            holder.tview5.setText(Frag_Suatin.this.mTinNhanGoc.get(position));
//            holder.tview7.setText(Frag_Suatin.this.mPhatHienLoi.get(position));
//            return mView;
//        }
//    }
//}