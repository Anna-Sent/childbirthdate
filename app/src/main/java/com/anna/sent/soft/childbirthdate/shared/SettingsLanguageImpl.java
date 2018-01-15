package com.anna.sent.soft.childbirthdate.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.settings.SettingsLanguage;

public class SettingsLanguageImpl extends SettingsLanguage {
    private static final String KEY_PREF_IS_LANGUAGE_SET_BY_USER = "com.anna.sent.soft.childbirthdate.islanguagesetbyuser";

    public SettingsLanguageImpl(Context context) {
        super(context);
    }

    @Override
    protected SharedPreferences getSettings() {
        return Settings.getSettings(context);
    }

    @Override
    public String getLanguageKey() {
        return context.getResources().getString(R.string.pref_language_key);
    }

    @Override
    protected String getIsLanguageSetByUserKey() {
        return KEY_PREF_IS_LANGUAGE_SET_BY_USER;
    }

    @Override
    protected int getLanguages() {
        return R.array.language;
    }

    @Override
    protected int getLanguageIds() {
        return R.array.language_ids;
    }

    @Override
    protected int getLocales() {
        return R.array.locale;
    }

    @Override
    protected int getDefaultLanguageId() {
        return context.getResources().getInteger(R.integer.defaultLanguageId);
    }
}
