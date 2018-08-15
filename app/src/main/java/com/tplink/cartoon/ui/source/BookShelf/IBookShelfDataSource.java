/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-15, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.BookShelf;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.source.IDataSource;

import java.util.List;

import io.reactivex.Flowable;

public interface IBookShelfDataSource extends IDataSource {

    Flowable<List<Comic>> getCollectedComicList();

}
