package com.tplink.cartoon.ui.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.tplink.cartoon.R;
import com.tplink.cartoon.ui.fragment.BookShelfFragment;
import com.tplink.cartoon.ui.widget.FloatEditLayout;
import com.tplink.cartoon.utils.LogUtil;
import com.tplink.cartoon.utils.PermissionUtils;

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
    @BindView(R.id.rl_edit_bottom)
    FloatEditLayout mEditBottom;
    private BookShelfFragment mBookShelfFragment;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.btn_home)
    public void toHomeFragment(View view) {
        selectTab(0);
        resetBottomBtn();
        mHome.setBackgroundResource(R.drawable.homepage_press);
        initStatusBar(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.btn_bookshelf)
    public void toBookShelfFragment(View view) {
        selectTab(1);
        resetBottomBtn();
        mBookShelf.setBackgroundResource(R.drawable.bookshelf_press);
        initStatusBar(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.btn_mine)
    public void toMineFragment(View view){
        selectTab(2);
        resetBottomBtn();
        mMine.setBackgroundResource(R.drawable.mine_press);
        initStatusBar(false);
    }

    public void resetBottomBtn() {
        mHome.setBackgroundResource(R.drawable.homepage);
        mMine.setBackgroundResource(R.drawable.mine);
        mBookShelf.setBackgroundResource(R.drawable.bookshelf);
    }

    @Override
    protected void initView() {
        initPermission();
        initFragment();
    }

    private void initPermission() {
        //检查读写权限
        if (PermissionUtils.checkPermission(PermissionUtils.READ_EXTERNAL_STORAGE, this) &&
                PermissionUtils.checkPermission(PermissionUtils.WRITE_EXTERNAL_STORAGE, this)) {
            initFragment();
        } else {
            PermissionUtils.verifyStoragePermissions(this);
        }
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        mBookShelfFragment = (BookShelfFragment) fragmentManager.findFragmentById(R.id.fm_bookshelf);
        fragments.add(fragmentManager.findFragmentById(R.id.fm_home));
        fragments.add(mBookShelfFragment);
        fragments.add(fragmentManager.findFragmentById(R.id.fm_mine));
        mHome.setBackgroundResource(R.drawable.homepage_press);
        mEditBottom.setListener(new FloatEditLayout.onClickListener() {
            @Override
            public void onClickSelect() {
                mBookShelfFragment.onClickSelect();
            }
            @Override
            public void onDelete() {
                mBookShelfFragment.onClickDelete();
            }
        });
        selectTab(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length != 0) {
            disposeRequestResult(grantResults[0]);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void disposeRequestResult(int grantResult) {
        switch (grantResult) {
            case PackageManager.PERMISSION_GRANTED:
                initFragment();
                break;
            case PackageManager.PERMISSION_DENIED:
                LogUtil.w("ceshi", "未授予权限");
                finish();
                break;
            default:
                break;
        }
    }

    public void setEditBottomVisible(int Visible) {
        mEditBottom.setVisibility(Visible);
    }

    public FloatEditLayout getEditBottom() {
        return mEditBottom;
    }

    public void quitEdit(){
        setEditBottomVisible(View.GONE);
        mBookShelfFragment.quitEdit();
    }
}