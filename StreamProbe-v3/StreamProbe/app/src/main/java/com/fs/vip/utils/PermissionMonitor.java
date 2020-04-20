package com.fs.vip.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import static android.os.Process.myUid;

public class PermissionMonitor {

    private final Context mContext;
    private final Intent mIntent;
    private final AppOpsManager appOpsManager;
    private final Handler handler;
    private boolean isListening;
    private Boolean lastValue;

    public PermissionMonitor(Context context, Intent intent) {
        this.mContext = context;
        this.mIntent = intent;
        appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        handler = new Handler();
    }

    public void startListening() {
        appOpsManager.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS, mContext.getPackageName(), usageOpListener);
        isListening = true;
    }

    public void stopListening() {
        lastValue = null;
        isListening = false;
        appOpsManager.stopWatchingMode(usageOpListener);
        handler.removeCallbacks(checkUsagePermission);
    }

    private final AppOpsManager.OnOpChangedListener usageOpListener = new AppOpsManager.OnOpChangedListener() {
        @Override
        public void onOpChanged(String op, String packageName) {
            // Android sometimes sets packageName to null
            if (packageName == null || mContext.getPackageName().equals(packageName)) {
                // Android actually notifies us of changes to ops other than the one we registered for, so filtering them out
                if (AppOpsManager.OPSTR_GET_USAGE_STATS.equals(op)) {
                    // We're not in main thread, so post to main thread queue
                    handler.post(checkUsagePermission);
                }
            }
        }
    };

    private final Runnable checkUsagePermission = new Runnable() {
        @Override
        public void run() {
            if (isListening) {
                int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, myUid(), mContext.getPackageName());
                boolean enabled = mode == AppOpsManager.MODE_ALLOWED;

                // Each change to the permission results in two callbacks instead of one.
                // Filtering out the duplicates.
                if (lastValue == null || lastValue != enabled) {
                    lastValue = enabled;

                    Log.i(PermissionMonitor.class.getSimpleName(), "Usage permission changed: " + enabled);
                    stopListening();
                    mContext.startActivity(mIntent);
                }
            }
        }
    };

}