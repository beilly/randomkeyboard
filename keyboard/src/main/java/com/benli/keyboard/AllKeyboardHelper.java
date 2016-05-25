package com.benli.keyboard;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.inputmethodservice.Keyboard.Key;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by shibenli on 2016/5/24.
 */
public class AllKeyboardHelper extends KeyboardHelper{
    protected Keyboard keySymbol;

    private boolean isCaps = false;
    private boolean isSymbols = false;

    public AllKeyboardHelper(Activity mActivity) {
        super(mActivity);
    }

    public AllKeyboardHelper(Activity mActivity, EditText editText) {
        super(mActivity, editText);
    }

    /**
     * 初始化键盘
     * @param mActivity
     * @return
     */
    protected void initKeyboar(Activity mActivity){
        keyMain = new Keyboard(mActivity.getApplicationContext(), R.xml.keyboard_chars);
        keySymbol = new Keyboard(mActivity.getApplicationContext(), R.xml.keyboard_symbols);
        keyboardView.setKeyboard(keyMain);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(this);
    }

    public void onKey(int primaryCode, int[] keyCodes) {
        super.onKey(primaryCode, keyCodes);
    }

    @Override
    protected void randomKey() {
        if (isSymbols){
            randomSymbolskey();
        }else {
            randomCharskey();
        }
    }

    private void randomSymbolskey(){

    }

    private void randomCharskey() {
        List<Key> keyList = keyMain.getKeys();
        // 查找出a-z的数字键
        List<Key> newkeyList = new ArrayList<Key>();
        for (int i = 0; i < keyList.size(); i++) {
            if (keyList.get(i).label != null
                    && isword(keyList.get(i).label.toString())) {
                newkeyList.add(keyList.get(i));
            }
        }
        // 数组长度
        int count = newkeyList.size();
        // 结果集

        List<KeyModel> resultList = new ArrayList<KeyModel>();
        // 用一个LinkedList作为中介
        LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
        // 初始化temp
        for (int i = 0; i < count; i++) {
            temp.add(new KeyModel(97 + i, "" + (char) (97 + i)));
        }
        // 取数
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(count - i);
            resultList.add(new KeyModel(temp.get(num).getCode(), temp.get(num)
                    .getLable()));
            temp.remove(num);
        }
        for (int i = 0; i < newkeyList.size(); i++) {
            newkeyList.get(i).label = resultList.get(i).getLable();
            newkeyList.get(i).codes[0] = resultList.get(i).getCode();
        }
        keyboardView.setKeyboard(keyMain);
    }
}
