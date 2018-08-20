/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.SearchBean;

public class SearchDynamicAdapter extends BaseRecyclerAdapter<SearchBean> {
    public SearchDynamicAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, SearchBean item, int position) {
        holder.setText(R.id.tv_dynamic_search, item.getTitle());
    }
}
