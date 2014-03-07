package com.anna.sent.soft.childbirthdate.shared;

import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.anna.sent.soft.childbirthdate.R;

public class Settings {
	private static class SharedPreferencesWrapper implements SharedPreferences {
		private SharedPreferences mSettings;

		public SharedPreferencesWrapper(SharedPreferences settings) {
			mSettings = settings;
		}

		@Override
		public Map<String, ?> getAll() {
			return mSettings.getAll();
		}

		@Override
		public String getString(String key, String defValue) {
			try {
				return mSettings.getString(key, defValue);
			} catch (ClassCastException e) {
				return defValue;
			}
		}

		@SuppressLint("NewApi")
		@Override
		public Set<String> getStringSet(String key, Set<String> defValues) {
			try {
				return mSettings.getStringSet(key, defValues);
			} catch (ClassCastException e) {
				return defValues;
			}
		}

		@Override
		public int getInt(String key, int defValue) {
			try {
				return mSettings.getInt(key, defValue);
			} catch (ClassCastException e) {
				return defValue;
			}
		}

		@Override
		public long getLong(String key, long defValue) {
			try {
				return mSettings.getLong(key, defValue);
			} catch (ClassCastException e) {
				return defValue;
			}
		}

		@Override
		public float getFloat(String key, float defValue) {
			try {
				return mSettings.getFloat(key, defValue);
			} catch (ClassCastException e) {
				return defValue;
			}
		}

		@Override
		public boolean getBoolean(String key, boolean defValue) {
			try {
				return mSettings.getBoolean(key, defValue);
			} catch (ClassCastException e) {
				return defValue;
			}
		}

		@Override
		public boolean contains(String key) {
			return mSettings.contains(key);
		}

		@Override
		public Editor edit() {
			return mSettings.edit();
		}

		@Override
		public void registerOnSharedPreferenceChangeListener(
				OnSharedPreferenceChangeListener listener) {
			mSettings.registerOnSharedPreferenceChangeListener(listener);
		}

		@Override
		public void unregisterOnSharedPreferenceChangeListener(
				OnSharedPreferenceChangeListener listener) {
			mSettings.registerOnSharedPreferenceChangeListener(listener);
		}
	}

	private static final String SETTINGS_FILE = "childbirthdatesettings";

	public static SharedPreferences getSettings(Context context) {
		return new SharedPreferencesWrapper(context.getApplicationContext()
				.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE));
	}

	private static SharedPreferences getDefaultSettings(Context context) {
		return new SharedPreferencesWrapper(
				PreferenceManager.getDefaultSharedPreferences(context));
	}

	// com.anna.sent.soft.childbirthdate.themeid
	public static final String KEY_PREF_THEME = "pref_theme";

	public static int getTheme(Context context) {
		SharedPreferences settings = getDefaultSettings(context);
		int defaultValue = context.getResources().getInteger(
				R.integer.defaultTheme);
		String value = settings.getString(KEY_PREF_THEME, "");
		int result = defaultValue;
		if (!value.equals("")) {
			try {
				result = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				result = defaultValue;
			}
		}

		return result;
	}

	public static void setTheme(Context context, int value) {
		SharedPreferences settings = getDefaultSettings(context);
		Editor editor = settings.edit();
		editor.putString(KEY_PREF_THEME, String.valueOf(value));
		editor.commit();
	}
}
