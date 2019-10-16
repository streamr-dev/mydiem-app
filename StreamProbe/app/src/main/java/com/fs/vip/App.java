package com.fs.vip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.multidex.MultiDexApplication;

import com.fs.vip.domain.DaoMaster;
import com.fs.vip.domain.DaoSession;
import com.fs.vip.service.DeService;
import com.fs.vip.utils.AppFilePath;
import com.fs.vip.utils.AppUtils;
import com.fs.vip.utils.SharedPreferencesUtil;
import com.xdandroid.hellodaemon.DaemonEnv;

public class App extends MultiDexApplication {
    private static App sInstance;

    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        AppUtils.init(this);
        AppFilePath.init(this);
        initPrefs();
        initGreenDao();

        DaemonEnv.initialize(this, DeService.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        DeService.sShouldStopService = false;
        DaemonEnv.startServiceMayBind(DeService.class);
    }

    private void initGreenDao() {
        //创建数据库表
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "wallet", null);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        daoSession = new DaoMaster(db).newSession();
    }

    public static App getsInstance() {
        return sInstance;
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }
}
