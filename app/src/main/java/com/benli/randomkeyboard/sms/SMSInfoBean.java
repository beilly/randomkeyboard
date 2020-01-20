package com.benli.randomkeyboard.sms;

import androidx.annotation.Keep;

@Keep
public class SMSInfoBean {
    public String name;
    public String phoneNumber;
    public String body;
    public String date;
    public int type;
    public String customerPhone;
    public String createTime;

    @Override
    public String toString() {
        return name + "---" + phoneNumber + "---" + body + "---" + date + "---" + type;
    }
}
