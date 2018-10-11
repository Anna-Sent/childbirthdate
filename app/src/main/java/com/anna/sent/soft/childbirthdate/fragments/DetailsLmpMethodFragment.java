package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;

import java.util.Calendar;

public class DetailsLmpMethodFragment extends DetailsFragment
        implements OnClickListener, NumberPicker.OnValueChangeListener, DatePicker.OnDateChangedListener {
    private DatePicker datePicker;
    private NumberPicker numberPickerMcl, numberPickerLpl;
    private Button button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_lmp_method, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker = view.findViewById(R.id.datePickerLastMenstruationDate);
        numberPickerMcl = view.findViewById(R.id.numberPickerMenstrualCycleLen);
        numberPickerLpl = view.findViewById(R.id.numberPickerLutealPhaseLen);
        button = view.findViewById(R.id.buttonRestoreDefaultValues);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DateUtils.init(datePicker, this);

        numberPickerMcl.setMinValue(PregnancyCalculator.MIN_MENSTRUAL_CYCLE_LEN);
        numberPickerMcl.setMaxValue(PregnancyCalculator.MAX_MENSTRUAL_CYCLE_LEN);
        numberPickerMcl.setOnValueChangedListener(this);

        numberPickerLpl.setMinValue(PregnancyCalculator.MIN_LUTEAL_PHASE_LEN);
        numberPickerLpl.setMaxValue(PregnancyCalculator.MAX_LUTEAL_PHASE_LEN);
        numberPickerLpl.setOnValueChangedListener(this);

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
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mData != null) {
            Calendar value = DateUtils.getDate(datePicker);
            mData.setLastMenstruationDate(value);

            dataChanged();
        }
    }
}
