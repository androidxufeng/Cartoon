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

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.fragment.bookshelf.DownloadFragment;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;
import com.tplink.cartoon.ui.view.ICollectionView;
import com.tplink.cartoon.utils.ShowErrorTextUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class DownloadPresenter extends SelectPresenter<BookShelfDataSource, ICollectionView> {

    private final CompositeDisposable mCompositeDisposable;

    public DownloadPresenter(BookShelfDataSource dataSource, ICollectionView view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
    }

    public void getDownloadList() {
        DisposableSubscriber<List<Comic>> disposableSubscriber = mDataSource.getDownloadComicList()
                .compose(((DownloadFragment) mView).<List<Comic>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        mComics = comics;
                        if (comics.size() > 0) {
                            mView.fillData(comics);
                            resetSelect();
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

    public void deleteDownloadComic() {
        List<Comic> deleteComics = new ArrayList<>();
        for (int i = 0; i < mComics.size(); i++) {
            if (mMap.get(i) == Constants.CHAPTER_SELECTED) {
                deleteComics.add(mComics.get(i));
            }
        }
        DisposableSubscriber<List<Comic>> disposableSubscriber = mDataSource.deleteDownloadComicList(deleteComics)
                .compose(((DownloadFragment) mView).<List<Comic>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
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

    @Override
    protected Context getContext() {
        return ((DownloadFragment) mView).getContext();
    }

    @Override
    protected void deleteComic() {
        deleteDownloadComic();
    }
}
