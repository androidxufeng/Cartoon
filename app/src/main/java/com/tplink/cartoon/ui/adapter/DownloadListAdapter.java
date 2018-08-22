/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.DBDownloadItem;

public class DownloadListAdapter extends BaseRecyclerAdapter<DBDownloadItem> {

    public DownloadListAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, DBDownloadItem item, int position) {
        holder.setText(R.id.tv_title, item.getChapters_title());
    }
}
