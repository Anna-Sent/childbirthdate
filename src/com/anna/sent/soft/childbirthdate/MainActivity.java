package com.anna.sent.soft.childbirthdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.actions.EmailDataActionActivity;
import com.anna.sent.soft.childbirthdate.base.StateSaverActivity;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidget;

public class MainActivity extends StateSaverActivity {
	@Override
	public void setViews(Bundle savedInstanceState) {
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

	public final static String EXTRA_THEME_CHANGED = "extra_theme_changed";

	@Override
	protected void onNewIntent(Intent intent) {
		// log("onNewIntent");
		super.onNewIntent(intent);
		Bundle extras = intent.getExtras();
		if (extras != null && extras.containsKey(EXTRA_THEME_CHANGED)) {
			Bundle state = new Bundle();
			saveState(state);
			finish();
			intent.putExtras(state);
			startActivity(intent);
		}
	}
}
