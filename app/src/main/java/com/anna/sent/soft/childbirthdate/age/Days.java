package com.anna.sent.soft.childbirthdate.age;

import android.content.Context;
import android.util.Log;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.utils.MyLog;

import java.io.Serializable;

public class Days implements ISetting, Serializable {
    private static final long serialVersionUID = -2116357134482613794L;

    private int days;

    public Days() {
        this(0);
    }

    public Days(int days) {
        setDays(days);
    }

    public int getDays() {
        return days;
    }

    private void setDays(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(
                    "Days value must be non-negative");
        }

        days = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Days d = (Days) obj;
        return days == d.days;
    }

    @Override
    public String toString() {
        return save();
    }

    @Override
    public String toString(Context context) {
        return days + " " + context.getString(R.string.days);
    }

    @Override
    public String save() {
        return String.valueOf(days);
    }

    @Override
    public ISetting load(String str) {
        if (str == null) {
            return null;
        }

        try {
            int d = Integer.parseInt(str);
            return new Days(d);
        } catch (Exception e) {
            MyLog.getInstance().logcat(Log.ERROR, e.toString());
        }

        return null;
    }
}