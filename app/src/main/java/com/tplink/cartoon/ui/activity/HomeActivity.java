package com.tplink.cartoon.ui.activity;

import android.view.View;
import android.widget.Button;

import com.tplink.cartoon.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseFragmentActivity {
    @BindView(R.id.btn_home)
    Button mHome;
    @BindView(R.id.btn_bookshelf)
    Button mBookShelf;
    @BindView(R.id.btn_mine)
    Button mMine;


    @OnClick(R.id.btn_home)
    public void toHomeFragment(View view) {
        selectTab(0);
        resetBottomBtn();
        mHome.setBackgroundResource(R.drawable.homepage_press);
    }

    @OnClick(R.id.btn_bookshelf)
    public void toBookShelfFragment(View view) {
        selectTab(1);
        resetBottomBtn();
        mBookShelf.setBackgroundResource(R.drawable.bookshelf_press);
    }

    public void resetBottomBtn() {
        mHome.setBackgroundResource(R.drawable.homepage);
        mMine.setBackgroundResource(R.drawable.mine);
        mBookShelf.setBackgroundResource(R.drawable.bookshelf);
    }

    @Override
    protected void initView() {
        fragments = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        fragments.add(fragmentManager.findFragmentById(R.id.fm_home));
        fragments.add(fragmentManager.findFragmentById(R.id.fm_bookshelf));
        fragments.add(fragmentManager.findFragmentById(R.id.fm_mine));
        mHome.setBackgroundResource(R.drawable.homepage_press);
        selectTab(0);
    }

}
