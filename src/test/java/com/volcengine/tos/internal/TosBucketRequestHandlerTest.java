package com.volcengine.tos.internal;

import com.volcengine.tos.Consts;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.common.*;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.GranteeV2;
import com.volcengine.tos.model.acl.Owner;
import com.volcengine.tos.model.bucket.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class TosBucketRequestHandlerTest {

    private TosBucketRequestHandler getHandler() {
        return ClientInstance.getBucketRequestHandlerInstance();
    }

    @Test
    void bucketCreateTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try{
            CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(bucketName).build();
            CreateBucketV2Output output = getHandler().createBucket(input);
            Assert.assertEquals(output.getLocation(), "/"+bucketName);
        } catch (Exception e) {
            testFailed(e);
        } finally{
            try{
                getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
            } catch (TosException e) {
                // ignore
            }
        }
    }

    @Test
    void bucketCreateWithAllParamsTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try{
            CreateBucketV2Input input = CreateBucketV2Input.builder()
                    .bucket(bucketName)
                    .acl(ACLType.ACL_BUCKET_OWNER_READ)
                    .storageClass(StorageClassType.STORAGE_CLASS_IA)
                    .build();
            CreateBucketV2Output output = getHandler().createBucket(input);
            Assert.assertEquals(output.getLocation(), "/"+bucketName);
            Thread.sleep(5 * 1000);
            HeadBucketV2Input headInput = HeadBucketV2Input.builder().bucket(bucketName).build();
            HeadBucketV2Output headOutput = getHandler().headBucket(headInput);
            Assert.assertEquals(headOutput.getRegion(), Consts.region);
            Assert.assertEquals(headOutput.getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
        } catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
        }
    }

    @Test
    void bucketHeadTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try{
            CreateBucketV2Input createInput = CreateBucketV2Input.builder().bucket(bucketName).build();
            getHandler().createBucket(createInput);
            Thread.sleep(5 * 1000);
            HeadBucketV2Input headInput = HeadBucketV2Input.builder().bucket(bucketName).build();
            HeadBucketV2Output headOutput = getHandler().headBucket(headInput);
            Assert.assertEquals(headOutput.getRegion(), Consts.region);
            Assert.assertEquals(headOutput.getStorageClass(), StorageClassType.STORAGE_CLASS_STANDARD);
        } catch (Exception e) {
            testFailed(e);
        } finally{
            getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
        }
    }

    @Test
    void bucketDeleteTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try{
            CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(bucketName).build();
            getHandler().createBucket(input);
            Thread.sleep(5 * 1000);
            DeleteBucketOutput output = getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
            Assert.assertEquals(output.getRequestInfo().getStatusCode(), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            testFailed(e);
        }
        try{
            getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            Assert.assertEquals(e.getCode(), Code.NO_SUCH_BUCKET);
        }
    }

    @Test
    void bucketListTest() {
        String bucketName = Consts.internalBucketCrudPrefix + System.currentTimeMillis();
        try {
            ListBucketsV2Output output = getHandler().listBuckets(new ListBucketsV2Input());
            int bucketsNum = output.getBuckets().size();
            CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(bucketName).build();
            getHandler().createBucket(input);
            Thread.sleep(5 * 1000);
            output = getHandler().listBuckets(new ListBucketsV2Input());
            Assert.assertEquals(output.getBuckets().size(), bucketsNum + 1);
            getHandler().deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
            output = getHandler().listBuckets(new ListBucketsV2Input());
            Assert.assertEquals(output.getBuckets().size(), bucketsNum);
        } catch (Exception e) {
            testFailed(e);
        }
    }

    @Test
    void bucketCORSTest() {
        try{
            getHandler().deleteBucketCORS(new DeleteBucketCORSInput().setBucket(Consts.bucket));
        } catch (TosException e) {
            // ignore
        } catch (Exception e) {
            testFailed(e);
        }

        try{
            getHandler().getBucketCORS(new GetBucketCORSInput().setBucket(Consts.bucket));
            Assert.fail();
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        List<CORSRule> rules = new ArrayList<>(2);
        CORSRule rule1 = new CORSRule()
                .setAllowedOrigins(Collections.singletonList("*"))
                .setAllowedMethods(Arrays.asList(HttpMethod.GET, HttpMethod.DELETE, HttpMethod.PUT))
                .setAllowedHeaders(Collections.singletonList("Authorization"))
                .setExposeHeaders(Arrays.asList("X-TOS-HEADER-1", "X-TOS-HEADER-2"))
                .setMaxAgeSeconds(3600);
        CORSRule rule2 = new CORSRule()
                .setAllowedOrigins(Collections.singletonList("https://www.volcengine.com"))
                .setAllowedMethods(Arrays.asList(HttpMethod.PUT, HttpMethod.POST))
                .setAllowedHeaders(Collections.singletonList("Authorization"))
                .setExposeHeaders(Arrays.asList("X-TOS-HEADER-1", "X-TOS-HEADER-2"))
                .setMaxAgeSeconds(3600);
        rules.add(rule1);
        rules.add(rule2);
        try{
            // put
            getHandler().putBucketCORS(new PutBucketCORSInput().setBucket(Consts.bucket).setRules(rules));
            // get
            GetBucketCORSOutput got = getHandler().getBucketCORS(new GetBucketCORSInput().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getRules());
            Assert.assertEquals(got.getRules().size(), 2);
            Assert.assertEquals(got.getRules().get(0).getMaxAgeSeconds(), 3600);
            Assert.assertEquals(got.getRules().get(0).getAllowedHeaders().size(), 1);
            Assert.assertEquals(got.getRules().get(0).getExposeHeaders().size(), 2);
            Assert.assertEquals(got.getRules().get(0).getAllowedOrigins().size(), 1);

            // update
            rules.remove(1);
            getHandler().putBucketCORS(new PutBucketCORSInput().setBucket(Consts.bucket).setRules(rules));
            // get
            got = getHandler().getBucketCORS(new GetBucketCORSInput().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getRules());
            Assert.assertEquals(got.getRules().size(), 1);
            Assert.assertEquals(got.getRules().get(0).getMaxAgeSeconds(), 3600);
            Assert.assertEquals(got.getRules().get(0).getAllowedHeaders().size(), 1);
            Assert.assertEquals(got.getRules().get(0).getExposeHeaders().size(), 2);
            Assert.assertEquals(got.getRules().get(0).getAllowedOrigins().size(), 1);
            Assert.assertEquals(got.getRules().get(0).getAllowedMethods().size(), 3);
        } catch (TosException e) {
            testFailed(e);
        } finally {
            try{
                getHandler().deleteBucketCORS(new DeleteBucketCORSInput().setBucket(Consts.bucket));
            } catch (Exception e) {
                testFailed(e);
            }
            try{
                getHandler().getBucketCORS(new GetBucketCORSInput().setBucket(Consts.bucket));
                Assert.fail();
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Test
    void bucketLifecycleTest() {
        try{
            getHandler().deleteBucketLifecycle(new DeleteBucketLifecycleInput().setBucket(Consts.bucket));
        } catch (TosException e) {
            // ignore
        } catch (Exception e) {
            testFailed(e);
        }

        try{
            getHandler().getBucketLifecycle(new GetBucketLifecycleInput().setBucket(Consts.bucket));
            Assert.fail();
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        List<LifecycleRule> rules = new ArrayList<>(1);
        Date transitionDate = Date.from(LocalDateTime.of(2022, 12, 31, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        Transition transition = new Transition().setDate(transitionDate).setStorageClass(StorageClassType.STORAGE_CLASS_IA);
        Date expirationDate = Date.from(LocalDateTime.of(2023, 12, 31, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        NoncurrentVersionTransition noncurrentVersionTransition = new NoncurrentVersionTransition().setNoncurrentDays(30)
                .setStorageClass(StorageClassType.STORAGE_CLASS_IA);
        Tag tag = new Tag().setKey("1").setValue("22");
        LifecycleRule rule = new LifecycleRule()
                .setId("1")
                .setPrefix("test")
                .setStatus(StatusType.STATUS_ENABLED)
                .setTransitions(Collections.singletonList(transition))
                .setExpiration(new Expiration().setDate(expirationDate))
                .setNoncurrentVersionTransitions(Collections.singletonList(noncurrentVersionTransition))
                .setNoncurrentVersionExpiration(new NoncurrentVersionExpiration().setNoncurrentDays(70))
                .setTags(Collections.singletonList(tag));
        rules.add(rule);
        try{
            // put
            getHandler().putBucketLifecycle(new PutBucketLifecycleInput().setBucket(Consts.bucket).setRules(rules));
            // get
            GetBucketLifecycleOutput got = getHandler().getBucketLifecycle(new GetBucketLifecycleInput().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getRules());
            Assert.assertEquals(got.getRules().size(), 1);
            Assert.assertEquals(got.getRules().get(0).getId(), "1");
            Assert.assertEquals(got.getRules().get(0).getPrefix(), "test");
            Assert.assertEquals(got.getRules().get(0).getStatus(), StatusType.STATUS_ENABLED);
            Assert.assertNotNull(got.getRules().get(0).getTransitions());
            Assert.assertEquals(got.getRules().get(0).getTransitions().size(), 1);
            Assert.assertEquals(got.getRules().get(0).getTransitions().get(0).getDate().toString(), transitionDate.toString());
            Assert.assertEquals(got.getRules().get(0).getTransitions().get(0).getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
            Assert.assertEquals(got.getRules().get(0).getTransitions().get(0).getDays(), 0);
            Assert.assertNotNull(got.getRules().get(0).getExpiration());
            Assert.assertEquals(got.getRules().get(0).getExpiration().getDate().toString(), expirationDate.toString());
            Assert.assertNotNull(got.getRules().get(0).getNoncurrentVersionTransitions());
            Assert.assertEquals(got.getRules().get(0).getNoncurrentVersionTransitions().size(), 1);
            Assert.assertEquals(got.getRules().get(0).getNoncurrentVersionTransitions().get(0).getNoncurrentDays(), 30);
            Assert.assertEquals(got.getRules().get(0).getNoncurrentVersionTransitions().get(0).getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
            Assert.assertNotNull(got.getRules().get(0).getNoncurrentVersionExpiration());
            Assert.assertEquals(got.getRules().get(0).getNoncurrentVersionExpiration().getNoncurrentDays(), 70);
            Assert.assertNotNull(got.getRules().get(0).getTags());
            Assert.assertEquals(got.getRules().get(0).getTags().size(), 1);
            Assert.assertEquals(got.getRules().get(0).getTags().get(0).getKey(), "1");
            Assert.assertEquals(got.getRules().get(0).getTags().get(0).getValue(), "22");
            Assert.assertNull(got.getRules().get(0).getAbortInCompleteMultipartUpload());

        } catch (TosException e) {
            testFailed(e);
        } finally {
            try{
                getHandler().deleteBucketLifecycle(new DeleteBucketLifecycleInput().setBucket(Consts.bucket));
            } catch (Exception e) {
                testFailed(e);
            }
            try{
                getHandler().getBucketLifecycle(new GetBucketLifecycleInput().setBucket(Consts.bucket));
                Assert.fail();
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Test
    void bucketMirrorBackTest() {
        try{
            getHandler().deleteBucketMirrorBack(new DeleteBucketMirrorBackInput().setBucket(Consts.bucket));
        } catch (TosException e) {
            // ignore
        } catch (Exception e) {
            testFailed(e);
        }

        try{
            getHandler().getBucketMirrorBack(new GetBucketMirrorBackInput().setBucket(Consts.bucket));
            Assert.fail();
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        Condition condition = new Condition().setHttpCode(HttpStatus.NOT_FOUND);
        MirrorHeader mirrorHeader = new MirrorHeader().setPassAll(true)
                .setPass(Arrays.asList("aa", "bb")).setRemove(Collections.singletonList("xx"));
        SourceEndpoint sourceEndpoint = new SourceEndpoint()
                .setPrimary(Collections.singletonList("http://www.volcengine.com/obj/tostest/"))
                .setFollower(Collections.singletonList("http://www.volcengine.com/obj/tostest/1"));
        PublicSource publicSource = new PublicSource().setSourceEndpoint(sourceEndpoint);
        Redirect redirect = new Redirect()
                .setRedirectType(RedirectType.REDIRECT_MIRROR)
                .setFetchSourceOnRedirect(true)
                .setPassQuery(true)
                .setFollowRedirect(true)
                .setMirrorHeader(mirrorHeader)
                .setPublicSource(publicSource);
        MirrorBackRule rule = new MirrorBackRule().setId("1").setCondition(condition).setRedirect(redirect);
        List<MirrorBackRule> rules = Collections.singletonList(rule);
        try{
            // put
            getHandler().putBucketMirrorBack(new PutBucketMirrorBackInput().setBucket(Consts.bucket).setRules(rules));
            // get
            GetBucketMirrorBackOutput got = getHandler().getBucketMirrorBack(new GetBucketMirrorBackInput().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getRules());
            Assert.assertEquals(got.getRules().size(), 1);
            MirrorBackRule gotRule = got.getRules().get(0);
            Assert.assertEquals(gotRule.getId(), "1");
            Assert.assertEquals(gotRule.getCondition().getHttpCode(), HttpStatus.NOT_FOUND);
            Assert.assertEquals(gotRule.getRedirect().toString(), redirect.toString());
        } catch (TosException e) {
            testFailed(e);
        } finally {
            try{
                getHandler().deleteBucketMirrorBack(new DeleteBucketMirrorBackInput().setBucket(Consts.bucket));
            } catch (Exception e) {
                testFailed(e);
            }
            try{
                getHandler().getBucketMirrorBack(new GetBucketMirrorBackInput().setBucket(Consts.bucket));
                Assert.fail();
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Test
    void bucketACLTest() {
        GetBucketACLOutput aclOutput = getHandler().getBucketACL(new GetBucketACLInput().setBucket(Consts.bucket));
        String accountId = aclOutput.getOwner().getId();
        String displayName = "displayName";
        String grantAccountId = "123";

        List<GrantV2> grantV2s = new ArrayList<>(5);
        GrantV2 grant1 = new GrantV2().setGrantee(new GranteeV2().setId(accountId).setType(GranteeType.GRANTEE_USER))
                .setPermission(PermissionType.PERMISSION_FULL_CONTROL);
        grantV2s.add(grant1);
        GrantV2 grant2 = new GrantV2().setGrantee(new GranteeV2().setId(grantAccountId).setDisplayName(displayName)
                .setType(GranteeType.GRANTEE_USER)).setPermission(PermissionType.PERMISSION_WRITE_ACP);
        grantV2s.add(grant2);
        GrantV2 grant3 = new GrantV2().setGrantee(new GranteeV2().setId(grantAccountId).setType(GranteeType.GRANTEE_USER))
                .setPermission(PermissionType.PERMISSION_READ_ACP);
        grantV2s.add(grant3);
        GrantV2 grant4 = new GrantV2().setGrantee(new GranteeV2().setType(GranteeType.GRANTEE_GROUP).setCanned(CannedType.CANNED_ALL_USERS))
                .setPermission(PermissionType.PERMISSION_WRITE);
        grantV2s.add(grant4);
        GrantV2 grant5 = new GrantV2().setGrantee(new GranteeV2().setType(GranteeType.GRANTEE_GROUP).setCanned(CannedType.CANNED_AUTHENTICATED_USERS))
                .setPermission(PermissionType.PERMISSION_READ);
        grantV2s.add(grant5);

        try{
            // put
            getHandler().putBucketACL(new PutBucketACLInput().setBucket(Consts.bucket).setGrants(grantV2s).setOwner(new Owner().setId(accountId)));
            // get
            GetBucketACLOutput got = getHandler().getBucketACL(new GetBucketACLInput().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getOwner());
            Assert.assertNotNull(got.getGrants());
            Assert.assertEquals(got.getGrants().size(), 5);
            boolean matched = false;
            for (GrantV2 grantV2 : got.getGrants()) {
                for (GrantV2 g : grantV2s) {
                    if (grantV2.getPermission() == g.getPermission()) {
                        matched = true;
                        Assert.assertEquals(grantV2.getGrantee().toString(), g.getGrantee().toString());
                    }
                }
            }
            Assert.assertTrue(matched);
        } catch (TosException e) {
            testFailed(e);
        }
    }

    @Test
    void bucketStorageClassTest() {
        try{
            // put
            getHandler().putBucketStorageClass(new PutBucketStorageClassInput().setBucket(Consts.bucket)
                    .setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD));
            // get
            HeadBucketV2Output got = getHandler().headBucket(new HeadBucketV2Input().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertEquals(got.getStorageClass(), StorageClassType.STORAGE_CLASS_STANDARD);

            // put
            getHandler().putBucketStorageClass(new PutBucketStorageClassInput().setBucket(Consts.bucket)
                    .setStorageClass(StorageClassType.STORAGE_CLASS_IA));
            // get
            got = getHandler().headBucket(new HeadBucketV2Input().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertEquals(got.getStorageClass(), StorageClassType.STORAGE_CLASS_IA);

            // put
            getHandler().putBucketStorageClass(new PutBucketStorageClassInput().setBucket(Consts.bucket)
                    .setStorageClass(StorageClassType.STORAGE_CLASS_ARCHIVE_FR));
            // get
            got = getHandler().headBucket(new HeadBucketV2Input().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertEquals(got.getStorageClass(), StorageClassType.STORAGE_CLASS_ARCHIVE_FR);
        } catch (TosException e) {
            testFailed(e);
        } finally {
            getHandler().putBucketStorageClass(new PutBucketStorageClassInput().setBucket(Consts.bucket)
                    .setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD));
            // get
            HeadBucketV2Output got = getHandler().headBucket(new HeadBucketV2Input().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertEquals(got.getStorageClass(), StorageClassType.STORAGE_CLASS_STANDARD);
        }
    }

    @Test
    void bucketLocationTest() {
        GetBucketLocationOutput output = getHandler().getBucketLocation(new GetBucketLocationInput().setBucket(Consts.bucket));
        Assert.assertNotNull(output);
        Assert.assertEquals(output.getRegion(), Consts.region);
        Assert.assertEquals(output.getExtranetEndpoint(), Consts.endpoint);
    }

    @Test
    void bucketNameValidateTest() {
        String longLengthBucketName = StringUtils.randomString(64);
        List<String> bucketNameInvalidList = Arrays.asList(null, "", "1", longLengthBucketName);
        for (String name :bucketNameInvalidList) {
            try{
                CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(name).build();
                getHandler().createBucket(input);
            } catch (Exception e) {
                Assert.assertEquals(e.getMessage(), "invalid bucket name, the length must be [3, 63]");
            }
        }
        try{
            CreateBucketV2Input input = CreateBucketV2Input.builder().bucket("-a-").build();
            getHandler().createBucket(input);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "invalid bucket name, the bucket name can be neither starting with '-' nor ending with '-'");
        }
        bucketNameInvalidList = Arrays.asList("&*(%^&", "ABCD", "中文测试");
        for (String name : bucketNameInvalidList) {
            try{
                CreateBucketV2Input input = CreateBucketV2Input.builder().bucket(name).build();
                getHandler().createBucket(input);
            } catch (Exception e) {
                Assert.assertEquals(e.getMessage(), "invalid bucket name, the character set is illegal");
            }
        }
    }

    private void testFailed(Exception e) {
        Consts.LOG.error("bucket test failed, {}", e.toString());
        e.printStackTrace();
        Assert.fail();
    }
}
