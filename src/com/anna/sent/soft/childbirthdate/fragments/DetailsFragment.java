package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.shared.Data;
import com.anna.sent.soft.childbirthdate.shared.DataClient;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public abstract class DetailsFragment extends Fragment implements DataClient {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

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

	public interface OnDetailsChangedListener {
		void detailsChanged();
	}

	private OnDetailsChangedListener mListener = null;

	public void setOnDetailsChangedListener(OnDetailsChangedListener listener) {
		mListener = listener;
	}

	protected Data mData = null;

	@Override
	public void setData(Data data) {
		mData = data;
	}

	public DetailsFragment() {
		super();
	}

	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static DetailsFragment newInstance(int index) {
		DetailsFragment details = null;
		switch (index) {
		case 0:
			details = new DetailsLmpMethodFragment();
			break;
		case 1:
			details = new DetailsOvulationMethodFragment();
			break;
		case 2:
			details = new DetailsUltrasoundMethodFragment();
			break;
		default:
			return null;
		}

		Bundle args = new Bundle();
		args.putInt(Shared.Titles.EXTRA_GUI_POSITION, index);
		details.setArguments(args);
		return details;
	}

	public int getShownIndex() {
		return getArguments().getInt(Shared.Titles.EXTRA_GUI_POSITION, -1);
	}

	protected void dataChanged() {
		log("data changed", false);
		if (mListener != null) {
			mListener.detailsChanged();
		}
	}

	protected abstract void updateData();

	@Override
	public void onResume() {
		super.onResume();
		log("resume, update ui");
		updateData();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}