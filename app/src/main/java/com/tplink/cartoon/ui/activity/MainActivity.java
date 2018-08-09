package com.tplink.cartoon.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.MainAdapter;
import com.tplink.cartoon.ui.presenter.MainPresenter;
import com.tplink.cartoon.ui.source.MainDataSource;
import com.tplink.cartoon.ui.view.IMainView;
import com.tplink.cartoon.ui.widget.DividerGridItemDecoration;
import com.tplink.cartoon.ui.widget.NoScrollStaggeredGridLayoutManager;
import com.tplink.cartoon.ui.widget.ZElasticRefreshScrollView;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainView, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    @BindView(R.id.sv_comic)
    ZElasticRefreshScrollView mScrollView;

    @BindView(R.id.rl_error_view)
    RelativeLayout mErrorView;

    @BindView(R.id.iv_error)
    ImageView mReload;

    @OnClick(R.id.iv_error)
    public void reloadData(View view){
        mErrorView.setVisibility(View.GONE);
        mPresenter.loadData();
    }
    private MainAdapter mAdapter;
    private int i = 3;

    @Override
    protected void initPresenter() {
        MainDataSource mainDataSource = new MainDataSource();
        mPresenter = new MainPresenter(mainDataSource, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        NoScrollStaggeredGridLayoutManager layoutManager = new NoScrollStaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(this, R.layout.item_image);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(this));
        mScrollView.setRefreshListener(new ZElasticRefreshScrollView.RefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadData();
            }

            @Override
            public void onRefreshFinish() {
            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMoreData(i);
                i++;
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.loadData();
    }

    @Override
    public void getDataFinish() {
        mScrollView.setRefreshing(false);
        if(mErrorView.isShown()){
            mErrorView.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showErrorView(Throwable throwable) {
        mScrollView.setRefreshing(false);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void hideRefresh() {

    }

    @Override
    public void fillData(List data) {
        if (data != null && data.size() != 0) {
            mAdapter.updateWithClear(data);
        } else {
            showToast("未取到数据");
        }
    }

    @Override
    public void appendMoreDataToView(List data) {
        if (data != null && data.size() != 0) {
            mAdapter.update(data);
        } else {
            showToast("未取到数据");
        }
    }

    @Override
    public void hasNoMoreData() {

    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        Comic comic = mAdapter.getItems(position);
        IntentUtil.ToComicDetail(this, comic.getId());
    }
}
