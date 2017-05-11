package com.example.testing.testrxandre.api;

import com.example.testing.testrxandre.bean.DataBean;
import com.example.testing.testrxandre.bean.HttpResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

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
