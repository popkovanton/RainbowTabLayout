package com.popkovanton.rainbowtablayoutlibrary.colorizer;

/**
 * Allows complete control over the colors drawn in the tab layout. Set with
 */
public interface ITabColorizer {

    int getTabLineColor(int position);

    int getTextUnselectedColor(int position);

    int getBackgroundColor(int position);
    /**
     * @return return the color of the tabLine used when {@code position} is selected.
     */
    int getIndicatorColor(int position);
    /**
     * @return return the color of the separator drawn to the right of {@code position}.
     */
    int getSeparatorColor(int position);
}
