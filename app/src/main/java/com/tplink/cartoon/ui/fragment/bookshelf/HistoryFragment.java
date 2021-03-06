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
import com.tplink.cartoon.data.bean.HomeTitle;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.HistoryAdapter;
import com.tplink.cartoon.ui.presenter.HistoryPresenter;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;
import com.tplink.cartoon.ui.view.ICollectionView;
import com.tplink.cartoon.ui.widget.DividerGridItemDecoration;
import com.tplink.cartoon.ui.widget.ElasticScrollView;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class HistoryFragment extends BaseBookShelfFragment<HistoryPresenter> implements
        ICollectionView<List<Comic>>, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    @BindView(R.id.ev_scrollview)
    ElasticScrollView mScrollView;
    @BindView(R.id.rl_empty_view)
    RelativeLayout mEmptyView;

    private HistoryAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new HistoryPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    public void onEditList(boolean isEdit) {
        if (mAdapter.isEditing() != isEdit) {
            mPresenter.clearSelect();
            mAdapter.setEditing(isEdit);
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
        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(mActivity, 1);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mAdapter = new HistoryAdapter(mActivity, R.layout.item_history, R.layout.item_history_title, R.layout.item_loading);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mScrollView.setListener(new ElasticScrollView.OnScrollListener() {
            @Override
            public void onScrollToBottom() {
                mPresenter.loadMoreData();
            }
        });
    }

    //切换到该fragment做的操作
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 不在最前端界面显示
        if (!hidden) {
            mPresenter.getHistoryList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getHistoryList();
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
    public void onItemClick(RecyclerView parent, View view, int position) {
        if (mAdapter.getItems(position) instanceof HomeTitle) {
        } else if (!mAdapter.isEditing()) {
            Comic comic = mAdapter.getItems(position);
            IntentUtil.toComicChapter(mActivity, comic.getCurrentChapter(), comic);
        } else {
            mPresenter.updateToSelected(position);
        }
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

}
