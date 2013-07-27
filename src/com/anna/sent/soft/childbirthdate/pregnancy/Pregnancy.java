package com.anna.sent.soft.childbirthdate.pregnancy;

import java.util.Calendar;

import android.content.Context;

import com.anna.sent.soft.childbirthdate.R;

public abstract class Pregnancy {
	public static final int FIRST_TRIMESTER = 1;
	public static final int SECOND_TRIMESTER = 2;
	public static final int THIRD_TRIMESTER = 3;

	private int weeks = 0;
	private int days = 0;
	private Calendar startPoint = null, currentPoint = null;

	/**
	 * 
	 * @param start
	 *            start point of pregnancy, must be not null
	 */
	public Pregnancy(Calendar start) {
		zeroDate(start);
		startPoint = (Calendar) start.clone();
		currentPoint = (Calendar) start.clone();
	}

	/**
	 * 
	 * @param weeks
	 * @param days
	 * @param current
	 *            current date, must be not null
	 */
	public Pregnancy(int weeks, int days, Calendar current) {
		zeroDate(current);
		this.weeks = weeks;
		this.days = days;
		startPoint = (Calendar) current.clone();
		startPoint.add(Calendar.DAY_OF_MONTH, -getDurationInDays());
		currentPoint = (Calendar) current.clone();
	}

	public void setCurrentPoint(Calendar current) {
		zeroDate(current);
		currentPoint = (Calendar) current.clone();
		long difference = currentPoint.getTimeInMillis()
				- startPoint.getTimeInMillis();
		int days = (int) (difference / (1000l * 3600l * 24l));
		weeks = days / 7;
		this.days = days - weeks * 7;
	}

	public Calendar getCurrentPoint() {
		return currentPoint;
	}

	public Calendar getStartPoint() {
		return startPoint;
	}

	public Calendar getEndPoint() {
		Calendar endPoint = (Calendar) startPoint.clone();
		endPoint.add(Calendar.DAY_OF_MONTH, getFullDurationInDays());
		return endPoint;
	}

	/**
	 * Checker for the correctness of values of weeks and days.
	 * 
	 * @return {@code true}, if {@code weeks} and {@code days} values are
	 *         correct; {@code false}, otherwise
	 */
	public boolean isCorrect() {
		int duration = getDurationInDays();
		return duration >= 0 && duration <= getMaxDurationInDays();
	}

	/**
	 * 
	 * @return number of trimester, if weeks value is correct; -1 otherwise
	 */
	public int getTrimesterNumber() {
		if (weeks >= 0) {
			if (weeks <= getFirstTrimesterEndInclusive()) {
				return FIRST_TRIMESTER;
			} else if (weeks <= getSecondTrimesterEndInclusive()) {
				return SECOND_TRIMESTER;
			} else if (getDurationInDays() <= getMaxDurationInDays()) {
				return THIRD_TRIMESTER;
			}
		}

		return -1;
	}

	public int getDurationInDays() {
		return weeks * 7 + days;
	}

	public int getWeeks() {
		return weeks;
	}

	public int getDays() {
		return days;
	}

	public abstract int getFullDurationInDays();

	public int getFullDurationWeeks() {
		return getFullDurationInDays() / 7;
	}

	public int getFullDurationDays() {
		return getFullDurationInDays() - getFullDurationWeeks() * 7;
	}

	public int getRestInDays() {
		return getFullDurationInDays() - getDurationInDays();
	}

	public int getRestWeeks() {
		return getRestInDays() / 7;
	}

	public int getRestDays() {
		return getRestInDays() - getRestWeeks() * 7;
	}

	protected abstract int getMaxDurationInDays();

	protected abstract int getFirstTrimesterEndInclusive();

	protected abstract int getSecondTrimesterEndInclusive();

	public static String getStringRepresentation(Context context, int weeks,
			int days) {
		if (weeks < 0 || days < 0) {
			return "?";
		}

		String result = "";
		if (weeks > 0) {
			result += weeks + " " + context.getString(R.string.weeks)
					+ (days > 0 ? " " : "");
		}

		if (days > 0) {
			result += days + " " + context.getString(R.string.days);
		}

		if (weeks == 0 && days == 0) {
			result = "0 " + context.getString(R.string.days);
		}

		return result;
	}

	public String getRestInfo(Context context) {
		return getStringRepresentation(context, getRestWeeks(), getRestDays());
	}

	public String getInfo(Context context) {
		return getStringRepresentation(context, weeks, days);
	}

	public String getAdditionalInfo(Context context) {
		String result;
		int rest = getRestInDays();
		if (rest > 0) {
			result = context.getString(
					R.string.positiveRest,
					getStringRepresentation(context, getRestWeeks(),
							getRestDays()));
		} else if (rest == 0) {
			result = context.getString(R.string.zeroRest);
		} else {
			result = context.getString(R.string.negativeRest);
		}

		result = context.getString(
				R.string.additionalInfo,
				getTrimesterNumber(),
				getStringRepresentation(context, getFullDurationWeeks(),
						getFullDurationDays()), result);

		return result;
	}

	public static String getTrimesterString(Context context, int trimester) {
		switch (trimester) {
		case Pregnancy.FIRST_TRIMESTER:
			return context.getString(R.string.firstTrimester);
		case Pregnancy.SECOND_TRIMESTER:
			return context.getString(R.string.secondTrimester);
		case Pregnancy.THIRD_TRIMESTER:
			return context.getString(R.string.thirdTrimester);
		}

		return "?";
	}

	private static void zeroDate(Calendar date) {
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		date.set(Calendar.AM_PM, Calendar.AM);
	}
}
