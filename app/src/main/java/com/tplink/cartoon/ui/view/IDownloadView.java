/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.view;

import java.util.HashMap;

public interface IDownloadView<T> extends ILoadDataView<T> {

    void onLoadMoreData(T t);

    void updateView(int postion);

    void onDownloadFinished();

    void onPauseOrStartAll();

    void updateList(HashMap map);

    void updateListItem(HashMap map, int position);

    void addAll();

    void removeAll();

    void quitEdit();
}
