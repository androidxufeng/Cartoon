/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.net.cache;

import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.data.bean.HttpResult;
import com.tplink.cartoon.data.bean.SearchBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.LifeCache;

public interface ComicCacheProviders {

    @LifeCache(duration = 1, timeUnit = TimeUnit.MINUTES)
    Flowable<DBChapters> getChapters(
            Flowable<DBChapters> comics, DynamicKey key, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 1, timeUnit = TimeUnit.MINUTES)
    Flowable<HttpResult<List<SearchBean>>> getDynamicSearchResult(
            Flowable<HttpResult<List<SearchBean>>> Result, DynamicKey key, EvictDynamicKey evictDynamicKey);
}
