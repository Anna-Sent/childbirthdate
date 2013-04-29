package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.ResultActivity;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.DateUtils;
import com.anna.sent.soft.utils.MainActivityStateSaver;
import com.anna.sent.soft.utils.StateSaverFragment;

public class TitlesHelperFragment extends StateSaverFragment implements
		OnClickListener {
	private static final String EXTRA_GUI_CURRENT_DATE = "com.anna.sent.soft.childbirthdate.currentdate";

	private DatePicker datePickerCurrentDate;

	@Override
	public void setViews(Bundle savedInstanceState) {
		Button buttonCalculateECD = (Button) getActivity().findViewById(
				R.id.buttonCalculateEstimatedChildbirthDate);
		buttonCalculateECD.setOnClickListener(this);
		Button buttonCalculateEGA = (Button) getActivity().findViewById(
				R.id.buttonCalculateEstimatedGestationalAge);
		buttonCalculateEGA.setOnClickListener(this);

		datePickerCurrentDate = (DatePicker) getActivity().findViewById(
				R.id.datePickerCurrentDate);

		Button buttonToday = (Button) getActivity().findViewById(
				R.id.buttonToday);
		buttonToday.setOnClickListener(this);
	}

	@Override
	public void restoreState(Bundle state) {
		if (datePickerCurrentDate != null) {
			Calendar value = Calendar.getInstance();
			value.setTimeInMillis(state.getLong(EXTRA_GUI_CURRENT_DATE,
					System.currentTimeMillis()));
			DateUtils.setDate(datePickerCurrentDate, value);
		}
	}

	@Override
	public void saveState(Bundle state) {
		if (datePickerCurrentDate != null) {
			state.putLong(EXTRA_GUI_CURRENT_DATE,
					DateUtils.getDate(datePickerCurrentDate).getTimeInMillis());
		}
	}

	private void today() {
		DateUtils.setDate(datePickerCurrentDate, Calendar.getInstance());
	}

	private void calculate(int whatToDo) {
		Intent intent = new Intent(getActivity(), ResultActivity.class);

		intent.putExtra(Shared.ResultParam.EXTRA_CURRENT_DATE, DateUtils
				.getDate(datePickerCurrentDate).getTimeInMillis());
		intent.putExtra(Shared.ResultParam.EXTRA_WHAT_TO_DO, whatToDo);

		MainActivityStateSaver.save(getActivity(), intent);

		startActivity(intent);
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