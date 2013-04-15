package com.anna.sent.soft.childbirthdate;

import android.content.res.Configuration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.anna.sent.soft.childbirthdate.fragments.DetailsFragment;
import com.anna.sent.soft.utils.Utils;

public class DetailsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Utils.onDialogStyleActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// If the screen is now in landscape mode, we can show the
			// dialog in-line with the list so we don't need this activity.
			// finish();
			// return;
		}

		String title = getIntent().getStringExtra("label");
		if (title != null) {
			setTitle(title);
		}

		int index = getIntent().getIntExtra("index", -1);

		DetailsFragment currentDetails = (DetailsFragment) getSupportFragmentManager()
				.findFragmentById(android.R.id.content);
		if (currentDetails == null) {
			Fragment details = DetailsFragment.newInstance(index);
			if (details == null) {
				finish();
				return;
			}

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, details).commit();
		} else {
			if (currentDetails.getShownIndex() != index) {
				Fragment details = DetailsFragment.newInstance(index);
				if (details == null) {
					finish();
					return;
				}

				getSupportFragmentManager().beginTransaction()
						.replace(android.R.id.content, details).commit();
			}
		}
	}
}