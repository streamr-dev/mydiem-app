package com.fs.vip.ui.main.applications;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fs.vip.R;
import com.fs.vip.Statistics.AppInformation;
import com.fs.vip.domain.EventApps;
import com.fs.vip.domain.EventName;
import com.fs.vip.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class EnableAppsAdapter extends BaseQuickAdapter<AppInformation, BaseViewHolder> {
    private String enableAll;

    public EnableAppsAdapter(int layoutResId, @Nullable List<AppInformation> data, String enableAll) {
        super(layoutResId, data);
        this.enableAll = enableAll;
    }

    @Override
    protected void convert(BaseViewHolder helper, AppInformation item) {
        if (!TextUtils.isEmpty(enableAll)) {
            item.setEnable(true);
            SharedPreferencesUtil.getInstance().putString(item.getPackageName(), "");
        }
        helper.setText(R.id.tv_enable, item.isEnable() ? "Enabled" : "Disable");
        helper.setText(R.id.tv_name, item.getLabel());
        helper.setImageDrawable(R.id.iv_icon, item.getIcon());
        helper.getView(R.id.rl_all).setOnClickListener(view -> {
            item.setEnable(!item.isEnable());
            helper.setText(R.id.tv_enable, item.isEnable() ? "Enabled" : "Disable");
            SharedPreferencesUtil.getInstance().putString(item.getPackageName(), item.isEnable() ? "" : "Disable");
        });
    }
}
