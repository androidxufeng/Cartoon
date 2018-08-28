/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-27, xufeng, Create file
 */
package com.tplink.cartoon.ui.fragment.bookshelf;

import android.app.Activity;

import com.tplink.cartoon.ui.activity.HomeActivity;
import com.tplink.cartoon.ui.fragment.BaseFragment;
import com.tplink.cartoon.ui.presenter.BasePresenter;

public abstract class BaseBookShelfFragment<P extends BasePresenter> extends BaseFragment<P> {

    protected HomeActivity mHomeActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHomeActivity = (HomeActivity) getActivity();
    }

    public abstract void onEditList(boolean isEditing);

    public abstract void onDelete();

    public abstract void onSelect();
}
