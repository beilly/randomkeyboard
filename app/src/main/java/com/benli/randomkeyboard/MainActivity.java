package com.benli.randomkeyboard;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.benli.keyboard.KeyboardHelper;

public class MainActivity extends AppCompatActivity implements CommonUtils.OnDynamicCodeDialogClickListener {

    private KeyboardHelper keyboardHelper;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyboardHelper = new KeyboardHelper(this)
                .addEditText((EditText) findViewById(R.id.edittext1))
                .addEditText((EditText) findViewById(R.id.edittext2))
                .setShouldRandom(true);

    }

    public void onClick(View v){
        alertDialog = CommonUtils.showDynamicCodeDialog(this, "提示信息", "请输入", false, true, -1, this);
        Window window = alertDialog.getWindow();
        final EditText edtWithdrawCashDynamicCode = (EditText) window.findViewById(R.id.edt_withdraw_cash_dynamic_code);
        keyboardHelper.addEditText(edtWithdrawCashDynamicCode);
    }

    @Override
    public void onGetCode(Button view, EditText editText) {

    }

    @Override
    public void onCancel(TextView view, EditText editText) {
        alertDialog.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void onSure(TextView view, EditText editText) {
        Toast.makeText(this, "onSure:" + editText.getText(), Toast.LENGTH_SHORT).show();
    }
}
