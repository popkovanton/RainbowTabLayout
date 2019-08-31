package com.watermelonman.rainbowtablayoutexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.watermelonman.rainbowtablayoutlibrary.RainbowTabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static int TABS_COUNT = 8;
    private ViewPager viewPager;
    private RainbowPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();
        initTabs();
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new RainbowPagerAdapter(getSupportFragmentManager(), getColorsForTabs());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
    }

    private void initTabs() {
        RainbowTabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setColorsForTabs(getColorsForTabs());
        tabLayout.setTabMinByMax(false);
        tabLayout.setViewPager(viewPager);
    }

    private int[] getColorsForTabs() {
        int[] colors = new int[TABS_COUNT];
        colors[0] = Color.parseColor("#FF5959");
        colors[1] = Color.parseColor("#FF9900");
        colors[2] = Color.parseColor("#FFCC16");
        colors[3] = Color.parseColor("#30FF45");
        colors[4] = Color.parseColor("#01E0FF");
        colors[5] = Color.parseColor("#4BA9FF");
        colors[6] = Color.parseColor("#9378FF");
        colors[7] = Color.parseColor("#FD7BF0");
        return colors;
    }
}
