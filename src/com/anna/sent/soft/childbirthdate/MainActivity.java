package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
import com.anna.sent.soft.utils.StateSaver;
import com.anna.sent.soft.utils.Utils;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements StateSaver {

	private CheckBox checkBox1, checkBox2, checkBox3;
	private LinearLayout linearLayout1, linearLayout2, linearLayout3;
	private TabHost mTabHost;
	private NumberPicker numberPickerMenstrualCycleLen,
			numberPcikerLutealPhaseLen, numberPickerWeeks, numberPickerDays;
	private DatePicker datePickerLastMenstruationDate, datePickerOvulationDate,
			datePickerUltrasoundDate, datePickerCurrentDate;
	private RadioButton radioButtonIsGestationalAge, radioButtonIsEmbryonicAge;
	private TextView textViewHelp;
	private ScrollView tab1, tab2, tab3;

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

	// rename to COUNTING_METHOD, compatibility with old version
	public static final String EXTRA_IS_EMBRYONIC_AGE = "com.anna.sent.soft.childbirthdate.isfetal";

	public static final String EXTRA_WHAT_TO_DO = "com.anna.sent.soft.childbirthdate.whattodo";
	public static final int CALCULATE_NOTHING = 0;
	public static final int CALCULATE_EGA = 1;
	public static final int CALCULATE_ECD = 2;
	public static final String EXTRA_CURRENT_DATE = "com.anna.sent.soft.childbirthdate.currentdate";

	public static final String SETTINGS_FILE = "childbirthdatesettings";

	private int menstrualCycleLen;
	private int lutealPhaseLen;
	private Calendar lastMenstruationDate = Calendar.getInstance();
	private Calendar ovulationDate = Calendar.getInstance();
	private Calendar ultrasoundDate = Calendar.getInstance();
	private int weeks;
	private int days;
	private boolean isEmbryonicAge;

	private SharedPreferences settings;

	private static final String EXTRA_GUI_SCROLL_Y = "com.anna.sent.soft.childbirthdate.scrolly";
	private static final String EXTRA_GUI_CURRENT_DATE = "com.anna.sent.soft.childbirthdate.currentdate";
	private static final String EXTRA_GUI_CURRENT_TAB = "com.anna.sent.soft.childbirthdate.currenttab";

	private static final String EXTRA_GUI_THEME_ID = "com.anna.sent.soft.childbirthdate.themeid";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		settings = getApplicationContext().getSharedPreferences(SETTINGS_FILE,
				Context.MODE_PRIVATE);
		int themeId = settings.getInt(EXTRA_GUI_THEME_ID, Utils.DARK_THEME);
		Utils.onActivityCreateSetTheme(this, themeId);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("tab_settings")
				.setIndicator(getString(R.string.settings))
				.setContent(R.id.tab1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_calculation")
				.setIndicator(getString(R.string.calculation))
				.setContent(R.id.tab2));
		mTabHost.addTab(mTabHost.newTabSpec("tab_help")
				.setIndicator(getString(R.string.help)).setContent(R.id.tab3));

		numberPickerMenstrualCycleLen = (NumberPicker) findViewById(R.id.editTextMenstrualCycleLen);
		numberPickerMenstrualCycleLen
				.setMinValue(PregnancyCalculator.MIN_MENSTRUAL_CYCLE_LEN);
		numberPickerMenstrualCycleLen
				.setMaxValue(PregnancyCalculator.MAX_MENSTRUAL_CYCLE_LEN);

		numberPcikerLutealPhaseLen = (NumberPicker) findViewById(R.id.editTextLutealPhaseLen);
		numberPcikerLutealPhaseLen
				.setMinValue(PregnancyCalculator.MIN_LUTEAL_PHASE_LEN);
		numberPcikerLutealPhaseLen
				.setMaxValue(PregnancyCalculator.MAX_LUTEAL_PHASE_LEN);

		checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		datePickerLastMenstruationDate = (DatePicker) findViewById(R.id.datePickerLastMenstruationDate);

		checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
		linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
		datePickerOvulationDate = (DatePicker) findViewById(R.id.datePickerOvulationDate);

		checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
		linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
		datePickerUltrasoundDate = (DatePicker) findViewById(R.id.datePickerUltrasoundDate);

		datePickerCurrentDate = (DatePicker) findViewById(R.id.datePickerCurrentDate);

		radioButtonIsGestationalAge = (RadioButton) findViewById(R.id.radioIsGestationalAge);
		radioButtonIsEmbryonicAge = (RadioButton) findViewById(R.id.radioIsEmbryonicAge);

		numberPickerWeeks = (NumberPicker) findViewById(R.id.editTextWeeks);
		numberPickerWeeks.setMinValue(0);

		numberPickerDays = (NumberPicker) findViewById(R.id.editTextDays);
		numberPickerDays.setMinValue(0);
		numberPickerDays.setMaxValue(6);

		textViewHelp = (TextView) findViewById(R.id.textViewHelp);
		textViewHelp.setText(Html.fromHtml(getString(R.string.bigHelp)));

		tab1 = (ScrollView) findViewById(R.id.tab1);
		tab2 = (ScrollView) findViewById(R.id.tab2);
		tab3 = (ScrollView) findViewById(R.id.tab3);

		Intent intent = getIntent();
		int currentTab = intent.getIntExtra(EXTRA_GUI_CURRENT_TAB, 1);
		mTabHost.setCurrentTab(currentTab);

		int y = intent.getIntExtra(EXTRA_GUI_SCROLL_Y, 0);
		setScrollY(y);

		Calendar value = Calendar.getInstance();
		value.setTimeInMillis(intent.getLongExtra(EXTRA_GUI_CURRENT_DATE,
				System.currentTimeMillis()));
		setDate(datePickerCurrentDate, value);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// settings = getApplicationContext()
		// .getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
		menstrualCycleLen = settings.getInt(EXTRA_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		lutealPhaseLen = settings.getInt(EXTRA_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
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
		isEmbryonicAge = settings.getBoolean(EXTRA_IS_EMBRYONIC_AGE,
				isEmbryonicAge);
		writeValues();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// SharedPreferences settings = getApplicationContext()
		// .getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
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
		editor.putBoolean(EXTRA_IS_EMBRYONIC_AGE, isEmbryonicAge);

		editor.putInt(EXTRA_GUI_THEME_ID, Utils.getThemeId());

		editor.commit();
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
		isEmbryonicAge = radioButtonIsEmbryonicAge.isChecked();
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
		checkVisibility(checkBox1, linearLayout1);
		checkVisibility(checkBox2, linearLayout2);
		checkVisibility(checkBox3, linearLayout3);
	}

	public void restoreDefaultValues(View view) {
		numberPickerMenstrualCycleLen
				.setValue(PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		numberPcikerLutealPhaseLen
				.setValue(PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
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
		intent.putExtra(EXTRA_IS_EMBRYONIC_AGE, isEmbryonicAge);

		int viewId = view.getId();
		if (view.getId() == R.id.buttonCalculateEstimatedChildbirthDate) {
			intent.putExtra(EXTRA_WHAT_TO_DO, CALCULATE_ECD);
		} else if (viewId == R.id.buttonCalculateEstimatedGestationalAge) {
			intent.putExtra(EXTRA_WHAT_TO_DO, CALCULATE_EGA);
			intent.putExtra(EXTRA_CURRENT_DATE, getDate(datePickerCurrentDate));
		}

		startActivity(intent);
	}

	public void radioClick(View view) {
		numberPickerWeeks
				.setMaxValue((radioButtonIsGestationalAge.isChecked() ? PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS
						: PregnancyCalculator.EMBRYONIC_AVG_AGE_IN_WEEKS) - 1);
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		int y = getScrollY();
		outState.putInt(EXTRA_GUI_SCROLL_Y, y);

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);

		int y = state.getInt(EXTRA_GUI_SCROLL_Y, 0);
		setScrollY(y);
	}

	@Override
	public void onSaveState(Intent outState) {
		outState.putExtra(EXTRA_GUI_CURRENT_DATE,
				getDate(datePickerCurrentDate).getTimeInMillis());
		outState.putExtra(EXTRA_GUI_SCROLL_Y, getScrollY());
		outState.putExtra(EXTRA_GUI_CURRENT_TAB, mTabHost.getCurrentTab());
	}

	private void setScrollY(final int y) {
		mTabHost.post(new Runnable() {
			@Override
			public void run() {
				ScrollView currentTab = getCurrentTab();
				currentTab.scrollTo(0, y);
			}
		});
	}

	private int getScrollY() {
		ScrollView currentTab = getCurrentTab();
		return currentTab.getScrollY();
	}

	private ScrollView getCurrentTab() {
		ScrollView currentTab = null;
		switch (mTabHost.getCurrentTab()) {
		case 0:
			currentTab = tab1;
			break;
		case 1:
			currentTab = tab2;
			break;
		case 2:
			currentTab = tab3;
			break;
		}

		return currentTab;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		switch (Utils.getThemeId()) {
		case Utils.LIGHT_THEME:
			menu.findItem(R.id.lighttheme).setChecked(true);
			break;
		case Utils.DARK_THEME:
		default:
			menu.findItem(R.id.darktheme).setChecked(true);
			break;
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.lighttheme:
			Utils.changeToTheme(this, Utils.LIGHT_THEME);
			return true;
		case R.id.darktheme:
			Utils.changeToTheme(this, Utils.DARK_THEME);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
