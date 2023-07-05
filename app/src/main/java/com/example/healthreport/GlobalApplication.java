package com.example.healthreport;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
     public void onCreate() {
        super.onCreate();
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "8f793362b79436d7b98fd038d0148799");
    }
}
