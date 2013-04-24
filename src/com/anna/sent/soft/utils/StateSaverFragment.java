package com.anna.sent.soft.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class StateSaverFragment extends Fragment {
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		internalOnActivityCreated();

		if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
			restoreState(savedInstanceState);
		} else {
			savedInstanceState = getActivity().getIntent().getExtras();
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

	protected abstract void internalOnActivityCreated();

	protected abstract void restoreState(Bundle state);

	protected abstract void saveState(Bundle state);
}
