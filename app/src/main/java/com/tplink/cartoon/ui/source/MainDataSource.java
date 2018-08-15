package com.tplink.cartoon.ui.source;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-30, xufeng, Create file
 */

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class MainDataSource implements IMainDataSource {
    @Override
    public Flowable<List<Comic>> loadData() {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                Document doc = Jsoup.connect(Constants.TENCENTTOPURL + "1").get();
                Document doc2 = Jsoup.connect(Constants.TENCENTTOPURL + "2").get();
                List<Comic> mdats = transToComic(doc);
                mdats.addAll(transToComic(doc2));
                emitter.onNext(mdats);

                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<Comic>> loadMoreData(final int page) {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                //使用java爬虫工具
                Document doc = Jsoup.connect(Constants.TENCENTTOPURL + page).get();
                List<Comic> mdats = transToComic(doc);
                emitter.onNext(mdats);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    private List<Comic> transToComic(Document doc) {
        List<Comic> mdats = new ArrayList<Comic>();
        List<Element> detail = doc.getElementsByAttributeValue("class", "ret-works-cover");
        List<Element> infos = doc.getElementsByAttributeValue("class", "ret-works-info");
        for (int i = 0; i < detail.size(); i++) {
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("data-original"));
            comic.setId(Long.parseLong(getID(infos.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    private static String getID(String splitID) {
        String[] ids = splitID.split("/");
        return ids[ids.length - 1];
    }

}
