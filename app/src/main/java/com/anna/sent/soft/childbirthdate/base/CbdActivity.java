package com.anna.sent.soft.childbirthdate.base;

import android.annotation.SuppressLint;

import com.anna.sent.soft.activity.BaseActivity;
import com.anna.sent.soft.childbirthdate.BuildConfig;
import com.anna.sent.soft.childbirthdate.shared.SettingsLanguageImpl;
import com.anna.sent.soft.childbirthdate.shared.SettingsThemeImpl;
import com.anna.sent.soft.settings.SettingsLanguage;
import com.anna.sent.soft.settings.SettingsTheme;

@SuppressLint("Registered")
public class CbdActivity extends BaseActivity {
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
