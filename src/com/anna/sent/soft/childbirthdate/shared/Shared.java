package com.anna.sent.soft.childbirthdate.shared;

import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Shared {
	private static class SharedPreferencesWrapper implements SharedPreferences {
		private SharedPreferences mSettings;

		public SharedPreferencesWrapper(Context context, String name, int mode) {
			mSettings = context.getApplicationContext().getSharedPreferences(
					name, mode);
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
		return new SharedPreferencesWrapper(context, SETTINGS_FILE,
				Context.MODE_PRIVATE);
	}

	public static class Titles {
		public final static String EXTRA_POSITION = "com.anna.sent.soft.childbirthdate.position";
	}

	public static class Child {
		public static final String EXTRA_IS_STARTED_FROM_WIDGET = "com.anna.sent.soft.childbirthdate.isstartedfromwidget";
	}

	public static class Calculation {
		public static final int METHODS_COUNT = 5;
		public static final int UNKNOWN = 0, BY_LMP = 1, BY_OVULATION = 2,
				BY_ULTRASOUND = 3, BY_FIRST_APPEARANCE = 4,
				BY_FIRST_MOVEMENTS = 5;
	}

	public static class Saved {
		public static class Calculation {
			public static final String EXTRA_BY_LMP = "com.anna.sent.soft.childbirthdate.bylastmenstruationdate";
			public static final String EXTRA_LMP_MENSTRUAL_CYCLE_LEN = "com.anna.sent.soft.childbirthdate.menstrualcyclelen";
			public static final String EXTRA_LMP_LUTEAL_PHASE_LEN = "com.anna.sent.soft.childbirthdate.lutealphaselen";
			public static final String EXTRA_LMP_DATE = "com.anna.sent.soft.childbirthdate.lastmenstruationdate";
			public static final String EXTRA_BY_OVULATION = "com.anna.sent.soft.childbirthdate.byovulationdate";
			public static final String EXTRA_OVULATION_DATE = "com.anna.sent.soft.childbirthdate.ovulationdate";
			public static final String EXTRA_BY_ULTRASOUND = "com.anna.sent.soft.childbirthdate.byultrasound";
			public static final String EXTRA_ULTRASOUND_DATE = "com.anna.sent.soft.childbirthdate.ultrasounddate";
			public static final String EXTRA_ULTRASOUND_WEEKS = "com.anna.sent.soft.childbirthdate.weeks";
			public static final String EXTRA_ULTRASOUND_DAYS = "com.anna.sent.soft.childbirthdate.days";
			public static final String EXTRA_ULTRASOUND_IS_EMBRYONIC_AGE = "com.anna.sent.soft.childbirthdate.isfetal";
			public static final String EXTRA_BY_FIRST_APPEARANCE = "com.anna.sent.soft.childbirthdate.byfirstappearance";
			public static final String EXTRA_FIRST_APPEARANCE_DATE = "com.anna.sent.soft.childbirthdate.firstappearancedate";
			public static final String EXTRA_FIRST_APPEARANCE_WEEKS = "com.anna.sent.soft.childbirthdate.firstappearanceweeks";
			public static final String EXTRA_BY_FIRST_MOVEMENTS = "com.anna.sent.soft.childbirthdate.byfirstmovements";
			public static final String EXTRA_FIRST_MOVEMENTS_DATE = "com.anna.sent.soft.childbirthdate.firstmovementsdate";
			public static final String EXTRA_FIRST_MOVEMENTS_IS_FIRST_PREGNANCY = "com.anna.sent.soft.childbirthdate.isfirstpregnancy";
		}

		public static class Widget {
			public static final String EXTRA_CALCULATION_METHOD = "com.anna.sent.soft.childbirthdate.widget.calculatingmethod";
			public static final String EXTRA_COUNTDOWN = "com.anna.sent.soft.childbirthdate.widget.countdown";
			public static final String EXTRA_SHOW_CALCULATION_METHOD = "com.anna.sent.soft.childbirthdate.widget.showCalculatingMethod";
		}
	}
}
