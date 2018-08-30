/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.Mine;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.tplink.cartoon.utils.FileUtil;
import com.tplink.cartoon.utils.GlideCacheUtil;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class MineDataSource implements IMineDataSource {
    private Context mContext;

    public MineDataSource(Context context) {

        mContext = context;
    }

    @Override
    public Flowable<String> clearCache() {
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                //首先清除API缓存
                FileUtil.deleteDir(FileUtil.SDPATH + FileUtil.CACHE);
                GlideCacheUtil.getInstance().clearImageAllCache(mContext);
                emitter.onNext(GlideCacheUtil.getInstance().getCacheSize(mContext));
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }
}
