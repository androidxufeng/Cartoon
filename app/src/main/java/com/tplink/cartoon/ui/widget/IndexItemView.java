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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.utils.DisplayUtil;

public class IndexItemView extends LinearLayout {
    TextView mTitle;
    private Drawable img_location;
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
        img_location = getResources().getDrawable(R.drawable.location);
        img_location.setBounds(0, 0, img_location.getMinimumWidth(), img_location.getMinimumHeight());
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

    @TargetApi(Build.VERSION_CODES.M)
    public void setCurrentColor(boolean isCurrent) {
        if (isCurrent) {
            mTitle.setTextAppearance(R.style.colorTextPrimary);
            mTitle.setCompoundDrawables(null, null, img_location, null);
            mTitle.setCompoundDrawablePadding(DisplayUtil.dip2px(getContext(), 10));
        } else {
            mTitle.setTextAppearance(R.style.colorTextBlack);
            mTitle.setCompoundDrawables(null, null, null, null);
        }
    }

    public TextView getTextView() {
        return mTitle;
    }

    public interface onItemClickLinstener {
        void onItemClick(View view, int position);
    }
}
