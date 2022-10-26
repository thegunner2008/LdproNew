package tamhoang.ldpro4.data;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.os.Bundle;

import java.util.ArrayList;

import tamhoang.ldpro4.notifLib.models.Action;
import tamhoang.ldpro4.notifLib.models.RemoteInputParcel;


public class Contact {
    public String app;
    public String name;
    public PendingIntent pendingIntent;
    public Bundle remoteExtras;
    public RemoteInput remoteInput;
    public Action action;
    public RemoteInputParcel remoteInput2;

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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public RemoteInputParcel getRemoteInput2() {
        return remoteInput2;
    }

    public void setRemoteInput2(RemoteInputParcel remoteInput2) {
        this.remoteInput2 = remoteInput2;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "app='" + app + '\'' +
                ", name='" + name + '\'' +
                ", pendingIntent=" + pendingIntent +
                ", remoteExtras=" + remoteExtras +
                ", remoteInput=" + remoteInput +
                ", action=" + action +
                ", remoteInput2=" + remoteInput2 +
                ", process=" + process +
                ", number=" + number +
                ", waitingList=" + waitingList +
                '}';
    }
}
