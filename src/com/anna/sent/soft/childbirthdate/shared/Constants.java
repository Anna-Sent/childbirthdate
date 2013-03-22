package com.anna.sent.soft.childbirthdate.shared;

public class Constants {
	public static final String SETTINGS_FILE = "childbirthdatesettings";

	// settings: saved user input
	public static final String EXTRA_MENSTRUAL_CYCLE_LEN = "com.anna.sent.soft.childbirthdate.menstrualcyclelen";
	public static final String EXTRA_LUTEAL_PHASE_LEN = "com.anna.sent.soft.childbirthdate.lutealphaselen";

	// calculation: saved user input
	public static final String EXTRA_BY_LAST_MENSTRUATION_DATE = "com.anna.sent.soft.childbirthdate.bylastmenstruationdate";
	public static final String EXTRA_LAST_MENSTRUATION_DATE = "com.anna.sent.soft.childbirthdate.lastmenstruationdate";
	public static final String EXTRA_BY_OVULATION_DATE = "com.anna.sent.soft.childbirthdate.byovulationdate";
	public static final String EXTRA_OVULATION_DATE = "com.anna.sent.soft.childbirthdate.ovulationdate";
	public static final String EXTRA_BY_ULTRASOUND = "com.anna.sent.soft.childbirthdate.byultrasound";
	public static final String EXTRA_ULTRASOUND_DATE = "com.anna.sent.soft.childbirthdate.ultrasounddate";
	public static final String EXTRA_WEEKS = "com.anna.sent.soft.childbirthdate.weeks";
	public static final String EXTRA_DAYS = "com.anna.sent.soft.childbirthdate.days";
	// TODO rename to COUNTING_METHOD, compatibility with old version
	public static final String EXTRA_IS_EMBRYONIC_AGE = "com.anna.sent.soft.childbirthdate.isfetal";

	// calculation: not saved user input
	public static final String EXTRA_WHAT_TO_DO = "com.anna.sent.soft.childbirthdate.whattodo";
	public static final int CALCULATE_NOTHING = 0;
	public static final int CALCULATE_EGA = 1;
	public static final int CALCULATE_ECD = 2;
	public static final String EXTRA_CURRENT_DATE = "com.anna.sent.soft.childbirthdate.currentdate";
}
