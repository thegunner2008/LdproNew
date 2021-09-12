package tamhoang.ldpro4.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;

import tamhoang.ldpro4.constants.Constants;

public class UpdateSMSService extends IntentService {
    public UpdateSMSService() {
        super("UpdateSMSReceiver");
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        markSmsRead(intent.getLongExtra("id", -123));
    }

    public void markSmsRead(long j) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.READ, "1");
            ContentResolver contentResolver = getContentResolver();
            contentResolver.update(Uri.parse("content://sms/" + j), contentValues, (String) null, (String[]) null);
        } catch (Exception unused) {
        }
    }
}
