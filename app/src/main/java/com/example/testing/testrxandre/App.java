package com.example.testing.testrxandre;

import android.app.Application;

import com.example.testing.testrxandre.utils.NetUtils;
import com.example.testing.testrxandre.utils.T;

/**
 * Created by ziabo on 2017/5/9.
 */

public class App extends Application{

    public static Application INSTANCE;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        T.register(this);
        NetUtils.register(this);
    }
}
