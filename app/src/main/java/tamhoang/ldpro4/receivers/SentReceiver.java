package tamhoang.ldpro4.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SentReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        int resultCode = getResultCode();
        if (resultCode == -1) {
            return;
        }
        if (resultCode == 1) {
            Toast.makeText(context, "Sai số điện thoại", 0).show();
        } else if (resultCode == 2) {
            Toast.makeText(context, "Không có mạng!", 0).show();
        } else if (resultCode == 3) {
            Toast.makeText(context, "Null PDU", 0).show();
        } else if (resultCode == 4) {
            Toast.makeText(context, "Không có dịch vụ", 0).show();
        }
    }
}
