package com.benli.randomkeyboard.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppUtils {

    /**
     * 获取设备所有应用信息
     *
     * @param context
     * @return
     */
    public static List<AppInfoBean> getAppInfos(Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());
        //创建要返回的集合对象
        List<AppInfoBean> appInfos = new ArrayList<>();
        //获取手机中所有安装的应用集合
        List<ApplicationInfo> applicationInfos = context.getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        //遍历所有的应用集合
        for (ApplicationInfo info : applicationInfos) {
            AppInfoBean appInfo = new AppInfoBean();
            //获取应用的名称
            String appName = info.loadLabel(context.getPackageManager()).toString();
            //获取应用的包名
            String packageName = info.packageName;
            try {
                //获取应用的版本号
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                String app_version = packageInfo.versionName;
                appInfo.appVersion = app_version;
                appInfo.createTime = createTime;
                appInfo.installTime = simpleDateFormat.format(new Date(packageInfo.firstInstallTime));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            appInfo.appName = appName;
            appInfo.packageName = packageName;
            appInfos.add(appInfo);
        }
        return appInfos;
    }
}
