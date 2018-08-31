/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-14, xufeng, Create file
 */
package com.tplink.cartoon.ui.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tplink.cartoon.R;
import com.tplink.cartoon.ui.activity.HomeActivity;
import com.tplink.cartoon.ui.adapter.BookShelfFragmentAdapter;
import com.tplink.cartoon.ui.fragment.bookshelf.BaseBookShelfFragment;
import com.tplink.cartoon.ui.fragment.bookshelf.CollectionFragment;
import com.tplink.cartoon.ui.fragment.bookshelf.DownloadFragment;
import com.tplink.cartoon.ui.fragment.bookshelf.HistoryFragment;
import com.tplink.cartoon.ui.presenter.BookShelfPresenter;
import com.tplink.cartoon.ui.view.IBookShelfView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BookShelfFragment extends BaseFragment<BookShelfPresenter> implements IBookShelfView {

    @BindView(R.id.vp_bookshelf)
    ViewPager mViewpager;

    @BindView(R.id.tv_download)
    TextView mDownload;
    @BindView(R.id.tv_history)
    TextView mHistory;
    @BindView(R.id.tv_collect)
    TextView mCollect;
    @BindView(R.id.iv_bottom_collect)
    ImageView mBottomCollect;
    @BindView(R.id.iv_bottom_history)
    ImageView mBottomHistory;
    @BindView(R.id.iv_bottom_download)
    ImageView mBottomDownload;
    @BindView(R.id.iv_edit)
    ImageView mEdit;

    private boolean isEditing;
    private HomeActivity mHomeActivity;
    private CollectionFragment collectionFragment;
    private HistoryFragment historyFragment;
    private DownloadFragment downloadFragment;

    @TargetApi(Build.VERSION_CODES.M)
    @OnClick(R.id.rl_collect)
    public void toCollect() {
        resetTitle();
        mCollect.setTextAppearance(R.style.colorTextBlack);
        mViewpager.setCurrentItem(0);
        mBottomCollect.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @OnClick(R.id.rl_history)
    public void toHistory() {
        resetTitle();
        mHistory.setTextAppearance(R.style.colorTextBlack);
        mViewpager.setCurrentItem(1);
        mBottomHistory.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @OnClick(R.id.rl_download)
    public void toDownload() {
        resetTitle();
        mDownload.setTextAppearance(R.style.colorTextBlack);
        mViewpager.setCurrentItem(2);
        mBottomDownload.setVisibility(View.VISIBLE);
    }

    BookShelfFragmentAdapter mAdapter;
    protected FragmentManager fragmentManager;
    protected List<BaseBookShelfFragment> fragments;

    @Override
    public void showToast(String t) {
        Toast.makeText(mActivity, t, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new BookShelfPresenter(this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mHomeActivity = (HomeActivity) getActivity();
        fragments = new ArrayList<>();
        collectionFragment = new CollectionFragment();
        historyFragment = new HistoryFragment();
        downloadFragment = new DownloadFragment();
        fragments.add(collectionFragment);
        fragments.add(historyFragment);
        fragments.add(downloadFragment);
        fragmentManager = getActivity().getSupportFragmentManager();
        mAdapter = new BookShelfFragmentAdapter(fragmentManager, fragments);
        mViewpager.setAdapter(mAdapter);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isEditing) {
                    mEdit.setImageResource(R.drawable.edit);
                    mHomeActivity.setEditBottomVisible(View.GONE);
                    for (int i = 0; i < fragments.size(); i++) {
                        fragments.get(i).onEditList(false);
                    }
                    isEditing = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toCollect();
                        break;
                    case 1:
                        toHistory();
                        break;
                    case 2:
                        toDownload();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    public void resetTitle() {
        mDownload.setTextAppearance(R.style.colorTextColorLight);
        mCollect.setTextAppearance(R.style.colorTextColorLight);
        mHistory.setTextAppearance(R.style.colorTextColorLight);
        mBottomCollect.setVisibility(View.GONE);
        mBottomDownload.setVisibility(View.GONE);
        mBottomHistory.setVisibility(View.GONE);
    }

    @OnClick(R.id.iv_edit)
    public void toEdit() {
        if (!isEditing) {
            mEdit.setImageResource(R.drawable.closed);
            mHomeActivity.setEditBottomVisible(View.VISIBLE);
            showEditModel(fragments.get(mViewpager.getCurrentItem()), true);
        } else {
            mEdit.setImageResource(R.drawable.edit);
            mHomeActivity.setEditBottomVisible(View.GONE);
            showEditModel(fragments.get(mViewpager.getCurrentItem()), false);
        }
        isEditing = !isEditing;
    }

    private void showEditModel(BaseBookShelfFragment fragment, boolean isEdit) {
        fragment.onEditList(isEdit);
    }

    public void onClickDelete() {
        fragments.get(mViewpager.getCurrentItem()).onDelete();
    }

    public void onClickSelect() {
        fragments.get(mViewpager.getCurrentItem()).onSelect();
    }

    public void quitEdit() {
        mEdit.setImageResource(R.drawable.edit);
        showEditModel(fragments.get(mViewpager.getCurrentItem()), false);
        isEditing = false;
    }

}
