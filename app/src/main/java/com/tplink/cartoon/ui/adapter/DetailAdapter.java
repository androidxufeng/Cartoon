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
 * Ver 1.0, 18-8-1, xufeng, Create file
 */

import android.content.Context;

import com.tplink.cartoon.R;

public class DetailAdapter extends BaseRecyclerAdapter<String> {


    private boolean isOrder;

    public DetailAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
        notifyDataSetChanged();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, String item, int position) {
        if (!isOrder) {
            holder.setText(R.id.tv_title, list.get(list.size() - position - 1));
            holder.setText(R.id.tv_position, list.size() - position + " - ");
        } else {
            holder.setText(R.id.tv_title, item);
            holder.setText(R.id.tv_position, (position + 1) + " - ");
        }

    }

}
