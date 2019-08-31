package com.watermelonman.rainbowtablayoutexample;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RainbowPagerAdapter extends FragmentPagerAdapter {
    private int[] colors;

    public RainbowPagerAdapter(FragmentManager manager, int[] colors) {
        super(manager);
        this.colors = colors;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return RainbowFragment.newInstance(
                colors[position]);
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Title Test";
    }

}
