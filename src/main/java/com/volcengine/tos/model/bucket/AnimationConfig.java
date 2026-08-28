package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnimationConfig {
    @JsonProperty("TimeInterval")
    private TimeInterval timeInterval;

    @JsonProperty("Container")
    private Container container;

    @JsonProperty("Animation")
    private Animation animation;

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public AnimationConfig setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
        return this;
    }

    public Container getContainer() {
        return container;
    }

    public AnimationConfig setContainer(Container container) {
        this.container = container;
        return this;
    }

    public Animation getAnimation() {
        return animation;
    }

    public AnimationConfig setAnimation(Animation animation) {
        this.animation = animation;
        return this;
    }

    public static AnimationConfigBuilder builder() {
        return new AnimationConfigBuilder();
    }

    public static class AnimationConfigBuilder {
        private TimeInterval timeInterval;
        private Container container;
        private Animation animation;

        public AnimationConfigBuilder timeInterval(TimeInterval timeInterval) {
            this.timeInterval = timeInterval;
            return this;
        }

        public AnimationConfigBuilder container(Container container) {
            this.container = container;
            return this;
        }

        public AnimationConfigBuilder animation(Animation animation) {
            this.animation = animation;
            return this;
        }

        public AnimationConfig build() {
            AnimationConfig animationConfig = new AnimationConfig();
            animationConfig.setTimeInterval(timeInterval);
            animationConfig.setContainer(container);
            animationConfig.setAnimation(animation);
            return animationConfig;
        }
    }
}
