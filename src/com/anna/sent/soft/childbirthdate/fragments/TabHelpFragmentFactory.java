package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.base.StateSaverFragment;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public class TabHelpFragmentFactory {
	public static Fragment newInstance(int position) {
		Fragment result = null;
		switch (position) {
		case Shared.Calculation.UNKNOWN:
			result = new TabHelpIntroductionFragment();
			break;
		case Shared.Calculation.BY_LMP:
			result = new TabHelpLmpFragment();
			break;
		case Shared.Calculation.BY_OVULATION:
			result = new TabHelpOvulationFragment();
			break;
		case Shared.Calculation.BY_ULTRASOUND:
			result = new TabHelpUltrasoundFragment();
			break;
		case Shared.Calculation.BY_FIRST_APPEARANCE:
			result = new TabHelpFirstAppearanceFragment();
			break;
		case Shared.Calculation.BY_FIRST_MOVEMENTS:
			result = new TabHelpFirstMovementsFragment();
			break;
		default:
			result = new Fragment();
			break;
		}

		Bundle args = new Bundle();
		args.putInt("position", position);
		result.setArguments(args);

		return result;
	}

	public abstract static class TabHelpFragment extends StateSaverFragment {
		private TextView textViewHelp;

		public TabHelpFragment() {
			super();
		}

		protected abstract int getLayoutResourceId();

		protected abstract int getTextViewResourceId();

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(getLayoutResourceId(), container, false);
			return v;
		}

		@Override
		public void setViews(Bundle savedInstanceState) {
			int position = getArguments().getInt("position");
			String[] help = getActivity().getResources().getStringArray(
					R.array.helpParts);
			String text = position >= 0 && position < help.length ? help[position]
					: null;
			textViewHelp = (TextView) getActivity().findViewById(
					getTextViewResourceId());
			textViewHelp.setText(Html.fromHtml(text));
		}

		@Override
		public void restoreState(Bundle state) {
		}

		@Override
		public void saveState(Bundle state) {
		}
	}

	public static class TabHelpIntroductionFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_intro;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpIntro;
		}
	}

	public static class TabHelpLmpFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_lmp;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpLmp;
		}
	}

	public static class TabHelpOvulationFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_ovulation;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpOvulation;
		}
	}

	public static class TabHelpUltrasoundFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_ultrasound;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpUltrasound;
		}
	}

	public static class TabHelpFirstAppearanceFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_first_appearance;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpFirstAppearance;
		}
	}

	public static class TabHelpFirstMovementsFragment extends TabHelpFragment {
		@Override
		protected int getLayoutResourceId() {
			return R.layout.tab_help_first_movements;
		}

		@Override
		protected int getTextViewResourceId() {
			return R.id.textViewHelpFirstMovements;
		}
	}
}