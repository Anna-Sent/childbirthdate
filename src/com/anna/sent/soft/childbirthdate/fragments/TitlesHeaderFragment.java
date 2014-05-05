package com.anna.sent.soft.childbirthdate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.ResultsActivity;
import com.anna.sent.soft.childbirthdate.SickListActivity;
import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.childbirthdate.data.DataClient;

public class TitlesHeaderFragment extends Fragment implements DataClient,
		OnClickListener {
	public TitlesHeaderFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.titles_header, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button buttonCalculate = (Button) getActivity().findViewById(
				R.id.buttonCalculate);
		buttonCalculate.setOnClickListener(this);
		Button buttonSickList = (Button) getActivity().findViewById(
				R.id.buttonSickList);
		buttonSickList.setOnClickListener(this);
	}

	private Data mData = null;

	@Override
	public void setData(Data data) {
		mData = data;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonCalculate:
			if (mData != null && mData.thereIsAtLeastOneSelectedMethod()) {
				Intent intent = new Intent(getActivity(), ResultsActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(getActivity(),
						getString(R.string.errorNotSelectedCalculationMethod),
						Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.buttonSickList:
			Intent intent = new Intent(getActivity(), SickListActivity.class);
			startActivity(intent);
			break;
		}
	}
}