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

import android.content.Context;
import android.media.MediaDataSource;
import android.util.Log;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.FullHomeItem;
import com.tplink.cartoon.data.bean.HomeTitle;
import com.tplink.cartoon.data.bean.LargeHomeItem;
import com.tplink.cartoon.data.bean.SmallHomeItem;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.db.DaoHelper;
import com.tplink.cartoon.ui.activity.HomeActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class HomeDataSource implements IHomeDataSource {

    private final DaoHelper mHelper;

    public HomeDataSource(Context context){
        mHelper = new DaoHelper<>(context);
    }
    @Override
    public Flowable<List<Comic>> loadData() {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                List<Comic> mdats = new ArrayList<>();
                //强推作品
                Document recommend = Jsoup.connect(Constants.TENCENTHOMEPAGE).get();
                //日漫馆
                Document japan = Jsoup.connect(Constants.TENCENTJAPANHOT).get();

                Document doc = Jsoup.connect(Constants.TENCENTTOPURL + "1").get();

                addComic(recommend, mdats, Constants.TYPE_RECOMMEND);
                addComic(recommend, mdats, Constants.TYPE_BOY_RANK);
                addComic(recommend, mdats, Constants.TYPE_GIRL_RANK);
                addComic(recommend, mdats, Constants.TYPE_HOT_SERIAL);
                addComic(japan, mdats, Constants.TYPE_HOT_JAPAN);
                addComic(doc, mdats, Constants.TYPE_RANK_LIST);

                //普通的类型
                HomeTitle homeTitle = new HomeTitle();
                homeTitle.setItemTitle("");
                mdats.add(homeTitle);

                emitter.onNext(mdats);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Comic> findRecentlyComic() {
        return Flowable.create(new FlowableOnSubscribe<Comic>() {
            @Override
            public void subscribe(FlowableEmitter<Comic> emitter) throws Exception {
                Comic recentlyComic = mHelper.findRecentlyComic();
                emitter.onNext(recentlyComic);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<Comic>> refreshData() {
        return Flowable.create(new FlowableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Comic>> emitter) throws Exception {
                List<Comic> mdats = new ArrayList<>();
                HomeTitle homeTitle = new HomeTitle();
                homeTitle.setItemTitle("排行榜");
                mdats.add(homeTitle);

                Document doc = Jsoup.connect(Constants.TENCENTTOPURL + "1").get();
                Document doc2 = Jsoup.connect(Constants.TENCENTTOPURL + "2").get();

                mdats.addAll(transToComic(doc));
                mdats.addAll(transToComic(doc2));

                homeTitle = new HomeTitle();
                homeTitle.setItemTitle("");
                mdats.add(homeTitle);

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

    /**
     * 添加漫画到List里
     *
     * @param doc
     * @param mdats
     * @param type
     */
    private void addComic(Document doc, List<Comic> mdats, int type) {
        HomeTitle homeTitle;
        try {
            switch (type) {
                case Constants.TYPE_RECOMMEND:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("强推作品");
                    homeTitle.setTitleType(Constants.TYPE_RECOMMEND);
                    mdats.add(homeTitle);
                    mdats.addAll(transToRecommendComic(doc));
                    break;
                case Constants.TYPE_BOY_RANK:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("少年漫画");
                    homeTitle.setTitleType(Constants.TYPE_BOY_RANK);
                    mdats.add(homeTitle);
                    mdats.addAll(transToBoysComic(doc));
                    break;
                case Constants.TYPE_GIRL_RANK:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("少女漫画");
                    homeTitle.setTitleType(Constants.TYPE_GIRL_RANK);
                    mdats.add(homeTitle);
                    mdats.addAll(transToGirlsComic(doc));
                    break;
                case Constants.TYPE_HOT_SERIAL:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("热门连载");
                    homeTitle.setTitleType(Constants.TYPE_HOT_SERIAL);
                    mdats.add(homeTitle);
                    mdats.addAll(transToNewComic(doc));
                    break;
                case Constants.TYPE_HOT_JAPAN:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("日漫馆");
                    homeTitle.setTitleType(Constants.TYPE_HOT_JAPAN);
                    mdats.add(homeTitle);
                    mdats.addAll(transToJapanComic(doc));
                    break;
                case Constants.TYPE_RANK_LIST:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("排行榜");
                    homeTitle.setTitleType(Constants.TYPE_RANK_LIST);
                    mdats.add(homeTitle);
                    mdats.addAll(transToComic(doc));
                    break;
            }
        } catch (Exception e) {
            Log.d("zhhr", "type = " + type + "is Error");
            throw e;
        }
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

    /**
     * banner的获取
     *
     * @param doc
     * @return
     */
    public static List<Comic> transToBanner(Document doc) {
        List<Comic> mdats = new ArrayList<>();
        Element detail = doc.getElementsByAttributeValue("class", "banner-list").get(0);
        List<Element> infos = detail.getElementsByTag("a");
        for (int i = 0; i < infos.size(); i++) {
            Comic comic = new Comic();
            comic.setTitle(infos.get(i).select("a").attr("title"));
            comic.setCover(infos.get(i).select("img").attr("src"));
            try {
                comic.setId(Long.parseLong(getID(infos.get(i).select("a").attr("href"))));
                mdats.add(comic);
            } catch (Exception e) {
            }
        }
        return mdats;
    }

    /**
     * 日漫首页
     *
     * @param doc
     * @return
     */
    public static List<Comic> transToBannerJapan(Document doc) {
        List<Comic> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class", "comic-text");
        Random random = new Random();
        int result = random.nextInt(3);
        for (int i = (result * 4); i < (result + 1) * 4; i++) {
            Comic comic = new Comic();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("src"));
            comic.setId(Long.parseLong(getID(detail.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    //处理强推作品
    private static List<LargeHomeItem> transToRecommendComic(Document doc) {
        List<LargeHomeItem> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class", "in-anishe-text");
        Random random = new Random();
        int result = random.nextInt(5);
        for (int i = (result * 6); i < (result + 1) * 6; i++) {
            LargeHomeItem comic = new LargeHomeItem();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("data-original"));
            Element ElementDescribe = detail.get(i).getElementsByAttributeValue("class", "mod-cover-list-intro").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(detail.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    //日漫馆
    public static List<FullHomeItem> transToJapanComic(Document doc) {
        List<FullHomeItem> mdats = new ArrayList<>();
        List<Element> detail = doc.getElementsByAttributeValue("class", "ret-works-cover");
        List<Element> infos = doc.getElementsByAttributeValue("class", "ret-works-info");
        for (int i = 0; i < 3; i++) {
            FullHomeItem comic = new FullHomeItem();
            comic.setTitle(detail.get(i).select("a").attr("title"));
            comic.setCover(detail.get(i).select("img").attr("data-original"));
            comic.setAuthor(infos.get(i).select("p").attr("title"));
            Element ElementDescribe = infos.get(i).getElementsByAttributeValue("class", "ret-works-decs").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(infos.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    //热门连载
    public static List<LargeHomeItem> transToNewComic(Document doc) {
        List<LargeHomeItem> mdats = new ArrayList<>();
        Random random = new Random();
        int result = random.nextInt(7);
        Element detail = doc.getElementsByAttributeValue("class", "in-anishe-list clearfix in-anishe-ul").get(result);
        List<Element> hots = detail.getElementsByTag("li");
        for (int i = 0; i < 4; i++) {
            LargeHomeItem comic = new LargeHomeItem();
            comic.setTitle(hots.get(i).select("img").attr("alt"));
            comic.setCover(hots.get(i).select("img").attr("data-original"));
            Element ElementDescribe = hots.get(i).getElementsByAttributeValue("class", "mod-cover-list-intro").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(hots.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    /**
     * 男生榜
     *
     * @param doc
     * @return
     */
    public static List<SmallHomeItem> transToBoysComic(Document doc) {
        List<SmallHomeItem> mdats = new ArrayList<>();
        Element detail = doc.getElementsByAttributeValue("class", "in-teen-list mod-cover-list clearfix").get(0);
        List<Element> boys = detail.getElementsByTag("li");
        for (int i = 0; i < boys.size(); i++) {
            SmallHomeItem comic = new SmallHomeItem();
            comic.setTitle(boys.get(i).select("img").attr("alt"));
            comic.setCover(boys.get(i).select("img").attr("data-original"));
            Element ElementDescribe = boys.get(i).getElementsByAttributeValue("class", "mod-cover-list-intro").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(boys.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }

    /**
     * 女生榜
     *
     * @param doc
     * @return
     */
    public static List<SmallHomeItem> transToGirlsComic(Document doc) {
        List<SmallHomeItem> mdats = new ArrayList<>();
        Element detail = doc.getElementsByAttributeValue("class", "in-girl-list mod-cover-list clearfix").get(0);
        List<Element> girls = detail.getElementsByTag("li");
        for (int i = 0; i < girls.size(); i++) {
            SmallHomeItem comic = new SmallHomeItem();
            comic.setTitle(girls.get(i).select("img").attr("alt"));
            comic.setCover(girls.get(i).select("img").attr("data-original"));
            Element ElementDescribe = girls.get(i).getElementsByAttributeValue("class", "mod-cover-list-intro").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());
            comic.setId(Long.parseLong(getID(girls.get(i).select("a").attr("href"))));
            mdats.add(comic);
        }
        return mdats;
    }


    private static String getID(String splitID) {
        String[] ids = splitID.split("/");
        return ids[ids.length - 1];
    }

}
