package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	private CheckBox checkBox1, checkBox2, checkBox3;
	private LinearLayout linearLayout1, linearLayout2, linearLayout3;
	private TabHost mTabHost;
	private NumberPicker numberPickerMenstrualCycleLen,
			numberPcikerLutealPhaseLen, numberPickerWeeks, numberPickerDays;
	private DatePicker datePickerLastMenstruationDate, datePickerOvulationDate,
			datePickerUltrasoundDate;
	private RadioButton radioButtonObstetic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
				.setIndicator(getString(R.string.tab1)).setContent(R.id.tab1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
				.setIndicator(getString(R.string.tab2)).setContent(R.id.tab2));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test3")
				.setIndicator(getString(R.string.tab3)).setContent(R.id.tab3));

		mTabHost.setCurrentTab(1);

		numberPickerMenstrualCycleLen = (NumberPicker) findViewById(R.id.editTextMenstrualCycleLen);
		numberPickerMenstrualCycleLen
				.setMinValue(ChildbirthDateCalculator.MIN_MENSTRUAL_CYCLE_LEN);
		numberPickerMenstrualCycleLen
				.setMaxValue(ChildbirthDateCalculator.MAX_MENSTRUAL_CYCLE_LEN);

		numberPcikerLutealPhaseLen = (NumberPicker) findViewById(R.id.editTextLutealPhaseLen);
		numberPcikerLutealPhaseLen
				.setMinValue(ChildbirthDateCalculator.MIN_LUTEAL_PHASE_LEN);
		numberPcikerLutealPhaseLen
				.setMaxValue(ChildbirthDateCalculator.MAX_LUTEAL_PHASE_LEN);

		restoreDefaultValues(null);

		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		checkVisibility(checkBox1, linearLayout1);
		datePickerLastMenstruationDate = (DatePicker) findViewById(R.id.datePickerLastMenstruationDate);

		checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
		linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
		checkVisibility(checkBox2, linearLayout2);
		datePickerOvulationDate = (DatePicker) findViewById(R.id.datePickerOvulationDate);

		checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
		linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
		checkVisibility(checkBox3, linearLayout3);
		datePickerUltrasoundDate = (DatePicker) findViewById(R.id.datePickerUltrasoundDate);

		radioButtonObstetic = (RadioButton) findViewById(R.id.radioObstetic);

		numberPickerWeeks = (NumberPicker) findViewById(R.id.editTextWeeks);
		numberPickerWeeks.setMinValue(0);
		radioClick(null);
		numberPickerWeeks.setValue(0);

		numberPickerDays = (NumberPicker) findViewById(R.id.editTextDays);
		numberPickerDays.setMinValue(0);
		numberPickerDays
				.setMaxValue(ChildbirthDateCalculator.DAYS_IN_A_WEEK - 1);
		numberPickerDays.setValue(0);
	}

	public void radioClick(View view) {
		numberPickerWeeks
				.setMaxValue((radioButtonObstetic.isChecked() ? ChildbirthDateCalculator.OBSTETIC_PREGNANCY_PERIOD
						: ChildbirthDateCalculator.FETAL_PREGNANCY_PERIOD) - 1);
	}

	public void restoreDefaultValues(View view) {
		numberPickerMenstrualCycleLen
				.setValue(ChildbirthDateCalculator.MENSTRUAL_CYCLE_AVG_LENGTH);
		numberPcikerLutealPhaseLen
				.setValue(ChildbirthDateCalculator.LUTEAL_PHASE_AVG_LENGTH);
	}

	private Calendar getDate(DatePicker datePicker) {
		Calendar date = Calendar.getInstance();
		date.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth());
		return date;
	}

	public static final String EXTRA_MENSTRUAL_CYCLE_LEN = "com.anna.sent.soft.childbirthdate.menstrualcyclelen";
	public static final String EXTRA_LUTEAL_PHASE_LEN = "com.anna.sent.soft.childbirthdate.lutealphaselen";
	public static final String EXTRA_BY_LAST_MENSTRUATION_DATE = "com.anna.sent.soft.childbirthdate.bylastmenstruationdate";
	public static final String EXTRA_LAST_MENSTRUATION_DATE = "com.anna.sent.soft.childbirthdate.lastmenstruationdate";
	public static final String EXTRA_BY_OVULATION_DATE = "com.anna.sent.soft.childbirthdate.byovulationdate";
	public static final String EXTRA_OVULATION_DATE = "com.anna.sent.soft.childbirthdate.ovulationdate";
	public static final String EXTRA_BY_ULTRASOUND = "com.anna.sent.soft.childbirthdate.byultrasound";
	public static final String EXTRA_ULTRASOUND_DATE = "com.anna.sent.soft.childbirthdate.ultrasounddate";
	public static final String EXTRA_WEEKS = "com.anna.sent.soft.childbirthdate.weeks";
	public static final String EXTRA_DAYS = "com.anna.sent.soft.childbirthdate.days";
	public static final String EXTRA_IS_FETAL = "com.anna.sent.soft.childbirthdate.isfetal";

	public void calculate(View view) {
		int menstrualCycleLen = numberPickerMenstrualCycleLen.getValue();
		int lutealPhaseLen = numberPcikerLutealPhaseLen.getValue();
		Calendar lastMenstruationDate = getDate(datePickerLastMenstruationDate);
		Calendar ovulationDate = getDate(datePickerOvulationDate);
		Calendar ultrasoundDate = getDate(datePickerUltrasoundDate);
		int weeks = numberPickerWeeks.getValue();
		int days = numberPickerDays.getValue();
		boolean isFetal = !radioButtonObstetic.isChecked();

		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra(EXTRA_MENSTRUAL_CYCLE_LEN, menstrualCycleLen);
		intent.putExtra(EXTRA_LUTEAL_PHASE_LEN, lutealPhaseLen);
		intent.putExtra(EXTRA_BY_LAST_MENSTRUATION_DATE, checkBox1.isChecked());
		intent.putExtra(EXTRA_LAST_MENSTRUATION_DATE, lastMenstruationDate);
		intent.putExtra(EXTRA_BY_OVULATION_DATE, checkBox2.isChecked());
		intent.putExtra(EXTRA_OVULATION_DATE, ovulationDate);
		intent.putExtra(EXTRA_BY_ULTRASOUND, checkBox3.isChecked());
		intent.putExtra(EXTRA_ULTRASOUND_DATE, ultrasoundDate);
		intent.putExtra(EXTRA_WEEKS, weeks);
		intent.putExtra(EXTRA_DAYS, days);
		intent.putExtra(EXTRA_IS_FETAL, isFetal);
		startActivity(intent);
	}

	private void checkVisibility(CheckBox checkBox, LinearLayout linearLayout) {
		linearLayout.setVisibility(checkBox.isChecked() ? View.VISIBLE
				: View.GONE);
	}

	public void check(View view) {
		if (view.equals(checkBox1)) {
			checkVisibility(checkBox1, linearLayout1);
		}

		if (view.equals(checkBox2)) {
			checkVisibility(checkBox2, linearLayout2);
		}

		if (view.equals(checkBox3)) {
			checkVisibility(checkBox3, linearLayout3);
		}
	}
}
