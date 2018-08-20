/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.search;

import com.tplink.cartoon.data.bean.SearchResult;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.net.RetrofitClient;

import io.reactivex.Flowable;

public class SearchDataSource implements ISearchDataSource {

    @Override
    public Flowable<SearchResult> getDynamicResult(String title) {
        return RetrofitClient.getInstance()
                .getComicService()
                .getDynamicSearchResult(Constants.TENCENT_SEARCH_URL + title);
    }
}
