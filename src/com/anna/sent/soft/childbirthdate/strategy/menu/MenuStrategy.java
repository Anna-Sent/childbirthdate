package com.anna.sent.soft.childbirthdate.strategy.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.HelpActivity;
import com.anna.sent.soft.childbirthdate.R;
import com.anna.sent.soft.childbirthdate.SettingsActivity;
import com.anna.sent.soft.childbirthdate.actions.EmailDataActionActivity;
import com.anna.sent.soft.childbirthdate.strategy.Strategy;

public class MenuStrategy implements Strategy {
	private Activity mActivity;

	public MenuStrategy(Activity activity) {
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mActivity.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sendData:
			Intent sendData = new Intent(mActivity,
					EmailDataActionActivity.class);
			mActivity.startActivity(sendData);
			return true;
		case R.id.settings:
			mActivity.startActivity(new Intent(mActivity,
					SettingsActivity.class));
			return true;
		case R.id.help:
			Intent help = new Intent(mActivity, HelpActivity.class);
			mActivity.startActivity(help);
			return true;
		default:
			return false;
		}
	}
}