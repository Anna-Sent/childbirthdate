package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

public final class MyPregnancyWidgetConfigure04 extends MyPregnancyWidgetConfigure {
	@Override
	protected Builder getBuilder() {
		return new Builder04();
	}

	@Override
	protected boolean hasCountdown() {
		return true;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.my_pregnancy_widget_configure_layout_04;
	}
}
