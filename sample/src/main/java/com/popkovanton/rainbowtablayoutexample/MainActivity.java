package com.popkovanton.rainbowtablayoutexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.popkovanton.rainbowtablayoutlibrary.TabLinePosition;
import com.popkovanton.rainbowtablayoutlibrary.RainbowTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        test();
    }

    private void initViewPager() {
        RainbowPagerAdapter pagerAdapter = new RainbowPagerAdapter(getSupportFragmentManager(), TABS_COUNT);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initTabs() {
        RainbowTabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setIndicatorTabColors(Color.WHITE);
        tabLayout.setTitleSelectedColor(Color.WHITE);
        tabLayout.setBackgroundTabColors(getColorsForTabs());
        tabLayout.setTabLine(false);
        tabLayout.setTabIndicator(true);
        tabLayout.setTabLinePosition(TabLinePosition.TOP);
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

    private void test() {
        List<Integer> number = new ArrayList<>();

        number.add(11);
        number.add(45);
        number.add(12);
        number.add(32);
        number.add(36);
        boolean even = false;
        for (Iterator i = number.iterator(); i.hasNext(); even = !even) {
            i.next();
            if (even) {
                i.remove();
            }
        }

        for(int num: number){
            Log.i("test_method", "number int list - " + num);
        }
    }

    private boolean isEven(int next) {
        return next % 2 == 0;
    }
}
