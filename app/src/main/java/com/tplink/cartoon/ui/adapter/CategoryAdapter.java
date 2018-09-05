/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-31, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;
import android.view.View;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Type;

import java.util.HashMap;
import java.util.Map;

public class CategoryAdapter extends BaseRecyclerAdapter<Type> {
    private Map<String, Integer> mSelectMap;

    public void setSelectMap(Map<String, Integer> mSelectMap) {
        this.mSelectMap = mSelectMap;
        notifyDataSetChanged();
    }

    public CategoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mSelectMap = new HashMap<>();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Type item, int position) {
        if (item.getValue() == mSelectMap.get(item.getType())) {
            holder.setVisibility(R.id.iv_title_bg, View.VISIBLE);
            holder.setTextViewAppearanceColor(R.id.tv_title, R.style.colorTextPrimary);
        } else {
            holder.setVisibility(R.id.iv_title_bg, View.GONE);
            holder.setTextViewAppearanceColor(R.id.tv_title, R.style.colorTextBlack);
        }
        holder.setText(R.id.tv_title, item.getTitle());

    }
}