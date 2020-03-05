package com.example.sayacim.Hatirlatici;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sayacim.Adapters.DataAdapter;
import com.example.sayacim.Tatiller.TatilActivity;
import com.example.sayacim.Util.AlertReceiver;
import com.example.sayacim.Util.DatabaseHelper;
import com.example.sayacim.R;
import com.example.sayacim.Util.BottomNavigationViewHelper;
import com.example.sayacim.Adapters.SectionsPagersAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class HatirlaticiMainActivity extends AppCompatActivity {

    private static final String TAG = "HatirlaticiMainActivity";
    private static final int ACTIVITY_NUM = 1;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private DataAdapter dataAdapter;
    NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hatirlatici);
        Log.d(TAG,"onCreate:starting.");
        notificationManager = NotificationManagerCompat.from(this);

        setupBottomNavigationView();
        setupViewPager();

        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        dataAdapter = new DataAdapter(this,getAllItems());
    }
    public DataAdapter getDataAdapter(){
        return dataAdapter;
    }


    /**
     * db insert
     */
    public void InsertData(ContentValues cv){
        String time = cv.getAsString("time");
        String title = cv.getAsString("title");
        String tarih = cv.getAsString("date");
        String[] timeparts = time.split("\\:");
        String[] tarihParts = tarih.split("\\/");
        Log.d(TAG, "InsertData: tarihparts yıl->"+tarihParts[2]+" ay->" + tarihParts[1] +" gun->"+tarihParts[0]);
        Log.d(TAG, "InsertData: parseint yıl:"+ Integer.parseInt(tarihParts[2]));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(tarihParts[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(tarihParts[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tarihParts[0] ));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeparts[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(timeparts[1]));
        calendar.set(Calendar.SECOND,00);

        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "InsertData: currentTime : "+currentTime);
        Log.d(TAG, "InsertData: calendarTime : "+calendar.getTime());
        Log.d(TAG,"alarm saati suan ki zamandan onceye mi kurulmus? --> "+calendar.before(currentTime));
        if (calendar.getTime().before(currentTime)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Uyari");
            builder.setMessage("Gecmis bir zamana hatirlatici kuramazsiniz!");
            builder.setNegativeButton("Tamam", null);
            builder.show();
        }else{
            long insertedVal = sqLiteDatabase.insert(DatabaseHelper.R_TABLE_NAME,null,cv);
            if (insertedVal != -1){
                Cursor cursor = dataAdapter.swapCursor(getAllItems());
                cursor.moveToLast();
                Integer id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID));
                startAlarm(calendar,tarih,id,title);
                Toast.makeText(this,"Olay kaydedildi!",Toast.LENGTH_LONG).show();
            }else{
            }

        }

    }

    public void startAlarm(Calendar calendar,String tarihString,Integer id,String title) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
                intent.putExtra("ID", id);
                intent.putExtra("TITLE",title);
                intent.putExtra("TARIH",tarihString);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,id,intent,0);
            Log.d(TAG, "startAlarm: id ->"+id);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
    }


    public void delData(Integer id){
        dataAdapter.swapCursor(getAllItems());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("ID", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,id,intent,0);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * db sorgusu
     */
    public Cursor getAllItems(){
        return sqLiteDatabase.query(databaseHelper.R_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * tepedeki yönlendirmeleri kuruyor
     */
    private void setupViewPager(){
        SectionsPagersAdapter adapter = new SectionsPagersAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecyclerViewFragment() );
        adapter.addFragment(new AddReminderFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_logo);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_addreminder);
    }


    /**
     *BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG,"setupBottomNavigationView:setting up setupBottomNavigationView.");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx,ACTIVITY_NUM);
        BottomNavigationViewHelper.enableNavigation(HatirlaticiMainActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = ((Menu) menu).getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);


    }
}
