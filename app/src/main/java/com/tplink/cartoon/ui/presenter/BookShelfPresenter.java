/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-14, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import com.tplink.cartoon.ui.fragment.BookShelfFragment;
import com.tplink.cartoon.ui.source.BookShelf.BookShelfDataSource;

public class BookShelfPresenter extends BasePresenter<BookShelfDataSource, BookShelfFragment> {

    public BookShelfPresenter(BookShelfFragment view) {
        super(view);
    }

    @Override
    protected BookShelfDataSource initDataSource() {
        return new BookShelfDataSource(mView.getContext());
    }
}
