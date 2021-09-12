package tamhoang.ldpro4.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import tamhoang.ldpro4.R;


public class DeliverReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        int resultCode = getResultCode();
        if (resultCode != -1 && resultCode == 0) {
            Toast.makeText(context, R.string.sms_not_delivered, Toast.LENGTH_SHORT).show();
        }
    }
}
