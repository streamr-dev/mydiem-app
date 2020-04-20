package com.fs.vip.ui.personal;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;
import com.fs.vip.utils.Md5Utils;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.view.ListPopup;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.tv_name)
    TextInputEditText tvName;
    @BindView(R.id.tv_gender)
    TextInputEditText tvGender;
    @BindView(R.id.tv_age)
    TextInputEditText tvAge;
    @BindView(R.id.tv_race)
    TextInputEditText tvRace;
    @BindView(R.id.tv_region)
    TextInputEditText tvRegion;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.btn_any)
    Button btnAny;
    @BindView(R.id.rl_btn)
    LinearLayout rlBtn;
    private ListPopup mListPopup;
    private ListPopup.Builder builder;


    @Override
    public int getLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    public void initToolBar() {
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        rlBtn.setVisibility(View.GONE);
        tvTitle.setText("Personal Information");
        tvTitle.setTextColor(0xffffffff);

    }

    @Override
    public void initDatas() {
        tvName.setText(SharedPreferencesUtil.getInstance().getString("name"));
        tvGender.setText(SharedPreferencesUtil.getInstance().getString("gender"));
        tvAge.setText(SharedPreferencesUtil.getInstance().getString("age"));
        tvRace.setText(SharedPreferencesUtil.getInstance().getString("race"));
        tvRegion.setText(SharedPreferencesUtil.getInstance().getString("region"));
    }

    @Override
    public void configViews() {


    }


    @OnClick({R.id.v_gender, R.id.v_age, R.id.btn_done, R.id.btn_any, R.id.rl_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.v_gender:
                builder = new ListPopup.Builder(PersonalActivity.this);
                builder.addItem("male");
                builder.addItem("female");
                mListPopup = builder.build();
                mListPopup.showPopupWindow();
                mListPopup.setOnListPopupItemClickListener(what -> {
                    tvGender.setText(what);
                    mListPopup.dismiss();
                });
                break;
            case R.id.v_age:
                ListPopup.Builder builder = new ListPopup.Builder(PersonalActivity.this);
                for (int i = 0; i < 120 / 5; i++) {//  1~5   6~10    11~15
                    builder.addItem(i * 5 + 1 + "~" + (i + 1) * 5);
                }
                mListPopup = builder.build();
                mListPopup.showPopupWindow();
                mListPopup.setOnListPopupItemClickListener(what -> {
                    mListPopup.dismiss();
                    tvAge.setText(what);
                });
                break;
            case R.id.btn_done:
                if (!TextUtils.isEmpty(tvName.getText()) && !TextUtils.isEmpty(tvGender.getText()) && !TextUtils.isEmpty(tvAge.getText()) && !TextUtils.isEmpty(tvRace.getText()) && !TextUtils.isEmpty(tvRegion.getText())) {
                    SharedPreferencesUtil.getInstance().putString("name", tvName.getText().toString());
                    SharedPreferencesUtil.getInstance().putString("gender", tvGender.getText().toString());
                    SharedPreferencesUtil.getInstance().putString("age", tvAge.getText().toString());
                    SharedPreferencesUtil.getInstance().putString("race", tvRace.getText().toString());
                    SharedPreferencesUtil.getInstance().putString("region", tvRegion.getText().toString());
                    ToastUtils.showLongToast("All saved.");
                } else {
                    ToastUtils.showLongToast("Please enter all info.");
                }
                break;
            case R.id.btn_any:
                tvName.setText(Md5Utils.md5(System.currentTimeMillis() + "").substring(0, 20));
                break;
            case R.id.rl_btn:
                break;
        }
    }
}
