package com.benli.randomkeyboard;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initHttp();
        Fresco.initialize(this);
    }

    private void initHttp(){
        HttpHeaders headers = new HttpHeaders();
        headers.put("Sign", "public_sign_key_IUID*$#@JKSNMNC___)WE@B");    //header不支持中文，不允许有特殊字符
        OkGo.getInstance().init(this)
                .addCommonHeaders(headers);
    }
}
