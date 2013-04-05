package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.Utils;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class ResultActivity extends Activity {

	private Context mContext;

	private TextView textView0, textView00;

	private TextView[] textViews;
	private TextView[] results;
	private TextView[] messages;
	private Pregnancy[] pregnancies;

	private int whatToDo;
	private Calendar currentDate = Calendar.getInstance();
	private int menstrualCycleLen, lutealPhaseLen;
	private boolean[] byMethod;
	private Calendar lastMenstruationDate = Calendar.getInstance();
	private Calendar ovulationDate = Calendar.getInstance();
	private Calendar ultrasoundDate = Calendar.getInstance();
	private int weeks, days;
	private boolean isEmbryonicAge;

	private AdView adView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Utils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		// Show the Up button in the action bar.
		setupActionBar();

		setMembers();

		setMembersFromIntent();

		setAdView();

		clearViews();

		calculate();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			Bundle savedState = getIntent().getBundleExtra(
					MainActivity.EXTRA_MAIN_ACTIVITY_STATE);
			if (savedState != null) {
				intent.putExtras(savedState);
			}

			startActivity(intent);
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void clearViews() {
		if (byMethod[0] || byMethod[1] || byMethod[2]) {
			if (Shared.ResultParam.Calculate.ECD == whatToDo) {
				textView0.setText(mContext
						.getString(R.string.estimatedChildbirthDate));
				textView00.setText(mContext.getString(R.string.rememberECD));
			} else if (Shared.ResultParam.Calculate.EGA == whatToDo
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
						pregnancies[i] = PregnancyCalculator.Factory.get(
								lastMenstruationDate, menstrualCycleLen,
								lutealPhaseLen);
					}

					break;
				case 1:
					if (ovulationDate != null) {
						pregnancies[i] = PregnancyCalculator.Factory
								.get(ovulationDate);
					}

					break;
				case 2:
					if (ultrasoundDate != null) {
						pregnancies[i] = PregnancyCalculator.Factory.get(
								ultrasoundDate, weeks, days, isEmbryonicAge);
					}

					break;
				}

				if (pregnancies[i] != null
						&& whatToDo == Shared.ResultParam.Calculate.EGA) {
					pregnancies[i].setCurrentPoint(currentDate);
				}

				switch (whatToDo) {
				case Shared.ResultParam.Calculate.ECD:
					setEdcTexts(i);
					break;
				case Shared.ResultParam.Calculate.EGA:
					setEgaTexts(i);
					break;
				}

				setLast(i);
			}
		}
	}

	private void setEgaTexts(int i) {
		if (results.length == 3 && messages.length == 3
				&& pregnancies.length == 3 && i < 3 && i >= 0) {
			TextView result = results[i], message = messages[i];
			Pregnancy pregnancy = pregnancies[i];
			if (pregnancy != null) {
				Calendar start = pregnancy.getStartPoint();
				Calendar end = pregnancy.getEndPoint();
				if (start != null && end != null && currentDate != null) {
					if (pregnancy.isCorrect()) {
						if (result != null) {
							result.setText(pregnancy.getInfo(mContext));
						}
						if (message != null) {
							message.setText(pregnancy
									.getAdditionalInfo(mContext));
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
		if (results.length == 3 && messages.length == 3
				&& pregnancies.length == 3 && i < 3 && i >= 0) {
			TextView result = results[i];
			Pregnancy pregnancy = pregnancies[i];
			if (pregnancy != null && result != null) {
				Calendar childbirthDate = pregnancy.getEndPoint();
				if (childbirthDate != null) {
					result.setText(DateFormat.getDateFormat(mContext).format(
							childbirthDate.getTime()));
				}
			}
		}
	}

	private void setLast(int i) {
		if (results.length == 3 && messages.length == 3
				&& pregnancies.length == 3 && i >= 0 && i < 3) {
			TextView message = messages[i];
			Pregnancy pregnancy = pregnancies[i];
			if (message != null && pregnancy != null) {
				if (i == 2 && pregnancy.isCorrect()) {
					CharSequence old = message.getText();
					if (pregnancy.isAccurateForUltrasound(weeks)) {
						message.setText((old.equals("") ? "" : old + "\n")
								+ mContext
										.getString(R.string.accurateUltrasoundResults));
					} else {
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
		pregnancies = new Pregnancy[] { null, null, null };
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

	private void setMembersFromIntent() {
		Intent intent = getIntent();
		whatToDo = intent.getIntExtra(Shared.ResultParam.EXTRA_WHAT_TO_DO,
				Shared.ResultParam.Calculate.NOTHING);
		if (Shared.ResultParam.Calculate.NOTHING == whatToDo) {
			finish();
		}

		currentDate.setTimeInMillis(intent.getLongExtra(
				Shared.ResultParam.EXTRA_CURRENT_DATE,
				System.currentTimeMillis()));

		SharedPreferences settings = Shared.getSettings(this);
		menstrualCycleLen = settings.getInt(
				Shared.Saved.Settings.EXTRA_MENSTRUAL_CYCLE_LEN,
				PregnancyCalculator.AVG_MENSTRUAL_CYCLE_LENGTH);
		lutealPhaseLen = settings.getInt(
				Shared.Saved.Settings.EXTRA_LUTEAL_PHASE_LEN,
				PregnancyCalculator.AVG_LUTEAL_PHASE_LENGTH);

		byMethod = new boolean[3];
		byMethod[0] = settings
				.getBoolean(
						Shared.Saved.Calculation.EXTRA_BY_LAST_MENSTRUATION_DATE,
						false);
		byMethod[1] = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_OVULATION_DATE, false);
		byMethod[2] = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_BY_ULTRASOUND, false);
		lastMenstruationDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_LAST_MENSTRUATION_DATE,
				System.currentTimeMillis()));
		ovulationDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_OVULATION_DATE,
				System.currentTimeMillis()));
		ultrasoundDate.setTimeInMillis(settings.getLong(
				Shared.Saved.Calculation.EXTRA_ULTRASOUND_DATE,
				System.currentTimeMillis()));

		weeks = settings.getInt(Shared.Saved.Calculation.EXTRA_WEEKS, 0);
		days = settings.getInt(Shared.Saved.Calculation.EXTRA_DAYS, 0);
		isEmbryonicAge = settings.getBoolean(
				Shared.Saved.Calculation.EXTRA_IS_EMBRYONIC_AGE, false);
	}

	private void setAdView() {
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
		if (adView != null) {
			adView.removeAllViews();
			adView.destroy();
		}

		super.onDestroy();
	}
}
