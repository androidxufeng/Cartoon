/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-31, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.category;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.source.IDataSource;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public interface ICategoryDataSource extends IDataSource {

    Flowable<List<Comic>> getCategroyList(Map<String, Integer> selectMap, int page);

}
