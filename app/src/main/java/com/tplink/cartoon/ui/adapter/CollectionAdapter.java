/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-14, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;

import java.util.List;

public class CollectionAdapter extends BaseRecyclerAdapter<Comic> {

    public CollectionAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public CollectionAdapter(Context context, List<Comic> list, int itemLayoutId) {
        super(context, list, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        if (item != null) {
            holder.setText(R.id.tv_title, item.getTitle());
            holder.setImageByUrl(R.id.iv_image, item.getCover());
            if (item.getChapters() != null && item.getChapters().size() != 0) {
                holder.setText(R.id.tv_index_reader, item.getChapters().size() + "话" + "/" + item.getCurrentChapter() + "话");
            }
        }
    }
}
