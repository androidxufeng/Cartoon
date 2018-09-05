/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-31, xufeng, Create file
 */
package com.tplink.cartoon.ui.view;

import com.tplink.cartoon.data.bean.Type;

import java.util.List;
import java.util.Map;

public interface ICategoryView<T> extends ILoadDataView<T> {
    void fillSelectData(List<Type> list, Map<String, Integer> map);

    void setMap(Map<String, Integer> map);
}
