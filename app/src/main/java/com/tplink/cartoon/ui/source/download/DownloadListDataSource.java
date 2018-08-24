/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.download;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.bean.DownState;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.db.DaoHelper;
import com.tplink.cartoon.net.RetrofitClient;
import com.tplink.cartoon.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class DownloadListDataSource implements IDownloadListDataSource {


    private final DaoHelper<DBDownloadItem> mHelper;

    public DownloadListDataSource(Context context) {

        mHelper = new DaoHelper<>(context);
    }

    public Flowable<DBChapters> getDownloadChaptersList(final long id, final int comicChapters) {
        return RetrofitClient.getInstance()
                .getComicService()
                .getChapters(id, comicChapters);
       /* return CacheProviders.getComicCacheInstance()
                .getChapters(flowable, new DynamicKey(id + comicChapters), new EvictDynamicKey(false));*/
    }

    @Override
    public Flowable<List<DBDownloadItem>> getDbDownloadItemFromDB(final long comicId) {
        return Flowable.create(new FlowableOnSubscribe<List<DBDownloadItem>>() {
            @Override
            public void subscribe(FlowableEmitter<List<DBDownloadItem>> emitter) throws Exception {
                List<DBDownloadItem> dbDownloadItems = mHelper.queryDownloaditmes(comicId);
                emitter.onNext(dbDownloadItems);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<DBDownloadItem>> getDbDownloadItemFromDBWithInsert(final Comic comic, final HashMap<Integer, Integer> map) {
        return Flowable.create(new FlowableOnSubscribe<List<DBDownloadItem>>() {
            @Override
            public void subscribe(FlowableEmitter<List<DBDownloadItem>> emitter) throws Exception {
                DBDownloadItem item;
                //把hashmap進行排序操作
                List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
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
                        item.setId(comic.getId() + mapping.getKey());
                        item.setChapters_title(comic.getChapters().get(mapping.getKey()));
                        item.setComic_id(comic.getId());
                        item.setChapters(mapping.getKey());
                        item.setState(DownState.NONE);
                        try {
                            //把数据先存入数据库
                            mHelper.insert(item);
                        } catch (SQLiteConstraintException exception) {
                            LogUtil.e("插入下载列表失败，更新数据库");
                            mHelper.update(item);
                        }
                    }
                }
                List<DBDownloadItem> results = mHelper.queryDownloaditmes(comic.getId());
                emitter.onNext(results);
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Observable<ResponseBody> download(DBDownloadItem info, int page) {

        return RetrofitClient.getInstance()
                .getComicService()
                .download("bytes=0" + "-", info.getChaptersUrl().get(page));
    }

    @Override
    public Flowable<Boolean> updateDownloadItemsList(final List<DBDownloadItem> lists) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                boolean result = true;
                for (int i = 0; i < lists.size(); i++) {
                    DBDownloadItem items = lists.get(i);
                    if (items.getState() != DownState.FINISH) {
                        items.setState(DownState.NONE);
                        result = mHelper.update(items);
                    }
                }
                emitter.onNext(result);
            }
        }, BackpressureStrategy.LATEST);
    }
}
