package com.anna.sent.soft.childbirthdate.shared;

import java.util.Calendar;

import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Data {
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

	public boolean getIsEmbryonicAge() {
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

	public static void save(Context context, Calendar lastMenstruationDate,
			int menstrualCycleLen, int lutealPhaseLen) {
		SharedPreferences settings = Shared.getSettings(context);
		Editor editor = settings.edit();
		editor.putLong(Shared.Saved.Calculation.EXTRA_LAST_MENSTRUATION_DATE,
				lastMenstruationDate.getTimeInMillis());
		editor.putInt(Shared.Saved.Calculation.EXTRA_MENSTRUAL_CYCLE_LEN,
				menstrualCycleLen);
		editor.putInt(Shared.Saved.Calculation.EXTRA_LUTEAL_PHASE_LEN,
				lutealPhaseLen);
		editor.commit();
	}

	public void restoreLmp(Context context) {
		SharedPreferences settings = Shared.getSettings(context);
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
	}

	public static void save(Context context, Calendar ovulationDate) {
		SharedPreferences settings = Shared.getSettings(context);
		Editor editor = settings.edit();
		editor.putLong(Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
				ovulationDate.getTimeInMillis());
		editor.commit();
	}

	public void restoreOvulation(Context context) {
		SharedPreferences settings = Shared.getSettings(context);
		ovulationDate = Calendar.getInstance();
		ovulationDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
				System.currentTimeMillis()));
	}

	public static void save(Context context, Calendar ultrasoundDate,
			int weeks, int days, boolean isEmbryonicAge) {
		SharedPreferences settings = Shared.getSettings(context);
		Editor editor = settings.edit();
		editor.putLong(Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
				ultrasoundDate.getTimeInMillis());
		editor.putInt(Shared.Saved.Calculation.EXTRA_WEEKS, weeks);
		editor.putInt(Shared.Saved.Calculation.EXTRA_DAYS, days);
		editor.putBoolean(Shared.Saved.Calculation.EXTRA_IS_EMBRYONIC_AGE,
				isEmbryonicAge);
		editor.commit();
	}

	public void restoreUltrasound(Context context) {
		SharedPreferences settings = Shared.getSettings(context);
		weeks = settings.getInt(Shared.Saved.Calculation.EXTRA_WEEKS, 0);
		days = settings.getInt(Shared.Saved.Calculation.EXTRA_DAYS, 0);
		isEmbryonicAge = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_IS_EMBRYONIC_AGE, false);
		ultrasoundDate = Calendar.getInstance();
		ultrasoundDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
				System.currentTimeMillis()));
	}

	public static void save(Context context, boolean[] checked) {
		SharedPreferences settings = Shared.getSettings(context);
		Editor editor = settings.edit();
		editor.putBoolean(
				Shared.Saved.Calculation.EXTRA_BY_LAST_MENSTRUATION_DATE,
				checked[0]);
		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_OVULATION_DATE,
				checked[1]);
		editor.putBoolean(Shared.Saved.Calculation.EXTRA_BY_ULTRASOUND,
				checked[2]);
		editor.commit();
	}

	public void restoreChecked(Context context) {
		SharedPreferences settings = Shared.getSettings(context);
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
