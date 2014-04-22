package com.anna.sent.soft.strategy;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public interface Strategy {
	void onCreate(Bundle savedInstanceState);

	void onRestart();

	void onStart();

	void onActivityResult(int requestCode, int resultCode, Intent data);

	void onRestoreInstanceState(Bundle savedInstanceState);

	void onResume();

	void onPause();

	void onSaveInstanceState(Bundle outState);

	void onStop();

	void onDestroy();

	void onConfigurationChanged(Configuration newConfig);

	boolean onCreateOptionsMenu(Menu menu);

	boolean onPrepareOptionsMenu(Menu menu);

	boolean onOptionsItemSelected(MenuItem item);
}