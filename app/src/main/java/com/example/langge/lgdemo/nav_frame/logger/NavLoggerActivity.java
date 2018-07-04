package com.example.langge.lgdemo.nav_frame.logger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.langge.lgdemo.R;
import com.orhanobut.logger.Logger;

public class NavLoggerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_logger);

        Logger.v("hello world");
        Logger.d("hello world");
        Logger.i("hello world");
        Logger.w("hello world");
        Logger.e("hello world");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.v("hello world");
                Logger.d("hello world");
                Logger.i("hello world");
                Logger.w("hello world");
                Logger.e("hello world");
            }
        }).start();
    }


}
