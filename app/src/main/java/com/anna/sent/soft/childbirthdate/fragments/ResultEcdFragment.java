package com.anna.sent.soft.childbirthdate.fragments;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.data.Data;
import com.anna.sent.soft.childbirthdate.data.DataClient;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.ui.AnimatedLinearLayout;
import com.anna.sent.soft.childbirthdate.ui.LongPressedButton;
import com.anna.sent.soft.childbirthdate.utils.AdUtils;
import com.anna.sent.soft.childbirthdate.utils.DateUtils;
import com.anna.sent.soft.childbirthdate.utils.MyLog;
import com.anna.sent.soft.strategy.statesaver.StateSaverBaseFragment;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

public class ResultEcdFragment extends StateSaverBaseFragment implements
        DataClient, OnClickListener, OnDateChangedListener,
        LongPressedButton.Listener {
    private static final String KEY_IS_ANIMATED_ACTIVITY_VISIBLE = "isAnimatedActivityVisible";
    private static final boolean DEFAULT_VALUE_IS_ANIMATED_LAYOUT_VISIBLE = false;
    private static final boolean USE_ANIMATION = false;
    private AdView mAdView;
    private TableLayout mTable;
    private DatePicker mDatePicker;
    private Calendar mDate = null;
    private Data mData;
    private AnimatedLinearLayout animatedLayout;
    private TextView textViewOnDate;
    private Button buttonShowHide;
    private boolean mIsVisible = DEFAULT_VALUE_IS_ANIMATED_LAYOUT_VISIBLE;
    private Drawable mCollapseDrawable, mExpandDrawable;
    private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand = null;
    private View mSelectedRow = null;

    private String wrapMsg(String msg) {
        return getClass().getSimpleName() + ": " + msg;
    }

    private void log(String msg) {
        MyLog.getInstance().logcat(Log.DEBUG, wrapMsg(msg));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.result_ecd, container, false);
    }

    private Data getData() {
        return mData;
    }

    @Override
    public void setData(Data data) {
        mData = data;
    }

    @Override
    public void setViews(Bundle savedInstanceState) {
        mAdView = AdUtils.setupAd(getData(), getActivity(), R.id.adView_ecd, 200, 100);

        mTable = getActivity().findViewById(R.id.table_ecd);
        mDatePicker = getActivity().findViewById(R.id.datePicker);
        DateUtils.init(mDatePicker, Calendar.getInstance(), this);
        Button today = getActivity().findViewById(R.id.buttonToday);
        today.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setDate(Calendar.getInstance());
            }
        });
        LongPressedButton nextDay = getActivity()
                .findViewById(R.id.buttonNextDay);
        nextDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                changeValueByOne(true);
            }
        });
        nextDay.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                postChangeCurrentByOneFromLongPress(true, 0);
                return true;
            }
        });
        nextDay.setListener(this);
        LongPressedButton prevDay = getActivity()
                .findViewById(R.id.buttonPrevDay);
        prevDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                changeValueByOne(false);
            }
        });
        prevDay.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                postChangeCurrentByOneFromLongPress(false, 0);
                return true;
            }
        });
        prevDay.setListener(this);
        animatedLayout = getActivity().findViewById(R.id.animatedLayout);
        textViewOnDate = getActivity().findViewById(R.id.textViewOnDate);
        buttonShowHide = getActivity().findViewById(R.id.buttonShowHide);
        buttonShowHide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsVisible = !mIsVisible;
                setVisibility(mIsVisible, USE_ANIMATION);
            }
        });
        mCollapseDrawable = getDrawableFromTheme(R.attr.iconCollapse);
        mExpandDrawable = getDrawableFromTheme(R.attr.iconExpand);
    }

    @Override
    public void restoreState(Bundle state) {
        mIsVisible = state.getBoolean(KEY_IS_ANIMATED_ACTIVITY_VISIBLE,
                DEFAULT_VALUE_IS_ANIMATED_LAYOUT_VISIBLE);
    }

    @Override
    public void saveState(Bundle state) {
        state.putBoolean(KEY_IS_ANIMATED_ACTIVITY_VISIBLE, mIsVisible);
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsVisible = USE_ANIMATION || mIsVisible;
        setVisibility(mIsVisible, false);
    }

    private void setVisibility(boolean isVisible, boolean withAnimation) {
        if (isVisible) {
            animatedLayout.show(withAnimation);
            textViewOnDate.setText("");
            buttonShowHide.setCompoundDrawablesWithIntrinsicBounds(
                    mCollapseDrawable, null, null, null);
            buttonShowHide.setContentDescription(getString(R.string.collapse));
        } else {
            animatedLayout.hide(withAnimation);
            textViewOnDate.setText(DateUtils.toString(getActivity(),
                    DateUtils.getDate(mDatePicker)));
            buttonShowHide.setCompoundDrawablesWithIntrinsicBounds(
                    mExpandDrawable, null, null, null);
            buttonShowHide.setContentDescription(getString(R.string.expand));
        }
    }

    private Drawable getDrawableFromTheme(int attribute) {
        int[] attrs = new int[]{attribute};
        TypedArray ta = getActivity().obtainStyledAttributes(attrs);
        Drawable result = ta.getDrawable(0);
        ta.recycle();
        return result;
    }

    @Override
    public void cancelLongPress() {
        mDatePicker.removeCallbacks(mChangeCurrentByOneFromLongPressCommand);
    }

    private void postChangeCurrentByOneFromLongPress(boolean increment, long delayMillis) {
        if (mChangeCurrentByOneFromLongPressCommand == null) {
            mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
        } else {
            mDatePicker
                    .removeCallbacks(mChangeCurrentByOneFromLongPressCommand);
        }

        mChangeCurrentByOneFromLongPressCommand.setStep(increment);
        mDatePicker.postDelayed(mChangeCurrentByOneFromLongPressCommand,
                delayMillis);
    }

    private void changeValueByOne(boolean increment) {
        Calendar date = DateUtils.getDate(mDatePicker);
        date.add(Calendar.DAY_OF_MONTH, increment ? 1 : -1);
        setDate(date);
    }

    private void setDate(Calendar date) {
        log("setDate");
        DateUtils.setDate(mDatePicker, date);
        updateResults();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mAdView != null) {
            mAdView.resume();
        }

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
        if (mAdView != null) {
            mAdView.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
        log("onDateChanged");
        updateResults();
    }

    @SuppressWarnings("InflateParams")
    private void fillResults() {
        mDate = DateUtils.getDate(mDatePicker);
        mTable.removeAllViews();
        String[] methodNames = getResources().getStringArray(
                R.array.methodNames);
        for (int i = 0; i < getData().byMethod().length; ++i) {
            if (getData().byMethod()[i]) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View row = inflater.inflate(R.layout.result_row, null);
                TextView textView = row.findViewById(R.id.textView);
                textView.setText(methodNames[i]);

                Pregnancy pregnancy = PregnancyCalculator.Factory.get(
                        getData(), i + 1);

                fillRows(pregnancy, row);

                mTable.addView(row);
                row.setTag(pregnancy);
                row.setOnClickListener(this);
            }
        }
    }

    private void updateResults() {
        Calendar newDate = DateUtils.getDate(mDatePicker);
        if (mDate == null || !DateUtils.areEqual(newDate, mDate)) {
            log("update " + DateUtils.toString(getActivity(), newDate));
            mDate = newDate;
            for (int i = 0; i < mTable.getChildCount(); ++i) {
                View row = mTable.getChildAt(i);

                Pregnancy pregnancy = (Pregnancy) row.getTag();

                fillRows(pregnancy, row);
            }
        }
    }

    private void fillRows(Pregnancy pregnancy, View row) {
        pregnancy.setCurrentPoint(mDate);

        String res1, res2, msg;

        Calendar end = pregnancy.getEndPoint();
        res2 = DateUtils.toString(getActivity(), end);

        if (pregnancy.isCorrect()) {
            res1 = pregnancy.getInfo(getActivity());
            msg = pregnancy.getAdditionalInfo(getActivity());
        } else {
            res1 = getString(R.string.errorIncorrectGestationalAge);
            if (pregnancy.getCurrentPoint().before(end)) {
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
        }
    }

    private class ChangeCurrentByOneFromLongPressCommand implements Runnable {
        private boolean mIncrement;

        private void setStep(boolean increment) {
            mIncrement = increment;
        }

        @Override
        public void run() {
            changeValueByOne(mIncrement);
            mDatePicker.postDelayed(this, 10);
        }
    }
}
