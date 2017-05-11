package com.example.testing.testrxandre.net;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

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
//        String sessionId = App.getSessionId();
//        if (StringUtil.checkStr(sessionId)) {
//            requestBuilder.addHeader("Cookie", "PHPSESSID=" + sessionId);
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
