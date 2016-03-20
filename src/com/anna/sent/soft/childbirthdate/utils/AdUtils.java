package com.anna.sent.soft.childbirthdate.utils;

import android.app.Activity;
import android.content.Context;

import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.utils.DisplayUtils;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.Gender;
import com.google.ads.AdView;

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
					AdRequest adRequest = new AdRequest();
					adRequest.addTestDevice("2600D922057328C48F2E6DBAB33639C1");
					adRequest.setGender(Gender.FEMALE);
					adView.loadAd(adRequest);
				}
			}
		}
	}
}