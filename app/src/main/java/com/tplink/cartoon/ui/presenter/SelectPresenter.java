/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-27, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.content.Context;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.source.IDataSource;
import com.tplink.cartoon.ui.view.ISelectDataView;
import com.tplink.cartoon.ui.widget.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class SelectPresenter<M extends IDataSource, V extends ISelectDataView>
        extends BasePresenter<M, V> {
    protected HashMap<Integer, Integer> mMap;
    protected boolean isSelectedAll;
    protected int mSelectedNum = 0;
    protected List<Comic> mComics;

    public SelectPresenter( V view) {
        super(view);
        this.mMap = new HashMap<>();
        this.isSelectedAll = false;
        this.mComics = new ArrayList<>();
    }

    public void updateToSelected(int position) {
        if (mMap.get(position) != null && mMap.get(position).equals(Constants.CHAPTER_FREE)) {
            mSelectedNum++;
            mMap.put(position, Constants.CHAPTER_SELECTED);
            if (mSelectedNum == mComics.size()) {
                mView.addAll();
                isSelectedAll = true;
            }
        } else if (mMap.get(position) != null && mMap.get(position).equals(Constants.CHAPTER_SELECTED)) {
            mMap.put(position, Constants.CHAPTER_FREE);
            mSelectedNum--;
            isSelectedAll = false;
            mView.removeAll();
        }
        mView.updateListItem(mMap, position);
    }

    public void selectOrMoveAll() {
        if (!isSelectedAll) {
            if (mComics != null && mComics.size() != 0) {
                for (int i = 0; i < mComics.size(); i++) {
                    if (mMap.get(i) == Constants.CHAPTER_FREE) {
                        mMap.put(i, Constants.CHAPTER_SELECTED);
                        mSelectedNum++;
                    }
                }
                mView.addAll();
            }
        } else {
            if (mComics != null && mComics.size() != 0) {
                for (int i = 0; i < mComics.size(); i++) {
                    if (mMap.get(i) == Constants.CHAPTER_SELECTED) {
                        mMap.put(i, Constants.CHAPTER_FREE);
                    }
                }
                mSelectedNum = 0;
                mView.removeAll();
            }
        }
        isSelectedAll = !isSelectedAll;
        mView.updateList(mMap);
    }


    /**
     * 重置选择信息
     */
    public void resetSelect() {
        for (int i = 0; i < mComics.size(); i++) {
            if (!mMap.containsKey(i)) {
                if (isSelectedAll) {
                    mMap.put(i, Constants.CHAPTER_SELECTED);
                } else {
                    mMap.put(i, Constants.CHAPTER_FREE);
                }
            }
        }
    }

    public void clearSelect() {
        mSelectedNum = 0;
        isSelectedAll = false;
        for (int i = 0; i < mComics.size(); i++) {
            mMap.put(i, Constants.CHAPTER_FREE);
        }
        mView.updateList(mMap);
    }

    public void showDeteleDialog() {
        if (mSelectedNum > 0) {
            final CustomDialog customDialog = new CustomDialog(getContext(), "提示", "确认删除选中的漫画？");
            customDialog.setListener(new CustomDialog.onClickListener() {
                @Override
                public void OnClickConfirm() {
                    deleteComic();
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                }

                @Override
                public void OnClickCancel() {
                    if (customDialog.isShowing()) {
                        customDialog.dismiss();
                    }
                }
            });
            customDialog.show();
        } else {
            mView.showToast("请选择需要删除的作品");
        }
    }

    protected abstract Context getContext();

    protected abstract void deleteComic();
}
