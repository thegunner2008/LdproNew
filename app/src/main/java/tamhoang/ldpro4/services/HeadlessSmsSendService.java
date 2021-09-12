package tamhoang.ldpro4.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HeadlessSmsSendService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }
}
