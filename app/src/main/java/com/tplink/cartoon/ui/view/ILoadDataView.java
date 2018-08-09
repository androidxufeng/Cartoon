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
 * Ver 1.0, 18-8-9, xufeng, Create file
 */

public interface ILoadDataView<T> extends BaseView {

    void showErrorView(String throwable);

    void fillData(T data);

    void getDataFinish();
}
