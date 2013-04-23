package com.anna.sent.soft.childbirthdate.fragments;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.anna.sent.soft.childbirthdate.DetailsActivity;
import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.StateSaver;

public class TitlesFragment extends ListFragment implements StateSaver {
	private boolean mDualPane;
	private int mSelectedItem;
	private View mHeader = null, mFooter = null;
	private TabCalculation mTabCalculation;

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
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.list_item, R.id.text1, titles));

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
		showDetails(position);
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
			getListView().setItemChecked(index, true);

			// Check what fragment is currently shown, replace if needed.
			FragmentManager fm = getFragmentManager();
			DetailsFragment details = (DetailsFragment) fm
					.findFragmentById(R.id.details);
			if (details == null || details.getShownIndex() != index) {
				Fragment newDetails = DetailsFragment.newInstance(index);
				FragmentTransaction ft = fm.beginTransaction();
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.replace(R.id.details, newDetails);
				ft.commit();
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
}
