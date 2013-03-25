package com.anna.sent.soft.childbirthdate.fragments;

import android.text.Html;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;

public class TabHelpFragment extends ScrollViewFragment {
	private TextView textViewHelp;

	public TabHelpFragment() {
		super();
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.tab_help;
	}

	@Override
	protected int getScrollViewResourceId() {
		return R.id.tabHelp;
	}

	@Override
	protected void setViews() {
		textViewHelp = (TextView) getActivity().findViewById(R.id.textViewHelp);
		textViewHelp.setText(Html.fromHtml(getString(R.string.bigHelp)));
	}
}
