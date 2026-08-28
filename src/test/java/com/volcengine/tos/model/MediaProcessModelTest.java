package com.volcengine.tos.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.volcengine.tos.comm.common.ContainerFormatType;
import com.volcengine.tos.internal.model.VideoConvertJobRequest;
import com.volcengine.tos.internal.util.PayloadConverter;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.bucket.Audio;
import com.volcengine.tos.model.bucket.Container;
import com.volcengine.tos.model.bucket.ConvertJobInput;
import com.volcengine.tos.model.bucket.ConvertJobOutput;
import com.volcengine.tos.model.bucket.CreateVideoConvertJobOutput;
import com.volcengine.tos.model.bucket.TimeInterval;
import com.volcengine.tos.model.bucket.Transcode;
import com.volcengine.tos.model.bucket.TranscodeConfig;
import com.volcengine.tos.model.bucket.Video;
import com.volcengine.tos.model.bucket.Watermark;
import com.volcengine.tos.model.bucket.WatermarkText;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

public class MediaProcessModelTest {
    @Test
    public void videoConvertJobRequestSerializesDocumentedFields() throws Exception {
        VideoConvertJobRequest request = new VideoConvertJobRequest()
                .setInput(ConvertJobInput.builder().object("input.mp4").build())
                .setTranscodeConfig(TranscodeConfig.builder()
                        .templateID("tpl_transcode_xxx")
                        .transcode(Transcode.builder()
                                .timeInterval(TimeInterval.builder().start(0).build())
                                .container(Container.builder().format(ContainerFormatType.MP4).build())
                                .video(Video.builder().codec("h264").build())
                                .audio(Audio.builder().codec("mp3").build())
                                .build())
                        .watermark(Collections.singletonList(Watermark.builder()
                                .type("Text")
                                .pos("TopRight")
                                .locMode("Relative")
                                .dx(10)
                                .dy(10)
                                .startTime(0)
                                .endTime(10000)
                                .text(WatermarkText.builder()
                                        .fontSize(100)
                                        .fontType("wqy-zenhei")
                                        .fontColor("0x000000")
                                        .transparency(20)
                                        .text("hello world")
                                        .build())
                                .build()))
                        .watermarkTemplateID(Collections.singletonList("tpl_watermark_xxx"))
                        .build())
                .setOutput(ConvertJobOutput.builder()
                        .region("cn-beijing")
                        .bucket("target-bucket")
                        .object("output.mp4")
                        .build())
                .setCallback("http://example.com/callback");

        JsonNode body = TosUtils.getJsonMapper().readTree(PayloadConverter.serializePayload(request).getData());
        Assert.assertFalse(body.has("Tag"));
        Assert.assertEquals(body.at("/Input/Object").asText(), "input.mp4");
        Assert.assertEquals(body.at("/TranscodeConfig/TemplateID").asText(), "tpl_transcode_xxx");
        Assert.assertEquals(body.at("/TranscodeConfig/Transcode/TimeInterval/Start").asInt(), 0);
        Assert.assertEquals(body.at("/TranscodeConfig/Transcode/Container/Format").asText(), "mp4");
        Assert.assertEquals(body.at("/TranscodeConfig/Transcode/Video/Codec").asText(), "h264");
        Assert.assertEquals(body.at("/TranscodeConfig/Transcode/Audio/Codec").asText(), "mp3");
        Assert.assertEquals(body.at("/TranscodeConfig/Watermark/0/Text/Text").asText(), "hello world");
        Assert.assertEquals(body.at("/TranscodeConfig/WatermarkTemplateID/0").asText(), "tpl_watermark_xxx");
        Assert.assertEquals(body.at("/Output/Region").asText(), "cn-beijing");
        Assert.assertEquals(body.at("/Output/Bucket").asText(), "target-bucket");
        Assert.assertEquals(body.at("/Output/Object").asText(), "output.mp4");
        Assert.assertEquals(body.at("/Callback").asText(), "http://example.com/callback");
    }

    @Test
    public void createVideoConvertJobOutputParsesCodeMessageAndJobId() {
        CreateVideoConvertJobOutput output = PayloadConverter.parsePayload(
                "{\"Code\":\"OK\",\"Message\":\"Success\",\"JobId\":\"job-1\"}",
                new TypeReference<CreateVideoConvertJobOutput>() {
                });
        Assert.assertEquals(output.getCode(), "OK");
        Assert.assertEquals(output.getMessage(), "Success");
        Assert.assertEquals(output.getJobId(), "job-1");
    }
}
