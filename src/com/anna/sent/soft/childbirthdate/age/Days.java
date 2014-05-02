package com.anna.sent.soft.childbirthdate.age;

public class Days {
	private int days;

	public Days() {
		this(0);
	}

	public Days(int days) {
		this.days = days;
	}

	public int getDays() {
		return days;
	}

	public static final String DELIMITER_LIST = ";";

	public String save() {
		return String.valueOf(days);
	}

	public boolean load(String str) {
		if (str == null) {
			return false;
		}

		try {
			int d = Integer.parseInt(str);
			days = d;
			return true;
		} catch (NumberFormatException e) {
		}

		return false;
	}
}