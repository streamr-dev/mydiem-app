package com.fs.vip.ui.main.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.domain.EventName;
import com.fs.vip.ui.personal.PersonalActivity;
import com.fs.vip.utils.Md5Utils;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.view.ListPopup;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AccountFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_name)
    TextInputEditText tvName;
    @BindView(R.id.tv_age)
    TextInputEditText tvAge;
    @BindView(R.id.v_age)
    View vAge;
    @BindView(R.id.tv_gender)
    TextInputEditText tvGender;
    @BindView(R.id.v_gender)
    View vGender;
    @BindView(R.id.tv_race)
    TextInputEditText tvRace;
    @BindView(R.id.tv_region)
    TextInputEditText tvRegion;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.btn_any)
    Button btnAny;
    private Unbinder mUnBinder;
    private ListPopup mListPopup;
    private ListPopup.Builder builder;
    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle("Account Info");
        initToolbarNav(mToolbar, true);
        mToolbar.setOnMenuItemClickListener(this);
        tvName.setText(SharedPreferencesUtil.getInstance().getString("name"));
        tvGender.setText(SharedPreferencesUtil.getInstance().getString("gender"));
        tvAge.setText(SharedPreferencesUtil.getInstance().getString("age"));
        tvRace.setText(SharedPreferencesUtil.getInstance().getString("race"));
        tvRegion.setText(SharedPreferencesUtil.getInstance().getString("region"));
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
     * 类似于 Activity的 onNewIntent()
     */
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
    }

    @OnClick({R.id.v_gender, R.id.v_age, R.id.btn_done, R.id.btn_any})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.v_gender:
                builder = new ListPopup.Builder(getActivity());
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
                ListPopup.Builder builder = new ListPopup.Builder(getActivity());
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
                    EventBus.getDefault().postSticky(new EventName(tvName.getText().toString()));
                } else {
                    ToastUtils.showLongToast("Please enter all info.");
                }
                break;
            case R.id.btn_any:
                tvName.setText(Md5Utils.md5(System.currentTimeMillis() + "").toUpperCase().substring(0, 8));
                break;

        }
    }
}
