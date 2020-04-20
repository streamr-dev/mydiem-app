package com.fs.vip.ui.main.wallet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fs.vip.R;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.domain.ETHWallet;
import com.fs.vip.domain.TradeBeaen;
import com.fs.vip.ui.personal.TradeAdapter;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.utils.WalletDaoUtils;
import com.fs.vip.view.WalletDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.fs.vip.utils.Utils.toDecimal;
import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

public class HistoryWalletFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    private ETHWallet ethWallet;
    private TradeAdapter adapter;
    private Unbinder mUnBinder;
    private DecimalFormat df;
    private int page = 1;
    private WalletDialog dialog;

    public static HistoryWalletFragment newInstance() {
        HistoryWalletFragment fragment = new HistoryWalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_wallet, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle("History of Balance");
        mToolbar.inflateMenu(R.menu.home);
        initToolbarNav(mToolbar, false);
        mToolbar.setOnMenuItemClickListener(this);
        df = new DecimalFormat("######0.0");
        initAdapter();
        if (WalletDaoUtils.getCurrent() != null) {
            ethWallet = WalletDaoUtils.getCurrent();
            showDialog("loading...");
            LoadData();
            getBalance(ethWallet.getAddress());
        }

    }

    private void initAdapter() {
        adapter = new TradeAdapter(null);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnLoadMoreListener(this, rv);
        rv.setAdapter(adapter);
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (dialog == null && WalletDaoUtils.getCurrent() != null) {
            dialog = new WalletDialog(getActivity(), R.style.dialog, ethWallet.getAddress());
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return false;
    }

    private void LoadData() {
        String url = "https://api.etherscan.io/api?module=account&action=tokentx&contractaddress=0x0Cf0Ee63788A0849fE5297F3407f701E122cC023&address=" + ethWallet.getAddress() + "&page=" + page + "&offset=20&sort=asc&apikey=R8CN9Y7XXN4Z2C41UCNE898XFJKCA1N3R6";
        OkGo.<String>get(url)
                .retryCount(3)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (tvBalance != null) {
                            dismissDialog();
                            Gson gson = new Gson();
                            String data = response.body();
                            Log.e("sdfadsfdfs",data);
                            try {
                                TradeBeaen bean = gson.fromJson(data, TradeBeaen.class);
                                if ("1".equals(bean.getStatus())) {
                                    adapter.addData(bean.getResult());
                                    adapter.notifyDataSetChanged();
                                    adapter.loadMoreComplete();
                                } else {
                                    adapter.loadMoreEnd();
                                }
                            } catch (Exception e) {
                                adapter.loadMoreEnd();
                            }

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (tvBalance != null) {
                            ToastUtils.showLongToast("Something Wrong...");
                            dismissDialog();
                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        LoadData();
    }

    private static final String DATA_PREFIX = "0x70a08231000000000000000000000000";

    private void getBalance(String adddress) {
        Observable.create((ObservableOnSubscribe<String>) e -> {
            String value = AdminFactory.build(new HttpService("https://mainnet.infura.io/v3/b3d37e5be0824340a24d34bb9f2196c1"))
                    .ethCall(createEthCallTransaction(adddress,
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
                        if (tvBalance != null)
                            tvBalance.setText(df.format(Double.parseDouble(balance)) + " DATA");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("sdfsdf", e.toString());
                        dismissDialog();
                        if (tvBalance != null)
                            tvBalance.setText("error");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}