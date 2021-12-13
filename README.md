# Volcengine TOS SDK for Java
The TOS Java SDK enables Java developers to easily work with TOS(Tinder Object Storage) service in the volcengine.
You can get started in minutes using Maven or by downloading a single zip file.
This document will show developers some basic examples about TOS bucket and object operation.
More details can be found in [xxx]()

## Install
### Requirements
- Java 1.8 or later
- Maven
### Use Maven
```xml
<dependency>
    <groupId>com.volcengine</groupId>
    <artifactId>ve-tos-java-sdk</artifactId>
    <version>0.2.2</version>
</dependency>
```

## Get Started
This section introduces how to create a bucket, upload/download/delete an object in TOS service through our SDK.
### Init a TOSClient
You can interact with TOS service after initiating a TOSClient instance.
The accesskey and secretkey of your account, endpoint and region are required as params.

```java
String endpoint = "your endpoint";
String region = "your region";
String accessKey = "Your Access Key";
String secretKey = "Your Secret Key";

TOSClient client = new TOSClient(endpoint, ClientOptions.withRegion(region),
        ClientOptions.withCredentials(new StaticCredentials(accessKey, secretKey)));
```

### Creat a bucket
The bucket is a kind of unique namespace in TOS, which is a container to store data.
This example shows you how to create a bucket.
```java
CreateBucketOutput createdBucket = client.createBucket(new CreateBucketInput(bucketName));
LOG.info("bucket created: {}", createdBucket);

HeadBucketOutput output = client.headBucket(bucketName);
LOG.info("bucket region {}", output.getRegion());

ListBucketsOutput output = client.listBuckets(new ListBucketsInput());
LOG.info("list {} bukets.", output.getBuckets().length);
```

### PutObject
You can put your file as an object into your own bucket.

```java
// data is what you want to upload, wrap it as an inputStream.
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

### GetObject
You can download objects in the TOS bucket through our SDK.

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

### DeleteObject
Your can delete your objects in the bucket.

```java
String key = "object-curd-"+System.currentTimeMillis();
String bucket = "your bucket name";
try{
    DeleteBucketOutput ret = client.deleteObject(bucket, key);
} catch (TosException e) {
    LOG.error(e.toString());
}
```

## License
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)