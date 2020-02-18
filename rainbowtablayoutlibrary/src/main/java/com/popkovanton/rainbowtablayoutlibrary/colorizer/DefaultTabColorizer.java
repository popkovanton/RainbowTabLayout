package com.popkovanton.rainbowtablayoutlibrary.colorizer;

public class DefaultTabColorizer implements ITabColorizer {

    private int[] mTabLineColors;
    private int[] mTextUnselectedColors;
    private int[] mBackgroundColors;
    private int[] mIndicatorColors;
    private int[] mSeparatorColors;

    @Override
    public int getTabLineColor(int position) {
        return  mTabLineColors[position % mTabLineColors.length];
    }

    @Override
    public int getTextUnselectedColor(int position) {
        return mTextUnselectedColors[position % mTextUnselectedColors.length];
    }

    @Override
    public int getBackgroundColor(int position) {
        return mBackgroundColors[position % mBackgroundColors.length];
    }

    @Override
    public final int getIndicatorColor(int position) {
        return mIndicatorColors[position % mIndicatorColors.length];
    }

    @Override
    public int getSeparatorColor(int position) {
        return mSeparatorColors[position % mSeparatorColors.length];
    }

    public void setTextUnselectedColors(int... colors) {
        this.mTextUnselectedColors = colors;
    }

    public void setBackgroundColors(int... colors) {
        this.mBackgroundColors = colors;
    }

    public void setIndicatorColors(int... colors) {
        mIndicatorColors = colors;
    }

    public void setSeparatorColors(int... colors) {
        mSeparatorColors = colors;
    }

    public void setTabLineColors(int... colors) {
        this.mTabLineColors = colors;
    }
}
