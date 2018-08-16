package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.tplink.cartoon.ui.widget.SwitchRelativeLayout;
import com.tplink.cartoon.ui.widget.ZBubbleSeekBar;
import com.tplink.cartoon.utils.IntentUtil;
import com.xw.repo.BubbleSeekBar;

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
    @BindView(R.id.tv_loading_title)
    TextView mtvLoading;

    @BindView(R.id.rl_switch_model)
    SwitchRelativeLayout mSwitchModel;
    @BindView(R.id.iv_normal_model)
    Button mNormal;
    @BindView(R.id.iv_j_comic_model)
    Button mJcomic;


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
                mPresenter.getComicChapterTitle(), getIntent().getStringExtra(Constants.COMIC_TITLE)
                , mPresenter.getDirect());
    }

    private ChapterViewpagerAdapter mAdapter;

    @OnClick({R.id.iv_back, R.id.iv_back_color})
    public void finish(View view) {
        this.finish();
    }

    @Override
    public void getDataFinish() {
        mRLloading.setVisibility(View.GONE);
    }


    @Override
    public void showErrorView(String error) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        mLoadingText.setText(error);
    }

    @Override
    public void fillData(PreloadChapters data) {
        mPresenter.setPreloadChapters(data);
        mAdapter.setDatas(data);
        mSeekBar.setmMax(data.getNowSize());
        if (mAdapter.getDirection() == Constants.RIGHT_TO_LEFT) {
            mViewpager.setCurrentItem(data.getNextSize() + data.getNowSize() - 1, false);//关闭切换动画
        } else {
            mViewpager.setCurrentItem(data.getPreSize(), false);
        }
        mPresenter.updateComicCurrentChapter();
    }

    @Override
    public void showMenu() {
        if (!menuLayout.isShow()) {
            menuLayout.setVisibility(View.VISIBLE);
        } else {
            menuLayout.setVisibility(View.GONE);
        }

        if (mSwitchModel.isShow()) {
            mSwitchModel.setVisibility(View.GONE);
        }
    }

    @Override
    public void nextChapter(PreloadChapters data, int loadingPosition) {
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(loadingPosition, false);
        mSeekBar.setmMax(data.getNowSize());
        //为什么第一页的时候需要单独再设置Progress?因为adapter的LIST并未发生改变，
        // 所以调用刷新方法后没有调用onPageSelected方法，故没有设置Progress
        if (mPresenter.getComicChapters() == 1) {
            if (mAdapter.getDirection() == Constants.LEFT_TO_RIGHT) {
                mSeekBar.setProgress(1);
            } else {
                mSeekBar.setProgress(data.getNowSize());
            }
        }
        mPresenter.updateComicCurrentChapter();
    }

    @Override
    public void preChapter(PreloadChapters data, int loadingPosition) {
        mAdapter.setDatas(data);
        mViewpager.setCurrentItem(loadingPosition, false);
        mSeekBar.setmMax(data.getNowSize());
        //为什么第一页的时候需要单独再设置Progress?因为adapter的LIST并未发生改变，
        // 所以调用刷新方法后没有调用onPageSelected方法，故没有设置Progress
        if (mPresenter.getComicChapters() == 0) {
            if (mAdapter.getDirection() == Constants.LEFT_TO_RIGHT) {
                mSeekBar.setProgress(data.getNowSize());
            } else {
                mSeekBar.setProgress(1);
            }
        }
        mPresenter.updateComicCurrentChapter();
    }


    @Override
    public void switchModel(int direct) {
        if (mAdapter.getDirection() != direct) {
            //必须重新new 一个adapter 否则在接近页面的时候，会有缓存，图片切换会有问题
            mAdapter = new ChapterViewpagerAdapter(this, mPresenter.getPreloadChapters(), direct);
            mAdapter.setListener(new ChapterViewpagerAdapter.OnceClickListener() {
                @Override
                public void onClick(View view, float x, float y) {
                    mPresenter.clickScreen(x, y);
                }
            });
            mViewpager.setAdapter(mAdapter);
            mPresenter.setDirect(direct);
            mSwitchModel.setVisibility(View.GONE, direct);
            initReaderModule(direct);
            mViewpager.setCurrentItem(mPresenter.getPreloadChapters().getDataSize() - mViewpager.getCurrentItem() - 1, false);
        }
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
    protected void initPresenter(Intent intent) {
        long comicId = intent.getLongExtra(Constants.COMIC_ID, 0);
        int comicChapter = intent.getIntExtra(Constants.COMIC_CHAPTERS, 0);
        int type = intent.getIntExtra(Constants.COMIC_READ_TYPE, Constants.LEFT_TO_RIGHT);
        ArrayList comicChapterTitle = intent.getStringArrayListExtra(Constants.COMIC_CHAPTER_TITLE);
        mPresenter = new ChapterPresenter(new ChapterDataSource(this), this);
        mPresenter.init(comicId, comicChapterTitle, comicChapter, type);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mRLloading.setVisibility(View.VISIBLE);
        mLoadingText.setText("正在加载,请稍后...");
        initPresenter(intent);
        initView();
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
        mAdapter.setDirection(mPresenter.getDirect());
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
                }
                if (mSwitchModel.isShow()) {
                    mSwitchModel.setVisibility(View.GONE);
                }
                if (mAdapter.getDirection() == Constants.LEFT_TO_RIGHT) {
                    mSeekBar.setProgress(position - mPresenter.getPreloadChapters().getPreSize() + 1);
                } else {
                    mSeekBar.setProgress(position - mPresenter.getPreloadChapters().getNextSize() + 1);
                }
                mPresenter.loadMoreData(position, mAdapter.getDirection());
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
        //设置加载时候的标题
        mtvLoading.setText(mPresenter.getComicChapterTitle().get(mPresenter.getComicChapters()));
    }

    @Override
    protected void initData() {
        mPresenter.getChapterData();
        initReaderModule(mPresenter.getDirect());
    }

    /**
     * 初始化阅读模式做一些处理
     */
    private void initReaderModule(int direct) {
        if (direct == Constants.LEFT_TO_RIGHT) {
            mSeekBar.setSeekBarColor(true);
            mNormal.setBackgroundResource(R.drawable.normal_model_press);
            mJcomic.setBackgroundResource(R.drawable.btn_switchmodel_j_comic);
        } else {
            mSeekBar.setSeekBarColor(false);
            mNormal.setBackgroundResource(R.drawable.btn_switchmodel_normal);
            mJcomic.setBackgroundResource(R.drawable.j_comic_model_press);
        }
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

    @OnClick(R.id.iv_setting)
    public void toSwitch(View view) {
        menuLayout.setVisibility(View.GONE);
        if (!mSwitchModel.isShow()) {
            mSwitchModel.setVisibility(View.VISIBLE);
        } else {
            mSwitchModel.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.iv_normal_model)
    public void switchToLeftToRight(View view) {
        mPresenter.setReaderModuel(Constants.LEFT_TO_RIGHT);
    }

    @OnClick(R.id.iv_j_comic_model)
    public void switchToRightToLeft(View view) {
        mPresenter.setReaderModuel(Constants.RIGHT_TO_LEFT);
    }
}
