/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-27, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.util.Log;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.HomeTitle;
import com.tplink.cartoon.data.bean.LoadingItem;
import com.tplink.cartoon.ui.fragment.bookshelf.HistoryFragment;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;
import com.tplink.cartoon.ui.view.ICollectionView;
import com.tplink.cartoon.utils.ShowErrorTextUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class HistoryPresenter extends BasePresenter<BookShelfDataSource, ICollectionView> {

    private final CompositeDisposable mCompositeDisposable;

    private List<Comic> mLists;
    int page = 1;
    private boolean isloadingdata;

    public HistoryPresenter(BookShelfDataSource dataSource, ICollectionView view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
        mLists = new ArrayList<>();
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
                        mLists.clear();
                        mLists.addAll(comics);
                        mView.fillData(addTitle(comics));

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
                            mLists.addAll(comics);
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
        for (int i = 0; i < mLists.size(); i++) {
            long millisecond = mLists.get(i).getClickTime();
            //间隔秒
            Long spaceSecond = (currentMillisecond - millisecond) / 1000;
            Log.d("ceshi", "addTitle: period = " + spaceSecond);
            if (spaceSecond / (60 * 60) < 24) {
                todays.add(mLists.get(i));
            } else if (spaceSecond / (60 * 60 * 24) < 3) {
                treedays.add(mLists.get(i));
            } else if (spaceSecond / (60 * 60 * 24) < 7) {
                weekenddays.add(mLists.get(i));
            } else {
                earlierdays.add(mLists.get(i));
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
        if (mLists.size() >= 12) {
            if (comics.size() == 0) {
                history.add(new LoadingItem(false));
            } else {
                history.add(new LoadingItem(true));
            }
        }
        return history;
    }
}
