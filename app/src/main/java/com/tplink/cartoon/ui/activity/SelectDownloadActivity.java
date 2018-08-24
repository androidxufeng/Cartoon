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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.SelectDownloadAdapter;
import com.tplink.cartoon.ui.presenter.SelectDownloadPresenter;
import com.tplink.cartoon.ui.source.download.DownloadListDataSource;
import com.tplink.cartoon.ui.view.ISelectDownloadView;
import com.tplink.cartoon.ui.widget.DividerGridItemDecoration;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;
import com.tplink.cartoon.utils.IntentUtil;

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
    @BindView(R.id.tv_select_all)
    TextView mSelected;
    @BindView(R.id.iv_select)
    ImageView mSelectedIcon;
    @BindView(R.id.tv_selected)
    TextView mSelectedNum;
    private Comic mComic;

    @OnClick({R.id.iv_order})
    public void orderList(ImageView order) {
        mAdapter.setOrder(!mAdapter.isOrder());
        if (mAdapter.isOrder()) {
            mIvOrder.setImageResource(R.drawable.zhengxu);
        } else {
            mIvOrder.setImageResource(R.drawable.daoxu);
        }
    }

    @OnClick({R.id.rl_select, R.id.rl_download})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_select:
                mPresenter.selectOrRemoveAll();
                break;
            case R.id.rl_download:
                startDownload();
                break;
            default:
                break;
        }
    }

    private SelectDownloadAdapter mAdapter;

    @Override
    protected void initPresenter(Intent intent) {
        mComic = (Comic) intent.getSerializableExtra(Constants.COMIC);
        mPresenter = new SelectDownloadPresenter(new DownloadListDataSource(this), this, mComic);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_select_download;
    }

    @Override
    protected void initView() {
        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(this, 3);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new SelectDownloadAdapter(this, R.layout.item_select_download);
        mAdapter.updateWithClear(mComic.getChapters());
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if (mAdapter.isOrder()) {
                    mPresenter.updateToSelected(position);
                } else {
                    mPresenter.updateToSelected(mComic.getChapters().size() - position - 1);
                }
            }
        });
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(this, R.drawable.decorationlist_dark));
        mChapterNum.setText("共" + mComic.getChapters().size() + "话");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void startDownload() {
        IntentUtil.toDownloadListActivity(this, mPresenter.getMap(), mComic);
    }

    @Override
    public void updateDownloadList(HashMap map) {
        mAdapter.setHashMap(map);
        mAdapter.notifyDataSetChanged();
        mSelectedNum.setText("已选择" + mPresenter.getSelectCount() + "话");
    }

    @Override
    public void addAll() {
        mSelected.setText("取消全选");
        mSelectedIcon.setImageResource(R.drawable.btn_cancel_select);
    }

    @Override
    public void removeAll() {
        mSelected.setText("全选");
        mSelectedIcon.setImageResource(R.drawable.btn_select);
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

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getDataFromDb();
    }
}
