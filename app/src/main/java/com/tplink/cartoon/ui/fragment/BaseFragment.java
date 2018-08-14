/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-13, xufeng, Create file
 */
package com.tplink.cartoon.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tplink.cartoon.ui.activity.BaseFragmentActivity;
import com.tplink.cartoon.ui.presenter.BasePresenter;

import butterknife.ButterKnife;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

    protected BaseFragmentActivity mActivity;
    protected P mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseFragmentActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        initPresenter();
        checkPresenterIsNull();
        initView(view, savedInstanceState);
        return view;
    }

    protected abstract void initPresenter();

    protected abstract void initView(View view, Bundle savedInstanceState);

    //获取布局文件ID
    protected abstract int getLayoutId();

    //获取宿主Activity
    protected BaseFragmentActivity getHoldingActivity() {
        return mActivity;
    }

    private void checkPresenterIsNull() {
        if (mPresenter == null) {
            throw new IllegalStateException("please init mPresenter in initPresenter() method ");
        }
    }

}
