package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;

public class CalculationPageLmpMethodFragment extends DetailsFragment implements
		OnClickListener {
	private DatePicker datePickerLastMenstruationDate;
	private NumberPicker numberPickerMenstrualCycleLen,
			numberPcikerLutealPhaseLen;

	public CalculationPageLmpMethodFragment() {
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
				.inflate(R.layout.calculation_page_lmp_method_fragment,
						container, false);
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

		numberPcikerLutealPhaseLen = (NumberPicker) getActivity().findViewById(
				R.id.editTextLutealPhaseLen);
		numberPcikerLutealPhaseLen
				.setMinValue(PregnancyCalculator.MIN_LUTEAL_PHASE_LEN);
		numberPcikerLutealPhaseLen
				.setMaxValue(PregnancyCalculator.MAX_LUTEAL_PHASE_LEN);

		Button button = (Button) getActivity().findViewById(
				R.id.buttonRestoreDefaultValues);
		button.setOnClickListener(this);
	}

	public void restoreDefaultValues(View view) {
		numberPickerMenstrualCycleLen
				.setValue(PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		numberPcikerLutealPhaseLen
				.setValue(PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
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
	public void onResume() {
		super.onResume();
		SharedPreferences settings = Shared.getSettings(getActivity());
		Calendar lastMenstruationDate = Calendar.getInstance();
		lastMenstruationDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_LAST_MENSTRUATION_DATE,
				System.currentTimeMillis()));
		Utils.setDate(datePickerLastMenstruationDate, lastMenstruationDate);
		int menstrualCycleLen = settings.getInt(
				Shared.Saved.Calculation.EXTRA_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		int lutealPhaseLen = settings.getInt(
				Shared.Saved.Calculation.EXTRA_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
		numberPickerMenstrualCycleLen.setValue(menstrualCycleLen);
		numberPcikerLutealPhaseLen.setValue(lutealPhaseLen);
	}

	@Override
	public void onPause() {
		super.onPause();
		int menstrualCycleLen = numberPickerMenstrualCycleLen.getValue();
		int lutealPhaseLen = numberPcikerLutealPhaseLen.getValue();
		Calendar lastMenstruationDate = Utils
				.getDate(datePickerLastMenstruationDate);

		SharedPreferences settings = Shared.getSettings(getActivity());
		Editor editor = settings.edit();
		editor.putLong(Shared.Saved.Calculation.EXTRA_LAST_MENSTRUATION_DATE,
				lastMenstruationDate.getTimeInMillis());
		editor.putInt(Shared.Saved.Calculation.EXTRA_MENSTRUAL_CYCLE_LEN,
				menstrualCycleLen);
		editor.putInt(Shared.Saved.Calculation.EXTRA_LUTEAL_PHASE_LEN,
				lutealPhaseLen);
		editor.commit();
	}
}
