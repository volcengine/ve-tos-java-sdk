package com.volcengine.tos.internal.util.dnscache;

import java.net.InetAddress;
import java.util.List;

public interface DnsCacheService {
    List<InetAddress> getIpList(String host);
    void removeAddress(String host, String ip);
}
