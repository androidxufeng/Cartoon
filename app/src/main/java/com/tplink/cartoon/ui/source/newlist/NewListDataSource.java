/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-9-5, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.newlist;

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

public class NewListDataSource implements INewListDataSource {
    @Override
    public Flowable<List<Comic>> getNewComicList(final int page) {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                Document doc = Jsoup.connect(Constants.TENCENT_NEWURL + page).get();
                List<Comic> mdats = transToNewListComic(doc);
                emitter.onNext(mdats);
            }
        }, BackpressureStrategy.LATEST);
    }

    //处理漫画列表
    public static List<Comic> transToNewListComic(Document doc) {
        List<Comic> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class", "ret-works-cover");
        List<Element> infos = doc.getElementsByAttributeValue("class", "ret-works-info");
        for (int i = 0; i < detail.size(); i++) {
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("data-original"));
            Element info = infos.get(i).getElementsByAttributeValue("class", "ret-works-tags").get(0);
            comic.setAuthor(infos.get(i).getElementsByAttributeValue("class", "ret-works-author").get(0).attr("title"));
            comic.setUpdates(info.select("em").text());
            comic.setDescribe(info.select("span").text());
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
