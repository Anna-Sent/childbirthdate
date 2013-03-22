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

import com.anna.sent.soft.childbirthdate.pregnancy.CorrectedAge;
import com.anna.sent.soft.childbirthdate.pregnancy.EmbryonicAge;
import com.anna.sent.soft.childbirthdate.pregnancy.GestationalAge;
import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator.CountingMethod;
import com.anna.sent.soft.utils.Utils;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.anna.sent.soft.childbirthdate.shared.Constants;

public class ResultActivity extends Activity {

	private Context mContext;

	private TextView textView0, textView00;

	private TextView[] textViews;
	private TextView[] results;
	private TextView[] messages;
	private Pregnancy[] gas;

	int whatToDo;
	Calendar currentDate;
	int menstrualCycleLen, lutealPhaseLen;
	boolean[] byMethod;
	Calendar lastMenstruationDate, ovulationDate, ultrasoundDate;
	int weeks, days;
	boolean isEmbryonicAge;

	private AdView adView;

	private final static int undefined = 1;
	private final static int accurate = 2;
	private final static int inaccurate = 3;
	private int isAccurateForUltrasound = undefined;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Utils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		setMembers();

		setMembersFromIntent();

		setAdView();

		clearViews();

		calculate();
	}

	private void clearViews() {
		if (byMethod[0] || byMethod[1] || byMethod[2]) {
			if (Constants.CALCULATE_ECD == whatToDo) {
				textView0.setText(mContext
						.getString(R.string.estimatedChildbirthDate));
				textView00.setText(mContext.getString(R.string.rememberECD));
			} else if (Constants.CALCULATE_EGA == whatToDo
					&& currentDate != null) {
				textView0.setText(mContext
						.getString(R.string.estimatedGestationAge)
						+ " ("
						+ DateFormat.getDateFormat(mContext).format(
								currentDate.getTime()) + ")");
				textView00.setText(mContext.getString(R.string.rememberEGA));
			} else {
				textView0.setText("");
				textView00.setText("");
			}
		} else {
			textView0.setText(mContext
					.getString(R.string.errorNotSelectedCalculationMethod));
			textView00.setText(mContext.getString(R.string.consultADoctor));
		}

		for (int i = 0; i < 3; ++i) {
			results[i].setText("");
			messages[i].setText("");
			int visibility = byMethod[i] ? View.VISIBLE : View.GONE;
			textViews[i].setVisibility(visibility);
			results[i].setVisibility(visibility);
			messages[i].setVisibility(visibility);
		}
	}

	private void calculate() {
		for (int i = 0; i < 3; ++i) {
			if (byMethod[i]) {
				switch (i) {
				case 0:
					if (lastMenstruationDate != null) {
						gas[i] = new CorrectedAge(lastMenstruationDate,
								menstrualCycleLen, lutealPhaseLen);
					}

					break;
				case 1:
					if (ovulationDate != null) {
						gas[i] = new EmbryonicAge(ovulationDate);
					}

					break;
				case 2:
					if (ultrasoundDate != null) {
						CountingMethod countingMethod = isEmbryonicAge ? CountingMethod.EmbryonicAge
								: CountingMethod.GestationalAge;
						switch (countingMethod) {
						case EmbryonicAge:
							gas[i] = new EmbryonicAge(weeks, days,
									ultrasoundDate);
							break;
						case GestationalAge:
							gas[i] = new GestationalAge(weeks, days,
									ultrasoundDate);
							break;
						}
					}

					isAccurateForUltrasound = gas[i].isAccurateForUltrasound() ? accurate
							: inaccurate;
					break;
				}

				if (gas[i] != null && whatToDo == Constants.CALCULATE_EGA) {
					gas[i].setCurrentPoint(currentDate);
				}

				switch (whatToDo) {
				case Constants.CALCULATE_ECD:
					setEdcTexts(i);
					break;
				case Constants.CALCULATE_EGA:
					setEgaTexts(i);
					break;
				}

				setLast(i);
			}
		}
	}

	private String getStringRepresentation(int weeks, int days) {
		String result = "";
		if (weeks > 0) {
			result += weeks + " " + mContext.getString(R.string.weeks)
					+ (days > 0 ? " " : "");
		}

		if (days > 0) {
			result += days + " " + mContext.getString(R.string.days);
		}

		return result;
	}

	private String getStringRepresentation(Pregnancy ga) {
		String result = mContext.getString(R.string.trimesteris)
				+ " "
				+ ga.getTrimesterNumber()
				+ "; "
				+ mContext.getString(R.string.fulltermis)
				+ " "
				+ getStringRepresentation(ga.getFullDurationWeeks(),
						ga.getFullDurationDays());
		int rest = ga.getRestInDays();
		if (rest > 0) {
			result += "; "
					+ mContext.getString(R.string.rest)
					+ " "
					+ getStringRepresentation(ga.getRestWeeks(),
							ga.getRestDays());
		} else if (rest == 0) {
			result += "; " + mContext.getString(R.string.ECD);
		}

		return result;
	}

	private void setEgaTexts(int i) {
		if (results.length == 3 && messages.length == 3 && gas.length == 3
				&& i < 3 && i >= 0) {
			TextView result = results[i], message = messages[i];
			Pregnancy ga = gas[i];
			if (ga != null) {
				Calendar start = ga.getStartPoint();
				Calendar end = ga.getEndPoint();
				if (start != null && end != null && currentDate != null) {
					if (ga.isCorrect()) {
						if (result != null) {
							result.setText(ga.getWeeks() + " "
									+ mContext.getString(R.string.weeks) + " "
									+ ga.getDays() + " "
									+ mContext.getString(R.string.days));
						}
						if (message != null) {
							message.setText(getStringRepresentation(ga));
						}
					} else {
						if (result != null) {
							result.setText(mContext
									.getString(R.string.errorIncorrectGestationAge));
						}

						if (message != null) {
							if (currentDate.before(start)) {
								message.setText(mContext
										.getString(R.string.errorIncorrectCurrentDateSmaller));
							} else if (currentDate.after(end)) {
								message.setText(mContext
										.getString(R.string.errorIncorrectCurrentDateGreater));
							}
						}
					}
				}
			}
		}
	}

	private void setEdcTexts(int i) {
		if (results.length == 3 && messages.length == 3 && gas.length == 3
				&& i < 3 && i >= 0) {
			TextView result = results[i];
			Pregnancy ga = gas[i];
			if (ga != null && result != null) {
				Calendar childbirthDate = ga.getEndPoint();
				if (childbirthDate != null) {
					result.setText(DateFormat.getDateFormat(mContext).format(
							childbirthDate.getTime()));
				}
			}
		}
	}

	private void setLast(int i) {
		if (results.length == 3 && messages.length == 3 && gas.length == 3
				&& i >= 0 && i < 3) {
			TextView message = messages[i];
			Pregnancy ga = gas[i];
			if (message != null && ga != null) {
				if (i == 2 && ga.isCorrect()) {
					CharSequence old = message.getText();
					if (isAccurateForUltrasound == accurate) {
						message.setText((old.equals("") ? "" : old + "\n")
								+ mContext
										.getString(R.string.accurateUltrasoundResults));
					} else if (isAccurateForUltrasound == inaccurate) {
						message.setText((old.equals("") ? "" : old + "\n")
								+ mContext
										.getString(R.string.inaccurateUltrasoundResults));
					}
				}

				if (message.getText().equals("")) {
					message.setVisibility(View.GONE);
				}
			}
		}
	}

	/**
	 * Gestational age must be correct before call of this method. So, call
	 * {@code isCorrect()} before using of this method.
	 * 
	 * @return string representation of trimester number
	 */
	public String getTrimesterString(Pregnancy gestationalAge) {
		int trimester = gestationalAge.getTrimesterNumber();
		switch (trimester) {
		case Pregnancy.FIRST_TRIMESTER:
			return mContext.getString(R.string.firstTrimester);
		case Pregnancy.SECOND_TRIMESTER:
			return mContext.getString(R.string.secondTrimester);
		case Pregnancy.THIRD_TRIMESTER:
			return mContext.getString(R.string.thirdTrimester);
		}

		return "?";
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

	private void setMembers() {
		mContext = getApplicationContext();
		textView0 = (TextView) findViewById(R.id.textView0);
		textView00 = (TextView) findViewById(R.id.textView00);
		gas = new Pregnancy[] { null, null, null };
		textViews = new TextView[] { (TextView) findViewById(R.id.textView1),
				(TextView) findViewById(R.id.textView2),
				(TextView) findViewById(R.id.textView3) };
		results = new TextView[] { (TextView) findViewById(R.id.result1),
				(TextView) findViewById(R.id.result2),
				(TextView) findViewById(R.id.result3) };
		messages = new TextView[] { (TextView) findViewById(R.id.message1),
				(TextView) findViewById(R.id.message2),
				(TextView) findViewById(R.id.message3) };
	}

	private Calendar getCalendarValue(Calendar date) {
		if (date == null) {
			date = Calendar.getInstance();
		}

		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		return date;
	}

	private void setMembersFromIntent() {
		Intent intent = getIntent();
		whatToDo = intent.getIntExtra(Constants.EXTRA_WHAT_TO_DO,
				Constants.CALCULATE_NOTHING);
		if (Constants.CALCULATE_NOTHING == whatToDo) {
			finish();
		}

		menstrualCycleLen = intent.getIntExtra(
				Constants.EXTRA_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		lutealPhaseLen = intent.getIntExtra(Constants.EXTRA_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);

		byMethod = new boolean[3];
		byMethod[0] = intent.getBooleanExtra(
				Constants.EXTRA_BY_LAST_MENSTRUATION_DATE, false);
		byMethod[1] = intent.getBooleanExtra(Constants.EXTRA_BY_OVULATION_DATE,
				false);
		byMethod[2] = intent.getBooleanExtra(Constants.EXTRA_BY_ULTRASOUND,
				false);
		currentDate = (Calendar) intent
				.getSerializableExtra(Constants.EXTRA_CURRENT_DATE);
		currentDate = getCalendarValue(currentDate);
		lastMenstruationDate = (Calendar) intent
				.getSerializableExtra(Constants.EXTRA_LAST_MENSTRUATION_DATE);
		lastMenstruationDate = getCalendarValue(lastMenstruationDate);
		ovulationDate = (Calendar) intent
				.getSerializableExtra(Constants.EXTRA_OVULATION_DATE);
		ovulationDate = getCalendarValue(ovulationDate);
		ultrasoundDate = (Calendar) intent
				.getSerializableExtra(Constants.EXTRA_ULTRASOUND_DATE);
		ultrasoundDate = getCalendarValue(ultrasoundDate);

		weeks = intent.getIntExtra(Constants.EXTRA_WEEKS, 0);
		days = intent.getIntExtra(Constants.EXTRA_DAYS, 0);
		isEmbryonicAge = intent.getBooleanExtra(
				Constants.EXTRA_IS_EMBRYONIC_AGE, false);
	}

	void setAdView() {
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

	@Override
	protected void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}
}
