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
 * Ver 1.0, 18-7-30, xufeng, Create file
 */

import com.tplink.cartoon.ui.source.IDataSource;
import com.tplink.cartoon.ui.source.IMainDataSource;
import com.tplink.cartoon.ui.view.BaseView;

public class BasePresenter<M extends IDataSource ,V extends BaseView> {

    protected final M mDataSource;
    protected final V mView;

    public BasePresenter(M dataSource, V view) {

        mDataSource = dataSource;
        mView = view;
    }
}
