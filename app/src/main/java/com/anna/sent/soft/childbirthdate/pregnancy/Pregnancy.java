package com.anna.sent.soft.childbirthdate.pregnancy;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.Age;

import java.util.Calendar;

public abstract class Pregnancy {
    private static final int FIRST_TRIMESTER = 1;
    private static final int SECOND_TRIMESTER = 2;
    private static final int THIRD_TRIMESTER = 3;

    private final Calendar startPoint;
    private Calendar currentPoint;
    private Age age;

    /**
     * @param start start point of pregnancy, must be not null
     */
    Pregnancy(@NonNull Calendar start) {
        startPoint = (Calendar) start.clone();
        zeroDate(startPoint);
        currentPoint = startPoint;
    }

    /**
     * @param weeks   number of weeks pregnancy lengths
     * @param days    number of days pregnancy lengths, should be in range from 0 to 6
     * @param current current date, must be not null
     */
    Pregnancy(int weeks, @IntRange(from = 0, to = 6) int days, @NonNull Calendar current) {
        age = new Age(weeks, days);
        startPoint = (Calendar) current.clone();
        zeroDate(startPoint);
        startPoint.add(Calendar.DAY_OF_MONTH, -getDurationInDays());
        currentPoint = startPoint;
    }

    @SuppressWarnings("unused")
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

    public Calendar getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Calendar current) {
        currentPoint = (Calendar) current.clone();
        zeroDate(currentPoint);
        long difference = currentPoint.getTimeInMillis()
                - startPoint.getTimeInMillis();
        int days = (int) (difference / (1000L * 3600L * 24L));
        int weeks = days / Age.DAYS_IN_WEEK;
        days = days - weeks * Age.DAYS_IN_WEEK;
        try {
            if (age == null) {
                age = new Age(weeks, days);
            } else {
                age.setDays(days);
                age.setWeeks(weeks);
            }
        } catch (Exception e) {
            age = null;
        }
    }

    public void setAge(Age age) {
        if (this.age == null) {
            this.age = new Age(age);
        } else {
            this.age.set(age);
        }

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
     * correct; {@code false}, otherwise
     */
    public boolean isCorrect() {
        if (age == null) {
            return false;
        }

        int duration = getDurationInDays();
        return duration >= 0 && duration <= getMaxDurationInDays();
    }

    /**
     * @return number of trimester, if weeks value is correct; -1 otherwise
     */
    private int getTrimesterNumber() {
        if (age != null && age.getWeeks() >= 0) {
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

    private int getDurationInDays() {
        return age == null ? -1 : age.getDurationInDays();
    }

    @SuppressWarnings("unused")
    public int getWeeks() {
        return age == null ? -1 : age.getWeeks();
    }

    @SuppressWarnings("unused")
    public int getDays() {
        return age == null ? -1 : age.getDays();
    }

    protected abstract int getFullDurationInDays();

    private int getFullDurationWeeks() {
        return getFullDurationInDays() / Age.DAYS_IN_WEEK;
    }

    private int getFullDurationDays() {
        return getFullDurationInDays() - getFullDurationWeeks()
                * Age.DAYS_IN_WEEK;
    }

    public int getRestInDays() {
        return getFullDurationInDays() - getDurationInDays();
    }

    private int getRestWeeks() {
        return getRestInDays() / Age.DAYS_IN_WEEK;
    }

    private int getRestDays() {
        return getRestInDays() - getRestWeeks() * Age.DAYS_IN_WEEK;
    }

    protected abstract int getMaxDurationInDays();

    protected abstract int getFirstTrimesterEndInclusive();

    protected abstract int getSecondTrimesterEndInclusive();

    public String getRestInfo(Context context) {
        return new Age(getRestWeeks(), getRestDays()).toString(context);
    }

    public String getInfo(Context context) {
        return age == null ? "null" : age.toString(context);
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
                String.valueOf(getTrimesterNumber()), new Age(getFullDurationWeeks(),
                        getFullDurationDays()).toString(context), result);

        return result;
    }
}
