package com.anna.sent.soft.childbirthdate.pregnancy;

import java.util.Calendar;

import com.anna.sent.soft.childbirthdate.age.Age;

/**
 * The class represents embryonic age in weeks and days.
 *
 * @author Anna
 */
public final class EmbryonicAge extends Pregnancy {
    public EmbryonicAge(Calendar start) {
        super(start);
    }

    public EmbryonicAge(int weeks, int days, Calendar current) {
        super(weeks, days, current);
    }

    @Override
    public int getFullDurationInDays() {
        return PregnancyCalculator.EMBRYONIC_AVG_AGE_IN_WEEKS
                * Age.DAYS_IN_WEEK;
    }

    @Override
    protected int getMaxDurationInDays() {
        return PregnancyCalculator.EMBRYONIC_MAX_AGE_DURATION;
    }

    @Override
    protected int getFirstTrimesterEndInclusive() {
        return PregnancyCalculator.EMBRYONIC_FIRST_TRIMESTER_END_INCLUSIVE_IN_WEEKS;
    }

    @Override
    protected int getSecondTrimesterEndInclusive() {
        return PregnancyCalculator.EMBRYONIC_SECOND_TRIMESTER_END_INCLUSIVE_IN_WEEKS;
    }
}