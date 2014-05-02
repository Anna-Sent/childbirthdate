package com.anna.sent.soft.childbirthdate.age;

import android.content.Context;

import com.anna.sent.soft.childbirthdate.R;

public class Days implements ISetting {
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

	public void setDays(int value) {
		if (value < 0) {
			throw new IllegalArgumentException(
					"Days value must be non-negative");
		}

		days = value;
	}

	@Override
	public String toString() {
		return save();
	}

	public String toString(Context context) {
		String result = days + " " + context.getString(R.string.days);
		return result;
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
		}

		return null;
	}
}