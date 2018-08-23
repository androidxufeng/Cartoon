/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-23, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.bean.DownInfo;
import com.tplink.cartoon.data.bean.DownState;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.db.DaoHelper;
import com.tplink.cartoon.ui.activity.DownloadChapterlistActivity;
import com.tplink.cartoon.ui.source.download.DownloadListDataSource;
import com.tplink.cartoon.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class DownloadChapterlistPresenter extends BasePresenter
        <DownloadListDataSource, DownloadChapterlistActivity> {
    private final CompositeDisposable mCompositeDisposable;
    private Comic mComic;
    private HashMap<Integer, Integer> mMap;
    private List<DBDownloadItem> mLists;
    private final DaoHelper<DBDownloadItem> mDaoHelper;

    public DownloadChapterlistPresenter(DownloadListDataSource dataSource, DownloadChapterlistActivity view, Intent intent) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
        mComic = (Comic) intent.getSerializableExtra(Constants.COMIC);
        mMap = (HashMap<Integer, Integer>) intent.getSerializableExtra(Constants.COMIC_SELECT_DOWNLOAD);
        mLists = new ArrayList<>();
        mDaoHelper = new DaoHelper<>(view.getApplicationContext());
    }


    /**
     * 初始化按照章節下載
     */
    public void initData() {
        DBDownloadItem item;
        //把hashmap進行排序操作
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(mMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1,
                               Map.Entry<Integer, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        //遍历map
        for (Map.Entry<Integer, Integer> mapping : list) {
            if (mapping.getValue() == Constants.CHAPTER_SELECTED) {
                item = new DBDownloadItem();
                item.setId(mComic.getId() + mapping.getKey());
                item.setChapters_title(mComic.getChapters().get(mapping.getKey()));
                item.setComic_id(mComic.getId());
                item.setChapters(mapping.getKey());
                item.setState(DownState.NONE);
                try {
                    //把数据先存入数据库
                    mDaoHelper.insert(item);
                } catch (SQLiteConstraintException exception) {
                    LogUtil.e("请不要插入重复值");
                }
            }
        }
        //从数据库拉取数据
        DisposableSubscriber<List<DBDownloadItem>> disposableSubscriber = mDataSource.getDbDownloadItemFromDB(mComic.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<DBDownloadItem>>() {
                    @Override
                    public void onNext(List<DBDownloadItem> dbDownloadItems) {
                        mLists.addAll(dbDownloadItems);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.e(mComic.getTitle() + "从数据库中拉取本地下载列表数据失败" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.fillData(mLists);
                    }
                });
        mCompositeDisposable.add(disposableSubscriber);
    }


    /**
     * 开始所有下载
     */
    public void startAll() {
    }

    /**
     * 暂停某个下载
     *
     * @param info
     */
    public void pause(DBDownloadItem info) {

    }

    /**
     * 开始某个下载
     *
     * @param info
     */
    public void startDown(final DBDownloadItem info) {
    }


    /**
     * 暂停所有下载
     */
    public void pauseAll() {
    }

}
