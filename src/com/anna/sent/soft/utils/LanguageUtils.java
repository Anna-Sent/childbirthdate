package com.anna.sent.soft.utils;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.anna.sent.soft.childbirthdate.shared.Settings;

public class LanguageUtils {
	public static void configurationChanged(Activity activity) {
		if (Settings.isLanguageSetByUser(activity)) {
			Configuration config = new Configuration(activity.getResources()
					.getConfiguration());
			config.locale = new Locale(Settings.getLocale(activity));
			Locale.setDefault(config.locale);
			Resources baseResources = activity.getBaseContext().getResources();
			baseResources.updateConfiguration(config,
					baseResources.getDisplayMetrics());
		}
	}
}