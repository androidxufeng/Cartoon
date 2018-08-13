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

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.source.IDataSource;

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

public class DetailDataSource implements IDetailDataSource, IDataSource {
    @Override
    public Flowable<Comic> getDetail(final String comicId) {
        return Flowable.create(new FlowableOnSubscribe<Comic>() {
            @Override
            public void subscribe(FlowableEmitter<Comic> emitter) throws Exception {
                Document doc = Jsoup.connect(Constants.TENCENTDETAIL + comicId).get();
                Comic comic = transToComicDetail(doc);
                emitter.onNext(comic);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    private Comic transToComicDetail(Document doc) {
        //设置标题
        Comic comic = new Comic();
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
        ArrayList<String> chapters = new ArrayList<>();
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
            comic.setReadType(Constants.LEFT_TO_RIGHT);
        }
        return comic;
    }
}
