package com.anna.sent.soft.childbirthdate.fragments;

import com.anna.sent.soft.childbirthdate.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalculationPageOvulationDateMethodFragment extends Fragment {

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
}
