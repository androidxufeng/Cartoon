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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.FullHomeItem;
import com.tplink.cartoon.data.bean.HomeTitle;
import com.tplink.cartoon.data.bean.LargeHomeItem;
import com.tplink.cartoon.data.bean.SmallHomeItem;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;

import java.util.List;

public class MainAdapter extends BaseRecyclerAdapter<Comic> {
    public static final int ITEM_TITLE = 0;
    public static final int ITEM_LARGE = 1;
    public static final int ITEM_SMALL = 2;
    public static final int ITEM_FULL = 3;

    private int itemTitleLayoutId;
    private int mFullLayoutId;
    private OnItemClickListener mItemClickListener;

    public MainAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public MainAdapter(Context context, int itemTitleLayoutId, int itemLayoutId, int fullLayoutId) {
        super(context, itemLayoutId);
        this.itemTitleLayoutId = itemTitleLayoutId;
        mFullLayoutId = fullLayoutId;
        this.itemLayoutId = itemLayoutId;
    }

    public MainAdapter(Context context, List<Comic> list, int itemLayoutId) {
        super(context, list, itemLayoutId);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof NoScrollGridLayoutManager) {
            final NoScrollGridLayoutManager gridManager = ((NoScrollGridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int size;
                    switch (getItemViewType(position)) {
                        // 代表每个item占几个位置
                        case ITEM_TITLE:
                            size = 6;
                            break;
                        case ITEM_SMALL:
                            size = 2;
                            break;
                        case ITEM_LARGE:
                            size = 3;
                            break;
                        case ITEM_FULL:
                            size = 6;
                            break;
                        default:
                            size = 2;
                            break;
                    }
                    return size;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null && view != null && recyclerView != null) {
                    int position = recyclerView.getChildAdapterPosition(view);
                    if (list.get(position) instanceof HomeTitle) {
                        mItemClickListener.onTitleClick(recyclerView, view, ((HomeTitle) list.get(position)).getTitleType());
                    } else {
                        mItemClickListener.onItemClick(recyclerView, view, position);
                    }
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (longClickListener != null && view != null && recyclerView != null) {
                    int position = recyclerView.getChildAdapterPosition(view);
                    longClickListener.onItemLongClick(recyclerView, view, position);
                    return true;
                }
                return false;
            }
        });
        convert(holder, list.get(position), position);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        switch (getItemViewType(position)) {
            case ITEM_TITLE:
                holder.setText(R.id.tv_hometitle, ((HomeTitle) item).getItemTitle());
                break;
            case ITEM_FULL:
                holder.setText(R.id.tv_title, item.getTitle());
                holder.setText(R.id.tv_describe, item.getDescribe());
                holder.setText(R.id.tv_author, item.getAuthor());
                holder.setImageByUrl(R.id.iv_image, item.getCover());
                break;
            default:
                holder.setText(R.id.tv_title, item.getTitle());
                holder.setText(R.id.tv_describe, item.getDescribe());
                holder.setImageByUrl(R.id.iv_image, item.getCover());
                break;

        }
    }

    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ITEM_TITLE:
                view = inflater.inflate(itemTitleLayoutId, parent, false);
                break;
            case ITEM_LARGE:
                view = inflater.inflate(itemLayoutId, parent, false);
                break;
            case ITEM_SMALL:
                view = inflater.inflate(itemLayoutId, parent, false);
                break;
            case ITEM_FULL:
                view = inflater.inflate(mFullLayoutId, parent, false);
                break;
            default:
                view = inflater.inflate(itemLayoutId, parent, false);
                break;
        }
        return BaseRecyclerHolder.getRecyclerHolder(context, view);
    }

    public int getItemViewType(int position) {
        Comic comic = list.get(position);
        if (comic instanceof HomeTitle) {
            return ITEM_TITLE;
        } else if (comic instanceof SmallHomeItem) {
            return ITEM_SMALL;
        } else if (comic instanceof LargeHomeItem) {
            return ITEM_LARGE;
        } else if (comic instanceof FullHomeItem) {
            return ITEM_FULL;
        } else {
            return ITEM_SMALL;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View view, int position);

        void onTitleClick(RecyclerView parent, View view, int type);
    }

}
