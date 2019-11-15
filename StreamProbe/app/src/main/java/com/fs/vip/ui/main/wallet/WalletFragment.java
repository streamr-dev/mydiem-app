package com.fs.vip.ui.main.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.domain.ETHWallet;
import com.fs.vip.utils.CopyUtil;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.utils.Utils;
import com.fs.vip.utils.WalletDaoUtils;
import com.fs.vip.view.WalletDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.text.DecimalFormat;

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

import static com.fs.vip.utils.Utils.toDecimal;

public class WalletFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.btn_export)
    Button btnExport;
    @BindView(R.id.btn_import)
    Button btnImport;
    @BindView(R.id.btn_history)
    Button btnHistory;
    @BindView(R.id.btn_address)
    Button btnAddress;
    @BindView(R.id.btn_copy)
    Button btnCopy;
    private Unbinder mUnBinder;
    private static final String DATA_PREFIX = "0x70a08231000000000000000000000000";

    public static WalletFragment newInstance() {
        return new WalletFragment();
    }

    private DecimalFormat df;
    WalletDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle("Wallet");
        df = new DecimalFormat("######0.0");
        mToolbar.inflateMenu(R.menu.home);
        initToolbarNav(mToolbar, true);
        mToolbar.setOnMenuItemClickListener(this);
        if (WalletDaoUtils.getCurrent() != null) {
            ETHWallet ethWallet = WalletDaoUtils.getCurrent();
            btnAddress.setText(ethWallet.getAddress());
            showDialog("loading...");
            getBalance(ethWallet.getAddress());
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (dialog == null && WalletDaoUtils.getCurrent() != null) {
            dialog = new WalletDialog(getActivity(), R.style.dialog, WalletDaoUtils.getCurrent().getAddress());
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
     * Something like Activity's onNewIntent()
     */
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);

    }

    private void getBalance(String adddress) {
        Observable.create((ObservableOnSubscribe<String>) e -> {
            String value = AdminFactory.build(new HttpService("https://mainnet.infura.io/"))
                    .ethCall(Transaction.createEthCallTransaction(adddress,
                            "0x0Cf0Ee63788A0849fE5297F3407f701E122cC023", DATA_PREFIX + adddress.substring(2)), DefaultBlockParameterName.PENDING).send().getValue();
            BigInteger s = new BigInteger(value.substring(2), 16);
            e.onNext(toDecimal(18, s));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String balance) {
                        if (tvBalance!=null){
                            tvBalance.setText(df.format(Double.parseDouble(balance)) + " DATA");
                            dismissDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (tvBalance != null) {
                            dismissDialog();
                            tvBalance.setText("error");
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    @OnClick({R.id.btn_export, R.id.btn_import, R.id.btn_history, R.id.btn_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_export:
                start(ExportWalletFragment.newInstance());
                break;
            case R.id.btn_import:
                startForResult(ImportWalletFragment.newInstance(),1);
                break;
            case R.id.btn_history:
                start(HistoryWalletFragment.newInstance());
                break;
            case R.id.btn_copy:
                CopyUtil.copy(getActivity(), btnAddress.getText().toString());
                ToastUtils.showLongToast("Copied");
                break;
        }
    }
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode==2){
            if (WalletDaoUtils.getCurrent() != null) {
                ETHWallet ethWallet = WalletDaoUtils.getCurrent();
                btnAddress.setText(ethWallet.getAddress());
                showDialog("loading...");
                getBalance(ethWallet.getAddress());
            }
        }
    }
}
