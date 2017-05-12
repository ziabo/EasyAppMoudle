package com.example.testing.testrxandre.net;

import com.example.testing.testrxandre.utils.NetUtils;
import com.example.testing.testrxandre.utils.T;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.adapter.rxjava2.HttpException;


/**
 * Created by ziabo on 2017/5/9.
 * 结果回调回来之后的接口的实现类
 * 有兴趣的话可以翻阅这里 http://reactivex.io/documentation/observable.html
 */

public abstract class HttpObserver<R> implements Observer<R> {


    /**
     * 建立链接的时候调用并生成Disposable对象,此处相当于1.x的onStart()方法我做了如下处理
     * 有更好建议的可以私聊我,或者评论
     * @param d 链接状态对象
     */
    @Override
    public void onSubscribe(Disposable d) {
        if (!NetUtils.isConnected()) {
            if (d!=null && !d.isDisposed()){
                d.dispose();
            }
            T.showShort("请检查网络连接后重试!");
            onFinished();
        }else{
            getDisposable(d);
        }
    }


    /**
     * 此处和1.x的onNext()基本没有什么变化,所以我选择注释,让实现类自己处理
     * 之前我是写了的,看过这篇博客的应该有印象
     * @param r 返回的结果,没网络时提示
     */
//    @Override
//    public void onNext(R r) {
//        onSuccess(r);
//    }
//
//    public abstract void onSuccess(R r);

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
