package tamhoang.ldpro4.Fragment;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
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
import tamhoang.ldpro4.data.model.Chat;

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
                XemListview();
                MainActivity.sms = false;
            }
            handler.postDelayed(this, 1000);
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
        this.btn_Thongbao.setOnClickListener(v -> startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")));
        this.listviewKH.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getActivity(), Chatbox.class);
            intent.putExtra("tenKH", mTenKH.get(i));
            intent.putExtra("so_dienthoai", mSDT.get(i));
            intent.putExtra("app", mApp.get(i));
            startActivity(intent);
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
        getSMS();
        XemListview();
        super.onResume();
    }

    private void getSMS() {// thay the data sms trong Chat_database = content://sms
        SQLiteDatabase database;
        String mDate = MainActivity.Get_date();
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), "android.permission.READ_SMS") == 0) {
            try {
                Date dateStart = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(mDate + "T00:00:00");
                String filter = "date>=" + dateStart.getTime();
                db.QueryData("DELETE FROM Chat_database WHERE ngay_nhan = '" + mDate + "' AND use_app = 'sms'");
                Cursor cur = db.GetData("Select * From tbl_kh_new");
                JSONObject json = new JSONObject();
                while (cur.moveToNext()) {
                    try {
                        JSONObject json_kh = new JSONObject();
                        json_kh.put("type_kh", cur.getString(3));
                        json_kh.put("ten_kh", cur.getString(0));
                        json_kh.put("sdt", cur.getString(1));
                        json_kh.put("so_tn", 0);
                        json.put(cur.getString(1), json_kh);
                    } catch (SQLiteException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                cur.close();
                Uri message = Uri.parse("content://sms");
                ContentResolver cr = getActivity().getContentResolver();
                Cursor c = cr.query(message, null, filter, null, "date ASC");
                getActivity().startManagingCursor(c);
                int totalSMS = c.getCount();
                if (c.moveToFirst()) {
                    database = db.getWritableDatabase();
                    DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(database, "Chat_database");
                    try {
                        database.beginTransaction();
                        int i = 0;
                        while (i < totalSMS) {
                            long millis = c.getLong(c.getColumnIndexOrThrow("date"));

                            String mGioNhan = ((Object) DateFormat.format("HH:mm:ss", new Date(millis))).toString();
                            String mSDT = c.getString(c.getColumnIndexOrThrow("address")).replaceAll(" ", "");
                            String body = c.getString(c.getColumnIndexOrThrow("body")).replaceAll("'", " ").replaceAll("\"", " ");
                            String typeKT = c.getString(c.getColumnIndexOrThrow("type"));
                            if (mSDT.startsWith("0"))
                                mSDT = "+84" + mSDT.substring(1);

                            db.QueryData("Update tbl_tinnhanS set gio_nhan ='" + mGioNhan + "' WHERE nd_goc = '" + body + "' AND so_dienthoai = '" + mSDT + "' AND ngay_nhan = '" + mDate + "'");
                            if (json.has(mSDT)) {
                                JSONObject Gia_khach = json.getJSONObject(mSDT);
                                Gia_khach.put("so_tn", Gia_khach.getInt("so_tn") + 1);
                                Gia_khach.put(body, body);
                                ih.prepareForInsert();
                                ih.bind(ih.getColumnIndex("ngay_nhan"), mDate);
                                ih.bind(ih.getColumnIndex("gio_nhan"), mGioNhan);
                                ih.bind(ih.getColumnIndex("type_kh"), typeKT);
                                ih.bind(ih.getColumnIndex("ten_kh"), Gia_khach.getString("ten_kh"));
                                ih.bind(ih.getColumnIndex("so_dienthoai"), mSDT);
                                ih.bind(ih.getColumnIndex("use_app"), "sms");
                                ih.bind(ih.getColumnIndex("nd_goc"), body);
                                ih.bind(ih.getColumnIndex("del_sms"), 1);
                                ih.execute();
                                json.put(mSDT, Gia_khach);
                            }
                            c.moveToNext();
                            i++;
                        }
                        database.setTransactionSuccessful();
                        database.endTransaction();
                        ih.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        database.endTransaction();
                        ih.close();
                        database.close();
                    }
                    database.close();
                }
            } catch (SQLiteException | ParseException  e) {
                e.printStackTrace();
            }
        }
    }

    private void XemListview() {//lay data trong Chat_database (chi lay moi khach hang 1 row) hien thi ra listviewKH
        mTenKH.clear();
        mNoiDung.clear();
        mApp.clear();
        mSDT.clear();
        String mDate = MainActivity.Get_date();
        JSONObject jsonObject = new JSONObject();
        Cursor cursor = db.GetData("SELECT * FROM Chat_database WHERE ngay_nhan = '" + mDate + "' ORDER BY Gio_nhan DESC, ID DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String ten_kh = cursor.getString(4);
                String use_app = cursor.getString(6);
                
                if ((MainActivity.arr_TenKH.contains(ten_kh) || use_app.contains("sms") || use_app.contains("ZL") || use_app.contains("TL")|| use_app.contains("VB"))
                        && !jsonObject.has(ten_kh)) {
                    try {
                        jsonObject.put(ten_kh, "OK");
                        mTenKH.add(cursor.getString(4));
                        mSDT.add(cursor.getString(5));
                        mApp.add(cursor.getString(6));
                        mNoiDung.add(cursor.getString(7));
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
            if (mApp.get(position).contains("WA")) {
                holder.imageView.setBackgroundResource(R.drawable.ic_perm_phone_msg);
            } else if (mApp.get(position).contains("VI")) {
                holder.imageView.setBackgroundResource(R.drawable.ic_phone);
            } else if (mApp.get(position).contains("ZL")) {
                holder.imageView.setBackgroundResource(R.drawable.ic_zalo);
            } else if (mApp.get(position).contains("TL")) {
                holder.imageView.setBackgroundResource(R.drawable.outline_telegram_20);
                holder.tv_delete.setVisibility(View.GONE);
            } else if (mApp.get(position).contains("sms")) {
                holder.imageView.setBackgroundResource(R.drawable.ic_sms);
                holder.add_contacts.setVisibility(View.GONE);
                holder.tv_delete.setVisibility(View.GONE);
            }
            holder.add_contacts.setFocusable(false);
            holder.add_contacts.setFocusableInTouchMode(false);
            holder.add_contacts.setOnClickListener(view1 -> {
                Intent intent = new Intent(getActivity(), Activity_AddKH.class);
                intent.putExtra("tenKH", mTenKH.get(position));
                intent.putExtra("so_dienthoai", mSDT.get(position));
                intent.putExtra("use_app", mApp.get(position));
                startActivity(intent);
            });
            if (MainActivity.DSkhachhang.contains(mTenKH.get(position))) {
                holder.add_contacts.setVisibility(View.GONE);
            }
            holder.tv_delete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xoá Khách");
                builder.setMessage("Sẽ xóa hết dữ liệu chat từ khách này, không thể khôi phục và không thể tải lại tin nhắn!");
                builder.setNegativeButton("Có", (dialog, which) -> {
                    int TTkhachhang = MainActivity.arr_TenKH.indexOf(mTenKH.get(position));
                    if (TTkhachhang >= 0) {
                        MainActivity.arr_TenKH.remove(TTkhachhang);
                        MainActivity.contactslist.remove(TTkhachhang);
                    }
                    XemListview();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Đã xóa!", 1).show();
                });
                builder.setPositiveButton("Không", (dialog, which) -> dialog.dismiss());
                builder.show();
            });
            holder.TenKH.setText(mTenKH.get(position));
            holder.ndChat.setText(mNoiDung.get(position));
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
                    getActivity().startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).show().setCanceledOnTouchOutside(false);
        }
    }

    public AlertDialog.Builder showAlertBox(String title, String message) {
        return new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message);
    }
}