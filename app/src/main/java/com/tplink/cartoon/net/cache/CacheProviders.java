/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.net.cache;

import android.os.Environment;

import java.io.File;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

public class CacheProviders {
    private static ComicCacheProviders comicCacheProviders;

    public synchronized static ComicCacheProviders getComicCache() {
        if (comicCacheProviders == null) {
            File cacheDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/cache");
            comicCacheProviders = new RxCache.Builder()
                    .persistence(cacheDirectory, new GsonSpeaker())//缓存文件的配置、数据的解析配置
                    .using(ComicCacheProviders.class);//这些配置对应的缓存接口
        }
        return comicCacheProviders;
    }
}
