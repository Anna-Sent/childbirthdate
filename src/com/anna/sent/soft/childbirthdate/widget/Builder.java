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
	protected abstract boolean hasTV3();

	protected abstract boolean hasTV4();

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
			views.setViewVisibility(getTV1Id(), View.GONE);
			views.setTextViewText(getTV2Id(),
					context.getString(R.string.errorIncorrectGestationAge));
			if (hasTV3()) {
				views.setViewVisibility(getTV3Id(), View.GONE);
			}

			if (hasTV4()) {
				views.setTextViewText(getTV4Id(), context
						.getString(R.string.errorNotSelectedCalculationMethod));
			}
		} else {
			Calendar currentDate = Calendar.getInstance();
			p.setCurrentPoint(currentDate);
			if (p.isCorrect()) {
				views.setViewVisibility(getTV1Id(), View.VISIBLE);
				views.setTextViewText(getTV1Id(),
						countdown ? context.getString(R.string.widgetTextRest)
								: context.getString(R.string.widgetTextInfo));
				views.setTextViewText(getTV2Id(),
						countdown ? p.getRestInfo(context) : p.getInfo(context));
				if (hasTV3()) {
					if (countdown) {
						views.setViewVisibility(getTV3Id(), View.GONE);
					} else {
						views.setViewVisibility(getTV3Id(), View.VISIBLE);
						views.setTextViewText(getTV3Id(),
								p.getAdditionalInfo(context));
					}
				}
			} else {
				views.setViewVisibility(getTV1Id(), View.GONE);
				views.setTextViewText(getTV2Id(),
						context.getString(R.string.errorIncorrectGestationAge));
				if (hasTV3()) {
					Calendar start = p.getStartPoint(), end = p.getEndPoint();
					views.setViewVisibility(getTV3Id(), View.VISIBLE);
					if (currentDate.before(start)) {
						views.setTextViewText(
								getTV3Id(),
								context.getString(R.string.errorIncorrectCurrentDateSmaller));
					} else if (currentDate.after(end)) {
						views.setTextViewText(
								getTV3Id(),
								context.getString(R.string.errorIncorrectCurrentDateGreater));
					}
				}
			}

			if (hasTV4()) {
				views.setTextViewText(getTV4Id(), context.getString(
						R.string.widgetCalculatingMethod,
						calculatingMethodString));
			}
		}

		return views;
	}

	protected int getTV1Id() {
		return 0;
	}

	protected int getTV2Id() {
		return 0;
	}

	protected int getTV3Id() {
		return 0;
	}

	protected int getTV4Id() {
		return 0;
	}
}