/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-23, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.content.Intent;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.DBChapters;
import com.tplink.cartoon.data.bean.DBDownloadItem;
import com.tplink.cartoon.data.bean.DownState;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.db.DaoHelper;
import com.tplink.cartoon.ui.activity.DownloadChapterlistActivity;
import com.tplink.cartoon.ui.source.download.DownloadListDataSource;
import com.tplink.cartoon.utils.FileUtil;
import com.tplink.cartoon.utils.LogUtil;
import com.tplink.cartoon.utils.ShowErrorTextUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.ResponseBody;

public class DownloadChapterlistPresenter extends BasePresenter
        <DownloadListDataSource, DownloadChapterlistActivity> {
    private final CompositeDisposable mCompositeDisposable;
    private Comic mComic;
    private HashMap<Integer, Integer> mMap;
    private List<DBDownloadItem> mLists;
    private final DaoHelper<DBDownloadItem> mDaoHelper;
    //下载队列
    private LinkedHashMap<String, DownloadComicDisposableObserver> subMap;
    //下载章节数，同时允许存在四个
    private TreeMap<Integer, DBDownloadItem> downloadMap;
    private final static int DOWNLOADNUM = 4;
    //已经下载完成的个数
    int downloadedNum = 0;
    /**
     * 0 下载中
     * 1 停止下载
     * 2 下载完成
     */
    public static final int DOWNLOADING = 0;
    public static final int STOP_DOWNLOAD = 1;
    public static final int FINISH = 2;

    public int isAllDownload = DOWNLOADING;

    public DownloadChapterlistPresenter(DownloadListDataSource dataSource, DownloadChapterlistActivity view, Intent intent) {
        super(dataSource, view);
        mCompositeDisposable = new CompositeDisposable();
        mComic = (Comic) intent.getSerializableExtra(Constants.COMIC);
        mMap = (HashMap<Integer, Integer>) intent.getSerializableExtra(Constants.COMIC_SELECT_DOWNLOAD);
        mLists = new ArrayList<>();
        mDaoHelper = new DaoHelper<>(view.getApplicationContext());
        subMap = new LinkedHashMap<>();
        downloadMap = new TreeMap<>();
    }

    public Comic getComic() {
        return mComic;
    }

    /**
     * 初始化按照章節下載
     */
    public void initData() {
        DisposableSubscriber<List<DBDownloadItem>> disposableSubscriber =
                mDataSource.getDbDownloadItemFromDBWithInsert(mComic, mMap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<List<DBDownloadItem>>() {
                            @Override
                            public void onNext(List<DBDownloadItem> items) {
                                mLists.addAll(items);
                                mView.fillData(mLists);
                                //判断有多少是之前已经下载过的
                                for (int i = 0; i < items.size(); i++) {
                                    if (items.get(i).getState() == DownState.FINISH) {
                                        downloadedNum++;
                                    }
                                }
                                //判断是否全部下载完了
                                if (downloadedNum == mLists.size()) {
                                    mView.onDownloadFinished();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
                                LogUtil.e(mComic.getTitle() + "从数据库中拉取本地下载列表数据失败" + e.toString());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        mCompositeDisposable.add(disposableSubscriber);
    }


    /**
     * 开始所有下载
     */
    public void startAll() {
        //找出最前面四个可以下载的bean
        for (int i = 0; i < mLists.size(); i++) {
            if (mLists.get(i).getState() == DownState.NONE) {
                if (downloadMap.size() < DOWNLOADNUM) {
                    startDown(mLists.get(i), i);
                }
            }
        }
    }

    public void reStartAll() {
        //找出最前面四个可以下载的bean
        for (int i = 0; i < mLists.size(); i++) {
            if (mLists.get(i).getState() != DownState.FINISH) {
                mLists.get(i).setState(DownState.NONE);
                if (downloadMap.size() < DOWNLOADNUM) {
                    startDown(mLists.get(i), i);
                } else {
                    mView.getDataFinish();
                }
            }
        }
    }

    /**
     * 暂停某个下载
     *
     * @param info
     * @param position
     * @param isContinue 是否继续下载
     */
    public void stop(DBDownloadItem info, int position, boolean isContinue) {
        if (info == null) return;
        info.setState(DownState.STOP);
        if (info.getChaptersUrl() != null && info.getCurrentNum() + 1 < info.getChaptersUrl().size()) {
            String url = info.getChaptersUrl().get(info.getCurrentNum() + 1);
            //中断单张图片的下载
            if (subMap.containsKey(url)) {
                DownloadComicDisposableObserver subscriber = subMap.get(url);
                subscriber.dispose();//解除请求
                subMap.remove(url);
                LogUtil.v(url + ":停止下载");
            }
        }
        //中断整个章节的下载，并且切换章节
        if (downloadMap.containsKey(position) && isContinue) {
            downloadMap.remove(position);
            for (int i = 0; i < mLists.size(); i++) {
                if (mLists.get(i).getState() == DownState.NONE) {
                    downloadMap.put(i, mLists.get(i));
                    startDown(mLists.get(i), i);
                    break;
                }
            }
        }
        mDaoHelper.update(info);
        mView.updateView(position);
    }

    /**
     * 暂停某个下载
     *
     * @param info
     */
    public void pause(DBDownloadItem info, int position) {
        LogUtil.d("testA", "点击了暂停");
        if (info == null) {
            return;
        }
        info.setState(DownState.PAUSE);
        String url = info.getChaptersUrl().get(info.getCurrentNum() + 1);
        if (subMap.containsKey(url)) {
            DownloadComicDisposableObserver subscriber = subMap.get(url);
            subscriber.dispose();//解除请求
            subMap.remove(url);
            LogUtil.v(url + ":暂停下载");
        }
        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
        mDaoHelper.update(info);
        mView.updateView(position);

    }

    /**
     * 开始某个下载
     *
     * @param info
     */
    public void startDown(final DBDownloadItem info, final int position) {
        //加入到下载队列中
        downloadMap.put(position, info);
        if (info.getNum() == 0) {
            //修改状态
            info.setState(DownState.START);
            mView.updateView(position);

            DisposableSubscriber<DBChapters> disposableSubscriber = mDataSource.getDownloadChaptersList(mComic.getId(), info.getChapters())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<DBChapters>() {
                        @Override
                        public void onNext(DBChapters chapters) {
                            if (info.getState() != DownState.STOP) {
                                //修改状态
                                info.setState(DownState.DOWN);
                                //设置下载地址
                                info.setChapters_url(chapters.getComiclist());
                                info.setNum(mLists.size());
                                info.setCurrentNum(0);
                                //把获取到的下载地址存进数据库
                                mDaoHelper.update(info);
                                mView.updateView(position);
                                if (mLists != null && mLists.size() != 0) {
                                    downloadChapter(info, info.getCurrentNum(), position);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            mCompositeDisposable.add(disposableSubscriber);
        } else {
            //修改状态
            info.setState(DownState.DOWN);
            mDaoHelper.update(info);
            mView.updateView(position);
            downloadChapter(info, info.getCurrentNum(), position);
        }
    }

    /**
     * 递归下载每一话的所有图片
     *
     * @param info
     * @param page
     */
    private void downloadChapter(final DBDownloadItem info, final int page, int postion) {
        DownloadComicDisposableObserver observer = new DownloadComicDisposableObserver(page, postion);
        Observable<ResponseBody> observable = mDataSource.download(info, page);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<ResponseBody, DBDownloadItem>() {

                    @Override
                    public DBDownloadItem apply(ResponseBody responseBody) throws Exception {
                        //把图片保存到SD卡
                        FileUtil.saveImgToSdCard(responseBody.byteStream(), FileUtil.SDPATH + FileUtil.COMIC + info.getComic_id() + "/" + info.getChapters() + "/", page + ".png");
                        ArrayList<String> paths = (ArrayList<String>) info.getChaptersPath();
                        if (paths == null) {
                            paths = new ArrayList<>();
                        }
                        paths.add(FileUtil.SDPATH + FileUtil.COMIC + info.getComic_id() + "/" + info.getChapters() + "/" + page + ".png");
                        //保存存储位置
                        info.setChaptersPath(paths);
                        return info;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        subMap.put(info.getChaptersUrl().get(page), observer);
    }

    /**
     * 设置为可以下载的等待状态
     *
     * @param items
     * @param position
     */
    public void ready(DBDownloadItem items, int position) {
        if (downloadMap.size() < DOWNLOADNUM) {
            startDown(items, position);
        } else {
            items.setState(DownState.PAUSE);
            mDaoHelper.update(items);
            mView.updateView(position);
        }
    }


    /**
     * 暂停所有下载
     */
    public void pauseAll() {
        DisposableSubscriber<Boolean> disposable = mDataSource.updateDownloadItemsList(mLists)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            LogUtil.d("所有状态保存在数据库成功");
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.e(t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mCompositeDisposable.add(disposable);
    }

    /**
     * 停止所有下载
     */
    public void stopAll() {
        for (int i = 0; i < mLists.size(); i++) {
            DBDownloadItem items = mLists.get(i);
            if (items.getState() != DownState.FINISH) {
                items.setState(DownState.STOP);
                if (items.getState() == DownState.DOWN) {
                    stop(items, i, false);
                }
                downloadMap.clear();
            }
        }
        mView.getDataFinish();
    }

    public void toComicChapter(DBDownloadItem info) {
        //IntentUtil.ToComicChapter();
    }

    public class DownloadComicDisposableObserver extends DisposableObserver<DBDownloadItem> {
        int page;
        DBDownloadItem info;
        int position;

        public DownloadComicDisposableObserver(int page, int position) {
            this.page = page;
            this.position = position;
        }

        @Override
        public void onNext(@NonNull DBDownloadItem dbDownloadItem) {
            info = dbDownloadItem;
            LogUtil.d(page + "/" + info.getNum() + "下载完成");
            //从队列中移除
            subMap.remove(info.getChaptersUrl().get(page));
            //写一个递归继续去下载这一话的下一张图片
            if (page < info.getNum() - 1) {
                if (info.getState() == DownState.DOWN) {
                    downloadChapter(info, page + 1, position);
                }
            } else {
                //如果这一话下载完成
                downloadedNum++;
                //修改状态
                info.setState(DownState.FINISH);
                downloadMap.remove(position);
                //遍历去寻找下一话
                for (int i = 0; i < mLists.size(); i++) {
                    if (mLists.get(i).getState() == DownState.NONE) {
                        downloadMap.put(i, mLists.get(i));
                        //开始下载下一话
                        startDown(mLists.get(i), i);
                        break;
                    }
                }
                if (downloadedNum == mLists.size()) {
                    mView.showToast(mComic.getTitle() + "下载完成,共下载" + downloadedNum + "话");
                    mView.onDownloadFinished();
                    isAllDownload = FINISH;
                }
            }
            //把已经下载完成的写入
            info.setCurrentNum(page + 1);
            //更新数据库
            mDaoHelper.update(info);
            //为了防止点击了stop之后，仍然刷新UI，即使图片下载已经完成，仍不刷新UI
            if (info.getState() != DownState.STOP) {
                mView.updateView(position);
            }

        }


        @Override
        public void onComplete() {

        }

        @Override
        public void onError(@NonNull Throwable e) {

        }
    }
}