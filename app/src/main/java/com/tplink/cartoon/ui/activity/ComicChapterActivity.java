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
    private String mComicId;
    private int mComicChapters;
    private ArrayList<String> mComicChapterTitle;
    private ChapterViewpagerAdapter mAdapter;
    private int mComicSize;
    private PreloadChapters mChapters;
    private int mComicPostion;
    private boolean mIsScrolled;
    private boolean isLoadingdata;

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
        mChapters = data;
        mAdapter.setDatas(data);
        mComicSize = data.getNextlist().size();
        mViewpager.setCurrentItem(data.getPrelist().size(), false);
        setTitle(mComicChapterTitle + "-1/" + mComicSize);
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
    public void nextChapter(PreloadChapters data) {

    }

    @Override
    public void preChapter(PreloadChapters data) {

    }


    @Override
    public void switchModel(int a) {

    }

    @Override
    public void prePage() {
        int postion = mViewpager.getCurrentItem() - 1;
        if (postion >= 0) {
            mViewpager.setCurrentItem(postion);
        } else {
        }
    }

    @Override
    public void nextPage() {
        int postion = mViewpager.getCurrentItem() + 1;
        if (postion < mAdapter.getCount()) {
            mViewpager.setCurrentItem(postion);
        } else {
        }
    }

    @Override
    public void setTitle(String name) {
        mTitle.setText(name);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChapterPresenter(new ChapterDataSource(), this);
        mPresenter.init(mComicChapterTitle, mComicChapters);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_chapter;
    }

    @Override
    protected void initView() {
        setNavigation();
        Intent intent = getIntent();
        mComicId = intent.getStringExtra(Constants.COMIC_ID);
        mComicChapters = intent.getIntExtra(Constants.COMIC_CHAPERS, 0);
        mComicChapterTitle = intent.getStringArrayListExtra(Constants.COMIC_CHAPER_TITLE);
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
                String chapter_title = null;
                int now_postion = 0;
                if (position < mChapters.getPrelist().size()) {
                    chapter_title = mComicChapterTitle.get(mComicChapters - 1);
                    mComicSize = mChapters.getPrelist().size();
                    now_postion = position;
                } else if (position >= mChapters.getPrelist().size() + mChapters.getNowlist().size()) {
                    mComicSize = mChapters.getNextlist().size();
                    chapter_title = mComicChapterTitle.get(mComicChapters + 1);
                    now_postion = position - mChapters.getPrelist().size() - mChapters.getNowlist().size();
                } else {
                    mComicSize = mChapters.getNowlist().size();
                    chapter_title = mComicChapterTitle.get(mComicChapters);
                    now_postion = position - mChapters.getPrelist().size();
                }
                mComicPostion = position;
                mPresenter.setTitle(chapter_title, mComicSize, now_postion, mAdapter.getDirection());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mIsScrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        mIsScrolled = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (!mIsScrolled) {
                            if (!isLoadingdata) {
                                //mPresenter.loadMoreData(comic_id,comic_chapters,comic_postion,mAdapter.getDirection());
                                isLoadingdata = true;
                            }
                        }
                        mIsScrolled = true;
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getChapterData(mComicId, mComicChapters);
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
