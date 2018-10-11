package com.anna.sent.soft.childbirthdate.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anna.sent.soft.ad.AdUtils;
import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.adapters.LocalizableSimpleSpinnerItemArrayAdapter;
import com.anna.sent.soft.childbirthdate.age.Age;
import com.anna.sent.soft.childbirthdate.age.Days;
import com.anna.sent.soft.childbirthdate.age.ISetting;
import com.anna.sent.soft.childbirthdate.age.LocalizableObject;
import com.anna.sent.soft.childbirthdate.base.CbdFragment;
import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.childbirthdate.data.DataClient;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.childbirthdate.sicklist.SickListUtils;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;
import com.anna.sent.soft.logging.MyLog;
import com.anna.sent.soft.utils.DisplayUtils;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResultSickListFragment extends CbdFragment
        implements DataClient, OnClickListener, OnItemSelectedListener {
    private static final String KEY_SPINNER_DAYS_POSITION = "key_spinner_days_position";
    private static final String KEY_SPINNER_AGE_POSITION = "key_spinner_age_position";
    private static final String KEY_OTHER_DAYS = "key_other_days";
    private static final String KEY_OTHER_AGE = "key_other_age";
    private AdView mAdView;
    private TableLayout mTable;
    private Spinner mSpinnerDays, mSpinnerAge;
    private LocalizableSimpleSpinnerItemArrayAdapter mSpinnerDaysAdapter, mSpinnerAgeAdapter;
    private int mSpinnerDaysIndex, mSpinnerAgeIndex;
    private List<Days> mTmpDaysList = new ArrayList<>();
    private List<Age> mTmpAgeList = new ArrayList<>();
    private Data mData;
    private View mSelectedRow;

    @SuppressWarnings("InflateParams")
    public static void showSickListInfoDialog(final Context context) {
        if (Settings.showSickListInfoDialog(context)) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_info, null);
            TextView textView = view.findViewById(R.id.textView);
            textView.setText(R.string.dialog_sick_list);
            final CheckBox checkBox = view.findViewById(R.id.checkBox);

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.result_sick_list, container, false);
    }

    private Data getData() {
        return mData;
    }

    @Override
    public void setData(Data data) {
        mData = data;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int initH = 300, rowH = 60;
        int count = mData.getSelectedMethodsCount();
        int expectedHeightDp = initH + count * rowH;
        int actualHeightDp = DisplayUtils.getScreenSizeInDp().y;
        boolean showAd = expectedHeightDp < actualHeightDp;
        mAdView = AdUtils.setupAd(getActivity(), R.id.adView_sick_list, R.string.adUnitId, showAd);

        //noinspection ConstantConditions
        mTable = getActivity().findViewById(R.id.table_sick_list);
        getActivity().findViewById(R.id.buttonEditDays).setOnClickListener(this);
        getActivity().findViewById(R.id.buttonEditAge).setOnClickListener(this);
        mSpinnerDays = getActivity().findViewById(R.id.spinnerDays);
        mSpinnerAge = getActivity().findViewById(R.id.spinnerAge);

        mSpinnerDaysIndex = savedInstanceState == null
                ? 0
                : savedInstanceState.getInt(KEY_SPINNER_DAYS_POSITION);
        mSpinnerAgeIndex = savedInstanceState == null
                ? 0
                : savedInstanceState.getInt(KEY_SPINNER_AGE_POSITION);

        //noinspection unchecked
        mTmpDaysList = savedInstanceState == null
                ? new ArrayList<Days>()
                : (ArrayList<Days>) savedInstanceState.getSerializable(KEY_OTHER_DAYS);
        //noinspection unchecked
        mTmpAgeList = savedInstanceState == null
                ? new ArrayList<Age>()
                : (ArrayList<Age>) savedInstanceState.getSerializable(KEY_OTHER_AGE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = mSpinnerDays.getSelectedItemPosition();
        outState.putInt(KEY_SPINNER_DAYS_POSITION, position);
        position = mSpinnerAge.getSelectedItemPosition();
        outState.putInt(KEY_SPINNER_AGE_POSITION, position);
        outState.putSerializable(KEY_OTHER_DAYS, new ArrayList<>(mTmpDaysList));
        outState.putSerializable(KEY_OTHER_AGE, new ArrayList<>(mTmpAgeList));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdView != null) {
            mAdView.resume();
        }

        setupSpinner(mSpinnerDays, Days.class);
        setupSpinner(mSpinnerAge, Age.class);

        fillResults();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAdView != null) {
            mAdView.destroy();
        }
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

    @SuppressWarnings("InflateParams")
    private void fillResults() {
        Days days = getSelectedDays();
        Age age = getSelectedAge();

        mTable.removeAllViews();

        if (age == null || days == null) {
            return;
        }

        String[] methodNames = getResources().getStringArray(R.array.methodNames);
        for (int i = 0; i < getData().byMethod().length; ++i) {
            if (getData().byMethod()[i]) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View row = inflater.inflate(R.layout.result_row, null);
                TextView textView = row.findViewById(R.id.textView);
                textView.setText(methodNames[i]);

                Pregnancy pregnancy = PregnancyCalculator.Factory.get(getData(), i + 1);
                if (pregnancy == null) {
                    MyLog.getInstance().logcat(Log.WARN, "pregnancy is null");
                    continue;
                }

                fillRows(pregnancy, age, days, row);

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

            fillRows(pregnancy, age, days, row);
        }
    }

    private void fillRows(Pregnancy pregnancy, Age age, Days days, View row) {
        pregnancy.setAge(age);

        String res1 = null, res2 = null, msg = null;

        Calendar current = pregnancy.getCurrentPoint();

        if (pregnancy.isCorrect()) {
            Calendar to = (Calendar) current.clone();
            to.add(Calendar.DAY_OF_MONTH, days.getDays() - 1);

            if (DateUtils.areEqual(current, to)) {
                res2 = DateUtils.toString(getActivity(), current);
            } else {
                res2 = DateUtils.toString(getActivity(), current) + " - "
                        + DateUtils.toString(getActivity(), to);
            }
        } else {
            res1 = getString(R.string.errorIncorrectGestationalAge);
            Calendar end = pregnancy.getEndPoint();
            if (current.before(end)) {
                msg = getString(R.string.errorIncorrectCurrentDateSmaller);
            } else {
                msg = getString(R.string.errorIncorrectCurrentDateGreater);
            }
        }

        TextView result1 = row.findViewById(R.id.result1);
        result1.setText(res1);
        result1.setVisibility(res1 == null ? View.GONE : View.VISIBLE);
        TextView result2 = row.findViewById(R.id.result2);
        result2.setText(res2);
        result2.setVisibility(res2 == null ? View.GONE : View.VISIBLE);
        TextView message = row.findViewById(R.id.message);
        message.setText(msg);
        message.setVisibility(msg == null ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TableRow) {
            if (mSelectedRow != null) {
                mSelectedRow.setBackgroundResource(0);
            }

            v.setBackgroundResource(R.drawable.bg_selected_view);
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

    @SuppressWarnings("InflateParams")
    private void editDays() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_sick_list_days, null);
        final EditText editTextItem = view.findViewById(R.id.editTextItem);

        view = wrapWithCheckBox(view);

        final CheckBox checkBox = view.findViewById(R.id.checkBox);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sick_list_days)
                .setView(view)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Days days = SickListUtils.checkDays(getActivity(),
                                        editTextItem,
                                        mSpinnerDaysAdapter.getObjects());
                                if (days != null) {
                                    mTmpDaysList.add(days);
                                    int index = mSpinnerDaysAdapter.addObject(days);
                                    mSpinnerDays.setSelection(index);
                                    mSpinnerDaysIndex = index;
                                    updateResults();

                                    if (checkBox.isChecked()) {
                                        List<LocalizableObject> list = Settings.getDays(getActivity());
                                        list.add(days);
                                        Settings.setDays(getActivity(), list);
                                    }
                                }
                            }
                        }).setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    @SuppressWarnings("InflateParams")
    private void editAge() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_sick_list_age, null);
        final NumberPicker numberPickerWeeks = view.findViewById(R.id.numberPickerWeeks);
        final NumberPicker numberPickerDays = view.findViewById(R.id.numberPickerDays);
        SickListUtils.setupAgeNumberPickers(numberPickerWeeks, numberPickerDays);

        view = wrapWithCheckBox(view);

        final CheckBox checkBox = view.findViewById(R.id.checkBox);

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
                                    int index = mSpinnerAgeAdapter.addObject(age);
                                    mSpinnerAge.setSelection(index);
                                    mSpinnerAgeIndex = index;
                                    updateResults();

                                    if (checkBox.isChecked()) {
                                        List<LocalizableObject> list = Settings.getAge(getActivity());
                                        list.add(age);
                                        Settings.setAge(getActivity(), list);
                                    }
                                }
                            }
                        }).setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    @SuppressWarnings("InflateParams")
    private View wrapWithCheckBox(View child) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_edit, null);
        ViewGroup viewGroup = view.findViewById(R.id.lastItem);
        viewGroup.addView(child,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
        return (Days) mSpinnerDays.getSelectedItem();
    }

    private Age getSelectedAge() {
        return (Age) mSpinnerAge.getSelectedItem();
    }
}
