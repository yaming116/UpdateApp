UpdateApp
===

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/yaming116/UpdateApp/blob/master/LICENSE)


开箱即用的app更新，主要负责软件下载、软件安装等。


### Use

```java
UpdateService.start(this, "xxx.apk",
                R.mipmap.ic_launcher, R.mipmap.ic_launcher (smallIco));
```

注：smallIco 有个需要主要的地方，[详情 Android通知栏的微技巧](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650235923&idx=1&sn=af1fc1a6b60282732d94b0e7a354488f&scene=1&srcid=0517c0t12GnMgc5tWAkEMHNs#)


### Gradle

```groovy

allprojects {
    repositories {
        maven { url "https://www.jitpack.io" }
    }
}

dependencies {
    compile 'com.github.yaming116:UpdateApp:1.0.0'
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