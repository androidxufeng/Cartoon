package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import com.tplink.cartoon.ui.widget.ZBubbleSeekBar;
import com.tplink.cartoon.utils.IntentUtil;
import com.xw.repo.BubbleSeekBar;

import java.net.ConnectException;
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
    @BindView(R.id.sb_seekbar)
    ZBubbleSeekBar mSeekBar;

    @BindView(R.id.iv_loading)
    ImageView mLoading;
    @BindView(R.id.rl_loading)
    RelativeLayout mRLloading;
    @BindView(R.id.tv_loading)
    TextView mLoadingText;
    @BindView(R.id.iv_error)
    ImageView mReload;

    @OnClick(R.id.iv_error)
    public void reload(View view) {
        mPresenter.getChapterData();
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
        mLoadingText.setText("正在重新加载，请稍后");
    }

    @OnClick(R.id.iv_index)
    public void toIndex(View view) {
        IntentUtil.toIndex(ComicChapterActivity.this, mPresenter.getComicId(),
                mPresenter.getComicChapters(), (ArrayList<String>) mPresenter.getComicChapterTitle());
    }

    private ChapterViewpagerAdapter mAdapter;

    @OnClick(R.id.iv_back)
    public void finish(View view) {
        this.finish();
    }

    @Override
    public void getDataFinish() {
        mRLloading.setVisibility(View.GONE);
    }


    @Override
    public void showErrorView(Throwable throwable) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        if (throwable instanceof ConnectException) {
            mLoadingText.setText("无法访问服务器接口");
        } else {
            mLoadingText.setText("未知错误" + throwable.toString());
        }
    }

    @Override
    public void fillData(PreloadChapters data) {
        mPresenter.setPreloadChapters(data);
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(data.getPreSize(), false);
        mSeekBar.setmMax(data.getNowSize());
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
        mViewpager.setCurrentItem(data.getPreSize() + loadingPosition, false);
        mSeekBar.setmMax(data.getNowSize());
        //为什么第一页的时候需要单独再设置Progress?因为adapter的LIST并未发生改变，
        // 所以调用刷新方法后没有调用onPageSelected方法，故没有设置Progress
        if (mPresenter.getComicChapters() == 1) {
            mSeekBar.setProgress(1);
        }
    }

    @Override
    public void preChapter(PreloadChapters data, int loadingPosition) {
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(data.getPreSize() + data.getNowSize() + loadingPosition - 1, false);
        mSeekBar.setmMax(data.getNowSize());
        //为什么第一页的时候需要单独再设置Progress?因为adapter的LIST并未发生改变，
        // 所以调用刷新方法后没有调用onPageSelected方法，故没有设置Progress
        if (mPresenter.getComicChapters() == 0) {
            mSeekBar.setProgress(data.getNowlist().size());
        }
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
            showToast("没有啦");
        }
    }

    @Override
    public void nextPage() {
        int position = mViewpager.getCurrentItem() + 1;
        if (position < mAdapter.getCount()) {
            mViewpager.setCurrentItem(position);
        } else {
            showToast("没有啦");
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
        mLoading.setImageResource(R.drawable.loading_list);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoading.getDrawable();
        animationDrawable.start();
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
                if (menuLayout.isShow()) {
                    menuLayout.setVisibility(View.GONE);
                    mSeekBar.setProgress(position - mPresenter.getPreloadChapters().getPrelist().size() + 1);
                }
                mPresenter.loadMoreData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                mViewpager.setCurrentItem(progress + mPresenter.getPreloadChapters().getPreSize() - 1);
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {
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
