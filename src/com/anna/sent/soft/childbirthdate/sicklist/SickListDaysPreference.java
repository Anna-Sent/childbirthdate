package com.anna.sent.soft.childbirthdate.sicklist;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
		try {
			String text = mEditText.getText().toString();

			int number = Integer.parseInt(text);

			if (number < SickListConstants.Days.MIN_VALUE
					|| number > SickListConstants.Days.MAX_VALUE) {
				Toast.makeText(
						getContext(),
						getContext().getString(R.string.error_value_bounds,
								SickListConstants.Days.MIN_VALUE,
								SickListConstants.Days.MAX_VALUE),
						Toast.LENGTH_SHORT).show();
				return;
			}

			Days days = new Days(number);

			List<Object> values = adapter.getValues();
			if (values.contains(days)) {
				Toast.makeText(
						getContext(),
						getContext().getString(
								R.string.error_value_already_exists),
						Toast.LENGTH_SHORT).show();
				return;
			}

			adapter.addItem(days);
		} catch (Exception e) {
			Toast.makeText(
					getContext(),
					getContext().getString(R.string.error_enter_value,
							SickListConstants.Days.MIN_VALUE,
							SickListConstants.Days.MAX_VALUE),
					Toast.LENGTH_SHORT).show();
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