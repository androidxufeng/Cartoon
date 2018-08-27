/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.download;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.ui.source.IDataSource;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface IDownloadListDataSource extends IDataSource {

    Flowable<DBChapters> getDownloadChaptersList(long id, int comicChapters);

    Flowable<List<DBDownloadItem>> getDbDownloadItemFromDB(long comicId);

    Flowable<List<DBDownloadItem>> getDbDownloadItemFromDBWithInsert(Comic comic, HashMap<Integer, Integer> mMap);

    Observable<ResponseBody> download(DBDownloadItem info, final int page);

    Flowable<Boolean> updateDownloadItemsList(List<DBDownloadItem> lists);

}
