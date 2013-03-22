package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.utils.StateSaver;

public class TabHelpFragment extends Fragment implements StateSaver {
	private static final String EXTRA_GUI_SCROLL_Y = "com.anna.sent.soft.childbirthdate.tabhelp.scrolly";
	private TextView textViewHelp;
	private ScrollView scrollView;

	public TabHelpFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab_help, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		textViewHelp = (TextView) getActivity().findViewById(R.id.textViewHelp);
		textViewHelp.setText(Html.fromHtml(getString(R.string.bigHelp)));

		scrollView = (ScrollView) getActivity().findViewById(R.id.tabHelp);

		Intent intent = getActivity().getIntent();

		final int y = intent.getIntExtra(EXTRA_GUI_SCROLL_Y, 0);
		scrollView.post(new Runnable() {
			@Override
			public void run() {
				scrollView.scrollTo(0, y);
			}
		});
	}

	@Override
	public void onSaveState(Intent outState) {
			outState.putExtra(EXTRA_GUI_SCROLL_Y, scrollView.getScrollY());
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			final int y = savedInstanceState.getInt(EXTRA_GUI_SCROLL_Y, 0);
			scrollView.post(new Runnable() {
				@Override
				public void run() {
					scrollView.scrollTo(0, y);
				}
			});
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (scrollView != null) {
			outState.putInt(EXTRA_GUI_SCROLL_Y, scrollView.getScrollY());
		}
	}
}
