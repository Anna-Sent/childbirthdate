package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.age.Age;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;

import java.util.Calendar;

public class DetailsUltrasoundMethodFragment extends DetailsFragment
        implements OnClickListener, NumberPicker.OnValueChangeListener, DatePicker.OnDateChangedListener {
    private TextView textViewDiagnosedAge;
    private NumberPicker numberPickerWeeks, numberPickerDays;
    private DatePicker datePicker;
    private RadioButton radioButtonIsGestationalAge, radioButtonIsEmbryonicAge;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_ultrasound_method, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewDiagnosedAge = view.findViewById(R.id.textViewDiagnosedAge);
        datePicker = view.findViewById(R.id.datePickerUltrasoundDate);
        radioButtonIsGestationalAge = view.findViewById(R.id.radioIsGestationalAge);
        radioButtonIsEmbryonicAge = view.findViewById(R.id.radioIsEmbryonicAge);
        numberPickerWeeks = view.findViewById(R.id.numberPickerUltrasoundWeeks);
        numberPickerDays = view.findViewById(R.id.numberPickerUltrasoundDays);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DateUtils.init(datePicker, this);

        radioButtonIsGestationalAge.setOnClickListener(this);
        radioButtonIsEmbryonicAge.setOnClickListener(this);

        numberPickerWeeks.setMinValue(0);
        numberPickerWeeks.setOnValueChangedListener(this);

        numberPickerDays.setMinValue(0);
        numberPickerDays.setMaxValue(Age.DAYS_IN_WEEK - 1);
        numberPickerDays.setOnValueChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        datePicker.clearFocus();
        numberPickerWeeks.clearFocus();
        numberPickerDays.clearFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioIsEmbryonicAge:
            case R.id.radioIsGestationalAge:
                if (mData != null) {
                    boolean isEmbryonicAge = radioButtonIsEmbryonicAge.isChecked();
                    mData.isEmbryonicAge(isEmbryonicAge);

                    int previous = numberPickerWeeks.getValue();
                    setMaxWeeks();
                    int current = numberPickerWeeks.getValue();
                    if (current != previous) {
                        mData.setUltrasoundWeeks(current);
                    }

                    dataChanged();
                }

                break;
        }
    }

    @Override
    protected void updateData() {
        if (mData != null) {
            DateUtils.setDate(datePicker, mData.getUltrasoundDate());

            if (mData.isEmbryonicAge()) {
                radioButtonIsEmbryonicAge.setChecked(true);
            } else {
                radioButtonIsGestationalAge.setChecked(true);
            }

            setMaxWeeks();

            numberPickerWeeks.setValue(mData.getUltrasoundWeeks());
            numberPickerDays.setValue(mData.getUltrasoundDays());
        }
    }

    private void setMaxWeeks() {
        boolean isEmbryonicAge = radioButtonIsEmbryonicAge.isChecked();
        textViewDiagnosedAge.setText(isEmbryonicAge
                ? R.string.diagnosedEmbryonicAge
                : R.string.diagnosedGestationalAge);
        numberPickerWeeks.setMaxValue((isEmbryonicAge
                ? PregnancyCalculator.EMBRYONIC_AVG_AGE_IN_WEEKS
                : PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS) - 1);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (mData != null) {
            if (picker.getId() == R.id.numberPickerUltrasoundWeeks) {
                int weeks = numberPickerWeeks.getValue();
                mData.setUltrasoundWeeks(weeks);
            } else if (picker.getId() == R.id.numberPickerUltrasoundDays) {
                int days = numberPickerDays.getValue();
                mData.setUltrasoundDays(days);
            }

            dataChanged();
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mData != null) {
            Calendar ultrasoundDate = DateUtils.getDate(datePicker);
            mData.setUltrasoundDate(ultrasoundDate);

            dataChanged();
        }
    }
}
