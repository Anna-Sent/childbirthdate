package com.anna.sent.soft.childbirthdate;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

public class CbdApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.adMobAppId));
    }
}
