/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-13, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tplink.cartoon.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseFragmentActivity extends RxAppCompatActivity {
    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;
    protected List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initStatusBar();
        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        ButterKnife.bind(this);
        initView();
    }

    protected abstract void initView();

    protected void handleIntent(Intent intent) {

    }

    private void initStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    protected int getContentViewId() {
        return R.layout.activity_base;
    }

    public void selectTab(int num) {
        fragmentTransaction = fragmentManager.beginTransaction();
        try {
            for (int i = 0; i < fragments.size(); i++) {
                fragmentTransaction.hide(fragments.get(i));
            }
            fragmentTransaction.show(fragments.get(num));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
