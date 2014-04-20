package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.base.StateSaverFragment;
import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.childbirthdate.data.DataClient;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
import com.anna.sent.soft.utils.DateUtils;

public class ResultSickListFragment extends StateSaverFragment implements
		DataClient, NumberPicker.OnValueChangeListener, OnClickListener {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private TableLayout mTable;
	private NumberPicker mNumberPickerWeeks, mNumberPickerSickDays;

	public ResultSickListFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.result_sick_list, container, false);
		return v;
	}

	protected Data mData = null;

	@Override
	public void setData(Data data) {
		mData = data;
	}

	private Data getData() {
		return mData;
	}

	@Override
	public void setViews(Bundle savedInstanceState) {
		mNumberPickerWeeks = (NumberPicker) getActivity().findViewById(
				R.id.numberPickerWeeks);
		mNumberPickerWeeks.setMinValue(0);
		mNumberPickerWeeks
				.setMaxValue(PregnancyCalculator.GESTATIONAL_AVG_AGE_IN_WEEKS);
		mNumberPickerWeeks.setOnValueChangedListener(this);
		mNumberPickerSickDays = (NumberPicker) getActivity().findViewById(
				R.id.numberPickerSickDays);
		mNumberPickerSickDays.setMinValue(0);
		mNumberPickerSickDays.setMaxValue(200);
		mNumberPickerSickDays.setOnValueChangedListener(this);
		mTable = (TableLayout) getActivity().findViewById(R.id.table_sick_days);
	}

	@Override
	public void restoreState(Bundle state) {
	}

	@Override
	public void saveState(Bundle state) {
	}

	@Override
	public void onResume() {
		super.onResume();
		mTable.removeAllViews();
		String[] methodNames = getResources().getStringArray(
				R.array.methodNames);
		for (int i = 0; i < getData().byMethod().length; ++i) {
			if (getData().byMethod()[i]) {
				Pregnancy pregnancy = PregnancyCalculator.Factory.get(
						getData(), i + 1);
				pregnancy.setWeeks(30);

				View row = getActivity().getLayoutInflater().inflate(
						R.layout.result_row, null);
				TextView textView = (TextView) row.findViewById(R.id.textView);
				textView.setText(methodNames[i]);

				Calendar current = pregnancy.getCurrentPoint();
				String res1 = null, res2 = null, msg = null;
				if (pregnancy.isCorrect()) {
					Calendar to = (Calendar) current.clone();
					to.add(Calendar.DAY_OF_MONTH, 120);
					res2 = DateUtils.toString(getActivity(), current) + " - "
							+ DateUtils.toString(getActivity(), to);
				} else {
					res1 = getString(R.string.errorIncorrectGestationalAge);
					Calendar end = pregnancy.getEndPoint();
					if (pregnancy.getCurrentPoint().before(end)) {
						msg = getString(R.string.errorIncorrectCurrentDateSmaller);
					} else {
						msg = getString(R.string.errorIncorrectCurrentDateGreater);
					}
				}

				TextView result1 = (TextView) row.findViewById(R.id.result1);
				result1.setText(res1);
				result1.setVisibility(res1 == null ? View.GONE : View.VISIBLE);
				TextView result2 = (TextView) row.findViewById(R.id.result2);
				result2.setText(res2);
				result2.setVisibility(res2 == null ? View.GONE : View.VISIBLE);
				TextView message = (TextView) row.findViewById(R.id.message);
				message.setText(msg);
				message.setVisibility(msg == null ? View.GONE : View.VISIBLE);

				mTable.addView(row);
				row.setTag(pregnancy);
				row.setOnClickListener(this);
			}
		}
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		update();
	}

	private void update() {
		for (int i = 0; i < mTable.getChildCount(); ++i) {
			View row = mTable.getChildAt(i);

			Pregnancy pregnancy = (Pregnancy) row.getTag();

			String res1, msg;
			if (pregnancy.isCorrect()) {
				res1 = pregnancy.getInfo(getActivity());
				msg = pregnancy.getAdditionalInfo(getActivity());
			} else {
				Calendar end = pregnancy.getEndPoint();
				res1 = getString(R.string.errorIncorrectGestationalAge);
				if (pregnancy.getCurrentPoint().before(end)) {
					msg = getString(R.string.errorIncorrectCurrentDateSmaller);
				} else {
					msg = getString(R.string.errorIncorrectCurrentDateGreater);
				}
			}

			TextView result1 = (TextView) row.findViewById(R.id.result1);
			result1.setText(res1);
			TextView message = (TextView) row.findViewById(R.id.message);
			message.setText(msg);
		}
	}

	private View mSelectedRow = null;

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v instanceof TableRow) {
			if (mSelectedRow != null) {
				mSelectedRow.setBackgroundDrawable(null);
			}

			v.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_selected_view));
			mSelectedRow = v;
		}
	}
}