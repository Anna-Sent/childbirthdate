package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

public final class BuilderAdditional extends Builder {
	@Override
	protected boolean hasTV3() {
		return true;
	}

	@Override
	protected int getWidgetLayoutId() {
		return R.layout.my_pregnancy_widget_layout_additional;
	}
}