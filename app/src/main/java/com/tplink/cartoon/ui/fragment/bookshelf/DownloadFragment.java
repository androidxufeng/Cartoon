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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.DownloadAdapter;
import com.tplink.cartoon.ui.presenter.DownloadPresenter;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;
import com.tplink.cartoon.ui.view.ICollectionView;
import com.tplink.cartoon.ui.widget.DividerGridItemDecoration;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class DownloadFragment extends BaseBookShelfFragment<DownloadPresenter> implements
        ICollectionView<List<Comic>>, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    @BindView(R.id.rl_empty_view)
    RelativeLayout mEmptyView;
    private DownloadAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new DownloadPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_download;
    }

    @Override
    public void onEditList(boolean isEditing) {
        if (mAdapter != null && mAdapter.isEditing() != isEditing) {
            mPresenter.clearSelect();
            mAdapter.setEditing(isEditing);
        }
    }

    @Override
    public void onDelete() {
        mPresenter.showDeteleDialog();
    }

    @Override
    public void onSelect() {
        mPresenter.selectOrMoveAll();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(mActivity, 3);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mAdapter = new DownloadAdapter(mActivity, R.layout.item_download);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    //切换到该fragment做的操作
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 不在最前端界面显示
        if (!hidden) {
            mPresenter.getDownloadList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getDownloadList();
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
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {
        mAdapter.updateWithClear(new ArrayList<Comic>());
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateList(HashMap map) {
        mAdapter.setMap(map);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateListItem(HashMap map, int position) {
        mAdapter.setMap(map);
        mAdapter.notifyItemChanged(position, "isNotNull");
    }

    @Override
    public void addAll() {
        mHomeActivity.getEditBottom().addAll();
    }

    @Override
    public void removeAll() {
        mHomeActivity.getEditBottom().removeAll();
    }

    @Override
    public void quitEdit() {
        mHomeActivity.quitEdit();
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        if (mAdapter.isEditing()) {
            mPresenter.updateToSelected(position);
        } else {
            Comic comic = mAdapter.getItems(position);
            IntentUtil.toComicDetail(mActivity, comic.getId(), comic.getTitle());
        }
    }
}
