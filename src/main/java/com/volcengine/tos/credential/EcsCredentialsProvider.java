package com.volcengine.tos.credential;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;

public class EcsCredentialsProvider implements CredentialsProvider, Closeable {

    private static final String DEFAULT_META_SERVICE_URL = "http://100.96.0.96/volcstack/latest/iam/security_credentials";
    private static final DateTimeFormatter expireTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    private final Thread refreshThread;
    private final String roleName;
    private final String url;
    private final Lock lock;
    private final CloseableHttpClient client;
    private volatile EcsCredentials ecsCredentials;

    public EcsCredentialsProvider(String roleName) {
        this(roleName, null);
    }

    public EcsCredentialsProvider(String roleName, String url) {
        if (StringUtils.isEmpty(roleName)) {
            throw new TosClientException("ecs role name is empty", null);
        }
        if (StringUtils.isEmpty(url)) {
            this.url = DEFAULT_META_SERVICE_URL;
        } else {
            this.url = url;
        }
        this.roleName = roleName;
        this.lock = new ReentrantLock();
        this.client = TosUtils.defaultApacheHttpClient();
        // 5min interval
        final int finalRefreshInterval = 300;
        this.refreshThread = new Thread() {
            public void run() {

                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(finalRefreshInterval * 1000);
                        EcsCredentials origin = EcsCredentialsProvider.this.ecsCredentials;
                        if (origin == null || System.nanoTime() - origin.lastUpdateTimeNanos > 1 * 1e9) {
                            EcsCredentialsProvider.this.fetchCredentials();
                        }
                    } catch (TosClientException e) {
                        TosUtils.getLogger().warn("try to fetch ecs credentials failed", e);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
        this.refreshThread.setDaemon(true);
        this.refreshThread.start();
    }

    @Override
    public Credentials getCredentials(int expires) {
        EcsCredentials credentials = this.ecsCredentials;
        if (credentials != null && credentials.isValid()) {
            return credentials;
        }
        this.lock.lock();
        try {
            credentials = this.ecsCredentials;
            if (credentials != null && credentials.isValid()) {
                return credentials;
            }
            return this.fetchCredentials();
        } finally {
            this.lock.unlock();
        }
    }

    private Credentials fetchCredentials() {
        EcsCredentials origin = this.ecsCredentials;
        try {
            EcsCredentials current = this.client.execute(new HttpGet(url + "/" + roleName), response -> {
                int statusCode = response.getCode();
                if (statusCode == HttpStatus.OK) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        try (InputStream inputStream = entity.getContent()) {
                            EcsCredentials creds = TosUtils.getJsonMapper().readValue(inputStream, EcsCredentials.class);
                            if (StringUtils.isNotEmpty(creds.ak) && StringUtils.isNotEmpty(creds.sk)) {
                                creds.lastUpdateTimeNanos = System.nanoTime();
                                if (StringUtils.isNotEmpty(creds.expiredTime)) {
                                    ZonedDateTime l = ZonedDateTime.parse(creds.expiredTime, expireTimeFormatter);
                                    creds.expiredDate = Date.from(Instant.from(l));
                                }
                                this.ecsCredentials = creds;
                                return creds;
                            }
                        }
                    }
                    throw new TosClientException("parse ecs token failed", null);
                }
                throw new TosClientException("get ecs token failed, unexpected status code: " + statusCode, null);
            });
            this.ecsCredentials = current;
            return current;
        } catch (Exception e) {
            if (origin != null) {
                origin.immortal = true;
                return origin;
            }

            if (e instanceof TosClientException) {
                throw (TosClientException) e;
            }
            throw new TosClientException("get ecs token failed", e);
        }
    }

    @Override
    public void close() throws IOException {
        this.refreshThread.interrupt();
        this.client.close();
    }

    private static class EcsCredentials implements Credentials {

        @JsonIgnore
        volatile boolean immortal;
        @JsonIgnore
        long lastUpdateTimeNanos;
        @JsonIgnore
        Date expiredDate;
        @JsonProperty("AccessKeyId")
        String ak;
        @JsonProperty("SecretAccessKey")
        String sk;
        @JsonProperty("SessionToken")
        String securityToken;
        @JsonProperty("ExpiredTime")
        String expiredTime;

        boolean isValid() {
            return immortal || expiredDate == null || expiredDate.after(new Date());
        }

        @Override
        public String getAk() {
            return ak;
        }

        @Override
        public String getSk() {
            return sk;
        }

        @Override
        public String getSecurityToken() {
            return securityToken;
        }
    }
}
