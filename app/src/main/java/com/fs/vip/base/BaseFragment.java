package com.fs.vip.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据
    protected Context mContext;
    private View convertView;
    private Unbinder mUnBinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutId(), container, false);
        }
        mUnBinder = ButterKnife.bind(this, convertView);
        initView(convertView);
        isInitView = true;
        lazyLoadData();
        return convertView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();

        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoadData() {
        if (!isFirstLoad || !isVisible || !isInitView) {
            return;
        }
        initData();
        isFirstLoad = false;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View convertView);

    protected abstract void initData();

    // 在 Fragment 的 onDestroy 中保存视图
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (convertView != null && convertView.getParent() != null) {
            ((ViewGroup) convertView.getParent()).removeView(convertView);
        }
        if (this.mUnBinder != null) {
            this.mUnBinder.unbind();
        }
    }

}
