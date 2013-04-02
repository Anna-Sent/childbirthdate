package com.anna.sent.soft.childbirthdate.widget;

import com.anna.sent.soft.childbirthdate.R;

public final class Builder04 extends Builder {
	@Override
	protected boolean hasTV3() {
		return false;
	}

	@Override
	protected boolean hasTV4() {
		return true;
	}

	@Override
	protected int getWidgetLayoutId() {
		return R.layout.my_pregnancy_widget_configure_layout_04;
	}

	@Override
	protected int getTV1Id() {
		return R.id.tv041;
	}

	@Override
	protected int getTV2Id() {
		return R.id.tv042;
	}

	@Override
	protected int getTV4Id() {
		return R.id.tv044;
	}
}