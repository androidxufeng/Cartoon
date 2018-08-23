/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-23, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.bean.DownState;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.DownloadChapterlistAdapter;
import com.tplink.cartoon.ui.presenter.DownloadChapterlistPresenter;
import com.tplink.cartoon.ui.source.download.DownloadListDataSource;
import com.tplink.cartoon.ui.view.IDownloadView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DownloadChapterlistActivity extends BaseActivity<DownloadChapterlistPresenter>
        implements IDownloadView<List<DBDownloadItem>> {

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.rv_downloadlist)
    RecyclerView mRecyclerview;
    private DownloadChapterlistAdapter mAdapter;

    @OnClick(R.id.iv_back_color)
    public void finish(View view) {
        this.finish();
    }

    @Override
    public void onLoadMoreData(List<DBDownloadItem> dbDownloadItems) {

    }

    @Override
    public void onStartAll() {
        mPresenter.startAll();
    }

    @Override
    public void onPauseAll() {
        mPresenter.pauseAll();
    }

    @Override
    public void onSelectALL() {

    }

    @Override
    public void onDeleteAll() {

    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<DBDownloadItem> data) {
        mAdapter.updateWithClear(data);
    }

    @Override
    public void getDataFinish() {

    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new DownloadChapterlistPresenter(new DownloadListDataSource(this), this, intent);
        mTitle.setText(((Comic) intent.getSerializableExtra(Constants.COMIC)).getTitle());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_downloadlist;
    }

    @Override
    protected void initView() {
        mAdapter = new DownloadChapterlistAdapter(this, R.layout.item_downloadlist);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutmanager);
        mRecyclerview.setAdapter(mAdapter);
        mPresenter.initData();
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                DBDownloadItem info = mAdapter.getItems(position);
                switch (info.getState()) {
                    case DownState.NONE:
                        mPresenter.startDown(info);
                        break;
                    case DownState.START:
                        //mPresenter.startDown(info);
                        break;
                    case DownState.PAUSE:
                        mPresenter.startDown(info);
                        break;
                    case DownState.DOWN:
                        mPresenter.pause(info);
                        break;
                    case DownState.STOP:
                        mPresenter.startDown(info);
                        break;
                    case DownState.ERROR:
                        mPresenter.startDown(info);
                        break;
                    case DownState.FINISH:
                        showToast("已下载");
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.pauseAll();
    }
}
