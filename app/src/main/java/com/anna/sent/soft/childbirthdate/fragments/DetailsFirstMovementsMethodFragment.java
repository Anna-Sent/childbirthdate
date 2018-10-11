package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;

import java.util.Calendar;

public class DetailsFirstMovementsMethodFragment extends DetailsFragment
        implements OnClickListener, DatePicker.OnDateChangedListener {
    private DatePicker datePicker;
    private CheckBox checkBox;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_first_movements_method, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        datePicker = view.findViewById(R.id.datePickerFirstMovementsDate);
        checkBox = view.findViewById(R.id.checkBoxIsFirstPregnancy);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DateUtils.init(datePicker, this);
        checkBox.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        datePicker.clearFocus();
    }

    @Override
    protected void updateData() {
        if (mData != null) {
            DateUtils.setDate(datePicker, mData.getFirstMovementsDate());
            checkBox.setChecked(mData.isFirstPregnancy());
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (mData != null) {
            Calendar value = DateUtils.getDate(datePicker);
            mData.setFirstMovementsDate(value);

            dataChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (mData != null) {
            boolean value = checkBox.isChecked();
            mData.isFirstPregnancy(value);

            dataChanged();
        }
    }
}
