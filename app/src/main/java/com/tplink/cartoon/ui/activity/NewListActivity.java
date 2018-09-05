/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-9-5, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.NewListAdapter;
import com.tplink.cartoon.ui.presenter.NewListPresenter;
import com.tplink.cartoon.ui.view.INewView;
import com.tplink.cartoon.ui.widget.ElasticImageScrollView;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;
import com.tplink.cartoon.utils.IntentUtil;
import com.tplink.cartoon.utils.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewListActivity extends BaseActivity<NewListPresenter> implements INewView<List<Comic>> {
    @BindView(R.id.rv_bookshelf)
    RecyclerView mRecyclerView;
    @BindView(R.id.ev_scrollview)
    ElasticImageScrollView mScrollView;
    @BindView(R.id.rl_title)
    RelativeLayout mTitle;

    @OnClick(R.id.iv_back_color)
    public void back(View view){
        finish();
    }

    private NewListAdapter mAdapter;

    @Override
    public void showErrorView(String throwable) {
        LogUtil.d(throwable);
    }

    @Override
    public void fillData(List<Comic> data) {
        mAdapter.updateWithClear(data);
        mAdapter.notifyDataSetChanged();
        mScrollView.setInnerHeight();
    }

    @Override
    public void getDataFinish() {

    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new NewListPresenter(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_new;
    }

    @Override
    protected void initView() {
        mAdapter = new NewListAdapter(this, R.layout.item_newlist, R.layout.item_loading);
        NoScrollGridLayoutManager gridLayoutManager = new NoScrollGridLayoutManager(this, 1);
        gridLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mScrollView.setListener(new ElasticImageScrollView.OnScrollListener() {
            @Override
            public void OnScrollToBottom() {
                mPresenter.loadData();
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAlphaActionBar(float a) {
                mTitle.setAlpha(a);
                if (a != 0) {
                    initWindow(false);
                } else {
                    initWindow(true);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if (position != mAdapter.getItemCount() - 1) {
                    Comic comic = mAdapter.getItems(position);
                    IntentUtil.toComicDetail(NewListActivity.this, comic.getId(), comic.getTitle());
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.loadData();
    }
}
