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

public class TabCalculationFragment extends ScrollViewFragment implements
		OnClickListener {
	private static final String EXTRA_GUI_CURRENT_DATE = "com.anna.sent.soft.childbirthdate.currentdate";

	private DatePicker datePickerCurrentDate;
	private StateSaver mListener;

	public TabCalculationFragment() {
		super();
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.tab_calculation;
	}

	@Override
	protected int getScrollViewResourceId() {
		return R.id.tabCalculation;
	}

	@Override
	protected void setViews() {
		datePickerCurrentDate = (DatePicker) getActivity().findViewById(
				R.id.datePickerCurrentDate);

		Button buttonToday = (Button) getActivity().findViewById(
				R.id.buttonToday);
		buttonToday.setOnClickListener(this);
	}

	@Override
	protected void restoreState(Bundle state) {
		super.restoreState(state);
		if (datePickerCurrentDate != null) {
			Calendar value = Calendar.getInstance();
			value.setTimeInMillis(state.getLong(EXTRA_GUI_CURRENT_DATE,
					System.currentTimeMillis()));
			Utils.setDate(datePickerCurrentDate, value);
		}
	}

	@Override
	protected void saveState(Bundle state) {
		super.saveState(state);
		if (datePickerCurrentDate != null) {
			state.putLong(EXTRA_GUI_CURRENT_DATE,
					Utils.getDate(datePickerCurrentDate).getTimeInMillis());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void today() {
		Utils.setDate(datePickerCurrentDate, Calendar.getInstance());
	}

	public void calculate(View view) {
		Intent intent = new Intent(getActivity(), ResultActivity.class);

		// int viewId = view.getId();
		int whatToDo = Shared.ResultParam.Calculate.NOTHING;
		// if (viewId == R.id.buttonCalculateEstimatedChildbirthDate) {
		whatToDo = Shared.ResultParam.Calculate.ECD;
		// } else if (viewId == R.id.buttonCalculateEstimatedGestationalAge) {
		whatToDo = Shared.ResultParam.Calculate.EGA;
		// }

		intent.putExtra(Shared.ResultParam.EXTRA_CURRENT_DATE,
				Utils.getDate(datePickerCurrentDate).getTimeInMillis());
		intent.putExtra(Shared.ResultParam.EXTRA_WHAT_TO_DO, whatToDo);

		// Save MainActivity state
		if (mListener != null) {
			Bundle state = new Bundle();
			mListener.onSaveInstanceState(state);
			intent.putExtra(Shared.ResultParam.EXTRA_MAIN_ACTIVITY_STATE, state);
		}

		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonToday:
			today();
			break;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (StateSaver) activity;
	}
}