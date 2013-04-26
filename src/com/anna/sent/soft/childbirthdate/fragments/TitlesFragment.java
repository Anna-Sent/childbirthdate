package com.anna.sent.soft.childbirthdate.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.anna.sent.soft.childbirthdate.adapters.ListItemArrayAdapter;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.shared.Data;
import com.anna.sent.soft.utils.DateUtils;
import com.anna.sent.soft.utils.StateSaver;

public class TitlesFragment extends ListFragment implements
		DetailsFragment.OnDetailsChangedListener, StateSaver {
	private static final String TAG = "moo";
	private static final boolean DEBUG = true;

	private final static int REQUEST_POSITION = 1;
	public final static String EXTRA_GUI_POSITION = "com.anna.sent.soft.childbirthdate.position";

	private View mHeader = null, mFooter = null;
	private TitlesFragmentHelper mHelper;
	private ListItemArrayAdapter mListAdapter;
	private boolean mDualPane;
	private int mSelectedItem;

	public TitlesFragment() {
		super();
		if (DEBUG) {
			Log.d(TAG, "TitlesFragment()");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (DEBUG) {
			Log.d(TAG, "onCreateView");
		}

		mHeader = inflater.inflate(R.layout.list_header, null);
		mFooter = inflater.inflate(R.layout.list_footer, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (DEBUG) {
			Log.d(TAG, "onActivityCreated");
		}

		super.onActivityCreated(savedInstanceState);

		setViews();

		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		} else {
			savedInstanceState = getActivity().getIntent().getExtras();
			if (savedInstanceState != null) {
				restoreState(savedInstanceState);
			}
		}
	}

	@Override
	public void setViews() {
		if (mHeader != null) {
			getListView().addFooterView(mHeader);
		}

		if (mFooter != null) {
			getListView().addFooterView(mFooter);
		}

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
		if (DEBUG) {
			Log.d(TAG, "init index=" + mSelectedItem);
		}

		mHelper = new TitlesFragmentHelper(getActivity());
		mHelper.setViews();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (DEBUG) {
			Log.d(TAG, "onSaveInstanceState");
		}

		saveState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void restoreState(Bundle state) {
		if (DEBUG) {
			Log.d(TAG, "restore state");
		}

		mSelectedItem = state.getInt(EXTRA_GUI_POSITION, 0);
		if (DEBUG) {
			Log.d(TAG, "restore index=" + mSelectedItem);
		}

		mHelper.restoreState(state);
	}

	@Override
	public void saveState(Bundle state) {
		if (DEBUG) {
			Log.d(TAG, "save state");
		}

		FragmentManager fm = getFragmentManager();
		Fragment details = fm.findFragmentById(R.id.details);
		if (details != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(details);
			ft.commit();
		}

		state.putInt(EXTRA_GUI_POSITION, mSelectedItem);
		mHelper.saveState(state);
	}

	@Override
	public void onStart() {
		super.onStart();

		// Make sure our UI is in the correct state.
		if (mDualPane) {
			showDetails(mSelectedItem);
			if (DEBUG) {
				Log.d(TAG, "start with index=" + mSelectedItem);
			}
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
		// We can display everything in-place with fragments, so update
		// the list to highlight the selected item and show the data.
		getListView().setItemChecked(index, true);
		mSelectedItem = index;
		if (DEBUG) {
			Log.d(TAG, "update index=" + mSelectedItem);
		}

		if (mDualPane) {
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
			intent.putExtra(EXTRA_GUI_POSITION, index);

			mHelper.saveMainActivityState(intent);

			startActivityForResult(intent, REQUEST_POSITION);
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
		if (requestCode == REQUEST_POSITION) {
			if (resultCode == Activity.RESULT_OK) {
				mSelectedItem = data.getIntExtra(EXTRA_GUI_POSITION,
						mSelectedItem);
				if (DEBUG) {
					Log.d(TAG, "got index=" + mSelectedItem);
				}

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
