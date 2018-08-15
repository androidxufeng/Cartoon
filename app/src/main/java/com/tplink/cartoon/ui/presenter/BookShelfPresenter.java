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
 * Ver 1.0, 18-8-14, xufeng, Create file
 */

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.fragment.BookShelfFragment;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;
import com.tplink.cartoon.ui.source.MainDataSource;
import com.tplink.cartoon.utils.ShowErrorTextUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class BookShelfPresenter extends BasePresenter<BookShelfDataSource, BookShelfFragment> {

    private final CompositeDisposable mCompositeDisposable;

    public BookShelfPresenter(BookShelfDataSource dataSource, BookShelfFragment view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
    }

    public void loadData() {
        DisposableSubscriber<List<Comic>> disposable = mDataSource.getCollectedComicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        mView.fillData(comics);
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
}
