package com.anna.sent.soft.utils;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.shared.DataClient;
import com.anna.sent.soft.childbirthdate.shared.DataImpl;

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

	private void log(String msg, boolean debug) {
		if (debug) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private DataImpl mConcreteData;
	private ArrayList<StateSaver> mStateSavers = new ArrayList<StateSaver>();

	protected DataImpl getData() {
		return mConcreteData;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ThemeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);

		mConcreteData = new DataImpl(this);
		setViews(savedInstanceState);

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
		log("onSaveInstanceState", false);
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
	public void saveState(Bundle state) {
		saveActivityState(state);
		saveFragmentState(state);
	}

	public void beforeOnSaveInstanceState() {
	}

	public void saveActivityState(Bundle state) {
	}

	private void saveFragmentState(Bundle state) {
		for (int i = 0; i < mStateSavers.size(); ++i) {
			mStateSavers.get(i).saveState(state);
			log("save fragment state " + i);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		log("resume, update data", false);
		mConcreteData.update();
	}

	@Override
	protected void onPause() {
		super.onPause();
		log("pause, save data", false);
		mConcreteData.save();
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		log("attach " + fragment.toString());
		if (fragment instanceof DataClient) {
			DataClient dataClient = (DataClient) fragment;
			dataClient.setData(mConcreteData);
		}

		if (fragment instanceof StateSaver) {
			StateSaver stateSaver = (StateSaver) fragment;
			mStateSavers.add(stateSaver);
		}
	}
}
