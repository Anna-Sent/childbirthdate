package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

        return inflater.inflate(R.layout.details_first_movements_method,
                container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        datePicker = getActivity().findViewById(R.id.datePickerFirstMovementsDate);
        DateUtils.init(datePicker, this);
        checkBox = getActivity().findViewById(R.id.checkBoxIsFirstPregnancy);
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
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
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
