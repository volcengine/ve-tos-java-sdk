# 火山引擎 TOS Android SDK
## 简介
TOS Android SDK 为 Android 开发者提供了访问火山引擎对象存储服务TOS（Tinder Object Storage）的系列接口。本文档将给出TOS桶和对象的基本操作代码，供开发者参考，具体API文档请参考[xxx]()

## 安装
### 最低依赖
- Java 1.8 或更高
### Gradle 引入
```xml
dependencies {
    implementation 'com.volcengine:ve-tos-android-sdk:2.2.0'
}
```
### 其他配置
本 Android SDK 基于 Java8 构建，支持的 minSDK 版本为 API 21。对于 minSDK 版本低于 API 26（不包括26）的 Android 应用，需额外进行如下设置才能正常运行。

#### 相关依赖
- Gradle 6.1.1 或更高版本
- Android Gradle 插件 4.0.0 或更高版本

在您的安卓应用模块（通常在 app 目录下）的 build.gradle 文件中，添加如下代码

```groovy
android {
    compileOptions {
        coreLibraryDesugaringEnabled true
        
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.1.5'
}
```

具体可参考 https://developer.android.com/studio/write/java8-support

## 快速入门
本节介绍，如何通过TOS Android SDK来完成常见的操作，如创建桶，上传、下载和删除对象等。
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