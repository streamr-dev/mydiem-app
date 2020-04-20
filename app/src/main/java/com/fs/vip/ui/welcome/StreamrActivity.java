package com.fs.vip.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StreamrActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @Override
    public int getLayoutId() {
        return R.layout.activity_streamr;
    }

    @Override
    public void initToolBar() {
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        tvTitle.setText("Connect to Streamr");
        tvTitle.setTextColor(0xffffffff);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }


    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        startActivity(new Intent(mContext, WalletWelcomeActivity.class));
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        overridePendingTransition(R.anim.left_in, R.anim.right_out);
//    }
}
