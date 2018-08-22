/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.source.search;

import android.content.Context;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DBSearchResult;
import com.tplink.cartoon.data.bean.HttpResult;
import com.tplink.cartoon.data.bean.SearchBean;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.db.DaoHelper;
import com.tplink.cartoon.net.RetrofitClient;
import com.tplink.cartoon.net.cache.CacheProviders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

public class SearchDataSource implements ISearchDataSource {

    private DaoHelper<DBSearchResult> mDaoHelper;

    public SearchDataSource(Context context) {
        mDaoHelper = new DaoHelper<>(context);
    }

    @Override
    public Flowable<HttpResult<List<SearchBean>>> getDynamicResult(String title) {
        Flowable<HttpResult<List<SearchBean>>> flowable = RetrofitClient.getInstance()
                .getComicService()
                .getDynamicSearchResult(Constants.TENCENT_SEARCH_URL + title);
        return CacheProviders.getComicCacheInstance()
                .getDynamicSearchResult(flowable, new DynamicKey(Constants.TENCENT_SEARCH_URL + title),
                        new EvictDynamicKey(false));
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

    @Override
    public Flowable<List<Comic>> getTopResult() {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                Document doc = Jsoup.connect(Constants.TENCENT_SEARCH_RECOMMEND).get();
                List<Comic> mdats = transToSearchTopComic(doc);
                emitter.onNext(mdats);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Boolean> updateSearchResultToDB(final String title) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                Date date = new Date();
                long datetime = date.getTime();
                DBSearchResult result = new DBSearchResult();
                result.setTitle(title);
                result.setSearch_time(datetime);
                if (mDaoHelper.insert(result)) {
                    emitter.onNext(true);
                } else {
                    emitter.onNext(false);
                }
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<Comic>> getHistorySearch() {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                List<DBSearchResult> results = mDaoHelper.querySearch();
                List<Comic> comics = transSearchToComic(results);
                emitter.onNext(comics);
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Boolean> clearSearchHistory() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                if (mDaoHelper.deleteAllSearch()) {
                    emitter.onNext(true);
                } else {
                    emitter.onNext(false);
                }
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

    public List<Comic> transToSearchTopComic(Document doc) {
        List<Comic> mdats = new ArrayList<>();
        Element detail = doc.getElementsByAttributeValue("class", "search-hot-list").get(0);
        List<Element> details = detail.select("a");
        for (int i = 0; i < details.size(); i++) {
            Comic comic = new Comic();
            comic.setTitle(details.get(i).text());
            comic.setId(Long.parseLong(getID(details.get(i).attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    public static List<Comic> transSearchToComic(List<DBSearchResult> results) {
        List<Comic> comics = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Comic comic = new Comic();
            comic.setTitle(results.get(i).getTitle());
            comics.add(comic);
        }
        return comics;
    }

    public List<Comic> transDynamicSearchToComic(List<SearchBean> results) {
        List<Comic> comics = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Comic comic = new Comic();
            comic.setTitle(results.get(i).getTitle());
            comic.setId(results.get(i).getId());
            comics.add(comic);
        }
        return comics;
    }

    private String getID(String splitID) {
        String[] ids = splitID.split("/");
        return ids[ids.length - 1];
    }
}
