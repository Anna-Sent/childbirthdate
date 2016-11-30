package com.anna.sent.soft.childbirthdate.base;

import android.util.Log;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.strategy.statesaver.StateSaverBaseActivity;
import com.anna.sent.soft.utils.LanguageUtils;
import com.anna.sent.soft.utils.ThemeUtils;
import com.google.firebase.crash.FirebaseCrash;

public abstract class StateSaverActivity extends StateSaverBaseActivity {
    @Override
    public void log(String msg) {
        FirebaseCrash.logcat(Log.DEBUG, getClass().getSimpleName(), msg);
    }

    protected void setupTheme() {
        ThemeUtils.setupThemeBeforeOnActivityCreate(this,
                Settings.settingsTheme.getStyle(this, R.array.style,
                        R.style.AppTheme));
    }

    protected void setupLanguage() {
        LanguageUtils.setupLanguageAfterOnActivityCreate(this,
                Settings.settingsLanguage.isLanguageSetByUser(this),
                Settings.settingsLanguage.getLocale(this));
    }
}