package com.example.testing.testrxandre.api;

import android.widget.Toast;

import com.example.testing.testrxandre.App;
import com.example.testing.testrxandre.R;
import com.example.testing.testrxandre.bean.HttpResult;

import io.reactivex.functions.Function;


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
