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

public class IntentUtil {

    public static void ToComicDetail(Context context, String id) {
        Intent intent = new Intent(context, ComicDetailActivity.class);
        intent.putExtra(Constants.COMIC_ID, id);
        context.startActivity(intent);
    }

    public static void ToComicChapter(Context context, String id, String chapters, String ChapterTitle) {
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_ID, id);
        intent.putExtra(Constants.COMIC_CHAPERS, chapters);
        intent.putExtra(Constants.COMIC_CHAPER_TITLE, ChapterTitle);
        context.startActivity(intent);
    }
}
