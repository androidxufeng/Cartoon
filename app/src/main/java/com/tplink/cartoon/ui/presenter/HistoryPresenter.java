/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-27, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.HomeTitle;
import com.tplink.cartoon.data.bean.LoadingItem;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.fragment.bookshelf.HistoryFragment;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;
import com.tplink.cartoon.ui.view.ICollectionView;
import com.tplink.cartoon.utils.ShowErrorTextUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class HistoryPresenter extends SelectPresenter<BookShelfDataSource, ICollectionView> {

    private final CompositeDisposable mCompositeDisposable;

    int page = 1;
    private boolean isloadingdata;
    private List<Comic> mHistoryList;

    public HistoryPresenter(ICollectionView view) {
        super(view);
        mCompositeDisposable = new CompositeDisposable();
        mHistoryList = new ArrayList<>();
    }

    @Override
    protected BookShelfDataSource initDataSource() {
        return new BookShelfDataSource(getContext());
    }

    public void getHistoryList() {
        page = 1;
        DisposableSubscriber<List<Comic>> disposableSubscriber = mDataSource.getHistoryComicList(0)
                .compose(((HistoryFragment) mView).<List<Comic>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        mComics.clear();
                        mComics.addAll(comics);
                        if (mComics.size() != 0) {
                            mView.fillData(addTitle(comics));
                        } else {
                            mView.showEmptyView();
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
        mCompositeDisposable.add(disposableSubscriber);
    }

    public void deleteHistoryComic() {
        List<Comic> deleteComics = new ArrayList<>();
        for (int i = 0; i < mHistoryList.size(); i++) {
            if (mMap.get(i) == Constants.CHAPTER_SELECTED) {
                deleteComics.add(mHistoryList.get(i));
            }
        }
        Flowable<List<Comic>> deleteFlowable;
        if (isSelectedAll) {
            deleteFlowable = mDataSource.deleteAllHistoryComicList();
        } else {
            deleteFlowable = mDataSource.deleteCollectComicList(deleteComics);
        }
        DisposableSubscriber<List<Comic>> disposableSubscriber = deleteFlowable
                .compose(((HistoryFragment) mView).<List<Comic>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        page = 1;
                        clearSelect();
                        mComics.clear();
                        mComics.addAll(comics);
                        if (comics.size() > 0) {
                            mView.fillData(comics);
                        } else {
                            mView.showEmptyView();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        mView.quitEdit();
                    }
                });
        mCompositeDisposable.add(disposableSubscriber);
    }

    public void loadMoreData() {
        if (!isloadingdata) {
            isloadingdata = true;
            DisposableSubscriber<List<Comic>> disposableSubscriber = mDataSource.getHistoryComicList(page)
                    .compose(((HistoryFragment) mView).<List<Comic>>bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                        @Override
                        public void onNext(List<Comic> comics) {
                            page++;
                            mComics.addAll(comics);
                            mView.fillData(addTitle(comics));
                            isloadingdata = false;
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
            mCompositeDisposable.add(disposableSubscriber);
        }
    }

    private List<Comic> addTitle(List<Comic> comics) {
        List<Comic> todays = new ArrayList<>();
        List<Comic> treedays = new ArrayList<>();
        List<Comic> weekenddays = new ArrayList<>();
        List<Comic> earlierdays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Long currentMillisecond = calendar.getTimeInMillis();
        for (int i = 0; i < mComics.size(); i++) {
            long millisecond = mComics.get(i).getClickTime();
            //间隔秒
            Long spaceSecond = (currentMillisecond - millisecond) / 1000;
            Log.d("ceshi", "addTitle: period = " + spaceSecond);
            if (spaceSecond / (60 * 60) < 24) {
                todays.add(mComics.get(i));
            } else if (spaceSecond / (60 * 60 * 24) < 3) {
                treedays.add(mComics.get(i));
            } else if (spaceSecond / (60 * 60 * 24) < 7) {
                weekenddays.add(mComics.get(i));
            } else {
                earlierdays.add(mComics.get(i));
            }
        }
        List<Comic> history = new ArrayList<>();
        if (todays.size() != 0) {
            history.add(new HomeTitle("今天"));
            history.addAll(todays);
        }
        if (treedays.size() != 0) {
            history.add(new HomeTitle("过去三天"));
            history.addAll(treedays);
        }
        if (weekenddays.size() != 0) {
            history.add(new HomeTitle("过去一周"));
            history.addAll(weekenddays);
        }
        if (earlierdays.size() != 0) {
            history.add(new HomeTitle("更早"));
            history.addAll(earlierdays);
        }
        if (mComics.size() >= 12) {
            if (comics.size() == 0) {
                history.add(new LoadingItem(false));
            } else {
                history.add(new LoadingItem(true));
            }
        }
        mHistoryList = history;
        resetSelect();
        return history;
    }

    public void selectOrMoveAll() {
        if (!isSelectedAll) {
            if (mHistoryList != null && mHistoryList.size() != 0) {
                for (int i = 0; i < mHistoryList.size(); i++) {
                    if (mMap.get(i) == Constants.CHAPTER_FREE) {
                        mMap.put(i, Constants.CHAPTER_SELECTED);
                        mSelectedNum++;
                    }
                }
                mView.addAll();
            }
        } else {
            if (mHistoryList != null && mHistoryList.size() != 0) {
                for (int i = 0; i < mHistoryList.size(); i++) {
                    if (mMap.get(i) == Constants.CHAPTER_SELECTED) {
                        mMap.put(i, Constants.CHAPTER_FREE);
                    }
                }
                mSelectedNum = 0;
                mView.removeAll();
            }
        }
        isSelectedAll = !isSelectedAll;
        mView.updateList(mMap);
    }

    public void updateToSelected(int position) {
        if (mMap.get(position) != null && mMap.get(position).equals(Constants.CHAPTER_FREE)) {
            mSelectedNum++;
            mMap.put(position, Constants.CHAPTER_SELECTED);
            if (mSelectedNum == mComics.size()) {
                mView.addAll();
                isSelectedAll = true;
            }
        } else if (mMap.get(position) != null && mMap.get(position).equals(Constants.CHAPTER_SELECTED)) {
            mMap.put(position, Constants.CHAPTER_FREE);
            mSelectedNum--;
            isSelectedAll = false;
            mView.removeAll();
        }
        mView.updateListItem(mMap, position);
    }

    /**
     * 重置选择信息
     */
    public void resetSelect() {
        for (int i = 0; i < mHistoryList.size(); i++) {
            if (!mMap.containsKey(i)) {
                if (isSelectedAll) {
                    mMap.put(i, Constants.CHAPTER_SELECTED);
                } else {
                    mMap.put(i, Constants.CHAPTER_FREE);
                }
            }
        }
    }

    public void clearSelect() {
        mSelectedNum = 0;
        isSelectedAll = false;
        for (int i = 0; i < mHistoryList.size(); i++) {
            mMap.put(i, Constants.CHAPTER_FREE);
        }
        mView.updateList(mMap);
    }

    @Override
    protected Context getContext() {
        return ((HistoryFragment) mView).getContext();
    }

    @Override
    protected void deleteComic() {
        deleteHistoryComic();
    }
}
