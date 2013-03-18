package com.anna.sent.soft.utils;

import com.anna.sent.soft.utils.StateSaver;
import com.anna.sent.soft.childbirthdate.R;

import android.app.Activity;
import android.content.Intent;

public class Utils {
	public final static int DARK_THEME = 0;
	public final static int LIGHT_THEME = 1;

	private static int themeId = DARK_THEME;

	public static int getThemeId() {
		return themeId;
	}

	/**
	 * Set the theme of the Activity, and restart it by creating a new Activity
	 * of the same type.
	 */
	public static void changeToTheme(Activity activity, int themeId) {
		if (themeId != Utils.themeId) {
			Utils.themeId = themeId;

			Intent intent = new Intent(activity, activity.getClass());
			if (activity instanceof StateSaver) {
				((StateSaver) activity).onSaveState(intent);
			}

			activity.finish();
			activity.startActivity(intent);
		}
	}

	/**
	 * Set the theme of the activity, according to the configuration.
	 */
	public static void onActivityCreateSetTheme(Activity activity) {
		switch (themeId) {
		case LIGHT_THEME:
			activity.setTheme(R.style.AppThemeLight);
			break;
		case DARK_THEME:
		default:
			activity.setTheme(R.style.AppTheme);
			break;
		}
	}

	public static void onActivityCreateSetTheme(Activity activity, int themeId) {
		Utils.themeId = themeId;
		onActivityCreateSetTheme(activity);
	}
}
