package com.benli.randomkeyboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shibenli on 2016/5/23.
 */
public class CommonUtils {
    private CommonUtils() {
    }

    /**
     * 显示发送验证码的弹出框
     *
     * @param context   显示AlertDialog的上下文对象
     * @param title     设置title，为null或者""时使用默认的
     * @param hint      设置验证码输入框的hint，为null时使用默认
     * @param inputAuto 是否自动弹出键盘
     * @param inputType 验证码输入框的输入类型
     * @param listener  AlertDialog的事件监听
     * @return
     */
    public static PopupWindow showDynamicCodeWindow(final Context context, String title, String hint, boolean inputAuto, boolean hasButton, int inputType, final OnDynamicCodeDialogClickListener listener, View anchor) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.withdraw_cash_contract_dynamic_code_dialog, null);

        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width3 = dm.widthPixels;
        int height3 = dm.heightPixels;

        final PopupWindow contractsDynamicCodeDialog = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        contractsDynamicCodeDialog.setFocusable(false);
        contractsDynamicCodeDialog.setOutsideTouchable(true);

        final TextView tvWithdrawCashDynamicDialogTitle = (TextView) contentView.findViewById(R.id.dialog_title);
        final Button btnWithdrawCashDynamicGetCode = (Button) contentView.findViewById(R.id.btn_withdraw_cash_dynamic_getCode);
        if (!hasButton)
            btnWithdrawCashDynamicGetCode.setVisibility(View.GONE);
        final EditText edtWithdrawCashDynamicCode = (EditText) contentView.findViewById(R.id.edt_withdraw_cash_dynamic_code);
        final TextView tvWithdrawCashDynamicDialogCancle = (TextView) contentView.findViewById(R.id.tv_withdraw_cash_dynamic_dialog_cancle);
        final TextView tvWithdrawCashDynamicDialogSure = (TextView) contentView.findViewById(R.id.tv_withdraw_cash_dynamic_dialog_sure);

        //设置标题和hint和输入框的类型
        if (!TextUtils.isEmpty(title))
            tvWithdrawCashDynamicDialogTitle.setText(title);
        if (hint != null)
            edtWithdrawCashDynamicCode.setHint(hint);
        edtWithdrawCashDynamicCode.setInputType(inputType);

        contractsDynamicCodeDialog.showAtLocation(anchor.getRootView(), Gravity.BOTTOM, 0, 0);

        //设置按钮的点击事件
        if (listener != null) {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_withdraw_cash_dynamic_getCode:
                            listener.onGetCode(btnWithdrawCashDynamicGetCode, edtWithdrawCashDynamicCode);
                            break;
                        case R.id.tv_withdraw_cash_dynamic_dialog_cancle:
                            listener.onCancel(tvWithdrawCashDynamicDialogCancle, edtWithdrawCashDynamicCode);
                            break;
                        case R.id.tv_withdraw_cash_dynamic_dialog_sure:
                            if (TextUtils.isEmpty(edtWithdrawCashDynamicCode.getText())) {
                                Toast.makeText(context, "不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                listener.onSure(tvWithdrawCashDynamicDialogSure, edtWithdrawCashDynamicCode);
                            }
                            break;
                    }

                }
            };
            btnWithdrawCashDynamicGetCode.setOnClickListener(onClickListener);
            tvWithdrawCashDynamicDialogCancle.setOnClickListener(onClickListener);
            tvWithdrawCashDynamicDialogSure.setOnClickListener(onClickListener);
        }

        //Dialog隐藏的时候自动隐藏键盘
        contractsDynamicCodeDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                InputTools.HideKeyboard(contractsDynamicCodeDialog.getContentView());
                if (listener != null)
                    listener.onDismiss(null);
            }
        });

        return contractsDynamicCodeDialog;
    }


    /**
     * 显示发送验证码的弹出框
     *
     * @param context   显示AlertDialog的上下文对象
     * @param title     设置title，为null或者""时使用默认的
     * @param hint      设置验证码输入框的hint，为null时使用默认
     * @param inputAuto 是否自动弹出键盘
     * @param inputType 验证码输入框的输入类型
     * @param listener  AlertDialog的事件监听
     * @return
     */
    public static AlertDialog showDynamicCodeDialog(final Context context, String title, String hint, boolean inputAuto, boolean hasButton, int inputType, final OnDynamicCodeDialogClickListener listener) {
        final AlertDialog contractsDynamicCodeDialog = new AlertDialog.Builder(context).create();
        contractsDynamicCodeDialog.show();

        Window window = contractsDynamicCodeDialog.getWindow();
        window.setContentView(R.layout.withdraw_cash_contract_dynamic_code_dialog);
        final TextView tvWithdrawCashDynamicDialogTitle = (TextView) window.findViewById(R.id.dialog_title);
        final Button btnWithdrawCashDynamicGetCode = (Button) window.findViewById(R.id.btn_withdraw_cash_dynamic_getCode);
        if (!hasButton)
            btnWithdrawCashDynamicGetCode.setVisibility(View.GONE);
        final EditText edtWithdrawCashDynamicCode = (EditText) window.findViewById(R.id.edt_withdraw_cash_dynamic_code);
        final TextView tvWithdrawCashDynamicDialogCancle = (TextView) window.findViewById(R.id.tv_withdraw_cash_dynamic_dialog_cancle);
        final TextView tvWithdrawCashDynamicDialogSure = (TextView) window.findViewById(R.id.tv_withdraw_cash_dynamic_dialog_sure);

        //设置标题和hint和输入框的类型
        if (!TextUtils.isEmpty(title))
            tvWithdrawCashDynamicDialogTitle.setText(title);
        if (hint != null)
            edtWithdrawCashDynamicCode.setHint(hint);
        edtWithdrawCashDynamicCode.setInputType(inputType);

        //设置按钮的点击事件
        if (listener != null) {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn_withdraw_cash_dynamic_getCode:
                            listener.onGetCode(btnWithdrawCashDynamicGetCode, edtWithdrawCashDynamicCode);
                            break;
                        case R.id.tv_withdraw_cash_dynamic_dialog_cancle:
                            listener.onCancel(tvWithdrawCashDynamicDialogCancle, edtWithdrawCashDynamicCode);
                            break;
                        case R.id.tv_withdraw_cash_dynamic_dialog_sure:
                            if (TextUtils.isEmpty(edtWithdrawCashDynamicCode.getText())) {
                                Toast.makeText(context, "不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                listener.onSure(tvWithdrawCashDynamicDialogSure, edtWithdrawCashDynamicCode);
                            }
                            break;
                    }

                }
            };
            btnWithdrawCashDynamicGetCode.setOnClickListener(onClickListener);
            tvWithdrawCashDynamicDialogCancle.setOnClickListener(onClickListener);
            tvWithdrawCashDynamicDialogSure.setOnClickListener(onClickListener);
        }

        //Dialog隐藏的时候自动隐藏键盘
        contractsDynamicCodeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InputTools.HideKeyboard(contractsDynamicCodeDialog.getWindow().getDecorView());
                if (listener != null)
                    listener.onDismiss(dialog);
            }
        });

        WindowManager.LayoutParams params = contractsDynamicCodeDialog.getWindow().getAttributes();
        //是否自动弹出键盘
        if (inputAuto) {
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
        }

        params.width = CommonUtils.Dp2Px(context, 260);
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;//就是这个属性导致不能获取焦点,默认的是FLAG_NOT_FOCUSABLE,故名思义不能获取输入焦点
        contractsDynamicCodeDialog.getWindow().setAttributes(params);

        return contractsDynamicCodeDialog;
    }

    public interface OnDynamicCodeDialogClickListener {
        /**
         * 获取验证码
         *
         * @param view     获取验证码的按钮
         * @param editText 当前填写验证码的EditText
         */
        public void onGetCode(Button view, EditText editText);

        /**
         * 点击关闭按钮
         *
         * @param view     关闭按钮
         * @param editText 当前填写验证码的EditText
         */
        public void onCancel(TextView view, EditText editText);

        /**
         * 弹出框隐藏
         *
         * @param dialog
         */
        public void onDismiss(DialogInterface dialog);

        /**
         * 点击确定按钮
         *
         * @param view     确定按钮
         * @param editText 当前填写验证码的EditText
         */
        public void onSure(TextView view, EditText editText);
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
