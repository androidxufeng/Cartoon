/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-14, xufeng, Create file
 */
package com.tplink.cartoon.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.presenter.HomePresenter;
import com.tplink.cartoon.ui.source.MainDataSource;
import com.tplink.cartoon.ui.view.IHomeView;

import java.util.List;

public class BookShelfFragment extends BaseFragment<HomePresenter> implements IHomeView<Comic> {
    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter(new MainDataSource(), this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    public void showToast(String t) {

    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void fillData(List<Comic> data) {

    }

    @Override
    public void appendMoreDataToView(List<Comic> data) {

    }

    @Override
    public void hasNoMoreData() {

    }

    @Override
    public void showErrorView(String throwable) {

    }
}
