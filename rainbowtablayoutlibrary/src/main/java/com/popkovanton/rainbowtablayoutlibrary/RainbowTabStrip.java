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
import com.popkovanton.rainbowtablayoutlibrary.utils.Utils;
import com.popkovanton.rainbowtablayoutlibrary.utils.ViewUtils;

import static androidx.appcompat.widget.ViewUtils.isLayoutRtl;
import static com.popkovanton.rainbowtablayoutlibrary.utils.Utils.getEnd;
import static com.popkovanton.rainbowtablayoutlibrary.utils.Utils.getMarginEnd;
import static com.popkovanton.rainbowtablayoutlibrary.utils.ViewUtils.blendColors;

public class RainbowTabStrip extends LinearLayout {

    private static final int DEFAULT_TAB_LINE_THICKNESS_DIPS = 4;
    private static final int DEFAULT_TAB_LINE_COLOR = 0xFF000000;
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

        mDefaultTabColorizer = new DefaultTabColorizer();
        mDefaultTabColorizer.setTextUnselectedColors(DEFAULT_TITLE_UNSELECTED_COLOR);
        mDefaultTabColorizer.setBackgroundColors(DEFAULT_BACKGROUND_COLOR);
        mDefaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);
        mDefaultTabColorizer.setSeparatorColors(DEFAULT_SEPARATOR_COLOR);
        mDefaultTabColorizer.setTabLineColors(DEFAULT_TAB_LINE_COLOR);

        mTabLineThickness = (int) (DEFAULT_TAB_LINE_THICKNESS_DIPS * density);
        mTabLinePaint = new Paint();

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
        mDefaultTabColorizer.setTextUnselectedColors(colors);
        invalidate();
    }

    public void setTabLineColors(int[] colors) {
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setTabLineColors(colors);
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

        for (int i = 0; i < childCount; i++) {
            int colorBack = tabColorizer.getBackgroundColor(i);
            int colorLine = tabColorizer.getTabLineColor(i);
            View selectedTitle = getChildAt(i);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();

            drawBackground(canvas, left, right, height, colorBack);

            if (isDrawLine) {
                drawTabLine(canvas, left, right, height, colorLine);
            }
        }

        if (childCount > 0) {
            View selectedTitle = getChildAt(mSelectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            int colorIndicator = tabColorizer.getIndicatorColor(mSelectedPosition);
            int titleNextColor = textSelectedColor;
            int titlePreviousColor = textSelectedColor;
            if (mSelectionOffset > 0f && mSelectedPosition < (childCount - 1)) {

                titleNextColor = blendColors(
                        textSelectedColor,
                        tabColorizer.getIndicatorColor(mSelectedPosition + 1),
                        mSelectionOffset);

                colorIndicator = blendColors(
                        tabColorizer.getIndicatorColor(mSelectedPosition + 1),
                        colorIndicator,
                        mSelectionOffset);

                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() +
                        (1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() +
                        (1.0f - mSelectionOffset) * right);
            }

            if (isDrawIndicator) {
                drawIndicator(canvas, left, right, height, colorIndicator);
            }

            if (mSelectionOffset > 0f) {
                titlePreviousColor = blendColors(tabColorizer.getIndicatorColor(
                        mSelectedPosition),
                        textSelectedColor,
                        mSelectionOffset);
            }

            if (isTitleBlend) {
                if (mSelectedPosition < (childCount - 1)) {
                    textView = (TextView) getChildAt(mSelectedPosition + 1);
                    textView.setTextColor(titleNextColor);

                }
                textView = (TextView) getChildAt(mSelectedPosition);
                textView.setTextColor(titlePreviousColor);
            }
        }

        for (int i = 0; i < childCount; i++) {
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

        final boolean isLayoutRtl = isLayoutRtl(this);
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);
            int end = getEnd(child);
            int endMargin = getMarginEnd(child);
            int separatorX = isLayoutRtl ? end - endMargin : end + endMargin;
            mSeparatorPaint.setColor(tabColorizer.getSeparatorColor(i));
            canvas.drawLine(separatorX, separatorTop, separatorX, separatorBottom, mSeparatorPaint);
        }
    }

    private void drawTabLine(Canvas canvas, int left, int right, int height, int color) {
        mTabLinePaint.setColor(color);
        switch (mTabLinePosition) {
            case TOP:
                canvas.drawRect(left, 0, right, mTabLineThickness, mTabLinePaint);
                break;
            case BOTTOM:
                canvas.drawRect(left, height - mTabLineThickness, right, height, mTabLinePaint);
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
}
