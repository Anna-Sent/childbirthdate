package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
import com.anna.sent.soft.utils.StateSaver;
import com.anna.sent.soft.childbirthdate.shared.Constants;

public class TabSettingsFragment extends Fragment implements StateSaver {
	private static final String EXTRA_GUI_SCROLL_Y = "com.anna.sent.soft.childbirthdate.tabsettings.scrolly";
	private NumberPicker numberPickerMenstrualCycleLen,
			numberPcikerLutealPhaseLen;
	private ScrollView scrollView;

	public TabSettingsFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab_settings, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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

		scrollView = (ScrollView) getActivity().findViewById(R.id.tabSettings);

		if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
			restoreState(savedInstanceState);
		} else {
			savedInstanceState = getActivity().getIntent().getExtras();
			if (savedInstanceState != null) {
				restoreState(savedInstanceState);
			}
		}
	}

	private void restoreState(Bundle state) {
		final int y = state.getInt(EXTRA_GUI_SCROLL_Y, 0);
		scrollView.post(new Runnable() {
			@Override
			public void run() {
				scrollView.scrollTo(0, y);
			}
		});
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (scrollView != null) {
			outState.putInt(EXTRA_GUI_SCROLL_Y, scrollView.getScrollY());
		}
	}
}
