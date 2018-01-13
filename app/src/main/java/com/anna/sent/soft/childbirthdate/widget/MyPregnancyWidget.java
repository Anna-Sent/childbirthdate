package com.anna.sent.soft.childbirthdate.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.RemoteViews;

import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.childbirthdate.utils.MyLog;

import java.util.Calendar;

public abstract class MyPregnancyWidget extends AppWidgetProvider {
    private static final String UPDATE_ACTION = "UPDATE_MY_PREGNANCY_WIDGET_ACTION";

    public static void updateAllWidgets(Context context) {
        updateWidgets(context, MyPregnancyWidgetSmall.class);
        updateWidgets(context, MyPregnancyWidgetSimple.class);
        updateWidgets(context, MyPregnancyWidgetAdditional.class);
    }

    private static PendingIntent getPendingIntent(Context context, Class<?> cls) {
        Intent updateWidget = new Intent(context, cls);
        updateWidget.setAction(MyPregnancyWidget.UPDATE_ACTION);
        return PendingIntent.getBroadcast(context, 0,
                updateWidget, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private static void updateWidgets(Context context, Class<?> cls) {
        try {
            getPendingIntent(context, cls).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    public static void installAlarms(Context context, Class<?> cls) {
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Calendar midnight = Calendar.getInstance();
        // midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.HOUR, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        midnight.set(Calendar.AM_PM, Calendar.AM);
        midnight.add(Calendar.DAY_OF_MONTH, 1);
        PendingIntent operation = getPendingIntent(context, cls);
        alarmManager.cancel(operation);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                midnight.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                operation);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Manual or automatic widget update started
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(UPDATE_ACTION)
                    || action.equals(Intent.ACTION_TIME_CHANGED)
                    || action.equals(Intent.ACTION_TIMEZONE_CHANGED)
                    || action.equals(Intent.ACTION_DATE_CHANGED)
                    || action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                AppWidgetManager appWidgetManager = AppWidgetManager
                        .getInstance(context);
                int[] appWidgetIds = appWidgetManager
                        .getAppWidgetIds(new ComponentName(context, getClass()));
                if (appWidgetIds.length > 0) {
                    MyLog.getInstance().logcat(Log.DEBUG, getClass().getSimpleName() + " got action " + action);

                    onUpdate(context, appWidgetManager, appWidgetIds);

                    // Need to reinstall alarm on these events
                    if (action.equals(Intent.ACTION_TIME_CHANGED)
                            || action.equals(Intent.ACTION_TIMEZONE_CHANGED)
                            || action.equals(Intent.ACTION_DATE_CHANGED)
                            || action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                        installAlarms(context, getClass());
                    }
                }
            }
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        // Update each of the widgets with the remote adapter
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = getBuilder().buildViews(context,
                    appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    protected abstract Builder getBuilder();

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences settings = Settings.getSettings(context);
        Editor editor = settings.edit();
        for (int appWidgetId : appWidgetIds) {
            editor.remove(Shared.Saved.Widget.EXTRA_CALCULATION_METHOD
                    + appWidgetId);
            editor.remove(Shared.Saved.Widget.EXTRA_COUNTDOWN + appWidgetId);
            editor.remove(Shared.Saved.Widget.EXTRA_SHOW_CALCULATION_METHOD
                    + appWidgetId);
        }

        editor.apply();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        MyLog.getInstance().logcat(Log.DEBUG, getClass().getSimpleName() + " cancel alarm");

        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context, getClass()));
    }
}
