package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.ResultActivity;
import com.anna.sent.soft.childbirthdate.shared.Shared;

public class TitlesHelperFragment extends Fragment implements OnClickListener {
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button buttonCalculateECD = (Button) getActivity().findViewById(
				R.id.buttonCalculateEstimatedChildbirthDate);
		buttonCalculateECD.setOnClickListener(this);
		Button buttonCalculateEGA = (Button) getActivity().findViewById(
				R.id.buttonCalculateEstimatedGestationalAge);
		buttonCalculateEGA.setOnClickListener(this);
	}

	private void calculate(int index) {
		Intent intent = new Intent(getActivity(), ResultActivity.class);
		intent.putExtra(Shared.Result.EXTRA_TAB_INDEX, index);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonCalculateEstimatedChildbirthDate:
			calculate(0);
			break;
		case R.id.buttonCalculateEstimatedGestationalAge:
			calculate(1);
			break;
		}
	}
}