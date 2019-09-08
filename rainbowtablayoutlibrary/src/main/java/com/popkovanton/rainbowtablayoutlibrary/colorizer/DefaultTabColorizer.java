package com.popkovanton.rainbowtablayoutlibrary.colorizer;

public class DefaultTabColorizer implements ITabColorizer {

    private int[] mIndicatorColors;
    private int[] mSeparatorColors;

    @Override
    public final int getIndicatorColor(int position) {
        return mIndicatorColors[position % mIndicatorColors.length];
    }

    @Override
    public int getSeparatorColor(int position) {
        return mSeparatorColors[position % mSeparatorColors.length];
    }

    public void setIndicatorColors(int... colors) {
        mIndicatorColors = colors;
    }

    public void setSeparatorColors(int... colors) {
        mSeparatorColors = colors;
    }

}
