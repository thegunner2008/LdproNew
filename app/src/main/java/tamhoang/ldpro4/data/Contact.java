package tamhoang.ldpro4.data;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.os.Bundle;

public class Contact {
    public String app;
    public String name;
    public PendingIntent pendingIntent;
    public Bundle remoteExtras;
    public RemoteInput remoteInput;

    public String getName() {
        return this.name;
    }

    public String getApp() {
        return this.app;
    }

    public PendingIntent getPendingIntent() {
        return this.pendingIntent;
    }

    public RemoteInput getRemoteInput() {
        return this.remoteInput;
    }

    public Bundle getRemoteExtras() {
        return this.remoteExtras;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public void setApp(String app2) {
        this.app = app2;
    }

    public void setPendingIntent(PendingIntent pendingIntent2) {
        this.pendingIntent = pendingIntent2;
    }

    public void setRemoteInput(RemoteInput remoteInput2) {
        this.remoteExtras = this.remoteExtras;
    }

    public void setRemoteExtras(Bundle remoteExtras2) {
        this.remoteExtras = remoteExtras2;
    }

    @Override
    public String toString() {
        return "Contact{" +
                ", name='" + name + '\'' +
                '}';
    }
}
