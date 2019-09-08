# RainbowTabLayout
[![](https://jitpack.io/v/popkovanton/RainbowTabLayout.svg)](https://jitpack.io/#popkovanton/RainbowTabLayout)
[![platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

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
        app:rtl_indicator="true"
        app:rtl_indicatorPosition="TOP"
        app:rtl_titleColor="@color/white"
        app:rtl_tabMinWidthByMax="false"
        app:rtl_distributeEvenly="false"
        app:rtl_fontFamily="@font/roboto_regular"
        app:rtl_tabViewPadding="8"
        app:rtl_tabViewTextSize="18"/>
```

### Java

```code
RainbowTabLayout rtb = findViewById(R.id.rainbowTabLayout);
rtb.setColorForTabs(getColorsForTabs());
rtb.setTitleColor(Color.WHITE);
rtb.setIndicator(true);
rtb.setIndicatorPosition(IndicatorPosition.TOP);
rtb.setDistributeEvenly(false);
rtb.setTabMinByMax(false);
rtb.setTabViewPadding(8);
rtb.setTabViewTextSize(17);
rtb.setTypeFace(ResourcesCompat.getFont(this, R.font.roboto_regular));
rtb.setViewPager(viewPager);
```
