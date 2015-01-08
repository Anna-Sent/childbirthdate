package com.anna.sent.soft.childbirthdate.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

import com.anna.sent.soft.childbirthdate.data.Data;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.Gender;
import com.google.ads.AdView;

public class AdUtils {
	private static int getScreenHeightDp(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int heightPx = metrics.heightPixels;
		int heightDp = (int) (heightPx / metrics.density);
		return heightDp;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private static int getScreenHeightDpNew(Activity activity) {
		int heightDp = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			heightDp = activity.getResources().getConfiguration().screenHeightDp;
			if (heightDp == Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
				heightDp = 0;
			}
		}

		if (heightDp == 0) {
			heightDp = getScreenHeightDp(activity);
		}

		return heightDp;
	}

	public static void setupAd(Data data, Activity activity, int adViewId,
			int initH, int rowH) {
		int count = data.getSelectedMethodsCount();
		int expectedHeightDp = initH + count * rowH;

		int actualHeightDp = getScreenHeightDpNew(activity);

		if (expectedHeightDp < actualHeightDp) {
			AdView mAdView = (AdView) activity.findViewById(adViewId);
			AdRequest mAdRequest = new AdRequest();
			mAdRequest.setGender(Gender.FEMALE);
			mAdView.loadAd(mAdRequest);
		}
	}
}