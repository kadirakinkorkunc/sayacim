package com.example.sayacim.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.sayacim.Hatirlatici.HatirlaticiMainActivity;

import java.util.Calendar;
import java.util.Date;

public class RebootReceiver extends BroadcastReceiver {

    Cursor cursor;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private static final String TAG = "RebootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Reboot onReceive: starting");
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        Log.d(TAG, "onReceive: objeler olusturuldu");
        cursor = sqLiteDatabase.query(databaseHelper.R_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        Log.d(TAG, "onReceive: cursor.getCount()->"+cursor.getCount());
        while (cursor.moveToNext()){
            Log.d(TAG, "onReceive: SELAM ABÝ ");
           // cursor.moveToNext();
            Integer id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE));
            String tarih = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DATE));
            String saat = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TIME));


            String[] tarihParts = tarih.split("\\/");
            String[] saatParts = saat.split("\\:");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(tarihParts[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(tarihParts[1]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tarihParts[0]));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(saatParts[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(saatParts[1]));
            calendar.set(Calendar.SECOND, 00);
            Date currentTime = Calendar.getInstance().getTime();


            if (calendar.getTime().before(currentTime)) {

                Log.d(TAG, "PAST startAlarm: id ->" + title);
            } else {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                intent = new Intent(context, AlertReceiver.class);
                intent.putExtra("ID", id);
                intent.putExtra("TITLE", title);
                intent.putExtra("TARIH", tarih);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
                Log.d(TAG, "startAlarm: id ->" + title);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            }
        }
        cursor.close();
        Log.d(TAG, "onReceive: son!");
    }
}