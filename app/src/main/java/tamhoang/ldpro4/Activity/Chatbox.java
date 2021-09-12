package tamhoang.ldpro4.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import tamhoang.ldpro4.Congthuc.BaseToolBarActivity;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.NotificationReader;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.Database;

public class Chatbox extends BaseToolBarActivity {
    boolean Running = true;
    JSONObject TinXuly = new JSONObject();
    String app_use;
    Database db;
    private final List<String> gio_nhan = new ArrayList();
    Handler handler;
    ListView listView;
    private final List<String> mApp = new ArrayList();
    private final List<String> mID = new ArrayList();
    private final List<String> mID_TinNhan = new ArrayList();
    private final List<String> mSDT = new ArrayList();
    private final List<String> mSo_TinNhan = new ArrayList();
    private final List<String> mTenKH = new ArrayList();
    private final List<String> mXulytin = new ArrayList();
    EditText messageS;
    private final List<String> nd_goc = new ArrayList();
    int position;
    private final Runnable runnable = new Runnable() {
        public void run() {
            if (MainActivity.sms) {
                Chatbox.this.Xem_lv();
                MainActivity.sms = false;
            }
            Chatbox.this.handler.postDelayed(this, 1000);
        }
    };
    ImageView send;
    String so_dienthoai;
    String ten_kh;
    private final List<String> type_kh = new ArrayList();

