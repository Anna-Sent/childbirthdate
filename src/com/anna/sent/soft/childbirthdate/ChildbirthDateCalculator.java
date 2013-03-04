package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;
import android.content.Context;

public class ChildbirthDateCalculator {
	public final static int MENSTRUAL_CYCLE_AVG_LENGTH = 28;
	public final static int LUTEAL_PHASE_AVG_LENGTH = 14;

	public final static int MIN_MENSTRUAL_CYCLE_LEN = 20;
	public final static int MAX_MENSTRUAL_CYCLE_LEN = 36;

	public final static int MIN_LUTEAL_PHASE_LEN = 10;
	public final static int MAX_LUTEAL_PHASE_LEN = 16;

	public final static int OBSTETIC_PREGNANCY_PERIOD = 40;
	public final static int FETAL_PREGNANCY_PERIOD = 38;
	public final static int DAYS_IN_A_WEEK = 7;

	private String message = "";
	private Context mContext;

	/**
	 * 
	 * @param context
	 */
	public ChildbirthDateCalculator(Context context) {
		mContext = context;
	}

	/**
	 * 
	 * @param menstrualCycleLen
	 * @param lutealPhaseLen
	 * @param date
	 * @return
	 */
	public Calendar getByLastMenstruationDate(int menstrualCycleLen,
			int lutealPhaseLen, Calendar date) {
		date.add(Calendar.DAY_OF_MONTH, menstrualCycleLen - lutealPhaseLen
				+ FETAL_PREGNANCY_PERIOD * DAYS_IN_A_WEEK);
		return date;
	}

	public Calendar getByOvulationDate(Calendar date) {
		date.add(Calendar.DAY_OF_MONTH, FETAL_PREGNANCY_PERIOD * DAYS_IN_A_WEEK);
		return date;
	}

	/**
	 * 
	 * @param date
	 * @param weeks
	 *            must be >= 0 and (< OBSTETIC_PREGNANCY_PERIOD if isFetal is
	 *            false or < FETAL_PREGNANCY_PERIOD if isFetal is true)
	 * @param days
	 *            must be >= 0 and < DAYS_IN_A_WEEK
	 * @param isFetal
	 * @return
	 */
	public Calendar getByUltrasound(Calendar date, int weeks, int days,
			boolean isFetal) {
		if (weeks <= 14 && !isFetal || weeks <= 12 && isFetal) {
			message = mContext.getString(R.string.message1);
		}

		message = mContext.getString(R.string.message2);

		int pregnancyPeriod = isFetal ? FETAL_PREGNANCY_PERIOD * DAYS_IN_A_WEEK
				: OBSTETIC_PREGNANCY_PERIOD * DAYS_IN_A_WEEK;
		date.add(Calendar.DAY_OF_MONTH, pregnancyPeriod - weeks * DAYS_IN_A_WEEK - days);
		return date;
	}

	public String getMessage() {
		return message;
	}
}
