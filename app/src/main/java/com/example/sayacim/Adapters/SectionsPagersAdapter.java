package com.example.sayacim.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Class that stores fragments for tasks
 */
public class SectionsPagersAdapter extends FragmentPagerAdapter {
    private static final String TAG = "SectionsPagersAdapter";
    public SectionsPagersAdapter(FragmentManager fm) {
        super(fm);
    }
    private final List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }
}
