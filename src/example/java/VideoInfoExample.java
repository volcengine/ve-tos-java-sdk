package example.java;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.object.GetObjectV2Input;
import com.volcengine.tos.model.object.GetObjectV2Output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class VideoInfoExample {
    static class VideoInfo {
        @JsonProperty("streams")
        List<Stream> streams;
        @JsonProperty("format")
        Format format;
    }

    static class Format {
        @JsonProperty("nb_streams")
        int NbStreams;
        @JsonProperty("nb_programs")
        int NbPrograms;
        @JsonProperty("format_name")
        String FormatName;
        @JsonProperty("format_long_name")
        String FormatLongName;
        @JsonProperty("start_time")
        String StartTime;
        @JsonProperty("duration")
        String Duration;
        @JsonProperty("size")
        String Size;
        @JsonProperty("bit_rate")
        String BitRate;
        @JsonProperty("probe_score")
        int ProbeScore;
        @JsonProperty("tags")
        FormatTags tags;
    }

    static class FormatTags {
        @JsonProperty("major_brand")
        String MajorBrand;
        @JsonProperty("minor_version")
        String MinorVersion;
        @JsonProperty("compatible_brands")
        String CompatibleBrands;
        @JsonProperty("encoder")
        String Encoder;
    }

    static class Stream {
        @JsonProperty("index")
        int Index;
        @JsonProperty("codec_name")
        String CodecName;
        @JsonProperty("codec_long_name")
        String CodecLongName;
        @JsonProperty("profile")
        String Profile;
        @JsonProperty("codec_type")
        String CodecType;
        @JsonProperty("codec_tag_string")
        String CodecTagString;
        @JsonProperty("codec_tag")
        String CodecTag;
        @JsonProperty("width")
        int Width;
        @JsonProperty("height")
        int Height;
        @JsonProperty("coded_width")
        int CodedWidth;
        @JsonProperty("coded_height")
        int CodedHeight;
        @JsonProperty("sample_aspect_ratio")
        String SampleAspectRatio;
        @JsonProperty("display_aspect_ratio")
        String DisplayAspectRatio;
        @JsonProperty("pix_fmt")
        String PixFmt;
        @JsonProperty("level")
        int Level;
        @JsonProperty("chroma_location")
        String ChromaLocation;
        @JsonProperty("refs")
        int Refs;
        @JsonProperty("is_avc")
        String IsAvc;
        @JsonProperty("nal_length_size")
        String NalLengthSize;
        @JsonProperty("r_frame_rate")
        String RFrameRate;
        @JsonProperty("avg_frame_rate")
        String AvgFrameRate;
        @JsonProperty("time_base")
        String TimeBase;
        @JsonProperty("start_pts")
        int StartPts;
        @JsonProperty("start_time")
        String StartTime;
        @JsonProperty("duration_ts")
        int DurationTs;
        @JsonProperty("duration")
        String Duration;
        @JsonProperty("bit_rate")
        String BitRate;
        @JsonProperty("bits_per_raw_sample")
        String BitsPerRawSample;
        @JsonProperty("nb_frames")
        String NbFrames;
        @JsonProperty("extradata_size")
        int ExtradataSize;
        @JsonProperty("disposition")
        Disposition disposition;
        @JsonProperty("tags")
        Tags tags;
        @JsonProperty("sample_fmt")
        String SampleFmt;
        @JsonProperty("sample_rate")
        String SampleRate;
        @JsonProperty("channels")
        int Channels;
        @JsonProperty("channel_layout")
        String ChannelLayout;
    }

    static class Disposition {
        @JsonProperty("default")
        int Default;
        @JsonProperty("dub")
        int Dub;
        @JsonProperty("original")
        int Original;
        @JsonProperty("comment")
        int Comment;
        @JsonProperty("lyrics")
        int Lyrics;
        @JsonProperty("karaoke")
        int Karaoke;
        @JsonProperty("forced")
        int Forced;
        @JsonProperty("hearing_impaired")
        int HearingImpaired;
        @JsonProperty("visual_impaired")
        int VisualImpaired;
        @JsonProperty("clean_effects")
        int CleanEffects;
        @JsonProperty("attached_pic")
        int AttachedPic;
        @JsonProperty("timed_thumbnails")
        int TimedThumbnails;
        @JsonProperty("captions")
        int Captions;
        @JsonProperty("descriptions")
        int Descriptions;
        @JsonProperty("metadata")
        int Metadata;
        @JsonProperty("dependent")
        int Dependent;
        @JsonProperty("still_image")
        int StillImage;
    }

    static class Tags {
        @JsonProperty("language")
        String Language;
        @JsonProperty("handler_name")
        String HandlerName;
        @JsonProperty("vendor_id")
        String VendorId;
    }

    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 需要确保对象已存在
        String objectKey = "video.mp4";
        String style = "video/info"; // 获取图片元信息

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
        try {
            GetObjectV2Input input = new GetObjectV2Input().setBucket(bucketName).setKey(objectKey).setProcess(style);
            // 以下代码展示如何将转码后的图片数据下载到本地文件
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
                 GetObjectV2Output output = tos.getObject(input)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = output.getContent().read(buffer)) != -1) {
                    stream.write(buffer, 0, length);
                }
                String respBody = stream.toString("UTF-8");
                System.out.println("Get video info success, video info is " + respBody);
                VideoInfo info = TosUtils.getJsonMapper().readValue(respBody, new TypeReference<VideoInfo>() {
                });
                System.out.println("Video info stream length:" + info.streams.size());
            } catch (JacksonException e) {
                System.out.println("parse response data failed");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("read response data failed");
                e.printStackTrace();
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("get video info failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("get video info failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("get video info failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}
