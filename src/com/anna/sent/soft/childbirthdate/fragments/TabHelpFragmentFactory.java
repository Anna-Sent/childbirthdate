package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.utils.StateSaverFragment;

public class TabHelpFragmentFactory {
	public static TabHelpFragment newInstance(int position) {
		switch (position) {
		case 0:
			return new TabHelpIntroductionFragment();
		case 1:
			return new TabHelpLmpFragment();
		case 2:
			return new TabHelpOvulationFragment();
		case 3:
			return new TabHelpUltrasoundFragment();
		}

		return null;
	}

	public abstract static class TabHelpFragment extends StateSaverFragment {
		private TextView textViewHelp;
		private ScrollView scrollView;
		private int mScrollY = 0;

		private final static String EXTRA_GUI_SCROLL_Y = "com.anna.sent.soft.childbirthdate.tabhelp.srolly";

		public TabHelpFragment() {
			super();
		}

		protected abstract int getLayoutResourceId();

		protected abstract int getHelpStringResourceId();

		protected abstract int getTextViewResourceId();

		protected abstract int getScrollViewResourceId();

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(getLayoutResourceId(), container, false);
			return v;
		}

		@Override
		public void setViews(Bundle savedInstanceState) {
			textViewHelp = (TextView) getActivity().findViewById(
					getTextViewResourceId());
			textViewHelp.setText(Html
					.fromHtml(getString(getHelpStringResourceId())));
			scrollView = (ScrollView) getActivity().findViewById(
					getScrollViewResourceId());
		}

		@Override
		public void restoreState(Bundle state) {
			if (scrollView != null) {
				mScrollY = state.getInt(EXTRA_GUI_SCROLL_Y, 0);
				// Log.d("moo", "restore scroll " + mScrollY);
				setScrollY();
			}
		}

		public void setScrollY() {
			scrollView.post(new Runnable() {
				@Override
				public void run() {
					scrollView.scrollTo(0, mScrollY);
				}
			});
		}

		@Override
		public void saveState(Bundle state) {
			if (scrollView != null) {
				// Log.d("moo", "save scroll " + scrollView.getScrollY());
				state.putInt(EXTRA_GUI_SCROLL_Y, scrollView.getScrollY());
			}
		}
	}

	public static class TabHelpIntroductionFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_intro;
		}

		@Override
		protected int getHelpStringResourceId() {
			return R.string.helpIntroduction;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpIntro;
		}

		@Override
		protected int getScrollViewResourceId() {
			return R.id.tabHelpIntro;
		}
	}

	public static class TabHelpLmpFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_lmp;
		}

		@Override
		protected int getHelpStringResourceId() {
			return R.string.helpLmp;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpLmp;
		}

		@Override
		protected int getScrollViewResourceId() {
			return R.id.tabHelpLmp;
		}
	}

	public static class TabHelpOvulationFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_ovulation;
		}

		@Override
		protected int getHelpStringResourceId() {
			return R.string.helpOvulation;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpOvulation;
		}

		@Override
		protected int getScrollViewResourceId() {
			return R.id.tabHelpOvulation;
		}
	}

	public static class TabHelpUltrasoundFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_ultrasound;
		}

		@Override
		protected int getHelpStringResourceId() {
			return R.string.helpUltrasound;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpUltrasound;
		}

		@Override
		protected int getScrollViewResourceId() {
			return R.id.tabHelpUltrasound;
		}
	}
}
