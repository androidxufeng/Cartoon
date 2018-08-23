/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-16, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;
import android.graphics.Color;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.common.Constants;

import java.util.HashMap;

public class SelectDownloadAdapter extends BaseRecyclerAdapter<String> {

    private HashMap<Integer, Integer> mHashMap;
    private boolean isOrder = true;

    public SelectDownloadAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, String item, int position) {
        if (!isOrder) {
            position = list.size() - position - 1;
        }
        holder.setText(R.id.tv_position, (position + 1) + "-" + list.get(position));
        if (mHashMap != null) {
            switch (mHashMap.get(position)) {
                case Constants.CHAPTER_SELECTED:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper, R.drawable.btn_selected_download);
                    holder.setTextViewColor(R.id.tv_position, Color.WHITE);
                    break;
                case Constants.CHAPTER_DOWNLOAD:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper, R.drawable.btn_downloaded_download);
                    holder.setTextViewColor(R.id.tv_position, Color.parseColor("#666666"));
                    break;
                case Constants.CHAPTER_FREE:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper, R.drawable.btn_select_download);
                    holder.setTextViewColor(R.id.tv_position, Color.parseColor("#666666"));
                    break;
                default:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper, R.drawable.btn_select_download);
                    holder.setTextViewColor(R.id.tv_position, Color.parseColor("#666666"));
                    break;
            }
        }
    }

    public void setHashMap(HashMap<Integer, Integer> hashMap) {
        mHashMap = hashMap;
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }
}
