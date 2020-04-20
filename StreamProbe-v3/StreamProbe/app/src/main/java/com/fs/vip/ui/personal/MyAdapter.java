package com.fs.vip.ui.personal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fs.vip.R;
import com.fs.vip.domain.UsageStatsWrapper;
import com.fs.vip.utils.LogUtils;
import com.fs.vip.utils.PermissionUtils;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.view.CirclePercentView;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.NETWORK_STATS_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

public class MyAdapter extends BaseQuickAdapter<UsageStatsWrapper, BaseViewHolder> {

    private NetworkStatsManager networkStatsManager;
    private long allTime;
    private long allNet;
    private Context mContext;

    public MyAdapter(@Nullable List<UsageStatsWrapper> data, long allTime, long allNet, Context mContext) {
        super(R.layout.item_words, data);
        this.allTime = allTime;
        this.allNet = allNet;
        this.mContext = mContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            networkStatsManager = (NetworkStatsManager) mContext.getSystemService(NETWORK_STATS_SERVICE);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, UsageStatsWrapper item) {
        helper.setImageDrawable(R.id.iv_app_logo, item.getAppIcon());
        helper.setText(R.id.tv_app_name, item.getAppName());
        helper.setGone(R.id.ll_cp, item.isCliced());
        helper.setGone(R.id.view, item.isCliced());
        CirclePercentView cp1 = helper.getView(R.id.cp1);
        CirclePercentView cp2 = helper.getView(R.id.cp2);
        if (item.isCliced()) {
            helper.setText(R.id.tv_time, item.getUsageStats().getTotalTimeInForeground() / 1000 / 60 + "");
            cp1.setPercentage(((float) item.getUsageStats().getTotalTimeInForeground() * 100 / (float) allTime));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                long net = getNetFromApp(getUidByPackageName(item.getUsageStats().getPackageName()));
//                ToastUtils.showLongToast(net + "");
            } else {
                cp2.setPercentage(0);
            }
        }
    }

    private int getUidByPackageName(String packageName) {
        int uid = -1;
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            uid = packageInfo.applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return uid;
    }

    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private long getNetFromApp(int uid) {
        // 获取subscriberId
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        String subId = null;
        if (PermissionUtils.checkPermission(mContext, Manifest.permission.READ_PHONE_STATE)) {
            if (tm != null) {
                subId = tm.getSubscriberId();
            }
        }
        LogUtils.e(subId + "====?");
        NetworkStats summaryStats;
        long summaryRx = 0;
        long summaryTx = 0;
        NetworkStats.Bucket summaryBucket = new NetworkStats.Bucket();
        long summaryTotal = 0;

        try {
            summaryStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, subId, getTimesMonthMorning(), System.currentTimeMillis());
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
        do {
            summaryStats.getNextBucket(summaryBucket);
            int summaryUid = summaryBucket.getUid();
            if (uid == summaryUid) {
                summaryRx += summaryBucket.getRxBytes();
                summaryTx += summaryBucket.getTxBytes();
            }
            summaryTotal += summaryRx + summaryTx;
            return summaryTotal;
        } while (summaryStats.hasNextBucket());
    }

    private long getTimesMonthMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }
}
