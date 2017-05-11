package com.example.testing.testrxandre.api;

import com.example.testing.testrxandre.bean.DataBean;
import com.example.testing.testrxandre.net.RequestInterceptor;
import com.example.testing.testrxandre.net.ResponseInterceptor;


import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
//        if (Constants.DEBUG_MODE) {
        builder.addInterceptor(httpLoggingInterceptor);
//        }
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
