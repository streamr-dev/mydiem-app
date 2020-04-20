package com.fs.vip.ui.main.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fs.vip.R;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.domain.ETHWallet;
import com.fs.vip.domain.ETHWalletDao;
import com.fs.vip.utils.WalletDaoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExportWalletFragment extends BaseMainFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_seed)
    TextView tvSeed;
    @BindView(R.id.btn_done)
    Button btnDone;
    private Unbinder mUnBinder;
    ETHWallet wallet;

    public static ExportWalletFragment newInstance() {
        ExportWalletFragment fragment = new ExportWalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_wallet, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle("Export my Wallet");
        initToolbarNav(mToolbar, false);
        wallet = WalletDaoUtils.getCurrent();
        tvSeed.setText(wallet != null ? wallet.getMnemonic() : "");
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
        pop();
    }
}
