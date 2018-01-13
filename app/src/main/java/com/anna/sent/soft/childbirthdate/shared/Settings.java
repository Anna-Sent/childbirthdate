package com.anna.sent.soft.childbirthdate.shared;

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
import com.anna.sent.soft.settings.SettingsLanguage;
import com.anna.sent.soft.settings.SettingsTheme;
import com.anna.sent.soft.settings.SharedPreferencesWrapper;

import java.util.List;

public class Settings {
    public static final SettingsLanguageImpl settingsLanguage = new SettingsLanguageImpl();
    public static final SettingsThemeImpl settingsTheme = new SettingsThemeImpl();
    private static final String SETTINGS_FILE = "childbirthdatesettings";
    private static final String KEY_PREF_DO_NOT_SHOW_SICK_LIST_INFO_DIALOG = "com.anna.sent.soft.childbirthdate.donotshowsicklistinfodialog";

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

        throw new IllegalArgumentException("Unknown class. Age or Days class expected.");
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
        return SettingsParser.toList(value, element);
    }

    @SuppressWarnings("unused")
    public static void setList(Context context, List<LocalizableObject> list,
                               Class<? extends ISetting> cls) {
        if (cls == Age.class) {
            setAge(context, list);
        } else if (cls == Days.class) {
            setDays(context, list);
        }
    }

    public static void setDays(Context context, List<LocalizableObject> list) {
        setList(context, R.string.pref_sick_list_days_key, list);
    }

    public static void setAge(Context context, List<LocalizableObject> list) {
        setList(context, R.string.pref_sick_list_age_key, list);
    }

    private static void setList(Context context, int keyStringRes,
                                List<LocalizableObject> list) {
        SharedPreferences settings = getSettings(context);
        Editor editor = settings.edit();
        String value = SettingsParser.toString(list);
        editor.putString(context.getString(keyStringRes), value);
        editor.apply();
    }

    public static boolean showSickListInfoDialog(Context context) {
        SharedPreferences settings = getSettings(context);
        return !settings.getBoolean(KEY_PREF_DO_NOT_SHOW_SICK_LIST_INFO_DIALOG,
                false);
    }

    public static void doNotShowSickListInfoDialog(Context context) {
        SharedPreferences settings = getSettings(context);
        Editor editor = settings.edit();
        editor.putBoolean(KEY_PREF_DO_NOT_SHOW_SICK_LIST_INFO_DIALOG, true);
        editor.apply();
    }

    public static void clear(Context context) {
        getSettings(context).edit().clear().apply();
    }

    public static class SettingsLanguageImpl extends SettingsLanguage {
        private static final String KEY_PREF_IS_LANGUAGE_SET_BY_USER = "com.anna.sent.soft.childbirthdate.islanguagesetbyuser";

        @Override
        protected SharedPreferences getSettings(Context context) {
            return Settings.getSettings(context);
        }

        @Override
        public String getLanguageKey(Context context) {
            return context.getResources().getString(R.string.pref_language_key);
        }

        @Override
        protected String getIsLanguageSetByUserKey(Context context) {
            return KEY_PREF_IS_LANGUAGE_SET_BY_USER;
        }

        @Override
        protected int getLocaleArrayResourceId() {
            return R.array.locale;
        }

        @Override
        protected int getLanguageValuesArrayResourceId() {
            return R.array.language_values;
        }

        @Override
        protected int getDefaultLanguageId(Context context) {
            return context.getResources().getInteger(R.integer.defaultLanguage);
        }
    }

    public static class SettingsThemeImpl extends SettingsTheme {
        @Override
        protected SharedPreferences getSettings(Context context) {
            return Settings.getSettings(context);
        }

        @Override
        public String getThemeKey(Context context) {
            return context.getString(R.string.pref_theme_key);
        }

        @Override
        protected int getThemeValuesArrayResourceId() {
            return R.array.theme_values;
        }

        @Override
        protected int getDefaultThemeId(Context context) {
            return context.getResources().getInteger(R.integer.defaultTheme);
        }
    }
}
