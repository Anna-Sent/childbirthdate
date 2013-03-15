package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;
import android.content.Context;

/**
 * Gestational age, childbirth date calculator, predictor (calculation,
 * prediction). Pregnancy due date calculator, pregnancy due date predictor.<br>
 * 
 * It's features: estimated childbirth date calculation and gestational age
 * calculation - by using different methods: by the date of last menstrual
 * period, by the date of ovulation (conception), by the ultrasound results.
 * 
 * @author Anna
 * 
 */
public class PregnancyCalculator {
	/**
	 * The average menstrual cycle length is 28 days.
	 */
	public final static int AVG_MENSTRUAL_CYCLE_LENGTH = 28;

	/**
	 * The average luteal phase length is 14 days.
	 */
	public final static int AVG_LUTEAL_PHASE_LENGTH = 14;

	/**
	 * It's common for cycles to range from 21 to 45 days.
	 */
	public final static int MIN_MENSTRUAL_CYCLE_LEN = 20;
	public final static int MAX_MENSTRUAL_CYCLE_LEN = 50;

	/**
	 * The length of the luteal phase ranges from 11 to 16 days in 95% of
	 * normally ovulating women.
	 */
	public final static int MIN_LUTEAL_PHASE_LEN = 10;
	public final static int MAX_LUTEAL_PHASE_LEN = 20;

	/**
	 * Average duration of pregnancy is actually about 40 weeks. On average, it
	 * corresponds to an embryonic age of 38 weeks.
	 */
	public final static int FULL_GESTATIONAL_AGE = 40;
	public final static int FULL_EMBRYONIC_AGE = 38;

	/**
	 * Assuming that 44 weeks is maximal possible value, because of incorrect
	 * estimation of gestational age (for example, for woman who have an
	 * irregular period).
	 */
	public final static int MAX_GESTATIONAL_AGE = 44;

	/**
	 * The calculation by using ultrasound method is the very accurate at
	 * gestational age of the 11-14 and embryonic age of the 9-12 weeks.
	 */
	public final static int MAX_GESTATIONAL_ULTRASOUND_ACCURACY = 14;
	public final static int MAX_EMBRYONIC_ULTRASOUND_ACCURACY = 12;

	/**
	 * Trimesters' ranges.
	 */
	public final static int FIRST_TRIMESTER_END_INCLUSIVE = 12;
	public final static int SECOND_TRIMESTER_END_INCLUSIVE = 28;

	/**
	 * There is 7 days in a week.
	 */
	public final static int DAYS_IN_A_WEEK = 7;

	/**
	 * Healthcare professionals name three different dates as the start of
	 * pregnancy:<br>
	 * 1. the first day of the woman's last normal menstrual period, and the
	 * resulting fetal age is called the gestational age;<br>
	 * 2. the date of conception (about two weeks before her next expected
	 * menstrual period), with the age called fertilization age;<br>
	 * 3. the date of implantation (about one week after conception).<br>
	 * Since these are spread over a significant period of time, the duration of
	 * pregnancy necessarily depends on the date selected as the starting point
	 * chosen.<br>
	 * Some countries count gestational age from fertilization instead of LMP.
	 * This method of counting is also known as fertilization age, embryonic
	 * age, fertilizational age or (intrauterine) developmental (IUD) age. This
	 * method is more prevalent in descriptions of prenatal development of the
	 * embryo or fetus. The LMP gestational age is usually greater by about two
	 * weeks. Pregnancy often is defined as beginning with implantation, which
	 * happens about three weeks after the LMP (see the beginning of pregnancy
	 * controversy).
	 * 
	 * @author Anna
	 * 
	 */
	public enum CountingMethod {
		EmbryonicAge, GestationalAge
	};

	private String message = "";
	private Context mContext;

	/**
	 * Constructor of the class {@code PregnancyCalculator} gets {@code Context}
	 * of the application.
	 * 
	 * @param context
	 */
	public PregnancyCalculator(Context context) {
		mContext = context;
	}

	/**
	 * Calculates childbirth date by the first day of the woman's last menstrual
	 * period.<br>
	 * Can return array of two elements: obstetrical estimated childbirth date
	 * and corrected estimated childbirth date.
	 * 
	 * @param menstrualCycleLen
	 *            average length of cycles (from the first day of your one
	 *            period to the first day of your next period), must be in range
	 *            from {@code MIN_MENSTRUAL_CYCLE_LEN} to
	 *            {@code MAX_MENSTRUAL_CYCLE_LEN} inclusive
	 * @param lutealPhaseLen
	 *            must be in range from {@code MIN_LUTEAL_PHASE_LEN} to
	 *            {@code MAX_LUTEAL_PHASE_LEN} inclusive
	 * @param date
	 *            first day of last menstrual period (first day of bleeding)
	 * @return
	 */
	public Calendar getChildbirthDateByLastMenstruationDate(
			int menstrualCycleLen, int lutealPhaseLen, Calendar date) {
		/*
		 * Calendar[] result = { (Calendar) date.clone(), (Calendar)
		 * date.clone()};
		 * 
		 * // Obstetrical result[0].add(Calendar.DAY_OF_MONTH,
		 * FULL_GESTATIONAL_AGE * DAYS_IN_A_WEEK);
		 * 
		 * // Corrected result[1].add(Calendar.DAY_OF_MONTH, menstrualCycleLen -
		 * lutealPhaseLen + FULL_EMBRYONIC_AGE * DAYS_IN_A_WEEK);
		 * 
		 * return result;
		 */
		date.add(Calendar.DAY_OF_MONTH, menstrualCycleLen - lutealPhaseLen
				+ FULL_EMBRYONIC_AGE * DAYS_IN_A_WEEK);
		return date;
	}

