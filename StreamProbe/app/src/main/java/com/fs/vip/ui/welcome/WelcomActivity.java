package com.fs.vip.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;
import com.fs.vip.ui.main.MainActivity;
import com.fs.vip.ui.personal.AppsInfoActivity;
import com.fs.vip.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.iv_btn)
    ImageView ivBtn;
    @BindView(R.id.rl_btn)
    LinearLayout rlBtn;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcom;
    }

    @Override
    public void initToolBar() {

        llyBack.setVisibility(View.GONE);
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        tvTitle.setText("Welcome");
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
        startActivity(new Intent(mContext, TransparencyActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
