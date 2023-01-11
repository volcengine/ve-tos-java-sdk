package com.volcengine.tos.internal.util.dnscache;

import com.volcengine.tos.internal.util.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DefaultDnsCacheService implements DnsCacheService {
    private static final ExecutorService executor = new ThreadPoolExecutor(1, 3, 1000,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    private static final Logger log = LoggerFactory.getLogger(DefaultDnsCacheService.class);
    private static final String VOLCES_HOST_SUFFIX = "volces.com";
    private static final String HOST_SPLIT_SEP = "\\.";
    private static final String HOST_CONCAT = ".";
    private static final int HOST_SPLIT_LENGTH = 4;

    private final Map<String, DnsCache> ipListMap;
    private final int timeoutMinutes;

    public DefaultDnsCacheService(int timeoutMinutes) {
        if (timeoutMinutes <= 0) {
            throw new IllegalArgumentException("dns timeout must be larger than 0");
        }
        this.ipListMap = new ConcurrentHashMap<>();
        this.timeoutMinutes = timeoutMinutes;
    }

    protected static ExecutorService getInstance() {
        return executor;
    }

    protected static Logger getLog() {
        return log;
    }

    @Override
    public List<InetAddress> getIpList(String host) {
        if (StringUtils.isEmpty(host)) {
            return null;
        }
        String wrappedHost = wrappedHost(host);
        if (ipListMap.containsKey(wrappedHost)) {
            long pastTime = System.nanoTime() - ipListMap.get(wrappedHost).getLastUpdateTimeNanos();
            if (pastTime < timeoutMinutes * 60 * 1e9) {
                // not expired
                return getIpListFromCache(wrappedHost);
            }
        }
        // not exist/older one expired, add a new one
        ipListMap.put(wrappedHost, new DnsCache(wrappedHost, timeoutMinutes));
        return getIpListFromCache(wrappedHost);
    }

    @Nullable
    private List<InetAddress> getIpListFromCache(String wrappedHost) {
        List<InetAddress> res = ipListMap.get(wrappedHost).getIpList();
        if (res != null && res.size() == 0) {
            ipListMap.remove(wrappedHost);
            return null;
        }
        return res;
    }

    @Override
    public void removeAddress(String host, String ip) {
        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(ip)) {
            return;
        }
        String wrappedHost = wrappedHost(host);
        if (ipListMap.containsKey(wrappedHost)) {
            List<InetAddress> removed = ipListMap.get(wrappedHost).removeIp(ip);
            if (removed != null && removed.size() == 0) {
                // no available ip, delete cache
                ipListMap.remove(wrappedHost);
            }
        }
    }

    private String wrappedHost(String host) {
        if (StringUtils.isEmpty(host)) {
            return host;
        }
        if (!host.endsWith(VOLCES_HOST_SUFFIX)) {
            return host;
        }
        String[] hostSplit = host.split(HOST_SPLIT_SEP);
        if (hostSplit.length != HOST_SPLIT_LENGTH) {
            return host;
        }
        List<String> hostWithoutFirstElement = new ArrayList<>(hostSplit.length-1);
        for (int i = 1; i < hostSplit.length; i++) {
            hostWithoutFirstElement.add(hostSplit[i]);
        }
        return StringUtils.join(hostWithoutFirstElement, HOST_CONCAT);
    }
}
