package com.tplink.cartoon.ui.activity;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.DetailAdapter;
import com.tplink.cartoon.ui.presenter.DetailPresenter;
import com.tplink.cartoon.ui.source.detail.DetailDataSource;
import com.tplink.cartoon.ui.view.IDetailView;
import com.tplink.cartoon.ui.widget.DetailScrollView;
import com.tplink.cartoon.ui.widget.NoScrollStaggeredGridLayoutManager;
import com.tplink.cartoon.utils.DisplayUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * create by xu
 */

public class ComicDetailActivity extends BaseActivity<DetailPresenter> implements
        IDetailView<Comic>, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.iv_image)
    ImageView mHeaderView;

    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

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
    private DetailAdapter mAdapter;
    private Comic mComic;
    private float mScale;
    private float mDy;
    private Rect normal = new Rect();

    @Override
    protected void initPresenter() {
        mPresenter = new DetailPresenter(new DetailDataSource(), this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_comic_detail;
    }

    @Override
    protected void initView() {
        NoScrollStaggeredGridLayoutManager layoutManager = new NoScrollStaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new DetailAdapter(this, R.layout.item_chapter);
        mRecycleView.setAdapter(mAdapter);
        mIvBack.setOnClickListener(this);
        mScrollView.setScaleTopListener(new MyScaleTopListener());
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        String comicId = getIntent().getStringExtra(Constants.COMIC_ID);
        mPresenter.getDetail(comicId);
    }

    @Override
    public void getDataFinish() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyView() {
        // TODO
    }

    @Override
    public void showErrorView(Throwable throwable) {

    }

    @Override
    public void fillData(Comic comic) {
        mComic = comic;
        Glide.with(this)
                .load(comic.getCover())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CenterCrop(this))
                .into(mHeaderView);
        Glide.with(this)
                .load(comic.getCover())
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
        List<String> datas = comic.getChapters();
        if (datas != null && datas.size() != 0) {
            mAdapter.updateWithClear(datas);
        } else {
            showToast("未取到数据");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.iv_oreder2, R.id.iv_order})
    public void OrderList(ImageView Order) {
        mAdapter.setOrder(!mAdapter.isOrder());
        if (!mAdapter.isOrder()) {
            mOrder2.setImageResource(R.drawable.daoxu);
            mOrder.setImageResource(R.drawable.daoxu);
        } else {
            mOrder2.setImageResource(R.drawable.zhengxu);
            mOrder.setImageResource(R.drawable.zhengxu);
        }
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {

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
            //Log.d("DetailActivity","y="+y+",gettopview="+y);
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
                        TranslateAnimation trans = new TranslateAnimation(0, 0, DisplayUtil.dip2px(getApplicationContext(), 12), 0);
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
                        TranslateAnimation trans = new TranslateAnimation(0, 0, 0, DisplayUtil.dip2px(getApplicationContext(), 12));
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
