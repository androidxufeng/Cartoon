/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-27, xufeng, Create file
 */
package com.tplink.cartoon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.activity.CategoryActivity;
import com.tplink.cartoon.ui.activity.ComicChapterActivity;
import com.tplink.cartoon.ui.activity.ComicDetailActivity;
import com.tplink.cartoon.ui.activity.DownloadChapterlistActivity;
import com.tplink.cartoon.ui.activity.IndexActivity;
import com.tplink.cartoon.ui.activity.NewListActivity;
import com.tplink.cartoon.ui.activity.RankActivity;
import com.tplink.cartoon.ui.activity.SearchActivity;
import com.tplink.cartoon.ui.activity.SelectDownloadActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IntentUtil {

    public static void toComicDetail(Context context, long id, String title) {
        Intent intent = new Intent(context, ComicDetailActivity.class);
        intent.putExtra(Constants.COMIC_ID, id);
        intent.putExtra(Constants.COMIC_TITLE, title);
        context.startActivity(intent);
    }

    public static void toComicChapter(Context context, int chapters, long id, String title,
                                      List<String> chapterTitles, int type) {
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPTERS, chapters);
        intent.putExtra(Constants.COMIC_ID, id);
        intent.putExtra(Constants.COMIC_TITLE, title);
        intent.putExtra(Constants.COMIC_READ_TYPE, type);
        intent.putStringArrayListExtra(Constants.COMIC_CHAPTER_TITLE, new ArrayList<>(chapterTitles));
        context.startActivity(intent);
    }

    public static void toComicChapter(Context context, int chapters, Comic comic) {
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPTERS, chapters);
        intent.putExtra(Constants.COMIC, comic);
        context.startActivity(intent);
    }

    public static void toComicChapterForResult(Activity context, int chapters, Comic comic) {
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPTERS, chapters);
        intent.putExtra(Constants.COMIC, comic);
        context.startActivityForResult(intent, 1);
    }

    public static void toIndex(Context context, Comic comic) {
        Intent intent = new Intent(context, IndexActivity.class);
        intent.putExtra(Constants.COMIC, comic);
        context.startActivity(intent);
    }

    public static void toSelectDownload(Context context, Comic comic) {
        Intent intent = new Intent(context, SelectDownloadActivity.class);
        intent.putExtra(Constants.COMIC, comic);
        context.startActivity(intent);
    }

    public static void toSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public static void toDownloadListActivity(Context context, HashMap<Integer, Integer> map, Comic comic) {
        Intent intent = new Intent(context, DownloadChapterlistActivity.class);
        intent.putExtra(Constants.COMIC_SELECT_DOWNLOAD, map);
        intent.putExtra(Constants.COMIC, comic);
        context.startActivity(intent);
    }

    public static void toRankActivity(Context context) {
        Intent intent = new Intent(context, RankActivity.class);
        context.startActivity(intent);
    }

    public static void toUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void toQQchat(Context context, String number) {
        //uin是发送过去的qq号码
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + number + "version=1";
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void toCategoryActivity(Context context) {
        Intent intent = new Intent(context, CategoryActivity.class);
        context.startActivity(intent);
    }

    public static void toCategoryActivity(Context context, String type, int value) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(type, value);
        context.startActivity(intent);
    }

    public static void toNewActivity(Context context) {
        Intent intent = new Intent(context, NewListActivity.class);
        context.startActivity(intent);
    }
}