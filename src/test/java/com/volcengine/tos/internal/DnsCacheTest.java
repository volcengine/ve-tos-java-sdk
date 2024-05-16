package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.internal.util.dnscache.DefaultDnsCacheService;
import com.volcengine.tos.internal.util.dnscache.DnsCacheService;
import com.volcengine.tos.internal.util.dnscache.DnsCacheServiceImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class DnsCacheTest {
    @Test
    void dnsCacheTest() throws InterruptedException {
        try{
            new DefaultDnsCacheService(0);
        }catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
        DnsCacheService cacheService = new DefaultDnsCacheService(1);
        List<InetAddress> addressList = cacheService.getIpList(null);
        Assert.assertNull(addressList);

        addressList = cacheService.getIpList("noSuchHost");
        Assert.assertNull(addressList);
        Thread.sleep(500);
        addressList = cacheService.getIpList("noSuchHost");
        Assert.assertNull(addressList);

        addressList = cacheService.getIpList("localhost");
        Assert.assertNull(addressList);
        Thread.sleep(5000);
        addressList = cacheService.getIpList("localhost");
        Assert.assertNotNull(addressList);
        Assert.assertTrue(addressList.size() > 0);
        Assert.assertEquals(addressList.get(0).getHostAddress(), "127.0.0.1");

        String endpoint = Consts.endpoint.toLowerCase();
        if (endpoint.startsWith("http://")) {
            endpoint = endpoint.substring("http://".length());
        } else if (endpoint.startsWith("https://")) {
            endpoint = endpoint.substring("https://".length());
        }

        addressList = cacheService.getIpList(endpoint);
        Assert.assertNull(addressList);
        Thread.sleep(500);
        addressList = cacheService.getIpList(endpoint);
        Assert.assertNotNull(addressList);
        Assert.assertTrue(addressList.size() > 0);
        String addr = addressList.get(0).getHostAddress();

        addressList = cacheService.getIpList(Consts.bucket + "." + endpoint);
        Assert.assertNotNull(addressList);
        Assert.assertTrue(addressList.size() > 0);
        Assert.assertEquals(addr, addressList.get(0).getHostAddress());

        cacheService.removeAddress(endpoint, addr);
        addressList = cacheService.getIpList(endpoint);
        Assert.assertNull(addressList);
    }

    @Test
    void dnsCacheServiceImplTest() {
        DnsCacheServiceImpl cacheService = new DnsCacheServiceImpl(1, 30);
        List<InetAddress> ipList = cacheService.getIpList("noSuchHost");
        Assert.assertNull(ipList);
        ipList = cacheService.getIpList("localhost");
        Assert.assertNotNull(ipList);
        Assert.assertTrue(ipList.size() > 0);
        Assert.assertTrue(ipList.get(0).getHostAddress().equals("127.0.0.1") || ipList.get(0).getHostAddress().equals("0:0:0:0:0:0:0:1"));

        String endpoint = Consts.endpoint.toLowerCase();
        if (endpoint.startsWith("http://")) {
            endpoint = endpoint.substring("http://".length());
        } else if (endpoint.startsWith("https://")) {
            endpoint = endpoint.substring("https://".length());
        }
        ipList = cacheService.getIpList(endpoint);
        Assert.assertNotNull(ipList);
        Assert.assertTrue(ipList.size() > 0);
        String addr = ipList.get(0).getHostAddress();

        ipList = cacheService.getIpList(Consts.bucket + "." + endpoint);
        Assert.assertNotNull(ipList);
        Assert.assertTrue(ipList.size() > 0);
        Assert.assertEquals(addr, ipList.get(0).getHostAddress());

        cacheService.removeAddress(endpoint, addr);
        ipList = cacheService.getIpList(endpoint);
        Assert.assertNotNull(ipList);
        Assert.assertTrue(ipList.size() > 0);
        try {
            cacheService.close();
        } catch (IOException e) {
            Assert.assertTrue(false);
        }
        final DnsCacheServiceImpl cacheService2 = new DnsCacheServiceImpl(1, 30);
        List<Thread> l = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    List<InetAddress> ipList = cacheService2.getIpList("localhost");
                    Assert.assertNotNull(ipList);
                    Assert.assertTrue(ipList.size() > 0);
                    Assert.assertTrue(ipList.get(0).getHostAddress().equals("127.0.0.1") || ipList.get(0).getHostAddress().equals("0:0:0:0:0:0:0:1"));
                }
            };
            l.add(t);
        }

        for (Thread t : l) {
            t.start();
        }

        for (Thread t : l) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Assert.assertTrue(false);
            }
        }
    }
}
