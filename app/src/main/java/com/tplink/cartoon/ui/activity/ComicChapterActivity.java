package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.PreloadChapters;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.ChapterViewpagerAdapter;
import com.tplink.cartoon.ui.presenter.ChapterPresenter;
import com.tplink.cartoon.ui.source.chapter.ChapterDataSource;
import com.tplink.cartoon.ui.view.IChapterView;
import com.tplink.cartoon.ui.widget.ComicReaderViewpager;
import com.tplink.cartoon.ui.widget.ReaderMenuLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xu
 */

public class ComicChapterActivity extends BaseActivity<ChapterPresenter> implements IChapterView<PreloadChapters> {

    @BindView(R.id.vp_chapters)
    ComicReaderViewpager mViewpager;
    @BindView(R.id.rl_top)
    RelativeLayout mTop;
    @BindView(R.id.rl_bottom)
    RelativeLayout mBottom;
    @BindView(R.id.rl_menu)
    ReaderMenuLayout menuLayout;
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.tv_title)
    TextView mTitle;
    private ChapterViewpagerAdapter mAdapter;

    @OnClick(R.id.iv_back)
    public void finish(View view) {
        this.finish();
    }

    @Override
    public void getDataFinish() {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showErrorView(Throwable throwable) {
        showToast("加载数据失败" + throwable);
    }

    @Override
    public void fillData(PreloadChapters data) {
        mPresenter.setPreloadChapters(data);
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(data.getPrelist().size(), false);
    }

    @Override
    public void showMenu() {
        if (!menuLayout.isShow()) {
            menuLayout.setVisibility(View.VISIBLE);
        } else {
            menuLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void nextChapter(PreloadChapters data, int loadingPosition) {
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(data.getPrelist().size() + loadingPosition, false);
        showToast("完成了预加载");
    }

    @Override
    public void preChapter(PreloadChapters data, int loadingPosition) {
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(data.getPrelist().size() + data.getNowlist().size() + loadingPosition - 1, false);
        showToast("完成了预加载");
    }


    @Override
    public void switchModel(int a) {

    }

    @Override
    public void prePage() {
        int position = mViewpager.getCurrentItem() - 1;
        if (position >= 0) {
            mViewpager.setCurrentItem(position);
        } else {
        }
    }

    @Override
    public void nextPage() {
        int position = mViewpager.getCurrentItem() + 1;
        if (position < mAdapter.getCount()) {
            mViewpager.setCurrentItem(position);
        } else {
        }
    }

    @Override
    public void setTitle(String name) {
        mTitle.setText(name);
    }

    @Override
    protected void initPresenter() {
        Intent intent = getIntent();
        String comicId = intent.getStringExtra(Constants.COMIC_ID);
        int comicChapter = intent.getIntExtra(Constants.COMIC_CHAPTERS, 0);
        ArrayList comicChapterTitle = intent.getStringArrayListExtra(Constants.COMIC_CHAPTER_TITLE);
        mPresenter = new ChapterPresenter(new ChapterDataSource(), this);
        mPresenter.init(comicId, comicChapterTitle, comicChapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chapter;
    }

    @Override
    protected void initView() {
        setNavigation();
        mAdapter = new ChapterViewpagerAdapter(this);
        mViewpager.setOffscreenPageLimit(4);
        mAdapter.setListener(new ChapterViewpagerAdapter.OnceClickListener() {
            @Override
            public void onClick(View view, float x, float y) {
                mPresenter.clickScreen(x, y);
            }
        });
        mViewpager.setAdapter(mAdapter);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPresenter.loadMoreData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getChapterData();
    }


    /**
     * 设置setNavigation()
     */
    private void setNavigation() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
