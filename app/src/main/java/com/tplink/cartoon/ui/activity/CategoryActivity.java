/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-9, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.data.bean.Type;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.CategoryAdapter;
import com.tplink.cartoon.ui.adapter.CategoryListAdapter;
import com.tplink.cartoon.ui.presenter.CategoryPresenter;
import com.tplink.cartoon.ui.view.ICategoryView;
import com.tplink.cartoon.ui.widget.ElasticHeadScrollView;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CategoryActivity extends BaseActivity<CategoryPresenter> implements ICategoryView<List<Comic>>,
        BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.rv_select)
    RecyclerView mSelectRecyclerView;
    @BindView(R.id.rv_bookshelf)
    RecyclerView mSelectListRecyclerView;
    @BindView(R.id.ev_scrollview)
    ElasticHeadScrollView mScrollView;
    @BindView(R.id.tv_actionbar_category)
    TextView mCategoryText;
    @BindView(R.id.rl_actionbar_category)
    RelativeLayout mCategoryRelativeLayout;

    CategoryAdapter mSelectAdapter;
    CategoryListAdapter mCategoryAdapter;

    @OnClick(R.id.iv_back_color)
    public void back(View view) {
        super.finish();
    }

    @OnClick(R.id.iv_search)
    public void toSearch(View view) {
        IntentUtil.toSearch(this);
    }

    @Override
    public void fillSelectData(List<Type> list, Map<String, Integer> map) {
        mSelectAdapter.setSelectMap(map);
        mSelectAdapter.updateWithClear(list);
        mSelectAdapter.notifyDataSetChanged();
        mCategoryText.setText(mPresenter.getTitle());
    }

    @Override
    public void setMap(Map<String, Integer> map) {
        mSelectAdapter.setSelectMap(map);
        mCategoryText.setText(mPresenter.getTitle());
    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new CategoryPresenter(this, intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_category;
    }

    @Override
    protected void initView() {
        initWindow(false);
        mSelectAdapter = new CategoryAdapter(this, R.layout.item_category_select);
        NoScrollGridLayoutManager gridLayoutManager = new NoScrollGridLayoutManager(this, 7);
        gridLayoutManager.setScrollEnabled(false);
        mSelectRecyclerView.setLayoutManager(gridLayoutManager);
        mSelectRecyclerView.setAdapter(mSelectAdapter);

        NoScrollGridLayoutManager ListgridLayoutManager = new NoScrollGridLayoutManager(this, 3);
        ListgridLayoutManager.setScrollEnabled(false);
        mCategoryAdapter = new CategoryListAdapter(this, R.layout.item_homepage_three, R.layout.item_loading);
        mSelectListRecyclerView.setLayoutManager(ListgridLayoutManager);
        mSelectListRecyclerView.setAdapter(mCategoryAdapter);

        mSelectAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Type type = mSelectAdapter.getItems(position);
                mPresenter.onItemClick(type);
            }
        });
        mScrollView.setListener(new ElasticHeadScrollView.OnScrollListener() {
            @Override
            public void onScrollToBottom() {
                mPresenter.loadCategoryList();
            }

            @Override
            public void onAlphaActionBar(float a) {
                mCategoryRelativeLayout.setAlpha(a);
            }
        });
        mCategoryAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                if (position != mCategoryAdapter.getItemCount() - 1) {
                    Comic comic = mCategoryAdapter.getItems(position);
                    IntentUtil.toComicDetail(CategoryActivity.this, comic.getId(), comic.getTitle());
                }
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.loadData();
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {

    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<Comic> data) {
        mCategoryAdapter.updateWithClear(data);
    }

    @Override
    public void getDataFinish() {
        mCategoryAdapter.notifyDataSetChanged();
    }
}
