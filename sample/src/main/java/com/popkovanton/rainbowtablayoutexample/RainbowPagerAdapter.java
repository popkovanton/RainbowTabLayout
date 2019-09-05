package com.popkovanton.rainbowtablayoutexample;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RainbowPagerAdapter extends FragmentPagerAdapter {
    private int count;

    public RainbowPagerAdapter(FragmentManager manager, int count) {
        super(manager);
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return RainbowFragment.newInstance(
                position);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title " + position;
    }

}
