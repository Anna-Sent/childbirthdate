package com.anna.sent.soft.childbirthdate.pregnancy;

import java.util.Calendar;

/**
 * The class represents gestational age in weeks and days.
 * 
 * @author Anna
 * 
 */
public class GestationalAge extends Pregnancy {
	public GestationalAge(Calendar start) {
		super(start);
	}

	public GestationalAge(int weeks, int days, Calendar current) {
		super(weeks, days, current);
	}

	@Override
	public int getFullDurationInDays() {
		return PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS * 7;
	}

	@Override
	protected final int getMaxDurationInDays() {
		return PregnancyCalculator.GESTATIONAL_MAX_AGE_DURATION;
	}

	@Override
	protected final int getFirstTrimesterEndInclusive() {
		return PregnancyCalculator.GESTATIIONAL_FIRST_TRIMESTER_END_INCLUSIVE_IN_WEEKS;
	}

	@Override
	protected final int getSecondTrimesterEndInclusive() {
		return PregnancyCalculator.GESTATIONAL_SECOND_TRIMESTER_END_INCLUSIVE_IN_WEEKS;
	}
}
