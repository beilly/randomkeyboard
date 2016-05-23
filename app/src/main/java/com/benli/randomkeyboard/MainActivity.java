package com.benli.randomkeyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.benli.keyboard.KeyboardHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new KeyboardHelper(this)
                .addEditText((EditText) findViewById(R.id.edittext1))
                .addEditText((EditText) findViewById(R.id.edittext2))
                .setShouldRandom(true);
    }
}
