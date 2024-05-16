package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.RedirectType;

import java.util.List;

public class Redirect {
    @JsonProperty("RedirectType")
    private RedirectType redirectType;
    @JsonProperty("FetchSourceOnRedirect")
    private boolean fetchSourceOnRedirect;
    @JsonProperty("PassQuery")
    private boolean passQuery;
    @JsonProperty("FollowRedirect")
    private boolean followRedirect;
    @JsonProperty("MirrorHeader")
    private MirrorHeader mirrorHeader;
    @JsonProperty("PublicSource")
    private PublicSource publicSource;
    @JsonProperty("Transform")
    private Transform transform;
    @JsonProperty("FetchHeaderToMetaDataRules")
    private List<FetchHeaderToMetaDataRules> fetchHeaderToMetaDataRules;

    public RedirectType getRedirectType() {
        return redirectType;
    }

    public Redirect setRedirectType(RedirectType redirectType) {
        this.redirectType = redirectType;
        return this;
    }

    public boolean isFetchSourceOnRedirect() {
        return fetchSourceOnRedirect;
    }

    public Redirect setFetchSourceOnRedirect(boolean fetchSourceOnRedirect) {
        this.fetchSourceOnRedirect = fetchSourceOnRedirect;
        return this;
    }

    public boolean isPassQuery() {
        return passQuery;
    }

    public Redirect setPassQuery(boolean passQuery) {
        this.passQuery = passQuery;
        return this;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public Redirect setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
        return this;
    }

    public MirrorHeader getMirrorHeader() {
        return mirrorHeader;
    }

    public Redirect setMirrorHeader(MirrorHeader mirrorHeader) {
        this.mirrorHeader = mirrorHeader;
        return this;
    }

    public PublicSource getPublicSource() {
        return publicSource;
    }

    public Redirect setPublicSource(PublicSource publicSource) {
        this.publicSource = publicSource;
        return this;
    }

    public Transform getTransform() {
        return transform;
    }

    public Redirect setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    public List<FetchHeaderToMetaDataRules> getFetchHeaderToMetaDataRules() {
        return fetchHeaderToMetaDataRules;
    }

    public Redirect setFetchHeaderToMetaDataRules(List<FetchHeaderToMetaDataRules> fetchHeaderToMetaDataRules) {
        this.fetchHeaderToMetaDataRules = fetchHeaderToMetaDataRules;
        return this;
    }

    @Override
    public String toString() {
        return "Redirect{" +
                "redirectType=" + redirectType +
                ", fetchSourceOnRedirect=" + fetchSourceOnRedirect +
                ", passQuery=" + passQuery +
                ", followRedirect=" + followRedirect +
                ", mirrorHeader=" + mirrorHeader +
                ", publicSource=" + publicSource +
                ", transform=" + transform +
                '}';
    }

    public static RedirectBuilder builder() {
        return new RedirectBuilder();
    }

    public static final class RedirectBuilder {
        private RedirectType redirectType;
        private boolean fetchSourceOnRedirect;
        private boolean passQuery;
        private boolean followRedirect;
        private MirrorHeader mirrorHeader;
        private PublicSource publicSource;
        private Transform transform;
        private List<FetchHeaderToMetaDataRules> fetchHeaderToMetaDataRules;

        private RedirectBuilder() {
        }

        public RedirectBuilder redirectType(RedirectType redirectType) {
            this.redirectType = redirectType;
            return this;
        }

        public RedirectBuilder fetchSourceOnRedirect(boolean fetchSourceOnRedirect) {
            this.fetchSourceOnRedirect = fetchSourceOnRedirect;
            return this;
        }

        public RedirectBuilder passQuery(boolean passQuery) {
            this.passQuery = passQuery;
            return this;
        }

        public RedirectBuilder followRedirect(boolean followRedirect) {
            this.followRedirect = followRedirect;
            return this;
        }

        public RedirectBuilder mirrorHeader(MirrorHeader mirrorHeader) {
            this.mirrorHeader = mirrorHeader;
            return this;
        }

        public RedirectBuilder publicSource(PublicSource publicSource) {
            this.publicSource = publicSource;
            return this;
        }

        public RedirectBuilder transform(Transform transform) {
            this.transform = transform;
            return this;
        }

        public RedirectBuilder fetchHeaderToMetaDataRules(List<FetchHeaderToMetaDataRules> fetchHeaderToMetaDataRules) {
            this.fetchHeaderToMetaDataRules = fetchHeaderToMetaDataRules;
            return this;
        }

        public Redirect build() {
            Redirect redirect = new Redirect();
            redirect.setRedirectType(redirectType);
            redirect.setFetchSourceOnRedirect(fetchSourceOnRedirect);
            redirect.setPassQuery(passQuery);
            redirect.setFollowRedirect(followRedirect);
            redirect.setMirrorHeader(mirrorHeader);
            redirect.setPublicSource(publicSource);
            redirect.setTransform(transform);
            redirect.setFetchHeaderToMetaDataRules(fetchHeaderToMetaDataRules);
            return redirect;
        }
    }
}
