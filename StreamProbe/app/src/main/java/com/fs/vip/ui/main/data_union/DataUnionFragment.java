package com.fs.vip.ui.main.data_union;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.fs.vip.R;
import com.fs.vip.base.BaseFragment;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.base.MySupportFragment;
import com.fs.vip.service.DeService;
import com.fs.vip.utils.Utils;
import com.xdandroid.hellodaemon.DaemonEnv;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

public class DataUnionFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_state)
    Button btnState;
    private Unbinder mUnBinder;
    public static DataUnionFragment newInstance() {
        return new DataUnionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_union, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (findChildFragment(ViewPagerFragment.class) == null) {
            loadRootFragment(R.id.fl_container, ViewPagerFragment.newInstance());
        }
        if ( DeService.isWorking()) {
            btnState.setText("DISABLE STREAMING");
            btnState.setBackgroundColor(0xFFD36581);
        } else {
            btnState.setText("ENABLE STREAMING");
            btnState.setBackgroundColor(0xFF74A9E9);
        }
    }

    private void initView(View view) {
        mToolbar.setTitle("Data Union");
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

    }

    @OnClick(R.id.btn_state)
    public void onViewClicked() {
        if (DeService.isWorking()) {
            DeService.cancelJobAlarmSub();
            DeService.stopService();
            btnState.setText("ENABLE STREAMING");
            btnState.setBackgroundColor(0xFF74A9E9);
        } else {
            DeService.sShouldStopService = false;
            DaemonEnv.startServiceMayBind(DeService.class);
            btnState.setText("DISABLE STREAMING");
            btnState.setBackgroundColor(0xFFD36581);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create
    }
}
