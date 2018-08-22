/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.download;

import com.tplink.cartoon.net.RetrofitClient;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class DownloadListDataSource implements IDownloadListDataSource {

    public Flowable<Response<ResponseBody>> downloadFile(String url) {
        return RetrofitClient.getInstance()
                .getComicService()
                .downloadFile(url);
    }
}
