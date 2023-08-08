package com.volcengine.tos.internal;

import com.volcengine.tos.TosException;
import com.volcengine.tos.Consts;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.common.*;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.GranteeV2;
import com.volcengine.tos.model.acl.Owner;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.internal.util.StringUtils;
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
                    .azRedundancy(AzRedundancyType.AZ_REDUNDANCY_SINGLE_AZ)
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
            System.out.println(output);
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
        Transform transform = new Transform().setReplaceKeyPrefix(new ReplaceKeyPrefix()
                .setKeyPrefix("aaa").setReplaceWith("bbb"));
        Redirect redirect = new Redirect()
                .setRedirectType(RedirectType.REDIRECT_MIRROR)
                .setFetchSourceOnRedirect(true)
                .setPassQuery(true)
                .setFollowRedirect(true)
                .setMirrorHeader(mirrorHeader)
                .setPublicSource(publicSource)
                .setTransform(transform);
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
    void bucketReplicationTest() {
        try{
            getHandler().deleteBucketReplication(new DeleteBucketReplicationInput().setBucket(Consts.bucketMultiVersionDisabled));
        } catch (TosException e) {
            // ignore
        } catch (Exception e) {
            testFailed(e);
        }

        try{
            getHandler().getBucketReplication(new GetBucketReplicationInput().setBucket(Consts.bucketMultiVersionDisabled));
            Assert.fail();
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        String role = "ServiceRoleforReplicationAccessTOS";
        ReplicationRule rule = new ReplicationRule().setId("1").setStatus(StatusType.STATUS_ENABLED)
                .setPrefixSet(Arrays.asList("prefix1", "prefix2"))
                .setDestination(new Destination().setBucket(Consts.bucketInRegion2).setLocation(Consts.region2)
                        .setStorageClass(StorageClassType.STORAGE_CLASS_IA)
                        .setStorageClassInheritDirectiveType(StorageClassInheritDirectiveType.STORAGE_CLASS_ID_SOURCE_OBJECT))
                .setHistoricalObjectReplication(StatusType.STATUS_DISABLED);
        List<ReplicationRule> rules = new ArrayList<>();
        rules.add(rule);
        try{
            // put
            getHandler().putBucketReplication(new PutBucketReplicationInput().setBucket(Consts.bucketMultiVersionDisabled).setRules(rules).setRole(role));
            // get
            GetBucketReplicationOutput got = getHandler().getBucketReplication(new GetBucketReplicationInput().setBucket(Consts.bucketMultiVersionDisabled));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getRules());
            Assert.assertEquals(got.getRules().size(), 1);
            ReplicationRule gotRule = got.getRules().get(0);
            Assert.assertEquals(gotRule.getId(), "1");
            Assert.assertEquals(gotRule.getHistoricalObjectReplication(), StatusType.STATUS_DISABLED);
            Assert.assertEquals(gotRule.getStatus(), StatusType.STATUS_ENABLED);
            Assert.assertNotNull(gotRule.getPrefixSet());
            Assert.assertEquals(gotRule.getPrefixSet().size(), 2);
            Assert.assertNotNull(gotRule.getDestination());
            Assert.assertEquals(gotRule.getDestination().getLocation(), Consts.region2);
            Assert.assertEquals(gotRule.getDestination().getBucket(), Consts.bucketInRegion2);
            Assert.assertEquals(gotRule.getDestination().getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
            Assert.assertEquals(gotRule.getDestination().getStorageClassInheritDirectiveType(),
                    StorageClassInheritDirectiveType.STORAGE_CLASS_ID_SOURCE_OBJECT);
            Assert.assertNotNull(gotRule.getProgress());
            Consts.LOG.debug("[getBucketReplication] progress is {}", gotRule.getProgress());
        } catch (TosException e) {
            testFailed(e);
        } finally {
            try{
                getHandler().deleteBucketReplication(new DeleteBucketReplicationInput().setBucket(Consts.bucketMultiVersionDisabled));
            } catch (Exception e) {
                testFailed(e);
            }
            try{
                getHandler().getBucketReplication(new GetBucketReplicationInput().setBucket(Consts.bucketMultiVersionDisabled));
                Assert.fail();
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Test
    void bucketVersioningTest() {
        try{
            PutBucketVersioningInput input = new PutBucketVersioningInput().setBucket(Consts.bucketForVersioning)
                    .setStatus(VersioningStatusType.VERSIONING_STATUS_ENABLED);
            getHandler().putBucketVersioning(input);
            Thread.sleep(10000);
            GetBucketVersioningOutput output = getHandler().getBucketVersioning(new GetBucketVersioningInput().setBucket(Consts.bucketForVersioning));
            Assert.assertNotNull(output);
            Assert.assertEquals(output.getStatus(), VersioningStatusType.VERSIONING_STATUS_ENABLED);

            input = new PutBucketVersioningInput().setBucket(Consts.bucketForVersioning)
                    .setStatus(VersioningStatusType.VERSIONING_STATUS_SUSPENDED);
            getHandler().putBucketVersioning(input);
            Thread.sleep(15000);
            output = getHandler().getBucketVersioning(new GetBucketVersioningInput().setBucket(Consts.bucketForVersioning));
            Assert.assertNotNull(output);
            Assert.assertEquals(output.getStatus(), VersioningStatusType.VERSIONING_STATUS_SUSPENDED);
        } catch (TosException | InterruptedException e) {
            testFailed(e);
        }
    }

    @Test
    void bucketWebsiteTest() {
        try{
            getHandler().deleteBucketWebsite(new DeleteBucketWebsiteInput().setBucket(Consts.bucket));
        } catch (TosException e) {
            // ignore
        } catch (Exception e) {
            testFailed(e);
        }

        try{
            getHandler().getBucketWebsite(new GetBucketWebsiteInput().setBucket(Consts.bucket));
            Assert.fail();
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        RoutingRule rule1 = new RoutingRule().setCondition(new RoutingRuleCondition().setKeyPrefixEquals("prefix").setHttpErrorCodeReturnedEquals(404))
                .setRedirect(new RoutingRuleRedirect().setProtocol(ProtocolType.PROTOCOL_HTTP).setHostname("test2.name")
                        .setReplaceKeyWith("replace_key_with").setHttpRedirectCode(302));
        RoutingRule rule2 = new RoutingRule().setCondition(new RoutingRuleCondition().setHttpErrorCodeReturnedEquals(403))
                .setRedirect(new RoutingRuleRedirect().setProtocol(ProtocolType.PROTOCOL_HTTPS).setHostname("test3.name")
                        .setReplaceKeyPrefixWith("replace_prefix2").setHttpRedirectCode(301));
        List<RoutingRule> rules = new ArrayList<>(Arrays.asList(rule1, rule2));
        RedirectAllRequestsTo redirectAllRequestsTo = new RedirectAllRequestsTo().setHostname("test.com").setProtocol("http");
        IndexDocument indexDocument = new IndexDocument().setSuffix("index.html").setForbiddenSubDir(true);
        ErrorDocument errorDocument = new ErrorDocument().setKey("error.html");
        try{
            // put
            getHandler().putBucketWebsite(new PutBucketWebsiteInput().setBucket(Consts.bucket).setRedirectAllRequestsTo(redirectAllRequestsTo));
            // get
            GetBucketWebsiteOutput got = getHandler().getBucketWebsite(new GetBucketWebsiteInput().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getRedirectAllRequestsTo());
            Assert.assertEquals(got.getRedirectAllRequestsTo().getHostname(), "test.com");
            Assert.assertEquals(got.getRedirectAllRequestsTo().getProtocol(), "http");

            getHandler().putBucketWebsite(new PutBucketWebsiteInput().setBucket(Consts.bucket).setRoutingRules(rules)
                    .setIndexDocument(indexDocument).setErrorDocument(errorDocument));
            got = getHandler().getBucketWebsite(new GetBucketWebsiteInput().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getIndexDocument());
            Assert.assertEquals(got.getIndexDocument().getSuffix(), "index.html");
            Assert.assertTrue(got.getIndexDocument().isForbiddenSubDir());
            Assert.assertNotNull(got.getErrorDocument());
            Assert.assertEquals(got.getErrorDocument().getKey(), "error.html");
            Assert.assertNotNull(got.getRoutingRules());
            Assert.assertEquals(got.getRoutingRules().size(), 2);
            Assert.assertNotNull(got.getRoutingRules().get(0).getCondition());
            Assert.assertNotNull(got.getRoutingRules().get(0).getRedirect());
            Assert.assertNotNull(got.getRoutingRules().get(1).getCondition());
            Assert.assertNotNull(got.getRoutingRules().get(1).getRedirect());
            RoutingRuleCondition condition =  got.getRoutingRules().get(0).getCondition();
            Assert.assertEquals(condition.getKeyPrefixEquals(), "prefix");
            Assert.assertEquals(condition.getHttpErrorCodeReturnedEquals(), 404);
            RoutingRuleCondition condition1 =  got.getRoutingRules().get(1).getCondition();
            Assert.assertNull(condition1.getKeyPrefixEquals());
            Assert.assertEquals(condition1.getHttpErrorCodeReturnedEquals(), 403);
            RoutingRuleRedirect redirect = got.getRoutingRules().get(0).getRedirect();
            Assert.assertEquals(redirect.getHostname(), "test2.name");
            Assert.assertEquals(redirect.getProtocol(), ProtocolType.PROTOCOL_HTTP);
            Assert.assertEquals(redirect.getHttpRedirectCode(), 302);
            Assert.assertEquals(redirect.getReplaceKeyWith(), "replace_key_with");
            Assert.assertNull(redirect.getReplaceKeyPrefixWith());
            redirect = got.getRoutingRules().get(1).getRedirect();
            Assert.assertEquals(redirect.getHostname(), "test3.name");
            Assert.assertEquals(redirect.getProtocol(), ProtocolType.PROTOCOL_HTTPS);
            Assert.assertEquals(redirect.getHttpRedirectCode(), 301);
            Assert.assertEquals(redirect.getReplaceKeyPrefixWith(), "replace_prefix2");
            Assert.assertNull(redirect.getReplaceKeyWith());
        } catch (TosException e) {
            testFailed(e);
        } finally {
            try{
                getHandler().deleteBucketWebsite(new DeleteBucketWebsiteInput().setBucket(Consts.bucket));
            } catch (Exception e) {
                testFailed(e);
            }
            try{
                getHandler().getBucketWebsite(new GetBucketWebsiteInput().setBucket(Consts.bucket));
                Assert.fail();
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Test
    void bucketRealTimeLogTest() {
        try{
            getHandler().deleteBucketRealTimeLog(new DeleteBucketRealTimeLogInput().setBucket(Consts.bucket));
        } catch (TosException e) {
            // ignore
        } catch (Exception e) {
            testFailed(e);
        }

        try{
            getHandler().getBucketRealTimeLog(new GetBucketRealTimeLogInput().setBucket(Consts.bucket));
            Assert.fail();
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        RealTimeLogConfiguration configuration = new RealTimeLogConfiguration().setRole("TOSLogArchiveTLSRole")
                .setConfiguration(new AccessLogConfiguration().setUseServiceTopic(true));
        try{
            // put
            getHandler().putBucketRealTimeLog(new PutBucketRealTimeLogInput().setBucket(Consts.bucket).setConfiguration(configuration));
            // get
            GetBucketRealTimeLogOutput got = getHandler().getBucketRealTimeLog(new GetBucketRealTimeLogInput().setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getConfiguration());
            Assert.assertEquals(got.getConfiguration().getRole(), "TOSLogArchiveTLSRole");
            Assert.assertNotNull(got.getConfiguration().getConfiguration());
            Assert.assertNotNull(got.getConfiguration().getConfiguration().getTlsProjectID());
            Assert.assertNotNull(got.getConfiguration().getConfiguration().getTlsTopicID());
        } catch (TosException e) {
            testFailed(e);
        } finally {
            try{
                getHandler().deleteBucketRealTimeLog(new DeleteBucketRealTimeLogInput().setBucket(Consts.bucket));
            } catch (Exception e) {
                testFailed(e);
            }
            try{
                getHandler().getBucketRealTimeLog(new GetBucketRealTimeLogInput().setBucket(Consts.bucket));
                Assert.fail();
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Test
    void bucketCustomDomainTest() {
        String domain = "www.volcengine.com";
        try{
            getHandler().deleteBucketCustomDomain(new DeleteBucketCustomDomainInput().setBucket(Consts.bucket).setDomain(domain));
        } catch (TosException e) {
            testFailed(e);
        }

        try{
            getHandler().listBucketCustomDomain(new ListBucketCustomDomainInput().setBucket(Consts.bucket));
            Assert.fail();
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        CustomDomainRule rule = new CustomDomainRule().setDomain(domain);
        PutBucketCustomDomainInput input = new PutBucketCustomDomainInput().setBucket(Consts.bucket).setRule(rule);
        try{
            // put
            getHandler().putBucketCustomDomain(input);
            // list
            ListBucketCustomDomainOutput output = getHandler().listBucketCustomDomain(new
                    ListBucketCustomDomainInput().setBucket(Consts.bucket));
            Assert.assertNotNull(output.getRule());
            Assert.assertEquals(output.getRule().size(), 1);
            Assert.assertEquals(output.getRule().get(0).getDomain(), domain);
            Assert.assertEquals(output.getRule().get(0).getCertID(), "");
        } catch (TosException e) {
            testFailed(e);
        } finally {
            try{
                getHandler().deleteBucketCustomDomain(new DeleteBucketCustomDomainInput().setBucket(Consts.bucket).setDomain(domain));
            } catch (Exception e) {
                testFailed(e);
            }
            try{
                getHandler().listBucketCustomDomain(new ListBucketCustomDomainInput().setBucket(Consts.bucket));
                Assert.fail();
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Test
    void bucketNotificationTest() {
        FilterRule rule = new FilterRule().setName("prefix").setValue("dals");
        Filter filter = new Filter().setKey(new FilterKey().setRules(Collections.singletonList(rule)));
        CloudFunctionConfiguration configuration = new CloudFunctionConfiguration().setId("test-id").setCloudFunction("zkru2tzw")
                .setEvents(Arrays.asList("tos:ObjectCreated:Put", "tos:ObjectDownload:*")).setFilter(filter);
        try{
            // put
            getHandler().putBucketNotification(new PutBucketNotificationInput().setBucket(Consts.bucket)
                    .setCloudFunctionConfigurations(new ArrayList<>(Collections.singletonList(configuration))));
            // get
            GetBucketNotificationOutput got = getHandler().getBucketNotification(new GetBucketNotificationInput()
                    .setBucket(Consts.bucket));
            Assert.assertNotNull(got);
            Assert.assertNotNull(got.getCloudFunctionConfiguration());
            Assert.assertEquals(got.getCloudFunctionConfiguration().size(), 1);
            CloudFunctionConfiguration gotConf = got.getCloudFunctionConfiguration().get(0);
            Assert.assertEquals(gotConf.getId(), "test-id");
            Assert.assertEquals(gotConf.getCloudFunction(), "zkru2tzw");
            Assert.assertEquals(gotConf.getFilter().toString(), filter.toString());
            Assert.assertNotNull(gotConf.getEvents());
            Assert.assertEquals(gotConf.getEvents().size(), 2);
            Assert.assertEquals(gotConf.getEvents().get(0), "tos:ObjectCreated:Put");
            Assert.assertEquals(gotConf.getEvents().get(1), "tos:ObjectDownload:*");
        } catch (TosException e) {
            testFailed(e);
        }
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

    @Test
    void bucketPolicyTest() {
        try{
            getHandler().deleteBucketPolicy(new DeleteBucketPolicyInput().setBucket(Consts.bucket));
        } catch (TosException e) {
            testFailed(e);
        }

        try{
            getHandler().getBucketPolicy(new GetBucketPolicyInput().setBucket(Consts.bucket));
            Assert.fail();
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        String policy = "{\"Statement\":[{\"Action\":[\"*\"],\"Effect\":\"Allow\",\"Principal\":\"*\",\"Resource\":[\"trn:tos:::"
                + Consts.bucket + "/*\",\"trn:tos:::" + Consts.bucket + "\"],\"Sid\":\"internal public\"}]}";
        PutBucketPolicyInput input = new PutBucketPolicyInput().setBucket(Consts.bucket).setPolicy(policy);
        try{
            // put
            getHandler().putBucketPolicy(input);
            // list
            GetBucketPolicyOutput output = getHandler().getBucketPolicy(new GetBucketPolicyInput().setBucket(Consts.bucket));;
            Assert.assertNotNull(output.getPolicy());
        } catch (TosException e) {
            testFailed(e);
        } finally {
            try{
                getHandler().deleteBucketPolicy(new DeleteBucketPolicyInput().setBucket(Consts.bucket));
            } catch (Exception e) {
                testFailed(e);
            }
            try{
                getHandler().getBucketPolicy(new GetBucketPolicyInput().setBucket(Consts.bucket));
                Assert.fail();
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
            }
        }
    }

    // todo open in next version
//    @Test
//    void bucketRenameTest() {
//        try{
//            getHandler().deleteBucketRename(new DeleteBucketRenameInput().setBucket(Consts.bucket));
//        } catch (TosException e) {
//            testFailed(e);
//        }
//
//        try{
//            GetBucketRenameOutput output = getHandler().getBucketRename(new GetBucketRenameInput().setBucket(Consts.bucket));
//            Assert.assertFalse(output.isRenameEnable());
//        } catch (TosException e) {
//            testFailed(e);
//        }
//
//        try{
//            // put
//            boolean renameEnable = true;
//            PutBucketRenameInput input = new PutBucketRenameInput().setBucket(Consts.bucket).setRenameEnable(renameEnable);
//            getHandler().putBucketRename(input);
//            // get
//            GetBucketRenameOutput output = getHandler().getBucketRename(new GetBucketRenameInput().setBucket(Consts.bucket));;
//            Assert.assertTrue(output.isRenameEnable());
//        } catch (TosException e) {
//            testFailed(e);
//        } finally {
//            try{
//                getHandler().deleteBucketRename(new DeleteBucketRenameInput().setBucket(Consts.bucket));
//            } catch (Exception e) {
//                testFailed(e);
//            }
//            try{
//                GetBucketRenameOutput output = getHandler().getBucketRename(new GetBucketRenameInput().setBucket(Consts.bucket));
//                Assert.assertFalse(output.isRenameEnable());
//            } catch (TosException e) {
//                testFailed(e);
//            }
//        }
//    }

    private void testFailed(Exception e) {
        Consts.LOG.error("bucket test failed, {}", e.toString());
        e.printStackTrace();
        Assert.fail();
    }
}
