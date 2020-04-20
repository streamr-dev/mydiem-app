package com.fs.vip.ui.main.data_union;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fs.vip.R;
import com.fs.vip.Statistics.AppInformation;

import java.text.DecimalFormat;
import java.util.List;

public class TimeUsedAdapter extends BaseQuickAdapter<AppInformation, BaseViewHolder> {

    public TimeUsedAdapter(int layoutResId, @Nullable List<AppInformation> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppInformation item) {
        helper.setText(R.id.tv_name, item.getLabel());
        DecimalFormat df = new DecimalFormat("#,###");
        helper.setText(R.id.tv_time, item.getUsedTimebyDay() / 1000 / 60 != 0 ? df.format(item.getUsedTimebyDay() / 1000 / 60) + " min" : df.format(item.getUsedTimebyDay() / 1000) + " s");
        helper.setImageDrawable(R.id.iv_icon, item.getIcon());
        helper.setText(R.id.tv_icon,helper.getAdapterPosition()+1+"");
    }
}
