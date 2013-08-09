package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

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
import com.anna.sent.soft.childbirthdate.ui.LongPressedButton;
import com.anna.sent.soft.utils.ChildActivity;
import com.anna.sent.soft.utils.DateUtils;

public class ResultActivity extends ChildActivity implements OnClickListener,
		OnDateChangedListener, LongPressedButton.Listener {
	private TableLayout table;
	private DatePicker datePicker;

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
		DateUtils.init(datePicker, date, this);
		update();
	}

	@Override
	protected void onResume() {
		super.onResume();
		table.removeAllViews();
		String[] methodNames = getResources().getStringArray(
				R.array.methodNames);
		for (int i = 0; i < getData().byMethod().length; ++i) {
			if (getData().byMethod()[i]) {
				Pregnancy pregnancy = PregnancyCalculator.Factory.get(
						getData(), i + 1);
				Calendar date = DateUtils.getDate(datePicker);
				pregnancy.setCurrentPoint(date);

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

				row.setTag(pregnancy);
				table.addView(row);
				row.setOnClickListener(this);
			}
		}
	}

	@Override
	public void onDateChanged(DatePicker arg0, int arg1, int arg2, int arg3) {
		update();
	}

	private void update() {
		Log.d("moo", "update");
		for (int i = 0; i < table.getChildCount(); ++i) {
			View row = table.getChildAt(i);

			Pregnancy pregnancy = (Pregnancy) row.getTag();
			Calendar date = DateUtils.getDate(datePicker);
			pregnancy.setCurrentPoint(date);

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
