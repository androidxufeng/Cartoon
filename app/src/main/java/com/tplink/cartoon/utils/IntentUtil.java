package com.tplink.cartoon.utils;
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

import android.content.Context;
import android.content.Intent;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.activity.ComicChapterActivity;
import com.tplink.cartoon.ui.activity.ComicDetailActivity;
import com.tplink.cartoon.ui.activity.IndexActivity;

import java.util.ArrayList;
import java.util.List;

public class IntentUtil {

    public static void toComicDetail(Context context, long id, String title) {
        Intent intent = new Intent(context, ComicDetailActivity.class);
        intent.putExtra(Constants.COMIC_ID, id);
        intent.putExtra(Constants.COMIC_TITLE, title);
        context.startActivity(intent);
    }

    public static void toComicChapter(Context context, int chapters, String id, String title,
                                      List<String> chapter_titles, int type) {
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPTERS, chapters);
        intent.putExtra(Constants.COMIC_ID, id);
        intent.putExtra(Constants.COMIC_TITLE, title);
        intent.putExtra(Constants.COMIC_READ_TYPE, type);
        intent.putStringArrayListExtra(Constants.COMIC_CHAPTER_TITLE, (ArrayList<String>) chapter_titles);
        context.startActivity(intent);
    }

    public static void toComicChapter(Context context, int chapters, Comic comic) {
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPTERS, chapters);
        intent.putExtra(Constants.COMIC_ID, comic.getId());
        intent.putExtra(Constants.COMIC_TITLE, comic.getTitle());
        intent.putExtra(Constants.COMIC_READ_TYPE, comic.getReadType());
        intent.putStringArrayListExtra(Constants.COMIC_CHAPTER_TITLE, (ArrayList<String>) comic.getChapters());
        context.startActivity(intent);
    }

    public static void toIndex(Context context, String id, List<String> ChapterTitles, String title, int type) {
        Intent intent = new Intent(context, IndexActivity.class);
        intent.putExtra(Constants.COMIC_ID, id);
        intent.putExtra(Constants.COMIC_TITLE, title);
        intent.putExtra(Constants.COMIC_READ_TYPE, type);
        intent.putStringArrayListExtra(Constants.COMIC_CHAPTER_TITLE, (ArrayList<String>) ChapterTitles);
        context.startActivity(intent);
    }
}
