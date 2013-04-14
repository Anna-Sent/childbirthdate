package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.widget.DatePicker;

public class Utils {
	public static Calendar getDate(DatePicker datePicker) {
		Calendar date = Calendar.getInstance();
		date.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth());
		return date;
	}

	public static void setDate(DatePicker datePicker, Calendar date) {
		datePicker.updateDate(date.get(Calendar.YEAR),
				date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
	}
}
