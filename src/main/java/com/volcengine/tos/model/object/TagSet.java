package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.bucket.Tag;

import java.util.List;

public class TagSet {
    @JsonProperty("Tags")
    private List<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public TagSet setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public String toString() {
        return "TagSet{" +
                "tags=" + tags +
                '}';
    }

    public static TagSetBuilder builder() {
        return new TagSetBuilder();
    }

    public static final class TagSetBuilder {
        private List<Tag> tags;

        private TagSetBuilder() {
        }

        public TagSetBuilder tags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public TagSet build() {
            TagSet tagSet = new TagSet();
            tagSet.setTags(tags);
            return tagSet;
        }
    }
}
