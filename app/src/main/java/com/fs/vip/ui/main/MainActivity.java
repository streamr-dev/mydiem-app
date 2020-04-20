package com.fs.vip.ui.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.fs.vip.R;
import com.fs.vip.Statistics.StatisticsInfo;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.base.MySupportActivity;
import com.fs.vip.base.MySupportFragment;
import com.fs.vip.domain.EventAppNum;
import com.fs.vip.domain.EventName;
import com.fs.vip.domain.RefreshHome;
import com.fs.vip.ui.main.account.AccountFragment;
import com.fs.vip.ui.main.applications.ApplicationsFragment;
import com.fs.vip.ui.main.data_union.DataUnionFragment;
import com.fs.vip.ui.main.extra.ExtraFragment;
import com.fs.vip.ui.main.privacy.PrivacyFragment;
import com.fs.vip.ui.main.wallet.WalletFragment;
import com.fs.vip.utils.Md5Utils;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultNoAnimator;

public class MainActivity extends MySupportActivity implements NavigationView.OnNavigationItemSelectedListener, BaseMainFragment.OnFragmentOpenDrawerListener {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    private Unbinder unbinder;
    private TextView tvName;
    private TextView tvAppNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        MySupportFragment fragment = findFragment(DataUnionFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, DataUnionFragment.newInstance());
        }
        this.setFragmentAnimator(new DefaultNoAnimator());
        initView();
        getConnetApps();
    }


    private void initView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_1);
        tvName = navView.getHeaderView(0).findViewById(R.id.tv_id);
        tvAppNum = navView.getHeaderView(0).findViewById(R.id.tv_app_num);
        if (TextUtils.isEmpty(SharedPreferencesUtil.getInstance().getString("name"))) {
            SharedPreferencesUtil.getInstance().putString("name", Md5Utils.md5(System.currentTimeMillis() + "").toUpperCase().substring(0, 8));
        }
        tvName.setText(SharedPreferencesUtil.getInstance().getString("name"));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawer.closeDrawer(GravityCompat.START);

        mDrawer.postDelayed(() -> {
            int id = item.getItemId();

            final ISupportFragment topFragment = getTopFragment();
            MySupportFragment myHome = (MySupportFragment) topFragment;

            if (id == R.id.nav_1) {

                DataUnionFragment fragment = findFragment(DataUnionFragment.class);
                if (fragment == null) {
                    myHome.startWithPopTo(DataUnionFragment.newInstance(), DataUnionFragment.class, false);
                } else {
                    myHome.start(fragment, SupportFragment.SINGLETASK);
                }
            } else if (id == R.id.nav_2) {
                WalletFragment fragment = findFragment(WalletFragment.class);
                if (fragment == null) {
                    myHome.startWithPopTo(WalletFragment.newInstance(), WalletFragment.class, false);
                } else {
                    myHome.start(fragment, SupportFragment.STANDARD);
                }
            } else if (id == R.id.nav_3) {
                AccountFragment fragment = findFragment(AccountFragment.class);
                if (fragment == null) {
                    myHome.startWithPopTo(AccountFragment.newInstance(), AccountFragment.class, false);
                } else {
                    myHome.start(fragment, SupportFragment.STANDARD);
                }
            } else if (id == R.id.nav_4) {
                ApplicationsFragment fragment = findFragment(ApplicationsFragment.class);
                if (fragment == null) {
                    myHome.startWithPopTo(ApplicationsFragment.newInstance(), ApplicationsFragment.class, false);
                } else {
                    myHome.start(fragment, SupportFragment.STANDARD);
                }
            } else if (id == R.id.nav_5) {
                PrivacyFragment fragment = findFragment(PrivacyFragment.class);
                if (fragment == null) {
                    myHome.startWithPopTo(PrivacyFragment.newInstance(), PrivacyFragment.class, false);
                } else {
                    myHome.start(fragment, SupportFragment.STANDARD);
                }
            } else if (id == R.id.nav_6) {
                ExtraFragment fragment = findFragment(ExtraFragment.class);
                if (fragment == null) {
                    myHome.startWithPopTo(ExtraFragment.newInstance(), ExtraFragment.class, false);
                } else {
                    myHome.start(fragment, SupportFragment.STANDARD);
                }
            }
        }, 200);

        return true;
    }

    @Override
    public void onBackPressedSupport() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                popTo(DataUnionFragment.class,false);
                EventBus.getDefault().postSticky(new RefreshHome(true));
                navView.setCheckedItem(R.id.nav_1);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (EventBus.getDefault() != null) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onOpenDrawer() {
        if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.openDrawer(GravityCompat.START);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void d(EventName eventMsg) {
        tvName.setText(eventMsg.getUserName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void d(EventAppNum eventMsg) {
        tvAppNum.setText(eventMsg.getNum() + " apps connected");
    }


    public void getConnetApps() {
        Observable.create((ObservableOnSubscribe<StatisticsInfo>) e -> {
            e.onNext(new StatisticsInfo(this, StatisticsInfo.YEAR));
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatisticsInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatisticsInfo state) {
                        if (tvAppNum != null) {
                            tvAppNum.setText(state.getEnableList().size() + " apps connected");
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

}
