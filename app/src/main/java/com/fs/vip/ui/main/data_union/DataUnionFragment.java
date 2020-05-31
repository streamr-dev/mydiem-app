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

import com.fs.vip.R;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.domain.AllApps;
import com.fs.vip.domain.EarnFromGrop;
import com.fs.vip.domain.EventAppNum;
import com.fs.vip.domain.RefreshHome;
import com.fs.vip.domain.Xxx;
import com.fs.vip.service.DeService;
import com.fs.vip.utils.LoggingInterceptor;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.Utils;
import com.fs.vip.utils.WalletDaoUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.xdandroid.hellodaemon.DaemonEnv;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;

import static com.fs.vip.utils.Utils.toDecimal;

public class DataUnionFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_earn)
    TextView tvEarn;
    @BindView(R.id.tv_ava)
    TextView tvAva;
    @BindView(R.id.tv_app_num)
    TextView tvAppNum;
    @BindView(R.id.btn_state)
    Button btnState;
    @BindView(R.id.btn_withdraw)
    Button btnWithdraw;
    private Unbinder mUnBinder;
    private EarnFromGrop mEarnFromGrop;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(new LoggingInterceptor())
            .build();
    private DecimalFormat df;

    public static DataUnionFragment newInstance() {
        return new DataUnionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_union, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
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
            btnState.setText("DISABLE");
            btnState.setBackgroundResource(R.drawable.base_button_shape_red);
        } else {
            btnState.setText("ENABLE");
            btnState.setBackgroundResource(R.drawable.base_button_shape_green);
        }
    }


    private void initView(View view) {
        df = new DecimalFormat("######0.00");
        mToolbar.setTitle("Data Union");
        initToolbarNav(mToolbar, true);
        mToolbar.setOnMenuItemClickListener(this);
        handleStream();
        getAppNums();
        getGas(false);
    }

    private void getGas(boolean needToTrans) {
        String url = "https://ethgasstation.info/api/ethgasAPI.json";
        OkGo.<String>get(url)
                .retryCount(3)
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String temp = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(temp);
                            long gasPrice = jsonObject.getLong("fast");
                            WithDraw(gasPrice);

                        } catch (JSONException e) {
                            e.printStackTrace();
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
    }

    private void handleStream() {
        if (WalletDaoUtils.getCurrent() != null)
            getEarn(WalletDaoUtils.getCurrent().getAddress());
    }

    private void getEarn(String address) {
        String url = "https://streamr.network/api/v1/communities/0xE7Ca8db13F6866E495dd38d4c5585F9c897CA49F/members/" + address;//0x8Aa66dfC6DA5DA31c08DEb35Da9D58A5B2f51099
        OkGo.<String>get(url)
                .retryCount(3)
                .headers("Content-Type", "application/x-www-form-urlencoded")
//                .headers("authorization", "bearer " + SharedPreferencesUtil.getInstance().getString("token"))
//                .client(client)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String temp = response.body();
                        Log.e("DataUnionFragment111", temp);
                        mEarnFromGrop = new Gson().fromJson(temp, EarnFromGrop.class);
                        if (tvEarn != null) {
                            if (!TextUtils.isEmpty(mEarnFromGrop.getEarnings())) {
                                tvEarn.setText(df.format(Double.parseDouble(toDecimal(18, new BigInteger(mEarnFromGrop.getEarnings())))));
                                getAvaData();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("DataUnionFragment111", response.toString());
//                        if (tvEarn != null) {
//                            getEarn2(WalletDaoUtils.getCurrent());
//                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

    }

    private void getAvaData() {
        if (WalletDaoUtils.getCurrent() != null) {
            new Thread(() -> {
                try {
                    Web3j web3j = AdminFactory.build(new HttpService("https://mainnet.infura.io/v3/b3d37e5be0824340a24d34bb9f2196c1"));//https://mainnet.infura.io/v3/b3d37e5be0824340a24d34bb9f2196c1
                    Credentials credentials = WalletUtils.loadCredentials(WalletDaoUtils.getCurrent().getPassword(), WalletDaoUtils.getCurrent().getKeystorePath());
                    Xxx xxx = Xxx.load("0xE7Ca8db13F6866E495dd38d4c5585F9c897CA49F", web3j, credentials, BigInteger.valueOf(5000000000L), BigInteger.valueOf(200000L));
                    boolean isValid = xxx.isValid();
                    if (isValid) {
                        BigInteger temp = xxx.withdrawn(WalletDaoUtils.getCurrent().getAddress()).send();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                tvAva.setText(df.format(Double.parseDouble(toDecimal(18, new BigInteger(mEarnFromGrop.getWithdrawableEarnings()).subtract(temp)))));
                            });
                        }

                    }
                } catch (Exception e) {
                    Log.e("sdfdsfsdf", e.toString() + "");
                    e.printStackTrace();
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
            }).start();
        }
    }

    private void getAppNums() {
        String url = "https://streamr.network/api/v1/communities/0xE7Ca8db13F6866E495dd38d4c5585F9c897CA49F/stats";
        OkGo.<String>get(url)
                .retryCount(3)
                .headers("Content-Type", "application/x-www-form-urlencoded")
//                .headers("authorization", "bearer " + SharedPreferencesUtil.getInstance().getString("token"))
//                .client(client)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String temp = response.body();
                        Log.e("DataUnionFragment", temp);
                        AllApps mAllApps = new Gson().fromJson(temp, AllApps.class);
                        if (mAllApps != null && mAllApps.getMemberCount() != null)
                            tvAppNum.setText(mAllApps.getMemberCount().getTotal() + "");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("DataUnionFragment", response.code() + "");
//                        if (tvEarn != null) {
//                            getEarn2(WalletDaoUtils.getCurrent());
//                        }
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
        if (EventBus.getDefault() != null) EventBus.getDefault().unregister(this);
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
            btnState.setText("ENABLE");
            btnState.setBackgroundResource(R.drawable.base_button_shape_green);
        } else {
            SharedPreferencesUtil.getInstance().putBoolean("needToSend", true);
            DeService.sShouldStopService = false;
            DaemonEnv.startServiceMayBind(DeService.class);
            btnState.setText("DISABLE");
            btnState.setBackgroundResource(R.drawable.base_button_shape_red);
//            btnState.setBackgroundColor(0xFFD36581);
        }
    }

    private void WithDraw(long gasPrice) {
        new Thread(() -> {
            try {
                Web3j web3j = AdminFactory.build(new HttpService("https://mainnet.infura.io/v3/b3d37e5be0824340a24d34bb9f2196c1"));//https://mainnet.infura.io/v3/b3d37e5be0824340a24d34bb9f2196c1
                Credentials credentials = WalletUtils.loadCredentials(WalletDaoUtils.getCurrent().getPassword(), WalletDaoUtils.getCurrent().getKeystorePath());
                Xxx xxx = Xxx.load("0xE7Ca8db13F6866E495dd38d4c5585F9c897CA49F", web3j, credentials, BigInteger.valueOf(gasPrice / 10 * 1000000000), BigInteger.valueOf(200000L));
                boolean isValid = xxx.isValid();
                if (isValid) {
                    List<byte[]> b = new ArrayList<>();
                    for (int i = 0; i < mEarnFromGrop.getProof().size(); i++) {
                        b.add(Numeric.hexStringToByteArray(mEarnFromGrop.getProof().get(i)));
                    }
                    BigInteger block = new BigInteger(mEarnFromGrop.getWithdrawableBlockNumber());
                    BigInteger earn = new BigInteger(mEarnFromGrop.getWithdrawableEarnings());//mEarnFromGrop.getWithdrawableEarnings()
                    TransactionReceipt transactionReceipt = xxx.withdrawAll(block, earn, b).send();
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            hideDialog();
                            Toast.makeText(getActivity(), transactionReceipt.getStatus().equals("0x0") ? "failed" : "succeed", Toast.LENGTH_LONG).show();
                            if (WalletDaoUtils.getCurrent() != null)
                                getEarn(WalletDaoUtils.getCurrent().getAddress());
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        }).start();
    }

    @OnClick(R.id.btn_withdraw)
    public void onViewClickedWith() {
        if (WalletDaoUtils.getCurrent() != null) {
            showDialog("working...");
            getGas(true);
        }

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void d(RefreshHome eventMsg) {
        if (eventMsg.isRefresh()) {
            Log.e("sdfasdfsadf", "fresh");
            handleStream();
            getAppNums();
        }
    }

}
