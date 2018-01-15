package com.anna.sent.soft.childbirthdate.base;

import com.anna.sent.soft.activity.BaseDialogFragment;
import com.anna.sent.soft.childbirthdate.shared.SettingsLanguageImpl;
import com.anna.sent.soft.childbirthdate.shared.SettingsThemeImpl;
import com.anna.sent.soft.settings.SettingsLanguage;
import com.anna.sent.soft.settings.SettingsTheme;

public class CbdDialogFragment extends BaseDialogFragment {
    @Override
    protected String getAppTag() {
        return CbdConstants.TAG;
    }

    @Override
    protected SettingsLanguage createSettingsLanguage() {
        return new SettingsLanguageImpl(getContext());
    }

    @Override
    protected SettingsTheme createSettingsTheme() {
        return new SettingsThemeImpl(getContext());
    }
}
