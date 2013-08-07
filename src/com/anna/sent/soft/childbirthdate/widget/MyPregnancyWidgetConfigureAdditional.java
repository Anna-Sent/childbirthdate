package com.anna.sent.soft.childbirthdate.widget;

public final class MyPregnancyWidgetConfigureAdditional extends
		MyPregnancyWidgetConfigure {
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
