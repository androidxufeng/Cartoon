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

import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.activity.ComicChapterActivity;
import com.tplink.cartoon.ui.activity.ComicDetailActivity;
import com.tplink.cartoon.ui.activity.IndexActivity;

import java.util.ArrayList;

public class IntentUtil {

    public static void toComicDetail(Context context, String id) {
        Intent intent = new Intent(context, ComicDetailActivity.class);
        intent.putExtra(Constants.COMIC_ID, id);
        context.startActivity(intent);
    }

    public static void toComicChapter(Context context, String id, int chapters, ArrayList<String> ChapterTitle) {
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_ID, id);
        intent.putExtra(Constants.COMIC_CHAPTERS, chapters);
        intent.putStringArrayListExtra(Constants.COMIC_CHAPTER_TITLE, ChapterTitle);
        context.startActivity(intent);
    }

    public static void toIndex(Context context, String id, int chapters, ArrayList<String> ChapterTitles) {
        Intent intent = new Intent(context, IndexActivity.class);
        intent.putExtra(Constants.COMIC_ID, id);
        intent.putExtra(Constants.COMIC_CHAPTERS, chapters);
        intent.putStringArrayListExtra(Constants.COMIC_CHAPTER_TITLE, ChapterTitles);
        context.startActivity(intent);
    }
}
