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

import com.tplink.cartoon.ui.activity.IndexActivity;
import com.tplink.cartoon.ui.source.Index.IndexDataSource;

public class IndexPresenter extends BasePresenter<IndexDataSource, IndexActivity> {
    public IndexPresenter(IndexDataSource dataSource, IndexActivity view) {
        super(dataSource, view);
    }
}
