package com.anna.sent.soft.childbirthdate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.util.Log;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.utils.ActionBarUtils;
import com.anna.sent.soft.utils.LanguageUtils;
import com.anna.sent.soft.utils.NavigationUtils;
import com.anna.sent.soft.utils.TaskStackBuilderUtils;
import com.anna.sent.soft.utils.ThemeUtils;

@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	private void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	@SuppressWarnings("unused")
	private void log(String msg, boolean debug) {
		if (DEBUG && debug) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ThemeUtils.onActivityCreateSetTheme(this);

		super.onCreate(savedInstanceState);

		LanguageUtils.configurationChanged(this);

		addPreferencesFromResource(R.xml.preferences);

		ActionBarUtils.setupActionBar(this);

		createLanguagePreference();
		setupThemePreference();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavigationUtils.navigateUp(this);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void createLanguagePreference() {
		log("create language preference");
		PreferenceCategory category = (PreferenceCategory) findPreference(getString(R.string.pref_ui_settings_key));
		final ListPreference pref = new ListPreference(this);
		pref.setKey(getString(R.string.pref_language_key));
		String[] entries = getResources().getStringArray(R.array.language);
		pref.setEntries(entries);
		String[] entryValues = getResources().getStringArray(
				R.array.language_values);
		pref.setEntryValues(entryValues);
		String title = getString(R.string.pref_language_title);
		pref.setDialogTitle(title);
		pref.setTitle(title);
		int value = Settings.getLanguage(this);
		pref.setDefaultValue(String.valueOf(value));
		pref.setValue(String.valueOf(value));
		log(pref.getValue() + " " + pref.getEntry());
		pref.setSummary(pref.getEntry());
		pref.setOnPreferenceChangeListener(this);
		category.addPreference(pref);
	}

	private void setupThemePreference() {
		ListPreference pref = (ListPreference) findPreference(getString(R.string.pref_theme_key));
		pref.setSummary(pref.getEntry());
		pref.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		try {
			int value = Integer.parseInt(newValue.toString());

			if (preference.getKey().equals(
					getString(R.string.pref_language_key))) {
				int current = Settings.getLanguage(SettingsActivity.this);
				if (value != current) {
					log("language changed");
					Settings.setLanguage(SettingsActivity.this, value);
					TaskStackBuilderUtils.restartFromSettings(this);
					return true;
				}
			} else if (preference.getKey().equals(
					getString(R.string.pref_theme_key))) {
				int current = Settings.getTheme(SettingsActivity.this);
				if (value != current) {
					log("theme changed");
					Settings.setTheme(SettingsActivity.this, value);
					TaskStackBuilderUtils.restartFromSettings(this);
					return true;
				}
			}
		} catch (NumberFormatException e) {
		}

		return false;
	}

	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		return Settings.getSettings(this);
	}
}