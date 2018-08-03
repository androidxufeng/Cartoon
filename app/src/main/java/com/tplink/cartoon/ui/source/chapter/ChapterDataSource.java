package com.tplink.cartoon.ui.source.chapter;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-2, xufeng, Create file
 */

import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.net.RetrofitClient;

import io.reactivex.Flowable;

public class ChapterDataSource implements IChapterDataSource {
    @Override
    public Flowable<DBChapters> getChapterData(String id, int chapter) {
        return RetrofitClient.getInstance()
                .getComicService()
                .getChapters(id, chapter);
    }

    @Override
    public Flowable<DBChapters> loadNextData(String id, int chapter, int direction) {
        return RetrofitClient.getInstance()
                .getComicService()
                .getChapters(id, chapter + 2);
    }

    @Override
    public Flowable<DBChapters> loadPreData(String id, int chapter, int direction) {
        return RetrofitClient.getInstance()
                .getComicService()
                .getChapters(id, chapter - 2);
    }

}
