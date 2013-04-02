package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

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
	protected int getLayoutId() {
		return R.layout.my_pregnancy_widget_configure_layout_simple;
	}

	@Override
	protected boolean hasShowCalculatingMethod() {
		return true;
	}
}
