package com.example.testing.testrxandre.api;


/**
 * Created by ziabo on 2017/5/12.
 * 自定义的ApiException
 */

public class ApiException extends Exception{

    public ApiException(String message) {
        super(message);
    }
}
