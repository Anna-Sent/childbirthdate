package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.ChildActivity;
import com.anna.sent.soft.utils.DateUtils;

public class ResultActivity extends ChildActivity implements OnClickListener {
	private static final String TAG = "moo";
	@SuppressWarnings("unused")
	private static final boolean DEBUG_AD = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg, boolean scenario) {
		if (scenario) {
			Log.d(TAG, wrapMsg(msg));
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

	private TextView textView0, textView00;
	private TableLayout table;

	private int whatToDo;
	private Calendar currentDate = Calendar.getInstance();

	@Override
	public void setViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_result);
		super.setViews(savedInstanceState);

		textView0 = (TextView) findViewById(R.id.textView0);
		textView00 = (TextView) findViewById(R.id.textView00);
		table = (TableLayout) findViewById(R.id.table);

		Intent intent = getIntent();
		whatToDo = intent.getIntExtra(Shared.Result.EXTRA_WHAT_TO_DO,
				Shared.Result.Calculate.NOTHING);
		if (Shared.Result.Calculate.NOTHING == whatToDo) {
			finish();
			return;
		}

		currentDate.setTimeInMillis(intent.getLongExtra(
				Shared.Result.EXTRA_CURRENT_DATE,
				System.currentTimeMillis()));
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (getData().thereIsAtLeastOneSelectedMethod()) {
			if (Shared.Result.Calculate.ECD == whatToDo) {
				textView0.setText(getString(R.string.estimatedChildbirthDate));
				textView00.setText(getString(R.string.rememberECD));
			} else if (Shared.Result.Calculate.EGA == whatToDo) {
				textView0.setText(getString(R.string.estimatedGestationalAge,
						DateUtils.toString(this, currentDate)));
				textView00.setText(getString(R.string.rememberEGA));
			} else {
				textView0.setText("");
				textView00.setText("");
			}
		} else {
			textView0
					.setText(getString(R.string.errorNotSelectedCalculationMethod));
			textView00.setText(getString(R.string.consultADoctor));
		}

		table.removeAllViews();
		String[] methodNames = getResources().getStringArray(
				R.array.methodNames);
		for (int i = 0; i < getData().byMethod().length; ++i) {
			if (getData().byMethod()[i]) {
				Pregnancy pregnancy = PregnancyCalculator.Factory.get(
						getData(), i + 1);
				Calendar start = pregnancy.getStartPoint();
				Calendar end = pregnancy.getEndPoint();

				View row = getLayoutInflater().inflate(R.layout.result_row,
						null);
				TextView textView = (TextView) row.findViewById(R.id.textView);
				textView.setText(methodNames[i]);
				TextView result = (TextView) row.findViewById(R.id.result);
				TextView message = (TextView) row.findViewById(R.id.message);
				table.addView(row);
				row.setOnClickListener(this);

				if (whatToDo == Shared.Result.Calculate.EGA) {
					pregnancy.setCurrentPoint(currentDate);
					if (pregnancy.isCorrect()) {
						result.setText(pregnancy.getInfo(this));
						message.setText(pregnancy.getAdditionalInfo(this));
					} else {
						result.setText(getString(R.string.errorIncorrectGestationalAge));
						if (currentDate.before(start)) {
							message.setText(getString(R.string.errorIncorrectCurrentDateSmaller));
						} else if (currentDate.after(end)) {
							message.setText(getString(R.string.errorIncorrectCurrentDateGreater));
						}
					}
				} else {
					result.setText(DateUtils.toString(this, end));
					message.setVisibility(View.GONE);
				}
			}
		}
	}
}
