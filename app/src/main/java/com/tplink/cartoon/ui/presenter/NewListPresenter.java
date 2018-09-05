/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-9-5, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.support.v4.view.PagerAdapter;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.LoadingItem;
import com.tplink.cartoon.ui.activity.NewListActivity;
import com.tplink.cartoon.ui.source.newlist.NewListDataSource;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class NewListPresenter extends BasePresenter<NewListDataSource, NewListActivity> {

    private boolean isloadingdata;
    private List<Comic> mList;
    private int page = 1;

    public NewListPresenter(NewListActivity view) {
        super(view);
        mList = new ArrayList<>();
        this.isloadingdata = false;
    }

    public void loadData() {
        if (!isloadingdata) {
            isloadingdata = true;

            mDataSource.getNewComicList(page)
                    .compose(mView.<List<Comic>>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<List<Comic>>() {
                        @Override
                        public void onNext(List<Comic> comics) {
                            mList.addAll(comics);
                            List<Comic> temp = new ArrayList<>(mList);
                            if (comics.size() == 12) {
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

                        }

                        @Override
                        public void onComplete() {
                            page++;
                        }
                    });
        }
    }

    @Override
    protected NewListDataSource initDataSource() {
        return new NewListDataSource();
    }
}
