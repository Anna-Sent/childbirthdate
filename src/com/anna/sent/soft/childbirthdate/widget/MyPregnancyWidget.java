package com.anna.sent.soft.childbirthdate.widget;

import java.util.Calendar;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Define a simple widget that shows a text.
 */
public abstract class MyPregnancyWidget extends AppWidgetProvider {
	public static final String UPDATE_ACTION = "UPDATE_MY_PREGNANCY_WIDGET_ACTION";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Manual or automatic widget update started
		String action = intent.getAction();
		if (action.equals(UPDATE_ACTION)
				|| action.equals(Intent.ACTION_TIME_CHANGED)
				|| action.equals(Intent.ACTION_TIMEZONE_CHANGED)
				|| action.equals(Intent.ACTION_DATE_CHANGED)) {
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			int[] appWidgetIds = appWidgetManager
					.getAppWidgetIds(new ComponentName(context,
							MyPregnancyWidget.class));
			onUpdate(context, appWidgetManager, appWidgetIds);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Calendar today = Calendar.getInstance();

		// Update each of the widgets with the remote adapter
		for (int i = 0; i < appWidgetIds.length; ++i) {
			RemoteViews views = getBuilder().buildViews(context, today,
					appWidgetIds[i]);
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	protected abstract Builder getBuilder();

	public static void updateAllWidgets(Context context) {
		updateWidgets(context, MyPregnancyWidgetSmall.class);
		updateWidgets(context, MyPregnancyWidgetSimple.class);
		updateWidgets(context, MyPregnancyWidgetAdditional.class);
	}

	private static void updateWidgets(Context context, Class<?> cls) {
		Intent updateWidget = new Intent(context, cls);
		updateWidget.setAction(MyPregnancyWidget.UPDATE_ACTION);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0,
				updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
		try {
			pending.send();
		} catch (CanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}