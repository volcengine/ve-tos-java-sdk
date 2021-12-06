package com.volcengine.tos;

@FunctionalInterface
public interface ClientOptionsBuilder {
    /**
     * options setting for TOSClient
     *
     * @param client the TOSClient
     */
    void clientOption(TOSClient client);
}