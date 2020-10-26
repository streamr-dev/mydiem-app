package com.fs.vip.ui.main.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.domain.ETHWallet;
import com.fs.vip.domain.JoinGroup;
import com.fs.vip.domain.TradeBeaen;
import com.fs.vip.utils.ETHWalletUtils;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.utils.WalletDaoUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.streamr.client.StreamrClient;
import com.streamr.client.authentication.EthereumAuthenticationMethod;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ImportWalletFragment extends BaseMainFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_seeds)
    EditText etSeeds;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_done)
    Button btnDone;
    private Unbinder mUnBinder;
    ETHWallet wallet;

    public static ImportWalletFragment newInstance() {
        ImportWalletFragment fragment = new ImportWalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import_wallet, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle("Import a Wallet");
        initToolbarNav(mToolbar, false);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mUnBinder != null) {
            this.mUnBinder.unbind();
        }
    }

    /**
     * Something like Activity's onNewIntent()
     */
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);

    }

    @OnClick(R.id.btn_done)
    public void onViewClicked() {
        if (!TextUtils.isEmpty(etSeeds.getText()) && !TextUtils.isEmpty(etPassword.getText())) {
            String mnemonic = etSeeds.getText().toString().trim();
            String walletPwd = etPassword.getText().toString().trim();
            showDialog("Loading...");

            Observable.create((ObservableOnSubscribe<ETHWallet>) e -> {
                ETHWallet ethWallet = ETHWalletUtils.importMnemonic(ETHWalletUtils.ETH_JAXX_TYPE
                        , Arrays.asList(mnemonic.split(" ")), walletPwd);
                e.onNext(ethWallet);
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ETHWallet>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(ETHWallet wallet) {
                            if (btnDone != null) {
                                WalletDaoUtils.insertNewWallet(wallet);
                                WalletDaoUtils.updateCurrent(wallet.getId());
                                AddToGroup(wallet);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (btnDone != null) {
                                dismissDialog();
                                ToastUtils.showLongToast(e.toString());
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }
    }

    private void AddToGroup(ETHWallet wallet) {
        new Thread(() -> {
            try {
                String privateKey = ETHWalletUtils.derivePrivateKey(wallet, wallet.getPassword());
                StreamrClient client = new StreamrClient(new EthereumAuthenticationMethod(privateKey));
                final String token = client.getSessionToken();
                SharedPreferencesUtil.getInstance().putString("token",token);
                String url = "https://www.streamr.com/api/v1/dataunions/0x65ea2587c89aff664ca8b7de8c2ff49ba07e050e/joinRequests";
                OkGo.<String>post(url)
                        .retryCount(3)
                        .params("memberAddress", wallet.getAddress())
                        .params("secret","QQJ7ElNuT66GcKAEM_tGpwsheuVHsOQpiMVXDMYGsfTw")
                        .headers("Content-Type","application/json")
                        .headers("authorization","bearer "+token)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                JoinGroup mJoin = new Gson().fromJson(response.body(),JoinGroup.class);
                                if (!TextUtils.isEmpty(mJoin.getId())){
                                    dismissDialog();
                                    ToastUtils.showLongToast("Complete");
                                    Bundle bundle = new Bundle();
                                    setFragmentResult(2, bundle);
                                    pop();
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
}