	/**
	 * Calculates childbirth date by the ovulation (conception) date.
	 * 
	 * @param date
	 *            probable ovulation (conception) date
	 * @return estimated childbirth date
	 */
	public Calendar getChildbirthDateByOvulationDate(Calendar date) {
		message = "";
		date.add(Calendar.DAY_OF_MONTH, FULL_EMBRYONIC_AGE * DAYS_IN_A_WEEK);
		return date;
	}

	private void setAccuracyMessage(int weeks, int days,
			CountingMethod countingMethod) {
		int maxAccuracy;
		switch (countingMethod) {
		case EmbryonicAge:
			maxAccuracy = MAX_EMBRYONIC_ULTRASOUND_ACCURACY;
		case GestationalAge:
		default:
			maxAccuracy = MAX_GESTATIONAL_ULTRASOUND_ACCURACY;
		}

		if (weeks < maxAccuracy) {
			message = mContext.getString(R.string.accurateUltrasoundResults);
		} else {
			message = mContext.getString(R.string.inaccurateUltrasoundResults);
		}
	}

	private void setIncorrectDateMessage(long current, long begin) {
		if (current < begin) {
			message = mContext
					.getString(R.string.errorIncorrectCurrentDateSmaller);
		} else if (current > begin + (long) MAX_GESTATIONAL_AGE
				* (long) DAYS_IN_A_WEEK * 24l * 3600l * 1000l) {
			message = mContext
					.getString(R.string.errorIncorrectCurrentDateGreater);
		} else {
			message = "";
		}
	}

	private int getFullTerm(CountingMethod countingMethod) {
		switch (countingMethod) {
		case EmbryonicAge:
			return FULL_EMBRYONIC_AGE * DAYS_IN_A_WEEK;
		case GestationalAge:
		default:
			return FULL_GESTATIONAL_AGE * DAYS_IN_A_WEEK;
		}
	}

	/**
	 * Calculates childbirth date by the ultrasound.
	 * 
	 * @param date
	 *            date of the screening
	 * @param weeks
	 *            count of weeks of embryonic/gestational age, must be >= 0 and
	 *            < {@code FULL_EMBRYONIC_AGE}/{@code FULL_GESTATIONAL_AGE}
	 * @param days
	 *            count of days, must be >= 0 and < {@code DAYS_IN_A_WEEK}
	 * @param countingMethod
	 *            gestational age, embryonic age
	 * @return estimated childbirth date
	 */
	public Calendar getChildbirthDateByUltrasound(Calendar date, int weeks,
			int days, CountingMethod countingMethod) {
		setAccuracyMessage(weeks, days, countingMethod);
		int fullTerm = getFullTerm(countingMethod);
		date.add(Calendar.DAY_OF_MONTH, fullTerm - weeks * DAYS_IN_A_WEEK
				- days);
		return date;
	}

	/**
	 * Use after call of any public operation of the class. It will contain some
	 * message to user.
	 * 
	 * @return message about the result of method execution
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * The class represents gestational age in weeks and days.
	 * 
	 * @author Anna
	 * 
	 */
	public class GestationalAge {
		private int weeks = 0;
		private int days = 0;

		public GestationalAge() {
		}

		public GestationalAge(int weeks, int days) {
			this.weeks = weeks;
			this.days = days;
		}

		/**
		 * Checker for the correctness of values of weeks and days.
		 * 
		 * @return {@code true}, if {@code weeks} and {@code days} values are
		 *         correct, {@code false} otherwise
		 */
		public boolean isCorrect() {
			int duration = weeks * DAYS_IN_A_WEEK + days;
			return duration >= 0
					&& duration <= MAX_GESTATIONAL_AGE * DAYS_IN_A_WEEK;
		}

		public static final int FIRST_TRIMESTER = 1;
		public static final int SECOND_TRIMESTER = 2;
		public static final int THIRD_TRIMESTER = 3;

		/**
		 * Gestational age must be correct before call of this method. So, call
		 * {@code isCorrect()} before using of this method.
		 * 
		 * @return number of trimester
		 */
		public int getTrimesterNumber() {
			if (weeks <= FIRST_TRIMESTER_END_INCLUSIVE) {
				return FIRST_TRIMESTER;
			} else if (weeks <= SECOND_TRIMESTER_END_INCLUSIVE) {
				return SECOND_TRIMESTER;
			}

			return THIRD_TRIMESTER;
		}

