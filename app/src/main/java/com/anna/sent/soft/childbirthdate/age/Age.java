package com.anna.sent.soft.childbirthdate.age;

import android.content.Context;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.utils.MyLog;

import java.io.Serializable;

public class Age implements ISetting, Serializable {
    private static final long serialVersionUID = 2407705304970505309L;

    public static final int DAYS_IN_WEEK = 7;

    private int weeks, days;

    public Age() {
        this(0, 0);
    }

    public Age(Age age) {
        set(age);
    }

    public Age(int weeks, int days) {
        setWeeks(weeks);
        setDays(days);
    }

    public int getWeeks() {
        return weeks;
    }

    public void set(Age age) {
        if (age == null) {
            throw new IllegalArgumentException("Age must be not null");
        }

        weeks = age.weeks;
        days = age.days;
    }

    public void setWeeks(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(
                    "Weeks value must be non-negative");
        }

        weeks = value;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(
                    "Days value must be non-negative");
        }

        if (value >= DAYS_IN_WEEK) {
            throw new IllegalArgumentException("Days value must be less then "
                    + DAYS_IN_WEEK);
        }

        days = value;
    }

    public int getDurationInDays() {
        return weeks * DAYS_IN_WEEK + days;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Age age = (Age) obj;
        return weeks == age.weeks && days == age.days;
    }

    @Override
    public String toString() {
        return save();
    }

    @Override
    public String toString(Context context) {
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

    private static final String DELIMITER = ",";

    @Override
    public String save() {
        return weeks + DELIMITER + days;
    }

    @Override
    public ISetting load(String str) {
        if (str == null) {
            return null;
        }

        String[] tokens = str.split(DELIMITER);
        if (tokens.length == 2) {
            try {
                int w = Integer.parseInt(tokens[0]);
                int d = Integer.parseInt(tokens[1]);
                return new Age(w, d);
            } catch (Exception e) {
                MyLog.getInstance().logcat(Log.ERROR, e.toString());
            }
        }

        return null;
    }
}