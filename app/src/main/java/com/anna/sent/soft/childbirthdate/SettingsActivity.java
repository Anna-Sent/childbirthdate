package com.anna.sent.soft.childbirthdate;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.util.Log;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.childbirthdate.sicklist.SickListAgePreference;
import com.anna.sent.soft.childbirthdate.sicklist.SickListDaysPreference;
import com.anna.sent.soft.childbirthdate.utils.MyLog;
import com.anna.sent.soft.utils.ActionBarUtils;
import com.anna.sent.soft.utils.LanguageUtils;
import com.anna.sent.soft.utils.NavigationUtils;
import com.anna.sent.soft.utils.TaskStackBuilderUtils;
import com.anna.sent.soft.utils.ThemeUtils;

@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity implements
        OnPreferenceChangeListener, OnSharedPreferenceChangeListener {
    private String wrapMsg(String msg) {
        return getClass().getSimpleName() + '@'
                + Integer.toHexString(hashCode()) + ": " + msg;
    }

    private void log(String msg) {
        MyLog.getInstance().logcat(Log.DEBUG, wrapMsg(msg));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ThemeUtils.setupThemeBeforeOnActivityCreate(this,
                Settings.settingsTheme.getStyle(this, R.array.style,
                        R.style.AppTheme));

        super.onCreate(savedInstanceState);

        LanguageUtils.setupLanguageAfterOnActivityCreate(this,
                Settings.settingsLanguage.isLanguageSetByUser(this),
                Settings.settingsLanguage.getLocale(this));

        setTitle(R.string.settings);

        addPreferencesFromResource(R.xml.preferences);

        ActionBarUtils.setupActionBar(this);

        createLanguagePreference();
        setupThemePreference();

        setupSickListDaysPreference();
        setupSickListAgePreference();
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
        pref.setKey(Settings.settingsLanguage.getLanguageKey(this));
        String[] entries = getResources().getStringArray(R.array.language);
        pref.setEntries(entries);
        String[] entryValues = getResources().getStringArray(
                R.array.language_values);
        pref.setEntryValues(entryValues);
        String title = getString(R.string.pref_language_title);
        pref.setDialogTitle(title);
        pref.setTitle(title);
        int value = Settings.settingsLanguage.getLanguage(this);
        pref.setDefaultValue(String.valueOf(value));
        pref.setValue(String.valueOf(value));
        Settings.settingsLanguage.setLanguage(this, value);
        log(pref.getValue() + " " + pref.getEntry());
        pref.setSummary(pref.getEntry());
        pref.setOnPreferenceChangeListener(this);
        category.addPreference(pref);
    }

    private void setupThemePreference() {
        ListPreference pref = (ListPreference) findPreference(Settings.settingsTheme
                .getThemeKey(this));
        pref.setSummary(pref.getEntry());
        pref.setOnPreferenceChangeListener(this);
    }

    private void setupSickListDaysPreference() {
        SickListDaysPreference prefDays = (SickListDaysPreference) findPreference(getString(R.string.pref_sick_list_days_key));
        prefDays.setSummary(prefDays.getValueToShow());
    }

    private void setupSickListAgePreference() {
        SickListAgePreference prefAge = (SickListAgePreference) findPreference(getString(R.string.pref_sick_list_age_key));
        prefAge.setSummary(prefAge.getValueToShow());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (key.equals(Settings.settingsLanguage.getLanguageKey(this))) {
            try {
                int value = Integer.parseInt(newValue.toString());
                int current = Settings.settingsLanguage.getLanguage(this);
                if (value != current) {
                    log("language changed");
                    Settings.settingsLanguage.setLanguage(this, value);
                    TaskStackBuilderUtils.restartFromSettings(this,
                            MainActivity.class,
                            MainActivity.EXTRA_CONFIGURATION_CHANGED);
                    return true;
                }
            } catch (NumberFormatException e) {
                MyLog.getInstance().logcat(Log.ERROR, e.toString());
            }
        } else if (key.equals(Settings.settingsTheme.getThemeKey(this))) {
            try {
                int value = Integer.parseInt(newValue.toString());
                int current = Settings.settingsTheme.getTheme(this);
                if (value != current) {
                    log("theme changed");
                    Settings.settingsTheme.setTheme(this, value);
                    TaskStackBuilderUtils.restartFromSettings(this,
                            MainActivity.class,
                            MainActivity.EXTRA_CONFIGURATION_CHANGED);
                    return true;
                }
            } catch (NumberFormatException e) {
                MyLog.getInstance().logcat(Log.ERROR, e.toString());
            }
        } else if (key.equals(getString(R.string.pref_sick_list_days_key))) {
            SickListDaysPreference prefDays = (SickListDaysPreference) preference;
            prefDays.setSummary(prefDays.getValueToShow((String) newValue));
        } else if (key.equals(getString(R.string.pref_sick_list_age_key))) {
            SickListAgePreference prefAge = (SickListAgePreference) preference;
            prefAge.setSummary(prefAge.getValueToShow((String) newValue));
        }

        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(getString(R.string.pref_sick_list_days_key))) {
            setupSickListDaysPreference();
        } else if (key.equals(getString(R.string.pref_sick_list_age_key))) {
            setupSickListAgePreference();
        }
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return Settings.getSettings(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}