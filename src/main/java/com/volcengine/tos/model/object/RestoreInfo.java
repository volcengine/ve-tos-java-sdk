package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.common.TierType;

import java.util.Date;

public class RestoreInfo {
    private RestoreStatus restoreStatus;

    private RestoreParam restoreParam;

    public RestoreStatus getRestoreStatus() {
        return restoreStatus;
    }

    protected void setRestoreStatus(RestoreStatus restoreStatus) {
        this.restoreStatus = restoreStatus;
    }

    public RestoreParam getRestoreParam() {
        return restoreParam;
    }

    protected void setRestoreParam(RestoreParam restoreParam) {
        this.restoreParam = restoreParam;
    }

    public static class RestoreStatus {
        private boolean ongoingRequest;

        private Date expiryDate;

        public Date getExpiryDate() {
            return expiryDate;
        }

        protected void setExpiryDate(Date expiryDate) {
            this.expiryDate = expiryDate;
        }

        public boolean isOngoingRequest() {
            return ongoingRequest;
        }

        protected void setOngoingRequest(boolean ongoingRequest) {
            this.ongoingRequest = ongoingRequest;
        }
    }

    public static class RestoreParam {
        private Date requestDate;

        private int expiryDays;

        private TierType tier;

        public Date getRequestDate() {
            return requestDate;
        }

        protected void setRequestDate(Date requestDate) {
            this.requestDate = requestDate;
        }

        public int getExpiryDays() {
            return expiryDays;
        }

        protected void setExpiryDays(int expiryDays) {
            this.expiryDays = expiryDays;
        }

        public TierType getTier() {
            return tier;
        }

        protected void setTier(TierType tier) {
            this.tier = tier;
        }
    }
}
