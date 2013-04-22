package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.ResultActivity;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.StateSaver;

public class TabCalculation implements OnClickListener {
	private static final String EXTRA_GUI_CURRENT_DATE = "com.anna.sent.soft.childbirthdate.currentdate";

	private DatePicker datePickerCurrentDate;
	private Activity mActivity;

	public TabCalculation(Activity activity) {
		mActivity = activity;
	}

	public void setViews() {
		datePickerCurrentDate = (DatePicker) mActivity
				.findViewById(R.id.datePickerCurrentDate);

		Button buttonToday = (Button) mActivity.findViewById(R.id.buttonToday);
		buttonToday.setOnClickListener(this);
	}

	protected void restoreState(Bundle state) {
		if (datePickerCurrentDate != null) {
			Calendar value = Calendar.getInstance();
			value.setTimeInMillis(state.getLong(EXTRA_GUI_CURRENT_DATE,
					System.currentTimeMillis()));
			Utils.setDate(datePickerCurrentDate, value);
		}
	}

	protected void saveState(Bundle state) {
		if (datePickerCurrentDate != null) {
			state.putLong(EXTRA_GUI_CURRENT_DATE,
					Utils.getDate(datePickerCurrentDate).getTimeInMillis());
		}
	}

	private void today() {
		Utils.setDate(datePickerCurrentDate, Calendar.getInstance());
	}

	public void calculate(int whatToDo) {
		Intent intent = new Intent(mActivity, ResultActivity.class);

		intent.putExtra(Shared.ResultParam.EXTRA_CURRENT_DATE,
				Utils.getDate(datePickerCurrentDate).getTimeInMillis());
		intent.putExtra(Shared.ResultParam.EXTRA_WHAT_TO_DO, whatToDo);

		// Save MainActivity state
		StateSaver listener = (StateSaver) mActivity;
		if (listener != null) {
			Bundle state = new Bundle();
			listener.onSaveInstanceState(state);
			intent.putExtra(Shared.ResultParam.EXTRA_MAIN_ACTIVITY_STATE, state);
		}

		mActivity.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonCalculateEstimatedChildbirthDate:
			calculate(Shared.ResultParam.Calculate.ECD);
			break;
		case R.id.buttonCalculateEstimatedGestationalAge:
			calculate(Shared.ResultParam.Calculate.EGA);
			break;
		case R.id.buttonToday:
			today();
			break;
		}
	}
}