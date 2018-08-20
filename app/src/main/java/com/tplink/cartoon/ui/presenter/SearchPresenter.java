/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.util.Log;

import com.tplink.cartoon.data.bean.SearchResult;
import com.tplink.cartoon.ui.activity.SearchActivity;
import com.tplink.cartoon.ui.source.search.ISearchDataSource;
import com.tplink.cartoon.utils.ShowErrorTextUtil;

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
                            Log.d("zhhr1122", searchResult.toString());
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
}
