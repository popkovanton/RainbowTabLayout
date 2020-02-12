package com.popkovanton.rainbowtablayoutlibrary.colorizer;

public class DefaultTabColorizer implements ITabColorizer {

    private int[] mTitleUnselectedColors;
    private int[] mBackgroundColors;
    private int[] mIndicatorColors;
    private int[] mSeparatorColors;

    @Override
    public int getTitleUnselectedColor(int position) {
        return mTitleUnselectedColors[position % mTitleUnselectedColors.length];
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

    public void setTitleUnselectedColors(int... colors) {
        this.mTitleUnselectedColors = colors;
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

}
