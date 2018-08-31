/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-16, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

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
//                    holder.setTextViewColor(R.id.tv_position, ContextCompat.getColor(context, R.color.colorBg));
                    holder.setVisibility(R.id.iv_download_status, View.GONE);
                    break;
                case Constants.CHAPTER_DOWNLOAD:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper, R.drawable.btn_downloaded_download);
                    holder.setTextViewAppearanceColor(R.id.tv_position,R.style.colorTextBlack);
                    holder.setVisibility(R.id.iv_download_status, View.VISIBLE);
                    holder.setImageResource(R.id.iv_download_status, R.drawable.icon_download_finished);
                    break;
                case Constants.CHAPTER_DOWNLOADING:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper, R.drawable.btn_downloaded_download);
                    holder.setTextViewAppearanceColor(R.id.tv_position,R.style.colorTextBlack);
                    holder.setVisibility(R.id.iv_download_status, View.VISIBLE);
                    holder.setImageResource(R.id.iv_download_status, R.drawable.icon_download_downloading);
                    break;
                case Constants.CHAPTER_FREE:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper, R.drawable.btn_select_download);
                    holder.setTextViewAppearanceColor(R.id.tv_position,R.style.colorTextBlack);
                    holder.setVisibility(R.id.iv_download_status, View.GONE);
                    break;
                default:
                    holder.setFrameLayoutImageResource(R.id.fl_position_wrapper, R.drawable.btn_select_download);
                    holder.setTextViewAppearanceColor(R.id.tv_position,R.style.colorTextBlack);
                    holder.setVisibility(R.id.iv_download_status, View.GONE);
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
