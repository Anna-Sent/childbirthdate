package com.anna.sent.soft.childbirthdate.utils;

import android.app.Activity;
import android.content.Context;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.utils.DisplayUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AdUtils {
    private static boolean isAdFreeVersion(Context context) {
        return context.getPackageName().endsWith(".pro");
    }

    public static void setupAd(Data data, Activity activity, int adViewId,
                               int initH, int rowH) {
        if (!isAdFreeVersion(activity)) {
            int count = data.getSelectedMethodsCount();
            int expectedHeightDp = initH + count * rowH;

            int actualHeightDp = DisplayUtils
                    .getScreenSizeInDpWrapped(activity).y;

            if (expectedHeightDp < actualHeightDp) {
                AdView adView = (AdView) activity.findViewById(adViewId);
                if (adView != null) {
                    MobileAds.initialize(activity.getApplicationContext(), activity.getString(R.string.adUnitId));
                    AdRequest adRequest = new AdRequest.Builder()
                            .setGender(AdRequest.GENDER_FEMALE)
                            .addTestDevice("2600D922057328C48F2E6DBAB33639C1")
                            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                            .build();
                    adView.loadAd(adRequest);
                }
            }
        }
    }
}