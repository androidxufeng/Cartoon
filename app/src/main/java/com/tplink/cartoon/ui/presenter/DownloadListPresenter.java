/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-22, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.content.Intent;
import android.os.Environment;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.activity.DownloadListActivity;
import com.tplink.cartoon.ui.source.download.DownloadListDataSource;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class DownloadListPresenter extends BasePresenter<DownloadListDataSource, DownloadListActivity> {

    private final CompositeDisposable mCompositeDisposable;

    private Comic mComic;
    private HashMap<Integer, Integer> mMap;
    private List<DBDownloadItem> mLists;

    public DownloadListPresenter(DownloadListDataSource dataSource, DownloadListActivity view, Intent intent) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
        mComic = (Comic) intent.getSerializableExtra(Constants.COMIC);
        mMap = (HashMap<Integer, Integer>) intent.getSerializableExtra(Constants.COMIC_SELECT_DOWNLOAD);
    }

    public void initData() {
        mLists = new ArrayList<>();
        DBDownloadItem item;
        for (Map.Entry<Integer, Integer> entry : mMap.entrySet()) {
            if (entry.getValue() != Constants.CHAPTER_FREE) {
                item = new DBDownloadItem();
                item.setComic_id(mComic.getId());
                item.setTitle(mComic.getTitle());
                item.setChapters(entry.getKey());
                item.setChapters_title(mComic.getChapters().get(entry.getKey()));
                mLists.add(item);
            }
        }
        if (mLists != null && mLists.size() != 0) {
            mView.fillData(mLists);
        }
    }

    public void downloadFile(String url) {
        DisposableSubscriber<Response<ResponseBody>> disposable = mDataSource.downloadFile(url)
                .compose(mView.<Response<ResponseBody>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseBody) {
                        try {
                            InputStream is = responseBody.body().byteStream();
                            File file = new File(Environment.getExternalStorageDirectory(), "text_img.png");
                            FileOutputStream fos = new FileOutputStream(file);
                            BufferedInputStream bis = new BufferedInputStream(is);
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = bis.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                                fos.flush();
                            }
                            fos.close();
                            bis.close();
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
