package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
                             Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist. The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed. Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        return inflater.inflate(R.layout.details_ultrasound_method, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //noinspection ConstantConditions
        textViewDiagnosedAge = getActivity().findViewById(R.id.textViewDiagnosedAge);

        datePicker = getActivity().findViewById(R.id.datePickerUltrasoundDate);
        DateUtils.init(datePicker, this);

        radioButtonIsGestationalAge = getActivity().findViewById(R.id.radioIsGestationalAge);
        radioButtonIsGestationalAge.setOnClickListener(this);
        radioButtonIsEmbryonicAge = getActivity().findViewById(R.id.radioIsEmbryonicAge);
        radioButtonIsEmbryonicAge.setOnClickListener(this);

        numberPickerWeeks = getActivity().findViewById(R.id.numberPickerUltrasoundWeeks);
        numberPickerWeeks.setMinValue(0);
        numberPickerWeeks.setOnValueChangedListener(this);

        numberPickerDays = getActivity().findViewById(R.id.numberPickerUltrasoundDays);
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
