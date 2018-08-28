/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-14, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;
import android.view.View;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;

public class CollectionAdapter extends BookShelfAdapter<Comic> {

    public CollectionAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        if (item != null) {
            if (!isEditing) {
                holder.setVisibility(R.id.iv_image_select, View.GONE);
            } else {
                if (mMap.size() != 0 && mMap.get(position) == Constants.CHAPTER_SELECTED) {
                    holder.setImageResource(R.id.iv_image_select, R.drawable.item_selected);
                } else {
                    holder.setImageResource(R.id.iv_image_select, R.drawable.item_select);
                }
                holder.setVisibility(R.id.iv_image_select, View.VISIBLE);
            }
            holder.setText(R.id.tv_title, item.getTitle());
            holder.setImageByUrlNone(R.id.iv_image, item.getCover());
            if (item.getChapters() != null && item.getChapters().size() != 0) {
                holder.setText(R.id.tv_index_reader, item.getChapters().size() + "话" + "/" + item.getCurrentChapter() + "话");
            }
        }
    }
}
