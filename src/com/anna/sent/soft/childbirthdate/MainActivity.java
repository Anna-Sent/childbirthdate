package com.anna.sent.soft.childbirthdate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.fragments.TitlesHelperFragment;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidget;
import com.anna.sent.soft.utils.StateSaverActivity;
import com.anna.sent.soft.utils.ThemeUtils;

public class MainActivity extends StateSaverActivity {
	@Override
	public void setViews() {
		setContentView(R.layout.tab_calculation);

		getSupportFragmentManager().beginTransaction()
				.add(new TitlesHelperFragment(), "TitlesHelper").commit();
	}

	@Override
	public void beforeOnSaveInstanceState() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment details = fm.findFragmentById(R.id.details);
		if (details != null) {
			FragmentTransaction ft = fm.beginTransaction();
			ft.remove(details);
			ft.commit();
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
