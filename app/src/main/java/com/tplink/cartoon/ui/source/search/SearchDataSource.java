/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.search;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.SearchResult;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.net.RetrofitClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class SearchDataSource implements ISearchDataSource {

    @Override
    public Flowable<SearchResult> getDynamicResult(String title) {
        return RetrofitClient.getInstance()
                .getComicService()
                .getDynamicSearchResult(Constants.TENCENT_SEARCH_URL + title);
    }

    @Override
    public Flowable<List<Comic>> getSearchResult(final String title) {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                Document doc = Jsoup.connect(Constants.TENCENT_SEARCH_RESULT_URL + title).get();
                List<Comic> mdats = transToSearchResultComic(doc);
                emitter.onNext(mdats);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    // 处理搜索结果
    private List<Comic> transToSearchResultComic(Document doc) {
        List<Comic> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class", "comic-item");
        for (int i = 0; i < detail.size(); i++) {
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("strong").text());
            comic.setCover(detail.get(i).select("img").attr("src"));
            comic.setId(Long.parseLong(getID(detail.get(i).select("a").attr("href"))));
            List<Element> info = detail.get(i).select("small");
            comic.setUpdates(info.get(0).text());
            String descriptions = info.get(1).text();
            String mdescriptions[] = descriptions.split("：");
            List<String> tags = new ArrayList<>();
            for (int j = 0; j < mdescriptions.length; j++) {
                tags.add(mdescriptions[j]);
            }
            comic.setTags(tags);
            comic.setAuthor(info.get(2).text());
            mdats.add(comic);
        }
        return mdats;
    }

    private String getID(String splitID) {
        String[] ids = splitID.split("/");
        return ids[ids.length - 1];
    }
}
