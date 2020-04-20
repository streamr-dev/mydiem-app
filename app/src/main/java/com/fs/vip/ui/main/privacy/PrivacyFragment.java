package com.fs.vip.ui.main.privacy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseMainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PrivacyFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {


    //    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.lly_back)
//    LinearLayout llyBack;
//    @BindView(R.id.iv_menu)
//    ImageView ivMenu;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Unbinder mUnBinder;

    public static PrivacyFragment newInstance() {
        return new PrivacyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle("Privacy & Terms of Usage");
        initToolbarNav(mToolbar, true);
        mToolbar.setOnMenuItemClickListener(this);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mUnBinder != null) {
            this.mUnBinder.unbind();
        }
    }
    /**
     * 类似于 Activity的 onNewIntent()
     */
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);

//        Toast.makeText(_mActivity, args.getString("from"), Toast.LENGTH_SHORT).show();
    }
}
