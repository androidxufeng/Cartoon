package com.tplink.cartoon.ui.widget;
/*
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ${FILE_NAME}
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-9, xufeng, Create file
 */

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tplink.cartoon.utils.DisplayUtil;

public class IndexItemView extends LinearLayout {
    TextView mTitle;
    private onItemClickLinstener listener;

    public onItemClickLinstener getListener() {
        return listener;
    }

    public void setListener(onItemClickLinstener listener) {
        this.listener = listener;
    }

    public IndexItemView(Context context, String title, final int position) {
        super(context);
        this.setOrientation(VERTICAL);
        /*ll = new LinearLayout(context);
        ll.setOrientation(HORIZONTAL);*/
        mTitle = new TextView(context);
        //mPosition = new TextView(context);
        mTitle.setText((position + 1) + " - " + title);
        //mPosition.setText((position+1)+" - ");
        View mBottom = new View(context);
        mBottom.setBackgroundColor(Color.parseColor("#e2e2e2"));
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, DisplayUtil.dip2px(context, 60));
        lp.setMargins(DisplayUtil.dip2px(context, 10), 0, 0, 0);
        mTitle.setGravity(Gravity.CENTER_VERTICAL);
        mTitle.setTextSize(12);
        mTitle.setTextColor(Color.parseColor("#666666"));
        addView(mBottom, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, 0.5f)));
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });
    }

    public interface onItemClickLinstener {
        public void onItemClick(View view, int position);
    }
}
