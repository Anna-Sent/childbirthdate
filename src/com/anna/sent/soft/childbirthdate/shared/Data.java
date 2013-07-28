package com.anna.sent.soft.childbirthdate.shared;

import java.util.Calendar;

import android.content.Context;

public interface Data {
	public boolean[] byMethod();

	public boolean thereIsAtLeastOneSelectedMethod();

	public String[] getStrings2(Context context);

	public void setByMethod(int index, boolean value);

	public int getMenstrualCycleLen();

	public void setMenstrualCycleLen(int value);

	public int getLutealPhaseLen();

	public void setLutealPhaseLen(int value);

	public Calendar getLastMenstruationDate();

	public void setLastMenstruationDate(Calendar value);

	public Calendar getOvulationDate();

	public void setOvulationDate(Calendar value);

	public Calendar getUltrasoundDate();

	public void setUltrasoundDate(Calendar value);

	public int getUltrasoundWeeks();

	public void setUltrasoundWeeks(int value);

	public int getUltrasoundDays();

	public void setUltrasoundDays(int value);

	public void isEmbryonicAge(boolean value);

	public boolean isEmbryonicAge();

	public Calendar getFirstAppearanceDate();

	public void setFirstAppearanceDate(Calendar value);

	public int getFirstAppearanceWeeks();

	public void setFirstAppearanceWeeks(int value);

	public Calendar getFirstMovementsDate();

	public void setFirstMovementsDate(Calendar value);

	public boolean isFirstPregnancy();

	public void isFirstPregnancy(boolean value);
}
