package com.example.testing.testrxandre.ui;



import android.util.Log;

import com.example.testing.testrxandre.R;
import com.example.testing.testrxandre.api.ApiService;
import com.example.testing.testrxandre.bean.DataBean;
import com.example.testing.testrxandre.net.HttpObserver;
import com.example.testing.testrxandre.utils.T;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {



    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }


    @Override
    protected void initUIAndListener() {

    }

    @Override
    protected void initData() {
        getData();
    }

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
                //不做任何处理
            }

            @Override
            public void getDisposable(Disposable disposable) {
                addDisposable(disposable);
            }
        },map);
    }


}
