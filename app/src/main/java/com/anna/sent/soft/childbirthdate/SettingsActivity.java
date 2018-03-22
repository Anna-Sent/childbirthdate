package com.anna.sent.soft.childbirthdate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.base.CbdSettingsActivity;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.childbirthdate.sicklist.SickListAgePreference;
import com.anna.sent.soft.childbirthdate.sicklist.SickListDaysPreference;
import com.anna.sent.soft.logging.MyLog;
import com.anna.sent.soft.utils.ActionBarUtils;
import com.anna.sent.soft.utils.ActivityUtils;

@SuppressWarnings("deprecated")
public class SettingsActivity extends CbdSettingsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.settings);

        addPreferencesFromResource(R.xml.preferences);

        ActionBarUtils.setupActionBar(this);

        createLanguagePreference();
        setupThemePreference();

        setupSickListDaysPreference();
        setupSickListAgePreference();
    }

    private void createLanguagePreference() {
        log("create language preference");
        PreferenceCategory category = (PreferenceCategory) findPreference(getString(R.string.pref_ui_settings_key));
        ListPreference pref = new ListPreference(this);
        pref.setKey(settingsLanguage.getLanguageKey());
        String[] entries = getResources().getStringArray(R.array.language);
        pref.setEntries(entries);
        String[] entryValues = getResources().getStringArray(R.array.language_ids);
        pref.setEntryValues(entryValues);
        String title = getString(R.string.pref_language_title);
        pref.setDialogTitle(title);
        pref.setTitle(title);
        int value = settingsLanguage.getLanguageId();
        pref.setDefaultValue(String.valueOf(value));
        pref.setValue(String.valueOf(value));
        settingsLanguage.setLanguageId(value);
        log(pref.getValue() + " " + pref.getEntry());
        pref.setSummary(pref.getEntry());
        pref.setOnPreferenceChangeListener(this);
        category.addPreference(pref);
    }

    private void setupThemePreference() {
        ListPreference pref = (ListPreference) findPreference(settingsTheme.getThemeKey());
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
        if (key.equals(settingsLanguage.getLanguageKey())) {
            try {
                int value = Integer.parseInt(newValue.toString());
                int current = settingsLanguage.getLanguageId();
                if (value != current) {
                    log("language changed");
                    settingsLanguage.setLanguageId(value);
                    ActivityUtils.restartActivity(this);
                    return true;
                }
            } catch (NumberFormatException e) {
                MyLog.getInstance().logcat(Log.ERROR, e.toString());
            }
        } else if (key.equals(settingsTheme.getThemeKey())) {
            try {
                int value = Integer.parseInt(newValue.toString());
                int current = settingsTheme.getThemeId();
                if (value != current) {
                    log("theme changed");
                    settingsTheme.setThemeId(value);
                    ActivityUtils.restartActivity(this);
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
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
}
