package com.fs.vip.ui.main.data_union;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.fs.vip.R;
import com.fs.vip.base.BaseFragment;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.base.MySupportFragment;
import com.fs.vip.domain.AllApps;
import com.fs.vip.domain.ETHWallet;
import com.fs.vip.domain.ETHWalletDao;
import com.fs.vip.domain.EarnFromGrop;
import com.fs.vip.domain.JoinGroup;
import com.fs.vip.domain.Xxx;
import com.fs.vip.service.DeService;
import com.fs.vip.utils.ETHMnemonic;
import com.fs.vip.utils.ETHWalletUtils;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.ToastUtils;
import com.fs.vip.utils.Utils;
import com.fs.vip.utils.WalletDaoUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.streamr.client.StreamrClient;
import com.streamr.client.authentication.EthereumAuthenticationMethod;
import com.xdandroid.hellodaemon.DaemonEnv;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

import static com.fs.vip.utils.Utils.toDecimal;
import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

public class DataUnionFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_earn)
    TextView tvEarn;
    @BindView(R.id.tv_app_num)
    TextView tvAppNum;
    @BindView(R.id.btn_state)
    Button btnState;
    @BindView(R.id.btn_withdraw)
    Button btnWithdraw;
    private Unbinder mUnBinder;
    private EarnFromGrop mEarnFromGrop;

    public static DataUnionFragment newInstance() {
        return new DataUnionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_union, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (findChildFragment(ViewPagerFragment.class) == null) {
            loadRootFragment(R.id.fl_container, ViewPagerFragment.newInstance());
        }
        if (DeService.isWorking() && SharedPreferencesUtil.getInstance().getBoolean("needToSend")) {
            btnState.setText("DISABLE STREAMING");
            btnState.setBackgroundColor(0xFFD36581);
        } else {
            btnState.setText("ENABLE STREAMING");
            btnState.setBackgroundColor(0xFF74A9E9);
        }
    }

    private void initView(View view) {
        mToolbar.setTitle("Data Union");
        initToolbarNav(mToolbar, true);
        mToolbar.setOnMenuItemClickListener(this);
        handleStream();
        getAppNums();
    }

    private void handleStream() {
        if (WalletDaoUtils.getCurrent() != null)
            getEarn(WalletDaoUtils.getCurrent().getAddress(), true);
    }

    private void getEarn(String address, final boolean needRe) {
        String url = "https://streamr.network/api/v1/communities/0x0df55C565881b253D307e9a8a95C907DFA228283/members/" + address;//0x8Aa66dfC6DA5DA31c08DEb35Da9D58A5B2f51099
        OkGo.<String>get(url)
                .retryCount(3)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("authorization", "bearer " + SharedPreferencesUtil.getInstance().getString("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String temp = response.body();
                        mEarnFromGrop = new Gson().fromJson(temp, EarnFromGrop.class);
                        Log.e("ssdfdsfdsf", temp);
                        if (tvEarn != null) {
                            if (!TextUtils.isEmpty(mEarnFromGrop.getEarnings())) {
                                BigInteger s = new BigInteger(mEarnFromGrop.getEarnings().equals("0") ? "0" : mEarnFromGrop.getEarnings().substring(2), 16);
                                BigInteger s1 = new BigInteger(mEarnFromGrop.getWithdrawableEarnings().equals("0") ? "0" : mEarnFromGrop.getWithdrawableEarnings().substring(2), 16);
                                DecimalFormat df = new DecimalFormat("######0.00");
                                String temp1 = df.format(Double.parseDouble(toDecimal(18, s)));
                                String temp2 = df.format(Double.parseDouble(toDecimal(18, s1)));
                                tvEarn.setText(temp1 + " DATA balance    " + temp2 + " DATA available");
                            } else {
                                if (needRe) {
                                    getEarn2(WalletDaoUtils.getCurrent());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (tvEarn != null) {
                            getEarn2(WalletDaoUtils.getCurrent());
                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

    }

    private void getEarn2(ETHWallet wallet) {
        new Thread(() -> {
            try {
                String privateKey = ETHWalletUtils.derivePrivateKey(wallet, wallet.getPassword());
                StreamrClient client = new StreamrClient(new EthereumAuthenticationMethod(privateKey));
                final String token = client.getSessionToken();
                SharedPreferencesUtil.getInstance().putString("token", token);
                getEarn(wallet.getAddress(), false);
            } catch (Exception e) {
                Log.e("token", e.toString());
            }

        }).start();

    }

    private void getAppNums() {
        String url = "https://streamr.network/api/v1/communities/0x0df55C565881b253D307e9a8a95C907DFA228283/stats";
        OkGo.<String>get(url)
                .retryCount(3)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .headers("authorization", "bearer " + SharedPreferencesUtil.getInstance().getString("token"))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String temp = response.body();
                        AllApps mAllApps = new Gson().fromJson(temp, AllApps.class);
                        if (mAllApps != null && mAllApps.getMemberCount() != null)
                            tvAppNum.setText(mAllApps.getMemberCount().getTotal() + "");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (tvEarn != null) {
                            getEarn2(WalletDaoUtils.getCurrent());
                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

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

    @OnClick(R.id.btn_state)
    public void onViewClicked() {
        if (DeService.isWorking()) {
            SharedPreferencesUtil.getInstance().putBoolean("needToSend", false);
            DeService.cancelJobAlarmSub();
            DeService.stopService();
            btnState.setText("ENABLE STREAMING");
            btnState.setBackgroundColor(0xFF74A9E9);
        } else {
            SharedPreferencesUtil.getInstance().putBoolean("needToSend", true);
            DeService.sShouldStopService = false;
            DaemonEnv.startServiceMayBind(DeService.class);
            btnState.setText("DISABLE STREAMING");
            btnState.setBackgroundColor(0xFFD36581);
        }
    }

    @OnClick(R.id.btn_withdraw)
    public void onViewClickedWith() {
        if (WalletDaoUtils.getCurrent() != null) {
            showDialog("working...");
            Web3j web3j = AdminFactory.build(new HttpService("https://mainnet.infura.io/v3/b3d37e5be0824340a24d34bb9f2196c1"));
            Credentials credentials = WalletUtils.loadBip39Credentials(WalletDaoUtils.getCurrent().getPassword(), WalletDaoUtils.getCurrent().getMnemonic());
            Xxx xxx = Xxx.load("0x0df55C565881b253D307e9a8a95C907DFA228283", web3j, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT);
            new Thread(() -> {
                try {
                    boolean isValid = xxx.isValid();
                    Log.e("TAG", "contract isValid : " + isValid);
                    if (isValid) {
//                            List<String> candidateNames = Arrays.asList("Bob", "Tom", "Jerry");
//                            String temp = xxx.withdrawAll(new BigInteger(mEarnFromGrop.getAddress()),
//                                    new BigInteger(mEarnFromGrop.getWithdrawableEarnings()),
//                                    candidateNames).send();
                        try {
                            TransactionReceipt temp = xxx.withdrawTo(WalletDaoUtils.getCurrent().getAddress(), mEarnFromGrop.getAddress(), new BigInteger(mEarnFromGrop.getWithdrawableEarnings())).send();
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    hideDialog();
                                    Toast.makeText(getActivity(), temp.toString(), Toast.LENGTH_LONG).show();
                                    if (WalletDaoUtils.getCurrent() != null)
                                        getEarn(WalletDaoUtils.getCurrent().getAddress(), false);
                                });
                            }
                        } catch (Exception e) {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideDialog();
                                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    hideDialog();
                    Log.i("TAG", "load: " + e.getMessage());
                }
            }).start();
        }

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create
    }
}
