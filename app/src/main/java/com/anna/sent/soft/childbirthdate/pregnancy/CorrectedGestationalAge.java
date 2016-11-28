package com.anna.sent.soft.childbirthdate.pregnancy;

import java.util.Calendar;

/**
 * The class represents corrected gestational age in weeks and days.
 * 
 * @author Anna
 * 
 */
public final class CorrectedGestationalAge extends GestationalAge {
	private final int difference;

	public CorrectedGestationalAge(Calendar start, int menstrualCycleLen,
								   int lutealPhaseLen) {
		super(start);
		int phollicularPhaseLen = menstrualCycleLen - lutealPhaseLen;
		difference = -PregnancyCalculator.AVG_PHOLLICULAR_PHASE_LENGTH
				+ phollicularPhaseLen;
	}

	@Override
	public int getFullDurationInDays() {
		return super.getFullDurationInDays() + difference;
	}
}