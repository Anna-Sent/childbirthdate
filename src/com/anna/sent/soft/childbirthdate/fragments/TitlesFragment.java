package com.anna.sent.soft.childbirthdate.fragments;

import android.app.Activity;
import android.content.Intent;
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
import com.anna.sent.soft.childbirthdate.adapters.ListItemArrayAdapter;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.shared.Data;
import com.anna.sent.soft.childbirthdate.shared.DataClient;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.DateUtils;
import com.anna.sent.soft.utils.MainActivityStateSaver;
import com.anna.sent.soft.utils.StateSaver;

public class TitlesFragment extends ListFragment implements
		DetailsFragment.OnDetailsChangedListener, StateSaver,
		ListItemArrayAdapter.OnCheckedListener, DataClient {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;
	private static final boolean DEBUG_INDEX = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	@SuppressWarnings("unused")
	private void log(String msg, boolean debug) {
		if (DEBUG && debug) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	@SuppressWarnings("unused")
	private void log(String msg, int index) {
		if (DEBUG_INDEX) {
			Log.d(TAG, wrapMsg(msg + index));
		}
	}

	private final static int REQUEST_POSITION = 1;
	private static final String TAG_TITLES_HELPER = "TitlesHelper";

	private View mHeader = null, mFooter = null;
	private ListItemArrayAdapter mListAdapter;
	private boolean mDualPane;
	private int mSelectedItem;

	protected Data mData;

	@Override
	public void setData(Data data) {
		mData = data;
	}

	public TitlesFragment() {
		super();
		// log("create", false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// log("onCreateView", false);
		mHeader = inflater.inflate(R.layout.list_header, null);
		mFooter = inflater.inflate(R.layout.list_footer, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// log("onActivityCreated", false);
		super.onActivityCreated(savedInstanceState);

		setViews(savedInstanceState);

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
	public void setViews(Bundle savedInstanceState) {
		if (mHeader != null) {
			getListView().addFooterView(mHeader);
		}

		if (mFooter != null) {
			getListView().addFooterView(mFooter);
		}

		// Populate list
		mListAdapter = new ListItemArrayAdapter(getActivity(), getStrings1());
		mListAdapter.setOnCheckedListener(this);
		setListAdapter(mListAdapter);

		mDualPane = getResources().getBoolean(R.bool.has_two_panes);

		getListView().setChoiceMode(
				mDualPane ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);

		mSelectedItem = 0;
		// log("init index=", mSelectedItem);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(new TitlesHelperFragment(), TAG_TITLES_HELPER)
					.commit();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// log("onSaveInstanceState", false);
		saveState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void restoreState(Bundle state) {
		// log("restore state", false);
		mSelectedItem = state.getInt(Shared.Titles.EXTRA_GUI_POSITION, 0);
		// log("restore index=", mSelectedItem);
	}

	@Override
	public void saveState(Bundle state) {
		// log("save state", false);
		state.putInt(Shared.Titles.EXTRA_GUI_POSITION, mSelectedItem);
		// log("save index=", mSelectedItem);
	}

	@Override
	public void onStart() {
		super.onStart();

		// Make sure our UI is in the correct state.
		if (mDualPane) {
			showDetails(mSelectedItem);
			// log("start with index=", mSelectedItem);
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
	private void showDetails(int index) {
		// We can display everything in-place with fragments, so update
		// the list to highlight the selected item and show the mData.
		getListView().setItemChecked(index, true);
		mSelectedItem = index;
		// log("update index=", mSelectedItem);
		if (mDualPane) {
			// Check what fragment is currently shown, replace if needed.
			FragmentManager fm = getFragmentManager();
			DetailsFragment details = (DetailsFragment) fm
					.findFragmentById(R.id.details);
			if (details == null || details.getShownIndex() != index) {
				DetailsFragment newDetails = getDetailsFragmentInstance(index);
				if (newDetails != null) {
					// log("update details " + newDetails.getShownIndex(),
					// false);
					FragmentTransaction ft = fm.beginTransaction();
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.replace(R.id.details, newDetails);
					ft.commit();
				} else if (details != null) {
					// log("remove details" + details.getShownIndex(), false);
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
			intent.putExtra(Shared.Titles.EXTRA_GUI_POSITION, index);

			MainActivityStateSaver.save(getActivity(), intent);

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
				result.setOnDetailsChangedListener(this);
				fragments[index] = result;
				// log("create new details " + result.getShownIndex(), false);
			}
		}

		return result;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_POSITION) {
			if (resultCode == Activity.RESULT_OK) {
				mSelectedItem = data.getIntExtra(
						Shared.Titles.EXTRA_GUI_POSITION, mSelectedItem);
				// log("got index=", mSelectedItem);
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

		// first
		result[0] = getString(
				R.string.titles0,
				DateUtils.toString(getActivity(),
						mData.getLastMenstruationDate()),
				mData.getMenstrualCycleLen(), mData.getLutealPhaseLen());

		// second
		result[1] = getString(R.string.titles1,
				DateUtils.toString(getActivity(), mData.getOvulationDate()));

		// third
		result[2] = getString(R.string.titles2, DateUtils.toString(
				getActivity(), mData.getUltrasoundDate()),
				Pregnancy.getStringRepresentation(getActivity(),
						mData.getWeeks(), mData.getDays()),
				mData.isEmbryonicAge() ? getString(R.string.embryonic)
						: getString(R.string.gestational));

		return result;
	}

	@Override
	public void detailsChanged() {
		// log("details changed, update values");
		mListAdapter.updateValues(getStrings2());
	}

	@Override
	public void onResume() {
		super.onResume();
		// log("resume, update ui");
		mListAdapter.updateValues(mData.byMethod(), getStrings2());
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void checked(int position, boolean isChecked) {
		mData.setByMethod(position, isChecked);
	}
}
