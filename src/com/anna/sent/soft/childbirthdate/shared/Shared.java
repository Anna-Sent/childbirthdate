package com.anna.sent.soft.childbirthdate.shared;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared {
	private static final String SETTINGS_FILE = "childbirthdatesettings";

	public static SharedPreferences getSettings(Context context) {
		return context.getApplicationContext().getSharedPreferences(
				SETTINGS_FILE, Context.MODE_PRIVATE);
	}

	public static class Saved {
		public static class Settings {
			public static final String EXTRA_MENSTRUAL_CYCLE_LEN = "com.anna.sent.soft.childbirthdate.menstrualcyclelen";
			public static final String EXTRA_LUTEAL_PHASE_LEN = "com.anna.sent.soft.childbirthdate.lutealphaselen";
		}

		public static class Calculation {
			public static final String EXTRA_BY_LAST_MENSTRUATION_DATE = "com.anna.sent.soft.childbirthdate.bylastmenstruationdate";
			public static final String EXTRA_LAST_MENSTRUATION_DATE = "com.anna.sent.soft.childbirthdate.lastmenstruationdate";
			public static final String EXTRA_BY_OVULATION_DATE = "com.anna.sent.soft.childbirthdate.byovulationdate";
			public static final String EXTRA_OVULATION_DATE = "com.anna.sent.soft.childbirthdate.ovulationdate";
			public static final String EXTRA_BY_ULTRASOUND = "com.anna.sent.soft.childbirthdate.byultrasound";
			public static final String EXTRA_ULTRASOUND_DATE = "com.anna.sent.soft.childbirthdate.ultrasounddate";
			public static final String EXTRA_WEEKS = "com.anna.sent.soft.childbirthdate.weeks";
			public static final String EXTRA_DAYS = "com.anna.sent.soft.childbirthdate.days";
			public static final String EXTRA_IS_EMBRYONIC_AGE = "com.anna.sent.soft.childbirthdate.isfetal";
		}

		public static class Widget {
			public static final String EXTRA_CALCULATING_METHOD = "com.anna.sent.soft.childbirthdate.widget.calculatingmethod";

			public static class Calculate {
				public static final int UNKNOWN = 0, BY_LMP = 1,
						BY_OVULATION_DAY = 2, BY_ULTRASOUND = 3;
			}

			public static final String EXTRA_COUNTDOWN = "com.anna.sent.soft.childbirthdate.widget.countdown";
			public static final String EXTRA_SHOW_CALCULATING_METHOD = "com.anna.sent.soft.childbirthdate.widget.showCalculatingMethod";
		}
	}

	public static class ResultParam {
		public static final String EXTRA_WHAT_TO_DO = "com.anna.sent.soft.childbirthdate.whattodo";

		public static class Calculate {
			public static final int NOTHING = 0, EGA = 1, ECD = 2;
		}

		public static final String EXTRA_CURRENT_DATE = "com.anna.sent.soft.childbirthdate.currentdate";

		public static final String EXTRA_MAIN_ACTIVITY_STATE = "com.anna.sent.soft.childbirthdate.mainactivitystate";
	}
}
