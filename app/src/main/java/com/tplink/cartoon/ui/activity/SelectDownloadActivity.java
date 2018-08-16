/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-16, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.SelectDownloadAdapter;
import com.tplink.cartoon.ui.presenter.SelectDownloadPresenter;
import com.tplink.cartoon.ui.source.download.DownloadDataSource;
import com.tplink.cartoon.ui.view.ISelectDownloadView;
import com.tplink.cartoon.ui.widget.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectDownloadActivity extends BaseActivity<SelectDownloadPresenter> implements ISelectDownloadView {

    @BindView(R.id.tv_chapters_num)
    TextView mChapterNum;
    @BindView(R.id.rv_index)
    RecyclerView mRecycleView;
    @BindView(R.id.iv_order)
    ImageView mIvOrder;

    @OnClick({R.id.iv_order})
    public void orderList(ImageView order) {
        mAdapter.setOrder(!mAdapter.isOrder());
        if (mAdapter.isOrder()) {
            mIvOrder.setImageResource(R.drawable.zhengxu);
        } else {
            mIvOrder.setImageResource(R.drawable.daoxu);

        }
    }

    private ArrayList<String> chapters;
    private SelectDownloadAdapter mAdapter;

    @Override
    protected void initPresenter(Intent intent) {
        chapters = intent.getStringArrayListExtra(Constants.COMIC_CHAPTER_TITLE);
        mPresenter = new SelectDownloadPresenter(new DownloadDataSource(), this, chapters);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_download;
    }

    @Override
    protected void initView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new SelectDownloadAdapter(this, R.layout.item_select_download);
        mAdapter.updateWithClear(chapters);
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if (mAdapter.isOrder()) {
                    mPresenter.addToSelected(position);
                } else {
                    mPresenter.addToSelected(chapters.size() - position - 1);
                }
            }
        });
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(this, R.drawable.decorationlist_dark));
        mChapterNum.setText("共" + chapters.size() + "话");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void startDownload() {

    }

    @Override
    public void addToDownloadList(HashMap map) {
        mAdapter.setHashMap(map);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addAll() {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(Object data) {

    }

    @Override
    public void getDataFinish() {

    }
}
