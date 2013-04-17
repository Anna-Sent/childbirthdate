package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.anna.sent.soft.childbirthdate.DetailsActivity;
import com.anna.sent.soft.childbirthdate.R;

public class TitlesFragment extends ListFragment {
	private String[] titles;
	private int[] detailsIds;
	private boolean mDualPane;
	private int mSelectedItem;

	public TitlesFragment() {
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		detailsIds = new int[] { R.id.detailsLmp, R.id.detailsOvulation,
				R.id.detailsUltrasound };

		titles = new String[] { getString(R.string.ByLMP),
				getString(R.string.ByOvulation),
				getString(R.string.ByUltrasound) };

		// Populate list with our array of titles.
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.list_item_checkable, R.id.text1, titles));

		mDualPane = getResources().getBoolean(R.bool.has_two_panes);

		mSelectedItem = -1;
		FragmentManager fm = getFragmentManager();
		for (int i = 0; i < detailsIds.length; ++i) {
			Fragment details = fm.findFragmentById(detailsIds[i]);
			fm.beginTransaction().hide(details).commit();
		}

		if (mDualPane) {
			// In dual-pane mode, the list view highlights the selected item.
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			int itemToSelect = 0;
			if (savedInstanceState != null) {
				// Restore last state for checked position.
				itemToSelect = savedInstanceState.getInt("curChoice", 0);
				if (itemToSelect < 0 || itemToSelect >= detailsIds.length) {
					itemToSelect = 0;
				}
			}

			// Make sure our UI is in the correct state.
			showDetails(itemToSelect);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mSelectedItem);
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
		if (mDualPane) {
			// We can display everything in-place with fragments, so update
			// the list to highlight the selected item and show the data.
			getListView().setItemChecked(index, true);

			// Check what fragment is currently shown, replace if needed.
			if (mSelectedItem != index) {
				if (index >= 0 && index < detailsIds.length) {
					FragmentManager fm = getFragmentManager();
					if (mSelectedItem >= 0 && mSelectedItem < detailsIds.length) {
						Fragment details = fm
								.findFragmentById(detailsIds[mSelectedItem]);
						fm.beginTransaction().hide(details).commit();
					}

					mSelectedItem = index;
					Fragment details = fm.findFragmentById(detailsIds[index]);
					FragmentTransaction ft = fm.beginTransaction();
					ft.show(details);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.commit();
				}
			}
		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			mSelectedItem = index;
			Intent intent = new Intent();
			intent.setClass(getActivity(), DetailsActivity.class);
			intent.putExtra("index", index);
			intent.putExtra("label", titles[index]);
			startActivity(intent);
		}
	}
}
