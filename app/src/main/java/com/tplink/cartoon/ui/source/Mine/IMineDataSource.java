/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.Mine;

import com.tplink.cartoon.ui.source.IDataSource;

import io.reactivex.Flowable;

public interface IMineDataSource extends IDataSource {

    Flowable<String> clearCache();
}
