package com.anna.sent.soft.childbirthdate.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;

public class TabSettingsFragment extends ScrollViewFragment implements
		OnClickListener {
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

		Button button = (Button) getActivity().findViewById(
				R.id.buttonRestoreDefaultValues);
		button.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences settings = Shared.getSettings(getActivity());
		int menstrualCycleLen = settings.getInt(
				Shared.Saved.Settings.EXTRA_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		int lutealPhaseLen = settings.getInt(
				Shared.Saved.Settings.EXTRA_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
		numberPickerMenstrualCycleLen.setValue(menstrualCycleLen);
		numberPcikerLutealPhaseLen.setValue(lutealPhaseLen);
	}

	@Override
	public void onPause() {
		super.onPause();
		int menstrualCycleLen = numberPickerMenstrualCycleLen.getValue();
		int lutealPhaseLen = numberPcikerLutealPhaseLen.getValue();
		SharedPreferences settings = Shared.getSettings(getActivity());

		Editor editor = settings.edit();
		editor.putInt(Shared.Saved.Settings.EXTRA_MENSTRUAL_CYCLE_LEN,
				menstrualCycleLen);
		editor.putInt(Shared.Saved.Settings.EXTRA_LUTEAL_PHASE_LEN,
				lutealPhaseLen);
		editor.commit();
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
}
