package com.anna.sent.soft.childbirthdate.fragments;

import android.app.Activity;
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
import com.anna.sent.soft.childbirthdate.adapters.ListItemArrayAdapter;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.shared.Data;
import com.anna.sent.soft.utils.DateUtils;

public class TitlesFragment extends ListFragment implements
		DetailsFragment.OnDetailsChangedListener {
	public final static int REQUEST_INDEX = 1;

	private View mHeader = null, mFooter = null;
	private TitlesFragmentHelper mHelper;
	private ListItemArrayAdapter mListAdapter;
	private boolean mDualPane;
	private int mSelectedItem;

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

		mHelper = new TitlesFragmentHelper(getActivity());
		mHelper.setViews();

		Data data = new Data();
		data.restoreChecked(getActivity());

		// Populate list
		mListAdapter = new ListItemArrayAdapter(getActivity(), getStrings1(),
				getStrings2(), data.byMethod());
		setListAdapter(mListAdapter);

		mDualPane = getResources().getBoolean(R.bool.has_two_panes);

		getListView().setChoiceMode(
				mDualPane ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);

		mSelectedItem = 0;
		/* Log.d("moo", "titles: init index=" + mSelectedItem); */
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

	private void restoreState(Bundle state) {
		mHelper.restoreState(state);
		mSelectedItem = state.getInt("index", 0);
		/* Log.d("moo", "titles: restore index=" + mSelectedItem); */
	}

	private void saveState(Bundle state) {
		FragmentManager fm = getFragmentManager();
		Fragment details = fm.findFragmentById(R.id.details);
		if (details != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(details);
			ft.commit();
		}

		mHelper.saveState(state);
		state.putInt("index", mSelectedItem);
		/* Log.d("moo", "titles: save index=" + mSelectedItem); */
	}

	@Override
	public void onStart() {
		super.onStart();

		// Make sure our UI is in the correct state.
		if (mDualPane) {
			showDetails(mSelectedItem);
			/* Log.d("moo", "titles: start with index=" + mSelectedItem); */
		}
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
		/* Log.d("moo", "titles: update index=" + mSelectedItem); */
		if (mDualPane) {
			// We can display everything in-place with fragments, so update
			// the list to highlight the selected item and show the data.
			getListView().setItemChecked(index, true);

			// Check what fragment is currently shown, replace if needed.
			FragmentManager fm = getFragmentManager();
			DetailsFragment details = (DetailsFragment) fm
					.findFragmentById(R.id.details);
			if (details == null || details.getShownIndex() != index) {
				DetailsFragment newDetails = getDetailsFragmentInstance(index);
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

			mHelper.saveMainActivityState(intent);

			startActivityForResult(intent, REQUEST_INDEX);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_INDEX) {
			if (resultCode == Activity.RESULT_OK) {
				mSelectedItem = data.getIntExtra("index", mSelectedItem);
				/* Log.d("moo", "titles: got index=" + mSelectedItem); */
				if (mDualPane) {
					showDetails(mSelectedItem);
				}
			}
		}
	}

	private String[] getStrings1() {
		return getResources().getStringArray(R.array.MethodNames);
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
