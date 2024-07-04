## Release Note

### 2024.6.1 Version 2.8.1

- 支持 DeleteObject 递归删除
- HeadObject 支持返回 EC

### 2024.4.15 Version 2.8.0

- 支持分层命名空间桶
- 支持 PutBucketInventory/GetBucketInventory/ListBucketInventory/DeleteBucketInventory
- 支持 PutSymlink/GetSymlink
- 支持 User-Agent 扩展规范
- 支持 CredentialsProvider 和 ECS 免密登录

### 2024.3.31 Version 2.7.0

- 支持匿名用户
- 支持 PutBucketEncryption/GetBucketEncryption/DeleteBucketEncryption
- 支持 PutBucketTagging/GetBucketTagging/DeleteBucketTagging
- 上传对象支持 100-continue 机制
- 适配服务端 Retry-After 重试策略

### 2024.2.21 Version 2.6.7
- add getFileStatus api
- fix some bugs
### 2023.12.6 Version 2.6.6
- add getBucketNotificationType2/putBucketNotificationType2 api
- add STORAGE_CLASS_DEEP_COLD_ARCHIVE for StorageClassType
### 2023.12.6 Version 2.6.5
- fix: remove bucket name check in uploadFile/downloadFile/resumableCopyObject methods.
### 2023.12.4 Version 2.6.4
- add STORAGE_CLASS_ARCHIVE for StorageClassType
### 2023.10.18 Version 2.6.3
- fix input stream not support reset
### 2023.10.7 Version 2.6.2
- add GetObject支持saveBucket和saveObject参数
- fix crc64计算未兼容老对象和图片处理
### 2023.8.8 Version 2.6.1
- fix:create bucket un-support az-redundancy param
### 2023.5.30 Version 2.6.0
- 支持若干新特性，归档取回、自定义域名、智能分层和冷归档存储类型、上传回调等
- 修复若干已知bug
### 2023.5.25 Version 2.5.5
- fix crc64 value not reset while InputStream is reset.
### 2023.4.6 Version 2.5.4
- add forceClose method in getObject for closing content immediately.
### 2023.3.30 Version 2.5.3
- default set content-type in setObjectMeta method.
- fix retry bug in BufferedInputStream.
- add readLimit setting in putObject/uploadPart method for retry in BufferedInputStream.
### 2023.3.1 Version 2.5.2
- fix verify ssl cert bug.
- remove 7-days-limit of expire param in preSignedXX methods.
- add retry while catching InterruptedIOException.
### 2023.1.18 Version 2.5.1
- fix upload null file upload bug.
- headBucket/headObject methods throw TosServerException while error happens.
- optimize RateLimiter token acquire method.
- fix InputStream reset bug in retrying request.
### 2022.11.28 Version 2.5.0
- A brand-new version with multiple features.
- Multiple Bucket APIs.
- Data Integrity Validation in put/get objects.
- Retry, rateLimit, progressbar, eventListener, full debug log, dns cache, etc.
- Breakpoint continuation in upload/download large file.
- Completely compatible with older 0.2.X versions, recommended update. 
### 2022.8.31 Version 0.2.10
- fix object key signature failed issue. 
### 2022.8.18 Version 0.2.9
- fix connection leak issue.
### 2022.6.9 Version 0.2.7
- support upload file concurrent and resumeble
### 2022.6.9 Version 0.2.6
- fix ListUploadedParts query params.
- add not-null-validation in method input.
### 2022.4.15 Version 0.2.5
- remove "/"-ending-object-key limitation
### 2022.3.18 Version 0.2.4
- fix some bugs about content-length
### 2022.2.15 Version 0.2.3
- fix bug that sdk no-support Chinese
### 2021.12.13 Version 0.2.2
- Update log4j maven version to 2.15.0
- add some logs
- fix the user-agent version mismatch
### 2021.12.6 Version 0.2.0
- The first version sdk