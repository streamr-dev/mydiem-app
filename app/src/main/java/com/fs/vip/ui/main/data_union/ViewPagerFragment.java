package com.fs.vip.ui.main.data_union;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.fs.vip.R;
import com.fs.vip.base.MySupportFragment;
import com.google.android.material.tabs.TabLayout;

public class ViewPagerFragment extends MySupportFragment {

    TabLayout mTab;
    ViewPager mViewPager;

    public static ViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_pager, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mTab = (TabLayout) view.findViewById(R.id.tab);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(),
                getString(R.string.today), getString(R.string.week), getString(R.string.month),
                getString(R.string.all)));
        mViewPager.setOffscreenPageLimit(mTab.getTabCount());
        mTab.setupWithViewPager(mViewPager);
    }
}
