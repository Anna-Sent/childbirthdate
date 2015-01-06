package com.anna.sent.soft.childbirthdate.widget;

import java.util.Calendar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.RemoteViews;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.ResultsActivity;
import com.anna.sent.soft.childbirthdate.data.DataImpl;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public abstract class Builder {
	protected abstract boolean hasTV1();

	protected abstract boolean hasTV3();

	private void setOnClickPendingIntent(Context context, RemoteViews views) {
		Intent intent = new Intent(context, ResultsActivity.class);
		intent.putExtra(Shared.Child.EXTRA_IS_STARTED_FROM_WIDGET, true);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = TaskStackBuilder.create(context)
				.addNextIntentWithParentStack(intent)
				.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.widget, pendingIntent);
	}

	public RemoteViews buildViews(Context context, int appWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.my_pregnancy_widget_layout);

		setOnClickPendingIntent(context, views);

		SharedPreferences settings = Settings.getSettings(context);
		int calculationMethod = settings.getInt(
				Shared.Saved.Widget.EXTRA_CALCULATION_METHOD + appWidgetId,
				Shared.Calculation.UNKNOWN);
		boolean countdown = settings.getBoolean(
				Shared.Saved.Widget.EXTRA_COUNTDOWN + appWidgetId, false);
		boolean showCalculationMethod = settings
				.getBoolean(Shared.Saved.Widget.EXTRA_SHOW_CALCULATION_METHOD
						+ appWidgetId, false);

		DataImpl data = new DataImpl(context);
		data.update();

		Pregnancy p = PregnancyCalculator.Factory.get(data, calculationMethod);

		if (p == null) {
			views.setViewVisibility(R.id.tv1, View.GONE);
			views.setTextViewText(R.id.tv2,
					context.getString(R.string.errorIncorrectGestationalAge));
			views.setViewVisibility(R.id.tv3, View.GONE);
			views.setTextViewText(R.id.tv4, context
					.getString(R.string.errorNotSelectedCalculationMethod));
			views.setViewVisibility(R.id.tv4,
					showCalculationMethod ? View.VISIBLE : View.GONE);
		} else {
			String[] methodNames = context.getResources().getStringArray(
					R.array.methodNames);
			String calculationMethodString = methodNames[calculationMethod - 1];
			Calendar currentDate = Calendar.getInstance();
			p.setCurrentPoint(currentDate);
			if (p.isCorrect()) {
				views.setTextViewText(
						R.id.tv1,
						countdown ? context
								.getString(R.string.widgetTextRestInfo)
								: context.getString(R.string.widgetTextInfo));
				views.setViewVisibility(R.id.tv1, hasTV1() ? View.VISIBLE
						: View.GONE);

				views.setTextViewText(R.id.tv2,
						countdown ? p.getRestInfo(context) : p.getInfo(context));
				views.setTextViewText(R.id.tv3, p.getAdditionalInfo(context));
				views.setViewVisibility(R.id.tv3,
						hasTV3() && !countdown ? View.VISIBLE : View.GONE);
			} else {
				views.setViewVisibility(R.id.tv1, View.GONE);
				views.setTextViewText(R.id.tv2, context
						.getString(R.string.errorIncorrectGestationalAge));
				Calendar start = p.getStartPoint(), end = p.getEndPoint();
				if (currentDate.before(start)) {
					views.setTextViewText(
							R.id.tv3,
							context.getString(R.string.errorIncorrectCurrentDateSmaller));
				} else if (currentDate.after(end)) {
					views.setTextViewText(
							R.id.tv3,
							context.getString(R.string.errorIncorrectCurrentDateGreater));
				} else {
					views.setTextViewText(R.id.tv3, "?");
				}

				views.setViewVisibility(R.id.tv3, hasTV3() ? View.VISIBLE
						: View.GONE);
			}

			views.setTextViewText(R.id.tv4, context.getString(
					R.string.widgetCalculationMethod, calculationMethodString));
			views.setViewVisibility(R.id.tv4,
					showCalculationMethod ? View.VISIBLE : View.GONE);
		}

		return views;
	}
}