package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
import com.anna.sent.soft.utils.DateUtils;

public class DetailsFirstAppearanceMethodFragment extends DetailsFragment
		implements NumberPicker.OnValueChangeListener,
		DatePicker.OnDateChangedListener {
	private DatePicker datePicker;
	private NumberPicker numberPicker;

	public DetailsFirstAppearanceMethodFragment() {
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

		View v = inflater.inflate(R.layout.details_first_appearance_method,
				container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		datePicker = (DatePicker) getActivity().findViewById(
				R.id.datePickerFirstAppearanceDate);
		numberPicker = (NumberPicker) getActivity().findViewById(
				R.id.numberPickerFirstAppearanceWeeks);
		numberPicker.setMinValue(0);
		numberPicker
				.setMaxValue(PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS - 1);
		numberPicker.setOnValueChangedListener(this);
	}

	@Override
	protected void updateData() {
		if (mData != null) {
			DateUtils.init(datePicker, null);
			DateUtils.init(datePicker, mData.getFirstAppearanceDate(), this);
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
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if (mData != null) {
			Calendar value = DateUtils.getDate(datePicker);
			mData.setFirstAppearanceDate(value);

			dataChanged();
		}
	}
}
