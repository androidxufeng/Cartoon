package com.tplink.cartoon.ui.presenter;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-9, xufeng, Create file
 */

import android.content.Intent;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.activity.IndexActivity;
import com.tplink.cartoon.ui.source.Index.IndexDataSource;

public class IndexPresenter extends BasePresenter<IndexDataSource, IndexActivity> {
    public Comic getComic() {
        return mComic;
    }

    private Comic mComic;

    public IndexPresenter(IndexDataSource context, IndexActivity view, Intent intent) {
        super(context, view);
        mComic = (Comic) intent.getSerializableExtra(Constants.COMIC);
    }
}
