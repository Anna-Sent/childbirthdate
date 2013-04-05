package com.anna.sent.soft.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public class Utils {
	private static final String EXTRA_GUI_THEME_ID = "com.anna.sent.soft.childbirthdate.themeid";

	public final static int DARK_THEME = 0;
	public final static int LIGHT_THEME = 1;
	public final static int DEFAULT_THEME = DARK_THEME;

	private static int themeId = DEFAULT_THEME;
	private static Bundle state = new Bundle();

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
			SharedPreferences settings = Shared.getSettings(activity);
			Editor editor = settings.edit();
			editor.putInt(EXTRA_GUI_THEME_ID, themeId);
			editor.commit();

			if (activity instanceof StateSaver) {
				state.clear();
				((StateSaver) activity).onSaveInstanceState(state);
			}

			activity.finish();

			Intent intent = new Intent(activity, activity.getClass());
			intent.putExtras(state);
			activity.startActivity(intent);
		}
	}

	/**
	 * Set the theme of the activity, according to the configuration.
	 */
	public static void onActivityCreateSetTheme(Activity activity) {
		SharedPreferences settings = Shared.getSettings(activity);
		themeId = settings.getInt(EXTRA_GUI_THEME_ID, DEFAULT_THEME);
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
}
