package com.tplink.cartoon.ui.activity;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-9, xufeng, Create file
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.DetailAdapter;
import com.tplink.cartoon.ui.presenter.IndexPresenter;
import com.tplink.cartoon.ui.source.Index.IndexDataSource;
import com.tplink.cartoon.ui.view.IIndexView;

import butterknife.BindView;
import butterknife.OnClick;

public class IndexActivity extends BaseActivity<IndexPresenter> implements IIndexView {
    @BindView(R.id.rv_index)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_order)
    ImageView mIvOrder;
    @BindView(R.id.tv_loading_title)
    TextView mTitle;
    @BindView(R.id.tv_downloaded)
    TextView mDownload;

    private DetailAdapter mAdapter;
    private Comic mComic;


    @OnClick({R.id.iv_order})
    public void OrderList(ImageView Order) {
        mAdapter.setOrder(!mAdapter.isOrder());
        if (!mAdapter.isOrder()) {
            mIvOrder.setImageResource(R.drawable.daoxu);
        } else {
            mIvOrder.setImageResource(R.drawable.zhengxu);
        }
    }

    @OnClick(R.id.iv_back_color)
    public void finish(View view) {
        this.finish();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new IndexPresenter(new IndexDataSource(), this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_index;
    }

    @Override
    protected void initView() {
        mComic = (Comic) getIntent().getSerializableExtra(Constants.COMIC);
        mAdapter = new DetailAdapter(this, R.layout.item_chapter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.updateWithClear(mComic.getChapters());
        mTitle.setText(mComic.getTitle());
        mDownload.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
    }
}
