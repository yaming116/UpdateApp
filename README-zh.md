UpdateApp
===

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/yaming116/UpdateApp/blob/master/LICENSE)
[![Jitpack](https://www.jitpack.io/v/yaming116/UpdateApp.svg)](https://www.jitpack.io/#yaming116/UpdateApp)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)


开箱即用的app更新，主要负责软件下载、软件安装等 Permission和Service默认添加在aar包里面的，如果使用gradle不需要
在意下面的配置了。

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


注：smallIco 有个需要主要的地方，[详情 Android通知栏的微技巧](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650235923&idx=1&sn=af1fc1a6b60282732d94b0e7a354488f&scene=1&srcid=0517c0t12GnMgc5tWAkEMHNs#)


# Params

|参数|描述|
|----|:---|
|downloadUrl|下载地址|
|icoResId|Notification 的icon，默认应用的icon|
|icoSmallResId|Notification 右下角的icon，默认应用的icon|
|storeDir|保存在sdcard路径，默认在sdcard/Android/package/update|
|updateProgress| 刷新notification 进度条，默认每次下载1%更新一次|
|downloadNotificationFlag|下载进行中的Notification Flag|
|downloadErrorNotificationFlag|下载失败的Notification Flag|
|downloadSuccessNotificationFlag|下载成功的Notification Flag|
|isSendBroadcast|是否会发送下载状态广播|

### Gradle

```groovy

allprojects {
    repositories {
        maven { url "https://www.jitpack.io" }
    }
}

dependencies {
    compile 'com.github.yaming116:UpdateApp:1.0.2'
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