package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

public final class MyPregnancyWidgetConfigureSmall extends
		MyPregnancyWidgetConfigure {
	@Override
	protected int getTitleStringResourceId() {
		return R.string.widget_small_name;
	}

	@Override
	protected Builder getBuilder() {
		return new BuilderSmall();
	}

	@Override
	protected boolean hasCountdown() {
		return false;
	}

	@Override
	protected boolean hasShowCalculationMethod() {
		return false;
	}

	@Override
	protected Class<?> getWidgetProviderClass() {
		return MyPregnancyWidgetSmall.class;
	}
}