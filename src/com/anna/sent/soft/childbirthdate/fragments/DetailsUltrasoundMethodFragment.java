package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Data;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
import com.anna.sent.soft.utils.DateUtils;

public class DetailsUltrasoundMethodFragment extends DetailsFragment implements
		OnClickListener {
	private NumberPicker numberPickerWeeks, numberPickerDays;
	private DatePicker datePickerUltrasoundDate;
	private RadioButton radioButtonIsGestationalAge, radioButtonIsEmbryonicAge;

	public DetailsUltrasoundMethodFragment() {
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

		View v = inflater.inflate(R.layout.details_ultrasound_method,
				container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		datePickerUltrasoundDate = (DatePicker) getActivity().findViewById(
				R.id.datePickerUltrasoundDate);
		DateUtils.init(datePickerUltrasoundDate, this);

		radioButtonIsGestationalAge = (RadioButton) getActivity().findViewById(
				R.id.radioIsGestationalAge);
		radioButtonIsGestationalAge.setOnClickListener(this);
		radioButtonIsEmbryonicAge = (RadioButton) getActivity().findViewById(
				R.id.radioIsEmbryonicAge);
		radioButtonIsEmbryonicAge.setOnClickListener(this);

		numberPickerWeeks = (NumberPicker) getActivity().findViewById(
				R.id.editTextWeeks);
		numberPickerWeeks.setOnValueChangedListener(this);
		numberPickerWeeks.setMinValue(0);

		numberPickerDays = (NumberPicker) getActivity().findViewById(
				R.id.editTextDays);
		numberPickerDays.setOnValueChangedListener(this);
		numberPickerDays.setMinValue(0);
		numberPickerDays.setMaxValue(6);
	}

	private void radioClick(View view) {
		numberPickerWeeks
				.setMaxValue((radioButtonIsGestationalAge.isChecked() ? PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS
						: PregnancyCalculator.EMBRYONIC_AVG_AGE_IN_WEEKS) - 1);
		dataChanged();
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
	protected void restoreData() {
		Data data = new Data();
		data.restoreUltrasound(getActivity());
		DateUtils.setDate(datePickerUltrasoundDate, data.getUltrasoundDate());

		// first: set radio button state
		if (data.getIsEmbryonicAge()) {
			radioButtonIsEmbryonicAge.setChecked(true);
		} else {
			radioButtonIsGestationalAge.setChecked(true);
		}

		// second: set max value for weeks number picker
		radioClick(null);

		// third: set value for weeks number picker
		numberPickerWeeks.setValue(data.getWeeks());
		numberPickerDays.setValue(data.getDays());

		dataChanged();
	}

	@Override
	protected void saveData() {
		Calendar ultrasoundDate = DateUtils.getDate(datePickerUltrasoundDate);
		int weeks = numberPickerWeeks.getValue();
		int days = numberPickerDays.getValue();
		boolean isEmbryonicAge = radioButtonIsEmbryonicAge.isChecked();
		Data.save(getActivity(), ultrasoundDate, weeks, days, isEmbryonicAge);
	}
}
