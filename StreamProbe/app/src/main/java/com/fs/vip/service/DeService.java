package com.fs.vip.service;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.fs.vip.domain.UploadBean;
import com.fs.vip.domain.UsageStatsWrapper;
import com.fs.vip.utils.LogUtils;
import com.fs.vip.utils.MtjServerDatabaseTools;
import com.fs.vip.utils.OkHttpUtil;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.Utils;
import com.fs.vip.utils.WalletDaoUtils;
import com.google.gson.Gson;
import com.mobile.mobilehardware.applist.ListAppHelper;
import com.mobile.mobilehardware.audio.AudioHelper;
import com.mobile.mobilehardware.band.BandHelper;
import com.mobile.mobilehardware.battery.BatteryHelper;
import com.mobile.mobilehardware.bluetooth.BluetoothHelper;
import com.mobile.mobilehardware.build.BuildHelper;
import com.mobile.mobilehardware.camera.CameraHelper;
import com.mobile.mobilehardware.cpu.CpuHelper;
import com.mobile.mobilehardware.dns.DnsHelper;
import com.mobile.mobilehardware.local.LocalHelper;
import com.mobile.mobilehardware.memory.MemoryHelper;
import com.mobile.mobilehardware.network.NetWorkHelper;
import com.mobile.mobilehardware.root.RootHelper;
import com.mobile.mobilehardware.screen.ScreenHelper;
import com.mobile.mobilehardware.sdcard.SDCardHelper;
import com.mobile.mobilehardware.setting.SettingsHelper;
import com.mobile.mobilehardware.signal.SignalHelper;
import com.mobile.mobilehardware.simcard.SimCardHelper;
import com.mobile.mobilehardware.uniqueid.PhoneIdHelper;
import com.mobile.mobilehardware.useragent.UserAgentHelper;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xdandroid.hellodaemon.AbsWorkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class DeService extends AbsWorkService {
    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService;
    public static Disposable sDisposable;
    private final String[] PERMISSIONS = new String[]{Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA};
    private UsageStatsManager usageStatsManager;
    private PackageManager packageManager;
    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;

    public static void stopService() {
        //我们现在不再需要服务运行了, 将标志位置为 true
        sShouldStopService = true;
        //取消对任务的订阅
        if (sDisposable != null) sDisposable.dispose();
        //取消 Job / Alarm / Subscription
        cancelJobAlarmSub();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        usageStatsManager = (UsageStatsManager) DeService.this.getSystemService(Context.USAGE_STATS_SERVICE);
        packageManager = DeService.this.getPackageManager();
    }

    /**
     * 是否 任务完成, 不再需要服务运行?
     *
     * @return 应当停止服务, true; 应当启动服务, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return sShouldStopService;
    }
    public static boolean isWorking() {
        return sDisposable != null && !sDisposable.isDisposed();
    }
    @Override
    public void startWork(Intent intent, int flags, int startId) {
        if (WalletDaoUtils.getCurrent() != null) {
            if (sDisposable != null) sDisposable.dispose();
            sDisposable = null;
            sDisposable = Observable
                    .interval(10, TimeUnit.MINUTES)
                    //取消任务时取消定时唤醒
                    .doOnDispose(AbsWorkService::cancelJobAlarmSub)
                    .subscribe(count -> getAppUse());
        }

    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopService();
    }

    /**
     * 任务是否正在运行?
     *
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        //若还没有取消订阅, 就说明任务仍在运行.
        return sDisposable != null && !sDisposable.isDisposed();
    }

    @Override
    public IBinder onBind(Intent intent, Void v) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
        System.out.println("保存数据到磁盘。");
    }

    protected void stream(String info) {
        String header = "token " + "rc0L3E0YQRCpfa-gWVz0YQoStpnwE7QTmeN9x3y21Xig";
        String url = "https://www.streamr.com/api/v1/streams/" + "JYm4iCZbS9-ZqYtzSwH0eg" + "/data";
        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.e(e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtils.e(response.body().string());
            }
        };
        UploadBean bean = new UploadBean(info);
        OkHttpUtil.httpPost(url, new Gson().toJson(bean), callback, header);
    }

    private void getAppUse() {
        List<String> installedApps = getInstalledAppList();
        Map<String, UsageStats> usageStats = usageStatsManager.queryAndAggregateUsageStats(getStartTime(), System.currentTimeMillis());
        List<UsageStats> stats = new ArrayList<>(usageStats.values());
        List<UsageStatsWrapper> finalList = buildUsageStatsWrapper(installedApps, stats);
        StringBuffer sbApp = new StringBuffer();
        for (UsageStatsWrapper usageStatsWrapper : finalList) {
            if (usageStatsWrapper.getUsageStats() != null) {
                sbApp.append(usageStatsWrapper.getAppName()).append("Used Time：").append(usageStatsWrapper.getUsageStats().getTotalTimeInForeground()).append(" ");
            }
        }
        String userInfo = "name:" + SharedPreferencesUtil.getInstance().getString("name")
                + "gender:" + SharedPreferencesUtil.getInstance().getString("gender")
                + "age:" + SharedPreferencesUtil.getInstance().getString("age")
                + "race:" + SharedPreferencesUtil.getInstance().getString("race")
                + "region:" + SharedPreferencesUtil.getInstance().getString("region");
        String sb = "AppInfo:" + sbApp.toString() +
                "音量数据获取" + AudioHelper.mobGetMobAudio(getApplicationContext()).toString() +
                "版本数据获取" + BandHelper.mobGetBandInfo().toString() +
                "电池数据获取" + BatteryHelper.mobGetBattery(getApplicationContext()).toString() +
                "蓝牙数据获取" + BluetoothHelper.mobGetMobBluetooth(getApplicationContext()).toString() +
                "系统Build数据获取" + BuildHelper.mobGetBuildInfo().toString() +
                "摄像头数据获取" + CameraHelper.getCameraInfo(getApplicationContext()).toString() +
                "调试数据获取" + CpuHelper.mobGetCpuInfo().toString() +
                "host数据获取" + DnsHelper.mobDNS("ip") +
                "本地数据获取" + LocalHelper.mobGetMobLocal().toString() +
                "内存数据获取" + MemoryHelper.getMemoryInfo(getApplicationContext()).toString() +
                "网络数据获取" + NetWorkHelper.mobGetMobNetWork(getApplicationContext()).toString() +
                "root数据获取" + (RootHelper.mobileRoot(getApplicationContext()) + "") +
                "屏幕数据获取" + ScreenHelper.mobGetMobScreen(getApplicationContext()).toString() +
                "SDCard数据获取" + SDCardHelper.mobGetSdCard().toString() +
                "设置数据获取" + SettingsHelper.mobGetMobSettings(getApplicationContext()).toString() +
                "信号数据获取" + SignalHelper.mobGetNetRssi(getApplicationContext()).toString() +
                "手机卡数据获取" + SimCardHelper.mobileSimInfo(getApplicationContext()).toString() +
                "唯一ID数据获取" + PhoneIdHelper.getPsuedoUniqueID() +
                "UA数据获取" + UserAgentHelper.getDefaultUserAgent(getApplicationContext()) +
                "用户信息" + userInfo;
        LogUtils.e(sb);
        MtjServerDatabaseTools.insertIntoData(WalletDaoUtils.getCurrent().getAddress());
        stream(sb);
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

    private UsageStatsWrapper fromUsageStat(String packageName) throws IllegalArgumentException {
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
}
