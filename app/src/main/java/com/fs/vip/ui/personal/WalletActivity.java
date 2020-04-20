package com.fs.vip.ui.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fs.vip.R;
import com.fs.vip.base.BaseActivity;
import com.fs.vip.domain.ETHWallet;
import com.fs.vip.domain.TradeBeaen;
import com.fs.vip.utils.CopyUtil;
import com.fs.vip.utils.WalletDaoUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

//import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.AdminFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

public class WalletActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lly_back)
    LinearLayout llyBack;
    @BindView(R.id.rl_btn)
    LinearLayout rlBtn;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.btn_copy)
    Button btnCopy;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.cv)
    CardView cv;
    private ETHWallet ethWallet;
    private TradeAdapter adapter;
    private int page = 1;
    private boolean isSee = false;
    private boolean isShow = true;
    private DecimalFormat df;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    public void initToolBar() {
        commonToolbar.setBackgroundColor(0xFF7CAEEB);
        tvTitle.setText("Wallet");
        tvTitle.setTextColor(0xffffffff);
        rlBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void initDatas() {
        cv.setVisibility(View.GONE);
        df = new DecimalFormat("######0.0");
        if (WalletDaoUtils.getCurrent() != null) {
            ethWallet = WalletDaoUtils.getCurrent();
            tvAddress.setText(ethWallet.getAddress());
            showDialog("加载中...");
            LoadData();
        }

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
                       tvAddress.setText(df.format(Double.parseDouble(balance)));
                       dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                        if (tvAddress != null)
                            tvAddress.setText("error");
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void LoadData() {
        String url = "https://api.etherscan.io/api?module=account&action=tokentx&contractaddress=0x0Cf0Ee63788A0849fE5297F3407f701E122cC023&address=" + ethWallet.getAddress() + "&page=" + page + "&offset=20&sort=asc";
        OkGo.<String>get(url)
                .retryCount(3)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissDialog();
                        Gson gson = new Gson();
                        TradeBeaen bean = gson.fromJson(response.body(), TradeBeaen.class);
                        if ("1".equals(bean.getStatus())) {
                            adapter.addData(bean.getResult());
                            adapter.notifyDataSetChanged();
                            adapter.loadMoreComplete();
                        } else {
                            adapter.loadMoreEnd();
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

    @Override
    public void onLoadMoreRequested() {
        page++;
        LoadData();
    }

    @Override
    public void configViews() {
        adapter = new TradeAdapter(null);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.setOnLoadMoreListener(this, rv);
        rv.setAdapter(adapter);
    }


    @OnClick({R.id.rl_btn, R.id.btn_copy, R.id.tv_edit, R.id.tv_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_btn:
                isSee = !isSee;
                if (isSee) {
                    cv.setVisibility(View.VISIBLE);
                } else {
                    cv.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_copy:
                if (isShow){
                    CopyUtil.copy(mContext, tvAddress.getText().toString().trim());
                    btnCopy.setText("COPIED!");
                }else{
                    isSee = false;
                    isShow = true;
                    tvAddress.setText(ethWallet.getAddress());
                    tvName.setText("ETHEREUM ADDRESS");
                }
                break;
            case R.id.tv_edit:
                isSee = false;
                isShow = true;
                tvAddress.setText(ethWallet.getAddress());
                tvName.setText("ETHEREUM ADDRESS");
                cv.setVisibility(View.GONE);
                break;
            case R.id.tv_info:
                isSee = false;
                isShow = false;
                tvName.setText("RECEIVED $DATA");
                showDialog("loading...");
                btnCopy.setText("SHOW ADDRESS");
                getBalance(ethWallet.getAddress());
                cv.setVisibility(View.GONE);
                break;
        }
    }

    public String toDecimal(int decimal, BigInteger integer) {
        StringBuffer sbf = new StringBuffer("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        return new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, BigDecimal.ROUND_DOWN).toPlainString();
    }


}
