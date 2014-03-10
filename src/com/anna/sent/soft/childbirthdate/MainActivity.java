package com.anna.sent.soft.childbirthdate;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.actions.EmailDataActionActivity;
import com.anna.sent.soft.childbirthdate.base.StateSaverActivity;
import com.anna.sent.soft.childbirthdate.shared.Settings;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidget;

public class MainActivity extends StateSaverActivity {
	private static final String TAG = "moo";
	private static final boolean DEBUG = false;
	private static final boolean TEST_CLEAR_SETTINGS = false;

	private String wrapMsg(String msg) {
		return getClass().getSimpleName() + ": " + msg;
	}

	@SuppressWarnings("unused")
	private void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	@SuppressWarnings("unused")
	private void log(String msg, boolean debug) {
		if (DEBUG && debug) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	@Override
	public void setViews(Bundle savedInstanceState) {
		if (TEST_CLEAR_SETTINGS) {
			Settings.clear(this);
		}

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void beforeOnSaveInstanceState() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment details = fm.findFragmentById(R.id.details);
		if (details != null) {
			fm.beginTransaction().remove(details).commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// update all widgets
		MyPregnancyWidget.updateAllWidgets(this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sendData:
			Intent sendData = new Intent(this, EmailDataActionActivity.class);
			startActivity(sendData);
			return true;
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.help:
			Intent help = new Intent(this, HelpActivity.class);
			startActivity(help);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public final static String EXTRA_CONFIGURATION_CHANGED = "extra_configuration_changed";

	@Override
	protected void onNewIntent(Intent intent) {
		// log("onNewIntent");
		super.onNewIntent(intent);
		Bundle extras = intent.getExtras();
		if (extras != null && extras.containsKey(EXTRA_CONFIGURATION_CHANGED)) {
			Bundle state = new Bundle();
			saveState(state);
			finish();
			intent.putExtras(state);
			startActivity(intent);
		}
	}
}