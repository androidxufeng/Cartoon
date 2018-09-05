/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-9-5, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.newlist;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.source.IDataSource;

import java.util.List;

import io.reactivex.Flowable;

public interface INewListDataSource extends IDataSource{

    Flowable<List<Comic>> getNewComicList(int page);
}
