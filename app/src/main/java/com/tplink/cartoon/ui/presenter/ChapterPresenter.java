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
 * Ver 1.0, 18-8-2, xufeng, Create file
 */

import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.activity.ComicChapterActivity;
import com.tplink.cartoon.ui.source.chapter.ChapterDataSource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ChapterPresenter extends BasePresenter<ChapterDataSource, ComicChapterActivity> {

    private final CompositeDisposable mCompositeDisposable;

    public ChapterPresenter(ChapterDataSource dataSource, ComicChapterActivity view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
    }

    public void getChapterData(String id, String chapter) {
        DisposableSubscriber<DBChapters> disposable = mDataSource.getChapterData(id, chapter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<DBChapters>() {
                    @Override
                    public void onNext(DBChapters subject) {
                        mView.fillData(subject);
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

    public void clickScreen(float x, float y) {
        if (x < 0.336) {
            mView.prePage();
        } else if (x < 0.666) {
            mView.showMenu();
        } else {
            mView.nextPage();
        }
    }

    public void setTitle(String comic_chapter_title, int comic_size, int position, int direct) {
        String title = null;
        if (direct == Constants.LEFT_TO_RIGHT) {
            title = comic_chapter_title + "-" + (position + 1) + "/" + comic_size;
        } else {
            title = comic_chapter_title + "-" + (comic_size - position) + "/" + comic_size;
        }
        mView.setTitle(title);
    }
}
