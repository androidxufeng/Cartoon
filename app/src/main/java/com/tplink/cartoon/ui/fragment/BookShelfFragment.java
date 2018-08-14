/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-14, xufeng, Create file
 */
package com.tplink.cartoon.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.adapter.BookShelfAdapter;
import com.tplink.cartoon.ui.presenter.BookShelfPresenter;
import com.tplink.cartoon.ui.source.MainDataSource;
import com.tplink.cartoon.ui.view.IBookShelfView;
import com.tplink.cartoon.ui.widget.DividerGridItemDecoration;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

import butterknife.BindView;

public class BookShelfFragment extends BaseFragment<BookShelfPresenter> implements IBookShelfView<List<Comic>> {

    @BindView(R.id.rv_bookshelf)
    RecyclerView mRecyclerView;

    private BookShelfAdapter mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showToast(String t) {
        Toast.makeText(mActivity, t, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorView(String throwable) {
        showToast(throwable);
    }

    @Override
    public void fillData(List<Comic> data) {
        if (data != null && !data.isEmpty()) {
            mAdapter.updateWithClear(data);
            mRecyclerView.smoothScrollToPosition(3);
        } else {
            showToast("未取到数据");
        }
    }

    @Override
    public void getDataFinish() {

    }

    @Override
    protected void initPresenter() {
        mPresenter = new BookShelfPresenter(new MainDataSource(), this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mAdapter = new BookShelfAdapter(mActivity, R.layout.item_bookshelf);

        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);

        ImageView foot = new ImageView(mActivity);
        foot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        foot.setImageResource(R.drawable.no_more);
        foot.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mHeaderAndFooterWrapper.addFootView(foot);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mPresenter.loadData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }
}
