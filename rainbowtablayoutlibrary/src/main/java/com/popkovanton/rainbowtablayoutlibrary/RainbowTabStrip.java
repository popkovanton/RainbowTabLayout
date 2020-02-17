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

import com.popkovanton.rainbowtablayoutlibrary.colorizer.DefaultTabColorizer;
import com.popkovanton.rainbowtablayoutlibrary.colorizer.ITabColorizer;

public class RainbowTabStrip extends LinearLayout {

    private static final int DEFAULT_TAB_LINE_THICKNESS_DIPS = 4;
    private static final byte DEFAULT_TAB_LINE_COLOR_ALPHA = 0x26;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 4;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;
    private static final int DEFAULT_SEPARATOR_COLOR = 0xFF000000;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final int DEFAULT_TITLE_UNSELECTED_COLOR = Color.BLACK;

    private static final int DEFAULT_SEPARATOR_THICKNESS_DIPS = 1;
    private static final float DEFAULT_SEPARATOR_HEIGHT = 0.5f;

    private boolean isDrawLine;
    private boolean isDrawIndicator;
    private boolean isDrawSeparator;
    private boolean isTitleBlend;

    private TabIndicatorPosition mTabIndicatorPosition;
    private TabLinePosition mTabLinePosition;
    private final int mTabLineThickness;
    private final Paint mTabLinePaint;

    private final int mSelectedIndicatorThickness;
    private final Paint mTabBackgroundPaint;
    private final Paint mSelectedIndicatorPaint;

    private final int mDefaultTabLineColor;

    private final Paint mSeparatorPaint;
    private final float mSeparatorHeight;

    private int mSelectedPosition;
    private float mSelectionOffset;

    private int textSelectedColor;

    private ITabColorizer mCustomTabColorizer;
    private final DefaultTabColorizer mDefaultTabColorizer;


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

        mDefaultTabLineColor = setColorAlpha(themeForegroundColor,
                DEFAULT_TAB_LINE_COLOR_ALPHA);

