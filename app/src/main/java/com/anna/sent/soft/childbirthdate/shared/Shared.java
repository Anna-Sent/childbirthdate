package com.anna.sent.soft.childbirthdate.shared;

public class Shared {
    private static final String PREFIX = "com.anna.sent.soft.childbirthdate.";

    public static class Titles {
        public static final String EXTRA_POSITION = PREFIX + "position";
    }

    public static class Child {
        public static final String EXTRA_IS_STARTED_FROM_WIDGET = PREFIX
                + "isstartedfromwidget";
    }

    public static class Calculation {
        public static final int METHODS_COUNT = 5;
        public static final int UNKNOWN = 0, BY_LMP = 1, BY_OVULATION = 2,
                BY_ULTRASOUND = 3, BY_FIRST_APPEARANCE = 4,
                BY_FIRST_MOVEMENTS = 5;
    }

    public static class Saved {
        public static class Calculation {
            public static final String EXTRA_BY_LMP = PREFIX
                    + "bylastmenstruationdate";
            public static final String EXTRA_LMP_MENSTRUAL_CYCLE_LEN = PREFIX
                    + "menstrualcyclelen";
            public static final String EXTRA_LMP_LUTEAL_PHASE_LEN = PREFIX
                    + "lutealphaselen";
            public static final String EXTRA_LMP_DATE = PREFIX
                    + "lastmenstruationdate";
            public static final String EXTRA_BY_OVULATION = PREFIX
                    + "byovulationdate";
            public static final String EXTRA_OVULATION_DATE = PREFIX
                    + "ovulationdate";
            public static final String EXTRA_BY_ULTRASOUND = PREFIX
                    + "byultrasound";
            public static final String EXTRA_ULTRASOUND_DATE = PREFIX
                    + "ultrasounddate";
            public static final String EXTRA_ULTRASOUND_WEEKS = PREFIX
                    + "weeks";
            public static final String EXTRA_ULTRASOUND_DAYS = PREFIX + "days";
            public static final String EXTRA_ULTRASOUND_IS_EMBRYONIC_AGE = PREFIX
                    + "isfetal";
            public static final String EXTRA_BY_FIRST_APPEARANCE = PREFIX
                    + "byfirstappearance";
            public static final String EXTRA_FIRST_APPEARANCE_DATE = PREFIX
                    + "firstappearancedate";
            public static final String EXTRA_FIRST_APPEARANCE_WEEKS = PREFIX
                    + "firstappearanceweeks";
            public static final String EXTRA_BY_FIRST_MOVEMENTS = PREFIX
                    + "byfirstmovements";
            public static final String EXTRA_FIRST_MOVEMENTS_DATE = PREFIX
                    + "firstmovementsdate";
            public static final String EXTRA_FIRST_MOVEMENTS_IS_FIRST_PREGNANCY = PREFIX
                    + "isfirstpregnancy";
        }

        public static class Widget {
            public static final String EXTRA_CALCULATION_METHOD = PREFIX
                    + "widget.calculatingmethod";
            public static final String EXTRA_COUNTDOWN = PREFIX
                    + "widget.countdown";
            public static final String EXTRA_SHOW_CALCULATION_METHOD = PREFIX
                    + "widget.showCalculatingMethod";
        }
    }
}