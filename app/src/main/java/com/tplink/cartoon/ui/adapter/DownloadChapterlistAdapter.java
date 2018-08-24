/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-23, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.bean.DownState;

import java.util.List;

public class DownloadChapterlistAdapter extends BaseRecyclerAdapter<DBDownloadItem> {
    public DownloadChapterlistAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public List<DBDownloadItem> getLists() {
        return list;
    }

    @Override
    public void convert(final BaseRecyclerHolder holder, final DBDownloadItem item, final int position) {
        holder.setText(R.id.tv_title, item.getChapters_title());
        holder.setProgress(R.id.pg_download, item.getNum(), item.getCurrentNum());
        switch (item.getState()) {
            case DownState.NONE:
                /*起始状态*/
                if (item.getNum() == 0) {
                    holder.setText(R.id.tv_progress, "等待下载");
                } else {
                    holder.setText(R.id.tv_progress, "等待下载:" + item.getCurrentNum() + "/" + item.getNum());
                }
                break;
            case DownState.START:
                /*起始状态*/
                holder.setText(R.id.tv_progress, "解析下载地址");
                break;
            case DownState.PAUSE:
//                holder.setText(R.id.tv_progress, "等待下载" + item.getCurrentNum() + "/" + item.getNum());
                break;
            case DownState.DOWN:
                holder.setText(R.id.tv_progress, "正在下载:" + item.getCurrentNum() + "/" + item.getNum());
                break;
            case DownState.STOP:
                if (item.getNum() == 0) {
                    holder.setText(R.id.tv_progress, "下载停止");
                } else {
                    holder.setText(R.id.tv_progress, "下载停止:" + item.getCurrentNum() + "/" + item.getNum());
                }
                break;
            case DownState.ERROR:
                holder.setText(R.id.tv_progress, "下载错误" + item.getCurrentNum() + "/" + item.getNum());
                break;
            case DownState.FINISH:
                holder.setText(R.id.tv_progress, "下载完成" + item.getCurrentNum() + "/" + item.getNum());
                break;
        }
    }
}
