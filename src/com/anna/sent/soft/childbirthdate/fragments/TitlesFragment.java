package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.anna.sent.soft.childbirthdate.DetailsActivity;
import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.shared.Data;
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
			getListView().addFooterView(mHeader);
		}

		if (mFooter != null) {
			getListView().addFooterView(mFooter);
		}

		// Populate list with our array of titles.
		String[] titles = getResources().getStringArray(R.array.MethodNames);
		Data data = new Data();
		data.restoreChecked(getActivity());
		boolean[] checked = data.byMethod();
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
		} else {
			savedInstanceState = getActivity().getIntent().getExtras();
			if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
				mTabCalculation.restoreState(savedInstanceState);
				mSelectedItem = savedInstanceState.getInt("curChoice", 0);
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
		FragmentManager fm = getFragmentManager();
		Fragment details = fm.findFragmentById(R.id.details);
		if (details != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(details);
			ft.commit();
		}

		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mSelectedItem);
		mTabCalculation.saveState(outState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showDetails(position);
	}

	/**
	 * Helper function to show the details of a selected item, either by
	 * displaying a fragment in-place in the current UI, or starting a whole new
	 * activity in which it is displayed.
	 */
	void showDetails(int index) {
		mSelectedItem = index;
		if (mDualPane) {
			// We can display everything in-place with fragments, so update
			// the list to highlight the selected item and show the data.
			getListView().setItemChecked(index, true);

			// Check what fragment is currently shown, replace if needed.
			FragmentManager fm = getFragmentManager();
			DetailsFragment details = (DetailsFragment) fm
					.findFragmentById(R.id.details);
			if (details == null || details.getShownIndex() != index) {
				DetailsFragment newDetails = getDetailsFragmentInstance(index); // DetailsFragment.newInstance(index);
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

	private DetailsFragment[] fragments = new DetailsFragment[3];

	private DetailsFragment getDetailsFragmentInstance(int index) {
		DetailsFragment result = null;
		if (index >= 0 && index < fragments.length) {
			result = fragments[index];
			if (result == null) {
				result = DetailsFragment.newInstance(index);
				fragments[index] = result;
			}
		}

		return result;
	}

	private String[] getStrings2() {
		String[] result = new String[3];
		Data data = new Data();

		// first
		data.restoreLmp(getActivity());
		result[0] = getString(
				R.string.titles0,
				DateUtils.toString(getActivity(),
						data.getLastMenstruationDate()),
				data.getMenstrualCycleLen(), data.getLutealPhaseLen());

		// second
		data.restoreOvulation(getActivity());
		result[1] = getString(R.string.titles1,
				DateUtils.toString(getActivity(), data.getOvulationDate()));

		// third
		data.restoreUltrasound(getActivity());
		result[2] = getString(R.string.titles2, DateUtils.toString(
				getActivity(), data.getUltrasoundDate()),
				Pregnancy.getStringRepresentation(getActivity(),
						data.getWeeks(), data.getDays()),
				data.getIsEmbryonicAge() ? getString(R.string.embryonic)
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
		Data.save(getActivity(), checked);
	}
}
