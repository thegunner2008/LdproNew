package tamhoang.ldpro4.receivers;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import tamhoang.ldpro4.Congthuc.Congthuc;
import tamhoang.ldpro4.MainActivity;
import tamhoang.ldpro4.R;
import tamhoang.ldpro4.data.BriteDb;
import tamhoang.ldpro4.data.Database;
import tamhoang.ldpro4.services.SaveSmsService;

public class SMSReceiver extends BroadcastReceiver {
    String Ten_KH;
    String body = "";
    JSONObject caidat_gia;
    JSONObject caidat_tg;
    Database db;
    JSONObject json;
    Context mContext;
    String mGionhan;
    String mNgayNhan;
    String mSDT;
    SmsMessage[] messages = null;
    int soTN;
    public void onReceive(Context context, Intent intent) {
        String trim;
        boolean Ktra;
        JSONException e;
        this.db = new Database(context);
        this.mContext = context;
        boolean Ktra2 = true;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            this.messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                this.messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                SmsMessage currentSMS = getIncomingMessage(pdus[i], bundle);
                issueNotification(context, currentSMS.getDisplayOriginatingAddress(), currentSMS.getDisplayMessageBody());
                saveSmsInInbox(context, currentSMS);
            }
            SmsMessage sms = this.messages[0];
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
            dmyFormat.setTimeZone(TimeZone.getDefault());
            hourFormat.setTimeZone(TimeZone.getDefault());
            this.mNgayNhan = dmyFormat.format(calendar.getTime());
            this.mGionhan = hourFormat.format(calendar.getTime());
            this.mSDT = "";
            try {
                if (this.messages.length == 1) {
                    try {
                        if (!sms.isReplace()) {
                            StringBuilder bodyText = new StringBuilder();
                            for (int i2 = 0; i2 < this.messages.length; i2++) {
                                bodyText.append(this.messages[i2].getMessageBody());
                            }
                            this.body = bodyText.toString().replace("'", "");
                            trim = sms.getDisplayOriginatingAddress().replace(" ", "").trim();
                            this.mSDT = trim;
                            if (trim.startsWith("0")) {
                                this.mSDT = "+84" + this.mSDT.substring(1);
                            }
                            if (MainActivity.DSkhachhang.size() == 0) {
                                this.db.LayDanhsachKH();
                            }
                            if ((MainActivity.DSkhachhang.contains(this.mSDT)
                                    || this.body.indexOf("Ok") == 0
                                    || this.body.indexOf("Bỏ") == 0
                                    || this.body.indexOf("Thiếu") == 0)
                                    || this.body.contains("Tra lai")) {
                                MainActivity.sms = true;
                                try {
                                    if (MainActivity.jSon_Setting.getInt("tin_trung") > 0) {
                                        Cursor Ktratin = this.db.GetData("Select id From tbl_tinnhanS WHERE so_dienthoai = '" + this.mSDT + "' AND ngay_nhan = '" + this.mNgayNhan + "' AND nd_goc = '" + this.body + "'");
                                        Ktratin.moveToFirst();
                                        if (Ktratin.getCount() > 0) {
                                            Ktra2 = false;
                                        }
                                        if (!Ktratin.isClosed()) {
                                            Ktratin.close();
                                        }
                                    }
                                } catch (JSONException e2) {
                                    Log.d(SMSReceiver.class.getName(), e2.getMessage());
                                }
                                try {
                                    Cursor getTenKH = this.db.GetData("Select * FROM tbl_kh_new WHERE sdt ='" + this.mSDT + "'");
                                    getTenKH.moveToFirst();
                                    if (Ktra2) {
                                        try {
                                            JSONObject jSONObject = new JSONObject(getTenKH.getString(5));
                                            this.json = jSONObject;
                                            this.caidat_gia = jSONObject.getJSONObject("caidat_gia");
                                            this.caidat_tg = this.json.getJSONObject("caidat_tg");

                                            Ktra = Ktra2;
                                            if (Congthuc.CheckTime(this.caidat_tg.getString("tg_debc"))) {

                                                int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mNgayNhan, 1, "so_dienthoai = '"+ mSDT +"'");

                                                this.Ten_KH = getTenKH.getString(0);
                                                this.soTN = maxSoTn + 1;
                                                this.db.QueryData(!this.body.contains("Tra lai") ? "Insert Into tbl_tinnhanS values (null, '" + this.mNgayNhan + "', '" + this.mGionhan + "',1, '" + this.Ten_KH + "', '" + getTenKH.getString(1) + "','sms', " + this.soTN + ", '" + this.body + "',null,'" + this.body + "', 'ko',0,1,1, null)" : "Insert Into tbl_tinnhanS values (null, '" + this.mNgayNhan + "', '" + this.mGionhan + "',1, '" + this.Ten_KH + "', '" + getTenKH.getString(1) + "','sms', " + this.soTN + ", '" + this.body + "',null,'" + this.body + "', 'ko',0,0,0, null)");
                                                if (Congthuc.CheckDate(MainActivity.hanSuDung)) {
                                                    Cursor c = this.db.GetData("Select * from tbl_tinnhanS WHERE ngay_nhan = '" + this.mNgayNhan + "' AND so_dienthoai = '" + this.mSDT + "' AND so_tin_nhan = " + this.soTN + " AND type_kh = 1");
                                                    c.moveToFirst();
                                                    this.db.Update_TinNhanGoc(c.getInt(0), 1);
                                                    if (!Congthuc.CheckTime("18:30") && this.body.indexOf("Tra lai") == -1) {
                                                        this.db.Gui_Tin_Nhan(c.getInt(0));
                                                    }
                                                    c.close();
                                                }
                                            } else {
                                                int maxSoTn = BriteDb.INSTANCE.getMaxSoTinNhan(mNgayNhan, 1, "so_dienthoai = '" + mSDT +"'");

                                                this.Ten_KH = getTenKH.getString(0);
                                                this.soTN = maxSoTn + 1;
                                                this.db.QueryData("Insert Into tbl_tinnhanS values (null, '" + this.mNgayNhan + "', '" + this.mGionhan + "',1, '" + this.Ten_KH + "', '" + getTenKH.getString(1) + "','sms', " + this.soTN + ", '" + this.body + "',null,'" + this.body + "', 'Hết giờ nhận số!',0,1,1, null)");
                                                if (!Congthuc.CheckTime("18:30") && MainActivity.jSon_Setting.getInt("tin_qua_gio") == 1) {
                                                    this.db.SendSMS(getTenKH.getString(1), "Hết giờ nhận!");
                                                }
                                            }

                                        } catch (JSONException e3) {
                                            Log.d(SMSReceiver.class.getName(), e3.getMessage());
                                        } catch (Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                    } else {
                                        Ktra = Ktra2;
                                    }
                                    if (!getTenKH.isClosed()) {
                                        getTenKH.close();
                                    }
                                    return;
                                } catch (Exception e10) {
                                    Log.d(SMSReceiver.class.getName(), e10.getMessage());
                                    return;
                                }
                            }
                        }
                    } catch (Exception e11) {
                        Log.d(SMSReceiver.class.getName(), e11.getMessage());
                        return;
                    }
                }
                this.body = sms.getDisplayMessageBody().replace("'", "");
                trim = sms.getDisplayOriginatingAddress().replace(" ", "").trim();
                this.mSDT = trim;
            } catch (Exception e13) {
                Log.d(SMSReceiver.class.getName(), e13.getMessage());
            }
        }
    }

    private void saveSmsInInbox(Context context, SmsMessage sms) {
        Intent serviceIntent = new Intent(context, SaveSmsService.class);
        serviceIntent.putExtra("sender_no", sms.getDisplayOriginatingAddress());
        serviceIntent.putExtra("message", sms.getDisplayMessageBody());
        serviceIntent.putExtra("date", sms.getTimestampMillis());
        context.startService(serviceIntent);
    }

    @SuppressLint("WrongConstant")
    private void issueNotification(Context context, String senderNo, String message) {
        ((NotificationManager) context.getSystemService("notification")).notify(101, new NotificationCompat.Builder(context).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(senderNo).setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setAutoCancel(true).setContentText(message).build());
    }

    /* JADX INFO: Multiple debug info for r0v7 android.telephony.SmsMessage: [D('format' java.lang.String), D('currentSMS' android.telephony.SmsMessage)] */
    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        if (Build.VERSION.SDK_INT < 23) {
            return SmsMessage.createFromPdu((byte[]) aObject);
        }
        return SmsMessage.createFromPdu((byte[]) aObject, bundle.getString("format"));
    }
}