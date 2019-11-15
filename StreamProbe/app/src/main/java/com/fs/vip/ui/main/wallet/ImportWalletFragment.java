package com.fs.vip.ui.main.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.domain.ETHWallet;
import com.fs.vip.utils.ETHWalletUtils;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.utils.WalletDaoUtils;

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
                            if (btnDone!=null){
                                WalletDaoUtils.insertNewWallet(wallet);
                                WalletDaoUtils.updateCurrent(wallet.getId());
                                dismissDialog();
                                ToastUtils.showLongToast("Complete");
                                Bundle bundle = new Bundle();
                                setFragmentResult(2,bundle);
                                pop();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (btnDone!=null){
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
}
