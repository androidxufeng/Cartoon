/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-14, xufeng, Create file
 */
package com.tplink.cartoon.ui.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.MainAdapter;
import com.tplink.cartoon.ui.presenter.HomePresenter;
import com.tplink.cartoon.ui.source.MainDataSource;
import com.tplink.cartoon.ui.view.IHomeView;
import com.tplink.cartoon.ui.widget.DividerGridItemDecoration;
import com.tplink.cartoon.ui.widget.NoScrollStaggeredGridLayoutManager;
import com.tplink.cartoon.ui.widget.ZElasticRefreshScrollView;
import com.tplink.cartoon.utils.GlideImageLoader;
import com.tplink.cartoon.utils.IntentUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment<HomePresenter> implements
        IHomeView<Comic>, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.sv_comic)
    ZElasticRefreshScrollView mScrollView;
    @BindView(R.id.rl_error_view)
    RelativeLayout mErrorView;
    @BindView(R.id.iv_error)
    ImageView mReload;
    @BindView(R.id.iv_loading_top)
    ImageView mLoadingTop;
    @BindView(R.id.B_banner)
    Banner mBanner;

    private int i = 3;


    @OnClick(R.id.iv_error)
    public void reloadData(View view) {
        mErrorView.setVisibility(View.GONE);
        mPresenter.loadData();
    }

    private MainAdapter mAdapter;

    @Override
    protected void initPresenter() {
        MainDataSource mainDataSource = new MainDataSource();
        mPresenter = new HomePresenter(mainDataSource, this);
    }

    //初始化动画
    private void initAnimation() {
        mLoadingTop.setImageResource(R.drawable.loading_top);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingTop.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initAnimation();
        //设置banner加载设置
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);

        NoScrollStaggeredGridLayoutManager layoutManager = new NoScrollStaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(mActivity, R.layout.item_image);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
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

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Comic comic = mPresenter.getBanners().get(position);
                IntentUtil.toComicDetail(mActivity, comic.getId(), comic.getTitle());
            }
        });
        mPresenter.loadData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void getDataFinish() {
        mScrollView.setRefreshing(false);
        if (mErrorView.isShown()) {
            mErrorView.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void fillBanner(List<Comic> data) {
        mBanner.setImages(data);
        mBanner.start();
    }

    @Override
    public void showErrorView(String throwable) {
        mScrollView.setRefreshing(false);
        mErrorView.setVisibility(View.VISIBLE);
        mRecycleView.setVisibility(View.GONE);
    }


    @Override
    public void fillData(List<Comic> data) {
        if (data != null && data.size() != 0) {
            mAdapter.updateWithClear(data);
        } else {
            showToast("未取到数据");
        }
    }

    @Override
    public void appendMoreDataToView(List<Comic> data) {
        if (data != null && data.size() != 0) {
            mAdapter.update(data);
        } else {
            showToast("未取到数据");
        }
    }

    @Override
    public void hasNoMoreData() {
        showToast("没有数据了");
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        Comic comic = mAdapter.getItems(position);
        IntentUtil.toComicDetail(mActivity, comic.getId(), comic.getTitle());
    }

    @Override
    public void showToast(String t) {
        Toast.makeText(mActivity, t, Toast.LENGTH_SHORT).show();
    }
}
