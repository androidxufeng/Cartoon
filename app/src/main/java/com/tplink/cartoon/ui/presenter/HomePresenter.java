/* Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;


import android.test.mock.MockContext;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.fragment.HomeFragment;
import com.tplink.cartoon.ui.source.HomeDataSource;
import com.tplink.cartoon.ui.source.IHomeDataSource;
import com.tplink.cartoon.utils.IntentUtil;
import com.tplink.cartoon.utils.LogUtil;
import com.tplink.cartoon.utils.ShowErrorTextUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class HomePresenter extends BasePresenter<IHomeDataSource, HomeFragment> {

    private static final String TAG = "HomePresenter";
    private final CompositeDisposable mDisposable;

    private List<Comic> mDatas, mBanners;
    private Comic recentComic;

    public List<Comic> getBanners() {
        return mBanners;
    }

    public List<Comic> getDatas() {
        return mDatas;
    }

    public HomePresenter(HomeFragment view) {
        super(view);
        mDisposable = new CompositeDisposable();
        mDatas = new ArrayList<>();
        mBanners = new ArrayList<>();
    }

    @Override
    protected IHomeDataSource initDataSource() {
        return new HomeDataSource(mView.getContext());
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

    public void toRecentComic() {
        IntentUtil.toComicChapter(mView.getContext(), recentComic.getCurrentChapter(), recentComic);
    }

    public void getRecent() {
        DisposableSubscriber<Comic> disposableSubscriber = mDataSource.findRecentlyComic()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Comic>() {
                    @Override
                    public void onNext(Comic comic) {
                        recentComic = comic;
                        if (recentComic != null) {
                            String recent = "续看:" + recentComic.getTitle() + " 第" + (recentComic.getCurrentChapter() + 1) + "话";
                            mView.fillRecent(recent);
                        } else {
                            mView.fillRecent(null);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.d(TAG, t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposable.add(disposableSubscriber);

    }

}
