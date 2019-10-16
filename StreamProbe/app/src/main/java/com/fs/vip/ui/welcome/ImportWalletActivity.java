package com.fs.vip.ui.welcome;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;
import com.fs.vip.domain.ETHWallet;
import com.fs.vip.ui.personal.AppsInfoActivity;
import com.fs.vip.utils.ETHWalletUtils;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.utils.WalletDaoUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ImportWalletActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.et_seeds)
    EditText etSeeds;
    @BindView(R.id.btn_import)
    Button btnImport;
    @BindView(R.id.et_password)
    EditText etPassWord;

    @Override
    public int getLayoutId() {
        return R.layout.activity_import_wallet;
    }

    @Override
    public void initToolBar() {
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        tvTitle.setText("Import a Wallet");
        tvTitle.setTextColor(0xffffffff);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }


    @OnClick(R.id.btn_import)
    public void onViewClicked() {
        if (!TextUtils.isEmpty(etSeeds.getText()) && !TextUtils.isEmpty(etPassWord.getText())) {
            String mnemonic = etSeeds.getText().toString().trim();
            String walletPwd = etPassWord.getText().toString().trim();
            showDialog("Loading...");

            Observable.create((ObservableOnSubscribe<ETHWallet>) e -> {
                ETHWallet ethWallet = ETHWalletUtils.importMnemonic(ETHWalletUtils.ETH_JAXX_TYPE
                        , Arrays.asList(mnemonic.split(" ")), walletPwd);
                if (ethWallet != null) {
                    WalletDaoUtils.insertNewWallet(ethWallet);
                    WalletDaoUtils.updateCurrent(ethWallet.getId());
                }
                e.onNext(ethWallet);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ETHWallet>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(ETHWallet wallet) {
                            dismissDialog();
                            SharedPreferencesUtil.getInstance().putString("welcomed", "true");
                            Intent intent = new Intent(mContext,AppsInfoActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissDialog();
                            ToastUtils.showLongToast(e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }
    }
}
