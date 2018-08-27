package com.tplink.cartoon.ui.source.detail;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-1, xufeng, Create file
 */

import android.content.Context;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DownState;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.db.DaoHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class DetailDataSource implements IDetailDataSource {

    private final DaoHelper<Comic> mDaoHelper;

    public DetailDataSource(Context context) {
        mDaoHelper = new DaoHelper<>(context);
    }

    @Override
    public Flowable<Comic> getDetail(final long comicId) {
        return Flowable.create(new FlowableOnSubscribe<Comic>() {
            @Override
            public void subscribe(FlowableEmitter<Comic> emitter) throws Exception {
                Document doc = Jsoup.connect(Constants.TENCENTDETAIL + comicId).get();
                Comic comic = transToComicDetail(doc);
                //获取当前章节
                Comic dbComic = mDaoHelper.findComic(comicId);
                if (dbComic != null) {
                    comic.setCurrentChapter(dbComic.getCurrentChapter());
                    comic.setCurrentChapter(dbComic.getCurrentChapter());
                    comic.setDownloadTime(dbComic.getDownloadTime());
                    comic.setCollectTime(dbComic.getCollectTime());
                    comic.setClickTime(dbComic.getClickTime());
                    comic.setDownload_num(dbComic.getDownload_num());
                    comic.setDownload_num_finish(dbComic.getDownload_num_finish());
                    comic.setCurrent_page(dbComic.getCurrent_page());
                    comic.setCurrent_page_all(dbComic.getCurrent_page_all());
                    comic.setIsCollected(dbComic.getIsCollected());
                    comic.setReadType(dbComic.getReadType());
                }
                emitter.onNext(comic);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Boolean> saveComicToDB(final Comic comic) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                if (mDaoHelper.findComic(comic.getId()) == null) {
                    if (mDaoHelper.insert(comic)) {
                        emitter.onNext(true);
                    } else {
                        emitter.onNext(false);
                    }
                } else {
                    emitter.onNext(false);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Boolean> updateComicToDB(final Comic comic) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                Comic dbComic = mDaoHelper.findComic(comic.getId());
                if (dbComic != null) {
                    if (mDaoHelper.update(comic)) {
                        emitter.onNext(true);
                    } else {
                        emitter.onNext(false);
                    }
                } else {
                    emitter.onNext(false);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Comic> getComicFromDB(final long comicId) {

        return Flowable.create(new FlowableOnSubscribe<Comic>() {
            @Override
            public void subscribe(FlowableEmitter<Comic> emitter) throws Exception {
                Comic comic = mDaoHelper.findComic(comicId);
                if (comic != null) {
                    emitter.onNext(comic);
                } else {
                    emitter.onNext(null);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    private Comic transToComicDetail(Document doc) {
        final Comic comic = new Comic();
        try {
            comic.setTitle(doc.title().split("-")[0]);
            //设置标签
            Element ElementDescription = doc.getElementsByAttributeValue("name", "Description").get(0);
            String descriptions = ElementDescription.select("meta").attr("content");
            String mdescriptions[] = descriptions.split("：");
            List<String> tags = new ArrayList<>();
            String mtags[] = mdescriptions[mdescriptions.length - 1].split(",");
            for (int i = 0; i < mtags.length; i++) {
                tags.add(mtags[i]);
            }
            comic.setTags(tags);
            Element detail = doc.getElementsByAttributeValue("class", "works-cover ui-left").get(0);
            comic.setCover(detail.select("img").attr("src"));
            //设置作者
            Element author = doc.getElementsByAttributeValue("class", " works-author-name").get(0);
            comic.setAuthor(author.select("a").attr("title"));
            //设置收藏数
            Element collect = doc.getElementsByAttributeValue("id", "coll_count").get(0);
            DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String collection = decimalFormat.format(Float.parseFloat(collect.text()) / 10000);//format 返回的是字符串
            comic.setCollections("(" + collection + ")万");
            //设置章节数
            Element DivChapter = doc.getElementsByAttributeValue("class", "chapter-page-all works-chapter-list").get(0);
            List<Element> ElementChapters = DivChapter.getElementsByAttributeValue("target", "_blank");
            final List<String> chapters = new ArrayList<>();
            for (int i = 0; i < ElementChapters.size(); i++) {
                chapters.add(ElementChapters.get(i).select("a").text());
            }
            comic.setChapters(chapters);

            Element ElementDescribe = doc.getElementsByAttributeValue("class", "works-intro-short ui-text-gray9").get(0);
            comic.setDescribe(ElementDescribe.select("p").text());


            Element ElementPopularity = doc.getElementsByAttributeValue("class", " works-intro-digi").get(0);
            comic.setPopularity(ElementPopularity.select("em").get(1).text());
            //设置状态
            String status = detail.select("label").get(0).text();
            //设置更新日期
            if (status.equals("已完结")) {
                comic.setStatus("已完结");
                comic.setUpdates("全" + ElementChapters.size() + "话");
            } else {
                Element ElementUpdate = doc.getElementsByAttributeValue("class", " ui-pl10 ui-text-gray6").get(0);
                String updates = ElementUpdate.select("span").get(0).text();
                comic.setUpdates(updates);
                comic.setStatus("更新最新话");
            }

            Element ElementPoint = doc.getElementsByAttributeValue("class", "ui-text-orange").get(0);
            comic.setPoint(ElementPoint.select("strong").get(0).text());
            //设置阅读方式
            List<Element> Element_isJ = doc.getElementsByAttributeValue("src", "http://q2.qlogo.cn/g?b=qq&k=hMPm8WLLDbcdk0Vs4epHxA&s=100&t=561");
            if (Element_isJ != null && Element_isJ.size() != 0) {
                comic.setReadType(Constants.RIGHT_TO_LEFT);
            } else {
                comic.setReadType(Constants.UP_TO_DOWN);
            }
            comic.setState(DownState.START);
        } catch (Exception e) {

        } finally {
            return comic;
        }
    }
}
