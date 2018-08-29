package com.tplink.cartoon.ui.presenter;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-1, xufeng, Create file
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.net.RetryFunction;
import com.tplink.cartoon.ui.activity.ComicDetailActivity;
import com.tplink.cartoon.ui.source.detail.DetailDataSource;
import com.tplink.cartoon.ui.widget.IndexItemView;
import com.tplink.cartoon.utils.DisplayUtil;
import com.tplink.cartoon.utils.ShowErrorTextUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class DetailPresenter extends BasePresenter<DetailDataSource, ComicDetailActivity> {

    private final CompositeDisposable mCompositeDisposable;

    private Context mContext;

    private boolean isOrder;

    private Comic mComic;

    public DetailPresenter(DetailDataSource dataSource, ComicDetailActivity view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
        mContext = view;
        mComic = new Comic();
    }

    public void getDetail(final long comicId) {
        DisposableSubscriber<Comic> disposable = mDataSource.getDetail(comicId)
                .retryWhen(new RetryFunction())
                .compose(mView.<Comic>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Comic>() {
                    @Override
                    public void onNext(Comic comic) {
                        comic.setId(comicId);
                        mComic = comic;
                        saveComicToDB(comic);
                        mView.fillData(comic);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.showErrorView(ShowErrorTextUtil.ShowErrorText(t));
                    }

                    @Override
                    public void onComplete() {
                        mView.getDataFinish();
                    }
                });

        mCompositeDisposable.add(disposable);
    }

    public void collectComic(boolean isCollected) {

        mComic.setCollectTime(getCurrentTime());
        mComic.setIsCollected(isCollected);
        DisposableSubscriber<Boolean> disposable = mDataSource.updateComicToDB(mComic)
                .compose(mView.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mView.setCollect(mComic.getIsCollected());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.showToast("收藏失败11111" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void saveComicToDB(Comic comic) {
        DisposableSubscriber<Boolean> disposable = mDataSource.saveComicToDB(comic)
                .compose(mView.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.d("ceshi", "onNext: 保存到数据库 result = " + aBoolean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.showToast("保存到数据库失败：" + t.toString());
                        Log.d("ceshi", "onError: 保存到数据库失败：" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void getCurrentChapters() {
        DisposableSubscriber<Comic> disposable = mDataSource.getComicFromDB(mComic.getId())
                .compose(mView.<Comic>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Comic>() {
                    @Override
                    public void onNext(Comic comic) {
                        if (comic != null) {
                            mComic = comic;
                            mView.setCurrent(comic.getCurrentChapter() + 1);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.showToast("获取当前章节数目失败");
                        Log.d("ceshi", "onError: " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void orderIndex(LinearLayout mlayout) {
        Drawable img_location = mContext.getResources().getDrawable(R.drawable.location);
        img_location.setBounds(0, 0, img_location.getMinimumWidth(), img_location.getMinimumHeight());
        for (int position = 0; position < mComic.getChapters().size(); position++) {
            IndexItemView itemView = (IndexItemView) mlayout.getChildAt(position);
            TextView textView = itemView.getTextView();
            if (!isOrder()) {
                textView.setText((position + 1) + " - " + mComic.getChapters().get(position));
                if (mComic.getCurrentChapter() == (position + 1)) {
                    textView.setTextColor(Color.parseColor("#ff9a6a"));
                    textView.setCompoundDrawables(null, null, img_location, null);
                    textView.setCompoundDrawablePadding(DisplayUtil.dip2px(mContext, 10));
                } else {
                    textView.setTextColor(Color.parseColor("#666666"));
                    textView.setCompoundDrawables(null, null, null, null);
                }
            } else {
                if (mComic.getChapters().size() - mComic.getCurrentChapter() == position) {
                    textView.setTextColor(Color.parseColor("#ff9a6a"));
                    textView.setCompoundDrawables(null, null, img_location, null);
                    textView.setCompoundDrawablePadding(DisplayUtil.dip2px(mContext, 10));
                } else {
                    textView.setTextColor(Color.parseColor("#666666"));
                    textView.setCompoundDrawables(null, null, null, null);
                }
                textView.setText((mComic.getChapters().size() - position) + " - " + mComic.getChapters().get(mComic.getChapters().size() - 1 - position));
            }

            if (!isOrder()) {
                mView.orderData(R.drawable.zhengxu);
            } else {
                mView.orderData(R.drawable.daoxu);
            }
        }
    }

    public Comic getComic() {
        return mComic;
    }

    public void setComic(Comic comic) {
        mComic = comic;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }

    public boolean isOrder() {
        return isOrder;
    }
}
