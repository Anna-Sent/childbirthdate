package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.anna.sent.soft.childbirthdate.DetailsActivity;
import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.DateUtils;
import com.anna.sent.soft.utils.StateSaver;

public class TitlesFragment extends ListFragment implements StateSaver,
		DetailsFragment.OnDetailsChangedListener {
	private boolean mDualPane;
	private int mSelectedItem;
	private View mHeader = null, mFooter = null;
	private TabCalculation mTabCalculation;
	private ListItemArrayAdapter mListAdapter;

	public TitlesFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mHeader = inflater.inflate(R.layout.list_header, null);
		mFooter = inflater.inflate(R.layout.list_footer, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (mHeader != null) {
			getListView().addHeaderView(mHeader);
		}

		if (mFooter != null) {
			getListView().addFooterView(mFooter);
		}

		// Populate list with our array of titles.
		String[] titles = getResources().getStringArray(R.array.MethodNames);
		SharedPreferences settings = Shared.getSettings(getActivity());
		boolean[] checked = new boolean[3];
		checked[0] = settings
				.getBoolean(
						Shared.Saved.Calculation.EXTRA_BY_LAST_MENSTRUATION_DATE,
						false);
		checked[1] = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_OVULATION_DATE, false);
		checked[2] = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_ULTRASOUND, false);
		/*
		 * setListAdapter(new ArrayAdapter<String>(getActivity(),
		 * R.layout.list_item, R.id.text1, titles));
		 */
		mListAdapter = new ListItemArrayAdapter(getActivity(), titles,
				getStrings2(), checked);
		setListAdapter(mListAdapter);

		mDualPane = getResources().getBoolean(R.bool.has_two_panes);

		getListView().setChoiceMode(
				mDualPane ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);

		mTabCalculation = new TabCalculation(getActivity());
		mTabCalculation.setViews();

		mSelectedItem = 0;
		if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
			mTabCalculation.restoreState(savedInstanceState);
			mSelectedItem = savedInstanceState.getInt("curChoice", 0);
			Log.d("moo", "restore index=" + mSelectedItem);
		} else {
			savedInstanceState = getActivity().getIntent().getExtras();
			if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
				mTabCalculation.restoreState(savedInstanceState);
				mSelectedItem = savedInstanceState.getInt("curChoice", 0);
				Log.d("moo", "restore index=" + mSelectedItem);
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// Make sure our UI is in the correct state.
		if (mDualPane) {
			showDetails(mSelectedItem);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mSelectedItem);
		Log.d("moo", "save index=" + mSelectedItem);
		mTabCalculation.saveState(outState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showDetails(position - 1); // because of header
	}

	/**
	 * Helper function to show the details of a selected item, either by
	 * displaying a fragment in-place in the current UI, or starting a whole new
	 * activity in which it is displayed.
	 */
	void showDetails(int index) {
		Log.d("moo", "index is " + index + "; selected is " + mSelectedItem);
		mSelectedItem = index;
		if (mDualPane) {
			// We can display everything in-place with fragments, so update
			// the list to highlight the selected item and show the data.
			getListView().setItemChecked(index + 1, true); // because of header

			// Check what fragment is currently shown, replace if needed.
			FragmentManager fm = getFragmentManager();
			DetailsFragment details = (DetailsFragment) fm
					.findFragmentById(R.id.details);
			if (details == null || details.getShownIndex() != index) {
				DetailsFragment newDetails = DetailsFragment.newInstance(index);
				if (newDetails != null) {
					newDetails.setOnDetailsChangedListener(this);

					FragmentTransaction ft = fm.beginTransaction();
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.replace(R.id.details, newDetails);
					ft.commit();
				} else if (details != null) {
					FragmentTransaction ft = fm.beginTransaction();
					ft.remove(details);
					ft.commit();
				}
			}
		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			Intent intent = new Intent();
			intent.setClass(getActivity(), DetailsActivity.class);
			intent.putExtra("index", index);

			// Save MainActivity state
			StateSaver listener = (StateSaver) getActivity();
			if (listener != null) {
				Bundle state = new Bundle();
				listener.onSaveInstanceState(state);
				intent.putExtra(Shared.ResultParam.EXTRA_MAIN_ACTIVITY_STATE,
						state);
			}

			startActivity(intent);
		}
	}

	private String[] getStrings2() {
		String[] result = new String[3];
		SharedPreferences settings = Shared.getSettings(getActivity());

		// first
		Calendar lastMenstruationDate = Calendar.getInstance();
		lastMenstruationDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_LAST_MENSTRUATION_DATE,
				System.currentTimeMillis()));
		int menstrualCycleLen = settings.getInt(
				Shared.Saved.Calculation.EXTRA_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		int lutealPhaseLen = settings.getInt(
				Shared.Saved.Calculation.EXTRA_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
		result[0] = getString(R.string.titles0,
				DateUtils.toString(getActivity(), lastMenstruationDate),
				menstrualCycleLen, lutealPhaseLen);

		// second
		Calendar ovulationDate = Calendar.getInstance();
		ovulationDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
				System.currentTimeMillis()));
		result[1] = getString(R.string.titles1,
				DateUtils.toString(getActivity(), ovulationDate));

		// third
		int weeks = settings.getInt(Shared.Saved.Calculation.EXTRA_WEEKS, 0);
		int days = settings.getInt(Shared.Saved.Calculation.EXTRA_DAYS, 0);
		boolean isEmbryonicAge = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_IS_EMBRYONIC_AGE, false);
		Calendar ultrasoundDate = Calendar.getInstance();
		ultrasoundDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
				System.currentTimeMillis()));
		result[2] = getString(R.string.titles2, DateUtils.toString(
				getActivity(), ultrasoundDate),
				Pregnancy.getStringRepresentation(getActivity(), weeks, days),
				isEmbryonicAge ? getString(R.string.embryonic)
						: getString(R.string.gestational));

		return result;
	}

	@Override
	public void detailsChanged() {
		mListAdapter.updateValues(getStrings2());
	}

	@Override
	public void onResume() {
		super.onResume();
		detailsChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		boolean[] checked = mListAdapter.getChecked();
		SharedPreferences settings = Shared.getSettings(getActivity());
		Editor editor = settings.edit();
		editor.putBoolean(
				Shared.Saved.Calculation.EXTRA_BY_LAST_MENSTRUATION_DATE,
				checked[0]);
		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_OVULATION_DATE,
				checked[1]);
		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_ULTRASOUND,
				checked[2]);
		editor.commit();
	}
}
