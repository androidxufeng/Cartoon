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

    public int getDirect() {
        return mDirect;
    }

    public void setDirect(int direct) {
        mDirect = direct;
    }

    //漫画阅读方向
    private int mDirect;

    public int getComicChapters() {
        return mComicChapters;
    }

    private int mComicChapters;
    private List<String> mComicChapterTitle;
    private long mComicId;
    private int mComicSize;

    public List<String> getComicChapterTitle() {
        return mComicChapterTitle;
    }

    public long getComicId() {

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

    public void init(long comicId, List<String> comicChapterTitle, int Chapters, int type) {
        mComicId = comicId;
        this.mComicChapterTitle = comicChapterTitle;
        this.mComicChapters = Chapters;
        mDirect = type;
    }

    public PreloadChapters getPreloadChapters() {
        return mPreloadChapters;
    }

    public void setPreloadChapters(PreloadChapters preloadChapters) {
        this.mPreloadChapters = preloadChapters;
    }

    public void setReaderModuel(int direct) {
        mView.switchModel(direct);
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


    public void updateComicCurrentChapter() {
        DisposableSubscriber<Boolean> disposableSubscriber = mDataSource.updateComicCurrentChapter(mComicId, mComicChapters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mView.showToast("保存当前话成功" + (mComicChapters + 1));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {


                    }
                });
        mCompositeDisposable.add(disposableSubscriber);
    }

    public void loadMoreData(int position, int direct) {
        String chapterTitle = null;
        mDirect = direct;
        int nowPostion = 0;

        switch (direct) {
            case Constants.LEFT_TO_RIGHT:
                if (position < mPreloadChapters.getPreSize()) {
                    mLoadingPosition = position - mPreloadChapters.getPreSize() + 1;
                    chapterTitle = mComicChapterTitle.get(mComicChapters - 1);//前一章
                    mComicSize = mPreloadChapters.getPreSize();
                    nowPostion = position;
                    if (!isLoadingdata) {
                        isLoadingdata = true;
                        getPreChapterData(mComicId, mComicChapters, direct);
                    }
                } else if (position >= mPreloadChapters.getPreSize() + mPreloadChapters.getNowSize()) {//后一章
                    mLoadingPosition = position - mPreloadChapters.getPreSize() - mPreloadChapters.getNowSize();
                    mComicSize = mPreloadChapters.getNextSize();
                    chapterTitle = mComicChapterTitle.get(mComicChapters + 1);
                    nowPostion = position - mPreloadChapters.getPreSize() - mPreloadChapters.getNowSize();
                    if (!isLoadingdata) {
                        isLoadingdata = true;
                        getNextChapterData(mComicId, mComicChapters, direct);
                    }
                } else {//当前章节
                    isLoadingdata = false;
                    mComicSize = mPreloadChapters.getNowSize();
                    chapterTitle = mComicChapterTitle.get(mComicChapters);
                    nowPostion = position - mPreloadChapters.getPreSize();
                }
                break;
            case Constants.RIGHT_TO_LEFT:
                if (position < mPreloadChapters.getNextSize()) {
                    mLoadingPosition = position - mPreloadChapters.getNextSize() + 1;
                    chapterTitle = mComicChapterTitle.get(mComicChapters + 1);//后一章
                    mComicSize = mPreloadChapters.getNextSize();
                    nowPostion = position;
                    if (!isLoadingdata) {
                        isLoadingdata = true;
                        getNextChapterData(mComicId, mComicChapters, direct);
                    }
                } else if (position >= mPreloadChapters.getNextSize() + mPreloadChapters.getNowSize()) {//前一章
                    mLoadingPosition = position - mPreloadChapters.getNextSize() - mPreloadChapters.getNowSize();
                    mComicSize = mPreloadChapters.getPreSize();
                    chapterTitle = mComicChapterTitle.get(mComicChapters - 1);
                    nowPostion = position - mPreloadChapters.getNextSize() - mPreloadChapters.getNowSize();
                    if (!isLoadingdata) {
                        isLoadingdata = true;
                        getPreChapterData(mComicId, mComicChapters, direct);
                    }
                } else {//当前章节
                    isLoadingdata = false;
                    mComicSize = mPreloadChapters.getNowSize();
                    chapterTitle = mComicChapterTitle.get(mComicChapters);
                    nowPostion = position - mPreloadChapters.getNextSize();
                }
                break;
            default:
                break;
        }

        setTitle(chapterTitle, mComicSize, nowPostion, mDirect);
    }

    public void getNextChapterData(long id, int chapter, int direction) {
        DisposableSubscriber<DBChapters> disposable = mDataSource.loadNextData(id, chapter, direction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<DBChapters>() {
                    @Override
                    public void onNext(DBChapters chapters) {
                        if (!isLoadingdata) {
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
                        int position;
                        if (mDirect == Constants.RIGHT_TO_LEFT) {
                            position = mPreloadChapters.getNextlist().size() + mPreloadChapters.getNowlist().size()
                                    - 1 + mLoadingPosition;//关闭切换动画
                            mView.setTitle(mComicChapterTitle.get(mComicChapters) + "-" + (1 - mLoadingPosition) +
                                    "/" + mPreloadChapters.getNowlist().size());
                        } else {
                            position = mPreloadChapters.getPrelist().size() + mLoadingPosition;
                            mView.setTitle(mComicChapterTitle.get(mComicChapters) + "-" + (1 + mLoadingPosition) +
                                    "/" + mPreloadChapters.getNowlist().size());
                        }
                        mView.nextChapter(mPreloadChapters, position);

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

    public void getPreChapterData(long id, int chapter, int direction) {
        DisposableSubscriber<DBChapters> disposable = mDataSource.loadPreData(id, chapter, direction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<DBChapters>() {
                    @Override
                    public void onNext(DBChapters chapters) {
                        if (!isLoadingdata) {
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

                        int position = 0;
                        if (mDirect == Constants.RIGHT_TO_LEFT) {
                            position = mPreloadChapters.getNextlist().size() + mLoadingPosition;//关闭切换动画
                            mView.setTitle(mComicChapterTitle.get(mComicChapters) + "-" +
                                    (mPreloadChapters.getNowlist().size() - mLoadingPosition) + "/" + mPreloadChapters.getNowlist().size());
                        } else {
                            position = mPreloadChapters.getPrelist().size() + mPreloadChapters.getNowlist().size() + mLoadingPosition - 1;
                            mView.setTitle(mComicChapterTitle.get(mComicChapters) + "-" +
                                    (mPreloadChapters.getNowlist().size() + mLoadingPosition) + "/" + mPreloadChapters.getNowlist().size());
                        }
                        mView.preChapter(mPreloadChapters, position);

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

    public void setTitle(String comicChapterTitle, int comicSize, int position, int direct) {
        String title = null;
        if (direct == Constants.LEFT_TO_RIGHT) {
            title = comicChapterTitle + (position + 1) + "/" + comicSize;
        } else {
            title = comicChapterTitle + (comicSize - position) + "/" + comicSize;
        }
        mView.setTitle(title);
    }
}
