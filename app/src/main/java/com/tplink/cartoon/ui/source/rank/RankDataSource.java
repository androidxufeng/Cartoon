package com.tplink.cartoon.ui.source.rank;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-29, xufeng, Create file
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

public class RankDataSource implements IRankDataSource {
    @Override
    public Flowable<List<Comic>> getRankList(final long currentTime, final String type, final int page) {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                Document doc = Jsoup.connect(Constants.TENCENTRANKURL + "t=" + currentTime
                        + "&type=" + type + "&page=" + page + "&pageSize=10&style=items").get();
                List<Comic> mdats = transToRank(doc);
                emitter.onNext(mdats);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    public static List<Comic> transToRank(Document doc) {
        List<Comic> mComic = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class", "comic-link");
        for (int i = 0; i < detail.size(); i++) {
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("strong").text());
            comic.setCover(detail.get(i).select("img").attr("src"));
            comic.setUpdates(detail.get(i).getElementsByAttributeValue("class", "comic-update").get(0).text());
            List<String> taglist = new ArrayList<>();
            try {
                Element ElementDescribe = detail.get(i).getElementsByAttributeValue("class", "comic-desc").get(0);
                comic.setDescribe(ElementDescribe.text());
                String stringtags = detail.get(i).getElementsByAttributeValue("class", "comic-tag").get(0).text();
                String tags[] = stringtags.split(" ");
                for (int j = 0; j < tags.length; j++) {
                    taglist.add(tags[j]);
                }
                comic.setTags(taglist);
            } catch (Exception e) {
            }
            comic.setId(Long.parseLong(getID(detail.get(i).attr("href"))));
            mComic.add(comic);
        }
        return mComic;
    }

    private static String getID(String splitID) {
        String[] ids = splitID.split("/");
        return ids[ids.length - 1];
    }
}
