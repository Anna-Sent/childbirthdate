package com.anna.sent.soft.childbirthdate.widget;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.RemoteViews;

import com.anna.sent.soft.childbirthdate.shared.Shared;

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
					.getAppWidgetIds(new ComponentName(context, getClass()));
			onUpdate(context, appWidgetManager, appWidgetIds);
		}

		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// Update each of the widgets with the remote adapter
		for (int i = 0; i < appWidgetIds.length; ++i) {
			RemoteViews views = getBuilder().buildViews(context,
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

	private static PendingIntent getPendingIntent(Context context, Class<?> cls) {
		Intent updateWidget = new Intent(context, cls);
		updateWidget.setAction(MyPregnancyWidget.UPDATE_ACTION);
		PendingIntent result = PendingIntent.getBroadcast(context, 0,
				updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
		return result;
	}

	private static void updateWidgets(Context context, Class<?> cls) {
		try {
			getPendingIntent(context, cls).send();
		} catch (CanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		SharedPreferences settings = Shared.getSettings(context);
		Editor editor = settings.edit();
		for (int i = 0; i < appWidgetIds.length; ++i) {
			editor.remove(Shared.Saved.Widget.EXTRA_CALCULATING_METHOD
					+ appWidgetIds[i]);
			editor.remove(Shared.Saved.Widget.EXTRA_COUNTDOWN + appWidgetIds[i]);
			editor.remove(Shared.Saved.Widget.EXTRA_SHOW_CALCULATING_METHOD
					+ appWidgetIds[i]);
		}

		editor.commit();
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}
}