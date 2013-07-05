package com.anna.sent.soft.childbirthdate;

import java.util.Calendar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anna.sent.soft.childbirthdate.pregnancy.Pregnancy;
import com.anna.sent.soft.childbirthdate.pregnancy.PregnancyCalculator;
import com.anna.sent.soft.childbirthdate.shared.Shared;
import com.anna.sent.soft.utils.ChildActivity;
import com.anna.sent.soft.utils.DateUtils;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class ResultActivity extends ChildActivity implements AdListener {
	private static final String TAG = "moo";
	private static final boolean DEBUG_AD = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	private void log(String msg, boolean scenario) {
		if (scenario) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	private TextView textView0, textView00;

	private TextView[] textViews;
	private TextView[] results;
	private TextView[] messages;
	private Pregnancy[] pregnancies;

	private int whatToDo;
	private Calendar currentDate = Calendar.getInstance();

	private AdView mAdView = null;

	@Override
	public void setViews(Bundle savedInstanceState) {
		setContentView(R.layout.activity_result);
		super.setViews(savedInstanceState);

		setMembers();

		setMembersFromIntent();

		setAdView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		clearViews();

		calculate();
	}

	private void clearViews() {
		if (getData().byLmp() || getData().byOvulation()
				|| getData().byUltrasound()) {
			if (Shared.ResultParam.Calculate.ECD == whatToDo) {
				textView0.setText(getString(R.string.estimatedChildbirthDate));
				textView00.setText(getString(R.string.rememberECD));
			} else if (Shared.ResultParam.Calculate.EGA == whatToDo) {
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

		for (int i = 0; i < getData().byMethod().length; ++i) {
			results[i].setText("");
			messages[i].setText("");
			int visibility = whatToDo != Shared.ResultParam.Calculate.NOTHING
					&& getData().byMethod()[i] ? View.VISIBLE : View.GONE;
			textViews[i].setVisibility(visibility);
			results[i].setVisibility(visibility);
			messages[i].setVisibility(visibility);
		}
	}

	private void calculate() {
		for (int i = 0; i < getData().byMethod().length; ++i) {
			if (getData().byMethod()[i]) {
				switch (i) {
				case 0:
					pregnancies[i] = PregnancyCalculator.Factory.get(getData()
							.getLastMenstruationDate(), getData()
							.getMenstrualCycleLen(), getData()
							.getLutealPhaseLen());
					break;
				case 1:
					pregnancies[i] = PregnancyCalculator.Factory.get(getData()
							.getOvulationDate());
					break;
				case 2:
					pregnancies[i] = PregnancyCalculator.Factory.get(getData()
							.getUltrasoundDate(), getData().getWeeks(),
							getData().getDays(), getData().isEmbryonicAge());
					break;
				}

				if (whatToDo == Shared.ResultParam.Calculate.EGA) {
					pregnancies[i].setCurrentPoint(currentDate);
				}

				switch (whatToDo) {
				case Shared.ResultParam.Calculate.ECD:
					setEdcTexts(i);
					setLast(i);
					break;
				case Shared.ResultParam.Calculate.EGA:
					setEgaTexts(i);
					setLast(i);
					break;
				}
			}
		}
	}

	private void setEgaTexts(int i) {
		TextView result = results[i], message = messages[i];
		Pregnancy pregnancy = pregnancies[i];
		Calendar start = pregnancy.getStartPoint();
		Calendar end = pregnancy.getEndPoint();
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
	}

	private void setEdcTexts(int i) {
		TextView result = results[i];
		Pregnancy pregnancy = pregnancies[i];
		Calendar childbirthDate = pregnancy.getEndPoint();
		result.setText(DateUtils.toString(this, childbirthDate));
	}

	private void setLast(int i) {
		TextView message = messages[i];
		Pregnancy pregnancy = pregnancies[i];
		if (i == 2 && pregnancy.isCorrect()) {
			CharSequence old = message.getText();
			if (pregnancy.isAccurateForUltrasound(getData().getWeeks())) {
				message.setText((old.equals("") ? "" : old + "\n")
						+ getString(R.string.accurateUltrasoundResults));
			} else {
				message.setText((old.equals("") ? "" : old + "\n")
						+ getString(R.string.inaccurateUltrasoundResults));
			}
		}

		if (message.getText().equals("")
				&& Configuration.ORIENTATION_PORTRAIT == getResources()
						.getConfiguration().orientation) {
			message.setVisibility(View.GONE);
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
			return;
		}

		currentDate.setTimeInMillis(intent.getLongExtra(
				Shared.ResultParam.EXTRA_CURRENT_DATE,
				System.currentTimeMillis()));
	}

	private void setAdView() {
		mAdView = new AdView(this, AdSize.BANNER, "a1513549e3d3050");
		mAdView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mAdView.setGravity(Gravity.CENTER);
		mAdView.setPadding(0,
				getResources().getDimensionPixelSize(R.dimen.view_padding), 0,
				0);
		mAdView.setAdListener(this);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		linearLayout.addView(mAdView);

		AdRequest request = new AdRequest();
		/*
		 * request.addTestDevice(AdRequest.TEST_EMULATOR);
		 * request.addTestDevice("2600D922057328C48F2E6DBAB33639C1");
		 */
		request.setGender(AdRequest.Gender.FEMALE);
		mAdView.loadAd(request);
	}

	private boolean mIsActivityDestroyed = false;
	private boolean mIsAdLoadingCompleted = false;

	@Override
	protected void onDestroy() {
		log("destroy activity", DEBUG_AD);
		mIsActivityDestroyed = true;
		if (mIsAdLoadingCompleted) {
			destroyAdView();
		}

		super.onDestroy();
	}

	private void destroyAdView() {
		if (mAdView != null) {
			log("destroy ad view", DEBUG_AD);
			mAdView.removeAllViews();
			mAdView.destroy();
			mAdView = null;
		}
	}

	@Override
	public void onDismissScreen(Ad arg0) {
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		log("failed to receive ad", DEBUG_AD);
		mIsAdLoadingCompleted = true;
		if (mIsActivityDestroyed) {
			destroyAdView();
		}
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
	}

	@Override
	public void onPresentScreen(Ad arg0) {
	}

	@Override
	public void onReceiveAd(Ad arg0) {
		log("receive ad", DEBUG_AD);
		mIsAdLoadingCompleted = true;
		if (mIsActivityDestroyed) {
			destroyAdView();
		}
	}
}
