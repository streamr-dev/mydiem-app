package com.fs.vip.ui.main.extra;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.domain.ETHWalletDao;
import com.fs.vip.service.DeService;
import com.fs.vip.ui.main.MainActivity;
import com.fs.vip.ui.main.privacy.PrivacyFragment;
import com.fs.vip.ui.welcome.CreateWalletActivity;
import com.fs.vip.ui.welcome.PermissionStatsActivity;
import com.fs.vip.utils.ETHMnemonic;
import com.fs.vip.utils.MyLocation;
import com.fs.vip.utils.PermissionUtils;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.Utils;
import com.fs.vip.utils.WalletDaoUtils;
import com.mobile.mobilehardware.local.LocalHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;

public class ExtraFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.s_location)
    Switch switchCompat;
    private Unbinder mUnBinder;
    private boolean has = false;
    private final String[] PERMISSIONS2 = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};

    private final int REQUEST_CODE_GPS = 2;


    public static ExtraFragment newInstance() {
        return new ExtraFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extra, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle("Extra Revenue");
        initToolbarNav(mToolbar, true);
        mToolbar.setOnMenuItemClickListener(this);
        initSwitch();

        switchCompat.setOnClickListener(v -> {
            if (has && WalletDaoUtils.getCurrent() != null) {
                switchCompat.setChecked(!SharedPreferencesUtil.getInstance().getBoolean(WalletDaoUtils.getCurrent().getAddress()));
                saveSwitchType(switchCompat.isChecked());
            } else {
                Toast.makeText(getActivity(), "have no permision", Toast.LENGTH_LONG).show();
            }
        });

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                if (location!=null) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    List<Address> addList = null;// 解析经纬度
                    try {
                        addList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addList != null && addList.size() > 0) {
                        for (int i = 0; i < addList.size(); i++) {
                            Address add = addList.get(i);
                            Log.e("esdfsdfsdf",add.toString());
                        }
                    }
                }
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getActivity(), locationResult);

    }

    private void initSwitch() {
        if (WalletDaoUtils.getCurrent() != null) {
            if (SharedPreferencesUtil.getInstance().getBoolean(WalletDaoUtils.getCurrent().getAddress())) {
                switchCompat.setChecked(true);
            } else {
                switchCompat.setChecked(false);
            }
        }
    }

    private void saveSwitchType(boolean selected) {
        if (WalletDaoUtils.getCurrent() != null) {
            SharedPreferencesUtil.getInstance().putBoolean(WalletDaoUtils.getCurrent().getAddress(), selected);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPermission();
        initSwitch();
        Log.e("sdfsdf", SharedPreferencesUtil.getInstance().getBoolean(WalletDaoUtils.getCurrent().getAddress()) + "");
    }

    void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestMorePermissions2();
        } else {
            has = true;
        }
    }

    // 普通申请多个权限
    private void requestMorePermissions2() {
        if (!PermissionUtils.checkPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            PermissionUtils.checkAndRequestMorePermissions(getActivity(), PERMISSIONS2, REQUEST_CODE_GPS);
        } else {
            has = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull @NonNull String[] permissions, @androidx.annotation.NonNull @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GPS) {
            PermissionUtils.onRequestMorePermissionsResult(getActivity(), PERMISSIONS2, new PermissionUtils.PermissionCheckCallBack() {
                @Override
                public void onHasPermission() {
                    has = true;
                }

                @Override
                public void onUserHasAlreadyTurnedDown(String... permission) {
                    Toast.makeText(getActivity(), "We need" + Arrays.toString(permission), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                    showToAppSettingDialog();
                }
            });
        }
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
     * 显示前往应用设置Dialog
     */
    private void showToAppSettingDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Need Permissions")
                .setMessage("We need relevant permissions in order to achieve the function. Click to go and it will go to the application settings interface. Please open the relevant permissions of the application.")
                .setPositiveButton("Go", (dialog, which) -> PermissionUtils.toAppSetting(getActivity()))
                .setNegativeButton("Cancel", null).show();
    }

    /**
     * 类似于 Activity的 onNewIntent()
     */
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);

//        Toast.makeText(_mActivity, args.getString("from"), Toast.LENGTH_SHORT).show();
    }
}
