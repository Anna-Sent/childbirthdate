package com.anna.sent.soft.strategy;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BaseStrategy implements Strategy {
	protected String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	}

	@Override
	public void onRestart() {
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
	public void onDestroy() {
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
}