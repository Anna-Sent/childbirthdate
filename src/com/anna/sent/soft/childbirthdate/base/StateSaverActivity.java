package com.anna.sent.soft.childbirthdate.base;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.childbirthdate.data.DataClient;
import com.anna.sent.soft.childbirthdate.data.DataImpl;
import com.anna.sent.soft.childbirthdate.strategy.StrategyActivity;
import com.anna.sent.soft.utils.LanguageUtils;
import com.anna.sent.soft.utils.ThemeUtils;

public abstract class StateSaverActivity extends StrategyActivity implements
		StateSaver {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	@SuppressWarnings("unused")
	private void log(String msg, boolean debug) {
		if (DEBUG && debug) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private DataImpl mConcreteData;

	protected Data getData() {
		return mConcreteData;
	}

	private ArrayList<StateSaver> mStateSavers = new ArrayList<StateSaver>();

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		ThemeUtils.onActivityCreateSetTheme(this);

		mConcreteData = new DataImpl(this);

		super.onCreate(savedInstanceState);

		LanguageUtils.configurationChanged(this);

		setViews(savedInstanceState);

		if (savedInstanceState != null) {
			// log("restore 1");
			restoreState(savedInstanceState);
		} else {
			savedInstanceState = getIntent().getExtras();
			if (savedInstanceState != null) {
				// log("restore 2");
				restoreState(savedInstanceState);
			}
		}
	}

	@Override
	protected final void onSaveInstanceState(Bundle outState) {
		// log("onSaveInstanceState", true);
		beforeOnSaveInstanceState();
		saveActivityState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void setViews(Bundle savedInstanceState) {
	}

	@Override
	public void restoreState(Bundle state) {
	}

	@Override
	public final void saveState(Bundle state) {
		// log("save state");
		saveActivityState(state);
		saveFragmentState(state);
	}

	protected void beforeOnSaveInstanceState() {
	}

	protected void saveActivityState(Bundle state) {
	}

	private final void saveFragmentState(Bundle state) {
		for (int i = 0; i < mStateSavers.size(); ++i) {
			mStateSavers.get(i).saveState(state);
			// log("save fragment state " + i);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// log("resume, update data", true);
		mConcreteData.update();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// log("pause, save data", true);
		mConcreteData.save();
	}

	@Override
	public final void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		// log("attach " + fragment.toString());
		if (fragment instanceof DataClient) {
			DataClient dataClient = (DataClient) fragment;
			dataClient.setData(mConcreteData);
			// log("set data");
		}

		if (fragment instanceof StateSaver) {
			StateSaver stateSaver = (StateSaver) fragment;
			mStateSavers.add(stateSaver);
		}
	}
}