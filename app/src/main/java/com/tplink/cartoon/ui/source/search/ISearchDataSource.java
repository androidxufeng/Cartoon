/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.search;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.HttpResult;
import com.tplink.cartoon.data.bean.SearchBean;
import com.tplink.cartoon.ui.source.IDataSource;

import java.util.List;

import io.reactivex.Flowable;

public interface ISearchDataSource extends IDataSource {

    Flowable<HttpResult<List<SearchBean>>> getDynamicResult(String title);

    Flowable<List<Comic>> getSearchResult(String title);

    Flowable<List<Comic>> getTopResult();

    Flowable<Boolean> updateSearchResultToDB(String title);

    Flowable<List<Comic>> getHistorySearch();

    Flowable<Boolean> clearSearchHistory();

}
