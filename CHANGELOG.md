## Release Note
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