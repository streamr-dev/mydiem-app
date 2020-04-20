package com.fs.vip.ui.main.data_union;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.fs.vip.base.BaseFragment;
import com.fs.vip.base.BaseMainFragment;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles;

    public MyPagerAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        BaseMainFragment fragment;
        Bundle args = new Bundle();
        args.putInt("position",position);
        fragment = DataUnionItemFragment.newInstance();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
