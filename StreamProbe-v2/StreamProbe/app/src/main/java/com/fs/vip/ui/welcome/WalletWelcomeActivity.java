package com.fs.vip.ui.welcome;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletWelcomeActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_import)
    TextView btnImport;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_welcome;
    }

    @Override
    public void initToolBar() {
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        tvTitle.setText("Wallet");
        tvTitle.setTextColor(0xffffffff);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }


    @OnClick({R.id.btn_create, R.id.btn_import})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                startActivity(new Intent(mContext,CreateWalletActivity.class));
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                break;
            case R.id.btn_import:
                finish();
                break;
        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        overridePendingTransition(R.anim.left_in, R.anim.right_out);
//    }
}
