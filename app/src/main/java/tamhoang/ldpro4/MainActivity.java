package tamhoang.ldpro4;

import static tamhoang.ldpro4.Congthuc.Congthuc.CheckIsToday;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Function;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tamhoang.ldpro4.Activity.Activity_ChuyenThang;
import tamhoang.ldpro4.Activity.Activity_GiuSo;
import tamhoang.ldpro4.Activity.Activity_thaythe;
import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.Fragment.Frag_CanChuyen;
import tamhoang.ldpro4.Fragment.Frag_Change_Password;
import tamhoang.ldpro4.Fragment.Frag_Chat_Manager;
import tamhoang.ldpro4.Fragment.Frag_Database;
import tamhoang.ldpro4.Fragment.Frag_Home;
import tamhoang.ldpro4.Fragment.Frag_MoRP1;
import tamhoang.ldpro4.Fragment.Frag_No_new;
import tamhoang.ldpro4.Fragment.Frag_No_old;
import tamhoang.ldpro4.Fragment.Frag_SMS_Templates;
import tamhoang.ldpro4.Fragment.Frag_Setting1;
import tamhoang.ldpro4.Fragment.Frag_Setting3;
import tamhoang.ldpro4.Fragment.Livestream;
import tamhoang.ldpro4.Fragment.Tab_ChayTrang;
import tamhoang.ldpro4.Fragment.Tab_Tinnhan;
import tamhoang.ldpro4.Fragment.TructiepXoso;
import tamhoang.ldpro4.Telegram.TelegramClient;
import tamhoang.ldpro4.data.BriteDb;
import tamhoang.ldpro4.data.BusEvent;
import tamhoang.ldpro4.data.Contact;
import tamhoang.ldpro4.data.Database;

public class MainActivity extends AppCompatActivity implements TelegramClient.Callback {
    public static Client client;
    public String firstNameTL = "";
    public String lastNameTL = "";

    //string
    public static final String BO = "Bỏ";
    public static final String HET_GIO_NHAN = "Hết giờ nhận!";
    public static final String OK = "Ok";
    public static final String THIEU = "Thiếu";
    public static final String TRA_LAI = "Tra lai";

    //Acc
    public static JSONObject thongTinAcc;
    public static String tenAcc = "";
    public static String hanSuDung = "31/12/2022";

    public static ArrayList<String> DSkhachhang = new ArrayList<>();
    public static JSONObject Json_Chat_Telegram = new JSONObject();
    public static JSONObject Json_Tinnhan = new JSONObject();
    public static String MyToken = "";
    public static NotificationReader Notifi = null;
    static int TIME_REMOVE = 0;
    public static HashMap<String, Contact> contactsMap = new HashMap();
    public static Context context;
    public static ArrayList<HashMap<String, String>> formArray = new ArrayList<>();
    public static ArrayList<HashMap<String, String>> formList = new ArrayList<>();
    public static Handler handler;
    public static JSONObject jSon_Setting;
    public static JSONObject json_Tinnhan = new JSONObject();
    public static List<Fragment> listFragments;
    public static int mDay;
    public static int mMonth;
    public static int mYear;
    private static DatePickerDialog.OnDateSetListener onDateSetListener;
    private ProgressDialog progressDialog;

