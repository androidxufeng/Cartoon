/* Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;


import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.source.IMainDataSource;
import com.tplink.cartoon.ui.view.IHomeView;
import com.tplink.cartoon.utils.ShowErrorTextUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class HomePresenter extends BasePresenter<IMainDataSource, IHomeView> {

    private final CompositeDisposable mDisposable;

    public HomePresenter(IMainDataSource dataSource, IHomeView view) {
        super(dataSource, view);
        mDisposable = new CompositeDisposable();
    }

    public void subscribe(){

    }

    public void loadData() {
        DisposableSubscriber<List<Comic>> disposable = mDataSource.loadData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        mView.fillData(comics);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.showToast("获取数据失败" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.getDataFinish();
                    }
                });
        mDisposable.add(disposable);
    }

    public void loadMoreData(int page) {

        DisposableSubscriber<List<Comic>> disposable = mDataSource.loadMoreData(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        mView.appendMoreDataToView(comics);
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
        mDisposable.add(disposable);
    }

    public void unSubscribe() {
        mDisposable.dispose();
    }
}
