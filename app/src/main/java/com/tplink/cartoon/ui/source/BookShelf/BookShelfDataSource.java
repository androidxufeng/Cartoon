/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-15, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.BookShelf;

import android.content.Context;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.db.DaoHelper;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class BookShelfDataSource implements IBookShelfDataSource {

    private final DaoHelper<Comic> mDaoHelper;

    public BookShelfDataSource(Context context) {
        mDaoHelper = new DaoHelper<>(context);
    }


    @Override
    public Flowable<List<Comic>> getCollectedComicList() {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                List<Comic> comics = mDaoHelper.listComicAll();
                emitter.onNext(comics);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }


}
