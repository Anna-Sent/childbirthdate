package com.anna.sent.soft.childbirthdate.fragments;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.adapters.HackedLocalizableSimpleSpinnerItemArrayAdapter;
import com.anna.sent.soft.childbirthdate.age.Age;
import com.anna.sent.soft.childbirthdate.age.Days;
import com.anna.sent.soft.childbirthdate.age.ISetting;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;
import com.anna.sent.soft.childbirthdate.base.StateSaverFragment;
import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.childbirthdate.data.DataClient;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.utils.DateUtils;

public class ResultSickListFragment extends StateSaverFragment implements
		DataClient, OnClickListener, OnItemSelectedListener {
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
	private Spinner mSpinnerDays, mSpinnerAge;
	private HackedLocalizableSimpleSpinnerItemArrayAdapter mSpinnerDaysAdapter,
			mSpinnerAgeAdapter;
	private Days mOtherDays;
	private Age mOtherAge;

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
		mTable = (TableLayout) getActivity().findViewById(R.id.table_sick_days);
		getActivity().findViewById(R.id.buttonEditDays)
				.setOnClickListener(this);
		getActivity().findViewById(R.id.buttonEditAge).setOnClickListener(this);
		mSpinnerDays = (Spinner) getActivity().findViewById(R.id.spinnerDays);
		mSpinnerAge = (Spinner) getActivity().findViewById(R.id.spinnerAge);
		setupSpinners();
	}

	private void setupSpinners() {
		mSpinnerDaysAdapter = setupSpinner(mSpinnerDays, Days.class);
		mSpinnerAgeAdapter = setupSpinner(mSpinnerAge, Age.class);
		mSpinnerDaysAdapter.setSelectedObject(mOtherDays);
		mSpinnerAgeAdapter.setSelectedObject(mOtherAge);
	}

	private HackedLocalizableSimpleSpinnerItemArrayAdapter setupSpinner(
			Spinner spinner, Class<? extends ISetting> cls) {
		List<LocalizableObject> objects = Settings.getList(getActivity(), cls);
		HackedLocalizableSimpleSpinnerItemArrayAdapter adapter = new HackedLocalizableSimpleSpinnerItemArrayAdapter(
				getActivity(), objects);

		int position = spinner.getSelectedItemPosition();

		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		if (position >= 0 && position < objects.size()) {
			spinner.setSelection(position);
		} else {
			spinner.setSelection(0);
		}

		return adapter;
	}

	private static final String KEY_SPINNER_DAYS_POSITION = "key_spinner_days_position";
	private static final String KEY_SPINNER_AGE_POSITION = "key_spinner_age_position";
	private static final String KEY_OTHER_DAYS = "key_other_days";
	private static final String KEY_OTHER_AGE = "key_other_age";

	@Override
	public void restoreState(Bundle state) {
		int position = state.getInt(KEY_SPINNER_DAYS_POSITION);
		mSpinnerDays.setSelection(position);
		position = state.getInt(KEY_SPINNER_AGE_POSITION);
		mSpinnerAge.setSelection(position);
		mOtherDays = (Days) state.getSerializable(KEY_OTHER_DAYS);
		mSpinnerDaysAdapter.setSelectedObject(mOtherDays);
		mOtherAge = (Age) state.getSerializable(KEY_OTHER_AGE);
		mSpinnerAgeAdapter.setSelectedObject(mOtherAge);
	}

	@Override
	public void saveState(Bundle state) {
		int position = mSpinnerDays.getSelectedItemPosition();
		state.putInt(KEY_SPINNER_DAYS_POSITION, position);
		position = mSpinnerAge.getSelectedItemPosition();
		state.putInt(KEY_SPINNER_AGE_POSITION, position);
		state.putSerializable(KEY_OTHER_DAYS, mOtherDays);
		state.putSerializable(KEY_OTHER_AGE, mOtherAge);
	}

	@Override
	public void onResume() {
		super.onResume();

		setupSpinners();

		Days days = getSelectedDays();
		Age age = getSelectedAge();
		fillResults(age, days);
	}

	private void fillResults(Age age, Days days) {
		mTable.removeAllViews();

		if (age == null || days == null) {
			return;
		}

		String[] methodNames = getResources().getStringArray(
				R.array.methodNames);
		for (int i = 0; i < getData().byMethod().length; ++i) {
			if (getData().byMethod()[i]) {
				Pregnancy pregnancy = PregnancyCalculator.Factory.get(
						getData(), i + 1);
				pregnancy.setAge(age);

				View row = getActivity().getLayoutInflater().inflate(
						R.layout.result_row, null);
				TextView textView = (TextView) row.findViewById(R.id.textView);
				textView.setText(methodNames[i]);

				Calendar current = pregnancy.getCurrentPoint();
				String res1 = null, res2 = null, msg = null;
				if (pregnancy.isCorrect()) {
					Calendar to = (Calendar) current.clone();
					to.add(Calendar.DAY_OF_MONTH, days.getDays());
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

	private void updateResults(Age age, Days days) {
		if (age == null || days == null) {
			mTable.removeAllViews();
			return;
		}

		for (int i = 0; i < mTable.getChildCount(); ++i) {
			View row = mTable.getChildAt(i);

			Pregnancy pregnancy = (Pregnancy) row.getTag();
			pregnancy.setAge(age);

			Calendar current = pregnancy.getCurrentPoint();
			String res1 = null, res2 = null, msg = null;
			if (pregnancy.isCorrect()) {
				Calendar to = (Calendar) current.clone();
				to.add(Calendar.DAY_OF_MONTH, days.getDays());
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
		} else {
			switch (v.getId()) {
			case R.id.buttonEditDays:
				editDays();
				break;
			case R.id.buttonEditAge:
				editAge();
				break;
			}
		}
	}

	private void editDays() {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_sick_list_days, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(null)
				.setView(view)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mOtherDays = new Days(120);
								mSpinnerDaysAdapter
										.setSelectedObject(mOtherDays);
								Days days = getSelectedDays();
								Age age = getSelectedAge();
								updateResults(age, days);
							}
						}).setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
	}

	private void editAge() {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_sick_list_age, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(null)
				.setView(view)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mOtherAge = new Age(32, 1);
								mSpinnerAgeAdapter.setSelectedObject(mOtherAge);
								Days days = getSelectedDays();
								Age age = getSelectedAge();
								updateResults(age, days);
							}
						}).setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == mSpinnerDays) {
			mOtherDays = null;
			mSpinnerDaysAdapter.setSelectedObject(null);
		} else if (parent == mSpinnerAge) {
			mOtherAge = null;
			mSpinnerAgeAdapter.setSelectedObject(null);
		}

		Days days = getSelectedDays();
		Age age = getSelectedAge();
		updateResults(age, days);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	private Days getSelectedDays() {
		Days days = mOtherDays == null ? (Days) mSpinnerDays.getSelectedItem()
				: mOtherDays;
		return days;
	}

	private Age getSelectedAge() {
		Age age = mOtherAge == null ? (Age) mSpinnerAge.getSelectedItem()
				: mOtherAge;
		return age;
	}
}