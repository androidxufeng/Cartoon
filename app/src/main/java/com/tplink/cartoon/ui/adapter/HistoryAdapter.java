/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-21, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;

public class HistoryAdapter extends BaseRecyclerAdapter<Comic> {

    public HistoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public void onClear() {
        list.clear();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setText(R.id.tv_history_search, item.getTitle());
    }
}
