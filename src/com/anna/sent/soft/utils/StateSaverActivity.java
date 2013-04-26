package com.anna.sent.soft.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public abstract class StateSaverActivity extends FragmentActivity implements
		StateSaver {
	private static final String TAG = "moo";
	private static final boolean DEBUG = true;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	private void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

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
	protected void onSaveInstanceState(Bundle outState) {
		log("onSaveInstanceState");
		beforeOnSaveInstanceState();
		saveActivityState(outState);
		super.onSaveInstanceState(outState);
	}

	public void setViews() {
	}

	public void restoreState(Bundle state) {
	}

	public void saveState(Bundle state) {
		saveActivityState(state);
		saveFragmentState(state);
	}

	public void beforeOnSaveInstanceState() {
	}

	public void saveActivityState(Bundle state) {
	}

	public void saveFragmentState(Bundle state) {
	}
}
