package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;

public class DetailsLmpMethodFragment extends DetailsFragment implements
        OnClickListener, NumberPicker.OnValueChangeListener,
        DatePicker.OnDateChangedListener {
    private DatePicker datePicker;
    private NumberPicker numberPickerMcl, numberPickerLpl;

    public DetailsLmpMethodFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        return inflater
                .inflate(R.layout.details_lmp_method, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        datePicker = (DatePicker) getActivity().findViewById(
                R.id.datePickerLastMenstruationDate);
        DateUtils.init(datePicker, this);

        numberPickerMcl = (NumberPicker) getActivity().findViewById(
                R.id.numberPickerMenstrualCycleLen);
        numberPickerMcl
                .setMinValue(PregnancyCalculator.MIN_MENSTRUAL_CYCLE_LEN);
        numberPickerMcl
                .setMaxValue(PregnancyCalculator.MAX_MENSTRUAL_CYCLE_LEN);
        numberPickerMcl.setOnValueChangedListener(this);

        numberPickerLpl = (NumberPicker) getActivity().findViewById(
                R.id.numberPickerLutealPhaseLen);
        numberPickerLpl.setMinValue(PregnancyCalculator.MIN_LUTEAL_PHASE_LEN);
        numberPickerLpl.setMaxValue(PregnancyCalculator.MAX_LUTEAL_PHASE_LEN);
        numberPickerLpl.setOnValueChangedListener(this);

        Button button = (Button) getActivity().findViewById(
                R.id.buttonRestoreDefaultValues);
        button.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        datePicker.clearFocus();
        numberPickerMcl.clearFocus();
        numberPickerLpl.clearFocus();
    }

    private void restoreDefaultValues() {
        if (mData != null) {
            int menstrualCycleLen = PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH;
            int lutealPhaseLen = PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH;

            numberPickerMcl.setValue(menstrualCycleLen);
            mData.setMenstrualCycleLen(menstrualCycleLen);

            numberPickerLpl.setValue(lutealPhaseLen);
            mData.setLutealPhaseLen(lutealPhaseLen);

            dataChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRestoreDefaultValues:
                restoreDefaultValues();
                break;
        }
    }

    @Override
    protected void updateData() {
        if (mData != null) {
            DateUtils.setDate(datePicker, mData.getLastMenstruationDate());

            numberPickerMcl.setValue(mData.getMenstrualCycleLen());
            numberPickerLpl.setValue(mData.getLutealPhaseLen());
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (mData != null) {
            if (picker.getId() == R.id.numberPickerMenstrualCycleLen) {
                int menstrualCycleLen = numberPickerMcl.getValue();
                mData.setMenstrualCycleLen(menstrualCycleLen);
            } else if (picker.getId() == R.id.numberPickerLutealPhaseLen) {
                int lutealPhaseLen = numberPickerLpl.getValue();
                mData.setLutealPhaseLen(lutealPhaseLen);
            }

            dataChanged();
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        if (mData != null) {
            Calendar value = DateUtils.getDate(datePicker);
            mData.setLastMenstruationDate(value);

            dataChanged();
        }
    }
}