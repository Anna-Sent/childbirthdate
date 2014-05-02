package com.anna.sent.soft.childbirthdate.sicklist;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.Age;
import com.anna.sent.soft.childbirthdate.age.ISetting;
import com.anna.sent.soft.childbirthdate.preferences.MoveableItemsArrayAdapter;
import com.anna.sent.soft.childbirthdate.preferences.MoveableItemsPreference;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;

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

	private NumberPicker mNumberPickerWeeks, mNumberPickerDays;

	protected void setupViewAdd(View viewAdd) {
		mNumberPickerWeeks = (NumberPicker) viewAdd
				.findViewById(R.id.numberPickerWeeks);
		mNumberPickerWeeks.setMinValue(0);
		mNumberPickerWeeks
				.setMaxValue(PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS - 1);
		mNumberPickerDays = (NumberPicker) viewAdd
				.findViewById(R.id.numberPickerDays);
		mNumberPickerDays.setMinValue(0);
		mNumberPickerDays.setMaxValue(Age.DAYS_IN_WEEK - 1);
	}

	@Override
	protected void addItem(MoveableItemsArrayAdapter adapter) {
		try {
			int w = mNumberPickerWeeks.getValue();
			int d = mNumberPickerDays.getValue();
			Age age = new Age(w, d);

			List<Object> values = adapter.getValues();
			if (values.contains(age)) {
				Toast.makeText(
						getContext(),
						getContext().getString(
								R.string.error_value_already_exists),
						Toast.LENGTH_SHORT).show();
				return;
			}

			adapter.addItem(age);
		} catch (Exception e) {
			Toast.makeText(
					getContext(),
					getContext().getString(
							R.string.errorIncorrectGestationalAge),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected String saveAddValue() {
		int w = mNumberPickerWeeks.getValue();
		int d = mNumberPickerDays.getValue();
		Age age = new Age(w, d);
		return age.save();
	}

	@Override
	protected void restoreAddValue(String value) {
		Age age = new Age();
		age = (Age) age.load(value);
		mNumberPickerWeeks.setValue(age.getWeeks());
		mNumberPickerDays.setValue(age.getDays());
	}

	@Override
	protected ISetting getElement() {
		return new Age();
	}
}