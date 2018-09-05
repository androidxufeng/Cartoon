/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-31, xufeng, Create file
 */
package com.tplink.cartoon.ui.presenter;

import android.content.Intent;
import android.util.Log;

import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.LoadingItem;
import com.tplink.cartoon.data.bean.Type;
import com.tplink.cartoon.data.common.Constants;
import com.tplink.cartoon.ui.activity.CategoryActivity;
import com.tplink.cartoon.ui.source.category.CategoryDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class CategoryPresenter extends BasePresenter<CategoryDataSource, CategoryActivity> {
    private List<Type> mSelectList;
    private Map<String, Integer> mSelectMap;
    private int page;
    private List<Comic> mList;
    private boolean isloadingdata;
    private String mCategroyTitle;
    private String[] themes,finish,audience,nation;

    public CategoryPresenter(CategoryActivity view, Intent intent) {
        super(view);
        mSelectList = new ArrayList<>();
        mSelectMap = new HashMap<>();
        page = 1;
        this.isloadingdata = false;
        mList = new ArrayList<>();

        mSelectMap.put(Constants.CATEGORY_TITLE_THEME, intent.getIntExtra(Constants.CATEGORY_TITLE_THEME, 0));
        mSelectMap.put(Constants.CATEGORY_TITLE_FINISH, intent.getIntExtra(Constants.CATEGORY_TITLE_FINISH, 0));
        mSelectMap.put(Constants.CATEGORY_TITLE_AUDIENCE, intent.getIntExtra(Constants.CATEGORY_TITLE_AUDIENCE, 0));
        mSelectMap.put(Constants.CATEGORY_TITLE_NATION, intent.getIntExtra(Constants.CATEGORY_TITLE_NATION, 0));
    }

    @Override
    protected CategoryDataSource initDataSource() {
        return new CategoryDataSource();
    }

    public void loadData() {
        themes = new String[]{"全部", "爆笑", "热血", "冒险", "恐怖", "科幻",
                "魔幻", "玄幻", "校园", "悬疑", "推理", "萌系", "穿越", "后宫"};
        for (int i = 0; i < 14; i++) {
            Type item = new Type(Constants.CATEGORY_TITLE_THEME, themes[i], i);
            mSelectList.add(item);
        }
        finish = new String[]{"全部", "连载", "完结", null, null, null, null};
        for (int i = 0; i < 7; i++) {
            Type item = new Type(Constants.CATEGORY_TITLE_FINISH, finish[i], i);
            mSelectList.add(item);
        }
        audience = new String[]{"全部", "少年", "少女", "青年", "少儿", null, null};
        for (int i = 0; i < 7; i++) {
            Type item = new Type(Constants.CATEGORY_TITLE_AUDIENCE, audience[i], i);
            mSelectList.add(item);
        }
        nation = new String[]{"全部", "内地", "港台", "韩国", "日本", null, null};
        for (int i = 0; i < 7; i++) {
            Type item = new Type(Constants.CATEGORY_TITLE_NATION, nation[i], i);
            mSelectList.add(item);
        }

        mView.fillSelectData(mSelectList, mSelectMap);
        loadCategoryList();
    }

    public void loadCategoryList() {
        if (!isloadingdata) {
            isloadingdata = true;
            mDataSource.getCategroyList(mSelectMap, page)
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
                            Log.d("ceshi", "onError: loadcategorylist" + t);
                        }

                        @Override
                        public void onComplete() {
                            mView.getDataFinish();
                            page++;
                        }
                    });
        }
    }

    public void onItemClick(Type type) {
        if (type.getTitle() != null) {
            mSelectMap.put(type.getType(), type.getValue());
            mView.setMap(mSelectMap);
            this.mList.clear();
            this.page = 1;
            this.isloadingdata = false;
            loadCategoryList();
        }
    }

    public String getTitle() {
        mCategroyTitle = "·精品";
        if (mSelectMap.get(Constants.CATEGORY_TITLE_THEME) != 0) {
            mCategroyTitle = mCategroyTitle + "&" + themes[mSelectMap.get(Constants.CATEGORY_TITLE_THEME)];
        }
        if (mSelectMap.get(Constants.CATEGORY_TITLE_FINISH) != 0) {
            mCategroyTitle = mCategroyTitle + "&" + finish[mSelectMap.get(Constants.CATEGORY_TITLE_FINISH)];
        }
        if (mSelectMap.get(Constants.CATEGORY_TITLE_AUDIENCE) != 0) {
            mCategroyTitle = mCategroyTitle + "&" + audience[mSelectMap.get(Constants.CATEGORY_TITLE_AUDIENCE)];
        }
        if (mSelectMap.get(Constants.CATEGORY_TITLE_NATION) != 0) {
            mCategroyTitle = mCategroyTitle + "&" + nation[mSelectMap.get(Constants.CATEGORY_TITLE_NATION)];
        }
        mCategroyTitle = mCategroyTitle + "·";
        return mCategroyTitle;
    }
}
