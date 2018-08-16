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


    //已经选中的章节
    private ArrayList<Integer> SelectedChapters;
    //已经下载的章节
    private ArrayList<Integer> DownloadedChapters;

    private ArrayList<String> mChapters;
    private HashMap<Integer, Integer> map;

    public SelectDownloadPresenter(IDownloadDataSource dataSource, ISelectDownloadView view) {
        super(dataSource, view);
    }

    public SelectDownloadPresenter(IDownloadDataSource dataSource, ISelectDownloadView view, ArrayList<String> chapters) {
        super(dataSource, view);
        this.mChapters = chapters;
        initData();
    }

    public ArrayList<Integer> getSelectedChapters() {
        return SelectedChapters;
    }

    public void setSelectedChapters(ArrayList<Integer> selectedChapters) {
        SelectedChapters = selectedChapters;
    }

    public ArrayList<Integer> getDownloadedChapters() {
        return DownloadedChapters;
    }

    public void setDownloadedChapters(ArrayList<Integer> downloadedChapters) {
        DownloadedChapters = downloadedChapters;
    }

    private void initData() {
        SelectedChapters = new ArrayList<>();
        DownloadedChapters = new ArrayList<>();
        map = new HashMap<>(4);
        if (mChapters != null && mChapters.size() != 0) {
            for (int i = 0; i < mChapters.size(); i++) {
                map.put(i, Constants.CHAPTER_FREE);
            }
        }
    }

    public void addToSelected(int position) {
        SelectedChapters.add(position);
        map.put(position, Constants.CHAPTER_SELECTED);
        mView.addToDownloadList(map);
    }
}
