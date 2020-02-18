package com.popkovanton.rainbowtablayoutexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.Color;
import android.os.Bundle;

import com.popkovanton.rainbowtablayoutlibrary.RainbowTabLayout;

import static com.popkovanton.rainbowtablayoutlibrary.TabIndicatorPosition.ALL;

public class MainActivity extends AppCompatActivity {
    private final static int TABS_COUNT = 7;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViewPager();
        initTabs1();
        initTabs2();
        initTabs3();
        initTabs4();
    }


    private void initViewPager() {
        RainbowPagerAdapter pagerAdapter = new RainbowPagerAdapter(getSupportFragmentManager(), TABS_COUNT);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initTabs1() {
        RainbowTabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setBackgroundTabColors(Color.LTGRAY);
        tabLayout.setDrawSeparator(true);
        tabLayout.setViewPager(viewPager);
    }

    private void initTabs2() {
        RainbowTabLayout tabLayout = findViewById(R.id.tabs2);
        tabLayout.setBackgroundTabColors(Color.LTGRAY);
        tabLayout.setIndicatorColors(getColors());
        tabLayout.setTabIndicator(true);
        tabLayout.setTextColorBlend(true);
        tabLayout.setViewPager(viewPager);
    }

    private void initTabs3() {
        RainbowTabLayout tabLayout = findViewById(R.id.tabs3);
        tabLayout.setBackgroundTabColors(Color.LTGRAY);
        tabLayout.setIndicatorColors(getColors());
        tabLayout.setTabIndicatorPosition(ALL);
        tabLayout.setTabIndicator(true);
        tabLayout.setTextColorBlend(true);
        tabLayout.setViewPager(viewPager);
    }

    private void initTabs4() {
        RainbowTabLayout tabLayout = findViewById(R.id.tabs4);
        tabLayout.setTextSelectedColor(Color.WHITE);
        tabLayout.setBackgroundTabColors(Color.LTGRAY);
        tabLayout.setTabLineColors(getColors());
        tabLayout.setIndicatorColors(getColors());
        tabLayout.setTabIndicatorPosition(ALL);
        tabLayout.setTabIndicator(true);
        tabLayout.setTabLine(true);
        tabLayout.setTextColorBlend(true);
        tabLayout.setViewPager(viewPager);
    }

    private int[] getColors() {
        int[] colors = new int[TABS_COUNT];
        colors[0] = getResources().getColor(R.color.color1);
        colors[1] = getResources().getColor(R.color.color2);
        colors[2] = getResources().getColor(R.color.color3);
        colors[3] = getResources().getColor(R.color.color4);
        colors[4] = getResources().getColor(R.color.color5);
        colors[5] = getResources().getColor(R.color.color6);
        colors[6] = getResources().getColor(R.color.color7);
        return colors;
    }
}
