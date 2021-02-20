package com.example.testhwvod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testhwvod.huawei.vod.DaVodClient;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DaVodClient.upload();
            }
        }).start();
    }
}