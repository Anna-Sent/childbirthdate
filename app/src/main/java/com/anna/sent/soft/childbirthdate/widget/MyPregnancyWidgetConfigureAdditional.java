package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

public final class MyPregnancyWidgetConfigureAdditional extends
        MyPregnancyWidgetConfigure {
    @Override
    protected int getTitleStringResourceId() {
        return R.string.widget_additional_name;
    }

    @Override
    protected Builder getBuilder() {
        return new BuilderAdditional();
    }

    @Override
    protected boolean hasCountdown() {
        return false;
    }

    @Override
    protected boolean hasShowCalculationMethod() {
        return true;
    }

    @Override
    protected Class<?> getWidgetProviderClass() {
        return MyPregnancyWidgetAdditional.class;
    }
}