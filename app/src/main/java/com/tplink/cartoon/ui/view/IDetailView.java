package com.tplink.cartoon.ui.view;
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

public interface IDetailView<T> extends BaseView {
    void getDataFinish();

    void showErrorView(Throwable throwable);

    void fillData(T data);
}
