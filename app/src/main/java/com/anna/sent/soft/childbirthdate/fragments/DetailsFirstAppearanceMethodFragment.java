package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;

import java.util.Calendar;

public class DetailsFirstAppearanceMethodFragment extends DetailsFragment
        implements NumberPicker.OnValueChangeListener, DatePicker.OnDateChangedListener {
    private DatePicker datePicker;
    private NumberPicker numberPicker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_first_appearance_method, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker = view.findViewById(R.id.datePickerFirstAppearanceDate);
        numberPicker = view.findViewById(R.id.numberPickerFirstAppearanceWeeks);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DateUtils.init(datePicker, this);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS - 1);
        numberPicker.setOnValueChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        datePicker.clearFocus();
        numberPicker.clearFocus();
    }

    @Override
    protected void updateData() {
        if (mData != null) {
            DateUtils.setDate(datePicker, mData.getFirstAppearanceDate());
            numberPicker.setValue(mData.getFirstAppearanceWeeks());
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (mData != null) {
            int value = numberPicker.getValue();
            mData.setFirstAppearanceWeeks(value);

            dataChanged();
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mData != null) {
            Calendar value = DateUtils.getDate(datePicker);
            mData.setFirstAppearanceDate(value);

            dataChanged();
            log("onDateChanged " + DateUtils.toString(getActivity(), value));
        }
    }
}
