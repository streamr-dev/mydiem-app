package com.fs.vip.domain;

import java.util.List;

public class EarnFromGrop {


    /**
     * address : 0x8Aa66dfC6DA5DA31c08DEb35Da9D58A5B2f51099
     * earnings : 2109523809523809522
     * active : true
     * recordedEarnings : 2109523809523809522
     * withdrawableEarnings : 2109523809523809522
     * frozenEarnings : 0
     * withdrawableBlockNumber : 9712196
     * proof : ["0xd75f1055cc5a4bf08ddfcfbf3ea404b491f2780a82745866b3fc15fa75cb2f31","0x2f139d5880a0ba7174d6a0a77a4f154a1ce3d6b52498addbdc67df186f5e8f2b","0x8f584fd0ecbc76808e66fabb9cfd17e271401c61a42f3bb63f0f8b9c3777bced","0xc41c7252e5d633dc2dc53575ef2c2a484a0919026738902da0b07f609124db77","0x42884ea8153eae1da4bf8a0de5cd5e2894c9f636157d70296881928737c20904"]
     */

    private String address;
    private String earnings;
    private boolean active;
    private String recordedEarnings;
    private String withdrawableEarnings;
    private String frozenEarnings;
    private String withdrawableBlockNumber;
    private List<String> proof;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public String getWithdrawableBlockNumber() {
        return withdrawableBlockNumber;
    }

    public void setWithdrawableBlockNumber(String withdrawableBlockNumber) {
        this.withdrawableBlockNumber = withdrawableBlockNumber;
    }

    public List<String> getProof() {
        return proof;
    }

    public void setProof(List<String> proof) {
        this.proof = proof;
    }
}
