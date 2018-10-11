package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;

import java.util.Calendar;

public class DetailsOvulationMethodFragment extends DetailsFragment
        implements DatePicker.OnDateChangedListener {
    private DatePicker datePicker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_ovulation_method, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker = view.findViewById(R.id.datePickerOvulationDate);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DateUtils.init(datePicker, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        datePicker.clearFocus();
    }

    @Override
    protected void updateData() {
        if (mData != null) {
            DateUtils.setDate(datePicker, mData.getOvulationDate());
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mData != null) {
            Calendar value = DateUtils.getDate(datePicker);
            mData.setOvulationDate(value);

            dataChanged();
        }
    }
}
