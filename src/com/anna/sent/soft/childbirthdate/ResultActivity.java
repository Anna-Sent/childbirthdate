package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends Activity {

	private TextView textView0, textView00, textView1, textView2, textView3,
			result1, result2, result3, message3;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		mContext = getApplicationContext();
		
		textView0 = (TextView) findViewById(R.id.textView0);
		textView00 = (TextView) findViewById(R.id.textView00);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		result1 = (TextView) findViewById(R.id.result1);
		result2 = (TextView) findViewById(R.id.result2);
		result3 = (TextView) findViewById(R.id.result3);
		message3 = (TextView) findViewById(R.id.message3);

		Intent intent = getIntent();
		int menstrualCycleLen = intent.getIntExtra(
				MainActivity.EXTRA_MENSTRUAL_CYCLE_LEN,
				ChildbirthDateCalculator.MENSTRUAL_CYCLE_AVG_LENGTH);
		int lutealPhaseLen = intent.getIntExtra(
				MainActivity.EXTRA_LUTEAL_PHASE_LEN,
				ChildbirthDateCalculator.LUTEAL_PHASE_AVG_LENGTH);
		boolean byMenstruationDate = intent.getBooleanExtra(
				MainActivity.EXTRA_BY_LAST_MENSTRUATION_DATE, false);
		Calendar lastMenstruationDate = (Calendar) intent
				.getSerializableExtra(MainActivity.EXTRA_LAST_MENSTRUATION_DATE);
		if (lastMenstruationDate == null) {
			lastMenstruationDate = Calendar.getInstance();
		}

		boolean byOvulationDate = intent.getBooleanExtra(
				MainActivity.EXTRA_BY_OVULATION_DATE, false);
		Calendar ovulationDate = (Calendar) intent
				.getSerializableExtra(MainActivity.EXTRA_OVULATION_DATE);
		if (ovulationDate == null) {
			ovulationDate = Calendar.getInstance();
		}

		boolean byUltrasound = intent.getBooleanExtra(
				MainActivity.EXTRA_BY_ULTRASOUND, false);
		Calendar ultrasoundDate = (Calendar) intent
				.getSerializableExtra(MainActivity.EXTRA_ULTRASOUND_DATE);
		if (ultrasoundDate == null) {
			ultrasoundDate = Calendar.getInstance();
		}

		int weeks = intent.getIntExtra(MainActivity.EXTRA_WEEKS, 0);
		int days = intent.getIntExtra(MainActivity.EXTRA_DAYS, 0);
		boolean isFetal = intent.getBooleanExtra(MainActivity.EXTRA_IS_FETAL,
				false);

		if (byMenstruationDate || byOvulationDate || byUltrasound) {
			ChildbirthDateCalculator calculator = new ChildbirthDateCalculator(mContext);
			if (byMenstruationDate) {
				Calendar result = calculator
						.getByLastMenstruationDate(menstrualCycleLen,
								lutealPhaseLen, lastMenstruationDate);
				result1.setText(result.toString());
			} else {
				//
			}

			if (byOvulationDate) {
				Calendar result = calculator.getByOvulationDate(ovulationDate);
				result2.setText(result.toString());
			} else {
				//
			}

			if (byUltrasound) {
				Calendar result = calculator.getByUltrasound(ultrasoundDate,
						weeks, days, isFetal);
				result3.setText(result.toString());
			} else {
				//
			}
		} else {
			textView0.setText(mContext.getString(R.string.error0));
			textView00.setText(mContext.getString(R.string.error00));
			textView1.setVisibility(View.GONE);
			textView2.setVisibility(View.GONE);
			textView3.setVisibility(View.GONE);
			result1.setVisibility(View.GONE);
			result2.setVisibility(View.GONE);
			result3.setVisibility(View.GONE);
			message3.setVisibility(View.GONE);
		}
	}
}
