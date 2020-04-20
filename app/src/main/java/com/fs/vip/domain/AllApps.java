package com.fs.vip.domain;

public class AllApps {

    /**
     * memberCount : {"total":14,"active":14,"inactive":0}
     * totalEarnings : 21120000000000000000
     * latestBlock : {"blockNumber":9593276,"timestamp":0,"memberCount":12,"totalEarnings":"21120000000000000000"}
     * latestWithdrawableBlock : {"blockNumber":9593276,"timestamp":0,"memberCount":12,"totalEarnings":"21120000000000000000"}
     * joinPartStreamId : ezdObsEZS-CwGnOLFZYV4A
     */

    private MemberCountBean memberCount;
    private String totalEarnings;
    private LatestBlockBean latestBlock;
    private LatestWithdrawableBlockBean latestWithdrawableBlock;
    private String joinPartStreamId;

    public MemberCountBean getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(MemberCountBean memberCount) {
        this.memberCount = memberCount;
    }

    public String getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(String totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public LatestBlockBean getLatestBlock() {
        return latestBlock;
    }

    public void setLatestBlock(LatestBlockBean latestBlock) {
        this.latestBlock = latestBlock;
    }

    public LatestWithdrawableBlockBean getLatestWithdrawableBlock() {
        return latestWithdrawableBlock;
    }

    public void setLatestWithdrawableBlock(LatestWithdrawableBlockBean latestWithdrawableBlock) {
        this.latestWithdrawableBlock = latestWithdrawableBlock;
    }

    public String getJoinPartStreamId() {
        return joinPartStreamId;
    }

    public void setJoinPartStreamId(String joinPartStreamId) {
        this.joinPartStreamId = joinPartStreamId;
    }

    public static class MemberCountBean {
        /**
         * total : 14
         * active : 14
         * inactive : 0
         */

        private int total;
        private int active;
        private int inactive;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public int getInactive() {
            return inactive;
        }

        public void setInactive(int inactive) {
            this.inactive = inactive;
        }
    }

    public static class LatestBlockBean {
        /**
         * blockNumber : 9593276
         * timestamp : 0
         * memberCount : 12
         * totalEarnings : 21120000000000000000
         */

        private int blockNumber;
        private int timestamp;
        private int memberCount;
        private String totalEarnings;

        public int getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(int blockNumber) {
            this.blockNumber = blockNumber;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public String getTotalEarnings() {
            return totalEarnings;
        }

        public void setTotalEarnings(String totalEarnings) {
            this.totalEarnings = totalEarnings;
        }
    }

    public static class LatestWithdrawableBlockBean {
        /**
         * blockNumber : 9593276
         * timestamp : 0
         * memberCount : 12
         * totalEarnings : 21120000000000000000
         */

        private int blockNumber;
        private int timestamp;
        private int memberCount;
        private String totalEarnings;

        public int getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(int blockNumber) {
            this.blockNumber = blockNumber;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public String getTotalEarnings() {
            return totalEarnings;
        }

        public void setTotalEarnings(String totalEarnings) {
            this.totalEarnings = totalEarnings;
        }
    }
}
