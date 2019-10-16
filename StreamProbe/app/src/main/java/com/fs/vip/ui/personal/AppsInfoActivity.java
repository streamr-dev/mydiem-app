package com.fs.vip.ui.personal;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;
import com.fs.vip.domain.UsageStatsWrapper;
import com.fs.vip.service.DeService;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.view.CirclePercentView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.xdandroid.hellodaemon.DaemonEnv;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jnr.ffi.annotations.In;

public class AppsInfoActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.iv_btn)
    ImageView ivBtn;
    @BindView(R.id.rl_btn)
    LinearLayout rlBtn;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.circle_percent_progress)
    CirclePercentView progressView;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.btn_send)
    TextView btnSend;
    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;
    @BindView(R.id.tv_app_num)
    TextView tvAppNum;
    @BindView(R.id.mCollapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.cv)
    CardView cv;
    private UsageStatsManager usageStatsManager;
    private PackageManager packageManager;
    private MyAdapter adapter;
    private boolean isSee = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_apps_info;
    }

    @Override
    public void initToolBar() {
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        tvTitle.setText("Apps");
        llyBack.setVisibility(View.GONE);
        tvTitle.setTextColor(0xffffffff);
        rlBtn.setVisibility(View.VISIBLE);
        cv.setVisibility(View.GONE);

    }

    @Override
    public void initDatas() {
        usageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
        packageManager = mContext.getPackageManager();
        List<String> installedApps = getInstalledAppList();
        Map<String, UsageStats> usageStats = usageStatsManager.queryAndAggregateUsageStats(getStartTime(), System.currentTimeMillis());
        List<UsageStats> stats = new ArrayList<>(usageStats.values());

        List<UsageStatsWrapper> finalList = buildUsageStatsWrapper(installedApps, stats);
        List<UsageStatsWrapper> newList = new ArrayList<>();
        long allTime = 0;
        long allNet = 0;
        for (int i = 0; i < finalList.size(); i++) {
            if (finalList.get(i).getUsageStats() != null) {
                newList.add(finalList.get(i));
                allTime += finalList.get(i).getUsageStats().getTotalTimeInForeground();
            }
        }
        tvAppNum.setText(newList.size() + "");
        for (UsageStatsWrapper usageStatsWrapper : newList) {
            usageStatsWrapper.setCliced(false);
        }
        progressView.setPercentage(newList.size() >= 100 ? 100 : newList.size());//传入百分比的值
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(NETWORK_STATS_SERVICE);
            try {
                NetworkStats.Bucket bucket = null;
                if (networkStatsManager != null) {
                    bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, "", 0, System.currentTimeMillis());
                    allNet = bucket.getRxBytes() + bucket.getTxBytes();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        adapter = new MyAdapter(newList, allTime, allNet, mContext);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            boolean temp = newList.get(position).isCliced();
            for (UsageStatsWrapper usageStatsWrapper : newList) {
                usageStatsWrapper.setCliced(false);
            }
            newList.get(position).setCliced(!temp);
            adapter.notifyDataSetChanged();
        });
        rv.setAdapter(adapter);

        if ( DeService.isWorking()) {
            btnSend.setText("DISABLE STREAMING");
            btnSend.setBackgroundColor(0xFFD36581);
        } else {
            btnSend.setText("ENABLE STREAMING");
            btnSend.setBackgroundColor(0xFF74A9E9);
        }


    }

    @Override
    public void configViews() {

    }


    private List<String> getInstalledAppList() {
        List<ApplicationInfo> infos = packageManager.getInstalledApplications(flags);
        List<String> installedApps = new ArrayList<>();
        for (ApplicationInfo info : infos) {
            installedApps.add(info.packageName);
        }
        return installedApps;
    }

    private long getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTimeInMillis();
    }

    private List<UsageStatsWrapper> buildUsageStatsWrapper(List<String> packageNames, List<UsageStats> usageStatses) {
        List<UsageStatsWrapper> list = new ArrayList<>();
        for (String name : packageNames) {
            boolean added = false;
            for (UsageStats stat : usageStatses) {
                if (stat != null && stat.getPackageName() != null) {
                    if (name.equals(stat.getPackageName())) {
                        added = true;
                        list.add(fromUsageStat(stat));
                    }
                }
            }
            if (!added) {
                UsageStatsWrapper usageStatsWrapper = fromUsageStat(name);
                if (usageStatsWrapper != null) {
                    list.add(usageStatsWrapper);
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    private UsageStatsWrapper fromUsageStat(String packageName) {

        ApplicationInfo ai = null;
        try {
            ai = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (ai != null) {
            return new UsageStatsWrapper(null, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());
        } else {
            return null;
        }


    }

    private UsageStatsWrapper fromUsageStat(UsageStats usageStats) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(usageStats.getPackageName(), 0);
            return new UsageStatsWrapper(usageStats, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }


    @OnClick({R.id.tv_wallet, R.id.tv_info, R.id.rl_btn,R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_wallet:
                cv.setVisibility(View.GONE);
                isSee = false;
                startActivity(new Intent(mContext, WalletActivity.class));
                break;
            case R.id.tv_info:
                cv.setVisibility(View.GONE);
                isSee = false;
                startActivity(new Intent(mContext, PersonalActivity.class));
                break;
            case R.id.rl_btn:
                isSee = !isSee;
                if (isSee) {
                    cv.setVisibility(View.VISIBLE);
                } else {
                    cv.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_send:
                if (DeService.isWorking()) {
                    DeService.cancelJobAlarmSub();
                    DeService.stopService();
                    btnSend.setText("ENABLE STREAMING");
                    btnSend.setBackgroundColor(0xFF74A9E9);
                } else {
                    DeService.sShouldStopService = false;
                    DaemonEnv.startServiceMayBind(DeService.class);
                    btnSend.setText("DISABLE STREAMING");
                    btnSend.setBackgroundColor(0xFFD36581);
                }
                break;
        }
    }
}
