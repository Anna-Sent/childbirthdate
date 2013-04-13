package com.anna.sent.soft.childbirthdate;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.anna.sent.soft.childbirthdate.fragments.CalculationPageLmpMethodFragment;
import com.anna.sent.soft.childbirthdate.fragments.CalculationPageOvulationDateMethodFragment;
import com.anna.sent.soft.childbirthdate.fragments.CalculationPageUltrasoundMethodFragment;

public class DetailsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// If the screen is now in landscape mode, we can show the
			// dialog in-line with the list so we don't need this activity.
			finish();
			return;
		}

		int index = getIntent().getIntExtra("index", 0);
		Fragment details = null;
		switch (index) {
		case 1:
			details = new CalculationPageLmpMethodFragment();
			break;
		case 2:
			details = new CalculationPageOvulationDateMethodFragment();
			break;
		case 3:
			details = new CalculationPageUltrasoundMethodFragment();
			break;
		default:
			finish();
			return;
		}

		details.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
				.add(android.R.id.content, details).commit();

	}
}