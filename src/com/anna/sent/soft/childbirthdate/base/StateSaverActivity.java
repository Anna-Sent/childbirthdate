package com.anna.sent.soft.childbirthdate.base;

import com.anna.sent.soft.strategy.statesaver.StateSaverBaseActivity;
import com.anna.sent.soft.utils.LanguageUtils;
import com.anna.sent.soft.utils.ThemeUtils;

public abstract class StateSaverActivity extends StateSaverBaseActivity {
	protected void setupTheme() {
		ThemeUtils.onActivityCreateSetTheme(this);
	}

	protected void setupLanguage() {
		LanguageUtils.configurationChanged(this);
	}
}