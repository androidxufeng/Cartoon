/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-27, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.fragment.BaseFragment;
import com.tplink.cartoon.ui.fragment.bookshelf.CollectionFragment;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;
import com.tplink.cartoon.ui.view.ICollectionView;
import com.tplink.cartoon.utils.ShowErrorTextUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class CollectionPresenter extends BasePresenter<BookShelfDataSource, ICollectionView> {
    private final CompositeDisposable mCompositeDisposable;

    public CollectionPresenter(BookShelfDataSource dataSource, ICollectionView view) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
    }

    public void loadCollectComic() {
        DisposableSubscriber<List<Comic>> disposable = mDataSource.getCollectedComicList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(((CollectionFragment)mView).<List<Comic>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        mView.fillData(comics);
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
