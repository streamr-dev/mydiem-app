package com.fs.vip.domain;

public class UploadBean {
    private String appInfo;

    public UploadBean(String appInfo) {
        this.appInfo = appInfo;
    }

    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }
}
