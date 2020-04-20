package com.fs.vip.domain;

public class EventApps {

    private boolean refresh;

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public EventApps(boolean refresh) {
        this.refresh = refresh;
    }
}
