# EasyAppMoudle

注:此处的文档可能更新的不会太频繁,里面有可能会有一些错误的地方,烦请结合[简书上面我的博客](http://www.jianshu.com/p/bb6aad88fd0d)配合着看

> 前言: 本人也是一个小菜鸟,写这篇文章意在抛砖引玉,希望有的大神可以来看看我哪里有不足之处,帮我答疑解难!假如有刚接触的小伙伴,也可以一起进步......

## 首先，看本博客之前你需要掌握以下技能：
>1.你是一个Android开发工程师,且迫切希望改变自己项目里面的moudle层
2.你对java的解耦思想有一定了解,基础相对较扎实
 3.你要对Android的okHttp3有一定的了解,比如拦截器等...
 4.对Retrofit有一定的了解,最起码自己写过Demo测试过
 5.对java1.8的RetroLamada知道是什么
 6.对RxJava有一定的了解,以及1.x升级到2.x做了什么改动
 7.对google的Gson熟练掌握
 8.对以上我所说的你确定你都达到了,当然没达到也没关系,后面我会一点一点的讲

首先先贴上一个[maven仓库](http://mvnrepository.com/)的地址,方便你查询当前maven仓库里面各种库的最新版本.
然后是[RxJava 的github地址](https://github.com/ReactiveX/RxJava)俗话说得好,任何不懂的问题都可以通过查询源码来解决,人家的注释给你写的很明白,英文水平决定了你的高度.
然后是[Retrofit的github地址](https://github.com/square/retrofit),个人一直比较喜欢Retrofit这个网络加载框架,Retrofit的英文翻译是改进,更新, 花样翻新...我觉得他们起名字的时候更倾向的是第三种翻译吧,哈哈...
还有[okHttp的github地址](https://github.com/square/okhttp),okHttp在HttpClient安卓弃用了以后(当然也不能讲弃用,是没法用),是Android开发中的一个利器,简洁方便,但是就是使用原生的话有点费劲,搭配Retrofit以后可以说是如虎添翼.

好了啰啰嗦嗦的说了这么多废话,下面进入正题!
### 首先是我的项目结构图

![项目结构](http://upload-images.jianshu.io/upload_images/3887839-4e30712f04dfee02.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

写这个大致分为以下几步,先列出来后面会一步一步的讲:
####
* 1 依赖倒入
* 2 封装BaseActivity和App(其实这一步每个人有每个人的想法,我只是建议这样写,不足之处望指出)
* 3 net包下两个拦截器以及自定义Observer
* 4 bean包下HttpResult类(针对自己的接口编写)
* 5 api包下面的四个类

大致分为上面的几个步骤,不要着急,容我倒杯茶慢慢道来...

## 1. 依赖导入
```
    //okhttp
    compile 'com.squareup.okhttp3:okhttp:3.7.0'
    compile 'com.squareup.okio:okio:1.12.0'
    compile 'com.squareup.okhttp3:logging-interceptor:3.7.0'
    //gson
    compile 'com.google.code.gson:gson:2.8.0'
    //retrofit2
    compile 'com.squareup.retrofit2:retrofit:2.2.0'
    compile 'com.squareup.retrofit2:converter-gson:2.2.0'
    compile 'com.squareup.retrofit2:converter-scalars:2.2.0'
    compile 'com.squareup.retrofit2:adapter-rxjava2:2.2.0'
    //rxJava
    compile'io.reactivex.rxjava2:rxjava:2.0.1'
    compile'io.reactivex.rxjava2:rxandroid:2.0.1'
```
下面我依次介绍一下上面几个依赖的具体用处
##### okhttp注释下面的三个
> 前两个不用多说,用过okhttp的自然都知道,需要注意的一点是第一个版本不能低于3.4.1,具体原因我也不是很清楚,有知道原因的烦请告诉我一声.第三个顾名思义,是okhttp自己提供的log拦截器,方便我们在控制台输出okhttp信息

##### Retrofit2注释下面的四个
> 第一个是不必多说,第二个作用是让Retrofit支持gson,添加这个以后可以直接结果出来就生成我们想要的JavaBean.第三个, <strong>A Converter which supports converting strings and both primitives and their boxed types to text/plain bodies.</strong>源码里面是这么介绍的,意思自己理解,我就不关公门前耍大刀了,毕竟自己是四级425分的渣渣...最后一个是Retrofit适配RxJava2必须要添加的

##### RxJava注释下面的两个
> 第一个是RxJava 第二个是RxJava适配Android的

注:gson注释下面的就不用我废话了吧

## 2. 封装BaseActivity和App
因为我在项目里面多次用到Application的Context,以及我的工具类里面需要用到Context的地方也都是用的Application的,所以简单写了一下就是下面这个样子的
```
public class App extends Application{

    public static Application INSTANCE;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        T.register(this);
        NetUtils.register(this);
    }
}
```
下面贴上我的T这个类,这个类主要是做一些Toast的工作
```
/**
 * Toast统一管理类
 */
public class T {
    public static Context mContext;
    private static Toast toast;


    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    public static void register(Context context) {
        mContext = context;
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(CharSequence message) {
        if (mContext==null){
            throw new RuntimeException("unRegister Context in Application");
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
        toast.setText(message);
        toast.show();
    }

    public static void showShort(int resId) {
        if (mContext==null){
            throw new RuntimeException("unRegister Context in Application");
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_LONG);
        toast.setText(mContext.getString(resId));
        toast.show();
    }

}
```
友情提示:自定义App以后不要忘了在Manifest.xml的application节点下面加入```android:name=".App"```这一句!

接下来是我自己的BaseActivity
```
/**
 * Created by ziabo on 2017/5/9.
 * Activity的Base类
 */

public abstract class BaseActivity extends AppCompatActivity{

    private CompositeDisposable mCompositeDisposable;
    private ApiService mApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mApiService == null){
            mApiService = ApiService.getApiService();
        }
        setContentView(initContentView());
        initUIAndListener();
        initData();
    }

    /**
     * 设置layout
     */
    protected abstract int initContentView();

    /**
     * 初始化UI和Listener
     */
    protected abstract void initUIAndListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 管理所有建立的链接,在onDestroy中清空 mCompositeDisposable
     */
    protected void addDisposable(Disposable disposable){
        if (mCompositeDisposable==null){
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null){
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }
}
```
里面有些看不懂的地方不要着急,可以先就看我那三个protected abstract的方法就好,这三个是强制要子类实现的,我们的Activity写出来的时候就是下面这个样子,比较简洁明了..
```
public class TestActivity extends BaseActivity{
    @Override
    public int initContentView() {
        return 0;//此处放上你的Layout的id
    }

    @Override
    protected void initUIAndListener() {

    }

    @Override
    protected void initData() {

    }
}
```
注: 一定要注意方法的先后执行顺序!

## 3. net包下两个拦截器以及自定义Observer
说到拦截器,这个就不得不提一下okHttp的强大之处,此处的两个拦截器一个拦截器是发送请求的时候的调用的,另一个是结果以jsonString返回回来的时候调用的,他们分别的用处我会在下面细讲!先上代码

##### RequestInterceptor
```
/**
 * 类名称：请求前拦截器,这个拦截器会在okhttp请求之前拦截并做处理
 */
public class RequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        //请求定制：添加请求头
        Request.Builder requestBuilder = original
                .newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //设置cookie
//        String cookie= App.getCookie();
//        if (StringUtil.checkStr(cookie)) {             //cookie判空检查
//            requestBuilder.addHeader("Cookie", cookie);
//        }

        //如果是post的情况下,请求体定制：统一添加参数,此处演示的是get请求,因此不做处理
        if (original.body() instanceof FormBody) {
            FormBody.Builder newFormBody = new FormBody.Builder();
            FormBody oidFormBody = (FormBody) original.body();
            for (int i = 0; i < oidFormBody.size(); i++) {
                newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
            }
        //当post请求的情况下在此处追加统一参数
//            String client = Constants.CONFIG_CLIENT;
//
//            newFormBody.add("client", client);

            requestBuilder.method(original.method(), newFormBody.build());
        }
        return chain.proceed(requestBuilder.build());
    }
}
```
里面的注释写的个人觉得挺全的,有什么问题可以具体详细再问!
##### ResponseInterceptor
```
/**
 * 结果拦截器,这个类的执行时间是返回结果返回的时候,返回一个json的String,对里面一些特殊字符做处理
 * 主要用来处理一些后台上会出现的bug,比如下面声明的这三种情况下统一替换为:null
 */
public class ResponseInterceptor implements Interceptor {
    private String emptyString = ":\"\"";
    private String emptyObject = ":{}";
    private String emptyArray = ":[]";
    private String newChars = ":null";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String json = responseBody.string();
            MediaType contentType = responseBody.contentType();
            if (!json.contains(emptyString)) {
                ResponseBody body = ResponseBody.create(contentType, json);
                return response.newBuilder().body(body).build();
            } else {
                String replace = json.replace(emptyString, newChars);
                String replace1 = replace.replace(emptyObject, newChars);
                String replace2 = replace1.replace(emptyArray, newChars);
                ResponseBody body = ResponseBody.create(contentType, replace2);
                return response.newBuilder().body(body).build();
            }
        }
        return response;
    }
}
```
这个注释好像也挺全的,哈哈...
HttpObserver我放到讲api包的时候再讲!!!!

##  bean包下HttpResult类
一般来讲,我们的接口请求下来的结构大致都是这样的
```
{
    "code":"noError",
    "data":{
        "banner":Array[3]
    },
    "msg":"",
    "result":true
}
```
接下来看一下我针对这个接口做的HttpResult类
```
/**
 * Created by ziabo on 2017/5/9.
 * T就是传递过来的data的类型
 */

public class HttpResult<T> {

    public String code;
    public String msg;
    public boolean result;
    public T data;
}
```
这个是要手写的,下面这个是GsonFormat自动生成的,toString方法是我自己加进去的,方便打印.所有的变量我都声明为public的方便存取.
```
/**
 * Created by ziabo on 2017/5/9.
 * 这个是实体类,里面只有我们关注的数据,其他的都统一处理
 */

public class DataBean {

    /**
     * nextPage : 1
     * count : 6
     * pageSize : 20
     * prevPage : 1
     * currentPage : 1
     * pageNum : 1
     * healthInfo : [{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/0394155040297634.png","formatCreateDate":"03-30","formatUpdateDate":"04-01","index":1,"updateTime":"2017-04-01 16:01:44","title":"茶的物极必反","accountId":"4028817d549332dd015494a0edd80000","subTitle":"知识","createTime":"2017-03-30 16:19:34","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b180e49015b1e4c604800ad","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/1143420851997416.jpeg","formatCreateDate":"03-30","formatUpdateDate":"04-01","index":2,"updateTime":"2017-04-01 15:59:24","title":"少食多餐在说什么？","accountId":"4028817d549332dd015494a0edd80000","subTitle":"糖尿病","createTime":"2017-03-30 16:12:03","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b180e49015b1e4581a100aa","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/7490881972133794.jpeg","formatCreateDate":"03-30","formatUpdateDate":"04-01","index":3,"updateTime":"2017-04-01 15:56:22","title":"健康运动踢毽子","accountId":"4028817d549332dd015494a0edd80000","subTitle":"高血压","createTime":"2017-03-30 16:10:58","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b180e49015b1e4483c400a8","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/8457604241319624.jpeg","formatCreateDate":"03-30","formatUpdateDate":"04-01","index":4,"updateTime":"2017-04-01 15:54:19","title":"糖尿病病足的定义与预防","accountId":"4028817d549332dd015494a0edd80000","subTitle":"糖尿病","createTime":"2017-03-30 11:59:50","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b180e49015b1d5e9897009a","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/4638045747699966.jpeg","formatCreateDate":"03-27","formatUpdateDate":"04-01","index":5,"updateTime":"2017-04-01 15:50:28","title":"日常小事才不是小事","accountId":"4028817d549332dd015494a0edd80000","subTitle":"高血压","createTime":"2017-03-27 11:13:05","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b09d7e0015b0dc0b4750005","account":"zkrj"},{"img":"http://test2.mb.zkrj.com/wlstatic/image/2017-04-01/9660687163661661.jpeg","formatCreateDate":"03-27","formatUpdateDate":"04-01","index":6,"updateTime":"2017-04-01 14:57:04","title":"洗澡谨记五个不","accountId":"4028817d549332dd015494a0edd80000","subTitle":"糖尿病","createTime":"2017-03-27 11:10:10","publish":true,"adminAccountId":"4028817d549332dd015494a0edd80000","orders":1,"id":"8a9a35085b09d7e0015b0dbe08e60003","account":"zkrj"}]
     */

    public int nextPage;
    public int count;
    public int pageSize;
    public int prevPage;
    public int currentPage;
    public int pageNum;
    public List<HealthInfoBean> healthInfo;

    public static class HealthInfoBean {
        /**
         * img : http://test2.mb.zkrj.com/wlstatic/image/2017-03-30/0394155040297634.png
         * formatCreateDate : 03-30
         * formatUpdateDate : 04-01
         * index : 1
         * updateTime : 2017-04-01 16:01:44
         * title : 茶的物极必反
         * accountId : 4028817d549332dd015494a0edd80000
         * subTitle : 知识
         * createTime : 2017-03-30 16:19:34
         * publish : true
         * adminAccountId : 4028817d549332dd015494a0edd80000
         * orders : 1
         * id : 8a9a35085b180e49015b1e4c604800ad
         * account : zkrj
         */

        public String img;
        public String formatCreateDate;
        public String formatUpdateDate;
        public int index;
        public String updateTime;
        public String title;
        public String accountId;
        public String subTitle;
        public String createTime;
        public boolean publish;
        public String adminAccountId;
        public int orders;
        public String id;
        public String account;

        @Override
        public String toString() {
            return "HealthInfoBean{" +
                    "img='" + img + '\'' +
                    ", formatCreateDate='" + formatCreateDate + '\'' +
                    ", formatUpdateDate='" + formatUpdateDate + '\'' +
                    ", index=" + index +
                    ", updateTime='" + updateTime + '\'' +
                    ", title='" + title + '\'' +
                    ", accountId='" + accountId + '\'' +
                    ", subTitle='" + subTitle + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", publish=" + publish +
                    ", adminAccountId='" + adminAccountId + '\'' +
                    ", orders=" + orders +
                    ", id='" + id + '\'' +
                    ", account='" + account + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "nextPage=" + nextPage +
                ", count=" + count +
                ", pageSize=" + pageSize +
                ", prevPage=" + prevPage +
                ", currentPage=" + currentPage +
                ", pageNum=" + pageNum +
                ", healthInfo=" + healthInfo +
                '}';
    }
}
```
当我们使用的时候只需要这么拼,就是整个完整的JavaBean
``HttpResult<DataBean>``这些网上讲的人很多,相信做过的肯定都了解,就不多说了!

## api包下面的四个类(以及上面没有说的HttpObserver)
这里面是整个部分的核心,相对而言也比较难理解,但是理解了以后就会发现豁然开朗!
##### 首先是ApiInterface
```
/**
 * Created by ziabo on 2017/5/9.
 * 不懂的地方可以仔细研究Retrofit
 */
public interface ApiInterface {

    /**
     * 获取健康信息
     */
    @GET("/rest/app/healthInfo")
    Observable<HttpResult<DataBean>> healthInfo(@QueryMap Map<String, Object> map);

}
```
不要嫌弃我画工捉急,下面是一个图解,简单介绍一下
![图解](http://upload-images.jianshu.io/upload_images/3887839-23bcdc415e721c90.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 1 规定了当前的请求方式是GET请求(因为我实在没有一个合适的接口,此处用的是我一个朋友友情提供的接口,非常感谢)
2  填的是你除了BaseUrl之外的部分
3 此处是与原生的Retrofit区别比较大的一点,也是精髓之处,对RxJava有了解的肯定知道Observable是个什么东西,这里就不多解释
4 同上,里面为什么这么写上面也已经解释过了
5 Retrofit在GET请求时规定的,不多解释
6 这个map的key是String,value是Object,你可以放任意数据类型

到这里也许有人会问,既然有了接口,那么接口的实现类呢?此处牵扯到[Retrofit的原理](http://www.jianshu.com/p/cd69c75d053e),大家可以通过这个博客了解一二,四个字,动态代理!(其实我也不明所以....此处感谢博客的作者 @[Alexclin](http://www.jianshu.com/u/8c68deca21ae) ,让我省了不少力,哈哈)
##### SchedulersTransformer
顾名思义:调度器,上完代码再解释,此处和RxJava 1.x有很大的不同
```
/**
 * Created by ziabo on 2017/5/9.
 * 线程调度器
 */

public class SchedulersTransformer{

    public static <T>ObservableTransformer<T,T> io_main(){
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
```
由于代码有部分Lamada,我大致说一下吧``<T>ObservableTransformer<T,T>``这个是我们需要返回的类型,点击去看源码是这样的``ObservableTransformer<Upstream, Downstream>``,返回的时候我们new一个ObservableTransformer的时候就会实现他内部的apply方法,``ObservableSource<Downstream> apply(Observable<Upstream> upstream);``他所做的事情就是把我们输入进来的Observable做了一些处理,具体做了哪些处理呢?我们具体看代码,``subscribeOn(Schedulers.io())``这句话意思是在io线程建立连接(此处暂时用这个措辞,因为用订阅老感觉不是很舒服),``unsubscribeOn(Schedulers.io())``这一句的意思是在io线程解除连接,``observeOn(AndroidSchedulers.mainThread())``这句话的意思是指定回调线程是Android的主线程,也就是我们常说的UI线程.

##### HttpResultFunc
这个类也是要针对接口进行编写的
```
/**
 * Created by ziabo on 2017/5/9.
 * 类描述：用来统一处理Http的status,并将HttpResult的data部分剥离出来返回给subscriber
 * @param <T> data部分的数据模型
 */

public class HttpResultFunc<T> implements Function<HttpResult<T>,T>{

    @Override
    public T apply(HttpResult<T> tHttpResult) throws Exception {
        if (!tHttpResult.result){//假设当结果为true的时候是请求成功
            if (tHttpResult.msg!=null){//请求失败的情况下吐司错误信息
                Toast.makeText(App.INSTANCE, tHttpResult.msg, Toast.LENGTH_SHORT).show();
            }
        }
        return tHttpResult.data;
    }
}
```
这个是在RxJava的map操作符里面放的,作用就是剥离公共区域的数据做处理,并且把数据转换成我们想要的类型DataBean,这样在成功的回调中就只有我们真正关心的数据,其他的一些问题都被统一处理了,具体看注释,我大致是这么写的,你也可以根据自己的业务逻辑做自己的操作!
##### ApiService
这个类就是所有准备工作完了之后的最最核心的地方了,看到这里也许你累了,此时你可以起来活动活动,喝杯咖啡打足精神,整理整理思路之后再来看这里!
```
/**
 * Created by ziabo on 2017/5/9.
 * ApiService
 */

public class ApiService {

    private ApiInterface mApiInterface;

    private ApiService() {
        //HTTP log
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //RequestInterceptor
        RequestInterceptor requestInterceptor = new RequestInterceptor();

        //ResponseInterceptor
        ResponseInterceptor responseInterceptor = new ResponseInterceptor();

        //OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(requestInterceptor)
                .addInterceptor(responseInterceptor);
//      通过你当前的控制debug的全局常量控制是否打log
        if (Constants.DEBUG_MODE) {
            builder.addInterceptor(httpLoggingInterceptor);
        }
        OkHttpClient mOkHttpClient = builder.build();

        //Retrofit
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://test2.mb.zkrj.com/")//替换为你自己的BaseUrl
                .build();

        mApiInterface = mRetrofit.create(ApiInterface.class);
    }

    //单例
    private static class SingletonHolder {
        private static final ApiService INSTANCE = new ApiService();
    }

    //单例
    public static ApiService getApiService() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取健康信息
     */
    public void get_health(Observer<DataBean> observer, Map<String, Object> map) {
        mApiInterface.healthInfo(map)
                .compose(SchedulersTransformer.io_main())
                .map(new HttpResultFunc<>())
                .subscribe(observer);
    }

}
```
里面把我们之前所有准备的东西都用上了,``Constants``类是我们整个项目里面的常量池!此处我着重介绍一下我下面这个方法!
![调用](http://upload-images.jianshu.io/upload_images/3887839-dfc3a2867523ee94.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这次箭头是不是比上面好看了?哈哈
1 这个是RxJava 2.x中的观察者,类似于1.x的SubScriber,有兴趣的点进去看看就知道了
2 ApiInterface接口的方法的调用
3 compose操作符,百度一搜一大堆,不多解释,概念1.x和2.x基本没有什么变化
4 map操作符,同上
5 建立连接

##### HttpObserver
这个放在最后讲,因为这个是整个框架成型的最后一步!不废话,直接上代码
```
/**
 * Created by ziabo on 2017/5/9.
 * 结果回调回来之后的接口的实现类
 * 有兴趣的话可以翻阅这里 http://reactivex.io/documentation/observable.html
 */

public abstract class HttpObserver<R> implements Observer<R> {

    private Disposable mDisposable;

    /**
     * 建立链接的时候调用并生成Disposable对象
     * @param d 链接状态对象
     */
    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        getDisposable(d);
    }


    /**
     * 请求结果回调回来的时候调用,会调用多次
     * @param r 返回的结果,没网络时提示
     */
    @Override
    public void onNext(R r) {
        if (!NetUtils.isConnected()) {
            if (mDisposable!=null && !mDisposable.isDisposed()){
                mDisposable.dispose();
            }
            T.showShort("请检查网络连接后重试!");
        }
        onSuccess(r);
    }

    public abstract void onSuccess(R r);

    /**
     * 出现异常的时候会走这里,我们统一放在 onFinished();处理
     */
    @Override
    public void onError(Throwable e) {
        onFinished();
        if (e instanceof HttpException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof TimeoutException){
            onNetworkException(e);
        }else {
            onUnknownException(e);
        }
    }

    /**
     * 不管成功与失败,这里都会走一次,所以加onFinished();方法
     */
    @Override
    public void onComplete() {
        onFinished();
    }

    /**
     * 请求结束之后的回调,无论成功还是失败,此处一般无逻辑代码,经常用来写ProgressBar的dismiss
     */
    public abstract void onFinished();

    /**
     * 向子类暴露 Disposable
     */
    public abstract void getDisposable(Disposable disposable);

    private void onNetworkException(Throwable e) {
        e.printStackTrace();
        T.showShort("获取数据失败，请检查网络状态");
    }

    private void onUnknownException(Throwable e) {
        e.printStackTrace();
    }
}
```
注释写的很详细了,还有很多需要完善的地方,希望大家看到的能够提出宝贵的意见!

## 最后再说两句
这个库是我一个朋友自己研究出来的,我只是在此基础上做了RxJava部分的升级,关于RxJava2.x还有很多很强大的地方我还没有用到,有思路的希望给我提一下,在这里先说声谢谢!
这个库的好处就是其他地方你都定好了以后,一个新的api,只要知道数据结构,就只需要做以下两步:
##### 在ApiInterface中添加接口具体如图

![接口](http://upload-images.jianshu.io/upload_images/3887839-97882b9039e4efd3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 在ApiService中添加一个方法具体如图

![ApiService](http://upload-images.jianshu.io/upload_images/3887839-b14b5558e86398f3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 调用的时候直接这么写:
```
private void getData() {
        Map<String,Object> map = new HashMap<>();
        map.put("currentPage",1);
        map.put("pageSize",20);
        ApiService.getApiService().get_health(new HttpObserver<DataBean>() {
            @Override
            public void onSuccess(DataBean dataBean) {
                T.showShort(dataBean.toString());
                Log.d("MainActivity", dataBean.toString());
            }

            @Override
            public void onFinished() {
                //一般不做任何处理
            }

            @Override
            public void getDisposable(Disposable disposable) {
                addDisposable(disposable);
            }
        },map);
    }
```

> 后言:历时两天,这个博客终于完结了!希望大家不要吝啬起码给个赞吧,就冲我这排版看起来还不错呀!
项目源码稍后我会上传到github,耐心等候,欢迎吐槽!