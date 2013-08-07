package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.ResultActivity;

public class TitlesHeaderFragment extends Fragment {
	public TitlesHeaderFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.header, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button buttonCalculate = (Button) getActivity().findViewById(
				R.id.buttonCalculate);
		buttonCalculate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ResultActivity.class);
				startActivity(intent);
			}
		});
	}
}