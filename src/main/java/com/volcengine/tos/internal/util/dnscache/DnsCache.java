package com.volcengine.tos.internal.util.dnscache;

import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Deprecated
class DnsCache {
    private String host;
    private volatile List<InetAddress> ipList;
    private volatile long lastUpdateTimeNanos;
    private AtomicReference<Integer> refreshing;

    DnsCache(String host, int timeoutMinutes) {
        ParamsChecker.ensureNotNull(host, "host");
        if (timeoutMinutes <= 0) {
            return;
        }
        this.host = host;
        this.refreshing = new AtomicReference<>(1);
        addRefreshTask(this::refresh);
    }

    public List<InetAddress> getIpList() {
        if (refreshing.get() == 0) {
            if (ipList != null) {
                return ipList;
            }
            if (refreshing.compareAndSet(0, 1)) {
                addRefreshTask(this::refresh);
            }
        }
        // updating, return older one directly, not block.
        return ipList;
    }

    public long getLastUpdateTimeNanos() {
        return lastUpdateTimeNanos;
    }

    public List<InetAddress> removeIp(String ip) {
        if (refreshing.get() == 1 || ipList == null || StringUtils.isEmpty(ip)) {
            return ipList;
        }
        if (refreshing.compareAndSet(0, 1)) {
            for (InetAddress addr : ipList) {
                if (StringUtils.equals(addr.getHostAddress(), ip)) {
                    ipList.remove(addr);
                    break;
                }
            }
            refreshing.set(0);
        }
        return ipList;
    }

    void addRefreshTask(Runnable action) {
        DefaultDnsCacheService.getInstance().submit(action);
    }

    void refresh() {
        try {
            ipList = new ArrayList<>(Arrays.asList(InetAddress.getAllByName(this.host)));
            if (ipList.size() == 0) {
                TosUtils.getLogger().debug("tos: host {} look up 0 address.", this.host);
            }
            lastUpdateTimeNanos = System.nanoTime();
        } catch (Exception e) {
            // 0 length means cannot look up any addresses.
            TosUtils.getLogger().debug("tos: host {} look up address failed, exception is {}.", this.host, e.toString());
        } finally {
            refreshing.set(0);
        }
    }
}
