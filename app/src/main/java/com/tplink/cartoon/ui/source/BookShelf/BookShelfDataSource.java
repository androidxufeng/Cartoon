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
                List<Comic> comics = mDaoHelper.queryCollect();
                emitter.onNext(comics);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<Comic>> getHistoryComicList(final int page) {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                List<Comic> comics = mDaoHelper.queryHistory(page);
                emitter.onNext(comics);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<Comic>> getDownloadComicList() {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                List<Comic> comics = mDaoHelper.queryDownloadComic();
                emitter.onNext(comics);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<Comic>> deleteHistoryComicList(final List<Comic> list) {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                for (int i = 0; i < list.size(); i++) {
                    Comic items = list.get(i);
                    items.setClickTime(0);
                    items.setCurrent_page(0);
                    items.setCurrentChapter(0);
                    mDaoHelper.update(items);
                }
                List<Comic> comics = mDaoHelper.queryHistory(0);
                emitter.onNext(comics);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<Comic>> deleteAllHistoryComicList() {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                List<Comic> list = mDaoHelper.queryHistory();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setClickTime(0);
                    list.get(i).setCurrent_page(0);
                    list.get(i).setCurrentChapter(0);
                }
                mDaoHelper.insertList(list);
                List<Comic> mComics = mDaoHelper.queryHistory(0);
                emitter.onNext(mComics);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<Comic>> deleteDownloadComicList(final List<Comic> list) {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                for (int i = 0; i < list.size(); i++) {
                    Comic items = list.get(i);
                    items.setStateInte(0);
                    mDaoHelper.update(items);
                }
                List<Comic> comics = mDaoHelper.queryDownloadComic();
                emitter.onNext(comics);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<Comic>> deleteCollectComicList(final List<Comic> list) {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                for (int i = 0; i < list.size(); i++) {
                    Comic items = list.get(i);
                    items.setIsCollected(false);
                    mDaoHelper.update(items);
                }
                List<Comic> comics = mDaoHelper.queryCollect();
                emitter.onNext(comics);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }
}
