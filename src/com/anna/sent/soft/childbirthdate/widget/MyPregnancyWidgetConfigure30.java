package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

public final class MyPregnancyWidgetConfigure30 extends MyPregnancyWidgetConfigure {
	@Override
	protected Builder getBuilder() {
		return new Builder30();
	}

	@Override
	protected boolean hasCountdown() {
		return false;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.my_pregnancy_widget_configure_layout_30;
	}
}
