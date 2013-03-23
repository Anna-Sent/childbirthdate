package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public abstract class ScrollViewFragment extends Fragment {
	private ScrollView scrollView;

	public ScrollViewFragment() {
		super();
	}

	protected abstract int getLayoutResourceId();

	protected abstract int getScrollViewResourceId();

	protected abstract void setViews();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View v = inflater.inflate(getLayoutResourceId(), container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setViews();

		scrollView = (ScrollView) getActivity().findViewById(
				getScrollViewResourceId());

		if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
			restoreState(savedInstanceState);
		} else {
			savedInstanceState = getActivity().getIntent().getExtras();
			if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
				restoreState(savedInstanceState);
			}
		}
	}

	private final String getExtraGuiScrollY() {
		String str = "com.anna.sent.soft.childbirthdate."
				+ getClass().getName() + ".srolly";
		return str;
	}

	protected void restoreState(Bundle state) {
		final int y = state.getInt(getExtraGuiScrollY(), 0);
		scrollView.post(new Runnable() {
			@Override
			public void run() {
				scrollView.scrollTo(0, y);
			}
		});
	}

	protected void saveState(Bundle state) {
		if (scrollView != null) {
			state.putInt(getExtraGuiScrollY(), scrollView.getScrollY());
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState(outState);
	}
}
