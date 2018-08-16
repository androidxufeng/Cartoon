package com.tplink.cartoon.ui.source.chapter;
/*
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
import com.tplink.cartoon.ui.source.IDataSource;

import io.reactivex.Flowable;

public interface IChapterDataSource extends IDataSource {

    Flowable<DBChapters> getChapterData(long id, int chapter);

    Flowable<DBChapters> loadNextData(long id, int chapter, int direction);

    Flowable<DBChapters> loadPreData(long id, int chapter, int direction);

    Flowable<Boolean> updateComicCurrentChapter(long comicId, int currentChapter);
}
