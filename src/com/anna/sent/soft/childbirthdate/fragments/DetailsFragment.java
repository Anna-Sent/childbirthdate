package com.anna.sent.soft.childbirthdate.fragments;

import com.anna.sent.soft.numberpickerlibrary.NumberPicker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

public abstract class DetailsFragment extends Fragment implements
		NumberPicker.OnValueChangeListener, DatePicker.OnDateChangedListener {
	public interface OnDetailsChangedListener {
		void detailsChanged();
	}

	private OnDetailsChangedListener mListener = null;

	public void setOnDetailsChangedListener(OnDetailsChangedListener listener) {
		mListener = listener;
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
		args.putInt("index", index);
		details.setArguments(args);
		return details;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", -1);
	}

	protected abstract void saveData();

	protected abstract void restoreData();

	protected void dataChanged() {
		if (mListener != null) {
			saveData();
			mListener.detailsChanged();
		}
	}

	@Override
	public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
		dataChanged();
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		dataChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		saveData();
	}

	@Override
	public void onResume() {
		super.onResume();
		restoreData();
	}
}