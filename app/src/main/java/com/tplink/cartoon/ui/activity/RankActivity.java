/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.RankAdapter;
import com.tplink.cartoon.ui.presenter.RankPresenter;
import com.tplink.cartoon.ui.source.rank.RankDataSource;
import com.tplink.cartoon.ui.view.IRankView;
import com.tplink.cartoon.ui.widget.CustomTab;
import com.tplink.cartoon.ui.widget.ElasticScrollView;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RankActivity extends BaseActivity<RankPresenter> implements IRankView<List<Comic>> {

    @BindView(R.id.rv_bookshelf)
    RecyclerView mRecycleView;
    @BindView(R.id.ev_scrollview)
    ElasticScrollView mScrollView;
    @BindView(R.id.ll_actionbar)
    CustomTab mTab;

    RankAdapter mAdapter;

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<Comic> data) {
        mAdapter.updateWithClear(data);
    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new RankPresenter(new RankDataSource(), this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_rank;
    }

    @Override
    protected void initView() {
        initWindow(false);
        mAdapter = new RankAdapter(this, R.layout.item_rank, R.layout.item_loading);
        final NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(this, 1);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setAdapter(mAdapter);
        mScrollView.setListener(new ElasticScrollView.OnScrollListener() {
            @Override
            public void onScrollToBottom() {
                mPresenter.loadData();
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mAdapter.getItems(position);
                IntentUtil.toComicDetail(RankActivity.this, comic.getId(), comic.getTitle());
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.loadData();
    }

    @OnClick(R.id.iv_back_color)
    public void finish(View view) {
        this.finish();
    }

    @OnClick(R.id.iv_search)
    public void toSearch(View view) {
        IntentUtil.toSearch(this);
    }

    @OnClick({R.id.rl_update, R.id.rl_sellgood, R.id.rl_hot, R.id.rl_mouth})
    public void setType(View view) {
        switch (view.getId()) {
            case R.id.rl_update:
                mPresenter.setType("upt");
                break;
            case R.id.rl_sellgood:
                mPresenter.setType("pay");
                break;
            case R.id.rl_hot:
                mPresenter.setType("pgv");
                break;
            case R.id.rl_mouth:
                mPresenter.setType("mt");
                break;
        }
    }

    @Override
    public void setType(int position) {
        mTab.setCurrentPosition(position);
    }
}
