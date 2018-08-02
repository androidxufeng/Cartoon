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

    public static final String COMIC_ID ="comic_id";
    public static final String COMIC_CHAPERS ="comic_chapter";
    public static final String COMIC_CHAPER_TITLE = "comic_chapter_title";
    public static final int RIGHT_TO_LEFT =0;
    public static final int LEFT_TO_RIGHT =1;

    //抓取腾讯漫画TOP
    public static String TENCENTTOPURL = "http://ac.qq.com/Comic/all/state/pink/search/hot/page/";
    //抓取腾讯漫画详情页面
    public static String TENCENTDETAIL = "http://ac.qq.com/Comic/comicInfo/id/";
    //抓取详细的漫画阅读界面
    public static String TENCENTCOMICCHAPTERS ="http://120.79.66.128:5001";
}
