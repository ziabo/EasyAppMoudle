package com.example.testing.testrxandre.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.testing.testrxandre.api.ApiService;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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
    public abstract int initContentView();

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
