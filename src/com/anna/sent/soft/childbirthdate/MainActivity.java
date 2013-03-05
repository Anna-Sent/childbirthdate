package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

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
	private TextView textViewHelp;

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

	public static final String SETTINGS_FILE = "childbirthdatesettings";

	private int menstrualCycleLen;
	private int lutealPhaseLen;
	private Calendar lastMenstruationDate = Calendar.getInstance();
	private Calendar ovulationDate = Calendar.getInstance();
	private Calendar ultrasoundDate = Calendar.getInstance();
	private int weeks;
	private int days;
	private boolean isFetal;

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

		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		datePickerLastMenstruationDate = (DatePicker) findViewById(R.id.datePickerLastMenstruationDate);

		checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
		linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
		datePickerOvulationDate = (DatePicker) findViewById(R.id.datePickerOvulationDate);

		checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
		linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
		datePickerUltrasoundDate = (DatePicker) findViewById(R.id.datePickerUltrasoundDate);

		radioButtonObstetic = (RadioButton) findViewById(R.id.radioObstetic);

		numberPickerWeeks = (NumberPicker) findViewById(R.id.editTextWeeks);
		numberPickerWeeks.setMinValue(0);

		numberPickerDays = (NumberPicker) findViewById(R.id.editTextDays);
		numberPickerDays.setMinValue(0);
		numberPickerDays
				.setMaxValue(ChildbirthDateCalculator.DAYS_IN_A_WEEK - 1);

		textViewHelp = (TextView) findViewById(R.id.textViewHelp);
		textViewHelp.setText(Html.fromHtml(getString(R.string.help)));
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences settings = getApplicationContext()
				.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
		menstrualCycleLen = settings.getInt(EXTRA_MENSTRUAL_CYCLE_LEN,
				ChildbirthDateCalculator.MENSTRUAL_CYCLE_AVG_LENGTH);
		lutealPhaseLen = settings.getInt(EXTRA_LUTEAL_PHASE_LEN,
				ChildbirthDateCalculator.LUTEAL_PHASE_AVG_LENGTH);
		checkBox1.setChecked(settings.getBoolean(
				EXTRA_BY_LAST_MENSTRUATION_DATE, false));
		lastMenstruationDate.setTimeInMillis(settings.getLong(
				EXTRA_LAST_MENSTRUATION_DATE, System.currentTimeMillis()));
		checkBox2.setChecked(settings
				.getBoolean(EXTRA_BY_OVULATION_DATE, false));
		ovulationDate.setTimeInMillis(settings.getLong(EXTRA_OVULATION_DATE,
				System.currentTimeMillis()));
		checkBox3.setChecked(settings.getBoolean(EXTRA_BY_ULTRASOUND, false));
		ultrasoundDate.setTimeInMillis(settings.getLong(EXTRA_ULTRASOUND_DATE,
				System.currentTimeMillis()));
		weeks = settings.getInt(EXTRA_WEEKS, 0);
		days = settings.getInt(EXTRA_DAYS, 0);
		isFetal = settings.getBoolean(EXTRA_IS_FETAL, isFetal);
		writeValues();
		checkVisibility(checkBox1, linearLayout1);
		checkVisibility(checkBox2, linearLayout2);
		checkVisibility(checkBox3, linearLayout3);
		radioClick(null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences settings = getApplicationContext()
				.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		readValues();
		editor.putInt(EXTRA_MENSTRUAL_CYCLE_LEN, menstrualCycleLen);
		editor.putInt(EXTRA_LUTEAL_PHASE_LEN, lutealPhaseLen);
		editor.putBoolean(EXTRA_BY_LAST_MENSTRUATION_DATE,
				checkBox1.isChecked());
		editor.putLong(EXTRA_LAST_MENSTRUATION_DATE,
				lastMenstruationDate.getTimeInMillis());
		editor.putBoolean(EXTRA_BY_OVULATION_DATE, checkBox2.isChecked());
		editor.putLong(EXTRA_OVULATION_DATE, ovulationDate.getTimeInMillis());
		editor.putBoolean(EXTRA_BY_ULTRASOUND, checkBox3.isChecked());
		editor.putLong(EXTRA_ULTRASOUND_DATE, ultrasoundDate.getTimeInMillis());
		editor.putInt(EXTRA_WEEKS, weeks);
		editor.putInt(EXTRA_DAYS, days);
		editor.putBoolean(EXTRA_IS_FETAL, isFetal);
		editor.commit();
	}

	public void radioClick(View view) {
		numberPickerWeeks
				.setMaxValue((radioButtonObstetic.isChecked() ? ChildbirthDateCalculator.OBSTETIC_PREGNANCY_PERIOD
						: ChildbirthDateCalculator.FETAL_PREGNANCY_PERIOD) - 1);
	}

	private Calendar getDate(DatePicker datePicker) {
		Calendar date = Calendar.getInstance();
		date.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth());
		return date;
	}

	private void readValues() {
		menstrualCycleLen = numberPickerMenstrualCycleLen.getValue();
		lutealPhaseLen = numberPcikerLutealPhaseLen.getValue();
		lastMenstruationDate = getDate(datePickerLastMenstruationDate);
		ovulationDate = getDate(datePickerOvulationDate);
		ultrasoundDate = getDate(datePickerUltrasoundDate);
		weeks = numberPickerWeeks.getValue();
		days = numberPickerDays.getValue();
		isFetal = !radioButtonObstetic.isChecked();
	}

	private void setDate(DatePicker datePicker, Calendar date) {
		datePicker.updateDate(date.get(Calendar.YEAR),
				date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
	}

	private void writeValues() {
		numberPickerMenstrualCycleLen.setValue(menstrualCycleLen);
		numberPcikerLutealPhaseLen.setValue(lutealPhaseLen);
		setDate(datePickerLastMenstruationDate, lastMenstruationDate);
		setDate(datePickerOvulationDate, ovulationDate);
		setDate(datePickerUltrasoundDate, ultrasoundDate);
		numberPickerWeeks.setValue(weeks);
		numberPickerDays.setValue(days);
		radioButtonObstetic.setChecked(!isFetal);
	}

	public void restoreDefaultValues(View view) {
		numberPickerMenstrualCycleLen
				.setValue(ChildbirthDateCalculator.MENSTRUAL_CYCLE_AVG_LENGTH);
		numberPcikerLutealPhaseLen
				.setValue(ChildbirthDateCalculator.LUTEAL_PHASE_AVG_LENGTH);
	}

	public void calculate(View view) {
		readValues();
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
