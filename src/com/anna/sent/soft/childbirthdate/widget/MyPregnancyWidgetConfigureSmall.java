package com.anna.sent.soft.childbirthdate.widget;

public final class MyPregnancyWidgetConfigureSmall extends
		MyPregnancyWidgetConfigure {
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