package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

public final class BuilderSimple extends Builder {
	@Override
	protected boolean hasTV3() {
		return false;
	}

	@Override
	protected int getWidgetLayoutId() {
		return R.layout.my_pregnancy_widget_layout_simple;
	}
}