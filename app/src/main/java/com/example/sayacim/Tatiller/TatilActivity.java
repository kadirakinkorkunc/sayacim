package com.example.sayacim.Tatiller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sayacim.Adapters.DataAdapter;
import com.example.sayacim.Util.DatabaseHelper;
import com.example.sayacim.R;
import com.example.sayacim.Util.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class TatilActivity extends AppCompatActivity {
    private static final String TAG = "TatilActivity";
    private static final int ACTIVITY_NUM = 0;
    SQLiteDatabase mDatabase;
    DatabaseHelper dbHelper;
    private DataAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tatiller);
        Log.d(TAG,"onCreate:starting.");
        setupBottomNavigationView();

        dbHelper = new DatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        recyclerView = findViewById(R.id.recycler_tatiller);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DataAdapter(this,getAllItems());
        recyclerView.setAdapter(adapter);


    }

    public void delData(){
        adapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems(){
        return mDatabase.query(dbHelper.H_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }



    /**
     *BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG,"setupBottomNavigationView:setting up setupBottomNavigationView.");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx,ACTIVITY_NUM);
        BottomNavigationViewHelper.enableNavigation(TatilActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = ((Menu) menu).getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);


    }
}
