/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.util.Log;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.SearchBean;
import com.tplink.cartoon.net.HttpResultFunc;
import com.tplink.cartoon.ui.activity.SearchActivity;
import com.tplink.cartoon.ui.source.search.SearchDataSource;
import com.tplink.cartoon.utils.ShowErrorTextUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SearchPresenter extends BasePresenter<SearchDataSource, SearchActivity> {

    private boolean isDynamicLoading;
    private final CompositeDisposable mCompositeDisposable;
    private List<Comic> mHistroys;

    public SearchPresenter(SearchActivity view) {
        super(view);
        mCompositeDisposable = new CompositeDisposable();
        mHistroys = new ArrayList<>();
    }

    @Override
    protected SearchDataSource initDataSource() {
        return new SearchDataSource(mView);
    }

    public void getDynamicResult(String title) {
        HttpResultFunc<List<SearchBean>> listHttpResultFunc = new HttpResultFunc<>();
        if (!isDynamicLoading) {
            DisposableSubscriber<List<SearchBean>> disposable = mDataSource.getDynamicResult(title)
                    .map(listHttpResultFunc)
                    .compose(mView.<List<SearchBean>>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<List<SearchBean>>() {
                        @Override
                        public void onNext(List<SearchBean> searchBeen) {
                            isDynamicLoading = false;
                            if (searchBeen != null && searchBeen.size() != 0) {
                                mView.fillDynamicResult(mDataSource.transDynamicSearchToComic(searchBeen));
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            mView.showErrorView(ShowErrorTextUtil.ShowErrorText(t));
                            isDynamicLoading = false;
                        }

                        @Override
                        public void onComplete() {
                            mView.getDataFinish();
                            isDynamicLoading = false;
                        }
                    });

            isDynamicLoading = true;
            mCompositeDisposable.add(disposable);
        }
    }

    public void getSearchResult() {
        final String title = mView.getSearchText();

        if (title != null) {
            DisposableSubscriber<List<Comic>> disposable = mDataSource.getSearchResult(title)
                    .compose(mView.<List<Comic>>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                        @Override
                        public void onNext(List<Comic> comics) {
                            mView.fillResult(comics);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.d("ceshi", "throwable=" + t.toString());
                            mView.showErrorView(title);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            mCompositeDisposable.add(disposable);
            updateSearchResultToDB(title);
        }
    }

    public void getSearchResult(String title) {
        mView.setSearchText(title);
        getSearchResult();
    }

    public void getSearch() {
        DisposableSubscriber<List<Comic>> disposable = mDataSource.getTopResult()
                .compose(mView.<List<Comic>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        if (comics != null && comics.size() != 0) {
                            mView.fillTopSearch(comics);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("ceshi", " getTop throwable=" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);
        getHistorySearch();
    }

    public void updateSearchResultToDB(final String title) {
        DisposableSubscriber<Boolean> disposable = mDataSource.updateSearchResultToDB(title)
                .compose(mView.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        Comic comic = new Comic();
                        comic.setTitle(title);
                        mHistroys.add(0, comic);
                        mView.fillData(mHistroys);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.showErrorView(title);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void getHistorySearch() {
        DisposableSubscriber<List<Comic>> disposable = mDataSource.getHistorySearch()
                .compose(mView.<List<Comic>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        Log.d("ceshi", "onNext: size = " + comics.size());
                        mView.fillData(comics);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("ceshi", " getHistory onError: " + t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void clearHistory() {
        DisposableSubscriber<Boolean> disposable = mDataSource.clearSearchHistory()
                .compose(mView.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mView.fillData(null);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
