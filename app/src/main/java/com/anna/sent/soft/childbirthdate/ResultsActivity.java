package com.anna.sent.soft.childbirthdate;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.anna.sent.soft.childbirthdate.adapters.TabsAdapter;
import com.anna.sent.soft.childbirthdate.base.ChildActivity;
import com.anna.sent.soft.childbirthdate.fragments.ResultEcdFragment;
import com.anna.sent.soft.childbirthdate.fragments.ResultSickListFragment;
import com.anna.sent.soft.childbirthdate.strategy.menu.MenuStrategy;

public class ResultsActivity extends ChildActivity implements
		OnTabChangeListener {
	@Override
	public void setViews(Bundle savedInstanceState) {
		setTitle(R.string.calculation);
		setContentView(R.layout.activity_results);
		super.setViews(savedInstanceState);

		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

		TabsAdapter tabsAdapter = new TabsAdapter(this, tabHost, viewPager);
		tabsAdapter
				.addTab(tabHost.newTabSpec(
						ResultEcdFragment.class.getSimpleName()).setIndicator(
						getString(R.string.ecdAndGestationalAge)),
						ResultEcdFragment.class, null);
		tabsAdapter.addTab(
				tabHost.newTabSpec(
						ResultSickListFragment.class.getSimpleName())
						.setIndicator(getString(R.string.sick_list)),
				ResultSickListFragment.class, null);
		tabsAdapter.setOnTabChangeListener(this);
	}

	@Override
	protected void addStrategies() {
		super.addStrategies();
		addStrategy(new MenuStrategy(this));
	}

	@Override
	public void onTabChanged(String tabId) {
		if (tabId.equals(ResultSickListFragment.class.getSimpleName())) {
			ResultSickListFragment.showSickListInfoDialog(this);
		}
	}
}