/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-17, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.PreloadChapters;

public class ChapterRecyclerAdapter extends BaseRecyclerAdapter<String> {

    public ChapterRecyclerAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public void setDatas(PreloadChapters preloadChapters) {
        this.list.clear();
        this.list.addAll(preloadChapters.getPrelist());
        this.list.addAll(preloadChapters.getNowlist());
        this.list.addAll(preloadChapters.getNextlist());
        notifyDataSetChanged();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, String item, int position) {
        holder.setPhotoViewImageByUrl(R.id.pv_comic, item);
    }
}
