package com.anna.sent.soft.childbirthdate.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.Age;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;

public class DataImpl implements Data {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;

	private static String wrapMsg(String msg) {
		return DataImpl.class.getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private static void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	public DataImpl(Context context) {
		mContext = context;
	}

	private Context mContext;

	private boolean isEmbryonicAge, isFirstPregnancy;
	private boolean[] byMethod = new boolean[Shared.Calculation.METHODS_COUNT];
	private Calendar lastMenstruationDate = Calendar.getInstance();
	private Calendar ovulationDate = Calendar.getInstance();
	private Calendar ultrasoundDate = Calendar.getInstance();
	private Calendar firstAppearanceDate = Calendar.getInstance();
	private Calendar firstMovementsDate = Calendar.getInstance();
	private int menstrualCycleLen, lutealPhaseLen, ultrasoundWeeks,
			ultrasoundDays, firstAppearanceWeeks;

	@Override
	public boolean[] byMethod() {
		return byMethod;
	}

	@Override
	public boolean isEmbryonicAge() {
		return isEmbryonicAge;
	}

	@Override
	public Calendar getLastMenstruationDate() {
		return lastMenstruationDate;
	}

	@Override
	public Calendar getOvulationDate() {
		return ovulationDate;
	}

	@Override
	public Calendar getUltrasoundDate() {
		return ultrasoundDate;
	}

	@Override
	public int getMenstrualCycleLen() {
		return menstrualCycleLen;
	}

	@Override
	public int getLutealPhaseLen() {
		return lutealPhaseLen;
	}

	@Override
	public int getUltrasoundWeeks() {
		return ultrasoundWeeks;
	}

	@Override
	public int getUltrasoundDays() {
		return ultrasoundDays;
	}

	@Override
	public void byMethod(int index, boolean value) {
		if (index >= 0 && index < byMethod.length) {
			byMethod[index] = value;
		}
	}

	@Override
	public void isEmbryonicAge(boolean value) {
		isEmbryonicAge = value;
	}

	@Override
	public void setLastMenstruationDate(Calendar value) {
		lastMenstruationDate = value;
	}

	@Override
	public void setOvulationDate(Calendar value) {
		ovulationDate = value;
	}

	@Override
	public void setUltrasoundDate(Calendar value) {
		ultrasoundDate = value;
	}

	@Override
	public void setMenstrualCycleLen(int value) {
		menstrualCycleLen = value;
	}

	@Override
	public void setLutealPhaseLen(int value) {
		lutealPhaseLen = value;
	}

	@Override
	public void setUltrasoundWeeks(int value) {
		ultrasoundWeeks = value;
	}

	@Override
	public void setUltrasoundDays(int value) {
		ultrasoundDays = value;
	}

	public void save() {
		// log("save");
		Editor editor = Settings.getSettings(mContext).edit();

		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_LMP,
				byMethod[Shared.Calculation.BY_LMP - 1]);
		saveDate(editor, Shared.Saved.Calculation.EXTRA_LMP_DATE,
				lastMenstruationDate);
		editor.putInt(Shared.Saved.Calculation.EXTRA_LMP_MENSTRUAL_CYCLE_LEN,
				menstrualCycleLen);
		editor.putInt(Shared.Saved.Calculation.EXTRA_LMP_LUTEAL_PHASE_LEN,
				lutealPhaseLen);

		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_OVULATION,
				byMethod[Shared.Calculation.BY_OVULATION - 1]);
		saveDate(editor, Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
				ovulationDate);

		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_ULTRASOUND,
				byMethod[Shared.Calculation.BY_ULTRASOUND - 1]);
		editor.putBoolean(
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_IS_EMBRYONIC_AGE,
				isEmbryonicAge);
		saveDate(editor, Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
				ultrasoundDate);
		editor.putInt(Shared.Saved.Calculation.EXTRA_ULTRASOUND_WEEKS,
				ultrasoundWeeks);
		editor.putInt(Shared.Saved.Calculation.EXTRA_ULTRASOUND_DAYS,
				ultrasoundDays);

		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_FIRST_APPEARANCE,
				byMethod[Shared.Calculation.BY_FIRST_APPEARANCE - 1]);
		saveDate(editor, Shared.Saved.Calculation.EXTRA_FIRST_APPEARANCE_DATE,
				firstAppearanceDate);
		editor.putInt(Shared.Saved.Calculation.EXTRA_FIRST_APPEARANCE_WEEKS,
				firstAppearanceWeeks);

		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_FIRST_MOVEMENTS,
				byMethod[Shared.Calculation.BY_FIRST_MOVEMENTS - 1]);
		saveDate(editor, Shared.Saved.Calculation.EXTRA_FIRST_MOVEMENTS_DATE,
				firstMovementsDate);
		editor.putBoolean(
				Shared.Saved.Calculation.EXTRA_FIRST_MOVEMENTS_IS_FIRST_PREGNANCY,
				isFirstPregnancy);

		editor.commit();
	}

	public void update() {
		// log("update");
		SharedPreferences settings = Settings.getSettings(mContext);

		updateDate(settings, lastMenstruationDate,
				Shared.Saved.Calculation.EXTRA_LMP_DATE);
		menstrualCycleLen = settings.getInt(
				Shared.Saved.Calculation.EXTRA_LMP_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		lutealPhaseLen = settings.getInt(
				Shared.Saved.Calculation.EXTRA_LMP_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);

		updateDate(settings, ovulationDate,
				Shared.Saved.Calculation.EXTRA_OVULATION_DATE);

		ultrasoundWeeks = settings.getInt(
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_WEEKS, 0);
		ultrasoundDays = settings.getInt(
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_DAYS, 0);
		isEmbryonicAge = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_IS_EMBRYONIC_AGE,
				false);
		updateDate(settings, ultrasoundDate,
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE);

		updateDate(settings, firstAppearanceDate,
				Shared.Saved.Calculation.EXTRA_FIRST_APPEARANCE_DATE);
		firstAppearanceWeeks = settings.getInt(
				Shared.Saved.Calculation.EXTRA_FIRST_APPEARANCE_WEEKS, 0);

		updateDate(settings, firstMovementsDate,
				Shared.Saved.Calculation.EXTRA_FIRST_MOVEMENTS_DATE);
		isFirstPregnancy = settings
				.getBoolean(
						Shared.Saved.Calculation.EXTRA_FIRST_MOVEMENTS_IS_FIRST_PREGNANCY,
						false);

		byMethod[Shared.Calculation.BY_LMP - 1] = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_LMP, false);
		byMethod[Shared.Calculation.BY_OVULATION - 1] = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_OVULATION, false);
		byMethod[Shared.Calculation.BY_ULTRASOUND - 1] = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_ULTRASOUND, false);
		byMethod[Shared.Calculation.BY_FIRST_APPEARANCE - 1] = settings
				.getBoolean(Shared.Saved.Calculation.EXTRA_BY_FIRST_APPEARANCE,
						false);
		byMethod[Shared.Calculation.BY_FIRST_MOVEMENTS - 1] = settings
				.getBoolean(Shared.Saved.Calculation.EXTRA_BY_FIRST_MOVEMENTS,
						false);
	}

	private final static String DATE_FORMAT = "yyyy-MM-dd";

	private void saveDate(Editor editor, String extra, Calendar date) {
		// editor.putLong(extra, date.getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT,
				Locale.getDefault());
		String value = formatter.format(date.getTime());
		// log("save " + value);
		editor.putString(extra, value);
	}

	private void updateDate(SharedPreferences settings, Calendar date,
			String extra) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT,
					Locale.getDefault());
			String value = settings.getString(extra, null);
			Date date1 = formatter.parse(value);
			date.setTime(date1);
			// log("restore 1 " + DateUtils.toString(mContext, date));
		} catch (Exception e) {
			date.setTimeInMillis(settings.getLong(extra,
					System.currentTimeMillis()));
			// log("restore 2 " + DateUtils.toString(mContext, date));
		}
	}

	@Override
	public Calendar getFirstAppearanceDate() {
		return firstAppearanceDate;
	}

	@Override
	public void setFirstAppearanceDate(Calendar value) {
		firstAppearanceDate = value;
	}

	@Override
	public int getFirstAppearanceWeeks() {
		return firstAppearanceWeeks;
	}

	@Override
	public void setFirstAppearanceWeeks(int value) {
		firstAppearanceWeeks = value;
	}

	@Override
	public Calendar getFirstMovementsDate() {
		return firstMovementsDate;
	}

	@Override
	public void setFirstMovementsDate(Calendar value) {
		firstMovementsDate = value;
	}

	@Override
	public boolean isFirstPregnancy() {
		return isFirstPregnancy;
	}

	@Override
	public void isFirstPregnancy(boolean value) {
		isFirstPregnancy = value;
	}

	@Override
	public String[] getStrings2() {
		String[] result = new String[Shared.Calculation.METHODS_COUNT];

		result[Shared.Calculation.BY_LMP - 1] = mContext.getString(
				R.string.titles0,
				DateUtils.toString(mContext, lastMenstruationDate),
				menstrualCycleLen, lutealPhaseLen);

		result[Shared.Calculation.BY_OVULATION - 1] = mContext.getString(
				R.string.titles1, DateUtils.toString(mContext, ovulationDate));

		result[Shared.Calculation.BY_ULTRASOUND - 1] = mContext.getString(
				isEmbryonicAge ? R.string.titles2_emryonic
						: R.string.titles2_gestational, DateUtils.toString(
						mContext, ultrasoundDate), new Age(ultrasoundWeeks,
						ultrasoundDays).toString(mContext));

		result[Shared.Calculation.BY_FIRST_APPEARANCE - 1] = mContext
				.getString(R.string.titles3,
						DateUtils.toString(mContext, firstAppearanceDate),
						new Age(firstAppearanceWeeks, 0).toString(mContext));

		result[Shared.Calculation.BY_FIRST_MOVEMENTS - 1] = mContext.getString(
				isFirstPregnancy ? R.string.titles4_first_pregnancy
						: R.string.titles4_second_pregnancy, DateUtils
						.toString(mContext, firstMovementsDate));

		return result;
	}

	@Override
	public boolean thereIsAtLeastOneSelectedMethod() {
		for (int i = 0; i < byMethod.length; ++i) {
			if (byMethod[i]) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int getSelectedMethodsCount() {
		int count = 0;
		for (int i = 0; i < byMethod.length; ++i) {
			if (byMethod[i]) {
				++count;
			}
		}

		return count;
	}
}