package com.tplink.cartoon.ui.widget;
/*
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-9, xufeng, Create file
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.common.Constants;

public class SwitchRelativeLayout extends RelativeLayout {
    private static final int ANIMATION_TIME = 200;
    private RelativeLayout mLayout;
    private RelativeLayout mSuccBg;
    private ImageView mSucc;
    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public SwitchRelativeLayout(Context context) {
        this(context, null);
    }

    public SwitchRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLayout = (RelativeLayout) getChildAt(0);
        mSuccBg = (RelativeLayout) getChildAt(1);
        mSucc = (ImageView) mSuccBg.getChildAt(0);
    }

    public void setVisibility(int visibility, int mDirect) {
        int id = 0;
        if (mDirect == Constants.LEFT_TO_RIGHT) {
            id = R.drawable.succ_left_to_right;
        } else {
            id = R.drawable.succ_right_to_left;
        }
        switch (visibility) {
            case View.GONE:
                mLayout.setVisibility(View.GONE);
                AnimationSet animationSet1 = new AnimationSet(true);
                AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
                animationSet1.addAnimation(alphaAnimation1);
                animationSet1.setDuration(ANIMATION_TIME);
                mLayout.startAnimation(animationSet1);
                AlphaAnimation alphaAnimation2 = new AlphaAnimation(1, 0);
                alphaAnimation2.setDuration(2000);
                mSucc.setImageResource(id);
                alphaAnimation2.setAnimationListener(new Animation.AnimationListener() {
                    //监听动画开始时弹出TOAST
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mSuccBg.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }                   //动画结束时开启第二个

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mSuccBg.setVisibility(View.GONE);
                    }
                });
                mSuccBg.startAnimation(alphaAnimation2);
                isShow = false;
                break;
            case View.VISIBLE:
                mLayout.setVisibility(View.VISIBLE);
                AnimationSet animationSet3 = new AnimationSet(true);
                AlphaAnimation alphaAnimation3 = new AlphaAnimation(0, 1);
                animationSet3.addAnimation(alphaAnimation3);
                animationSet3.setDuration(ANIMATION_TIME);
                mLayout.startAnimation(animationSet3);
                isShow = true;
                break;
            case View.INVISIBLE:
                super.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void setVisibility(int visibility) {
        switch (visibility) {
            case View.GONE:
                mLayout.setVisibility(View.GONE);
                AnimationSet animationSet1 = new AnimationSet(true);
                AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
                animationSet1.addAnimation(alphaAnimation1);
                animationSet1.setDuration(ANIMATION_TIME);
                mLayout.startAnimation(animationSet1);
                isShow = false;
                break;
            case View.VISIBLE:
                mLayout.setVisibility(View.VISIBLE);
                AnimationSet animationSet3 = new AnimationSet(true);
                AlphaAnimation alphaAnimation3 = new AlphaAnimation(0, 1);
                animationSet3.addAnimation(alphaAnimation3);
                animationSet3.setDuration(ANIMATION_TIME);
                mLayout.startAnimation(animationSet3);
                isShow = true;
                break;
            case View.INVISIBLE:
                super.setVisibility(View.INVISIBLE);
                break;
        }
    }
}