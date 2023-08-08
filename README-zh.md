# 火山引擎 TOS Java SDK
## 简介
TOS Java SDK为Java开发者提供了访问火山引擎对象存储服务TOS（Tinder Object Storage）的系列接口。您可以通过Maven工具快速引入。本文档将给出TOS桶和对象的基本操作代码，供开发者参考，具体API文档请参考[https://www.volcengine.com/docs/6349/79895]()

## 安装
### 最低依赖
- Java 1.8或更高
- Maven
### Maven引入
```xml
<dependency>
    <groupId>com.volcengine</groupId>
    <artifactId>ve-tos-java-sdk</artifactId>
    <version>2.6.1</version>
</dependency>
```

## 快速入门
本节介绍，如何通过TOS Java SDK来完成常见的操作，如创建桶，上传、下载和删除对象等。
### 初始化TOSV2客户端
初始化TOSV2Client实例之后，才可以向TOS服务发送HTTP/HTTPS请求。
初始化客户端时，需要带上accesskey，secretkey，endpoint和region。初始化代码如下：

```java
String region = "region to access";
String endpoint = "endpoint to access";
String accessKey = "your access key";
String secretKey = "your secret key";

TOSV2 client = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
```

### 创建桶
桶是TOS的全局唯一的命名空间，相当于数据的容器，用来储存对象数据。如下代码展示如何创建一个新桶：

```java
String region = "region to access";
String endpoint = "endpoint to access";
String accessKey = "your access key";
String secretKey = "your secret key";
String bucket = "your bucket name";

TOSV2 client = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

CreateBucketV2Input input = new CreateBucketV2Input().setBucket(bucket);
try{
    CreateBucketV2Output output = client.createBucket(input);
    System.out.println("Created bucket location is " + output.getLocation());
} catch (TosException e) {
    System.out.println("Create bucket failed");
    e.printStackTrace();
}
```

### 上传对象
新建桶成功后，可以往桶中上传对象，如下展示如何上传一个对象到已创建的桶中：

```java
String region = "region to access";
String endpoint = "endpoint to access";
String accessKey = "your access key";
String secretKey = "your secret key";
String bucket = "your bucket name";

TOSV2 client = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
InputStream stream = new ByteArrayInputStream(data.getBytes());
String key = "object-crud-"+System.currentTimeMillis();

try{
    PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucket).setKey(key);
    PutObjectInput input = new PutObjectInput().setPutObjectBasicInput(basicInput).setContent(stream);
    PutObjectOutput output = client.putObject(input);
    System.out.println("Put object success, the object's etag is " + output.getEtag());
} catch (TosException e) {
    System.out.println("Put object failed");
    e.printStackTrace();
}
```

### 下载对象
如下展示如何从桶中下载一个已经存在的对象到本地文件：

```java
String region = "region to access";
String endpoint = "endpoint to access";
String accessKey = "your access key";
String secretKey = "your secret key";
String bucket = "your bucket name";
String key = "your key name";
String filePath = "your local file name to store downloaded file"; // eg. "/home/user/aaa.txt"

TOSV2 client = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

GetObjectV2Input input = new GetObjectV2Input().setBucket(bucket).setKey(key);
try (GetObjectV2Output object = client.getObject(input);
        FileOutputStream writer = new FileOutputStream(filePath)) {
    if (object.getContent() != null) {
        int once, total = 0;
        byte[] buffer = new byte[4096];
        InputStream inputStream = object.getContent();
        while ((once = inputStream.read(buffer)) > 0) {
            total += once;
            writer.write(buffer, 0, once);
        }
        System.out.println("object's size is " + total + " bytes");
    } else {
        // key不存在返回null
        System.out.println("key " + key + " not found");
    }
} catch (TosException | IOException e) {
    System.out.println("getObject error");
    e.printStackTrace();
}
```

### 删除对象
如下展示如何从桶中删除一个已经存在的对象：

```java
String region = "region to access";
String endpoint = "endpoint to access";
String accessKey = "your access key";
String secretKey = "your secret key";
String bucket = "your bucket name";
String key = "your key name";

TOSV2 client = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

DeleteObjectInput input = new DeleteObjectInput().setBucket(bucket).setKey(key);
try {
    DeleteObjectOutput output = client.deleteObject(input);
    System.out.println("Delete success, " + output);
} catch (TosException e) {
    System.out.println("Delete failed");
    e.printStackTrace();
}
```

## 开源许可
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)