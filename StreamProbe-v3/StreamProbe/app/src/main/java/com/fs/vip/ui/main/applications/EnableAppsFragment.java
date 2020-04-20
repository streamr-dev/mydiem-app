package com.fs.vip.ui.main.applications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fs.vip.R;
import com.fs.vip.Statistics.AppInformation;
import com.fs.vip.Statistics.StatisticsInfo;
import com.fs.vip.base.BaseMainFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EnableAppsFragment extends BaseMainFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv)
    RecyclerView rv;
    private Unbinder mUnBinder;
    private EnableAppsAdapter adapter;

    public static EnableAppsFragment newInstance() {
        return new EnableAppsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enalbe_apps, container, false);
        mUnBinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle("Enable Apps");
        initToolbarNav(mToolbar, false);
        initAdapter();
        Observable.create((ObservableOnSubscribe<ArrayList<AppInformation>>) e -> {
            StatisticsInfo statisticsInfo = new StatisticsInfo(getActivity(), StatisticsInfo.YEAR);
            ArrayList<AppInformation> datalist = statisticsInfo.getShowList();
            e.onNext(datalist);
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<AppInformation>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (mToolbar != null) {
                            showDialog("loading...");
                        }
                    }

                    @Override
                    public void onNext(ArrayList<AppInformation> balance) {
                        if (mToolbar != null) {
                            dismissDialog();
                            adapter.addData(balance);
                            adapter.loadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    private void initAdapter() {
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(ll);
        adapter = new EnableAppsAdapter(R.layout.item_enable_apps, null,getArguments()!=null?getArguments().getString("enable"):null);
        rv.setAdapter(adapter);
    }


    @Override
    public void onDetach() {
        super.onDetach();
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

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create
    }
}
