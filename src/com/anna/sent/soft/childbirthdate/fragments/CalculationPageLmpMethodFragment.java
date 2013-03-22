package com.anna.sent.soft.childbirthdate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anna.sent.soft.childbirthdate.R;

public class CalculationPageLmpMethodFragment extends Fragment {

	public CalculationPageLmpMethodFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.calculation_page_lmp_method_fragment,
						container, false);
		return v;
	}
}
