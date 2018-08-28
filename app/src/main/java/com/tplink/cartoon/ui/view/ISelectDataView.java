/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-27, xufeng, Create file
 */
package com.tplink.cartoon.ui.view;

import java.util.HashMap;

public interface ISelectDataView<T> extends ILoadDataView<T> {
    void updateList(HashMap map);

    void updateListItem(HashMap map, int position);

    void addAll();

    void removeAll();
}
