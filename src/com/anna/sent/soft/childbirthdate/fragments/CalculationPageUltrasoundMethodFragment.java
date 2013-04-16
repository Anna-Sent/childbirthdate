package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;

public class CalculationPageUltrasoundMethodFragment extends DetailsFragment
		implements OnClickListener {
	private NumberPicker numberPickerWeeks, numberPickerDays;
	private DatePicker datePickerUltrasoundDate;
	private RadioButton radioButtonIsGestationalAge, radioButtonIsEmbryonicAge;

	public CalculationPageUltrasoundMethodFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		View v = inflater.inflate(
				R.layout.calculation_page_ultrasound_method_fragment,
				container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		datePickerUltrasoundDate = (DatePicker) getActivity().findViewById(
				R.id.datePickerUltrasoundDate);
		radioButtonIsGestationalAge = (RadioButton) getActivity().findViewById(
				R.id.radioIsGestationalAge);
		radioButtonIsGestationalAge.setOnClickListener(this);
		radioButtonIsEmbryonicAge = (RadioButton) getActivity().findViewById(
				R.id.radioIsEmbryonicAge);
		radioButtonIsEmbryonicAge.setOnClickListener(this);
		numberPickerWeeks = (NumberPicker) getActivity().findViewById(
				R.id.editTextWeeks);
		numberPickerWeeks.setMinValue(0);

		numberPickerDays = (NumberPicker) getActivity().findViewById(
				R.id.editTextDays);
		numberPickerDays.setMinValue(0);
		numberPickerDays.setMaxValue(6);
	}

	private void radioClick(View view) {
		numberPickerWeeks
				.setMaxValue((radioButtonIsGestationalAge.isChecked() ? PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS
						: PregnancyCalculator.EMBRYONIC_AVG_AGE_IN_WEEKS) - 1);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radioIsEmbryonicAge:
		case R.id.radioIsGestationalAge:
			radioClick(v);
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences settings = Shared.getSettings(getActivity());
		int weeks = settings.getInt(Shared.Saved.Calculation.EXTRA_WEEKS, 0);
		int days = settings.getInt(Shared.Saved.Calculation.EXTRA_DAYS, 0);
		boolean isEmbryonicAge = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_IS_EMBRYONIC_AGE, false);
		Calendar ultrasoundDate = Calendar.getInstance();
		ultrasoundDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
				System.currentTimeMillis()));
		Utils.setDate(datePickerUltrasoundDate, ultrasoundDate);

		// first: set radio button state
		if (isEmbryonicAge) {
			radioButtonIsEmbryonicAge.setChecked(true);
		} else {
			radioButtonIsGestationalAge.setChecked(true);
		}

		// second: set max value for weeks number picker
		radioClick(null);

		// third: set value for weeks number picker
		numberPickerWeeks.setValue(weeks);
		numberPickerDays.setValue(days);
	}

	@Override
	public void onPause() {
		super.onPause();
		Calendar ultrasoundDate = Utils.getDate(datePickerUltrasoundDate);
		int weeks = numberPickerWeeks.getValue();
		int days = numberPickerDays.getValue();
		boolean isEmbryonicAge = radioButtonIsEmbryonicAge.isChecked();

		SharedPreferences settings = Shared.getSettings(getActivity());
		Editor editor = settings.edit();
		editor.putLong(Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
				ultrasoundDate.getTimeInMillis());
		editor.putInt(Shared.Saved.Calculation.EXTRA_WEEKS, weeks);
		editor.putInt(Shared.Saved.Calculation.EXTRA_DAYS, days);
		editor.putBoolean(Shared.Saved.Calculation.EXTRA_IS_EMBRYONIC_AGE,
				isEmbryonicAge);
		editor.commit();
	}
}
