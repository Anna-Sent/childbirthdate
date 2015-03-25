package com.anna.sent.soft.childbirthdate.utils;

import android.app.Activity;

import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.utils.DisplayUtils;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.Gender;
import com.google.ads.AdView;

public class AdUtils {
	public static void setupAd(Data data, Activity activity, int adViewId,
			int initH, int rowH) {
		int count = data.getSelectedMethodsCount();
		int expectedHeightDp = initH + count * rowH;

		int actualHeightDp = DisplayUtils.getScreenSizeInDpWrapped(activity).y;

		if (expectedHeightDp < actualHeightDp) {
			AdView mAdView = (AdView) activity.findViewById(adViewId);
			AdRequest mAdRequest = new AdRequest();
			mAdRequest.setGender(Gender.FEMALE);
			mAdView.loadAd(mAdRequest);
		}
	}
}