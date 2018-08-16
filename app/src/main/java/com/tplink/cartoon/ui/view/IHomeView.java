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

public interface IHomeView<T extends BaseBean> extends BaseView {
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

    void showErrorView(String throwable);

    void fillData(List<T> data);

    void getDataFinish();

    void fillBanner(List<T> data);

    void onRefreshFinish(List<T> data);

}
