/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-23, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.tplink.cartoon.utils.IntentUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DownloadChapterlistActivity extends BaseActivity<DownloadChapterlistPresenter>
        implements IDownloadView<List<DBDownloadItem>> {

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.rv_downloadlist)
    RecyclerView mRecyclerview;
    @BindView(R.id.rl_loading)
    RelativeLayout mRLloading;
    @BindView(R.id.tv_loading)
    TextView mLoadingText;
    @BindView(R.id.iv_error)
    ImageView mReload;
    @BindView(R.id.iv_loading)
    ImageView mLoading;
    @BindView(R.id.tv_download)
    TextView mDownloadText;
    @BindView(R.id.iv_download)
    ImageView mDownloadImage;

    private DownloadChapterlistAdapter mAdapter;
    private int recycleState = 0;

    @OnClick(R.id.iv_back_color)
    public void finish(View view) {
        this.finish();
    }

    @OnClick(R.id.iv_error)
    public void reload(View view) {
        mPresenter.initData();
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
        mLoadingText.setText("正在重新加载，请稍后");
    }

    @OnClick(R.id.rl_all)
    public void pauseOrStartAll(View view) {
        onPauseOrStartAll();
    }

    @OnClick(R.id.rl_order)
    public void toSelectDownload() {
        IntentUtil.toSelectDownload(this, mPresenter.getComic());
    }


    @Override
    public void onLoadMoreData(List<DBDownloadItem> dbDownloadItems) {

    }

    @Override
    public void onSelectALL() {

    }

    @Override
    public void onDeleteAll() {

    }

    @Override
    public void showErrorView(String throwable) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        mLoadingText.setText(throwable);
    }

    @Override
    public void fillData(List<DBDownloadItem> data) {
        mAdapter.updateWithClear(data);
        mRLloading.setVisibility(View.GONE);
        mPresenter.startAll();
    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
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
        //动画初始化
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoading.getDrawable();
        animationDrawable.start();
        //
        mAdapter = new DownloadChapterlistAdapter(this, R.layout.item_downloadlist);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(layoutmanager);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //实时监测滑动状态
                recycleState = newState;
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                DBDownloadItem info = mAdapter.getItems(position);
                switch (info.getState()) {
                    case DownState.NONE:
                        mPresenter.stop(info, position, false);
                        break;
                    case DownState.START:
                        //mPresenter.startDown(info);
                        break;
                    case DownState.PAUSE:
//                        mPresenter.startDown(info, position);
                        break;
                    case DownState.DOWN:
                        mPresenter.stop(info, position, true);
                        break;
                    case DownState.STOP:
                        mPresenter.ready(info, position);
                        break;
                    case DownState.ERROR:
                        mPresenter.ready(info, position);
                        break;
                    case DownState.FINISH:
                        mPresenter.toComicChapter(info);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void updateView(int postion) {
        //如果是静止状态，则刷新局部，滑动则全局刷新
        if (recycleState == 0) {
            //刷新局部，不然会影响点击事件
            mAdapter.notifyItemChanged(postion, "payload");
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDownloadFinished() {
        mDownloadText.setText("下载完成");
        mDownloadImage.setVisibility(View.GONE);
        mPresenter.isAllDownload = DownloadChapterlistPresenter.FINISH;
    }

    public void onPauseOrStartAll() {
        switch (mPresenter.isAllDownload) {
            case DownloadChapterlistPresenter.DOWNLOADING:
                mPresenter.stopAll();
                mDownloadText.setText("全部开始");
                mDownloadImage.setImageResource(R.drawable.pasue);
                mPresenter.isAllDownload = DownloadChapterlistPresenter.STOP_DOWNLOAD;
                break;
            case DownloadChapterlistPresenter.STOP_DOWNLOAD:
                mPresenter.reStartAll();
                mDownloadText.setText("全部停止");
                mDownloadImage.setImageResource(R.drawable.pasue_select);
                mPresenter.isAllDownload = DownloadChapterlistPresenter.DOWNLOADING;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initPresenter(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.updateComic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.pauseAll();
    }
}
