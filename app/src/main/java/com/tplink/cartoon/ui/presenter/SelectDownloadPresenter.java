/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-16, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.activity.SelectDownloadActivity;
import com.tplink.cartoon.ui.source.download.DownloadListDataSource;
import com.tplink.cartoon.ui.source.download.IDownloadListDataSource;
import com.tplink.cartoon.utils.LogUtil;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SelectDownloadPresenter extends BasePresenter<IDownloadListDataSource, SelectDownloadActivity> {


    private List<String> mChapters;
    private Comic mComic;
    private CompositeDisposable mCompositeDisposable;

    public HashMap<Integer, Integer> getMap() {
        return map;
    }

    private HashMap<Integer, Integer> map;

    private boolean isSelectAll;
    private int selectCount;

    public SelectDownloadPresenter(SelectDownloadActivity view, Comic comic) {
        super(view);
        this.mChapters = comic.getChapters();
        mCompositeDisposable = new CompositeDisposable();
        mComic = comic;
        initData();
    }

    private void initData() {

        map = new HashMap<>(4);
        if (mChapters != null && mChapters.size() != 0) {
            for (int i = 0; i < mChapters.size(); i++) {
                map.put(i, Constants.CHAPTER_FREE);
            }
        }
    }

    public void getDataFromDb() {
        //从数据库拉取数据
        DisposableSubscriber<List<DBDownloadItem>> disposableSubscriber =
                mDataSource.getDbDownloadItemFromDB(mComic.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<List<DBDownloadItem>>() {
                            @Override
                            public void onNext(List<DBDownloadItem> items) {
                                for (int i = 0; i < items.size(); i++) {
                                    map.put(items.get(i).getChapters(), Constants.CHAPTER_DOWNLOAD);
                                    mView.updateList(map);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                LogUtil.e(mComic.getTitle() + "从数据库中拉取本地下载列表数据失败" + t.toString());
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
        mCompositeDisposable.add(disposableSubscriber);
    }

    public void updateToSelected(int position) {
        Integer integer = map.get(position);
        if (integer != null && integer.equals(Constants.CHAPTER_FREE)) {
            map.put(position, Constants.CHAPTER_SELECTED);
            selectCount++;
            if (selectCount == mChapters.size()) {
                mView.addAll();
                isSelectAll = true;
            }
        } else if (integer != null && integer.equals(Constants.CHAPTER_SELECTED)) {
            map.put(position, Constants.CHAPTER_FREE);
            selectCount--;
            isSelectAll = false;
            mView.removeAll();
        }
        mView.updateList(map);
    }

    public void selectOrRemoveAll() {
        if (!isSelectAll) {
            if (mChapters != null && mChapters.size() != 0) {
                for (int i = 0; i < mChapters.size(); i++) {
                    if (map.get(i) == Constants.CHAPTER_FREE) {
                        map.put(i, Constants.CHAPTER_SELECTED);
                        selectCount++;
                    }
                }
                mView.addAll();
            }
        } else {
            if (mChapters != null && mChapters.size() != 0) {
                for (int i = 0; i < mChapters.size(); i++) {
                    if (map.get(i) == Constants.CHAPTER_SELECTED) {
                        map.put(i, Constants.CHAPTER_FREE);
                    }
                }
                selectCount = 0;
                mView.removeAll();
            }
        }
        isSelectAll = !isSelectAll;
        mView.updateList(map);
    }

    public int getSelectCount() {
        return selectCount;
    }

    @Override
    protected IDownloadListDataSource initDataSource() {
        return new DownloadListDataSource(mView);
    }
}
