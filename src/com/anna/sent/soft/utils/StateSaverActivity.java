package com.anna.sent.soft.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class StateSaverActivity extends FragmentActivity implements
		StateSaver {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ThemeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);

		setViews();

		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		} else {
			savedInstanceState = getIntent().getExtras();
			if (savedInstanceState != null) {
				restoreState(savedInstanceState);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		saveState(outState);
		super.onSaveInstanceState(outState);
	}

	public abstract void setViews();

	public abstract void restoreState(Bundle state);

	public abstract void saveState(Bundle state);
}
