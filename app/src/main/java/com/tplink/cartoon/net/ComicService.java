/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-30, xufeng, Create file
 */
package com.tplink.cartoon.net;

import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.data.bean.HttpResult;
import com.tplink.cartoon.data.bean.SearchBean;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ComicService {

    @GET("getChapterList/{id}/{chapter}")
    Flowable<DBChapters> getChapters(@Path("id") long id, @Path("chapter") int chapter);

    @GET
    Flowable<HttpResult<List<SearchBean>>> getDynamicSearchResult(@Url String url);

    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);

}
