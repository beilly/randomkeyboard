package com.benli.randomkeyboard.app;


import androidx.annotation.Keep;

@Keep
public class AppInfoBean {

    //应用名
    public String appName;
    //应用版本号
    public String appVersion;
    //应用包名
    public String packageName;
    //应用安装时间
    public String installTime;
    //数据采集时间
    public String createTime;

    @Override
    public String toString() {
        return "AppInfo{" + "appName='" + appName + '\'' + ", appVersion='" + appVersion + '\'' + ", packageName='" + packageName + '\'' + '}';
    }
}
