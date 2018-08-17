/*
 *
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-16, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.source.download.IDownloadDataSource;
import com.tplink.cartoon.ui.view.ISelectDownloadView;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectDownloadPresenter extends BasePresenter<IDownloadDataSource, ISelectDownloadView> {


    private ArrayList<String> mChapters;
    private HashMap<Integer, Integer> map;

    private boolean isSelectAll;
    private int selectCount;

    public SelectDownloadPresenter(IDownloadDataSource dataSource, ISelectDownloadView view) {
        super(dataSource, view);
    }

    public SelectDownloadPresenter(IDownloadDataSource dataSource, ISelectDownloadView view, ArrayList<String> chapters) {
        super(dataSource, view);
        this.mChapters = chapters;
        initData();
    }

    private void initData() {

        map = new HashMap<>(4);
        if (mChapters != null && mChapters.size() != 0) {
            for (int i = 0; i < mChapters.size(); i++) {
                map.put(i, Constants.CHAPTER_FREE);
            }
        }
    }

    public void updateToSelected(int position) {
        if (map.get(position).equals(Constants.CHAPTER_FREE)) {
            map.put(position, Constants.CHAPTER_SELECTED);
            selectCount++;
            if (selectCount == mChapters.size()){
                mView.addAll();
                isSelectAll = true;
            }
        } else if (map.get(position).equals(Constants.CHAPTER_SELECTED)) {
            map.put(position, Constants.CHAPTER_FREE);
            selectCount--;
            isSelectAll = false;
            mView.removeAll();
        }
        mView.updateDownloadList(map);
    }

    public void selectOrRemoveAll() {
        if (!isSelectAll) {
            if (mChapters != null && mChapters.size() != 0) {
                for (int i = 0; i < mChapters.size(); i++) {
                    if (map.get(i) == Constants.CHAPTER_FREE) {
                        map.put(i, Constants.CHAPTER_SELECTED);
                        selectCount++;
                    }
                }
                mView.addAll();
            }
        } else {
            if (mChapters != null && mChapters.size() != 0) {
                for (int i = 0; i < mChapters.size(); i++) {
                    if (map.get(i) == Constants.CHAPTER_SELECTED) {
                        map.put(i, Constants.CHAPTER_FREE);
                    }
                }
                selectCount = 0;
                mView.removeAll();
            }
        }
        isSelectAll = !isSelectAll;
        mView.updateDownloadList(map);
    }

    public int getSelectCount() {
        return selectCount;
    }
}
