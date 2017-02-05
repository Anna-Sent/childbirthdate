package com.anna.sent.soft.childbirthdate.sicklist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.Age;
import com.anna.sent.soft.childbirthdate.age.ISetting;
import com.anna.sent.soft.childbirthdate.preferences.MoveableItemsArrayAdapter;
import com.anna.sent.soft.childbirthdate.preferences.MoveableItemsPreference;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;

public class SickListAgePreference extends MoveableItemsPreference {
    private NumberPicker mNumberPickerWeeks, mNumberPickerDays;

    public SickListAgePreference(Context context) {
        super(context);
    }

    public SickListAgePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected String getDefaultValue() {
        return SickListConstants.Age.DEFAULT_VALUE;
    }

    protected int getAddLayoutResourceId() {
        return R.layout.dialog_sick_list_age;
    }

    protected void setupViewAdd(View viewAdd) {
        mNumberPickerWeeks = (NumberPicker) viewAdd
                .findViewById(R.id.numberPickerWeeks);
        mNumberPickerDays = (NumberPicker) viewAdd
                .findViewById(R.id.numberPickerDays);
        SickListUtils.setupAgeNumberPickers(mNumberPickerWeeks,
                mNumberPickerDays);
    }

    @Override
    protected void addItem(MoveableItemsArrayAdapter adapter) {
        Age age = SickListUtils.checkAge(getContext(), mNumberPickerWeeks,
                mNumberPickerDays, adapter.getValues());
        if (age != null) {
            adapter.addItem(age);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            mNumberPickerWeeks = null;
            mNumberPickerDays = null;
        }
    }

    @Override
    protected String saveAddValue() {
        if (mNumberPickerWeeks != null && mNumberPickerDays != null) {
            int w = mNumberPickerWeeks.getValue();
            int d = mNumberPickerDays.getValue();
            Age age = new Age(w, d);
            return age.save();
        } else {
            return null;
        }
    }

    @Override
    protected void restoreAddValue(String value) {
        if (mNumberPickerWeeks == null || mNumberPickerDays == null) {
            return;
        }

        Age age = new Age();
        age = (Age) age.load(value);

        if (age != null) {
            mNumberPickerWeeks.setValue(age.getWeeks());
            mNumberPickerDays.setValue(age.getDays());
        }
    }

    @Override
    protected ISetting getElement() {
        return new Age();
    }
}
