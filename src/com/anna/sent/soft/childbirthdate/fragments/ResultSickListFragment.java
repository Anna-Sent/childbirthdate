package com.anna.sent.soft.childbirthdate.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.adapters.LocalizableSimpleSpinnerItemArrayAdapter;
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
import com.anna.sent.soft.childbirthdate.sicklist.SickListUtils;
import com.anna.sent.soft.numberpickerlibrary.NumberPicker;
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
	private LocalizableSimpleSpinnerItemArrayAdapter mSpinnerDaysAdapter,
			mSpinnerAgeAdapter;
	private int mSpinnerDaysIndex, mSpinnerAgeIndex;
	private ArrayList<Days> mTmpDaysList = new ArrayList<Days>();
	private ArrayList<Age> mTmpAgeList = new ArrayList<Age>();

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
	}

	private static final String KEY_SPINNER_DAYS_POSITION = "key_spinner_days_position";
	private static final String KEY_SPINNER_AGE_POSITION = "key_spinner_age_position";
	private static final String KEY_OTHER_DAYS = "key_other_days";
	private static final String KEY_OTHER_AGE = "key_other_age";

	@SuppressWarnings("unchecked")
	@Override
	public void restoreState(Bundle state) {
		mSpinnerDaysIndex = state.getInt(KEY_SPINNER_DAYS_POSITION);
		mSpinnerAgeIndex = state.getInt(KEY_SPINNER_AGE_POSITION);
		mTmpDaysList = (ArrayList<Days>) state.getSerializable(KEY_OTHER_DAYS);
		mTmpAgeList = (ArrayList<Age>) state.getSerializable(KEY_OTHER_AGE);
	}

	@Override
	public void saveState(Bundle state) {
		int position = mSpinnerDays.getSelectedItemPosition();
		state.putInt(KEY_SPINNER_DAYS_POSITION, position);
		position = mSpinnerAge.getSelectedItemPosition();
		state.putInt(KEY_SPINNER_AGE_POSITION, position);
		state.putSerializable(KEY_OTHER_DAYS, mTmpDaysList);
		state.putSerializable(KEY_OTHER_AGE, mTmpAgeList);
	}

	@Override
	public void onResume() {
		super.onResume();

		setupSpinner(mSpinnerDays, Days.class);
		setupSpinner(mSpinnerAge, Age.class);

		fillResults();
	}

	private void setupSpinner(Spinner spinner, Class<? extends ISetting> cls) {
		List<LocalizableObject> objects = Settings.getList(getActivity(), cls);

		if (cls == Days.class) {
			objects.addAll(mTmpDaysList);
		} else if (cls == Age.class) {
			objects.addAll(mTmpAgeList);
		}

		LocalizableSimpleSpinnerItemArrayAdapter adapter = new LocalizableSimpleSpinnerItemArrayAdapter(
				getActivity(), objects);

		int position = 0;
		if (cls == Days.class) {
			mSpinnerDaysAdapter = adapter;
			position = mSpinnerDaysIndex;
		} else if (cls == Age.class) {
			mSpinnerAgeAdapter = adapter;
			position = mSpinnerAgeIndex;
		}

		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		if (position >= 0 && position < objects.size()) {
			spinner.setSelection(position);
		} else {
			spinner.setSelection(0);
		}
	}

	private void fillResults() {
		Days days = getSelectedDays();
		Age age = getSelectedAge();

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

	private void updateResults() {
		Days days = getSelectedDays();
		Age age = getSelectedAge();

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

	public static void showSickListInfoDialog(final Context context) {
		if (Settings.showSickListInfoDialog(context)) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.dialog_info, null);
			TextView textView = (TextView) view.findViewById(R.id.textView);
			textView.setText(R.string.dialog_sick_list);
			final CheckBox checkBox = (CheckBox) view
					.findViewById(R.id.checkBox);

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.sick_list_days)
					.setView(view)
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (checkBox.isChecked()) {
										Settings.doNotShowSickListInfoDialog(context);
									}
								}
							});
			builder.create().show();
		}
	}

	private void editDays() {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_sick_list_days, null);
		final EditText editTextItem = (EditText) view
				.findViewById(R.id.editTextItem);

		view = wrapWithCheckBox(view);

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.sick_list_days)
				.setView(view)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Days days = SickListUtils.checkDays(
										getActivity(), editTextItem,
										mSpinnerDaysAdapter.getObjects());
								if (days != null) {
									mTmpDaysList.add(days);
									int index = mSpinnerDaysAdapter
											.addObject(days);
									mSpinnerDays.setSelection(index);
									mSpinnerDaysIndex = index;
									updateResults();
								}

								if (checkBox.isChecked()) {
									List<LocalizableObject> list = Settings
											.getDays(getActivity());
									list.add(days);
									Settings.setDays(getActivity(), list);
								}
							}
						}).setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
	}

	private void editAge() {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_sick_list_age, null);
		final NumberPicker numberPickerWeeks = (NumberPicker) view
				.findViewById(R.id.numberPickerWeeks);
		final NumberPicker numberPickerDays = (NumberPicker) view
				.findViewById(R.id.numberPickerDays);
		SickListUtils
				.setupAgeNumberPickers(numberPickerWeeks, numberPickerDays);

		view = wrapWithCheckBox(view);

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.sick_list_age)
				.setView(view)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Age age = SickListUtils.checkAge(getActivity(),
										numberPickerWeeks, numberPickerDays,
										mSpinnerAgeAdapter.getObjects());
								if (age != null) {
									mTmpAgeList.add(age);
									int index = mSpinnerAgeAdapter
											.addObject(age);
									mSpinnerAge.setSelection(index);
									mSpinnerAgeIndex = index;
									updateResults();

									if (checkBox.isChecked()) {
										List<LocalizableObject> list = Settings
												.getAge(getActivity());
										list.add(age);
										Settings.setAge(getActivity(), list);
									}
								}
							}
						}).setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
	}

	private View wrapWithCheckBox(View child) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_edit, null);
		ViewGroup viewGroup = (ViewGroup) view.findViewById(R.id.lastItem);
		viewGroup.addView(child, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == mSpinnerDays) {
			mSpinnerDaysIndex = position;
		} else if (parent == mSpinnerAge) {
			mSpinnerAgeIndex = position;
		}

		updateResults();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	private Days getSelectedDays() {
		Days days = (Days) mSpinnerDays.getSelectedItem();
		return days;
	}

	private Age getSelectedAge() {
		Age age = (Age) mSpinnerAge.getSelectedItem();
		return age;
	}
}