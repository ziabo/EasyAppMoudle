package com.example.testing.testrxandre.bean;

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
