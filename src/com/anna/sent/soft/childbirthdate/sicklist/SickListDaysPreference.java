package com.anna.sent.soft.childbirthdate.sicklist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.Days;
import com.anna.sent.soft.childbirthdate.age.ISetting;
import com.anna.sent.soft.childbirthdate.preferences.MoveableItemsArrayAdapter;
import com.anna.sent.soft.childbirthdate.preferences.MoveableItemsPreference;

public class SickListDaysPreference extends MoveableItemsPreference {
	public SickListDaysPreference(Context context) {
		super(context);
	}

	public SickListDaysPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private EditText mEditText;

	@Override
	protected String getDefaultValue() {
		return SickListConstants.Days.DEFAULT_VALUE;
	}

	@Override
	protected int getAddLayoutResourceId() {
		return R.layout.dialog_sick_list_days;
	}

	@Override
	protected void setupViewAdd(View viewAdd) {
		mEditText = (EditText) viewAdd.findViewById(R.id.editTextItem);
	}

	@Override
	protected void addItem(MoveableItemsArrayAdapter adapter) {
		Days days = SickListUtils.checkDays(getContext(), mEditText,
				adapter.getValues());
		if (days != null) {
			adapter.addItem(days);
		}
	}

	@Override
	protected String saveAddValue() {
		return mEditText.getText().toString();
	}

	@Override
	protected void restoreAddValue(String value) {
		mEditText.setText(value);
	}

	@Override
	protected ISetting getElement() {
		return new Days();
	}
}