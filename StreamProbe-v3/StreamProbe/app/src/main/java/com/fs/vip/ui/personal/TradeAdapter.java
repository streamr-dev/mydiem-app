package com.fs.vip.ui.personal;

import android.util.Log;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fs.vip.R;
import com.fs.vip.domain.TradeBeaen;
import com.fs.vip.utils.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;


public class TradeAdapter extends BaseQuickAdapter<TradeBeaen.ResultBean, BaseViewHolder> {

    private final DecimalFormat df;

    public TradeAdapter(@Nullable List<TradeBeaen.ResultBean> data) {
        super(R.layout.item_trade, data);
        df = new DecimalFormat("######0.0");
    }

    @Override
    protected void convert(BaseViewHolder helper, TradeBeaen.ResultBean item) {
        Log.e("asdasd",item.getTimeStamp());
        helper.setText(R.id.tv_time, Utils.getDateToString(item.getTimeStamp()));
        BigInteger s = new BigInteger(item.getValue());
        helper.setText(R.id.tv_value, df.format(Double.parseDouble(toDecimal(18,s))));
    }

    public String toDecimal(int decimal, BigInteger integer) {
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        return new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
    }

}
