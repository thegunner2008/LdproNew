package tamhoang.ldpro4.Activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import tamhoang.ldpro4.NotificationNewReader;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.BriteDb;
import tamhoang.ldpro4.data.Database;
import tamhoang.ldpro4.data.model.KhachHang;
import tamhoang.ldpro4.data.model.TinNhanS;

public class Chatbox extends BaseToolBarActivity {
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
                Xem_lv();
                MainActivity.sms = false;
            }
            handler.postDelayed(this, 1000);
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
        this.TinXuly = new JSONObject();
        this.db = new Database(this);
        Intent intent = getIntent();
        this.ten_kh = intent.getStringExtra("tenKH");
        this.so_dienthoai = intent.getStringExtra("so_dienthoai");
        this.app_use = intent.getStringExtra("app");
        this.send.setOnClickListener(view -> {
            String mess = messageS.getText().toString();
            try {
                if (mess.replace(" ", "").length() > 0) {
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
                            new MainActivity();
                            MainActivity.sendMessage(Long.parseLong(so_dienthoai), mess);
                            GuiTinTrucTiep(mNgayNhan, mGionhan, ten_kh, mess);
                            messageS.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (!app_use.contains("sms")) {
                        new NotificationNewReader().NotificationWearReader(ten_kh, mess);
                        db.QueryData("Insert into Chat_database Values( null,'" + mNgayNhan + "', '" + mGionhan + "', 2, '" + ten_kh + "', '" + so_dienthoai + "', '" + app_use + "','" + mess + "',1)");
                        messageS.setText("");
                        GuiTinTrucTiep(mNgayNhan, mGionhan, ten_kh, mess);
                        MainActivity.sms = true;
                        Xem_lv();
                    } else {
                        try {
                            Cursor c = db.GetData("Select * From tbl_kh_new Where ten_kh = '" + ten_kh + "'");
                            c.moveToFirst();
                            db.SendSMS(c.getString(1), mess);
                            db.QueryData("Insert into Chat_database Values( null,'" + mNgayNhan + "', '" + mGionhan + "', 2, '" + ten_kh + "', '" + so_dienthoai + "', '" + app_use + "','" + mess + "',1)");
                            messageS.setText("");
                            GuiTinTrucTiep(mNgayNhan, mGionhan, ten_kh, mess);
                            MainActivity.sms = true;
                            Xem_lv();
                            c.close();
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            position = i;
            return false;
        });
        handler = new Handler();
        handler.postDelayed(this.runnable, 1000);
        registerForContextMenu(this.listView);
        Xem_lv();
    }

    public void GuiTinTrucTiep(String Ngay_gui, String Gio_gui, String Ten_kh, String mText) {
//        Cursor cursor = db.GetData("Select * From tbl_kh_new Where ten_kh = '" + Ten_kh + "'");
//        cursor.moveToFirst();
        KhachHang khachHang = BriteDb.INSTANCE.selectKhachHang(Ten_kh);
        if (khachHang != null) {
            if (khachHang.getType_kh() > 1) {
                int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(Ngay_gui, khachHang.getType_kh(), "ten_kh = '"+ Ten_kh +"'");
                String ndTinNhan = mText.replaceAll("'", " ").trim();
                TinNhanS tinNhanS = new TinNhanS(null, Ngay_gui, Gio_gui, 2, Ten_kh, khachHang.getSdt(), khachHang.getUse_app(),
                        maxSoTn + 1, ndTinNhan, ndTinNhan, ndTinNhan, "ko", 0, 0, 0, null);

                BriteDb.INSTANCE.insertTinNhanS(tinNhanS);

                TinNhanS tinNhanS2 = BriteDb.INSTANCE.selectTinNhanS(Ngay_gui, Ten_kh, maxSoTn + 1, 2);
                if (Congthuc.CheckDate(MainActivity.hanSuDung)) {
                    try {
                        db.Update_TinNhanGoc(tinNhanS2.getID(), khachHang.getType_kh());
                    } catch (Exception e) {
                        Log.e(TAG, "GuiTinTrucTiep: er"+ e );

                        db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + tinNhanS2.getID());
                        db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + Ngay_gui + "' AND ten_kh = '" + Ten_kh + "' AND so_tin_nhan = " + (maxSoTn + 1) + " And type_kh = 2");
                        Toast.makeText(this, "Đã xảy ra lỗi!", Toast.LENGTH_LONG).show();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                } else if (MainActivity.tenAcc.length() == 0) {
                    AlertDialog.Builder bui = new AlertDialog.Builder(this);
                    bui.setTitle("Thông báo:");
                    bui.setMessage("Kiểm tra kết nối Internet!");
                    bui.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                    bui.create().show();
                } else {
                    try {
                        AlertDialog.Builder bui2 = new AlertDialog.Builder(this);
                        bui2.setTitle("Thông báo:");
                        bui2.setMessage("Đã hết hạn sử dụng phần mềm\n\nHãy liên hệ đại lý hoặc SĐT: " + MainActivity.thongTinAcc.getString("k_tra") + " để gia hạn");
                        bui2.setNegativeButton("Đóng", (dialog, which) -> dialog.cancel());
                        bui2.create().show();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
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
        Cursor cursor = database.GetData("Select chat_database.*, tbl_tinnhanS.phat_hien_loi, tbl_tinnhanS.id, tbl_tinnhanS.so_tin_nhan From chat_database \nLEFT JOIN tbl_tinnhanS " +
                "ON chat_database.ngay_nhan = tbl_tinnhanS.ngay_nhan AND chat_database.gio_nhan = tbl_tinnhanS.gio_nhan AND chat_database.ten_kh = tbl_tinnhanS.ten_kh " +
                "AND chat_database.nd_goc = tbl_tinnhanS.nd_goc\nWhere chat_database.ten_kh = '" + this.ten_kh + "'  AND chat_database.ngay_nhan = '" + mDate + "' AND chat_database.del_sms = 1 ORDER by gio_nhan");
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
                if (mXulytin.get(position).length() > 0) {
                    Database database = db;
                    Cursor cursor = database.GetData("Select * From tbl_tinnhanS where ID = " + mID_TinNhan.get(position));
                    cursor.moveToFirst();
                    Database database2 = db;
                    database2.QueryData("DELETE FROM tbl_tinnhanS WHERE ngay_nhan = '" + cursor.getString(1) + "' AND ten_kh = '" + cursor.getString(4) + "' AND so_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3));
                    Database database3 = db;
                    database3.QueryData("DELETE FROM tbl_soctS WHERE ngay_nhan = '" + cursor.getString(1) + "' AND ten_kh = '" + cursor.getString(4) + "' AND so_tin_nhan = " + cursor.getString(7) + " AND type_kh = " + cursor.getString(3));
                    Database database4 = db;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Update chat_database set del_sms = 0 WHERE ID = ");
                    sb.append(mID.get(position));
                    database4.QueryData(sb.toString());
                    Xem_lv();
                    Toast.makeText(Chatbox.this, "Đã xóa!", Toast.LENGTH_LONG).show();
                    return;
                }
                Database database5 = db;
                database5.QueryData("Update chat_database set del_sms = 0 WHERE ID = " + mID.get(position));
                Xem_lv();
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
            if (type_kh.get(position).contains("2")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_list_item_out, null);
                TextView textV1 = convertView.findViewById(R.id.body_out);
                if (mXulytin.get(position).indexOf("ok") == 0) {
                    SpannableString spanString = new SpannableString(nd_goc.get(position));
                    spanString.setSpan(new StyleSpan(1), 0, spanString.length(), 0);
                    textV1.setText(spanString);
                } else {
                    textV1.setText(nd_goc.get(position));
                }
                ((TextView) convertView.findViewById(R.id.status_out)).setText(gio_nhan.get(position));
            }
            if (type_kh.get(position).contains("1")) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_list_item_in, null);
                TextView tview1 = convertView.findViewById(R.id.body_in);
                if (mXulytin.get(position).indexOf("ok") == 0) {
                    SpannableString spanString2 = new SpannableString(nd_goc.get(position));
                    spanString2.setSpan(new StyleSpan(1), 0, spanString2.length(), 0);
                    tview1.setText(spanString2);
                } else {
                    tview1.setText(nd_goc.get(position));
                }
                ((TextView) convertView.findViewById(R.id.status_in)).setText(gio_nhan.get(position));
            }
            return convertView;
        }
    }
}