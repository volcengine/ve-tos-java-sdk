package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.credential.Credentials;
import com.volcengine.tos.credential.CredentialsProvider;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.Closeable;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// TOS SDK 定期异步调用该接口获取 Credentials（例如 10 分钟刷新一次），获取一个长有效期的 Credentials（例如 10 小时有效期） 并在本地缓存，实际请求时直接使用该 Credentials；
// 当异步调用该接口获取 Credentials 失败时（大概率原因是 IAM 服务不可用），继续使用本地缓存的 Credentials（给 TOS 服务端预留降级的机会）；
public class CredentialsProviderWrapper implements CredentialsProvider, Closeable {
    private static final int CREDENTIALS_EXPIRES = 3600 * 10;
    private final CredentialsProvider inner;
    private final Lock lock;
    private final Thread refreshThread;
    private volatile CredentialsWrapper longCredentials;

    public CredentialsProviderWrapper(CredentialsProvider credentialsProvider) {
        if (credentialsProvider == null) {
            throw new TosClientException("CredentialsProvider cannot be null", null);
        }
        this.inner = credentialsProvider;
        this.lock = new ReentrantLock();
        // 600s interval
        final int finalRefreshInterval = 600;
        this.refreshThread = new Thread() {
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(finalRefreshInterval * 1000);
                        CredentialsProviderWrapper.this.lock.lock();
                        try {
                            CredentialsProviderWrapper.this.refreshCredentials();
                        } finally {
                            CredentialsProviderWrapper.this.lock.unlock();
                        }
                    } catch (TosClientException e) {
                        TosUtils.getLogger().warn("try to refreshCredentials failed", e);
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
        CredentialsWrapper cw = this.longCredentials;
        if (cw != null && cw.isValid()) {
            return cw.credentials;
        }

        this.lock.lock();
        try {
            cw = this.longCredentials;
            if (cw != null && cw.isValid()) {
                return cw.credentials;
            }
            this.refreshCredentials();
            return this.longCredentials.credentials;
        } catch (TosClientException e) {
            // 获取 Credentials 失败时（大概率原因是 IAM 服务不可用），继续使用本地缓存的 Credentials
            cw = this.longCredentials;
            if (cw != null && cw.isValid()) {
                return cw.credentials;
            }
            throw e;
        } finally {
            this.lock.unlock();
        }
    }

    private void refreshCredentials() {
        try {
            CredentialsWrapper cw = new CredentialsWrapper();
            // 10 hour
            int expires = CREDENTIALS_EXPIRES;
            cw.credentials = this.inner.getCredentials(expires);
            cw.expiredDate = Date.from(Instant.now().plusSeconds(expires - 600));
            this.longCredentials = cw;
        } catch (Exception e) {
            CredentialsWrapper cw = this.longCredentials;
            if (cw != null) {
                cw.immortal = true;
            }
            if (e instanceof TosClientException) {
                throw (TosClientException) e;
            }
            throw new TosClientException("get credentials failed", e);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.refreshThread != null) {
            this.refreshThread.interrupt();
        }
    }


    private static class CredentialsWrapper {
        Credentials credentials;
        Date expiredDate;
        volatile boolean immortal;

        boolean isValid() {
            return this.immortal || expiredDate.after(new Date());
        }
    }
}
