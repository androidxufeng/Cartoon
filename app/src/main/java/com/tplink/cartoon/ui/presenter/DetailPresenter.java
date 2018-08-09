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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.activity.ComicDetailActivity;
import com.tplink.cartoon.ui.source.detail.DetailDataSource;
import com.tplink.cartoon.ui.widget.IndexItemView;
import com.tplink.cartoon.utils.ShowErrorTextUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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

    public void getDetail(final String comicId) {
        DisposableSubscriber<Comic> disposable = mDataSource.getDetail(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Comic>() {
                    @Override
                    public void onNext(Comic comic) {
                        comic.setId(comicId);
                        mComic = comic;
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

    public void orderIndex(LinearLayout mlayout) {
        for (int position = 0; position < mComic.getChapters().size(); position++) {
            IndexItemView itemView = (IndexItemView) mlayout.getChildAt(position);
            TextView textView = itemView.getTextView();
            if (!isOrder()) {
                textView.setText((position + 1) + " - " + mComic.getChapters().get(position));
                mView.orderData(R.drawable.zhengxu);
            } else {
                mView.orderData(R.drawable.daoxu);
                textView.setText((mComic.getChapters().size() - position) + " - " + mComic.getChapters().get(mComic.getChapters().size() - 1 - position));
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
