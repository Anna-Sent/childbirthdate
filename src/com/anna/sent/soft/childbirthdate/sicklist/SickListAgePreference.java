package com.anna.sent.soft.childbirthdate.sicklist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.preferences.MoveableItemsArrayAdapter;
import com.anna.sent.soft.childbirthdate.preferences.MoveableItemsPreference;

public class SickListAgePreference extends MoveableItemsPreference {
	public SickListAgePreference(Context context) {
		super(context);
	}

	public SickListAgePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected String getDefaultValue() {
		return SickListConstants.Days.DEFAULT_VALUE;
	}

	protected int getAddLayoutResourceId() {
		return R.layout.dialog_sick_list_age;
	}

	protected void setupViewAdd(View viewAdd) {
	}

	@Override
	protected void addItem(MoveableItemsArrayAdapter adapter) {
	}
}