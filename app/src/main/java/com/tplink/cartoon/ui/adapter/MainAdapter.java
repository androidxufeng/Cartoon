package com.tplink.cartoon.ui.adapter;
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

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;

public class MainAdapter extends BaseRecyclerAdapter<Comic> {
    public MainAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setImageByUrl(R.id.iv_image, item.getCover());
    }
}
