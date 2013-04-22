package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class DetailsFragment extends Fragment {

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
}