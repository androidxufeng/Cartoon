/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-29, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.rank;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.source.IDataSource;

import java.util.List;

import io.reactivex.Flowable;

public interface IRankDataSource extends IDataSource{

    Flowable<List<Comic>> getRankList(long currentTime, String type, final int page);
}
