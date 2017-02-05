package com.anna.sent.soft.childbirthdate.sicklist;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.Age;
import com.anna.sent.soft.childbirthdate.age.Days;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;

import java.util.List;

public class SickListUtils {
    public static Days checkDays(Context context, EditText editText,
                                 List<LocalizableObject> values) {
        try {
            String text = editText.getText().toString();

            int number = Integer.parseInt(text);

            if (number < SickListConstants.Days.MIN_VALUE
                    || number > SickListConstants.Days.MAX_VALUE) {
                Toast.makeText(
                        context,
                        context.getString(R.string.error_value_bounds,
                                SickListConstants.Days.MIN_VALUE,
                                SickListConstants.Days.MAX_VALUE),
                        Toast.LENGTH_SHORT).show();
                return null;
            }

            Days days = new Days(number);

            // TODO не сохраняется в настройки, если попытка добавить второй раз
            if (values.contains(days)) {
                Toast.makeText(context,
                        context.getString(R.string.error_value_already_exists),
                        Toast.LENGTH_SHORT).show();
                return null;
            }

            return days;
        } catch (Exception e) {
            Toast.makeText(
                    context,
                    context.getString(R.string.error_enter_value,
                            SickListConstants.Days.MIN_VALUE,
                            SickListConstants.Days.MAX_VALUE),
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static Age checkAge(Context context, NumberPicker numberPickerWeeks,
                               NumberPicker numberPickerDays, List<LocalizableObject> values) {
        numberPickerWeeks.clearFocus();
        numberPickerDays.clearFocus();
        try {
            int w = numberPickerWeeks.getValue();
            int d = numberPickerDays.getValue();
            Age age = new Age(w, d);

            // TODO не сохраняется в настройки, если попытка добавить второй раз
            if (values.contains(age)) {
                Toast.makeText(context,
                        context.getString(R.string.error_value_already_exists),
                        Toast.LENGTH_SHORT).show();
                return null;
            }

            return age;
        } catch (Exception e) {
            Toast.makeText(context,
                    context.getString(R.string.errorIncorrectGestationalAge),
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static void setupAgeNumberPickers(NumberPicker numberPickerWeeks,
                                             NumberPicker numberPickerDays) {
        numberPickerWeeks.setMinValue(0);
        numberPickerWeeks
                .setMaxValue(PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS - 1);
        numberPickerDays.setMinValue(0);
        numberPickerDays.setMaxValue(Age.DAYS_IN_WEEK - 1);
    }
}
