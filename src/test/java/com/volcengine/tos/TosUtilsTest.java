package com.volcengine.tos;

import org.testng.annotations.Test;

import org.testng.Assert;

public class TosUtilsTest {
    @Test
    void IsValidBucketNameTest(){
        try{
            TOSClient.isValidBucketName("-x-");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e);
        }
        try{
            TOSClient.isValidBucketName("x");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e);
        }
        try{
            TOSClient.isValidBucketName("xx\uD83D\uDE0Axx");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e);
        }
        StringBuilder name = new StringBuilder(100);
        for (int i = 0; i < 100; i++) {
            name.append("a");
        }
        try{
            TOSClient.isValidBucketName(name.toString());
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e);
        }
        try{
            TOSClient.isValidBucketName("abc123");
        } catch (IllegalArgumentException e) {
            Assert.fail();
        }
    }
}
