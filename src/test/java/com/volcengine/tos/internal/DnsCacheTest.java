package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.internal.util.dnscache.DefaultDnsCacheService;
import com.volcengine.tos.internal.util.dnscache.DnsCacheService;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.InetAddress;
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
        Thread.sleep(500);
        addressList = cacheService.getIpList("localhost");
        Assert.assertNotNull(addressList);
        Assert.assertTrue(addressList.size() > 0);
        Assert.assertEquals(addressList.get(0).getHostAddress(), "127.0.0.1");

        addressList = cacheService.getIpList(Consts.endpoint);
        Assert.assertNull(addressList);
        Thread.sleep(500);
        addressList = cacheService.getIpList(Consts.endpoint);
        Assert.assertNotNull(addressList);
        Assert.assertTrue(addressList.size() > 0);
        String addr = addressList.get(0).getHostAddress();

        addressList = cacheService.getIpList(Consts.bucket + "." + Consts.endpoint);
        Assert.assertNotNull(addressList);
        Assert.assertTrue(addressList.size() > 0);
        Assert.assertEquals(addr, addressList.get(0).getHostAddress());

        cacheService.removeAddress(Consts.endpoint, addr);
        addressList = cacheService.getIpList(Consts.endpoint);
        Assert.assertNull(addressList);
    }
}