        mDefaultTabColorizer = new DefaultTabColorizer();
        mDefaultTabColorizer.setTitleUnselectedColors(DEFAULT_TITLE_UNSELECTED_COLOR);
        mDefaultTabColorizer.setBackgroundColors(DEFAULT_BACKGROUND_COLOR);
        mDefaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);
        mDefaultTabColorizer.setSeparatorColors(DEFAULT_SEPARATOR_COLOR);

        mTabLineThickness = (int) (DEFAULT_TAB_LINE_THICKNESS_DIPS * density);
        mTabLinePaint = new Paint();
        mTabLinePaint.setColor(mDefaultTabLineColor);

        mSelectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        mSelectedIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTabBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSeparatorHeight = DEFAULT_SEPARATOR_HEIGHT;
        mSeparatorPaint = new Paint();
        mSeparatorPaint.setStrokeWidth((int) (DEFAULT_SEPARATOR_THICKNESS_DIPS * density));
    }

    void setCustomTabColorizer(ITabColorizer customTabColorizer) {
        mCustomTabColorizer = customTabColorizer;
        invalidate();
    }

    void setSeparatorColors(int... colors) {
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setSeparatorColors(colors);
        invalidate();
    }

    void setIndicatorColors(int... colors) {
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setIndicatorColors(colors);
        invalidate();
    }

    void setBackgroundTabColors(int... colors) {
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setBackgroundColors(colors);
        invalidate();
    }

    void setTextUnselectedColors(int... colors) {
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setTitleUnselectedColors(colors);
        invalidate();
    }

    public void setTitleBlend(boolean titleBlend) {
        isTitleBlend = titleBlend;
    }

    public void setDrawSeparator(boolean drawSeparator) {
        isDrawSeparator = drawSeparator;
    }

    public void setTabLine(boolean isDrawLine, TabLinePosition mTabLinePosition) {
        this.isDrawLine = isDrawLine;
        this.mTabLinePosition = mTabLinePosition;
    }

    public void setTabIndicator(boolean isDrawIndicator, TabIndicatorPosition mTabIndicatorPosition) {
        this.isDrawIndicator = isDrawIndicator;
        this.mTabIndicatorPosition = mTabIndicatorPosition;
    }

    public void setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
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
        final ITabColorizer tabColorizer = getTabColorizer();
        TextView textView;

        for (int i = 0; i < getChildCount(); i++) {
            int color = tabColorizer.getIndicatorColor(i);
            int colorBack = tabColorizer.getBackgroundColor(i);
            View selectedTitle = getChildAt(i);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            mTabLinePaint.setColor(color);

            drawBackground(canvas, left, right, height, colorBack);

            if (isDrawLine) {
                drawTabLine(canvas, left, right, height, mTabLineThickness, mTabLinePaint);
            }
        }

        if (childCount > 0) {
            View selectedTitle = getChildAt(mSelectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            int color = tabColorizer.getIndicatorColor(mSelectedPosition);
            int titleNextColor = textSelectedColor;
            int titlePreviousColor = textSelectedColor;
            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1)) {
                int nextColor = tabColorizer.getIndicatorColor(mSelectedPosition + 1);

                titleNextColor = blendColors(textSelectedColor, nextColor, mSelectionOffset);

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
                titlePreviousColor = blendColors(previousColor, textSelectedColor, mSelectionOffset);
            }
            if (isDrawIndicator) {
                drawIndicator(canvas, left, right, height, color);
            }
            if (isTitleBlend) {
                if (mSelectedPosition < (getChildCount() - 1)) {
                    textView = (TextView) getChildAt(mSelectedPosition + 1);
                    textView.setTextColor(titleNextColor);

                }
                textView = (TextView) getChildAt(mSelectedPosition);
                textView.setTextColor(titlePreviousColor);
            }
        }

        for (int i = 0; i < getChildCount(); i++) {
            int color = tabColorizer.getIndicatorColor(i);
            if (mSelectionOffset <= 0f) {
                textView = (TextView) getChildAt(i);
                if (mSelectedPosition == i) {
                    textView.setTextColor(textSelectedColor);
                } else {
                    textView.setTextColor(isTitleBlend ? color : textSelectedColor);
                }
            }
        }
        // Vertical separators between the titles
        if (isDrawSeparator) {
            drawSeparator(canvas, height, childCount);
        }
    }

    private void drawSeparator(Canvas canvas, int height, int childCount) {
        final int dividerHeightPx = (int) (Math.min(Math.max(0f, mSeparatorHeight), 1f) * height);
        final ITabColorizer tabColorizer = getTabColorizer();

        // Vertical separators between the titles
        final int separatorTop = (height - dividerHeightPx) / 2;
        final int separatorBottom = separatorTop + dividerHeightPx;

        final boolean isLayoutRtl = Utils.isLayoutRtl(this);
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);
            int end = Utils.getEnd(child);
            int endMargin = Utils.getMarginEnd(child);
            int separatorX = isLayoutRtl ? end - endMargin : end + endMargin;
            mSeparatorPaint.setColor(tabColorizer.getSeparatorColor(i));
            canvas.drawLine(separatorX, separatorTop, separatorX, separatorBottom, mSeparatorPaint);
        }
    }

    private void drawTabLine(Canvas canvas, int left, int right, int height,
                             int indicatorHeight, Paint paint) {
        switch (mTabLinePosition) {
            case TOP:
                canvas.drawRect(left, 0, right, indicatorHeight, paint);
                break;
            case BOTTOM:
                canvas.drawRect(left, height - indicatorHeight, right, height, paint);
                break;

        }
    }

    private void drawIndicator(Canvas canvas, int left, int right, int height,
                               int color) {
        mSelectedIndicatorPaint.setColor(color);
        Path path = new TabRect.Builder()
                .setLeft(left)
                .setRight(right)
                .setBottom(height)
                .setIndicatorHeight(mSelectedIndicatorThickness)
                .setPosition(mTabIndicatorPosition)
                .create();
        canvas.drawPath(path, mSelectedIndicatorPaint);
    }

    private void drawBackground(Canvas canvas, int left, int right, int height,
                                int color) {
        mTabBackgroundPaint.setColor(color);
        Path path = new TabRect.Builder()
                .setLeft(left)
                .setRight(right)
                .setBottom(height)
                .create();
        canvas.drawPath(path, mTabBackgroundPaint);

    }

    private ITabColorizer getTabColorizer() {
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
}
