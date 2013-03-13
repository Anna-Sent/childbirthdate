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
	 * The length of the luteal phase ranges from 10 to 16 days in 95% of
	 * normally ovulating women.
	 */
	public final static int MIN_LUTEAL_PHASE_LEN = 10;
	public final static int MAX_LUTEAL_PHASE_LEN = 16;

	/**
	 * Average duration of pregnancy is actually about 40 weeks. On average, it
	 * corresponds to an embryonic age of 38 weeks.
	 */
	public final static int FULL_GESTATIONAL_AGE = 40;
	public final static int FULL_EMBRYONIC_AGE = 38;

	/**
	 * There is 7 days in a week.
	 */
	public final static int DAYS_IN_A_WEEK = 7;

	private String message = "";
	private Context mContext;

	/**
	 * Constructor of the class {@link PregnancyCalculator}.
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
	 *            from MIN_MENSTRUAL_CYCLE_LEN to MAX_MENSTRUAL_CYCLE_LEN
	 *            inclusive
	 * @param lutealPhaseLen
	 *            must be in range from MIN_LUTEAL_PHASE_LEN to
	 *            MAX_LUTEAL_PHASE_LEN inclusive
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

	/**
	 * Calculates childbirth date by the ultrasound. The method is the very
	 * accurate at gestational age of the 11-14 weeks.
	 * 
	 * @param date
	 *            date of the screening
	 * @param weeks
	 *            count of weeks of pregnancy, must be >= 0 and (<
	 *            FULL_EMBRYONIC_AGE if isEmbryonicAge is false or <
	 *            FULL_GESTATIONAL_AGE if isEmbryonicAge is true)
	 * @param days
	 *            must be >= 0 and < DAYS_IN_A_WEEK
	 * @param isEmbryonicAge
	 * @return estimated childbirth date
	 */
	public Calendar getChildbirthDateByUltrasound(Calendar date, int weeks,
			int days, boolean isEmbryonicAge) {
		if (weeks <= 13 && !isEmbryonicAge || weeks <= 11 && isEmbryonicAge) {
			message = mContext.getString(R.string.message1);
		} else {
			message = mContext.getString(R.string.message2);
		}

		int pregnancyPeriod = isEmbryonicAge ? FULL_EMBRYONIC_AGE
				* DAYS_IN_A_WEEK : FULL_GESTATIONAL_AGE * DAYS_IN_A_WEEK;

		date.add(Calendar.DAY_OF_MONTH, pregnancyPeriod - weeks
				* DAYS_IN_A_WEEK - days);
		return date;
	}

	/**
	 * Use after call getChildbirthDateByUltrasound() or
	 * getPregnancyAgeByUltrasound().
	 * 
	 * @return message about the accuracy of method
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * The class represents pregnancy age as count of weeks and count of days.
	 * 
	 * @author Anna
	 * 
	 */
	public class PregnancyAge {
		private int weeks = 0;
		private int days = 0;

		public PregnancyAge() {
		}

		public PregnancyAge(int weeks, int days) {
			this.weeks = weeks;
			this.days = days;
		}

		/**
		 * Checker for the correctness of values of weeks and days.
		 * 
		 * @return {@code true}, if pregnancy age is correct, {@code false}
		 *         otherwise
		 */
		public boolean isCorrect() {
			int due = weeks * 7 + days;
			return due >= 0 && due <= 42 * 7;
		}

		/**
		 * Pregnancy age must be correct before call of this method. So, call
		 * isCorrect() before using of this method.
		 * 
		 * @return
		 */
		public int getTrimester() {
			if (weeks <= 12) {
				return 1;
			} else if (weeks <= 28) {
				return 2;
			}

			return 3;
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

	private PregnancyAge getPregnancyDue(long current, long begin) {
		long difference = current - begin;
		int days = (int) (difference / (1000 * 3600 * 24));
		int weeks = days / 7;
		days = days - weeks * 7;
		return new PregnancyAge(weeks, days);
	}

	/**
	 * Calculates pregnancy age by the first day of the woman's last menstrual
	 * period.
	 * 
	 * @param lastMenstrualPeriodDate
	 *            first day of last menstrual period (first day of bleeding)
	 * @param date
	 * @return estimated pregnancy age
	 */
	public PregnancyAge getPregnancyAgeByLastMenstruationDate(
			Calendar lastMenstrualPeriodDate, Calendar date) {
		long current = getTimeInMillis(date);
		long begin = getTimeInMillis(lastMenstrualPeriodDate);
		return getPregnancyDue(current, begin);
	}

	/**
	 * Calculates pregnancy age by the ovulation (conception) date.
	 * 
	 * @param menstrualCycleLen
	 *            average length of cycles (from first day of your period to the
	 *            first day of your next period) must be in range from
	 *            MIN_MENSTRUAL_CYCLE_LEN to MAX_MENSTRUAL_CYCLE_LEN inclusive
	 * @param lutealPhaseLen
	 *            must be in range from MIN_LUTEAL_PHASE_LEN to
	 *            MAX_LUTEAL_PHASE_LEN inclusive
	 * @param ovulationDate
	 *            probable ovulation (conception) date
	 * @param date
	 * @return estimated pregnancy age
	 */
	public PregnancyAge getPregnancyAgeByOvulationDate(int menstrualCycleLen,
			int lutealPhaseLen, Calendar ovulationDate, Calendar date) {
		int follicularPhaseLen = menstrualCycleLen - lutealPhaseLen;
		ovulationDate.add(Calendar.DAY_OF_MONTH, -follicularPhaseLen);
		long current = getTimeInMillis(date);
		long begin = getTimeInMillis(ovulationDate);
		return getPregnancyDue(current, begin);
	}

	/**
	 * Calculates pregnancy age by the ultrasound. The method is the very
	 * accurate at gestational age of the 11-14 weeks.
	 * 
	 * @param ultrasoundDate
	 *            date of the screening
	 * @param weeks
	 *            count of weeks of pregnancy, must be >= 0 and (<
	 *            FULL_EMBRYONIC_AGE if isEmbryonicAge is false or <
	 *            FULL_GESTATIONAL_AGE if isEmbryonicAge is true)
	 * @param days
	 *            must be >= 0 and < DAYS_IN_A_WEEK
	 * @param isEmbryonicAge
	 * @param date
	 * @return estimated gestational age of pregnancy
	 */
	public PregnancyAge getPregnancyAgeByUltrasound(Calendar ultrasoundDate,
			int weeks, int days, boolean isEmbryonicAge, Calendar date) {
		if (weeks <= 13 && !isEmbryonicAge || weeks <= 11 && isEmbryonicAge) {
			message = mContext.getString(R.string.message1);
		} else {
			message = mContext.getString(R.string.message2);
		}

		int pregnancyPeriod = (isEmbryonicAge ? weeks : weeks + 2)
				* DAYS_IN_A_WEEK + days;
		ultrasoundDate.add(Calendar.DAY_OF_MONTH, -pregnancyPeriod);
		return new PregnancyAge();
	}
}
