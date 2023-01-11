package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.CertStatusType;

public class CustomDomainRule {
    @JsonProperty("CertId")
    private String certID;
    @JsonProperty("Cname")
    private String cname;
    @JsonProperty("CertStatus")
    private CertStatusType certStatus;
    @JsonProperty("Domain")
    private String domain;
    @JsonProperty("Forbidden")
    private boolean forbidden;
    @JsonProperty("ForbiddenReason")
    private String forbiddenReason;

    public String getCertID() {
        return certID;
    }

    public CustomDomainRule setCertID(String certID) {
        this.certID = certID;
        return this;
    }

    public String getCname() {
        return cname;
    }

    public CustomDomainRule setCname(String cname) {
        this.cname = cname;
        return this;
    }

    public CertStatusType getCertStatus() {
        return certStatus;
    }

    public CustomDomainRule setCertStatus(CertStatusType certStatus) {
        this.certStatus = certStatus;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public CustomDomainRule setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public boolean isForbidden() {
        return forbidden;
    }

    public CustomDomainRule setForbidden(boolean forbidden) {
        this.forbidden = forbidden;
        return this;
    }

    public String getForbiddenReason() {
        return forbiddenReason;
    }

    public CustomDomainRule setForbiddenReason(String forbiddenReason) {
        this.forbiddenReason = forbiddenReason;
        return this;
    }

    @Override
    public String toString() {
        return "CustomDomainRule{" +
                "certID='" + certID + '\'' +
                ", cname='" + cname + '\'' +
                ", certStatus=" + certStatus +
                ", domain='" + domain + '\'' +
                ", forbidden=" + forbidden +
                ", forbiddenReason='" + forbiddenReason + '\'' +
                '}';
    }

    public static CustomDomainRuleBuilder builder() {
        return new CustomDomainRuleBuilder();
    }

    public static final class CustomDomainRuleBuilder {
        private String certID;
        private CertStatusType certStatus;
        private String cname;
        private String domain;
        private boolean forbidden;
        private String forbiddenReason;

        private CustomDomainRuleBuilder() {
        }

        public CustomDomainRuleBuilder certID(String certID) {
            this.certID = certID;
            return this;
        }

        public CustomDomainRuleBuilder certStatus(CertStatusType certStatus) {
            this.certStatus = certStatus;
            return this;
        }

        public CustomDomainRuleBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public CustomDomainRuleBuilder forbidden(boolean forbidden) {
            this.forbidden = forbidden;
            return this;
        }

        public CustomDomainRuleBuilder forbiddenReason(String forbiddenReason) {
            this.forbiddenReason = forbiddenReason;
            return this;
        }

        public CustomDomainRuleBuilder cname(String cname) {
            this.cname = cname;
            return this;
        }

        public CustomDomainRule build() {
            CustomDomainRule customDomainRule = new CustomDomainRule();
            customDomainRule.setCertID(certID);
            customDomainRule.setCertStatus(certStatus);
            customDomainRule.setCname(cname);
            customDomainRule.setDomain(domain);
            customDomainRule.setForbidden(forbidden);
            customDomainRule.setForbiddenReason(forbiddenReason);
            return customDomainRule;
        }
    }
}
