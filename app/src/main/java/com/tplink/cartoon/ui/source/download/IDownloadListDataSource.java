/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.download;

import com.tplink.cartoon.ui.source.IDataSource;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Response;

public interface IDownloadListDataSource extends IDataSource {

    Flowable<Response<ResponseBody>> downloadFile(String url);

}
