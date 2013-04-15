package com.anna.sent.soft.childbirthdate.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.anna.sent.soft.childbirthdate.DetailsActivity;
import com.anna.sent.soft.childbirthdate.R;

public class TitlesFragment extends ListFragment {
	private String[] titles = null;

	public TitlesFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		Log.d("moo", "view height is " + v.getLayoutParams().height);
		v.getLayoutParams().height = 320;// getItemHeight() * 3;
		Log.d("moo", "item height is " + getItemHeight());
		Log.d("moo", "view height is " + v.getLayoutParams().height);
		if (container != null) {
			Log.d("moo", "container height is "
					+ container.getLayoutParams().height);
			container.getLayoutParams().height = 600;
			Log.d("moo", "container height is "
					+ container.getLayoutParams().height);
		}

		return v;
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);
		// Log.d("moo", "list view height is " + getListView().getHeight());
	}

	private int getItemHeight() {
		TypedValue value = new TypedValue();
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getTheme().resolveAttribute(
				android.R.attr.listPreferredItemHeight, value, true);
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		return TypedValue.complexToDimensionPixelSize(value.data, metrics);
	}

	boolean mDualPane;
	int mCurCheckPosition = 0;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		titles = new String[] { getString(R.string.ByLMP),
				getString(R.string.ByOvulation),
				getString(R.string.ByUltrasound) };

		// Populate list with our array of titles.
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.list_item_checkable, R.id.text1, titles));

		// Check to see if we have a frame in which to embed the details
		// fragment directly in the containing UI.
		View detailsFrame = getActivity().findViewById(R.id.details);
		mDualPane = detailsFrame != null
				&& detailsFrame.getVisibility() == View.VISIBLE;

		if (savedInstanceState != null) {
			// Restore last state for checked position.
			mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}

		if (mDualPane) {
			// In dual-pane mode, the list view highlights the selected item.
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			// Make sure our UI is in the correct state.
			showDetails(mCurCheckPosition);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurCheckPosition);
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
		mCurCheckPosition = index;
		Log.d("moo", "index is " + index);

		if (mDualPane) {
			// We can display everything in-place with fragments, so update
			// the list to highlight the selected item and show the data.
			getListView().setItemChecked(index, true);

			// Check what fragment is currently shown, replace if needed.
			DetailsFragment details = (DetailsFragment) getFragmentManager()
					.findFragmentById(R.id.details);
			Log.d("moo", "details is " + details.toString());
			if (details == null || details.getShownIndex() != index) {
				// Make new fragment to show this selection.
				details = DetailsFragment.newInstance(index);

				// Execute a transaction, replacing any existing fragment
				// with this one inside the frame.
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.details, details);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
