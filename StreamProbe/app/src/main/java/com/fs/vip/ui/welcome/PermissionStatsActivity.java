package com.fs.vip.ui.welcome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;
import com.fs.vip.utils.PermissionMonitor;
import com.fs.vip.utils.PermissionUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.os.Process.myUid;

public class PermissionStatsActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_permission_stats;
    }

    @Override
    public void initToolBar() {
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        tvTitle.setText("App Transperancy");
        tvTitle.setTextColor(0xffffffff);
    }


    @Override
    public void initDatas() {
        VideoView video = (VideoView)findViewById(R.id.videoView);
        String uriPath2 = "android.resource://" + getPackageName() + "/" +R.raw.stats_permission;
        Uri uri2 = Uri.parse(uriPath2);
        video.setVideoURI(uri2);
        video.requestFocus();
        video.start();
    }

    @Override
    public void configViews() {

    }


    @OnClick({R.id.btn_sure})
    public void onViewClicked(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermissons();
        }
    }

    private void initPermissons() {
        if (checkUseMannager()) {
            startActivity(new Intent(mContext, WalletWelcomeActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            //Listen to the permission change and send user to next Activity automatically
            //instead of making them click on back buttons after granting permission
            PermissionMonitor pMonitor = new PermissionMonitor(mContext, new Intent(mContext, WalletWelcomeActivity.class));
            pMonitor.startListening();
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

    }

    private boolean checkUseMannager() {
        AppOpsManager appOps = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
        if (appOps != null) {
            int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), mContext.getPackageName());
            return mode == MODE_ALLOWED;
        } else {
            return false;
        }
    }

}
