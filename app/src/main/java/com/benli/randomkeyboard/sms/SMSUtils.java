package com.benli.randomkeyboard.sms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SMSUtils {

    /**
     * 获取短信记录
     *
     * @param context 上下文对象
     * @return 短信记录
     */
    public static List<SMSInfoBean> getSmsInPhone(Context context) {
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        final String SMS_URI_INBOX = "content://sms/";
        List<SMSInfoBean> list = new ArrayList<>();
        try {
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Uri uri = Uri.parse(SMS_URI_INBOX);
            Cursor cursor = cr.query(uri, projection, null, null, "date desc");

            if (cursor == null)
                return list;
            while (cursor.moveToNext()) {
                SMSInfoBean messageInfo = new SMSInfoBean();
                // -----------------------信息----------------
                int nameColumn = cursor.getColumnIndex("person");// 联系人姓名列表序号
                int phoneNumberColumn = cursor.getColumnIndex("address");// 手机号
                int smsbodyColumn = cursor.getColumnIndex("body");// 短信内容
                int dateColumn = cursor.getColumnIndex("date");// 日期
                int typeColumn = cursor.getColumnIndex("type");// 收发类型 1表示接受 2表示发送
                String nameId = cursor.getString(nameColumn);
                String phoneNumber = cursor.getString(phoneNumberColumn);
                String smsbody = cursor.getString(smsbodyColumn);
                Date d = new Date(Long.parseLong(cursor.getString(dateColumn)));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd " + "\n" + "HH:mm:ss");
                String date = dateFormat.format(d);
                int type = cursor.getInt(typeColumn);
                messageInfo.name = nameId;
                messageInfo.phoneNumber = phoneNumber;
                messageInfo.body = smsbody;
                messageInfo.date = date;
                messageInfo.type = type;
                messageInfo.customerPhone = getDevicePhoneNumber(context);
                messageInfo.createTime = createTime;
                list.add(messageInfo);
            }

        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取本机手机号码
     * @param context
     * @return
     */
    public static String getDevicePhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        StringBuilder phoneNum = new StringBuilder();
        SubscriptionManager sm = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (null != sm){
            List<SubscriptionInfo> subscriptionInfos = sm.getActiveSubscriptionInfoList();
            for (SubscriptionInfo info : subscriptionInfos){
                int subId = info.getSubscriptionId();
                try {
                    Method getLine1Number = TelephonyManager.class.getMethod("getLine1Number", int.class);
                    String phone  = (String) getLine1Number.invoke(tm, subId);
                    if (!TextUtils.isEmpty(phone)){
                        if (phoneNum.length() <= 0){
                            phoneNum.append(phone);
                        }else {
                            phoneNum.append(",").append(phone);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (phoneNum.length() <= 0){
            return tm.getLine1Number();
        }else {
            return phoneNum.toString();
        }
    }
}
