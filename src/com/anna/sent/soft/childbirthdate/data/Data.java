package com.anna.sent.soft.childbirthdate.data;

import java.util.Calendar;

public interface Data {
	public boolean[] byMethod();

	public void byMethod(int index, boolean value);

	public boolean thereIsAtLeastOneSelectedMethod();

	public int getSelectedMethodsCount();

	public String[] getStrings2();

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

	public boolean isEmbryonicAge();

	public void isEmbryonicAge(boolean value);

	public Calendar getFirstAppearanceDate();

	public void setFirstAppearanceDate(Calendar value);

	public int getFirstAppearanceWeeks();

	public void setFirstAppearanceWeeks(int value);

	public Calendar getFirstMovementsDate();

	public void setFirstMovementsDate(Calendar value);

	public boolean isFirstPregnancy();

	public void isFirstPregnancy(boolean value);
}