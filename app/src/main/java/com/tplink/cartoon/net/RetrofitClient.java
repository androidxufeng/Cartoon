/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-30, xufeng, Create file
 */
package com.tplink.cartoon.net;

import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static final String BASE_URL = Constants.TENCENTCOMICCHAPTERS;

    private static final int DISCOLLECT_TIME = 5;
    private final Retrofit mRetrofit;
    private final ComicService mComicService;

    private static RetrofitClient sClient;

    private RetrofitClient() {
       /* OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DISCOLLECT_TIME, TimeUnit.SECONDS)
                .build();*/

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response orginalResponse = chain.proceed(chain.request());
                        return orginalResponse.newBuilder()
                                .body(new ProgressResponseBody(orginalResponse.body(), new ProgressListener() {
                                    @Override
                                    public void onProgress(long progress, long total, boolean done) {
                                        LogUtil.d( "onProgress: " + "total ---->" + total + "done ---->" + progress );
                                    }
                                }))
                                .build();
                    }
                })
                .connectTimeout(DISCOLLECT_TIME, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mComicService = mRetrofit.create(ComicService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (sClient == null) {
            sClient = new RetrofitClient();
        }
        return sClient;
    }

    public ComicService getComicService(){
        return mComicService;
    }
}
