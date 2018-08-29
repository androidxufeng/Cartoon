/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-23, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;
import android.view.View;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.bean.DownState;
import com.tplink.cartoon.data.common.Constants;

public class DownloadChapterlistAdapter extends BookShelfAdapter<DBDownloadItem> {
    public DownloadChapterlistAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(final BaseRecyclerHolder holder, final DBDownloadItem item, final int position) {
        holder.setText(R.id.tv_title, item.getChapters_title());
        holder.setProgress(R.id.pg_download, item.getNum(), item.getCurrentNum());

        if (isEditing()) {
            holder.setVisibility(R.id.iv_download_select, View.VISIBLE);
            if (mMap.size() != 0 && mMap.get(position) == Constants.CHAPTER_SELECTED) {
                holder.setImageResource(R.id.iv_download_select, R.drawable.item_selected);
            } else {
                holder.setImageResource(R.id.iv_download_select, R.drawable.item_select);
            }
        } else {
            holder.setVisibility(R.id.iv_download_select, View.GONE);
        }

        switch (item.getState()) {
            case DownState.NONE:
                /*起始状态*/
                if (item.getNum() == 0) {
                    holder.setText(R.id.tv_progress, "等待下载");
                } else {
                    holder.setText(R.id.tv_progress, "等待下载:" + item.getCurrentNum() + "/" + item.getNum());
                }
                holder.setImageResource(R.id.iv_download_status, R.drawable.item_downloading);
                break;
            case DownState.START:
                /*起始状态*/
                holder.setText(R.id.tv_progress, "解析下载地址");
                holder.setImageResource(R.id.iv_download_status, R.drawable.item_downloading);
                break;
            case DownState.PAUSE:
//                holder.setText(R.id.tv_progress, "等待下载" + item.getCurrentNum() + "/" + item.getNum());
                break;
            case DownState.DOWN:
                holder.setText(R.id.tv_progress, "正在下载:" + item.getCurrentNum() + "/" + item.getNum());
                holder.setImageResource(R.id.iv_download_status, R.drawable.item_downloading);
                break;
            case DownState.STOP:
                if (item.getNum() == 0) {
                    holder.setText(R.id.tv_progress, "下载停止");
                } else {
                    holder.setText(R.id.tv_progress, "下载停止:" + item.getCurrentNum() + "/" + item.getNum());
                }
                holder.setImageResource(R.id.iv_download_status, R.drawable.item_pasue);
                break;
            case DownState.ERROR:
                if (item.getNum() == 0) {
                    holder.setText(R.id.tv_progress, "下载错误");
                } else {
                    holder.setText(R.id.tv_progress, "下载错误:" + item.getCurrent_num() + "/" + item.getNum());
                }
                holder.setImageResource(R.id.iv_download_status, R.drawable.item_pasue);
                break;
            case DownState.FINISH:
                holder.setText(R.id.tv_progress, "下载完成" + item.getCurrentNum() + "/" + item.getNum());
                holder.setImageResource(R.id.iv_download_status, R.drawable.item_finish);
                break;
        }
    }
}
