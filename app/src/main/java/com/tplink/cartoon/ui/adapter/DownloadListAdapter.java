/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.adapter;

import android.content.Context;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.DownInfo;
import com.tplink.cartoon.net.download.HttpDownOnNextListener;
import com.tplink.cartoon.utils.LogUtil;

import java.util.List;

import static com.tplink.cartoon.data.bean.DownState.DOWN;
import static com.tplink.cartoon.data.bean.DownState.ERROR;
import static com.tplink.cartoon.data.bean.DownState.FINISH;
import static com.tplink.cartoon.data.bean.DownState.PAUSE;
import static com.tplink.cartoon.data.bean.DownState.START;
import static com.tplink.cartoon.data.bean.DownState.STOP;

public class DownloadListAdapter extends BaseRecyclerAdapter<DownInfo> {

    public DownloadListAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public List<DownInfo> getLists() {
        return list;
    }

    @Override
    public void convert(final BaseRecyclerHolder holder, final DownInfo item, final int position) {
        holder.setText(R.id.tv_title, item.getSavePath());
        holder.setProgress(R.id.pg_download, item.getCountLength(), item.getReadLength());
        item.setListener(new HttpDownOnNextListener<DownInfo>() {
            @Override
            public void onNext(DownInfo downInfo) {
                holder.setText(R.id.tv_progress, "下载暂停");
                super.onPuase();
            }

            @Override
            public void onStart() {
                holder.setText(R.id.tv_progress, "开始下载");
                LogUtil.v(item.getSavePath() + "开始下载");
            }

            @Override
            public void onComplete() {
                holder.setText(R.id.tv_progress, "下载完成");
                holder.setProgress(R.id.pg_download, item.getCountLength(), item.getReadLength());
                LogUtil.v(item.getSavePath() + "下载完成");
            }

            @Override
            public void updateProgress(long readLength, long countLength) {
                holder.setText(R.id.tv_progress, "下载中");
                holder.setProgress(R.id.pg_download, countLength, readLength);
                LogUtil.d("url=" + item.getSavePath() + ",readLength=" + readLength +
                        ",countLength=" + countLength + ",position=" + position);
            }
        });
        switch (item.getState()) {
            case START:
                /*起始状态*/
                holder.setText(R.id.tv_progress, "点击下载");
                break;
            case PAUSE:
                holder.setText(R.id.tv_progress, "下载暂停");
                break;
            case DOWN:
                //manager.startDown(apkApi);
                break;
            case STOP:
                holder.setText(R.id.tv_progress, "下载停止");
                break;
            case ERROR:
                holder.setText(R.id.tv_progress, "下载错误");
                break;
            case FINISH:
                holder.setText(R.id.tv_progress, "下载完成");
                break;
        }
        holder.setText(R.id.tv_title, item.getSavePath());
        holder.setProgress(R.id.pg_download, item.getCountLength(), item.getReadLength());
    }
}
