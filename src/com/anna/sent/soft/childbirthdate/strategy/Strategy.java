package com.anna.sent.soft.childbirthdate.strategy;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public interface Strategy {
	void onCreate(Bundle savedInstanceState);

	void onStart();

	void onRestoreInstanceState(Bundle savedInstanceState);

	void onResume();

	void onPause();

	void onSaveInstanceState(Bundle outState);

	void onStop();

	void onActivityResult(int requestCode, int resultCode, Intent data);

	boolean onCreateOptionsMenu(Menu menu);

	boolean onPrepareOptionsMenu(Menu menu);

	boolean onOptionsItemSelected(MenuItem item);
}