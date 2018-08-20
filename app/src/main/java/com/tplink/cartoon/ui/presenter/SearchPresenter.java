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
import com.tplink.cartoon.data.bean.SearchResult;
import com.tplink.cartoon.ui.activity.SearchActivity;
import com.tplink.cartoon.ui.source.search.ISearchDataSource;
import com.tplink.cartoon.utils.ShowErrorTextUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SearchPresenter extends BasePresenter<ISearchDataSource, SearchActivity> {

    private boolean isDynamicLoading;
    private SearchResult mDynamicResult;
    private final CompositeDisposable mCompositeDisposable;

    public SearchPresenter(ISearchDataSource dataSource, SearchActivity view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
    }

    public SearchResult getmDynamicResult() {
        return mDynamicResult;
    }

    public void getDynamicResult(String title) {
        if (!isDynamicLoading) {
            DisposableSubscriber<SearchResult> disposable = mDataSource.getDynamicResult(title)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<SearchResult>() {
                        @Override
                        public void onNext(SearchResult searchResult) {
                            isDynamicLoading = false;
                            if (searchResult.status == 2) {
                                mDynamicResult = searchResult;
                                mView.fillDynamicResult(searchResult);
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
        String title = mView.getSearchText();

        if (title != null) {
            DisposableSubscriber<List<Comic>> disposable = mDataSource.getSearchResult(title)
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
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            mCompositeDisposable.add(disposable);
        }
    }

    public void getTopSearch() {
        DisposableSubscriber<List<Comic>> disposable = mDataSource.getTopResult()
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
                        Log.d("ceshi", "throwable=" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);

    }
}