    @Override
    public int getLayoutId() {
        return R.layout.activity_chatbox;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);
        this.listView = findViewById(R.id.Listview);
        this.send = findViewById(R.id.send);
        this.messageS = findViewById(R.id.messageS);
        new MainActivity();
        MainActivity.Get_date();
        this.TinXuly = new JSONObject();
        this.db = new Database(this);
        Intent intent = getIntent();
        this.ten_kh = intent.getStringExtra("tenKH");
        this.so_dienthoai = intent.getStringExtra("so_dienthoai");
        this.app_use = intent.getStringExtra("app");
        this.send.setOnClickListener(view -> {
            Exception e;
            String Mess = Chatbox.this.messageS.getText().toString();
            try {
                if (Mess.replace(" ", "").length() > 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                    dmyFormat.setTimeZone(TimeZone.getDefault());
                    hourFormat.setTimeZone(TimeZone.getDefault());
                    String mNgayNhan = dmyFormat.format(calendar.getTime());
                    String mGionhan = hourFormat.format(calendar.getTime());
                    if (Chatbox.this.app_use.contains("TL")) {
                        try {
                            String SSS = Chatbox.this.messageS.getText().toString();
                            new MainActivity();
                            MainActivity.sendMessage(Long.parseLong(Chatbox.this.so_dienthoai), SSS);
                            Chatbox.this.GuiTinTrucTiep(mNgayNhan, mGionhan, Chatbox.this.ten_kh, SSS);
                            Chatbox.this.messageS.setText("");
                        } catch (Exception e2) {
                            e = e2;
                            e.printStackTrace();
                        }
                    } else if (Chatbox.this.app_use.indexOf("sms") == -1) {
                        new NotificationReader().NotificationWearReader(Chatbox.this.ten_kh, Mess);
                        String SSS2 = Chatbox.this.messageS.getText().toString();
                        Chatbox.this.db.QueryData("Insert into Chat_database Values( null,'" + mNgayNhan + "', '" + mGionhan + "', 2, '" + Chatbox.this.ten_kh + "', '" + Chatbox.this.so_dienthoai + "', '" + Chatbox.this.app_use + "','" + SSS2 + "',1)");
                        Chatbox.this.messageS.setText("");
                        Chatbox.this.GuiTinTrucTiep(mNgayNhan, mGionhan, Chatbox.this.ten_kh, SSS2);
                        MainActivity.sms = true;
                        Chatbox.this.Xem_lv();
                    } else {
                        String SSS3 = Chatbox.this.messageS.getText().toString();
                        Database database = Chatbox.this.db;
                        StringBuilder sb = new StringBuilder();
                        try {
                            sb.append("Select * From tbl_kh_new Where ten_kh = '");
                            sb.append(Chatbox.this.ten_kh);
                            sb.append("'");
                            Cursor c = database.GetData(sb.toString());
                            c.moveToFirst();
                            Chatbox.this.db.SendSMS(c.getString(1), SSS3);
                            Chatbox.this.db.QueryData("Insert into Chat_database Values( null,'" + mNgayNhan + "', '" + mGionhan + "', 2, '" + Chatbox.this.ten_kh + "', '" + Chatbox.this.so_dienthoai + "', '" + Chatbox.this.app_use + "','" + SSS3 + "',1)");
                            Chatbox.this.messageS.setText("");
                            Chatbox.this.GuiTinTrucTiep(mNgayNhan, mGionhan, Chatbox.this.ten_kh, SSS3);
                            MainActivity.sms = true;
                            Chatbox.this.Xem_lv();
                            c.close();
                        } catch (Exception e3) {
                            e = e3;
                        }
                    }
                }
            } catch (Exception e4) {
                e = e4;
                e.printStackTrace();
            }
        });
        this.listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Chatbox.this.position = i;
            return false;
        });
        Handler handler2 = new Handler();
        this.handler = handler2;
        handler2.postDelayed(this.runnable, 1000);
        registerForContextMenu(this.listView);
        Xem_lv();
    }

    public void GuiTinTrucTiep(String Ngay_gui, String Gio_gui, String Ten_kh, String mText) {
        Database database = this.db;
        Cursor cursor = database.GetData("Select * From tbl_kh_new Where ten_kh = '" + Ten_kh + "'");
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            if (cursor.getInt(3) > 1) {
                Cursor getSoTN = this.db.GetData("Select max(so_tin_nhan) from tbl_tinnhanS WHERE ngay_nhan = '" + Ngay_gui + "' AND ten_kh = '" + Ten_kh + "' AND type_kh = 2");
                getSoTN.moveToFirst();
                this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + Ngay_gui + "', '" + Gio_gui + "', 2, '" + Ten_kh + "', '" + cursor.getString(1) + "', '" + cursor.getString(2) + "', " + (getSoTN.getInt(0) + 1) + ", '" + mText.replaceAll("'", " ").trim() + "', '" + mText.replaceAll("'", " ").trim() + "', '" + mText.replaceAll("'", " ").trim() + "', 'ko',0, 0, 0, null)");
                Database database2 = this.db;
                StringBuilder sb = new StringBuilder();
                sb.append("Select id From tbl_tinnhanS WHERE ngay_nhan = '");
                sb.append(Ngay_gui);
                sb.append("' AND ten_kh = '");
                sb.append(Ten_kh);
                sb.append("' AND so_tin_nhan = ");
                sb.append(getSoTN.getInt(0) + 1);
                sb.append(" And type_kh = 2");
                Cursor c = database2.GetData(sb.toString());
                c.moveToFirst();
                if (Congthuc.CheckDate(MainActivity.myDate)) {
                    try {
                        this.db.Update_TinNhanGoc(c.getInt(0), cursor.getInt(3));
                    } catch (Exception e) {
                        Database database3 = this.db;
                        database3.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                        this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + Ngay_gui + "' AND ten_kh = '" + Ten_kh + "' AND so_tin_nhan = " + (getSoTN.getInt(0) + 1) + " And type_kh = 2");
                        Toast.makeText(this, "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                } else if (MainActivity.Acc_manager.length() == 0) {
                    AlertDialog.Builder bui = new AlertDialog.Builder(this);
                    bui.setTitle("Thông báo:");
                    bui.setMessage("Kiểm tra kết nối Internet!");
                    bui.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                    bui.create().show();
                } else {
                    try {
                        AlertDialog.Builder bui2 = new AlertDialog.Builder(this);
                        bui2.setTitle("Thông báo:");
                        bui2.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.listKH.getString("k_tra") + " để gia hạn");
                        bui2.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                        bui2.create().show();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.runnable);
    }

    @Override
    public void onResume() {
        Xem_lv();
        super.onResume();
    }

    public void Xem_lv() {
        this.mID.clear();
        this.mTenKH.clear();
        this.gio_nhan.clear();
        this.type_kh.clear();
        this.nd_goc.clear();
        this.mApp.clear();
        this.mXulytin.clear();
        this.mID_TinNhan.clear();
        this.mSo_TinNhan.clear();
        new MainActivity();
        String mDate = MainActivity.Get_date();
        Database database = this.db;
        Cursor cursor = database.GetData("Select chat_database.*, tbl_tinnhanS.phat_hien_loi, tbl_tinnhanS.id, tbl_tinnhanS.so_tin_nhan From chat_database \nLEFT JOIN tbl_tinnhanS ON chat_database.ngay_nhan = tbl_tinnhanS.ngay_nhan AND chat_database.gio_nhan = tbl_tinnhanS.gio_nhan AND chat_database.ten_kh = tbl_tinnhanS.ten_kh AND chat_database.nd_goc = tbl_tinnhanS.nd_goc\nWhere chat_database.ten_kh = '" + this.ten_kh + "'  AND chat_database.ngay_nhan = '" + mDate + "' AND chat_database.del_sms = 1 ORDER by gio_nhan");
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                this.mID.add(cursor.getString(0));
                this.mTenKH.add(cursor.getString(4));
                this.mSDT.add(cursor.getString(5));
                this.gio_nhan.add(cursor.getString(2));
                this.type_kh.add(cursor.getString(3));
                this.nd_goc.add(cursor.getString(7));
                this.mApp.add(cursor.getString(6));
                if (cursor.isNull(9)) {
                    this.mXulytin.add("");
                    this.mID_TinNhan.add("");
                    this.mSo_TinNhan.add("");
                } else {
                    this.mXulytin.add(cursor.getString(9));
                    this.mID_TinNhan.add(cursor.getString(10));
                    this.mSo_TinNhan.add(cursor.getString(11));
                }
            }
            cursor.close();
        }
        this.listView.setAdapter(new Chat(this, R.layout.message_list_item_in, this.mTenKH));
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Database database = this.db;
        Cursor cursor = database.GetData("Select * From tbl_kh_new Where ten_kh = '" + this.ten_kh + "'");
        if (cursor.getCount() != 0) {
            menu.add("Sửa tin");
            menu.add("Xem chi tiết");
        }
        menu.add("Copy");
        menu.add("Xóa");
        cursor.close();
    }

    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        @SuppressLint("WrongConstant")
        ClipboardManager clipboard = (ClipboardManager) getSystemService("clipboard");
        if (item.getTitle() == "Sửa tin") {
            if (this.mXulytin.get(this.position).length() > 0) {
                Intent intent = new Intent(this, Activity_Tinnhan.class);
                intent.putExtra("m_ID", this.mID_TinNhan.get(this.position));
                startActivity(intent);
            }
        } else if (item.getTitle() == "Xem chi tiết") {
            if (this.mXulytin.get(this.position).indexOf("ok") == 0) {
                Intent intent2 = new Intent(this, Activity_CTTinnhan.class);
                intent2.putExtra("m_ID", this.mID_TinNhan.get(this.position));
                intent2.putExtra("type_kh", this.type_kh.get(this.position));
                startActivity(intent2);
            }
        } else if (item.getTitle() == "Copy") {
            clipboard.setPrimaryClip(ClipData.newPlainText("Tin nhắn:", this.nd_goc.get(this.position)));
            Toast.makeText(this, "Đã copy vào bộ nhớ tạm!", Toast.LENGTH_LONG).show();
        } else if (item.getTitle() == "Xóa") {
            new MainActivity();
            MainActivity.Get_date();
            AlertDialog.Builder bui = new AlertDialog.Builder(this);
            bui.setTitle("Xóa tin này");
            bui.setPositiveButton("YES", (dialog, which) -> {
                if (Chatbox.this.mXulytin.get(Chatbox.this.position).length() > 0) {
                    Database database = Chatbox.this.db;
                    Cursor cursor = database.GetData("Select * From tbl_tinnhanS where ID = " + Chatbox.this.mID_TinNhan.get(Chatbox.this.position));
                    cursor.moveToFirst();
                    Database database2 = Chatbox.this.db;
                    database2.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + cursor.getString(1) + "' AND ten_kh = '" + cursor.getString(4) + "' AND so_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3));
                    Database database3 = Chatbox.this.db;
                    database3.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + cursor.getString(1) + "' AND ten_kh = '" + cursor.getString(4) + "' AND so_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3));
                    Database database4 = Chatbox.this.db;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Update chat_database set del_sms = 0 WHERE ID = ");
                    sb.append(Chatbox.this.mID.get(Chatbox.this.position));
                    database4.QueryData(sb.toString());
                    Chatbox.this.Xem_lv();
                    Toast.makeText(Chatbox.this, "Đã xóa!", Toast.LENGTH_LONG).show();
                    return;
                }
                Database database5 = Chatbox.this.db;
                database5.QueryData("Update chat_database set del_sms = 0 WHERE ID = " + Chatbox.this.mID.get(Chatbox.this.position));
                Chatbox.this.Xem_lv();
            });
            bui.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            bui.create().show();
        }
        return true;
    }

    public class Chat extends ArrayAdapter {
        public Chat(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (Chatbox.this.type_kh.get(position).contains("2")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_list_item_out, null);
                TextView textV1 = convertView.findViewById(R.id.body_out);
                if (Chatbox.this.mXulytin.get(position).indexOf("ok") == 0) {
                    SpannableString spanString = new SpannableString(Chatbox.this.nd_goc.get(position));
                    spanString.setSpan(new StyleSpan(1), 0, spanString.length(), 0);
                    textV1.setText(spanString);
                } else {
                    textV1.setText(Chatbox.this.nd_goc.get(position));
                }
                ((TextView) convertView.findViewById(R.id.status_out)).setText(Chatbox.this.gio_nhan.get(position));
            }
            if (Chatbox.this.type_kh.get(position).contains("1")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_list_item_in, null);
                TextView tview1 = convertView.findViewById(R.id.body_in);
                if (Chatbox.this.mXulytin.get(position).indexOf("ok") == 0) {
                    SpannableString spanString2 = new SpannableString(Chatbox.this.nd_goc.get(position));
                    spanString2.setSpan(new StyleSpan(1), 0, spanString2.length(), 0);
                    tview1.setText(spanString2);
                } else {
                    tview1.setText(Chatbox.this.nd_goc.get(position));
                }
                ((TextView) convertView.findViewById(R.id.status_in)).setText(Chatbox.this.gio_nhan.get(position));
            }
            return convertView;
        }
    }
}