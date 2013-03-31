package com.anna.sent.soft.childbirthdate.pregnancy;

import java.util.Calendar;

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

	public final static int AVG_PHOLLICULAR_PHASE_LENGTH = AVG_MENSTRUAL_CYCLE_LENGTH
			- AVG_LUTEAL_PHASE_LENGTH;
	public final static int MIN_PHOLLICULAR_PHASE_LENGTH = MIN_MENSTRUAL_CYCLE_LEN
			- MAX_LUTEAL_PHASE_LEN;
	public final static int MAX_PHOLLICULAR_PHASE_LENGTH = MAX_MENSTRUAL_CYCLE_LEN
			- MIN_LUTEAL_PHASE_LEN;

	public final static int GESTATIONAL_AVG_AGE_IN_WEEKS = 40;
	public final static int EMBRYONIC_AVG_AGE_IN_WEEKS = GESTATIONAL_AVG_AGE_IN_WEEKS - 2;

	public final static int GESTATIONAL_MAX_AGE_DURATION = GESTATIONAL_AVG_AGE_IN_WEEKS
			* 7 - AVG_PHOLLICULAR_PHASE_LENGTH + MAX_PHOLLICULAR_PHASE_LENGTH;
	public final static int EMBRYONIC_MAX_AGE_DURATION = GESTATIONAL_MAX_AGE_DURATION
			- MIN_PHOLLICULAR_PHASE_LENGTH;

	public final static int GESTATIONAL_MAX_ULTRASOUND_ACCURACY_IN_WEEKS = 14;
	public final static int EMBRYONIC_MAX_ULTRASOUND_ACCURACY_IN_WEEKS = GESTATIONAL_MAX_ULTRASOUND_ACCURACY_IN_WEEKS - 2;

	public final static int GESTATIIONAL_FIRST_TRIMESTER_END_INCLUSIVE_IN_WEEKS = 12;
	public final static int EMBRYONIC_FIRST_TRIMESTER_END_INCLUSIVE_IN_WEEKS = GESTATIIONAL_FIRST_TRIMESTER_END_INCLUSIVE_IN_WEEKS - 2;

	public final static int GESTATIONAL_SECOND_TRIMESTER_END_INCLUSIVE_IN_WEEKS = 28;
	public final static int EMBRYONIC_SECOND_TRIMESTER_END_INCLUSIVE_IN_WEEKS = GESTATIONAL_SECOND_TRIMESTER_END_INCLUSIVE_IN_WEEKS - 2;

	public static class Factory {
		public static Pregnancy get(Calendar lastMenstruationDate,
				int menstrualCycleLen, int lutealPhaseLen) {
			return new CorrectedAge(lastMenstruationDate, menstrualCycleLen,
					lutealPhaseLen);
		}

		public static Pregnancy get(Calendar ovulationDate) {
			return new EmbryonicAge(ovulationDate);
		}

		public static Pregnancy get(Calendar ultrasoundDate, int weeks,
				int days, boolean isEmbryonicAge) {
			if (isEmbryonicAge) {
				return new EmbryonicAge(weeks, days, ultrasoundDate);
			} else {
				return new GestationalAge(weeks, days, ultrasoundDate);
			}
		}
	}
}
