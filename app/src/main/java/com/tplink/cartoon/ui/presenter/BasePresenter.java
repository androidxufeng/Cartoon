/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import com.tplink.cartoon.ui.source.IDataSource;
import com.tplink.cartoon.ui.view.BaseView;

public abstract class BasePresenter<M extends IDataSource, V extends BaseView> {

    protected final M mDataSource;
    protected final V mView;

    public BasePresenter(V view) {
        mView = view;
        mDataSource = initDataSource();
    }

    protected abstract M initDataSource();


    public long getCurrentTime() {
        java.util.Date date = new java.util.Date();
        long datetime = date.getTime();
        return datetime;
    }
}
