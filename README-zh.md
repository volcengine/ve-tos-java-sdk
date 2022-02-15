# 火山引擎 TOS Java SDK
## 简介
TOS Java SDK为Java开发者提供了访问火山引擎对象存储服务TOS（Tinder Object Storage）的系列接口。您可以通过Maven工具快速引入。本文档将给出TOS桶和对象的基本操作代码，供开发者参考，具体API文档请参考[火山引擎TOS Java SDK参考手册](https://www.volcengine.com/docs/6349/79895)

## 安装
### 最低依赖
- Java 1.8或更高
- Maven
### Maven引入
```xml
<dependency>
    <groupId>com.volcengine</groupId>
    <artifactId>ve-tos-java-sdk</artifactId>
    <version>0.2.3</version>
</dependency>
```

## 快速入门
本节介绍，如何通过TOS Java SDK来完成常见的操作，如创建桶，上传、下载和删除对象等。
### 初始化TOS客户端
初始化TOSClient实例之后，才可以向TOS服务发送HTTP/HTTPS请求。
初始化客户端时，需要带上accesskey，secretkey，endpoint和region。初始化代码如下：

```java
String endpoint = "your endpoint";
String region = "your region";
String accessKey = "Your Access Key";
String secretKey = "Your Secret Key";
TOSClient client = new TOSClient(endpoint, ClientOptions.withRegion(region),
        ClientOptions.withCredentials(new StaticCredentials(accessKey, secretKey)));
```

### 创建桶
桶是TOS的全局唯一的命名空间，相当于数据的容器，用来储存对象数据。如下代码展示如何创建一个新桶：

```java
CreateBucketOutput createdBucket = client.createBucket(new CreateBucketInput(bucketName));
LOG.info("bucket created: {}", createdBucket);

HeadBucketOutput output = client.headBucket(bucketName);
LOG.info("bucket region {}", output.getRegion());

ListBucketsOutput output = client.listBuckets(new ListBucketsInput());
LOG.info("list {} bukets.", output.getBuckets().length);
```

### 上传对象
新建桶成功后，可以往桶中上传对象，如下展示如何上传一个对象到已创建的桶中：

```java
// 需要上传的对象数据，以InputStream的形式上传
String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
InputStream stream = new ByteArrayInputStream(data.getBytes());
String key = "object-curd-"+System.currentTimeMillis();
String bucket = "your bucket name";
try{
    PutObjectOutput put = client.putObject(bucket, key, stream);
} catch (TosException | IOException e) {
    LOG.error(e.toString());
}
```

### 下载对象
如下展示如何从桶中下载一个已经存在的对象：

```java
String bucket = "your bucket name";
String key = "your object name";

FileOutputStream writer = null;
try (GetObjectOutput object = client.getObject(bucket, key)) {
    if (object != null) {
        writer = new FileOutputStream("path to store file");
        int once, total = 0;
        byte[] buffer = new byte[4096];
        InputStream inputStream = object.getContent();
        while ((once = inputStream.read(buffer)) > 0) {
            total += once;
            writer.write(buffer, 0, once);
        }
        assertEquals(total, object.getObjectMeta().getContentLength());
        LOG.info("object's size {}, meta {}", total, object.getObjectMeta());
    } else {
        // key不存在返回null
        LOG.info("key {} not found", key);
        }
} catch (TosException | IOException e) {
    LOG.error("getObject error", e);
} finally {
    if (writer != null){
        writer.flush();
        writer.close();
    }
}
```

### 删除对象
如下展示如何从桶中删除一个已经存在的对象：

```java
String key = "object-curd-"+System.currentTimeMillis();
String bucket = "your bucket name";
try{
    DeleteBucketOutput ret = client.deleteObject(bucket, key);
} catch (TosException e) {
    LOG.error(e.toString());
}
```

## 开源许可
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)