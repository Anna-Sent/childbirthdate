package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.ui.AnimatedLinearLayout;
import com.anna.sent.soft.childbirthdate.ui.LongPressedButton;
import com.anna.sent.soft.utils.ChildActivity;
import com.anna.sent.soft.utils.DateUtils;

public class ResultActivity extends ChildActivity implements OnClickListener,
		OnDateChangedListener, LongPressedButton.Listener {
	private static final String TAG = "moo";
	private static final boolean DEBUG = true;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	private void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private TableLayout table;
	private DatePicker datePicker;
	private Calendar mDate;

	@Override
	public void setViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_result);
		super.setViews(savedInstanceState);
		table = (TableLayout) findViewById(R.id.table);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		DateUtils.init(datePicker, Calendar.getInstance(), this);
		Button today = (Button) findViewById(R.id.buttonToday);
		today.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setDate(Calendar.getInstance());
			}
		});
		LongPressedButton nextDay = (LongPressedButton) findViewById(R.id.buttonNextDay);
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
			};
		});
		nextDay.setListener(this);
		LongPressedButton prevDay = (LongPressedButton) findViewById(R.id.buttonPrevDay);
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
			};
		});
		prevDay.setListener(this);
		animatedLayout = (AnimatedLinearLayout) findViewById(R.id.animatedLayout);
		textViewOnDate = (TextView) findViewById(R.id.textViewOnDate);
		buttonShowHide = (Button) findViewById(R.id.buttonShowHide);
		buttonShowHide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initDatePickerLayout();
			}
		});
		mCollapseDrawable = getDrawableFromTheme(R.attr.iconCollapse);
		mExpandDrawable = getDrawableFromTheme(R.attr.iconExpand);
		initDatePickerLayout();
	}

	private AnimatedLinearLayout animatedLayout;
	private TextView textViewOnDate;
	private Button buttonShowHide;
	private boolean isDatePickerLayoutVisible = true;

	private void initDatePickerLayout() {
		if (isDatePickerLayoutVisible) {
			animatedLayout.show();
			textViewOnDate.setText("");
			buttonShowHide.setCompoundDrawablesWithIntrinsicBounds(
					mCollapseDrawable, null, null, null);
			buttonShowHide.setContentDescription(getString(R.string.collapse));
		} else {
			animatedLayout.hide();
			textViewOnDate.setText(DateUtils.toString(this,
					DateUtils.getDate(datePicker)));
			buttonShowHide.setCompoundDrawablesWithIntrinsicBounds(
					mExpandDrawable, null, null, null);
			buttonShowHide.setContentDescription(getString(R.string.expand));
		}

		isDatePickerLayoutVisible = !isDatePickerLayoutVisible;
	}

	private Drawable mCollapseDrawable, mExpandDrawable;

	private Drawable getDrawableFromTheme(int attribute) {
		int[] attrs = new int[] { attribute };
		TypedArray ta = obtainStyledAttributes(attrs);
		Drawable result = ta.getDrawable(0);
		ta.recycle();
		return result;
	}

	@Override
	public void cancelLongPress() {
		datePicker.removeCallbacks(mChangeCurrentByOneFromLongPressCommand);
	}

	private void postChangeCurrentByOneFromLongPress(boolean increment,
			long delayMillis) {
		if (mChangeCurrentByOneFromLongPressCommand == null) {
			mChangeCurrentByOneFromLongPressCommand = new ChangeCurrentByOneFromLongPressCommand();
		} else {
			datePicker.removeCallbacks(mChangeCurrentByOneFromLongPressCommand);
		}

		mChangeCurrentByOneFromLongPressCommand.setStep(increment);
		datePicker.postDelayed(mChangeCurrentByOneFromLongPressCommand,
				delayMillis);
	}

	private ChangeCurrentByOneFromLongPressCommand mChangeCurrentByOneFromLongPressCommand = null;

	class ChangeCurrentByOneFromLongPressCommand implements Runnable {
		private boolean mIncrement;

		private void setStep(boolean increment) {
			mIncrement = increment;
		}

		@Override
		public void run() {
			changeValueByOne(mIncrement);
			datePicker.postDelayed(this, 10);
		}
	}

	private void changeValueByOne(boolean increment) {
		Calendar date = DateUtils.getDate(datePicker);
		date.add(Calendar.DAY_OF_MONTH, increment ? 1 : -1);
		setDate(date);
	}

	private void setDate(Calendar date) {
		log("setDate");
		DateUtils.setDate(datePicker, date);
		update();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDate = DateUtils.getDate(datePicker);
		table.removeAllViews();
		String[] methodNames = getResources().getStringArray(
				R.array.methodNames);
		for (int i = 0; i < getData().byMethod().length; ++i) {
			if (getData().byMethod()[i]) {
				Pregnancy pregnancy = PregnancyCalculator.Factory.get(
						getData(), i + 1);
				pregnancy.setCurrentPoint(mDate);

				View row = getLayoutInflater().inflate(R.layout.result_row,
						null);
				TextView textView = (TextView) row.findViewById(R.id.textView);
				textView.setText(methodNames[i]);

				Calendar end = pregnancy.getEndPoint();
				String res1, res2, msg;
				res2 = DateUtils.toString(this, end);
				if (pregnancy.isCorrect()) {
					res1 = pregnancy.getInfo(this);
					msg = pregnancy.getAdditionalInfo(this);
				} else {
					res1 = getString(R.string.errorIncorrectGestationalAge);
					if (pregnancy.getCurrentPoint().before(end)) {
						msg = getString(R.string.errorIncorrectCurrentDateSmaller);
					} else {
						msg = getString(R.string.errorIncorrectCurrentDateGreater);
					}
				}

				TextView result1 = (TextView) row.findViewById(R.id.result1);
				result1.setText(res1);
				TextView result2 = (TextView) row.findViewById(R.id.result2);
				result2.setText(res2);
				TextView message = (TextView) row.findViewById(R.id.message);
				message.setText(msg);

				table.addView(row);
				row.setTag(pregnancy);
				row.setOnClickListener(this);
			}
		}
	}

	@Override
	public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
		log("onDateChanged");
		update();
	}

	private void update() {
		Calendar newDate = DateUtils.getDate(datePicker);
		if (!DateUtils.areEqual(newDate, mDate)) {
			log("update " + DateUtils.toString(this, newDate));
			mDate = newDate;
			for (int i = 0; i < table.getChildCount(); ++i) {
				View row = table.getChildAt(i);

				Pregnancy pregnancy = (Pregnancy) row.getTag();
				pregnancy.setCurrentPoint(mDate);

				String res1, msg;
				if (pregnancy.isCorrect()) {
					res1 = pregnancy.getInfo(this);
					msg = pregnancy.getAdditionalInfo(this);
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
