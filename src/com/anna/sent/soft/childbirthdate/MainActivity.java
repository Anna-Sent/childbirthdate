package com.anna.sent.soft.childbirthdate;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidget;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidgetAdditional;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidgetSimple;
import com.anna.sent.soft.childbirthdate.widget.MyPregnancyWidgetSmall;
import com.anna.sent.soft.utils.StateSaver;
import com.anna.sent.soft.utils.Utils;

public class MainActivity extends FragmentActivity implements StateSaver {
	/*
	 * private TabHost mTabHost; ViewPager mViewPager; TabsAdapter mTabsAdapter;
	 * 
	 * private TabCalculationFragment mTabCalculationFragment = null; private
	 * TabHelpFragment mTabHelpFragment = null;
	 * 
	 * private static final String EXTRA_GUI_CURRENT_TAB =
	 * "com.anna.sent.soft.childbirthdate.currenttab";
	 */
	@SuppressWarnings("unused")
	private void disableWidgets() {
		disableWidget(MyPregnancyWidgetSmall.class);
		disableWidget(MyPregnancyWidgetSimple.class);
		disableWidget(MyPregnancyWidgetAdditional.class);
	}

	@SuppressWarnings("unused")
	private void enableWidgets() {
		enableWidget(MyPregnancyWidgetSmall.class);
		enableWidget(MyPregnancyWidgetSimple.class);
		enableWidget(MyPregnancyWidgetAdditional.class);
	}

	private void disableWidget(Class<?> widgetClass) {
		ComponentName component = new ComponentName(this, widgetClass);
		int status = getPackageManager().getComponentEnabledSetting(component);
		if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
				|| status == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
			getPackageManager().setComponentEnabledSetting(component,
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
					PackageManager.DONT_KILL_APP);
		}
	}

	private void enableWidget(Class<?> widgetClass) {
		ComponentName component = new ComponentName(this, widgetClass);
		int status = getPackageManager().getComponentEnabledSetting(component);
		if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
				|| status == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
			getPackageManager().setComponentEnabledSetting(component,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Utils.onActivityCreateSetTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout_support); // activity_main);
		// View view = findViewById(R.id.details);
		// getSupportFragmentManager().popBackStack();
		// Fragment fr = getSupportFragmentManager()
		// .findFragmentById(R.id.details);
		/*
		 * if (view != null) { Log.d("moo", "found view in main activity"); } if
		 * (fr != null) { Log.d("moo", "found fr in main activity");
		 * getSupportFragmentManager().beginTransaction().remove(fr).commit(); }
		 */
		/*
		 * final String tab_calculation = "tab_calculation", tab_help =
		 * "tab_help";
		 * 
		 * mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		 * mTabHost.setup();
		 * 
		 * mViewPager = (ViewPager) findViewById(R.id.pager);
		 * 
		 * mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		 * mTabsAdapter.addTab(
		 * mTabHost.newTabSpec(tab_calculation).setIndicator(
		 * getString(R.string.calculation)), FragmentLayoutSupport.class, null);
		 * mTabsAdapter.addTab( mTabHost.newTabSpec(tab_help).setIndicator(
		 * getString(R.string.help)), TabHelpFragment.class, null);
		 * 
		 * if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
		 * mTabHost.setCurrentTabByTag(savedInstanceState
		 * .getString(EXTRA_GUI_CURRENT_TAB)); } else { savedInstanceState =
		 * getIntent().getExtras(); if (savedInstanceState != null &&
		 * !savedInstanceState.isEmpty()) {
		 * mTabHost.setCurrentTabByTag(savedInstanceState
		 * .getString(EXTRA_GUI_CURRENT_TAB)); } else {
		 * mTabHost.setCurrentTabByTag(tab_calculation); } }
		 */
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
		super.onSaveInstanceState(outState);
		/*
		 * outState.putString(EXTRA_GUI_CURRENT_TAB,
		 * mTabHost.getCurrentTabTag());
		 * 
		 * if (mTabCalculationFragment != null) {
		 * mTabCalculationFragment.onSaveInstanceState(outState); }
		 * 
		 * if (mTabHelpFragment != null) {
		 * mTabHelpFragment.onSaveInstanceState(outState); }
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		switch (Utils.getThemeId(this)) {
		case Utils.LIGHT_THEME:
			menu.findItem(R.id.lighttheme).setChecked(true);
			break;
		case Utils.DARK_THEME:
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
			Utils.changeToTheme(this, Utils.LIGHT_THEME);
			return true;
		case R.id.darktheme:
			Utils.changeToTheme(this, Utils.DARK_THEME);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);/*
										 * if (fragment instanceof
										 * TabCalculationFragment) {
										 * mTabCalculationFragment =
										 * (TabCalculationFragment) fragment; }
										 * else if (fragment instanceof
										 * TabHelpFragment) { mTabHelpFragment =
										 * (TabHelpFragment) fragment; }
										 */
	}
}
