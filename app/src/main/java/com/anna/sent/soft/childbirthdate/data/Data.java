package com.anna.sent.soft.childbirthdate.data;

import java.util.Calendar;

public interface Data {
    boolean[] byMethod();

    void byMethod(int index, boolean value);

    boolean thereIsAtLeastOneSelectedMethod();

    int getSelectedMethodsCount();

    String[] getStrings2();

    int getMenstrualCycleLen();

    void setMenstrualCycleLen(int value);

    int getLutealPhaseLen();

    void setLutealPhaseLen(int value);

    Calendar getLastMenstruationDate();

    void setLastMenstruationDate(Calendar value);

    Calendar getOvulationDate();

    void setOvulationDate(Calendar value);

    Calendar getUltrasoundDate();

    void setUltrasoundDate(Calendar value);

    int getUltrasoundWeeks();

    void setUltrasoundWeeks(int value);

    int getUltrasoundDays();

    void setUltrasoundDays(int value);

    boolean isEmbryonicAge();

    void isEmbryonicAge(boolean value);

    Calendar getFirstAppearanceDate();

    void setFirstAppearanceDate(Calendar value);

    int getFirstAppearanceWeeks();

    void setFirstAppearanceWeeks(int value);

    Calendar getFirstMovementsDate();

    void setFirstMovementsDate(Calendar value);

    boolean isFirstPregnancy();

    void isFirstPregnancy(boolean value);
}
