package com.benli.randomkeyboard.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PackageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //app包名
        val packageName = intent?.data?.schemeSpecificPart ?: ""
        val action = intent?.action ?: ""

        Log.e("PackageReceiver", "【action: $action】, 【packageName: $packageName】")

        when (intent?.action) {
            Intent.ACTION_PACKAGE_ADDED -> {
                // 安装

            }
            Intent.ACTION_PACKAGE_REPLACED -> {
                // 覆盖安装

            }
            Intent.ACTION_PACKAGE_REMOVED -> {
                // 移除

            }
        }
    }
}