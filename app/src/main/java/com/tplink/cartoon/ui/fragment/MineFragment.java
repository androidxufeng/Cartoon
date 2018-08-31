/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-8-30, xufeng, Create file
 */
package com.tplink.cartoon.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tplink.cartoon.R;
import com.tplink.cartoon.data.bean.MineTitle;
import com.tplink.cartoon.ui.adapter.BaseRecyclerAdapter;
import com.tplink.cartoon.ui.adapter.MineAdapter;
import com.tplink.cartoon.ui.presenter.MinePresenter;
import com.tplink.cartoon.ui.view.IMineView;
import com.tplink.cartoon.utils.GlideImageLoader;
import com.tplink.cartoon.utils.IntentUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment<MinePresenter> implements IMineView<List<MineTitle>> {

    @BindView(R.id.rv_mine)
    RecyclerView mRecycle;
    @BindView(R.id.iv_cover)
    ImageView mCover;
    private MineAdapter mineAdapter;

    @Override
    public void showToast(String t) {
        Toast.makeText(mActivity, t, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorView(String throwable) {
        showToast(throwable);
    }

    @Override
    public void fillData(List<MineTitle> data) {
        mineAdapter.updateWithClear(data);
    }

    @Override
    public void getDataFinish() {
        mineAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MinePresenter(this);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mineAdapter = new MineAdapter(getActivity(), R.layout.item_mine);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mRecycle.setLayoutManager(layoutManager);
        mRecycle.setAdapter(mineAdapter);
        GlideImageLoader.loadRoundImage(getActivity(),
                "https://avatars2.githubusercontent.com/u/23337086?s=460&v=4", mCover);
        mineAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                mPresenter.onItemClick(position);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadData();
    }


    @OnClick(R.id.rl_information)
    public void toGithub(){
        IntentUtil.toUrl(getActivity().getApplicationContext(),"https://github.com/androidxufeng/Cartoon");
    }

    @Override
    public void switchSkin(boolean isNight) {
        mineAdapter.setNight(isNight);
        mActivity.switchModel();
    }
}
