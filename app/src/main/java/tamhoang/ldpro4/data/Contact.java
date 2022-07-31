package tamhoang.ldpro4.data;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.os.Bundle;

import java.util.ArrayList;

public class Contact {
    public String app;
    public String name;
    public PendingIntent pendingIntent;
    public Bundle remoteExtras;
    public RemoteInput remoteInput;
    public int process = 0;
    public int number = 1;
    public ArrayList<String> waitingList = new ArrayList<>();

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

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<String> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(ArrayList<String> waitingList) {
        this.waitingList = waitingList;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "app='" + app + '\'' +
                ", name='" + name + '\'' +
                ", process=" + process +
                ", number=" + number +
                ", waitingList=" + waitingList +
                '}';
    }
}
