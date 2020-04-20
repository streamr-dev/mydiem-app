package com.fs.vip.domain;

public class EarnFromGrop {

    /**
     * address : 0x8Aa66dfC6DA5DA31c08DEb35Da9D58A5B2f51099
     * earnings : 0
     * recordedEarnings : 0
     * withdrawableEarnings : 0
     * frozenEarnings : 0
     */

    private String address;
    private String earnings;
    private String recordedEarnings;
    private String withdrawableEarnings;
    private String frozenEarnings;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public String getRecordedEarnings() {
        return recordedEarnings;
    }

    public void setRecordedEarnings(String recordedEarnings) {
        this.recordedEarnings = recordedEarnings;
    }

    public String getWithdrawableEarnings() {
        return withdrawableEarnings;
    }

    public void setWithdrawableEarnings(String withdrawableEarnings) {
        this.withdrawableEarnings = withdrawableEarnings;
    }

    public String getFrozenEarnings() {
        return frozenEarnings;
    }

    public void setFrozenEarnings(String frozenEarnings) {
        this.frozenEarnings = frozenEarnings;
    }
}
