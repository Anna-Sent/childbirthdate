package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Constants;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
import com.anna.sent.soft.utils.StateSaver;

public class TabCalculationFragment extends ScrollViewFragment implements
		StateSaver, OnClickListener {
	private static final String EXTRA_GUI_CURRENT_DATE = "com.anna.sent.soft.childbirthdate.currentdate";

	private CheckBox checkBox1, checkBox2, checkBox3;
	private NumberPicker numberPickerWeeks, numberPickerDays;
	private DatePicker datePickerLastMenstruationDate, datePickerOvulationDate,
			datePickerUltrasoundDate, datePickerCurrentDate;
	private RadioButton radioButtonIsGestationalAge, radioButtonIsEmbryonicAge;
	private CalculationPageLmpMethodFragment lmpMethodFragment;
	private CalculationPageOvulationDateMethodFragment ovulationDateFragment;
	private CalculationPageUltrasoundMethodFragment ultrasoundFragment;

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
		FragmentManager fm = getFragmentManager();

		checkBox1 = (CheckBox) getActivity().findViewById(R.id.checkBox1);
		checkBox1.setOnClickListener(this);
		lmpMethodFragment = (CalculationPageLmpMethodFragment) fm
				.findFragmentById(R.id.lmpMethodFragment);
		datePickerLastMenstruationDate = (DatePicker) getActivity()
				.findViewById(R.id.datePickerLastMenstruationDate);

		checkBox2 = (CheckBox) getActivity().findViewById(R.id.checkBox2);
		checkBox2.setOnClickListener(this);
		ovulationDateFragment = (CalculationPageOvulationDateMethodFragment) fm
				.findFragmentById(R.id.ovulationDateMethodFragment);
		datePickerOvulationDate = (DatePicker) getActivity().findViewById(
				R.id.datePickerOvulationDate);

		checkBox3 = (CheckBox) getActivity().findViewById(R.id.checkBox3);
		checkBox3.setOnClickListener(this);
		ultrasoundFragment = (CalculationPageUltrasoundMethodFragment) fm
				.findFragmentById(R.id.ultrasoundMethodFragment);
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
		datePickerCurrentDate = (DatePicker) getActivity().findViewById(
				R.id.datePickerCurrentDate);
	}

	@Override
	protected void restoreState(Bundle state) {
		super.restoreState(state);
		Calendar value = Calendar.getInstance();
		value.setTimeInMillis(state.getLong(EXTRA_GUI_CURRENT_DATE,
				System.currentTimeMillis()));
		setDate(datePickerCurrentDate, value);
	}

	@Override
	protected void saveState(Bundle state) {
		super.saveState(state);
		if (datePickerCurrentDate != null) {
			state.putLong(EXTRA_GUI_CURRENT_DATE,
					getDate(datePickerCurrentDate).getTimeInMillis());
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		SharedPreferences settings = getActivity().getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS_FILE,
						Context.MODE_PRIVATE);
		checkBox1.setChecked(settings.getBoolean(
				Constants.EXTRA_BY_LAST_MENSTRUATION_DATE, false));
		Calendar lastMenstruationDate = Calendar.getInstance();
		lastMenstruationDate.setTimeInMillis(settings.getLong(
				Constants.EXTRA_LAST_MENSTRUATION_DATE,
				System.currentTimeMillis()));
		checkBox2.setChecked(settings.getBoolean(
				Constants.EXTRA_BY_OVULATION_DATE, false));
		Calendar ovulationDate = Calendar.getInstance();
		ovulationDate.setTimeInMillis(settings.getLong(
				Constants.EXTRA_OVULATION_DATE, System.currentTimeMillis()));
		checkBox3.setChecked(settings.getBoolean(Constants.EXTRA_BY_ULTRASOUND,
				false));
		Calendar ultrasoundDate = Calendar.getInstance();
		ultrasoundDate.setTimeInMillis(settings.getLong(
				Constants.EXTRA_ULTRASOUND_DATE, System.currentTimeMillis()));
		int weeks = settings.getInt(Constants.EXTRA_WEEKS, 0);
		int days = settings.getInt(Constants.EXTRA_DAYS, 0);
		boolean isEmbryonicAge = settings.getBoolean(
				Constants.EXTRA_IS_EMBRYONIC_AGE, false);
		setDate(datePickerLastMenstruationDate, lastMenstruationDate);
		setDate(datePickerOvulationDate, ovulationDate);
		setDate(datePickerUltrasoundDate, ultrasoundDate);

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
		checkVisibility(checkBox1, lmpMethodFragment);
		checkVisibility(checkBox2, ovulationDateFragment);
		checkVisibility(checkBox3, ultrasoundFragment);
	}

	@Override
	public void onPause() {
		super.onPause();

		Calendar lastMenstruationDate = getDate(datePickerLastMenstruationDate);
		Calendar ovulationDate = getDate(datePickerOvulationDate);
		Calendar ultrasoundDate = getDate(datePickerUltrasoundDate);
		int weeks = numberPickerWeeks.getValue();
		int days = numberPickerDays.getValue();
		boolean isEmbryonicAge = radioButtonIsEmbryonicAge.isChecked();

		SharedPreferences settings = getActivity().getApplicationContext()
				.getSharedPreferences(Constants.SETTINGS_FILE,
						Context.MODE_PRIVATE);

		Editor editor = settings.edit();

		editor.putBoolean(Constants.EXTRA_BY_LAST_MENSTRUATION_DATE,
				checkBox1.isChecked());
		editor.putLong(Constants.EXTRA_LAST_MENSTRUATION_DATE,
				lastMenstruationDate.getTimeInMillis());
		editor.putBoolean(Constants.EXTRA_BY_OVULATION_DATE,
				checkBox2.isChecked());
		editor.putLong(Constants.EXTRA_OVULATION_DATE,
				ovulationDate.getTimeInMillis());
		editor.putBoolean(Constants.EXTRA_BY_ULTRASOUND, checkBox3.isChecked());
		editor.putLong(Constants.EXTRA_ULTRASOUND_DATE,
				ultrasoundDate.getTimeInMillis());
		editor.putInt(Constants.EXTRA_WEEKS, weeks);
		editor.putInt(Constants.EXTRA_DAYS, days);
		editor.putBoolean(Constants.EXTRA_IS_EMBRYONIC_AGE, isEmbryonicAge);

		editor.commit();
	}

	private void checkVisibility(CheckBox checkBox, Fragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.anim.fade_in, 0);

		if (checkBox.isChecked()) {
			ft.show(fragment);
		} else {
			ft.hide(fragment);
		}

		ft.commit();
	}

	private Calendar getDate(DatePicker datePicker) {
		Calendar date = Calendar.getInstance();
		date.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth());
		return date;
	}

	private void setDate(DatePicker datePicker, Calendar date) {
		datePicker.updateDate(date.get(Calendar.YEAR),
				date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
	}

	private void radioClick(View view) {
		numberPickerWeeks
				.setMaxValue((radioButtonIsGestationalAge.isChecked() ? PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS
						: PregnancyCalculator.EMBRYONIC_AVG_AGE_IN_WEEKS) - 1);

	}

	private void check(View view) {
		if (view.equals(checkBox1)) {
			checkVisibility(checkBox1, lmpMethodFragment);
		}

		if (view.equals(checkBox2)) {
			checkVisibility(checkBox2, ovulationDateFragment);
		}

		if (view.equals(checkBox3)) {
			checkVisibility(checkBox3, ultrasoundFragment);
		}
	}

	public void putExtras(Intent intent) {
		Calendar lastMenstruationDate = getDate(datePickerLastMenstruationDate);
		Calendar ovulationDate = getDate(datePickerOvulationDate);
		Calendar ultrasoundDate = getDate(datePickerUltrasoundDate);
		int weeks = numberPickerWeeks.getValue();
		int days = numberPickerDays.getValue();
		boolean isEmbryonicAge = radioButtonIsEmbryonicAge.isChecked();

		intent.putExtra(Constants.EXTRA_BY_LAST_MENSTRUATION_DATE,
				checkBox1.isChecked());
		intent.putExtra(Constants.EXTRA_LAST_MENSTRUATION_DATE,
				lastMenstruationDate);
		intent.putExtra(Constants.EXTRA_BY_OVULATION_DATE,
				checkBox2.isChecked());
		intent.putExtra(Constants.EXTRA_OVULATION_DATE, ovulationDate);
		intent.putExtra(Constants.EXTRA_BY_ULTRASOUND, checkBox3.isChecked());
		intent.putExtra(Constants.EXTRA_ULTRASOUND_DATE, ultrasoundDate);
		intent.putExtra(Constants.EXTRA_WEEKS, weeks);
		intent.putExtra(Constants.EXTRA_DAYS, days);
		intent.putExtra(Constants.EXTRA_IS_EMBRYONIC_AGE, isEmbryonicAge);
		intent.putExtra(Constants.EXTRA_CURRENT_DATE,
				getDate(datePickerCurrentDate));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkBox1:
		case R.id.checkBox2:
		case R.id.checkBox3:
			check(v);
			break;
		case R.id.radioIsEmbryonicAge:
		case R.id.radioIsGestationalAge:
			radioClick(v);
		}
	}
}