package com.anna.sent.soft.childbirthdate.pregnancy;

import java.util.Calendar;

public class ByFirstMovementsGestationalAge extends GestationalAge {
	public ByFirstMovementsGestationalAge(Calendar date,
			boolean isFirstPregnancy) {
		super(isFirstPregnancy ? 20 : 22, 0, date);
	}
}
