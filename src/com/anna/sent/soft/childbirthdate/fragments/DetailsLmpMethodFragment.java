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
import com.anna.sent.soft.childbirthdate.shared.Data;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
import com.anna.sent.soft.utils.DateUtils;

public class DetailsLmpMethodFragment extends DetailsFragment implements
		OnClickListener {
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
		DateUtils.init(datePickerLastMenstruationDate, this);

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
		numberPickerMenstrualCycleLen
				.setValue(PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		numberPcikerLutealPhaseLen
				.setValue(PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
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
	protected void restoreData() {
		Data data = new Data();
		data.restoreLmp(getActivity());
		DateUtils.setDate(datePickerLastMenstruationDate,
				data.getLastMenstruationDate());
		numberPickerMenstrualCycleLen.setValue(data.getMenstrualCycleLen());
		numberPcikerLutealPhaseLen.setValue(data.getLutealPhaseLen());

		dataChanged();
	}

	@Override
	protected void saveData() {
		int menstrualCycleLen = numberPickerMenstrualCycleLen.getValue();
		int lutealPhaseLen = numberPcikerLutealPhaseLen.getValue();
		Calendar lastMenstruationDate = DateUtils
				.getDate(datePickerLastMenstruationDate);
		Data.save(getActivity(), lastMenstruationDate, menstrualCycleLen,
				lutealPhaseLen);
	}
}
