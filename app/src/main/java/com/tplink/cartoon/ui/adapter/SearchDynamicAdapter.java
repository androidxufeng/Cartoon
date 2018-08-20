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
import com.tplink.cartoon.utils.TextUtil;

public class SearchDynamicAdapter extends BaseRecyclerAdapter<SearchBean> {
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public SearchDynamicAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, SearchBean item, int position) {
        holder.setHtmlText(R.id.tv_dynamic_search, TextUtil.getSearchText(key, item.getTitle()));
    }
}
