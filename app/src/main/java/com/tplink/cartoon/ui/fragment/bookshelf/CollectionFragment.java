/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-27, xufeng, Create file
 */
package com.tplink.cartoon.ui.fragment.bookshelf;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.CollectionAdapter;
import com.tplink.cartoon.ui.fragment.BaseFragment;
import com.tplink.cartoon.ui.presenter.CollectionPresenter;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;
import com.tplink.cartoon.ui.view.ICollectionView;
import com.tplink.cartoon.ui.widget.DividerGridItemDecoration;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.List;

import butterknife.BindView;

public class CollectionFragment extends BaseFragment<CollectionPresenter> implements
        ICollectionView<List<Comic>>, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    private CollectionAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new CollectionPresenter(new BookShelfDataSource(mActivity), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collection;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(mActivity, 3);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mAdapter = new CollectionAdapter(mActivity, R.layout.item_collection);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    //切换到该fragment做的操作
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 不在最前端界面显示
        if (!hidden) {
            mPresenter.loadCollectComic();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadCollectComic();
    }

    @Override
    public void showToast(String t) {
        Toast.makeText(mActivity, t, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorView(String throwable) {
        showToast("重新加载");
    }

    @Override
    public void fillData(List<Comic> data) {
        mAdapter.updateWithClear(data);
    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        Comic comic = mAdapter.getItems(position);
        IntentUtil.toComicDetail(mActivity, comic.getId(), comic.getTitle());
    }
}
