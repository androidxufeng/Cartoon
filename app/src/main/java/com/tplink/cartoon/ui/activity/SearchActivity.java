/*
 * Author xufeng
 *
 * Ver 1.0, 18-8-20, xufeng, Create file
 */
package com.tplink.cartoon.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.Comic;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.SearchHistoryAdapter;
import com.tplink.cartoon.ui.adapter.SearchDynamicAdapter;
import com.tplink.cartoon.ui.adapter.SearchResultAdapter;
import com.tplink.cartoon.ui.adapter.SearchTopAdapter;
import com.tplink.cartoon.ui.presenter.SearchPresenter;
import com.tplink.cartoon.ui.source.search.SearchDataSource;
import com.tplink.cartoon.ui.view.ISearchView;
import com.tplink.cartoon.ui.widget.NoScrollGridLayoutManager;
import com.tplink.cartoon.ui.widget.NoScrollStaggeredGridLayoutManager;
import com.tplink.cartoon.utils.IntentUtil;
import com.tplink.cartoon.utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView<List<Comic>> {

    @BindView(R.id.et_search)
    EditText mSearchText;
    @BindView(R.id.iv_dynamic_recycle)
    RecyclerView mDynamicRecycle;
    @BindView(R.id.iv_clear)
    ImageView mClearText;
    @BindView(R.id.iv_result_recycle)
    RecyclerView mResultRecycle;
    @BindView(R.id.iv_top_search_recycle)
    RecyclerView mTopRecycle;
    @BindView(R.id.iv_history_recycle)
    RecyclerView mHistoryRecycle;
    @BindView(R.id.rl_normal)
    RelativeLayout mNormal;
    @BindView(R.id.tv_error)
    TextView mError;

    SearchDynamicAdapter mDynaicAdapter;
    SearchResultAdapter mResultAdapter;
    private SearchTopAdapter mTopAdapter;
    SearchHistoryAdapter mSearchHistoryAdapter;

    @OnClick(R.id.iv_clear)
    public void clear() {
        clearText();
        mResultRecycle.setVisibility(View.GONE);
        mDynamicRecycle.setVisibility(View.GONE);
        mNormal.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_cancel)
    public void cancel(View view) {
        finish();
    }

    @OnClick(R.id.iv_clear_history)
    public void clearHistory(View view) {
        mPresenter.clearHistory();
    }

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new SearchPresenter(new SearchDataSource(this), this);
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

        mResultAdapter = new SearchResultAdapter(this, R.layout.item_search_result);
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        mResultRecycle.setLayoutManager(manager2);
        mResultRecycle.setAdapter(mResultAdapter);

        NoScrollStaggeredGridLayoutManager staggeredGridLayoutManager = new NoScrollStaggeredGridLayoutManager
                (4, StaggeredGridLayoutManager.HORIZONTAL);
        staggeredGridLayoutManager.setScrollEnabled(false);
        mTopAdapter = new SearchTopAdapter(this, R.layout.item_top_search);
        mTopRecycle.setLayoutManager(staggeredGridLayoutManager);
        mTopRecycle.setAdapter(mTopAdapter);

        mSearchHistoryAdapter = new SearchHistoryAdapter(this, R.layout.item_history_search);
        NoScrollGridLayoutManager gridLayoutManager = new NoScrollGridLayoutManager(this, 1);
        gridLayoutManager.setScrollEnabled(false);
        mHistoryRecycle.setLayoutManager(gridLayoutManager);
        mHistoryRecycle.setAdapter(mSearchHistoryAdapter);

        mPresenter.getSearch();
        setLisenter();
    }

    private void setLisenter() {
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
                mResultRecycle.setVisibility(View.GONE);
                if (s.length() != 0) {
                    //文字改变，动态获取搜索结果
                    mResultAdapter.setKey(s.toString());
                    mDynaicAdapter.setKey(s.toString());
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
                Comic comic = mDynaicAdapter.getItems(position);
                IntentUtil.toComicDetail(SearchActivity.this, comic.getId(), comic.getTitle());
                SearchActivity.this.finish();
            }
        });

        mResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mResultAdapter.getItems(position);
                IntentUtil.toComicDetail(SearchActivity.this, comic.getId(), comic.getTitle());
                SearchActivity.this.finish();
            }
        });

        mTopAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mTopAdapter.getItems(position);
                IntentUtil.toComicDetail(SearchActivity.this, comic.getId(), comic.getTitle());
            }
        });

        mSearchHistoryAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                mPresenter.getSearchResult(mSearchHistoryAdapter.getItems(position).getTitle());
            }
        });

        //设置搜索监听事件
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //关闭软键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mPresenter.getSearchResult();
                    return true;
                }
                return false;
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
    public void fillDynamicResult(List<Comic> list) {
        mDynamicRecycle.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        mDynaicAdapter.updateWithClear(list);
    }

    @Override
    public void fillResult(List<Comic> comics) {
        mResultRecycle.setVisibility(View.VISIBLE);
        mDynamicRecycle.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mNormal.setVisibility(View.GONE);
        mResultAdapter.updateWithClear(comics);
    }

    @Override
    public String getSearchText() {
        return mSearchText.getText().toString();
    }

    @Override
    public void setSearchText(String title) {
        mSearchText.setText(title);
        mSearchText.setSelection(title.length());
        mClearText.setVisibility(View.VISIBLE);
    }

    @Override
    public void fillTopSearch(List<Comic> comics) {
        mTopAdapter.updateWithClear(comics);
    }

    @Override
    public void showErrorView(String throwable) {
        mError.setVisibility(View.VISIBLE);
        mError.setText(Html.fromHtml(TextUtil.getSearchErrorText(throwable)));
    }

    @Override
    public void fillData(List<Comic> comics) {
        if (comics != null && comics.size() != 0) {
            mSearchHistoryAdapter.updateWithClear(comics);
        } else {
            mSearchHistoryAdapter.onClear();
            mSearchHistoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getDataFinish() {

    }
}
