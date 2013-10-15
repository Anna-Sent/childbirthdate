package com.anna.sent.soft.childbirthdate.widget;

public final class MyPregnancyWidgetConfigureSimple extends
		MyPregnancyWidgetConfigure {
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
