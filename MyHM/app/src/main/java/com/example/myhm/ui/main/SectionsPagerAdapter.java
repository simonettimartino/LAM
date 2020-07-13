package com.example.myhm.ui.main;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myhm.Grafici;
import com.example.myhm.Home;
import com.example.myhm.MyData;
import com.example.myhm.NewReport;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{0,1,2,4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MyData();
                break;
            case 1:
                fragment = new Home();
               // fragment = new Calendario();
                break;
            case 2:
                fragment = new NewReport();
                break;
            case 3:
                fragment = new Grafici();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PREFERENZE";
            case 1:
                return "CALENDAR";
            case 2:
                return "NEW REP";
            case 3:
                return "GRAFICI";
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 4;
    }
}