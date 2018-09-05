/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;

public class CustomTab extends LinearLayout {
    private LinearLayout mTab;
    private RelativeLayout mTabs[] = new RelativeLayout[4];

    public CustomTab(Context context) {
        this(context, null);
    }

    public CustomTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        mTab = (LinearLayout) getChildAt(1);
        for (int i = 0; i < 4; i++) {
            mTabs[i] = (RelativeLayout) mTab.getChildAt(i);
        }
        super.onFinishInflate();
    }

    public void setCurrentPosition(int postion) {
        for (int i = 0; i < 4; i++) {
            if (i != postion) {
                TextView mTextView = (TextView) mTabs[i].getChildAt(0);
                mTextView.setTextAppearance(R.style.colorTextColorLight);
                ImageView mBottom = (ImageView) mTabs[i].getChildAt(1);
                mBottom.setVisibility(View.GONE);
            } else {
                TextView mTextView = (TextView) mTabs[i].getChildAt(0);
                mTextView.setTextAppearance(R.style.colorTextBlack);
                ImageView mBottom = (ImageView) mTabs[i].getChildAt(1);
                mBottom.setVisibility(View.VISIBLE);
            }
        }
    }
}
