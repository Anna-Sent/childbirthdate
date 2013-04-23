package com.anna.sent.soft.utils;

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
}
