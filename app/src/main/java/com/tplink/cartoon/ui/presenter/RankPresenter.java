/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-29, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.LoadingItem;
import com.tplink.cartoon.ui.activity.RankActivity;
import com.tplink.cartoon.ui.source.rank.RankDataSource;
import com.tplink.cartoon.utils.ShowErrorTextUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class RankPresenter extends BasePresenter<RankDataSource, RankActivity> {

    private int page = 1;
    private List<Comic> mList;
    private boolean isloadingdata;
    private String type = "upt";

    public RankPresenter(RankActivity view) {
        super(view);
        mList = new ArrayList<>();
    }

    @Override
    protected RankDataSource initDataSource() {
        return new RankDataSource();
    }

    public void loadData() {
        if (!isloadingdata) {
            isloadingdata = true;
            mDataSource.getRankList(getCurrentTime(), type, page)
                    .compose(mView.<List<Comic>>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                        @Override
                        public void onNext(List<Comic> comics) {
                            mList.addAll(comics);
                            List<Comic> temp = new ArrayList<>(mList);
                            if (comics.size() != 0) {
                                temp.add(new LoadingItem(true));
                                mView.fillData(temp);
                                isloadingdata = false;
                            } else {
                                temp.add(new LoadingItem(false));
                                mView.fillData(temp);
                            }

                        }

                        @Override
                        public void onError(Throwable t) {
                            mView.showErrorView(ShowErrorTextUtil.ShowErrorText(t));
                        }

                        @Override
                        public void onComplete() {
                            mView.getDataFinish();
                            page++;
                            int position = 0;
                            if (type.equals("upt")) {
                                position = 0;
                            } else if (type.equals("pay")) {
                                position = 1;
                            } else if (type.equals("pgv")) {
                                position = 2;
                            } else {
                                position = 3;
                            }
                            mView.setType(position);
                        }
                    });
        }
    }

    public void setType(String type) {
        this.type = type;
        this.mList.clear();
        this.page = 1;
        this.isloadingdata = false;
        loadData();
    }

    public String getType() {
        return type;
    }
}