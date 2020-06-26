package com.kosmo.mukja;

import android.app.Application;

import com.naver.maps.map.NaverMapSdk;

public class Map_app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("h620a2wjb4"));
    }
}