package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
import com.anna.sent.soft.utils.DateUtils;

public class DetailsLmpMethodFragment extends DetailsFragment implements
		OnClickListener, NumberPicker.OnValueChangeListener,
		DatePicker.OnDateChangedListener {
	private DatePicker datePickerLastMenstruationDate;
	private NumberPicker numberPickerMenstrualCycleLen,
			numberPcikerLutealPhaseLen;

	public DetailsLmpMethodFragment() {
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

		View v = inflater
				.inflate(R.layout.details_lmp_method, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		datePickerLastMenstruationDate = (DatePicker) getActivity()
				.findViewById(R.id.datePickerLastMenstruationDate);

		numberPickerMenstrualCycleLen = (NumberPicker) getActivity()
				.findViewById(R.id.editTextMenstrualCycleLen);
		numberPickerMenstrualCycleLen
				.setMinValue(PregnancyCalculator.MIN_MENSTRUAL_CYCLE_LEN);
		numberPickerMenstrualCycleLen
				.setMaxValue(PregnancyCalculator.MAX_MENSTRUAL_CYCLE_LEN);
		numberPickerMenstrualCycleLen.setOnValueChangedListener(this);

		numberPcikerLutealPhaseLen = (NumberPicker) getActivity().findViewById(
				R.id.editTextLutealPhaseLen);
		numberPcikerLutealPhaseLen
				.setMinValue(PregnancyCalculator.MIN_LUTEAL_PHASE_LEN);
		numberPcikerLutealPhaseLen
				.setMaxValue(PregnancyCalculator.MAX_LUTEAL_PHASE_LEN);
		numberPcikerLutealPhaseLen.setOnValueChangedListener(this);

		Button button = (Button) getActivity().findViewById(
				R.id.buttonRestoreDefaultValues);
		button.setOnClickListener(this);
	}

	public void restoreDefaultValues(View view) {
		int menstrualCycleLen = PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH;
		int lutealPhaseLen = PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH;

		numberPickerMenstrualCycleLen.setValue(menstrualCycleLen);
		if (mData != null) {
			mData.setMenstrualCycleLen(menstrualCycleLen);
		}

		numberPcikerLutealPhaseLen.setValue(lutealPhaseLen);
		if (mData != null) {
			mData.setLutealPhaseLen(lutealPhaseLen);
		}

		dataChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonRestoreDefaultValues:
			restoreDefaultValues(v);
			break;
		}
	}

	@Override
	protected void updateData() {
		if (mData != null) {
			DateUtils.init(datePickerLastMenstruationDate, null);
			DateUtils.init(datePickerLastMenstruationDate,
					mData.getLastMenstruationDate(), this);

			numberPickerMenstrualCycleLen
					.setValue(mData.getMenstrualCycleLen());
			numberPcikerLutealPhaseLen.setValue(mData.getLutealPhaseLen());
		}
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		if (mData != null) {
			if (picker.getId() == R.id.editTextMenstrualCycleLen) {
				int menstrualCycleLen = numberPickerMenstrualCycleLen
						.getValue();
				mData.setMenstrualCycleLen(menstrualCycleLen);
			} else if (picker.getId() == R.id.editTextLutealPhaseLen) {
				int lutealPhaseLen = numberPcikerLutealPhaseLen.getValue();
				mData.setLutealPhaseLen(lutealPhaseLen);
			}
		}

		dataChanged();
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if (mData != null) {
			Calendar lastMenstruationDate = DateUtils
					.getDate(datePickerLastMenstruationDate);
			mData.setLastMenstruationDate(lastMenstruationDate);
		}

		dataChanged();
	}
}
