package com.example.sayacim.Util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sayacim.Hatirlatici.HatirlaticiMainActivity;
import com.example.sayacim.Tatiller.TatilActivity;
import com.example.sayacim.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";
    static Integer currentItemId;

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx,Integer activity_num){
        Log.d(TAG,"setupBottomNavigationView: setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);

        Menu menu = bottomNavigationViewEx.getMenu();
        currentItemId = menu.getItem(activity_num).getItemId();


    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){

        view.setOnNavigationItemSelectedListener(new BottomNavigationViewEx.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Integer itemId = menuItem.getItemId();
                if (itemId.intValue() == currentItemId.intValue()){
                }else if(itemId == R.id.ic_tatil){
                    currentItemId = itemId;

                    Intent intentTatil = new Intent(context, TatilActivity.class); // activity_num 0
                    context.startActivity(intentTatil);
                }else if(itemId == R.id.ic_hatirlatici){
                    currentItemId = itemId;
                    Intent intentHatirlatici = new Intent(context, HatirlaticiMainActivity.class); // activity_num 1
                    context.startActivity(intentHatirlatici);
                }
                return false;
            }
        });
    }
}
