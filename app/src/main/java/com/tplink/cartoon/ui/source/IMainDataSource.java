package com.tplink.cartoon.ui.source;
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

import com.tplink.cartoon.data.bean.Comic;

import java.util.List;

import io.reactivex.Flowable;

public interface IMainDataSource extends IDataSource {
    Flowable<List<Comic>> loadData();
    Flowable<List<Comic>> loadMoreData(int page);
}
