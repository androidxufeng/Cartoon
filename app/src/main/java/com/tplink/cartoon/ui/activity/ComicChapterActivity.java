package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.PreloadChapters;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.ChapterRecyclerAdapter;
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
    @BindView(R.id.iv_down_model)
    Button mDown;
    @BindView(R.id.rv_chapters)
    RecyclerView mRecycleView;

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

    @OnClick(R.id.iv_down_model)
    public void switchToUpToDown(View view) {
        mPresenter.setReaderModuel(Constants.UP_TO_DOWN);
    }

    @OnClick({R.id.iv_back, R.id.iv_back_color})
    public void finish(View view) {
        this.finish();
    }

    private ChapterViewpagerAdapter mAdapter;

    ChapterRecyclerAdapter mVerticalAdapter;

    private int mCurrentPosition;

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

        if (mPresenter.getDirect() == Constants.UP_TO_DOWN) {
            mVerticalAdapter.setDatas(data);
            mRecycleView.setVisibility(View.VISIBLE);
            mViewpager.setVisibility(View.GONE);
            mRecycleView.scrollToPosition(data.getPreSize());
            mCurrentPosition = data.getPreSize();
        } else {
            mAdapter.setDatas(data);
            mRecycleView.setVisibility(View.GONE);
            mViewpager.setVisibility(View.VISIBLE);
            if (mAdapter.getDirection() == Constants.RIGHT_TO_LEFT) {
                mViewpager.setCurrentItem(data.getNextSize() + data.getNowSize() - 1, false);//关闭切换动画
            } else {
                mViewpager.setCurrentItem(data.getPreSize(), false);
            }
        }
        mSeekBar.setmMax(data.getNowSize());
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
        if (mPresenter.getDirect() == Constants.UP_TO_DOWN) {
            mVerticalAdapter.setDatas(data);
            mRecycleView.scrollToPosition(loadingPosition);
            //为什么在这里要设置progress而其他的不用，因为没有viewpager的onPageSelected方法
            mSeekBar.setProgress(loadingPosition - data.getPreSize());

        } else {
            mAdapter.setDatas(data);
            mViewpager.setCurrentItem(loadingPosition, false);
            //为什么第一页的时候需要单独再设置Progress?因为adapter的LIST并未发生改变，
            // 所以调用刷新方法后没有调用onPageSelected方法，故没有设置Progress
            if (mPresenter.getComicChapters() == 1) {
                if (mAdapter.getDirection() == Constants.LEFT_TO_RIGHT) {
                    mSeekBar.setProgress(1);
                } else {
                    mSeekBar.setProgress(data.getNowSize());
                }
            }
        }
        mSeekBar.setmMax(data.getNowSize());
        mPresenter.updateComicCurrentChapter();
    }

    @Override
    public void preChapter(PreloadChapters data, int loadingPosition) {
        if (mPresenter.getDirect() == Constants.UP_TO_DOWN) {
            mVerticalAdapter.setDatas(data);
            mRecycleView.scrollToPosition(loadingPosition);
            //为什么在这里要设置progress而其他的不用，因为没有viewpager的onPageSelected方法
            mSeekBar.setProgress(loadingPosition - data.getPreSize());
        } else {
            mAdapter.setDatas(data);
            mViewpager.setCurrentItem(loadingPosition, false);
            //为什么第一页的时候需要单独再设置Progress?因为adapter的LIST并未发生改变，
            // 所以调用刷新方法后没有调用onPageSelected方法，故没有设置Progress
            if (mPresenter.getComicChapters() == 0) {
                if (mAdapter.getDirection() == Constants.LEFT_TO_RIGHT) {
                    mSeekBar.setProgress(data.getNowSize());
                } else {
                    mSeekBar.setProgress(1);
                }
            }
        }
        mSeekBar.setmMax(data.getNowSize());
        mPresenter.updateComicCurrentChapter();
    }


    @Override
    public void switchModel(int direct) {
        if (mPresenter.getDirect() != direct) {

            //卷轴模式
            if (direct == Constants.UP_TO_DOWN) {
                mRecycleView.setVisibility(View.VISIBLE);
                mViewpager.setVisibility(View.GONE);
                int nowPosition = mViewpager.getCurrentItem();
                mVerticalAdapter.setDatas(mPresenter.getPreloadChapters());
                if (mPresenter.getDirect() == Constants.LEFT_TO_RIGHT) {
                    mRecycleView.scrollToPosition(nowPosition);
                    mCurrentPosition = nowPosition;
                } else if (mPresenter.getDirect() == Constants.RIGHT_TO_LEFT) {
                    mRecycleView.scrollToPosition(mPresenter.getPreloadChapters().getDataSize() - nowPosition - 1);
                    mCurrentPosition = mPresenter.getPreloadChapters().getDataSize() - nowPosition - 1;
                }
                mSeekBar.setProgress(mCurrentPosition - mPresenter.getPreloadChapters().getPreSize() + 1);

            } else {
                //横版模式
                mRecycleView.setVisibility(View.GONE);
                mViewpager.setVisibility(View.VISIBLE);
                int nowPosition;
                if (mPresenter.getDirect() == Constants.UP_TO_DOWN) {
                    if (direct == Constants.RIGHT_TO_LEFT) {
                        nowPosition = mCurrentPosition + 1;
                    } else {
                        nowPosition = mPresenter.getPreloadChapters().getDataSize() - mCurrentPosition - 2;
                    }
                } else {
                    nowPosition = mViewpager.getCurrentItem();
                }
                //必须重新new 一个adapter 否则在接近页面的时候，会有缓存，图片切换会有问题
                mAdapter = new ChapterViewpagerAdapter(this, mPresenter.getPreloadChapters(), direct);
                mAdapter.setListener(new ChapterViewpagerAdapter.OnceClickListener() {
                    @Override
                    public void onClick(View view, float x, float y) {
                        mPresenter.clickScreen(x, y);
                    }
                });
                mViewpager.setAdapter(mAdapter);
                mViewpager.setCurrentItem(mPresenter.getPreloadChapters().getDataSize() - nowPosition - 1, false);
            }
            mPresenter.setDirect(direct);
            mSwitchModel.setVisibility(View.GONE, direct);
            initReaderModule(direct);
        }
    }

    @Override
    public void prePage() {
        if (mPresenter.getDirect() == Constants.UP_TO_DOWN) {

        } else {
            int position = mViewpager.getCurrentItem() - 1;
            if (position >= 0) {
                mViewpager.setCurrentItem(position);
            } else {
                showToast("没有啦");
            }
        }
    }

    @Override
    public void nextPage() {
        if (mPresenter.getDirect() == Constants.UP_TO_DOWN) {
        } else {
            int position = mViewpager.getCurrentItem() + 1;
            if (position < mAdapter.getCount()) {
                mViewpager.setCurrentItem(position);
            } else {
                showToast("没有啦");
            }
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
        initPagerRead();

        initVerticalRead();

        mSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                if (mAdapter.getDirection() == Constants.RIGHT_TO_LEFT) {
                    mViewpager.setCurrentItem(progress + mPresenter.getPreloadChapters().getNextSize() - 1);
                } else if (mAdapter.getDirection() == Constants.LEFT_TO_RIGHT) {
                    mViewpager.setCurrentItem(progress + mPresenter.getPreloadChapters().getPreSize() - 1);
                } else {
                    mRecycleView.smoothScrollToPosition(progress + mPresenter.getPreloadChapters().getPreSize() - 1);
                }
            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
        //设置加载时候的标题
        mtvLoading.setText(mPresenter.getComicChapterTitle().get(mPresenter.getComicChapters()));
    }

    private void initVerticalRead() {
        mVerticalAdapter = new ChapterRecyclerAdapter(this, R.layout.item_comic_reader);
        mRecycleView.setAdapter(mVerticalAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mVerticalAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                mPresenter.clickScreen(0.5f, 0.5f);
            }
        });
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (menuLayout.isShow()) {
                    menuLayout.setVisibility(View.GONE);
                }
                if (mSwitchModel.isShow()) {
                    mSwitchModel.setVisibility(View.GONE);
                }
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    if (firstItemPosition != mCurrentPosition) {
                        mCurrentPosition = firstItemPosition;
                        mSeekBar.setProgress(mCurrentPosition - mPresenter.getPreloadChapters().getPreSize() + 1);
                        mPresenter.loadMoreData(mCurrentPosition, Constants.UP_TO_DOWN);
                        if (firstItemPosition == 0 || lastItemPosition == mPresenter.getPreloadChapters().getDataSize() - 1) {
                            showToast("没有了");
                        }
                    }
                }
            }
        });
    }

    /**
     * 横版模式初始化
     */
    private void initPagerRead() {
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
            mDown.setBackgroundResource(R.drawable.btn_switchmodel_down);
        } else if (direct == Constants.RIGHT_TO_LEFT) {
            mSeekBar.setSeekBarColor(false);
            mNormal.setBackgroundResource(R.drawable.btn_switchmodel_normal);
            mJcomic.setBackgroundResource(R.drawable.j_comic_model_press);
            mDown.setBackgroundResource(R.drawable.btn_switchmodel_down);
        } else {
            mSeekBar.setSeekBarColor(true);
            mNormal.setBackgroundResource(R.drawable.btn_switchmodel_normal);
            mJcomic.setBackgroundResource(R.drawable.btn_switchmodel_j_comic);
            mDown.setBackgroundResource(R.drawable.down_model_press);
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
}
