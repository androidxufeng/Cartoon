/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.net.cache;

import com.tplink.cartoon.utils.FileUtil;

import java.io.File;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

public class CacheProviders {
    private static ComicCacheProviders comicCacheProviders;

    public synchronized static ComicCacheProviders getComicCacheInstance() {
        if (comicCacheProviders == null) {
            File cacheDirectory = new File(FileUtil.SDPATH + FileUtil.CACHE);
            if (!cacheDirectory.exists()) {
                FileUtil.createDir(cacheDirectory.getAbsolutePath());
            }
            comicCacheProviders = new RxCache.Builder()
                    .persistence(cacheDirectory, new GsonSpeaker())//缓存文件的配置、数据的解析配置
                    .using(ComicCacheProviders.class);//这些配置对应的缓存接口
        }
        return comicCacheProviders;
    }
}
