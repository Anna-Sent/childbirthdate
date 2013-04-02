package com.anna.sent.soft.childbirthdate.widget;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public abstract class Builder {
	protected abstract boolean hasTV1();

	protected abstract boolean hasTV3();

	protected abstract int getWidgetLayoutId();

	public RemoteViews buildViews(Context context, Calendar today,
			int appWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				getWidgetLayoutId());

		SharedPreferences settings = Shared.getSettings(context);
		int calculatingMethod = settings.getInt(
				Shared.Saved.Widget.EXTRA_CALCULATING_METHOD + appWidgetId,
				Shared.Saved.Widget.Calculate.UNKNOWN);
		boolean countdown = settings.getBoolean(
				Shared.Saved.Widget.EXTRA_COUNTDOWN + appWidgetId, false);
		boolean showCalculatingMethod = settings
				.getBoolean(Shared.Saved.Widget.EXTRA_SHOW_CALCULATING_METHOD
						+ appWidgetId, false);
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

		if (p == null) {
			views.setViewVisibility(R.id.tv1, View.GONE);
			views.setTextViewText(R.id.tv2,
					context.getString(R.string.errorIncorrectGestationAge));
			if (hasTV3()) {
				views.setViewVisibility(R.id.tv3, View.GONE);
			}

			views.setViewVisibility(R.id.tv4,
					showCalculatingMethod ? View.VISIBLE : View.GONE);
			if (showCalculatingMethod) {
				views.setTextViewText(R.id.tv4, context
						.getString(R.string.errorNotSelectedCalculationMethod));
			}
		} else {
			Calendar currentDate = Calendar.getInstance();
			p.setCurrentPoint(currentDate);
			if (p.isCorrect()) {
				if (hasTV1()) {
					views.setViewVisibility(R.id.tv1, View.VISIBLE);
					views.setTextViewText(
							R.id.tv1,
							countdown ? context
									.getString(R.string.widgetTextRest)
									: context
											.getString(R.string.widgetTextInfo));
				}

				views.setTextViewText(R.id.tv2,
						countdown ? p.getRestInfo(context) : p.getInfo(context));
				if (hasTV3()) {
					if (countdown) {
						views.setViewVisibility(R.id.tv3, View.GONE);
					} else {
						views.setViewVisibility(R.id.tv3, View.VISIBLE);
						views.setTextViewText(R.id.tv3,
								p.getAdditionalInfo(context));
					}
				}
			} else {
				views.setViewVisibility(R.id.tv1, View.GONE);
				views.setTextViewText(R.id.tv2,
						context.getString(R.string.errorIncorrectGestationAge));
				if (hasTV3()) {
					Calendar start = p.getStartPoint(), end = p.getEndPoint();
					views.setViewVisibility(R.id.tv3, View.VISIBLE);
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

			views.setViewVisibility(R.id.tv4,
					showCalculatingMethod ? View.VISIBLE : View.GONE);
			if (showCalculatingMethod) {
				views.setTextViewText(R.id.tv4, context.getString(
						R.string.widgetCalculatingMethod,
						calculatingMethodString));
			}
		}

		return views;
	}
}