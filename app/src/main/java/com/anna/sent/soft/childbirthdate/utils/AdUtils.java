package com.anna.sent.soft.childbirthdate.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.BuildConfig;
import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.utils.DisplayUtils;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class AdUtils {
    private static boolean isAdFreeVersion(Context context) {
        return context.getPackageName().endsWith(".pro");
    }

    public static AdView setupAd(Data data, Activity activity, int adViewId,
                                 int initH, int rowH) {
        if (!isAdFreeVersion(activity)) {
            MyLog.getInstance().logcat(Log.INFO, "ad: Device id is " + getTestDeviceId(activity));
            int count = data.getSelectedMethodsCount();
            int expectedHeightDp = initH + count * rowH;

            int actualHeightDp = DisplayUtils.getScreenSizeInDp().y;

            if (expectedHeightDp < actualHeightDp) {
                AdView adView = activity.findViewById(adViewId);
                if (adView != null) {
                    MobileAds.initialize(activity.getApplicationContext(), activity.getString(R.string.adUnitId));
                    com.google.android.gms.ads.AdRequest.Builder adRequestBuilder = new com.google.android.gms.ads.AdRequest.Builder()
                            .setGender(com.google.android.gms.ads.AdRequest.GENDER_FEMALE);

                    if (BuildConfig.DEBUG) {
                        adRequestBuilder
                                .addTestDevice("2600D922057328C48F2E6DBAB33639C1")
                                .addTestDevice("9181DC11966389868E60DE66CAC818A3")
                                .addTestDevice("0A2245B8887D4B05DF59EB37AD741C46")
                                .addTestDevice("47D9C39F51DAC2173986C7832B6CAB57")
                                .addTestDevice("2F2B82AD62F209D48AFC29A0C88065FA")
                                .addTestDevice(com.google.android.gms.ads.AdRequest.DEVICE_ID_EMULATOR);
                    }

                    com.google.android.gms.ads.AdRequest adRequest = adRequestBuilder.build();

                    MyLog.getInstance().logcat(Log.INFO, "ad: isTestDevice = " + adRequest.isTestDevice(activity));

                    adView.loadAd(adRequest);
                    return adView;
                }
            }
        }

        return null;
    }

    private static String getTestDeviceId(Context context) {
        @SuppressWarnings("HardwareIds")
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return getMD5(androidId);
    }

    private static String getMD5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            return String.format(Locale.US, "%032X", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            MyLog.getInstance().report(e);
        }

        return "";
    }
}
