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
import com.tplink.cartoon.data.bean.PreloadChapters;
import com.tplink.cartoon.net.RetrofitClient;

import io.reactivex.Flowable;

public class ChapterDataSource implements IChapterDataSource {
    @Override
    public Flowable<DBChapters> getChapterData(String id, int chapter) {
        return RetrofitClient.getInstance()
                .getComicService()
//                .getChapters(id, chapter);
                .getChapters(id, chapter);
    }

    public Flowable<DBChapters> loadMoreData(String id, int chapter, final int poistion, int direction) {
        int new_chapter;
        if (poistion == 0) {
            new_chapter = chapter - 1;
        } else {
            new_chapter = chapter + 1;
        }
        return RetrofitClient.getInstance()
                .getComicService().getChapters(id, new_chapter);

    }
}
