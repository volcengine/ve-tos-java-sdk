package com.volcengine.tos.internal.util.dnscache;

import com.volcengine.tos.internal.util.StringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DnsCacheServiceImpl implements DnsCacheService, Closeable {
    private final int timeoutMinutes;

    private final Map<String, IpListItem> ipListMap;

    private final ReadWriteLock lock;

    private final Thread refreshThread;

    public DnsCacheServiceImpl(int timeoutMinutes, int refreshInterval) {
        this.timeoutMinutes = timeoutMinutes;
        this.ipListMap = new HashMap<>(16);
        this.lock = new ReentrantReadWriteLock();
        final int finalRefreshInterval = refreshInterval <= 0 ? 30 : refreshInterval;
        this.refreshThread = new Thread() {
            public void run() {

                while (!Thread.interrupted()) {
                    try {
                        Thread.sleep(finalRefreshInterval * 1000);
                        DnsCacheServiceImpl.this.refreshCache();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
        this.refreshThread.start();
    }

    @Override
    public List<InetAddress> getIpList(String host) {
        if (StringUtils.isEmpty(host)) {
            return null;
        }
        List<InetAddress> result;
        this.lock.readLock().lock();
        try {
            result = this.getIpListFromCache(host);
            if (result != null) {
                return result;
            }
        } finally {
            this.lock.readLock().unlock();
        }
        this.lock.writeLock().lock();
        try {
            result = this.getIpListFromCache(host);
            if (result != null) {
                return result;
            }
            IpListItem item = new IpListItem(host, this.timeoutMinutes * 60 * 1e9);
            item.refresh();
            this.ipListMap.put(host, item);
            return item.getIpList();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void removeAddress(String host, String ip) {
        this.lock.readLock().lock();
        try {
            IpListItem item = this.ipListMap.get(host);
            if (item == null) {
                return;
            }
            IpListItemValue value = item.value;
            if (!value.isValid()) {
                return;
            }
            List<InetAddress> ipList = new ArrayList<>(value.ipList.size());
            for (InetAddress addr : value.ipList) {
                if (!StringUtils.equals(addr.getHostAddress(), ip)) {
                    ipList.add(addr);
                }
            }
            value.ipList = ipList;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    List<InetAddress> getIpListFromCache(String host) {
        IpListItem item = this.ipListMap.get(host);
        if (item == null) {
            return null;
        }
        return item.getIpList();
    }

    void refreshCache() {
        this.lock.readLock().lock();
        List<IpListItem> values = new ArrayList<>(this.ipListMap.size());
        try {
            for (IpListItem value : this.ipListMap.values()) {
                values.add(value);
            }
        } finally {
            this.lock.readLock().unlock();
        }

        for (IpListItem value : values) {
            if (System.nanoTime() - value.value.lastUpdateTimeNanos > 1 * 1e9) {
                value.refresh();
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.refreshThread.interrupt();
    }

    static class IpListItemValue {
        List<InetAddress> ipList;
        long lastUpdateTimeNanos;
        boolean immortal;

        double timeout;

        IpListItemValue() {

        }

        IpListItemValue(List<InetAddress> ipList, long lastUpdateTimeNanos, double timeout) {
            this.ipList = ipList;
            this.lastUpdateTimeNanos = lastUpdateTimeNanos;
            this.timeout = timeout;
        }

        boolean isValid() {
            return ipList != null && ipList.size() > 0 && (immortal || System.nanoTime() - lastUpdateTimeNanos < timeout);
        }
    }

    static class IpListItem {
        final String host;
        final double timeout;

        volatile IpListItemValue value;

        IpListItem(String host, double timeout) {
            this.host = host;
            this.timeout = timeout;
            this.value = new IpListItemValue();
        }

        List<InetAddress> getIpList() {
            IpListItemValue value = this.value;
            if (!value.isValid()) {
                return null;
            }
            return value.ipList;
        }

        void refresh() {
            try {
                List<InetAddress> ipList = new ArrayList<>(Arrays.asList(InetAddress.getAllByName(this.host)));
                if (!ipList.isEmpty()) {
                    this.value = new IpListItemValue(ipList, System.nanoTime(), this.timeout);
                } else {
                    this.value.immortal = true;
                }
            } catch (Exception e) {
                this.value.immortal = true;
            }
        }
    }
}
