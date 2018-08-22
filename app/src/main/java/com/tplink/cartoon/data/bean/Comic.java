package com.tplink.cartoon.data.bean;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-27, xufeng, Create file
 */

import com.tplink.cartoon.utils.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Comic extends BaseBean {

    @Id
    protected long id;

    /**
     * 标题
     */
    protected String title;
    /**
     * 封面图片
     */
    protected String cover;
    /**
     * 作者
     */
    protected String author;
    /**
     * 章节标题
     */
    @Convert(columnType = String.class, converter = StringConverter.class)
    protected List<String> chapters;
    /**
     * 标签
     */
    @Convert(columnType = String.class, converter = StringConverter.class)
    protected List<String> tags;
    /**
     * 收藏数
     */
    protected String collections;
    /**
     * 详情
     */
    protected String describe;
    /**
     * 评分
     */
    protected String point;
    /**
     * 人气值
     */
    protected String popularity;
    /**
     * 话题量
     */
    protected String topics;

    /**
     * 更新时间
     */

    protected String updates;
    /**
     * 状态
     */
    protected String status;

    protected int currentChapter;

    protected long updateTime;

    protected long createTime;

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    protected boolean isCollect;

    public int getReadType() {
        return readType;
    }

    public void setReadType(int readType) {
        this.readType = readType;
    }

    protected int readType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<String> chapters) {
        this.chapters = chapters;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCollections() {
        return collections;
    }

    public void setCollections(String collections) {
        this.collections = collections;
    }

    public String getDescribe() {
        return "作品简介： " + describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getUpdates() {
        return updates;
    }

    public void setUpdates(String updates) {
        this.updates = updates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", author='" + author + '\'' +
                ", chapters=" + chapters +
                ", tags=" + tags +
                ", collections='" + collections + '\'' +
                ", describe='" + describe + '\'' +
                ", point='" + point + '\'' +
                ", popularity='" + popularity + '\'' +
                ", topics='" + topics + '\'' +
                ", updates='" + updates + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public void setChapters(List<String> chapters) {
        this.chapters = chapters;
    }

    public boolean getIsCollect() {
        return this.isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    @Generated(hash = 1733893838)
    public Comic(long id, String title, String cover, String author,
                 List<String> chapters, List<String> tags, String collections,
                 String describe, String point, String popularity, String topics,
                 String updates, String status, int currentChapter, long updateTime,
                 long createTime, boolean isCollect, int readType) {
        this.id = id;
        this.title = title;
        this.cover = cover;
        this.author = author;
        this.chapters = chapters;
        this.tags = tags;
        this.collections = collections;
        this.describe = describe;
        this.point = point;
        this.popularity = popularity;
        this.topics = topics;
        this.updates = updates;
        this.status = status;
        this.currentChapter = currentChapter;
        this.updateTime = updateTime;
        this.createTime = createTime;
        this.isCollect = isCollect;
        this.readType = readType;
    }

    @Generated(hash = 1347984162)
    public Comic() {
    }
}
