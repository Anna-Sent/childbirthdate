package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

/**
 * Define a simple widget that shows a text.
 */
public class MyPregnancyWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Calendar today = Calendar.getInstance();

		// Update each of the widgets with the remote adapter
		for (int i = 0; i < appWidgetIds.length; ++i) {
			RemoteViews views = buildViews(context, today);
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static RemoteViews buildViews(Context context, Calendar today) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.my_pregnancy_widget_layout);
		return views;
	}
}