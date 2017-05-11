package com.example.testing.testrxandre.api;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
