/*
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.SearchBean;
import com.tplink.cartoon.data.bean.SearchResult;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.SearchDynamicAdapter;
import com.tplink.cartoon.ui.presenter.SearchPresenter;
import com.tplink.cartoon.ui.source.search.SearchDataSource;
import com.tplink.cartoon.ui.view.ISearchView;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView<SearchResult> {

    @BindView(R.id.et_search)
    EditText mSearchText;
    @BindView(R.id.iv_dynamic_recycle)
    RecyclerView mDynamicRecycle;
    @BindView(R.id.iv_clear)
    ImageView mClearText;
    SearchDynamicAdapter mDynaicAdapter;

    @OnClick(R.id.iv_clear)
    public void clear() {
        clearText();
    }

    @OnClick(R.id.tv_cancel)
    public void cancel(View view) {
        finish();
    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new SearchPresenter(new SearchDataSource(), this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        mDynaicAdapter = new SearchDynamicAdapter(this, R.layout.item_dynamic_search);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mDynamicRecycle.setLayoutManager(manager);
        mDynamicRecycle.setAdapter(mDynaicAdapter);

        //搜索
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d("zhhr1122","beforeTextChanged="+s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("zhhr1122","onTextChanged="+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.d("zhhr1122","Editable="+s.toString());
                if (s.length() != 0) {
                    //文字改变，动态获取搜索结果
                    mDynamicRecycle.setVisibility(View.VISIBLE);
                    mPresenter.getDynamicResult(s.toString());
                    mClearText.setVisibility(View.VISIBLE);
                } else {
                    mDynamicRecycle.setVisibility(View.GONE);
                    mClearText.setVisibility(View.GONE);
                }
            }
        });
        mDynaicAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                SearchBean searchBean = new SearchBean();
                if (mPresenter.getmDynamicResult() != null && mPresenter.getmDynamicResult().getData().size() != 0) {
                    searchBean = mPresenter.getmDynamicResult().getData().get(position);
                }
                IntentUtil.toComicDetail(SearchActivity.this, searchBean.getId(), searchBean.getTitle());
                SearchActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void clearText() {
        mSearchText.setText("");
    }

    @Override
    public void fillDynamicResult(SearchResult searchResult) {
        List<SearchBean> list = searchResult.getData();
        if (list != null && list.size() != 0) {
            mDynaicAdapter.updateWithClear(searchResult.getData());
        }
    }

    @Override
    public void fillResult(SearchResult searchResult) {

    }

    @Override
    public void fillHotRank(List ranks) {

    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(SearchResult data) {

    }

    @Override
    public void getDataFinish() {

    }
}
