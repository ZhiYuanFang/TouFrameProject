## TouFrameProject Android开发框架
项目地址：[https://github.com/ZhiYuanFang/TouFrameProject](https://github.com/ZhiYuanFang/TouFrameProject)

> 小型项目基本框架 网络请求+开发框架， 便于快速实现页面需求

**项目拉下来后，需要在`local.properties`中配置ndk地址，才能运行**
<font size=1>我的本地地址是这样的，你跟我的肯定不一样，不清楚的，可以另行搜索一下相关配置。</font>

```
sdk.dir=E\:\\Android\\settings\\sdk
ndk.dir=E\:\\Android\\settings\\sdk\\ndk\\23.0.7196353
```

## TouRfRxOHC【Retrofit+RxJava+OkHttpClient】

> 网络集成框架 ：接口顺序执行，一个接口请求完成之后，才会执行下一个接口
> 当前页面关闭后，关闭所有的接口请求
> 当前APP退出到桌面后，关闭所有的接口请求

网络缓存策略：

> --1：cache 缓存，由服务端控制
> --2：本地缓存--如果无网络，会走本地缓存数据，有网络则会获取最新接口数据
 

 - [ ]  未处理token失效刷新机制，因为发现不同的服务端，对token的失效判断是不一样的，需要使用者，自己去继承处理。

使用方法：

> 复制RfRxOHC模块，放到项目更目录下，然后在项目的setting.gradle中声明当前模块

```java
include ':app', ':RfRxOHC'
```

> 在APP的build.gradle中引用当前模块

```java
implementation project(':RfRxOHC')
```

> 主项目的Application中调用

```java
RfRxOHCUtil.initApiService(this, "http://api.x16.com/", getPackageName() + "-cache",
                2 * 1024 * 1024, 30, BuildConfig.BUILD_TYPE.equals("release"), BuildConfig.DEBUG, BuildConfig.VERSION_NAME,
                "huawei", "android", 0, new RfRxOHCUtil.TouRRCDelegate() {
                    @Override
                    public void addMoreForOkHttpClient(OkHttpClient.Builder httpBuilder) {
                        //动态值
                        httpBuilder.addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request originalRequest = chain.request();
                                if (DefaultUtils.token == null) {
                                    return chain.proceed(originalRequest);
                                }
                                Request authorised = originalRequest.newBuilder()
                                        .header("Authorization", "Bearer " + DefaultUtils.token)
                                        .header("Role", "32")
                                        .build();
                                return chain.proceed(authorised);
                            }
                        });
                    }

                    @Override
                    public void addMoreForInterceptor(LoggingInterceptor.Builder logBuilder) {
                        //静态值
                        logBuilder.addHeader("Data-Type", "json")
                                .addHeader("Accept", "*/*")
                                .addHeader("Cache-Control", "no-cache")
                                .addHeader("x-app-version", "1.0");
                    }

                    @Override
                    public void initApiService(Retrofit retrofit) {
                        apiService = retrofit.create(ApiService.class);
                    }
                });
```

> 接口管理：根据Retrofit规则定义，看不懂的可以先去了解一下retrofit的使用

```java
public interface ApiService {
    public interface ApiService {
    @POST("msu/v1/signin/code")
    Observable<BaseModule<UserModel>> login(@Body RequestBody data);

    @GET("msr/v1/app/personal/index")
    Observable<BaseModule<MainModel>> getHistory(@QueryMap Map<String, Object> map);


//    @Headers("Content-type:text/x-plain-rsa-json")
//    @POST("api/Account/phoneCode")
//    Observable<BaseModule<PhoneCodeModule>> phoneCode(@Body RequestBody data);
//
//    @Headers("Content-type:text/x-plain-rsa-json")
//    @POST("api/Account/codeLogin")
//    Observable<BaseModule<LoginModule>> codeLogin(@Body RequestBody data);
//
//    @Headers("Content-type:text/x-plain-aes-json")
//    @POST("api/Account/userInfo")
//    Observable<BaseModule<LoginModule>> userInfo(@Body RequestBody data);
//
//    @POST("ecmobile/test.php")
//    Observable<BaseModule> test(@Body String data);
}
}
```

> 网络请求调用：伴随页面的生命周期【Activity继承BaseTouActivity后，只需要传当前Activity对象就可以了， 如果不继承则需要自己去实现LifecycleProvider或者不传，则表示不中断接口请求】，如果页面摧毁，则请求中断

```java
new RxOHCUtils<>(this).executeApi(BaseApplication.apiService.getHistory(map), new BaseSubscriber<MainModel>(this, loadEnd) {
            @Override
            public void success(MainModel data) {
                if (data != null) {
                    historyAdapter.setList(data.getReadHistory());
                }
            }

            @Override
            public String initCacheKey() {
                return "getHistory";//如果该页面涉及隐私，则不传cacheKey，就不会产生缓存数据
            }

        });
```

## toubasemvvm

> 开发框架：databinding的应用，解耦Activity与Layout。 不想用databinding的话，可以不引用这个框架.
> 里面集成了我很多开发中常用的工具，有兴趣的可以去Utils里去看看，这里不做介绍
> 

使用方法

> 复制当前模块到主项目下，并与RfRxOHC引用类似

settting.gradle
```java
include ':app', ':toubasemvvm'
```
app/build.gradle

```java
android {
    ...
    dataBinding{
        enabled true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
	...
    implementation project(':toubasemvvm')
}
```

> 主项目的Application中初始化

```java
 ApplicationUtils.init(this, 750, 1334, new TouDelegate() {
            @Override
            public boolean isLogin() {
                return false;//当前用户是否登录
            }

            @Override
            public void gotoLoginActivity() {
                //前往登录界面
            }

            @Override
            public void checkVersion(VersionDelegate versionDelegate) {
                //请求接口判断是否需要更新
                if(true){
                    versionDelegate.installVersion("", "更新了一些功能", BuildConfig.VERSION_CODE);
                }
            }

            @Override
            public String applicationId() {
                return BuildConfig.APPLICATION_ID;
            }

            @Override
            public void cacheMainThrowable(Throwable e) {
                //捕获主线程异常，上传bugly
            }
        });
```
基类继承

> BaseTouActivity

```java
public class MainActivity extends BaseTouActivity<ActivityMainBinding> {

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String[] initPermission() {
        return new String[]{Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    protected void initData() {
        //在activity_main中引用，参考dataBinding试用方法
        mBinding.setViewModel( new MainViewModel(this, this));
    }

    @Override
    protected void initServer() {
        //网络请求
    }
}
```

> BaseTouFragment：碎片化页面布局，也是常用的手段，其使用频率比起Activity更多，故而也需要一个基类去统一处理

使用了懒加载概念实现，具体使用如下：

```java
public class MainFragment extends BaseTouFragment<FragmentMainBinding>{

    public ObservableField<String> imageFiled = new ObservableField<>("");
    @Override
    protected boolean isInViewPager() {
        return false;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected String[] initPermission() {
        return new String[0];
    }

    @Override
    protected void initData() {
        mBinding.setContext(this);
    }

    @Override
    protected void initServer() {
        imageFiled.set(ImageLoaderUtil.testPic);
    }
}

```

> RecyclerView：列表展示是所有软件常用的工具，为了方便使用，我做了一些集成，让RecyclerView的使用，代码趋于傻瓜话。让我们直接看看使用场景吧

app/src/main/res/layout/activity_main.xml
```java
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:binding="http://schemas.android.com/apk/res-auto">

    <data>
    	...
        <variable
            name="adapter"
            type="xyz.ttyz.toubasemvvm.adapter.utils.BaseEmptyAdapterParent" />
    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/white"
        android:orientation="vertical"
        tools:context="xyz.ttyz.tourfrxohc.MainActivity">

		...
        <androidx.recyclerview.widget.RecyclerView
                        android:layout_marginStart="15dp"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        binding:verticalDis="@{8}"
                        binding:adapter="@{adapter}" />
    </LinearLayout>

</layout>
```

xyz.ttyz.tourfrxohc.MainActivity

```java
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

	...
    //recycler适配器
    BaseEmptyAdapterParent historyAdapter;
   
    @Override
    protected void initData() {
        ...
        historyAdapter = new BaseEmptyAdapterParent(this, new BaseRecyclerAdapter.NormalAdapterDelegate() {
            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ResorceViewHolder(MainActivity.this, parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((ResorceViewHolder)holder).bindData((ResorceModel) historyAdapter.getItem(position));
            }
        });
        mBinding.setAdapter(historyAdapter);
    }

    @Override
    protected void initServer() {
        ...
    }

    ...
}
```
**没错，就是这么简单，不用做其它繁杂的定义了，就这样实现了一个通用列表形式的列表展示。

**之所以能这么简洁，是在 `xyz.ttyz.toubasemvvm.adapter.RecyclerAdapter` 中做了统一处理，看不懂的，可以先去了解一下`DataBinding`机制

除了RecyclerView以外，还优化了很多其它常用的页面处理，太多了，就不一一赘述了，可以自己去看看`xyz.ttyz.toubasemvvm.adapter.` 里面的Adapter