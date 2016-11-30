package com.anna.sent.soft.childbirthdate.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.utils.DisplayUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.crash.FirebaseCrash;

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
            FirebaseCrash.logcat(Log.INFO, "ad", "ad: Device id is " + getTestDeviceId(activity));
            int count = data.getSelectedMethodsCount();
            int expectedHeightDp = initH + count * rowH;

            int actualHeightDp = DisplayUtils
                    .getScreenSizeInDpWrapped(activity).y;

            if (expectedHeightDp < actualHeightDp) {
                AdView adView = (AdView) activity.findViewById(adViewId);
                if (adView != null) {
                    if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity) != ConnectionResult.SUCCESS) {
                        FirebaseCrash.logcat(Log.INFO, "ad", "ad: GooglePlayServices not available");
                        return null;
                    }

                    MobileAds.initialize(activity.getApplicationContext(), activity.getString(R.string.adUnitId));
                    AdRequest adRequest = new AdRequest.Builder()
                            .setGender(AdRequest.GENDER_FEMALE)
                            .addTestDevice("2600D922057328C48F2E6DBAB33639C1")
                            .addTestDevice("9181DC11966389868E60DE66CAC818A3")
                            .addTestDevice("0A2245B8887D4B05DF59EB37AD741C46")
                            .addTestDevice("47D9C39F51DAC2173986C7832B6CAB57")
                            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                            .build();

                    FirebaseCrash.logcat(Log.INFO, "ad", "ad: isTestDevice = " + adRequest.isTestDevice(activity));

                    adView.loadAd(adRequest);
                    return adView;
                }
            }
        }

        return null;
    }

    private static String getTestDeviceId(Context context) {
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return getMD5(androidId);
    }

    private static String getMD5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            return String.format(Locale.US, "%032X", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            FirebaseCrash.logcat(Log.WARN, e.getMessage(), e.toString());
        }

        return "";
    }
}