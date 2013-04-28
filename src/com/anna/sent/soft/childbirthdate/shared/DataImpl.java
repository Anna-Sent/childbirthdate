package com.anna.sent.soft.childbirthdate.shared;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;

public class DataImpl implements Data {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;
	private static final boolean DEBUG_EDITOR = false;

	private static String wrapMsg(String msg) {
		return "Data: " + msg;
	}

	private static void log(String msg, boolean scenario) {
		if (scenario) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	public DataImpl(Context context) {
		mContext = context;
	}

	private Context mContext;

	private boolean isEmbryonicAge;
	private boolean[] byMethod = new boolean[3];
	private Calendar lastMenstruationDate, ovulationDate, ultrasoundDate;
	private int menstrualCycleLen, lutealPhaseLen, weeks, days;

	public boolean byLmp() {
		return byMethod[0];
	}

	public boolean byOvulation() {
		return byMethod[1];
	}

	public boolean byUltrasound() {
		return byMethod[2];
	}

	public boolean[] byMethod() {
		return byMethod;
	}

	public boolean isEmbryonicAge() {
		return isEmbryonicAge;
	}

	public Calendar getLastMenstruationDate() {
		return lastMenstruationDate;
	}

	public Calendar getOvulationDate() {
		return ovulationDate;
	}

	public Calendar getUltrasoundDate() {
		return ultrasoundDate;
	}

	public int getMenstrualCycleLen() {
		return menstrualCycleLen;
	}

	public int getLutealPhaseLen() {
		return lutealPhaseLen;
	}

	public int getWeeks() {
		return weeks;
	}

	public int getDays() {
		return days;
	}

	@Override
	public void setByMethod(int index, boolean value) {
		log("setByMethod " + index, DEBUG_EDITOR);
		if (index >= 0 && index < byMethod.length) {
			byMethod[index] = value;
		}
	}

	@Override
	public void setIsEmbryonicAge(boolean value) {
		log("setIsEmbryonicAge", DEBUG_EDITOR);
		isEmbryonicAge = value;
	}

	@Override
	public void setLastMenstruationDate(Calendar value) {
		log("setLastMenstruationDate", DEBUG_EDITOR);
		lastMenstruationDate = value;
	}

	@Override
	public void setOvulationDate(Calendar value) {
		log("setOvulationDate", DEBUG_EDITOR);
		ovulationDate = value;
	}

	@Override
	public void setUltrasoundDate(Calendar value) {
		log("setUltrasoundDate", DEBUG_EDITOR);
		ultrasoundDate = value;
	}

	@Override
	public void setMenstrualCycleLen(int value) {
		log("setMenstrualCycleLen", DEBUG_EDITOR);
		menstrualCycleLen = value;
	}

	@Override
	public void setLutealPhaseLen(int value) {
		log("setLutealPhaseLen", DEBUG_EDITOR);
		lutealPhaseLen = value;
	}

	@Override
	public void setWeeks(int value) {
		log("setWeeks", DEBUG_EDITOR);
		weeks = value;
	}

	@Override
	public void setDays(int value) {
		log("setDays", DEBUG_EDITOR);
		days = value;
	}

	public void save() {
		log("save", DEBUG);
		Editor editor = Shared.getSettings(mContext).edit();

		editor.putBoolean(
				Shared.Saved.Calculation.EXTRA_BY_LAST_MENSTRUATION_DATE,
				byMethod[0]);
		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_OVULATION_DATE,
				byMethod[1]);
		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_ULTRASOUND,
				byMethod[2]);
		editor.putBoolean(Shared.Saved.Calculation.EXTRA_IS_EMBRYONIC_AGE,
				isEmbryonicAge);
		editor.putLong(Shared.Saved.Calculation.EXTRA_LAST_MENSTRUATION_DATE,
				lastMenstruationDate.getTimeInMillis());
		editor.putLong(Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
				ovulationDate.getTimeInMillis());
		editor.putLong(Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
				ultrasoundDate.getTimeInMillis());
		editor.putInt(Shared.Saved.Calculation.EXTRA_MENSTRUAL_CYCLE_LEN,
				menstrualCycleLen);
		editor.putInt(Shared.Saved.Calculation.EXTRA_LUTEAL_PHASE_LEN,
				lutealPhaseLen);
		editor.putInt(Shared.Saved.Calculation.EXTRA_WEEKS, weeks);
		editor.putInt(Shared.Saved.Calculation.EXTRA_DAYS, days);

		editor.commit();
	}

	public void update() {
		log("update", DEBUG);
		SharedPreferences settings = Shared.getSettings(mContext);

		lastMenstruationDate = Calendar.getInstance();
		lastMenstruationDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_LAST_MENSTRUATION_DATE,
				System.currentTimeMillis()));
		menstrualCycleLen = settings.getInt(
				Shared.Saved.Calculation.EXTRA_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		lutealPhaseLen = settings.getInt(
				Shared.Saved.Calculation.EXTRA_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);

		ovulationDate = Calendar.getInstance();
		ovulationDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
				System.currentTimeMillis()));

		weeks = settings.getInt(Shared.Saved.Calculation.EXTRA_WEEKS, 0);
		days = settings.getInt(Shared.Saved.Calculation.EXTRA_DAYS, 0);
		isEmbryonicAge = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_IS_EMBRYONIC_AGE, false);
		ultrasoundDate = Calendar.getInstance();
		ultrasoundDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
				System.currentTimeMillis()));

		byMethod[0] = settings
				.getBoolean(
						Shared.Saved.Calculation.EXTRA_BY_LAST_MENSTRUATION_DATE,
						false);
		byMethod[1] = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_OVULATION_DATE, false);
		byMethod[2] = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_ULTRASOUND, false);
	}
}
