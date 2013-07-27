package com.anna.sent.soft.childbirthdate.pregnancy;

import java.util.Calendar;

public class ByFirstAppearanceGestationalAge extends GestationalAge {
	public ByFirstAppearanceGestationalAge(Calendar date, int weeks) {
		super(weeks, 0, date);
	}
}
