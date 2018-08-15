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

public class BookShelfAdapter extends BaseRecyclerAdapter<Comic> {

    public BookShelfAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setImageByUrl(R.id.iv_image, item.getCover());
        holder.setText(R.id.tv_index_reader, item.getChapters().size() + "话");
    }
}
