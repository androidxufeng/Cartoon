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
import android.mtp.MtpEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;

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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_detail, null);
        addView(view);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mTitle.setText((position + 1) + "-" + title);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, position);
                }
            }
        });
    }

    public TextView getTextView() {
        return mTitle;
    }

    public interface onItemClickLinstener {
        void onItemClick(View view, int position);
    }
}
