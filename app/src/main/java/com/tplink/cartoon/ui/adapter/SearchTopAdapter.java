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
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.utils.TextUtil;

public class SearchTopAdapter extends BaseRecyclerAdapter<Comic> {

    private String key;

    public SearchTopAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {

        holder.setText(R.id.tv_top_search, TextUtil.getSearchText(key, item.getTitle()));
    }
}
