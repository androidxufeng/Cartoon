/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.view;

public interface IDownloadView<T> extends ILoadDataView<T> {

    void onLoadMoreData(T t);

    void onStartAll();

    void onPauseAll();

    void onSelectALL();

    void onDeleteAll();
}
