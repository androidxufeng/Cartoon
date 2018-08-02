package com.tplink.cartoon.ui.widget;

import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * @author xu
 */
public class NoScrollStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    private boolean isScrollEnabled = true;

    public NoScrollStaggeredGridLayoutManager(int num, int Orientation) {
        super(num, Orientation);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
