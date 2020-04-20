package com.fs.vip.ui.main.applications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.Statistics.StatisticsInfo;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.domain.EventAppNum;
import com.fs.vip.ui.main.data_union.ViewPagerFragment;
import com.fs.vip.view.CirclePercentView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ApplicationsFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.tv_enable_apps)
    TextView tvEnableApps;
    @BindView(R.id.tv_enable_app)
    TextView tvEnableApp;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.cp)
    CirclePercentView cp;
    private Unbinder mUnBinder;

    public static ApplicationsFragment newInstance() {
        return new ApplicationsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applications, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle("Applications");
        initToolbarNav(mToolbar, true);
        mToolbar.setOnMenuItemClickListener(this);
        getConnetApps();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (findChildFragment(ViewPagerFragment.class) == null) {
            loadRootFragment(R.id.fl_container, ViewPagerFragment.newInstance());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mUnBinder != null) {
            this.mUnBinder.unbind();
        }
    }

    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @OnClick({R.id.tv_enable_apps, R.id.tv_enable_app})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_enable_apps:
                startForResult(EnableAppsFragment.newInstance(), 1);
                break;
            case R.id.tv_enable_app:
                BaseMainFragment fragment;
                Bundle args = new Bundle();
                args.putString("enable", "true");
                fragment = EnableAppsFragment.newInstance();
                fragment.setArguments(args);
                startForResult(fragment, 1);
                break;
        }
    }

    private void getConnetApps() {
        Observable.create((ObservableOnSubscribe<StatisticsInfo>) e -> {
            e.onNext(new StatisticsInfo(getActivity(), StatisticsInfo.YEAR));
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatisticsInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatisticsInfo state) {
                        if (tvNum != null) {
                            cp.setPercentage(((float)(state.getEnableList().size())/((float)state.getShowList().size()))*100);
                            tvNum.setText(state.getEnableList().size() + "/" + state.getShowList().size());
                            EventBus.getDefault().postSticky(new EventAppNum(state.getEnableList().size()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        getConnetApps();
    }
}
