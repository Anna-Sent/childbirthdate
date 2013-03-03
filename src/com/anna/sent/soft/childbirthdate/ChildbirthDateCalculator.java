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

	private String message = "";
	private Context mContext;

	public ChildbirthDateCalculator(Context context) {
		mContext = context;
	}

	public Calendar getByLastMenstruationDate(int menstrualCycleLen,
			int lutealPhaseLen, Calendar date) {
		date.add(Calendar.DAY_OF_MONTH, menstrualCycleLen - lutealPhaseLen + 38
				* 7);
		return date;
	}

	public Calendar _getByOvulationDate(Calendar date) {
		date.add(Calendar.DAY_OF_MONTH, 38 * 7);
		return date;
	}

	private Calendar _getByUltrasound(Calendar date, int weeks, int days,
			boolean isFetal) {
		int pregnancyPeriod = isFetal ? 38 * 7 : 40 * 7;
		date.add(Calendar.DAY_OF_MONTH, pregnancyPeriod - weeks * 7 - days);
		return date;
	}

	public Calendar getByUltrasound(Calendar date, int weeks, int days,
			boolean isFetal) {
		if (weeks >= 0 && weeks <= 14 && days >= 0 && days <= 6) {
			message = mContext.getString(R.string.message1);
			return _getByUltrasound(date, weeks, days, isFetal);
		}

		if (weeks < 38) {
			message = mContext.getString(R.string.message2);
			return _getByUltrasound(date, weeks, days, isFetal);
		}

		if (weeks <= 42) {
			message = mContext.getString(R.string.message3);
			return null;
		}

		message = mContext.getString(R.string.message4);
		return null;
	}

	public String getMessage() {
		return message;
	}
}
