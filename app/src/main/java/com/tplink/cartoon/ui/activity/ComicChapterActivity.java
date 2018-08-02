package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.ChapterViewpagerAdapter;
import com.tplink.cartoon.ui.presenter.ChapterPresenter;
import com.tplink.cartoon.ui.source.chapter.ChapterDataSource;
import com.tplink.cartoon.ui.view.IChapterView;
import com.tplink.cartoon.ui.widget.ComicReaderViewpager;
import com.tplink.cartoon.ui.widget.ReaderMenuLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xu
 */

public class ComicChapterActivity extends BaseActivity<ChapterPresenter> implements IChapterView<DBChapters> {

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
    private String mComicChapters;
    private String mComicChapterTitle;
    private ChapterViewpagerAdapter mAdapter;
    private int mComicSize;

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
    public void fillData(DBChapters data) {
        mAdapter.setDatas(data.getComiclist());
        if (mAdapter.getDirection() == Constants.RIGHT_TO_LEFT) {
            mViewpager.setCurrentItem(data.getComiclist().size() - 1, false);//关闭切换动画
        }
        mComicSize = data.getComiclist().size();
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
    public void nextChapter() {

    }

    @Override
    public void preChapter() {

    }

    @Override
    public void SwitchModel(int a) {

    }

    @Override
    public void prePage() {
        int postion = mViewpager.getCurrentItem() - 1;
        if (postion >= 0) {
            mViewpager.setCurrentItem(postion);
        } else {
            preChapter();
        }
    }

    @Override
    public void nextPage() {
        int postion = mViewpager.getCurrentItem() + 1;
        if (postion < mAdapter.getCount()) {
            mViewpager.setCurrentItem(postion);
        } else {
            nextChapter();
        }
    }

    @Override
    public void setTitle(String name) {
        mTitle.setText(name);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ChapterPresenter(new ChapterDataSource(), this);
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
        mComicChapters = intent.getStringExtra(Constants.COMIC_CHAPERS);
        mComicChapterTitle = intent.getStringExtra(Constants.COMIC_CHAPER_TITLE);
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
                mPresenter.setTitle(mComicChapterTitle, mComicSize, position, mAdapter.getDirection());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
