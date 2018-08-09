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
import com.tplink.cartoon.utils.ShowErrorTextUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ChapterPresenter extends BasePresenter<ChapterDataSource, ComicChapterActivity> {

    private final CompositeDisposable mCompositeDisposable;
    //当前三话漫画
    private PreloadChapters mPreloadChapters;
    //漫画阅读方向
    private int mDirect;

    public int getComicChapters() {
        return mComicChapters;
    }

    private int mComicChapters;
    private List<String> mComicChapterTitle;
    private String mComicId;
    private int mComicSize;

    public List<String> getComicChapterTitle() {
        return mComicChapterTitle;
    }

    public String getComicId() {

        return mComicId;
    }

    //以中间的章节为分界限，前面时负值
    private int mLoadingPosition;
    private boolean isLoadingdata;

    public ChapterPresenter(ChapterDataSource dataSource, ComicChapterActivity view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
        mPreloadChapters = new PreloadChapters();
        mDirect = Constants.LEFT_TO_RIGHT;
    }

    public void init(String comicId, List<String> comicChapterTitle, int Chapters) {
        mComicId = comicId;
        this.mComicChapterTitle = comicChapterTitle;
        this.mComicChapters = Chapters;
    }

    public PreloadChapters getPreloadChapters() {
        return mPreloadChapters;
    }

    public void setPreloadChapters(PreloadChapters preloadChapters) {
        this.mPreloadChapters = preloadChapters;
    }

    public void getChapterData() {

        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            DisposableSubscriber<DBChapters> disposable = mDataSource.getChapterData(mComicId, mComicChapters - 1 + i)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<DBChapters>() {
                        @Override
                        public void onNext(DBChapters chapters) {
                            Log.d("ceshi", "onNext: " + chapters);
                            //分别设置三个章节
                            if (finalI == 0) {
                                if (mComicChapters - 1 < 0) {
                                    mPreloadChapters.setPrelist(new ArrayList<String>());
                                }
                                mPreloadChapters.setPrelist(chapters.getComiclist());
                            } else if (finalI == 1) {
                                mPreloadChapters.setNowlist(chapters.getComiclist());
                            } else {
                                if (mComicChapters + 1 > mComicChapterTitle.size()) {
                                    mPreloadChapters.setNextlist(new ArrayList<String>());
                                }
                                mPreloadChapters.setNextlist(chapters.getComiclist());
                            }
                            //三个章节都不为NULL
                            Log.d("ceshi", "onNext: mpreload = " + mPreloadChapters);

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

    public void loadMoreData(int position) {
        String chapter_title = null;
        int now_postion = 0;
        int comicSize = 0;
        if (position < mPreloadChapters.getPrelist().size()) {
            mLoadingPosition = position - mPreloadChapters.getPrelist().size() + 1;
            chapter_title = mComicChapterTitle.get(mComicChapters - 1);//前一章
            comicSize = mPreloadChapters.getPrelist().size();
            now_postion = position;
            if (!isLoadingdata) {
                isLoadingdata = true;
                getPreChapterData(mComicId, mComicChapters, Constants.LEFT_TO_RIGHT);
            }
        } else if (position >= mPreloadChapters.getPrelist().size() + mPreloadChapters.getNowlist().size()) {//后一章
            mLoadingPosition = position - mPreloadChapters.getPrelist().size() - mPreloadChapters.getNowlist().size();
            comicSize = mPreloadChapters.getNextlist().size();
            chapter_title = mComicChapterTitle.get(mComicChapters + 1);
            now_postion = position - mPreloadChapters.getPrelist().size() - mPreloadChapters.getNowlist().size();
            if (!isLoadingdata) {
                isLoadingdata = true;
                getNextChapterData(mComicId, mComicChapters, Constants.LEFT_TO_RIGHT);
            }
        } else {//当前章节
            isLoadingdata = false;
            comicSize = mPreloadChapters.getNowlist().size();
            chapter_title = mComicChapterTitle.get(mComicChapters);
            now_postion = position - mPreloadChapters.getPrelist().size();
        }
        setTitle(chapter_title, comicSize, now_postion, mDirect);
    }

    public void getNextChapterData(String id, int chapter, int direction) {
        DisposableSubscriber<DBChapters> disposable = mDataSource.loadNextData(id, chapter, direction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<DBChapters>() {
                    @Override
                    public void onNext(DBChapters chapters) {
                        if (!isLoadingdata){
                            return;
                        }
                        mPreloadChapters.setPrelist(mPreloadChapters.getNowlist());
                        mPreloadChapters.setNowlist(mPreloadChapters.getNextlist());
                        if (chapters.getComiclist().size() == 1) {
                            mPreloadChapters.setNextlist(new ArrayList<String>());
                        } else {
                            mPreloadChapters.setNextlist(chapters.getComiclist());
                        }
                        mComicChapters++;
                        mView.nextChapter(mPreloadChapters, mLoadingPosition);
                        mView.setTitle(mComicChapterTitle.get(mComicChapters) + "-" + (1 + mLoadingPosition) +
                                "/" + mPreloadChapters.getNowlist().size());
                    }

                    @Override
                    public void onError(Throwable t) {
                        isLoadingdata = false;
                        mView.showErrorView(ShowErrorTextUtil.ShowErrorText(t));
                    }

                    @Override
                    public void onComplete() {
                        isLoadingdata = false;
                        mView.getDataFinish();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void getPreChapterData(String id, int chapter, int direction) {
        DisposableSubscriber<DBChapters> disposable = mDataSource.loadPreData(id, chapter, direction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<DBChapters>() {
                    @Override
                    public void onNext(DBChapters chapters) {
                        if (!isLoadingdata){
                            return;
                        }
                        mPreloadChapters.setNextlist(mPreloadChapters.getNowlist());
                        mPreloadChapters.setNowlist(mPreloadChapters.getPrelist());
                        if (chapters.getComiclist().size() == 1) {
                            mPreloadChapters.setPrelist(new ArrayList<String>());
                        } else {
                            mPreloadChapters.setPrelist(chapters.getComiclist());
                        }
                        mComicChapters--;
                        mView.preChapter(mPreloadChapters, mLoadingPosition);
                        mView.setTitle(mComicChapterTitle.get(mComicChapters) + "-" + (mPreloadChapters.getNowlist().size()
                                + mLoadingPosition) + "/" + mPreloadChapters.getNowlist().size());
                    }

                    @Override
                    public void onError(Throwable t) {
                        isLoadingdata = false;
                        mView.showErrorView(ShowErrorTextUtil.ShowErrorText(t));
                    }

                    @Override
                    public void onComplete() {
                        isLoadingdata = false;
                        mView.getDataFinish();
                    }
                });
        mCompositeDisposable.add(disposable);
    }


    public void clickScreen(float x, float y) {
        if (x < 0.336) {
            mView.prePage();
        } else if (x < 0.666) {
            if (isLoadingdata) {
                mView.showToast("正在加载数据，请稍候");
            } else {
                mView.showMenu();

            }
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
