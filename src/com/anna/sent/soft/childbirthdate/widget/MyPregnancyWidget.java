package com.anna.sent.soft.childbirthdate.widget;

import java.util.Calendar;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Shared;

/**
 * Define a simple widget that shows a text.
 */
public class MyPregnancyWidget extends AppWidgetProvider {
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
			RemoteViews views = buildViews(context, today, appWidgetIds[i]);
			appWidgetManager.updateAppWidget(appWidgetIds[i], views);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	public static RemoteViews buildViews(Context context, Calendar today,
			int appWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.my_pregnancy_widget_layout);

		SharedPreferences settings = Shared.getSettings(context);
		int calculatingMethod = settings.getInt(
				Shared.Saved.Widget.EXTRA_CALCULATING_METHOD + appWidgetId,
				Shared.Saved.Widget.Calculate.UNKNOWN);
		boolean countdown = settings.getBoolean(
				Shared.Saved.Widget.EXTRA_COUNTDOWN + appWidgetId, false);
		Pregnancy p = null;
		String calculatingMethodString = "";
		switch (calculatingMethod) {
		case Shared.Saved.Widget.Calculate.BY_LMP:
			int menstrualCycleLen = settings.getInt(
					Shared.Saved.Settings.EXTRA_MENSTRUAL_CYCLE_LEN,
					PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
			int lutealPhaseLen = settings.getInt(
					Shared.Saved.Settings.EXTRA_LUTEAL_PHASE_LEN,
					PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);
			Calendar lastMenstruationDate = Calendar.getInstance();
			lastMenstruationDate.setTimeInMillis(settings.getLong(
					Shared.Saved.Calculation.EXTRA_LAST_MENSTRUATION_DATE,
					System.currentTimeMillis()));
			p = PregnancyCalculator.Factory.get(lastMenstruationDate,
					menstrualCycleLen, lutealPhaseLen);
			calculatingMethodString = context.getString(R.string.byLMP);
			break;
		case Shared.Saved.Widget.Calculate.BY_OVULATION_DAY:
			Calendar ovulationDate = Calendar.getInstance();
			ovulationDate.setTimeInMillis(settings.getLong(
					Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
					System.currentTimeMillis()));
			p = PregnancyCalculator.Factory.get(ovulationDate);
			calculatingMethodString = context.getString(R.string.byOvulation);
			break;
		case Shared.Saved.Widget.Calculate.BY_ULTRASOUND:
			Calendar ultrasoundDate = Calendar.getInstance();
			ultrasoundDate.setTimeInMillis(settings.getLong(
					Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
					System.currentTimeMillis()));
			int weeks = settings
					.getInt(Shared.Saved.Calculation.EXTRA_WEEKS, 0);
			int days = settings.getInt(Shared.Saved.Calculation.EXTRA_DAYS, 0);
			boolean isEmbryonicAge = settings.getBoolean(
					Shared.Saved.Calculation.EXTRA_IS_EMBRYONIC_AGE, false);
			p = PregnancyCalculator.Factory.get(ultrasoundDate, weeks, days,
					isEmbryonicAge);
			calculatingMethodString = context.getString(R.string.byUltrasound);
			break;
		}

		views.setTextViewText(R.id.tv1,
				countdown ? context.getString(R.string.widgetTextRest)
						: context.getString(R.string.widgetTextInfo));
		if (p == null) {
			views.setTextViewText(R.id.tv2,
					context.getString(R.string.errorIncorrectGestationAge));
			views.setTextViewText(R.id.tv3, context
					.getString(R.string.errorNotSelectedCalculationMethod));
		} else {
			Calendar currentDate = Calendar.getInstance();
			p.setCurrentPoint(currentDate);
			if (p.isCorrect()) {
				views.setTextViewText(R.id.tv2, countdown ? p.getRest(context)
						: p.getInfo(context));
				views.setTextViewText(R.id.tv3, context.getString(
						R.string.widgetCalculatingMethod,
						calculatingMethodString));
			} else {
				views.setTextViewText(R.id.tv2,
						context.getString(R.string.errorIncorrectGestationAge));
				Calendar start = p.getStartPoint(), end = p.getEndPoint();
				if (currentDate.before(start)) {
					views.setTextViewText(
							R.id.tv3,
							context.getString(R.string.errorIncorrectCurrentDateSmaller));
				} else if (currentDate.after(end)) {
					views.setTextViewText(
							R.id.tv3,
							context.getString(R.string.errorIncorrectCurrentDateGreater));
				}
			}
		}

		return views;
	}
}