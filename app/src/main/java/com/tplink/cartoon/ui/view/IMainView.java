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
 * Ver 1.0, 18-7-30, xufeng, Create file
 */

import com.tplink.cartoon.data.bean.BaseBean;

import java.util.List;

public interface IMainView<T extends BaseBean> extends BaseView {
    void getDataFinish();

    void showEmptyView();

    void showErrorView(Throwable throwable);

    void showRefresh();

    void hideRefresh();

    /**
     * 填充数据
     */
    void fillData(List<T> data);

    /**
     * 添加更多数据（用于刷新）
     *
     * @param data
     */
    void appendMoreDataToView(List<T> data);

    /**
     * 没有更多
     */
    void hasNoMoreData();

}
