package com.anna.sent.soft.childbirthdate.utils;

import java.util.Calendar;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.DatePicker;

public class DateUtils {
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

	public static String toString(Context context, Calendar date) {
		return DateFormat.getDateFormat(context).format(date.getTime());
	}

	public static void init(DatePicker datePicker,
							DatePicker.OnDateChangedListener listener) {
		Calendar today = Calendar.getInstance();
		init(datePicker, today, listener);
	}

	public static void init(DatePicker datePicker, Calendar date,
							DatePicker.OnDateChangedListener listener) {
		int year = date.get(Calendar.YEAR), monthOfYear = date
				.get(Calendar.MONTH), dayOfMonth = date
				.get(Calendar.DAY_OF_MONTH);
		datePicker.init(year, monthOfYear, dayOfMonth, listener);
	}

	public static boolean areEqual(Calendar date1, Calendar date2) {
		return date1.get(Calendar.DAY_OF_MONTH) == date2
				.get(Calendar.DAY_OF_MONTH)
				&& date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
				&& date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR);
	}
}