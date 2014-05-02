package com.anna.sent.soft.childbirthdate.age;

import java.util.ArrayList;
import java.util.List;

public class Age {
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

	public static final String DELIMITER_LIST = ";";

	public static String saveList(List<Age> list) {
		String result = "";
		for (int i = 0; i < list.size(); ++i) {
			result += list.get(i).save()
					+ (i == list.size() - 1 ? "" : DELIMITER_LIST);
		}

		return result;
	}

	public static List<Age> loadList(String str) {
		List<Age> result = new ArrayList<Age>();

		if (str != null) {
			String[] tokens = str.split(DELIMITER_LIST);
			for (int i = 0; i < tokens.length; ++i) {
				Age age = new Age();
				if (age.load(tokens[i])) {
					result.add(age);
				}
			}
		}

		return result;
	}

	private static final String DELIMITER = ",";

	public String save() {
		return weeks + DELIMITER + days;
	}

	public boolean load(String str) {
		if (str == null) {
			return false;
		}

		String[] tokens = str.split(DELIMITER);
		if (tokens.length == 2) {
			try {
				int w = Integer.parseInt(tokens[0]);
				int d = Integer.parseInt(tokens[1]);
				weeks = w;
				days = d;
				return true;
			} catch (NumberFormatException e) {
			}
		}

		return false;
	}
}