# RainbowTabLayout
[![](https://jitpack.io/v/popkovanton/RainbowTabLayout.svg)](https://jitpack.io/#popkovanton/RainbowTabLayout)
[![platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

**This is not the final version! Key functions subject to change.**

## Demo app
[<img src="media/rainbow_tabs_demo.gif" width="250" />]()

## Download

```groovy
repositories {
        maven { url "https://jitpack.io" }
}

dependencies {
      implementation 'com.github.popkovanton:RainbowTabLayout:$latest_version'
}
```

## Usage


### XML

```XML
<com.popkovanton.rainbowtablayoutlibrary.RainbowTabLayout
        android:id="@+id/rainbowTabLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:rtl_tabLine="true"
        app:rtl_tabLinePosition="BOTTOM"
        app:rtl_indicator="true"
        app:rtl_indicatorPosition="ALL"
        app:rtl_tabSeparator="true"
        app:rtl_titleBlend="false"      
        app:rtl_titleColor="@color/white"      
        app:rtl_distributeEvenly="false"
        app:rtl_fontFamily="@font/roboto_regular"
        app:rtl_tabViewPadding="8dp"
        app:rtl_tabViewTextSize="18sp"/>
```

### Java

```code
RainbowTabLayout rtb = findViewById(R.id.rainbowTabLayout);
rtb.setTabLineColors(getColorsForLines());
rtb.setSeparatorColors(getColorsForSeparator());
rtb.setBackgroundTabColors(getColorsForTabsBack());
rtb.setIndicatorColors(getColorsForIndicator());
rtb.setTextSelectedColor(Color.BLACK);
rtb.setTabViewPadding(getResources().getDimension(R.dimens.padding));
rtb.setTabViewTextSize(getResources().getDimension(R.dimens.text_size));
rtb.setTabIndicator(true);
rtb.setTabIndicatorPosition(TabIndicatorPosition.ALL);
rtb.setDrawSeparator(true);
rtb.setTextColorBlend(true);
rtb.setTabLine(true);
rtb.setTabLinePosition(TabLinePosition.BOTTOM);
rtb.setViewPager(viewPager);
```
