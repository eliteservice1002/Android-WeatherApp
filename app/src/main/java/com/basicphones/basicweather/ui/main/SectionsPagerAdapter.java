package com.basicphones.basicweather.ui.main;

import android.content.Context;
import android.widget.Toast;

import com.basicphones.basicweather.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public  ArrayList<Fragment> mFragmentList = new ArrayList<>();
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.weather_tab, R.string.hourly_title, R.string.daily, R.string.weather_map_tab};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getItemPosition(Object object){
        return super.getItemPosition(object);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

}