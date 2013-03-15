package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.PregnancyCalculator.CountingMethod;
import com.anna.sent.soft.childbirthdate.PregnancyCalculator.GestationalAge;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class ResultActivity extends Activity {

	private TextView textView0, textView00, textView1, textView2, textView3,
			result1, result2, result3, message1, message2, message3;
	private Context mContext;
	private AdView adView;

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
		message1 = (TextView) findViewById(R.id.message1);
		message2 = (TextView) findViewById(R.id.message2);
		message3 = (TextView) findViewById(R.id.message3);

		Intent intent = getIntent();
		int whatToDo = intent.getIntExtra(MainActivity.EXTRA_WHAT_TO_DO,
				MainActivity.CALCULATE_NOTHING);
		if (MainActivity.CALCULATE_NOTHING == whatToDo) {
			finish();
		}

		Calendar currentDate = (Calendar) intent
				.getSerializableExtra(MainActivity.EXTRA_CURRENT_DATE);
		if (currentDate == null) {
			currentDate = Calendar.getInstance();
		}

		int menstrualCycleLen = intent.getIntExtra(
				MainActivity.EXTRA_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		int lutealPhaseLen = intent.getIntExtra(
				MainActivity.EXTRA_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);

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
		boolean isEmbryonicAge = intent.getBooleanExtra(
				MainActivity.EXTRA_IS_EMBRYONIC_AGE, false);

		boolean doCalculation = byMenstruationDate || byOvulationDate
				|| byUltrasound;

		textView0.setText(doCalculation ? "" : mContext
				.getString(R.string.errorNotSelectedCalculationMethod));
		textView00.setText(doCalculation ? "" : mContext
				.getString(R.string.consultADoctor));
		textView1.setVisibility(byMenstruationDate ? View.VISIBLE : View.GONE);
		textView2.setVisibility(byOvulationDate ? View.VISIBLE : View.GONE);
		textView3.setVisibility(byUltrasound ? View.VISIBLE : View.GONE);
		result1.setVisibility(byMenstruationDate ? View.VISIBLE : View.GONE);
		message1.setVisibility(byMenstruationDate ? View.VISIBLE : View.GONE);
		result2.setVisibility(byOvulationDate ? View.VISIBLE : View.GONE);
		message2.setVisibility(byOvulationDate ? View.VISIBLE : View.GONE);
		result3.setVisibility(byUltrasound ? View.VISIBLE : View.GONE);
		message3.setVisibility(byUltrasound ? View.VISIBLE : View.GONE);

		if (doCalculation) {
			PregnancyCalculator calculator = new PregnancyCalculator(mContext);
			if (MainActivity.CALCULATE_ECD == whatToDo) {
				textView0.setText(mContext
						.getString(R.string.estimatedChildbirthDate));
				textView00.setText(mContext.getString(R.string.rememberECD));

				if (byMenstruationDate) {
					Calendar result = calculator
							.getChildbirthDateByLastMenstruationDate(
									menstrualCycleLen, lutealPhaseLen,
									lastMenstruationDate);
					setTexts(result, result1, message1, calculator.getMessage());
				}

				if (byOvulationDate) {
					Calendar result = calculator
							.getChildbirthDateByOvulationDate(ovulationDate);
					setTexts(result, result2, message2, calculator.getMessage());
				}

				if (byUltrasound) {
					Calendar result = calculator.getChildbirthDateByUltrasound(
							ultrasoundDate, weeks, days,
							isEmbryonicAge ? CountingMethod.EmbryonicAge
									: CountingMethod.GestationalAge);
					setTexts(result, result3, message3, calculator.getMessage());
				}
			} else if (MainActivity.CALCULATE_EGA == whatToDo) {
				textView0.setText(mContext
						.getString(R.string.estimatedGestationAge)
						+ " ("
						+ DateFormat.getDateFormat(mContext).format(
								currentDate.getTime()) + ")");
				textView00.setText(mContext.getString(R.string.rememberEGA));

				if (byMenstruationDate) {
					GestationalAge result = calculator
							.getGestationalAgeByLastMenstruationDate(
									lastMenstruationDate, currentDate);
					setTexts(result, result1, message1, calculator.getMessage());
				}

				if (byOvulationDate) {
					GestationalAge result = calculator
							.getGestationalAgeByOvulationDate(ovulationDate,
									currentDate);
					setTexts(result, result2, message2, calculator.getMessage());
				}

				if (byUltrasound) {
					GestationalAge result = calculator
							.getGestationalAgeByUltrasound(
									ultrasoundDate,
									weeks,
									days,
									isEmbryonicAge ? CountingMethod.EmbryonicAge
											: CountingMethod.GestationalAge,
									currentDate);
					setTexts(result, result3, message3, calculator.getMessage());
				}
			}
		}

		// AdView adView = (AdView) findViewById(R.id.adView);
		adView = new AdView(this, AdSize.BANNER, "a1513549e3d3050");
		adView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		adView.setGravity(Gravity.CENTER);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		linearLayout.addView(adView);

		AdRequest request = new AdRequest();
		/*
		 * request.addTestDevice(AdRequest.TEST_EMULATOR);
		 * request.addTestDevice("2600D922057328C48F2E6DBAB33639C1");
		 */
		request.setGender(AdRequest.Gender.FEMALE);
		adView.loadAd(request);
	}

	private void setTexts(GestationalAge result, TextView resultTextView,
			TextView messageTextView, String message) {
		if (result.isCorrect()) {
			resultTextView.setText(result.getWeeks() + " "
					+ mContext.getString(R.string.weeks) + " "
					+ result.getDays() + " "
					+ mContext.getString(R.string.days));
		} else {
			resultTextView.setText(mContext
					.getString(R.string.errorIncorrectCurrentDate));
		}

		if (!message.equals("")) {
			messageTextView.setText(message);
		} else {
			messageTextView.setVisibility(View.GONE);
		}
	}

	private void setTexts(Calendar result, TextView resultTextView,
			TextView messageTextView, String message) {
		resultTextView.setText(DateFormat.getDateFormat(mContext).format(
				result.getTime()));
		if (!message.equals("")) {
			messageTextView.setText(message);
		} else {
			messageTextView.setVisibility(View.GONE);
		}
	}

	public void rate(View view) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=" + getPackageName()));
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, R.string.marketNotFound, Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	protected void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}
}
