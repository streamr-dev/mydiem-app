package com.fs.vip.service;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import com.fs.vip.Statistics.AppInformation;
import com.fs.vip.Statistics.StatisticsInfo;
import com.fs.vip.domain.UploadBean;
import com.fs.vip.utils.LogUtils;
import com.fs.vip.utils.OkHttpUtil;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.fs.vip.utils.WalletDaoUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xdandroid.hellodaemon.AbsWorkService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;

public class DeService extends AbsWorkService {
    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService;
    public static Disposable sDisposable;
    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;

    public static void stopService() {
        sShouldStopService = true;
        if (sDisposable != null) sDisposable.dispose();
        cancelJobAlarmSub();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return sShouldStopService;
    }

    public static boolean isWorking() {
        return sDisposable != null && !sDisposable.isDisposed();
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        if (WalletDaoUtils.getCurrent() != null) {
            if (sDisposable != null) sDisposable.dispose();
            sDisposable = null;
            sDisposable = Observable
                    .interval(10, TimeUnit.SECONDS)
                    .doOnDispose(AbsWorkService::cancelJobAlarmSub)
                    .subscribe(count -> getAppUse());
        }
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopService();
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        return sDisposable != null && !sDisposable.isDisposed();
    }

    @Override
    public IBinder onBind(Intent intent, Void v) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
    }

    protected void stream(String info) {
        String header = "token " + "rc0L3E0YQRCpfa-gWVz0YQoStpnwE7QTmeN9x3y21Xig";
        String url = "https://www.streamr.com/api/v1/streams/" + "JYm4iCZbS9-ZqYtzSwH0eg" + "/data";
        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.e(e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtils.e(response.body().string());
            }
        };
        UploadBean bean = new UploadBean(info);
        OkHttpUtil.httpPost(url, new Gson().toJson(bean), callback, header);
    }

    private void getAppUse() {


        Observable observeDay = Observable.create((ObservableOnSubscribe<String>) e -> {
            StatisticsInfo statisticsInfo = new StatisticsInfo(DeService.this, StatisticsInfo.DAY);
            ArrayList<AppInformation> datalist = statisticsInfo.getEnableList();
            StringBuilder temp = new StringBuilder("Day Used:").append("\n");
            for (AppInformation info : datalist) {
                temp.append(info.getLabel()).append("Used time:").append(info.getUsedTimebyDay()).append("\n");
            }
            e.onNext(temp.toString());
        }).subscribeOn(Schedulers.computation());
        Observable observeWeek = Observable.create((ObservableOnSubscribe<String>) e -> {
            StatisticsInfo statisticsInfo = new StatisticsInfo(DeService.this, StatisticsInfo.WEEK);
            ArrayList<AppInformation> datalist = statisticsInfo.getEnableList();
            StringBuilder temp = new StringBuilder("Week Used:").append("\n");
            for (AppInformation info : datalist) {
                temp.append(info.getLabel()).append("   Used time:").append(info.getUsedTimebyDay()).append("\n");
            }
            e.onNext(temp.toString());
        }).subscribeOn(Schedulers.computation());
        Observable observeMonth = Observable.create((ObservableOnSubscribe<String>) e -> {
            StatisticsInfo statisticsInfo = new StatisticsInfo(DeService.this, StatisticsInfo.MONTH);
            ArrayList<AppInformation> datalist = statisticsInfo.getEnableList();
            StringBuilder temp = new StringBuilder("Month Used:").append("\n");
            for (AppInformation info : datalist) {
                temp.append(info.getLabel()).append("   Used time:").append(info.getUsedTimebyDay()).append("\n");
            }
            e.onNext(temp.toString());
        }).subscribeOn(Schedulers.computation());
        Observable observeYear = Observable.create((ObservableOnSubscribe<String>) e -> {
            StatisticsInfo statisticsInfo = new StatisticsInfo(DeService.this, StatisticsInfo.YEAR);
            ArrayList<AppInformation> datalist = statisticsInfo.getEnableList();
            StringBuilder temp = new StringBuilder("Year Used:").append("\n");
            for (AppInformation info : datalist) {
                temp.append(info.getLabel()).append("   Used time:").append(info.getUsedTimebyDay()).append("\n");
            }
            e.onNext(temp.toString());
        }).subscribeOn(Schedulers.computation());

        Observable.zip(observeDay, observeWeek, observeMonth, observeYear,
                ((Function4<String, String, String, String, String>)
                        (app1, app2, app3, app4) -> app1 + app2 + app3 + app4))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String apps) {
                        String userInfo = "name:" + SharedPreferencesUtil.getInstance().getString("name") + "\n"
                                + "gender:" + SharedPreferencesUtil.getInstance().getString("gender") + "\n"
                                + "age:" + SharedPreferencesUtil.getInstance().getString("age") + "\n"
                                + "race:" + SharedPreferencesUtil.getInstance().getString("race") + "\n"
                                + "region:" + SharedPreferencesUtil.getInstance().getString("region");
                        String sb = "AppInfo:" + apps + "\n" + "UserInfo" + "\n" + userInfo;
                        if (WalletDaoUtils.getCurrent()!=null){
                            sb = "\n"+sb+"wallet:"+WalletDaoUtils.getCurrent().getAddress();
//                            MtjServerDatabaseTools.insertIntoData(WalletDaoUtils.getCurrent().getAddress());
                            stream(sb);
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

}
