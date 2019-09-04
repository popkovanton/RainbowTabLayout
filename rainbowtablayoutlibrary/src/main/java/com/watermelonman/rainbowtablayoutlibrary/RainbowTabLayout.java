package com.watermelonman.rainbowtablayoutlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class RainbowTabLayout extends HorizontalScrollView {

    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 24;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private int[] selectedIndicatorColors = null;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final RainbowTabStrip mTabStrip;

    private boolean distributeEvenly;
    private boolean tabMinWidthByMax;
    private boolean indicator;
    private int tabViewPadding;
    private int tabViewTextSize;
    private Typeface typeFace;
    private ArrayList<Integer> listOfViewSize;
    private ArrayList<View> listOfView;

    public RainbowTabLayout(Context context) {
        this(context, null);
    }

    public RainbowTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainbowTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RainbowTabLayout, 0, 0);
        try {
            distributeEvenly = a.getBoolean(R.styleable.RainbowTabLayout_rtl_distributeEvenly, false);
            tabMinWidthByMax = a.getBoolean(R.styleable.RainbowTabLayout_rtl_tabMinWidthByMax, false);
            indicator = a.getBoolean(R.styleable.RainbowTabLayout_rtl_indicator, true);
            tabViewPadding = a.getInt(R.styleable.RainbowTabLayout_rtl_tabViewPadding, 8);
            tabViewTextSize = a.getInt(R.styleable.RainbowTabLayout_rtl_tabViewTextSize, 17);
            if (a.hasValue(R.styleable.RainbowTabLayout_rtl_fontFamily)) {
                int fontId = a.getResourceId(R.styleable.RainbowTabLayout_rtl_fontFamily, -1);
                typeFace = ResourcesCompat.getFont(context, fontId);
            }
        } finally {
            a.recycle();
        }
        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final float density = dm.density;

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * density);

        mTabStrip = new RainbowTabStrip(context);

        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void setColorForTabs(int... selectedIndicatorColor) {
        this.selectedIndicatorColors = selectedIndicatorColor;
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     * <p>
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Set the padding of tab
     */
    public void setTabViewPadding(int tabViewPadding) {
        this.tabViewPadding = tabViewPadding;
    }

    /**
     * Set the size of tab title in SP
     */
    public void setTabViewTextSize(int tabViewTextSize) {
        this.tabViewTextSize = tabViewTextSize;
    }

    /**
     * Init tab indicator
     */
    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }

    /**
     * Set the same weight for tab
     */
    public void setDistributeEvenly(boolean distributeEvenly) {
        this.distributeEvenly = distributeEvenly;
    }

    /**
     * Set the min width for tab, like max tab
     */
    public void setTabMinByMax(boolean tabMinWidthByMax) {
        this.tabMinWidthByMax = tabMinWidthByMax;
    }

    /**
     * Set the custom font for tabs title
     */
    public void setTypeFace(Typeface typeFace) {
        this.typeFace = typeFace;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
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
        listOfViewSize = new ArrayList<>();
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
            if (tabMinWidthByMax) {
                setMinTabLikeMax();
            }
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
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, tabViewTextSize);
        textView.setTypeface(typeFace);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(false);
        }

        int padding = (int) (tabViewPadding * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        listOfViewSize.add(getTextWidth(textView, String.valueOf(title)));
        return textView;
    }

    private int getTextWidth(TextView textView, String text) {
        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int df = tabViewPadding * 2;
        return bounds.width() + df;
    }

    private void setMinTabLikeMax() {
        int max = 0;
        if (listOfViewSize.size() > 0) {
            for (int number : listOfViewSize) {
                if (number > max) {
                    max = number;
                }
            }
        }
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View view = listOfView.get(i);
            view.setMinimumWidth(max);
        }
        invalidate();
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();
        listOfView = new ArrayList<>();
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
                listOfView.add(tabView);
            }

            if (selectedIndicatorColors != null) {
                setSelectedIndicatorColors(selectedIndicatorColors);
            }

            mTabStrip.setIndicator(indicator);
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
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
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
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
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
