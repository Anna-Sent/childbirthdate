package com.anna.sent.soft.childbirthdate.base;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.strategy.statesaver.StateSaverBaseActivity;
import com.anna.sent.soft.utils.LanguageUtils;
import com.anna.sent.soft.utils.ThemeUtils;

public abstract class StateSaverActivity extends StateSaverBaseActivity {
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