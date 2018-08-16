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

import android.content.Context;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.db.DaoHelper;
import com.tplink.cartoon.net.RetrofitClient;

import java.util.Date;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class ChapterDataSource implements IChapterDataSource {

    private final DaoHelper<Comic> mDaoHelper;

    public ChapterDataSource(Context context) {
        mDaoHelper = new DaoHelper<>(context);
    }

    @Override
    public Flowable<DBChapters> getChapterData(long id, int chapter) {
        return RetrofitClient.getInstance()
                .getComicService()
                .getChapters(id, chapter);
    }

    @Override
    public Flowable<DBChapters> loadNextData(long id, int chapter) {
        return RetrofitClient.getInstance()
                .getComicService()
                .getChapters(id, chapter + 2);
    }

    @Override
    public Flowable<DBChapters> loadPreData(long id, int chapter) {
        return RetrofitClient.getInstance()
                .getComicService()
                .getChapters(id, chapter - 2);
    }

    @Override
    public Flowable<Boolean> updateComicCurrentChapter(final long comicId, final int currentChapter) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                Comic comic = mDaoHelper.findComic(comicId);
                Date date = new Date();
                long datetime = date.getTime();
                if (comic != null) {
                    comic.setCurrentChapter(currentChapter + 1);
                    comic.setUpdateTime(datetime);
                    if (mDaoHelper.update(comic)) {
                        emitter.onNext(true);
                    } else {
                        emitter.onNext(false);
                    }
                } else {
                    emitter.onNext(false);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

}
