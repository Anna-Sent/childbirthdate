package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

public class MyPregnancyWidgetConfigureSmall extends MyPregnancyWidgetConfigure {
	@Override
	protected int getLayoutId() {
		return R.layout.my_pregnancy_widget_configure_layout_small;
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
	protected boolean hasShowCalculatingMethod() {
		return false;
	}
}
