package com.popkovanton.rainbowtablayoutlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.popkovanton.rainbowtablayoutlibrary.colorizer.ITabColorizer;
import com.popkovanton.rainbowtablayoutlibrary.utils.ViewUtils;

import androidx.annotation.Dimension;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class RainbowTabLayout extends HorizontalScrollView {

    private static final int TITLE_OFFSET_DIPS = 24;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private RainbowTabStrip mTabStrip;

    private boolean distributeEvenly;
    private boolean tabIndicator;
    private boolean tabLine;
    private boolean isDrawSeparator;
    private boolean isTextColorBlend;
    private TabIndicatorPosition tabIndicatorPosition;
    private TabLinePosition tabLinePosition;
    private float tabViewPadding;
    private float tabViewTextSize;
    private Typeface typeFace;
    private int textSelectedColor;

    public RainbowTabLayout(Context context) {
        this(context, null);
    }

    public RainbowTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RainbowTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.RainbowTabLayout, 0, 0);
        try {
            distributeEvenly = a.getBoolean(R.styleable.RainbowTabLayout_rtl_distributeEvenly, false);
            tabIndicator = a.getBoolean(R.styleable.RainbowTabLayout_rtl_tabIndicator, true);
            tabIndicatorPosition = TabIndicatorPosition.values()[a.getInt(R.styleable.RainbowTabLayout_rtl_tabIndicatorPosition, 1)];
            isTextColorBlend = a.getBoolean(R.styleable.RainbowTabLayout_rtl_titleBlend, false);
            isDrawSeparator = a.getBoolean(R.styleable.RainbowTabLayout_rtl_tabSeparator, false);
            tabLine = a.getBoolean(R.styleable.RainbowTabLayout_rtl_tabLine, false);
            tabLinePosition = TabLinePosition.values()[a.getInt(R.styleable.RainbowTabLayout_rtl_tabLinePosition, 0)];
            tabViewPadding = a.getDimension(R.styleable.RainbowTabLayout_rtl_tabViewPadding, ViewUtils.dpToPx(getContext(), 20));
            tabViewTextSize = a.getDimension(R.styleable.RainbowTabLayout_rtl_tabViewTextSize, ViewUtils.spToPx(getContext(), 14));
            textSelectedColor = a.getColor(R.styleable.RainbowTabLayout_rtl_titleColor, Color.BLACK);
            if (a.hasValue(R.styleable.RainbowTabLayout_rtl_fontFamily)) {
                int fontId = a.getResourceId(R.styleable.RainbowTabLayout_rtl_fontFamily, -1);
                typeFace = ResourcesCompat.getFont(context, fontId);
            }
        } finally {
            a.recycle();
        }

        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final float density = dm.density;

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * density);

        mTabStrip = new RainbowTabStrip(context);

        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { //todo create tabLayout mode
        // If we have a MeasureSpec which allows us to decide our height, try and use the default
        // height
        final int idealHeight = Math.round(ViewUtils.dpToPx(getContext(), getDefaultHeight()));
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST:
                if (getChildCount() == 1 && MeasureSpec.getSize(heightMeasureSpec) >= idealHeight) {
                    getChildAt(0).setMinimumHeight(idealHeight);
                }
                break;
            case MeasureSpec.UNSPECIFIED:
                heightMeasureSpec =
                        MeasureSpec.makeMeasureSpec(
                                idealHeight + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
                break;
            default:
                break;
        }

        // Now super measure itself using the (possibly) modified height spec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Dimension(unit = Dimension.DP)
    private int getDefaultHeight() {
        return 0;
    }

    /**
     * Set the custom {@link ITabColorizer} to be used.
     * If you only require simple customisation then you can use
     * {@link #setIndicatorColors(int...)}
     * {@link #setBackgroundTabColors(int...)}
     * {@link #setSeparatorColors(int...)}
     * {@link #setTextUnselectedColors(int...)}
     * to achieve similar effects.
     */
    public void setCustomTabColorizer(ITabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Set the padding of tab
     */
    public void setTabViewPadding(float tabViewPadding) {
        this.tabViewPadding = ViewUtils.dpToPx(getContext(), tabViewPadding);
    }

    /**
     * Set the size of tab title in SP
     */
    public void setTabViewTextSize(float tabViewTextSize) {
        this.tabViewTextSize = ViewUtils.spToPx(getContext(), tabViewTextSize);
    }

    /**
     * Init tab indicator
     */
    public void setTabIndicator(boolean tabIndicator) {
        this.tabIndicator = tabIndicator;
    }

    /**
     * Setting indicator position for tabs
     * {@link TabIndicatorPosition}
     */
    public void setTabIndicatorPosition(TabIndicatorPosition tabIndicatorPosition) {
        this.tabIndicatorPosition = tabIndicatorPosition;
    }

    /**
     * Init drawing separator line between tabs
     */
    public void setDrawSeparator(boolean drawSeparator) {
        isDrawSeparator = drawSeparator;
    }

    /**
     * Init blend next and previous text color
     */
    public void setTextColorBlend(boolean isTextColorBlend) {
        this.isTextColorBlend = isTextColorBlend;
    }

    /**
     * Init static line
     */
    public void setTabLine(boolean tabLine) {
        this.tabLine = tabLine;
    }

    /**
     * Setting tabLine position
     * {@link TabLinePosition}
     */
    public void setTabLinePosition(TabLinePosition tabLinePosition) {
        this.tabLinePosition = tabLinePosition;
    }

    /**
     * Set the same weight for tab
     */
    public void setDistributeEvenly(boolean distributeEvenly) {
        this.distributeEvenly = distributeEvenly;
    }

    /**
     * Set the custom font for tabs title
     */
    public void setTypeFace(Typeface typeFace) {
        this.typeFace = typeFace;
    }

    /**
     * Set the color of title text
     */
    public void setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
    }

    /**
     * Set the color to be used for tabs line
     * Providing one color will mean that all lines are colored with the same color.
     */
    public void setTabLineColors(int... colors) {
        mTabStrip.setTabLineColors(colors);
    }

    /**
     * Set the color to be used for unselected tabs title text
     * Providing one color will mean that all title text are colored with the same color.
     */
    public void setTextUnselectedColors(int... colors) {
        mTabStrip.setTextUnselectedColors(colors);
    }

    /**
     * Set the color to be used for separator between tabs
     * Providing one color will mean that all separators are colored with the same color.
     */
    public void setSeparatorColors(int... colors) {
        mTabStrip.setSeparatorColors(colors);
    }

    /**
     * Sets the colors to be used for background of the tabs.
     * Providing one color will mean that all tabs are colored with the same color.
     */
    public void setBackgroundTabColors(int... colors) {
        mTabStrip.setBackgroundTabColors(colors);
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setIndicatorColors(int... colors) {
        mTabStrip.setIndicatorColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link RainbowTabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(CharSequence title) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(title);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabViewTextSize);
        textView.setTypeface(typeFace);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                outValue, true);
        textView.setBackgroundResource(outValue.resourceId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(false);
        }

        int padding = (int) tabViewPadding;
        textView.setPadding(padding, 0, padding, 0);

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                View tabView = null;

                if (mTabViewLayoutId != 0) {
                    // If there is a custom tab view layout id set, try and inflate it
                    tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                            false);
                }

                if (tabView == null) {
                    tabView = createDefaultTabView(adapter.getPageTitle(i));
                }

                if (distributeEvenly) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    lp.width = 0;
                    lp.weight = 1;
                }

                tabView.setOnClickListener(tabClickListener);

                mTabStrip.addView(tabView);
            }

            mTabStrip.setTitleBlend(isTextColorBlend);
            mTabStrip.setDrawSeparator(isDrawSeparator);
            mTabStrip.setTabLine(tabLine, tabLinePosition);
            mTabStrip.setTabIndicator(tabIndicator, tabIndicatorPosition);
            mTabStrip.setTextSelectedColor(textSelectedColor);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }


    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }
}
