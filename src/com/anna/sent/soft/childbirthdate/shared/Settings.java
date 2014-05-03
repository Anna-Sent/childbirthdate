package com.anna.sent.soft.childbirthdate.shared;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.Age;
import com.anna.sent.soft.childbirthdate.age.Days;
import com.anna.sent.soft.childbirthdate.age.ISetting;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;
import com.anna.sent.soft.childbirthdate.age.SettingsParser;
import com.anna.sent.soft.childbirthdate.sicklist.SickListConstants;

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

	public static List<LocalizableObject> getList(Context context,
			Class<? extends ISetting> cls) {
		if (cls == Age.class) {
			return getAge(context);
		} else if (cls == Days.class) {
			return getDays(context);
		}

		return null;
	}

	public static List<LocalizableObject> getDays(Context context) {
		return getList(context, R.string.pref_sick_list_days_key,
				SickListConstants.Days.DEFAULT_VALUE, new Days());
	}

	public static List<LocalizableObject> getAge(Context context) {
		return getList(context, R.string.pref_sick_list_age_key,
				SickListConstants.Age.DEFAULT_VALUE, new Age());
	}

	private static List<LocalizableObject> getList(Context context,
			int keyStringRes, String defaultValue, ISetting element) {
		SharedPreferences settings = getSettings(context);
		String value = settings.getString(context.getString(keyStringRes),
				defaultValue);
		List<LocalizableObject> result = SettingsParser.toList(value, element);
		return result;
	}

	public static int getTheme(Context context) {
		SharedPreferences settings = getSettings(context);
		int defaultValue = context.getResources().getInteger(
				R.integer.defaultTheme);
		String value = settings.getString(
				context.getString(R.string.pref_theme_key), "");
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
		SharedPreferences settings = getSettings(context);
		Editor editor = settings.edit();
		editor.putString(context.getString(R.string.pref_theme_key),
				String.valueOf(value));
		editor.commit();
	}

	public static int getLanguage(Context context) {
		int result = getDefaultLanguage(context);
		if (Settings.isLanguageSetByUser(context)) {
			SharedPreferences settings = getSettings(context);
			String value = settings.getString(
					context.getResources()
							.getString(R.string.pref_language_key), "");
			if (!value.equals("")) {
				try {
					result = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
			}

		}

		return result;
	}

	/**
	 * Gets language set in configuration and returns the language's index in
	 * supported languages array.
	 * 
	 * @return index between 0 and (count of supported languages - 1) - UI
	 *         language index
	 */
	private static int getDefaultLanguage(Context context) {
		String[] supportedLocales = context.getResources().getStringArray(
				R.array.locales);
		String currentLocale = context.getResources().getConfiguration().locale
				.getLanguage();
		for (int i = 0; i < supportedLocales.length; ++i) {
			if (supportedLocales[i].equals(currentLocale)) {
				return i;
			}
		}

		return 0; // 0 is ru, default UI language
	}

	public static String getLocale(Context context) {
		int languageId = getLanguage(context);
		String[] supportedLocales = context.getResources().getStringArray(
				R.array.locales);
		return supportedLocales[languageId];
	}

	private static final String KEY_PREF_IS_LANGUAGE_SET_BY_USER = "com.anna.sent.soft.childbirthdate.islanguagesetbyuser";

	public static boolean isLanguageSetByUser(Context context) {
		SharedPreferences settings = getSettings(context);
		return settings.getBoolean(KEY_PREF_IS_LANGUAGE_SET_BY_USER, false);
	}

	public static void setLanguage(Context context, int value) {
		SharedPreferences settings = getSettings(context);
		Editor editor = settings.edit();
		editor.putString(context.getString(R.string.pref_language_key),
				String.valueOf(value));
		editor.putBoolean(KEY_PREF_IS_LANGUAGE_SET_BY_USER, true);
		editor.commit();
	}

	public static void clear(Context context) {
		getSettings(context).edit().clear().commit();
	}
}