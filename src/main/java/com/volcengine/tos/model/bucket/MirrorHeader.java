package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MirrorHeader {
    @JsonProperty("PassAll")
    private boolean passAll;
    @JsonProperty("Pass")
    private List<String> pass;
    @JsonProperty("Remove")
    private List<String> remove;

    public boolean isPassAll() {
        return passAll;
    }

    public MirrorHeader setPassAll(boolean passAll) {
        this.passAll = passAll;
        return this;
    }

    public List<String> getPass() {
        return pass;
    }

    public MirrorHeader setPass(List<String> pass) {
        this.pass = pass;
        return this;
    }

    public List<String> getRemove() {
        return remove;
    }

    public MirrorHeader setRemove(List<String> remove) {
        this.remove = remove;
        return this;
    }

    @Override
    public String toString() {
        return "MirrorHeader{" +
                "passAll=" + passAll +
                ", pass=" + pass +
                ", remove=" + remove +
                '}';
    }

    public static MirrorHeaderBuilder builder() {
        return new MirrorHeaderBuilder();
    }

    public static final class MirrorHeaderBuilder {
        private boolean passAll;
        private List<String> pass;
        private List<String> remove;

        private MirrorHeaderBuilder() {
        }

        public MirrorHeaderBuilder passAll(boolean passAll) {
            this.passAll = passAll;
            return this;
        }

        public MirrorHeaderBuilder pass(List<String> pass) {
            this.pass = pass;
            return this;
        }

        public MirrorHeaderBuilder remove(List<String> remove) {
            this.remove = remove;
            return this;
        }

        public MirrorHeader build() {
            MirrorHeader mirrorHeader = new MirrorHeader();
            mirrorHeader.setPassAll(passAll);
            mirrorHeader.setPass(pass);
            mirrorHeader.setRemove(remove);
            return mirrorHeader;
        }
    }
}