    static Runnable runnable = new Runnable() {
        public void run() {
            try {
                Iterator<String> keys = json_Tinnhan.keys();
                while (true) {
                    if (!keys.hasNext()) {
                        break;
                    }
                    String key = keys.next();
                    JSONObject dan = new JSONObject(json_Tinnhan.getString(key));
                    dan.put("Time", dan.getInt("Time") + 1);
                    json_Tinnhan.put(key, dan.toString());
                    if (dan.getInt("Time") > 3 && dan.length() > 1) {
                        Iterator<String> tinnhans = dan.keys();
                        while (tinnhans.hasNext()) {
                            String tinnhan = tinnhans.next();
                            if (!tinnhan.contains("Time")) {
                                NotificationNewReader notificationReader = new NotificationNewReader();
                                notificationReader.NotificationWearReader(key, tinnhan);
                            }
                        }
                        JSONObject dan2 = new JSONObject();
                        dan2.put("Time", 0);
                        json_Tinnhan.put(key, dan2.toString());
                    } else if (dan.getInt("Time") > 100) {
                        json_Tinnhan.remove(key);
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (json_Tinnhan.length() == 0) {
                TIME_REMOVE++;
            } else {
                TIME_REMOVE = 0;
            }
            if (TIME_REMOVE < 100) {
                handler.postDelayed(this, 1000);
                return;
            }
            handler.removeCallbacks(runnable);
            handler = null;
        }
    };
    int currentMenuPosition = -1;

    public static boolean sms = false;
    TextView Text_Menu;
    TextView Text_date;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Database db;
    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    String insertData;
    List<NavItem> listNavItems = new ArrayList<>();
    ListView lvNav;
    String my_id = "";
    String viewData;

    RelativeLayout notification;
    boolean notifivationNavigated = false;
    TextView textErrItemCount;
    int mErrItemCount = 0;

    /* access modifiers changed from: protected */
    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.db = new Database(this);
        BriteDb.INSTANCE.init(getApplication());
//        Suagia();
        this.viewData = Get_link() + "json_data.php";
        this.insertData = Get_link() + "json_insert.php";
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LayDulieuJson1();
        LayDulieuJson2();
        this.db.LayDanhsachKH();
        Cursor cursor = this.db.GetData("Select * From tbl_Setting WHERE ID = 1");
        if (cursor != null && cursor.moveToFirst()) {
            try {
                jSon_Setting = new JSONObject(cursor.getString(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        @SuppressLint("WrongConstant") View customactionbar = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.customactionbar, null);
        actionBar.setCustomView(customactionbar);
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(1);
        mMonth = calendar.get(2);
        mDay = calendar.get(5);
        this.Text_date = customactionbar.findViewById(R.id.myTextDate);
        this.Text_Menu = customactionbar.findViewById(R.id.myTextMenu);
        StringBuilder sb = new StringBuilder();
        if (mDay < 10) {
            sb.append("0");
        }
        sb.append(mDay);
        sb.append("-");
        if (mMonth + 1 < 10) {
            sb.append("0");
        }
        sb.append(mMonth + 1);
        sb.append("-");
        sb.append(mYear);
        this.Text_date.setText(sb.toString());
        onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            sms = true;
            StringBuilder sb1 = new StringBuilder();
            if (mDay < 10) {
                sb1.append("0");
            }
            sb1.append(mDay);
            sb1.append("-");
            if (mMonth + 1 < 10) {
                sb1.append("0");
            }
            sb1.append(mMonth + 1);
            sb1.append("-");
            sb1.append(mYear);
            this.Text_date.setText(sb1.toString());
        };
        actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimaryDark)));
        this.drawerLayout = findViewById(R.id.drawer_layout);
        this.drawerPane = findViewById(R.id.drawer_pane);
        this.lvNav = findViewById(R.id.nav_list);
        this.listNavItems.add(new NavItem("Trang chủ", "Imei, hạn sử dụng", R.drawable.home));
        this.listNavItems.add(new NavItem("Sửa tin nhắn", "Sửa/tải lại tin nhắn", R.drawable.edit));
        this.listNavItems.add(new NavItem("Quản lý tin nhắn", "SMS, Zalo, Viber, WhatsApp", R.drawable.number_report));
        this.listNavItems.add(new NavItem("Chuyển số/Giữ số", "Chuyển số và giữ số", R.drawable.number_report));
        this.listNavItems.add(new NavItem("Báo cáo thắng thua", "Báo cáo kết quả từng khách", R.drawable.number_report));
        this.listNavItems.add(new NavItem("Chạy trang", "Vào trang One789", R.drawable.ld789));
        this.listNavItems.add(new NavItem("Cân bảng", "Cân bảng trực tiếp", R.drawable.livestream));
        this.listNavItems.add(new NavItem("Xổ số trực tiếp", "Quay và tính tiền trực tiếp", R.drawable.livekq));
        this.listNavItems.add(new NavItem("Quản lý công nợ", "Công nợ/Thanh toán", R.drawable.money_report));
        this.listNavItems.add(new NavItem("Danh sách khách hàng", "Thông tin khách hàng", R.drawable.contact));
        this.listNavItems.add(new NavItem("Cài đặt", "Cài đặt cho ứng dụng", R.drawable.settings));
        this.listNavItems.add(new NavItem("Các tin nhắn mẫu", "Các cú pháp chuẩn", R.drawable.guilde));
        this.listNavItems.add(new NavItem("Bảo mật", "Quản lý mật khẩu/Kích hoạt PM", R.drawable.password));
        this.listNavItems.add(new NavItem("Cơ sở dữ liệu", "Cập nhật KQ/Tính tiền", R.drawable.database));
        this.lvNav.setAdapter(new NavListAdapter(getApplicationContext(), R.layout.item_nav_list, this.listNavItems));

        listFragments = new ArrayList<>();
        listFragments.add(new Frag_Home());
        listFragments.add(new Tab_Tinnhan());
        listFragments.add(new Frag_Chat_Manager());
        listFragments.add(new Frag_CanChuyen());
        try {
            if (jSon_Setting.getInt("kieu_bao_cao") == 1) {
                listFragments.add(new Frag_No_new());
            } else {
                listFragments.add(new Frag_No_old());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            listFragments.add(new Frag_No_new());
        }
        listFragments.add(new Tab_ChayTrang());
        listFragments.add(new Livestream());
        listFragments.add(new TructiepXoso());
        listFragments.add(new Frag_MoRP1());
        listFragments.add(new Frag_Setting1());
        listFragments.add(new Frag_Setting3());
        listFragments.add(new Frag_SMS_Templates());
        listFragments.add(new Frag_Change_Password());
        listFragments.add(new Frag_Database());
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, listFragments.get(0)).commit();
        setTitle(this.listNavItems.get(0).getTitle());
        this.lvNav.setItemChecked(0, true);
        this.drawerLayout.closeDrawer(this.drawerPane);
        this.lvNav.setOnItemClickListener((adapterView, view, position, id) -> {
            currentMenuPosition = position;
            notifivationNavigated = false;
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, listFragments.get(position)).commit();
            setTitle(listNavItems.get(position).getTitle());
            lvNav.setItemChecked(position, true);
            drawerLayout.closeDrawer(drawerPane);
        });

        ActionBarDrawerToggle r6 = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                @SuppressLint("WrongConstant") InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
                View view = getCurrentFocus();
                if (view == null) {
                    view = new View(MainActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
                View view = getCurrentFocus();
                if (view == null) {
                    view = new View(MainActivity.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        };
        this.actionBarDrawerToggle = r6;
        this.drawerLayout.setDrawerListener(r6);
        this.actionBarDrawerToggle.syncState();
        notificationPermission();
        ((NotificationManager) getSystemService("notification")).cancel(1);
        startService(new Intent(this, ZBroadcast.class));

        toggleNotificationReader();

        client = TelegramClient.getClient(this);
        client.send(new TdApi.GetMe(), this);

        textErrItemCount = customactionbar.findViewById(R.id.error_badge);
        notification = customactionbar.findViewById(R.id.notification);
        notification.setOnClickListener(view -> {
            if (!notifivationNavigated || currentMenuPosition == -1) {
                notifivationNavigated = true;
                Toast.makeText(this, "Sang màn sửa tin...", 0).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, listFragments.get(1)).commit();
                setTitle(listNavItems.get(1).getTitle());
                lvNav.setItemChecked(1, true);
                drawerLayout.closeDrawer((View) drawerPane);
                return;
            }
            notifivationNavigated = false;
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, listFragments.get(currentMenuPosition)).commit();
            setTitle(listNavItems.get(currentMenuPosition).getTitle());
            lvNav.setItemChecked(currentMenuPosition, true);
            drawerLayout.closeDrawer((View) drawerPane);
        });

        postDelayBadge(new BusEvent.SetupErrorBagde(3000));

        setupBadge();
    }

    private void toggleNotificationReader() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, tamhoang.ldpro4.NotificationNewReader.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(this, tamhoang.ldpro4.NotificationNewReader.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public static void setListFragment(int i) {
        listFragments.remove(4);
        if (i == 1) {
            listFragments.add(4, new Frag_No_new());
        } else {
            listFragments.add(4, new Frag_No_old());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void postDelayBadge(BusEvent.SetupErrorBagde event) {
        final Handler handler = new Handler();
        handler.postDelayed((Runnable) () -> {
            try {
                String query = "select * from tbl_tinnhanS WHERE phat_hien_loi <> 'ok' AND ngay_nhan = '" + Get_date() + "'";

                Cursor cursor = db.GetData(query);
                mErrItemCount = cursor.getCount();
                cursor.close();
            } catch (Exception e) {
                mErrItemCount = 0;
            }

            setupBadge();
        }, event.getDelayTime());
    }

    public void setupBadge() {
        if (textErrItemCount != null) {
            if (mErrItemCount != 0) {
                textErrItemCount.setText(String.valueOf(mErrItemCount));
                textErrItemCount.setVisibility(View.VISIBLE);
            } else textErrItemCount.setVisibility(View.GONE);
        }
    }


    @Override // tamhoang.ldpro4.Telegram.TelegramClient.Callback, org.drinkless.td.libcore.telegram.Client.ResultHandler
    public void onResult(TdApi.Object object) {
        Log.e("ContentValues", "onResult: TdApi.Object: " +object);

        boolean tinHethong;
        String ten_kh;
        int type_kh;
        Cursor cursor;
        switch (object.getConstructor()) {
            case TdApi.User.CONSTRUCTOR /*{ENCODED_INT: -824771497}*/:
                this.my_id = ((TdApi.User) object).id + "";
                break;
            case TdApi.UpdateNewMessage.CONSTRUCTOR /*{ENCODED_INT: -563105266}*/:
                break;
            case TdApi.UpdateOption.CONSTRUCTOR /*{ENCODED_INT: 900822020}*/:
                TdApi.UpdateOption updateOption = (TdApi.UpdateOption) object;
                if (updateOption.name.contains("my_id")) {
                    String optionValue = updateOption.value.toString();
                    this.my_id = optionValue;
                    String substring = optionValue.substring(optionValue.indexOf("=") + 1);
                    this.my_id = substring;
                    this.my_id = substring.substring(0, substring.indexOf("\n")).trim();
                    this.db.QueryData("Update So_Om set Sphu1 = '" + this.my_id + "' WHERE ID = 1");
                    return;
                }
                return;
            case TdApi.UpdateUser.CONSTRUCTOR /*{ENCODED_INT: 1183394041}*/:
                TdApi.UpdateUser updateUser = (TdApi.UpdateUser) object;
                try {
                    if (!Json_Chat_Telegram.has(updateUser.user.id + "")) {
                        JSONObject json = new JSONObject();
                        String type = updateUser.user.type.toString();
                        json.put("type", type.substring(0, type.indexOf("{")).trim());
                        json.put("basicGroupId", updateUser.user.id);
                        firstNameTL = updateUser.user.firstName;
                        lastNameTL = updateUser.user.lastName;
                        json.put("title", "TL - " + firstNameTL + " " + lastNameTL);
                        Json_Chat_Telegram.put(updateUser.user.id + "", json);
                        return;
                    }
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            case TdApi.UpdateConnectionState.CONSTRUCTOR /*{ENCODED_INT: 1469292078}*/:
                if (((TdApi.UpdateConnectionState) object).state.getConstructor() == TdApi.ConnectionStateReady.CONSTRUCTOR) {
                    Log.d("AuthActivity", "onResult: ConnectionStateReady");
                    return;
                }
                return;
            case TdApi.UpdateAuthorizationState.CONSTRUCTOR /*{ENCODED_INT: 1622347490}*/:
                onAuthStateUpdated(((TdApi.UpdateAuthorizationState) object).authorizationState);
                return;
            case TdApi.UpdateNewChat.CONSTRUCTOR /*{ENCODED_INT: 2075757773}*/:
                TdApi.UpdateNewChat updateNewChat = (TdApi.UpdateNewChat) object;
                try {
                    if (!Json_Chat_Telegram.has(updateNewChat.chat.id + "")) {
                        JSONObject json = new JSONObject();
                        String type2 = updateNewChat.chat.type.toString();
                        json.put("type", type2.substring(0, type2.indexOf("{")).trim());
                        json.put("basicGroupId", updateNewChat.chat.id);
                        json.put("title", "TL - " + updateNewChat.chat.title);
                        Json_Chat_Telegram.put(updateNewChat.chat.id + "", json);
                        return;
                    }
                    return;
                } catch (JSONException e2) {
                    e2.printStackTrace();
                    return;
                }
            default:
                return;
        }
        if (this.my_id == "") {
            Cursor cursor2 = this.db.GetData("Select Sphu1 from so_om where ID = 1");
            cursor2.moveToFirst();
            this.my_id = cursor2.getString(0);
            cursor2.close();
        }
        TdApi.UpdateNewMessage newMessage = (TdApi.UpdateNewMessage) object;
        String senderUserId = newMessage.message.senderId + "";
        String chatId = newMessage.message.chatId + "";
        String text = ((TdApi.MessageText) newMessage.message.content).text.text.replace("'", "");
        tinHethong = !newMessage.message.isChannelPost && newMessage.message.chatId != 777000 && newMessage.message.chatId != 93372553;
        if (tinHethong) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
            dmyFormat.setTimeZone(TimeZone.getDefault());
            hourFormat.setTimeZone(TimeZone.getDefault());
            Date time = new Date((long) newMessage.message.date * 1000);
            String mNgayNhan = dmyFormat.format(time);
            String mGionhan = hourFormat.format(calendar.getTime());

            try {
                ten_kh = Json_Chat_Telegram.getJSONObject(chatId).getString("title");
            } catch (JSONException e3) {
                Cursor cursor3 = this.db.GetData("Select * From tbl_kh_new Where sdt = '" + chatId + "'");
                if (cursor3.getCount() > 0) {
                    cursor3.moveToFirst();
                    ten_kh = cursor3.getString(0);
                    cursor3.close();
                } else {
                    ten_kh = "TL - " + chatId;
                }
            }
            if (chatId.contains(this.my_id) || senderUserId.contains(this.my_id)) {
                type_kh = 2;
            } else {
                type_kh = 1;
            }
            this.db.QueryData("Insert into Chat_database Values( null,'" + mNgayNhan + "', '" + mGionhan + "', "
                    + type_kh + ", '" + ten_kh + "','" + chatId + "', 'TL','" + text + "',1)");
            sms = true;
            String sb2 = "Select * From tbl_tinnhanS WHERE ngay_nhan = '" + mNgayNhan +
                    "' And Ten_kh = '" + ten_kh + "' AND nd_goc = '" + text + "'";
            Cursor cursor111 = this.db.GetData(sb2);
            if (cursor111.getCount() == 0) {
                cursor = this.db.GetData("Select * From tbl_kh_new Where sdt = '" + chatId + "'");
                if (cursor.getCount() > 0 && text.length() > 5 && CheckIsToday(time)){
                    cursor.moveToFirst();
                    if (cursor.getInt(3) == 1 && type_kh == 1) {
                        Xulytin(chatId, text, mNgayNhan, mGionhan, type_kh);
                        return;
                    }
                    if (cursor.getInt(3) == 2 && type_kh == 1 && text.indexOf("Tra lai") == 0) {
                        Xulytin(chatId, text, mNgayNhan, mGionhan, type_kh);
                        return;
                    }
                    if (cursor.getInt(3) == 3 && type_kh == 1) {
                        Xulytin(chatId, text, mNgayNhan, mGionhan, type_kh);
                        return;
                    }
                }
                cursor.close();
            }
            cursor111.close();
        }
    }

    private void onAuthStateUpdated(TdApi.AuthorizationState authorizationState) {
        int constructor = authorizationState.getConstructor();

        Log.e("ContentValues", "onAuthStateUpdated: constructor" +constructor);

        //TODO: fake constructor
        if (constructor == TdApi.AuthorizationStateWaitCode.CONSTRUCTOR) {
            new Handler(Looper.getMainLooper()).post(this::showDialog2);
        } else if (constructor == TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR) {
            client.send(new TdApi.CheckDatabaseEncryptionKey(), this);
        } else if (constructor == TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR) {
            TdApi.TdlibParameters authStateRequest = new TdApi.TdlibParameters();
            authStateRequest.apiId = 10464015;
            authStateRequest.apiHash = "1f1b9d36ea61c12e771746fc17120173";
            authStateRequest.useMessageDatabase = true;
            authStateRequest.useSecretChats = true;
            authStateRequest.systemLanguageCode = "en";
            authStateRequest.databaseDirectory = getApplicationContext().getFilesDir().getAbsolutePath();
            authStateRequest.deviceModel = "Moto";
            authStateRequest.systemVersion = "9.0";
            authStateRequest.applicationVersion = "0.1";
            authStateRequest.enableStorageOptimizer = true;
            client.send(new TdApi.SetTdlibParameters(authStateRequest), this);
        }
    }

    private void Xulytin(String mSDT, String body, String mNgayNhan, String mGionhan, int type_kh) {
        JSONException e;
        String str = null;
        String S;
        if ((DSkhachhang.contains(mSDT) && body.indexOf("Ok") != 0 && body.indexOf("Bỏ") != 0 && body.indexOf("Thiếu") != 0) || body.contains("Tra lai")) {
            sms = true;
            JSONObject json = null;
            JSONObject caidat_tg = null;
            Cursor getTenKH = this.db.GetData("Select * FROM tbl_kh_new WHERE sdt ='" + mSDT + "'");
            getTenKH.moveToFirst();
            try {
                json = new JSONObject(getTenKH.getString(5));
                caidat_tg = json.getJSONObject("caidat_tg");
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            try {
                if (!Congthuc.CheckTime(caidat_tg.getString("tg_debc"))) {
                    try {
                        int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mNgayNhan, 1, "so_dienthoai = '"+ mSDT +"'");

                        String Ten_KH = getTenKH.getString(0);
                        int soTN = maxSoTn + 1;
                        if (!body.contains("Tra lai")) {
                            try {
                                S = "Insert Into tbl_tinnhanS values (null, '" + mNgayNhan + "', '" + mGionhan + "'," + type_kh + ", '" + Ten_KH + "', '" + getTenKH.getString(1) + "','TL', " + soTN + ", '" + body + "',null,'" + body + "', 'ko',0,1,1, null)";
                                this.db.QueryData(S);
                                str = "Tra lai";
                            } catch (SQLException e3) {}
                        } else {
                            str = "Tra lai";
                            try {
                                S = "Insert Into tbl_tinnhanS values (null, '" + mNgayNhan + "', '" + mGionhan + "'," + type_kh + ", '" + Ten_KH + "', '" + getTenKH.getString(1) + "','TL', " + soTN + ", '" + body + "',null,'" + body + "', 'ko',0,0,0, null)";
                                this.db.QueryData(S);
                            } catch (SQLException e5) {}
                        }

                        if (Congthuc.CheckDate(hanSuDung)) {
                            String sb = "Select * from tbl_tinnhanS WHERE ngay_nhan = '" + mNgayNhan +
                                    "' AND so_dienthoai = '" + mSDT +
                                    "' AND so_tin_nhan = " + soTN +
                                    " AND type_kh = " + type_kh;
                            Cursor c = this.db.GetData(sb);
                            c.moveToFirst();
                            try {
                                this.db.Update_TinNhanGoc(c.getInt(0), 1);
                            } catch (Exception e7) {
                                this.db.QueryData("Update tbl_tinnhanS set phat_hien_loi = 'ko' WHERE id = " + c.getInt(0));
                                this.db.QueryData("Delete From tbl_soctS WHERE ngay_nhan = '" + mNgayNhan + "' AND so_dienthoai = '" + mSDT + "' AND so_tin_nhan = " + soTN + " AND type_kh =" + type_kh);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                            if (!Congthuc.CheckTime("18:30") && body.indexOf(str) == -1 && type_kh == 1) {
                                this.db.Gui_Tin_Nhan(c.getInt(0));
                            }
                            c.close();
                        }
                    } catch (SQLException e8) {
                    }
                    if (!getTenKH.isClosed()) {
                        getTenKH.close();
                        return;
                    }
                }

                int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mNgayNhan, 1, "so_dienthoai = '"+ mSDT +"'");

                this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + mNgayNhan + "', '" + mGionhan + "',1, '" + getTenKH.getString(0) + "', '" + getTenKH.getString(1) + "','TL', " + (maxSoTn + 1) + ", '" + body + "',null,'" + body + "', 'Hết giờ nhận số!',0,1,1, null)");

                if (!Congthuc.CheckTime("18:30") && jSon_Setting.getInt("tin_qua_gio") == 1) {
                    sendMessage(getTenKH.getLong(1), "Hết giờ nhận!");
                }

            } catch (JSONException e10) {
                e = e10;
                e.printStackTrace();
            }
        }
    }

    public static void sendMessage(long chatId, String message) {

      TdApi.InlineKeyboardButton[] row = {new TdApi.InlineKeyboardButton("https://telegram.org?1", new TdApi.InlineKeyboardButtonTypeUrl()), new TdApi.InlineKeyboardButton("https://telegram.org?2", new TdApi.InlineKeyboardButtonTypeUrl()), new TdApi.InlineKeyboardButton("https://telegram.org?3", new TdApi.InlineKeyboardButtonTypeUrl())};
        client.send(
                new TdApi.SendMessage(chatId, 0L, 0L, null,
                        new TdApi.ReplyMarkupInlineKeyboard(new TdApi.InlineKeyboardButton[][]{row, row, row}),
                        new TdApi.InputMessageText(new TdApi.FormattedText(message, null), false, true)),
                null);
    }

    public void Suagia() {
        Cursor cur = null;
        try {
            cur = this.db.GetData("Select * From tbl_kh_new");
            if (cur.getCount() > 0 && cur.moveToFirst() && new JSONObject(new JSONObject(cur.getString(5)).getString("caidat_gia")).getDouble("dea") > 10.0d) {
                Cursor cursor = this.db.GetData("Select * From tbl_kh_new");
                while (cursor.moveToNext()) {
                    JSONObject json = new JSONObject(cursor.getString(5));
                    JSONObject caidat_gia = new JSONObject(json.getString("caidat_gia"));
                    Iterator<String> keys = caidat_gia.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        if (caidat_gia.getDouble(key) > 100.0d) {
                            caidat_gia.put(key, caidat_gia.getDouble(key) / 1000.0d);
                        }
                    }
                    json.put("caidat_gia", caidat_gia);
                    Database database = this.db;
                    database.QueryData("update tbl_kh_new set tbl_mb = '" + json.toString() + "' WHERE ten_kh = '" + cursor.getString(0) + "'");
                }
                cursor.close();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable th) {
            cur.close();
            throw th;
        }
        cur.close();
    }

    private void notificationPermission() {
        boolean enabled;
        ComponentName cn = new ComponentName(this, NotificationNewReader.class);
        String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        enabled = flat != null && flat.contains(cn.flattenToString());
        if (!enabled) {
            showAlertBox("Truy cập thông báo!", "Hãy cho phép phần mềm được truy cập thông báo của điện thoại để kích hoạt chức năng nhắn tin.")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show().setCanceledOnTouchOutside(false);
        }
    }

    public AlertDialog.Builder showAlertBox(String title, String message) {
        return new AlertDialog.Builder(this).setTitle(title).setMessage(message);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity
    public void onDestroy() {
        super.onDestroy();
        deleteCache(getApplicationContext());
    }

    public static void deleteCache(Context context2) {
        try {
            deleteDir(context2.getCacheDir());
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            for (String str : dir.list()) {
                if (!deleteDir(new File(dir, str))) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir == null || !dir.isFile()) {
            return false;
        } else {
            return dir.delete();
        }
    }

    public void LayDulieuJson1() {
        try {
            JSONArray m_jArry = new JSONObject(loadJSONFromAsset("kytuthaythe.json")).getJSONArray("formules");
            int count = 0;
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject item = m_jArry.getJSONObject(i);
                String key = item.optString("type", "");
                if (!key.isEmpty()) {
                    JSONArray datas = item.getJSONArray("datas");
                    count += datas.length();
                    for (int k = 0; k < datas.length(); k++) {
                        String value = datas.getString(k);
                        HashMap<String, String> m_li = new HashMap<>();
                        m_li.put("type", key);
                        m_li.put("datas", value);
                        formList.add(m_li);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void LayDulieuJson2() {
        if (formArray.size() == 0) {
            try {
                JSONArray m_jArry = new JSONObject(loadJSONFromAsset("thaythe.json")).getJSONArray("listKHs");
                for (int i = 0; i < m_jArry.length(); i++) {
                    JSONObject item = m_jArry.getJSONObject(i);
                    String key = item.optString("str", "");
                    String value = item.optString("repl_str", "");
                    if (!key.isEmpty()) {
                        HashMap<String, String> m_li = new HashMap<>();
                        m_li.put("str", key);
                        m_li.put("repl_str", value);
                        formArray.add(m_li);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String loadJSONFromAsset(String Filename) {
        try {
            InputStream is = getAssets().open(Filename);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void onClick(View v) {
        showDialog(0);
    }

    public void onMenu(View v) {
        String[] menus;
        menus = new String[]{"Từ điển cá nhân", "Nhập dàn giữ số", "Cài đặt chuyển thẳng", (my_id != "")? "Logout Telegram" : "Login Telegram"};

        PopupMenu popupMenu = new PopupMenu(this, v);
        for (int i = 0; i < menus.length; i++) {
            popupMenu.getMenu().add(1, i, i, menus[i]);
        }
        new AlertDialog.Builder(this);
        popupMenu.setOnMenuItemClickListener(item -> {
            currentMenuPosition = -1;
            notifivationNavigated = false;
            int order = item.getOrder();
            if (order == 0) {
                startActivity(new Intent(this,  Activity_thaythe.class));
            } else if (order == 1) {
                this.startActivity(new Intent(this, Activity_GiuSo.class));
            } else if (order == 2) {
                this.startActivity(new Intent(this, Activity_ChuyenThang.class));
            } else if (order == 3) {
                if (this.my_id != "") {
                    AlertDialog.Builder bui = new AlertDialog.Builder(this);
                    bui.setTitle("Thoát Telegram?");
                    bui.setPositiveButton("OK", (dialog, which) -> {
                        db.QueryData("Update So_om set  Sphu1 ='' where ID = 1");
                        client.send(new TdApi.LogOut(), this, null);
                        TelegramClient.client = null;
                        my_id = "";
                        Toast.makeText(this, "Đã thoát Telegram", Toast.LENGTH_SHORT).show();
                    });
                    bui.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    bui.create().show();
                } else {
                    showDialog1();
                }
            }
            return true;
        });
        popupMenu.show();
    }

    public void showDialog1() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_tele_login);
        dialog.getWindow().setLayout(-1, -2);
        final EditText authPhone = (EditText) dialog.findViewById(R.id.authPhone);
        TextView textView = (TextView) dialog.findViewById(R.id.authCodeInfo);
        ((Button) dialog.findViewById(R.id.loginBtn)).setOnClickListener(view -> {
            String PhoneNumber = authPhone.getText().toString();
            if (PhoneNumber.length() == 10) {
                client = TelegramClient.getClient(this);
                client.send(new TdApi.SetAuthenticationPhoneNumber("+84" + PhoneNumber.substring(1), null), this);
                dialog.dismiss();
                return;
            }
            Toast.makeText(this, "Hãy nhập 10 số của số điện thoại!", Toast.LENGTH_SHORT).show();
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public void showDialog2() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_auth);
        dialog.getWindow().setLayout(-1, -2);
        final EditText authPhone = (EditText) dialog.findViewById(R.id.authCode);
        ((Button) dialog.findViewById(R.id.checkBtn)).setOnClickListener(view -> {
            String code = authPhone.getText().toString();
            if (code.length() == 5) {
                client.send(new TdApi.CheckAuthenticationCode(code), this);
                dialog.dismiss();
                return;
            }
            Toast.makeText(this, "Hãy nhập đủ 5 số được gửi về Telegram!", Toast.LENGTH_SHORT).show();
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override // android.support.v4.app.FragmentActivity
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Bạn có muốn thoát không?").setCancelable(true).setPositiveButton("Thoát", (dialog, id) -> {
            Intent homeIntent = new Intent("android.intent.action.MAIN");
            homeIntent.addCategory("android.intent.category.HOME");
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }).setNegativeButton("Không", (DialogInterface.OnClickListener) null).show();
    }

    public static String Get_date() {//"yyyy-mm-dd"
        if (mDay < 10 && mMonth + 1 < 10) {
            return mYear + "-0" + (mMonth + 1) + "-0" + mDay;
        } else if (mDay < 10) {
            return mYear + "-" + (mMonth + 1) + "-0" + mDay;
        } else if (mMonth + 1 < 10) {
            return mYear + "-0" + (mMonth + 1) + "-" + mDay;
        } else {
            return mYear + "-" + (mMonth + 1) + "-" + mDay;
        }
    }

    public static String Get_ngay() {//"dd/mm/yyyy"
        if (mDay < 10 && mMonth + 1 < 10) {
            return "0" + mDay + "/0" + (mMonth + 1) + "/" + mYear;
        } else if (mDay < 10) {
            return "0" + mDay + "/" + (mMonth + 1) + "/" + mYear;
        } else if (mMonth + 1 < 10) {
            return mDay + "/0" + (mMonth + 1) + "/" + mYear;
        } else {
            return mDay + "/" + (mMonth + 1) + "/" + mYear;
        }
    }

    public Dialog onCreateDialog(int id) {
        if (id != 0) {
            return null;
        }
        return new DatePickerDialog(this, onDateSetListener, mYear, mMonth, mDay);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // android.support.v7.app.AppCompatActivity
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.actionBarDrawerToggle.syncState();
    }

    public static String Get_link() {
        return "http://103.74.121.24:9030/";
    }

    public static String Get_link_signin() {
        return Get_link() + "/api/signin";
    }

    public void showLoadingDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideLoadingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override // android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback, android.support.v4.app.FragmentActivity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 1) {
            if (requestCode != 2) {
                return;
            }
        } else if (grantResults.length <= 0 || grantResults[0] != 0) {
            Toast.makeText(getApplicationContext(), "Can't access messages.", Toast.LENGTH_SHORT).show();
            return;
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") != 0 && !ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_CONTACTS")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_CONTACTS"}, 2);
        }
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            Toast.makeText(getApplicationContext(), "Can't access messages.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}