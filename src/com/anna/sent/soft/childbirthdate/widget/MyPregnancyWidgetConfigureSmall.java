package com.anna.sent.soft.childbirthdate.widget;

public class MyPregnancyWidgetConfigureSmall extends MyPregnancyWidgetConfigure {
	@Override
	protected Builder getBuilder() {
		return new BuilderSmall();
	}

	@Override
	protected boolean hasCountdown() {
		return false;
	}

	@Override
	protected boolean hasShowCalculatingMethod() {
		return false;
	}
}
