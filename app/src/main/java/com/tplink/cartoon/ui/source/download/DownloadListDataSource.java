/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.download;

import android.content.Context;

import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.bean.DownInfo;
import com.tplink.cartoon.db.DaoHelper;
import com.tplink.cartoon.net.RetrofitClient;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class DownloadListDataSource implements IDownloadListDataSource {


    private final DaoHelper<DownInfo> mHelper;

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
    public Flowable<List<DownInfo>> getDownInfoFromDB(final long comicId) {
        return Flowable.create(new FlowableOnSubscribe<List<DownInfo>>() {
            @Override
            public void subscribe(FlowableEmitter<List<DownInfo>> emitter) throws Exception {
                List<DownInfo> downInfos = mHelper.queryDownInfo(comicId);
                emitter.onNext(downInfos);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
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

    public Observable<ResponseBody> download(String url) {
        return RetrofitClient.getInstance()
                .getComicService()
                .download("bytes=0" + "-", url);
    }

    public boolean insert(DownInfo info) {
        return mHelper.insert(info);
    }
}
