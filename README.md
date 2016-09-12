UpdateApp
===

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/yaming116/UpdateApp/blob/master/LICENSE)
[![Jitpack](https://www.jitpack.io/v/yaming116/UpdateApp.svg)](https://www.jitpack.io/#yaming116/UpdateApp)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![Buildpass](https://api.travis-ci.org/yaming116/UpdateApp.svg?branch=master)](https://travis-ci.org/yaming116/UpdateApp)

[中文说明](https://github.com/yaming116/UpdateApp/blob/master/README-zh.md)

App update, is mainly responsible for app download and installation, etc.
Permission and Service are has been added to the aar inside.


### Permission

```xml
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.INTERNET"/>
```

### Service

```xml
    <service android:name=".UpdateService" />
```

### Use

```java
   UpdateService.Builder.create(URL).build(this);
```


NOTE: smallIcon where there is a major need，[Micro Tips Details Android notification bar](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650235923&idx=1&sn=af1fc1a6b60282732d94b0e7a354488f&scene=1&srcid=0517c0t12GnMgc5tWAkEMHNs#)


# Params

|parameter|description|
|----|:---|
|downloadUrl|download link|
|icoResId|Notification icon，default is app icon|
|icoSmallResId|Notification bottom right corner icon，default is app icon|
|storeDir|store apk dir，default in sdcard/Android/package/update|
|updateProgress| Refresh notification progress bar, default updated each time you download add 1%|
|downloadNotificationFlag|downloading Notification Flag|
|downloadErrorNotificationFlag|download error Notification Flag|
|downloadSuccessNotificationFlag|download success Notification Flag|
|isSendBroadcast|Whether to send broadcast|
### Gradle

```groovy

allprojects {
    repositories {
        maven { url "https://www.jitpack.io" }
    }
}

dependencies {
    compile 'com.github.yaming116:UpdateApp:1.0.3'
}
```

License
-------

    Copyright (C) 2011 花开堪折枝 Software Ltd

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.