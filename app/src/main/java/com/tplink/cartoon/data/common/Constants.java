package com.tplink.cartoon.data.common;
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

public class Constants {

    public static final String COMIC_ID = "comic_id";
    public static final String COMIC_CHAPTERS = "comic_chapter";
    public static final String COMIC_CHAPTER_TITLE = "comic_chapter_title";
    public static final String COMIC_TITLE = "comic_title";
    public static final String COMIC_READ_TYPE = "comic_read_type";
    public static final String COMIC = "comic";
    public static final int RIGHT_TO_LEFT = 0;
    public static final int LEFT_TO_RIGHT = 1;

    public static final String DB_NAME = "comic.db";

    //抓取腾讯漫画TOP
    public static String TENCENTTOPURL = "http://ac.qq.com/Comic/all/state/pink/search/hot/page/";
    //抓取腾讯漫画详情页面
    public static String TENCENTDETAIL = "http://ac.qq.com/Comic/comicInfo/id/";
    //抓取详细的漫画阅读界面
    public static String TENCENTCOMICCHAPTERS = "http://120.79.66.128:5001";

    public static String TENCENTHOMEPAGE = "http://ac.qq.com/";

    public static String TENCENTJAPANHOT = "http://ac.qq.com/Comic/all/state/pink/nation/4/search/hot/page/1";

    public static String TENCENTBANNERJANPAN = "http://ac.qq.com/Jump";

    public static String TENCENTBANNER = "http://m.ac.qq.com";

    /**
     * 表示当前章节的状态
     * 0 未选取 1 已选择 2 已下载
     */
    public final static int CHAPTER_FREE = 0;
    public final static int CHAPTER_SELECTED= 1;
    public final static int CHAPTER_DOWNLOAD = 2;

    public final static int TYPE_RECOMMEND = 0;
    public final static int TYPE_RANK_LIST = 1;
    public final static int TYPE_HOT_SERIAL = 2;
    public final static int TYPE_HOT_JAPAN = 3;

}
