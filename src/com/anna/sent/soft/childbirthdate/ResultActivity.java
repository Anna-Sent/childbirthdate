package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.utils.ChildActivity;
import com.anna.sent.soft.utils.DateUtils;

public class ResultActivity extends ChildActivity implements OnClickListener {
	private TableLayout table;

	@Override
	public void setViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_result);
		super.setViews(savedInstanceState);
		table = (TableLayout) findViewById(R.id.table);
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
				pregnancy.setCurrentPoint(Calendar.getInstance()); //
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
				row.setOnClickListener(this);
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
