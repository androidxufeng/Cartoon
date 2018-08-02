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

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.activity.ComicDetailActivity;
import com.tplink.cartoon.ui.source.detail.DetailDataSource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class DetailPresenter extends BasePresenter<DetailDataSource, ComicDetailActivity> {

    private final CompositeDisposable mCompositeDisposable;

    public DetailPresenter(DetailDataSource dataSource, ComicDetailActivity view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
    }

    public void getDetail(String comicId) {
        DisposableSubscriber<Comic> disposable = mDataSource.getDetail(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Comic>() {
                    @Override
                    public void onNext(Comic comic) {
                        mView.fillData(comic);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.showErrorView(t);
                    }

                    @Override
                    public void onComplete() {
                        mView.getDataFinish();
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
