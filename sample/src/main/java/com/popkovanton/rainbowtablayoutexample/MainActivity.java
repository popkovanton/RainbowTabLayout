package com.popkovanton.rainbowtablayoutexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.Color;
import android.os.Bundle;

import com.popkovanton.rainbowtablayoutlibrary.IndicatorPosition;
import com.popkovanton.rainbowtablayoutlibrary.RainbowTabLayout;

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
        initTabs();
    }

    private void initViewPager() {
        RainbowPagerAdapter pagerAdapter = new RainbowPagerAdapter(getSupportFragmentManager(), TABS_COUNT);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initTabs() {
        RainbowTabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setColorForTabs(getColorsForTabs());
        tabLayout.setTitleColor(Color.WHITE);
        tabLayout.setIndicator(true);
        tabLayout.setIndicatorPosition(IndicatorPosition.TOP);
        tabLayout.setDistributeEvenly(false);
        tabLayout.setTabMinByMax(false);
        tabLayout.setTabViewPadding(8);
        tabLayout.setTabViewTextSize(17);
        tabLayout.setTypeFace(ResourcesCompat.getFont(this, R.font.roboto_regular));
        tabLayout.setViewPager(viewPager);
    }

    private int[] getColorsForTabs() {
        int[] colors = new int[TABS_COUNT];
        colors[0] = getResources().getColor(R.color.red);
        colors[1] = getResources().getColor(R.color.orange);
        colors[2] = getResources().getColor(R.color.yellow);
        colors[3] = getResources().getColor(R.color.green);
        colors[4] = getResources().getColor(R.color.blue);
        colors[5] = getResources().getColor(R.color.indigo);
        colors[6] = getResources().getColor(R.color.violet);
        return colors;
    }
}
