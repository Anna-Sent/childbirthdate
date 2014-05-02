package com.anna.sent.soft.childbirthdate.age;

public class Days implements ISetting {
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
		} catch (NumberFormatException e) {
		}

		return null;
	}
}