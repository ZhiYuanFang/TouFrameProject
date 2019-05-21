##TouRfRxOHC

TouRetrofitRxJavaOkHttpClient

网络集成框架
(之所以想自己集成一下框架，就是觉得大部分小项目都是一样的网络配置，就偷懒一下，集成好了，以后拿去直接用了。
如果有缘遇到了你，拿去玩~)

应用三方框架：
```java
        //log日志打印
        api 'com.github.ihsanbal:LoggingInterceptor:3.0.0'
        //网络请求
        api 'com.squareup.okhttp3:okhttp:3.12.1'
        //网络请求格式处理
        api 'com.squareup.retrofit2:retrofit:2.4.0'
        //观察者模式配合
        api 'com.squareup.retrofit2:adapter-rxjava:2.0.2'
        //RxJava线程切换
        api 'io.reactivex:rxandroid:1.2.1'
        //api module转换
        api 'com.squareup.retrofit2:converter-gson:2.4.0'
```

网络缓存策略：
--1：cache 缓存，由服务端控制
--2: 本地缓存--异步更新UI数据，假性流畅

使用方法：
主项目的Application中调用
```java
RfRxOHCUtil.initApiService(this, "http://bgtest.yougbang.com", getPackageName() + "-cache",
                2 * 1024 * 1024, 30, BuildConfig.BUILD_TYPE.equals("release"), BuildConfig.DEBUG, BuildConfig.VERSION_NAME,
                BuildConfig.FLAVOR, "android", new RfRxOHCUtil.TouRRCDelegate() {
                    @Override
                    public void addMoreForOkHttpClient(OkHttpClient.Builder httpBuilder) {

                    }

                    @Override
                    public void addMoreForInterceptor(LoggingInterceptor.Builder logBuilder) {

                    }

                    @Override
                    public void initApiService(Retrofit retrofit) {
                        apiService = retrofit.create(ApiService.class);
                    }
                });
```

在网络请求模块调用：
```java
new RxOHCUtils<>(context).executeApi(BaseApplication.apiService.indexNormalEngineering(), new BaseSubscriber<List<EngineerModule>>() {
            @Override
            public void success(List<EngineerModule> data) {
                name.set(data.get(0).getName());
            }

            @Override
            public String initCacheKey() {
                return "indexNormalEngineering";//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
            }

        });
```

当前app主module中采用的是MVVM框架，不熟悉的同学不用紧张，你需要的只是RfRxOHC模块而已
