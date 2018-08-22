/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.DownloadListAdapter;
import com.tplink.cartoon.ui.presenter.DownloadListPresenter;
import com.tplink.cartoon.ui.source.download.DownloadListDataSource;
import com.tplink.cartoon.ui.view.IDownloadView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DownloadListActivity extends BaseActivity<DownloadListPresenter> implements
        IDownloadView<List<DBDownloadItem>> {

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.rv_downloadlist)
    RecyclerView mRvDownload;

    private DownloadListAdapter mDownloadListAdapter;

    @OnClick(R.id.iv_back_color)
    public void finish(View view) {
        this.finish();
    }

    @Override
    public void onStartDownload(int chapters) {

    }

    @Override
    public void onPausedDownload(int chapters) {

    }

    @Override
    public void onStartAll() {

    }

    @Override
    public void onPauseAll() {

    }

    @Override
    public void onSelectALL() {

    }

    @Override
    public void onDeleteAll() {

    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new DownloadListPresenter(new DownloadListDataSource(), this, intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_downloadlist;
    }

    @Override
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvDownload.setLayoutManager(linearLayoutManager);
        mDownloadListAdapter = new DownloadListAdapter(this, R.layout.item_downloadlist);
        mRvDownload.setAdapter(mDownloadListAdapter);
    }

    @Override
    protected void initData() {
        mPresenter.initData();
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<DBDownloadItem> data) {
        mDownloadListAdapter.updateWithClear(data);
    }

    @Override
    public void getDataFinish() {

    }
}
