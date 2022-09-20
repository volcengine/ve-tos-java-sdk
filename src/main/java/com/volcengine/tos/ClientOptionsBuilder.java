package com.volcengine.tos;

@Deprecated
@FunctionalInterface
public interface ClientOptionsBuilder {
    /**
     * options setting for TOSClient
     *
     * @param client the TOSClient
     */
    void clientOption(TOSClient client);
}