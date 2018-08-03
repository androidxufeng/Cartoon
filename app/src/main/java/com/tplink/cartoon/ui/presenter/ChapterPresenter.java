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

import android.util.Log;

import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.data.bean.PreloadChapters;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.activity.ComicChapterActivity;
import com.tplink.cartoon.ui.source.chapter.ChapterDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ChapterPresenter extends BasePresenter<ChapterDataSource, ComicChapterActivity> {

    private final CompositeDisposable mCompositeDisposable;
    private PreloadChapters mPreloadChapters = new PreloadChapters();
    private int mComicChapters;
    private List<String> mComicChapterTitle;

    public ChapterPresenter(ChapterDataSource dataSource, ComicChapterActivity view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
    }

    public void init(List<String> comicChapterTitle, int Chapters) {
        //判断如果是点进上次点击的那一话
        this.mComicChapterTitle = comicChapterTitle;
        this.mComicChapters = Chapters;
    }

    public void getChapterData(String id, final int chapter) {

        for (int i = 0; i < 3; i++) {
            DisposableSubscriber<DBChapters> disposable = mDataSource.getChapterData(id, chapter - 1 + i)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<DBChapters>() {
                        @Override
                        public void onNext(DBChapters chapters) {
                            Log.d("ceshi", "onNext: "+chapters.getTitle());
                            //分别设置三个章节
                            if (mComicChapters - 1 == chapters.getChapters()) {
                                if (mComicChapters - 1 < 0) {
                                    mPreloadChapters.setPrelist(new ArrayList<String>());
                                }
                                mPreloadChapters.setPrelist(chapters.getComiclist());
                            } else if (mComicChapters == chapters.getChapters()) {
                                mPreloadChapters.setNowlist(chapters.getComiclist());
                            } else {
                                if (mComicChapters + 1 > mComicChapterTitle.size()) {
                                    mPreloadChapters.setNextlist(new ArrayList<String>());
                                }
                                mPreloadChapters.setNextlist(chapters.getComiclist());
                            }
                            //三个章节都不为NULL
                            if (mPreloadChapters.isNotNull()) {
                                if (mPreloadChapters.getNowSize() == 1) {
                                    mView.showToast("该章节是腾讯付费章节，处于版权问题不以展示，请去腾讯观看");
                                } else {
                                    mView.fillData(mPreloadChapters);
                                }
                            }
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

    public void getMoreChapterData(String id, int chapter, final int position, int direction) {
        DisposableSubscriber<DBChapters> disposable = mDataSource.loadMoreData(id, chapter, position, direction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<DBChapters>() {
                    @Override
                    public void onNext(DBChapters chapters) {
//                        mView.fillData(chapters);
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
            title = comic_chapter_title + (position + 1) + "/" + comic_size;
        } else {
            title = comic_chapter_title + (comic_size - position) + "/" + comic_size;
        }
        mView.setTitle(title);
    }
}
