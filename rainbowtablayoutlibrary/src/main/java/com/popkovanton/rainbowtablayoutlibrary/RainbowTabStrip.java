package com.popkovanton.rainbowtablayoutlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RainbowTabStrip extends LinearLayout {

    private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 4;
    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 8;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;

    private static final int DEFAULT_DIVIDER_THICKNESS_DIPS = 1;
    private static final float DEFAULT_DIVIDER_HEIGHT = 0.5f;

    private boolean indicator;

    private IndicatorPosition mIndicatorPosition;
    private final int mTopBorderThickness;
    private final Paint mTopBorderPaint;

    private final int mSelectedIndicatorThickness;
    private final Paint mSelectedIndicatorPaint;

    private final int mDefaultBottomBorderColor;

    private final Paint mDividerPaint;
    private final float mDividerHeight;

    private int mSelectedPosition;
    private float mSelectionOffset;

    private int titleColor;

    private RainbowTabLayout.TabColorizer mCustomTabColorizer;
    private final SimpleTabColorizer mDefaultTabColorizer;


    RainbowTabStrip(Context context) {
        this(context, null);
    }

    RainbowTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        final float density = getResources().getDisplayMetrics().density;

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorForeground, outValue, true);
        final int themeForegroundColor = outValue.data;

        mDefaultBottomBorderColor = setColorAlpha(themeForegroundColor,
                DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);

        mDefaultTabColorizer = new SimpleTabColorizer();
        mDefaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);

        mTopBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
        mTopBorderPaint = new Paint();
        mTopBorderPaint.setColor(mDefaultBottomBorderColor);

        mSelectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        mSelectedIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mDividerHeight = DEFAULT_DIVIDER_HEIGHT;
        mDividerPaint = new Paint();
        mDividerPaint.setStrokeWidth((int) (DEFAULT_DIVIDER_THICKNESS_DIPS * density));
    }

    void setCustomTabColorizer(RainbowTabLayout.TabColorizer customTabColorizer) {
        mCustomTabColorizer = customTabColorizer;
        invalidate();
    }

    void setSelectedIndicatorColors(int... colors) {
        // Make sure that the custom colorizer is removed
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setIndicatorColors(colors);
        invalidate();
    }

    public void setIndicator(boolean indicator, IndicatorPosition mIndicatorPosition) {
        this.indicator = indicator;
        this.mIndicatorPosition = mIndicatorPosition;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDecoration(canvas);
    }

    private void drawDecoration(Canvas canvas) {
        final int height = getHeight();
        final int childCount = getChildCount();
        final RainbowTabLayout.TabColorizer tabColorizer = getTabColorizer();
        TextView textView = null;
        // Thick colored underline below the current selection
        if (childCount > 0) {
            View selectedTitle = getChildAt(mSelectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            int color = tabColorizer.getIndicatorColor(mSelectedPosition);
            int titleNextColor = titleColor;
            int titlePreviousColor = titleColor;
            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {
                int nextColor = tabColorizer.getIndicatorColor(mSelectedPosition + 1);

                titleNextColor = blendColors(titleColor, nextColor, mSelectionOffset);

                if (color != nextColor) {
                    color = blendColors(nextColor, color, mSelectionOffset);
                }
                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() +
                        (1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() +
                        (1.0f - mSelectionOffset) * right);
            }

            if (mSelectionOffset > 0f) {
                int previousColor = tabColorizer.getIndicatorColor(mSelectedPosition);
                titlePreviousColor = blendColors(previousColor, titleColor, mSelectionOffset);
            }
            drawRainbow(canvas, left, right, height, color);

            if (mSelectedPosition < (getChildCount() - 1)) {
                textView = (TextView) getChildAt(mSelectedPosition + 1);
                textView.setTextColor(titleNextColor);

            }
            textView = (TextView) getChildAt(mSelectedPosition);
            textView.setTextColor(titlePreviousColor);
        }

        for (int i = 0; i < getChildCount(); i++) {
            int color = tabColorizer.getIndicatorColor(i);
            View selectedTitle = getChildAt(i);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            mTopBorderPaint.setColor(color);

            if (indicator) {
                drawIndicator(canvas, left, right, height, mTopBorderThickness, mTopBorderPaint);
            }
            if (mSelectionOffset <= 0f) {
                textView = (TextView) getChildAt(i);
                if (mSelectedPosition == i) {
                    textView.setTextColor(titleColor);
                } else {
                    textView.setTextColor(color);
                }
            }
        }
    }

    private void drawIndicator(Canvas canvas, int left, int right, int height,
                               int indicatorHeight, Paint paint) {
        if(mIndicatorPosition == IndicatorPosition.TOP) {
            canvas.drawRect(left, 0, right, indicatorHeight, paint);
        } else {
            canvas.drawRect(left, height - indicatorHeight, right, height, paint);
        }
    }

    private void drawRainbow(Canvas canvas, int left, int right, int height,
                             int color) {
        mSelectedIndicatorPaint.setColor(color);
        Path path = new TabRect.Builder()
                .setLeft(left)
                .setRight(right)
                .setBottom(height)
                .create();
        canvas.drawPath(path, mSelectedIndicatorPaint);
    }

    private RainbowTabLayout.TabColorizer getTabColorizer() {
        return (mCustomTabColorizer != null) ? mCustomTabColorizer : mDefaultTabColorizer;
    }

    /**
     * Set the alpha value of the {@code color} to be the given {@code alpha} value.
     */
    private static int setColorAlpha(int color, byte alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
     *              0.0 will return {@code color2}.
     */
    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    private static class SimpleTabColorizer implements RainbowTabLayout.TabColorizer {
        private int[] mIndicatorColors;

        @Override
        public final int getIndicatorColor(int position) {
            return mIndicatorColors[position % mIndicatorColors.length];
        }


        void setIndicatorColors(int... colors) {
            mIndicatorColors = colors;
        }

    }
}
