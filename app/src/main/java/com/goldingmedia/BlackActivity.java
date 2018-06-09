package com.goldingmedia;

import android.os.Bundle;
import android.view.MotionEvent;

import com.goldingmedia.jni.BacklightLevel;

public class BlackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(BacklightLevel.lcdGet() == 1){
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }
}
