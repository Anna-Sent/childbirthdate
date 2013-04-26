package com.anna.sent.soft.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class StateSaverFragment extends Fragment implements StateSaver {
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setViews();

		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		} else {
			savedInstanceState = getActivity().getIntent().getExtras();
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
