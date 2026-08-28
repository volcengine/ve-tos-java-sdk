package com.volcengine.tos.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.volcengine.tos.comm.common.StatusType;
import com.volcengine.tos.internal.util.PayloadConverter;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.bucket.BucketLifecycleNotFilter;
import com.volcengine.tos.model.bucket.Expiration;
import com.volcengine.tos.model.bucket.GetBucketLifecycleOutput;
import com.volcengine.tos.model.bucket.LifecycleRule;
import com.volcengine.tos.model.bucket.LifecycleRuleFilter;
import com.volcengine.tos.model.bucket.PutBucketLifecycleInput;
import com.volcengine.tos.model.bucket.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

public class LifecycleRuleFilterTest {
    @Test
    public void lifecycleNotFilterSerializesAndDeserializes() throws Exception {
        BucketLifecycleNotFilter first = new BucketLifecycleNotFilter().setPrefix("skip-a/")
                .setTags(Arrays.asList(new Tag().setKey("team").setValue("sdk"),
                        new Tag().setKey("tier").setValue("test")));
        BucketLifecycleNotFilter second = new BucketLifecycleNotFilter().setPrefix("skip-b/")
                .setTags(Collections.singletonList(new Tag().setKey("env").setValue("boe")));
        PutBucketLifecycleInput input = new PutBucketLifecycleInput().setBucket("bucket-name")
                .setRules(Collections.singletonList(new LifecycleRule().setId("not-rule")
                        .setStatus(StatusType.STATUS_ENABLED).setExpiration(new Expiration().setDays(1))
                        .setFilter(new LifecycleRuleFilter().setNot(Arrays.asList(first, second)))));

        JsonNode body = TosUtils.getJsonMapper().readTree(PayloadConverter.serializePayload(input).getData());
        Assert.assertEquals(body.at("/Rules/0/Filter/Not/0/Prefix").asText(), "skip-a/");
        Assert.assertEquals(body.at("/Rules/0/Filter/Not/0/Tags/1/Key").asText(), "tier");
        Assert.assertEquals(body.at("/Rules/0/Filter/Not/1/Tags/0/Value").asText(), "boe");

        GetBucketLifecycleOutput output = PayloadConverter.parsePayload(body.toString(),
                new TypeReference<GetBucketLifecycleOutput>() {});
        Assert.assertEquals(output.getRules().get(0).getFilter().getNot().get(0).getTags().get(0).getValue(), "sdk");
        Assert.assertEquals(output.getRules().get(0).getFilter().getNot().get(1).getPrefix(), "skip-b/");
    }
}
