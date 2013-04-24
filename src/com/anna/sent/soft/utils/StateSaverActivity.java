package com.anna.sent.soft.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class StateSaverActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ThemeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);

		internalOnCreate();

		if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
			restoreState(savedInstanceState);
		} else {
			savedInstanceState = getIntent().getExtras();
			if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
				restoreState(savedInstanceState);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		saveState(outState);
		super.onSaveInstanceState(outState);
	}

	protected abstract void internalOnCreate();

	protected abstract void restoreState(Bundle state);

	protected abstract void saveState(Bundle state);
}
