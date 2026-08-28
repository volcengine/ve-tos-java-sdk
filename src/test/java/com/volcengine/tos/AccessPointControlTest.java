package com.volcengine.tos;

import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.model.bucket.CreateBucketV2Input;
import com.volcengine.tos.model.bucket.DeleteBucketInput;
import com.volcengine.tos.model.object.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AccessPointControlTest {

        private TOSV2 buildClient() {
                return new TOSV2ClientBuilder().build(TOSClientConfiguration.builder()
                                .region(Consts.region)
                                .endpoint(Consts.endpoint)
                                .controlEndpoint(Consts.controlEndpoint)
                                .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey))
                                .build());
        }

        @Test
        public void accessPointCrudTest() {

                TOSV2 client = buildClient();
                String bucketName = "access-point-test-" + System.currentTimeMillis();
                String accessPointName = "sdk-ap-" + System.currentTimeMillis();

                try {
                        // CreateBucket
                        client.createBucket(CreateBucketV2Input.builder().bucket(bucketName).build());

                        try {
                                // CreateAccessPoint
                                CreateAccessPointInput createInput = CreateAccessPointInput.builder()
                                                .accountId(Consts.accountId)
                                                .accessPointName(accessPointName)
                                                .bucket(bucketName)
                                                .bucketAccountId(Consts.accountId)
                                                .networkOrigin("internet")
                                                .build();
                                CreateAccessPointOutput createOutput = client.createAccessPoint(createInput);
                                Assert.assertEquals(createOutput.getRequestInfo().getStatusCode(), HttpStatus.OK);
                                Assert.assertNotNull(createOutput.getAlias());
                                Assert.assertFalse(createOutput.getAlias().isEmpty());
                                Assert.assertNotNull(createOutput.getAccessPointTrn());
                                Assert.assertFalse(createOutput.getAccessPointTrn().isEmpty());
                                Consts.LOG.info("create access point, trn={}, alias={}",
                                                createOutput.getAccessPointTrn(), createOutput.getAlias());

                                // GetAccessPoint
                                GetAccessPointInput getInput = GetAccessPointInput.builder()
                                                .accountId(Consts.accountId)
                                                .accessPointName(accessPointName)
                                                .build();
                                GetAccessPointOutput getOutput = client.getAccessPoint(getInput);
                                Assert.assertNotNull(getOutput);
                                Assert.assertEquals(getOutput.getName(), accessPointName);

                                // ListAccessPoints
                                ListAccessPointsInput listInput = ListAccessPointsInput.builder()
                                                .accountId(Consts.accountId)
                                                .bucket(bucketName)
                                                .maxResults(100)
                                                .build();
                                ListAccessPointsOutput listOutput = client.listAccessPoints(listInput);
                                Assert.assertNotNull(listOutput);
                                Assert.assertNotNull(listOutput.getAccessPoints());
                                Assert.assertEquals(listOutput.getAccessPoints().size(), 1);

                                // ListBindAcceleratorForAccessPoint
                                ListBindAcceleratorForAccessPointInput listBindInput = ListBindAcceleratorForAccessPointInput
                                                .builder()
                                                .accountId(Consts.accountId)
                                                .accessPointName(accessPointName)
                                                .build();
                                ListBindAcceleratorForAccessPointOutput listBindOutput = client
                                                .listBindAcceleratorForAccessPoint(listBindInput);
                                Assert.assertNotNull(listBindOutput);
                                Consts.LOG.info("accelerators bound to access point {}: {}", accessPointName,
                                                listBindOutput.getAccelerators());

                                // DeleteAccessPoint
                                DeleteAccessPointInput deleteInput = DeleteAccessPointInput.builder()
                                                .accountId(Consts.accountId)
                                                .accessPointName(accessPointName)
                                                .build();
                                DeleteAccessPointOutput deleteOutput = client.deleteAccessPoint(deleteInput);
                                Assert.assertEquals(deleteOutput.getRequestInfo().getStatusCode(),
                                                HttpStatus.NO_CONTENT);

                                // GetAccessPoint again, expect not found
                                try {
                                        client.getAccessPoint(getInput);
                                        Assert.fail("expected getAccessPoint to fail after deletion");
                                } catch (TosServerException e) {
                                        Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
                                }
                        } finally {
                                try {
                                        client.deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
                                } catch (Throwable t) {
                                        // ignore
                                        t.printStackTrace();
                                        Assert.fail(t.getMessage());
                                }
                        }
                } catch (Throwable t) {
                        t.printStackTrace();
                        Assert.fail(t.getMessage());
                }
        }

        @Test
        public void accessPointVpcTest() {
                String vpcId = System.getenv("TEST_VPC_ID");
                if (vpcId == null || vpcId.isEmpty()) {
                        Consts.LOG.info("TEST_VPC_ID is not set, skip accessPointVpcTest");
                        return;
                }

                TOSV2 client = buildClient();
                String bucketName = "access-point-vpc-test-" + System.currentTimeMillis();
                String accessPointName = "sdk-ap-vpc-" + System.currentTimeMillis();

                try {
                        // CreateBucket
                        client.createBucket(CreateBucketV2Input.builder().bucket(bucketName).build());

                        try {
                                // CreateAccessPoint
                                CreateAccessPointInput createInput = CreateAccessPointInput.builder()
                                                .accountId(Consts.accountId)
                                                .accessPointName(accessPointName)
                                                .bucket(bucketName)
                                                .bucketAccountId(Consts.accountId)
                                                .networkOrigin("vpc")
                                                .vpcId(vpcId)
                                                .build();
                                CreateAccessPointOutput createOutput = client.createAccessPoint(createInput);
                                Assert.assertEquals(createOutput.getRequestInfo().getStatusCode(), HttpStatus.OK);
                                Assert.assertNotNull(createOutput.getAlias());
                                Assert.assertFalse(createOutput.getAlias().isEmpty());
                                Assert.assertNotNull(createOutput.getAccessPointTrn());
                                Assert.assertFalse(createOutput.getAccessPointTrn().isEmpty());
                                Consts.LOG.info("create access point, trn={}, alias={}",
                                                createOutput.getAccessPointTrn(), createOutput.getAlias());

                                // GetAccessPoint
                                GetAccessPointInput getInput = GetAccessPointInput.builder()
                                                .accountId(Consts.accountId)
                                                .accessPointName(accessPointName)
                                                .build();
                                GetAccessPointOutput getOutput = client.getAccessPoint(getInput);
                                Assert.assertNotNull(getOutput);
                                Assert.assertEquals(getOutput.getName(), accessPointName);
                                Assert.assertEquals(getOutput.getNetworkOrigin(), "vpc");
                                Assert.assertEquals(getOutput.getVpcId(), vpcId);

                                // DeleteAccessPoint
                                DeleteAccessPointInput deleteInput = DeleteAccessPointInput.builder()
                                                .accountId(Consts.accountId)
                                                .accessPointName(accessPointName)
                                                .build();
                                DeleteAccessPointOutput deleteOutput = client.deleteAccessPoint(deleteInput);
                                Assert.assertEquals(deleteOutput.getRequestInfo().getStatusCode(),
                                                HttpStatus.NO_CONTENT);

                                // GetAccessPoint again, expect not found
                                try {
                                        client.getAccessPoint(getInput);
                                        Assert.fail("expected getAccessPoint to fail after deletion");
                                } catch (TosServerException e) {
                                        Assert.assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
                                }
                        } finally {
                                try {
                                        client.deleteBucket(DeleteBucketInput.builder().bucket(bucketName).build());
                                } catch (Throwable t) {
                                        // ignore
                                        t.printStackTrace();
                                        Assert.fail(t.getMessage());
                                }
                        }
                } catch (Throwable t) {
                        t.printStackTrace();
                        Assert.fail(t.getMessage());
                }
        }

        @Test
        public void listBindAccessPointForAcceleratorTest() {
                TOSV2 client = buildClient();

                try {
                        ListBindAccessPointForAcceleratorInput input = ListBindAccessPointForAcceleratorInput.builder()
                                        .accountId(Consts.accountId)
                                        .acceleratorId(Consts.acceleratorId)
                                        .build();
                        ListBindAccessPointForAcceleratorOutput output = client
                                        .listBindAccessPointForAccelerator(input);
                        Consts.LOG.info("access points bound to accelerator {}: {}",
                                        Consts.acceleratorId, output.getAccessPoints());
                        Assert.assertNotNull(output.getAccessPoints());
                } catch (Throwable t) {
                        t.printStackTrace();
                        Assert.fail(t.getMessage());
                }
        }
}
