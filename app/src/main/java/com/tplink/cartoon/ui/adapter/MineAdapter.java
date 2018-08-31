/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.MineTitle;

public class MineAdapter extends BaseRecyclerAdapter<MineTitle> {
    private boolean isNight;

    public MineAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, MineTitle item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setImageResource(R.id.iv_mine_icon, item.getResID());
        holder.setText(R.id.tv_size, item.getSize());
        if (position == 0) {
            if (!isNight) {
                holder.setImageResource(R.id.iv_select, R.drawable.item_select_dark);
            } else {
                holder.setImageResource(R.id.iv_select, R.drawable.item_selected_dark);
            }
        } else {
            holder.setImageResource(R.id.iv_select, R.drawable.add_more);
        }
    }

    public void setNight(boolean isNight) {
        this.isNight = isNight;
        notifyDataSetChanged();
    }
}
