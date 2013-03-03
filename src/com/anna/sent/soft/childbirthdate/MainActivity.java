package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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
			numberPcikerLutealPhaseLen;
	private DatePicker datePickerLastMenstruationDate, datePickerOvulationDate,
			datePickerUltrasoundDate;
	private EditText editTextWeeks, editTextDays;
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
		
		editTextWeeks = (EditText) findViewById(R.id.editTextWeeks);
		editTextDays = (EditText) findViewById(R.id.editTextDays);
		
		radioButtonObstetic = (RadioButton) findViewById(R.id.radioObstetic);
	}

	public void restoreDefaultValues(View view) {
		numberPickerMenstrualCycleLen
				.setValue(ChildbirthDateCalculator.MENSTRUAL_CYCLE_AVG_LENGTH);
		numberPcikerLutealPhaseLen
				.setValue(ChildbirthDateCalculator.LUTEAL_PHASE_AVG_LENGTH);
	}

	public void calculate(View view) {
		if (checkBox1.isChecked() || checkBox2.isChecked()
				|| checkBox3.isChecked()) {
			//Calendar lastMenstruationDate = datePickerLastMenstruationDate.
			
			Intent intent = new Intent(this, ResultActivity.class);
			// EditText editText = (EditText) findViewById(R.id.edit_message);
			// String message = editText.getText().toString();
			// intent.putExtra(EXTRA_MESSAGE, message);
			startActivity(intent);
		} else {
			Dialog dialog = new Dialog(getApplicationContext());
			dialog.show(); // данные не введены. обратитесь в жк для уточнения
							// срока беременности
		}
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
