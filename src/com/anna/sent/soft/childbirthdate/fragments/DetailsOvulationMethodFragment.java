package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.utils.DateUtils;

public class DetailsOvulationMethodFragment extends DetailsFragment implements
		DatePicker.OnDateChangedListener {
	private DatePicker datePickerOvulationDate;

	public DetailsOvulationMethodFragment() {
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

		View v = inflater.inflate(R.layout.details_ovulation_method, container,
				false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		datePickerOvulationDate = (DatePicker) getActivity().findViewById(
				R.id.datePickerOvulationDate);
	}

	@Override
	protected void updateData() {
		if (mData != null) {
			DateUtils.init(datePickerOvulationDate, null);
			DateUtils.init(datePickerOvulationDate, mData.getOvulationDate(),
					this);
		}
	}

	@Override
	public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
		if (mData != null) {
			Calendar ovulationDate = DateUtils.getDate(datePickerOvulationDate);
			mData.setOvulationDate(ovulationDate);

			dataChanged();
		}
	}
}
