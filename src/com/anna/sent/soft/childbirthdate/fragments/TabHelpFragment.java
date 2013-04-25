package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.utils.StateSaverFragment;

public class TabHelpFragment extends StateSaverFragment {
	private TextView textViewHelp;
	private ScrollView scrollView;

	private final static String EXTRA_GUI_SCROLL_Y = "com.anna.sent.soft.childbirthdate.tabhelp.srolly";

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
	public void internalOnActivityCreated() {
		textViewHelp = (TextView) getActivity().findViewById(R.id.textViewHelp);
		textViewHelp.setText(Html.fromHtml(getString(R.string.bigHelp)));
		scrollView = (ScrollView) getActivity().findViewById(R.id.tabHelp);
	}

	@Override
	protected void restoreState(Bundle state) {
		if (scrollView != null) {
			final int y = state.getInt(EXTRA_GUI_SCROLL_Y, 0);
			scrollView.post(new Runnable() {
				@Override
				public void run() {
					scrollView.scrollTo(0, y);
				}
			});
		}
	}

	@Override
	protected void saveState(Bundle state) {
		if (scrollView != null) {
			state.putInt(EXTRA_GUI_SCROLL_Y, scrollView.getScrollY());
		}
	}
}
