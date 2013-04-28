package com.anna.sent.soft.childbirthdate.shared;

import java.util.Calendar;

public interface Data {
	public boolean byLmp();

	public boolean byOvulation();

	public boolean byUltrasound();

	public boolean[] byMethod();

	public boolean isEmbryonicAge();

	public Calendar getLastMenstruationDate();

	public Calendar getOvulationDate();

	public Calendar getUltrasoundDate();

	public int getMenstrualCycleLen();

	public int getLutealPhaseLen();

	public int getWeeks();

	public int getDays();

	public void setByMethod(int index, boolean value);

	public void setIsEmbryonicAge(boolean value);

	public void setLastMenstruationDate(Calendar value);

	public void setOvulationDate(Calendar value);

	public void setUltrasoundDate(Calendar value);

	public void setMenstrualCycleLen(int value);

	public void setLutealPhaseLen(int value);

	public void setWeeks(int value);

	public void setDays(int value);
}
