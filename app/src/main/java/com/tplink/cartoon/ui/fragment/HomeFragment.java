/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-14, xufeng, Create file
 */
package com.tplink.cartoon.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.MainAdapter;
import com.tplink.cartoon.ui.presenter.HomePresenter;
import com.tplink.cartoon.ui.source.HomeDataSource;
import com.tplink.cartoon.ui.view.IHomeView;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;
import com.tplink.cartoon.ui.widget.ZElasticRefreshScrollView;
import com.tplink.cartoon.utils.DisplayUtil;
import com.tplink.cartoon.utils.GlideImageLoader;
import com.tplink.cartoon.utils.IntentUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment<HomePresenter> implements
        IHomeView<Comic>, MainAdapter.OnItemClickListener {

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.sv_comic)
    ZElasticRefreshScrollView mScrollView;
    @BindView(R.id.rl_error_view)
    RelativeLayout mErrorView;
    @BindView(R.id.iv_error)
    ImageView mReload;
    @BindView(R.id.B_banner)
    Banner mBanner;
    @BindView(R.id.rl_actionbar)
    RelativeLayout mActionBar;
    @BindView(R.id.v_actionbar_bg)
    View mActionBarBg;

    private int i = 3;


    @OnClick(R.id.iv_error)
    public void reloadData(View view) {
        mErrorView.setVisibility(View.GONE);
        mPresenter.loadData();
    }

    @OnClick({R.id.ll_category1, R.id.ll_category2, R.id.ll_category3, R.id.ll_category4, R.id.ll_category5})
    public void toCategory(View view) {
        switch (view.getId()) {
            case R.id.ll_category1:
                showToast("开发中，敬请期待");
                break;
            case R.id.ll_category2:
                showToast("开发中，敬请期待");
                break;
            case R.id.ll_category3:
                showToast("开发中，敬请期待");
                break;
            case R.id.ll_category4:
                showToast("开发中，敬请期待");
                break;
            case R.id.ll_category5:
                showToast("开发中，敬请期待");
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.iv_search)
    void toSearch(View v) {
        IntentUtil.toSearch(getActivity());
    }

    private MainAdapter mAdapter;

    @Override
    protected void initPresenter() {
        HomeDataSource mainDataSource = new HomeDataSource();
        mPresenter = new HomePresenter(mainDataSource, this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        //设置banner加载设置
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);

        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(mActivity, 6);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(mActivity, R.layout.item_hometitle,
                R.layout.item_homepage, R.layout.item_homepage_full);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
//        mRecycleView.addItemDecoration(new DividerGridItemDecoration(mActivity));
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

            @Override
            public void onActionDown() {
                mBanner.stopAutoPlay();
            }

            @Override
            public void onActionUp() {
                mBanner.startAutoPlay();
            }

            @Override
            public void onScroll(int y) {
                if (y == ZElasticRefreshScrollView.SCROLL_TO_DOWN) {
                    if (mActionBar.getVisibility() == View.VISIBLE) {
                        mActionBar.setVisibility(View.GONE);
                        AnimationSet animationSet = new AnimationSet(true);
                        TranslateAnimation trans = new TranslateAnimation(0, 0, 0, -DisplayUtil.dip2px(mActivity.getApplicationContext(), 60));
                        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                        animationSet.addAnimation(trans);
                        animationSet.addAnimation(alphaAnimation);
                        animationSet.setDuration(200);
                        mActionBar.startAnimation(animationSet);
                    }
                } else {
                    if (mActionBar.getVisibility() == View.GONE) {
                        mActionBar.setVisibility(View.VISIBLE);
                        AnimationSet animationSet = new AnimationSet(true);
                        TranslateAnimation trans = new TranslateAnimation(0, 0, -DisplayUtil.dip2px(mActivity.getApplicationContext(), 60), 0);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                        animationSet.addAnimation(trans);
                        animationSet.addAnimation(alphaAnimation);
                        animationSet.setDuration(200);
                        mActionBar.startAnimation(animationSet);
                    }
                }

            }

            @Override
            public void onAlphaActionBar(float a) {
                mActionBarBg.setAlpha(a);
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
        mErrorView.setVisibility(View.GONE);
        mRecycleView.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void fillBanner(List<Comic> data) {
        mBanner.setImages(data);
        mBanner.start();
    }

    @Override
    public void onRefreshFinish(List<Comic> data) {
        mScrollView.setRefreshing(false);
        if (mErrorView.isShown()) {
            mErrorView.setVisibility(View.GONE);
            mRecycleView.setVisibility(View.VISIBLE);
        }
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
    public void onTitleClick(RecyclerView parent, View view, int type) {
        switch (type) {
            case Constants.TYPE_RANK_LIST:
                showToast("更多排行开发中");
                break;
            case Constants.TYPE_RECOMMEND:
                showToast("更多热门推荐开发中");
                break;
            default:
                showToast("开发中");
                break;
        }
    }

    @Override
    public void showToast(String t) {
        Toast.makeText(mActivity, t, Toast.LENGTH_SHORT).show();
    }
}
