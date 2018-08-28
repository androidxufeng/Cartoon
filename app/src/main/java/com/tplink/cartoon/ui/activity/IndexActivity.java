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

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.DetailAdapter;
import com.tplink.cartoon.ui.presenter.IndexPresenter;
import com.tplink.cartoon.ui.source.Index.IndexDataSource;
import com.tplink.cartoon.ui.view.IIndexView;
import com.tplink.cartoon.utils.IntentUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class IndexActivity extends BaseActivity<IndexPresenter> implements IIndexView,
        BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.rv_index)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_order)
    ImageView mIvOrder;
    @BindView(R.id.tv_loading_title)
    TextView mTitle;
    @BindView(R.id.tv_downloaded)
    TextView mDownload;
    @BindView(R.id.tv_chapters_num)
    TextView mChapterNum;

    private DetailAdapter mAdapter;

    @OnClick({R.id.iv_order})
    public void orderList(ImageView order) {
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
    protected void initPresenter(Intent intent) {
        mPresenter = new IndexPresenter(new IndexDataSource(), this, intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_index;
    }

    @Override
    protected void initView() {
        mAdapter = new DetailAdapter(this, R.layout.item_chapter);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter.updateWithClear(mPresenter.getComic().getChapters());
        mAdapter.setOnItemClickListener(this);
        mTitle.setText(mPresenter.getComic().getTitle());
        mChapterNum.setText("共" + mPresenter.getComic().getChapters().size() + "话");
        mDownload.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        if (!mAdapter.isOrder()) {
            position = mPresenter.getComic().getChapters().size() - position - 1;
        }
        IntentUtil.toComicChapter(IndexActivity.this, position, mPresenter.getComic());
    }
}
