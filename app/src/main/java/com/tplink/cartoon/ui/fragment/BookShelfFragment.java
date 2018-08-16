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
import android.widget.Toast;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.BookShelfAdapter;
import com.tplink.cartoon.ui.presenter.BookShelfPresenter;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;
import com.tplink.cartoon.ui.view.IBookShelfView;
import com.tplink.cartoon.ui.widget.DividerGridItemDecoration;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.List;

import butterknife.BindView;

public class BookShelfFragment extends BaseFragment<BookShelfPresenter> implements IBookShelfView<List<Comic>>,
        BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.rv_bookshelf)
    RecyclerView mRecyclerView;

    private BookShelfAdapter mAdapter;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.loadData();
        }
    }

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
        }
    }

    @Override
    public void getDataFinish() {

    }

    @Override
    protected void initPresenter() {
        mPresenter = new BookShelfPresenter(new BookShelfDataSource(mActivity), this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mAdapter = new BookShelfAdapter(mActivity, R.layout.item_bookshelf);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        Comic comic = mAdapter.getItems(position);
        IntentUtil.toComicDetail(mActivity, comic.getId(), comic.getTitle());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadData();
    }
}
