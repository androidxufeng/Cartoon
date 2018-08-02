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
 * Ver 1.0, 18-8-2, xufeng, Create file
 */

public interface IChapterView<T> extends BaseView {
    //获取数据完成
    void getDataFinish();
    //未获取到数据
    void showEmptyView();
    //展示错误页面
    void showErrorView(Throwable throwable);
    //填充数据
    void fillData(T data);
    //弹出菜单
    void showMenu();
    //下一章
    void nextChapter();
    //前一章
    void preChapter();
    //切换预览模式
    void SwitchModel(int a);
    //前一页
    void prePage();
    //下一页
    void nextPage();

    void setTitle(String name);
}
