package com.fs.vip.domain;

public class JoinGroup {
    /**
     * id : ySJQ3SEfS6CYZ96DBK0sIgCXeun5ivTf6ZHB1Y9f82Kg
     * memberAddress : 0x5a0D95A151476fa62257280e406c45dd38b9c428
     * communityAddress : 0x0df55C565881b253D307e9a8a95C907DFA228283
     * state : ACCEPTED
     * dateCreated : 2020-02-12T06:56:20Z
     * lastUpdated : 2020-02-12T06:56:20Z
     */

    private String id;
    private String memberAddress;
    private String communityAddress;
    private String state;
    private String dateCreated;
    private String lastUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getCommunityAddress() {
        return communityAddress;
    }

    public void setCommunityAddress(String communityAddress) {
        this.communityAddress = communityAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
