package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Constants;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
import com.anna.sent.soft.utils.StateSaver;

public class TabSettingsFragment extends ScrollViewFragment implements
		StateSaver {
	private NumberPicker numberPickerMenstrualCycleLen,
			numberPcikerLutealPhaseLen;

	public TabSettingsFragment() {
		super();
	}

	@Override
	protected int getLayoutResourceId() {
		return R.layout.tab_settings;
	}

	@Override
	protected int getScrollViewResourceId() {
		return R.id.tabSettings;
	}

	@Override
	protected void setViews() {
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
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences settings = getActivity().getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS_FILE,
						Context.MODE_PRIVATE);
		int menstrualCycleLen = settings.getInt(
				Constants.EXTRA_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		int lutealPhaseLen = settings.getInt(Constants.EXTRA_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
		numberPickerMenstrualCycleLen.setValue(menstrualCycleLen);
		numberPcikerLutealPhaseLen.setValue(lutealPhaseLen);
	}

	@Override
	public void onPause() {
		super.onPause();
		int menstrualCycleLen = numberPickerMenstrualCycleLen.getValue();
		int lutealPhaseLen = numberPcikerLutealPhaseLen.getValue();
		SharedPreferences settings = getActivity().getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS_FILE,
						Context.MODE_PRIVATE);

		Editor editor = settings.edit();
		editor.putInt(Constants.EXTRA_MENSTRUAL_CYCLE_LEN, menstrualCycleLen);
		editor.putInt(Constants.EXTRA_LUTEAL_PHASE_LEN, lutealPhaseLen);
		editor.commit();
	}

	public void putExtras(Intent intent) {
		int menstrualCycleLen = numberPickerMenstrualCycleLen.getValue();
		int lutealPhaseLen = numberPcikerLutealPhaseLen.getValue();

		intent.putExtra(Constants.EXTRA_MENSTRUAL_CYCLE_LEN, menstrualCycleLen);
		intent.putExtra(Constants.EXTRA_LUTEAL_PHASE_LEN, lutealPhaseLen);
	}

	public void restoreDefaultValues(View view) {
		numberPickerMenstrualCycleLen
				.setValue(PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		numberPcikerLutealPhaseLen
				.setValue(PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
	}
}
