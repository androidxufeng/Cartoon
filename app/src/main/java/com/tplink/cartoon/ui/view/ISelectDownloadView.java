/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-16, xufeng, Create file
 */
package com.tplink.cartoon.ui.view;

import java.util.HashMap;

public interface ISelectDownloadView<T> extends ILoadDataView<T> {

    void startDownload();

    void addToDownloadList(HashMap map);

    void addAll();

    void removeAll();
}
