package com.anna.sent.soft.utils;

import android.app.Activity;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public class ThemeUtils {
	public final static int DARK_THEME = 0;
	public final static int LIGHT_THEME = 1;

	/**
	 * Set the theme of the activity, according to the configuration.
	 */
	public static void onActivityCreateSetTheme(Activity activity) {
		switch (Shared.getTheme(activity)) {
		case LIGHT_THEME:
			activity.setTheme(R.style.AppThemeLight);
			break;
		case DARK_THEME:
		default:
			activity.setTheme(R.style.AppTheme);
			break;
		}
	}

	/**
	 * Set the theme of the dialog-style activity, according to the
	 * configuration.
	 */
	public static void onDialogStyleActivityCreateSetTheme(Activity activity) {
		switch (Shared.getTheme(activity)) {
		case LIGHT_THEME:
			activity.setTheme(R.style.DialogThemeLight);
			break;
		case DARK_THEME:
		default:
			activity.setTheme(R.style.DialogTheme);
			break;
		}
	}
}
