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
import com.anna.sent.soft.childbirthdate.base.StateSaver;
import com.anna.sent.soft.childbirthdate.shared.Data;
import com.anna.sent.soft.childbirthdate.shared.DataClient;
import com.anna.sent.soft.childbirthdate.shared.Shared;

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

	private ListItemArrayAdapter mListAdapter;
	private boolean mDualPane;
	private int mSelectedItem = 0;

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
		mListAdapter = new ListItemArrayAdapter(getActivity(), getStrings1());
		mListAdapter.setOnCheckedListener(this);
		setListAdapter(mListAdapter);

		mDualPane = getResources().getBoolean(R.bool.has_two_panes);

		getListView().setChoiceMode(
				mDualPane ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);

		// log("init index=", mSelectedItem);
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
		mSelectedItem = state.getInt(Shared.Titles.EXTRA_POSITION, 0);
		// log("restore index=", mSelectedItem);
	}

	@Override
	public void saveState(Bundle state) {
		// log("save state", false);
		state.putInt(Shared.Titles.EXTRA_POSITION, mSelectedItem);
		// log("save index=", mSelectedItem);
	}

	@Override
	public void onStart() {
		super.onStart();

		if (mDualPane) {
			showDetails();
			// log("start with index=", mSelectedItem);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mSelectedItem = position;
		showDetails();
	}

	private void showDetails() {
		getListView().setItemChecked(mSelectedItem, true);
		// log("update index=", mSelectedItem);
		if (mDualPane) {
			FragmentManager fm = getFragmentManager();
			DetailsFragment details = (DetailsFragment) fm
					.findFragmentById(R.id.details);
			if (details == null || details.getShownIndex() != mSelectedItem) {
				DetailsFragment newDetails = getDetailsFragmentInstance(mSelectedItem);
				if (newDetails != null) {
					// log("update details " + newDetails.getShownIndex(),
					// false);
					FragmentTransaction ft = fm.beginTransaction();
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.replace(R.id.details, newDetails);
					ft.commit();
				} else if (details != null) {
					// log("remove details" + details.getShownIndex(),
					// false);
					FragmentTransaction ft = fm.beginTransaction();
					ft.remove(details);
					ft.commit();
				}
			}
		} else {
			Intent intent = new Intent(getActivity(), DetailsActivity.class);
			intent.putExtra(Shared.Titles.EXTRA_POSITION, mSelectedItem);
			startActivityForResult(intent, REQUEST_POSITION);
		}
	}

	/**
	 * DO NOT CACHE THESE FRAGMENTS!
	 * 
	 * @param index
	 *            index of method
	 * @return fragment or null
	 */
	private DetailsFragment getDetailsFragmentInstance(int index) {
		DetailsFragment result = DetailsFragment.newInstance(index);
		if (result != null) {
			result.setOnDetailsChangedListener(this);
		}

		return result;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_POSITION) {
			if (resultCode == Activity.RESULT_OK) {
				mSelectedItem = data.getIntExtra(Shared.Titles.EXTRA_POSITION,
						mSelectedItem);
				// log("got index=", mSelectedItem);
				if (mDualPane) {
					showDetails();
				}
			}
		}
	}

	private String[] getStrings1() {
		return getResources().getStringArray(R.array.MethodNames);
	}

	@Override
	public void detailsChanged() {
		// log("details changed, update values");
		mListAdapter.updateValues(mData.getStrings2(getActivity()));
	}

	@Override
	public void onResume() {
		super.onResume();
		// log("resume, update ui");
		mListAdapter.updateValues(mData.byMethod(),
				mData.getStrings2(getActivity()));
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
