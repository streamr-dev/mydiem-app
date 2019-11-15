package com.fs.vip.ui.welcome;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;
import com.fs.vip.utils.PermissionUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

public class Permission2Activity extends BaseActivity {

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
    private final int REQUEST_CODE_GPS = 2;
    private final String[] PERMISSIONS2 = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public int getLayoutId() {
        return R.layout.activity_permission2;
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
            requestMorePermissions2();
        }else{
            startActivity(new Intent(mContext, PermissionStatsActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }



    // 普通申请多个权限
    private void requestMorePermissions2() {
        if (!PermissionUtils.checkPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)){
            PermissionUtils.checkAndRequestMorePermissions(mContext, PERMISSIONS2, REQUEST_CODE_GPS);
        }else{
            startActivity(new Intent(mContext, CreateWalletActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull @NonNull String[] permissions, @androidx.annotation.NonNull @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GPS) {
            PermissionUtils.onRequestMorePermissionsResult(mContext, PERMISSIONS2, new PermissionUtils.PermissionCheckCallBack() {
                @Override
                public void onHasPermission() {
                    startActivity(new Intent(mContext, PermissionStatsActivity.class));
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }

                @Override
                public void onUserHasAlreadyTurnedDown(String... permission) {
                    Toast.makeText(mContext, "We need" + Arrays.toString(permission), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                    Log.e("permission",permission.toString());
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
