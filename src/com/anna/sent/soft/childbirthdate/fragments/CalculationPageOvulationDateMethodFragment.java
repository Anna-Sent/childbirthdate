package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.shared.Shared;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

public class CalculationPageOvulationDateMethodFragment extends DetailsFragment {
	private DatePicker datePickerOvulationDate;

	public CalculationPageOvulationDateMethodFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.calculation_page_ovulation_date_method_fragment,
				container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		datePickerOvulationDate = (DatePicker) getActivity().findViewById(
				R.id.datePickerOvulationDate);
	}

	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences settings = Shared.getSettings(getActivity());
		Calendar ovulationDate = Calendar.getInstance();
		ovulationDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
				System.currentTimeMillis()));
		Utils.setDate(datePickerOvulationDate, ovulationDate);
	}

	@Override
	public void onPause() {
		super.onPause();
		Calendar ovulationDate = Utils.getDate(datePickerOvulationDate);

		SharedPreferences settings = Shared.getSettings(getActivity());
		Editor editor = settings.edit();
		editor.putLong(Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
				ovulationDate.getTimeInMillis());
		editor.commit();
	}
}
