package com.volcengine.tos;

import com.volcengine.tos.comm.common.AudioContainerFormatType;
import com.volcengine.tos.comm.common.ContainerFormatType;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.DeleteObjectInput;
import com.volcengine.tos.model.object.ListObjectsType2Input;
import com.volcengine.tos.model.object.ListObjectsType2Output;
import com.volcengine.tos.model.object.ListedObjectV2;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.volcengine.tos.auth.StaticCredentials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


import java.util.Arrays;
import java.util.List;

public class DataProcessTest {
    private static TOSV2 client = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder().region(Consts.region).endpoint(Consts.endpoint)
            .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).build());


    private static String base64String(String originalStr){
        // 步骤1：将字符串转为字节数组（指定UTF-8，避免乱码）
        byte[] originalBytes = originalStr.getBytes(StandardCharsets.UTF_8);

        // 步骤2：Base64 编码（Basic 编码器）
        byte[] encodedBytes = Base64.getEncoder().encode(originalBytes);

        // 步骤3：将编码后的字节数组转为字符串
        String encodedStr = new String(encodedBytes, StandardCharsets.UTF_8);
        return encodedStr;
    }

    private static void deleteBucket(String bucketName){
        try {
            ListObjectsType2Input linput = new ListObjectsType2Input().setBucket(bucketName).setMaxKeys(1000);
            ListObjectsType2Output loutput = client.listObjectsType2(linput);
            if (loutput.getContents() != null && !loutput.getContents().isEmpty()){
                for (ListedObjectV2 content : loutput.getContents()){
                    DeleteObjectInput deleteObjectInput = new DeleteObjectInput().setBucket(bucketName).setKey(content.getKey());
                    client.deleteObject(deleteObjectInput);
                }
            }

            client.deleteBucket(bucketName);
        } catch (TosException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void AudioConvertTemplateCurdTest() {
        String bucketName = "java-sdk-" + System.currentTimeMillis();
        try {
            client.createBucket(new CreateBucketV2Input().setBucket(bucketName));
            TimeInterval timeInterval = TimeInterval.builder().start(1).duration(10).build();
            AudioConvertConfig audioConvertConfig = AudioConvertConfig.builder()
                    .timeInterval(timeInterval)
                    .containerFormat(AudioContainerFormatType.MP3)
                    .bitRate(8000)
                    .bitRateOpt(1)
                    .sampleFormat("fltp")
                    .sampleRate(11025)
                    .channels(2)
                    .build();
            PutAudioConvertTemplateInput putAudioConvertTemplateInput = PutAudioConvertTemplateInput.builder()
                    .bucket(bucketName)
                    .name("test")
                    .audioConvertConfig(audioConvertConfig)
                    .build();
            PutAudioConvertTemplateOutput putAudioConvertTemplateOutput = client.putAudioConvertTemplate(putAudioConvertTemplateInput);
            Assert.assertNotNull(putAudioConvertTemplateOutput);
            Assert.assertEquals(putAudioConvertTemplateOutput.getRequestInfo().getStatusCode(), 200);
            Assert.assertNotNull(putAudioConvertTemplateOutput.getId());

            GetAudioConvertTemplateInput getAudioConvertTemplateInput = GetAudioConvertTemplateInput.builder()
                    .bucket(bucketName)
                    .id(putAudioConvertTemplateOutput.getId())
                    .build();
            GetAudioConvertTemplateOutput getAudioConvertTemplateOutput = client.getAudioConvertTemplate(getAudioConvertTemplateInput);
            Consts.LOG.info("getAudioConvertTemplateOutput: {}", getAudioConvertTemplateOutput);
            Assert.assertNotNull(getAudioConvertTemplateOutput);
            Assert.assertEquals(getAudioConvertTemplateOutput.getRequestInfo().getStatusCode(), 200);
            Assert.assertEquals(getAudioConvertTemplateOutput.getId(), putAudioConvertTemplateOutput.getId());
            Assert.assertEquals(getAudioConvertTemplateOutput.getName(), "test");
            Assert.assertEquals(getAudioConvertTemplateOutput.getTag(), "AudioConvert");
            AudioConvertConfig outputAudioConvertConfig = getAudioConvertTemplateOutput.getAudioConvertConfig();
            Assert.assertEquals(outputAudioConvertConfig.getTimeInterval().getStart(), timeInterval.getStart());
            Assert.assertEquals(outputAudioConvertConfig.getTimeInterval().getDuration(), timeInterval.getDuration());
            Assert.assertEquals(outputAudioConvertConfig.getContainerFormat(), audioConvertConfig.getContainerFormat());
            Assert.assertEquals(outputAudioConvertConfig.getBitRate(), audioConvertConfig.getBitRate());
            Assert.assertEquals(outputAudioConvertConfig.getBitRateOpt(), audioConvertConfig.getBitRateOpt());
            Assert.assertEquals(outputAudioConvertConfig.getSampleFormat(), audioConvertConfig.getSampleFormat());
            Assert.assertEquals(outputAudioConvertConfig.getSampleRate(), audioConvertConfig.getSampleRate());
            Assert.assertEquals(outputAudioConvertConfig.getChannels(), audioConvertConfig.getChannels());

            ListAudioConvertTemplatesInput listAudioConvertTemplatesInput = ListAudioConvertTemplatesInput.builder()
                    .bucket(bucketName)
                    .build();
            ListAudioConvertTemplatesOutput listAudioConvertTemplatesOutput = client.listAudioConvertTemplates(listAudioConvertTemplatesInput);
            Assert.assertNotNull(listAudioConvertTemplatesOutput);
            Consts.LOG.info("listAudioConvertTemplatesOutput: {}", listAudioConvertTemplatesOutput);
            Assert.assertEquals(listAudioConvertTemplatesOutput.getRequestInfo().getStatusCode(), 200);
            List<AudioConvertTemplate> audioConvertTemplates = listAudioConvertTemplatesOutput.getAudioConvertTemplates();
            Assert.assertNotNull(audioConvertTemplates);
            Assert.assertEquals(audioConvertTemplates.size(), 1);
            AudioConvertTemplate audioConvertTemplate = audioConvertTemplates.get(0);
            Assert.assertEquals(audioConvertTemplate.getId(), putAudioConvertTemplateOutput.getId());
            Assert.assertEquals(audioConvertTemplate.getName(), "test");
            Assert.assertEquals(audioConvertTemplate.getTag(), "AudioConvert");
            AudioConvertConfig outputAudioConvertConfig2 = audioConvertTemplate.getAudioConvertConfig();
            Assert.assertEquals(outputAudioConvertConfig2.getTimeInterval().getStart(), timeInterval.getStart());
            Assert.assertEquals(outputAudioConvertConfig2.getTimeInterval().getDuration(), timeInterval.getDuration());
            Assert.assertEquals(outputAudioConvertConfig2.getContainerFormat(), audioConvertConfig.getContainerFormat());
            Assert.assertEquals(outputAudioConvertConfig2.getBitRate(), audioConvertConfig.getBitRate());
            Assert.assertEquals(outputAudioConvertConfig2.getBitRateOpt(), audioConvertConfig.getBitRateOpt());
            Assert.assertEquals(outputAudioConvertConfig2.getSampleFormat(), audioConvertConfig.getSampleFormat());
            Assert.assertEquals(outputAudioConvertConfig2.getSampleRate(), audioConvertConfig.getSampleRate());
            Assert.assertEquals(outputAudioConvertConfig2.getChannels(), audioConvertConfig.getChannels());


            DeleteAudioConvertTemplateInput deleteAudioConvertTemplateInput = DeleteAudioConvertTemplateInput.builder()
                    .bucket(bucketName)
                    .id(putAudioConvertTemplateOutput.getId())
                    .build();
            DeleteAudioConvertTemplateOutput deleteAudioConvertTemplateOutput = client.deleteAudioConvertTemplate(deleteAudioConvertTemplateInput);
            Assert.assertNotNull(deleteAudioConvertTemplateOutput);
            Assert.assertEquals(deleteAudioConvertTemplateOutput.getRequestInfo().getStatusCode(), 204);

            try {
                client.getAudioConvertTemplate(getAudioConvertTemplateInput);
                Assert.fail("get audio convert template after delete should fail");
            } catch (TosException e) {
                Assert.assertEquals(e.getStatusCode(), 404);
            }
        } catch (TosException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            client.deleteBucket(new DeleteBucketInput().setBucket(bucketName));
        }
    }

    @Test
    public void DifferentAudioContainerFormatTypeTest(){
        String bucketName = "java-sdk-" + System.currentTimeMillis();
        try {
            client.createBucket(new CreateBucketV2Input().setBucket(bucketName));
            List<AudioContainerFormatType> containerFormatTypes = Arrays.asList(
                    AudioContainerFormatType.WAV,
                    AudioContainerFormatType.MP3,
                    AudioContainerFormatType.AAC,
                    AudioContainerFormatType.FLAC,
                    AudioContainerFormatType.OGA,
                    AudioContainerFormatType.AC3,
                    AudioContainerFormatType.OPUS
            );
            for (AudioContainerFormatType containerFormatType : containerFormatTypes) {
                Consts.LOG.info("containerFormatType: {}", containerFormatType.getValue());
                AudioConvertConfig audioConvertConfig = AudioConvertConfig.builder()
                        .containerFormat(containerFormatType)
                        .build();
                PutAudioConvertTemplateInput putAudioConvertTemplateInput = PutAudioConvertTemplateInput.builder()
                        .bucket(bucketName)
                        .name("test")
                        .audioConvertConfig(audioConvertConfig)
                        .build();
                PutAudioConvertTemplateOutput putAudioConvertTemplateOutput = client.putAudioConvertTemplate(putAudioConvertTemplateInput);

                GetAudioConvertTemplateInput getAudioConvertTemplateInput = GetAudioConvertTemplateInput.builder()
                        .bucket(bucketName)
                        .id(putAudioConvertTemplateOutput.getId())
                        .build();
                GetAudioConvertTemplateOutput getAudioConvertTemplateOutput = client.getAudioConvertTemplate(getAudioConvertTemplateInput);
                Consts.LOG.info("getAudioConvertTemplateOutput: {}", getAudioConvertTemplateOutput);
                Assert.assertEquals(getAudioConvertTemplateOutput.getAudioConvertConfig().getContainerFormat(), containerFormatType);
            }
        } catch (TosException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            client.deleteBucket(new DeleteBucketInput().setBucket(bucketName));
        }
    }

    @Test
    public void VideoConvertTemplateCurdTest() {
        String bucketName = "java-sdk-" + System.currentTimeMillis();
        try {
            client.createBucket(new CreateBucketV2Input().setBucket(bucketName));

            // Create container
            ClipConfig clipConfig = ClipConfig.builder()
                    .duration(3600000)
                    .build();
            Container container = Container.builder()
                    .format(ContainerFormatType.MP4)
                    .clipConfig(clipConfig)
                    .build();

            // Create video config
            Video video = Video.builder()
                    .codec("h264")
                    .width(4096)
                    .height(4096)
                    .crf(23)
                    .pixFmt("yuv420p")
                    .bitRate(50000000)
                    .fps(30)
                    .remove(false)
                    .build();

            // Create audio config
            Audio audio = Audio.builder()
                    .codec("aac")
                    .bitRate(1000000)
                    .sampleFormat("fltp")
                    .sampleRate(96000)
                    .channels(2)
                    .remove(false)
                    .build();

            // Create time interval
            TimeInterval timeInterval = TimeInterval.builder()
                    .start(1000)
                    .duration(6000)
                    .build();

            // Create AIGC metadata
            AIGCMetadata aigcMetadata = AIGCMetadata.builder()
                    .label("1")
                    .contentProducer("test_producer")
                    .produceID("123456")
                    .propagateID("654321")
                    .reservedCode1(base64String("123abc"))
                    .reservedCode2(base64String("456def"))
                    .build();

            TranscodeConfigOptions options = TranscodeConfigOptions.builder()
                    .aigcMetadata(aigcMetadata)
                    .build();

            // Create transcode config
            Transcode transcodeConfig = Transcode.builder()
                    .timeInterval(timeInterval)
                    .container(container)
                    .video(video)
                    .audio(audio)
                    .options(options)
                    .build();

            // Create video convert template
            PutVideoConvertTemplateInput putVideoConvertTemplateInput = PutVideoConvertTemplateInput.builder()
                    .bucket(bucketName)
                    .name("test-video-template")
                    .transcodeConfig(transcodeConfig)
                    .build();

            PutVideoConvertTemplateOutput putVideoConvertTemplateOutput = client.putVideoConvertTemplate(putVideoConvertTemplateInput);
            Assert.assertNotNull(putVideoConvertTemplateOutput);
            Assert.assertEquals(putVideoConvertTemplateOutput.getRequestInfo().getStatusCode(), 200);
            Assert.assertNotNull(putVideoConvertTemplateOutput.getId());

            Consts.LOG.info("Video convert template created with ID: {}", putVideoConvertTemplateOutput);

            GetVideoConvertTemplateInput getVideoConvertTemplateInput = GetVideoConvertTemplateInput.builder()
                    .bucket(bucketName)
                    .id(putVideoConvertTemplateOutput.getId())
                    .build();

            GetVideoConvertTemplateOutput getVideoConvertTemplateOutput = client.getVideoConvertTemplate(getVideoConvertTemplateInput);
            Consts.LOG.info("getVideoConvertTemplateOutput: {}", getVideoConvertTemplateOutput);
            Assert.assertNotNull(getVideoConvertTemplateOutput);
            Assert.assertEquals(getVideoConvertTemplateOutput.getRequestInfo().getStatusCode(), 200);
            Assert.assertEquals(getVideoConvertTemplateOutput.getId(), putVideoConvertTemplateOutput.getId());
            Assert.assertEquals(getVideoConvertTemplateOutput.getName(), "test-video-template");
            Assert.assertEquals(getVideoConvertTemplateOutput.getTag(), "Transcode");
            Transcode transcode = getVideoConvertTemplateOutput.getTranscodeConfig();
            Assert.assertNotNull(transcode);
            // time interval
            Assert.assertEquals(transcode.getTimeInterval().getStart(), timeInterval.getStart());
            Assert.assertEquals(transcode.getTimeInterval().getDuration(), timeInterval.getDuration());

            // container
            Assert.assertEquals(transcode.getContainer().getFormat(), container.getFormat());
            Assert.assertEquals(transcode.getContainer().getClipConfig().getDuration(), clipConfig.getDuration());

            // video
            Assert.assertEquals(transcode.getVideo().getCodec(), video.getCodec());
            Assert.assertEquals(transcode.getVideo().getWidth(), video.getWidth());
            Assert.assertEquals(transcode.getVideo().getHeight(), video.getHeight());
            Assert.assertEquals(transcode.getVideo().getCrf(), video.getCrf());
            Assert.assertEquals(transcode.getVideo().getPixFmt(), video.getPixFmt());
            Assert.assertEquals(transcode.getVideo().getBitRate(), video.getBitRate());
            Assert.assertEquals(transcode.getVideo().getFps(), video.getFps());
            Assert.assertEquals(transcode.getVideo().getRemove(), video.getRemove());

            // audio
            Assert.assertEquals(transcode.getAudio().getCodec(), audio.getCodec());
            Assert.assertEquals(transcode.getAudio().getBitRate(), audio.getBitRate());
            Assert.assertEquals(transcode.getAudio().getSampleFormat(), audio.getSampleFormat());
            Assert.assertEquals(transcode.getAudio().getSampleRate(), audio.getSampleRate());
            Assert.assertEquals(transcode.getAudio().getChannels(), audio.getChannels());
            Assert.assertEquals(transcode.getAudio().getRemove(), audio.getRemove());

            // aigc metadata
            AIGCMetadata aigcMetadataOut = transcode.getOptions().getAigcMetadata();
            // label
            Assert.assertEquals(aigcMetadataOut.getLabel(), aigcMetadata.getLabel());
            // contentProducer
            Assert.assertEquals(aigcMetadataOut.getContentProducer(), aigcMetadata.getContentProducer());
            // produceID
            Assert.assertEquals(aigcMetadataOut.getProduceID(), aigcMetadata.getProduceID());
            // propagateID
            Assert.assertEquals(aigcMetadataOut.getPropagateID(), aigcMetadata.getPropagateID());
            // reservedCode1
            Assert.assertEquals(aigcMetadataOut.getReservedCode1(), aigcMetadata.getReservedCode1());
            // reservedCode2
            Assert.assertEquals(aigcMetadataOut.getReservedCode2(), aigcMetadata.getReservedCode2());

            // list video convert templates
            ListVideoConvertTemplatesInput listVideoConvertTemplatesInput = ListVideoConvertTemplatesInput.builder()
                    .bucket(bucketName)
                    .build();

            ListVideoConvertTemplatesOutput listVideoConvertTemplatesOutput = client.listVideoConvertTemplates(listVideoConvertTemplatesInput);
            Consts.LOG.info("listVideoConvertTemplatesOutput: {}", listVideoConvertTemplatesOutput);
            Assert.assertNotNull(listVideoConvertTemplatesOutput);
            Assert.assertEquals(listVideoConvertTemplatesOutput.getRequestInfo().getStatusCode(), 200);
            Assert.assertNotNull(listVideoConvertTemplatesOutput.getVideoConvertTemplates());
            Assert.assertEquals(listVideoConvertTemplatesOutput.getVideoConvertTemplates().size(), 1);
            VideoConvertTemplate videoConvertTemplate = listVideoConvertTemplatesOutput.getVideoConvertTemplates().get(0);
            Assert.assertEquals(videoConvertTemplate.getId(), putVideoConvertTemplateOutput.getId());
            Assert.assertEquals(videoConvertTemplate.getName(), "test-video-template");
            Assert.assertEquals(videoConvertTemplate.getTag(), "Transcode");

            Transcode transcode2 = videoConvertTemplate.getTranscodeConfig();

            // time interval
            Assert.assertEquals(transcode2.getTimeInterval().getStart(), timeInterval.getStart());
            Assert.assertEquals(transcode2.getTimeInterval().getDuration(), timeInterval.getDuration());

            // container
            Assert.assertEquals(transcode2.getContainer().getFormat(), container.getFormat());
            Assert.assertEquals(transcode2.getContainer().getClipConfig().getDuration(), clipConfig.getDuration());

            // video
            Assert.assertEquals(transcode2.getVideo().getCodec(), video.getCodec());
            Assert.assertEquals(transcode2.getVideo().getWidth(), video.getWidth());
            Assert.assertEquals(transcode2.getVideo().getHeight(), video.getHeight());
            Assert.assertEquals(transcode2.getVideo().getCrf(), video.getCrf());
            Assert.assertEquals(transcode2.getVideo().getPixFmt(), video.getPixFmt());
            Assert.assertEquals(transcode2.getVideo().getBitRate(), video.getBitRate());
            Assert.assertEquals(transcode2.getVideo().getFps(), video.getFps());
            Assert.assertEquals(transcode2.getVideo().getRemove(), video.getRemove());

            // audio
            Assert.assertEquals(transcode2.getAudio().getCodec(), audio.getCodec());
            Assert.assertEquals(transcode2.getAudio().getBitRate(), audio.getBitRate());
            Assert.assertEquals(transcode2.getAudio().getSampleFormat(), audio.getSampleFormat());
            Assert.assertEquals(transcode2.getAudio().getSampleRate(), audio.getSampleRate());
            Assert.assertEquals(transcode2.getAudio().getChannels(), audio.getChannels());
            Assert.assertEquals(transcode2.getAudio().getRemove(), audio.getRemove());

            // aigc metadata
            AIGCMetadata aigcMetadataOut2 = transcode2.getOptions().getAigcMetadata();
            // label
            Assert.assertEquals(aigcMetadataOut2.getLabel(), aigcMetadata.getLabel());
            // contentProducer
            Assert.assertEquals(aigcMetadataOut2.getContentProducer(), aigcMetadata.getContentProducer());
            // produceID
            Assert.assertEquals(aigcMetadataOut2.getProduceID(), aigcMetadata.getProduceID());
            // propagateID
            Assert.assertEquals(aigcMetadataOut2.getPropagateID(), aigcMetadata.getPropagateID());
            // reservedCode1
            Assert.assertEquals(aigcMetadataOut2.getReservedCode1(), aigcMetadata.getReservedCode1());
            // reservedCode2
            Assert.assertEquals(aigcMetadataOut2.getReservedCode2(), aigcMetadata.getReservedCode2());

            DeleteVideoConvertTemplateInput deleteVideoConvertTemplateInput = DeleteVideoConvertTemplateInput.builder()
                    .bucket(bucketName)
                    .id(putVideoConvertTemplateOutput.getId())
                    .build();
            DeleteVideoConvertTemplateOutput deleteVideoConvertTemplateOutput = client.deleteVideoConvertTemplate(deleteVideoConvertTemplateInput);
            Assert.assertNotNull(deleteVideoConvertTemplateOutput);
            Assert.assertEquals(deleteVideoConvertTemplateOutput.getRequestInfo().getStatusCode(), 204);
            Consts.LOG.info("Video convert template deleted with ID: {}", deleteVideoConvertTemplateInput);

            try {
                client.getVideoConvertTemplate(getVideoConvertTemplateInput);
                Assert.fail("Get video convert template should fail after deletion");
            } catch (TosException e) {
                Consts.LOG.info("Get video convert template failed with status err: {}", e.getStatusCode());
                Assert.assertEquals(e.getStatusCode(), 404);
            }
        } catch (TosException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            client.deleteBucket(new DeleteBucketInput().setBucket(bucketName));
        }
    }

    @Test
    public void DifferentContainerFormatTypeTest() {
        String bucketName = "java-sdk-" + System.currentTimeMillis();
        try {
            client.createBucket(new CreateBucketV2Input().setBucket(bucketName));
            List<ContainerFormatType> containerFormatTypes = Arrays.asList(ContainerFormatType.MP4, ContainerFormatType.TS);
            for (ContainerFormatType containerFormatType : containerFormatTypes) {
                Consts.LOG.info("Test container format type: {}", containerFormatType.getValue());
                Container container = Container.builder()
                        .format(containerFormatType)
                        .build();
                Transcode transcode = Transcode.builder()
                        .container(container)
                        .build();
                PutVideoConvertTemplateInput putVideoConvertTemplateInput = PutVideoConvertTemplateInput.builder()
                        .bucket(bucketName)
                        .name("video-convert-template-" + containerFormatType.getValue())
                        .transcodeConfig(transcode)
                        .build();
                PutVideoConvertTemplateOutput putVideoConvertTemplateOutput = client.putVideoConvertTemplate(putVideoConvertTemplateInput);
                Consts.LOG.info("Video convert template created with ID: {}", putVideoConvertTemplateOutput.getId());

                GetVideoConvertTemplateInput getVideoConvertTemplateInput = GetVideoConvertTemplateInput.builder()
                        .bucket(bucketName)
                        .id(putVideoConvertTemplateOutput.getId())
                        .build();
                GetVideoConvertTemplateOutput getVideoConvertTemplateOutput = client.getVideoConvertTemplate(getVideoConvertTemplateInput);
                Assert.assertEquals(getVideoConvertTemplateOutput.getTranscodeConfig().getContainer().getFormat(), containerFormatType);
                Assert.assertEquals(getVideoConvertTemplateOutput.getTranscodeConfig().getContainer().getFormat().getValue(), containerFormatType.getValue());
            }

        } catch (TosException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            client.deleteBucket(new DeleteBucketInput().setBucket(bucketName));
        }

    }

    @Test
    public void VideoConvertJobCrudTest(){
        String bucketName = "java-sdk-" + System.currentTimeMillis();
        String objectKey = "test.mp4";
        String targetBucket = "java-sdk-target" + System.currentTimeMillis();
        try {
            client.createBucket(new CreateBucketV2Input().setBucket(bucketName));
            String data = "test";
            InputStream stream = new ByteArrayInputStream(data.getBytes());
            client.putObject(bucketName, objectKey, stream);
            client.createBucket(new CreateBucketV2Input().setBucket(targetBucket));

            // Create container
            ClipConfig clipConfig = ClipConfig.builder()
                    .duration(3600000)
                    .build();
            Container container = Container.builder()
                    .format(ContainerFormatType.MP4)
                    .clipConfig(clipConfig)
                    .build();

            // Create video config
            Video video = Video.builder()
                    .codec("h264")
                    .width(4096)
                    .height(4096)
                    .crf(23)
                    .pixFmt("yuv420p")
                    .bitRate(50000000)
                    .fps(30)
                    .remove(false)
                    .build();

            // Create audio config
            Audio audio = Audio.builder()
                    .codec("aac")
                    .bitRate(1000000)
                    .sampleFormat("fltp")
                    .sampleRate(96000)
                    .channels(2)
                    .remove(false)
                    .build();

            // Create time interval
            TimeInterval timeInterval = TimeInterval.builder()
                    .start(1000)
                    .duration(6000)
                    .build();

            // Create AIGC metadata
            AIGCMetadata aigcMetadata = AIGCMetadata.builder()
                    .label("1")
                    .contentProducer("test_producer")
                    .produceID("123456")
                    .propagateID("654321")
                    .reservedCode1(base64String("123abc"))
                    .reservedCode2(base64String("456def"))
                    .build();

            TranscodeConfigOptions options = TranscodeConfigOptions.builder()
                    .aigcMetadata(aigcMetadata)
                    .build();

            // Create transcode config
            Transcode transcodeConfig = Transcode.builder()
                    .timeInterval(timeInterval)
                    .container(container)
                    .video(video)
                    .audio(audio)
                    .options(options)
                    .build();

            ConvertJobInput convertJobInput = ConvertJobInput.builder()
                    .object(objectKey)
                    .build();

            ConvertJobOutput convertJobOutput = ConvertJobOutput.builder()
                    .object("target.mp4")
                    .bucket(targetBucket)
                    .region(Consts.region)
                    .build();

            // create job by config
            TranscodeConfig transcodeConfig1 = TranscodeConfig.builder().transcode(transcodeConfig).build();

             CreateVideoConvertJobInput createVideoConvertJobInput = CreateVideoConvertJobInput.builder()
                     .bucket(bucketName)
                     .input(convertJobInput)
                     .output(convertJobOutput)
                     .transcodeConfig(transcodeConfig1)
                     .build();
             CreateVideoConvertJobOutput createVideoConvertJobOutput = client.createVideoConvertJob(createVideoConvertJobInput);
             Assert.assertEquals(createVideoConvertJobOutput.getRequestInfo().getStatusCode(), 200);
             Consts.LOG.info("Video convert job created with ID: {}", createVideoConvertJobOutput);

             // get job by id
             GetVideoConvertJobInput getVideoConvertJobInput = GetVideoConvertJobInput.builder()
                     .bucket(bucketName)
                     .jobId(createVideoConvertJobOutput.getJobId())
                     .build();
             GetVideoConvertJobOutput getVideoConvertJobOutput = client.getVideoConvertJob(getVideoConvertJobInput);
             Consts.LOG.info("Video convert job got: {}", getVideoConvertJobOutput);
             Assert.assertEquals(getVideoConvertJobOutput.getRequestInfo().getStatusCode(), 200);

             Assert.assertEquals(getVideoConvertJobOutput.getInput().getObject(), convertJobInput.getObject());
             Assert.assertEquals(getVideoConvertJobOutput.getOutput().getObject(), convertJobOutput.getObject());
             Assert.assertEquals(getVideoConvertJobOutput.getOutput().getBucket(), convertJobOutput.getBucket());
             Assert.assertEquals(getVideoConvertJobOutput.getOutput().getRegion(), convertJobOutput.getRegion());

             // check time interval
             TimeInterval timeIntervalOutput = getVideoConvertJobOutput.getTranscodeConfig().getTranscode().getTimeInterval();
             Assert.assertEquals(timeIntervalOutput.getStart(), timeInterval.getStart());
             Assert.assertEquals(timeIntervalOutput.getDuration(), timeInterval.getDuration());

             // check aigc metadata
             AIGCMetadata aigcMetadataOutput = getVideoConvertJobOutput.getTranscodeConfig().getTranscode().getOptions().getAigcMetadata();
             Assert.assertEquals(aigcMetadataOutput.getLabel(), aigcMetadata.getLabel());
             Assert.assertEquals(aigcMetadataOutput.getContentProducer(), aigcMetadata.getContentProducer());
             Assert.assertEquals(aigcMetadataOutput.getProduceID(), aigcMetadata.getProduceID());
             Assert.assertEquals(aigcMetadataOutput.getPropagateID(), aigcMetadata.getPropagateID());
             Assert.assertEquals(aigcMetadataOutput.getReservedCode1(), aigcMetadata.getReservedCode1());
             Assert.assertEquals(aigcMetadataOutput.getReservedCode2(), aigcMetadata.getReservedCode2());

             // check container
             Container containerOutput = getVideoConvertJobOutput.getTranscodeConfig().getTranscode().getContainer();
             Assert.assertEquals(containerOutput.getClipConfig().getDuration(), container.getClipConfig().getDuration());
             Assert.assertEquals(containerOutput.getFormat(), container.getFormat());

             // check video
             Video videoOutput = getVideoConvertJobOutput.getTranscodeConfig().getTranscode().getVideo();
             Assert.assertEquals(videoOutput.getCodec(), video.getCodec());
             Assert.assertEquals(videoOutput.getWidth(), video.getWidth());
             Assert.assertEquals(videoOutput.getHeight(), video.getHeight());
             Assert.assertEquals(videoOutput.getCrf(), video.getCrf());
             Assert.assertEquals(videoOutput.getPixFmt(), video.getPixFmt());
             Assert.assertEquals(videoOutput.getBitRate(), video.getBitRate());
             Assert.assertEquals(videoOutput.getFps(), video.getFps());
             Assert.assertEquals(videoOutput.getRemove(), video.getRemove());

             // check audio
             Audio audioOutput = getVideoConvertJobOutput.getTranscodeConfig().getTranscode().getAudio();
             Assert.assertEquals(audioOutput.getCodec(), audio.getCodec());
             Assert.assertEquals(audioOutput.getBitRate(), audio.getBitRate());
             Assert.assertEquals(audioOutput.getSampleFormat(), audio.getSampleFormat());
             Assert.assertEquals(audioOutput.getSampleRate(), audio.getSampleRate());
             Assert.assertEquals(audioOutput.getChannels(), audio.getChannels());
             Assert.assertEquals(audioOutput.getRemove(), audio.getRemove());

             // create job by template
             PutVideoConvertTemplateInput putVideoConvertTemplateInput = PutVideoConvertTemplateInput.builder()
                     .bucket(bucketName)
                     .name("test-template")
                     .transcodeConfig(transcodeConfig)
                     .build();
             PutVideoConvertTemplateOutput putVideoConvertTemplateOutput = client.putVideoConvertTemplate(putVideoConvertTemplateInput);
             TranscodeConfig transcodeConfig2 = TranscodeConfig.builder()
                     .templateID(putVideoConvertTemplateOutput.getId())
                     .transcode(new Transcode())
                     .build();
             CreateVideoConvertJobInput createVideoConvertJobInput2 = CreateVideoConvertJobInput.builder()
                     .bucket(bucketName)
                     .transcodeConfig(transcodeConfig2)
                     .input(convertJobInput)
                     .output(convertJobOutput)
                     .callback("http://example.com/callback")
                     .build();
             CreateVideoConvertJobOutput createJobOutput = client.createVideoConvertJob(createVideoConvertJobInput2);
             GetVideoConvertJobInput getJobInput = GetVideoConvertJobInput.builder()
                     .bucket(bucketName)
                     .jobId(createJobOutput.getJobId())
                     .build();
             GetVideoConvertJobOutput getJobOutput = client.getVideoConvertJob(getJobInput);
             Assert.assertEquals(getJobOutput.getTranscodeConfig().getTemplateID(), putVideoConvertTemplateOutput.getId());
             Consts.LOG.info("callback url: {}", getJobOutput.getCallback());
             Assert.assertEquals(getJobOutput.getCallback(), "http://example.com/callback");


             // check different container format
            List<ContainerFormatType> containerFormatTypes = Arrays.asList(ContainerFormatType.MP4, ContainerFormatType.TS);
            for (ContainerFormatType formatType: containerFormatTypes) {
                Consts.LOG.info("check container format: {}", formatType.getValue());
                Container container2 = Container.builder()
                        .format(formatType)
                        .build();

                String objectKeyInFormat = "test." + formatType.getValue();
                client.putObject(bucketName, objectKeyInFormat, stream);

                ConvertJobInput jobInput = ConvertJobInput.builder()
                        .object(objectKeyInFormat)
                        .build();

                ConvertJobOutput jobOutput = ConvertJobOutput.builder()
                        .object("target." + formatType.getValue())
                        .bucket(targetBucket)
                        .region(Consts.region)
                        .build();

                Transcode transcodeFormat = Transcode.builder()
                        .container(container2)
                        .build();

                TranscodeConfig transcodeConfigFormat = TranscodeConfig.builder()
                        .transcode(transcodeFormat)
                        .build();

                CreateVideoConvertJobInput createJobInputFormat = CreateVideoConvertJobInput.builder()
                        .bucket(bucketName)
                        .transcodeConfig(transcodeConfigFormat)
                        .input(jobInput)
                        .output(jobOutput)
                        .build();

                CreateVideoConvertJobOutput createJobOutputFormat = client.createVideoConvertJob(createJobInputFormat);

                GetVideoConvertJobInput getJobInputFormat = GetVideoConvertJobInput.builder()
                        .bucket(bucketName)
                        .jobId(createJobOutputFormat.getJobId())
                        .build();

                GetVideoConvertJobOutput getJobOutputFormat = client.getVideoConvertJob(getJobInputFormat);
                Assert.assertEquals(getJobOutputFormat.getTranscodeConfig().getTranscode().getContainer().getFormat(), formatType);
                Assert.assertEquals(getJobOutputFormat.getTranscodeConfig().getTranscode().getContainer().getFormat().getValue(), formatType.getValue());
            }


        } catch (TosException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            deleteBucket(bucketName);
            deleteBucket(targetBucket);
        }
    }

    @Test
    public void AudioConvertJobCrudTest(){
        String bucketName = "java-sdk-" + System.currentTimeMillis();
        String objectKey = "test.mp3";
        String targetBucket = "java-sdk-target" + System.currentTimeMillis();
        try {
            client.createBucket(new CreateBucketV2Input().setBucket(bucketName));
            String data = "test";
            InputStream stream = new ByteArrayInputStream(data.getBytes());
            client.putObject(bucketName, objectKey, stream);
            client.createBucket(new CreateBucketV2Input().setBucket(targetBucket));

            // 创建音频转换配置
            TimeInterval timeInterval = TimeInterval.builder()
                    .start(1000)
                    .duration(10000)
                    .build();
            AudioConvertConfig audioConvertConfig = AudioConvertConfig.builder()
                    .timeInterval(timeInterval)
                    .containerFormat(AudioContainerFormatType.MP3)
                    .bitRate(8000)
                    .bitRateOpt(1)
                    .sampleFormat("fltp")
                    .sampleRate(11025)
                    .channels(2)
                    .build();

            // 任务输入输出
            ConvertJobInput convertJobInput = ConvertJobInput.builder()
                    .object(objectKey)
                    .build();
            ConvertJobOutput convertJobOutput = ConvertJobOutput.builder()
                    .object("target.mp3")
                    .bucket(targetBucket)
                    .region(Consts.region)
                    .build();

            // 创建音频转换任务（直接配置）
            CreateAudioConvertJobInput createAudioConvertJobInput = CreateAudioConvertJobInput.builder()
                    .bucket(bucketName)
                    .input(convertJobInput)
                    .audioConvertConfig(audioConvertConfig)
                    .output(convertJobOutput)
                    .build();
            CreateAudioConvertJobOutput createAudioConvertJobOutput = client.createAudioConvertJob(createAudioConvertJobInput);
            Assert.assertEquals(createAudioConvertJobOutput.getRequestInfo().getStatusCode(), 200);

            // 根据 JobID 查询任务
            GetAudioConvertJobInput getAudioConvertJobInput = GetAudioConvertJobInput.builder()
                    .bucket(bucketName)
                    .jobId(createAudioConvertJobOutput.getJobId())
                    .build();
            GetAudioConvertJobOutput getAudioConvertJobOutput = client.getAudioConvertJob(getAudioConvertJobInput);
            Assert.assertEquals(getAudioConvertJobOutput.getRequestInfo().getStatusCode(), 200);

            // 校验输入输出
            Assert.assertEquals(getAudioConvertJobOutput.getInput().getObject(), convertJobInput.getObject());
            Assert.assertEquals(getAudioConvertJobOutput.getOutput().getObject(), convertJobOutput.getObject());
            Assert.assertEquals(getAudioConvertJobOutput.getOutput().getBucket(), convertJobOutput.getBucket());
            Assert.assertEquals(getAudioConvertJobOutput.getOutput().getRegion(), convertJobOutput.getRegion());

            // 校验时间区间
            TimeInterval timeOut = getAudioConvertJobOutput.getAudioConvertConfig().getTimeInterval();
            Assert.assertEquals(timeOut.getStart(), timeInterval.getStart());
            Assert.assertEquals(timeOut.getDuration(), timeInterval.getDuration());

            // 校验音频配置
            AudioConvertConfig cfgOut = getAudioConvertJobOutput.getAudioConvertConfig();
            Assert.assertEquals(cfgOut.getContainerFormat(), audioConvertConfig.getContainerFormat());
            Assert.assertEquals(cfgOut.getBitRate(), audioConvertConfig.getBitRate());
            Assert.assertEquals(cfgOut.getBitRateOpt(), audioConvertConfig.getBitRateOpt());
            Assert.assertEquals(cfgOut.getSampleFormat(), audioConvertConfig.getSampleFormat());
            Assert.assertEquals(cfgOut.getSampleRate(), audioConvertConfig.getSampleRate());
            Assert.assertEquals(cfgOut.getChannels(), audioConvertConfig.getChannels());

            // 不同容器格式校验
            List<AudioContainerFormatType> containerFormatTypes = Arrays.asList(
                    AudioContainerFormatType.WAV,
                    AudioContainerFormatType.MP3,
                    AudioContainerFormatType.AAC,
                    AudioContainerFormatType.FLAC,
                    AudioContainerFormatType.OGA,
                    AudioContainerFormatType.AC3,
                    AudioContainerFormatType.OPUS
            );
            for (AudioContainerFormatType fmt : containerFormatTypes) {
                Consts.LOG.info("check audio container format: {}", fmt.getValue());
                String objectKeyInFormat = "test." + fmt.getValue();
                client.putObject(bucketName, objectKeyInFormat, new ByteArrayInputStream(data.getBytes()));

                ConvertJobInput jobInput = ConvertJobInput.builder()
                        .object(objectKeyInFormat)
                        .build();
                ConvertJobOutput jobOutput = ConvertJobOutput.builder()
                        .object("target." + fmt.getValue())
                        .bucket(targetBucket)
                        .region(Consts.region)
                        .build();

                AudioConvertConfig fmtConfig = AudioConvertConfig.builder()
                        .containerFormat(fmt)
                        .build();

                CreateAudioConvertJobInput jobInputFmt = CreateAudioConvertJobInput.builder()
                        .bucket(bucketName)
                        .input(jobInput)
                        .audioConvertConfig(fmtConfig)
                        .output(jobOutput)
                        .build();

                CreateAudioConvertJobOutput jobOutFmt = client.createAudioConvertJob(jobInputFmt);
                GetAudioConvertJobInput getJobFmt = GetAudioConvertJobInput.builder()
                        .bucket(bucketName)
                        .jobId(jobOutFmt.getJobId())
                        .build();
                GetAudioConvertJobOutput gotFmt = client.getAudioConvertJob(getJobFmt);
                Assert.assertEquals(gotFmt.getAudioConvertConfig().getContainerFormat(), fmt);
                Assert.assertEquals(gotFmt.getAudioConvertConfig().getContainerFormat().getValue(), fmt.getValue());
            }

        } catch (TosException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            deleteBucket(bucketName);
            deleteBucket(targetBucket);
        }
    }

     @Test
    public void ConvertWorkflowTest() {
        String bucketName = "java-sdk-convert-workflow-" + System.currentTimeMillis();
        try {
            client.createBucket(new CreateBucketV2Input().setBucket(bucketName));
            // 1. Create audio convert template
            TimeInterval timeInterval = TimeInterval.builder()
                    .start(1000)
                    .duration(60000)
                    .build();
            
            AudioConvertConfig audioConvertConfig = AudioConvertConfig.builder()
                    .timeInterval(timeInterval)
                    .containerFormat(AudioContainerFormatType.MP3)
                    .bitRate(8000)
                    .bitRateOpt(1)
                    .sampleFormat("fltp")
                    .sampleRate(11025)
                    .channels(2)
                    .build();
            PutAudioConvertTemplateInput putAudioInput = PutAudioConvertTemplateInput.builder()
                    .bucket(bucketName)
                    .name("test-audio-template")
                    .audioConvertConfig(audioConvertConfig)
                    .build();
            PutAudioConvertTemplateOutput putAudioOutput = client.putAudioConvertTemplate(putAudioInput);
            Assert.assertEquals(putAudioOutput.getRequestInfo().getStatusCode(), 200);
            Assert.assertNotNull(putAudioOutput.getId());
            // 2. Create video convert template
            Container container = Container.builder()
                    .format(ContainerFormatType.MP4)
                    .clipConfig(ClipConfig.builder().duration(10).build())
                    .build();
            Video video = Video.builder()
                    .codec("h264")
                    .width(1920)
                    .height(1080)
                    .crf(23)
                    .pixFmt("yuv420p")
                    .bitRate(5000000)
                    .fps(30)
                    .remove(false)
                    .build();
            Audio audio = Audio.builder()
                    .codec("aac")
                    .bitRate(128000)
                    .sampleFormat("fltp")
                    .sampleRate(44100)
                    .channels(2)
                    .remove(false)
                    .build();
            Transcode transcode = Transcode.builder()
                    .timeInterval(timeInterval)
                    .container(container)
                    .video(video)
                    .audio(audio)
                    .options(TranscodeConfigOptions.builder()
                            .aigcMetadata(AIGCMetadata.builder()
                                    .label("1")
                                    .contentProducer("test_producer")
                                    .produceID("123456")
                                    .propagateID("654321")
                                    .reservedCode1(base64String("123"))
                                    .reservedCode2(base64String("456"))
                                    .build())
                            .build())
                    .build();
            PutVideoConvertTemplateInput putVideoInput = PutVideoConvertTemplateInput.builder()
                    .bucket(bucketName)
                    .name("test-video-template")
                    .transcodeConfig(transcode)
                    .build();
            PutVideoConvertTemplateOutput putVideoOutput = client.putVideoConvertTemplate(putVideoInput);
            Assert.assertEquals(putVideoOutput.getRequestInfo().getStatusCode(), 200);
            Assert.assertNotNull(putVideoOutput.getId());
            // 3. Create convert workflow
            String ruleId = "test-rule-" + System.currentTimeMillis();
            ConvertJobOutput audioOutput = ConvertJobOutput.builder()
                    .region(Consts.region)
                    .bucket(bucketName)
                    .object("output/audio.mp3")
                    .build();
            ConvertJobOutput videoOutput = ConvertJobOutput.builder()
                    .region(Consts.region)
                    .bucket(bucketName)
                    .object("output/video.mp4")
                    .build();
            OperationsAudioTranscode audioTranscode = OperationsAudioTranscode.builder()
                    .operationID("audio-transcode")
                    .templateID(putAudioOutput.getId())
                    .output(audioOutput)
                    .build();
            OperationsTranscode videoTranscode = OperationsTranscode.builder()
                    .operationID("transcode")
                    .templateID(putVideoOutput.getId())
                    .output(videoOutput)
                    .build();
            WorkflowOperations operations = WorkflowOperations.builder()
                    .audioTranscode(Arrays.asList(audioTranscode))
                    .transcode(Arrays.asList(videoTranscode))
                    .build();
            WorkflowExtFilter extFilter = WorkflowExtFilter.builder()
                    .audioExts(Arrays.asList("mp3"))
                    .videoExts(Arrays.asList("mp4"))
                    .build();
            ConvertWorkflowRule rule = ConvertWorkflowRule.builder()
                    .id(ruleId)
                    .enabled(true)
                    .prefix("videos/")
                    .extFilter(extFilter)
                    .topology(Arrays.asList(Arrays.asList("transcode"), Arrays.asList("audio-transcode")))
                    .operations(operations)
                    .build();
            PutConvertWorkflowInput putWorkflowInput = PutConvertWorkflowInput.builder()
                    .bucket(bucketName)
                    .rules(Arrays.asList(rule))
                    .build();
            // 4. Test put convert workflow
            PutConvertWorkflowOutput putWorkflowOutput = client.putConvertWorkflow(putWorkflowInput);
            Assert.assertEquals(putWorkflowOutput.getRequestInfo().getStatusCode(), 200);
            Assert.assertNotNull(putWorkflowOutput.getRequestInfo());
            Consts.LOG.info("ConvertWorkflow test completed successfully");

            GetConvertWorkflowInput getWorkflowInput = GetConvertWorkflowInput.builder()
                    .bucket(bucketName)
                    .build();
            GetConvertWorkflowOutput getWorkflowOutput = client.getConvertWorkflow(getWorkflowInput);
            Consts.LOG.info("GetConvertWorkflow test completed successfully {}", getWorkflowOutput);
            Assert.assertEquals(getWorkflowOutput.getRequestInfo().getStatusCode(), 200);
            Assert.assertNotNull(getWorkflowOutput.getRules());
            Assert.assertEquals(getWorkflowOutput.getRules().size(), 1);
            ConvertWorkflowRule ruleOut = getWorkflowOutput.getRules().get(0);
            Assert.assertEquals(ruleOut.getId(), rule.getId());
            Assert.assertEquals(ruleOut.getEnabled(), rule.getEnabled());
            Assert.assertEquals(ruleOut.getPrefix(), rule.getPrefix());
            Assert.assertEquals(ruleOut.getExtFilter().getAudioExts(), rule.getExtFilter().getAudioExts());
            Assert.assertEquals(ruleOut.getExtFilter().getVideoExts(), rule.getExtFilter().getVideoExts());
            Assert.assertEquals(ruleOut.getTopology(), rule.getTopology());

            // check transcode
            OperationsTranscode videoTranscodeOut = ruleOut.getOperations().getTranscode().get(0);
            Assert.assertEquals(videoTranscodeOut.getOperationID(), videoTranscode.getOperationID());
            Assert.assertEquals(videoTranscodeOut.getTemplateID(), videoTranscode.getTemplateID());
            Assert.assertEquals(videoTranscodeOut.getOutput().getRegion(), videoTranscode.getOutput().getRegion());
            Assert.assertEquals(videoTranscodeOut.getOutput().getBucket(), videoTranscode.getOutput().getBucket());
            Assert.assertEquals(videoTranscodeOut.getOutput().getObject(), videoTranscode.getOutput().getObject());

            // check audio transcode
            OperationsAudioTranscode audioTranscodeOut = ruleOut.getOperations().getAudioTranscode().get(0);
            Assert.assertEquals(audioTranscodeOut.getOperationID(), audioTranscode.getOperationID());
            Assert.assertEquals(audioTranscodeOut.getTemplateID(), audioTranscode.getTemplateID());
            Assert.assertEquals(audioTranscodeOut.getOutput().getRegion(), audioTranscode.getOutput().getRegion());
            Assert.assertEquals(audioTranscodeOut.getOutput().getBucket(), audioTranscode.getOutput().getBucket());
            Assert.assertEquals(audioTranscodeOut.getOutput().getObject(), audioTranscode.getOutput().getObject());

            DeleteConvertWorkflowInput deleteWorkflowInput = DeleteConvertWorkflowInput.builder()
                    .bucket(bucketName)
                    .build();

            DeleteConvertWorkflowOutput deleteWorkflowOutput = client.deleteConvertWorkflow(deleteWorkflowInput);
            Assert.assertEquals(deleteWorkflowOutput.getRequestInfo().getStatusCode(), 204);
            Assert.assertNotNull(deleteWorkflowOutput.getRequestInfo().getRequestId());

            try {
                GetConvertWorkflowInput getInputAfterDelete = new GetConvertWorkflowInput().setBucket(bucketName);
                client.getConvertWorkflow(getInputAfterDelete);
                Assert.fail("Get convert workflow after delete should fail");
            } catch (TosException e) {
                Consts.LOG.info("Get convert workflow after delete failed, status code: {}", e.getStatusCode());
                Assert.assertEquals(e.getStatusCode(), 404);
            }
        } catch (TosException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            deleteBucket(bucketName);
        }
    }
}
