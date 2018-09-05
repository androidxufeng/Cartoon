/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-31, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.category;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class CategoryDataSource implements ICategoryDataSource {
    @Override
    public Flowable<List<Comic>> getCategroyList(final Map<String, Integer> selectMap, final int page) {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                String theme = "";
                String audience = "";
                String finish = "";
                String nation = "";
                if (selectMap.get("theme") != 0) {
                    theme = "/theme/" + selectMap.get("theme");
                }
                if (selectMap.get("audience") != 0) {
                    audience = "/audience/" + selectMap.get("audience");
                }
                if (selectMap.get("finish") != 0) {
                    finish = "/finish/" + selectMap.get("finish");
                }
                if (selectMap.get("nation") != 0) {
                    nation = "/nation/" + selectMap.get("nation");
                }
                String url = Constants.TENCENT_CATEGORY_URL_HEAD + audience + theme + finish +
                        Constants.TENCENT_CATEGORY_URL_MIDDLE + nation + Constants.TENCENT_CATEGORY_URL_FOOT + page;
                Document doc = Jsoup.connect(url).get();
                List<Comic> mdats = transToComic(doc);
                emitter.onNext(mdats);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    private List<Comic> transToCategory(Document doc) {
        List<Comic> mComic = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class", "comic-link");
        for (int i = 0; i < detail.size(); i++) {
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("strong").text());
            comic.setCover(detail.get(i).select("img").attr("src"));
            comic.setId(Long.parseLong(getID(detail.get(i).attr("href"))));
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
            mComic.add(comic);
        }
        return mComic;
    }

    private List<Comic> transToComic(Document doc) {
        List<Comic> mdats = new ArrayList<>();
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
