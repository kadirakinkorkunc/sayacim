package com.example.sayacim.Util;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG = "AlertReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: starting");
        Integer id;
        String tarih,title;
        Bundle extra=intent.getExtras();
        if (extra != null){
            id = extra.getInt("ID");
            tarih = extra.getString("TARIH");
            title = extra.getString("TITLE");

            Log.d(TAG, "onReceive: if success situation ->  ");
        }else{
            id = -1;
            tarih = "";
            title = "";
            Log.d(TAG, "onReceive in else situation ");
        }
        Log.d(TAG, "onReceive: id ->  "+id);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(id,title,tarih);
        notificationHelper.getManager().notify(id,nb.build());
    }
}
