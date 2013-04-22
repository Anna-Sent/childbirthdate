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

public class TitlesFragment extends ListFragment {
	private String[] titles;
	private boolean mDualPane;
	private int mSelectedItem;
	private View mFooter = null;

	public TitlesFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFooter = inflater.inflate(R.layout.tab_calculation, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		titles = new String[] { getString(R.string.ByLMP),
				getString(R.string.ByOvulation),
				getString(R.string.ByUltrasound) };

		if (mFooter != null) {
			getListView().addFooterView(mFooter);
		}

		// Populate list with our array of titles.
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.list_item_checkable, R.id.text1, titles));

		mDualPane = getResources().getBoolean(R.bool.has_two_panes);

		if (mDualPane) {
			// In dual-pane mode, the list view highlights the selected item.
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			mSelectedItem = 0;
			if (savedInstanceState != null) {
				// Restore last state for checked position.
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
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mSelectedItem);
		Log.d("moo", "save index=" + mSelectedItem);
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
			intent.putExtra("label", titles[index]);
			startActivity(intent);
		}
	}
}
