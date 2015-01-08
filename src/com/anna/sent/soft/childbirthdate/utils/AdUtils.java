package com.anna.sent.soft.childbirthdate.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.anna.sent.soft.childbirthdate.data.Data;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.Gender;
import com.google.ads.AdView;

public class AdUtils {
	public static void setupAd(Data data, Activity activity, int adViewId,
			int initH, int rowH) {
		int count = data.getSelectedMethodsCount();
		int expectedHeightDp = initH + count * rowH;

		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int heightPx = activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.heightPixels
				: metrics.widthPixels;
		int actualHeightDp = (int) (heightPx / metrics.density);

		if (expectedHeightDp < actualHeightDp) {
			AdView mAdView = (AdView) activity.findViewById(adViewId);
			AdRequest mAdRequest = new AdRequest();
			mAdRequest.setGender(Gender.FEMALE);
			mAdView.loadAd(mAdRequest);
		}
	}
}