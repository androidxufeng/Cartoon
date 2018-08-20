/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.view;

import java.util.List;

public interface ISearchView<T> extends ILoadDataView<T> {
    void clearText();

    //动态搜索
    void fillDynamicResult(T t);

    //搜索结果
    void fillResult(T t);

    void fillHotRank(List<String> ranks);
}
