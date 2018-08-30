/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.content.Context;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.MineTitle;
import com.tplink.cartoon.ui.fragment.MineFragment;
import com.tplink.cartoon.ui.source.Mine.MineDataSource;
import com.tplink.cartoon.ui.widget.CustomDialog;
import com.tplink.cartoon.utils.GlideCacheUtil;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class MinePresenter extends BasePresenter<MineDataSource, MineFragment> {

    private List<MineTitle> mLists;

    private String size;

    public MinePresenter(MineFragment view) {
        super(view);
        mLists = new ArrayList<>();
    }

    @Override
    protected MineDataSource initDataSource() {
        return new MineDataSource(mView.getContext());
    }

    public void loadData() {
        size = GlideCacheUtil.getInstance().getCacheSize(mView.getContext());
        mLists.clear();

        MineTitle mTitle = new MineTitle();
        mTitle.setTitle("夜间模式");
        mTitle.setResID(R.drawable.icon_night);
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.drawable.icon_cache);
        mTitle.setTitle("清除缓存");
        mTitle.setSize(size);
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.drawable.icon_feedback);
        mTitle.setTitle("问题反馈");
        mLists.add(mTitle);
        mTitle = new MineTitle();
        mTitle.setResID(R.drawable.icon_author);
        mTitle.setTitle("关于作者");
        mLists.add(mTitle);
        mView.fillData(mLists);
        mView.getDataFinish();
    }

    public void onItemClick(int position) {
        switch (position) {
            case 0:
                switchSkin();
                break;
            case 1:
                clearCache();
                break;
            case 2:
                try {
                    IntentUtil.toQQchat(getContext(), "641983695");
                    mView.showToast("已为您跳转到作者QQ");
                } catch (Exception e) {
                    e.printStackTrace();
                    mView.showToast("请检查是否安装QQ");
                }
                break;
            case 3:
                mView.showToast("跳转到作者博客");
                IntentUtil.toUrl(getContext().getApplicationContext(), "http://blog.csdn.net/zhhr1122");
                break;
        }
    }

    /**
     * 更换皮肤
     */
    private void switchSkin() {
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        final CustomDialog customDialog = new CustomDialog(mView.getContext(), "提示", "确认清除漫画所有缓存？");
        customDialog.setListener(new CustomDialog.onClickListener() {
            @Override
            public void OnClickConfirm() {
                mDataSource.clearCache()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<String>() {
                            @Override
                            public void onNext(String s) {
                                GlideCacheUtil.getInstance().clearImageMemoryCache(mView.getContext());
                                size = s;
                                loadData();
                            }

                            @Override
                            public void onError(Throwable t) {
                                mView.showToast("清除失败" + t.toString());
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                            }

                            @Override
                            public void onComplete() {
                                mView.showToast("清除成功");
                                if (customDialog.isShowing()) {
                                    customDialog.dismiss();
                                }
                            }
                        });
            }

            @Override
            public void OnClickCancel() {
                if (customDialog.isShowing()) {
                    customDialog.dismiss();
                }
            }
        });
        customDialog.show();
    }

    private Context getContext() {
        return mView.getContext();
    }
}

