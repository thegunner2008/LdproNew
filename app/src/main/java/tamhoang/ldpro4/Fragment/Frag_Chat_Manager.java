package tamhoang.ldpro4.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import tamhoang.ldpro4.Activity.Activity_AddKH;
import tamhoang.ldpro4.Activity.Chatbox;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationReader;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Frag_Chat_Manager extends Fragment {
    boolean Running = true;
    Button btn_Thongbao;
    Button btn_login;
    Database db;
    Handler handler;
    ListView listviewKH;
    private final List<String> mApp = new ArrayList();
    private final List<String> mNoiDung = new ArrayList();
    private final List<String> mSDT = new ArrayList();
    private final List<String> mTenKH = new ArrayList();
    private final Runnable runnable = new Runnable() {
        public void run() {
            if (MainActivity.sms) {
                Frag_Chat_Manager.this.XemListview();
                MainActivity.sms = false;
            }
            Frag_Chat_Manager.this.handler.postDelayed(this, 1000);
        }
    };
    public View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.frag_chat_manager, container, false);
        this.db = new Database(getActivity());
        this.btn_Thongbao = this.v.findViewById(R.id.btn_Thongbao);
        this.btn_login = this.v.findViewById(R.id.btn_login);
        this.listviewKH = this.v.findViewById(R.id.listviewKH);
        this.btn_Thongbao.setOnClickListener(v -> Frag_Chat_Manager.this.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")));
        this.listviewKH.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(Frag_Chat_Manager.this.getActivity(), Chatbox.class);
            intent.putExtra("tenKH", Frag_Chat_Manager.this.mTenKH.get(i));
            intent.putExtra("so_dienthoai", Frag_Chat_Manager.this.mSDT.get(i));
            intent.putExtra("app", Frag_Chat_Manager.this.mApp.get(i));
            Frag_Chat_Manager.this.startActivity(intent);
        });
        notificationPermission();
        Handler handler2 = new Handler();
        this.handler = handler2;
        handler2.postDelayed(this.runnable, 1000);
        return this.v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.runnable);
    }

    @Override
    public void onResume() {
        try {
            getSMS();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        XemListview();
        super.onResume();
    }

    private void getSMS() throws Throwable {
        SQLiteException e;
        JSONException e2;
        ParseException e3;
        SQLiteDatabase database;
        DatabaseUtils.InsertHelper ih;
        Throwable th;
        Exception e4;
        Long millis;
        StringBuilder sb;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        Frag_Chat_Manager frag_Chat_Manager = this;
        String str6 = "'";
        String str7 = "";
        String str8 = " ";
        MainActivity mainActivity = new MainActivity();
        String mDate = MainActivity.Get_date();
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), "android.permission.READ_SMS") == 0) {
            try {
                Date dateStart = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(mDate + "T00:00:00");
                String filter = "date>=" + dateStart.getTime();
                frag_Chat_Manager.db.QueryData("DELETE FROM Chat_database WHERE ngay_nhan = '" + mDate + "' AND use_app = 'sms'");
                Cursor cur1 = frag_Chat_Manager.db.GetData("Select * From tbl_kh_new");
                JSONObject json = new JSONObject();
                while (cur1.moveToNext()) {
                    try {
                        JSONObject json_kh = new JSONObject();
                        json_kh.put("type_kh", cur1.getString(3));
                        json_kh.put("ten_kh", cur1.getString(0));
                        json_kh.put("sdt", cur1.getString(1));
                        json_kh.put("so_tn", 0);
                        json.put(cur1.getString(1), json_kh);
                    } catch (SQLiteException e5) {
                        e = e5;
                        e.printStackTrace();
                    } catch (JSONException e6) {
                        e2 = e6;
                        e2.printStackTrace();
                    }
                }
                cur1.close();
                String str9 = "ten_kh";
                Uri message = Uri.parse("content://sms");
                ContentResolver cr = getActivity().getContentResolver();
                String str10 = "type_kh";
                Cursor c = cr.query(message, null, filter, null, "date ASC");
                getActivity().startManagingCursor(c);
                int totalSMS = c.getCount();
                if (c.moveToFirst()) {
                    SQLiteDatabase database2 = frag_Chat_Manager.db.getWritableDatabase();
                    DatabaseUtils.InsertHelper ih2 = new DatabaseUtils.InsertHelper(database2, "Chat_database");
                    try {
                        database2.beginTransaction();
                        int i = 0;
                        DatabaseUtils.InsertHelper ih3 = ih2;
                        while (i < totalSMS) {
                            try {
                                millis = Long.valueOf(c.getLong(c.getColumnIndexOrThrow("date")));

                                sb = new StringBuilder();
                                database = database2;

                                sb.append((Object) DateFormat.format("HH:mm:ss", new Date(millis.longValue())));
                                sb.append(str7);
                                String mGioNhan = sb.toString();
                                String mSDT2 = c.getString(c.getColumnIndexOrThrow("address")).replaceAll(str8, str7);
                                String body = c.getString(c.getColumnIndexOrThrow("body")).replaceAll(str6, str8).replaceAll("\"", str8);
                                String typeKT = c.getString(c.getColumnIndexOrThrow("type"));
                                if (mSDT2.startsWith("0")) {
                                    StringBuilder sb2 = new StringBuilder();
                                    str2 = str7;
                                    sb2.append("+84");
                                    str = str8;
                                    sb2.append(mSDT2.substring(1));
                                    mSDT2 = sb2.toString();
                                } else {
                                    str2 = str7;
                                    str = str8;
                                }
                                frag_Chat_Manager.db.QueryData("Update tbl_tinnhanS set gio_nhan ='" + mGioNhan + "' WHERE nd_goc = '" + body + "' AND so_dienthoai = '" + mSDT2 + "' AND ngay_nhan = '" + mDate + str6);
                                if (json.has(mSDT2)) {
                                    JSONObject Gia_khach = json.getJSONObject(mSDT2);
                                    Gia_khach.put("so_tn", Gia_khach.getInt("so_tn") + 1);
                                    Gia_khach.put(body, body);
                                    ih2.prepareForInsert();
                                    ih3 = ih2;
                                    ih3.bind(ih3.getColumnIndex("ngay_nhan"), mDate);
                                    ih3.bind(ih3.getColumnIndex("gio_nhan"), mGioNhan);
                                    str3 = str6;
                                    ih3.bind(ih3.getColumnIndex(str10), typeKT);
                                    str5 = str9;
                                    str4 = str10;
                                    ih3.bind(ih3.getColumnIndex(str5), Gia_khach.getString(str5));
                                    ih3.bind(ih3.getColumnIndex("so_dienthoai"), mSDT2);
                                    ih3.bind(ih3.getColumnIndex("use_app"), "sms");
                                    ih3.bind(ih3.getColumnIndex("nd_goc"), body);
                                    ih3.bind(ih3.getColumnIndex("del_sms"), 1);
                                    ih3.execute();
                                    json.put(mSDT2, Gia_khach);
                                } else {
                                    ih3 = ih2;
                                    str3 = str6;
                                    str5 = str9;
                                    str4 = str10;
                                }
                                c.moveToNext();
                                i++;
                                frag_Chat_Manager = this;
                                ih2 = ih3;
                                cur1 = cur1;
                                cr = cr;
                                message = message;
                                ih3 = ih3;
                                database2 = database;
                                str7 = str2;
                                str8 = str;
                                str9 = str5;
                                str6 = str3;
                                str10 = str4;
                            } catch (Exception e8) {
                                e4 = e8;
                                database = database2;
                                ih = ih2;
                                try {
                                    e4.printStackTrace();
                                    database.endTransaction();
                                    ih.close();
                                    database.close();
                                } catch (Throwable th2) {
                                    th = th2;
                                    database.endTransaction();
                                    ih.close();
                                    database.close();
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                database = database2;
                                ih = ih2;
                                database.endTransaction();
                                ih.close();
                                database.close();
                                throw th;
                            }
                        }
                        database = database2;
                        database.setTransactionSuccessful();
                        database.endTransaction();
                        ih2.close();
                    } catch (Exception e14) {
                        e4 = e14;
                        database = database2;
                        ih = ih2;
                        e4.printStackTrace();
                        database.endTransaction();
                        ih.close();
                        database.close();
                    } catch (Throwable th8) {
                        th = th8;
                        database = database2;
                        ih = ih2;
                        database.endTransaction();
                        ih.close();
                        database.close();
                        throw th;
                    }
                    database.close();
                }
            } catch (SQLiteException e15) {
                e = e15;
                e.printStackTrace();
            } catch (JSONException e16) {
                e2 = e16;
                e2.printStackTrace();
            } catch (ParseException e17) {
                e3 = e17;
                e3.printStackTrace();
            }
        }
    }

    private void XemListview() {
        this.mTenKH.clear();
        this.mNoiDung.clear();
        this.mApp.clear();
        this.mSDT.clear();
        new MainActivity();
        String mDate = MainActivity.Get_date();
        JSONObject jsonObject = new JSONObject();
        Database database = this.db;
        Cursor cursor = database.GetData("SELECT * FROM Chat_database WHERE ngay_nhan = '" + mDate + "' ORDER BY Gio_nhan DESC, ID DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if ((MainActivity.arr_TenKH.indexOf(cursor.getString(4)) > -1 || cursor.getString(6).indexOf("sms") > -1 || cursor.getString(6).indexOf("TL") > -1) && !jsonObject.has(cursor.getString(4))) {
                    try {
                        jsonObject.put(cursor.getString(4), "OK");
                        this.mTenKH.add(cursor.getString(4));
                        this.mSDT.add(cursor.getString(5));
                        this.mApp.add(cursor.getString(6));
                        this.mNoiDung.add(cursor.getString(7));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            cursor.close();
        }
        for (String ten_kh : MainActivity.arr_TenKH) {
            if (!this.mTenKH.contains(ten_kh)) {
                this.mTenKH.add(ten_kh);
                this.mNoiDung.add("Hôm nay chưa có tin nhắn!");
                if (ten_kh.contains("ZL")) {
                    this.mApp.add("ZL");
                } else if (ten_kh.contains("VB")) {
                    this.mApp.add("VB");
                } else if (ten_kh.contains("WA")) {
                    this.mApp.add("WA");
                }
            }
        }
        if (getActivity() != null) {
            this.listviewKH.setAdapter(new Chat_Main(getActivity(), R.layout.frag_chat_manager_lv, this.mTenKH));
        }
    }

    public class Chat_Main extends ArrayAdapter {
        public Chat_Main(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        class ViewHolder {
            TextView TenKH;
            ImageButton add_contacts;
            ImageView imageView;
            TextView ndChat;
            TextView tv_delete;

            ViewHolder() {
            }
        }

        @SuppressLint("WrongConstant")
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder holder;
            View view2 = null;
            view2 = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.frag_chat_manager_lv, null);
            holder = new ViewHolder();
            holder.add_contacts = view2.findViewById(R.id.add_contacts);
            holder.tv_delete = view2.findViewById(R.id.tv_delete);
            holder.imageView = view2.findViewById(R.id.imv_app);
            holder.TenKH = view2.findViewById(R.id.tv_KhachHang);
            holder.ndChat = view2.findViewById(R.id.tv_NoiDung);
            if (Frag_Chat_Manager.this.mApp.get(position).contains("WA")) {
                holder.imageView.setBackgroundResource(R.drawable.ic_perm_phone_msg);
            } else if (Frag_Chat_Manager.this.mApp.get(position).contains("VI")) {
                holder.imageView.setBackgroundResource(R.drawable.ic_phone);
            } else if (Frag_Chat_Manager.this.mApp.get(position).contains("ZL")) {
                holder.imageView.setBackgroundResource(R.drawable.ic_zalo);
            } else if (Frag_Chat_Manager.this.mApp.get(position).contains("TL")) {
                holder.imageView.setBackgroundResource(R.drawable.outline_telegram_20);
                holder.tv_delete.setVisibility(View.GONE);
            } else if (Frag_Chat_Manager.this.mApp.get(position).contains("sms")) {
                holder.imageView.setBackgroundResource(R.drawable.ic_sms);
                holder.add_contacts.setVisibility(View.GONE);
                holder.tv_delete.setVisibility(View.GONE);
            }
            holder.add_contacts.setFocusable(false);
            holder.add_contacts.setFocusableInTouchMode(false);
            holder.add_contacts.setOnClickListener(view1 -> {
                Intent intent = new Intent(Frag_Chat_Manager.this.getActivity(), Activity_AddKH.class);
                intent.putExtra("tenKH", Frag_Chat_Manager.this.mTenKH.get(position));
                intent.putExtra("so_dienthoai", Frag_Chat_Manager.this.mSDT.get(position));
                intent.putExtra("use_app", Frag_Chat_Manager.this.mApp.get(position));
                Frag_Chat_Manager.this.startActivity(intent);
            });
            if (MainActivity.DSkhachhang.contains(Frag_Chat_Manager.this.mTenKH.get(position))) {
                holder.add_contacts.setVisibility(View.GONE);
            }
            holder.tv_delete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(Frag_Chat_Manager.this.getActivity());
                builder.setTitle("Xoá Khách");
                builder.setMessage("Sẽ xóa hết dữ liệu chat từ khách này, không thể khôi phục và không thể tải lại tin nhắn!");
                builder.setNegativeButton("Có", (dialog, which) -> {
                    int TTkhachhang = MainActivity.arr_TenKH.indexOf(Frag_Chat_Manager.this.mTenKH.get(position));
                    MainActivity.arr_TenKH.remove(TTkhachhang);
                    MainActivity.contactslist.remove(TTkhachhang);
                    Frag_Chat_Manager.this.XemListview();
                    dialog.dismiss();
                    Toast.makeText(Frag_Chat_Manager.this.getActivity(), "Đã xóa!", 1).show();
                });
                builder.setPositiveButton("Không", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
            holder.TenKH.setText(Frag_Chat_Manager.this.mTenKH.get(position));
            holder.ndChat.setText(Frag_Chat_Manager.this.mNoiDung.get(position));
            return view2;
        }
    }

    private void notificationPermission() {
        boolean enabled;
        ComponentName cn = new ComponentName(getActivity(), NotificationReader.class);
        String flat = Settings.Secure.getString(getActivity().getContentResolver(), "enabled_notification_listeners");
        enabled = flat != null && flat.contains(cn.flattenToString());
        if (!enabled) {
            showAlertBox("Truy cập thông báo!", "Hãy cho phép phần mềm được truy cập thông báo của điện thoại để kích hoạt chức năng nhắn tin.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Frag_Chat_Manager.this.getActivity().startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).show().setCanceledOnTouchOutside(false);
        }
    }

    public AlertDialog.Builder showAlertBox(String title, String message) {
        return new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message);
    }
}