package com.tplink.cartoon.ui.source.detail;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-1, xufeng, Create file
 */

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.source.IDataSource;

import io.reactivex.Flowable;

public interface IDetailDataSource extends IDataSource{

    Flowable<Comic> getDetail(long comicId);

    Flowable<Boolean> collectComic(Comic comic);

    Flowable<Boolean> isCollect(long id);
}
