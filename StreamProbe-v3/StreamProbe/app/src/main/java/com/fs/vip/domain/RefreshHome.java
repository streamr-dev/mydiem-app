package com.fs.vip.domain;

public class RefreshHome {

    private boolean refresh;

    public RefreshHome(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
