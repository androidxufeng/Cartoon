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
 * Ver 1.0, 18-8-9, xufeng, Create file
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tplink.cartoon.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {
    private Context mContext;
    private List<String> mDatas;
    public boolean isOrder = true;

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
        notifyDataSetChanged();
    }

    public IndexAdapter(Context context, List<String> lists) {
        this.mContext = context;
        mDatas = lists;
    }

    public List<String> getmDatas() {
        return mDatas;
    }

    public void setDatas(List<String> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
    }

    public void updateWithClear(List<String> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    @Override
    public IndexViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IndexViewHolder holder = new IndexViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_chapter, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(IndexViewHolder holder, int position) {
        if (!isOrder) {
            holder.mTitle.setText(mDatas.get(mDatas.size() - position - 1));
            holder.mPosition.setText(mDatas.size() - position + " - ");
        } else {
            holder.mTitle.setText(mDatas.get(position));
            holder.mPosition.setText((position + 1) + " - ");
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class IndexViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_position)
        TextView mPosition;
        @BindView(R.id.tv_title)
        TextView mTitle;

        public IndexViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }
}