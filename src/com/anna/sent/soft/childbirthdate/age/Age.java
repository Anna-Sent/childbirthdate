package com.anna.sent.soft.childbirthdate.age;

public class Age implements ISetting {
	private int weeks, days;

	public Age() {
		this(0, 0);
	}

	public Age(int weeks, int days) {
		this.weeks = weeks;
		this.days = days;
	}

	public int getWeeks() {
		return weeks;
	}

	public int getDays() {
		return days;
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
			} catch (NumberFormatException e) {
			}
		}

		return null;
	}
}