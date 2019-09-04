package com.popkovanton.rainbowtablayoutexample;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RainbowPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;

    public RainbowPagerAdapter(FragmentManager manager, String[] titles) {
        super(manager);
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return RainbowFragment.newInstance(
                titles[position]);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
