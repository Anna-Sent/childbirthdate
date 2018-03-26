package com.anna.sent.soft.childbirthdate.base;

import com.anna.sent.soft.activity.BaseSettingsActivity;
import com.anna.sent.soft.childbirthdate.BuildConfig;
import com.anna.sent.soft.childbirthdate.shared.SettingsLanguageImpl;
import com.anna.sent.soft.childbirthdate.shared.SettingsThemeImpl;
import com.anna.sent.soft.settings.SettingsLanguage;
import com.anna.sent.soft.settings.SettingsTheme;

public abstract class CbdSettingsActivity extends BaseSettingsActivity {
    @Override
    protected String getAppTag() {
        return CbdConstants.TAG;
    }

    @Override
    protected boolean enableCrashReporting() {
        return BuildConfig.ENABLE_CRASHLYTICS;
    }

    @Override
    protected SettingsLanguage createSettingsLanguage() {
        return new SettingsLanguageImpl(this);
    }

    @Override
    protected SettingsTheme createSettingsTheme() {
        return new SettingsThemeImpl(this);
    }
}
