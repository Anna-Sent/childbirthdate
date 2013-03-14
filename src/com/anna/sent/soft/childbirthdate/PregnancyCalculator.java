package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;
import android.content.Context;

/**
 * Pregnancy due date calculator, pregnancy due date predictor.</br>
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
	public final static int MAX_MENSTRUAL_CYCLE_LEN = 46;

	/**
	 * The length of the luteal phase ranges from 11 to 16 days in 95% of
	 * normally ovulating women.
	 */
	public final static int MIN_LUTEAL_PHASE_LEN = 10;
	public final static int MAX_LUTEAL_PHASE_LEN = 17;

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

	private String accuracyMessage = "";
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
	 * period.
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
	 * @return estimated childbirth date
	 */
	public Calendar getChildbirthDateByLastMenstruationDate(
			int menstrualCycleLen, int lutealPhaseLen, Calendar date) {
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
		date.add(Calendar.DAY_OF_MONTH, FULL_EMBRYONIC_AGE * DAYS_IN_A_WEEK);
		return date;
	}

	private void setAccuracyMessage(int weeks, int days, CountingMethod countingMethod) {
		int maxAccuracy;
		switch (countingMethod) {
		case EmbryonicAge:
			maxAccuracy = MAX_EMBRYONIC_ULTRASOUND_ACCURACY;
		case GestationalAge:
		default:
			maxAccuracy = MAX_GESTATIONAL_ULTRASOUND_ACCURACY;
		}
		
		accuracyMessage = "";
		if (weeks < maxAccuracy) {
			accuracyMessage = mContext.getString(R.string.messageAccurate);
		} else {
			accuracyMessage = mContext.getString(R.string.messageInaccurate);
		}
	}
		
	private int getFullAge(CountingMethod countingMethod) {
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
		int fullAge = getFullAge(countingMethod);
		date.add(Calendar.DAY_OF_MONTH, fullAge - weeks * DAYS_IN_A_WEEK - days);
		return date;
	}

	/**
	 * Use after call {@code getChildbirthDateByUltrasound()} or
	 * {@code getGestationalAgeByUltrasound()}.
	 * 
	 * @return message about the accuracy of method
	 */
	public String getMessage() {
		return accuracyMessage;
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
		 * @return {@code true}, if gestational age is correct, {@code false}
		 *         otherwise
		 */
		public boolean isCorrect() {
			int due = weeks * DAYS_IN_A_WEEK + days;
			return due >= 0 && due <= MAX_GESTATIONAL_AGE * DAYS_IN_A_WEEK;
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
		int days = (int) (difference / (1000 * 3600 * 24));
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
	 * @return estimated gestational age
	 */
	public GestationalAge getGestationalAgeByLastMenstruationDate(
			Calendar lastMenstrualPeriodDate, Calendar date) {
		long current = getTimeInMillis(date);
		long begin = getTimeInMillis(lastMenstrualPeriodDate);
		return getGestationalAge(current, begin);
	}

	/**
	 * Calculates gestational age by the ovulation (conception) date.
	 * 
	 * @param menstrualCycleLen
	 *            average length of cycles (from first day of your period to the
	 *            first day of your next period) must be in range from
	 *            {@code MIN_MENSTRUAL_CYCLE_LEN} to
	 *            {@code MAX_MENSTRUAL_CYCLE_LEN} inclusive
	 * @param lutealPhaseLen
	 *            must be in range from {@code MIN_LUTEAL_PHASE_LEN} to
	 *            {@code MAX_LUTEAL_PHASE_LEN} inclusive
	 * @param ovulationDate
	 *            probable ovulation (conception) date
	 * @param date
	 * @return estimated gestational age
	 */
	public GestationalAge getGestationalAgeByOvulationDate(
			int menstrualCycleLen, int lutealPhaseLen, Calendar ovulationDate,
			Calendar date) {
		int follicularPhaseLen = menstrualCycleLen - lutealPhaseLen;
		ovulationDate.add(Calendar.DAY_OF_MONTH, -follicularPhaseLen);
		long current = getTimeInMillis(date);
		long begin = getTimeInMillis(ovulationDate);
		return getGestationalAge(current, begin);
	}

	private int getCurrentGestationalAge(int weeks, int days, CountingMethod countingMethod) {
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
	 * @return estimated gestational age of pregnancy
	 */
	public GestationalAge getGestationalAgeByUltrasound(
			Calendar ultrasoundDate, int weeks, int days,
			CountingMethod countingMethod, Calendar date) {
		setAccuracyMessage(weeks, days, countingMethod);
		int currentGestationalAge = getCurrentGestationalAge(weeks, days, countingMethod);
		ultrasoundDate.add(Calendar.DAY_OF_MONTH, -currentGestationalAge);
		long current = getTimeInMillis(date);
		long begin = getTimeInMillis(ultrasoundDate);
		return getGestationalAge(current, begin);
	}
}