package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

public final class MyPregnancyWidgetConfigureSimple extends MyPregnancyWidgetConfigure {
    @Override
    protected int getTitleStringResourceId() {
        return R.string.widget_simple_name;
    }

    @Override
    protected Builder getBuilder() {
        return new BuilderSimple();
    }

    @Override
    protected boolean hasCountdown() {
        return true;
    }

    @Override
    protected boolean hasShowCalculationMethod() {
        return true;
    }

    @Override
    protected Class<?> getWidgetProviderClass() {
        return MyPregnancyWidgetSimple.class;
    }
}
