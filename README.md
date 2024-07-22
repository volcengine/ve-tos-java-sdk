# Volcengine TOS SDK for Java
The TOS Java SDK enables Java developers to easily work with TOS(Tinder Object Storage) service in the volcengine.
You can get started in minutes using Maven or by downloading a single zip file.
This document will show developers some basic examples about TOS bucket and object operation.
More details can be found in [https://www.volcengine.com/docs/6349/79895]()

## Install
### Requirements
- Java 1.8 or later
- Maven
### Use Maven
```xml
<dependency>
    <groupId>com.volcengine</groupId>
    <artifactId>ve-tos-java-sdk</artifactId>
    <version>2.8.2</version>
</dependency>
```

## Get Started
This section introduces how to create a bucket, upload/download/delete an object in TOS service through our SDK.
### Init a TOSV2Client
You can interact with TOS service after initiating a TOSV2Client instance.
The accesskey and secretkey of your account, endpoint and region are required as params.

```java
String region = "region to access";
String endpoint = "endpoint to access";
String accessKey = "your access key";
String secretKey = "your secret key";

TOSV2 client = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
```

### Creat a bucket
The bucket is a kind of unique namespace in TOS, which is a container to store data.
This example shows you how to create a bucket.
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

### PutObject
You can put your file as an object into your own bucket.

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

### GetObject
You can download objects in the TOS bucket through our SDK.

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
        // return null if key not exist
        System.out.println("key " + key + " not found");
    }
} catch (TosException | IOException e) {
    System.out.println("getObject error");
    e.printStackTrace();
}
```

### DeleteObject
Your can delete your objects in the bucket.

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

## License
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)