		/**
		 * Gestational age must be correct before call of this method. So, call
		 * {@code isCorrect()} before using of this method.
		 * 
		 * @return string representation of trimester number
		 */
		public String getTrimesterString() {
			if (weeks <= FIRST_TRIMESTER_END_INCLUSIVE) {
				return mContext.getString(R.string.firstTrimester);
			} else if (weeks <= SECOND_TRIMESTER_END_INCLUSIVE) {
				return mContext.getString(R.string.secondTrimester);
			}

			return mContext.getString(R.string.thirdTrimester);
		}

		public int getWeeks() {
			return weeks;
		}

		public int getDays() {
			return days;
		}
	}

	private long getTimeInMillis(Calendar date) {
		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		return date.getTimeInMillis();
	}

	private GestationalAge getGestationalAge(long current, long begin) {
		long difference = current - begin;
		int days = (int) (difference / (1000l * 3600l * 24l));
		int weeks = days / DAYS_IN_A_WEEK;
		days = days - weeks * DAYS_IN_A_WEEK;
		return new GestationalAge(weeks, days);
	}

	/**
	 * Calculates gestational age by the first day of the woman's last menstrual
	 * period.
	 * 
	 * @param lastMenstrualPeriodDate
	 *            first day of last menstrual period (first day of bleeding)
	 * @param date
	 * @return estimated gestational age, check it's correctness
	 */
	public GestationalAge getGestationalAgeByLastMenstruationDate(
			Calendar lastMenstrualPeriodDate, Calendar date) {
		long current = getTimeInMillis(date);
		long begin = getTimeInMillis(lastMenstrualPeriodDate);
		setIncorrectDateMessage(current, begin);
		return getGestationalAge(current, begin);
		/*
		 * GestationalAge ga = getGestationalAge(current, begin); if
		 * (date.before(lastMenstrualPeriodDate)) { message =
		 * mContext.getString(R.string.errorIncorrectCurrentDateSmaller); } else
		 * { lastMenstrualPeriodDate.add(Calendar.DAY_OF_MONTH,
		 * MAX_GESTATIONAL_AGE * 7); if (date.after(lastMenstrualPeriodDate)) {
		 * message =
		 * mContext.getString(R.string.errorIncorrectCurrentDateSmaller); } else
		 * { message = ""; } }
		 * 
		 * return ga;
		 */
	}

	/**
	 * Calculates gestational age by the ovulation (conception) date.
	 * 
	 * @param ovulationDate
	 *            probable ovulation (conception) date
	 * @param date
	 * @return estimated gestational age, check it's correctness
	 */
	public GestationalAge getGestationalAgeByOvulationDate(
			Calendar ovulationDate, Calendar date) {
		/*
		 * obstetrical:
		 * 
		 * int follicularPhaseLen = menstrualCycleLen - 14;
		 * 
		 * corrected:
		 * 
		 * int follicularPhaseLen = menstrualCycleLen - lutealPhaseLen;
		 * 
		 * ovulationDate.add(Calendar.DAY_OF_MONTH, -follicularPhaseLen);
		 */
		long current = getTimeInMillis(date);
		long begin = getTimeInMillis(ovulationDate);
		setIncorrectDateMessage(current, begin);
		return getGestationalAge(current, begin);
	}

	private int getCurrentGestationalAge(int weeks, int days,
			CountingMethod countingMethod) {
		switch (countingMethod) {
		case EmbryonicAge:
			return (weeks + 2) * DAYS_IN_A_WEEK + days;
		case GestationalAge:
		default:
			return weeks * DAYS_IN_A_WEEK + days;
		}
	}

	/**
	 * Calculates gestational age by the ultrasound.
	 * 
	 * @param ultrasoundDate
	 *            date of the screening
	 * @param weeks
	 *            count of weeks of embryonic/gestational age, must be >= 0 and
	 *            < {@code FULL_EMBRYONIC_AGE}/{@code FULL_GESTATIONAL_AGE}
	 * @param days
	 *            count of days, must be >= 0 and < {@code DAYS_IN_A_WEEK}
	 * @param countingMethod
	 *            gestational age, embryonic age
	 * @param date
	 * @return estimated gestational age of pregnancy, check it's correctness
	 */
	public GestationalAge getGestationalAgeByUltrasound(
			Calendar ultrasoundDate, int weeks, int days,
			CountingMethod countingMethod, Calendar date) {
		int currentGestationalAge = getCurrentGestationalAge(weeks, days,
				countingMethod);
		ultrasoundDate.add(Calendar.DAY_OF_MONTH, -currentGestationalAge);
		long current = getTimeInMillis(date);
		long begin = getTimeInMillis(ultrasoundDate);

		setIncorrectDateMessage(current, begin);
		if (message.equals("")) {
			setAccuracyMessage(weeks, days, countingMethod);
		}

		return getGestationalAge(current, begin);
	}
}
