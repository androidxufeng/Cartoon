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
 * Ver 1.0, 18-8-2, xufeng, Create file
 */

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.PreloadChapters;
import com.tplink.cartoon.data.common.Constants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ChapterViewpagerAdapter extends PagerAdapter {
    private Context mContext;
    private final ArrayList<String> mDatas;
    private OnceClickListener listener;
    private int mDirection = Constants.LEFT_TO_RIGHT;
    private LinkedList<View> mViews = null;

    public ChapterViewpagerAdapter(Context context) {
        mDatas = new ArrayList<>();
        mContext = context;
        mViews = new LinkedList<>();
    }

    public ChapterViewpagerAdapter(Context context, PreloadChapters preloadChapters, int direct) {
        this(context);
        mDatas.addAll(preloadChapters.getPrelist());
        mDatas.addAll(preloadChapters.getNowlist());
        mDatas.addAll(preloadChapters.getNextlist());
        mDirection = direct;
    }

    public void clearList() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int direction) {
        mDirection = direction;
        notifyDataSetChanged();
    }

    public void setDatas(PreloadChapters datas) {
        mDatas.clear();
        mDatas.addAll(datas.getPrelist());
        mDatas.addAll(datas.getNowlist());
        mDatas.addAll(datas.getNextlist());
        Log.d("ceshi", "setDatas: data  " + mDatas);
        notifyDataSetChanged();
    }

    public void setNextDatas(List<String> mdatas) {
        mDatas.addAll(mdatas);
        notifyDataSetChanged();
    }

    public void setPreDatas(List<String> mdatas) {
        for (int i = 0; i < mdatas.size(); i++) {
            this.mDatas.add(0, mdatas.get(mdatas.size() - 1 - i));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View convertView = null;
        ViewHolder holder = null;
        if (mViews.size() == 0) {
            holder = new ViewHolder();
            convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_chapters, null);
            holder.imageView = (PhotoView) convertView.findViewById(R.id.pv_comic);
            convertView.setTag(holder);
        } else {
            convertView = mViews.removeFirst();
            holder = (ViewHolder) convertView.getTag();
        }
        if (mDirection == Constants.RIGHT_TO_LEFT) {
            Glide.with(mContext)
                    .load(mDatas.get(mDatas.size() - position - 1))
                    .placeholder(R.drawable.pic_default_vertical)
                    .into(holder.imageView);
        } else {
            Glide.with(mContext)
                    .load(mDatas.get(position))
                    .placeholder(R.drawable.pic_default_vertical)
                    .into(holder.imageView);
        }
        holder.imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (listener != null) {
                    listener.onClick(view, x, y);
                }
            }
        });
        container.addView(convertView);

        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
        container.removeView((View) object);
    }

    public class ViewHolder {
        public PhotoView imageView = null;
    }

    public void setListener(OnceClickListener listener) {
        this.listener = listener;
    }

    public interface OnceClickListener {
        void onClick(View view, float x, float y);
    }
}
