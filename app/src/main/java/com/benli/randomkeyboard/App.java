package com.benli.randomkeyboard;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.statistics.common.DeviceConfig;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initHttp();
        Fresco.initialize(this);
        initUM();
    }

    private void initHttp(){
        HttpHeaders headers = new HttpHeaders();
        headers.put("Sign", "public_sign_key_IUID*$#@JKSNMNC___)WE@B");    //header不支持中文，不允许有特殊字符
        OkGo.getInstance().init(this)
                .addCommonHeaders(headers);
    }

    private void initUM(){
        UMConfigure.init(this, "5e37a696570df30d27000098", "GooglePlay", UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        String[] deviceInfo = getTestDeviceInfo(this);
        LogUtils.i(deviceInfo[0], deviceInfo[1]);
    }

    public static String[] getTestDeviceInfo(Context context){
        String[] deviceInfo = new String[2];
        try {
            if(context != null){
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
        } catch (Exception e){
        }
        return deviceInfo;
    }
}
