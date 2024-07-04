package com.volcengine.tos.auth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class FederationCredentials implements Credentials{
    private FederationToken cachedToken;
    private AtomicInteger refreshing = new AtomicInteger(0);
    private Duration preFetch;
    private FederationTokenProvider tokenProvider;

    public FederationCredentials(FederationTokenProvider tokenProvider) {
        this.cachedToken = tokenProvider.federationToken();
        this.tokenProvider = tokenProvider;
        this.preFetch = Duration.ofMinutes(5);
    }

    public FederationCredentials withPreFetch(Duration preFetch){
        this.preFetch = preFetch;
        return this;
    }

    public FederationToken token(){
        return cachedToken;
    }

    @Override
    public Credential credential() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(token().getExpiration())){
            // 已经过期
            synchronized (this){
                cachedToken = tokenProvider.federationToken();
            }
        } else if (Duration.between(now, token().getExpiration()).toNanos() < preFetch.toNanos() && refreshing.get() == 0) {
            // 将要过期, prefetch token
            // CAS 更新
            if (refreshing.compareAndSet(0, 1)) {
                try{
                    cachedToken = tokenProvider.federationToken();
                } finally{
                    refreshing.set(0);
                }
            }
        }
        return token().getCredential();
    }
}
