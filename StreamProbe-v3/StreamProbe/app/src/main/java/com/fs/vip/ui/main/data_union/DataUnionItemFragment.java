package com.fs.vip.ui.main.data_union;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fs.vip.R;
import com.fs.vip.Statistics.AppInformation;
import com.fs.vip.Statistics.StatisticsInfo;
import com.fs.vip.base.BaseMainFragment;
import com.fs.vip.view.CirclePercentView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataUnionItemFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {
    RecyclerView rv;
    SwipeRefreshLayout swipeLayout;
    private static int pos;
    private StatisticsInfo statisticsInfo;
    private BottomSheetDialog answerSheetDialog;
    private long totalTime;
    private ArrayList<AppInformation> datalist;
    private TimeUsedAdapter adapter;

    public static DataUnionItemFragment newInstance() {
        Bundle args = new Bundle();
        DataUnionItemFragment fragment = new DataUnionItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_union_item, container, false);
        pos = getArguments().getInt("position");
        initView(view);
        return view;
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(() -> {
            adapter.setNewData(null);
            getAppData();
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initAdapter();
        getAppData();
    }

    private void getAppData() {
        Observable.create((ObservableOnSubscribe<ArrayList<AppInformation>>) e -> {
            statisticsInfo = new StatisticsInfo(getActivity(), getArguments().getInt("position"));
            datalist = statisticsInfo.getEnableList();
            totalTime = getTimeAll(datalist);
            e.onNext(datalist);
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<AppInformation>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (swipeLayout != null) {
                            swipeLayout.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onNext(ArrayList<AppInformation> balance) {
                        if (swipeLayout != null) {
                            swipeLayout.setRefreshing(false);
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
        DecimalFormat df = new DecimalFormat("#,###");
        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(ll);
        adapter = new TimeUsedAdapter(R.layout.item_time_used, null);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (answerSheetDialog == null) {
                answerSheetDialog = new BottomSheetDialog(getActivity());
            }
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.view_botom_sheet, null, false);
            answerSheetDialog.setContentView(inflate);
            answerSheetDialog.setCanceledOnTouchOutside(true);
            answerSheetDialog.setCancelable(true);
            answerSheetDialog.show();
            CirclePercentView cp = inflate.findViewById(R.id.cp1);
            TextView tvTime = inflate.findViewById(R.id.tv_time);
            TextView tvAppName = inflate.findViewById(R.id.tv_app_name);
            TextView tvType = inflate.findViewById(R.id.tv_type);
            tvTime.setText(datalist.get(position).getUsedTimebyDay() / 1000 / 60 != 0 ? df.format(datalist.get(position).getUsedTimebyDay() / 1000 / 60) : df.format(datalist.get(position).getUsedTimebyDay() / 1000));
            tvAppName.setText(datalist.get(position).getLabel());
            tvType.setText(datalist.get(position).getUsedTimebyDay() / 1000 / 60 != 0 ? "minutes" : "seconds");
            cp.setPercentage((float) ((double) datalist.get(position).getUsedTimebyDay() / (double) totalTime * 100));
            answerSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        });
    }

    private long getTimeAll(ArrayList<AppInformation> datalist) {
        long time = 0;
        for (AppInformation information : datalist) {
            time += information.getUsedTimebyDay();
        }
        return time;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
