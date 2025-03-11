package com.volcengine.tos.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.hc.client5.http.DnsResolver;

import com.volcengine.tos.internal.util.dnscache.DnsCacheService;

public class RequestDnsResolver implements DnsResolver {

    private final DnsResolver dnsResolver;
    private DnsCacheService dnsCacheService = null;

    public RequestDnsResolver(DnsResolver dnsResolver) {
        this.dnsResolver = dnsResolver;
    }

    public RequestDnsResolver setDnsCacheService(DnsCacheService dnsCacheService) {
        this.dnsCacheService = dnsCacheService;
        return this;
    }

    @Override
    public InetAddress[] resolve(String host) throws UnknownHostException {
        long start = System.currentTimeMillis();
        InetAddress[] inetAddresses = null;
        if (this.dnsCacheService != null) {
            java.util.List<InetAddress> ipList = this.dnsCacheService.getIpList(host);
            if (ipList != null) {
                inetAddresses = ipList.toArray(new InetAddress[ipList.size()]);
            }
        } else {
            inetAddresses = this.dnsResolver.resolve(host);
        }
        long end = System.currentTimeMillis();
        RequestDnsTimeHolder.setTime(start, end);
        return inetAddresses;
    }

    @Override
    public String resolveCanonicalHostname(String host) throws UnknownHostException {
        return this.dnsResolver.resolveCanonicalHostname(host);
    }

}
