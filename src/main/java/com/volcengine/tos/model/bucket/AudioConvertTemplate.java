package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AudioConvertTemplate {
    @JsonProperty("Tag")
    private String tag;
    
    @JsonProperty("ID")
    private String id;
    
    @JsonProperty("Name")
    private String name;
    
    @JsonProperty("AudioConvertConfig")
    private AudioConvertConfig audioConvertConfig;

    public String getTag() {
        return tag;
    }

    public AudioConvertTemplate setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getId() {
        return id;
    }

    public AudioConvertTemplate setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AudioConvertTemplate setName(String name) {
        this.name = name;
        return this;
    }

    public AudioConvertConfig getAudioConvertConfig() {
        return audioConvertConfig;
    }

    public AudioConvertTemplate setAudioConvertConfig(AudioConvertConfig audioConvertConfig) {
        this.audioConvertConfig = audioConvertConfig;
        return this;
    }

    @Override
    public String toString() {
        return "AudioConvertTemplate{" +
                "tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", audioConvertConfig=" + audioConvertConfig +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tag;
        private String id;
        private String name;
        private AudioConvertConfig audioConvertConfig;

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder audioConvertConfig(AudioConvertConfig audioConvertConfig) {
            this.audioConvertConfig = audioConvertConfig;
            return this;
        }

        public AudioConvertTemplate build() {
            AudioConvertTemplate template = new AudioConvertTemplate();
            template.setTag(tag);
            template.setId(id);
            template.setName(name);
            template.setAudioConvertConfig(audioConvertConfig);
            return template;
        }
    }
}