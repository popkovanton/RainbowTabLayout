package com.popkovanton.rainbowtablayoutexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;

import com.popkovanton.rainbowtablayoutlibrary.RainbowTabLayout;


public class MainActivity extends AppCompatActivity {
    private final static int TABS_COUNT = 7;
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
        tabLayout.setColorForTabs(
                Color.parseColor("#FF5959"),
                Color.parseColor("#30FF45"));
        tabLayout.setViewPager(viewPager);
    }

    private String[] getColorsForTabs() {
        String[] colors = new String[TABS_COUNT];
        colors[0] = "Test";
        colors[1] = "TestTestTest";
        colors[2] = "TestTestTestTestTestTest";
        colors[3] = "TestTestTestTestTest";
        colors[4] = "TestTestTestTest";
        colors[5] = "Test";
        colors[6] = "TestTest";
        return colors;
    }
}
