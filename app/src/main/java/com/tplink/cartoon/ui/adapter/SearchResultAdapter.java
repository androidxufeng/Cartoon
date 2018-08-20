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

public class SearchResultAdapter extends BaseRecyclerAdapter<Comic> {
    private String key;

    public SearchResultAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setHtmlText(R.id.tv_title, TextUtil.getSearchText(key, item.getTitle()));
        holder.setHtmlText(R.id.tv_author, TextUtil.getSearchText(key, item.getAuthor()));
        holder.setText(R.id.tv_update, item.getUpdates());
        holder.setText(R.id.tv_tag, item.getTags().toString());
        holder.setImageByUrl(R.id.iv_cover, item.getCover());
    }
}
