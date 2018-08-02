package com.tplink.cartoon.net;
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

import com.tplink.cartoon.data.bean.DBChapters;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ComicService {

    @GET("getChapterList/{id}/{chapter}")
    Flowable<DBChapters> getChapters(@Path("id") String id, @Path("chapter") String chapter);

}
