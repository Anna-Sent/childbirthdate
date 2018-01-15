package com.anna.sent.soft.childbirthdate.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.settings.SettingsTheme;

public class SettingsThemeImpl extends SettingsTheme {
    public SettingsThemeImpl(Context context) {
        super(context);
    }

    @Override
    protected SharedPreferences getSettings() {
        return Settings.getSettings(context);
    }

    @Override
    public String getThemeKey() {
        return context.getString(R.string.pref_theme_key);
    }

    @Override
    protected int getThemes() {
        return R.array.theme;
    }

    @Override
    protected int getThemeIds() {
        return R.array.theme_ids;
    }

    @Override
    protected int getStyles() {
        return R.array.style;
    }

    @Override
    protected int getDefaultThemeId() {
        return context.getResources().getInteger(R.integer.defaultThemeId);
    }
}
