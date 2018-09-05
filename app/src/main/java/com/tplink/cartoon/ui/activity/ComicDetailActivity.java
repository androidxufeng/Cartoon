package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.presenter.DetailPresenter;
import com.tplink.cartoon.ui.view.IDetailView;
import com.tplink.cartoon.ui.widget.DetailFloatLinearLayout;
import com.tplink.cartoon.ui.widget.DetailScrollView;
import com.tplink.cartoon.ui.widget.IndexItemView;
import com.tplink.cartoon.utils.DisplayUtil;
import com.tplink.cartoon.utils.IntentUtil;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * create by xu
 */

public class ComicDetailActivity extends BaseActivity<DetailPresenter> implements
        IDetailView<Comic>, IndexItemView.onItemClickLinstener {

    @BindView(R.id.iv_image)
    ImageView mHeaderView;

    @BindView(R.id.sv_comic)
    DetailScrollView mScrollView;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.ll_text)
    LinearLayout mText;
    @BindView(R.id.iv_image_bg)
    ImageView mHeaderViewBg;
    @BindView(R.id.tv_author_tag)
    TextView mAuthorTag;
    @BindView(R.id.tv_collects)
    TextView mCollects;
    @BindView(R.id.tv_describe)
    TextView mDescribe;
    @BindView(R.id.tv_popularity)
    TextView mPopularity;
    @BindView(R.id.tv_status)
    TextView mStatus;
    @BindView(R.id.tv_update)
    TextView mUpdate;
    @BindView(R.id.tv_point)
    TextView mPoint;
    @BindView(R.id.ll_detail)
    RelativeLayout mDetail;
    @BindView(R.id.tv_tab)
    TextView mTab;
    @BindView(R.id.iv_order)
    ImageView mOrder;
    @BindView(R.id.tv_actionbar_title)
    TextView mActionBarTitle;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_oreder2)
    ImageView mOrder2;
    @BindView(R.id.btn_read)
    Button mRead;

    @BindView(R.id.iv_loading)
    ImageView mLoading;
    @BindView(R.id.rl_loading)
    RelativeLayout mRLloading;
    @BindView(R.id.tv_loading)
    TextView mLoadingText;
    @BindView(R.id.iv_error)
    ImageView mReload;

    @BindView(R.id.tv_loading_title)
    TextView mLoadingTitle;
    @BindView(R.id.ll_index)
    LinearLayout mIndex;
    @BindView(R.id.iv_collect)
    ImageView mCollect;
    @BindView(R.id.tv_collect)
    TextView mTvCollect;
    @BindView(R.id.ll_floatbottom)
    DetailFloatLinearLayout mFloatButtom;

    private int mCurrent;

    @OnClick(R.id.iv_error)
    public void reload(View view) {
        mPresenter.getDetail(mComicId);
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.GONE);
        mLoadingText.setText("正在重新加载，请稍后");
    }

    @OnClick({R.id.iv_back_color, R.id.iv_back})
    public void OnFinish(View view) {
        finish();
    }

    @OnClick(R.id.iv_collect)
    void selectComic(View v) {
        mPresenter.collectComic(!mPresenter.getComic().getIsCollected());
    }

    @OnClick({R.id.iv_oreder2, R.id.iv_order})
    public void orderList(ImageView order) {
        mPresenter.setOrder(!mPresenter.isOrder());
        if (!mPresenter.isOrder()) {
            mOrder2.setImageResource(R.drawable.zhengxu);
            mOrder.setImageResource(R.drawable.zhengxu);
        } else {
            mOrder2.setImageResource(R.drawable.daoxu);
            mOrder.setImageResource(R.drawable.daoxu);
        }
        mPresenter.orderIndex(mIndex);
    }

    @OnClick(R.id.btn_read)
    public void startRead(View view) {
        if (mCurrent == 0) {
            IntentUtil.toComicChapter(this, 0, mPresenter.getComic());
        } else {
            IntentUtil.toComicChapter(this, mCurrent - 1, mPresenter.getComic());
        }
    }

    @OnClick(R.id.iv_download)
    public void toSelectDownloadActivity(View view) {
        IntentUtil.toSelectDownload(this, mPresenter.getComic());
    }

    private float mScale;
    private float mDy;
    private Rect normal = new Rect();
    private Long mComicId;

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getCurrentChapters();
    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new DetailPresenter(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_comic_detail;
    }

    @Override
    protected void initView() {
        mLoadingTitle.setText(getIntent().getStringExtra(Constants.COMIC_TITLE));
        mScrollView.setScaleTopListener(new MyScaleTopListener());
        //动画
        mLoading.setImageResource(R.drawable.loading_list);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoading.getDrawable();
        animationDrawable.start();

        mFloatButtom.setOnFloatBottomClickListener(new DetailFloatLinearLayout.FloatBottomOnclickListener() {
            @Override
            public void onClickLocation(View view) {
                mScrollView.scrollToPosition(mCurrent);
            }

            @Override
            public void onClickScroll(View view) {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void initData() {
        mComicId = getIntent().getLongExtra(Constants.COMIC_ID, 0);
        mPresenter.getDetail(mComicId);
    }

    @Override
    public void getDataFinish() {
    }

    @Override
    public void showErrorView(String throwable) {
        mRLloading.setVisibility(View.VISIBLE);
        mReload.setVisibility(View.VISIBLE);
        mLoadingText.setText(throwable);
    }

    @Override
    public void fillData(Comic comic) {
        mRLloading.setVisibility(View.GONE);
        Glide.with(this)
                .load(comic.getCover())
                .placeholder(R.drawable.pic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CenterCrop(this))
                .into(mHeaderView);
        Glide.with(this)
                .load(comic.getCover())
                .placeholder(R.drawable.pic_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new BlurTransformation(this, 10), new CenterCrop(this))
                .into(mHeaderViewBg);
        mActionBarTitle.setText(comic.getTitle());
        mTitle.setText(comic.getTitle());
        mAuthorTag.setText(comic.getAuthor() + comic.getTags().toString());
        mCollects.setText(comic.getCollections());
        mDescribe.setText(comic.getDescribe());
        mStatus.setText(comic.getStatus());
        mPopularity.setText(comic.getPopularity());
        mUpdate.setText(comic.getUpdates());
        mPoint.setText(comic.getPoint());
        //showToast(comic.getCollections());
        normal.set(mText.getLeft(), mText.getTop(), DisplayUtil.getMobileWidth(this), mText.getBottom());
        mCurrent = mPresenter.getComic().getCurrentChapter();
        if (mCurrent > 0) {
            mRead.setText("续看第" + mCurrent + "话");
        }
        for (int i = 0; i < comic.getChapters().size(); i++) {
            IndexItemView indexItemView = new IndexItemView(this, comic.getChapters().get(i), i);
            indexItemView.setListener(this);
            mIndex.addView(indexItemView);
        }
        mScrollView.setInnerHeight();
        setCollect(comic.getIsCollected());
    }

    @Override
    public void orderData(int res) {
        mOrder.setImageResource(res);
        mOrder2.setImageResource(res);
    }

    @Override
    public void setCollect(boolean isCollect) {
        if (isCollect) {
            mCollect.setImageResource(R.drawable.collect_select);
            mTvCollect.setText("已收藏");
        } else {
            mCollect.setImageResource(R.drawable.collect);
            mTvCollect.setText("收藏");
        }
    }

    @Override
    public void setCurrent(int current) {
        if (!mPresenter.isOrder()) {
            if (mCurrent - 1 >= 0) {
                ((IndexItemView) mIndex.getChildAt(mCurrent - 1)).setCurrentColor(false);
            }
            if (current - 1 >= 0) {
                ((IndexItemView) mIndex.getChildAt(current - 1)).setCurrentColor(true);
            }
        } else {
            if (mPresenter.getComic().getChapters().size() - mCurrent >= 0) {
                ((IndexItemView) mIndex.getChildAt(mPresenter.getComic().getChapters().size() - mCurrent)).setCurrentColor(false);
            }
            if (mPresenter.getComic().getChapters().size() - current >= 0) {
                ((IndexItemView) mIndex.getChildAt(mPresenter.getComic().getChapters().size() - current)).setCurrentColor(true);
            }
        }
        mCurrent = current;
        mRead.setText("续看第" + mCurrent + "话");
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mPresenter.isOrder()) {
            position = mPresenter.getComic().getChapters().size() - position - 1;
            Log.d("ComicDetailActivity", "position=" + position);
        }
        IntentUtil.toComicChapter(ComicDetailActivity.this,
                position,
                mPresenter.getComic());
    }

    public class MyScaleTopListener implements DetailScrollView.ScaleTopListener {
        @Override
        public void isScale(float y) {
            int height = DisplayUtil.dip2px(ComicDetailActivity.this, 200);
            int mobileWidth = DisplayUtil.getMobileWidth(ComicDetailActivity.this);
            mScale = y / height;
            float width = mobileWidth * mScale;
            float dx = (width - mobileWidth) / 2;
            mHeaderView.layout((int) (0 - dx), 0, (int) (mobileWidth + dx), (int) y);
            mDy = y - height;
            mText.layout(normal.left, (int) (normal.top + mDy), normal.right, (int) (normal.bottom + mDy));
            Log.d("DetailActivity", "Dy=" + mDy + ",normal=" + normal.toString());
        }

        @Override
        public void isFinished() {
            Log.d("DetailScrollView", "Dy=" + mDy);
            int mobileWidth = DisplayUtil.getMobileWidth(ComicDetailActivity.this);
            Interpolator in = new DecelerateInterpolator();
            ScaleAnimation ta = new ScaleAnimation(mScale, 1.0f, mScale, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            //第一个参数fromX为动画起始时 X坐标上的伸缩尺寸
            //第二个参数toX为动画结束时 X坐标上的伸缩尺寸
            //第三个参数fromY为动画起始时Y坐标上的伸缩尺寸
            //第四个参数toY为动画结束时Y坐标上的伸缩尺寸
            //第五个参数pivotXType为动画在X轴相对于物件位置类型
            //第六个参数pivotXValue为动画相对于物件的X坐标的开始位置
            //第七个参数pivotXType为动画在Y轴相对于物件位置类型
            //第八个参数pivotYValue为动画相对于物件的Y坐标的开始位置
            ta.setInterpolator(in);
            ta.setDuration(300);
            mHeaderView.startAnimation(ta);
            mHeaderView.layout(0, 0, mobileWidth, DisplayUtil.dip2px(ComicDetailActivity.this, 200));
            //设置文字活动的动画
            TranslateAnimation trans = new TranslateAnimation(0, 0, mDy, 0);
            trans.setInterpolator(in);
            trans.setDuration(300);
            mText.startAnimation(trans);
            mText.layout(normal.left, normal.top, normal.right, normal.bottom);
            //统统重置
            mScale = 1.0f;
            mDy = 0;
        }

        @Override
        public void isBlurTransform(float y) {
            mHeaderViewBg.setAlpha(1 - y);
        }

        @Override
        public void isShowTab(int a) {
            setTab(a);
        }

        private void setTab(int a) {
            switch (a) {
                case DetailScrollView.SHOW_CHAPER_TAB:
                    if (mDetail.getVisibility() == View.GONE) {
                        mDetail.setVisibility(View.VISIBLE);
                    } else {
                        mTab.setText("详情");
                        mOrder.setVisibility(View.GONE);
                    }
                    break;
                case DetailScrollView.SHOW_DETAIL_TAB:
                    mTab.setText("目录");
                    mOrder.setVisibility(View.VISIBLE);
                    break;
                case DetailScrollView.SHOW_ACTIONBAR_TITLE:
                    if (mDetail.getVisibility() == View.VISIBLE) {
                        mOrder.setVisibility(View.GONE);
                        mDetail.setVisibility(View.GONE);
                        mTab.setText("详情");
                    }
                    if (mActionBarTitle.getVisibility() == View.GONE) {
                        mActionBarTitle.setVisibility(View.VISIBLE);
                        AnimationSet animationSet = new AnimationSet(true);
                        TranslateAnimation trans = new TranslateAnimation(0, 0,
                                DisplayUtil.dip2px(getApplicationContext(), 12), 0);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                        animationSet.addAnimation(trans);
                        animationSet.addAnimation(alphaAnimation);
                        animationSet.setDuration(200);
                        mActionBarTitle.startAnimation(animationSet);
                    }
                    break;
                case DetailScrollView.SHOW_NONE:
                    if (mDetail.getVisibility() == View.VISIBLE) {
                        mTab.setText("详情");
                        mOrder.setVisibility(View.GONE);
                        mDetail.setVisibility(View.GONE);
                    }
                    if (mActionBarTitle.getVisibility() == View.VISIBLE) {
                        mActionBarTitle.setVisibility(View.GONE);
                        AnimationSet animationSet = new AnimationSet(true);
                        TranslateAnimation trans = new TranslateAnimation(0, 0, 0,
                                DisplayUtil.dip2px(getApplicationContext(), 12));
                        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                        animationSet.addAnimation(trans);
                        animationSet.addAnimation(alphaAnimation);
                        animationSet.setDuration(200);
                        mActionBarTitle.startAnimation(animationSet);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}