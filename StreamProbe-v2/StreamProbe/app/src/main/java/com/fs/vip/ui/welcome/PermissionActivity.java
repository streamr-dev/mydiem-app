package com.fs.vip.ui.welcome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class PermissionActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.btn_continue)
    Button btnSure;
    @BindView(R.id.btn_back)
    TextView btnBack;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    private final int REQUEST_CODE_STORAGE = 1;
    private final int REQUEST_CODE_GPS = 2;
    private final String[] PERMISSIONS1 = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public int getLayoutId() {
        return R.layout.activity_permission;
    }

    @Override
    public void initToolBar() {
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        tvTitle.setText("Permissions");
        tvTitle.setTextColor(0xffffffff);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }


    @OnClick({R.id.btn_continue})
    public void onViewClicked(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestMorePermissions1();
        }else{
            startActivity(new Intent(mContext, Permission2Activity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }


    // 普通申请多个权限
    private void requestMorePermissions1() {
        if (!PermissionUtils.checkPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            PermissionUtils.checkAndRequestMorePermissions(mContext, PERMISSIONS1, REQUEST_CODE_STORAGE);
        }else{
            startActivity(new Intent(mContext, Permission2Activity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull @NonNull String[] permissions, @androidx.annotation.NonNull @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_STORAGE) {
            PermissionUtils.onRequestMorePermissionsResult(mContext, PERMISSIONS1, new PermissionUtils.PermissionCheckCallBack() {
                @Override
                public void onHasPermission() {
                    startActivity(new Intent(mContext, Permission2Activity.class));
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                @Override
                public void onUserHasAlreadyTurnedDown(String... permission) {
                    Toast.makeText(mContext, "We need" + Arrays.toString(permission), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                    showToAppSettingDialog();
                }
            });
        }
    }


    /**
     * 显示前往应用设置Dialog
     */
    private void showToAppSettingDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("Need Permissions")
                .setMessage("We need relevant permissions in order to achieve the function. Click to go and it will go to the application settings interface. Please open the relevant permissions of the application.")
                .setPositiveButton("Go", (dialog, which) -> PermissionUtils.toAppSetting(mContext))
                .setNegativeButton("Cancel", null).show();
    }

}
