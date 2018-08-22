/* Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;


import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.fragment.HomeFragment;
import com.tplink.cartoon.ui.source.IHomeDataSource;
import com.tplink.cartoon.utils.ShowErrorTextUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class HomePresenter extends BasePresenter<IHomeDataSource, HomeFragment> {

    private final CompositeDisposable mDisposable;

    private List<Comic> mDatas, mBanners;

    public List<Comic> getBanners() {
        return mBanners;
    }

    public List<Comic> getDatas() {
        return mDatas;
    }

    public HomePresenter(IHomeDataSource dataSource, HomeFragment view) {
        super(dataSource, view);
        mDisposable = new CompositeDisposable();
        mDatas = new ArrayList<>();
        mBanners = new ArrayList<>();
    }

    public void loadData() {
        DisposableSubscriber<List<Comic>> disposable = mDataSource.loadData()
                .compose(mView.<List<Comic>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                    @Override
                    public void onNext(List<Comic> comics) {
                        if (comics.size() > 12) {
                            mView.fillData(comics);
                            for (int i = 1; i < 5; i++) {
                                mBanners.add(comics.get(i));
                            }
                            mView.fillBanner(mBanners);
                            mDatas = comics;
                        } else {
                            mView.fillBanner(comics);
                            mBanners = comics;
                        }
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

    public void refreshData() {
        loadData();
    }

    public void loadMoreData(int page) {

        DisposableSubscriber<List<Comic>> disposable = mDataSource.loadMoreData(page)
                .compose(mView.<List<Comic>>bindUntilEvent(FragmentEvent.DESTROY))
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

}
