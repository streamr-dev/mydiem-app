package com.fs.vip.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;
import com.fs.vip.domain.ETHWallet;
import com.fs.vip.domain.JoinGroup;
import com.fs.vip.ui.main.MainActivity;
import com.fs.vip.ui.personal.AppsInfoActivity;
import com.fs.vip.ui.personal.PersonalActivity;
import com.fs.vip.ui.personal.WalletActivity;
import com.fs.vip.utils.ETHWalletUtils;
import com.fs.vip.utils.LogUtils;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.utils.WalletDaoUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.streamr.client.StreamrClient;
import com.streamr.client.authentication.EthereumAuthenticationMethod;

import org.web3j.crypto.WalletUtils;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateWalletActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.ll_next1)
    LinearLayout llNext1;
    @BindView(R.id.ll_next2)
    LinearLayout llNext2;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.tv_password)
    TextInputEditText tvPassword;
    @BindView(R.id.tv_re_password)
    TextInputEditText tvRePassword;
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.tv_seed)
    TextView tvSeed;
    @BindView(R.id.btn_continue)
    Button btnContinue;

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_wallet;
    }

    @Override
    public void initToolBar() {
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        tvTitle.setText("Create a wallet");
        tvTitle.setTextColor(0xffffffff);
    }

    @Override
    public void initDatas() {
        tvPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               changeCreateButton();

            }
        });
        tvRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeCreateButton();

            }
        });
    }

    private void changeCreateButton() {
        if (!TextUtils.isEmpty(tvPassword.getText())&&!TextUtils.isEmpty(tvRePassword.getText())&&tvPassword.getText().toString().length()>=8&&tvRePassword.getText().toString().length()>=8){
            btnCreate.setBackgroundResource(R.drawable.base_button_shape);
            btnCreate.setClickable(true);
        }else{
            btnCreate.setBackgroundResource(R.drawable.base_button_shape_gray);
            btnCreate.setClickable(false);
        }
    }

    @Override
    public void configViews() {

    }


    @OnClick({R.id.btn_create, R.id.btn_continue,R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_create:
                if (!TextUtils.isEmpty(tvPassword.getText())&&!TextUtils.isEmpty(tvRePassword.getText())&&tvPassword.getText().toString().equals(tvRePassword.getText().toString())){
                    showDialog("Creating");
                    //Hide input keyboard after clicking Create wallet button
                    InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    Observable.create((ObservableOnSubscribe<ETHWallet>) e -> {
                        ETHWallet ethWallet = ETHWalletUtils.generateMnemonic(generateNewWalletName(), tvPassword.getText().toString());
                        WalletDaoUtils.insertNewWallet(ethWallet);
                        e.onNext(ethWallet);
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<ETHWallet>() {
                                @Override
                                public void onSubscribe(Disposable d) {
//                                    dismissDialog();
                                }

                                @Override
                                public void onNext(ETHWallet wallet) {
                                    AddToGroup(wallet);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastUtils.showLongToast(e.toString());
                                    LogUtils.e("CreateWalletPresenter", e.toString());
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                    //Disable input fields for password after wallet creation
                    tvPassword.setEnabled(false);
                    tvRePassword.setEnabled(false);
                    btnCreate.setEnabled(false);
                }
                break;
            case R.id.btn_continue:
                SharedPreferencesUtil.getInstance().putString("welcomed","true");
                Intent intent = new Intent(mContext,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
    }

    @NonNull
    private static String generateNewWalletName() {
        char letter1 = (char) (int) (Math.random() * 26 + 97);
        char letter2 = (char) (int) (Math.random() * 26 + 97);
        String walletName = String.valueOf(letter1) + String.valueOf(letter2) + "-新钱包";
        while (WalletDaoUtils.walletNameChecking(walletName)) {
            letter1 = (char) (int) (Math.random() * 26 + 97);
            letter2 = (char) (int) (Math.random() * 26 + 97);
            walletName = String.valueOf(letter1) + String.valueOf(letter2) + "-新钱包";
        }
        return walletName;
    }

    private void AddToGroup(ETHWallet wallet) {
        new Thread(() -> {
            try {
                String privateKey = ETHWalletUtils.derivePrivateKey(wallet, wallet.getPassword());
                StreamrClient client = new StreamrClient(new EthereumAuthenticationMethod(privateKey));
                final String token = client.getSessionToken();
                SharedPreferencesUtil.getInstance().putString("token",token);
                String url = "https://www.streamr.com/api/v1/communities/0xE7Ca8db13F6866E495dd38d4c5585F9c897CA49F/joinRequests";
                OkGo.<String>post(url)
                        .retryCount(3)
                        .params("memberAddress", wallet.getAddress())
                        .params("secret","h6MdRoxQN69PXNVMxJoLM")
                        .headers("Content-Type","application/x-www-form-urlencoded")
                        .headers("authorization","bearer "+token)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                JoinGroup mJoin = new Gson().fromJson(response.body(),JoinGroup.class);
                                if (!TextUtils.isEmpty(mJoin.getId())){
                                    dismissDialog();
                                    ToastUtils.showLongToast("success");
                                    tvSeed.setText(wallet.getMnemonic().trim());
                                    llNext1.setVisibility(View.GONE);
                                    llNext2.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);

                            }


                            @Override
                            public void onFinish() {
                                super.onFinish();
                            }
                        });
            } catch (Exception e) {
                Log.e("token", e.toString());
            }

        }).start();

    }
    /**
     * 加载一个已经部署的合约
     * @param contractAddress 合约地址
     */
    public void load(String contractAddress) {
//        simpleStorage = SimpleStorage.load(contractAddress, web3j, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    boolean isValid = simpleStorage.isValid();
//                    Log.i(TAG, "contract isValid : " + isValid);
//                    if (isValid) {
//                        get();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.i(TAG, "load: " + e.getMessage());
//                }
//            }
//        }).start();
    }
}
