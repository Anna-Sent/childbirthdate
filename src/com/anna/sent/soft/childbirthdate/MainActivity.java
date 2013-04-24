package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.fragments.TitlesFragment;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidget;
import com.anna.sent.soft.utils.StateSaver;
import com.anna.sent.soft.utils.ThemeUtils;

public class MainActivity extends FragmentActivity implements StateSaver {
	private TitlesFragment mTitlesFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ThemeUtils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_calculation);

		mTitlesFragment = (TitlesFragment) getSupportFragmentManager()
				.findFragmentById(R.id.titles);
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
	public void onSaveInstanceState(Bundle outState) {
		// call below must be before super.onSaveInstanceState(outState);
		mTitlesFragment.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		switch (ThemeUtils.getThemeId(this)) {
		case ThemeUtils.LIGHT_THEME:
			menu.findItem(R.id.lighttheme).setChecked(true);
			break;
		case ThemeUtils.DARK_THEME:
		default:
			menu.findItem(R.id.darktheme).setChecked(true);
			break;
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.lighttheme:
			ThemeUtils.changeToTheme(this, ThemeUtils.LIGHT_THEME);
			return true;
		case R.id.darktheme:
			ThemeUtils.changeToTheme(this, ThemeUtils.DARK_THEME);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
