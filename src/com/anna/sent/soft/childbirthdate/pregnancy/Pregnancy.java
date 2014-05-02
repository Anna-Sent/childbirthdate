package com.anna.sent.soft.childbirthdate.pregnancy;

import java.util.Calendar;

import android.content.Context;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.Age;

public abstract class Pregnancy {
	public static final int FIRST_TRIMESTER = 1;
	public static final int SECOND_TRIMESTER = 2;
	public static final int THIRD_TRIMESTER = 3;

	private Age age = new Age();
	private Calendar startPoint = null, currentPoint = null;

	/**
	 * 
	 * @param start
	 *            start point of pregnancy, must be not null
	 */
	public Pregnancy(Calendar start) {
		startPoint = (Calendar) start.clone();
		zeroDate(startPoint);
		currentPoint = startPoint;
	}

	/**
	 * 
	 * @param weeks
	 * @param days
	 * @param current
	 *            current date, must be not null
	 */
	public Pregnancy(int weeks, int days, Calendar current) {
		age.setDays(days);
		age.setWeeks(weeks);
		startPoint = (Calendar) current.clone();
		zeroDate(startPoint);
		startPoint.add(Calendar.DAY_OF_MONTH, -getDurationInDays());
		currentPoint = startPoint;
	}

	public void setCurrentPoint(Calendar current) {
		currentPoint = (Calendar) current.clone();
		zeroDate(currentPoint);
		long difference = currentPoint.getTimeInMillis()
				- startPoint.getTimeInMillis();
		int days = (int) (difference / (1000l * 3600l * 24l));
		int weeks = days / 7;
		days = days - weeks * 7;
		age.setDays(days);
		age.setWeeks(weeks);
	}

	public Calendar getCurrentPoint() {
		return currentPoint;
	}

	public void setWeeks(int value) {
		age.setWeeks(value);
		age.setDays(0);
		currentPoint = (Calendar) startPoint.clone();
		currentPoint.add(Calendar.DAY_OF_MONTH, getDurationInDays());
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
		if (age.getWeeks() >= 0) {
			if (age.getWeeks() <= getFirstTrimesterEndInclusive()) {
				return FIRST_TRIMESTER;
			} else if (age.getWeeks() <= getSecondTrimesterEndInclusive()) {
				return SECOND_TRIMESTER;
			} else if (getDurationInDays() <= getMaxDurationInDays()) {
				return THIRD_TRIMESTER;
			}
		}

		return -1;
	}

	public int getDurationInDays() {
		return age.getDurationInDays();
	}

	public int getWeeks() {
		return age.getWeeks();
	}

	public int getDays() {
		return age.getDays();
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

	public String getRestInfo(Context context) {
		return new Age(getRestWeeks(), getRestDays()).toString(context);
	}

	public String getInfo(Context context) {
		return age.toString(context);
	}

	public String getAdditionalInfo(Context context) {
		String result;
		int rest = getRestInDays();
		if (rest > 0) {
			result = context.getString(R.string.positiveRest,
					getRestInfo(context));
		} else if (rest == 0) {
			result = context.getString(R.string.zeroRest);
		} else {
			result = context.getString(R.string.negativeRest);
		}

		result = context.getString(R.string.additionalInfo,
				getTrimesterNumber(), new Age(getFullDurationWeeks(),
						getFullDurationDays()).toString(context), result);

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